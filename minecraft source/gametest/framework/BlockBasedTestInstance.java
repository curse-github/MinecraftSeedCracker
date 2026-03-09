/*    */ package net.minecraft.gametest.framework;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.TestBlock;
/*    */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*    */ 
/*    */ public class BlockBasedTestInstance extends GameTestInstance {
/* 20 */   public static final MapCodec<BlockBasedTestInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TestData.CODEC
/* 21 */         .forGetter(GameTestInstance::info))
/* 22 */       .apply(i, BlockBasedTestInstance::new));
/*    */ 
/*    */   
/* 25 */   public BlockBasedTestInstance(TestData<Holder<TestEnvironmentDefinition>> testData) { super(testData); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run(GameTestHelper helper) {
/* 30 */     BlockPos startPos = findStartBlock(helper);
/* 31 */     TestBlockEntity blockEntity = (TestBlockEntity)helper.getBlockEntity(startPos, TestBlockEntity.class);
/* 32 */     blockEntity.trigger();
/*    */     
/* 34 */     helper.onEachTick(() -> {
/* 35 */           List<BlockPos> acceptBlocks = findTestBlocks(helper, TestBlockMode.ACCEPT);
/* 36 */           if (acceptBlocks.isEmpty()) {
/* 37 */             helper.fail(Component.translatable("test_block.error.missing", new Object[] { TestBlockMode.ACCEPT.getDisplayName() }));
/*    */           }
/*    */ 
/*    */           
/* 41 */           boolean acceptTriggered = acceptBlocks.stream().map(()).anyMatch(TestBlockEntity::hasTriggered);
/* 42 */           if (acceptTriggered) {
/* 43 */             helper.succeed();
/*    */           } else {
/* 45 */             forAllTriggeredTestBlocks(helper, TestBlockMode.FAIL, ());
/* 46 */             forAllTriggeredTestBlocks(helper, TestBlockMode.LOG, TestBlockEntity::trigger);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private void forAllTriggeredTestBlocks(GameTestHelper helper, TestBlockMode mode, Consumer<TestBlockEntity> action) {
/* 52 */     List<BlockPos> failBlocks = findTestBlocks(helper, mode);
/* 53 */     for (BlockPos failBlock : failBlocks) {
/* 54 */       TestBlockEntity blockEntity = (TestBlockEntity)helper.getBlockEntity(failBlock, TestBlockEntity.class);
/* 55 */       if (blockEntity.hasTriggered()) {
/* 56 */         action.accept(blockEntity);
/* 57 */         blockEntity.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private BlockPos findStartBlock(GameTestHelper helper) {
/* 63 */     List<BlockPos> testBlocks = findTestBlocks(helper, TestBlockMode.START);
/* 64 */     if (testBlocks.isEmpty()) {
/* 65 */       helper.fail(Component.translatable("test_block.error.missing", new Object[] { TestBlockMode.START.getDisplayName() }));
/*    */     }
/* 67 */     if (testBlocks.size() != 1) {
/* 68 */       helper.fail(Component.translatable("test_block.error.too_many", new Object[] { TestBlockMode.START.getDisplayName() }));
/*    */     }
/* 70 */     return (BlockPos)testBlocks.getFirst();
/*    */   }
/*    */   
/*    */   private List<BlockPos> findTestBlocks(GameTestHelper helper, TestBlockMode mode) {
/* 74 */     List<BlockPos> blocks = new ArrayList<BlockPos>();
/* 75 */     helper.forEveryBlockInStructure(pos -> {
/* 76 */           BlockState state = helper.getBlockState(pos);
/* 77 */           if (state.is(Blocks.TEST_BLOCK) && state.getValue(TestBlock.MODE) == mode) {
/* 78 */             blocks.add(pos.immutable());
/*    */           }
/*    */         });
/* 81 */     return blocks;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public MapCodec<BlockBasedTestInstance> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   protected MutableComponent typeDescription() { return Component.translatable("test_instance.type.block_based"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\BlockBasedTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
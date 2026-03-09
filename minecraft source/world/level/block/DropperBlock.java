/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DropperBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DropperBlock extends DispenserBlock {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 25 */   public static final MapCodec<DropperBlock> CODEC = simpleCodec(DropperBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 29 */   public MapCodec<DropperBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 32 */   private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();
/*    */ 
/*    */   
/* 35 */   public DropperBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected DispenseItemBehavior getDispenseMethod(Level level, ItemStack itemStack) { return DISPENSE_BEHAVIOUR; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new DropperBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */   
/*    */   protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
/*    */     ItemStack remaining;
/* 50 */     DispenserBlockEntity blockEntity = (DispenserBlockEntity)level.getBlockEntity(pos, BlockEntityType.DROPPER).orElse(null);
/* 51 */     if (blockEntity == null) {
/* 52 */       LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", pos);
/*    */       return;
/*    */     } 
/* 55 */     BlockSource source = new BlockSource(level, pos, state, blockEntity);
/*    */     
/* 57 */     int slot = blockEntity.getRandomSlot(level.random);
/* 58 */     if (slot < 0) {
/* 59 */       level.levelEvent(1001, pos, 0);
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     ItemStack itemStack = blockEntity.getItem(slot);
/* 64 */     if (itemStack.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 68 */     Direction direction = (Direction)level.getBlockState(pos).getValue(FACING);
/* 69 */     Container into = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
/*    */ 
/*    */     
/* 72 */     if (into == null) {
/* 73 */       remaining = DISPENSE_BEHAVIOUR.dispense(source, itemStack);
/*    */     } else {
/* 75 */       remaining = HopperBlockEntity.addItem(blockEntity, into, itemStack.copyWithCount(1), direction.getOpposite());
/*    */       
/* 77 */       if (remaining.isEmpty()) {
/* 78 */         remaining = itemStack.copy();
/* 79 */         remaining.shrink(1);
/*    */       } else {
/*    */         
/* 82 */         remaining = itemStack.copy();
/*    */       } 
/*    */     } 
/*    */     
/* 86 */     blockEntity.setItem(slot, remaining);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DropperBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
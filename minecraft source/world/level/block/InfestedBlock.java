/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class InfestedBlock extends Block {
/* 23 */   public static final MapCodec<InfestedBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/* 24 */         .byNameCodec().fieldOf("host").forGetter(InfestedBlock::getHostBlock), 
/* 25 */         propertiesCodec())
/* 26 */       .apply(i, InfestedBlock::new));
/*    */   
/*    */   private final Block hostBlock;
/*    */   
/* 30 */   public MapCodec<? extends InfestedBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK = Maps.newIdentityHashMap();
/*    */   
/* 37 */   private static final Map<BlockState, BlockState> HOST_TO_INFESTED_STATES = Maps.newIdentityHashMap();
/* 38 */   private static final Map<BlockState, BlockState> INFESTED_TO_HOST_STATES = Maps.newIdentityHashMap();
/*    */   
/*    */   public InfestedBlock(Block hostBlock, BlockBehaviour.Properties properties) {
/* 41 */     super(properties.destroyTime(hostBlock.defaultDestroyTime() / 2.0F).explosionResistance(0.75F));
/* 42 */     this.hostBlock = hostBlock;
/* 43 */     BLOCK_BY_HOST_BLOCK.put(hostBlock, this);
/*    */   }
/*    */ 
/*    */   
/* 47 */   public Block getHostBlock() { return this.hostBlock; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static boolean isCompatibleHostBlock(BlockState blockState) { return BLOCK_BY_HOST_BLOCK.containsKey(blockState.getBlock()); }
/*    */ 
/*    */   
/*    */   private void spawnInfestation(ServerLevel level, BlockPos pos) {
/* 55 */     Silverfish silverfish = (Silverfish)EntityType.SILVERFISH.create(level, EntitySpawnReason.TRIGGERED);
/* 56 */     if (silverfish != null) {
/* 57 */       silverfish.snapTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 58 */       level.addFreshEntity(silverfish);
/*    */       
/* 60 */       silverfish.spawnAnim();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 66 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/*    */     
/* 68 */     if (((Boolean)level.getGameRules().get(GameRules.BLOCK_DROPS)).booleanValue() && 
/* 69 */       !EnchantmentHelper.hasTag(tool, EnchantmentTags.PREVENTS_INFESTED_SPAWNS)) {
/* 70 */       spawnInfestation(level, pos);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 75 */   public static BlockState infestedStateByHost(BlockState hostState) { return getNewStateWithProperties(HOST_TO_INFESTED_STATES, hostState, () -> ((Block)BLOCK_BY_HOST_BLOCK.get(hostState.getBlock())).defaultBlockState()); }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public BlockState hostStateByInfested(BlockState infestedState) { return getNewStateWithProperties(INFESTED_TO_HOST_STATES, infestedState, () -> getHostBlock().defaultBlockState()); }
/*    */ 
/*    */   
/*    */   private static BlockState getNewStateWithProperties(Map<BlockState, BlockState> map, BlockState oldState, Supplier<BlockState> newStateSupplier) {
/* 83 */     return (BlockState)map.computeIfAbsent(oldState, k -> {
/* 84 */           BlockState newState = (BlockState)newStateSupplier.get();
/* 85 */           for (Property property : k.getProperties()) {
/* 86 */             newState = newState.hasProperty(property) ? (BlockState)newState.setValue(property, k.getValue(property)) : newState;
/*    */           }
/* 88 */           return newState;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\InfestedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
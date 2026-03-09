/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CaveVines
/*    */ {
/* 24 */   public static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/* 26 */   public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
/*    */   
/*    */   static InteractionResult use(Entity sourceEntity, BlockState state, Level level, BlockPos pos) {
/* 29 */     if (((Boolean)state.getValue(BERRIES)).booleanValue()) {
/* 30 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 31 */         Block.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.HARVEST_CAVE_VINE, state, level
/*    */ 
/*    */ 
/*    */             
/* 35 */             .getBlockEntity(pos), null, sourceEntity, (serverlvl, itemStack) -> 
/*    */ 
/*    */             
/* 38 */             Block.popResource(serverlvl, pos, itemStack));
/*    */         
/* 40 */         float pitch = Mth.randomBetween(serverLevel.random, 0.8F, 1.2F);
/* 41 */         serverLevel.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, pitch);
/* 42 */         BlockState newState = (BlockState)state.setValue(BERRIES, Boolean.valueOf(false));
/* 43 */         serverLevel.setBlock(pos, newState, 2);
/* 44 */         serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState)); }
/*    */       
/* 46 */       return InteractionResult.SUCCESS;
/*    */     } 
/* 48 */     return InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/* 52 */   static boolean hasGlowBerries(BlockState state) { return (state.hasProperty(BERRIES) && ((Boolean)state.getValue(BERRIES)).booleanValue()); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   static ToIntFunction<BlockState> emission(int lightEmission) { return state -> ((Boolean)state.getValue(BlockStateProperties.BERRIES)).booleanValue() ? lightEmission : 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CaveVines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
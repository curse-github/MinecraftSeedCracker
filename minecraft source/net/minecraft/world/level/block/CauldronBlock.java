/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.cauldron.CauldronInteraction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class CauldronBlock extends AbstractCauldronBlock {
/* 14 */   public static final MapCodec<CauldronBlock> CODEC = simpleCodec(CauldronBlock::new);
/*    */   private static final float RAIN_FILL_CHANCE = 0.05F;
/*    */   private static final float POWDER_SNOW_FILL_CHANCE = 0.1F;
/*    */   
/* 18 */   public MapCodec<CauldronBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public CauldronBlock(BlockBehaviour.Properties properties) { super(properties, CauldronInteraction.EMPTY); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean isFull(BlockState state) { return false; }
/*    */ 
/*    */   
/*    */   protected static boolean shouldHandlePrecipitation(Level level, Biome.Precipitation precipitation) {
/* 34 */     if (precipitation == Biome.Precipitation.RAIN)
/* 35 */       return (level.getRandom().nextFloat() < 0.05F); 
/* 36 */     if (precipitation == Biome.Precipitation.SNOW) {
/* 37 */       return (level.getRandom().nextFloat() < 0.1F);
/*    */     }
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
/* 44 */     if (!shouldHandlePrecipitation(level, precipitation)) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     if (precipitation == Biome.Precipitation.RAIN) {
/* 49 */       level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
/* 50 */       level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
/* 51 */     } else if (precipitation == Biome.Precipitation.SNOW) {
/* 52 */       level.setBlockAndUpdate(pos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
/* 53 */       level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected boolean canReceiveStalactiteDrip(Fluid fluid) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
/* 64 */     if (fluid == Fluids.WATER) {
/* 65 */       BlockState newState = Blocks.WATER_CAULDRON.defaultBlockState();
/* 66 */       level.setBlockAndUpdate(pos, newState);
/* 67 */       level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/* 68 */       level.levelEvent(1047, pos, 0);
/* 69 */     } else if (fluid == Fluids.LAVA) {
/* 70 */       BlockState newState = Blocks.LAVA_CAULDRON.defaultBlockState();
/* 71 */       level.setBlockAndUpdate(pos, newState);
/* 72 */       level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/* 73 */       level.levelEvent(1046, pos, 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.pathfinder.PathType;
/*    */ 
/*    */ 
/*    */ public abstract class WaterAnimal
/*    */   extends PathfinderMob
/*    */ {
/*    */   public static final int AMBIENT_SOUND_INTERVAL = 120;
/*    */   
/*    */   protected WaterAnimal(EntityType<? extends WaterAnimal> type, Level level) {
/* 23 */     super(type, level);
/*    */     
/* 25 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean checkSpawnObstruction(LevelReader level) { return level.isUnobstructed(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public int getAmbientSoundInterval() { return 120; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected int getBaseExperienceReward(ServerLevel level) { return 1 + this.random.nextInt(3); }
/*    */ 
/*    */   
/*    */   protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
/* 44 */     if (isAlive() && !isInWater()) {
/* 45 */       setAirSupply(preTickAirSupply - 1);
/* 46 */       if (shouldTakeDrowningDamage()) {
/* 47 */         setAirSupply(0);
/* 48 */         hurtServer(level, damageSources().drown(), 2.0F);
/*    */       } 
/*    */     } else {
/* 51 */       setAirSupply(300);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void baseTick() {
/* 57 */     int airSupply = getAirSupply();
/* 58 */     super.baseTick();
/* 59 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 60 */       handleAirSupply(serverLevel, airSupply); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public boolean isPushedByFluid() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public boolean canBeLeashed() { return false; }
/*    */ 
/*    */   
/*    */   public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 76 */     int seaLevel = level.getSeaLevel();
/* 77 */     int minSpawnLevel = seaLevel - 13;
/* 78 */     return (pos.getY() >= minSpawnLevel && pos
/* 79 */       .getY() <= seaLevel && level
/* 80 */       .getFluidState(pos.below()).is(FluidTags.WATER) && level
/* 81 */       .getBlockState(pos.above()).is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\WaterAnimal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AgeableMobGroupData
/*     */   implements SpawnGroupData
/*     */ {
/*     */   private int groupSize;
/*     */   private final boolean shouldSpawnBaby;
/*     */   private final float babySpawnChance;
/*     */   
/*     */   public AgeableMobGroupData(boolean shouldSpawnBaby, float babySpawnChance) {
/* 190 */     this.shouldSpawnBaby = shouldSpawnBaby;
/* 191 */     this.babySpawnChance = babySpawnChance;
/*     */   }
/*     */ 
/*     */   
/* 195 */   public AgeableMobGroupData(boolean shouldSpawnBaby) { this(shouldSpawnBaby, 0.05F); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public AgeableMobGroupData(float babySpawnChance) { this(true, babySpawnChance); }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public int getGroupSize() { return this.groupSize; }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public void increaseGroupSizeByOne() { this.groupSize++; }
/*     */ 
/*     */ 
/*     */   
/* 211 */   public boolean isShouldSpawnBaby() { return this.shouldSpawnBaby; }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public float getBabySpawnChance() { return this.babySpawnChance; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\AgeableMob$AgeableMobGroupData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
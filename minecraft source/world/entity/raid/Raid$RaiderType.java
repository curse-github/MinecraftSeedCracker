/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import net.minecraft.world.entity.EntityType;
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
/*     */ static enum RaiderType
/*     */ {
/*     */   private static final RaiderType[] VALUES;
/*  89 */   VINDICATOR(EntityType.VINDICATOR, new int[] { 0, 0, 2, 0, 1, 4, 2, 5 }),
/*  90 */   EVOKER(EntityType.EVOKER, new int[] { 0, 0, 0, 0, 0, 1, 1, 2
/*     */     }),
/*  92 */   PILLAGER(EntityType.PILLAGER, new int[] { 0, 4, 3, 3, 4, 4, 4, 2 }),
/*  93 */   WITCH(EntityType.WITCH, new int[] { 0, 0, 0, 0, 3, 0, 0, 1 }),
/*  94 */   RAVAGER(EntityType.RAVAGER, new int[] { 0, 0, 0, 1, 0, 1, 0, 2 });
/*     */   
/*     */   static  {
/*  97 */     VALUES = values();
/*     */   }
/*     */   
/*     */   private final EntityType<? extends Raider> entityType;
/*     */   
/*     */   RaiderType(EntityType<? extends Raider> entityType, int[] spawnsPerWaveBeforeBonus) {
/* 103 */     this.entityType = entityType;
/* 104 */     this.spawnsPerWaveBeforeBonus = spawnsPerWaveBeforeBonus;
/*     */   }
/*     */   
/*     */   private final int[] spawnsPerWaveBeforeBonus;
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raid$RaiderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
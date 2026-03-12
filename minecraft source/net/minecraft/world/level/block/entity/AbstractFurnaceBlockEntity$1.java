/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.world.inventory.ContainerData;
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
/*     */ class null
/*     */   implements ContainerData
/*     */ {
/*     */   public int get(int dataId) {
/*  86 */     switch (dataId) {
/*     */       case 0:
/*  88 */         return AbstractFurnaceBlockEntity.this.litTimeRemaining;
/*     */       case 1:
/*  90 */         return AbstractFurnaceBlockEntity.this.litTotalTime;
/*     */       case 2:
/*  92 */         return AbstractFurnaceBlockEntity.this.cookingTimer;
/*     */       case 3:
/*  94 */         return AbstractFurnaceBlockEntity.this.cookingTotalTime;
/*     */     } 
/*     */ 
/*     */     
/*  98 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int dataId, int value) {
/* 103 */     switch (dataId) {
/*     */       case 0:
/* 105 */         AbstractFurnaceBlockEntity.this.litTimeRemaining = value;
/*     */         break;
/*     */       case 1:
/* 108 */         AbstractFurnaceBlockEntity.this.litTotalTime = value;
/*     */         break;
/*     */       case 2:
/* 111 */         AbstractFurnaceBlockEntity.this.cookingTimer = value;
/*     */         break;
/*     */       case 3:
/* 114 */         AbstractFurnaceBlockEntity.this.cookingTotalTime = value;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public int getCount() { return 4; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\AbstractFurnaceBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
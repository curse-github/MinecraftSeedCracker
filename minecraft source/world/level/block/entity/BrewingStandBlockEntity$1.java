/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.inventory.ContainerData;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ContainerData
/*    */ {
/*    */   public int get(int dataId) {
/* 65 */     switch (dataId) { case 0: case 1:  }  return 
/*    */ 
/*    */       
/* 68 */       0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(int dataId, int value) {
/* 74 */     switch (dataId) { case 0:
/* 75 */         BrewingStandBlockEntity.this.brewTime = value; break;
/* 76 */       case 1: BrewingStandBlockEntity.this.fuel = value;
/*    */         break; }
/*    */   
/*    */   }
/*    */ 
/*    */   
/* 82 */   public int getCount() { return 2; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BrewingStandBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
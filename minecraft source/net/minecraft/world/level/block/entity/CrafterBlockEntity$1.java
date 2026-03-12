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
/*    */ class null
/*    */   implements ContainerData
/*    */ {
/* 46 */   private final int[] slotStates = new int[9];
/* 47 */   private int triggered = 0;
/*    */   
/*    */   null(CrafterBlockEntity this$0) {}
/*    */   
/* 51 */   public int get(int dataId) { return (dataId == 9) ? this.triggered : this.slotStates[dataId]; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(int dataId, int value) {
/* 56 */     if (dataId == 9) {
/* 57 */       this.triggered = value;
/*    */     } else {
/* 59 */       this.slotStates[dataId] = value;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public int getCount() { return 10; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\CrafterBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
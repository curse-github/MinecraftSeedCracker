/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public abstract class DataSlot {
/*    */   public static DataSlot forContainer(final ContainerData container, final int dataId) {
/*  5 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/*  8 */           return container.get(dataId);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 13 */         public void set(int value) { container.set(dataId, value); }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public static DataSlot shared(final int[] storage, final int index) {
/* 19 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/* 22 */           return storage[index];
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 27 */         public void set(int value) { storage[index] = value; }
/*    */       };
/*    */   }
/*    */   private int prevValue;
/*    */   
/*    */   public static DataSlot standalone() {
/* 33 */     return new DataSlot()
/*    */       {
/*    */         private int value;
/*    */ 
/*    */         
/* 38 */         public int get() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 43 */         public void set(int value) { this.value = value; }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract int get();
/*    */ 
/*    */   
/*    */   public abstract void set(int paramInt);
/*    */ 
/*    */   
/*    */   public boolean checkAndClearUpdateFlag() {
/* 55 */     int currentValue = get();
/* 56 */     boolean result = (currentValue != this.prevValue);
/* 57 */     this.prevValue = currentValue;
/* 58 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\DataSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
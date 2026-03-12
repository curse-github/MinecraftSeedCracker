/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public class SimpleContainerData
/*    */   implements ContainerData {
/*    */   private final int[] ints;
/*    */   
/*  7 */   public SimpleContainerData(int count) { this.ints = new int[count]; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 12 */   public int get(int dataId) { return this.ints[dataId]; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public void set(int dataId, int value) { this.ints[dataId] = value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int getCount() { return this.ints.length; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\SimpleContainerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
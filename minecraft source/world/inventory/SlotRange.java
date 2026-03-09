/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ 
/*    */ public interface SlotRange
/*    */   extends StringRepresentable
/*    */ {
/*    */   IntList slots();
/*    */   
/* 12 */   default int size() { return slots().size(); }
/*    */ 
/*    */   
/*    */   static SlotRange of(final String name, final IntList slots) {
/* 16 */     return new SlotRange()
/*    */       {
/*    */         public IntList slots() {
/* 19 */           return slots;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 24 */         public String getSerializedName() { return name; }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 29 */         public String toString() { return name; }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\SlotRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
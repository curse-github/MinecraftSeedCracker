/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ public interface ScoreAccess
/*    */ {
/*    */   int get();
/*    */   
/*    */   void set(int paramInt);
/*    */   
/*    */   default int add(int count) {
/* 13 */     int newValue = get() + count;
/* 14 */     set(newValue);
/* 15 */     return newValue;
/*    */   }
/*    */ 
/*    */   
/* 19 */   default int increment() { return add(1); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   default void reset() { set(0); }
/*    */   
/*    */   boolean locked();
/*    */   
/*    */   void unlock();
/*    */   
/*    */   void lock();
/*    */   
/*    */   Component display();
/*    */   
/*    */   void display(Component paramComponent);
/*    */   
/*    */   void numberFormatOverride(NumberFormat paramNumberFormat);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\ScoreAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
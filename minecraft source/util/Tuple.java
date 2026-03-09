/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class Tuple<A, B> extends Object {
/*    */   private A a;
/*    */   private B b;
/*    */   
/*    */   public Tuple(A a, B b) {
/*  8 */     this.a = a;
/*  9 */     this.b = b;
/*    */   }
/*    */ 
/*    */   
/* 13 */   public A getA() { return (A)this.a; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public void setA(A a) { this.a = a; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public B getB() { return (B)this.b; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void setB(B b) { this.b = b; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Tuple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
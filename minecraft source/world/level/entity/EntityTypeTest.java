/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ 
/*    */ public interface EntityTypeTest<B, T extends B>
/*    */ {
/*    */   static <B, T extends B> EntityTypeTest<B, T> forClass(final Class<T> cls) {
/*  7 */     return new EntityTypeTest<B, T>()
/*    */       {
/*    */         public T tryCast(B entity)
/*    */         {
/* 11 */           return (T)(cls.isInstance(entity) ? entity : null);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 16 */         public Class<? extends B> getBaseClass() { return cls; }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   static <B, T extends B> EntityTypeTest<B, T> forExactClass(final Class<T> cls) {
/* 22 */     return new EntityTypeTest<B, T>()
/*    */       {
/*    */         public T tryCast(B entity)
/*    */         {
/* 26 */           return (T)(cls.equals(entity.getClass()) ? entity : null);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 31 */         public Class<? extends B> getBaseClass() { return cls; }
/*    */       };
/*    */   }
/*    */   
/*    */   T tryCast(B paramB);
/*    */   
/*    */   Class<? extends B> getBaseClass();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntityTypeTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
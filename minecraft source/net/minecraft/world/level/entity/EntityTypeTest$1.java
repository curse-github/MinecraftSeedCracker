/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements EntityTypeTest<B, T>
/*    */ {
/* 11 */   public T tryCast(B entity) { return (T)(cls.isInstance(entity) ? entity : null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public Class<? extends B> getBaseClass() { return cls; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntityTypeTest$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
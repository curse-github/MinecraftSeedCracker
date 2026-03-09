/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IdMap<T>
/*    */   extends Iterable<T>
/*    */ {
/*    */   public static final int DEFAULT = -1;
/*    */   
/*    */   int getId(T paramT);
/*    */   
/*    */   T byId(int paramInt);
/*    */   
/*    */   default T byIdOrThrow(int id) {
/* 16 */     T result = (T)byId(id);
/* 17 */     if (result == null) {
/* 18 */       throw new IllegalArgumentException("No value with id " + id);
/*    */     }
/* 20 */     return result;
/*    */   }
/*    */   
/*    */   default int getIdOrThrow(T value) {
/* 24 */     int id = getId(value);
/* 25 */     if (id == -1) {
/* 26 */       throw new IllegalArgumentException("Can't find id for '" + String.valueOf(value) + "' in map " + String.valueOf(this));
/*    */     }
/* 28 */     return id;
/*    */   }
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\IdMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
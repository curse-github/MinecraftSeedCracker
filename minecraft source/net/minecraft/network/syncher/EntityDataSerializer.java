/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface EntityDataSerializer<T>
/*    */ {
/*    */   StreamCodec<? super RegistryFriendlyByteBuf, T> codec();
/*    */   
/* 10 */   default EntityDataAccessor<T> createAccessor(int id) { return new EntityDataAccessor(id, this); }
/*    */ 
/*    */   
/*    */   T copy(T paramT);
/*    */   
/*    */   public static interface ForValueType<T>
/*    */     extends EntityDataSerializer<T>
/*    */   {
/* 18 */     default T copy(T value) { return value; }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   static <T> EntityDataSerializer<T> forValueType(StreamCodec<? super RegistryFriendlyByteBuf, T> codec) { return () -> codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\EntityDataSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
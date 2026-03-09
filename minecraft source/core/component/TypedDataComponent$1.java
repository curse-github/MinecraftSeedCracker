/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>>
/*    */ {
/*    */   public TypedDataComponent<?> decode(RegistryFriendlyByteBuf input) {
/* 15 */     DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 16 */     return decodeTyped(input, type);
/*    */   }
/*    */ 
/*    */   
/* 20 */   private static <T> TypedDataComponent<T> decodeTyped(RegistryFriendlyByteBuf input, DataComponentType<T> type) { return new TypedDataComponent(type, type.streamCodec().decode(input)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void encode(RegistryFriendlyByteBuf output, TypedDataComponent<?> value) { encodeCap(output, value); }
/*    */ 
/*    */   
/*    */   private static <T> void encodeCap(RegistryFriendlyByteBuf output, TypedDataComponent<T> component) {
/* 29 */     DataComponentType.STREAM_CODEC.encode(output, component.type());
/* 30 */     component.type().streamCodec().encode(output, component.value());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\TypedDataComponent$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
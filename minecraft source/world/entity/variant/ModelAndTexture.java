/*    */ package net.minecraft.world.entity.variant;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ModelAndTexture<T> extends Record {
/*    */   private final T model;
/*    */   private final ClientAsset.ResourceTexture asset;
/*    */   
/* 11 */   public ModelAndTexture(T model, ClientAsset.ResourceTexture asset) { this.model = model; this.asset = asset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/ModelAndTexture;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>; } public T model() { return (T)this.model; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/ModelAndTexture;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/ModelAndTexture;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 11 */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>; } public ClientAsset.ResourceTexture asset() { return this.asset; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public ModelAndTexture(T model, Identifier assetId) { this(model, new ClientAsset.ResourceTexture(assetId)); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static <T> MapCodec<ModelAndTexture<T>> codec(Codec<T> modelCodec, T defaultModel) { return RecordCodecBuilder.mapCodec(i -> i.group(modelCodec
/* 21 */           .optionalFieldOf("model", defaultModel).forGetter(ModelAndTexture::model), ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC
/* 22 */           .forGetter(ModelAndTexture::asset))
/* 23 */         .apply(i, ModelAndTexture::new)); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static <T> StreamCodec<RegistryFriendlyByteBuf, ModelAndTexture<T>> streamCodec(StreamCodec<? super RegistryFriendlyByteBuf, T> modelCodec) { return StreamCodec.composite(modelCodec, ModelAndTexture::model, ClientAsset.ResourceTexture.STREAM_CODEC, ModelAndTexture::asset, ModelAndTexture::new); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\ModelAndTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
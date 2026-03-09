/*    */ package net.minecraft.world.entity.decoration.painting;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class PaintingVariant extends Record {
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/* 18 */   public PaintingVariant(int width, int height, Identifier assetId, Optional<Component> title, Optional<Component> author) { this.width = width; this.height = height; this.assetId = assetId; this.title = title; this.author = author; } private final Identifier assetId; private final Optional<Component> title; private final Optional<Component> author; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public int height() { return this.height; } public Identifier assetId() { return this.assetId; } public Optional<Component> title() { return this.title; } public Optional<Component> author() { return this.author; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final Codec<PaintingVariant> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
/* 26 */         ExtraCodecs.intRange(1, 16).fieldOf("width").forGetter(PaintingVariant::width), 
/* 27 */         ExtraCodecs.intRange(1, 16).fieldOf("height").forGetter(PaintingVariant::height), Identifier.CODEC
/* 28 */         .fieldOf("asset_id").forGetter(PaintingVariant::assetId), ComponentSerialization.CODEC
/* 29 */         .optionalFieldOf("title").forGetter(PaintingVariant::title), ComponentSerialization.CODEC
/* 30 */         .optionalFieldOf("author").forGetter(PaintingVariant::author))
/* 31 */       .apply(i, PaintingVariant::new));
/*    */   
/* 33 */   public static final StreamCodec<RegistryFriendlyByteBuf, PaintingVariant> DIRECT_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, PaintingVariant::width, ByteBufCodecs.VAR_INT, PaintingVariant::height, Identifier.STREAM_CODEC, PaintingVariant::assetId, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PaintingVariant::title, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PaintingVariant::author, PaintingVariant::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final Codec<Holder<PaintingVariant>> CODEC = RegistryFixedCodec.create(Registries.PAINTING_VARIANT);
/*    */   
/* 44 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PaintingVariant>> STREAM_CODEC = ByteBufCodecs.holder(Registries.PAINTING_VARIANT, DIRECT_STREAM_CODEC);
/*    */ 
/*    */   
/* 47 */   public int area() { return width() * height(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\painting\PaintingVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
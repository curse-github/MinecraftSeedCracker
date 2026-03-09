/*    */ package net.minecraft.world.level.block.entity;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class BannerPattern extends Record {
/*    */   private final Identifier assetId;
/*    */   private final String translationKey;
/*    */   
/* 13 */   public BannerPattern(Identifier assetId, String translationKey) { this.assetId = assetId; this.translationKey = translationKey; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPattern;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPattern; } public Identifier assetId() { return this.assetId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPattern;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPattern; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPattern;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPattern;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public String translationKey() { return this.translationKey; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final Codec<BannerPattern> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Identifier.CODEC
/* 18 */         .fieldOf("asset_id").forGetter(BannerPattern::assetId), Codec.STRING
/* 19 */         .fieldOf("translation_key").forGetter(BannerPattern::translationKey))
/* 20 */       .apply(i, BannerPattern::new));
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, BannerPattern> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, BannerPattern::assetId, ByteBufCodecs.STRING_UTF8, BannerPattern::translationKey, BannerPattern::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final Codec<Holder<BannerPattern>> CODEC = RegistryFileCodec.create(Registries.BANNER_PATTERN, DIRECT_CODEC);
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BannerPattern>> STREAM_CODEC = ByteBufCodecs.holder(Registries.BANNER_PATTERN, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BannerPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
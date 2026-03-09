/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class TrimPattern extends Record {
/*    */   private final Identifier assetId;
/*    */   private final Component description;
/*    */   private final boolean decal;
/*    */   
/* 15 */   public TrimPattern(Identifier assetId, Component description, boolean decal) { this.assetId = assetId; this.description = description; this.decal = decal; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern; } public Identifier assetId() { return this.assetId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public boolean decal() { return this.decal; }
/* 16 */   public static final Codec<TrimPattern> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Identifier.CODEC
/* 17 */         .fieldOf("asset_id").forGetter(TrimPattern::assetId), ComponentSerialization.CODEC
/* 18 */         .fieldOf("description").forGetter(TrimPattern::description), Codec.BOOL
/* 19 */         .fieldOf("decal").orElse(Boolean.valueOf(false)).forGetter(TrimPattern::decal))
/* 20 */       .apply(i, TrimPattern::new));
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, TrimPattern> DIRECT_STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, TrimPattern::assetId, ComponentSerialization.STREAM_CODEC, TrimPattern::description, ByteBufCodecs.BOOL, TrimPattern::decal, TrimPattern::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final Codec<Holder<TrimPattern>> CODEC = RegistryFileCodec.create(Registries.TRIM_PATTERN, DIRECT_CODEC);
/* 30 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<TrimPattern>> STREAM_CODEC = ByteBufCodecs.holder(Registries.TRIM_PATTERN, DIRECT_STREAM_CODEC);
/*    */ 
/*    */   
/* 33 */   public Component copyWithStyle(Holder<TrimMaterial> material) { return this.description.copy().withStyle(((TrimMaterial)material.value()).description().getStyle()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\trim\TrimPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
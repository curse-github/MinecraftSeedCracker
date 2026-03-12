/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class TrimMaterial extends Record {
/*    */   private final MaterialAssetGroup assets;
/*    */   private final Component description;
/*    */   
/* 14 */   public TrimMaterial(MaterialAssetGroup assets, Component description) { this.assets = assets; this.description = description; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/TrimMaterial;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimMaterial; } public MaterialAssetGroup assets() { return this.assets; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/TrimMaterial;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimMaterial; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/TrimMaterial;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/TrimMaterial;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; }
/* 15 */   public static final Codec<TrimMaterial> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(MaterialAssetGroup.MAP_CODEC
/* 16 */         .forGetter(TrimMaterial::assets), ComponentSerialization.CODEC
/* 17 */         .fieldOf("description").forGetter(TrimMaterial::description))
/* 18 */       .apply(i, TrimMaterial::new));
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, TrimMaterial> DIRECT_STREAM_CODEC = StreamCodec.composite(MaterialAssetGroup.STREAM_CODEC, TrimMaterial::assets, ComponentSerialization.STREAM_CODEC, TrimMaterial::description, TrimMaterial::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final Codec<Holder<TrimMaterial>> CODEC = RegistryFileCodec.create(Registries.TRIM_MATERIAL, DIRECT_CODEC);
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<TrimMaterial>> STREAM_CODEC = ByteBufCodecs.holder(Registries.TRIM_MATERIAL, DIRECT_STREAM_CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\trim\TrimMaterial.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
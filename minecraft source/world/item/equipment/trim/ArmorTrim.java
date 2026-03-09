/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.component.TooltipProvider;
/*    */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*    */ 
/*    */ public final class ArmorTrim extends Record implements TooltipProvider {
/*    */   private final Holder<TrimMaterial> material;
/*    */   private final Holder<TrimPattern> pattern;
/*    */   
/* 22 */   public ArmorTrim(Holder<TrimMaterial> material, Holder<TrimPattern> pattern) { this.material = material; this.pattern = pattern; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/ArmorTrim;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 22 */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/ArmorTrim; } public Holder<TrimMaterial> material() { return this.material; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/ArmorTrim;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/ArmorTrim; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/ArmorTrim;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/ArmorTrim;
/* 22 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<TrimPattern> pattern() { return this.pattern; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final Codec<ArmorTrim> CODEC = RecordCodecBuilder.create(i -> i.group(TrimMaterial.CODEC
/* 27 */         .fieldOf("material").forGetter(ArmorTrim::material), TrimPattern.CODEC
/* 28 */         .fieldOf("pattern").forGetter(ArmorTrim::pattern))
/* 29 */       .apply(i, ArmorTrim::new));
/*    */   
/* 31 */   public static final StreamCodec<RegistryFriendlyByteBuf, ArmorTrim> STREAM_CODEC = StreamCodec.composite(TrimMaterial.STREAM_CODEC, ArmorTrim::material, TrimPattern.STREAM_CODEC, ArmorTrim::pattern, ArmorTrim::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   private static final Component UPGRADE_TITLE = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.upgrade"))).withStyle(ChatFormatting.GRAY);
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 41 */     consumer.accept(UPGRADE_TITLE);
/* 42 */     consumer.accept(CommonComponents.space().append(((TrimPattern)this.pattern.value()).copyWithStyle(this.material)));
/* 43 */     consumer.accept(CommonComponents.space().append(((TrimMaterial)this.material.value()).description()));
/*    */   }
/*    */   
/*    */   public Identifier layerAssetId(String layerAssetPrefix, ResourceKey<EquipmentAsset> equipmentAsset) {
/* 47 */     MaterialAssetGroup.AssetInfo materialAsset = ((TrimMaterial)material().value()).assets().assetId(equipmentAsset);
/* 48 */     return ((TrimPattern)pattern().value()).assetId().withPath(patternPath -> layerAssetPrefix + "/" + layerAssetPrefix + "_" + patternPath);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\trim\ArmorTrim.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
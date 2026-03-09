/*    */ package net.minecraft.world.level.block.entity;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.TooltipProvider;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class BannerPatternLayers extends Record implements TooltipProvider {
/*    */   private final List<Layer> layers;
/*    */   
/* 27 */   public BannerPatternLayers(List<Layer> layers) { this.layers = layers; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 27 */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers; } public List<Layer> layers() { return this.layers; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 30 */   public static final BannerPatternLayers EMPTY = new BannerPatternLayers(List.of());
/*    */   
/* 32 */   public static final Codec<BannerPatternLayers> CODEC = Layer.CODEC.listOf().xmap(BannerPatternLayers::new, BannerPatternLayers::layers);
/* 33 */   public static final StreamCodec<RegistryFriendlyByteBuf, BannerPatternLayers> STREAM_CODEC = Layer.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BannerPatternLayers::new, BannerPatternLayers::layers);
/*    */ 
/*    */   
/* 36 */   public BannerPatternLayers removeLast() { return new BannerPatternLayers(List.copyOf(this.layers.subList(0, this.layers.size() - 1))); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 41 */     for (int i = 0; i < Math.min(layers().size(), 6); i++)
/* 42 */       consumer.accept(((Layer)layers().get(i)).description().withStyle(ChatFormatting.GRAY)); 
/*    */   }
/*    */   public static final class Layer extends Record { private final Holder<BannerPattern> pattern; private final DyeColor color;
/*    */     
/* 46 */     public Layer(Holder<BannerPattern> pattern, DyeColor color) { this.pattern = pattern; this.color = color; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Layer;
/* 46 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<BannerPattern> pattern() { return this.pattern; } public DyeColor color() { return this.color; }
/* 47 */     public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(BannerPattern.CODEC
/* 48 */           .fieldOf("pattern").forGetter(Layer::pattern), DyeColor.CODEC
/* 49 */           .fieldOf("color").forGetter(Layer::color))
/* 50 */         .apply(i, Layer::new));
/*    */     
/* 52 */     public static final StreamCodec<RegistryFriendlyByteBuf, Layer> STREAM_CODEC = StreamCodec.composite(BannerPattern.STREAM_CODEC, Layer::pattern, DyeColor.STREAM_CODEC, Layer::color, Layer::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public MutableComponent description() {
/* 59 */       String prefix = ((BannerPattern)this.pattern.value()).translationKey();
/* 60 */       return Component.translatable(prefix + "." + prefix);
/*    */     } }
/*    */ 
/*    */   
/*    */   public static class Builder {
/* 65 */     private final ImmutableList.Builder<BannerPatternLayers.Layer> layers = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     @Deprecated
/*    */     public Builder addIfRegistered(HolderGetter<BannerPattern> patternGetter, ResourceKey<BannerPattern> patternKey, DyeColor color) {
/* 72 */       Optional<Holder.Reference<BannerPattern>> pattern = patternGetter.get(patternKey);
/* 73 */       if (pattern.isEmpty()) {
/* 74 */         BannerPatternLayers.LOGGER.warn("Unable to find banner pattern with id: '{}'", patternKey.identifier());
/* 75 */         return this;
/*    */       } 
/* 77 */       return add((Holder)pattern.get(), color);
/*    */     }
/*    */ 
/*    */     
/* 81 */     public Builder add(Holder<BannerPattern> pattern, DyeColor color) { return add(new BannerPatternLayers.Layer(pattern, color)); }
/*    */ 
/*    */     
/*    */     public Builder add(BannerPatternLayers.Layer layer) {
/* 85 */       this.layers.add(layer);
/* 86 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addAll(BannerPatternLayers layers) {
/* 90 */       this.layers.addAll(layers.layers);
/* 91 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 95 */     public BannerPatternLayers build() { return new BannerPatternLayers(this.layers.build()); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BannerPatternLayers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
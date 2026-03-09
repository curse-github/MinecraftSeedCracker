/*     */ package net.minecraft.world.item.component;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ 
/*     */ public final class FireworkExplosion extends Record implements TooltipProvider {
/*     */   private final Shape shape;
/*     */   private final IntList colors;
/*     */   
/*  25 */   public FireworkExplosion(Shape shape, IntList colors, IntList fadeColors, boolean hasTrail, boolean hasTwinkle) { this.shape = shape; this.colors = colors; this.fadeColors = fadeColors; this.hasTrail = hasTrail; this.hasTwinkle = hasTwinkle; } private final IntList fadeColors; private final boolean hasTrail; private final boolean hasTwinkle; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/FireworkExplosion;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/FireworkExplosion; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/FireworkExplosion;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/FireworkExplosion; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/FireworkExplosion;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/FireworkExplosion;
/*  25 */     //   0	8	1	o	Ljava/lang/Object; } public Shape shape() { return this.shape; } public IntList colors() { return this.colors; } public IntList fadeColors() { return this.fadeColors; } public boolean hasTrail() { return this.hasTrail; } public boolean hasTwinkle() { return this.hasTwinkle; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static final FireworkExplosion DEFAULT = new FireworkExplosion(Shape.SMALL_BALL, IntList.of(), IntList.of(), false, false);
/*     */   
/*  34 */   public static final Codec<IntList> COLOR_LIST_CODEC = Codec.INT.listOf().xmap(IntArrayList::new, java.util.ArrayList::new);
/*     */   
/*  36 */   public static final Codec<FireworkExplosion> CODEC = RecordCodecBuilder.create(i -> i.group(Shape.CODEC
/*  37 */         .fieldOf("shape").forGetter(FireworkExplosion::shape), COLOR_LIST_CODEC
/*  38 */         .optionalFieldOf("colors", IntList.of()).forGetter(FireworkExplosion::colors), COLOR_LIST_CODEC
/*  39 */         .optionalFieldOf("fade_colors", IntList.of()).forGetter(FireworkExplosion::fadeColors), Codec.BOOL
/*  40 */         .optionalFieldOf("has_trail", Boolean.valueOf(false)).forGetter(FireworkExplosion::hasTrail), Codec.BOOL
/*  41 */         .optionalFieldOf("has_twinkle", Boolean.valueOf(false)).forGetter(FireworkExplosion::hasTwinkle))
/*  42 */       .apply(i, FireworkExplosion::new));
/*     */   
/*  44 */   private static final StreamCodec<ByteBuf, IntList> COLOR_LIST_STREAM_CODEC = ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(IntArrayList::new, java.util.ArrayList::new);
/*     */   
/*  46 */   public static final StreamCodec<ByteBuf, FireworkExplosion> STREAM_CODEC = StreamCodec.composite(Shape.STREAM_CODEC, FireworkExplosion::shape, COLOR_LIST_STREAM_CODEC, FireworkExplosion::colors, COLOR_LIST_STREAM_CODEC, FireworkExplosion::fadeColors, ByteBufCodecs.BOOL, FireworkExplosion::hasTrail, ByteBufCodecs.BOOL, FireworkExplosion::hasTwinkle, FireworkExplosion::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final Component CUSTOM_COLOR_NAME = Component.translatable("item.minecraft.firework_star.custom_color");
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/*  59 */     consumer.accept(this.shape.getName().withStyle(ChatFormatting.GRAY));
/*  60 */     addAdditionalTooltip(consumer);
/*     */   }
/*     */   
/*     */   public void addAdditionalTooltip(Consumer<Component> consumer) {
/*  64 */     if (!this.colors.isEmpty()) {
/*  65 */       consumer.accept(appendColors(Component.empty().withStyle(ChatFormatting.GRAY), this.colors));
/*     */     }
/*     */     
/*  68 */     if (!this.fadeColors.isEmpty()) {
/*  69 */       consumer.accept(appendColors(Component.translatable("item.minecraft.firework_star.fade_to").append(CommonComponents.SPACE).withStyle(ChatFormatting.GRAY), this.fadeColors));
/*     */     }
/*     */     
/*  72 */     if (this.hasTrail) {
/*  73 */       consumer.accept(Component.translatable("item.minecraft.firework_star.trail").withStyle(ChatFormatting.GRAY));
/*     */     }
/*     */     
/*  76 */     if (this.hasTwinkle) {
/*  77 */       consumer.accept(Component.translatable("item.minecraft.firework_star.flicker").withStyle(ChatFormatting.GRAY));
/*     */     }
/*     */   }
/*     */   
/*     */   private static Component appendColors(MutableComponent builder, IntList colors) {
/*  82 */     for (int i = 0; i < colors.size(); i++) {
/*  83 */       if (i > 0) {
/*  84 */         builder.append(", ");
/*     */       }
/*  86 */       builder.append(getColorName(colors.getInt(i)));
/*     */     } 
/*     */     
/*  89 */     return builder;
/*     */   }
/*     */   
/*     */   private static Component getColorName(int colorIndex) {
/*  93 */     DyeColor color = DyeColor.byFireworkColor(colorIndex);
/*  94 */     if (color == null) {
/*  95 */       return CUSTOM_COLOR_NAME;
/*     */     }
/*  97 */     return Component.translatable("item.minecraft.firework_star." + color.getName());
/*     */   }
/*     */ 
/*     */   
/* 101 */   public FireworkExplosion withFadeColors(IntList fadeColors) { return new FireworkExplosion(this.shape, this.colors, new IntArrayList(fadeColors), this.hasTrail, this.hasTwinkle); }
/*     */   
/*     */   public enum Shape
/*     */     implements StringRepresentable {
/* 105 */     SMALL_BALL(0, "small_ball"),
/* 106 */     LARGE_BALL(1, "large_ball"),
/* 107 */     STAR(2, "star"),
/* 108 */     CREEPER(3, "creeper"),
/* 109 */     BURST(4, "burst"); private static final IntFunction<Shape> BY_ID; public static final StreamCodec<ByteBuf, Shape> STREAM_CODEC; public static final Codec<Shape> CODEC; private final int id; private final String name;
/*     */     
/*     */     static  {
/* 112 */       BY_ID = ByIdMap.continuous(Shape::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 113 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Shape::getId);
/*     */       
/* 115 */       CODEC = StringRepresentable.fromValues(Shape::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Shape(int id, String name) {
/* 121 */       this.id = id;
/* 122 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/* 126 */     public MutableComponent getName() { return Component.translatable("item.minecraft.firework_star.shape." + this.name); }
/*     */ 
/*     */ 
/*     */     
/* 130 */     public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/* 134 */     public static Shape byId(int id) { return (Shape)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\FireworkExplosion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
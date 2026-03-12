/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ 
/*    */ public final class DyedItemColor extends Record implements TooltipProvider {
/*    */   private final int rgb;
/*    */   
/* 23 */   public DyedItemColor(int rgb) { this.rgb = rgb; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/DyedItemColor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/item/component/DyedItemColor; } public int rgb() { return this.rgb; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/DyedItemColor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DyedItemColor; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/DyedItemColor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/DyedItemColor;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 24 */   public static final Codec<DyedItemColor> CODEC = ExtraCodecs.RGB_COLOR_CODEC.xmap(DyedItemColor::new, DyedItemColor::rgb);
/*    */   
/* 26 */   public static final StreamCodec<ByteBuf, DyedItemColor> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, DyedItemColor::rgb, DyedItemColor::new);
/*    */ 
/*    */   
/*    */   public static final int LEATHER_COLOR = -6265536;
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getOrDefault(ItemStack itemStack, int defaultColor) {
/* 34 */     DyedItemColor color = (DyedItemColor)itemStack.get(DataComponents.DYED_COLOR);
/* 35 */     return (color != null) ? ARGB.opaque(color.rgb()) : defaultColor;
/*    */   }
/*    */   
/*    */   public static ItemStack applyDyes(ItemStack itemStack, List<DyeItem> dyes) {
/* 39 */     if (!itemStack.is(ItemTags.DYEABLE)) {
/* 40 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 43 */     ItemStack result = itemStack.copyWithCount(1);
/*    */     
/* 45 */     int redTotal = 0;
/* 46 */     int greenTotal = 0;
/* 47 */     int blueTotal = 0;
/* 48 */     int intensityTotal = 0;
/* 49 */     int colorCount = 0;
/*    */     
/* 51 */     DyedItemColor currentDye = (DyedItemColor)result.get(DataComponents.DYED_COLOR);
/* 52 */     if (currentDye != null) {
/* 53 */       int red = ARGB.red(currentDye.rgb());
/* 54 */       int green = ARGB.green(currentDye.rgb());
/* 55 */       int blue = ARGB.blue(currentDye.rgb());
/* 56 */       intensityTotal += Math.max(red, Math.max(green, blue));
/* 57 */       redTotal += red;
/* 58 */       greenTotal += green;
/* 59 */       blueTotal += blue;
/* 60 */       colorCount++;
/*    */     } 
/*    */     
/* 63 */     for (DyeItem dye : dyes) {
/* 64 */       int color = dye.getDyeColor().getTextureDiffuseColor();
/* 65 */       int red = ARGB.red(color);
/* 66 */       int green = ARGB.green(color);
/* 67 */       int blue = ARGB.blue(color);
/*    */       
/* 69 */       intensityTotal += Math.max(red, Math.max(green, blue));
/*    */       
/* 71 */       redTotal += red;
/* 72 */       greenTotal += green;
/* 73 */       blueTotal += blue;
/* 74 */       colorCount++;
/*    */     } 
/*    */     
/* 77 */     int red = redTotal / colorCount;
/* 78 */     int green = greenTotal / colorCount;
/* 79 */     int blue = blueTotal / colorCount;
/*    */     
/* 81 */     float averageIntensity = intensityTotal / colorCount;
/* 82 */     float resultIntensity = Math.max(red, Math.max(green, blue));
/*    */     
/* 84 */     red = (int)(red * averageIntensity / resultIntensity);
/* 85 */     green = (int)(green * averageIntensity / resultIntensity);
/* 86 */     blue = (int)(blue * averageIntensity / resultIntensity);
/*    */     
/* 88 */     int rgb = ARGB.color(0, red, green, blue);
/* 89 */     result.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb));
/*    */     
/* 91 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 96 */     if (flag.isAdvanced()) {
/* 97 */       consumer.accept(Component.translatable("item.color", new Object[] { String.format(Locale.ROOT, "#%06X", new Object[] { Integer.valueOf(this.rgb) }) }).withStyle(ChatFormatting.GRAY));
/*    */     } else {
/* 99 */       consumer.accept(Component.translatable("item.dyed").withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\DyedItemColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
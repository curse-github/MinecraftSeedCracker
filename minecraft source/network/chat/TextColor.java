/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TextColor
/*    */ {
/*    */   private static final String CUSTOM_COLOR_PREFIX = "#";
/* 19 */   public static final Codec<TextColor> CODEC = Codec.STRING.comapFlatMap(TextColor::parseColor, TextColor::serialize);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static final Map<ChatFormatting, TextColor> LEGACY_FORMAT_TO_COLOR = (Map)Stream.of(ChatFormatting.values()).filter(ChatFormatting::isColor).collect(ImmutableMap.toImmutableMap(Function.identity(), f -> new TextColor(f.getColor().intValue(), f.getName())));
/* 25 */   private static final Map<String, TextColor> NAMED_COLORS = (Map)LEGACY_FORMAT_TO_COLOR.values().stream().collect(ImmutableMap.toImmutableMap(e -> e.name, Function.identity()));
/*    */   
/*    */   private final int value;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private TextColor(int value, String name) {
/* 32 */     this.value = value & 0xFFFFFF;
/* 33 */     this.name = name;
/*    */   }
/*    */   
/*    */   private TextColor(int value) {
/* 37 */     this.value = value & 0xFFFFFF;
/* 38 */     this.name = null;
/*    */   }
/*    */ 
/*    */   
/* 42 */   public int getValue() { return this.value; }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public String serialize() { return (this.name != null) ? this.name : formatValue(); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   private String formatValue() { return String.format(Locale.ROOT, "#%06X", new Object[] { Integer.valueOf(this.value) }); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 55 */     if (this == o) {
/* 56 */       return true;
/*    */     }
/* 58 */     if (o == null || getClass() != o.getClass()) {
/* 59 */       return false;
/*    */     }
/* 61 */     TextColor other = (TextColor)o;
/* 62 */     return (this.value == other.value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public int hashCode() { return Objects.hash(new Object[] { Integer.valueOf(this.value), this.name }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public String toString() { return serialize(); }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public static TextColor fromLegacyFormat(ChatFormatting format) { return (TextColor)LEGACY_FORMAT_TO_COLOR.get(format); }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public static TextColor fromRgb(int rgb) { return new TextColor(rgb); }
/*    */ 
/*    */   
/*    */   public static DataResult<TextColor> parseColor(String color) {
/* 84 */     if (color.startsWith("#")) {
/*    */       try {
/* 86 */         int value = Integer.parseInt(color.substring(1), 16);
/* 87 */         if (value < 0 || value > 16777215) {
/* 88 */           return DataResult.error(() -> "Color value out of range: " + color);
/*    */         }
/* 90 */         return DataResult.success(fromRgb(value), Lifecycle.stable());
/* 91 */       } catch (NumberFormatException e) {
/* 92 */         return DataResult.error(() -> "Invalid color value: " + color);
/*    */       } 
/*    */     }
/* 95 */     TextColor predefinedColor = (TextColor)NAMED_COLORS.get(color);
/* 96 */     if (predefinedColor == null) {
/* 97 */       return DataResult.error(() -> "Invalid color name: " + color);
/*    */     }
/* 99 */     return DataResult.success(predefinedColor, Lifecycle.stable());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\TextColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
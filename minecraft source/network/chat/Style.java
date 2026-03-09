/*     */ package net.minecraft.network.chat;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Style {
/*     */   public static final int NO_SHADOW = 0;
/*     */   private final TextColor color;
/*     */   private final Integer shadowColor;
/*     */   private final Boolean bold;
/*     */   private final Boolean italic;
/*     */   private final Boolean underlined;
/*  17 */   public static final Style EMPTY = new Style(null, null, null, null, null, null, null, null, null, null, null); private final Boolean strikethrough; private final Boolean obfuscated; private final ClickEvent clickEvent; private final HoverEvent hoverEvent;
/*     */   private final String insertion;
/*     */   private final FontDescription font;
/*     */   
/*  21 */   public static class Serializer { public static final MapCodec<Style> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TextColor.CODEC
/*  22 */           .optionalFieldOf("color").forGetter(()), ExtraCodecs.ARGB_COLOR_CODEC
/*  23 */           .optionalFieldOf("shadow_color").forGetter(()), Codec.BOOL
/*  24 */           .optionalFieldOf("bold").forGetter(()), Codec.BOOL
/*  25 */           .optionalFieldOf("italic").forGetter(()), Codec.BOOL
/*  26 */           .optionalFieldOf("underlined").forGetter(()), Codec.BOOL
/*  27 */           .optionalFieldOf("strikethrough").forGetter(()), Codec.BOOL
/*  28 */           .optionalFieldOf("obfuscated").forGetter(()), ClickEvent.CODEC
/*  29 */           .optionalFieldOf("click_event").forGetter(()), HoverEvent.CODEC
/*  30 */           .optionalFieldOf("hover_event").forGetter(()), Codec.STRING
/*  31 */           .optionalFieldOf("insertion").forGetter(()), FontDescription.CODEC
/*  32 */           .optionalFieldOf("font").forGetter(()))
/*  33 */         .apply(i, Style::create));
/*     */ 
/*     */     
/*  36 */     public static final Codec<Style> CODEC = MAP_CODEC.codec();
/*  37 */     public static final StreamCodec<RegistryFriendlyByteBuf, Style> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Style create(Optional<TextColor> color, Optional<Integer> shadowColor, Optional<Boolean> bold, Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough, Optional<Boolean> obfuscated, Optional<ClickEvent> clickEvent, Optional<HoverEvent> hoverEvent, Optional<String> insertion, Optional<FontDescription> font) {
/*  55 */     Style result = new Style((TextColor)color.orElse(null), (Integer)shadowColor.orElse(null), (Boolean)bold.orElse(null), (Boolean)italic.orElse(null), (Boolean)underlined.orElse(null), (Boolean)strikethrough.orElse(null), (Boolean)obfuscated.orElse(null), (ClickEvent)clickEvent.orElse(null), (HoverEvent)hoverEvent.orElse(null), (String)insertion.orElse(null), (FontDescription)font.orElse(null));
/*  56 */     if (result.equals(EMPTY)) {
/*  57 */       return EMPTY;
/*     */     }
/*  59 */     return result;
/*     */   }
/*     */   
/*     */   private Style(TextColor color, Integer shadowColor, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, FontDescription font) {
/*  63 */     this.color = color;
/*  64 */     this.shadowColor = shadowColor;
/*  65 */     this.bold = bold;
/*  66 */     this.italic = italic;
/*  67 */     this.underlined = underlined;
/*  68 */     this.strikethrough = strikethrough;
/*  69 */     this.obfuscated = obfuscated;
/*  70 */     this.clickEvent = clickEvent;
/*  71 */     this.hoverEvent = hoverEvent;
/*  72 */     this.insertion = insertion;
/*  73 */     this.font = font;
/*     */   }
/*     */ 
/*     */   
/*  77 */   public TextColor getColor() { return this.color; }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public Integer getShadowColor() { return this.shadowColor; }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public boolean isBold() { return (this.bold == Boolean.TRUE); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public boolean isItalic() { return (this.italic == Boolean.TRUE); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean isStrikethrough() { return (this.strikethrough == Boolean.TRUE); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean isUnderlined() { return (this.underlined == Boolean.TRUE); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean isObfuscated() { return (this.obfuscated == Boolean.TRUE); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean isEmpty() { return (this == EMPTY); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public ClickEvent getClickEvent() { return this.clickEvent; }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public HoverEvent getHoverEvent() { return this.hoverEvent; }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public String getInsertion() { return this.insertion; }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public FontDescription getFont() { return (this.font != null) ? this.font : FontDescription.DEFAULT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Style checkEmptyAfterChange(Style newStyle, T previous, T next) {
/* 127 */     if (previous != null && next == null && newStyle.equals(EMPTY)) {
/* 128 */       return EMPTY;
/*     */     }
/* 130 */     return newStyle;
/*     */   }
/*     */   
/*     */   public Style withColor(TextColor color) {
/* 134 */     if (Objects.equals(this.color, color)) {
/* 135 */       return this;
/*     */     }
/* 137 */     return checkEmptyAfterChange(new Style(color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.color, color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public Style withColor(ChatFormatting color) { return withColor((color != null) ? TextColor.fromLegacyFormat(color) : null); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public Style withColor(int color) { return withColor(TextColor.fromRgb(color)); }
/*     */ 
/*     */   
/*     */   public Style withShadowColor(int shadowColor) {
/* 152 */     if (Objects.equals(this.shadowColor, Integer.valueOf(shadowColor))) {
/* 153 */       return this;
/*     */     }
/*     */     
/* 156 */     return checkEmptyAfterChange(new Style(this.color, 
/* 157 */           Integer.valueOf(shadowColor), this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.shadowColor, 
/* 158 */         Integer.valueOf(shadowColor));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public Style withoutShadow() { return withShadowColor(0); }
/*     */ 
/*     */   
/*     */   public Style withBold(Boolean bold) {
/* 167 */     if (Objects.equals(this.bold, bold)) {
/* 168 */       return this;
/*     */     }
/* 170 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.bold, bold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withItalic(Boolean italic) {
/* 177 */     if (Objects.equals(this.italic, italic)) {
/* 178 */       return this;
/*     */     }
/* 180 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.italic, italic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withUnderlined(Boolean underlined) {
/* 187 */     if (Objects.equals(this.underlined, underlined)) {
/* 188 */       return this;
/*     */     }
/* 190 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.underlined, underlined);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withStrikethrough(Boolean strikethrough) {
/* 197 */     if (Objects.equals(this.strikethrough, strikethrough)) {
/* 198 */       return this;
/*     */     }
/* 200 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.strikethrough, strikethrough);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withObfuscated(Boolean obfuscated) {
/* 207 */     if (Objects.equals(this.obfuscated, obfuscated)) {
/* 208 */       return this;
/*     */     }
/* 210 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.obfuscated, obfuscated);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withClickEvent(ClickEvent clickEvent) {
/* 217 */     if (Objects.equals(this.clickEvent, clickEvent)) {
/* 218 */       return this;
/*     */     }
/* 220 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, clickEvent, this.hoverEvent, this.insertion, this.font), this.clickEvent, clickEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withHoverEvent(HoverEvent hoverEvent) {
/* 227 */     if (Objects.equals(this.hoverEvent, hoverEvent)) {
/* 228 */       return this;
/*     */     }
/* 230 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, hoverEvent, this.insertion, this.font), this.hoverEvent, hoverEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withInsertion(String insertion) {
/* 237 */     if (Objects.equals(this.insertion, insertion)) {
/* 238 */       return this;
/*     */     }
/* 240 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, insertion, this.font), this.insertion, insertion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withFont(FontDescription font) {
/* 247 */     if (Objects.equals(this.font, font)) {
/* 248 */       return this;
/*     */     }
/* 250 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, font), this.font, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style applyFormat(ChatFormatting format) {
/* 257 */     TextColor color = this.color;
/* 258 */     Boolean bold = this.bold;
/* 259 */     Boolean italic = this.italic;
/* 260 */     Boolean strikethrough = this.strikethrough;
/* 261 */     Boolean underlined = this.underlined;
/* 262 */     Boolean obfuscated = this.obfuscated;
/*     */     
/* 264 */     switch (format)
/*     */     { case OBFUSCATED:
/* 266 */         obfuscated = Boolean.valueOf(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 286 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case BOLD: bold = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case STRIKETHROUGH: strikethrough = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case UNDERLINE: underlined = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case ITALIC: italic = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case RESET: return EMPTY; }  color = TextColor.fromLegacyFormat(format); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyLegacyFormat(ChatFormatting format) {
/* 290 */     TextColor color = this.color;
/* 291 */     Boolean bold = this.bold;
/* 292 */     Boolean italic = this.italic;
/* 293 */     Boolean strikethrough = this.strikethrough;
/* 294 */     Boolean underlined = this.underlined;
/* 295 */     Boolean obfuscated = this.obfuscated;
/*     */     
/* 297 */     switch (format)
/*     */     { case OBFUSCATED:
/* 299 */         obfuscated = Boolean.valueOf(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 325 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case BOLD: bold = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case STRIKETHROUGH: strikethrough = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case UNDERLINE: underlined = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case ITALIC: italic = Boolean.valueOf(true); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);case RESET: return EMPTY; }  obfuscated = Boolean.valueOf(false); bold = Boolean.valueOf(false); strikethrough = Boolean.valueOf(false); underlined = Boolean.valueOf(false); italic = Boolean.valueOf(false); color = TextColor.fromLegacyFormat(format); return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyFormats(ChatFormatting... formats) {
/* 329 */     TextColor color = this.color;
/* 330 */     Boolean bold = this.bold;
/* 331 */     Boolean italic = this.italic;
/* 332 */     Boolean strikethrough = this.strikethrough;
/* 333 */     Boolean underlined = this.underlined;
/* 334 */     Boolean obfuscated = this.obfuscated;
/*     */     
/* 336 */     for (ChatFormatting format : formats) {
/* 337 */       switch (format) {
/*     */         case OBFUSCATED:
/* 339 */           obfuscated = Boolean.valueOf(true);
/*     */           break;
/*     */         case BOLD:
/* 342 */           bold = Boolean.valueOf(true);
/*     */           break;
/*     */         case STRIKETHROUGH:
/* 345 */           strikethrough = Boolean.valueOf(true);
/*     */           break;
/*     */         case UNDERLINE:
/* 348 */           underlined = Boolean.valueOf(true);
/*     */           break;
/*     */         case ITALIC:
/* 351 */           italic = Boolean.valueOf(true);
/*     */           break;
/*     */         case RESET:
/* 354 */           return EMPTY;
/*     */         default:
/* 356 */           color = TextColor.fromLegacyFormat(format);
/*     */           break;
/*     */       } 
/*     */     } 
/* 360 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyTo(Style other) {
/* 364 */     if (this == EMPTY) {
/* 365 */       return other;
/*     */     }
/*     */     
/* 368 */     if (other == EMPTY) {
/* 369 */       return this;
/*     */     }
/*     */     
/* 372 */     return new Style(
/* 373 */         (this.color != null) ? this.color : other.color, 
/* 374 */         (this.shadowColor != null) ? this.shadowColor : other.shadowColor, 
/* 375 */         (this.bold != null) ? this.bold : other.bold, 
/* 376 */         (this.italic != null) ? this.italic : other.italic, 
/* 377 */         (this.underlined != null) ? this.underlined : other.underlined, 
/* 378 */         (this.strikethrough != null) ? this.strikethrough : other.strikethrough, 
/* 379 */         (this.obfuscated != null) ? this.obfuscated : other.obfuscated, 
/* 380 */         (this.clickEvent != null) ? this.clickEvent : other.clickEvent, 
/* 381 */         (this.hoverEvent != null) ? this.hoverEvent : other.hoverEvent, 
/* 382 */         (this.insertion != null) ? this.insertion : other.insertion, 
/* 383 */         (this.font != null) ? this.font : other.font);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 389 */     final StringBuilder result = new StringBuilder("{");
/*     */     class Collector { private boolean isNotFirst;
/*     */       
/*     */       Collector(Style this$0) {}
/*     */       
/*     */       private void prependSeparator() {
/* 395 */         if (this.isNotFirst) {
/* 396 */           result.append(',');
/*     */         }
/* 398 */         this.isNotFirst = true;
/*     */       }
/*     */       
/*     */       private void addFlagString(String name, Boolean value) {
/* 402 */         if (value != null) {
/* 403 */           prependSeparator();
/* 404 */           if (!value.booleanValue()) {
/* 405 */             result.append('!');
/*     */           }
/* 407 */           result.append(name);
/*     */         } 
/*     */       }
/*     */       
/*     */       private void addValueString(String name, Object value) {
/* 412 */         if (value != null) {
/* 413 */           prependSeparator();
/* 414 */           result.append(name);
/* 415 */           result.append('=');
/* 416 */           result.append(value);
/*     */         } 
/*     */       } }
/*     */     ;
/*     */     
/* 421 */     Collector collector = new Collector(this);
/*     */     
/* 423 */     collector.addValueString("color", this.color);
/*     */     
/* 425 */     collector.addValueString("shadowColor", this.shadowColor);
/*     */     
/* 427 */     collector.addFlagString("bold", this.bold);
/* 428 */     collector.addFlagString("italic", this.italic);
/* 429 */     collector.addFlagString("underlined", this.underlined);
/* 430 */     collector.addFlagString("strikethrough", this.strikethrough);
/* 431 */     collector.addFlagString("obfuscated", this.obfuscated);
/*     */     
/* 433 */     collector.addValueString("clickEvent", this.clickEvent);
/* 434 */     collector.addValueString("hoverEvent", this.hoverEvent);
/* 435 */     collector.addValueString("insertion", this.insertion);
/* 436 */     collector.addValueString("font", this.font);
/*     */     
/* 438 */     result.append("}");
/* 439 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 444 */     if (this == o) {
/* 445 */       return true;
/*     */     }
/* 447 */     if (o instanceof Style) { Style style = (Style)o;
/* 448 */       return (this.bold == style.bold && 
/* 449 */         Objects.equals(getColor(), style.getColor()) && 
/* 450 */         Objects.equals(getShadowColor(), style.getShadowColor()) && this.italic == style.italic && this.obfuscated == style.obfuscated && this.strikethrough == style.strikethrough && this.underlined == style.underlined && 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 455 */         Objects.equals(this.clickEvent, style.clickEvent) && 
/* 456 */         Objects.equals(this.hoverEvent, style.hoverEvent) && 
/* 457 */         Objects.equals(this.insertion, style.insertion) && 
/* 458 */         Objects.equals(this.font, style.font)); }
/*     */ 
/*     */ 
/*     */     
/* 462 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 467 */   public int hashCode() { return Objects.hash(new Object[] { this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\Style.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class StringDecomposer
/*     */ {
/*     */   private static final char REPLACEMENT_CHAR = '�';
/*  11 */   private static final Optional<Object> STOP_ITERATION = Optional.of(Unit.INSTANCE);
/*     */   
/*     */   private static boolean feedChar(Style style, FormattedCharSink output, int pos, char ch) {
/*  14 */     if (Character.isSurrogate(ch)) {
/*  15 */       return output.accept(pos, style, 65533);
/*     */     }
/*  17 */     return output.accept(pos, style, ch);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean iterate(String string, Style style, FormattedCharSink output) {
/*  22 */     int size = string.length();
/*  23 */     for (int i = 0; i < size; i++) {
/*  24 */       char ch = string.charAt(i);
/*  25 */       if (Character.isHighSurrogate(ch)) {
/*  26 */         if (i + 1 >= size) {
/*  27 */           if (!output.accept(i, style, 65533)) {
/*  28 */             return false;
/*     */           }
/*     */           break;
/*     */         } 
/*  32 */         char low = string.charAt(i + 1);
/*  33 */         if (Character.isLowSurrogate(low)) {
/*  34 */           if (!output.accept(i, style, Character.toCodePoint(ch, low))) {
/*  35 */             return false;
/*     */           }
/*  37 */           i++;
/*     */         }
/*  39 */         else if (!output.accept(i, style, 65533)) {
/*  40 */           return false;
/*     */         }
/*     */       
/*  43 */       } else if (!feedChar(style, output, i, ch)) {
/*  44 */         return false;
/*     */       } 
/*     */     } 
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean iterateBackwards(String string, Style style, FormattedCharSink output) {
/*  51 */     int size = string.length();
/*  52 */     for (int i = size - 1; i >= 0; i--) {
/*  53 */       char ch = string.charAt(i);
/*  54 */       if (Character.isLowSurrogate(ch)) {
/*  55 */         if (i - 1 < 0) {
/*  56 */           if (!output.accept(0, style, 65533)) {
/*  57 */             return false;
/*     */           }
/*     */           break;
/*     */         } 
/*  61 */         char high = string.charAt(i - 1);
/*  62 */         if (Character.isHighSurrogate(high)) {
/*  63 */           i--;
/*  64 */           if (!output.accept(i, style, Character.toCodePoint(high, ch))) {
/*  65 */             return false;
/*     */           }
/*     */         }
/*  68 */         else if (!output.accept(i, style, 65533)) {
/*  69 */           return false;
/*     */         }
/*     */       
/*  72 */       } else if (!feedChar(style, output, i, ch)) {
/*  73 */         return false;
/*     */       } 
/*     */     } 
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  80 */   public static boolean iterateFormatted(String string, Style style, FormattedCharSink output) { return iterateFormatted(string, 0, style, output); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static boolean iterateFormatted(String string, int offset, Style style, FormattedCharSink output) { return iterateFormatted(string, offset, style, style, output); }
/*     */ 
/*     */   
/*     */   public static boolean iterateFormatted(String string, int offset, Style currentStyle, Style resetStyle, FormattedCharSink output) {
/*  88 */     int size = string.length();
/*  89 */     Style style = currentStyle;
/*  90 */     for (int i = offset; i < size; i++) {
/*  91 */       char ch = string.charAt(i);
/*  92 */       if (ch == '§') {
/*  93 */         if (i + 1 >= size) {
/*     */           break;
/*     */         }
/*  96 */         char code = string.charAt(i + 1);
/*  97 */         ChatFormatting formatting = ChatFormatting.getByCode(code);
/*  98 */         if (formatting != null) {
/*  99 */           style = (formatting == ChatFormatting.RESET) ? resetStyle : style.applyLegacyFormat(formatting);
/*     */         }
/* 101 */         i++;
/* 102 */       } else if (Character.isHighSurrogate(ch)) {
/* 103 */         if (i + 1 >= size) {
/* 104 */           if (!output.accept(i, style, 65533)) {
/* 105 */             return false;
/*     */           }
/*     */           break;
/*     */         } 
/* 109 */         char low = string.charAt(i + 1);
/* 110 */         if (Character.isLowSurrogate(low)) {
/* 111 */           if (!output.accept(i, style, Character.toCodePoint(ch, low))) {
/* 112 */             return false;
/*     */           }
/* 114 */           i++;
/*     */         }
/* 116 */         else if (!output.accept(i, style, 65533)) {
/* 117 */           return false;
/*     */         }
/*     */       
/* 120 */       } else if (!feedChar(style, output, i, ch)) {
/* 121 */         return false;
/*     */       } 
/*     */     } 
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 128 */   public static boolean iterateFormatted(FormattedText component, Style rootStyle, FormattedCharSink output) { return component.visit((style, contents) -> iterateFormatted(contents, 0, style, output) ? Optional.empty() : STOP_ITERATION, rootStyle).isEmpty(); }
/*     */ 
/*     */   
/*     */   public static String filterBrokenSurrogates(String input) {
/* 132 */     StringBuilder builder = new StringBuilder();
/* 133 */     iterate(input, Style.EMPTY, (position, style, codepoint) -> {
/* 134 */           builder.appendCodePoint(codepoint);
/* 135 */           return true;
/*     */         });
/* 137 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static String getPlainText(FormattedText input) {
/* 141 */     StringBuilder builder = new StringBuilder();
/* 142 */     iterateFormatted(input, Style.EMPTY, (position, style, codepoint) -> {
/* 143 */           builder.appendCodePoint(codepoint);
/* 144 */           return true;
/*     */         });
/* 146 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StringDecomposer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
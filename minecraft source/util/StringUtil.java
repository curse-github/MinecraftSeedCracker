/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtil
/*     */ {
/*  12 */   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
/*  13 */   private static final Pattern LINE_PATTERN = Pattern.compile("\\r\\n|\\v");
/*  14 */   private static final Pattern LINE_END_PATTERN = Pattern.compile("(?:\\r\\n|\\v)$");
/*     */   
/*     */   public static String formatTickDuration(int ticks, float tickrate) {
/*  17 */     int seconds = Mth.floor(ticks / tickrate);
/*  18 */     int minutes = seconds / 60;
/*  19 */     seconds %= 60;
/*  20 */     int hours = minutes / 60;
/*  21 */     minutes %= 60;
/*     */     
/*  23 */     if (hours > 0) {
/*  24 */       return String.format(Locale.ROOT, "%02d:%02d:%02d", new Object[] { Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds) });
/*     */     }
/*  26 */     return String.format(Locale.ROOT, "%02d:%02d", new Object[] { Integer.valueOf(minutes), Integer.valueOf(seconds) });
/*     */   }
/*     */ 
/*     */   
/*  30 */   public static String stripColor(String input) { return STRIP_COLOR_PATTERN.matcher(input).replaceAll(""); }
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static boolean isNullOrEmpty(String s) { return StringUtils.isEmpty(s); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String truncateStringIfNecessary(String s, int maxLength, boolean addDotDotDotIfTruncated) {
/*  43 */     if (s.length() <= maxLength) {
/*  44 */       return s;
/*     */     }
/*     */     
/*  47 */     if (addDotDotDotIfTruncated && maxLength > 3) {
/*  48 */       return s.substring(0, maxLength - 3) + "...";
/*     */     }
/*  50 */     return s.substring(0, maxLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int lineCount(String s) {
/*  55 */     if (s.isEmpty()) {
/*  56 */       return 0;
/*     */     }
/*     */     
/*  59 */     Matcher matcher = LINE_PATTERN.matcher(s);
/*     */     
/*  61 */     int count = 1;
/*  62 */     while (matcher.find()) {
/*  63 */       count++;
/*     */     }
/*  65 */     return count;
/*     */   }
/*     */ 
/*     */   
/*  69 */   public static boolean endsWithNewLine(String s) { return LINE_END_PATTERN.matcher(s).find(); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static String trimChatMessage(String message) { return truncateStringIfNecessary(message, 256, false); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static boolean isAllowedChatCharacter(int ch) { return (ch != 167 && ch >= 32 && ch != 127); }
/*     */ 
/*     */   
/*     */   public static boolean isValidPlayerName(String name) {
/*  81 */     if (name.length() > 16) {
/*  82 */       return false;
/*     */     }
/*  84 */     return name.chars().filter(c -> (c <= 32 || c >= 127)).findAny().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*  88 */   public static String filterText(String input) { return filterText(input, false); }
/*     */ 
/*     */   
/*     */   public static String filterText(String input, boolean multiline) {
/*  92 */     StringBuilder builder = new StringBuilder();
/*     */     
/*  94 */     for (char c : input.toCharArray()) {
/*  95 */       if (isAllowedChatCharacter(c)) {
/*  96 */         builder.append(c);
/*  97 */       } else if (multiline && c == '\n') {
/*  98 */         builder.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static boolean isWhitespace(int codepoint) { return (Character.isWhitespace(codepoint) || Character.isSpaceChar(codepoint)); }
/*     */ 
/*     */   
/*     */   public static boolean isBlank(String string) {
/* 111 */     if (string == null || string.isEmpty()) {
/* 112 */       return true;
/*     */     }
/* 114 */     return string.chars().allMatch(StringUtil::isWhitespace);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StringUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
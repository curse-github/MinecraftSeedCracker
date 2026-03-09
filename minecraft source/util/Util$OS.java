/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.Path;
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
/*     */ public static final enum OS
/*     */ {
/*     */   LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN;
/*     */   private final String telemetryName;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/util/Util$OS
/*     */     //   3: dup
/*     */     //   4: ldc 'LINUX'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'linux'
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   12: putstatic net/minecraft/util/Util$OS.LINUX : Lnet/minecraft/util/Util$OS;
/*     */     //   15: new net/minecraft/util/Util$OS
/*     */     //   18: dup
/*     */     //   19: ldc 'SOLARIS'
/*     */     //   21: iconst_1
/*     */     //   22: ldc 'solaris'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   27: putstatic net/minecraft/util/Util$OS.SOLARIS : Lnet/minecraft/util/Util$OS;
/*     */     //   30: new net/minecraft/util/Util$OS$1
/*     */     //   33: dup
/*     */     //   34: ldc 'WINDOWS'
/*     */     //   36: iconst_2
/*     */     //   37: ldc 'windows'
/*     */     //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   42: putstatic net/minecraft/util/Util$OS.WINDOWS : Lnet/minecraft/util/Util$OS;
/*     */     //   45: new net/minecraft/util/Util$OS$2
/*     */     //   48: dup
/*     */     //   49: ldc 'OSX'
/*     */     //   51: iconst_3
/*     */     //   52: ldc 'mac'
/*     */     //   54: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   57: putstatic net/minecraft/util/Util$OS.OSX : Lnet/minecraft/util/Util$OS;
/*     */     //   60: new net/minecraft/util/Util$OS
/*     */     //   63: dup
/*     */     //   64: ldc 'UNKNOWN'
/*     */     //   66: iconst_4
/*     */     //   67: ldc 'unknown'
/*     */     //   69: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   72: putstatic net/minecraft/util/Util$OS.UNKNOWN : Lnet/minecraft/util/Util$OS;
/*     */     //   75: invokestatic $values : ()[Lnet/minecraft/util/Util$OS;
/*     */     //   78: putstatic net/minecraft/util/Util$OS.$VALUES : [Lnet/minecraft/util/Util$OS;
/*     */     //   81: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #447	-> 0
/*     */     //   #448	-> 15
/*     */     //   #449	-> 30
/*     */     //   #455	-> 45
/*     */     //   #461	-> 60
/*     */     //   #446	-> 75
/*     */   }
/*     */   
/* 466 */   OS(String telemetryName) { this.telemetryName = telemetryName; }
/*     */ 
/*     */   
/*     */   public void openUri(URI uri) {
/*     */     try {
/* 471 */       Process process = Runtime.getRuntime().exec(getOpenUriArguments(uri));
/* 472 */       process.getInputStream().close();
/* 473 */       process.getErrorStream().close();
/* 474 */       process.getOutputStream().close();
/* 475 */     } catch (IOException e) {
/* 476 */       Util.LOGGER.error("Couldn't open location '{}'", uri, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 481 */   public void openFile(File file) { openUri(file.toURI()); }
/*     */ 
/*     */ 
/*     */   
/* 485 */   public void openPath(Path path) { openUri(path.toUri()); }
/*     */ 
/*     */   
/*     */   protected String[] getOpenUriArguments(URI uri) {
/* 489 */     String string = uri.toString();
/* 490 */     if ("file".equals(uri.getScheme()))
/*     */     {
/* 492 */       string = string.replace("file:", "file://");
/*     */     }
/* 494 */     return new String[] { "xdg-open", string };
/*     */   }
/*     */   
/*     */   public void openUri(String uri) {
/*     */     try {
/* 499 */       openUri(new URI(uri));
/*     */     }
/* 501 */     catch (URISyntaxException|IllegalArgumentException e) {
/* 502 */       Util.LOGGER.error("Couldn't open uri '{}'", uri, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 507 */   public String telemetryName() { return this.telemetryName; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Util$OS.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util;
/*    */ 
/*    */ public final class ModCheck extends Record {
/*    */   private final Confidence confidence;
/*    */   private final String description;
/*    */   
/*  7 */   public ModCheck(Confidence confidence, String description) { this.confidence = confidence; this.description = description; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ModCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/util/ModCheck; } public Confidence confidence() { return this.confidence; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ModCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ModCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ModCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ModCheck;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public String description() { return this.description; }
/*    */ 
/*    */ 
/*    */   
/*    */   public enum Confidence
/*    */   {
/* 13 */     PROBABLY_NOT("Probably not.", false),
/* 14 */     VERY_LIKELY("Very likely;", true),
/* 15 */     DEFINITELY("Definitely;", true);
/*    */     
/*    */     private final String description;
/*    */     
/*    */     private final boolean shouldReportAsModified;
/*    */     
/*    */     Confidence(String description, boolean shouldReportAsModified) {
/* 22 */       this.description = description;
/* 23 */       this.shouldReportAsModified = shouldReportAsModified;
/*    */     }
/*    */   }
/*    */   
/*    */   public static ModCheck identify(String expectedBrand, Supplier<String> actualBrand, String component, Class<?> canaryClass) {
/* 28 */     String mod = (String)actualBrand.get();
/* 29 */     if (!expectedBrand.equals(mod)) {
/* 30 */       return new ModCheck(Confidence.DEFINITELY, component + " brand changed to '" + component + "'");
/*    */     }
/* 32 */     if (canaryClass.getSigners() == null) {
/* 33 */       return new ModCheck(Confidence.VERY_LIKELY, component + " jar signature invalidated");
/*    */     }
/* 35 */     return new ModCheck(Confidence.PROBABLY_NOT, component + " jar signature and brand is untouched");
/*    */   }
/*    */ 
/*    */   
/* 39 */   public boolean shouldReportAsModified() { return this.confidence.shouldReportAsModified; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public ModCheck merge(ModCheck other) { return new ModCheck((Confidence)ObjectUtils.max(new Confidence[] { this.confidence, other.confidence }, ), this.description + "; " + this.description); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String fullDescription() { return this.confidence.description + " " + this.confidence.description; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ModCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
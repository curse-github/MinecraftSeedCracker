/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class IntegerLiteral
/*     */   extends Record
/*     */ {
/*     */   private final SnbtGrammar.Sign sign;
/*     */   private final SnbtGrammar.Base base;
/*     */   private final String digits;
/*     */   private final SnbtGrammar.IntegerSuffix suffix;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral; }
/*     */   
/* 342 */   private IntegerLiteral(SnbtGrammar.Sign sign, SnbtGrammar.Base base, String digits, SnbtGrammar.IntegerSuffix suffix) { this.sign = sign; this.base = base; this.digits = digits; this.suffix = suffix; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerLiteral;
/* 342 */     //   0	8	1	o	Ljava/lang/Object; } public SnbtGrammar.Sign sign() { return this.sign; } public SnbtGrammar.Base base() { return this.base; } public String digits() { return this.digits; } public SnbtGrammar.IntegerSuffix suffix() { return this.suffix; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SnbtGrammar.SignedPrefix signedOrDefault() {
/* 349 */     if (this.suffix.signed != null) {
/* 350 */       return this.suffix.signed;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 355 */     switch (this.base.ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  return 
/*     */       
/* 357 */       SnbtGrammar.SignedPrefix.SIGNED;
/*     */   }
/*     */ 
/*     */   
/*     */   private String cleanupDigits(SnbtGrammar.Sign sign) {
/* 362 */     boolean needsUnderscoreRemoval = SnbtGrammar.needsUnderscoreRemoval(this.digits);
/*     */     
/* 364 */     if (sign == SnbtGrammar.Sign.MINUS || needsUnderscoreRemoval) {
/* 365 */       StringBuilder result = new StringBuilder();
/* 366 */       sign.append(result);
/* 367 */       SnbtGrammar.cleanAndAppend(result, this.digits, needsUnderscoreRemoval);
/* 368 */       return result.toString();
/*     */     } 
/* 370 */     return this.digits;
/*     */   }
/*     */ 
/*     */   
/* 374 */   public <T> T create(DynamicOps<T> ops, ParseState<?> state) { return (T)create(ops, (SnbtGrammar.TypeSuffix)Objects.requireNonNullElse(this.suffix.type, SnbtGrammar.TypeSuffix.INT), state); }
/*     */ 
/*     */   
/*     */   public <T> T create(DynamicOps<T> ops, SnbtGrammar.TypeSuffix type, ParseState<?> state) {
/* 378 */     boolean isSigned = (signedOrDefault() == SnbtGrammar.SignedPrefix.SIGNED);
/* 379 */     if (!isSigned && this.sign == SnbtGrammar.Sign.MINUS) {
/*     */       
/* 381 */       state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_NON_NEGATIVE_NUMBER);
/* 382 */       return null;
/*     */     } 
/*     */     
/* 385 */     String fixedDigits = cleanupDigits(this.sign);
/* 386 */     switch (this.base.ordinal()) { default: throw new MatchException(null, null);
/*     */       case 0: 
/*     */       case 1: 
/* 389 */       case 2: break; }  int radix = 16;
/*     */ 
/*     */     
/*     */     try {
/* 393 */       if (isSigned) {
/* 394 */         switch (type.ordinal()) { case 2: 
/*     */           case 3:
/*     */           
/*     */           case 4:
/*     */           
/*     */           case 5:
/* 400 */            }  state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE);
/* 401 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 405 */       switch (type.ordinal()) { case 2: 
/*     */         case 3:
/*     */         
/*     */         case 4:
/*     */         
/*     */         case 5:
/* 411 */          }  state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_EXPECTED_INTEGER_TYPE);
/* 412 */       return null;
/*     */ 
/*     */     
/*     */     }
/* 416 */     catch (NumberFormatException e) {
/*     */ 
/*     */       
/* 419 */       state.errorCollector().store(state.mark(), SnbtGrammar.createNumberParseError(e));
/* 420 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtGrammar$IntegerLiteral.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
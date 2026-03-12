/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
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
/*     */ static final abstract enum ArrayPrefix
/*     */ {
/*     */   BYTE, INT, LONG;
/*     */   private final SnbtGrammar.TypeSuffix defaultType;
/*     */   private final Set<SnbtGrammar.TypeSuffix> additionalTypes;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/nbt/SnbtGrammar$ArrayPrefix$1
/*     */     //   3: dup
/*     */     //   4: ldc 'BYTE'
/*     */     //   6: iconst_0
/*     */     //   7: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.BYTE : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   10: iconst_0
/*     */     //   11: anewarray net/minecraft/nbt/SnbtGrammar$TypeSuffix
/*     */     //   14: invokespecial <init> : (Ljava/lang/String;ILnet/minecraft/nbt/SnbtGrammar$TypeSuffix;[Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;)V
/*     */     //   17: putstatic net/minecraft/nbt/SnbtGrammar$ArrayPrefix.BYTE : Lnet/minecraft/nbt/SnbtGrammar$ArrayPrefix;
/*     */     //   20: new net/minecraft/nbt/SnbtGrammar$ArrayPrefix$2
/*     */     //   23: dup
/*     */     //   24: ldc 'INT'
/*     */     //   26: iconst_1
/*     */     //   27: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.INT : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   30: iconst_2
/*     */     //   31: anewarray net/minecraft/nbt/SnbtGrammar$TypeSuffix
/*     */     //   34: dup
/*     */     //   35: iconst_0
/*     */     //   36: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.BYTE : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   39: aastore
/*     */     //   40: dup
/*     */     //   41: iconst_1
/*     */     //   42: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.SHORT : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   45: aastore
/*     */     //   46: invokespecial <init> : (Ljava/lang/String;ILnet/minecraft/nbt/SnbtGrammar$TypeSuffix;[Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;)V
/*     */     //   49: putstatic net/minecraft/nbt/SnbtGrammar$ArrayPrefix.INT : Lnet/minecraft/nbt/SnbtGrammar$ArrayPrefix;
/*     */     //   52: new net/minecraft/nbt/SnbtGrammar$ArrayPrefix$3
/*     */     //   55: dup
/*     */     //   56: ldc 'LONG'
/*     */     //   58: iconst_2
/*     */     //   59: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.LONG : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   62: iconst_3
/*     */     //   63: anewarray net/minecraft/nbt/SnbtGrammar$TypeSuffix
/*     */     //   66: dup
/*     */     //   67: iconst_0
/*     */     //   68: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.BYTE : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   71: aastore
/*     */     //   72: dup
/*     */     //   73: iconst_1
/*     */     //   74: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.SHORT : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   77: aastore
/*     */     //   78: dup
/*     */     //   79: iconst_2
/*     */     //   80: getstatic net/minecraft/nbt/SnbtGrammar$TypeSuffix.INT : Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;
/*     */     //   83: aastore
/*     */     //   84: invokespecial <init> : (Ljava/lang/String;ILnet/minecraft/nbt/SnbtGrammar$TypeSuffix;[Lnet/minecraft/nbt/SnbtGrammar$TypeSuffix;)V
/*     */     //   87: putstatic net/minecraft/nbt/SnbtGrammar$ArrayPrefix.LONG : Lnet/minecraft/nbt/SnbtGrammar$ArrayPrefix;
/*     */     //   90: invokestatic $values : ()[Lnet/minecraft/nbt/SnbtGrammar$ArrayPrefix;
/*     */     //   93: putstatic net/minecraft/nbt/SnbtGrammar$ArrayPrefix.$VALUES : [Lnet/minecraft/nbt/SnbtGrammar$ArrayPrefix;
/*     */     //   96: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     //   #166	-> 20
/*     */     //   #185	-> 52
/*     */     //   #144	-> 90
/*     */   }
/*     */   
/*     */   ArrayPrefix(SnbtGrammar.TypeSuffix defaultType, TypeSuffix... additionalTypes) {
/* 210 */     this.additionalTypes = Set.of(additionalTypes);
/* 211 */     this.defaultType = defaultType;
/*     */   }
/*     */ 
/*     */   
/* 215 */   public boolean isAllowed(SnbtGrammar.TypeSuffix type) { return (type == this.defaultType || this.additionalTypes.contains(type)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Number buildNumber(SnbtGrammar.IntegerLiteral entry, ParseState<?> state) {
/* 223 */     SnbtGrammar.TypeSuffix actualType = computeType(entry.suffix);
/* 224 */     if (actualType == null) {
/* 225 */       state.errorCollector().store(state.mark(), SnbtGrammar.ERROR_INVALID_ARRAY_ELEMENT_TYPE);
/* 226 */       return null;
/*     */     } 
/* 228 */     return (Number)entry.create(JavaOps.INSTANCE, actualType, state);
/*     */   }
/*     */   
/*     */   private SnbtGrammar.TypeSuffix computeType(SnbtGrammar.IntegerSuffix value) {
/* 232 */     SnbtGrammar.TypeSuffix type = value.type();
/* 233 */     if (type == null) {
/* 234 */       return this.defaultType;
/*     */     }
/* 236 */     if (!isAllowed(type)) {
/* 237 */       return null;
/*     */     }
/* 239 */     return type;
/*     */   }
/*     */   
/*     */   public abstract <T> T create(DynamicOps<T> paramDynamicOps);
/*     */   
/*     */   public abstract <T> T create(DynamicOps<T> paramDynamicOps, List<SnbtGrammar.IntegerLiteral> paramList, ParseState<?> paramParseState);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtGrammar$ArrayPrefix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
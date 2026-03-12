/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.primitives.Longs;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RandomSupport
/*    */ {
/*    */   public static final long GOLDEN_RATIO_64 = -7046029254386353131L;
/*    */   public static final long SILVER_RATIO_64 = 7640891576956012809L;
/* 18 */   private static final HashFunction MD5_128 = Hashing.md5();
/* 19 */   private static final AtomicLong SEED_UNIQUIFIER = new AtomicLong(8682522807148012L);
/*    */   
/*    */   @VisibleForTesting
/*    */   public static long mixStafford13(long z) {
/* 23 */     z = (z ^ z >>> 30) * -4658895280553007687L;
/* 24 */     z = (z ^ z >>> 27) * -7723592293110705685L;
/* 25 */     return z ^ z >>> 31;
/*    */   }
/*    */   
/*    */   public static Seed128bit upgradeSeedTo128bitUnmixed(long legacySeed) {
/* 29 */     long lowBits = legacySeed ^ 0x6A09E667F3BCC909L;
/* 30 */     long highBits = lowBits + -7046029254386353131L;
/* 31 */     return new Seed128bit(lowBits, highBits);
/*    */   }
/*    */ 
/*    */   
/* 35 */   public static Seed128bit upgradeSeedTo128bit(long legacySeed) { return upgradeSeedTo128bitUnmixed(legacySeed).mixed(); }
/*    */ 
/*    */   
/*    */   public static Seed128bit seedFromHashOf(String input) {
/* 39 */     byte[] hashCode = MD5_128.hashString(input, StandardCharsets.UTF_8).asBytes();
/*    */     
/* 41 */     long hashLo = Longs.fromBytes(hashCode[0], hashCode[1], hashCode[2], hashCode[3], hashCode[4], hashCode[5], hashCode[6], hashCode[7]);
/* 42 */     long hashHi = Longs.fromBytes(hashCode[8], hashCode[9], hashCode[10], hashCode[11], hashCode[12], hashCode[13], hashCode[14], hashCode[15]);
/* 43 */     return new Seed128bit(hashLo, hashHi);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static long generateUniqueSeed() { return SEED_UNIQUIFIER.updateAndGet(current -> current * 1181783497276652981L) ^ System.nanoTime(); }
/*    */   public static final class Seed128bit extends Record { private final long seedLo; private final long seedHi;
/*    */     
/* 53 */     public Seed128bit(long seedLo, long seedHi) { this.seedLo = seedLo; this.seedHi = seedHi; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 53 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit; } public long seedLo() { return this.seedLo; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;
/* 53 */       //   0	8	1	o	Ljava/lang/Object; } public long seedHi() { return this.seedHi; }
/*    */     
/* 55 */     public Seed128bit xor(long lo, long hi) { return new Seed128bit(this.seedLo ^ lo, this.seedHi ^ hi); }
/*    */ 
/*    */ 
/*    */     
/* 59 */     public Seed128bit xor(Seed128bit other) { return xor(other.seedLo, other.seedHi); }
/*    */ 
/*    */ 
/*    */     
/* 63 */     public Seed128bit mixed() { return new Seed128bit(RandomSupport.mixStafford13(this.seedLo), RandomSupport.mixStafford13(this.seedHi)); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\RandomSupport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.stream.LongStream;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Xoroshiro128PlusPlus
/*    */ {
/*    */   private long seedLo;
/*    */   private long seedHi;
/* 16 */   public static final Codec<Xoroshiro128PlusPlus> CODEC = Codec.LONG_STREAM.comapFlatMap(seed -> 
/* 17 */       Util.fixedSize(seed, 2).map(()), r -> 
/* 18 */       LongStream.of(new long[] { r.seedLo, r.seedHi }));
/*    */ 
/*    */ 
/*    */   
/* 22 */   public Xoroshiro128PlusPlus(RandomSupport.Seed128bit seed) { this(seed.seedLo(), seed.seedHi()); }
/*    */ 
/*    */   
/*    */   public Xoroshiro128PlusPlus(long seedLo, long seedHi) {
/* 26 */     this.seedLo = seedLo;
/* 27 */     this.seedHi = seedHi;
/* 28 */     if ((this.seedLo | this.seedHi) == 0L) {
/* 29 */       this.seedLo = -7046029254386353131L;
/* 30 */       this.seedHi = 7640891576956012809L;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public long nextLong() {
/* 36 */     long s0 = this.seedLo;
/* 37 */     long s1 = this.seedHi;
/* 38 */     long result = Long.rotateLeft(s0 + s1, 17) + s0;
/*    */     
/* 40 */     s1 ^= s0;
/* 41 */     this.seedLo = Long.rotateLeft(s0, 49) ^ s1 ^ s1 << 21;
/* 42 */     this.seedHi = Long.rotateLeft(s1, 28);
/*    */     
/* 44 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Xoroshiro128PlusPlus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
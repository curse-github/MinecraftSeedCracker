/*    */ package net.minecraft.world;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*    */ 
/*    */ public class RandomSequence {
/* 13 */   public static final Codec<RandomSequence> CODEC = RecordCodecBuilder.create(i -> i.group(XoroshiroRandomSource.CODEC
/* 14 */         .fieldOf("source").forGetter(()))
/* 15 */       .apply(i, RandomSequence::new));
/*    */   
/*    */   private final XoroshiroRandomSource source;
/*    */ 
/*    */   
/* 20 */   public RandomSequence(XoroshiroRandomSource source) { this.source = source; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public RandomSequence(long seed, Identifier key) { this(createSequence(seed, Optional.of(key))); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public RandomSequence(long seed, Optional<Identifier> key) { this(createSequence(seed, key)); }
/*    */ 
/*    */ 
/*    */   
/*    */   private static XoroshiroRandomSource createSequence(long seed, Optional<Identifier> key) {
/* 33 */     RandomSupport.Seed128bit seed128bit = RandomSupport.upgradeSeedTo128bitUnmixed(seed);
/* 34 */     if (key.isPresent()) {
/* 35 */       seed128bit = seed128bit.xor(seedForKey((Identifier)key.get()));
/*    */     }
/* 37 */     return new XoroshiroRandomSource(seed128bit.mixed());
/*    */   }
/*    */ 
/*    */   
/* 41 */   public static RandomSupport.Seed128bit seedForKey(Identifier key) { return RandomSupport.seedFromHashOf(key.toString()); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public RandomSource random() { return this.source; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\RandomSequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
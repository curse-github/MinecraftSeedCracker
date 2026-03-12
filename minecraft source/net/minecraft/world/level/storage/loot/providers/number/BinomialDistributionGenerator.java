/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class BinomialDistributionGenerator extends Record implements NumberProvider {
/*    */   private final NumberProvider n;
/*    */   private final NumberProvider p;
/*    */   
/* 12 */   public BinomialDistributionGenerator(NumberProvider n, NumberProvider p) { this.n = n; this.p = p; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator; } public NumberProvider n() { return this.n; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/BinomialDistributionGenerator;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public NumberProvider p() { return this.p; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<BinomialDistributionGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(NumberProviders.CODEC
/* 17 */         .fieldOf("n").forGetter(BinomialDistributionGenerator::n), NumberProviders.CODEC
/* 18 */         .fieldOf("p").forGetter(BinomialDistributionGenerator::p))
/* 19 */       .apply(i, BinomialDistributionGenerator::new));
/*    */ 
/*    */ 
/*    */   
/* 23 */   public LootNumberProviderType getType() { return NumberProviders.BINOMIAL; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getInt(LootContext context) {
/* 30 */     int n = this.n.getInt(context);
/* 31 */     float p = this.p.getFloat(context);
/* 32 */     RandomSource random = context.getRandom();
/* 33 */     int result = 0;
/* 34 */     for (int i = 0; i < n; i++) {
/* 35 */       if (random.nextFloat() < p) {
/* 36 */         result++;
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public float getFloat(LootContext context) { return getInt(context); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public static BinomialDistributionGenerator binomial(int n, float p) { return new BinomialDistributionGenerator(ConstantValue.exactly(n), ConstantValue.exactly(p)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Set<ContextKey<?>> getReferencedContextParams() { return Sets.union(this.n.getReferencedContextParams(), this.p.getReferencedContextParams()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\BinomialDistributionGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
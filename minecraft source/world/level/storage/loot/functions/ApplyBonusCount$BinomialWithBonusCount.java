/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class BinomialWithBonusCount
/*    */   extends Record
/*    */   implements ApplyBonusCount.Formula
/*    */ {
/*    */   private final int extraRounds;
/*    */   private final float probability;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 36 */   private BinomialWithBonusCount(int extraRounds, float probability) { this.extraRounds = extraRounds; this.probability = probability; } public int extraRounds() { return this.extraRounds; } public float probability() { return this.probability; }
/* 37 */   private static final Codec<BinomialWithBonusCount> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 38 */         .fieldOf("extra").forGetter(BinomialWithBonusCount::extraRounds), Codec.FLOAT
/* 39 */         .fieldOf("probability").forGetter(BinomialWithBonusCount::probability))
/* 40 */       .apply(i, BinomialWithBonusCount::new));
/*    */   
/* 42 */   public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("binomial_with_bonus_count"), CODEC);
/*    */ 
/*    */   
/*    */   public int calculateNewCount(RandomSource random, int count, int level) {
/* 46 */     for (int i = 0; i < level + this.extraRounds; i++) {
/* 47 */       if (random.nextFloat() < this.probability) {
/* 48 */         count++;
/*    */       }
/*    */     } 
/* 51 */     return count;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public ApplyBonusCount.FormulaType getType() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$BinomialWithBonusCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
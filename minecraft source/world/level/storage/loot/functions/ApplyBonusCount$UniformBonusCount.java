/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
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
/*    */ final class UniformBonusCount
/*    */   extends Record
/*    */   implements ApplyBonusCount.Formula
/*    */ {
/*    */   private final int bonusMultiplier;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 60 */   private UniformBonusCount(int bonusMultiplier) { this.bonusMultiplier = bonusMultiplier; } public int bonusMultiplier() { return this.bonusMultiplier; }
/* 61 */   public static final Codec<UniformBonusCount> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 62 */         .fieldOf("bonusMultiplier").forGetter(UniformBonusCount::bonusMultiplier))
/* 63 */       .apply(i, UniformBonusCount::new));
/*    */   
/* 65 */   public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("uniform_bonus_count"), CODEC);
/*    */ 
/*    */ 
/*    */   
/* 69 */   public int calculateNewCount(RandomSource random, int count, int level) { return count + random.nextInt(this.bonusMultiplier * level + 1); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public ApplyBonusCount.FormulaType getType() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$UniformBonusCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
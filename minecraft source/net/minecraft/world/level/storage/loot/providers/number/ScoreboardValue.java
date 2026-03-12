/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProviders;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ScoreboardValue extends Record implements NumberProvider {
/*    */   private final ScoreboardNameProvider target;
/*    */   private final String score;
/*    */   private final float scale;
/*    */   
/* 18 */   public ScoreboardValue(ScoreboardNameProvider target, String score, float scale) { this.target = target; this.score = score; this.scale = scale; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue; } public ScoreboardNameProvider target() { return this.target; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public String score() { return this.score; } public float scale() { return this.scale; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<ScoreboardValue> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ScoreboardNameProviders.CODEC
/* 24 */         .fieldOf("target").forGetter(ScoreboardValue::target), Codec.STRING
/* 25 */         .fieldOf("score").forGetter(ScoreboardValue::score), Codec.FLOAT
/* 26 */         .fieldOf("scale").orElse(Float.valueOf(1.0F)).forGetter(ScoreboardValue::scale))
/* 27 */       .apply(i, ScoreboardValue::new));
/*    */ 
/*    */ 
/*    */   
/* 31 */   public LootNumberProviderType getType() { return NumberProviders.SCORE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.target.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public static ScoreboardValue fromScoreboard(LootContext.EntityTarget entityTarget, String score) { return fromScoreboard(entityTarget, score, 1.0F); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public static ScoreboardValue fromScoreboard(LootContext.EntityTarget entityTarget, String score, float scale) { return new ScoreboardValue(ContextScoreboardNameProvider.forTarget(entityTarget), score, scale); }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 49 */     ScoreHolder scoreHolder = this.target.getScoreHolder(context);
/* 50 */     if (scoreHolder == null) {
/* 51 */       return 0.0F;
/*    */     }
/*    */     
/* 54 */     ServerScoreboard serverScoreboard = context.getLevel().getScoreboard();
/* 55 */     Objective objective = serverScoreboard.getObjective(this.score);
/* 56 */     if (objective == null) {
/* 57 */       return 0.0F;
/*    */     }
/*    */     
/* 60 */     ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(scoreHolder, objective);
/* 61 */     if (scoreInfo == null) {
/* 62 */       return 0.0F;
/*    */     }
/* 64 */     return scoreInfo.value() * this.scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\ScoreboardValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
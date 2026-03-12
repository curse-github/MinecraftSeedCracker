/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.Scoreboard;
/*    */ 
/*    */ public final class EntityHasScoreCondition extends Record implements LootItemCondition {
/*    */   private final Map<String, IntRange> scores;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/* 20 */   public EntityHasScoreCondition(Map<String, IntRange> scores, LootContext.EntityTarget entityTarget) { this.scores = scores; this.entityTarget = entityTarget; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition; } public Map<String, IntRange> scores() { return this.scores; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public LootContext.EntityTarget entityTarget() { return this.entityTarget; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final MapCodec<EntityHasScoreCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 25 */         Codec.unboundedMap(Codec.STRING, IntRange.CODEC).fieldOf("scores").forGetter(EntityHasScoreCondition::scores), LootContext.EntityTarget.CODEC
/* 26 */         .fieldOf("entity").forGetter(EntityHasScoreCondition::entityTarget))
/* 27 */       .apply(i, EntityHasScoreCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 31 */   public LootItemConditionType getType() { return LootItemConditions.ENTITY_SCORES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Set<ContextKey<?>> getReferencedContextParams() { return (Set)Stream.concat(Stream.of(this.entityTarget.contextParam()), this.scores.values().stream().flatMap(r -> r.getReferencedContextParams().stream())).collect(ImmutableSet.toImmutableSet()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 41 */     Entity entity = (Entity)context.getOptionalParameter(this.entityTarget.contextParam());
/*    */     
/* 43 */     if (entity == null) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     ServerScoreboard serverScoreboard = context.getLevel().getScoreboard();
/* 48 */     for (Map.Entry<String, IntRange> entry : this.scores.entrySet()) {
/* 49 */       if (!hasScore(context, entity, serverScoreboard, (String)entry.getKey(), (IntRange)entry.getValue())) {
/* 50 */         return false;
/*    */       }
/*    */     } 
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   protected boolean hasScore(LootContext context, Entity entity, Scoreboard scoreboard, String objectiveName, IntRange range) {
/* 57 */     Objective objective = scoreboard.getObjective(objectiveName);
/* 58 */     if (objective == null) {
/* 59 */       return false;
/*    */     }
/* 61 */     ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo(entity, objective);
/* 62 */     if (scoreInfo == null) {
/* 63 */       return false;
/*    */     }
/* 65 */     return range.test(context, scoreInfo.value());
/*    */   }
/*    */   public static class Builder implements LootItemCondition.Builder { private final ImmutableMap.Builder<String, IntRange> scores; private final LootContext.EntityTarget entityTarget;
/*    */     public Builder(LootContext.EntityTarget entityTarget) {
/* 69 */       this.scores = ImmutableMap.builder();
/*    */ 
/*    */ 
/*    */       
/* 73 */       this.entityTarget = entityTarget;
/*    */     }
/*    */     
/*    */     public Builder withScore(String score, IntRange bounds) {
/* 77 */       this.scores.put(score, bounds);
/* 78 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 83 */     public LootItemCondition build() { return new EntityHasScoreCondition(this.scores.build(), this.entityTarget); } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   public static Builder hasScores(LootContext.EntityTarget target) { return new Builder(target); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\EntityHasScoreCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
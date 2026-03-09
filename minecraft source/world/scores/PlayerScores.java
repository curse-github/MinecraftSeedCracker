/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ class PlayerScores
/*    */ {
/* 14 */   private final Reference2ObjectOpenHashMap<Objective, Score> scores = new Reference2ObjectOpenHashMap(16, 0.5F);
/*    */ 
/*    */   
/* 17 */   public Score get(Objective objective) { return (Score)this.scores.get(objective); }
/*    */ 
/*    */   
/*    */   public Score getOrCreate(Objective objective, Consumer<Score> newResultCallback) {
/* 21 */     return (Score)this.scores.computeIfAbsent(objective, obj -> {
/* 22 */           Score newScore = new Score();
/* 23 */           newResultCallback.accept(newScore);
/* 24 */           return newScore;
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 29 */   public boolean remove(Objective objective) { return (this.scores.remove(objective) != null); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public boolean hasScores() { return !this.scores.isEmpty(); }
/*    */ 
/*    */   
/*    */   public Object2IntMap<Objective> listScores() {
/* 37 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 38 */     this.scores.forEach((objective, score) -> result.put(objective, score.value()));
/* 39 */     return object2IntOpenHashMap;
/*    */   }
/*    */ 
/*    */   
/* 43 */   void setScore(Objective objective, Score score) { this.scores.put(objective, score); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   Map<Objective, Score> listRawScores() { return Collections.unmodifiableMap(this.scores); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\PlayerScores.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
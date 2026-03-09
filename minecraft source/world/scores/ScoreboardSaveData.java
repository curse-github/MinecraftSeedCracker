/*    */ package net.minecraft.world.scores;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class ScoreboardSaveData extends SavedData {
/* 13 */   public static final SavedDataType<ScoreboardSaveData> TYPE = new SavedDataType("scoreboard", ScoreboardSaveData::new, Packed.CODEC
/*    */ 
/*    */       
/* 16 */       .xmap(ScoreboardSaveData::new, ScoreboardSaveData::getData), DataFixTypes.SAVED_DATA_SCOREBOARD);
/*    */ 
/*    */   
/*    */   private Packed data;
/*    */ 
/*    */ 
/*    */   
/* 23 */   private ScoreboardSaveData() { this(Packed.EMPTY); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public ScoreboardSaveData(Packed data) { this.data = data; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Packed getData() { return this.data; }
/*    */ 
/*    */   
/*    */   public void setData(Packed data) {
/* 35 */     if (!data.equals(this.data)) {
/* 36 */       this.data = data;
/* 37 */       setDirty();
/*    */     } 
/*    */   }
/*    */   public static final class Packed extends Record { private final List<Objective.Packed> objectives; private final List<Scoreboard.PackedScore> scores; private final Map<DisplaySlot, String> displaySlots; private final List<PlayerTeam.Packed> teams;
/* 41 */     public Packed(List<Objective.Packed> objectives, List<Scoreboard.PackedScore> scores, Map<DisplaySlot, String> displaySlots, List<PlayerTeam.Packed> teams) { this.objectives = objectives; this.scores = scores; this.displaySlots = displaySlots; this.teams = teams; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #41	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 41 */       //   0	7	0	this	Lnet/minecraft/world/scores/ScoreboardSaveData$Packed; } public List<Objective.Packed> objectives() { return this.objectives; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #41	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/scores/ScoreboardSaveData$Packed; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #41	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/scores/ScoreboardSaveData$Packed;
/* 41 */       //   0	8	1	o	Ljava/lang/Object; } public List<Scoreboard.PackedScore> scores() { return this.scores; } public Map<DisplaySlot, String> displaySlots() { return this.displaySlots; } public List<PlayerTeam.Packed> teams() { return this.teams; }
/* 42 */     public static final Packed EMPTY = new Packed(List.of(), List.of(), Map.of(), List.of());
/*    */     
/* 44 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Objective.Packed.CODEC
/* 45 */           .listOf().optionalFieldOf("Objectives", List.of()).forGetter(Packed::objectives), Scoreboard.PackedScore.CODEC
/* 46 */           .listOf().optionalFieldOf("PlayerScores", List.of()).forGetter(Packed::scores), 
/* 47 */           Codec.unboundedMap(DisplaySlot.CODEC, Codec.STRING).optionalFieldOf("DisplaySlots", Map.of()).forGetter(Packed::displaySlots), PlayerTeam.Packed.CODEC
/* 48 */           .listOf().optionalFieldOf("Teams", List.of()).forGetter(Packed::teams))
/* 49 */         .apply(i, Packed::new)); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\ScoreboardSaveData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
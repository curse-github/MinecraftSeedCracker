/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class FixedScoreboardNameProvider extends Record implements ScoreboardNameProvider {
/*    */   private final String name;
/*    */   
/* 12 */   public FixedScoreboardNameProvider(String name) { this.name = name; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final MapCodec<FixedScoreboardNameProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 14 */         .fieldOf("name").forGetter(FixedScoreboardNameProvider::name))
/* 15 */       .apply(i, FixedScoreboardNameProvider::new));
/*    */ 
/*    */   
/* 18 */   public static ScoreboardNameProvider forName(String name) { return new FixedScoreboardNameProvider(name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public LootScoreProviderType getType() { return ScoreboardNameProviders.FIXED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public ScoreHolder getScoreHolder(LootContext context) { return ScoreHolder.forNameOnly(this.name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\score\FixedScoreboardNameProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
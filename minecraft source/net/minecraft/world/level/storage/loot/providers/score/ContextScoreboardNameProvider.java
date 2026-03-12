/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ContextScoreboardNameProvider extends Record implements ScoreboardNameProvider {
/*    */   private final LootContext.EntityTarget target;
/*    */   
/* 13 */   public ContextScoreboardNameProvider(LootContext.EntityTarget target) { this.target = target; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider; } public LootContext.EntityTarget target() { return this.target; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 14 */   public static final MapCodec<ContextScoreboardNameProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LootContext.EntityTarget.CODEC
/* 15 */         .fieldOf("target").forGetter(ContextScoreboardNameProvider::target))
/* 16 */       .apply(i, ContextScoreboardNameProvider::new));
/*    */   
/* 18 */   public static final Codec<ContextScoreboardNameProvider> INLINE_CODEC = LootContext.EntityTarget.CODEC.xmap(ContextScoreboardNameProvider::new, ContextScoreboardNameProvider::target);
/*    */ 
/*    */   
/* 21 */   public static ScoreboardNameProvider forTarget(LootContext.EntityTarget target) { return new ContextScoreboardNameProvider(target); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public LootScoreProviderType getType() { return ScoreboardNameProviders.CONTEXT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public ScoreHolder getScoreHolder(LootContext context) { return (ScoreHolder)context.getOptionalParameter(this.target.contextParam()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(this.target.contextParam()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\score\ContextScoreboardNameProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
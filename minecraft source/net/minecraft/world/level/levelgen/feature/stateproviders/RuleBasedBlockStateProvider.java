/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public final class RuleBasedBlockStateProvider extends Record {
/*    */   private final BlockStateProvider fallback;
/*    */   private final List<Rule> rules;
/*    */   
/* 14 */   public RuleBasedBlockStateProvider(BlockStateProvider fallback, List<Rule> rules) { this.fallback = fallback; this.rules = rules; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider; } public BlockStateProvider fallback() { return this.fallback; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public List<Rule> rules() { return this.rules; }
/* 15 */   public static final Codec<RuleBasedBlockStateProvider> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/* 16 */         .fieldOf("fallback").forGetter(RuleBasedBlockStateProvider::fallback), Rule.CODEC
/* 17 */         .listOf().fieldOf("rules").forGetter(RuleBasedBlockStateProvider::rules))
/* 18 */       .apply(i, RuleBasedBlockStateProvider::new));
/*    */ 
/*    */   
/* 21 */   public static RuleBasedBlockStateProvider simple(BlockStateProvider provider) { return new RuleBasedBlockStateProvider(provider, List.of()); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static RuleBasedBlockStateProvider simple(Block block) { return simple(BlockStateProvider.simple(block)); }
/*    */ 
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 29 */     for (Rule rule : this.rules) {
/* 30 */       if (rule.ifTrue().test(level, pos)) {
/* 31 */         return rule.then().getState(random, pos);
/*    */       }
/*    */     } 
/* 34 */     return this.fallback.getState(random, pos);
/*    */   }
/*    */   public static final class Rule extends Record { private final BlockPredicate ifTrue; private final BlockStateProvider then;
/* 37 */     public Rule(BlockPredicate ifTrue, BlockStateProvider then) { this.ifTrue = ifTrue; this.then = then; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;
/* 37 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPredicate ifTrue() { return this.ifTrue; } public BlockStateProvider then() { return this.then; }
/* 38 */     public static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPredicate.CODEC
/* 39 */           .fieldOf("if_true").forGetter(Rule::ifTrue), BlockStateProvider.CODEC
/* 40 */           .fieldOf("then").forGetter(Rule::then))
/* 41 */         .apply(i, Rule::new)); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RuleBasedBlockStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
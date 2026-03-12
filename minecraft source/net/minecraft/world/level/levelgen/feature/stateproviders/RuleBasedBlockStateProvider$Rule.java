/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
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
/*    */ public final class Rule
/*    */   extends Record
/*    */ {
/*    */   private final BlockPredicate ifTrue;
/*    */   private final BlockStateProvider then;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/stateproviders/RuleBasedBlockStateProvider$Rule;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 37 */   public Rule(BlockPredicate ifTrue, BlockStateProvider then) { this.ifTrue = ifTrue; this.then = then; } public BlockPredicate ifTrue() { return this.ifTrue; } public BlockStateProvider then() { return this.then; }
/* 38 */   public static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPredicate.CODEC
/* 39 */         .fieldOf("if_true").forGetter(Rule::ifTrue), BlockStateProvider.CODEC
/* 40 */         .fieldOf("then").forGetter(Rule::then))
/* 41 */       .apply(i, Rule::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RuleBasedBlockStateProvider$Rule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
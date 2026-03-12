/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class MatchTool extends Record implements LootItemCondition {
/* 14 */   public MatchTool(Optional<ItemPredicate> predicate) { this.predicate = predicate; } private final Optional<ItemPredicate> predicate; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool; } public Optional<ItemPredicate> predicate() { return this.predicate; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 17 */   public static final MapCodec<MatchTool> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ItemPredicate.CODEC
/* 18 */         .optionalFieldOf("predicate").forGetter(MatchTool::predicate))
/* 19 */       .apply(i, MatchTool::new));
/*    */ 
/*    */ 
/*    */   
/* 23 */   public LootItemConditionType getType() { return LootItemConditions.MATCH_TOOL; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.TOOL); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 33 */     ItemStack tool = (ItemStack)context.getOptionalParameter(LootContextParams.TOOL);
/* 34 */     return (tool != null && (this.predicate.isEmpty() || ((ItemPredicate)this.predicate.get()).test(tool)));
/*    */   }
/*    */ 
/*    */   
/* 38 */   public static LootItemCondition.Builder toolMatches(ItemPredicate.Builder predicate) { return () -> new MatchTool(Optional.of(predicate.build())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\MatchTool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
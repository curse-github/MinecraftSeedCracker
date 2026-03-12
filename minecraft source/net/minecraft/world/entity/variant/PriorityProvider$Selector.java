/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class Selector<Context, Condition extends PriorityProvider.SelectorCondition<Context>>
/*    */   extends Record
/*    */ {
/*    */   private final Optional<Condition> condition;
/*    */   private final int priority;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>; }
/*    */   
/* 21 */   public Selector(Optional<Condition> condition, int priority) { this.condition = condition; this.priority = priority; } public Optional<Condition> condition() { return this.condition; } public int priority() { return this.priority; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public Selector(Condition condition, int priority) { this(Optional.of(condition), priority); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public Selector(int priority) { this(Optional.empty(), priority); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static <Context, Condition extends PriorityProvider.SelectorCondition<Context>> Codec<Selector<Context, Condition>> codec(Codec<Condition> conditionCodec) { return RecordCodecBuilder.create(i -> i.group(conditionCodec
/* 35 */           .optionalFieldOf("condition").forGetter(Selector::condition), Codec.INT
/* 36 */           .fieldOf("priority").forGetter(Selector::priority))
/* 37 */         .apply(i, Selector::new)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\PriorityProvider$Selector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
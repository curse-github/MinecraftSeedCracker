/*    */ package net.minecraft.world.entity.variant;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface PriorityProvider<Context, Condition extends PriorityProvider.SelectorCondition<Context>> {
/*    */   public static final class Selector<Context, Condition extends SelectorCondition<Context>> extends Record {
/*    */     private final Optional<Condition> condition;
/*    */     private final int priority;
/*    */     
/* 21 */     public Selector(Optional<Condition> condition, int priority) { this.condition = condition; this.priority = priority; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 21 */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>; } public Optional<Condition> condition() { return this.condition; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 21 */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$Selector<TContext;TCondition;>; } public int priority() { return this.priority; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     public Selector(Condition condition, int priority) { this(Optional.of(condition), priority); }
/*    */ 
/*    */ 
/*    */     
/* 30 */     public Selector(int priority) { this(Optional.empty(), priority); }
/*    */ 
/*    */ 
/*    */     
/* 34 */     public static <Context, Condition extends PriorityProvider.SelectorCondition<Context>> Codec<Selector<Context, Condition>> codec(Codec<Condition> conditionCodec) { return RecordCodecBuilder.create(i -> i.group(conditionCodec
/* 35 */             .optionalFieldOf("condition").forGetter(Selector::condition), Codec.INT
/* 36 */             .fieldOf("priority").forGetter(Selector::priority))
/* 37 */           .apply(i, Selector::new)); }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface SelectorCondition<C>
/*    */     extends Predicate<C>
/*    */   {
/* 44 */     static <C> SelectorCondition<C> alwaysTrue() { return context -> true; } }
/*    */   public static final class UnpackedEntry<C, T> extends Record { private final T entry; private final int priority;
/*    */     private final PriorityProvider.SelectorCondition<C> condition;
/*    */     
/* 48 */     public UnpackedEntry(T entry, int priority, PriorityProvider.SelectorCondition<C> condition) { this.entry = entry; this.priority = priority; this.condition = condition; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 48 */       //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>; } public T entry() { return (T)this.entry; } public int priority() { return this.priority; } public PriorityProvider.SelectorCondition<C> condition() { return this.condition; }
/* 49 */     public static final Comparator<UnpackedEntry<?, ?>> HIGHEST_PRIORITY_FIRST = Comparator.comparingInt(UnpackedEntry::priority).reversed(); }
/*    */ 
/*    */   
/*    */   static <C, T> Stream<T> select(Stream<T> entries, Function<T, PriorityProvider<C, ?>> extractor, C context) {
/* 53 */     List<UnpackedEntry<C, T>> unpackedEntries = new ArrayList<UnpackedEntry<C, T>>();
/* 54 */     entries.forEach(entry -> {
/* 55 */           PriorityProvider<C, ?> provider = (PriorityProvider)extractor.apply(entry);
/* 56 */           for (Selector<C, ?> selector : provider.selectors()) {
/* 57 */             unpackedEntries.add(new UnpackedEntry(entry, selector.priority(), (SelectorCondition)DataFixUtils.orElseGet(selector.condition(), SelectorCondition::alwaysTrue)));
/*    */           }
/*    */         });
/*    */     
/* 61 */     unpackedEntries.sort(UnpackedEntry.HIGHEST_PRIORITY_FIRST);
/*    */     
/* 63 */     Iterator<UnpackedEntry<C, T>> iterator = unpackedEntries.iterator();
/* 64 */     int highestMatchedPriority = Integer.MIN_VALUE;
/* 65 */     while (iterator.hasNext()) {
/* 66 */       UnpackedEntry<C, T> entry = (UnpackedEntry)iterator.next();
/*    */ 
/*    */       
/* 69 */       if (entry.priority < highestMatchedPriority) {
/* 70 */         iterator.remove(); continue;
/*    */       } 
/* 72 */       if (entry.condition.test(context)) {
/* 73 */         highestMatchedPriority = entry.priority; continue;
/*    */       } 
/* 75 */       iterator.remove();
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 80 */     return unpackedEntries.stream().map(UnpackedEntry::entry);
/*    */   }
/*    */   
/*    */   static <C, T> Optional<T> pick(Stream<T> entries, Function<T, PriorityProvider<C, ?>> extractor, RandomSource randomSource, C context) {
/* 84 */     List<T> selected = select(entries, extractor, context).toList();
/* 85 */     return Util.getRandomSafe(selected, randomSource);
/*    */   }
/*    */ 
/*    */   
/* 89 */   static <Context, Condition extends SelectorCondition<Context>> List<Selector<Context, Condition>> single(Condition check, int priority) { return List.of(new Selector(check, priority)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <Context, Condition extends SelectorCondition<Context>> List<Selector<Context, Condition>> alwaysTrue(int priority) {
/* 95 */     return List.of(new Selector(
/* 96 */           Optional.empty(), priority));
/*    */   }
/*    */   
/*    */   List<Selector<Context, Condition>> selectors();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\PriorityProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
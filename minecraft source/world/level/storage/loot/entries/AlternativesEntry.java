/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class AlternativesEntry extends CompositeEntryBase {
/* 14 */   public static final MapCodec<AlternativesEntry> CODEC = createCodec(AlternativesEntry::new);
/*    */   
/* 16 */   public static final ProblemReporter.Problem UNREACHABLE_PROBLEM = new ProblemReporter.Problem()
/*    */     {
/*    */       public String description() {
/* 19 */         return "Unreachable entry!";
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 24 */   AlternativesEntry(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) { super(children, conditions); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public LootPoolEntryType getType() { return LootPoolEntries.ALTERNATIVES; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/* 34 */     switch (entries.size()) { case 0: case 1: case 2:  }  return (context, output) -> {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 39 */         for (ComposableEntryContainer entry : entries) {
/* 40 */           if (entry.expand(context, output)) {
/* 41 */             return true;
/*    */           }
/*    */         } 
/* 44 */         return false;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 51 */     super.validate(context);
/*    */     
/* 53 */     for (int i = 0; i < this.children.size() - 1; i++) {
/*    */       
/* 55 */       if (((LootPoolEntryContainer)this.children.get(i)).conditions.isEmpty())
/* 56 */         context.reportProblem(UNREACHABLE_PROBLEM); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder> {
/* 62 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(Builder... entries) {
/* 65 */       for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 66 */         this.entries.add(entry.build());
/*    */       }
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 72 */     protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder otherwise(LootPoolEntryContainer.Builder<?> other) {
/* 77 */       this.entries.add(other.build());
/* 78 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 83 */     public LootPoolEntryContainer build() { return new AlternativesEntry(this.entries.build(), getConditions()); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public static Builder alternatives(Builder... entries) { return new Builder(entries); }
/*    */ 
/*    */ 
/*    */   
/* 92 */   public static <E> Builder alternatives(Collection<E> items, Function<E, LootPoolEntryContainer.Builder<?>> provider) { Objects.requireNonNull(provider); return new Builder((Builder[])items.stream().map(provider::apply).toArray(x$0 -> new LootPoolEntryContainer.Builder[x$0])); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SequentialEntry extends CompositeEntryBase {
/* 10 */   public static final MapCodec<SequentialEntry> CODEC = createCodec(SequentialEntry::new);
/*    */ 
/*    */   
/* 13 */   SequentialEntry(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) { super(children, conditions); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public LootPoolEntryType getType() { return LootPoolEntries.SEQUENCE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/* 23 */     switch (entries.size()) { case 0: case 1: case 2:  }  return (context, output) -> {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 28 */         for (ComposableEntryContainer entry : entries) {
/* 29 */           if (!entry.expand(context, output)) {
/* 30 */             return false;
/*    */           }
/*    */         } 
/* 33 */         return true;
/*    */       };
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder> {
/* 39 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(Builder... entries) {
/* 42 */       for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 43 */         this.entries.add(entry.build());
/*    */       }
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 49 */     protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder then(LootPoolEntryContainer.Builder<?> other) {
/* 54 */       this.entries.add(other.build());
/* 55 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 60 */     public LootPoolEntryContainer build() { return new SequentialEntry(this.entries.build(), getConditions()); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public static Builder sequential(Builder... entries) { return new Builder(entries); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\SequentialEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
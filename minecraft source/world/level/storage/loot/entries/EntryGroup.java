/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EntryGroup extends CompositeEntryBase {
/* 10 */   public static final MapCodec<EntryGroup> CODEC = createCodec(EntryGroup::new);
/*    */ 
/*    */   
/* 13 */   EntryGroup(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) { super(children, conditions); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public LootPoolEntryType getType() { return LootPoolEntries.GROUP; }
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/*    */     ComposableEntryContainer second;
/*    */     ComposableEntryContainer first;
/* 23 */     switch (entries.size()) { case 0: 
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 27 */         first = (ComposableEntryContainer)entries.get(0);
/* 28 */         second = (ComposableEntryContainer)entries.get(1); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     return (context, output) -> {
/* 36 */         for (ComposableEntryContainer entry : entries) {
/* 37 */           entry.expand(context, output);
/*    */         }
/* 39 */         return true;
/*    */       };
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder> {
/* 45 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(Builder... entries) {
/* 48 */       for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 49 */         this.entries.add(entry.build());
/*    */       }
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 55 */     protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder append(LootPoolEntryContainer.Builder<?> other) {
/* 60 */       this.entries.add(other.build());
/* 61 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 66 */     public LootPoolEntryContainer build() { return new EntryGroup(this.entries.build(), getConditions()); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public static Builder list(Builder... entries) { return new Builder(entries); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\EntryGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
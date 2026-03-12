/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
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
/*    */ public class Builder
/*    */   extends LootPoolEntryContainer.Builder<SequentialEntry.Builder>
/*    */ {
/* 39 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(Builder... entries) {
/* 42 */     for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 43 */       this.entries.add(entry.build());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Builder then(LootPoolEntryContainer.Builder<?> other) {
/* 54 */     this.entries.add(other.build());
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public LootPoolEntryContainer build() { return new SequentialEntry(this.entries.build(), getConditions()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\SequentialEntry$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
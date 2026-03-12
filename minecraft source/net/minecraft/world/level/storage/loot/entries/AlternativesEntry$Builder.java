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
/*    */   extends LootPoolEntryContainer.Builder<AlternativesEntry.Builder>
/*    */ {
/* 62 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(Builder... entries) {
/* 65 */     for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 66 */       this.entries.add(entry.build());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Builder otherwise(LootPoolEntryContainer.Builder<?> other) {
/* 77 */     this.entries.add(other.build());
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public LootPoolEntryContainer build() { return new AlternativesEntry(this.entries.build(), getConditions()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
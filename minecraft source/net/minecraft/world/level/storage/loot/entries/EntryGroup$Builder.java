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
/*    */ public class Builder
/*    */   extends LootPoolEntryContainer.Builder<EntryGroup.Builder>
/*    */ {
/* 45 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(Builder... entries) {
/* 48 */     for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 49 */       this.entries.add(entry.build());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Builder append(LootPoolEntryContainer.Builder<?> other) {
/* 60 */     this.entries.add(other.build());
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public LootPoolEntryContainer build() { return new EntryGroup(this.entries.build(), getConditions()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\EntryGroup$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
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
/*    */   extends LootItemConditionalFunction.Builder<SetContainerContents.Builder>
/*    */ {
/*    */   private final ImmutableList.Builder<LootPoolEntryContainer> entries;
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   public Builder(ContainerComponentManipulator<?> component) {
/* 63 */     this.entries = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */     
/* 67 */     this.component = component;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   protected Builder getThis() { return this; }
/*    */ 
/*    */   
/*    */   public Builder withEntry(LootPoolEntryContainer.Builder<?> entry) {
/* 76 */     this.entries.add(entry.build());
/* 77 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public LootItemFunction build() { return new SetContainerContents(getConditions(), this.component, this.entries.build()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ItemDisplayBuilder
/*     */   implements CreativeModeTab.Output
/*     */ {
/*     */   public final Collection<ItemStack> tabContents;
/*     */   public final Set<ItemStack> searchTabContents;
/*     */   private final CreativeModeTab tab;
/*     */   private final FeatureFlagSet featureFlagSet;
/*     */   
/*     */   public ItemDisplayBuilder(CreativeModeTab tab, FeatureFlagSet featureFlagSet) {
/* 218 */     this.tabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/* 219 */     this.searchTabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     this.tab = tab;
/* 225 */     this.featureFlagSet = featureFlagSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
/* 230 */     if (stack.getCount() != 1) {
/* 231 */       throw new IllegalArgumentException("Stack size must be exactly 1");
/*     */     }
/*     */ 
/*     */     
/* 235 */     boolean foundDuplicateStack = (this.tabContents.contains(stack) && tabVisibility != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
/*     */     
/* 237 */     if (foundDuplicateStack) {
/* 238 */       throw new IllegalStateException("Accidentally adding the same item stack twice " + stack
/* 239 */           .getDisplayName().getString() + " to a Creative Mode Tab: " + this.tab
/*     */           
/* 241 */           .getDisplayName().getString());
/*     */     }
/*     */     
/* 244 */     if (stack.getItem().isEnabled(this.featureFlagSet))
/* 245 */       switch (tabVisibility.ordinal()) {
/*     */         case 0:
/* 247 */           this.tabContents.add(stack);
/* 248 */           this.searchTabContents.add(stack); break;
/*     */         case 1:
/* 250 */           this.tabContents.add(stack); break;
/* 251 */         case 2: this.searchTabContents.add(stack);
/*     */           break;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\CreativeModeTab$ItemDisplayBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
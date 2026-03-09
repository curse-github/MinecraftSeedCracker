/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.apache.commons.lang3.math.Fraction;
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
/*     */ public class Mutable
/*     */ {
/*     */   private final List<ItemStack> items;
/*     */   private Fraction weight;
/*     */   private int selectedItem;
/*     */   
/*     */   public Mutable(BundleContents contents) {
/* 156 */     this.items = new ArrayList(contents.items);
/* 157 */     this.weight = contents.weight;
/* 158 */     this.selectedItem = contents.selectedItem;
/*     */   }
/*     */   
/*     */   public Mutable clearItems() {
/* 162 */     this.items.clear();
/* 163 */     this.weight = Fraction.ZERO;
/* 164 */     this.selectedItem = -1;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   private int findStackIndex(ItemStack itemsToAdd) {
/* 169 */     if (!itemsToAdd.isStackable()) {
/* 170 */       return -1;
/*     */     }
/* 172 */     for (int i = 0; i < this.items.size(); i++) {
/* 173 */       if (ItemStack.isSameItemSameComponents((ItemStack)this.items.get(i), itemsToAdd)) {
/* 174 */         return i;
/*     */       }
/*     */     } 
/* 177 */     return -1;
/*     */   }
/*     */   
/*     */   private int getMaxAmountToAdd(ItemStack item) {
/* 181 */     Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
/* 182 */     return Math.max(remainingWeight.divideBy(BundleContents.getWeight(item)).intValue(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int tryInsert(ItemStack itemsToAdd) {
/* 189 */     if (!BundleContents.canItemBeInBundle(itemsToAdd)) {
/* 190 */       return 0;
/*     */     }
/*     */     
/* 193 */     int amountToAdd = Math.min(itemsToAdd.getCount(), getMaxAmountToAdd(itemsToAdd));
/* 194 */     if (amountToAdd == 0) {
/* 195 */       return 0;
/*     */     }
/*     */     
/* 198 */     this.weight = this.weight.add(BundleContents.getWeight(itemsToAdd).multiplyBy(Fraction.getFraction(amountToAdd, 1)));
/*     */     
/* 200 */     int stackIndex = findStackIndex(itemsToAdd);
/* 201 */     if (stackIndex != -1) {
/* 202 */       ItemStack removedStack = (ItemStack)this.items.remove(stackIndex);
/* 203 */       ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
/* 204 */       itemsToAdd.shrink(amountToAdd);
/*     */       
/* 206 */       this.items.add(0, mergedStack);
/*     */     } else {
/* 208 */       this.items.add(0, itemsToAdd.split(amountToAdd));
/*     */     } 
/*     */     
/* 211 */     return amountToAdd;
/*     */   }
/*     */   
/*     */   public int tryTransfer(Slot slot, Player player) {
/* 215 */     ItemStack other = slot.getItem();
/* 216 */     int maxAmount = getMaxAmountToAdd(other);
/* 217 */     return BundleContents.canItemBeInBundle(other) ? tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
/*     */   }
/*     */ 
/*     */   
/* 221 */   public void toggleSelectedItem(int selectedItem) { this.selectedItem = (this.selectedItem == selectedItem || indexIsOutsideAllowedBounds(selectedItem)) ? -1 : selectedItem; }
/*     */ 
/*     */ 
/*     */   
/* 225 */   private boolean indexIsOutsideAllowedBounds(int selectedItem) { return (selectedItem < 0 || selectedItem >= this.items.size()); }
/*     */ 
/*     */   
/*     */   public ItemStack removeOne() {
/* 229 */     if (this.items.isEmpty()) {
/* 230 */       return null;
/*     */     }
/* 232 */     int removeIndex = indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
/* 233 */     ItemStack stack = ((ItemStack)this.items.remove(removeIndex)).copy();
/* 234 */     this.weight = this.weight.subtract(BundleContents.getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/* 235 */     toggleSelectedItem(-1);
/* 236 */     return stack;
/*     */   }
/*     */ 
/*     */   
/* 240 */   public Fraction weight() { return this.weight; }
/*     */ 
/*     */ 
/*     */   
/* 244 */   public BundleContents toImmutable() { return new BundleContents(List.copyOf(this.items), this.weight, this.selectedItem); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\BundleContents$Mutable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
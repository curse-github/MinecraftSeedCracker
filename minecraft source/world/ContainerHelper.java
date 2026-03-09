/*    */ package net.minecraft.world;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class ContainerHelper
/*    */ {
/*    */   public static final String TAG_ITEMS = "Items";
/*    */   
/*    */   public static ItemStack removeItem(List<ItemStack> itemStacks, int slot, int count) {
/* 15 */     if (slot < 0 || slot >= itemStacks.size() || ((ItemStack)itemStacks.get(slot)).isEmpty() || count <= 0) {
/* 16 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 19 */     return ((ItemStack)itemStacks.get(slot)).split(count);
/*    */   }
/*    */   
/*    */   public static ItemStack takeItem(List<ItemStack> itemStacks, int slot) {
/* 23 */     if (slot < 0 || slot >= itemStacks.size()) {
/* 24 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 27 */     return (ItemStack)itemStacks.set(slot, ItemStack.EMPTY);
/*    */   }
/*    */ 
/*    */   
/* 31 */   public static void saveAllItems(ValueOutput output, NonNullList<ItemStack> itemStacks) { saveAllItems(output, itemStacks, true); }
/*    */ 
/*    */   
/*    */   public static void saveAllItems(ValueOutput output, NonNullList<ItemStack> itemStacks, boolean alsoWhenEmpty) {
/* 35 */     ValueOutput.TypedOutputList<ItemStackWithSlot> itemsOutput = output.list("Items", ItemStackWithSlot.CODEC);
/* 36 */     for (int i = 0; i < itemStacks.size(); i++) {
/* 37 */       ItemStack itemStack = (ItemStack)itemStacks.get(i);
/* 38 */       if (!itemStack.isEmpty()) {
/* 39 */         itemsOutput.add(new ItemStackWithSlot(i, itemStack));
/*    */       }
/*    */     } 
/* 42 */     if (itemsOutput.isEmpty() && !alsoWhenEmpty) {
/* 43 */       output.discard("Items");
/*    */     }
/*    */   }
/*    */   
/*    */   public static void loadAllItems(ValueInput input, NonNullList<ItemStack> itemStacks) {
/* 48 */     for (ItemStackWithSlot item : input.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
/* 49 */       if (item.isValidInContainer(itemStacks.size())) {
/* 50 */         itemStacks.set(item.slot(), item.stack());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public static int clearOrCountMatchingItems(Container container, Predicate<ItemStack> predicate, int amountToRemove, boolean countingOnly) {
/* 56 */     int count = 0;
/* 57 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 58 */       ItemStack itemStack = container.getItem(i);
/* 59 */       int amountRemoved = clearOrCountMatchingItems(itemStack, predicate, amountToRemove - count, countingOnly);
/* 60 */       if (amountRemoved > 0 && !countingOnly && itemStack.isEmpty()) {
/* 61 */         container.setItem(i, ItemStack.EMPTY);
/*    */       }
/* 63 */       count += amountRemoved;
/*    */     } 
/* 65 */     return count;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int clearOrCountMatchingItems(ItemStack itemStack, Predicate<ItemStack> predicate, int amountToRemove, boolean countingOnly) {
/* 70 */     if (itemStack.isEmpty() || !predicate.test(itemStack)) {
/* 71 */       return 0;
/*    */     }
/*    */     
/* 74 */     if (countingOnly) {
/* 75 */       return itemStack.getCount();
/*    */     }
/*    */     
/* 78 */     int amountRemoved = (amountToRemove < 0) ? itemStack.getCount() : Math.min(amountToRemove, itemStack.getCount());
/* 79 */     itemStack.shrink(amountRemoved);
/* 80 */     return amountRemoved;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ContainerHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
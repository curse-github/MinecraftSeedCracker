/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ContainerComponentManipulator<T>
/*    */ {
/*    */   default void setContents(ItemStack itemStack, T defaultValue, Stream<ItemStack> newContents) {
/* 20 */     T currentValue = (T)itemStack.getOrDefault(type(), defaultValue);
/* 21 */     T newValue = (T)setContents(currentValue, newContents);
/* 22 */     itemStack.set(type(), newValue);
/*    */   }
/*    */ 
/*    */   
/* 26 */   default void setContents(ItemStack itemStack, Stream<ItemStack> newContents) { setContents(itemStack, empty(), newContents); }
/*    */ 
/*    */   
/*    */   default void modifyItems(ItemStack itemStack, UnaryOperator<ItemStack> modifier) {
/* 30 */     T contents = (T)itemStack.get(type());
/* 31 */     if (contents != null) {
/*    */       
/* 33 */       UnaryOperator<ItemStack> nonEmptyModifier = currentItemStack -> {
/* 34 */           if (currentItemStack.isEmpty()) {
/* 35 */             return currentItemStack;
/*    */           }
/* 37 */           ItemStack newItemStack = (ItemStack)modifier.apply(currentItemStack);
/* 38 */           newItemStack.limitSize(newItemStack.getMaxStackSize());
/* 39 */           return newItemStack;
/*    */         };
/* 41 */       setContents(itemStack, getContents(contents).map(nonEmptyModifier));
/*    */     } 
/*    */   }
/*    */   
/*    */   default SlotCollection getSlots(ItemStack itemStack) {
/* 46 */     return () -> {
/* 47 */         T contents = (T)itemStack.get(type());
/* 48 */         if (contents != null)
/*    */         {
/* 50 */           return getContents(contents).filter(());
/*    */         }
/* 52 */         return Stream.empty();
/*    */       };
/*    */   }
/*    */   
/*    */   DataComponentType<T> type();
/*    */   
/*    */   T empty();
/*    */   
/*    */   T setContents(T paramT, Stream<ItemStack> paramStream);
/*    */   
/*    */   Stream<ItemStack> getContents(T paramT);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
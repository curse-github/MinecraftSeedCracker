/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ItemContainerContents;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements ContainerComponentManipulator<ItemContainerContents>
/*    */ {
/* 21 */   public DataComponentType<ItemContainerContents> type() { return DataComponents.CONTAINER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public Stream<ItemStack> getContents(ItemContainerContents component) { return component.stream(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public ItemContainerContents empty() { return ItemContainerContents.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public ItemContainerContents setContents(ItemContainerContents component, Stream<ItemStack> newContents) { return ItemContainerContents.fromItems(newContents.toList()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
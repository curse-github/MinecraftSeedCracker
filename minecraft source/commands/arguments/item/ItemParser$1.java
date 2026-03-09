/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import org.apache.commons.lang3.mutable.MutableObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ItemParser.Visitor
/*    */ {
/*    */   null(ItemParser this$0) {}
/*    */   
/* 72 */   public void visitItem(Holder<Item> item) { itemResult.setValue(item); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public <T> void visitComponent(DataComponentType<T> type, T value) { componentsBuilder.set(type, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   public <T> void visitRemovedComponent(DataComponentType<T> type) { componentsBuilder.remove(type); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemParser$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
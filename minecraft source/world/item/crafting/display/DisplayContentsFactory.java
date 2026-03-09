/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface DisplayContentsFactory<T>
/*    */ {
/*    */   public static interface ForRemainders<T>
/*    */     extends DisplayContentsFactory<T> {
/*    */     T addRemainder(T param1T, List<T> param1List);
/*    */   }
/*    */   
/*    */   public static interface ForStacks<T>
/*    */     extends DisplayContentsFactory<T> {
/* 17 */     default T forStack(Holder<Item> item) { return (T)forStack(new ItemStack(item)); }
/*    */ 
/*    */ 
/*    */     
/* 21 */     default T forStack(Item item) { return (T)forStack(new ItemStack(item)); }
/*    */     
/*    */     T forStack(ItemStack param1ItemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\DisplayContentsFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
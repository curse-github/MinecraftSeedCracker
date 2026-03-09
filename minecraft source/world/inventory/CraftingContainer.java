/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.CraftingInput;
/*    */ 
/*    */ 
/*    */ public interface CraftingContainer
/*    */   extends Container, StackedContentsCompatible
/*    */ {
/*    */   int getWidth();
/*    */   
/*    */   int getHeight();
/*    */   
/*    */   List<ItemStack> getItems();
/*    */   
/* 18 */   default CraftingInput asCraftInput() { return asPositionedCraftInput().input(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   default CraftingInput.Positioned asPositionedCraftInput() { return CraftingInput.ofPositioned(getWidth(), getHeight(), getItems()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CraftingContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
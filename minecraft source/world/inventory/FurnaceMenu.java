/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class FurnaceMenu
/*    */   extends AbstractFurnaceMenu {
/* 10 */   public FurnaceMenu(int containerId, Inventory inventory) { super(MenuType.FURNACE, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, inventory); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public FurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) { super(MenuType.FURNACE, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, inventory, container, data); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\FurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
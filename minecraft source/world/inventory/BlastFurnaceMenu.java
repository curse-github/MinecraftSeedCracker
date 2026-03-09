/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class BlastFurnaceMenu
/*    */   extends AbstractFurnaceMenu {
/* 10 */   public BlastFurnaceMenu(int containerId, Inventory inventory) { super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, inventory); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public BlastFurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) { super(MenuType.BLAST_FURNACE, RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, inventory, container, data); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\BlastFurnaceMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
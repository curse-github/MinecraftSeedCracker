/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class SmokerMenu
/*    */   extends AbstractFurnaceMenu {
/* 10 */   public SmokerMenu(int containerId, Inventory inventory) { super(MenuType.SMOKER, RecipeType.SMOKING, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, containerId, inventory); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public SmokerMenu(int containerId, Inventory inventory, Container container, ContainerData data) { super(MenuType.SMOKER, RecipeType.SMOKING, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, containerId, inventory, container, data); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\SmokerMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
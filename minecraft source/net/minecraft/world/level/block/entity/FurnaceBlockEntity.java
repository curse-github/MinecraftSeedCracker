/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.FurnaceMenu;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/* 12 */   private static final Component DEFAULT_NAME = Component.translatable("container.furnace");
/*    */ 
/*    */   
/* 15 */   public FurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.FURNACE, worldPosition, blockState, RecipeType.SMELTING); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new FurnaceMenu(containerId, inventory, this, this.dataAccess); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\FurnaceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
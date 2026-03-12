/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.BlastFurnaceMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlastFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/* 13 */   private static final Component DEFAULT_NAME = Component.translatable("container.blast_furnace");
/*    */ 
/*    */   
/* 16 */   public BlastFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.BLAST_FURNACE, worldPosition, blockState, RecipeType.BLASTING); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) { return super.getBurnDuration(fuelValues, itemStack) / 2; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new BlastFurnaceMenu(containerId, inventory, this, this.dataAccess); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BlastFurnaceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */   extends Slot
/*    */ {
/* 63 */   null(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*    */ 
/*    */   
/* 66 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean mayPickup(Player player) { return ItemCombinerMenu.this.mayPickup(player, hasItem()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public void onTake(Player player, ItemStack carried) { ItemCombinerMenu.this.onTake(player, carried); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ItemCombinerMenu$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
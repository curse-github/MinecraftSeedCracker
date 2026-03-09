/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class FurnaceFuelSlot extends Slot {
/*    */   private final AbstractFurnaceMenu menu;
/*    */   
/*    */   public FurnaceFuelSlot(AbstractFurnaceMenu menu, Container container, int slot, int x, int y) {
/* 11 */     super(container, slot, x, y);
/* 12 */     this.menu = menu;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean mayPlace(ItemStack itemStack) { return (this.menu.isFuel(itemStack) || isBucket(itemStack)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int getMaxStackSize(ItemStack itemStack) { return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static boolean isBucket(ItemStack itemStack) { return itemStack.is(Items.BUCKET); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\FurnaceFuelSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
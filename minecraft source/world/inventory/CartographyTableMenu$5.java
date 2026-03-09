/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
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
/*    */   extends Slot
/*    */ {
/* 68 */   null(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*    */ 
/*    */   
/* 71 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 76 */     ((Slot)CartographyTableMenu.this.slots.get(0)).remove(1);
/* 77 */     ((Slot)CartographyTableMenu.this.slots.get(1)).remove(1);
/*    */     
/* 79 */     carried.getItem().onCraftedBy(carried, player);
/*    */     
/* 81 */     access.execute((level, pos) -> {
/*    */           
/* 83 */           long gameTime = level.getGameTime();
/* 84 */           if (CartographyTableMenu.this.lastSoundTime != gameTime) {
/* 85 */             level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 86 */             CartographyTableMenu.this.lastSoundTime = gameTime;
/*    */           } 
/*    */         });
/*    */     
/* 90 */     super.onTake(player, carried);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CartographyTableMenu$5.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
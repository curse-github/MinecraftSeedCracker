/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Slot
/*     */ {
/*  94 */   null(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*     */ 
/*     */   
/*  97 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/* 102 */     LoomMenu.this.bannerSlot.remove(1);
/* 103 */     LoomMenu.this.dyeSlot.remove(1);
/* 104 */     if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem()) {
/* 105 */       LoomMenu.this.selectedBannerPatternIndex.set(-1);
/*     */     }
/* 107 */     access.execute((level, pos) -> {
/*     */           
/* 109 */           long gameTime = level.getGameTime();
/* 110 */           if (LoomMenu.this.lastSoundTime != gameTime) {
/* 111 */             level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 112 */             LoomMenu.this.lastSoundTime = gameTime;
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 117 */     super.onTake(player, carried);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\LoomMenu$6.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
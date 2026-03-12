/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MenuConstructor;
/*    */ 
/*    */ public final class SimpleMenuProvider implements MenuProvider {
/*    */   private final Component title;
/*    */   private final MenuConstructor menuConstructor;
/*    */   
/*    */   public SimpleMenuProvider(MenuConstructor menuConstructor, Component title) {
/* 14 */     this.menuConstructor = menuConstructor;
/* 15 */     this.title = title;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public Component getDisplayName() { return this.title; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) { return this.menuConstructor.createMenu(containerId, inventory, player); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\SimpleMenuProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LecternMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private static final int DATA_COUNT = 1;
/*    */   private static final int SLOT_COUNT = 1;
/*    */   public static final int BUTTON_PREV_PAGE = 1;
/*    */   public static final int BUTTON_NEXT_PAGE = 2;
/*    */   public static final int BUTTON_TAKE_BOOK = 3;
/*    */   public static final int BUTTON_PAGE_JUMP_RANGE_START = 100;
/*    */   private final Container lectern;
/*    */   private final ContainerData lecternData;
/*    */   
/* 22 */   public LecternMenu(int containerId) { this(containerId, new SimpleContainer(1), new SimpleContainerData(1)); }
/*    */ 
/*    */   
/*    */   public LecternMenu(int containerId, Container lectern, ContainerData lecternData) {
/* 26 */     super(MenuType.LECTERN, containerId);
/* 27 */     checkContainerSize(lectern, 1);
/* 28 */     checkContainerDataCount(lecternData, 1);
/* 29 */     this.lectern = lectern;
/* 30 */     this.lecternData = lecternData;
/* 31 */     addSlot(new Slot(lectern, 0, 0, 0)
/*    */         {
/*    */           public void setChanged() {
/* 34 */             super.setChanged();
/* 35 */             LecternMenu.this.slotsChanged(this.container);
/*    */           }
/*    */         });
/*    */     
/* 39 */     addDataSlots(lecternData);
/*    */   } public boolean clickMenuButton(Player player, int buttonId) {
/*    */     int currentPage;
/*    */     int currentPage;
/*    */     ItemStack book;
/* 44 */     if (buttonId >= 100) {
/* 45 */       int pageToSet = buttonId - 100;
/* 46 */       setData(0, pageToSet);
/* 47 */       return true;
/*    */     } 
/*    */     
/* 50 */     switch (buttonId) {
/*    */       case 2:
/* 52 */         currentPage = this.lecternData.get(0);
/* 53 */         setData(0, currentPage + 1);
/* 54 */         return true;
/*    */       
/*    */       case 1:
/* 57 */         currentPage = this.lecternData.get(0);
/* 58 */         setData(0, currentPage - 1);
/* 59 */         return true;
/*    */       
/*    */       case 3:
/* 62 */         if (!player.mayBuild()) {
/* 63 */           return false;
/*    */         }
/* 65 */         book = this.lectern.removeItemNoUpdate(0);
/* 66 */         this.lectern.setChanged();
/* 67 */         if (!player.getInventory().add(book)) {
/* 68 */           player.drop(book, false);
/*    */         }
/* 70 */         return true;
/*    */     } 
/*    */     
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 78 */   public ItemStack quickMoveStack(Player player, int slotIndex) { return ItemStack.EMPTY; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setData(int id, int value) {
/* 83 */     super.setData(id, value);
/* 84 */     broadcastChanges();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public boolean stillValid(Player player) { return this.lectern.stillValid(player); }
/*    */ 
/*    */ 
/*    */   
/* 93 */   public ItemStack getBook() { return this.lectern.getItem(0); }
/*    */ 
/*    */ 
/*    */   
/* 97 */   public int getPage() { return this.lecternData.get(0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\LecternMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
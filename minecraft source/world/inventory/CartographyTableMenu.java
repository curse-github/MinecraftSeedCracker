/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.item.component.MapPostProcessing;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CartographyTableMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   public static final int MAP_SLOT = 0;
/*     */   public static final int ADDITIONAL_SLOT = 1;
/*     */   public static final int RESULT_SLOT = 2;
/*     */   private static final int INV_SLOT_START = 3;
/*     */   
/*  29 */   public final Container container = new SimpleContainer(2)
/*     */     {
/*     */       public void setChanged() {
/*  32 */         CartographyTableMenu.this.slotsChanged(this);
/*  33 */         super.setChanged();
/*     */       }
/*     */     }; private static final int INV_SLOT_END = 30; private static final int USE_ROW_SLOT_START = 30; private static final int USE_ROW_SLOT_END = 39; private final ContainerLevelAccess access; private long lastSoundTime;
/*  36 */   private final ResultContainer resultContainer = new ResultContainer()
/*     */     {
/*     */       public void setChanged()
/*     */       {
/*  40 */         CartographyTableMenu.this.slotsChanged(this);
/*  41 */         super.setChanged();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  46 */   public CartographyTableMenu(int containerId, Inventory inventory) { this(containerId, inventory, ContainerLevelAccess.NULL); }
/*     */ 
/*     */   
/*     */   public CartographyTableMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
/*  50 */     super(MenuType.CARTOGRAPHY_TABLE, containerId);
/*     */     
/*  52 */     this.access = access;
/*     */     
/*  54 */     addSlot(new Slot(this, this.container, 0, 15, 15)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  57 */             return itemStack.has(DataComponents.MAP_ID);
/*     */           }
/*     */         });
/*     */     
/*  61 */     addSlot(new Slot(this, this.container, 1, 15, 52)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  64 */             return (itemStack.is(Items.PAPER) || itemStack.is(Items.MAP) || itemStack.is(Items.GLASS_PANE));
/*     */           }
/*     */         });
/*     */     
/*  68 */     addSlot(new Slot(this.resultContainer, 2, 145, 39)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  71 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/*  76 */             ((Slot)CartographyTableMenu.this.slots.get(0)).remove(1);
/*  77 */             ((Slot)CartographyTableMenu.this.slots.get(1)).remove(1);
/*     */             
/*  79 */             carried.getItem().onCraftedBy(carried, player);
/*     */             
/*  81 */             access.execute((level, pos) -> {
/*     */                   
/*  83 */                   long gameTime = level.getGameTime();
/*  84 */                   if (CartographyTableMenu.this.lastSoundTime != gameTime) {
/*  85 */                     level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  86 */                     CartographyTableMenu.this.lastSoundTime = gameTime;
/*     */                   } 
/*     */                 });
/*     */             
/*  90 */             super.onTake(player, carried);
/*     */           }
/*     */         });
/*     */     
/*  94 */     addStandardInventorySlots(inventory, 8, 84);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean stillValid(Player player) { return stillValid(this.access, player, Blocks.CARTOGRAPHY_TABLE); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/* 104 */     ItemStack mapStack = this.container.getItem(0);
/* 105 */     ItemStack additionalStack = this.container.getItem(1);
/* 106 */     ItemStack resultStack = this.resultContainer.getItem(2);
/*     */     
/* 108 */     if (!resultStack.isEmpty() && (mapStack.isEmpty() || additionalStack.isEmpty())) {
/* 109 */       this.resultContainer.removeItemNoUpdate(2);
/* 110 */     } else if (!mapStack.isEmpty() && !additionalStack.isEmpty()) {
/* 111 */       setupResultSlot(mapStack, additionalStack, resultStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupResultSlot(ItemStack mapStack, ItemStack additionalStack, ItemStack resultStack) {
/* 116 */     this.access.execute((level, pos) -> {
/* 117 */           ItemStack result; MapItemSavedData mapData = MapItem.getSavedData(mapStack, level);
/*     */           
/* 119 */           if (mapData == null) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 124 */           if (additionalStack.is(Items.PAPER) && !mapData.locked && mapData.scale < 4) {
/* 125 */             result = mapStack.copyWithCount(1);
/* 126 */             result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.SCALE);
/* 127 */             broadcastChanges();
/* 128 */           } else if (additionalStack.is(Items.GLASS_PANE) && !mapData.locked) {
/* 129 */             result = mapStack.copyWithCount(1);
/* 130 */             result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.LOCK);
/* 131 */             broadcastChanges();
/* 132 */           } else if (additionalStack.is(Items.MAP)) {
/* 133 */             result = mapStack.copyWithCount(2);
/* 134 */             broadcastChanges();
/*     */           } else {
/* 136 */             this.resultContainer.removeItemNoUpdate(2);
/* 137 */             broadcastChanges();
/*     */             
/*     */             return;
/*     */           } 
/* 141 */           if (!ItemStack.matches(result, resultStack)) {
/* 142 */             this.resultContainer.setItem(2, result);
/* 143 */             broadcastChanges();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) { return (target.container != this.resultContainer && super.canTakeItemForPickAll(carried, target)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 155 */     ItemStack clicked = ItemStack.EMPTY;
/* 156 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 157 */     if (slot != null && slot.hasItem()) {
/* 158 */       ItemStack stack = slot.getItem();
/* 159 */       clicked = stack.copy();
/*     */       
/* 161 */       if (slotIndex == 2) {
/* 162 */         stack.getItem().onCraftedBy(stack, player);
/* 163 */         if (!moveItemStackTo(stack, 3, 39, true)) {
/* 164 */           return ItemStack.EMPTY;
/*     */         }
/* 166 */         slot.onQuickCraft(stack, clicked);
/* 167 */       } else if (slotIndex == 1 || slotIndex == 0) {
/* 168 */         if (!moveItemStackTo(stack, 3, 39, false)) {
/* 169 */           return ItemStack.EMPTY;
/*     */         }
/* 171 */       } else if (stack.has(DataComponents.MAP_ID)) {
/* 172 */         if (!moveItemStackTo(stack, 0, 1, false)) {
/* 173 */           return ItemStack.EMPTY;
/*     */         }
/* 175 */       } else if (stack.is(Items.PAPER) || stack.is(Items.MAP) || stack.is(Items.GLASS_PANE)) {
/* 176 */         if (!moveItemStackTo(stack, 1, 2, false)) {
/* 177 */           return ItemStack.EMPTY;
/*     */         }
/* 179 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 180 */         if (!moveItemStackTo(stack, 30, 39, false)) {
/* 181 */           return ItemStack.EMPTY;
/*     */         }
/* 183 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 184 */         !moveItemStackTo(stack, 3, 30, false)) {
/* 185 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 189 */       if (stack.isEmpty()) {
/* 190 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       }
/*     */       
/* 193 */       slot.setChanged();
/*     */       
/* 195 */       if (stack.getCount() == clicked.getCount()) {
/* 196 */         return ItemStack.EMPTY;
/*     */       }
/* 198 */       slot.onTake(player, stack);
/* 199 */       broadcastChanges();
/*     */     } 
/*     */     
/* 202 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 207 */     super.removed(player);
/*     */     
/* 209 */     this.resultContainer.removeItemNoUpdate(2);
/* 210 */     this.access.execute((level, pos) -> clearContainer(player, this.container));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CartographyTableMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
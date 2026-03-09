/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.ChiseledBookShelfBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChiseledBookShelfBlockEntity
/*     */   extends BlockEntity
/*     */   implements ListBackedContainer {
/*     */   public static final int MAX_BOOKS_IN_STORAGE = 6;
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DEFAULT_LAST_INTERACTED_SLOT = -1;
/*     */   
/*  33 */   private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
/*     */   
/*  35 */   private int lastInteractedSlot = -1;
/*     */ 
/*     */   
/*  38 */   public ChiseledBookShelfBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.CHISELED_BOOKSHELF, worldPosition, blockState); }
/*     */ 
/*     */   
/*     */   private void updateState(int interactedSlot) {
/*  42 */     if (interactedSlot < 0 || interactedSlot >= 6) {
/*  43 */       LOGGER.error("Expected slot 0-5, got {}", Integer.valueOf(interactedSlot));
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     this.lastInteractedSlot = interactedSlot;
/*  48 */     BlockState updatedState = getBlockState();
/*  49 */     for (int slot = 0; slot < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); slot++) {
/*  50 */       boolean slotIsOccupied = !getItem(slot).isEmpty();
/*  51 */       BooleanProperty slotProperty = (BooleanProperty)ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(slot);
/*     */       
/*  53 */       updatedState = (BlockState)updatedState.setValue(slotProperty, Boolean.valueOf(slotIsOccupied));
/*     */     } 
/*     */     
/*  56 */     ((Level)Objects.requireNonNull(this.level)).setBlock(this.worldPosition, updatedState, 3);
/*     */     
/*  58 */     this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(updatedState));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  63 */     super.loadAdditional(input);
/*     */     
/*  65 */     this.items.clear();
/*  66 */     ContainerHelper.loadAllItems(input, this.items);
/*  67 */     this.lastInteractedSlot = input.getIntOr("last_interacted_slot", -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  72 */     super.saveAdditional(output);
/*     */     
/*  74 */     ContainerHelper.saveAllItems(output, this.items, true);
/*  75 */     output.putInt("last_interacted_slot", this.lastInteractedSlot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public boolean acceptsItemType(ItemStack itemStack) { return itemStack.is(ItemTags.BOOKSHELF_BOOKS); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  90 */     ItemStack retrievedItem = (ItemStack)Objects.requireNonNullElse((ItemStack)getItems().get(slot), ItemStack.EMPTY);
/*  91 */     getItems().set(slot, ItemStack.EMPTY);
/*     */     
/*  93 */     if (!retrievedItem.isEmpty()) {
/*  94 */       updateState(slot);
/*     */     }
/*     */     
/*  97 */     return retrievedItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 102 */     if (acceptsItemType(itemStack)) {
/* 103 */       getItems().set(slot, itemStack);
/* 104 */       updateState(slot);
/* 105 */     } else if (itemStack.isEmpty()) {
/* 106 */       removeItem(slot, getMaxStackSize());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(Container into, int slot, ItemStack itemStack) {
/* 112 */     return into.hasAnyMatching(toItem -> {
/* 113 */           if (toItem.isEmpty()) {
/* 114 */             return true;
/*     */           }
/*     */           
/* 117 */           return (ItemStack.isSameItemSameComponents(itemStack, toItem) && toItem.getCount() + itemStack.getCount() <= into.getMaxStackSize(toItem));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public boolean stillValid(Player player) { return Container.stillValidBlockEntity(this, player); }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public int getLastInteractedSlot() { return this.lastInteractedSlot; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 137 */     super.applyImplicitComponents(components);
/* 138 */     ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 143 */     super.collectImplicitComponents(components);
/* 144 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public void removeComponentsFromTag(ValueOutput output) { output.discard("Items"); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ChiseledBookShelfBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
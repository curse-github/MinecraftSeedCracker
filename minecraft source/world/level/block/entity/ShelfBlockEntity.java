/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.ShelfBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ShelfBlockEntity
/*     */   extends BlockEntity implements ListBackedContainer, ItemOwner {
/*     */   public static final int MAX_ITEMS = 3;
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String ALIGN_ITEMS_TO_BOTTOM_TAG = "align_items_to_bottom";
/*  38 */   private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   
/*     */   private boolean alignItemsToBottom;
/*     */   
/*  42 */   public ShelfBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.SHELF, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  47 */     super.loadAdditional(input);
/*  48 */     this.items.clear();
/*  49 */     ContainerHelper.loadAllItems(input, this.items);
/*  50 */     this.alignItemsToBottom = input.getBooleanOr("align_items_to_bottom", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  55 */     super.saveAdditional(output);
/*  56 */     ContainerHelper.saveAllItems(output, this.items, true);
/*  57 */     output.putBoolean("align_items_to_bottom", this.alignItemsToBottom);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  67 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/*  68 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
/*  69 */       ContainerHelper.saveAllItems(output, this.items, true);
/*  70 */       output.putBoolean("align_items_to_bottom", this.alignItemsToBottom);
/*  71 */       CompoundTag compoundTag = output.buildResult();
/*  72 */       reporter.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1)
/*     */       { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  77 */      } public NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public boolean stillValid(Player player) { return Container.stillValidBlockEntity(this, player); }
/*     */ 
/*     */   
/*     */   public ItemStack swapItemNoUpdate(int slot, ItemStack heldItemStack) {
/*  86 */     ItemStack retrievedItem = removeItemNoUpdate(slot);
/*  87 */     setItemNoUpdate(slot, heldItemStack);
/*  88 */     return retrievedItem;
/*     */   }
/*     */   
/*     */   public void setChanged(Holder.Reference<GameEvent> event) {
/*  92 */     super.setChanged();
/*  93 */     if (this.level != null) {
/*  94 */       if (event != null) {
/*  95 */         this.level.gameEvent(event, this.worldPosition, GameEvent.Context.of(getBlockState()));
/*     */       }
/*  97 */       getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public void setChanged() { setChanged(GameEvent.BLOCK_ACTIVATE); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 108 */     super.applyImplicitComponents(components);
/* 109 */     ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 114 */     super.collectImplicitComponents(components);
/* 115 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public void removeComponentsFromTag(ValueOutput output) { output.discard("Items"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public Level level() { return this.level; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public Vec3 position() { return getBlockPos().getCenter(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public float getVisualRotationYInDegrees() { return ((Direction)getBlockState().getValue(ShelfBlock.FACING)).getOpposite().toYRot(); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public boolean getAlignItemsToBottom() { return this.alignItemsToBottom; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ShelfBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
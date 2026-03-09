/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ShulkerBoxMenu;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class ShulkerBoxBlockEntity
/*     */   extends RandomizableContainerBlockEntity
/*     */   implements WorldlyContainer
/*     */ {
/*     */   public static final int COLUMNS = 9;
/*     */   public static final int ROWS = 3;
/*     */   public static final int CONTAINER_SIZE = 27;
/*     */   public static final int EVENT_SET_OPEN_COUNT = 1;
/*     */   public static final int OPENING_TICK_LENGTH = 10;
/*     */   public static final float MAX_LID_HEIGHT = 0.5F;
/*     */   public static final float MAX_LID_ROTATION = 270.0F;
/*  46 */   private static final int[] SLOTS = IntStream.range(0, 27).toArray();
/*  47 */   private static final Component DEFAULT_NAME = Component.translatable("container.shulkerBox");
/*     */   
/*  49 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   private int openCount;
/*  51 */   private AnimationStatus animationStatus = AnimationStatus.CLOSED;
/*     */   private float progress;
/*     */   private float progressOld;
/*     */   private final DyeColor color;
/*     */   
/*     */   public ShulkerBoxBlockEntity(DyeColor color, BlockPos worldPosition, BlockState blockState) {
/*  57 */     super(BlockEntityType.SHULKER_BOX, worldPosition, blockState);
/*  58 */     this.color = color;
/*     */   }
/*     */   
/*     */   public ShulkerBoxBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  62 */     super(BlockEntityType.SHULKER_BOX, worldPosition, blockState);
/*  63 */     Block block = blockState.getBlock(); ShulkerBoxBlock shulkerBoxBlock = (ShulkerBoxBlock)block; this.color = (block instanceof ShulkerBoxBlock) ? shulkerBoxBlock.getColor() : null;
/*     */   }
/*     */   
/*     */   public enum AnimationStatus {
/*  67 */     CLOSED,
/*  68 */     OPENING,
/*  69 */     OPENED,
/*  70 */     CLOSING;
/*     */   }
/*     */ 
/*     */   
/*  74 */   public static void tick(Level level, BlockPos pos, BlockState state, ShulkerBoxBlockEntity entity) { entity.updateAnimation(level, pos, state); }
/*     */ 
/*     */   
/*     */   private void updateAnimation(Level level, BlockPos pos, BlockState blockState) {
/*  78 */     this.progressOld = this.progress;
/*  79 */     switch (this.animationStatus.ordinal()) { case 0:
/*  80 */         this.progress = 0.0F; break;
/*     */       case 1:
/*  82 */         this.progress += 0.1F;
/*  83 */         if (this.progressOld == 0.0F) {
/*  84 */           doNeighborUpdates(level, pos, blockState);
/*     */         }
/*  86 */         if (this.progress >= 1.0F) {
/*  87 */           this.animationStatus = AnimationStatus.OPENED;
/*  88 */           this.progress = 1.0F;
/*  89 */           doNeighborUpdates(level, pos, blockState);
/*     */         } 
/*  91 */         moveCollidedEntities(level, pos, blockState);
/*     */         break;
/*     */       case 3:
/*  94 */         this.progress -= 0.1F;
/*  95 */         if (this.progressOld == 1.0F) {
/*  96 */           doNeighborUpdates(level, pos, blockState);
/*     */         }
/*  98 */         if (this.progress <= 0.0F) {
/*  99 */           this.animationStatus = AnimationStatus.CLOSED;
/* 100 */           this.progress = 0.0F;
/* 101 */           doNeighborUpdates(level, pos, blockState);
/*     */         }  break;
/*     */       case 2:
/* 104 */         this.progress = 1.0F;
/*     */         break; }
/*     */   
/*     */   }
/*     */   
/* 109 */   public AnimationStatus getAnimationStatus() { return this.animationStatus; }
/*     */ 
/*     */   
/*     */   public AABB getBoundingBox(BlockState state) {
/* 113 */     Vec3 bottomCenter = new Vec3(0.5D, 0.0D, 0.5D);
/* 114 */     return Shulker.getProgressAabb(1.0F, (Direction)state.getValue(ShulkerBoxBlock.FACING), 0.5F * getProgress(1.0F), bottomCenter);
/*     */   }
/*     */   
/*     */   private void moveCollidedEntities(Level level, BlockPos pos, BlockState state) {
/* 118 */     if (!(state.getBlock() instanceof ShulkerBoxBlock)) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     Direction direction = (Direction)state.getValue(ShulkerBoxBlock.FACING);
/* 123 */     AABB aabb = Shulker.getProgressDeltaAabb(1.0F, direction, this.progressOld, this.progress, pos.getBottomCenter());
/*     */     
/* 125 */     List<Entity> entities = level.getEntities(null, aabb);
/* 126 */     if (entities.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     for (Entity entity : entities) {
/* 131 */       if (entity.getPistonPushReaction() == PushReaction.IGNORE) {
/*     */         continue;
/*     */       }
/*     */       
/* 135 */       entity.move(MoverType.SHULKER_BOX, new Vec3((aabb
/* 136 */             .getXsize() + 0.01D) * direction.getStepX(), (aabb
/* 137 */             .getYsize() + 0.01D) * direction.getStepY(), (aabb
/* 138 */             .getZsize() + 0.01D) * direction.getStepZ()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public int getContainerSize() { return this.itemStacks.size(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int b0, int b1) {
/* 150 */     if (b0 == 1) {
/* 151 */       this.openCount = b1;
/* 152 */       if (b1 == 0) {
/* 153 */         this.animationStatus = AnimationStatus.CLOSING;
/*     */       }
/* 155 */       if (b1 == 1) {
/* 156 */         this.animationStatus = AnimationStatus.OPENING;
/*     */       }
/* 158 */       return true;
/*     */     } 
/*     */     
/* 161 */     return super.triggerEvent(b0, b1);
/*     */   }
/*     */   
/*     */   private static void doNeighborUpdates(Level level, BlockPos pos, BlockState blockState) {
/* 165 */     blockState.updateNeighbourShapes(level, pos, 3);
/* 166 */     level.updateNeighborsAt(pos, blockState.getBlock());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void startOpen(ContainerUser containerUser) {
/* 177 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 178 */       if (this.openCount < 0) {
/* 179 */         this.openCount = 0;
/*     */       }
/* 181 */       this.openCount++;
/* 182 */       this.level.blockEvent(this.worldPosition, getBlockState().getBlock(), 1, this.openCount);
/* 183 */       if (this.openCount == 1) {
/* 184 */         this.level.gameEvent(containerUser.getLivingEntity(), GameEvent.CONTAINER_OPEN, this.worldPosition);
/* 185 */         this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser containerUser) {
/* 192 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 193 */       this.openCount--;
/* 194 */       this.level.blockEvent(this.worldPosition, getBlockState().getBlock(), 1, this.openCount);
/* 195 */       if (this.openCount <= 0) {
/* 196 */         this.level.gameEvent(containerUser.getLivingEntity(), GameEvent.CONTAINER_CLOSE, this.worldPosition);
/* 197 */         this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 204 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 209 */     super.loadAdditional(input);
/* 210 */     loadFromTag(input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 215 */     super.saveAdditional(output);
/*     */     
/* 217 */     if (!trySaveLootTable(output)) {
/* 218 */       ContainerHelper.saveAllItems(output, this.itemStacks, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadFromTag(ValueInput input) {
/* 223 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 224 */     if (!tryLoadLootTable(input)) {
/* 225 */       ContainerHelper.loadAllItems(input, this.itemStacks);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 231 */   protected NonNullList<ItemStack> getItems() { return this.itemStacks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   protected void setItems(NonNullList<ItemStack> items) { this.itemStacks = items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   public int[] getSlotsForFace(Direction direction) { return SLOTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 246 */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return !(Block.byItem(itemStack.getItem()) instanceof ShulkerBoxBlock); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 251 */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) { return true; }
/*     */ 
/*     */ 
/*     */   
/* 255 */   public float getProgress(float a) { return Mth.lerp(a, this.progressOld, this.progress); }
/*     */ 
/*     */ 
/*     */   
/* 259 */   public DyeColor getColor() { return this.color; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 264 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new ShulkerBoxMenu(containerId, inventory, this); }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public boolean isClosed() { return (this.animationStatus == AnimationStatus.CLOSED); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ShulkerBoxBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
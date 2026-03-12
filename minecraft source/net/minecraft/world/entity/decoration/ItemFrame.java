/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ItemFrame
/*     */   extends HangingEntity
/*     */ {
/*  46 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.ITEM_STACK);
/*  47 */   private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int NUM_ROTATIONS = 8;
/*     */   
/*     */   private static final float DEPTH = 0.0625F;
/*     */   private static final float WIDTH = 0.75F;
/*     */   private static final float HEIGHT = 0.75F;
/*     */   private static final byte DEFAULT_ROTATION = 0;
/*     */   private static final float DEFAULT_DROP_CHANCE = 1.0F;
/*     */   private static final boolean DEFAULT_INVISIBLE = false;
/*     */   private static final boolean DEFAULT_FIXED = false;
/*  58 */   private float dropChance = 1.0F;
/*     */   private boolean fixed = false;
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> type, Level level) {
/*  62 */     super(type, level);
/*  63 */     setInvisible(false);
/*     */   }
/*     */ 
/*     */   
/*  67 */   public ItemFrame(Level level, BlockPos pos, Direction direction) { this(EntityType.ITEM_FRAME, level, pos, direction); }
/*     */ 
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> type, Level level, BlockPos pos, Direction direction) {
/*  71 */     super(type, level, pos);
/*  72 */     setDirection(direction);
/*  73 */     setInvisible(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  78 */     super.defineSynchedData(entityData);
/*  79 */     entityData.define(DATA_ITEM, ItemStack.EMPTY);
/*  80 */     entityData.define(DATA_ROTATION, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDirection(Direction direction) {
/*  85 */     Objects.requireNonNull(direction);
/*     */     
/*  87 */     setDirectionRaw(direction);
/*  88 */     if (direction.getAxis().isHorizontal()) {
/*  89 */       setXRot(0.0F);
/*  90 */       setYRot((direction.get2DDataValue() * 90));
/*     */     } else {
/*  92 */       setXRot((-90 * direction.getAxisDirection().getStep()));
/*  93 */       setYRot(0.0F);
/*     */     } 
/*  95 */     this.xRotO = getXRot();
/*  96 */     this.yRotO = getYRot();
/*     */     
/*  98 */     recalculateBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void recalculateBoundingBox() {
/* 103 */     super.recalculateBoundingBox();
/* 104 */     syncPacketPositionCodec(getX(), getY(), getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 109 */   protected AABB calculateBoundingBox(BlockPos blockPos, Direction direction) { return createBoundingBox(blockPos, direction, hasFramedMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   protected AABB getPopBox() { return createBoundingBox(this.pos, getDirection(), false); }
/*     */ 
/*     */   
/*     */   private AABB createBoundingBox(BlockPos blockPos, Direction direction, boolean hasFramedMap) {
/* 118 */     float shiftToBlockWall = 0.46875F;
/* 119 */     Vec3 position = Vec3.atCenterOf(blockPos).relative(direction, -0.46875D);
/*     */     
/* 121 */     float width = hasFramedMap ? 1.0F : 0.75F;
/* 122 */     float height = hasFramedMap ? 1.0F : 0.75F;
/* 123 */     Direction.Axis axis = direction.getAxis();
/* 124 */     double xSize = (axis == Direction.Axis.X) ? 0.0625D : width;
/* 125 */     double ySize = (axis == Direction.Axis.Y) ? 0.0625D : height;
/* 126 */     double zSize = (axis == Direction.Axis.Z) ? 0.0625D : width;
/* 127 */     return AABB.ofSize(position, xSize, ySize, zSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean survives() {
/* 132 */     if (this.fixed) {
/* 133 */       return true;
/*     */     }
/*     */     
/* 136 */     if (hasLevelCollision(getPopBox())) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     BlockState state = level().getBlockState(this.pos.relative(getDirection().getOpposite()));
/* 141 */     if (!state.isSolid() && (!getDirection().getAxis().isHorizontal() || !DiodeBlock.isDiode(state))) {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     return canCoexist(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 150 */     if (!this.fixed) {
/* 151 */       super.move(moverType, delta);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 157 */     if (!this.fixed) {
/* 158 */       super.push(xa, ya, za);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 164 */     removeFramedMap(getItem());
/* 165 */     super.kill(level);
/*     */   }
/*     */ 
/*     */   
/* 169 */   private boolean shouldDamageDropItem(DamageSource source) { return (!source.is(DamageTypeTags.IS_EXPLOSION) && !getItem().isEmpty()); }
/*     */ 
/*     */ 
/*     */   
/* 173 */   private static boolean canHurtWhenFixed(DamageSource source) { return (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.isCreativePlayer()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource source) {
/* 178 */     if (this.fixed && !canHurtWhenFixed(source)) {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     return !isInvulnerableToBase(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 187 */     if (this.fixed) {
/* 188 */       return (canHurtWhenFixed(source) && super.hurtServer(level, source, damage));
/*     */     }
/*     */     
/* 191 */     if (isInvulnerableToBase(source)) {
/* 192 */       return false;
/*     */     }
/*     */     
/* 195 */     if (shouldDamageDropItem(source)) {
/* 196 */       dropItem(level, source.getEntity(), false);
/* 197 */       gameEvent(GameEvent.BLOCK_CHANGE, source.getEntity());
/* 198 */       playSound(getRemoveItemSound(), 1.0F, 1.0F);
/* 199 */       return true;
/*     */     } 
/* 201 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/* 205 */   public SoundEvent getRemoveItemSound() { return SoundEvents.ITEM_FRAME_REMOVE_ITEM; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 210 */     double size = 16.0D;
/* 211 */     size *= 64.0D * getViewScale();
/* 212 */     return (distance < size * size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(ServerLevel level, Entity causedBy) {
/* 217 */     playSound(getBreakSound(), 1.0F, 1.0F);
/* 218 */     dropItem(level, causedBy, true);
/* 219 */     gameEvent(GameEvent.BLOCK_CHANGE, causedBy);
/*     */   }
/*     */ 
/*     */   
/* 223 */   public SoundEvent getBreakSound() { return SoundEvents.ITEM_FRAME_BREAK; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public void playPlacementSound() { playSound(getPlaceSound(), 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/* 232 */   public SoundEvent getPlaceSound() { return SoundEvents.ITEM_FRAME_PLACE; }
/*     */ 
/*     */   
/*     */   private void dropItem(ServerLevel level, Entity causedBy, boolean withFrame) {
/* 236 */     if (this.fixed) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     ItemStack itemStack = getItem();
/* 241 */     setItem(ItemStack.EMPTY);
/*     */     
/* 243 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 244 */       if (causedBy == null) {
/* 245 */         removeFramedMap(itemStack);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 250 */     if (causedBy instanceof Player) { Player player = (Player)causedBy;
/* 251 */       if (player.hasInfiniteMaterials()) {
/* 252 */         removeFramedMap(itemStack);
/*     */         
/*     */         return;
/*     */       }  }
/*     */     
/* 257 */     if (withFrame) {
/* 258 */       spawnAtLocation(level, getFrameItemStack());
/*     */     }
/* 260 */     if (!itemStack.isEmpty()) {
/* 261 */       itemStack = itemStack.copy();
/* 262 */       removeFramedMap(itemStack);
/* 263 */       if (this.random.nextFloat() < this.dropChance) {
/* 264 */         spawnAtLocation(level, itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFramedMap(ItemStack itemStack) {
/* 270 */     MapId mapId = getFramedMapId(itemStack);
/* 271 */     if (mapId != null) {
/* 272 */       MapItemSavedData mapItemSavedData = MapItem.getSavedData(mapId, level());
/* 273 */       if (mapItemSavedData != null) {
/* 274 */         mapItemSavedData.removedFromFrame(this.pos, getId());
/*     */       }
/*     */     } 
/* 277 */     itemStack.setEntityRepresentation(null);
/*     */   }
/*     */ 
/*     */   
/* 281 */   public ItemStack getItem() { return (ItemStack)getEntityData().get(DATA_ITEM); }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public MapId getFramedMapId(ItemStack itemStack) { return (MapId)itemStack.get(DataComponents.MAP_ID); }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public boolean hasFramedMap() { return getItem().has(DataComponents.MAP_ID); }
/*     */ 
/*     */ 
/*     */   
/* 293 */   public void setItem(ItemStack itemStack) { setItem(itemStack, true); }
/*     */ 
/*     */   
/*     */   public void setItem(ItemStack itemStack, boolean updateNeighbours) {
/* 297 */     if (!itemStack.isEmpty()) {
/* 298 */       itemStack = itemStack.copyWithCount(1);
/*     */     }
/*     */     
/* 301 */     onItemChanged(itemStack);
/* 302 */     getEntityData().set(DATA_ITEM, itemStack);
/* 303 */     if (!itemStack.isEmpty()) {
/* 304 */       playSound(getAddItemSound(), 1.0F, 1.0F);
/*     */     }
/*     */     
/* 307 */     if (updateNeighbours && this.pos != null) {
/* 308 */       level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 313 */   public SoundEvent getAddItemSound() { return SoundEvents.ITEM_FRAME_ADD_ITEM; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 318 */     if (slot == 0) {
/* 319 */       return SlotAccess.of(this::getItem, this::setItem);
/*     */     }
/* 321 */     return super.getSlot(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 326 */     super.onSyncedDataUpdated(accessor);
/* 327 */     if (accessor.equals(DATA_ITEM)) {
/* 328 */       onItemChanged(getItem());
/*     */     }
/*     */   }
/*     */   
/*     */   private void onItemChanged(ItemStack item) {
/* 333 */     if (!item.isEmpty() && item.getFrame() != this) {
/* 334 */       item.setEntityRepresentation(this);
/*     */     }
/* 336 */     recalculateBoundingBox();
/*     */   }
/*     */ 
/*     */   
/* 340 */   public int getRotation() { return ((Integer)getEntityData().get(DATA_ROTATION)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public void setRotation(int rotation) { setRotation(rotation, true); }
/*     */ 
/*     */   
/*     */   private void setRotation(int rotation, boolean updateNeighbours) {
/* 348 */     getEntityData().set(DATA_ROTATION, Integer.valueOf(rotation % 8));
/*     */     
/* 350 */     if (updateNeighbours && this.pos != null) {
/* 351 */       level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 357 */     super.addAdditionalSaveData(output);
/*     */     
/* 359 */     ItemStack currentItem = getItem();
/* 360 */     if (!currentItem.isEmpty()) {
/* 361 */       output.store("Item", ItemStack.CODEC, currentItem);
/*     */     }
/*     */     
/* 364 */     output.putByte("ItemRotation", (byte)getRotation());
/* 365 */     output.putFloat("ItemDropChance", this.dropChance);
/* 366 */     output.store("Facing", Direction.LEGACY_ID_CODEC, getDirection());
/* 367 */     output.putBoolean("Invisible", isInvisible());
/* 368 */     output.putBoolean("Fixed", this.fixed);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 373 */     super.readAdditionalSaveData(input);
/* 374 */     ItemStack itemStack = (ItemStack)input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*     */ 
/*     */     
/* 377 */     ItemStack currentItem = getItem();
/* 378 */     if (!currentItem.isEmpty() && 
/* 379 */       !ItemStack.matches(itemStack, currentItem)) {
/* 380 */       removeFramedMap(currentItem);
/*     */     }
/*     */ 
/*     */     
/* 384 */     setItem(itemStack, false);
/*     */     
/* 386 */     setRotation(input.getByteOr("ItemRotation", (byte)0), false);
/* 387 */     this.dropChance = input.getFloatOr("ItemDropChance", 1.0F);
/* 388 */     setDirection((Direction)input.read("Facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN));
/* 389 */     setInvisible(input.getBooleanOr("Invisible", false));
/* 390 */     this.fixed = input.getBooleanOr("Fixed", false);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 395 */     ItemStack itemStack = player.getItemInHand(hand);
/* 396 */     boolean frameHasItem = !getItem().isEmpty();
/* 397 */     boolean hasHeldItem = !itemStack.isEmpty();
/*     */ 
/*     */     
/* 400 */     if (this.fixed) {
/* 401 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 404 */     if (player.level().isClientSide()) {
/* 405 */       return (frameHasItem || hasHeldItem) ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*     */     }
/*     */     
/* 408 */     if (!frameHasItem) {
/* 409 */       if (hasHeldItem && !isRemoved()) {
/* 410 */         MapItemSavedData data = MapItem.getSavedData(itemStack, level());
/* 411 */         if (data != null && data.isTrackedCountOverLimit(256)) {
/* 412 */           return InteractionResult.FAIL;
/*     */         }
/* 414 */         setItem(itemStack);
/* 415 */         gameEvent(GameEvent.BLOCK_CHANGE, player);
/* 416 */         itemStack.consume(1, player);
/* 417 */         return InteractionResult.SUCCESS;
/*     */       } 
/* 419 */       return InteractionResult.PASS;
/*     */     } 
/*     */     
/* 422 */     playSound(getRotateItemSound(), 1.0F, 1.0F);
/* 423 */     setRotation(getRotation() + 1);
/* 424 */     gameEvent(GameEvent.BLOCK_CHANGE, player);
/* 425 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 430 */   public SoundEvent getRotateItemSound() { return SoundEvents.ITEM_FRAME_ROTATE_ITEM; }
/*     */ 
/*     */   
/*     */   public int getAnalogOutput() {
/* 434 */     if (getItem().isEmpty()) {
/* 435 */       return 0;
/*     */     }
/*     */     
/* 438 */     return getRotation() % 8 + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 443 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { return new ClientboundAddEntityPacket(this, getDirection().get3DDataValue(), getPos()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 448 */     super.recreateFromPacket(packet);
/* 449 */     setDirection(Direction.from3DDataValue(packet.getData()));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 454 */     ItemStack framedStack = getItem();
/* 455 */     if (framedStack.isEmpty()) {
/* 456 */       return getFrameItemStack();
/*     */     }
/* 458 */     return framedStack.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 463 */   protected ItemStack getFrameItemStack() { return new ItemStack(Items.ITEM_FRAME); }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getVisualRotationYInDegrees() {
/* 468 */     Direction frameDirection = getDirection();
/* 469 */     int rotationCorrection = frameDirection.getAxis().isVertical() ? (90 * frameDirection.getAxisDirection().getStep()) : 0;
/* 470 */     return Mth.wrapDegrees(180 + frameDirection.get2DDataValue() * 90 + getRotation() * 45 + rotationCorrection);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\ItemFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
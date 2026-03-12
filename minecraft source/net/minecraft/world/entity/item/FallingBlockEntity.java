/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.DirectionalPlaceContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Fallable;
/*     */ import net.minecraft.world.level.block.FallingBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class FallingBlockEntity
/*     */   extends Entity
/*     */ {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  61 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.SAND.defaultBlockState();
/*     */   
/*     */   private static final int DEFAULT_TIME = 0;
/*     */   private static final float DEFAULT_FALL_DAMAGE_PER_DISTANCE = 0.0F;
/*     */   private static final int DEFAULT_MAX_FALL_DAMAGE = 40;
/*     */   private static final boolean DEFAULT_DROP_ITEM = true;
/*     */   private static final boolean DEFAULT_CANCEL_DROP = false;
/*  68 */   private BlockState blockState = DEFAULT_BLOCK_STATE;
/*  69 */   public int time = 0;
/*     */   public boolean dropItem = true;
/*     */   private boolean cancelDrop = false;
/*     */   private boolean hurtEntities;
/*  73 */   private int fallDamageMax = 40;
/*  74 */   private float fallDamagePerDistance = 0.0F;
/*     */   
/*     */   public CompoundTag blockData;
/*     */   public boolean forceTickAfterTeleportToDuplicate;
/*  78 */   protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
/*     */ 
/*     */   
/*  81 */   public FallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   private FallingBlockEntity(Level level, double x, double y, double z, BlockState blockState) {
/*  85 */     this(EntityType.FALLING_BLOCK, level);
/*  86 */     this.blockState = blockState;
/*  87 */     this.blocksBuilding = true;
/*     */     
/*  89 */     setPos(x, y, z);
/*     */     
/*  91 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/*  93 */     this.xo = x;
/*  94 */     this.yo = y;
/*  95 */     this.zo = z;
/*     */     
/*  97 */     setStartPos(blockPosition());
/*     */   }
/*     */   
/*     */   public static FallingBlockEntity fall(Level level, BlockPos pos, BlockState state) {
/* 101 */     FallingBlockEntity entity = new FallingBlockEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state.hasProperty(BlockStateProperties.WATERLOGGED) ? (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : state);
/*     */     
/* 103 */     level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
/* 104 */     level.addFreshEntity(entity);
/* 105 */     return entity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public boolean isAttackable() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 115 */     if (!isInvulnerableToBase(source)) {
/* 116 */       markHurt();
/*     */     }
/*     */     
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 123 */   public void setStartPos(BlockPos pos) { this.entityData.set(DATA_START_POS, pos); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public BlockPos getStartPos() { return (BlockPos)this.entityData.get(DATA_START_POS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_START_POS, BlockPos.ZERO); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public boolean isPickable() { return !isRemoved(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   protected double getDefaultGravity() { return 0.04D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 152 */     if (this.blockState.isAir()) {
/* 153 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/* 157 */     Block block = this.blockState.getBlock();
/* 158 */     this.time++;
/*     */     
/* 160 */     applyGravity();
/* 161 */     move(MoverType.SELF, getDeltaMovement());
/* 162 */     applyEffectsFromBlocks();
/* 163 */     handlePortal();
/*     */     
/* 165 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (isAlive() || this.forceTickAfterTeleportToDuplicate) {
/* 166 */         BlockPos pos = blockPosition();
/*     */         
/* 168 */         boolean isConcrete = this.blockState.getBlock() instanceof net.minecraft.world.level.block.ConcretePowderBlock;
/* 169 */         boolean isStuckInWater = (isConcrete && level().getFluidState(pos).is(FluidTags.WATER));
/* 170 */         double moveVec = getDeltaMovement().lengthSqr();
/*     */         
/* 172 */         if (isConcrete && moveVec > 1.0D) {
/*     */ 
/*     */           
/* 175 */           BlockHitResult clip = level().clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
/* 176 */           if (clip.getType() != HitResult.Type.MISS && level().getFluidState(clip.getBlockPos()).is(FluidTags.WATER)) {
/*     */             
/* 178 */             pos = clip.getBlockPos();
/* 179 */             isStuckInWater = true;
/*     */           } 
/*     */         } 
/*     */         
/* 183 */         if (onGround() || isStuckInWater) {
/* 184 */           BlockState currentState = level().getBlockState(pos);
/*     */ 
/*     */           
/* 187 */           setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/*     */           
/* 189 */           if (!currentState.is(Blocks.MOVING_PISTON)) {
/* 190 */             if (!this.cancelDrop) {
/* 191 */               boolean mayReplace = currentState.canBeReplaced(new DirectionalPlaceContext(level(), pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
/*     */               
/* 193 */               boolean wouldContinueFalling = (FallingBlock.isFree(level().getBlockState(pos.below())) && (!isConcrete || !isStuckInWater));
/* 194 */               boolean wouldSurvive = (this.blockState.canSurvive(level(), pos) && !wouldContinueFalling);
/* 195 */               if (mayReplace && wouldSurvive) {
/* 196 */                 if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && level().getFluidState(pos).getType() == Fluids.WATER) {
/* 197 */                   this.blockState = (BlockState)this.blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */                 }
/* 199 */                 if (level().setBlock(pos, this.blockState, 3)) {
/*     */                   
/* 201 */                   (serverLevel.getChunkSource()).chunkMap.sendToTrackingPlayers(this, new ClientboundBlockUpdatePacket(pos, level().getBlockState(pos)));
/* 202 */                   discard();
/* 203 */                   if (block instanceof Fallable) { Fallable fallable = (Fallable)block;
/* 204 */                     fallable.onLand(level(), pos, this.blockState, currentState, this); }
/*     */                   
/* 206 */                   if (this.blockData != null && this.blockState.hasBlockEntity()) {
/* 207 */                     BlockEntity blockEntity = level().getBlockEntity(pos);
/*     */                     
/* 209 */                     if (blockEntity != null) {
/*     */                       try {
/* 211 */                         ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER); 
/* 212 */                         try { RegistryAccess registryAccess = level().registryAccess();
/*     */                           
/* 214 */                           TagValueOutput output = TagValueOutput.createWithContext(reporter, registryAccess);
/* 215 */                           blockEntity.saveWithoutMetadata(output);
/* 216 */                           CompoundTag merged = output.buildResult();
/*     */                           
/* 218 */                           this.blockData.forEach((name, tag) -> merged.put(name, tag.copy()));
/*     */                           
/* 220 */                           blockEntity.loadWithComponents(TagValueInput.create(reporter, registryAccess, merged));
/* 221 */                           reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/* 222 */                       } catch (Exception e) {
/* 223 */                         LOGGER.error("Failed to load block entity from falling block", e);
/*     */                       } 
/* 225 */                       blockEntity.setChanged();
/*     */                     } 
/*     */                   } 
/* 228 */                 } else if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 229 */                   discard();
/* 230 */                   callOnBrokenAfterFall(block, pos);
/* 231 */                   spawnAtLocation(serverLevel, block);
/*     */                 } 
/*     */               } else {
/* 234 */                 discard();
/* 235 */                 if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 236 */                   callOnBrokenAfterFall(block, pos);
/* 237 */                   spawnAtLocation(serverLevel, block);
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 241 */               discard();
/* 242 */               callOnBrokenAfterFall(block, pos);
/*     */             } 
/*     */           }
/* 245 */         } else if ((this.time > 100 && (pos.getY() <= level().getMinY() || pos.getY() > level().getMaxY())) || this.time > 600) {
/* 246 */           if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 247 */             spawnAtLocation(serverLevel, block);
/*     */           }
/* 249 */           discard();
/*     */         } 
/*     */       }  }
/* 252 */      setDeltaMovement(getDeltaMovement().scale(0.98D));
/*     */   }
/*     */   
/*     */   public void callOnBrokenAfterFall(Block block, BlockPos pos) {
/* 256 */     if (block instanceof Fallable) {
/* 257 */       ((Fallable)block).onBrokenAfterFall(level(), pos, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 263 */     if (!this.hurtEntities) {
/* 264 */       return false;
/*     */     }
/*     */     
/* 267 */     int fallDistanceInt = Mth.ceil(fallDistance - 1.0D);
/* 268 */     if (fallDistanceInt < 0) {
/* 269 */       return false;
/*     */     }
/*     */     
/* 272 */     Predicate<Entity> entitySelector = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
/* 273 */     Block block = this.blockState.getBlock(); Fallable fallable = (Fallable)block;
/*     */     
/* 275 */     DamageSource actualDamageSource = (block instanceof Fallable) ? fallable.getFallDamageSource(this) : damageSources().fallingBlock(this);
/*     */     
/* 277 */     float damage = Math.min(Mth.floor(fallDistanceInt * this.fallDamagePerDistance), this.fallDamageMax);
/* 278 */     level().getEntities(this, getBoundingBox(), entitySelector).forEach(entity -> 
/* 279 */         entity.hurt(actualDamageSource, damage));
/*     */ 
/*     */     
/* 282 */     boolean isAnvil = this.blockState.is(BlockTags.ANVIL);
/* 283 */     if (isAnvil && damage > 0.0F && this.random.nextFloat() < 0.05F + fallDistanceInt * 0.05F) {
/* 284 */       BlockState newBlockState = AnvilBlock.damage(this.blockState);
/* 285 */       if (newBlockState == null) {
/* 286 */         this.cancelDrop = true;
/*     */       } else {
/* 288 */         this.blockState = newBlockState;
/*     */       } 
/*     */     } 
/* 291 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 296 */     output.store("BlockState", BlockState.CODEC, this.blockState);
/* 297 */     output.putInt("Time", this.time);
/* 298 */     output.putBoolean("DropItem", this.dropItem);
/* 299 */     output.putBoolean("HurtEntities", this.hurtEntities);
/* 300 */     output.putFloat("FallHurtAmount", this.fallDamagePerDistance);
/* 301 */     output.putInt("FallHurtMax", this.fallDamageMax);
/* 302 */     if (this.blockData != null) {
/* 303 */       output.store("TileEntityData", CompoundTag.CODEC, this.blockData);
/*     */     }
/* 305 */     output.putBoolean("CancelDrop", this.cancelDrop);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 310 */     this.blockState = (BlockState)input.read("BlockState", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE);
/*     */     
/* 312 */     this.time = input.getIntOr("Time", 0);
/*     */     
/* 314 */     boolean defaultHurtEntities = this.blockState.is(BlockTags.ANVIL);
/* 315 */     this.hurtEntities = input.getBooleanOr("HurtEntities", defaultHurtEntities);
/* 316 */     this.fallDamagePerDistance = input.getFloatOr("FallHurtAmount", 0.0F);
/* 317 */     this.fallDamageMax = input.getIntOr("FallHurtMax", 40);
/*     */     
/* 319 */     this.dropItem = input.getBooleanOr("DropItem", true);
/*     */     
/* 321 */     this.blockData = (CompoundTag)input.read("TileEntityData", CompoundTag.CODEC).orElse(null);
/*     */     
/* 323 */     this.cancelDrop = input.getBooleanOr("CancelDrop", false);
/*     */   }
/*     */   
/*     */   public void setHurtsEntities(float damagePerDistance, int damageMax) {
/* 327 */     this.hurtEntities = true;
/* 328 */     this.fallDamagePerDistance = damagePerDistance;
/* 329 */     this.fallDamageMax = damageMax;
/*     */   }
/*     */ 
/*     */   
/* 333 */   public void disableDrop() { this.cancelDrop = true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public boolean displayFireAnimation() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 343 */     super.fillCrashReportCategory(category);
/* 344 */     category.setDetail("Immitating BlockState", this.blockState.toString());
/*     */   }
/*     */ 
/*     */   
/* 348 */   public BlockState getBlockState() { return this.blockState; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   protected Component getTypeName() { return Component.translatable("entity.minecraft.falling_block_type", new Object[] { this.blockState.getBlock().getName() }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 358 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { return new ClientboundAddEntityPacket(this, serverEntity, Block.getId(getBlockState())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 363 */     super.recreateFromPacket(packet);
/* 364 */     this.blockState = Block.stateById(packet.getData());
/* 365 */     this.blocksBuilding = true;
/*     */     
/* 367 */     double x = packet.getX();
/* 368 */     double y = packet.getY();
/* 369 */     double z = packet.getZ();
/*     */     
/* 371 */     setPos(x, y, z);
/* 372 */     setStartPos(blockPosition());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 381 */     ResourceKey<Level> newDimension = transition.newLevel().dimension();
/* 382 */     ResourceKey<Level> oldDimension = level().dimension();
/* 383 */     boolean fromOrToEnd = ((oldDimension == Level.END || newDimension == Level.END) && oldDimension != newDimension);
/*     */     
/* 385 */     Entity newEntity = super.teleport(transition);
/* 386 */     this.forceTickAfterTeleportToDuplicate = (newEntity != null && fromOrToEnd);
/* 387 */     return newEntity;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\item\FallingBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
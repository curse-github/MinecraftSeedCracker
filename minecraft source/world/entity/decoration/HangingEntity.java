/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public abstract class HangingEntity
/*     */   extends BlockAttachedEntity
/*     */ {
/*  28 */   private static final EntityDataAccessor<Direction> DATA_DIRECTION = SynchedEntityData.defineId(HangingEntity.class, EntityDataSerializers.DIRECTION);
/*  29 */   private static final Direction DEFAULT_DIRECTION = Direction.SOUTH;
/*     */ 
/*     */   
/*  32 */   protected HangingEntity(EntityType<? extends HangingEntity> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   protected HangingEntity(EntityType<? extends HangingEntity> type, Level level, BlockPos pos) {
/*  36 */     this(type, level);
/*  37 */     this.pos = pos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_DIRECTION, DEFAULT_DIRECTION); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  47 */     super.onSyncedDataUpdated(accessor);
/*  48 */     if (accessor.equals(DATA_DIRECTION)) {
/*  49 */       setDirection(getDirection());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public Direction getDirection() { return (Direction)this.entityData.get(DATA_DIRECTION); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected void setDirectionRaw(Direction direction) { this.entityData.set(DATA_DIRECTION, direction); }
/*     */ 
/*     */   
/*     */   protected void setDirection(Direction direction) {
/*  63 */     Objects.requireNonNull(direction);
/*  64 */     Validate.isTrue(direction.getAxis().isHorizontal());
/*     */     
/*  66 */     setDirectionRaw(direction);
/*  67 */     setYRot((direction.get2DDataValue() * 90));
/*  68 */     this.yRotO = getYRot();
/*     */     
/*  70 */     recalculateBoundingBox();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  76 */     if (getDirection() == null) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     AABB aabb = calculateBoundingBox(this.pos, getDirection());
/*  81 */     Vec3 center = aabb.getCenter();
/*  82 */     setPosRaw(center.x, center.y, center.z);
/*  83 */     setBoundingBox(aabb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean survives() {
/*  90 */     if (hasLevelCollision(getPopBox())) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     boolean isSupported = BlockPos.betweenClosedStream(calculateSupportBox()).allMatch(pos -> {
/*  95 */           BlockState state = level().getBlockState(pos);
/*     */ 
/*     */           
/*  98 */           return (state.isSolid() || DiodeBlock.isDiode(state));
/*     */         });
/*     */     
/* 101 */     return (isSupported && canCoexist(false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected AABB calculateSupportBox() { return getBoundingBox().move(getDirection().step().mul(-0.5F)).deflate(1.0E-7D); }
/*     */ 
/*     */   
/*     */   protected boolean canCoexist(boolean allowIntersectingSameType) {
/* 111 */     Predicate<HangingEntity> nonIntersectable = hangingEntity -> {
/* 112 */         boolean intersectsSameType = (!allowIntersectingSameType && hangingEntity.getType() == getType());
/* 113 */         boolean isSameDirection = (hangingEntity.getDirection() == getDirection());
/* 114 */         return (hangingEntity != this && (intersectsSameType || isSameDirection));
/*     */       };
/* 116 */     return !level().hasEntities(EntityTypeTest.forClass(HangingEntity.class), getPopBox(), nonIntersectable);
/*     */   }
/*     */   
/*     */   protected boolean hasLevelCollision(AABB popBox) {
/* 120 */     Level level = level();
/* 121 */     return (!level.noBlockCollision(this, popBox) || !level.noBorderCollision(this, popBox));
/*     */   }
/*     */ 
/*     */   
/* 125 */   protected AABB getPopBox() { return getBoundingBox(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack, float yOffs) {
/* 132 */     ItemEntity entity = new ItemEntity(level(), getX() + (getDirection().getStepX() * 0.15F), getY() + yOffs, getZ() + (getDirection().getStepZ() * 0.15F), itemStack);
/* 133 */     entity.setDefaultPickUpDelay();
/* 134 */     level().addFreshEntity(entity);
/* 135 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public float rotate(Rotation rotation) {
/* 140 */     Direction direction = getDirection();
/* 141 */     if (direction.getAxis() != Direction.Axis.Y) {
/* 142 */       switch (rotation) { case CLOCKWISE_180:
/* 143 */           direction = direction.getOpposite(); break;
/* 144 */         case COUNTERCLOCKWISE_90: direction = direction.getCounterClockWise(); break;
/* 145 */         case CLOCKWISE_90: direction = direction.getClockWise();
/*     */           break; }
/*     */ 
/*     */       
/* 149 */       setDirection(direction);
/*     */     } 
/*     */     
/* 152 */     float angle = Mth.wrapDegrees(getYRot());
/* 153 */     switch (rotation) { case CLOCKWISE_180: case COUNTERCLOCKWISE_90: case CLOCKWISE_90:  }  return 
/*     */ 
/*     */ 
/*     */       
/* 157 */       angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public float mirror(Mirror mirror) { return rotate(mirror.getRotation(getDirection())); }
/*     */   
/*     */   protected abstract AABB calculateBoundingBox(BlockPos paramBlockPos, Direction paramDirection);
/*     */   
/*     */   public abstract void playPlacementSound();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\HangingEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
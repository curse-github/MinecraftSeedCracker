/*     */ package net.minecraft.core;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Rotation;
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
/*     */ public class MutableBlockPos
/*     */   extends BlockPos
/*     */ {
/* 301 */   public MutableBlockPos() { this(0, 0, 0); }
/*     */ 
/*     */ 
/*     */   
/* 305 */   public MutableBlockPos(int x, int y, int z) { super(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/* 309 */   public MutableBlockPos(double x, double y, double z) { this(Mth.floor(x), Mth.floor(y), Mth.floor(z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 314 */   public BlockPos offset(int x, int y, int z) { return super.offset(x, y, z).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 319 */   public BlockPos multiply(int scale) { return super.multiply(scale).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 324 */   public BlockPos relative(Direction direction, int steps) { return super.relative(direction, steps).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 329 */   public BlockPos relative(Direction.Axis axis, int steps) { return super.relative(axis, steps).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 334 */   public BlockPos rotate(Rotation rotation) { return super.rotate(rotation).immutable(); }
/*     */ 
/*     */   
/*     */   public MutableBlockPos set(int x, int y, int z) {
/* 338 */     setX(x);
/* 339 */     setY(y);
/* 340 */     setZ(z);
/* 341 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 345 */   public MutableBlockPos set(double x, double y, double z) { return set(Mth.floor(x), Mth.floor(y), Mth.floor(z)); }
/*     */ 
/*     */ 
/*     */   
/* 349 */   public MutableBlockPos set(Vec3i vec) { return set(vec.getX(), vec.getY(), vec.getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 353 */   public MutableBlockPos set(long pos) { return set(getX(pos), getY(pos), getZ(pos)); }
/*     */ 
/*     */   
/*     */   public MutableBlockPos set(AxisCycle transform, int x, int y, int z) {
/* 357 */     return set(transform
/* 358 */         .cycle(x, y, z, Direction.Axis.X), transform
/* 359 */         .cycle(x, y, z, Direction.Axis.Y), transform
/* 360 */         .cycle(x, y, z, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 365 */   public MutableBlockPos setWithOffset(Vec3i pos, Direction direction) { return set(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ()); }
/*     */ 
/*     */ 
/*     */   
/* 369 */   public MutableBlockPos setWithOffset(Vec3i pos, int x, int y, int z) { return set(pos.getX() + x, pos.getY() + y, pos.getZ() + z); }
/*     */ 
/*     */ 
/*     */   
/* 373 */   public MutableBlockPos setWithOffset(Vec3i pos, Vec3i offset) { return set(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 377 */   public MutableBlockPos move(Direction direction) { return move(direction, 1); }
/*     */ 
/*     */ 
/*     */   
/* 381 */   public MutableBlockPos move(Direction direction, int steps) { return set(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps); }
/*     */ 
/*     */ 
/*     */   
/* 385 */   public MutableBlockPos move(int x, int y, int z) { return set(getX() + x, getY() + y, getZ() + z); }
/*     */ 
/*     */ 
/*     */   
/* 389 */   public MutableBlockPos move(Vec3i pos) { return set(getX() + pos.getX(), getY() + pos.getY(), getZ() + pos.getZ()); }
/*     */ 
/*     */   
/*     */   public MutableBlockPos clamp(Direction.Axis axis, int minimum, int maximum) {
/* 393 */     switch (BlockPos.null.$SwitchMap$net$minecraft$core$Direction$Axis[axis.ordinal()]) { default: throw new MatchException(null, null);case 1: case 2: case 3: break; }  return 
/*     */ 
/*     */       
/* 396 */       set(getX(), getY(), Mth.clamp(getZ(), minimum, maximum));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableBlockPos setX(int x) {
/* 402 */     super.setX(x);
/* 403 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableBlockPos setY(int y) {
/* 408 */     super.setY(y);
/* 409 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableBlockPos setZ(int z) {
/* 414 */     super.setZ(z);
/* 415 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 420 */   public BlockPos immutable() { return new BlockPos(this); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\BlockPos$MutableBlockPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
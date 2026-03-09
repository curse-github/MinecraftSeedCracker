/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Position
/*     */ {
/*     */   private double x;
/*     */   private double z;
/*     */   
/*     */   double dist(Position target) {
/* 252 */     double dx = this.x - target.x;
/* 253 */     double dz = this.z - target.z;
/*     */     
/* 255 */     return Math.sqrt(dx * dx + dz * dz);
/*     */   }
/*     */   
/*     */   void normalize() {
/* 259 */     double dist = getLength();
/* 260 */     this.x /= dist;
/* 261 */     this.z /= dist;
/*     */   }
/*     */ 
/*     */   
/* 265 */   double getLength() { return Math.sqrt(this.x * this.x + this.z * this.z); }
/*     */ 
/*     */   
/*     */   public void moveAway(Position pos) {
/* 269 */     this.x -= pos.x;
/* 270 */     this.z -= pos.z;
/*     */   }
/*     */   
/*     */   public boolean clamp(double minX, double minZ, double maxX, double maxZ) {
/* 274 */     boolean changed = false;
/*     */     
/* 276 */     if (this.x < minX) {
/* 277 */       this.x = minX;
/* 278 */       changed = true;
/* 279 */     } else if (this.x > maxX) {
/* 280 */       this.x = maxX;
/* 281 */       changed = true;
/*     */     } 
/*     */     
/* 284 */     if (this.z < minZ) {
/* 285 */       this.z = minZ;
/* 286 */       changed = true;
/* 287 */     } else if (this.z > maxZ) {
/* 288 */       this.z = maxZ;
/* 289 */       changed = true;
/*     */     } 
/*     */     
/* 292 */     return changed;
/*     */   }
/*     */   
/*     */   public int getSpawnY(BlockGetter level, int maxHeight) {
/* 296 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.x, (maxHeight + 1), this.z);
/* 297 */     boolean air2Above = level.getBlockState(pos).isAir();
/* 298 */     pos.move(Direction.DOWN);
/* 299 */     boolean air1Above = level.getBlockState(pos).isAir();
/* 300 */     while (pos.getY() > level.getMinY()) {
/* 301 */       pos.move(Direction.DOWN);
/* 302 */       boolean currentIsAir = level.getBlockState(pos).isAir();
/*     */       
/* 304 */       if (!currentIsAir && air1Above && air2Above) {
/* 305 */         return pos.getY() + 1;
/*     */       }
/* 307 */       air2Above = air1Above;
/* 308 */       air1Above = currentIsAir;
/*     */     } 
/*     */     
/* 311 */     return maxHeight + 1;
/*     */   }
/*     */   
/*     */   public boolean isSafe(BlockGetter level, int maxHeight) {
/* 315 */     BlockPos pos = BlockPos.containing(this.x, (getSpawnY(level, maxHeight) - 1), this.z);
/* 316 */     BlockState state = level.getBlockState(pos);
/* 317 */     return (pos.getY() < maxHeight && !state.liquid() && !state.is(BlockTags.FIRE));
/*     */   }
/*     */   
/*     */   public void randomize(RandomSource random, double minX, double minZ, double maxX, double maxZ) {
/* 321 */     this.x = Mth.nextDouble(random, minX, maxX);
/* 322 */     this.z = Mth.nextDouble(random, minZ, maxZ);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SpreadPlayersCommand$Position.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
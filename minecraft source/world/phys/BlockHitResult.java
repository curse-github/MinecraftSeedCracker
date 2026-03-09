/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class BlockHitResult
/*    */   extends HitResult {
/*    */   private final Direction direction;
/*    */   private final BlockPos blockPos;
/*    */   private final boolean miss;
/*    */   private final boolean inside;
/*    */   private final boolean worldBorderHit;
/*    */   
/* 14 */   public static BlockHitResult miss(Vec3 location, Direction direction, BlockPos pos) { return new BlockHitResult(true, location, direction, pos, false, false); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public BlockHitResult(Vec3 location, Direction direction, BlockPos pos, boolean inside) { this(false, location, direction, pos, inside, false); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public BlockHitResult(Vec3 location, Direction direction, BlockPos pos, boolean inside, boolean worldBorderHit) { this(false, location, direction, pos, inside, worldBorderHit); }
/*    */ 
/*    */   
/*    */   private BlockHitResult(boolean miss, Vec3 location, Direction direction, BlockPos blockPos, boolean inside, boolean worldBorderHit) {
/* 26 */     super(location);
/* 27 */     this.miss = miss;
/* 28 */     this.direction = direction;
/* 29 */     this.blockPos = blockPos;
/* 30 */     this.inside = inside;
/* 31 */     this.worldBorderHit = worldBorderHit;
/*    */   }
/*    */ 
/*    */   
/* 35 */   public BlockHitResult withDirection(Direction direction) { return new BlockHitResult(this.miss, this.location, direction, this.blockPos, this.inside, this.worldBorderHit); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public BlockHitResult withPosition(BlockPos blockPos) { return new BlockHitResult(this.miss, this.location, this.direction, blockPos, this.inside, this.worldBorderHit); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public BlockHitResult hitBorder() { return new BlockHitResult(this.miss, this.location, this.direction, this.blockPos, this.inside, true); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public BlockPos getBlockPos() { return this.blockPos; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Direction getDirection() { return this.direction; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public HitResult.Type getType() { return this.miss ? HitResult.Type.MISS : HitResult.Type.BLOCK; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public boolean isInside() { return this.inside; }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public boolean isWorldBorderHit() { return this.worldBorderHit; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\BlockHitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
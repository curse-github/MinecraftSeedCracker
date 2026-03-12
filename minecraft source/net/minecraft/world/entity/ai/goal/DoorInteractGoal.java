/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ public abstract class DoorInteractGoal extends Goal {
/*     */   protected Mob mob;
/*     */   protected BlockPos doorPos;
/*     */   protected boolean hasDoor;
/*     */   
/*     */   public DoorInteractGoal(Mob mob) {
/*  13 */     this.doorPos = BlockPos.ZERO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  20 */     this.mob = mob;
/*  21 */     if (!GoalUtils.hasGroundPathNavigation(mob))
/*  22 */       throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal"); 
/*     */   }
/*     */   private boolean passed; private float doorOpenDirX; private float doorOpenDirZ;
/*     */   
/*     */   protected boolean isOpen() {
/*  27 */     if (!this.hasDoor) {
/*  28 */       return false;
/*     */     }
/*  30 */     BlockState blockState = this.mob.level().getBlockState(this.doorPos);
/*  31 */     if (!(blockState.getBlock() instanceof DoorBlock)) {
/*  32 */       this.hasDoor = false;
/*  33 */       return false;
/*     */     } 
/*  35 */     return ((Boolean)blockState.getValue(DoorBlock.OPEN)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void setOpen(boolean open) {
/*  39 */     if (this.hasDoor) {
/*  40 */       BlockState blockState = this.mob.level().getBlockState(this.doorPos);
/*  41 */       if (blockState.getBlock() instanceof DoorBlock) {
/*  42 */         ((DoorBlock)blockState.getBlock()).setOpen(this.mob, this.mob.level(), blockState, this.doorPos, open);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  49 */     if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
/*  50 */       return false;
/*     */     }
/*  52 */     if (!this.mob.horizontalCollision) {
/*  53 */       return false;
/*     */     }
/*  55 */     Path path = this.mob.getNavigation().getPath();
/*  56 */     if (path == null || path.isDone()) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); i++) {
/*  61 */       Node node = path.getNode(i);
/*  62 */       this.doorPos = new BlockPos(node.x, node.y + 1, node.z);
/*  63 */       if (this.mob.distanceToSqr(this.doorPos.getX(), this.mob.getY(), this.doorPos.getZ()) <= 2.25D) {
/*     */ 
/*     */         
/*  66 */         this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
/*  67 */         if (this.hasDoor) {
/*  68 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  72 */     this.doorPos = this.mob.blockPosition().above();
/*  73 */     this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
/*  74 */     return this.hasDoor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean canContinueToUse() { return !this.passed; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  84 */     this.passed = false;
/*  85 */     this.doorOpenDirX = (float)(this.doorPos.getX() + 0.5D - this.mob.getX());
/*  86 */     this.doorOpenDirZ = (float)(this.doorPos.getZ() + 0.5D - this.mob.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public boolean requiresUpdateEveryTick() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  96 */     float newDoorDirX = (float)(this.doorPos.getX() + 0.5D - this.mob.getX());
/*  97 */     float newDoorDirZ = (float)(this.doorPos.getZ() + 0.5D - this.mob.getZ());
/*  98 */     float dot = this.doorOpenDirX * newDoorDirX + this.doorOpenDirZ * newDoorDirZ;
/*  99 */     if (dot < 0.0F)
/* 100 */       this.passed = true; 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\DoorInteractGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
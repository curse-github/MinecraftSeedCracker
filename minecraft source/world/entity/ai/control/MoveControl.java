/*     */ package net.minecraft.world.entity.ai.control;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MoveControl
/*     */   implements Control
/*     */ {
/*     */   public static final float MIN_SPEED = 5.0E-4F;
/*     */   public static final float MIN_SPEED_SQR = 2.5000003E-7F;
/*     */   protected static final int MAX_TURN = 90;
/*     */   protected final Mob mob;
/*     */   protected double wantedX;
/*     */   
/*     */   public MoveControl(Mob mob) {
/*  28 */     this.operation = Operation.WAIT;
/*     */ 
/*     */     
/*  31 */     this.mob = mob;
/*     */   }
/*     */   protected double wantedY; protected double wantedZ; protected double speedModifier; protected float strafeForwards; protected float strafeRight; protected Operation operation;
/*     */   
/*  35 */   public boolean hasWanted() { return (this.operation == Operation.MOVE_TO); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public double getSpeedModifier() { return this.speedModifier; }
/*     */ 
/*     */   
/*     */   public void setWantedPosition(double x, double y, double z, double speedModifier) {
/*  43 */     this.wantedX = x;
/*  44 */     this.wantedY = y;
/*  45 */     this.wantedZ = z;
/*  46 */     this.speedModifier = speedModifier;
/*  47 */     if (this.operation != Operation.JUMPING) {
/*  48 */       this.operation = Operation.MOVE_TO;
/*     */     }
/*     */   }
/*     */   
/*     */   public void strafe(float forwards, float right) {
/*  53 */     this.operation = Operation.STRAFE;
/*  54 */     this.strafeForwards = forwards;
/*  55 */     this.strafeRight = right;
/*  56 */     this.speedModifier = 0.25D;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  60 */     if (this.operation == Operation.STRAFE) {
/*  61 */       float speed = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
/*  62 */       float speedModified = (float)this.speedModifier * speed;
/*     */       
/*  64 */       float xa = this.strafeForwards;
/*  65 */       float za = this.strafeRight;
/*  66 */       float dist = Mth.sqrt(xa * xa + za * za);
/*  67 */       if (dist < 1.0F) {
/*  68 */         dist = 1.0F;
/*     */       }
/*  70 */       dist = speedModified / dist;
/*  71 */       xa *= dist;
/*  72 */       za *= dist;
/*     */       
/*  74 */       float sin = Mth.sin((this.mob.getYRot() * 0.017453292F));
/*  75 */       float cos = Mth.cos((this.mob.getYRot() * 0.017453292F));
/*  76 */       float dx = xa * cos - za * sin;
/*  77 */       float dz = za * cos + xa * sin;
/*     */       
/*  79 */       if (!isWalkable(dx, dz)) {
/*     */         
/*  81 */         this.strafeForwards = 1.0F;
/*  82 */         this.strafeRight = 0.0F;
/*     */       } 
/*     */       
/*  85 */       this.mob.setSpeed(speedModified);
/*  86 */       this.mob.setZza(this.strafeForwards);
/*  87 */       this.mob.setXxa(this.strafeRight);
/*     */       
/*  89 */       this.operation = Operation.WAIT;
/*  90 */     } else if (this.operation == Operation.MOVE_TO) {
/*  91 */       this.operation = Operation.WAIT;
/*     */       
/*  93 */       double xd = this.wantedX - this.mob.getX();
/*  94 */       double zd = this.wantedZ - this.mob.getZ();
/*  95 */       double yd = this.wantedY - this.mob.getY();
/*  96 */       double dd = xd * xd + yd * yd + zd * zd;
/*  97 */       if (dd < 2.500000277905201E-7D) {
/*  98 */         this.mob.setZza(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 102 */       float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/*     */       
/* 104 */       this.mob.setYRot(rotlerp(this.mob.getYRot(), yRotD, 90.0F));
/* 105 */       this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */       
/* 107 */       BlockPos pos = this.mob.blockPosition();
/* 108 */       BlockState blockState = this.mob.level().getBlockState(pos);
/* 109 */       VoxelShape shape = blockState.getCollisionShape(this.mob.level(), pos);
/* 110 */       if ((yd > this.mob.maxUpStep() && xd * xd + zd * zd < Math.max(1.0F, this.mob.getBbWidth())) || (
/* 111 */         !shape.isEmpty() && this.mob.getY() < shape.max(Direction.Axis.Y) + pos.getY() && !blockState.is(BlockTags.DOORS) && !blockState.is(BlockTags.FENCES))) {
/*     */         
/* 113 */         this.mob.getJumpControl().jump();
/* 114 */         this.operation = Operation.JUMPING;
/*     */       } 
/* 116 */     } else if (this.operation == Operation.JUMPING) {
/* 117 */       this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 118 */       if (this.mob.onGround() || (this.mob.isInLiquid() && this.mob.isAffectedByFluids())) {
/* 119 */         this.operation = Operation.WAIT;
/*     */       }
/*     */     } else {
/* 122 */       this.mob.setZza(0.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isWalkable(float dx, float dz) {
/* 127 */     PathNavigation pathNavigation = this.mob.getNavigation();
/* 128 */     if (pathNavigation != null) {
/* 129 */       NodeEvaluator nodeEvaluator = pathNavigation.getNodeEvaluator();
/* 130 */       if (nodeEvaluator != null && nodeEvaluator.getPathType(this.mob, BlockPos.containing(this.mob.getX() + dx, this.mob.getBlockY(), this.mob.getZ() + dz)) != PathType.WALKABLE) {
/* 131 */         return false;
/*     */       }
/*     */     } 
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   protected float rotlerp(float a, float b, float max) {
/* 138 */     float diff = Mth.wrapDegrees(b - a);
/* 139 */     if (diff > max) {
/* 140 */       diff = max;
/*     */     }
/* 142 */     if (diff < -max) {
/* 143 */       diff = -max;
/*     */     }
/* 145 */     float result = a + diff;
/* 146 */     if (result < 0.0F) {
/* 147 */       result += 360.0F;
/* 148 */     } else if (result > 360.0F) {
/* 149 */       result -= 360.0F;
/*     */     } 
/* 151 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 155 */   public double getWantedX() { return this.wantedX; }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public double getWantedY() { return this.wantedY; }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public double getWantedZ() { return this.wantedZ; }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public void setWait() { this.operation = Operation.WAIT; }
/*     */   
/*     */   protected enum Operation
/*     */   {
/* 171 */     WAIT,
/* 172 */     MOVE_TO,
/* 173 */     STRAFE,
/* 174 */     JUMPING;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\MoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
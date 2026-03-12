/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class NewMinecartBehavior extends MinecartBehavior {
/*     */   public static final int POS_ROT_LERP_TICKS = 3;
/*     */   public static final double ON_RAIL_Y_OFFSET = 0.1D;
/*     */   public static final double OPPOSING_SLOPES_REST_AT_SPEED_THRESHOLD = 0.005D;
/*     */   private StepPartialTicks cacheIndexAlpha;
/*     */   private int cachedLerpDelay;
/*     */   private float cachedPartialTick;
/*     */   
/*     */   public static final class MinecartStep extends Record {
/*     */     private final Vec3 position;
/*     */     private final Vec3 movement;
/*     */     private final float yRot;
/*     */     private final float xRot;
/*     */     private final float weight;
/*     */     
/*  41 */     public MinecartStep(Vec3 position, Vec3 movement, float yRot, float xRot, float weight) { this.position = position; this.movement = movement; this.yRot = yRot; this.xRot = xRot; this.weight = weight; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;
/*  41 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 position() { return this.position; } public Vec3 movement() { return this.movement; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; } public float weight() { return this.weight; }
/*     */     
/*  43 */     public static final StreamCodec<ByteBuf, MinecartStep> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, MinecartStep::position, Vec3.STREAM_CODEC, MinecartStep::movement, ByteBufCodecs.ROTATION_BYTE, MinecartStep::yRot, ByteBufCodecs.ROTATION_BYTE, MinecartStep::xRot, ByteBufCodecs.FLOAT, MinecartStep::weight, MinecartStep::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     public static MinecartStep ZERO = new MinecartStep(Vec3.ZERO, Vec3.ZERO, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */   private static class TrackIteration {
/*  56 */     double movementLeft = 0.0D;
/*     */     
/*     */     boolean firstIteration = true;
/*     */     boolean hasGainedSlopeSpeed = false;
/*     */     boolean hasHalted = false;
/*     */     boolean hasBoosted = false;
/*     */     
/*  63 */     public boolean shouldIterate() { return (this.firstIteration || this.movementLeft > 9.999999747378752E-6D); }
/*     */   }
/*     */ 
/*     */   
/*  67 */   private int lerpDelay = 0;
/*     */   
/*  69 */   public final List<MinecartStep> lerpSteps = new LinkedList();
/*     */   
/*  71 */   public final List<MinecartStep> currentLerpSteps = new LinkedList();
/*  72 */   public double currentLerpStepsTotalWeight = 0.0D;
/*  73 */   public MinecartStep oldLerp = MinecartStep.ZERO;
/*     */ 
/*     */   
/*  76 */   public NewMinecartBehavior(AbstractMinecart minecart) { super(minecart); }
/*     */ 
/*     */   
/*     */   public void tick() {
/*     */     ServerLevel serverLevel;
/*  81 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  82 */     else { lerpClientPositionAndRotation();
/*  83 */       boolean onRails = BaseRailBlock.isRail(level().getBlockState(this.minecart.getCurrentBlockPosOrRailBelow()));
/*  84 */       this.minecart.setOnRails(onRails);
/*     */       
/*     */       return; }
/*     */     
/*  88 */     BlockPos pos = this.minecart.getCurrentBlockPosOrRailBelow();
/*  89 */     BlockState state = level().getBlockState(pos);
/*     */     
/*  91 */     if (this.minecart.isFirstTick()) {
/*  92 */       this.minecart.setOnRails(BaseRailBlock.isRail(state));
/*  93 */       adjustToRails(pos, state, true);
/*     */     } 
/*     */     
/*  96 */     this.minecart.applyGravity();
/*     */     
/*  98 */     this.minecart.moveAlongTrack(serverLevel);
/*     */   }
/*     */   
/*     */   private void lerpClientPositionAndRotation() {
/* 102 */     if (--this.lerpDelay <= 0) {
/* 103 */       setOldLerpValues();
/*     */ 
/*     */       
/* 106 */       this.currentLerpSteps.clear();
/*     */ 
/*     */       
/* 109 */       if (!this.lerpSteps.isEmpty()) {
/* 110 */         this.currentLerpSteps.addAll(this.lerpSteps);
/* 111 */         this.lerpSteps.clear();
/*     */         
/* 113 */         this.currentLerpStepsTotalWeight = 0.0D;
/* 114 */         for (MinecartStep minecartStep : this.currentLerpSteps) {
/* 115 */           this.currentLerpStepsTotalWeight += minecartStep.weight;
/*     */         }
/*     */         
/* 118 */         this.lerpDelay = (this.currentLerpStepsTotalWeight == 0.0D) ? 0 : 3;
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     if (cartHasPosRotLerp()) {
/*     */       
/* 124 */       setPos(getCartLerpPosition(1.0F));
/* 125 */       setDeltaMovement(getCartLerpMovements(1.0F));
/* 126 */       setXRot(getCartLerpXRot(1.0F));
/* 127 */       setYRot(getCartLerpYRot(1.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 132 */   public void setOldLerpValues() { this.oldLerp = new MinecartStep(position(), getDeltaMovement(), getYRot(), getXRot(), 0.0F); }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public boolean cartHasPosRotLerp() { return !this.currentLerpSteps.isEmpty(); }
/*     */ 
/*     */   
/*     */   public float getCartLerpXRot(float partialTicks) {
/* 140 */     StepPartialTicks currentStepPartialTicks = getCurrentLerpStep(partialTicks);
/* 141 */     return Mth.rotLerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.xRot, currentStepPartialTicks.currentStep.xRot);
/*     */   }
/*     */   
/*     */   public float getCartLerpYRot(float partialTicks) {
/* 145 */     StepPartialTicks currentStepPartialTicks = getCurrentLerpStep(partialTicks);
/* 146 */     return Mth.rotLerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.yRot, currentStepPartialTicks.currentStep.yRot);
/*     */   }
/*     */   
/*     */   public Vec3 getCartLerpPosition(float partialTicks) {
/* 150 */     StepPartialTicks currentStepPartialTicks = getCurrentLerpStep(partialTicks);
/* 151 */     return Mth.lerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.position, currentStepPartialTicks.currentStep.position);
/*     */   }
/*     */   
/*     */   public Vec3 getCartLerpMovements(float partialTicks) {
/* 155 */     StepPartialTicks currentStepPartialTicks = getCurrentLerpStep(partialTicks);
/* 156 */     return Mth.lerp(currentStepPartialTicks.partialTicksInStep, currentStepPartialTicks.previousStep.movement, currentStepPartialTicks.currentStep.movement);
/*     */   }
/*     */   private static final class StepPartialTicks extends Record { private final float partialTicksInStep; private final NewMinecartBehavior.MinecartStep currentStep; private final NewMinecartBehavior.MinecartStep previousStep;
/* 159 */     private StepPartialTicks(float partialTicksInStep, NewMinecartBehavior.MinecartStep currentStep, NewMinecartBehavior.MinecartStep previousStep) { this.partialTicksInStep = partialTicksInStep; this.currentStep = currentStep; this.previousStep = previousStep; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #159	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$StepPartialTicks;
/* 159 */       //   0	8	1	o	Ljava/lang/Object; } public float partialTicksInStep() { return this.partialTicksInStep; } public NewMinecartBehavior.MinecartStep currentStep() { return this.currentStep; } public NewMinecartBehavior.MinecartStep previousStep() { return this.previousStep; } }
/*     */   
/*     */   private StepPartialTicks getCurrentLerpStep(float partialTick) {
/* 162 */     if (partialTick == this.cachedPartialTick && this.lerpDelay == this.cachedLerpDelay && this.cacheIndexAlpha != null) {
/* 163 */       return this.cacheIndexAlpha;
/*     */     }
/*     */     
/* 166 */     float alpha = ((3 - this.lerpDelay) + partialTick) / 3.0F;
/* 167 */     float countUp = 0.0F;
/*     */     
/* 169 */     float indexedPartialTick = 1.0F;
/* 170 */     boolean foundIndex = false; int index;
/* 171 */     for (index = 0; index < this.currentLerpSteps.size(); index++) {
/* 172 */       float weight = ((MinecartStep)this.currentLerpSteps.get(index)).weight;
/* 173 */       if (weight > 0.0F) {
/*     */ 
/*     */         
/* 176 */         countUp += weight;
/* 177 */         if (countUp >= this.currentLerpStepsTotalWeight * alpha) {
/* 178 */           float current = countUp - weight;
/* 179 */           indexedPartialTick = (float)((alpha * this.currentLerpStepsTotalWeight - current) / weight);
/* 180 */           foundIndex = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     if (!foundIndex) {
/* 186 */       index = this.currentLerpSteps.size() - 1;
/*     */     }
/*     */     
/* 189 */     MinecartStep currentStep = (MinecartStep)this.currentLerpSteps.get(index);
/* 190 */     MinecartStep previousStep = (index > 0) ? (MinecartStep)this.currentLerpSteps.get(index - 1) : this.oldLerp;
/*     */     
/* 192 */     this.cacheIndexAlpha = new StepPartialTicks(indexedPartialTick, currentStep, previousStep);
/* 193 */     this.cachedLerpDelay = this.lerpDelay;
/* 194 */     this.cachedPartialTick = partialTick;
/* 195 */     return this.cacheIndexAlpha;
/*     */   }
/*     */   public void adjustToRails(BlockPos targetBlockPos, BlockState currentState, boolean instant) {
/*     */     Vec3 targetPosition;
/* 199 */     if (!BaseRailBlock.isRail(currentState)) {
/*     */       return;
/*     */     }
/*     */     
/* 203 */     RailShape shape = (RailShape)currentState.getValue(((BaseRailBlock)currentState.getBlock()).getShapeProperty());
/* 204 */     Pair<Vec3i, Vec3i> exits = AbstractMinecart.exits(shape);
/* 205 */     Vec3 exit0 = (new Vec3((Vec3i)exits.getFirst())).scale(0.5D);
/* 206 */     Vec3 exit1 = (new Vec3((Vec3i)exits.getSecond())).scale(0.5D);
/*     */     
/* 208 */     Vec3 horizontalOutDirection = exit0.horizontal();
/* 209 */     Vec3 horizontalInDirection = exit1.horizontal();
/*     */     
/* 211 */     if ((getDeltaMovement().length() > 9.999999747378752E-6D && 
/* 212 */       getDeltaMovement().dot(horizontalOutDirection) < getDeltaMovement().dot(horizontalInDirection)) || 
/* 213 */       isDecending(horizontalInDirection, shape)) {
/*     */ 
/*     */       
/* 216 */       Vec3 swap = horizontalOutDirection;
/* 217 */       horizontalOutDirection = horizontalInDirection;
/* 218 */       horizontalInDirection = swap;
/*     */     } 
/*     */     
/* 221 */     float yRot = 180.0F - (float)(Math.atan2(horizontalOutDirection.z, horizontalOutDirection.x) * 180.0D / Math.PI);
/* 222 */     yRot += (this.minecart.isFlipped() ? 180.0F : 0.0F);
/*     */     
/* 224 */     Vec3 previousPosition = position();
/*     */     
/* 226 */     boolean inCorner = (exit0.x() != exit1.x() && exit0.z() != exit1.z());
/* 227 */     if (inCorner) {
/*     */       
/* 229 */       Vec3 from0to1 = exit1.subtract(exit0);
/* 230 */       Vec3 from0toPos = previousPosition.subtract(targetBlockPos.getBottomCenter()).subtract(exit0);
/* 231 */       Vec3 travelVectorFrom0 = from0to1.scale(from0to1.dot(from0toPos) / from0to1.dot(from0to1));
/* 232 */       targetPosition = targetBlockPos.getBottomCenter().add(exit0).add(travelVectorFrom0);
/*     */ 
/*     */       
/* 235 */       yRot = 180.0F - (float)(Math.atan2(travelVectorFrom0.z, travelVectorFrom0.x) * 180.0D / Math.PI);
/* 236 */       yRot += (this.minecart.isFlipped() ? 180.0F : 0.0F);
/*     */     } else {
/* 238 */       boolean zSnap = ((exit0.subtract(exit1)).x != 0.0D);
/* 239 */       boolean xSnap = ((exit0.subtract(exit1)).z != 0.0D);
/* 240 */       targetPosition = new Vec3(xSnap ? (targetBlockPos.getCenter()).x : previousPosition.x, targetBlockPos.getY(), zSnap ? (targetBlockPos.getCenter()).z : previousPosition.z);
/*     */     } 
/* 242 */     Vec3 diffFromBlock = targetPosition.subtract(previousPosition);
/* 243 */     setPos(previousPosition.add(diffFromBlock));
/*     */     
/* 245 */     float xRot = 0.0F;
/* 246 */     boolean inHill = (exit0.y() != exit1.y());
/* 247 */     if (inHill) {
/*     */       
/* 249 */       Vec3 inPosition = targetBlockPos.getBottomCenter().add(horizontalInDirection);
/* 250 */       double horizontalDistanceFromIn = inPosition.distanceTo(position());
/* 251 */       setPos(position().add(0.0D, horizontalDistanceFromIn + 0.1D, 0.0D));
/* 252 */       xRot = this.minecart.isFlipped() ? 45.0F : -45.0F;
/*     */     } else {
/* 254 */       setPos(position().add(0.0D, 0.1D, 0.0D));
/*     */     } 
/*     */     
/* 257 */     setRotation(yRot, xRot);
/*     */     
/* 259 */     double adjustDistance = previousPosition.distanceTo(position());
/* 260 */     if (adjustDistance > 0.0D)
/*     */     {
/* 262 */       this.lerpSteps.add(new MinecartStep(
/* 263 */             position(), 
/* 264 */             getDeltaMovement(), 
/* 265 */             getYRot(), 
/* 266 */             getXRot(), 
/* 267 */             instant ? 0.0F : (float)adjustDistance));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void setRotation(float yRot, float xRot) {
/* 273 */     double yRotDiff = Math.abs(yRot - getYRot());
/* 274 */     if (yRotDiff >= 175.0D && yRotDiff <= 185.0D) {
/* 275 */       this.minecart.setFlipped(!this.minecart.isFlipped());
/* 276 */       yRot -= 180.0F;
/* 277 */       xRot *= -1.0F;
/*     */     } 
/*     */     
/* 280 */     xRot = Math.clamp(xRot, -45.0F, 45.0F);
/* 281 */     setXRot(xRot % 360.0F);
/* 282 */     setYRot(yRot % 360.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveAlongTrack(ServerLevel level) {
/* 287 */     TrackIteration trackIteration = new TrackIteration();
/* 288 */     while (trackIteration.shouldIterate() && this.minecart.isAlive()) {
/* 289 */       Vec3 initialStepDeltaMovement = getDeltaMovement();
/* 290 */       BlockPos currentPos = this.minecart.getCurrentBlockPosOrRailBelow();
/* 291 */       BlockState currentState = level().getBlockState(currentPos);
/* 292 */       boolean onRails = BaseRailBlock.isRail(currentState);
/* 293 */       if (this.minecart.isOnRails() != onRails) {
/* 294 */         this.minecart.setOnRails(onRails);
/* 295 */         adjustToRails(currentPos, currentState, false);
/*     */       } 
/*     */       
/* 298 */       if (onRails) {
/* 299 */         this.minecart.resetFallDistance();
/* 300 */         this.minecart.setOldPosAndRot();
/*     */         
/* 302 */         if (currentState.is(Blocks.ACTIVATOR_RAIL)) {
/* 303 */           this.minecart.activateMinecart(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), ((Boolean)currentState.getValue(PoweredRailBlock.POWERED)).booleanValue());
/*     */         }
/*     */         
/* 306 */         RailShape shape = (RailShape)currentState.getValue(((BaseRailBlock)currentState.getBlock()).getShapeProperty());
/*     */ 
/*     */         
/* 309 */         Vec3 newDeltaMovement = calculateTrackSpeed(level, initialStepDeltaMovement.horizontal(), trackIteration, currentPos, currentState, shape);
/*     */         
/* 311 */         if (trackIteration.firstIteration) {
/* 312 */           trackIteration.movementLeft = newDeltaMovement.horizontalDistance();
/*     */         } else {
/*     */           
/* 315 */           trackIteration.movementLeft += newDeltaMovement.horizontalDistance() - initialStepDeltaMovement.horizontalDistance();
/*     */         } 
/*     */         
/* 318 */         setDeltaMovement(newDeltaMovement);
/* 319 */         trackIteration.movementLeft = this.minecart.makeStepAlongTrack(currentPos, shape, trackIteration.movementLeft);
/*     */       } else {
/* 321 */         this.minecart.comeOffTrack(level);
/* 322 */         trackIteration.movementLeft = 0.0D;
/*     */       } 
/*     */       
/* 325 */       Vec3 stepPosition = position();
/* 326 */       Vec3 stepDelta = stepPosition.subtract(this.minecart.oldPosition());
/* 327 */       double stepLength = stepDelta.length();
/* 328 */       if (stepLength > 9.999999747378752E-6D) {
/* 329 */         if (stepDelta.horizontalDistanceSqr() > 9.999999747378752E-6D) {
/* 330 */           float yRot = 180.0F - (float)(Math.atan2(stepDelta.z, stepDelta.x) * 180.0D / Math.PI);
/* 331 */           float xRot = (this.minecart.onGround() && !this.minecart.isOnRails()) ? 0.0F : (90.0F - (float)(Math.atan2(stepDelta.horizontalDistance(), stepDelta.y) * 180.0D / Math.PI));
/* 332 */           yRot += (this.minecart.isFlipped() ? 180.0F : 0.0F);
/* 333 */           xRot *= (this.minecart.isFlipped() ? -1.0F : 1.0F);
/* 334 */           setRotation(yRot, xRot);
/* 335 */         } else if (!this.minecart.isOnRails()) {
/* 336 */           setXRot(this.minecart.onGround() ? 0.0F : Mth.rotLerp(0.2F, getXRot(), 0.0F));
/*     */         } 
/*     */ 
/*     */         
/* 340 */         this.lerpSteps.add(new MinecartStep(stepPosition, 
/*     */               
/* 342 */               getDeltaMovement(), 
/* 343 */               getYRot(), 
/* 344 */               getXRot(), 
/* 345 */               (float)Math.min(stepLength, getMaxSpeed(level))));
/*     */       }
/* 347 */       else if (initialStepDeltaMovement.horizontalDistanceSqr() > 0.0D) {
/*     */         
/* 349 */         this.lerpSteps.add(new MinecartStep(stepPosition, 
/*     */               
/* 351 */               getDeltaMovement(), 
/* 352 */               getYRot(), 
/* 353 */               getXRot(), 1.0F));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 358 */       if (stepLength > 9.999999747378752E-6D || trackIteration.firstIteration) {
/*     */         
/* 360 */         this.minecart.applyEffectsFromBlocks();
/*     */ 
/*     */         
/* 363 */         this.minecart.applyEffectsFromBlocks();
/*     */       } 
/*     */       
/* 366 */       trackIteration.firstIteration = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3 calculateTrackSpeed(ServerLevel level, Vec3 deltaMovement, TrackIteration trackIteration, BlockPos currentPos, BlockState currentState, RailShape shape) {
/* 371 */     Vec3 newDeltaMovement = deltaMovement;
/*     */     
/* 373 */     if (!trackIteration.hasGainedSlopeSpeed) {
/* 374 */       Vec3 slopedDeltaMovement = calculateSlopeSpeed(newDeltaMovement, shape);
/* 375 */       if (slopedDeltaMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
/* 376 */         trackIteration.hasGainedSlopeSpeed = true;
/* 377 */         newDeltaMovement = slopedDeltaMovement;
/*     */       } 
/*     */     } 
/*     */     
/* 381 */     if (trackIteration.firstIteration) {
/* 382 */       Vec3 playerInputMovement = calculatePlayerInputSpeed(newDeltaMovement);
/* 383 */       if (playerInputMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
/* 384 */         trackIteration.hasHalted = true;
/* 385 */         newDeltaMovement = playerInputMovement;
/*     */       } 
/*     */     } 
/*     */     
/* 389 */     if (!trackIteration.hasHalted) {
/* 390 */       Vec3 haltedDeltaMovement = calculateHaltTrackSpeed(newDeltaMovement, currentState);
/* 391 */       if (haltedDeltaMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
/* 392 */         trackIteration.hasHalted = true;
/* 393 */         newDeltaMovement = haltedDeltaMovement;
/*     */       } 
/*     */     } 
/*     */     
/* 397 */     if (trackIteration.firstIteration) {
/*     */       
/* 399 */       newDeltaMovement = this.minecart.applyNaturalSlowdown(newDeltaMovement);
/*     */ 
/*     */       
/* 402 */       if (newDeltaMovement.lengthSqr() > 0.0D) {
/* 403 */         double speed = Math.min(newDeltaMovement.length(), this.minecart.getMaxSpeed(level));
/* 404 */         newDeltaMovement = newDeltaMovement.normalize().scale(speed);
/*     */       } 
/*     */     } 
/*     */     
/* 408 */     if (!trackIteration.hasBoosted) {
/* 409 */       Vec3 boostedDeltaMovement = calculateBoostTrackSpeed(newDeltaMovement, currentPos, currentState);
/* 410 */       if (boostedDeltaMovement.horizontalDistanceSqr() != newDeltaMovement.horizontalDistanceSqr()) {
/* 411 */         trackIteration.hasBoosted = true;
/* 412 */         newDeltaMovement = boostedDeltaMovement;
/*     */       } 
/*     */     } 
/*     */     
/* 416 */     return newDeltaMovement;
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3 calculateSlopeSpeed(Vec3 deltaMovement, RailShape shape) {
/* 421 */     double slideSpeed = Math.max(0.0078125D, deltaMovement.horizontalDistance() * 0.02D);
/*     */     
/* 423 */     if (this.minecart.isInWater()) {
/* 424 */       slideSpeed *= 0.2D;
/*     */     }
/*     */ 
/*     */     
/* 428 */     switch (shape) { case ASCENDING_EAST: case ASCENDING_WEST: case ASCENDING_NORTH: case ASCENDING_SOUTH:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 433 */       deltaMovement;
/*     */   }
/*     */   
/*     */   private Vec3 calculatePlayerInputSpeed(Vec3 deltaMovement) {
/*     */     ServerPlayer player;
/* 438 */     Entity entity = this.minecart.getFirstPassenger(); if (entity instanceof ServerPlayer) { player = (ServerPlayer)entity; }
/* 439 */     else { return deltaMovement; }
/*     */ 
/*     */     
/* 442 */     Vec3 moveIntent = player.getLastClientMoveIntent();
/* 443 */     if (moveIntent.lengthSqr() > 0.0D) {
/* 444 */       Vec3 riderMovement = moveIntent.normalize();
/* 445 */       double ownDist = deltaMovement.horizontalDistanceSqr();
/* 446 */       if (riderMovement.lengthSqr() > 0.0D && ownDist < 0.01D) {
/* 447 */         return deltaMovement.add((new Vec3(riderMovement.x, 0.0D, riderMovement.z)).normalize().scale(0.001D));
/*     */       }
/*     */     } 
/* 450 */     return deltaMovement;
/*     */   }
/*     */   
/*     */   private Vec3 calculateHaltTrackSpeed(Vec3 deltaMovement, BlockState state) {
/* 454 */     if (!state.is(Blocks.POWERED_RAIL) || ((Boolean)state.getValue(PoweredRailBlock.POWERED)).booleanValue()) {
/* 455 */       return deltaMovement;
/*     */     }
/*     */     
/* 458 */     if (deltaMovement.length() < 0.03D) {
/* 459 */       return Vec3.ZERO;
/*     */     }
/*     */     
/* 462 */     return deltaMovement.scale(0.5D);
/*     */   }
/*     */   
/*     */   private Vec3 calculateBoostTrackSpeed(Vec3 deltaMovement, BlockPos pos, BlockState state) {
/* 466 */     if (!state.is(Blocks.POWERED_RAIL) || !((Boolean)state.getValue(PoweredRailBlock.POWERED)).booleanValue()) {
/* 467 */       return deltaMovement;
/*     */     }
/*     */     
/* 470 */     if (deltaMovement.length() > 0.01D)
/*     */     {
/* 472 */       return deltaMovement.normalize().scale(deltaMovement.length() + 0.06D);
/*     */     }
/*     */     
/* 475 */     Vec3 powerDirection = this.minecart.getRedstoneDirection(pos);
/* 476 */     if (powerDirection.lengthSqr() <= 0.0D)
/*     */     {
/* 478 */       return deltaMovement;
/*     */     }
/*     */     
/* 481 */     return powerDirection.scale(deltaMovement.length() + 0.2D);
/*     */   }
/*     */ 
/*     */   
/*     */   public double stepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) {
/* 486 */     if (movementLeft < 9.999999747378752E-6D) {
/* 487 */       return 0.0D;
/*     */     }
/*     */     
/* 490 */     Vec3 oldPosition = position();
/* 491 */     Pair<Vec3i, Vec3i> exits = AbstractMinecart.exits(shape);
/* 492 */     Vec3i exit0 = (Vec3i)exits.getFirst();
/* 493 */     Vec3i exit1 = (Vec3i)exits.getSecond();
/*     */     
/* 495 */     Vec3 movement = getDeltaMovement().horizontal();
/* 496 */     if (movement.length() < 9.999999747378752E-6D) {
/* 497 */       setDeltaMovement(Vec3.ZERO);
/* 498 */       return 0.0D;
/*     */     } 
/*     */     
/* 501 */     boolean inHill = (exit0.getY() != exit1.getY());
/*     */     
/* 503 */     Vec3 horizontalInDirection = (new Vec3(exit1)).scale(0.5D).horizontal();
/* 504 */     Vec3 horizontalOutDirection = (new Vec3(exit0)).scale(0.5D).horizontal();
/*     */     
/* 506 */     if (movement.dot(horizontalOutDirection) < movement.dot(horizontalInDirection))
/*     */     {
/* 508 */       horizontalOutDirection = horizontalInDirection;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 514 */     Vec3 outPosition = pos.getBottomCenter().add(horizontalOutDirection).add(0.0D, 0.1D, 0.0D).add(horizontalOutDirection.normalize().scale(9.999999747378752E-6D));
/*     */     
/* 516 */     if (inHill && !isDecending(movement, shape))
/*     */     {
/* 518 */       outPosition = outPosition.add(0.0D, 1.0D, 0.0D);
/*     */     }
/*     */ 
/*     */     
/* 522 */     Vec3 towardsOut = outPosition.subtract(position()).normalize();
/* 523 */     movement = towardsOut.scale(movement.length() / towardsOut.horizontalDistance());
/* 524 */     Vec3 newPosition = oldPosition.add(movement.normalize().scale(movementLeft * (inHill ? Mth.SQRT_OF_TWO : 1.0F)));
/*     */ 
/*     */     
/* 527 */     if (oldPosition.distanceToSqr(outPosition) <= oldPosition.distanceToSqr(newPosition)) {
/* 528 */       movementLeft = outPosition.subtract(newPosition).horizontalDistance();
/* 529 */       newPosition = outPosition;
/*     */     } else {
/* 531 */       movementLeft = 0.0D;
/*     */     } 
/*     */     
/* 534 */     this.minecart.move(MoverType.SELF, newPosition.subtract(oldPosition));
/* 535 */     BlockState newBlockState = level().getBlockState(BlockPos.containing(newPosition));
/* 536 */     if (inHill) {
/* 537 */       if (BaseRailBlock.isRail(newBlockState)) {
/* 538 */         RailShape newRailShape = (RailShape)newBlockState.getValue(((BaseRailBlock)newBlockState.getBlock()).getShapeProperty());
/* 539 */         if (restAtVShape(shape, newRailShape)) {
/* 540 */           return 0.0D;
/*     */         }
/*     */       } 
/*     */       
/* 544 */       double horizontalDistanceFromOut = outPosition.horizontal().distanceTo(position().horizontal());
/* 545 */       double projectYPos = outPosition.y + (isDecending(movement, shape) ? horizontalDistanceFromOut : -horizontalDistanceFromOut);
/* 546 */       if ((position()).y < projectYPos) {
/* 547 */         setPos((position()).x, projectYPos, (position()).z);
/*     */       }
/*     */     } 
/*     */     
/* 551 */     if (position().distanceTo(oldPosition) < 9.999999747378752E-6D && newPosition.distanceTo(oldPosition) > 9.999999747378752E-6D) {
/* 552 */       setDeltaMovement(Vec3.ZERO);
/* 553 */       return 0.0D;
/*     */     } 
/*     */     
/* 556 */     setDeltaMovement(movement);
/* 557 */     return movementLeft;
/*     */   }
/*     */   
/*     */   private boolean restAtVShape(RailShape currentRailShape, RailShape newRailShape) {
/* 561 */     if (getDeltaMovement().lengthSqr() < 0.005D && newRailShape
/* 562 */       .isSlope() && 
/* 563 */       isDecending(getDeltaMovement(), currentRailShape) && 
/* 564 */       !isDecending(getDeltaMovement(), newRailShape)) {
/*     */       
/* 566 */       setDeltaMovement(Vec3.ZERO);
/* 567 */       return true;
/*     */     } 
/* 569 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 574 */   public double getMaxSpeed(ServerLevel level) { return ((Integer)level.getGameRules().get(GameRules.MAX_MINECART_SPEED)).intValue() * (this.minecart.isInWater() ? 0.5D : 1.0D) / 20.0D; }
/*     */ 
/*     */   
/*     */   private boolean isDecending(Vec3 movement, RailShape shape) {
/* 578 */     switch (shape) { case ASCENDING_EAST: return 
/* 579 */           (movement.x < 0.0D);
/* 580 */       case ASCENDING_WEST: return (movement.x > 0.0D);
/* 581 */       case ASCENDING_NORTH: return (movement.z > 0.0D);
/* 582 */       case ASCENDING_SOUTH: return (movement.z < 0.0D); }
/*     */     
/*     */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 589 */   public double getSlowdownFactor() { return this.minecart.isVehicle() ? 0.997D : 0.975D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean pushAndPickupEntities() {
/* 594 */     boolean pickedUp = pickupEntities(this.minecart.getBoundingBox().inflate(0.2D, 0.0D, 0.2D));
/* 595 */     if (this.minecart.horizontalCollision || this.minecart.verticalCollision) {
/* 596 */       boolean pushed = pushEntities(this.minecart.getBoundingBox().inflate(1.0E-7D));
/* 597 */       return (pickedUp && !pushed);
/*     */     } 
/* 599 */     return false;
/*     */   }
/*     */   
/*     */   public boolean pickupEntities(AABB hitbox) {
/* 603 */     if (this.minecart.isRideable() && !this.minecart.isVehicle()) {
/* 604 */       List<Entity> entities = level().getEntities(this.minecart, hitbox, EntitySelector.pushableBy(this.minecart));
/* 605 */       if (!entities.isEmpty()) {
/* 606 */         for (Entity entity : entities) {
/* 607 */           if (!(entity instanceof net.minecraft.world.entity.player.Player) && !(entity instanceof net.minecraft.world.entity.animal.golem.IronGolem) && !(entity instanceof AbstractMinecart) && !this.minecart.isVehicle() && !entity.isPassenger()) {
/* 608 */             boolean pickedUp = entity.startRiding(this.minecart);
/* 609 */             if (pickedUp) {
/* 610 */               return true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 616 */     return false;
/*     */   }
/*     */   
/*     */   public boolean pushEntities(AABB hitbox) {
/* 620 */     boolean pushed = false;
/* 621 */     if (this.minecart.isRideable()) {
/* 622 */       List<Entity> entities = level().getEntities(this.minecart, hitbox, EntitySelector.pushableBy(this.minecart));
/* 623 */       if (!entities.isEmpty()) {
/* 624 */         for (Entity entity : entities) {
/* 625 */           if (entity instanceof net.minecraft.world.entity.player.Player || entity instanceof net.minecraft.world.entity.animal.golem.IronGolem || entity instanceof AbstractMinecart || this.minecart.isVehicle() || entity.isPassenger()) {
/* 626 */             entity.push(this.minecart);
/* 627 */             pushed = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } else {
/* 632 */       for (Entity entity : level().getEntities(this.minecart, hitbox)) {
/* 633 */         if (!this.minecart.hasPassenger(entity) && entity.isPushable() && entity instanceof AbstractMinecart) {
/* 634 */           entity.push(this.minecart);
/* 635 */           pushed = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 639 */     return pushed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
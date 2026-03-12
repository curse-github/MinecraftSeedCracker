/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Leashable
/*     */ {
/*     */   public static final String LEASH_TAG = "leash";
/*     */   public static final double LEASH_TOO_FAR_DIST = 12.0D;
/*     */   public static final double LEASH_ELASTIC_DIST = 6.0D;
/*     */   public static final double MAXIMUM_ALLOWED_LEASHED_DIST = 16.0D;
/*     */   
/*  42 */   default boolean isLeashed() { return (getLeashData() != null && (getLeashData()).leashHolder != null); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   default boolean mayBeLeashed() { return (getLeashData() != null); }
/*     */ 
/*     */   
/*     */   default boolean canHaveALeashAttachedTo(Entity entity) {
/*  50 */     if (this == entity) {
/*  51 */       return false;
/*     */     }
/*  53 */     if (leashDistanceTo(entity) > leashSnapDistance()) {
/*  54 */       return false;
/*     */     }
/*  56 */     return canBeLeashed();
/*     */   }
/*     */ 
/*     */   
/*  60 */   default double leashDistanceTo(Entity entity) { return entity.getBoundingBox().getCenter().distanceTo(((Entity)this).getBoundingBox().getCenter()); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   default boolean canBeLeashed() { return true; }
/*     */ 
/*     */   
/*     */   default void setDelayedLeashHolderId(int entityId) {
/*  68 */     setLeashData(new LeashData(entityId));
/*  69 */     dropLeash((Entity)this, false, false);
/*     */   }
/*     */   
/*     */   default void readLeashData(ValueInput input) {
/*  73 */     LeashData newLeashData = (LeashData)input.read("leash", LeashData.CODEC).orElse(null);
/*  74 */     if (getLeashData() != null && newLeashData == null) {
/*  75 */       removeLeash();
/*     */     }
/*  77 */     setLeashData(newLeashData);
/*     */   }
/*     */ 
/*     */   
/*  81 */   default void writeLeashData(ValueOutput output, LeashData leashData) { output.storeNullable("leash", LeashData.CODEC, leashData); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E extends Entity & Leashable> void restoreLeashFromSave(E entity, LeashData leashData) {
/*  87 */     if (leashData.delayedLeashInfo != null) { Level level = entity.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  88 */         Optional<UUID> leashUuid = leashData.delayedLeashInfo.left();
/*  89 */         Optional<BlockPos> pos = leashData.delayedLeashInfo.right();
/*  90 */         if (leashUuid.isPresent()) {
/*  91 */           Entity leasher = serverLevel.getEntity((UUID)leashUuid.get());
/*  92 */           if (leasher != null) {
/*  93 */             setLeashedTo(entity, leasher, true);
/*     */             return;
/*     */           } 
/*  96 */         } else if (pos.isPresent()) {
/*  97 */           setLeashedTo(entity, LeashFenceKnotEntity.getOrCreateKnot(serverLevel, (BlockPos)pos.get()), true);
/*     */           
/*     */           return;
/*     */         } 
/* 101 */         if (entity.tickCount > 100) {
/* 102 */           entity.spawnAtLocation(serverLevel, Items.LEAD);
/* 103 */           ((Leashable)entity).setLeashData(null);
/*     */         }  }
/*     */        }
/*     */   
/*     */   }
/*     */   
/* 109 */   default void dropLeash() { dropLeash((Entity)this, true, true); }
/*     */ 
/*     */ 
/*     */   
/* 113 */   default void removeLeash() { dropLeash((Entity)this, true, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   default void onLeashRemoved() {}
/*     */ 
/*     */   
/*     */   private static <E extends Entity & Leashable> void dropLeash(E entity, boolean sendPacket, boolean dropLead) {
/* 121 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 122 */     if (leashData != null && leashData.leashHolder != null) {
/* 123 */       ((Leashable)entity).setLeashData(null);
/* 124 */       ((Leashable)entity).onLeashRemoved();
/* 125 */       Level level1 = entity.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 126 */         if (dropLead) {
/* 127 */           entity.spawnAtLocation(level, Items.LEAD);
/*     */         }
/*     */         
/* 130 */         if (sendPacket) {
/* 131 */           level.getChunkSource().sendToTrackingPlayers(entity, new ClientboundSetEntityLinkPacket(entity, null));
/*     */         }
/* 133 */         leashData.leashHolder.notifyLeasheeRemoved((Leashable)entity); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   static <E extends Entity & Leashable> void tickLeash(ServerLevel level, E entity) {
/* 139 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 140 */     if (leashData != null && leashData.delayedLeashInfo != null) {
/* 141 */       restoreLeashFromSave(entity, leashData);
/*     */     }
/* 143 */     if (leashData == null || leashData.leashHolder == null) {
/*     */       return;
/*     */     }
/* 146 */     if (!entity.canInteractWithLevel() || !leashData.leashHolder.canInteractWithLevel()) {
/* 147 */       if (((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 148 */         ((Leashable)entity).dropLeash();
/*     */       } else {
/* 150 */         ((Leashable)entity).removeLeash();
/*     */       } 
/*     */     }
/* 153 */     Entity leashHolder = ((Leashable)entity).getLeashHolder();
/* 154 */     if (leashHolder != null && leashHolder.level() == entity.level()) {
/* 155 */       double distanceTo = ((Leashable)entity).leashDistanceTo(leashHolder);
/*     */       
/* 157 */       ((Leashable)entity).whenLeashedTo(leashHolder);
/*     */       
/* 159 */       if (distanceTo > ((Leashable)entity).leashSnapDistance()) {
/* 160 */         level.playSound(null, leashHolder.getX(), leashHolder.getY(), leashHolder.getZ(), SoundEvents.LEAD_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 161 */         ((Leashable)entity).leashTooFarBehaviour();
/* 162 */       } else if (distanceTo > ((Leashable)entity).leashElasticDistance() - leashHolder.getBbWidth() - entity.getBbWidth() && ((Leashable)entity).checkElasticInteractions(leashHolder, leashData)) {
/* 163 */         ((Leashable)entity).onElasticLeashPull();
/*     */       } else {
/* 165 */         ((Leashable)entity).closeRangeLeashBehaviour(leashHolder);
/*     */       } 
/*     */       
/* 168 */       entity.setYRot((float)(entity.getYRot() - leashData.angularMomentum));
/* 169 */       leashData.angularMomentum *= angularFriction(entity);
/*     */     } 
/*     */   }
/*     */   
/*     */   default void onElasticLeashPull() {
/* 174 */     Entity entity = (Entity)this;
/* 175 */     entity.checkFallDistanceAccumulation();
/*     */   }
/*     */ 
/*     */   
/* 179 */   default double leashSnapDistance() { return 12.0D; }
/*     */ 
/*     */ 
/*     */   
/* 183 */   default double leashElasticDistance() { return 6.0D; }
/*     */ 
/*     */   
/*     */   static <E extends Entity & Leashable> float angularFriction(E entity) {
/* 187 */     if (entity.onGround()) {
/* 188 */       return entity.level().getBlockState(entity.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
/*     */     }
/*     */     
/* 191 */     if (entity.isInLiquid()) {
/* 192 */       return 0.8F;
/*     */     }
/* 194 */     return 0.91F;
/*     */   }
/*     */ 
/*     */   
/* 198 */   default void whenLeashedTo(Entity leashHolder) { leashHolder.notifyLeashHolder(this); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   default void leashTooFarBehaviour() { dropLeash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void closeRangeLeashBehaviour(Entity leashHolder) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public static final Vec3 AXIS_SPECIFIC_ELASTICITY = new Vec3(0.8D, 0.2D, 0.8D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float SPRING_DAMPENING = 0.7F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final double TORSIONAL_ELASTICITY = 10.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final double STIFFNESS = 0.11D;
/*     */ 
/*     */ 
/*     */   
/* 231 */   public static final List<Vec3> ENTITY_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0D, 0.5D, 0.5D));
/* 232 */   public static final List<Vec3> LEASHER_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0D, 0.5D, 0.0D));
/* 233 */   public static final List<Vec3> SHARED_QUAD_ATTACHMENT_POINTS = ImmutableList.of(new Vec3(-0.5D, 0.5D, 0.5D), new Vec3(-0.5D, 0.5D, -0.5D), new Vec3(0.5D, 0.5D, -0.5D), new Vec3(0.5D, 0.5D, 0.5D));
/*     */   
/*     */   default boolean checkElasticInteractions(Entity leashHolder, LeashData leashData) {
/* 236 */     boolean quadConnection = (leashHolder.supportQuadLeashAsHolder() && supportQuadLeash());
/* 237 */     List<Wrench> wrenches = computeElasticInteraction((Entity)this, leashHolder, 
/* 238 */         quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT, 
/* 239 */         quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT);
/*     */     
/* 241 */     if (wrenches.isEmpty())
/*     */     {
/* 243 */       return false;
/*     */     }
/* 245 */     Wrench result = Wrench.accumulate(wrenches).scale(quadConnection ? 0.25D : 1.0D);
/* 246 */     leashData.angularMomentum += 10.0D * result.torque();
/* 247 */     Vec3 relativeVelocityToLeasher = getHolderMovement(leashHolder).subtract(((Entity)this).getKnownMovement());
/* 248 */     ((Entity)this).addDeltaMovement(result.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(relativeVelocityToLeasher.scale(0.11D)));
/* 249 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Vec3 getHolderMovement(Entity leashHolder) {
/* 254 */     if (leashHolder instanceof Mob) { Mob mob = (Mob)leashHolder; if (mob.isNoAi())
/* 255 */         return Vec3.ZERO;  }
/*     */     
/* 257 */     return leashHolder.getKnownMovement();
/*     */   }
/*     */   
/*     */   private static <E extends Entity & Leashable> List<Wrench> computeElasticInteraction(E entity, Entity leashHolder, List<Vec3> entityAttachmentPoints, List<Vec3> leasherAttachmentPoints) {
/* 261 */     double slackDistance = ((Leashable)entity).leashElasticDistance();
/* 262 */     Vec3 currentMovement = getHolderMovement(entity);
/*     */     
/* 264 */     float entityYRot = entity.getYRot() * 0.017453292F;
/* 265 */     Vec3 entityDimensions = new Vec3(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
/*     */     
/* 267 */     float leashHolderYRot = leashHolder.getYRot() * 0.017453292F;
/* 268 */     Vec3 leasherDimensions = new Vec3(leashHolder.getBbWidth(), leashHolder.getBbHeight(), leashHolder.getBbWidth());
/*     */     
/* 270 */     List<Wrench> wrenches = new ArrayList<Wrench>();
/* 271 */     for (int i = 0; i < entityAttachmentPoints.size(); i++) {
/* 272 */       Vec3 entityAttachVector = ((Vec3)entityAttachmentPoints.get(i)).multiply(entityDimensions).yRot(-entityYRot);
/* 273 */       Vec3 entityAttachPos = entity.position().add(entityAttachVector);
/* 274 */       Vec3 leasherAttachVector = ((Vec3)leasherAttachmentPoints.get(i)).multiply(leasherDimensions).yRot(-leashHolderYRot);
/* 275 */       Vec3 leasherAttachPos = leashHolder.position().add(leasherAttachVector);
/*     */       
/* 277 */       Objects.requireNonNull(wrenches); computeDampenedSpringInteraction(leasherAttachPos, entityAttachPos, slackDistance, currentMovement, entityAttachVector).ifPresent(wrenches::add);
/*     */     } 
/* 279 */     return wrenches;
/*     */   }
/*     */   
/*     */   private static Optional<Wrench> computeDampenedSpringInteraction(Vec3 pivotPoint, Vec3 objectPosition, double springSlack, Vec3 objectMotion, Vec3 leverArm) {
/* 283 */     double distance = objectPosition.distanceTo(pivotPoint);
/* 284 */     if (distance < springSlack) {
/* 285 */       return Optional.empty();
/*     */     }
/* 287 */     Vec3 displacement = pivotPoint.subtract(objectPosition).normalize().scale(distance - springSlack);
/* 288 */     double torque = Wrench.torqueFromForce(leverArm, displacement);
/*     */     
/* 290 */     boolean sameDirectionToMovement = (objectMotion.dot(displacement) >= 0.0D);
/* 291 */     if (sameDirectionToMovement) {
/* 292 */       displacement = displacement.scale(0.30000001192092896D);
/*     */     }
/*     */     
/* 295 */     return Optional.of(new Wrench(displacement, torque));
/*     */   }
/*     */ 
/*     */   
/* 299 */   default boolean supportQuadLeash() { return false; }
/*     */ 
/*     */ 
/*     */   
/* 303 */   default Vec3[] getQuadLeashOffsets() { return createQuadLeashOffsets((Entity)this, 0.0D, 0.5D, 0.5D, 0.5D); }
/*     */ 
/*     */   
/*     */   static Vec3[] createQuadLeashOffsets(Entity entity, double frontOffset, double frontBack, double leftRight, double height) {
/* 307 */     float width = entity.getBbWidth();
/* 308 */     double frontOffsetScaled = frontOffset * width;
/* 309 */     double frontBackScaled = frontBack * width;
/* 310 */     double leftRightScaled = leftRight * width;
/* 311 */     double heightScaled = height * entity.getBbHeight();
/*     */     
/* 313 */     return new Vec3[] { new Vec3(-leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled), new Vec3(-leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled), new Vec3(leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled), new Vec3(leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   default Vec3 getLeashOffset(float partialTicks) { return getLeashOffset(); }
/*     */ 
/*     */   
/*     */   default Vec3 getLeashOffset() {
/* 330 */     Entity entity = (Entity)this;
/* 331 */     return new Vec3(0.0D, entity.getEyeHeight(), (entity.getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   default void setLeashedTo(Entity holder, boolean synch) {
/* 335 */     if (this == holder) {
/*     */       return;
/*     */     }
/* 338 */     setLeashedTo((Entity)this, holder, synch);
/*     */   }
/*     */   
/*     */   private static <E extends Entity & Leashable> void setLeashedTo(E entity, Entity holder, boolean synch) {
/* 342 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 343 */     if (leashData == null) {
/* 344 */       leashData = new LeashData(holder);
/* 345 */       ((Leashable)entity).setLeashData(leashData);
/*     */     } else {
/* 347 */       Entity oldHolder = leashData.leashHolder;
/* 348 */       leashData.setLeashHolder(holder);
/* 349 */       if (oldHolder != null && oldHolder != holder) {
/* 350 */         oldHolder.notifyLeasheeRemoved((Leashable)entity);
/*     */       }
/*     */     } 
/*     */     
/* 354 */     if (synch) { Level level1 = entity.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 355 */         level.getChunkSource().sendToTrackingPlayers(entity, new ClientboundSetEntityLinkPacket(entity, holder)); }
/*     */        }
/*     */     
/* 358 */     if (entity.isPassenger()) {
/* 359 */       entity.stopRiding();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 365 */   default Entity getLeashHolder() { return getLeashHolder((Entity)this); }
/*     */ 
/*     */   
/*     */   private static <E extends Entity & Leashable> Entity getLeashHolder(E entity) {
/* 369 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 370 */     if (leashData == null) {
/* 371 */       return null;
/*     */     }
/*     */     
/* 374 */     if (leashData.delayedLeashHolderId != 0 && entity.level().isClientSide()) { Entity entity1 = entity.level().getEntity(leashData.delayedLeashHolderId); if (entity1 instanceof Entity) { Entity ntt = entity1;
/* 375 */         leashData.setLeashHolder(ntt); }
/*     */        }
/* 377 */      return leashData.leashHolder;
/*     */   }
/*     */ 
/*     */   
/* 381 */   static List<Leashable> leashableLeashedTo(Entity entity) { return leashableInArea(entity, l -> (l.getLeashHolder() == entity)); }
/*     */ 
/*     */ 
/*     */   
/* 385 */   static List<Leashable> leashableInArea(Entity entity, Predicate<Leashable> test) { return leashableInArea(entity.level(), entity.getBoundingBox().getCenter(), test); }
/*     */   LeashData getLeashData();
/*     */   
/*     */   static List<Leashable> leashableInArea(Level level, Vec3 pos, Predicate<Leashable> test) {
/* 389 */     double size = 32.0D;
/* 390 */     AABB scanArea = AABB.ofSize(pos, 32.0D, 32.0D, 32.0D);
/* 391 */     Objects.requireNonNull(Leashable.class); return level.getEntitiesOfClass(Entity.class, scanArea, e -> { if (e instanceof Leashable) { Leashable leashable = (Leashable)e; if (test.test(leashable)); }  return false; }).stream().map(Leashable.class::cast).toList();
/*     */   }
/*     */   void setLeashData(LeashData paramLeashData);
/*     */   
/* 395 */   public static final class LeashData { public static final Codec<LeashData> CODEC = Codec.xor(UUIDUtil.CODEC
/* 396 */         .fieldOf("UUID").codec(), BlockPos.CODEC)
/*     */       
/* 398 */       .xmap(LeashData::new, data -> {
/* 399 */           Entity patt0$temp = data.leashHolder; if (patt0$temp instanceof LeashFenceKnotEntity) { LeashFenceKnotEntity leashKnot = (LeashFenceKnotEntity)patt0$temp;
/*     */             
/* 401 */             return Either.right(leashKnot.getPos()); }
/* 402 */            if (data.leashHolder != null)
/*     */           {
/* 404 */             return Either.left(data.leashHolder.getUUID());
/*     */           }
/* 406 */           return (Either)Objects.requireNonNull(data.delayedLeashInfo, "Invalid LeashData had no attachment");
/*     */         }); private int delayedLeashHolderId;
/*     */     public Entity leashHolder;
/*     */     
/* 410 */     private LeashData(Either<UUID, BlockPos> delayedLeashInfo) { this.delayedLeashInfo = delayedLeashInfo; }
/*     */     public Either<UUID, BlockPos> delayedLeashInfo;
/*     */     public double angularMomentum;
/*     */     
/* 414 */     private LeashData(Entity entity) { this.leashHolder = entity; }
/*     */ 
/*     */ 
/*     */     
/* 418 */     private LeashData(int entityId) { this.delayedLeashHolderId = entityId; }
/*     */ 
/*     */     
/*     */     public void setLeashHolder(Entity leashHolder) {
/* 422 */       this.leashHolder = leashHolder;
/* 423 */       this.delayedLeashInfo = null;
/* 424 */       this.delayedLeashHolderId = 0;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static final class Wrench extends Record {
/*     */     private final Vec3 force;
/*     */     private final double torque;
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Leashable$Wrench;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #438	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Leashable$Wrench; }
/*     */     
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Leashable$Wrench;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #438	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Leashable$Wrench; }
/*     */     
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Leashable$Wrench;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #438	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     
/* 438 */     public Wrench(Vec3 force, double torque) { this.force = force; this.torque = torque; } public Vec3 force() { return this.force; } public double torque() { return this.torque; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 449 */     static Wrench ZERO = new Wrench(Vec3.ZERO, 0.0D);
/*     */ 
/*     */ 
/*     */     
/* 453 */     static double torqueFromForce(Vec3 leverArm, Vec3 force) { return leverArm.z * force.x - leverArm.x * force.z; }
/*     */ 
/*     */     
/*     */     static Wrench accumulate(List<Wrench> wrenches) {
/* 457 */       if (wrenches.isEmpty()) {
/* 458 */         return ZERO;
/*     */       }
/* 460 */       double x = 0.0D;
/* 461 */       double y = 0.0D;
/* 462 */       double z = 0.0D;
/* 463 */       double t = 0.0D;
/* 464 */       for (Wrench wrench : wrenches) {
/* 465 */         Vec3 force = wrench.force;
/* 466 */         x += force.x;
/* 467 */         y += force.y;
/* 468 */         z += force.z;
/* 469 */         t += wrench.torque;
/*     */       } 
/* 471 */       return new Wrench(new Vec3(x, y, z), t);
/*     */     }
/*     */ 
/*     */     
/* 475 */     public Wrench scale(double scale) { return new Wrench(this.force.scale(scale), this.torque * scale); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Leashable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
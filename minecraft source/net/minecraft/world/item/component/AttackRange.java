/*     */ package net.minecraft.world.item.component;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class AttackRange extends Record {
/*     */   private final float minRange;
/*     */   private final float maxRange;
/*     */   private final float minCreativeRange;
/*     */   
/*  28 */   public AttackRange(float minRange, float maxRange, float minCreativeRange, float maxCreativeRange, float hitboxMargin, float mobFactor) { this.minRange = minRange; this.maxRange = maxRange; this.minCreativeRange = minCreativeRange; this.maxCreativeRange = maxCreativeRange; this.hitboxMargin = hitboxMargin; this.mobFactor = mobFactor; } private final float maxCreativeRange; private final float hitboxMargin; private final float mobFactor; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/AttackRange;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/AttackRange; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/AttackRange;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/AttackRange; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/AttackRange;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/AttackRange;
/*  28 */     //   0	8	1	o	Ljava/lang/Object; } public float minRange() { return this.minRange; } public float maxRange() { return this.maxRange; } public float minCreativeRange() { return this.minCreativeRange; } public float maxCreativeRange() { return this.maxCreativeRange; } public float hitboxMargin() { return this.hitboxMargin; } public float mobFactor() { return this.mobFactor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final Codec<AttackRange> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  37 */         ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("min_reach", Float.valueOf(0.0F)).forGetter(AttackRange::minRange), 
/*  38 */         ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("max_reach", Float.valueOf(3.0F)).forGetter(AttackRange::maxRange), 
/*  39 */         ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("min_creative_reach", Float.valueOf(0.0F)).forGetter(AttackRange::minCreativeRange), 
/*  40 */         ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("max_creative_reach", Float.valueOf(5.0F)).forGetter(AttackRange::maxCreativeRange), 
/*  41 */         ExtraCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("hitbox_margin", Float.valueOf(0.3F)).forGetter(AttackRange::hitboxMargin), 
/*  42 */         Codec.floatRange(0.0F, 2.0F).optionalFieldOf("mob_factor", Float.valueOf(1.0F)).forGetter(AttackRange::mobFactor))
/*  43 */       .apply(i, AttackRange::new));
/*     */   
/*  45 */   public static final StreamCodec<ByteBuf, AttackRange> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, AttackRange::minRange, ByteBufCodecs.FLOAT, AttackRange::maxRange, ByteBufCodecs.FLOAT, AttackRange::minCreativeRange, ByteBufCodecs.FLOAT, AttackRange::maxCreativeRange, ByteBufCodecs.FLOAT, AttackRange::hitboxMargin, ByteBufCodecs.FLOAT, AttackRange::mobFactor, AttackRange::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AttackRange defaultFor(LivingEntity livingEntity) {
/*  57 */     return new AttackRange(0.0F, 
/*     */         
/*  59 */         (float)livingEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), 0.0F, 
/*     */         
/*  61 */         (float)livingEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HitResult getClosesetHit(Entity attacker, float partial, Predicate<Entity> matching) {
/*  68 */     Either<BlockHitResult, Collection<EntityHitResult>> result = ProjectileUtil.getHitEntitiesAlong(attacker, this, matching, ClipContext.Block.OUTLINE);
/*  69 */     if (result.left().isPresent()) {
/*  70 */       return (HitResult)result.left().get();
/*     */     }
/*     */     
/*  73 */     Collection<EntityHitResult> targets = (Collection)result.right().get();
/*     */     
/*  75 */     EntityHitResult entity = null;
/*  76 */     Vec3 attackerPos = attacker.getEyePosition(partial);
/*  77 */     double closestDistance = Double.MAX_VALUE;
/*  78 */     for (EntityHitResult target : targets) {
/*  79 */       double distance = attackerPos.distanceToSqr(target.getLocation());
/*  80 */       if (distance < closestDistance) {
/*  81 */         closestDistance = distance;
/*  82 */         entity = target;
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     if (entity != null) {
/*  87 */       return entity;
/*     */     }
/*     */     
/*  90 */     Vec3 eyeGaze = attacker.getHeadLookAngle();
/*  91 */     Vec3 missPosition = attacker.getEyePosition(partial).add(eyeGaze);
/*  92 */     return BlockHitResult.miss(missPosition, Direction.getApproximateNearest(eyeGaze), BlockPos.containing(missPosition));
/*     */   }
/*     */   
/*     */   public float effectiveMinRange(Entity entity) {
/*  96 */     if (entity instanceof Player) { Player player = (Player)entity;
/*  97 */       if (player.isSpectator()) {
/*  98 */         return 0.0F;
/*     */       }
/* 100 */       return player.isCreative() ? this.minCreativeRange : this.minRange; }
/*     */     
/* 102 */     return this.minRange * this.mobFactor;
/*     */   }
/*     */ 
/*     */   
/*     */   public float effectiveMaxRange(Entity entity) {
/* 107 */     if (entity instanceof Player) { Player player = (Player)entity;
/* 108 */       return player.isCreative() ? this.maxCreativeRange : this.maxRange; }
/*     */     
/* 110 */     return this.maxRange * this.mobFactor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean isInRange(LivingEntity attacker, Vec3 location) { Objects.requireNonNull(location); return isInRange(attacker, location::distanceToSqr, 0.0D); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean isInRange(LivingEntity attacker, AABB boundingBox, double extraBuffer) { Objects.requireNonNull(boundingBox); return isInRange(attacker, boundingBox::distanceToSqr, extraBuffer); }
/*     */ 
/*     */   
/*     */   private boolean isInRange(LivingEntity attacker, ToDoubleFunction<Vec3> distanceFunction, double extraBuffer) {
/* 123 */     double distance = Math.sqrt(distanceFunction.applyAsDouble(attacker.getEyePosition()));
/* 124 */     double minReach = (effectiveMinRange(attacker) - this.hitboxMargin) - extraBuffer;
/* 125 */     double maxReach = (effectiveMaxRange(attacker) + this.hitboxMargin) + extraBuffer;
/* 126 */     return (distance >= minReach && distance <= maxReach);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\AttackRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
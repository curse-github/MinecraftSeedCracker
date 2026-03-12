/*     */ package net.minecraft.world.item.component;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class KineticWeapon extends Record {
/*     */   private final int contactCooldownTicks;
/*     */   private final int delayTicks;
/*     */   private final Optional<Condition> dismountConditions;
/*     */   private final Optional<Condition> knockbackConditions;
/*     */   private final Optional<Condition> damageConditions;
/*     */   
/*  32 */   public KineticWeapon(int contactCooldownTicks, int delayTicks, Optional<Condition> dismountConditions, Optional<Condition> knockbackConditions, Optional<Condition> damageConditions, float forwardMovement, float damageMultiplier, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) { this.contactCooldownTicks = contactCooldownTicks; this.delayTicks = delayTicks; this.dismountConditions = dismountConditions; this.knockbackConditions = knockbackConditions; this.damageConditions = damageConditions; this.forwardMovement = forwardMovement; this.damageMultiplier = damageMultiplier; this.sound = sound; this.hitSound = hitSound; } private final float forwardMovement; private final float damageMultiplier; private final Optional<Holder<SoundEvent>> sound; private final Optional<Holder<SoundEvent>> hitSound; public static final int HIT_FEEDBACK_TICKS = 10; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/KineticWeapon;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/KineticWeapon;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/KineticWeapon;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/KineticWeapon;
/*  32 */     //   0	8	1	o	Ljava/lang/Object; } public int contactCooldownTicks() { return this.contactCooldownTicks; } public int delayTicks() { return this.delayTicks; } public Optional<Condition> dismountConditions() { return this.dismountConditions; } public Optional<Condition> knockbackConditions() { return this.knockbackConditions; } public Optional<Condition> damageConditions() { return this.damageConditions; } public float forwardMovement() { return this.forwardMovement; } public float damageMultiplier() { return this.damageMultiplier; } public Optional<Holder<SoundEvent>> sound() { return this.sound; } public Optional<Holder<SoundEvent>> hitSound() { return this.hitSound; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static final Codec<KineticWeapon> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/*  46 */         .optionalFieldOf("contact_cooldown_ticks", Integer.valueOf(10)).forGetter(KineticWeapon::contactCooldownTicks), ExtraCodecs.NON_NEGATIVE_INT
/*  47 */         .optionalFieldOf("delay_ticks", Integer.valueOf(0)).forGetter(KineticWeapon::delayTicks), Condition.CODEC
/*  48 */         .optionalFieldOf("dismount_conditions").forGetter(KineticWeapon::dismountConditions), Condition.CODEC
/*  49 */         .optionalFieldOf("knockback_conditions").forGetter(KineticWeapon::knockbackConditions), Condition.CODEC
/*  50 */         .optionalFieldOf("damage_conditions").forGetter(KineticWeapon::damageConditions), Codec.FLOAT
/*  51 */         .optionalFieldOf("forward_movement", Float.valueOf(0.0F)).forGetter(KineticWeapon::forwardMovement), Codec.FLOAT
/*  52 */         .optionalFieldOf("damage_multiplier", Float.valueOf(1.0F)).forGetter(KineticWeapon::damageMultiplier), SoundEvent.CODEC
/*  53 */         .optionalFieldOf("sound").forGetter(KineticWeapon::sound), SoundEvent.CODEC
/*  54 */         .optionalFieldOf("hit_sound").forGetter(KineticWeapon::hitSound))
/*  55 */       .apply(i, KineticWeapon::new));
/*  56 */   public static final StreamCodec<RegistryFriendlyByteBuf, KineticWeapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, KineticWeapon::contactCooldownTicks, ByteBufCodecs.VAR_INT, KineticWeapon::delayTicks, Condition.STREAM_CODEC
/*     */ 
/*     */       
/*  59 */       .apply(ByteBufCodecs::optional), KineticWeapon::dismountConditions, Condition.STREAM_CODEC
/*  60 */       .apply(ByteBufCodecs::optional), KineticWeapon::knockbackConditions, Condition.STREAM_CODEC
/*  61 */       .apply(ByteBufCodecs::optional), KineticWeapon::damageConditions, ByteBufCodecs.FLOAT, KineticWeapon::forwardMovement, ByteBufCodecs.FLOAT, KineticWeapon::damageMultiplier, SoundEvent.STREAM_CODEC
/*     */ 
/*     */       
/*  64 */       .apply(ByteBufCodecs::optional), KineticWeapon::sound, SoundEvent.STREAM_CODEC
/*  65 */       .apply(ByteBufCodecs::optional), KineticWeapon::hitSound, KineticWeapon::new);
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vec3 getMotion(Entity livingEntity) {
/*  70 */     if (!(livingEntity instanceof net.minecraft.world.entity.player.Player) && livingEntity.isPassenger()) {
/*  71 */       livingEntity = livingEntity.getRootVehicle();
/*     */     }
/*  73 */     return livingEntity.getKnownSpeed().scale(20.0D);
/*     */   }
/*     */ 
/*     */   
/*  77 */   public void makeSound(Entity causer) { this.sound.ifPresent(s -> causer.level().playSound(causer, causer.getX(), causer.getY(), causer.getZ(), s, causer.getSoundSource(), 1.0F, 1.0F)); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public void makeLocalHitSound(Entity causer) { this.hitSound.ifPresent(s -> causer.level().playLocalSound(causer, (SoundEvent)s.value(), causer.getSoundSource(), 1.0F, 1.0F)); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public int computeDamageUseDuration() { return this.delayTicks + ((Integer)this.damageConditions.map(Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue(); }
/*     */ 
/*     */   
/*     */   public void damageEntities(ItemStack stack, int ticksRemaining, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
/*  89 */     int ticksUsed = stack.getUseDuration(livingEntity) - ticksRemaining;
/*  90 */     if (ticksUsed < this.delayTicks) {
/*     */       return;
/*     */     }
/*  93 */     ticksUsed -= this.delayTicks;
/*     */     
/*  95 */     Vec3 attackerLookVector = livingEntity.getLookAngle();
/*  96 */     double attackerSpeedProjection = attackerLookVector.dot(getMotion(livingEntity));
/*  97 */     float actionFactor = (livingEntity instanceof net.minecraft.world.entity.player.Player) ? 1.0F : 0.2F;
/*  98 */     AttackRange attackRange = livingEntity.entityAttackRange();
/*     */ 
/*     */ 
/*     */     
/* 102 */     double baseMobDamage = livingEntity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/*     */     
/* 104 */     boolean affected = false;
/* 105 */     for (EntityHitResult hitResult : (Collection)ProjectileUtil.getHitEntitiesAlong(livingEntity, attackRange, e -> PiercingWeapon.canHitEntity(livingEntity, e), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e)) {
/* 106 */       EnderDragon enderDragon = hitResult.getEntity();
/* 107 */       if (enderDragon instanceof EnderDragonPart) { EnderDragonPart dragonPart = (EnderDragonPart)enderDragon;
/* 108 */         enderDragon = dragonPart.parentMob; }
/*     */       
/* 110 */       boolean wasStabbed = livingEntity.wasRecentlyStabbed(enderDragon, this.contactCooldownTicks);
/* 111 */       if (wasStabbed) {
/*     */         continue;
/*     */       }
/* 114 */       livingEntity.rememberStabbedEntity(enderDragon);
/* 115 */       double targetSpeedProjection = attackerLookVector.dot(getMotion(enderDragon));
/* 116 */       double relativeSpeed = Math.max(0.0D, attackerSpeedProjection - targetSpeedProjection);
/*     */       
/* 118 */       boolean dealsDismount = (this.dismountConditions.isPresent() && ((Condition)this.dismountConditions.get()).test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor));
/* 119 */       boolean dealsKnockback = (this.knockbackConditions.isPresent() && ((Condition)this.knockbackConditions.get()).test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor));
/* 120 */       boolean dealsDamage = (this.damageConditions.isPresent() && ((Condition)this.damageConditions.get()).test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor));
/*     */       
/* 122 */       if (!dealsDismount && !dealsKnockback && !dealsDamage) {
/*     */         continue;
/*     */       }
/*     */       
/* 126 */       float damageDealt = (float)baseMobDamage + Mth.floor(relativeSpeed * this.damageMultiplier);
/*     */       
/* 128 */       affected |= livingEntity.stabAttack(equipmentSlot, enderDragon, damageDealt, dealsDamage, dealsKnockback, dealsDismount);
/*     */     } 
/*     */     
/* 131 */     if (affected) {
/* 132 */       livingEntity.level().broadcastEntityEvent(livingEntity, (byte)2);
/* 133 */       if (livingEntity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)livingEntity;
/* 134 */         CriteriaTriggers.SPEAR_MOBS_TRIGGER.trigger(player, livingEntity.stabbedEntities(e -> e instanceof LivingEntity)); }
/*     */     
/*     */     } 
/*     */   }
/*     */   public static final class Condition extends Record { private final int maxDurationTicks; private final float minSpeed; private final float minRelativeSpeed;
/* 139 */     public Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) { this.maxDurationTicks = maxDurationTicks; this.minSpeed = minSpeed; this.minRelativeSpeed = minRelativeSpeed; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition;
/* 139 */       //   0	8	1	o	Ljava/lang/Object; } public int maxDurationTicks() { return this.maxDurationTicks; } public float minSpeed() { return this.minSpeed; } public float minRelativeSpeed() { return this.minRelativeSpeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     public static final Codec<Condition> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 145 */           .fieldOf("max_duration_ticks").forGetter(Condition::maxDurationTicks), Codec.FLOAT
/* 146 */           .optionalFieldOf("min_speed", Float.valueOf(0.0F)).forGetter(Condition::minSpeed), Codec.FLOAT
/* 147 */           .optionalFieldOf("min_relative_speed", Float.valueOf(0.0F)).forGetter(Condition::minRelativeSpeed))
/* 148 */         .apply(i, Condition::new));
/*     */     
/* 150 */     public static final StreamCodec<ByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Condition::maxDurationTicks, ByteBufCodecs.FLOAT, Condition::minSpeed, ByteBufCodecs.FLOAT, Condition::minRelativeSpeed, Condition::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     public boolean test(int ticksUsed, double attackerSpeed, double relativeSpeed, double entityFactor) { return (ticksUsed <= this.maxDurationTicks && attackerSpeed >= this.minSpeed * entityFactor && relativeSpeed >= this.minRelativeSpeed * entityFactor); }
/*     */ 
/*     */ 
/*     */     
/* 162 */     public static Optional<Condition> ofAttackerSpeed(int untilTicks, float minAttackerSpeed) { return Optional.of(new Condition(untilTicks, minAttackerSpeed, 0.0F)); }
/*     */ 
/*     */ 
/*     */     
/* 166 */     public static Optional<Condition> ofRelativeSpeed(int untilTicks, float minRelativeSpeed) { return Optional.of(new Condition(untilTicks, 0.0F, minRelativeSpeed)); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\KineticWeapon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
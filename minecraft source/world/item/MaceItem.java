/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MaceItem
/*     */   extends Item
/*     */ {
/*     */   private static final int DEFAULT_ATTACK_DAMAGE = 5;
/*     */   private static final float DEFAULT_ATTACK_SPEED = -3.4F;
/*     */   public static final float SMASH_ATTACK_FALL_THRESHOLD = 1.5F;
/*     */   private static final float SMASH_ATTACK_HEAVY_THRESHOLD = 5.0F;
/*     */   public static final float SMASH_ATTACK_KNOCKBACK_RADIUS = 3.5F;
/*     */   private static final float SMASH_ATTACK_KNOCKBACK_POWER = 0.7F;
/*     */   
/*  40 */   public MaceItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static ItemAttributeModifiers createAttributes() { return ItemAttributeModifiers.builder()
/*  45 */       .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 5.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  46 */       .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.4000000953674316D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  47 */       .build(); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static Tool createToolProperties() { return new Tool(List.of(), 1.0F, 2, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
/*  56 */     if (canSmashAttack(attacker)) {
/*  57 */       ServerLevel level = (ServerLevel)attacker.level();
/*     */       
/*  59 */       attacker.setDeltaMovement(attacker.getDeltaMovement().with(Direction.Axis.Y, 0.009999999776482582D));
/*  60 */       if (attacker instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)attacker;
/*  61 */         player.currentImpulseImpactPos = calculateImpactPosition(player);
/*  62 */         player.setIgnoreFallDamageFromCurrentImpulse(true);
/*  63 */         player.connection.send(new ClientboundSetEntityMotionPacket(player)); }
/*     */ 
/*     */       
/*  66 */       if (mob.onGround()) {
/*  67 */         if (attacker instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)attacker;
/*  68 */           player.setSpawnExtraParticlesOnFall(true); }
/*     */         
/*  70 */         SoundEvent sound = (attacker.fallDistance > 5.0D) ? SoundEvents.MACE_SMASH_GROUND_HEAVY : SoundEvents.MACE_SMASH_GROUND;
/*  71 */         level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), sound, attacker.getSoundSource(), 1.0F, 1.0F);
/*     */       } else {
/*  73 */         level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.MACE_SMASH_AIR, attacker.getSoundSource(), 1.0F, 1.0F);
/*     */       } 
/*     */       
/*  76 */       knockback(level, attacker, mob);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3 calculateImpactPosition(ServerPlayer player) {
/*  81 */     if (player.isIgnoringFallDamageFromCurrentImpulse() && player.currentImpulseImpactPos != null && player.currentImpulseImpactPos.y <= 
/*     */       
/*  83 */       (player.position()).y)
/*     */     {
/*  85 */       return player.currentImpulseImpactPos;
/*     */     }
/*  87 */     return player.position();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
/*  94 */     if (canSmashAttack(attacker)) {
/*  95 */       attacker.resetFallDistance();
/*     */     }
/*     */   }
/*     */   
/*     */   public float getAttackDamageBonus(Entity victim, float ignoredDamage, DamageSource damageSource) {
/*     */     double damage;
/*     */     LivingEntity attacker;
/* 102 */     Entity entity = damageSource.getDirectEntity(); if (entity instanceof LivingEntity) { attacker = (LivingEntity)entity; }
/* 103 */     else { return 0.0F; }
/*     */     
/* 105 */     if (!canSmashAttack(attacker)) {
/* 106 */       return 0.0F;
/*     */     }
/*     */     
/* 109 */     double fallHeightThreshold1 = 3.0D;
/* 110 */     double fallHeightThreshold2 = 8.0D;
/*     */     
/* 112 */     double fallDistance = attacker.fallDistance;
/*     */ 
/*     */     
/* 115 */     if (fallDistance <= 3.0D) {
/* 116 */       damage = 4.0D * fallDistance;
/* 117 */     } else if (fallDistance <= 8.0D) {
/* 118 */       damage = 12.0D + 2.0D * (fallDistance - 3.0D);
/*     */     } else {
/* 120 */       damage = 22.0D + fallDistance - 8.0D;
/*     */     } 
/* 122 */     Level level1 = attacker.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 123 */       return (float)(damage + EnchantmentHelper.modifyFallBasedDamage(level, attacker.getWeaponItem(), victim, damageSource, 0.0F) * fallDistance); }
/*     */     
/* 125 */     return (float)damage;
/*     */   }
/*     */   
/*     */   private static void knockback(Level level, Entity attacker, Entity entity) {
/* 129 */     level.levelEvent(2013, entity.getOnPos(), 750);
/*     */     
/* 131 */     level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5D), knockbackPredicate(attacker, entity))
/* 132 */       .forEach(nearby -> {
/* 133 */           Vec3 direction = nearby.position().subtract(entity.position());
/* 134 */           double knockbackPower = getKnockbackPower(attacker, nearby, direction);
/* 135 */           Vec3 knockbackVector = direction.normalize().scale(knockbackPower);
/*     */           
/* 137 */           if (knockbackPower > 0.0D) {
/* 138 */             nearby.push(knockbackVector.x, 0.699999988079071D, knockbackVector.z);
/* 139 */             if (nearby instanceof ServerPlayer) { ServerPlayer otherPlayer = (ServerPlayer)nearby;
/* 140 */               otherPlayer.connection.send(new ClientboundSetEntityMotionPacket(otherPlayer)); }
/*     */           
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static Predicate<LivingEntity> knockbackPredicate(Entity attacker, Entity entity) {
/* 147 */     return nearby -> {
/*     */         // Byte code:
/*     */         //   0: aload_2
/*     */         //   1: invokevirtual isSpectator : ()Z
/*     */         //   4: ifne -> 11
/*     */         //   7: iconst_1
/*     */         //   8: goto -> 12
/*     */         //   11: iconst_0
/*     */         //   12: istore_3
/*     */         //   13: aload_2
/*     */         //   14: aload_0
/*     */         //   15: if_acmpeq -> 27
/*     */         //   18: aload_2
/*     */         //   19: aload_1
/*     */         //   20: if_acmpeq -> 27
/*     */         //   23: iconst_1
/*     */         //   24: goto -> 28
/*     */         //   27: iconst_0
/*     */         //   28: istore #4
/*     */         //   30: aload_0
/*     */         //   31: aload_2
/*     */         //   32: invokevirtual isAlliedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*     */         //   35: ifne -> 42
/*     */         //   38: iconst_1
/*     */         //   39: goto -> 43
/*     */         //   42: iconst_0
/*     */         //   43: istore #5
/*     */         //   45: aload_2
/*     */         //   46: instanceof net/minecraft/world/entity/TamableAnimal
/*     */         //   49: ifeq -> 93
/*     */         //   52: aload_2
/*     */         //   53: checkcast net/minecraft/world/entity/TamableAnimal
/*     */         //   56: astore #8
/*     */         //   58: aload_1
/*     */         //   59: instanceof net/minecraft/world/entity/LivingEntity
/*     */         //   62: ifeq -> 93
/*     */         //   65: aload_1
/*     */         //   66: checkcast net/minecraft/world/entity/LivingEntity
/*     */         //   69: astore #7
/*     */         //   71: aload #8
/*     */         //   73: invokevirtual isTame : ()Z
/*     */         //   76: ifeq -> 93
/*     */         //   79: aload #8
/*     */         //   81: aload #7
/*     */         //   83: invokevirtual isOwnedBy : (Lnet/minecraft/world/entity/LivingEntity;)Z
/*     */         //   86: ifeq -> 93
/*     */         //   89: iconst_1
/*     */         //   90: goto -> 94
/*     */         //   93: iconst_0
/*     */         //   94: ifne -> 101
/*     */         //   97: iconst_1
/*     */         //   98: goto -> 102
/*     */         //   101: iconst_0
/*     */         //   102: istore #6
/*     */         //   104: aload_2
/*     */         //   105: instanceof net/minecraft/world/entity/decoration/ArmorStand
/*     */         //   108: ifeq -> 125
/*     */         //   111: aload_2
/*     */         //   112: checkcast net/minecraft/world/entity/decoration/ArmorStand
/*     */         //   115: astore #8
/*     */         //   117: aload #8
/*     */         //   119: invokevirtual isMarker : ()Z
/*     */         //   122: ifne -> 129
/*     */         //   125: iconst_1
/*     */         //   126: goto -> 130
/*     */         //   129: iconst_0
/*     */         //   130: istore #7
/*     */         //   132: aload_1
/*     */         //   133: aload_2
/*     */         //   134: invokevirtual distanceToSqr : (Lnet/minecraft/world/entity/Entity;)D
/*     */         //   137: ldc2_w 3.5
/*     */         //   140: ldc2_w 2.0
/*     */         //   143: invokestatic pow : (DD)D
/*     */         //   146: dcmpg
/*     */         //   147: ifgt -> 154
/*     */         //   150: iconst_1
/*     */         //   151: goto -> 155
/*     */         //   154: iconst_0
/*     */         //   155: istore #8
/*     */         //   157: aload_2
/*     */         //   158: instanceof net/minecraft/world/entity/player/Player
/*     */         //   161: ifeq -> 193
/*     */         //   164: aload_2
/*     */         //   165: checkcast net/minecraft/world/entity/player/Player
/*     */         //   168: astore #10
/*     */         //   170: aload #10
/*     */         //   172: invokevirtual isCreative : ()Z
/*     */         //   175: ifeq -> 193
/*     */         //   178: aload #10
/*     */         //   180: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */         //   183: getfield flying : Z
/*     */         //   186: ifeq -> 193
/*     */         //   189: iconst_1
/*     */         //   190: goto -> 194
/*     */         //   193: iconst_0
/*     */         //   194: ifne -> 201
/*     */         //   197: iconst_1
/*     */         //   198: goto -> 202
/*     */         //   201: iconst_0
/*     */         //   202: istore #9
/*     */         //   204: iload_3
/*     */         //   205: ifeq -> 242
/*     */         //   208: iload #4
/*     */         //   210: ifeq -> 242
/*     */         //   213: iload #5
/*     */         //   215: ifeq -> 242
/*     */         //   218: iload #6
/*     */         //   220: ifeq -> 242
/*     */         //   223: iload #7
/*     */         //   225: ifeq -> 242
/*     */         //   228: iload #8
/*     */         //   230: ifeq -> 242
/*     */         //   233: iload #9
/*     */         //   235: ifeq -> 242
/*     */         //   238: iconst_1
/*     */         //   239: goto -> 243
/*     */         //   242: iconst_0
/*     */         //   243: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #148	-> 0
/*     */         //   #149	-> 13
/*     */         //   #150	-> 30
/*     */         //   #151	-> 45
/*     */         //   #152	-> 104
/*     */         //   #153	-> 132
/*     */         //   #154	-> 157
/*     */         //   #156	-> 204
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   71	22	7	livingAttacker	Lnet/minecraft/world/entity/LivingEntity;
/*     */         //   58	35	8	animal	Lnet/minecraft/world/entity/TamableAnimal;
/*     */         //   117	8	8	armorStand	Lnet/minecraft/world/entity/decoration/ArmorStand;
/*     */         //   129	1	8	armorStand	Lnet/minecraft/world/entity/decoration/ArmorStand;
/*     */         //   170	23	10	player	Lnet/minecraft/world/entity/player/Player;
/*     */         //   0	244	0	attacker	Lnet/minecraft/world/entity/Entity;
/*     */         //   0	244	1	entity	Lnet/minecraft/world/entity/Entity;
/*     */         //   0	244	2	nearby	Lnet/minecraft/world/entity/LivingEntity;
/*     */         //   13	231	3	notSpectator	Z
/*     */         //   30	214	4	notPlayer	Z
/*     */         //   45	199	5	notAlliedToPlayer	Z
/*     */         //   104	140	6	notTamedByPlayer	Z
/*     */         //   132	112	7	notArmorStand	Z
/*     */         //   157	87	8	withinRange	Z
/*     */         //   204	40	9	notFlyingInCreative	Z
/*     */       };
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
/* 161 */   private static double getKnockbackPower(Entity attacker, LivingEntity nearby, Vec3 direction) { return (3.5D - direction.length()) * 0.699999988079071D * ((attacker.fallDistance > 5.0D) ? 2 : true) * (1.0D - nearby.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)); }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public static boolean canSmashAttack(LivingEntity attacker) { return (attacker.fallDistance > 1.5D && !attacker.isFallFlying()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public DamageSource getItemDamageSource(LivingEntity attacker) {
/* 170 */     if (canSmashAttack(attacker)) {
/* 171 */       return attacker.damageSources().mace(attacker);
/*     */     }
/* 173 */     return super.getItemDamageSource(attacker);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\MaceItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
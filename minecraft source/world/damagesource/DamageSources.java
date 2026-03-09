/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DamageSources
/*     */ {
/*     */   private final Registry<DamageType> damageTypes;
/*     */   private final DamageSource inFire;
/*     */   private final DamageSource campfire;
/*     */   private final DamageSource lightningBolt;
/*     */   private final DamageSource onFire;
/*     */   private final DamageSource lava;
/*     */   private final DamageSource hotFloor;
/*     */   private final DamageSource inWall;
/*     */   private final DamageSource cramming;
/*     */   private final DamageSource drown;
/*     */   private final DamageSource starve;
/*     */   private final DamageSource cactus;
/*     */   private final DamageSource fall;
/*     */   private final DamageSource enderPearl;
/*     */   private final DamageSource flyIntoWall;
/*     */   private final DamageSource fellOutOfWorld;
/*     */   private final DamageSource generic;
/*     */   private final DamageSource magic;
/*     */   private final DamageSource wither;
/*     */   private final DamageSource dragonBreath;
/*     */   private final DamageSource dryOut;
/*     */   private final DamageSource sweetBerryBush;
/*     */   private final DamageSource freeze;
/*     */   private final DamageSource stalagmite;
/*     */   private final DamageSource outsideBorder;
/*     */   private final DamageSource genericKill;
/*     */   
/*     */   public DamageSources(RegistryAccess registries) {
/*  47 */     this.damageTypes = registries.lookupOrThrow(Registries.DAMAGE_TYPE);
/*  48 */     this.inFire = source(DamageTypes.IN_FIRE);
/*  49 */     this.campfire = source(DamageTypes.CAMPFIRE);
/*  50 */     this.lightningBolt = source(DamageTypes.LIGHTNING_BOLT);
/*  51 */     this.onFire = source(DamageTypes.ON_FIRE);
/*  52 */     this.lava = source(DamageTypes.LAVA);
/*  53 */     this.hotFloor = source(DamageTypes.HOT_FLOOR);
/*  54 */     this.inWall = source(DamageTypes.IN_WALL);
/*  55 */     this.cramming = source(DamageTypes.CRAMMING);
/*  56 */     this.drown = source(DamageTypes.DROWN);
/*  57 */     this.starve = source(DamageTypes.STARVE);
/*  58 */     this.cactus = source(DamageTypes.CACTUS);
/*  59 */     this.fall = source(DamageTypes.FALL);
/*  60 */     this.enderPearl = source(DamageTypes.ENDER_PEARL);
/*  61 */     this.flyIntoWall = source(DamageTypes.FLY_INTO_WALL);
/*  62 */     this.fellOutOfWorld = source(DamageTypes.FELL_OUT_OF_WORLD);
/*  63 */     this.generic = source(DamageTypes.GENERIC);
/*  64 */     this.magic = source(DamageTypes.MAGIC);
/*  65 */     this.wither = source(DamageTypes.WITHER);
/*  66 */     this.dragonBreath = source(DamageTypes.DRAGON_BREATH);
/*  67 */     this.dryOut = source(DamageTypes.DRY_OUT);
/*  68 */     this.sweetBerryBush = source(DamageTypes.SWEET_BERRY_BUSH);
/*  69 */     this.freeze = source(DamageTypes.FREEZE);
/*  70 */     this.stalagmite = source(DamageTypes.STALAGMITE);
/*  71 */     this.outsideBorder = source(DamageTypes.OUTSIDE_BORDER);
/*  72 */     this.genericKill = source(DamageTypes.GENERIC_KILL);
/*     */   }
/*     */ 
/*     */   
/*  76 */   private DamageSource source(ResourceKey<DamageType> key) { return new DamageSource(this.damageTypes.getOrThrow(key)); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   private DamageSource source(ResourceKey<DamageType> key, Entity cause) { return new DamageSource(this.damageTypes.getOrThrow(key), cause); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   private DamageSource source(ResourceKey<DamageType> key, Entity directEntity, Entity causingEntity) { return new DamageSource(this.damageTypes.getOrThrow(key), directEntity, causingEntity); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public DamageSource inFire() { return this.inFire; }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public DamageSource campfire() { return this.campfire; }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public DamageSource lightningBolt() { return this.lightningBolt; }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public DamageSource onFire() { return this.onFire; }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public DamageSource lava() { return this.lava; }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public DamageSource hotFloor() { return this.hotFloor; }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public DamageSource inWall() { return this.inWall; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public DamageSource cramming() { return this.cramming; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public DamageSource drown() { return this.drown; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public DamageSource starve() { return this.starve; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public DamageSource cactus() { return this.cactus; }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public DamageSource fall() { return this.fall; }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public DamageSource enderPearl() { return this.enderPearl; }
/*     */ 
/*     */ 
/*     */   
/* 140 */   public DamageSource flyIntoWall() { return this.flyIntoWall; }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public DamageSource fellOutOfWorld() { return this.fellOutOfWorld; }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public DamageSource generic() { return this.generic; }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public DamageSource magic() { return this.magic; }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public DamageSource wither() { return this.wither; }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public DamageSource dragonBreath() { return this.dragonBreath; }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public DamageSource dryOut() { return this.dryOut; }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public DamageSource sweetBerryBush() { return this.sweetBerryBush; }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public DamageSource freeze() { return this.freeze; }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public DamageSource stalagmite() { return this.stalagmite; }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public DamageSource fallingBlock(Entity entity) { return source(DamageTypes.FALLING_BLOCK, entity); }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public DamageSource anvil(Entity entity) { return source(DamageTypes.FALLING_ANVIL, entity); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public DamageSource fallingStalactite(Entity entity) { return source(DamageTypes.FALLING_STALACTITE, entity); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public DamageSource sting(LivingEntity mob) { return source(DamageTypes.STING, mob); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public DamageSource mobAttack(LivingEntity mob) { return source(DamageTypes.MOB_ATTACK, mob); }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public DamageSource noAggroMobAttack(LivingEntity mob) { return source(DamageTypes.MOB_ATTACK_NO_AGGRO, mob); }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public DamageSource playerAttack(Player player) { return source(DamageTypes.PLAYER_ATTACK, player); }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public DamageSource arrow(AbstractArrow arrow, Entity owner) { return source(DamageTypes.ARROW, arrow, owner); }
/*     */ 
/*     */ 
/*     */   
/* 212 */   public DamageSource trident(Entity trident, Entity owner) { return source(DamageTypes.TRIDENT, trident, owner); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public DamageSource mobProjectile(Entity entity, LivingEntity mob) { return source(DamageTypes.MOB_PROJECTILE, entity, mob); }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public DamageSource spit(Entity entity, LivingEntity mob) { return source(DamageTypes.SPIT, entity, mob); }
/*     */ 
/*     */ 
/*     */   
/* 225 */   public DamageSource windCharge(Entity entity, LivingEntity mob) { return source(DamageTypes.WIND_CHARGE, entity, mob); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   public DamageSource fireworks(FireworkRocketEntity rocket, Entity owner) { return source(DamageTypes.FIREWORKS, rocket, owner); }
/*     */ 
/*     */   
/*     */   public DamageSource fireball(Fireball fireball, Entity owner) {
/* 233 */     if (owner == null) {
/* 234 */       return source(DamageTypes.UNATTRIBUTED_FIREBALL, fireball);
/*     */     }
/* 236 */     return source(DamageTypes.FIREBALL, fireball, owner);
/*     */   }
/*     */ 
/*     */   
/* 240 */   public DamageSource witherSkull(WitherSkull witherSkull, Entity owner) { return source(DamageTypes.WITHER_SKULL, witherSkull, owner); }
/*     */ 
/*     */ 
/*     */   
/* 244 */   public DamageSource thrown(Entity entity, Entity owner) { return source(DamageTypes.THROWN, entity, owner); }
/*     */ 
/*     */ 
/*     */   
/* 248 */   public DamageSource indirectMagic(Entity entity, Entity owner) { return source(DamageTypes.INDIRECT_MAGIC, entity, owner); }
/*     */ 
/*     */ 
/*     */   
/* 252 */   public DamageSource thorns(Entity source) { return source(DamageTypes.THORNS, source); }
/*     */ 
/*     */ 
/*     */   
/* 256 */   public DamageSource explosion(Explosion explosion) { return (explosion != null) ? explosion(explosion.getDirectSourceEntity(), explosion.getIndirectSourceEntity()) : explosion(null, null); }
/*     */ 
/*     */ 
/*     */   
/* 260 */   public DamageSource explosion(Entity entity, Entity cause) { return source((cause != null && entity != null) ? DamageTypes.PLAYER_EXPLOSION : DamageTypes.EXPLOSION, entity, cause); }
/*     */ 
/*     */ 
/*     */   
/* 264 */   public DamageSource sonicBoom(Entity entity) { return source(DamageTypes.SONIC_BOOM, entity); }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public DamageSource badRespawnPointExplosion(Vec3 boomPos) { return new DamageSource(this.damageTypes.getOrThrow(DamageTypes.BAD_RESPAWN_POINT), boomPos); }
/*     */ 
/*     */ 
/*     */   
/* 272 */   public DamageSource outOfBorder() { return this.outsideBorder; }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public DamageSource genericKill() { return this.genericKill; }
/*     */ 
/*     */ 
/*     */   
/* 280 */   public DamageSource mace(Entity owner) { return source(DamageTypes.MACE_SMASH, owner); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\DamageSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
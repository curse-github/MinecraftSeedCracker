/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class ServerExplosion
/*     */   implements Explosion
/*     */ {
/*  41 */   private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
/*     */   
/*     */   private static final int MAX_DROPS_PER_COMBINED_STACK = 16;
/*     */   
/*     */   private static final float LARGE_EXPLOSION_RADIUS = 2.0F;
/*     */   
/*     */   private final boolean fire;
/*     */   
/*     */   private final Explosion.BlockInteraction blockInteraction;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   public ServerExplosion(ServerLevel level, Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, Vec3 center, float radius, boolean fire, Explosion.BlockInteraction blockInteraction) {
/*  54 */     this.hitPlayers = new HashMap();
/*     */ 
/*     */     
/*  57 */     this.level = level;
/*  58 */     this.source = source;
/*  59 */     this.radius = radius;
/*  60 */     this.center = center;
/*  61 */     this.fire = fire;
/*  62 */     this.blockInteraction = blockInteraction;
/*  63 */     this.damageSource = (damageSource == null) ? level.damageSources().explosion(this) : damageSource;
/*  64 */     this.damageCalculator = (damageCalculator == null) ? makeDamageCalculator(source) : damageCalculator;
/*     */   }
/*     */   private final Vec3 center; private final Entity source; private final float radius; private final DamageSource damageSource; private final ExplosionDamageCalculator damageCalculator; private final Map<Player, Vec3> hitPlayers;
/*     */   
/*  68 */   private ExplosionDamageCalculator makeDamageCalculator(Entity source) { return (source == null) ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(source); }
/*     */ 
/*     */   
/*     */   public static float getSeenPercent(Vec3 center, Entity entity) {
/*  72 */     AABB bb = entity.getBoundingBox();
/*  73 */     double xs = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
/*  74 */     double ys = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
/*  75 */     double zs = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
/*     */     
/*  77 */     double xOffset = (1.0D - Math.floor(1.0D / xs) * xs) / 2.0D;
/*  78 */     double zOffset = (1.0D - Math.floor(1.0D / zs) * zs) / 2.0D;
/*     */     
/*  80 */     if (xs < 0.0D || ys < 0.0D || zs < 0.0D) {
/*  81 */       return 0.0F;
/*     */     }
/*  83 */     int hits = 0;
/*  84 */     int count = 0; double xx;
/*  85 */     for (xx = 0.0D; xx <= 1.0D; xx += xs) {
/*  86 */       double yy; for (yy = 0.0D; yy <= 1.0D; yy += ys) {
/*  87 */         double zz; for (zz = 0.0D; zz <= 1.0D; zz += zs) {
/*  88 */           double x = Mth.lerp(xx, bb.minX, bb.maxX);
/*  89 */           double y = Mth.lerp(yy, bb.minY, bb.maxY);
/*  90 */           double z = Mth.lerp(zz, bb.minZ, bb.maxZ);
/*  91 */           Vec3 from = new Vec3(x + xOffset, y, z + zOffset);
/*  92 */           if (entity.level().clip(new ClipContext(from, center, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS) {
/*  93 */             hits++;
/*     */           }
/*  95 */           count++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     return hits / count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public float radius() { return this.radius; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public Vec3 center() { return this.center; }
/*     */ 
/*     */   
/*     */   private List<BlockPos> calculateExplodedPositions() {
/* 114 */     Set<BlockPos> toBlowSet = new HashSet<BlockPos>();
/*     */     
/* 116 */     int size = 16;
/* 117 */     for (int xx = 0; xx < 16; xx++) {
/* 118 */       for (int yy = 0; yy < 16; yy++) {
/* 119 */         for (int zz = 0; zz < 16; zz++) {
/* 120 */           if (xx == 0 || xx == 15 || yy == 0 || yy == 15 || zz == 0 || zz == 15) {
/*     */ 
/*     */ 
/*     */             
/* 124 */             double xd = (xx / 15.0F * 2.0F - 1.0F);
/* 125 */             double yd = (yy / 15.0F * 2.0F - 1.0F);
/* 126 */             double zd = (zz / 15.0F * 2.0F - 1.0F);
/* 127 */             double d = Math.sqrt(xd * xd + yd * yd + zd * zd);
/*     */             
/* 129 */             xd /= d;
/* 130 */             yd /= d;
/* 131 */             zd /= d;
/*     */             
/* 133 */             float remainingPower = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
/* 134 */             double xp = this.center.x;
/* 135 */             double yp = this.center.y;
/* 136 */             double zp = this.center.z;
/*     */             
/* 138 */             float stepSize = 0.3F;
/* 139 */             while (remainingPower > 0.0F) {
/* 140 */               BlockPos pos = BlockPos.containing(xp, yp, zp);
/* 141 */               BlockState block = this.level.getBlockState(pos);
/* 142 */               FluidState fluid = this.level.getFluidState(pos);
/*     */               
/* 144 */               if (!this.level.isInWorldBounds(pos)) {
/*     */                 break;
/*     */               }
/*     */               
/* 148 */               Optional<Float> resistance = this.damageCalculator.getBlockExplosionResistance(this, this.level, pos, block, fluid);
/* 149 */               if (resistance.isPresent()) {
/* 150 */                 remainingPower -= (((Float)resistance.get()).floatValue() + 0.3F) * 0.3F;
/*     */               }
/*     */               
/* 153 */               if (remainingPower > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, pos, block, remainingPower)) {
/* 154 */                 toBlowSet.add(pos);
/*     */               }
/*     */               
/* 157 */               xp += xd * 0.30000001192092896D;
/* 158 */               yp += yd * 0.30000001192092896D;
/* 159 */               zp += zd * 0.30000001192092896D;
/* 160 */               remainingPower -= 0.22500001F;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 166 */     return new ObjectArrayList(toBlowSet);
/*     */   }
/*     */   
/*     */   private void hurtEntities() {
/* 170 */     if (this.radius < 1.0E-5F) {
/*     */       return;
/*     */     }
/* 173 */     float doubleRadius = this.radius * 2.0F;
/*     */     
/* 175 */     int x0 = Mth.floor(this.center.x - doubleRadius - 1.0D);
/* 176 */     int x1 = Mth.floor(this.center.x + doubleRadius + 1.0D);
/* 177 */     int y0 = Mth.floor(this.center.y - doubleRadius - 1.0D);
/* 178 */     int y1 = Mth.floor(this.center.y + doubleRadius + 1.0D);
/* 179 */     int z0 = Mth.floor(this.center.z - doubleRadius - 1.0D);
/* 180 */     int z1 = Mth.floor(this.center.z + doubleRadius + 1.0D);
/* 181 */     List<Entity> entities = this.level.getEntities(this.source, new AABB(x0, y0, z0, x1, y1, z1));
/*     */     
/* 183 */     for (Entity entity : entities) {
/* 184 */       if (entity.ignoreExplosion(this)) {
/*     */         continue;
/*     */       }
/* 187 */       double dist = Math.sqrt(entity.distanceToSqr(this.center)) / doubleRadius;
/* 188 */       if (dist > 1.0D) {
/*     */         continue;
/*     */       }
/*     */       
/* 192 */       Vec3 entityOrigin = (entity instanceof net.minecraft.world.entity.item.PrimedTnt) ? entity.position() : entity.getEyePosition();
/* 193 */       Vec3 direction = entityOrigin.subtract(this.center).normalize();
/*     */       
/* 195 */       boolean shouldDamageEntity = this.damageCalculator.shouldDamageEntity(this, entity);
/* 196 */       float knockbackMultiplier = this.damageCalculator.getKnockbackMultiplier(entity);
/* 197 */       float exposure = (shouldDamageEntity || knockbackMultiplier != 0.0F) ? getSeenPercent(this.center, entity) : 0.0F;
/*     */       
/* 199 */       if (shouldDamageEntity) {
/* 200 */         entity.hurtServer(this.level, this.damageSource, this.damageCalculator.getEntityDamageAmount(this, entity, exposure));
/*     */       }
/*     */       
/* 203 */       LivingEntity livingEntity = (LivingEntity)entity; double knockbackResistance = (entity instanceof LivingEntity) ? livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE) : 0.0D;
/*     */       
/* 205 */       double knockbackPower = (1.0D - dist) * exposure * knockbackMultiplier * (1.0D - knockbackResistance);
/* 206 */       Vec3 knockback = direction.scale(knockbackPower);
/* 207 */       entity.push(knockback);
/*     */       
/* 209 */       if (entity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile) { Projectile projectile = (Projectile)entity;
/* 210 */         projectile.setOwner(this.damageSource.getEntity()); }
/* 211 */       else if (entity instanceof Player) { Player player = (Player)entity;
/* 212 */         if (!player.isSpectator() && (!player.isCreative() || !(player.getAbilities()).flying)) {
/* 213 */           this.hitPlayers.put(player, knockback);
/*     */         } }
/*     */ 
/*     */       
/* 217 */       entity.onExplosionHit(this.source);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void interactWithBlocks(List<BlockPos> targetBlocks) {
/* 222 */     List<StackCollector> stacks = new ArrayList<StackCollector>();
/* 223 */     Util.shuffle(targetBlocks, this.level.random);
/*     */     
/* 225 */     for (BlockPos pos : targetBlocks) {
/* 226 */       this.level.getBlockState(pos).onExplosionHit(this.level, pos, this, (stack, position) -> addOrAppendStack(stacks, stack, position));
/*     */     }
/*     */     
/* 229 */     for (StackCollector stack : stacks) {
/* 230 */       Block.popResource(this.level, stack.pos, stack.stack);
/*     */     }
/*     */   }
/*     */   
/*     */   private void createFire(List<BlockPos> targetBlocks) {
/* 235 */     for (BlockPos pos : targetBlocks) {
/* 236 */       if (this.level.random.nextInt(3) == 0 && this.level.getBlockState(pos).isAir() && this.level.getBlockState(pos.below()).isSolidRender()) {
/* 237 */         this.level.setBlockAndUpdate(pos, BaseFireBlock.getState(this.level, pos));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int explode() {
/* 243 */     this.level.gameEvent(this.source, GameEvent.EXPLODE, this.center);
/*     */     
/* 245 */     List<BlockPos> toBlow = calculateExplodedPositions();
/* 246 */     hurtEntities();
/*     */     
/* 248 */     if (interactsWithBlocks()) {
/* 249 */       ProfilerFiller profiler = Profiler.get();
/* 250 */       profiler.push("explosion_blocks");
/* 251 */       interactWithBlocks(toBlow);
/* 252 */       profiler.pop();
/*     */     } 
/*     */     
/* 255 */     if (this.fire) {
/* 256 */       createFire(toBlow);
/*     */     }
/* 258 */     return toBlow.size();
/*     */   }
/*     */   
/*     */   private static class StackCollector {
/*     */     private final BlockPos pos;
/*     */     private ItemStack stack;
/*     */     
/*     */     private StackCollector(BlockPos pos, ItemStack stack) {
/* 266 */       this.pos = pos;
/* 267 */       this.stack = stack;
/*     */     }
/*     */     
/*     */     public void tryMerge(ItemStack input) {
/* 271 */       if (ItemEntity.areMergable(this.stack, input)) {
/* 272 */         this.stack = ItemEntity.merge(this.stack, input, 16);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addOrAppendStack(List<StackCollector> stacks, ItemStack stack, BlockPos pos) {
/* 278 */     for (StackCollector stackCollector : stacks) {
/* 279 */       stackCollector.tryMerge(stack);
/* 280 */       if (stack.isEmpty()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 286 */     stacks.add(new StackCollector(pos, stack));
/*     */   }
/*     */ 
/*     */   
/* 290 */   private boolean interactsWithBlocks() { return (this.blockInteraction != Explosion.BlockInteraction.KEEP); }
/*     */ 
/*     */ 
/*     */   
/* 294 */   public Map<Player, Vec3> getHitPlayers() { return this.hitPlayers; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 299 */   public ServerLevel level() { return this.level; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 304 */   public LivingEntity getIndirectSourceEntity() { return Explosion.getIndirectSourceEntity(this.source); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 309 */   public Entity getDirectSourceEntity() { return this.source; }
/*     */ 
/*     */ 
/*     */   
/* 313 */   public DamageSource getDamageSource() { return this.damageSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 318 */   public Explosion.BlockInteraction getBlockInteraction() { return this.blockInteraction; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTriggerBlocks() {
/* 323 */     if (this.blockInteraction != Explosion.BlockInteraction.TRIGGER_BLOCK) {
/* 324 */       return false;
/*     */     }
/*     */     
/* 327 */     if (this.source != null && this.source.getType() == EntityType.BREEZE_WIND_CHARGE) {
/* 328 */       return ((Boolean)this.level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/*     */     }
/*     */     
/* 331 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldAffectBlocklikeEntities() {
/* 336 */     boolean mobGriefingEnabled = ((Boolean)this.level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/* 337 */     boolean isNotWindCharge = (this.source == null || (this.source.getType() != EntityType.BREEZE_WIND_CHARGE && this.source.getType() != EntityType.WIND_CHARGE));
/* 338 */     if (mobGriefingEnabled) {
/* 339 */       return isNotWindCharge;
/*     */     }
/* 341 */     return (this.blockInteraction.shouldAffectBlocklikeEntities() && isNotWindCharge);
/*     */   }
/*     */ 
/*     */   
/* 345 */   public boolean isSmall() { return (this.radius < 2.0F || !interactsWithBlocks()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ServerExplosion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
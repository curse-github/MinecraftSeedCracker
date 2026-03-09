/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinecartTNT
/*     */   extends AbstractMinecart
/*     */ {
/*     */   private static final byte EVENT_PRIME = 10;
/*     */   private static final String TAG_EXPLOSION_POWER = "explosion_power";
/*     */   private static final String TAG_EXPLOSION_SPEED_FACTOR = "explosion_speed_factor";
/*     */   private static final String TAG_FUSE = "fuse";
/*     */   private static final float DEFAULT_EXPLOSION_POWER_BASE = 4.0F;
/*     */   private static final float DEFAULT_EXPLOSION_SPEED_FACTOR = 1.0F;
/*     */   private static final int NO_FUSE = -1;
/*     */   private DamageSource ignitionSource;
/*  42 */   private int fuse = -1;
/*  43 */   private float explosionPowerBase = 4.0F;
/*  44 */   private float explosionSpeedFactor = 1.0F;
/*     */ 
/*     */   
/*  47 */   public MinecartTNT(EntityType<? extends MinecartTNT> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public BlockState getDefaultDisplayBlockState() { return Blocks.TNT.defaultBlockState(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  57 */     super.tick();
/*     */     
/*  59 */     if (this.fuse > 0) {
/*  60 */       this.fuse--;
/*  61 */       level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*  62 */     } else if (this.fuse == 0) {
/*  63 */       explode(this.ignitionSource, getDeltaMovement().horizontalDistanceSqr());
/*     */     } 
/*     */     
/*  66 */     if (this.horizontalCollision) {
/*  67 */       double speedSqr = getDeltaMovement().horizontalDistanceSqr();
/*  68 */       if (speedSqr >= 0.009999999776482582D) {
/*  69 */         explode(this.ignitionSource, speedSqr);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  76 */     Entity sourceEntity = source.getDirectEntity();
/*     */     
/*  78 */     if (sourceEntity instanceof AbstractArrow) { AbstractArrow projectile = (AbstractArrow)sourceEntity; if (projectile.isOnFire()) {
/*  79 */         DamageSource damageSource = damageSources().explosion(this, source.getEntity());
/*  80 */         explode(damageSource, projectile.getDeltaMovement().lengthSqr());
/*     */       }  }
/*  82 */      return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  87 */     double speedSqr = getDeltaMovement().horizontalDistanceSqr();
/*     */     
/*  89 */     if (damageSourceIgnitesTnt(source) || speedSqr >= 0.009999999776482582D) {
/*  90 */       if (this.fuse < 0) {
/*  91 */         primeFuse(source);
/*  92 */         this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
/*     */       } 
/*     */       return;
/*     */     } 
/*  96 */     destroy(level, getDropItem());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected Item getDropItem() { return Items.TNT_MINECART; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public ItemStack getPickResult() { return new ItemStack(Items.TNT_MINECART); }
/*     */ 
/*     */   
/*     */   protected void explode(DamageSource damageSource, double speedSqr) {
/* 110 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 111 */       if (((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 112 */         double speed = Math.min(Math.sqrt(speedSqr), 5.0D);
/* 113 */         level.explode(this, damageSource, null, getX(), getY(), getZ(), (float)(this.explosionPowerBase + this.explosionSpeedFactor * this.random.nextDouble() * 1.5D * speed), false, Level.ExplosionInteraction.TNT);
/* 114 */         discard();
/* 115 */       } else if (isPrimed()) {
/* 116 */         discard();
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 123 */     if (fallDistance >= 3.0D) {
/* 124 */       double power = fallDistance / 10.0D;
/* 125 */       explode(this.ignitionSource, power * power);
/*     */     } 
/*     */     
/* 128 */     return super.causeFallDamage(fallDistance, damageModifier, damageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/* 133 */     if (state && this.fuse < 0) {
/* 134 */       primeFuse(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 140 */     if (id == 10) {
/* 141 */       primeFuse(null);
/*     */     } else {
/* 143 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void primeFuse(DamageSource source) {
/* 148 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*     */         return;
/*     */       } }
/*     */     
/* 152 */     this.fuse = 80;
/*     */     
/* 154 */     if (!level().isClientSide()) {
/* 155 */       if (source != null && this.ignitionSource == null) {
/* 156 */         this.ignitionSource = damageSources().explosion(this, source.getEntity());
/*     */       }
/* 158 */       level().broadcastEntityEvent(this, (byte)10);
/* 159 */       if (!isSilent()) {
/* 160 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 166 */   public int getFuse() { return this.fuse; }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public boolean isPrimed() { return (this.fuse > -1); }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid, float resistance) {
/* 175 */     if (isPrimed() && (block.is(BlockTags.RAILS) || level.getBlockState(pos.above()).is(BlockTags.RAILS))) {
/* 176 */       return 0.0F;
/*     */     }
/*     */     
/* 179 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid, resistance);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 184 */     if (isPrimed() && (state.is(BlockTags.RAILS) || level.getBlockState(pos.above()).is(BlockTags.RAILS))) {
/* 185 */       return false;
/*     */     }
/*     */     
/* 188 */     return super.shouldBlockExplode(explosion, level, pos, state, power);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 193 */     super.readAdditionalSaveData(input);
/* 194 */     this.fuse = input.getIntOr("fuse", -1);
/* 195 */     this.explosionPowerBase = Mth.clamp(input.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
/* 196 */     this.explosionSpeedFactor = Mth.clamp(input.getFloatOr("explosion_speed_factor", 1.0F), 0.0F, 128.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 201 */     super.addAdditionalSaveData(output);
/* 202 */     output.putInt("fuse", this.fuse);
/*     */     
/* 204 */     if (this.explosionPowerBase != 4.0F) {
/* 205 */       output.putFloat("explosion_power", this.explosionPowerBase);
/*     */     }
/* 207 */     if (this.explosionSpeedFactor != 1.0F) {
/* 208 */       output.putFloat("explosion_speed_factor", this.explosionSpeedFactor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 214 */   protected boolean shouldSourceDestroy(DamageSource source) { return damageSourceIgnitesTnt(source); }
/*     */ 
/*     */   
/*     */   private static boolean damageSourceIgnitesTnt(DamageSource source) {
/* 218 */     Entity entity = source.getDirectEntity(); if (entity instanceof Projectile) { Projectile projectile = (Projectile)entity;
/* 219 */       return projectile.isOnFire(); }
/*     */     
/* 221 */     return (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartTNT.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
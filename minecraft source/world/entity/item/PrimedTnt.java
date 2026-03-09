/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class PrimedTnt
/*     */   extends Entity
/*     */   implements TraceableEntity {
/*  33 */   private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.INT);
/*  34 */   private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.BLOCK_STATE);
/*     */   private static final short DEFAULT_FUSE_TIME = 80;
/*     */   private static final float DEFAULT_EXPLOSION_POWER = 4.0F;
/*  37 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.TNT.defaultBlockState();
/*     */   
/*     */   private static final String TAG_BLOCK_STATE = "block_state";
/*     */   public static final String TAG_FUSE = "fuse";
/*     */   private static final String TAG_EXPLOSION_POWER = "explosion_power";
/*     */   
/*  43 */   private static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator()
/*     */     {
/*     */       public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/*  46 */         if (state.is(Blocks.NETHER_PORTAL)) {
/*  47 */           return false;
/*     */         }
/*  49 */         return super.shouldBlockExplode(explosion, level, pos, state, power);
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/*  54 */         if (block.is(Blocks.NETHER_PORTAL)) {
/*  55 */           return Optional.empty();
/*     */         }
/*  57 */         return super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
/*     */       }
/*     */     };
/*     */   
/*     */   private EntityReference<LivingEntity> owner;
/*     */   private boolean usedPortal;
/*  63 */   private float explosionPower = 4.0F;
/*     */   
/*     */   public PrimedTnt(EntityType<? extends PrimedTnt> type, Level level) {
/*  66 */     super(type, level);
/*  67 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   public PrimedTnt(Level level, double x, double y, double z, LivingEntity owner) {
/*  71 */     this(EntityType.TNT, level);
/*     */     
/*  73 */     setPos(x, y, z);
/*     */     
/*  75 */     double rot = level.random.nextDouble() * 6.2831854820251465D;
/*     */     
/*  77 */     setDeltaMovement(
/*  78 */         -Math.sin(rot) * 0.02D, 0.20000000298023224D, 
/*     */         
/*  80 */         -Math.cos(rot) * 0.02D);
/*     */ 
/*     */     
/*  83 */     setFuse(80);
/*     */     
/*  85 */     this.xo = x;
/*  86 */     this.yo = y;
/*  87 */     this.zo = z;
/*  88 */     this.owner = EntityReference.of(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  93 */     entityData.define(DATA_FUSE_ID, Integer.valueOf(80));
/*  94 */     entityData.define(DATA_BLOCK_STATE_ID, DEFAULT_BLOCK_STATE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean isPickable() { return !isRemoved(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   protected double getDefaultGravity() { return 0.04D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 114 */     handlePortal();
/* 115 */     applyGravity();
/* 116 */     move(MoverType.SELF, getDeltaMovement());
/* 117 */     applyEffectsFromBlocks();
/* 118 */     setDeltaMovement(getDeltaMovement().scale(0.98D));
/*     */     
/* 120 */     if (onGround())
/*     */     {
/* 122 */       setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/*     */     }
/*     */     
/* 125 */     int fuse = getFuse() - 1;
/* 126 */     setFuse(fuse);
/* 127 */     if (fuse <= 0) {
/* 128 */       discard();
/* 129 */       if (!level().isClientSide()) {
/* 130 */         explode();
/*     */       }
/*     */     } else {
/* 133 */       updateInWaterStateAndDoFluidPushing();
/* 134 */       if (level().isClientSide()) {
/* 135 */         level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void explode() {
/* 141 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 142 */         level().explode(this, Explosion.getDefaultDamageSource(level(), this), this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null, getX(), getY(0.0625D), getZ(), this.explosionPower, false, Level.ExplosionInteraction.TNT);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 148 */     output.putShort("fuse", (short)getFuse());
/* 149 */     output.store("block_state", BlockState.CODEC, getBlockState());
/* 150 */     if (this.explosionPower != 4.0F) {
/* 151 */       output.putFloat("explosion_power", this.explosionPower);
/*     */     }
/* 153 */     EntityReference.store(this.owner, output, "owner");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 158 */     setFuse(input.getShortOr("fuse", (short)80));
/* 159 */     setBlockState((BlockState)input.read("block_state", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE));
/* 160 */     this.explosionPower = Mth.clamp(input.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
/* 161 */     this.owner = EntityReference.read(input, "owner");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public LivingEntity getOwner() { return EntityReference.getLivingEntity(this.owner, level()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/* 171 */     super.restoreFrom(oldEntity);
/* 172 */     if (oldEntity instanceof PrimedTnt) { PrimedTnt primedTnt = (PrimedTnt)oldEntity;
/* 173 */       this.owner = primedTnt.owner; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 178 */   public void setFuse(int time) { this.entityData.set(DATA_FUSE_ID, Integer.valueOf(time)); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   public int getFuse() { return ((Integer)this.entityData.get(DATA_FUSE_ID)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 186 */   public void setBlockState(BlockState blockState) { this.entityData.set(DATA_BLOCK_STATE_ID, blockState); }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public BlockState getBlockState() { return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID); }
/*     */ 
/*     */ 
/*     */   
/* 194 */   private void setUsedPortal(boolean usedPortal) { this.usedPortal = usedPortal; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 199 */     Entity newEntity = super.teleport(transition);
/* 200 */     if (newEntity instanceof PrimedTnt) { PrimedTnt tnt = (PrimedTnt)newEntity;
/* 201 */       tnt.setUsedPortal(true); }
/*     */     
/* 203 */     return newEntity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\item\PrimedTnt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
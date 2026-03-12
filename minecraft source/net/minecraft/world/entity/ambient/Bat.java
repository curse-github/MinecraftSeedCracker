/*     */ package net.minecraft.world.entity.ambient;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Bat
/*     */   extends AmbientCreature
/*     */ {
/*     */   public static final float FLAP_LENGTH_SECONDS = 0.5F;
/*     */   public static final float TICKS_PER_FLAP = 10.0F;
/*  37 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Bat.class, EntityDataSerializers.BYTE);
/*     */   private static final int FLAG_RESTING = 1;
/*  39 */   private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0D);
/*     */   
/*     */   private static final byte DEFAULT_FLAGS = 0;
/*     */   
/*  43 */   public final AnimationState flyAnimationState = new AnimationState();
/*  44 */   public final AnimationState restAnimationState = new AnimationState();
/*     */   
/*     */   private BlockPos targetPosition;
/*     */   
/*     */   public Bat(EntityType<? extends Bat> type, Level level) {
/*  49 */     super(type, level);
/*     */     
/*  51 */     if (!level.isClientSide()) {
/*  52 */       setResting(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public boolean isFlapping() { return (!isResting() && this.tickCount % 10.0F == 0.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  64 */     super.defineSynchedData(entityData);
/*  65 */     entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected float getSoundVolume() { return 0.1F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public float getVoicePitch() { return super.getVoicePitch() * 0.95F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SoundEvent getAmbientSound() {
/*  80 */     if (isResting() && this.random.nextInt(4) != 0) {
/*  81 */       return null;
/*     */     }
/*  83 */     return SoundEvents.BAT_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.BAT_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean isPushable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity entity) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushEntities() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 113 */     return Mob.createMobAttributes()
/* 114 */       .add(Attributes.MAX_HEALTH, 6.0D);
/*     */   }
/*     */ 
/*     */   
/* 118 */   public boolean isResting() { return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & true) != 0); }
/*     */ 
/*     */   
/*     */   public void setResting(boolean value) {
/* 122 */     byte current = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/* 123 */     if (value) {
/* 124 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current | true)));
/*     */     } else {
/* 126 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 132 */     super.tick();
/* 133 */     if (isResting()) {
/* 134 */       setDeltaMovement(Vec3.ZERO);
/* 135 */       setPosRaw(getX(), Mth.floor(getY()) + 1.0D - getBbHeight(), getZ());
/*     */     } else {
/* 137 */       setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
/*     */     } 
/* 139 */     setupAnimationStates();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 144 */     super.customServerAiStep(level);
/*     */     
/* 146 */     BlockPos pos = blockPosition();
/* 147 */     BlockPos above = pos.above();
/*     */     
/* 149 */     if (isResting()) {
/* 150 */       boolean isSilent = isSilent();
/* 151 */       if (level.getBlockState(above).isRedstoneConductor(level, pos)) {
/* 152 */         if (this.random.nextInt(200) == 0) {
/* 153 */           this.yHeadRot = this.random.nextInt(360);
/*     */         }
/*     */         
/* 156 */         if (level.getNearestPlayer(BAT_RESTING_TARGETING, this) != null) {
/* 157 */           setResting(false);
/* 158 */           if (!isSilent) {
/* 159 */             level.levelEvent(null, 1025, pos, 0);
/*     */           }
/*     */         } 
/*     */       } else {
/* 163 */         setResting(false);
/* 164 */         if (!isSilent) {
/* 165 */           level.levelEvent(null, 1025, pos, 0);
/*     */         }
/*     */       } 
/*     */     } else {
/* 169 */       if (this.targetPosition != null && (!level.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= level.getMinY())) {
/* 170 */         this.targetPosition = null;
/*     */       }
/* 172 */       if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(position(), 2.0D)) {
/* 173 */         this.targetPosition = BlockPos.containing(getX() + this.random.nextInt(7) - this.random.nextInt(7), getY() + this.random.nextInt(6) - 2.0D, getZ() + this.random.nextInt(7) - this.random.nextInt(7));
/*     */       }
/*     */ 
/*     */       
/* 177 */       double dx = this.targetPosition.getX() + 0.5D - getX();
/* 178 */       double dy = this.targetPosition.getY() + 0.1D - getY();
/* 179 */       double dz = this.targetPosition.getZ() + 0.5D - getZ();
/*     */       
/* 181 */       Vec3 movement = getDeltaMovement();
/* 182 */       Vec3 newMovement = movement.add((
/* 183 */           Math.signum(dx) * 0.5D - movement.x) * 0.10000000149011612D, (
/* 184 */           Math.signum(dy) * 0.699999988079071D - movement.y) * 0.10000000149011612D, (
/* 185 */           Math.signum(dz) * 0.5D - movement.z) * 0.10000000149011612D);
/*     */       
/* 187 */       setDeltaMovement(newMovement);
/*     */       
/* 189 */       float yRotD = (float)(Mth.atan2(newMovement.z, newMovement.x) * 57.2957763671875D) - 90.0F;
/* 190 */       float rotDiff = Mth.wrapDegrees(yRotD - getYRot());
/* 191 */       this.zza = 0.5F;
/* 192 */       setYRot(getYRot() + rotDiff);
/*     */       
/* 194 */       if (this.random.nextInt(100) == 0 && level.getBlockState(above).isRedstoneConductor(level, above)) {
/* 195 */         setResting(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 202 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   public boolean isIgnoringBlockTriggers() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 218 */     if (isInvulnerableTo(level, source)) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     if (isResting()) {
/* 223 */       setResting(false);
/*     */     }
/* 225 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 230 */     super.readAdditionalSaveData(input);
/* 231 */     this.entityData.set(DATA_ID_FLAGS, Byte.valueOf(input.getByteOr("BatFlags", (byte)0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 236 */     super.addAdditionalSaveData(output);
/* 237 */     output.putByte("BatFlags", ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue());
/*     */   }
/*     */   
/*     */   public static boolean checkBatSpawnRules(EntityType<Bat> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 241 */     if (pos.getY() >= level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos).getY()) {
/* 242 */       return false;
/*     */     }
/*     */     
/* 245 */     if (random.nextBoolean()) {
/* 246 */       return false;
/*     */     }
/*     */     
/* 249 */     if (level.getMaxLocalRawBrightness(pos) > random.nextInt(4)) {
/* 250 */       return false;
/*     */     }
/*     */     
/* 253 */     if (!level.getBlockState(pos.below()).is(BlockTags.BATS_SPAWNABLE_ON)) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     return checkMobSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 261 */     if (isResting()) {
/* 262 */       this.flyAnimationState.stop();
/* 263 */       this.restAnimationState.startIfStopped(this.tickCount);
/*     */     } else {
/* 265 */       this.restAnimationState.stop();
/* 266 */       this.flyAnimationState.startIfStopped(this.tickCount);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ambient\Bat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
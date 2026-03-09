/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Animal
/*     */   extends AgeableMob
/*     */ {
/*     */   protected static final int PARENT_AGE_AFTER_BREEDING = 6000;
/*     */   private static final int DEFAULT_IN_LOVE_TIME = 0;
/*  49 */   private int inLove = 0;
/*     */   private EntityReference<ServerPlayer> loveCause;
/*     */   
/*     */   protected Animal(EntityType<? extends Animal> type, Level level) {
/*  53 */     super(type, level);
/*  54 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  55 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAnimalAttributes() {
/*  59 */     return Mob.createMobAttributes()
/*  60 */       .add(Attributes.TEMPT_RANGE, 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  65 */     if (getAge() != 0) {
/*  66 */       this.inLove = 0;
/*     */     }
/*  68 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  73 */     super.aiStep();
/*     */     
/*  75 */     if (getAge() != 0) {
/*  76 */       this.inLove = 0;
/*     */     }
/*     */     
/*  79 */     if (this.inLove > 0) {
/*  80 */       this.inLove--;
/*  81 */       if (this.inLove % 10 == 0) {
/*  82 */         double xa = this.random.nextGaussian() * 0.02D;
/*  83 */         double ya = this.random.nextGaussian() * 0.02D;
/*  84 */         double za = this.random.nextGaussian() * 0.02D;
/*  85 */         level().addParticle(ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/*  92 */     resetLove();
/*  93 */     super.actuallyHurt(level, source, dmg);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/*  98 */     if (level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
/*  99 */       return 10.0F;
/*     */     }
/* 101 */     return level.getPathfindingCostFromLightLevels(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 106 */     super.addAdditionalSaveData(output);
/* 107 */     output.putInt("InLove", this.inLove);
/* 108 */     EntityReference.store(this.loveCause, output, "LoveCause");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 113 */     super.readAdditionalSaveData(input);
/* 114 */     this.inLove = input.getIntOr("InLove", 0);
/* 115 */     this.loveCause = EntityReference.read(input, "LoveCause");
/*     */   }
/*     */   
/*     */   public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 119 */     boolean brightEnoughToSpawn = (EntitySpawnReason.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn(level, pos));
/* 120 */     return (level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && brightEnoughToSpawn);
/*     */   }
/*     */ 
/*     */   
/* 124 */   protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter level, BlockPos pos) { return (level.getRawBrightness(pos, 0) > 8); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public int getAmbientSoundInterval() { return 120; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public boolean removeWhenFarAway(double distSqr) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   protected int getBaseExperienceReward(ServerLevel level) { return 1 + this.random.nextInt(3); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 146 */     ItemStack itemStack = player.getItemInHand(hand);
/* 147 */     if (isFood(itemStack)) {
/* 148 */       int age = getAge();
/* 149 */       if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player; if (age == 0 && canFallInLove())
/* 150 */         { usePlayerItem(player, hand, itemStack);
/* 151 */           setInLove(serverPlayer);
/* 152 */           playEatingSound();
/* 153 */           return InteractionResult.SUCCESS_SERVER; }  }
/* 154 */        if (isBaby()) {
/* 155 */         usePlayerItem(player, hand, itemStack);
/*     */         
/* 157 */         ageUp(getSpeedUpSecondsWhenFeeding(-age), true);
/* 158 */         playEatingSound();
/* 159 */         return InteractionResult.SUCCESS;
/* 160 */       }  if (level().isClientSide()) {
/* 161 */         return InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/* 165 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playEatingSound() {}
/*     */ 
/*     */   
/* 172 */   public boolean canFallInLove() { return (this.inLove <= 0); }
/*     */ 
/*     */   
/*     */   public void setInLove(Player player) {
/* 176 */     this.inLove = 600;
/*     */     
/* 178 */     if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 179 */       this.loveCause = EntityReference.of(serverPlayer); }
/*     */ 
/*     */     
/* 182 */     level().broadcastEntityEvent(this, (byte)18);
/*     */   }
/*     */ 
/*     */   
/* 186 */   public void setInLoveTime(int time) { this.inLove = time; }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public int getInLoveTime() { return this.inLove; }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public ServerPlayer getLoveCause() { return (ServerPlayer)EntityReference.get(this.loveCause, level(), ServerPlayer.class); }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public boolean isInLove() { return (this.inLove > 0); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   public void resetLove() { this.inLove = 0; }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 206 */     if (partner == this) {
/* 207 */       return false;
/*     */     }
/* 209 */     if (partner.getClass() != getClass()) {
/* 210 */       return false;
/*     */     }
/* 212 */     return (isInLove() && partner.isInLove());
/*     */   }
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
/* 216 */     AgeableMob offspring = getBreedOffspring(level, partner);
/* 217 */     if (offspring == null) {
/*     */       return;
/*     */     }
/* 220 */     offspring.setBaby(true);
/* 221 */     offspring.snapTo(getX(), getY(), getZ(), 0.0F, 0.0F);
/*     */     
/* 223 */     finalizeSpawnChildFromBreeding(level, partner, offspring);
/* 224 */     level.addFreshEntityWithPassengers(offspring);
/*     */   }
/*     */   
/*     */   public void finalizeSpawnChildFromBreeding(ServerLevel level, Animal partner, AgeableMob offspring) {
/* 228 */     Optional.ofNullable(getLoveCause())
/* 229 */       .or(() -> Optional.ofNullable(partner.getLoveCause()))
/* 230 */       .ifPresent(cause -> {
/* 231 */           cause.awardStat(Stats.ANIMALS_BRED);
/* 232 */           CriteriaTriggers.BRED_ANIMALS.trigger(cause, this, partner, offspring);
/*     */         });
/*     */     
/* 235 */     setAge(6000);
/* 236 */     partner.setAge(6000);
/* 237 */     resetLove();
/* 238 */     partner.resetLove();
/*     */     
/* 240 */     level.broadcastEntityEvent(this, (byte)18);
/*     */     
/* 242 */     if (((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue()) {
/* 243 */       level.addFreshEntity(new ExperienceOrb(level, getX(), getY(), getZ(), getRandom().nextInt(7) + 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 249 */     if (id == 18) {
/* 250 */       for (int i = 0; i < 7; i++) {
/* 251 */         double xa = this.random.nextGaussian() * 0.02D;
/* 252 */         double ya = this.random.nextGaussian() * 0.02D;
/* 253 */         double za = this.random.nextGaussian() * 0.02D;
/* 254 */         level().addParticle(ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*     */       } 
/*     */     } else {
/* 257 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 263 */     Direction forward = getMotionDirection();
/* 264 */     if (forward.getAxis() == Direction.Axis.Y) {
/* 265 */       return super.getDismountLocationForPassenger(passenger);
/*     */     }
/*     */     
/* 268 */     int[][] offsets = DismountHelper.offsetsForDirection(forward);
/* 269 */     BlockPos vehicleBlockPos = blockPosition();
/* 270 */     BlockPos.MutableBlockPos targetBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 272 */     for (UnmodifiableIterator unmodifiableIterator = passenger.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose dismountPose = (Pose)unmodifiableIterator.next();
/* 273 */       AABB poseCollisionBox = passenger.getLocalBoundsForPose(dismountPose);
/*     */       
/* 275 */       for (int[] offsetXZ : offsets) {
/* 276 */         targetBlockPos.set(vehicleBlockPos.getX() + offsetXZ[0], vehicleBlockPos.getY(), vehicleBlockPos.getZ() + offsetXZ[1]);
/*     */         
/* 278 */         double blockFloorHeight = level().getBlockFloorHeight(targetBlockPos);
/* 279 */         if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/*     */ 
/*     */ 
/*     */           
/* 283 */           Vec3 location = Vec3.upFromBottomCenterOf(targetBlockPos, blockFloorHeight);
/* 284 */           if (DismountHelper.canDismountTo(level(), passenger, poseCollisionBox.move(location))) {
/* 285 */             passenger.setPose(dismountPose);
/* 286 */             return location;
/*     */           } 
/*     */         } 
/*     */       }  }
/*     */     
/* 291 */     return super.getDismountLocationForPassenger(passenger);
/*     */   }
/*     */   
/*     */   public abstract boolean isFood(ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\Animal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
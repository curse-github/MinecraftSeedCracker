/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Pufferfish
/*     */   extends AbstractFish
/*     */ {
/*  33 */   private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(Pufferfish.class, EntityDataSerializers.INT);
/*     */   
/*     */   private int inflateCounter;
/*     */   
/*     */   private int deflateTimer;
/*     */   private static final TargetingConditions.Selector SCARY_MOB = (target, level) -> {
/*  39 */       if (target instanceof Player) { Player player = (Player)target; if (player.isCreative()) {
/*  40 */           return false;
/*     */         } }
/*     */       
/*  43 */       return !target.getType().is(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH);
/*     */     };
/*  45 */   private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(SCARY_MOB);
/*     */   
/*     */   public static final int STATE_SMALL = 0;
/*     */   public static final int STATE_MID = 1;
/*     */   public static final int STATE_FULL = 2;
/*     */   private static final int DEFAULT_PUFF_STATE = 0;
/*     */   
/*     */   public Pufferfish(EntityType<? extends Pufferfish> type, Level level) {
/*  53 */     super(type, level);
/*     */     
/*  55 */     refreshDimensions();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  60 */     super.defineSynchedData(entityData);
/*     */     
/*  62 */     entityData.define(PUFF_STATE, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*  66 */   public int getPuffState() { return ((Integer)this.entityData.get(PUFF_STATE)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public void setPuffState(int state) { this.entityData.set(PUFF_STATE, Integer.valueOf(state)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  75 */     if (PUFF_STATE.equals(accessor)) {
/*  76 */       refreshDimensions();
/*     */     }
/*     */     
/*  79 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  84 */     super.addAdditionalSaveData(output);
/*     */     
/*  86 */     output.putInt("PuffState", getPuffState());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  91 */     super.readAdditionalSaveData(input);
/*     */     
/*  93 */     setPuffState(Math.min(input.getIntOr("PuffState", 0), 2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public ItemStack getBucketItemStack() { return new ItemStack(Items.PUFFERFISH_BUCKET); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 103 */     super.registerGoals();
/*     */     
/* 105 */     this.goalSelector.addGoal(1, new PufferfishPuffGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 110 */     if (!level().isClientSide() && isAlive() && isEffectiveAi()) {
/* 111 */       if (this.inflateCounter > 0) {
/*     */         
/* 113 */         if (getPuffState() == 0) {
/* 114 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
/* 115 */           setPuffState(1);
/*     */         }
/* 117 */         else if (this.inflateCounter > 40 && getPuffState() == 1) {
/* 118 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
/* 119 */           setPuffState(2);
/*     */         } 
/*     */ 
/*     */         
/* 123 */         this.inflateCounter++;
/* 124 */       } else if (getPuffState() != 0) {
/*     */         
/* 126 */         if (this.deflateTimer > 60 && getPuffState() == 2) {
/* 127 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
/* 128 */           setPuffState(1);
/* 129 */         } else if (this.deflateTimer > 100 && getPuffState() == 1) {
/* 130 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
/* 131 */           setPuffState(0);
/*     */         } 
/*     */         
/* 134 */         this.deflateTimer++;
/*     */       } 
/*     */     }
/* 137 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 142 */     super.aiStep();
/*     */     
/* 144 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (isAlive() && getPuffState() > 0) {
/* 145 */         List<Mob> mobs = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(0.3D), target -> TARGETING_CONDITIONS.test(level, this, target));
/* 146 */         for (Mob mob : mobs) {
/* 147 */           if (mob.isAlive())
/* 148 */             touch(level, mob); 
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private void touch(ServerLevel level, Mob mob) {
/* 155 */     int puffState = getPuffState();
/* 156 */     if (mob.hurtServer(level, damageSources().mobAttack(this), (1 + puffState))) {
/* 157 */       mob.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * puffState, 0), this);
/* 158 */       playSound(SoundEvents.PUFFER_FISH_STING, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player player) {
/* 164 */     int puffState = getPuffState();
/* 165 */     if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player; if (puffState > 0 && 
/* 166 */         player.hurtServer(serverPlayer.level(), damageSources().mobAttack(this), (1 + puffState))) {
/* 167 */         if (!isSilent()) {
/* 168 */           serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F));
/*     */         }
/* 170 */         player.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * puffState, 0), this);
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 177 */   protected SoundEvent getDeathSound() { return SoundEvents.PUFFER_FISH_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PUFFER_FISH_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   protected SoundEvent getFlopSound() { return SoundEvents.PUFFER_FISH_FLOP; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public EntityDimensions getDefaultDimensions(Pose pose) { return super.getDefaultDimensions(pose).scale(getScale(getPuffState())); }
/*     */ 
/*     */   
/*     */   private static float getScale(int state) {
/* 196 */     switch (state) {
/*     */       case 1:
/* 198 */         return 0.7F;
/*     */       case 0:
/* 200 */         return 0.5F;
/*     */     } 
/* 202 */     return 1.0F;
/*     */   }
/*     */   
/*     */   private static class PufferfishPuffGoal
/*     */     extends Goal
/*     */   {
/*     */     private final Pufferfish fish;
/*     */     
/* 210 */     public PufferfishPuffGoal(Pufferfish fish) { this.fish = fish; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 215 */       List<LivingEntity> entities = this.fish.level().getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), target -> Pufferfish.TARGETING_CONDITIONS.test(getServerLevel(this.fish), this.fish, target));
/*     */       
/* 217 */       return !entities.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 222 */       this.fish.inflateCounter = 1;
/* 223 */       this.fish.deflateTimer = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 228 */     public void stop() { this.fish.inflateCounter = 0; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\Pufferfish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
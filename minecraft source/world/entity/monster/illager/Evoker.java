/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Evoker
/*     */   extends SpellcasterIllager
/*     */ {
/*     */   private Sheep wololoTarget;
/*     */   
/*     */   public Evoker(EntityType<? extends Evoker> type, Level level) {
/*  52 */     super(type, level);
/*     */     
/*  54 */     this.xpReward = 10;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  59 */     super.registerGoals();
/*     */     
/*  61 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  62 */     this.goalSelector.addGoal(1, new EvokerCastingSpellGoal());
/*  63 */     this.goalSelector.addGoal(2, new AvoidEntityGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F, 0.6D, 1.0D));
/*  64 */     this.goalSelector.addGoal(3, new AvoidEntityGoal(this, net.minecraft.world.entity.monster.creaking.Creaking.class, 8.0F, 0.6D, 1.0D));
/*  65 */     this.goalSelector.addGoal(4, new EvokerSummonSpellGoal());
/*  66 */     this.goalSelector.addGoal(5, new EvokerAttackSpellGoal());
/*  67 */     this.goalSelector.addGoal(6, new EvokerWololoSpellGoal());
/*  68 */     this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
/*  69 */     this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 3.0F, 1.0F));
/*  70 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, net.minecraft.world.entity.Mob.class, 8.0F));
/*     */     
/*  72 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { net.minecraft.world.entity.raid.Raider.class })).setAlertOthers(new Class[0]));
/*  73 */     this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true)).setUnseenMemoryTicks(300));
/*  74 */     this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal(this, net.minecraft.world.entity.npc.villager.AbstractVillager.class, false)).setUnseenMemoryTicks(300));
/*  75 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  79 */     return Monster.createMonsterAttributes()
/*  80 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/*  81 */       .add(Attributes.FOLLOW_RANGE, 12.0D)
/*  82 */       .add(Attributes.MAX_HEALTH, 24.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public SoundEvent getCelebrateSound() { return SoundEvents.EVOKER_CELEBRATE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean considersEntityAsAlly(Entity other) {
/*  92 */     if (other == this) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (super.considersEntityAsAlly(other)) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (other instanceof Vex) { Vex vex = (Vex)other; if (vex.getOwner() != null)
/*  99 */         return considersEntityAsAlly(vex.getOwner());  }
/*     */     
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected SoundEvent getAmbientSound() { return SoundEvents.EVOKER_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected SoundEvent getDeathSound() { return SoundEvents.EVOKER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.EVOKER_HURT; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   private void setWololoTarget(Sheep wololoTarget) { this.wololoTarget = wololoTarget; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   private Sheep getWololoTarget() { return this.wololoTarget; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   protected SoundEvent getCastingSoundEvent() { return SoundEvents.EVOKER_CAST_SPELL; }
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */   
/*     */   private class EvokerCastingSpellGoal
/*     */     extends SpellcasterIllager.SpellcasterCastingSpellGoal {
/*     */     private EvokerCastingSpellGoal() {
/* 136 */       super(Evoker.this);
/*     */     }
/*     */     public void tick() {
/* 139 */       if (Evoker.this.getTarget() != null) {
/* 140 */         Evoker.this.getLookControl().setLookAt(Evoker.this.getTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/* 141 */       } else if (Evoker.this.getWololoTarget() != null) {
/* 142 */         Evoker.this.getLookControl().setLookAt(Evoker.this.getWololoTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/*     */       } 
/*     */     } }
/*     */   
/*     */   private class EvokerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal { private EvokerAttackSpellGoal() {
/* 147 */       super(Evoker.this);
/*     */     }
/*     */     
/* 150 */     protected int getCastingTime() { return 40; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     protected int getCastingInterval() { return 100; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 161 */       LivingEntity target = Evoker.this.getTarget();
/* 162 */       double minY = Math.min(target.getY(), Evoker.this.getY());
/* 163 */       double maxY = Math.max(target.getY(), Evoker.this.getY()) + 1.0D;
/* 164 */       float angleTowardsTarget = (float)Mth.atan2(target.getZ() - Evoker.this.getZ(), target.getX() - Evoker.this.getX());
/* 165 */       if (Evoker.this.distanceToSqr(target) < 9.0D) {
/*     */         
/* 167 */         for (int i = 0; i < 5; i++) {
/* 168 */           float angle = angleTowardsTarget + i * 3.1415927F * 0.4F;
/* 169 */           createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 1.5D, Evoker.this.getZ() + Mth.sin(angle) * 1.5D, minY, maxY, angle, 0);
/*     */         } 
/*     */         
/* 172 */         for (int i = 0; i < 8; i++) {
/* 173 */           float angle = angleTowardsTarget + i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
/* 174 */           createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 2.5D, Evoker.this.getZ() + Mth.sin(angle) * 2.5D, minY, maxY, angle, 3);
/*     */         } 
/*     */       } else {
/*     */         
/* 178 */         for (int i = 0; i < 16; i++) {
/* 179 */           double reach = 1.25D * (i + 1);
/* 180 */           int spellSpeed = 1 * i;
/* 181 */           createSpellEntity(Evoker.this.getX() + Mth.cos(angleTowardsTarget) * reach, Evoker.this.getZ() + Mth.sin(angleTowardsTarget) * reach, minY, maxY, angleTowardsTarget, spellSpeed);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void createSpellEntity(double x, double z, double minY, double maxY, float angle, int delayTicks) {
/* 188 */       BlockPos pos = BlockPos.containing(x, maxY, z);
/* 189 */       boolean success = false;
/* 190 */       double topOffset = 0.0D;
/*     */       do {
/* 192 */         BlockPos below = pos.below();
/* 193 */         BlockState belowState = Evoker.this.level().getBlockState(below);
/* 194 */         if (belowState.isFaceSturdy(Evoker.this.level(), below, Direction.UP)) {
/* 195 */           if (!Evoker.this.level().isEmptyBlock(pos)) {
/* 196 */             BlockState blockState = Evoker.this.level().getBlockState(pos);
/* 197 */             VoxelShape shape = blockState.getCollisionShape(Evoker.this.level(), pos);
/* 198 */             if (!shape.isEmpty()) {
/* 199 */               topOffset = shape.max(Direction.Axis.Y);
/*     */             }
/*     */           } 
/* 202 */           success = true;
/*     */           break;
/*     */         } 
/* 205 */         pos = pos.below();
/* 206 */       } while (pos.getY() >= Mth.floor(minY) - 1);
/* 207 */       if (success) {
/* 208 */         Evoker.this.level().addFreshEntity(new EvokerFangs(Evoker.this.level(), x, pos.getY() + topOffset, z, angle, delayTicks, Evoker.this));
/* 209 */         Evoker.this.level().gameEvent(GameEvent.ENTITY_PLACE, new Vec3(x, pos.getY() + topOffset, z), GameEvent.Context.of(Evoker.this));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 215 */     protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_ATTACK; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.FANGS; } }
/*     */   
/*     */   private class EvokerSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     private EvokerSummonSpellGoal() {
/* 224 */       super(Evoker.this);
/* 225 */       this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */     }
/*     */     private final TargetingConditions vexCountTargeting;
/*     */     public boolean canUse() {
/* 229 */       if (!super.canUse()) {
/* 230 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 234 */       int vexes = getServerLevel(Evoker.this.level()).getNearbyEntities(Vex.class, this.vexCountTargeting, Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D)).size();
/* 235 */       return (Evoker.this.random.nextInt(8) + 1 > vexes);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 240 */     protected int getCastingTime() { return 100; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     protected int getCastingInterval() { return 340; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 250 */       ServerLevel serverLevel = (ServerLevel)Evoker.this.level();
/* 251 */       PlayerTeam evokerTeam = Evoker.this.getTeam();
/* 252 */       for (int i = 0; i < 3; i++) {
/* 253 */         BlockPos pos = Evoker.this.blockPosition().offset(-2 + Evoker.this.random.nextInt(5), 1, -2 + Evoker.this.random.nextInt(5));
/* 254 */         Vex vex = (Vex)EntityType.VEX.create(Evoker.this.level(), EntitySpawnReason.MOB_SUMMONED);
/* 255 */         if (vex != null) {
/* 256 */           vex.snapTo(pos, 0.0F, 0.0F);
/* 257 */           vex.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
/* 258 */           vex.setOwner(Evoker.this);
/* 259 */           vex.setBoundOrigin(pos);
/* 260 */           vex.setLimitedLife(20 * (30 + Evoker.this.random.nextInt(90)));
/* 261 */           if (evokerTeam != null) {
/* 262 */             serverLevel.getScoreboard().addPlayerToTeam(vex.getScoreboardName(), evokerTeam);
/*     */           }
/* 264 */           serverLevel.addFreshEntityWithPassengers(vex);
/* 265 */           serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(Evoker.this));
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 272 */     protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_SUMMON; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.SUMMON_VEX; } }
/*     */   public class EvokerWololoSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal { private final TargetingConditions wololoTargeting;
/*     */     
/*     */     public EvokerWololoSpellGoal() {
/* 281 */       super(Evoker.this);
/* 282 */       this.wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((target, level) -> (((Sheep)target).getColor() == DyeColor.BLUE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 286 */       if (Evoker.this.getTarget() != null)
/*     */       {
/* 288 */         return false;
/*     */       }
/* 290 */       if (Evoker.this.isCastingSpell())
/*     */       {
/* 292 */         return false;
/*     */       }
/* 294 */       if (Evoker.this.tickCount < this.nextAttackTickCount) {
/* 295 */         return false;
/*     */       }
/* 297 */       ServerLevel level = getServerLevel(Evoker.this.level());
/* 298 */       if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 299 */         return false;
/*     */       }
/*     */       
/* 302 */       List<Sheep> entities = level.getNearbyEntities(Sheep.class, this.wololoTargeting, Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
/*     */       
/* 304 */       if (entities.isEmpty()) {
/* 305 */         return false;
/*     */       }
/* 307 */       Evoker.this.setWololoTarget((Sheep)entities.get(Evoker.this.random.nextInt(entities.size())));
/* 308 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     public boolean canContinueToUse() { return (Evoker.this.getWololoTarget() != null && this.attackWarmupDelay > 0); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {
/* 319 */       super.stop();
/* 320 */       Evoker.this.setWololoTarget(null);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 325 */       Sheep wololoTarget = Evoker.this.getWololoTarget();
/* 326 */       if (wololoTarget != null && wololoTarget.isAlive()) {
/* 327 */         wololoTarget.setColor(DyeColor.RED);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 333 */     protected int getCastWarmupTime() { return 40; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     protected int getCastingTime() { return 60; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     protected int getCastingInterval() { return 140; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 348 */     protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_WOLOLO; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.WOLOLO; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Evoker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
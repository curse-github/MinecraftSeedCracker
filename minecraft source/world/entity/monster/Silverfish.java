/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.InfestedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ public class Silverfish
/*     */   extends Monster
/*     */ {
/*     */   private SilverfishWakeUpFriendsGoal friendsGoal;
/*     */   
/*  39 */   public Silverfish(EntityType<? extends Silverfish> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  44 */     this.friendsGoal = new SilverfishWakeUpFriendsGoal(this);
/*     */     
/*  46 */     this.goalSelector.addGoal(1, new FloatGoal(this));
/*  47 */     this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, level()));
/*     */     
/*  49 */     this.goalSelector.addGoal(3, this.friendsGoal);
/*     */     
/*  51 */     this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
/*  52 */     this.goalSelector.addGoal(5, new SilverfishMergeWithStoneGoal(this));
/*     */     
/*  54 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  55 */     this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  59 */     return Monster.createMonsterAttributes()
/*  60 */       .add(Attributes.MAX_HEALTH, 8.0D)
/*  61 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  62 */       .add(Attributes.ATTACK_DAMAGE, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.EVENTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   protected SoundEvent getAmbientSound() { return SoundEvents.SILVERFISH_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SILVERFISH_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected SoundEvent getDeathSound() { return SoundEvents.SILVERFISH_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  92 */     if (isInvulnerableTo(level, source)) {
/*  93 */       return false;
/*     */     }
/*  95 */     if ((source.getEntity() != null || source.is(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)) && this.friendsGoal != null) {
/*  96 */       this.friendsGoal.notifyHurt();
/*     */     }
/*  98 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 104 */     this.yBodyRot = getYRot();
/*     */     
/* 106 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float yBodyRot) {
/* 111 */     setYRot(yBodyRot);
/* 112 */     super.setYBodyRot(yBodyRot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 118 */     if (InfestedBlock.isCompatibleHostBlock(level.getBlockState(pos.below()))) {
/* 119 */       return 10.0F;
/*     */     }
/* 121 */     return super.getWalkTargetValue(pos, level);
/*     */   }
/*     */   
/*     */   public static boolean checkSilverfishSpawnRules(EntityType<Silverfish> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 125 */     if (!checkAnyLightMonsterSpawnRules(type, level, spawnReason, pos, random)) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     if (EntitySpawnReason.isSpawner(spawnReason)) {
/* 130 */       return true;
/*     */     }
/*     */     
/* 133 */     Player nearestPlayer = level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0D, true);
/* 134 */     return (nearestPlayer == null);
/*     */   }
/*     */   
/*     */   private static class SilverfishWakeUpFriendsGoal
/*     */     extends Goal {
/*     */     private final Silverfish silverfish;
/*     */     private int lookForFriends;
/*     */     
/* 142 */     public SilverfishWakeUpFriendsGoal(Silverfish silverfish) { this.silverfish = silverfish; }
/*     */ 
/*     */     
/*     */     public void notifyHurt() {
/* 146 */       if (this.lookForFriends == 0) {
/* 147 */         this.lookForFriends = adjustedTickDelay(20);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 153 */     public boolean canUse() { return (this.lookForFriends > 0); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 158 */       this.lookForFriends--;
/* 159 */       if (this.lookForFriends <= 0) {
/* 160 */         Level level = this.silverfish.level();
/* 161 */         RandomSource random = this.silverfish.getRandom();
/*     */ 
/*     */         
/* 164 */         BlockPos basePos = this.silverfish.blockPosition();
/*     */         
/*     */         int yOff;
/* 167 */         for (yOff = 0; yOff <= 5 && yOff >= -5; yOff = ((yOff <= 0) ? 1 : 0) - yOff) {
/* 168 */           int xOff; for (xOff = 0; xOff <= 10 && xOff >= -10; xOff = ((xOff <= 0) ? 1 : 0) - xOff) {
/* 169 */             int zOff; for (zOff = 0; zOff <= 10 && zOff >= -10; zOff = ((zOff <= 0) ? 1 : 0) - zOff) {
/* 170 */               BlockPos testPos = basePos.offset(xOff, yOff, zOff);
/* 171 */               BlockState blockState = level.getBlockState(testPos);
/*     */               
/* 173 */               Block block = blockState.getBlock();
/* 174 */               if (block instanceof InfestedBlock) {
/* 175 */                 if (((Boolean)getServerLevel(level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 176 */                   level.destroyBlock(testPos, true, this.silverfish);
/*     */                 } else {
/* 178 */                   level.setBlock(testPos, ((InfestedBlock)block).hostStateByInfested(level.getBlockState(testPos)), 3);
/*     */                 } 
/* 180 */                 if (random.nextBoolean())
/*     */                   // Byte code: goto -> 251 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SilverfishMergeWithStoneGoal
/*     */     extends RandomStrollGoal {
/*     */     private Direction selectedDirection;
/*     */     private boolean doMerge;
/*     */     
/*     */     public SilverfishMergeWithStoneGoal(Silverfish silverfish) {
/* 196 */       super(silverfish, 1.0D, 10);
/*     */       
/* 198 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 203 */       if (this.mob.getTarget() != null) {
/* 204 */         return false;
/*     */       }
/* 206 */       if (!this.mob.getNavigation().isDone()) {
/* 207 */         return false;
/*     */       }
/*     */       
/* 210 */       RandomSource random = this.mob.getRandom();
/* 211 */       if (((Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && random.nextInt(reducedTickDelay(10)) == 0) {
/* 212 */         this.selectedDirection = Direction.getRandom(random);
/*     */         
/* 214 */         BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
/* 215 */         BlockState blockState = this.mob.level().getBlockState(pos);
/* 216 */         if (InfestedBlock.isCompatibleHostBlock(blockState)) {
/* 217 */           this.doMerge = true;
/* 218 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 222 */       this.doMerge = false;
/* 223 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 228 */       if (this.doMerge) {
/* 229 */         return false;
/*     */       }
/* 231 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 236 */       if (!this.doMerge) {
/* 237 */         super.start();
/*     */         
/*     */         return;
/*     */       } 
/* 241 */       Level level1 = this.mob.level();
/* 242 */       BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
/* 243 */       BlockState blockState = level1.getBlockState(pos);
/*     */       
/* 245 */       if (InfestedBlock.isCompatibleHostBlock(blockState)) {
/* 246 */         level1.setBlock(pos, InfestedBlock.infestedStateByHost(blockState), 3);
/* 247 */         this.mob.spawnAnim();
/* 248 */         this.mob.discard();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Silverfish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
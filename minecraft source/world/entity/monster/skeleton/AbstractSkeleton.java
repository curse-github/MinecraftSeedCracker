/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSkeleton
/*     */   extends Monster
/*     */   implements RangedAttackMob
/*     */ {
/*     */   private static final int HARD_ATTACK_INTERVAL = 20;
/*     */   private static final int NORMAL_ATTACK_INTERVAL = 40;
/*     */   protected static final int INCREASED_HARD_ATTACK_INTERVAL = 50;
/*     */   protected static final int INCREASED_NORMAL_ATTACK_INTERVAL = 70;
/*  61 */   private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal(this, 1.0D, 20, 15.0F);
/*  62 */   private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false)
/*     */     {
/*     */       public void stop() {
/*  65 */         super.stop();
/*  66 */         AbstractSkeleton.this.setAggressive(false);
/*     */       }
/*     */ 
/*     */       
/*     */       public void start() {
/*  71 */         super.start();
/*  72 */         AbstractSkeleton.this.setAggressive(true);
/*     */       }
/*     */     };
/*     */   
/*     */   protected AbstractSkeleton(EntityType<? extends AbstractSkeleton> type, Level level) {
/*  77 */     super(type, level);
/*     */     
/*  79 */     reassessWeaponGoal();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  84 */     this.goalSelector.addGoal(2, new RestrictSunGoal(this));
/*  85 */     this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
/*  86 */     this.goalSelector.addGoal(3, new AvoidEntityGoal(this, net.minecraft.world.entity.animal.wolf.Wolf.class, 6.0F, 1.0D, 1.2D));
/*  87 */     this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  88 */     this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
/*  89 */     this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
/*     */     
/*  91 */     this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
/*  92 */     this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true));
/*  93 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, true));
/*  94 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  98 */     return Monster.createMonsterAttributes()
/*  99 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(getStepSound(), 0.15F, 1.0F); }
/*     */ 
/*     */   
/*     */   abstract SoundEvent getStepSound();
/*     */ 
/*     */   
/*     */   public void rideTick() {
/* 111 */     super.rideTick();
/*     */     
/* 113 */     Entity entity1 = getControlledVehicle(); if (entity1 instanceof PathfinderMob) { PathfinderMob entity = (PathfinderMob)entity1;
/* 114 */       this.yBodyRot = entity.yBodyRot; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 120 */     super.populateDefaultEquipmentSlots(random, difficulty);
/*     */     
/* 122 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 127 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/* 129 */     RandomSource random = level.getRandom();
/* 130 */     populateDefaultEquipmentSlots(random, difficulty);
/* 131 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/* 132 */     reassessWeaponGoal();
/*     */     
/* 134 */     setCanPickUpLoot((random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier()));
/*     */     
/* 136 */     if (getItemBySlot(EquipmentSlot.HEAD).isEmpty() && 
/* 137 */       SpecialDates.isHalloween() && random.nextFloat() < 0.25F) {
/*     */       
/* 139 */       setItemSlot(EquipmentSlot.HEAD, new ItemStack((random.nextFloat() < 0.1F) ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
/* 140 */       setDropChance(EquipmentSlot.HEAD, 0.0F);
/*     */     } 
/*     */     
/* 143 */     return groupData;
/*     */   }
/*     */   
/*     */   public void reassessWeaponGoal() {
/* 147 */     if (level() == null || level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 151 */     this.goalSelector.removeGoal(this.meleeGoal);
/* 152 */     this.goalSelector.removeGoal(this.bowGoal);
/*     */     
/* 154 */     ItemStack usedWeapon = getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
/* 155 */     if (usedWeapon.is(Items.BOW)) {
/*     */       
/* 157 */       int minAttackInterval = getHardAttackInterval();
/* 158 */       if (level().getDifficulty() != Difficulty.HARD) {
/* 159 */         minAttackInterval = getAttackInterval();
/*     */       }
/* 161 */       this.bowGoal.setMinAttackInterval(minAttackInterval);
/* 162 */       this.goalSelector.addGoal(4, this.bowGoal);
/*     */     } else {
/* 164 */       this.goalSelector.addGoal(4, this.meleeGoal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 169 */   protected int getHardAttackInterval() { return 20; }
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected int getAttackInterval() { return 40; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 178 */     ItemStack bowItem = getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
/* 179 */     ItemStack projectile = getProjectile(bowItem);
/* 180 */     AbstractArrow arrow = getArrow(projectile, power, bowItem);
/*     */     
/* 182 */     double xd = target.getX() - getX();
/* 183 */     double yd = target.getY(0.3333333333333333D) - arrow.getY();
/* 184 */     double zd = target.getZ() - getZ();
/* 185 */     double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
/* 186 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 187 */       Projectile.spawnProjectileUsingShoot(arrow, serverLevel, projectile, xd, yd + distanceToTarget * 0.20000000298023224D, zd, 1.6F, (14 - serverLevel
/*     */ 
/*     */ 
/*     */           
/* 191 */           .getDifficulty().getId() * 4)); }
/*     */     
/* 193 */     playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */ 
/*     */   
/* 197 */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) { return ProjectileUtil.getMobArrow(this, projectile, power, firingWeapon); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public boolean canUseNonMeleeWeapon(ItemStack item) { return (item.getItem() == Items.BOW); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public TagKey<Item> getPreferredWeaponType() { return ItemTags.SKELETON_PREFERRED_WEAPONS; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 212 */     super.readAdditionalSaveData(input);
/*     */     
/* 214 */     reassessWeaponGoal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack stack) {
/* 219 */     super.onEquipItem(slot, oldStack, stack);
/*     */     
/* 221 */     if (!level().isClientSide()) {
/* 222 */       reassessWeaponGoal();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 227 */   public boolean isShaking() { return isFullyFrozen(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 232 */     if (itemStack.is(ItemTags.SPEARS)) {
/* 233 */       return false;
/*     */     }
/* 235 */     return super.wantsToPickUp(level, itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\skeleton\AbstractSkeleton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
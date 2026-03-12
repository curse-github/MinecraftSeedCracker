/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Vindicator
/*     */   extends AbstractIllager
/*     */ {
/*     */   private static final String TAG_JOHNNY = "Johnny";
/*  53 */   private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = d -> (d == Difficulty.NORMAL || d == Difficulty.HARD);
/*     */   
/*     */   private static final boolean DEFAULT_JOHNNY = false;
/*     */   
/*     */   private boolean isJohnny = false;
/*     */   
/*  59 */   public Vindicator(EntityType<? extends Vindicator> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     super.registerGoals();
/*     */     
/*  66 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  67 */     this.goalSelector.addGoal(1, new AvoidEntityGoal(this, net.minecraft.world.entity.monster.creaking.Creaking.class, 8.0F, 1.0D, 1.2D));
/*  68 */     this.goalSelector.addGoal(2, new VindicatorBreakDoorGoal(this));
/*  69 */     this.goalSelector.addGoal(3, new AbstractIllager.RaiderOpenDoorGoal(this, this));
/*  70 */     this.goalSelector.addGoal(4, new Raider.HoldGroundAttackGoal(this, 10.0F));
/*  71 */     this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
/*  72 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  73 */     this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true));
/*  74 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.npc.villager.AbstractVillager.class, true));
/*  75 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, true));
/*  76 */     this.targetSelector.addGoal(4, new VindicatorJohnnyAttackGoal(this));
/*  77 */     this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
/*  78 */     this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 3.0F, 1.0F));
/*  79 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  84 */     if (!isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
/*  85 */       boolean canOpenDoors = level.isRaided(blockPosition());
/*  86 */       getNavigation().setCanOpenDoors(canOpenDoors);
/*     */     } 
/*     */     
/*  89 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  93 */     return Monster.createMonsterAttributes()
/*  94 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  95 */       .add(Attributes.FOLLOW_RANGE, 12.0D)
/*  96 */       .add(Attributes.MAX_HEALTH, 24.0D)
/*  97 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 102 */     super.addAdditionalSaveData(output);
/*     */     
/* 104 */     if (this.isJohnny) {
/* 105 */       output.putBoolean("Johnny", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/* 111 */     if (isAggressive())
/* 112 */       return AbstractIllager.IllagerArmPose.ATTACKING; 
/* 113 */     if (isCelebrating()) {
/* 114 */       return AbstractIllager.IllagerArmPose.CELEBRATING;
/*     */     }
/* 116 */     return AbstractIllager.IllagerArmPose.CROSSED;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 121 */     super.readAdditionalSaveData(input);
/* 122 */     this.isJohnny = input.getBooleanOr("Johnny", false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public SoundEvent getCelebrateSound() { return SoundEvents.VINDICATOR_CELEBRATE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 132 */     SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/* 134 */     getNavigation().setCanOpenDoors(true);
/*     */     
/* 136 */     RandomSource random = level.getRandom();
/* 137 */     populateDefaultEquipmentSlots(random, difficulty);
/* 138 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/*     */     
/* 140 */     return spawnGroupData;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 145 */     if (getCurrentRaid() == null) {
/* 146 */       setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(Component name) {
/* 152 */     super.setCustomName(name);
/* 153 */     if (!this.isJohnny && name != null && name.getString().equals("Johnny")) {
/* 154 */       this.isJohnny = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 160 */   protected SoundEvent getAmbientSound() { return SoundEvents.VINDICATOR_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   protected SoundEvent getDeathSound() { return SoundEvents.VINDICATOR_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.VINDICATOR_HURT; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {
/* 175 */     ItemStack axe = new ItemStack(Items.IRON_AXE);
/* 176 */     Raid raid = getCurrentRaid();
/*     */     
/* 178 */     boolean shouldEnchant = (this.random.nextFloat() <= raid.getEnchantOdds());
/* 179 */     if (shouldEnchant) {
/*     */ 
/*     */       
/* 182 */       ResourceKey<EnchantmentProvider> provider = (wave > raid.getNumGroups(Difficulty.NORMAL)) ? VanillaEnchantmentProviders.RAID_VINDICATOR_POST_WAVE_5 : VanillaEnchantmentProviders.RAID_VINDICATOR;
/* 183 */       EnchantmentHelper.enchantItemFromProvider(axe, level.registryAccess(), provider, level.getCurrentDifficultyAt(blockPosition()), this.random);
/*     */     } 
/*     */     
/* 186 */     setItemSlot(EquipmentSlot.MAINHAND, axe);
/*     */   }
/*     */   
/*     */   private static class VindicatorBreakDoorGoal extends BreakDoorGoal {
/*     */     public VindicatorBreakDoorGoal(Mob mob) {
/* 191 */       super(mob, 6, Vindicator.DOOR_BREAKING_PREDICATE);
/* 192 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 197 */       Vindicator vindicator = (Vindicator)this.mob;
/* 198 */       return (vindicator.hasActiveRaid() && super.canContinueToUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 203 */       Vindicator vindicator = (Vindicator)this.mob;
/* 204 */       return (vindicator.hasActiveRaid() && vindicator.random.nextInt(reducedTickDelay(10)) == 0 && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 209 */       super.start();
/* 210 */       this.mob.setNoActionTime(0);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class VindicatorJohnnyAttackGoal
/*     */     extends NearestAttackableTargetGoal<LivingEntity> {
/* 216 */     public VindicatorJohnnyAttackGoal(Vindicator mob) { super(mob, LivingEntity.class, 0, true, true, (target, level) -> target.attackable()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     public boolean canUse() { return (((Vindicator)this.mob).isJohnny && super.canUse()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 226 */       super.start();
/* 227 */       this.mob.setNoActionTime(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Vindicator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
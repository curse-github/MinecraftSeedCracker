/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SnowGolem
/*     */   extends AbstractGolem
/*     */   implements RangedAttackMob, Shearable
/*     */ {
/*  47 */   private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID = SynchedEntityData.defineId(SnowGolem.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final byte PUMPKIN_FLAG = 16;
/*     */   
/*     */   private static final boolean DEFAULT_PUMPKIN = true;
/*     */   
/*  53 */   public SnowGolem(EntityType<? extends SnowGolem> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  58 */     this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 20, 10.0F));
/*  59 */     this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
/*  60 */     this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  61 */     this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
/*     */     
/*  63 */     this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, (target, level) -> target instanceof net.minecraft.world.entity.monster.Enemy));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  67 */     return Mob.createMobAttributes()
/*  68 */       .add(Attributes.MAX_HEALTH, 4.0D)
/*  69 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  74 */     super.defineSynchedData(entityData);
/*  75 */     entityData.define(DATA_PUMPKIN_ID, Byte.valueOf((byte)16));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  80 */     super.addAdditionalSaveData(output);
/*     */     
/*  82 */     output.putBoolean("Pumpkin", hasPumpkin());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  87 */     super.readAdditionalSaveData(input);
/*     */     
/*  89 */     setPumpkin(input.getBooleanOr("Pumpkin", true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public boolean isSensitiveToWater() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  99 */     super.aiStep();
/*     */     
/* 101 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 102 */       if (((Boolean)serverLevel.environmentAttributes().getValue(EnvironmentAttributes.SNOW_GOLEM_MELTS, position())).booleanValue()) {
/* 103 */         hurtServer(serverLevel, damageSources().onFire(), 1.0F);
/*     */       }
/*     */       
/* 106 */       if (!((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 111 */       BlockState snow = Blocks.SNOW.defaultBlockState();
/* 112 */       for (int i = 0; i < 4; i++) {
/* 113 */         int xx = Mth.floor(getX() + ((i % 2 * 2 - 1) * 0.25F));
/* 114 */         int yy = Mth.floor(getY());
/* 115 */         int zz = Mth.floor(getZ() + ((i / 2 % 2 * 2 - 1) * 0.25F));
/* 116 */         BlockPos snowPos = new BlockPos(xx, yy, zz);
/* 117 */         if (level().getBlockState(snowPos).isAir() && snow.canSurvive(level(), snowPos)) {
/* 118 */           level().setBlockAndUpdate(snowPos, snow);
/* 119 */           level().gameEvent(GameEvent.BLOCK_PLACE, snowPos, GameEvent.Context.of(this, snow));
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 127 */     double xd = target.getX() - getX();
/* 128 */     double yd = target.getEyeY() - 1.100000023841858D;
/* 129 */     double zd = target.getZ() - getZ();
/* 130 */     double yo = Math.sqrt(xd * xd + zd * zd) * 0.20000000298023224D;
/*     */     
/* 132 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 133 */       ItemStack itemStack = new ItemStack(Items.SNOWBALL);
/* 134 */       Projectile.spawnProjectile(new Snowball(serverLevel, this, itemStack), serverLevel, itemStack, projectile -> 
/*     */ 
/*     */           
/* 137 */           projectile.shoot(xd, yd + yo - projectile.getY(), zd, 1.6F, 12.0F)); }
/*     */ 
/*     */ 
/*     */     
/* 141 */     playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 146 */     ItemStack itemStack = player.getItemInHand(hand);
/* 147 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 148 */       Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 149 */         shear(level, SoundSource.PLAYERS, itemStack);
/* 150 */         gameEvent(GameEvent.SHEAR, player);
/* 151 */         itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot()); }
/*     */       
/* 153 */       return InteractionResult.SUCCESS;
/*     */     } 
/* 155 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 160 */     level.playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
/*     */     
/* 162 */     setPumpkin(false);
/* 163 */     dropFromShearingLootTable(level, BuiltInLootTables.SHEAR_SNOW_GOLEM, tool, (l, drop) -> spawnAtLocation(l, drop, getEyeHeight()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public boolean readyForShearing() { return (isAlive() && hasPumpkin()); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public boolean hasPumpkin() { return ((((Byte)this.entityData.get(DATA_PUMPKIN_ID)).byteValue() & 0x10) != 0); }
/*     */ 
/*     */   
/*     */   public void setPumpkin(boolean pumpkin) {
/* 176 */     byte current = ((Byte)this.entityData.get(DATA_PUMPKIN_ID)).byteValue();
/* 177 */     if (pumpkin) {
/* 178 */       this.entityData.set(DATA_PUMPKIN_ID, Byte.valueOf((byte)(current | 0x10)));
/*     */     } else {
/* 180 */       this.entityData.set(DATA_PUMPKIN_ID, Byte.valueOf((byte)(current & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 186 */   protected SoundEvent getAmbientSound() { return SoundEvents.SNOW_GOLEM_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SNOW_GOLEM_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 196 */   protected SoundEvent getDeathSound() { return SoundEvents.SNOW_GOLEM_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.75F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\golem\SnowGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
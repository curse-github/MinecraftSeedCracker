/*     */ package net.minecraft.world.entity.animal.sheep;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.EatBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ public class Sheep
/*     */   extends Animal
/*     */   implements Shearable
/*     */ {
/*     */   private static final int EAT_ANIMATION_TICKS = 40;
/*  58 */   private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.BYTE);
/*     */ 
/*     */   
/*  61 */   private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
/*     */   
/*     */   private static final boolean DEFAULT_SHEARED = false;
/*     */   
/*     */   private int eatAnimationTick;
/*     */   private EatBlockGoal eatBlockGoal;
/*     */   
/*  68 */   public Sheep(EntityType<? extends Sheep> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  73 */     this.eatBlockGoal = new EatBlockGoal(this);
/*  74 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  75 */     this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
/*  76 */     this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
/*  77 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, i -> i.is(ItemTags.SHEEP_FOOD), false));
/*  78 */     this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
/*  79 */     this.goalSelector.addGoal(5, this.eatBlockGoal);
/*  80 */     this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  81 */     this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  82 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.SHEEP_FOOD); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  92 */     this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
/*  93 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  98 */     if (level().isClientSide()) {
/*  99 */       this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
/*     */     }
/* 101 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 105 */     return Animal.createAnimalAttributes()
/* 106 */       .add(Attributes.MAX_HEALTH, 8.0D)
/* 107 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 112 */     super.defineSynchedData(entityData);
/*     */ 
/*     */     
/* 115 */     entityData.define(DATA_WOOL_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 120 */     if (id == 10) {
/* 121 */       this.eatAnimationTick = 40;
/*     */     } else {
/* 123 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getHeadEatPositionScale(float a) {
/* 128 */     if (this.eatAnimationTick <= 0) {
/* 129 */       return 0.0F;
/*     */     }
/* 131 */     if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
/* 132 */       return 1.0F;
/*     */     }
/* 134 */     if (this.eatAnimationTick < 4) {
/* 135 */       return (this.eatAnimationTick - a) / 4.0F;
/*     */     }
/* 137 */     return -((this.eatAnimationTick - 40) - a) / 4.0F;
/*     */   }
/*     */   
/*     */   public float getHeadEatAngleScale(float a) {
/* 141 */     if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
/* 142 */       float scale = ((this.eatAnimationTick - 4) - a) / 32.0F;
/* 143 */       return 0.62831855F + 0.21991149F * Mth.sin((scale * 28.7F));
/*     */     } 
/* 145 */     if (this.eatAnimationTick > 0) {
/* 146 */       return 0.62831855F;
/*     */     }
/* 148 */     return getXRot(a) * 0.017453292F;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 153 */     ItemStack itemStack = player.getItemInHand(hand);
/* 154 */     if (itemStack.is(Items.SHEARS)) {
/* 155 */       Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (readyForShearing()) {
/* 156 */           shear(level, SoundSource.PLAYERS, itemStack);
/* 157 */           gameEvent(GameEvent.SHEAR, player);
/* 158 */           itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
/* 159 */           return InteractionResult.SUCCESS_SERVER;
/*     */         }  }
/* 161 */        return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 164 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 169 */     level.playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
/*     */     
/* 171 */     dropFromShearingLootTable(level, BuiltInLootTables.SHEAR_SHEEP, tool, (l, drop) -> {
/* 172 */           for (int i = 0; i < drop.getCount(); i++) {
/* 173 */             ItemEntity entity = spawnAtLocation(l, drop.copyWithCount(1), 1.0F);
/* 174 */             if (entity != null) {
/* 175 */               entity.setDeltaMovement(entity.getDeltaMovement().add(((this.random
/* 176 */                     .nextFloat() - this.random.nextFloat()) * 0.1F), (this.random
/* 177 */                     .nextFloat() * 0.05F), ((this.random
/* 178 */                     .nextFloat() - this.random.nextFloat()) * 0.1F)));
/*     */             }
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 184 */     setSheared(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public boolean readyForShearing() { return (isAlive() && !isSheared() && !isBaby()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 194 */     super.addAdditionalSaveData(output);
/* 195 */     output.putBoolean("Sheared", isSheared());
/* 196 */     output.store("Color", DyeColor.LEGACY_ID_CODEC, getColor());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 201 */     super.readAdditionalSaveData(input);
/* 202 */     setSheared(input.getBooleanOr("Sheared", false));
/* 203 */     setColor((DyeColor)input.read("Color", DyeColor.LEGACY_ID_CODEC).orElse(DEFAULT_COLOR));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 208 */   protected SoundEvent getAmbientSound() { return SoundEvents.SHEEP_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SHEEP_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   protected SoundEvent getDeathSound() { return SoundEvents.SHEEP_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/* 227 */   public DyeColor getColor() { return DyeColor.byId(((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue() & 0xF); }
/*     */ 
/*     */   
/*     */   public void setColor(DyeColor color) {
/* 231 */     byte current = ((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue();
/* 232 */     this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(current & 0xF0 | color.getId() & 0xF)));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 237 */     if (type == DataComponents.SHEEP_COLOR) {
/* 238 */       return (T)castComponentValue(type, getColor());
/*     */     }
/*     */     
/* 241 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 246 */     applyImplicitComponentIfPresent(components, DataComponents.SHEEP_COLOR);
/* 247 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 252 */     if (type == DataComponents.SHEEP_COLOR) {
/* 253 */       setColor((DyeColor)castComponentValue(DataComponents.SHEEP_COLOR, value));
/* 254 */       return true;
/*     */     } 
/*     */     
/* 257 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/* 261 */   public boolean isSheared() { return ((((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue() & 0x10) != 0); }
/*     */ 
/*     */   
/*     */   public void setSheared(boolean value) {
/* 265 */     byte current = ((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue();
/* 266 */     if (value) {
/* 267 */       this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(current | 0x10)));
/*     */     } else {
/* 269 */       this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(current & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static DyeColor getRandomSheepColor(ServerLevelAccessor level, BlockPos pos) {
/* 274 */     Holder<Biome> biome = level.getBiome(pos);
/* 275 */     return SheepColorSpawnRules.getSheepColor(biome, level.getRandom());
/*     */   }
/*     */ 
/*     */   
/*     */   public Sheep getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 280 */     Sheep sheep = (Sheep)EntityType.SHEEP.create(level, EntitySpawnReason.BREEDING);
/*     */     
/* 282 */     if (sheep != null) {
/* 283 */       DyeColor parent1DyeColor = getColor();
/* 284 */       DyeColor parent2DyeColor = ((Sheep)partner).getColor();
/* 285 */       sheep.setColor(DyeColor.getMixedColor(level, parent1DyeColor, parent2DyeColor));
/*     */     } 
/*     */     
/* 288 */     return sheep;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ate() {
/* 293 */     super.ate();
/* 294 */     setSheared(false);
/* 295 */     if (isBaby())
/*     */     {
/* 297 */       ageUp(60);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 303 */     setColor(getRandomSheepColor(level, blockPosition()));
/* 304 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sheep\Sheep.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
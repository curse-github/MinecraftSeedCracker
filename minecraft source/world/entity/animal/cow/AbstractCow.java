/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public abstract class AbstractCow extends Animal {
/*  32 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.COW.getDimensions().scale(0.5F).withEyeHeight(0.665F);
/*     */ 
/*     */   
/*  35 */   public AbstractCow(EntityType<? extends AbstractCow> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  40 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  41 */     this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
/*  42 */     this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
/*  43 */     this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, i -> i.is(ItemTags.COW_FOOD), false));
/*  44 */     this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
/*  45 */     this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  46 */     this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  47 */     this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.COW_FOOD); }
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  56 */     return Animal.createAnimalAttributes()
/*  57 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  58 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected SoundEvent getAmbientSound() { return SoundEvents.COW_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.COW_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected SoundEvent getDeathSound() { return SoundEvents.COW_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.COW_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected float getSoundVolume() { return 0.4F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  88 */     ItemStack itemStack = player.getItemInHand(hand);
/*  89 */     if (itemStack.is(Items.BUCKET) && !isBaby()) {
/*  90 */       player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
/*  91 */       ItemStack bucketOrMilkBucket = ItemUtils.createFilledResult(itemStack, player, Items.MILK_BUCKET.getDefaultInstance());
/*  92 */       player.setItemInHand(hand, bucketOrMilkBucket);
/*  93 */       return InteractionResult.SUCCESS;
/*     */     } 
/*  95 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\cow\AbstractCow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ public class CamelHusk
/*     */   extends Camel
/*     */ {
/*  25 */   public CamelHusk(EntityType<? extends Camel> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public boolean removeWhenFarAway(double distSqr) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   public boolean isMobControlled() { return getFirstPassenger() instanceof net.minecraft.world.entity.Mob; }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  40 */     setPersistenceRequired();
/*  41 */     return super.interact(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public boolean canBeLeashed() { return !isMobControlled(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.CAMEL_HUSK_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected SoundEvent getAmbientSound() { return SoundEvents.CAMEL_HUSK_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public boolean canMate(Animal partner) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public Camel getBreedOffspring(ServerLevel level, AgeableMob partner) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean canFallInLove() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected SoundEvent getDeathSound() { return SoundEvents.CAMEL_HUSK_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.CAMEL_HUSK_HURT; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  86 */     if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
/*  87 */       playSound(SoundEvents.CAMEL_HUSK_STEP_SAND, 0.4F, 1.0F);
/*     */     } else {
/*  89 */       playSound(SoundEvents.CAMEL_HUSK_STEP, 0.4F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected SoundEvent getDashingSound() { return SoundEvents.CAMEL_HUSK_DASH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected SoundEvent getDashReadySound() { return SoundEvents.CAMEL_HUSK_DASH_READY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected SoundEvent getEatingSound() { return SoundEvents.CAMEL_HUSK_EAT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   protected SoundEvent getStandUpSound() { return SoundEvents.CAMEL_HUSK_STAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   protected SoundEvent getSitDownSound() { return SoundEvents.CAMEL_HUSK_SIT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   protected Holder.Reference<SoundEvent> getSaddleSound() { return SoundEvents.CAMEL_HUSK_SADDLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public float chargeSpeedModifier() { return 4.0F; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\camel\CamelHusk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
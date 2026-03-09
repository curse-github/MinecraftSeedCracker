/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ public class Arrow
/*     */   extends AbstractArrow
/*     */ {
/*     */   private static final int EXPOSED_POTION_DECAY_TIME = 600;
/*     */   private static final int NO_EFFECT_COLOR = -1;
/*  24 */   private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(Arrow.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final byte EVENT_POTION_PUFF = 0;
/*     */ 
/*     */   
/*  29 */   public Arrow(EntityType<? extends Arrow> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public Arrow(Level level, double x, double y, double z, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/*  33 */     super(EntityType.ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
/*  34 */     updateColor();
/*     */   }
/*     */   
/*     */   public Arrow(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/*  38 */     super(EntityType.ARROW, owner, level, pickupItemStack, firedFromWeapon);
/*  39 */     updateColor();
/*     */   }
/*     */ 
/*     */   
/*  43 */   private PotionContents getPotionContents() { return (PotionContents)getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   private float getPotionDurationScale() { return ((Float)getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue(); }
/*     */ 
/*     */   
/*     */   private void setPotionContents(PotionContents potionContents) {
/*  51 */     getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, potionContents);
/*  52 */     updateColor();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setPickupItemStack(ItemStack itemStack) {
/*  57 */     super.setPickupItemStack(itemStack);
/*  58 */     updateColor();
/*     */   }
/*     */   
/*     */   private void updateColor() {
/*  62 */     PotionContents potionContents = getPotionContents();
/*  63 */     this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(potionContents.equals(PotionContents.EMPTY) ? -1 : potionContents.getColor()));
/*     */   }
/*     */ 
/*     */   
/*  67 */   public void addEffect(MobEffectInstance effect) { setPotionContents(getPotionContents().withEffectAdded(effect)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  72 */     super.defineSynchedData(entityData);
/*  73 */     entityData.define(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     super.tick();
/*     */     
/*  80 */     if (level().isClientSide()) {
/*  81 */       if (isInGround()) {
/*  82 */         if (this.inGroundTime % 5 == 0) {
/*  83 */           makeParticle(1);
/*     */         }
/*     */       } else {
/*  86 */         makeParticle(2);
/*     */       }
/*     */     
/*  89 */     } else if (isInGround() && this.inGroundTime != 0 && 
/*  90 */       !getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
/*  91 */       level().broadcastEntityEvent(this, (byte)0);
/*  92 */       setPickupItemStack(new ItemStack(Items.ARROW));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeParticle(int amount) {
/*  99 */     int colorValue = getColor();
/* 100 */     if (colorValue == -1 || amount <= 0) {
/*     */       return;
/*     */     }
/* 103 */     for (int i = 0; i < amount; i++) {
/* 104 */       level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, colorValue), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 109 */   public int getColor() { return ((Integer)this.entityData.get(ID_EFFECT_COLOR)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity mob) {
/* 114 */     super.doPostHurtEffects(mob);
/*     */     
/* 116 */     Entity effectSource = getEffectSource();
/*     */     
/* 118 */     PotionContents potionContents = getPotionContents();
/* 119 */     float durationScale = getPotionDurationScale();
/* 120 */     potionContents.forEachEffect(effect -> mob.addEffect(effect, effectSource), durationScale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 125 */   protected ItemStack getDefaultPickupItem() { return new ItemStack(Items.ARROW); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 130 */     if (id == 0) {
/* 131 */       int colorValue = getColor();
/* 132 */       if (colorValue != -1) {
/* 133 */         float red = (colorValue >> 16 & 0xFF) / 255.0F;
/* 134 */         float green = (colorValue >> 8 & 0xFF) / 255.0F;
/* 135 */         float blue = (colorValue >> 0 & 0xFF) / 255.0F;
/*     */         
/* 137 */         for (int i = 0; i < 20; i++) {
/* 138 */           level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */       } 
/*     */     } else {
/* 142 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\arrow\Arrow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
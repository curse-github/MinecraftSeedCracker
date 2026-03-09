/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.monster.skeleton.Skeleton;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkeletonTrapGoal
/*     */   extends Goal
/*     */ {
/*     */   private final SkeletonHorse horse;
/*     */   
/*  25 */   public SkeletonTrapGoal(SkeletonHorse horse) { this.horse = horse; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public boolean canUse() { return this.horse.level().hasNearbyAlivePlayer(this.horse.getX(), this.horse.getY(), this.horse.getZ(), 10.0D); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  35 */     ServerLevel level = (ServerLevel)this.horse.level();
/*  36 */     DifficultyInstance difficulty = level.getCurrentDifficultyAt(this.horse.blockPosition());
/*  37 */     this.horse.setTrap(false);
/*  38 */     this.horse.setTamed(true);
/*  39 */     this.horse.setAge(0);
/*  40 */     LightningBolt bolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
/*  41 */     if (bolt == null) {
/*     */       return;
/*     */     }
/*  44 */     bolt.snapTo(this.horse.getX(), this.horse.getY(), this.horse.getZ());
/*  45 */     bolt.setVisualOnly(true);
/*  46 */     level.addFreshEntity(bolt);
/*  47 */     Skeleton skeleton = createSkeleton(difficulty, this.horse);
/*  48 */     if (skeleton == null) {
/*     */       return;
/*     */     }
/*  51 */     skeleton.startRiding(this.horse);
/*     */     
/*  53 */     level.addFreshEntityWithPassengers(skeleton);
/*     */     
/*  55 */     for (int i = 0; i < 3; i++) {
/*  56 */       AbstractHorse otherHorse = createHorse(difficulty);
/*  57 */       if (otherHorse != null) {
/*     */ 
/*     */         
/*  60 */         Skeleton otherSkeleton = createSkeleton(difficulty, otherHorse);
/*  61 */         if (otherSkeleton != null) {
/*     */ 
/*     */           
/*  64 */           otherSkeleton.startRiding(otherHorse);
/*  65 */           otherHorse.push(this.horse.getRandom().triangle(0.0D, 1.1485D), 0.0D, this.horse.getRandom().triangle(0.0D, 1.1485D));
/*  66 */           level.addFreshEntityWithPassengers(otherHorse);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private AbstractHorse createHorse(DifficultyInstance difficulty) {
/*  71 */     SkeletonHorse horse = (SkeletonHorse)EntityType.SKELETON_HORSE.create(this.horse.level(), EntitySpawnReason.TRIGGERED);
/*  72 */     if (horse != null) {
/*  73 */       horse.finalizeSpawn((ServerLevel)this.horse.level(), difficulty, EntitySpawnReason.TRIGGERED, null);
/*  74 */       horse.setPos(this.horse.getX(), this.horse.getY(), this.horse.getZ());
/*  75 */       horse.invulnerableTime = 60;
/*  76 */       horse.setPersistenceRequired();
/*  77 */       horse.setTamed(true);
/*  78 */       horse.setAge(0);
/*     */     } 
/*  80 */     return horse;
/*     */   }
/*     */   
/*     */   private Skeleton createSkeleton(DifficultyInstance difficulty, AbstractHorse horse) {
/*  84 */     Skeleton skeleton = (Skeleton)EntityType.SKELETON.create(horse.level(), EntitySpawnReason.TRIGGERED);
/*  85 */     if (skeleton != null) {
/*  86 */       skeleton.finalizeSpawn((ServerLevel)horse.level(), difficulty, EntitySpawnReason.TRIGGERED, null);
/*  87 */       skeleton.setPos(horse.getX(), horse.getY(), horse.getZ());
/*  88 */       skeleton.invulnerableTime = 60;
/*  89 */       skeleton.setPersistenceRequired();
/*     */       
/*  91 */       if (skeleton.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/*  92 */         skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
/*     */       }
/*     */       
/*  95 */       enchant(skeleton, EquipmentSlot.MAINHAND, difficulty);
/*  96 */       enchant(skeleton, EquipmentSlot.HEAD, difficulty);
/*     */     } 
/*  98 */     return skeleton;
/*     */   }
/*     */   
/*     */   private void enchant(Skeleton skeleton, EquipmentSlot slot, DifficultyInstance difficulty) {
/* 102 */     ItemStack stack = skeleton.getItemBySlot(slot);
/* 103 */     stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 104 */     EnchantmentHelper.enchantItemFromProvider(stack, skeleton.level().registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, skeleton.getRandom());
/* 105 */     skeleton.setItemSlot(slot, stack);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\SkeletonTrapGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
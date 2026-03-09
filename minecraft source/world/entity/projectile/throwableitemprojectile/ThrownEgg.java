/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.particles.ItemParticleOption;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*    */ import net.minecraft.world.item.EitherHolder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownEgg
/*    */   extends ThrowableItemProjectile {
/* 23 */   private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);
/*    */ 
/*    */   
/* 26 */   public ThrownEgg(EntityType<? extends ThrownEgg> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public ThrownEgg(Level level, LivingEntity mob, ItemStack itemStack) { super(EntityType.EGG, mob, level, itemStack); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public ThrownEgg(Level level, double x, double y, double z, ItemStack itemStack) { super(EntityType.EGG, x, y, z, level, itemStack); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleEntityEvent(byte id) {
/* 39 */     if (id == 3) {
/* 40 */       double v = 0.08D;
/* 41 */       for (int i = 0; i < 8; i++) {
/* 42 */         level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/* 49 */     super.onHitEntity(hitResult);
/* 50 */     hitResult.getEntity().hurt(damageSources().thrown(this, getOwner()), 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 55 */     super.onHit(hitResult);
/*    */     
/* 57 */     if (!level().isClientSide()) {
/* 58 */       if (this.random.nextInt(8) == 0) {
/* 59 */         int count = 1;
/* 60 */         if (this.random.nextInt(32) == 0) {
/* 61 */           count = 4;
/*    */         }
/* 63 */         for (int i = 0; i < count; i++) {
/* 64 */           Chicken chicken = (Chicken)EntityType.CHICKEN.create(level(), EntitySpawnReason.TRIGGERED);
/* 65 */           if (chicken != null) {
/* 66 */             chicken.setAge(-24000);
/* 67 */             chicken.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/*    */ 
/*    */ 
/*    */             
/* 71 */             Objects.requireNonNull(chicken); Optional.ofNullable((EitherHolder)getItem().get(DataComponents.CHICKEN_VARIANT)).flatMap(v -> v.unwrap(registryAccess())).ifPresent(chicken::setVariant);
/*    */             
/* 73 */             if (!chicken.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
/*    */               break;
/*    */             }
/* 76 */             level().addFreshEntity(chicken);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 81 */       level().broadcastEntityEvent(this, (byte)3);
/* 82 */       discard();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 88 */   protected Item getDefaultItem() { return Items.EGG; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownEgg.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
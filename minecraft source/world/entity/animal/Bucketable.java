/*    */ package net.minecraft.world.entity.animal;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemUtils;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Bucketable
/*    */ {
/*    */   @Deprecated
/*    */   static void saveDefaultDataToBucketTag(Mob entity, ItemStack bucket) {
/* 41 */     bucket.copyFrom(DataComponents.CUSTOM_NAME, entity);
/* 42 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
/* 43 */           if (entity.isNoAi()) {
/* 44 */             tag.putBoolean("NoAI", entity.isNoAi());
/*    */           }
/* 46 */           if (entity.isSilent()) {
/* 47 */             tag.putBoolean("Silent", entity.isSilent());
/*    */           }
/* 49 */           if (entity.isNoGravity()) {
/* 50 */             tag.putBoolean("NoGravity", entity.isNoGravity());
/*    */           }
/* 52 */           if (entity.hasGlowingTag()) {
/* 53 */             tag.putBoolean("Glowing", entity.hasGlowingTag());
/*    */           }
/* 55 */           if (entity.isInvulnerable()) {
/* 56 */             tag.putBoolean("Invulnerable", entity.isInvulnerable());
/*    */           }
/* 58 */           tag.putFloat("Health", entity.getHealth());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   static void loadDefaultDataFromBucketTag(Mob entity, CompoundTag tag) {
/* 68 */     Objects.requireNonNull(entity); tag.getBoolean("NoAI").ifPresent(entity::setNoAi);
/* 69 */     Objects.requireNonNull(entity); tag.getBoolean("Silent").ifPresent(entity::setSilent);
/* 70 */     Objects.requireNonNull(entity); tag.getBoolean("NoGravity").ifPresent(entity::setNoGravity);
/* 71 */     Objects.requireNonNull(entity); tag.getBoolean("Glowing").ifPresent(entity::setGlowingTag);
/* 72 */     Objects.requireNonNull(entity); tag.getBoolean("Invulnerable").ifPresent(entity::setInvulnerable);
/* 73 */     Objects.requireNonNull(entity); tag.getFloat("Health").ifPresent(entity::setHealth);
/*    */   }
/*    */   
/*    */   static <T extends net.minecraft.world.entity.LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T pickupEntity) {
/* 77 */     ItemStack itemStack = player.getItemInHand(hand);
/*    */     
/* 79 */     if (itemStack.getItem() == Items.WATER_BUCKET && pickupEntity.isAlive()) {
/* 80 */       pickupEntity.playSound(((Bucketable)pickupEntity).getPickupSound(), 1.0F, 1.0F);
/*    */       
/* 82 */       ItemStack bucket = ((Bucketable)pickupEntity).getBucketItemStack();
/* 83 */       ((Bucketable)pickupEntity).saveToBucketTag(bucket);
/*    */       
/* 85 */       ItemStack result = ItemUtils.createFilledResult(itemStack, player, bucket, false);
/* 86 */       player.setItemInHand(hand, result);
/*    */       
/* 88 */       Level level = pickupEntity.level();
/*    */       
/* 90 */       if (!level.isClientSide()) {
/* 91 */         CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucket);
/*    */       }
/*    */       
/* 94 */       pickupEntity.discard();
/*    */       
/* 96 */       return Optional.of(InteractionResult.SUCCESS);
/*    */     } 
/* 98 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   boolean fromBucket();
/*    */   
/*    */   void setFromBucket(boolean paramBoolean);
/*    */   
/*    */   void saveToBucketTag(ItemStack paramItemStack);
/*    */   
/*    */   void loadFromBucketTag(CompoundTag paramCompoundTag);
/*    */   
/*    */   ItemStack getBucketItemStack();
/*    */   
/*    */   SoundEvent getPickupSound();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\Bucketable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
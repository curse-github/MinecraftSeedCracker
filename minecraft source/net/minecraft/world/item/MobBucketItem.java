/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.animal.Bucketable;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public class MobBucketItem
/*    */   extends BucketItem {
/*    */   private final EntityType<? extends Mob> type;
/*    */   private final SoundEvent emptySound;
/*    */   
/*    */   public MobBucketItem(EntityType<? extends Mob> type, Fluid content, SoundEvent emptySound, Item.Properties properties) {
/* 25 */     super(content, properties);
/* 26 */     this.type = type;
/* 27 */     this.emptySound = emptySound;
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkExtraContent(LivingEntity user, Level level, ItemStack itemStack, BlockPos pos) {
/* 32 */     if (level instanceof ServerLevel) {
/* 33 */       spawn((ServerLevel)level, itemStack, pos);
/* 34 */       level.gameEvent(user, GameEvent.ENTITY_PLACE, pos);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected void playEmptySound(LivingEntity user, LevelAccessor level, BlockPos pos) { level.playSound(user, pos, this.emptySound, SoundSource.NEUTRAL, 1.0F, 1.0F); }
/*    */ 
/*    */   
/*    */   private void spawn(ServerLevel level, ItemStack itemStack, BlockPos spawnPos) {
/* 44 */     Mob mob = (Mob)this.type.create(level, EntityType.createDefaultStackConfig(level, itemStack, null), spawnPos, EntitySpawnReason.BUCKET, true, false);
/*    */     
/* 46 */     if (mob instanceof Bucketable) { Bucketable bucketable = (Bucketable)mob;
/* 47 */       CustomData entityData = (CustomData)itemStack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
/* 48 */       bucketable.loadFromBucketTag(entityData.copyTag());
/* 49 */       bucketable.setFromBucket(true); }
/*    */ 
/*    */     
/* 52 */     if (mob != null) {
/* 53 */       level.addFreshEntityWithPassengers(mob);
/* 54 */       mob.playAmbientSound();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\MobBucketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Cod
/*    */   extends AbstractSchoolingFish {
/* 13 */   public Cod(EntityType<? extends Cod> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public ItemStack getBucketItemStack() { return new ItemStack(Items.COD_BUCKET); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected SoundEvent getAmbientSound() { return SoundEvents.COD_AMBIENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected SoundEvent getDeathSound() { return SoundEvents.COD_DEATH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.COD_HURT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected SoundEvent getFlopSound() { return SoundEvents.COD_FLOP; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\Cod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
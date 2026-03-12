/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.SlotAccess;
/*    */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class Fireball
/*    */   extends AbstractHurtingProjectile implements ItemSupplier {
/*    */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/* 20 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(Fireball.class, EntityDataSerializers.ITEM_STACK);
/*    */ 
/*    */   
/* 23 */   public Fireball(EntityType<? extends Fireball> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public Fireball(EntityType<? extends Fireball> type, double x, double y, double z, Vec3 direction, Level level) { super(type, x, y, z, direction, level); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Fireball(EntityType<? extends Fireball> type, LivingEntity mob, Vec3 direction, Level level) { super(type, mob, direction, level); }
/*    */ 
/*    */   
/*    */   public void setItem(ItemStack source) {
/* 35 */     if (source.isEmpty()) {
/* 36 */       getEntityData().set(DATA_ITEM_STACK, getDefaultItem());
/*    */     } else {
/* 38 */       getEntityData().set(DATA_ITEM_STACK, source.copyWithCount(1));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void playEntityOnFireExtinguishedSound() {}
/*    */ 
/*    */ 
/*    */   
/* 48 */   public ItemStack getItem() { return (ItemStack)getEntityData().get(DATA_ITEM_STACK); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_ITEM_STACK, getDefaultItem()); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 58 */     super.addAdditionalSaveData(output);
/* 59 */     output.store("Item", ItemStack.CODEC, getItem());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 64 */     super.readAdditionalSaveData(input);
/* 65 */     setItem((ItemStack)input.read("Item", ItemStack.CODEC).orElse(getDefaultItem()));
/*    */   }
/*    */ 
/*    */   
/* 69 */   private ItemStack getDefaultItem() { return new ItemStack(Items.FIRE_CHARGE); }
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotAccess getSlot(int slot) {
/* 74 */     if (slot == 0) {
/* 75 */       return SlotAccess.of(this::getItem, this::setItem);
/*    */     }
/* 77 */     return super.getSlot(slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 82 */     if (this.tickCount < 2 && distance < 12.25D) {
/* 83 */       return false;
/*    */     }
/* 85 */     return super.shouldRenderAtSqrDistance(distance);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\Fireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
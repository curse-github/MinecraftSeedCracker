/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface OwnableEntity
/*    */ {
/*    */   EntityReference<LivingEntity> getOwnerReference();
/*    */   
/*    */   Level level();
/*    */   
/* 15 */   default LivingEntity getOwner() { return EntityReference.getLivingEntity(getOwnerReference(), level()); }
/*    */ 
/*    */ 
/*    */   
/*    */   default LivingEntity getRootOwner() {
/* 20 */     ObjectArraySet objectArraySet = new ObjectArraySet();
/* 21 */     LivingEntity owner = getOwner();
/* 22 */     objectArraySet.add(this);
/* 23 */     while (owner instanceof OwnableEntity) { OwnableEntity ownableOwner = (OwnableEntity)owner;
/* 24 */       LivingEntity ownersOwner = ownableOwner.getOwner();
/* 25 */       if (objectArraySet.contains(ownersOwner)) {
/* 26 */         return null;
/*    */       }
/* 28 */       objectArraySet.add(owner);
/* 29 */       owner = ownableOwner.getOwner(); }
/*    */     
/* 31 */     return owner;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\OwnableEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
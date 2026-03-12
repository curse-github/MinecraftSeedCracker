/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*    */ 
/*    */ public interface ContainerUser
/*    */ {
/*    */   boolean hasContainerOpen(ContainerOpenersCounter paramContainerOpenersCounter, BlockPos paramBlockPos);
/*    */   
/*    */   double getContainerInteractionRange();
/*    */   
/*    */   default LivingEntity getLivingEntity() {
/* 13 */     if (this instanceof LivingEntity) {
/* 14 */       return (LivingEntity)this;
/*    */     }
/* 16 */     throw new IllegalStateException("A container user must be a LivingEntity");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ContainerUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
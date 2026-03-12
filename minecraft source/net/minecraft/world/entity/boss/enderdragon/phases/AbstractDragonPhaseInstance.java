/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class AbstractDragonPhaseInstance
/*    */   implements DragonPhaseInstance
/*    */ {
/*    */   protected final EnderDragon dragon;
/*    */   
/* 16 */   public AbstractDragonPhaseInstance(EnderDragon dragon) { this.dragon = dragon; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public boolean isSitting() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doClientTick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onCrystalDestroyed(EndCrystal crystal, BlockPos pos, DamageSource source, Player player) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void begin() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void end() {}
/*    */ 
/*    */ 
/*    */   
/* 46 */   public float getFlySpeed() { return 0.6F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Vec3 getFlyTargetLocation() { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public float onHurt(DamageSource source, float damage) { return damage; }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getTurnSpeed() {
/* 61 */     float rotSpeed = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
/* 62 */     float dist = Math.min(rotSpeed, 40.0F);
/*    */     
/* 64 */     return 0.7F / dist / rotSpeed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonPhaseInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
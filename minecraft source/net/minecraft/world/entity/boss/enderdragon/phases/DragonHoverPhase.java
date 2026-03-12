/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonHoverPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/*    */   private Vec3 targetLocation;
/*    */   
/* 12 */   public DragonHoverPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 17 */     if (this.targetLocation == null) {
/* 18 */       this.targetLocation = this.dragon.position();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean isSitting() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void begin() { this.targetLocation = null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public float getFlySpeed() { return 1.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public EnderDragonPhase<DragonHoverPhase> getPhase() { return EnderDragonPhase.HOVERING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonHoverPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
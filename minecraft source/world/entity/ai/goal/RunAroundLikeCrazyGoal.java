/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RunAroundLikeCrazyGoal
/*    */   extends Goal
/*    */ {
/*    */   private final AbstractHorse horse;
/*    */   private final double speedModifier;
/*    */   private double posX;
/*    */   private double posY;
/*    */   private double posZ;
/*    */   
/*    */   public RunAroundLikeCrazyGoal(AbstractHorse mob, double speedModifier) {
/* 20 */     this.horse = mob;
/* 21 */     this.speedModifier = speedModifier;
/* 22 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     if (this.horse.isMobControlled() || this.horse.isTamed() || !this.horse.isVehicle()) {
/* 28 */       return false;
/*    */     }
/* 30 */     Vec3 pos = DefaultRandomPos.getPos(this.horse, 5, 4);
/* 31 */     if (pos == null) {
/* 32 */       return false;
/*    */     }
/* 34 */     this.posX = pos.x;
/* 35 */     this.posY = pos.y;
/* 36 */     this.posZ = pos.z;
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void start() { this.horse.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean canContinueToUse() { return (!this.horse.isTamed() && !this.horse.getNavigation().isDone() && this.horse.isVehicle()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 52 */     if (!this.horse.isTamed() && this.horse.getRandom().nextInt(adjustedTickDelay(50)) == 0) {
/* 53 */       Entity passenger = this.horse.getFirstPassenger();
/* 54 */       if (passenger == null) {
/*    */         return;
/*    */       }
/*    */       
/* 58 */       if (passenger instanceof Player) { Player player = (Player)passenger;
/* 59 */         int temper = this.horse.getTemper();
/* 60 */         int maxTemper = this.horse.getMaxTemper();
/* 61 */         if (maxTemper > 0 && this.horse.getRandom().nextInt(maxTemper) < temper) {
/* 62 */           this.horse.tameWithName(player);
/*    */           return;
/*    */         } 
/* 65 */         this.horse.modifyTemper(5); }
/*    */ 
/*    */       
/* 68 */       this.horse.ejectPassengers();
/* 69 */       this.horse.makeMad();
/* 70 */       this.horse.level().broadcastEntityEvent(this.horse, (byte)6);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RunAroundLikeCrazyGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
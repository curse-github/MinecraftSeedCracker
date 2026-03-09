/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.InterpolationHandler;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public abstract class MinecartBehavior
/*    */ {
/*    */   protected final AbstractMinecart minecart;
/*    */   
/* 16 */   protected MinecartBehavior(AbstractMinecart minecart) { this.minecart = minecart; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public InterpolationHandler getInterpolation() { return null; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void lerpMotion(Vec3 movement) { setDeltaMovement(movement); }
/*    */ 
/*    */   
/*    */   public abstract void tick();
/*    */ 
/*    */   
/* 30 */   public Level level() { return this.minecart.level(); }
/*    */ 
/*    */   
/*    */   public abstract void moveAlongTrack(ServerLevel paramServerLevel);
/*    */ 
/*    */   
/*    */   public abstract double stepAlongTrack(BlockPos paramBlockPos, RailShape paramRailShape, double paramDouble);
/*    */   
/*    */   public abstract boolean pushAndPickupEntities();
/*    */   
/* 40 */   public Vec3 getDeltaMovement() { return this.minecart.getDeltaMovement(); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public void setDeltaMovement(Vec3 deltaMovement) { this.minecart.setDeltaMovement(deltaMovement); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public void setDeltaMovement(double x, double y, double z) { this.minecart.setDeltaMovement(x, y, z); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public Vec3 position() { return this.minecart.position(); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public double getX() { return this.minecart.getX(); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public double getY() { return this.minecart.getY(); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public double getZ() { return this.minecart.getZ(); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public void setPos(Vec3 pos) { this.minecart.setPos(pos); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public void setPos(double x, double y, double z) { this.minecart.setPos(x, y, z); }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public float getXRot() { return this.minecart.getXRot(); }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public void setXRot(float rot) { this.minecart.setXRot(rot); }
/*    */ 
/*    */ 
/*    */   
/* 84 */   public float getYRot() { return this.minecart.getYRot(); }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public void setYRot(float rot) { this.minecart.setYRot(rot); }
/*    */ 
/*    */ 
/*    */   
/* 92 */   public Direction getMotionDirection() { return this.minecart.getDirection(); }
/*    */ 
/*    */ 
/*    */   
/* 96 */   public Vec3 getKnownMovement(Vec3 knownMovement) { return knownMovement; }
/*    */   
/*    */   public abstract double getMaxSpeed(ServerLevel paramServerLevel);
/*    */   
/*    */   public abstract double getSlowdownFactor();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerEntity;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.material.PushReaction;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class Marker
/*    */   extends Entity {
/*    */   public Marker(EntityType<?> type, Level level) {
/* 17 */     super(type, level);
/* 18 */     this.noPhysics = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {}
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { throw new IllegalStateException("Markers should never be sent"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected boolean canAddPassenger(Entity passenger) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected boolean couldAcceptPassenger() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected void addPassenger(Entity passenger) { throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public PushReaction getPistonPushReaction() { return PushReaction.IGNORE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public boolean isIgnoringBlockTriggers() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Marker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
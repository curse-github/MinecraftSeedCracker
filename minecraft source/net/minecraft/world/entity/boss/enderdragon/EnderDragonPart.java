/*    */ package net.minecraft.world.entity.boss.enderdragon;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerEntity;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class EnderDragonPart
/*    */   extends Entity
/*    */ {
/*    */   public final EnderDragon parentMob;
/*    */   public final String name;
/*    */   private final EntityDimensions size;
/*    */   
/*    */   public EnderDragonPart(EnderDragon parentMob, String name, float w, float h) {
/* 24 */     super(parentMob.getType(), parentMob.level());
/* 25 */     this.size = EntityDimensions.scalable(w, h);
/* 26 */     refreshDimensions();
/* 27 */     this.parentMob = parentMob;
/* 28 */     this.name = name;
/*    */   }
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
/* 45 */   public boolean isPickable() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public ItemStack getPickResult() { return this.parentMob.getPickResult(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 55 */     if (isInvulnerableToBase(source)) {
/* 56 */       return false;
/*    */     }
/* 58 */     return this.parentMob.hurt(level, this, source, damage);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public boolean is(Entity other) { return (this == other || this.parentMob == other); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { throw new UnsupportedOperationException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public EntityDimensions getDimensions(Pose pose) { return this.size; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public boolean shouldBeSaved() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\EnderDragonPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
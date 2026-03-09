/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.world.entity.player.PlayerModelPart;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Avatar
/*    */   extends LivingEntity {
/* 14 */   public static final HumanoidArm DEFAULT_MAIN_HAND = HumanoidArm.RIGHT;
/*    */   public static final int DEFAULT_MODEL_CUSTOMIZATION = 0;
/*    */   public static final float DEFAULT_EYE_HEIGHT = 1.62F;
/* 17 */   public static final Vec3 DEFAULT_VEHICLE_ATTACHMENT = new Vec3(0.0D, 0.6D, 0.0D);
/*    */   
/*    */   private static final float CROUCH_BB_HEIGHT = 1.5F;
/*    */   
/*    */   private static final float SWIMMING_BB_WIDTH = 0.6F;
/*    */   public static final float SWIMMING_BB_HEIGHT = 0.6F;
/* 23 */   protected static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.8F)
/* 24 */     .withEyeHeight(1.62F)
/* 25 */     .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, DEFAULT_VEHICLE_ATTACHMENT));
/*    */   
/* 27 */   protected static final Map<Pose, EntityDimensions> POSES = ImmutableMap.builder()
/* 28 */     .put(Pose.STANDING, STANDING_DIMENSIONS)
/* 29 */     .put(Pose.SLEEPING, SLEEPING_DIMENSIONS)
/* 30 */     .put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F))
/* 31 */     .put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F))
/* 32 */     .put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F))
/* 33 */     .put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.5F)
/* 34 */       .withEyeHeight(1.27F)
/* 35 */       .withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, DEFAULT_VEHICLE_ATTACHMENT)))
/*    */     
/* 37 */     .put(Pose.DYING, EntityDimensions.fixed(0.2F, 0.2F).withEyeHeight(1.62F))
/* 38 */     .build();
/*    */   
/* 40 */   protected static final EntityDataAccessor<HumanoidArm> DATA_PLAYER_MAIN_HAND = SynchedEntityData.defineId(Avatar.class, EntityDataSerializers.HUMANOID_ARM);
/* 41 */   protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.defineId(Avatar.class, EntityDataSerializers.BYTE);
/*    */ 
/*    */   
/* 44 */   protected Avatar(EntityType<? extends LivingEntity> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 49 */     super.defineSynchedData(entityData);
/*    */     
/* 51 */     entityData.define(DATA_PLAYER_MAIN_HAND, DEFAULT_MAIN_HAND);
/* 52 */     entityData.define(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf((byte)0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public HumanoidArm getMainArm() { return (HumanoidArm)this.entityData.get(DATA_PLAYER_MAIN_HAND); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public void setMainArm(HumanoidArm mainArm) { this.entityData.set(DATA_PLAYER_MAIN_HAND, mainArm); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public boolean isModelPartShown(PlayerModelPart part) { return ((((Byte)getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION)).byteValue() & part.getMask()) == part.getMask()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public EntityDimensions getDefaultDimensions(Pose pose) { return (EntityDimensions)POSES.getOrDefault(pose, STANDING_DIMENSIONS); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Avatar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
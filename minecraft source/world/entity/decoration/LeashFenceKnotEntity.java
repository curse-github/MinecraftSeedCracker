/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class LeashFenceKnotEntity
/*     */   extends BlockAttachedEntity
/*     */ {
/*     */   public static final double OFFSET_Y = 0.375D;
/*     */   
/*  34 */   public LeashFenceKnotEntity(EntityType<? extends LeashFenceKnotEntity> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public LeashFenceKnotEntity(Level level, BlockPos pos) {
/*  38 */     super(EntityType.LEASH_KNOT, level, pos);
/*  39 */     setPos(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */ 
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  48 */     setPosRaw(this.pos.getX() + 0.5D, this.pos.getY() + 0.375D, this.pos.getZ() + 0.5D);
/*  49 */     double halfWidth = getType().getWidth() / 2.0D;
/*  50 */     double height = getType().getHeight();
/*  51 */     setBoundingBox(new AABB(getX() - halfWidth, getY(), getZ() - halfWidth, getX() + halfWidth, getY() + height, getZ() + halfWidth));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public boolean shouldRenderAtSqrDistance(double distance) { return (distance < 1024.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public void dropItem(ServerLevel level, Entity causedBy) { playSound(SoundEvents.LEAD_UNTIED, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  74 */     if (level().isClientSide()) {
/*  75 */       return InteractionResult.SUCCESS;
/*     */     }
/*  77 */     if (player.getItemInHand(hand).is(Items.SHEARS)) {
/*     */       
/*  79 */       InteractionResult result = super.interact(player, hand);
/*  80 */       if (result instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)result; if (success.wasItemInteraction()) {
/*  81 */           return result;
/*     */         } }
/*     */     
/*     */     } 
/*  85 */     boolean attachedMob = false;
/*     */     
/*  87 */     List<Leashable> playerLeashable = Leashable.leashableLeashedTo(player);
/*     */     
/*  89 */     for (Leashable leashable : playerLeashable) {
/*  90 */       if (leashable.canHaveALeashAttachedTo(this)) {
/*  91 */         leashable.setLeashedTo(this, true);
/*  92 */         attachedMob = true;
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     boolean anyDropped = false;
/*  97 */     if (!attachedMob && !player.isSecondaryUseActive()) {
/*  98 */       List<Leashable> knotLeashable = Leashable.leashableLeashedTo(this);
/*  99 */       for (Leashable mob : knotLeashable) {
/* 100 */         if (mob.canHaveALeashAttachedTo(player)) {
/* 101 */           mob.setLeashedTo(player, true);
/* 102 */           anyDropped = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     if (attachedMob || anyDropped) {
/* 108 */       gameEvent(GameEvent.BLOCK_ATTACH, player);
/* 109 */       playSound(SoundEvents.LEAD_TIED);
/* 110 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 113 */     return super.interact(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void notifyLeasheeRemoved(Leashable entity) {
/* 118 */     if (Leashable.leashableLeashedTo(this).isEmpty()) {
/* 119 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public boolean survives() { return level().getBlockState(this.pos).is(BlockTags.FENCES); }
/*     */ 
/*     */   
/*     */   public static LeashFenceKnotEntity getOrCreateKnot(Level level, BlockPos pos) {
/* 130 */     int x = pos.getX();
/* 131 */     int y = pos.getY();
/* 132 */     int z = pos.getZ();
/*     */     
/* 134 */     List<LeashFenceKnotEntity> knots = level.getEntitiesOfClass(LeashFenceKnotEntity.class, new AABB(x - 1.0D, y - 1.0D, z - 1.0D, x + 1.0D, y + 1.0D, z + 1.0D));
/* 135 */     for (LeashFenceKnotEntity knot : knots) {
/* 136 */       if (knot.getPos().equals(pos)) {
/* 137 */         return knot;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     LeashFenceKnotEntity knot = new LeashFenceKnotEntity(level, pos);
/* 142 */     level.addFreshEntity(knot);
/* 143 */     return knot;
/*     */   }
/*     */ 
/*     */   
/* 147 */   public void playPlacementSound() { playSound(SoundEvents.LEAD_TIED, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) { return new ClientboundAddEntityPacket(this, 0, getPos()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public Vec3 getRopeHoldPosition(float partialTickTime) { return getPosition(partialTickTime).add(0.0D, 0.2D, 0.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public ItemStack getPickResult() { return new ItemStack(Items.LEAD); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\LeashFenceKnotEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
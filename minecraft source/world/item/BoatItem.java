/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BoatItem
/*    */   extends Item {
/*    */   private final EntityType<? extends AbstractBoat> entityType;
/*    */   
/*    */   public BoatItem(EntityType<? extends AbstractBoat> entityType, Item.Properties properties) {
/* 27 */     super(properties);
/* 28 */     this.entityType = entityType;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 33 */     ItemStack itemStack = player.getItemInHand(hand);
/*    */     
/* 35 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
/* 36 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 37 */       return InteractionResult.PASS;
/*    */     }
/*    */ 
/*    */     
/* 41 */     Vec3 viewVector = player.getViewVector(1.0F);
/* 42 */     double range = 5.0D;
/* 43 */     List<Entity> entities = level.getEntities(player, player.getBoundingBox().expandTowards(viewVector.scale(5.0D)).inflate(1.0D), EntitySelector.CAN_BE_PICKED);
/* 44 */     if (!entities.isEmpty()) {
/* 45 */       Vec3 from = player.getEyePosition();
/* 46 */       for (Entity entity : entities) {
/* 47 */         AABB bb = entity.getBoundingBox().inflate(entity.getPickRadius());
/* 48 */         if (bb.contains(from)) {
/* 49 */           return InteractionResult.PASS;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/* 55 */       AbstractBoat boat = getBoat(level, blockHitResult, itemStack, player);
/* 56 */       if (boat == null) {
/* 57 */         return InteractionResult.FAIL;
/*    */       }
/* 59 */       boat.setYRot(player.getYRot());
/* 60 */       if (!level.noCollision(boat, boat.getBoundingBox())) {
/* 61 */         return InteractionResult.FAIL;
/*    */       }
/* 63 */       if (!level.isClientSide()) {
/* 64 */         level.addFreshEntity(boat);
/* 65 */         level.gameEvent(player, GameEvent.ENTITY_PLACE, blockHitResult.getLocation());
/* 66 */         itemStack.consume(1, player);
/*    */       } 
/* 68 */       player.awardStat(Stats.ITEM_USED.get(this));
/*    */       
/* 70 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 73 */     return InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   private AbstractBoat getBoat(Level level, HitResult hitResult, ItemStack itemStack, Player player) {
/* 77 */     AbstractBoat boat = (AbstractBoat)this.entityType.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
/*    */     
/* 79 */     if (boat != null) {
/* 80 */       Vec3 location = hitResult.getLocation();
/* 81 */       boat.setInitialPos(location.x, location.y, location.z);
/* 82 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 83 */         EntityType.createDefaultStackConfig(serverLevel, itemStack, player).accept(boat); }
/*    */     
/*    */     } 
/* 86 */     return boat;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BoatItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.ArmorStand;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ArmorStandItem
/*    */   extends Item
/*    */ {
/* 24 */   public ArmorStandItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 29 */     Direction clickedFace = context.getClickedFace();
/* 30 */     if (clickedFace == Direction.DOWN) {
/* 31 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 34 */     Level level = context.getLevel();
/* 35 */     BlockPlaceContext placeContext = new BlockPlaceContext(context);
/* 36 */     BlockPos blockPos = placeContext.getClickedPos();
/*    */     
/* 38 */     ItemStack itemStack = context.getItemInHand();
/* 39 */     Vec3 pos = Vec3.atBottomCenterOf(blockPos);
/* 40 */     AABB box = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(pos.x(), pos.y(), pos.z());
/*    */     
/* 42 */     if (!level.noCollision(null, box) || !level.getEntities(null, box).isEmpty()) {
/* 43 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 46 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 47 */       Consumer<ArmorStand> entityConfig = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
/* 48 */       ArmorStand entity = (ArmorStand)EntityType.ARMOR_STAND.create(serverLevel, entityConfig, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
/*    */       
/* 50 */       if (entity == null) {
/* 51 */         return InteractionResult.FAIL;
/*    */       }
/*    */       
/* 54 */       float yRot = Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
/* 55 */       entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), yRot, 0.0F);
/*    */       
/* 57 */       serverLevel.addFreshEntityWithPassengers(entity);
/*    */       
/* 59 */       level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
/* 60 */       entity.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer()); }
/*    */ 
/*    */     
/* 63 */     itemStack.shrink(1);
/* 64 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ArmorStandItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
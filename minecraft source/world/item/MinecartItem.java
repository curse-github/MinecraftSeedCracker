/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseRailBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartItem
/*    */   extends Item {
/*    */   private final EntityType<? extends AbstractMinecart> type;
/*    */   
/*    */   public MinecartItem(EntityType<? extends AbstractMinecart> type, Item.Properties properties) {
/* 25 */     super(properties);
/* 26 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 31 */     Level level = context.getLevel();
/* 32 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 34 */     BlockState blockState = level.getBlockState(pos);
/* 35 */     if (!blockState.is(BlockTags.RAILS)) {
/* 36 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 39 */     ItemStack itemStack = context.getItemInHand();
/*    */     
/* 41 */     RailShape shape = (blockState.getBlock() instanceof BaseRailBlock) ? (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
/* 42 */     double offset = 0.0D;
/* 43 */     if (shape.isSlope()) {
/* 44 */       offset = 0.5D;
/*    */     }
/* 46 */     Vec3 spawnPos = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.0625D + offset, pos.getZ() + 0.5D);
/* 47 */     AbstractMinecart cart = AbstractMinecart.createMinecart(level, spawnPos.x, spawnPos.y, spawnPos.z, this.type, EntitySpawnReason.DISPENSER, itemStack, context.getPlayer());
/* 48 */     if (cart == null) {
/* 49 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 52 */     if (AbstractMinecart.useExperimentalMovement(level)) {
/* 53 */       List<Entity> entities = level.getEntities(null, cart.getBoundingBox());
/* 54 */       for (Entity entity : entities) {
/* 55 */         if (entity instanceof AbstractMinecart) {
/* 56 */           return InteractionResult.FAIL;
/*    */         }
/*    */       } 
/*    */     } 
/* 60 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 61 */       serverLevel.addFreshEntity(cart);
/* 62 */       serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), serverLevel.getBlockState(pos.below()))); }
/*    */ 
/*    */     
/* 65 */     itemStack.shrink(1);
/* 66 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\MinecartItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
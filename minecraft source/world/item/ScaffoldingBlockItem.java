/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.ScaffoldingBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ScaffoldingBlockItem
/*    */   extends BlockItem
/*    */ {
/* 18 */   public ScaffoldingBlockItem(Block block, Item.Properties properties) { super(block, properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
/* 23 */     BlockPos pos = context.getClickedPos();
/* 24 */     Level level = context.getLevel();
/*    */     
/* 26 */     BlockState replacedState = level.getBlockState(pos);
/* 27 */     Block block = getBlock();
/* 28 */     if (replacedState.is(block)) {
/*    */       Direction direction;
/* 30 */       if (context.isSecondaryUseActive()) {
/* 31 */         direction = context.isInside() ? context.getClickedFace().getOpposite() : context.getClickedFace();
/*    */       } else {
/* 33 */         direction = (context.getClickedFace() == Direction.UP) ? context.getHorizontalDirection() : Direction.UP;
/*    */       } 
/*    */       
/* 36 */       int horizontalDistance = 0;
/* 37 */       BlockPos.MutableBlockPos placementPos = pos.mutable().move(direction);
/* 38 */       while (horizontalDistance < 7) {
/* 39 */         if (!level.isClientSide() && !level.isInWorldBounds(placementPos)) {
/*    */           
/* 41 */           Player player = context.getPlayer();
/* 42 */           int maxY = level.getMaxY();
/* 43 */           if (player instanceof ServerPlayer && placementPos.getY() > maxY) {
/* 44 */             ((ServerPlayer)player).sendSystemMessage(Component.translatable("build.tooHigh", new Object[] { Integer.valueOf(maxY) }).withStyle(ChatFormatting.RED), true);
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/* 49 */         replacedState = level.getBlockState(placementPos);
/*    */         
/* 51 */         if (!replacedState.is(getBlock())) {
/* 52 */           if (replacedState.canBeReplaced(context)) {
/* 53 */             return BlockPlaceContext.at(context, placementPos, direction);
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/* 58 */         placementPos.move(direction);
/* 59 */         if (direction.getAxis().isHorizontal()) {
/* 60 */           horizontalDistance++;
/*    */         }
/*    */       } 
/*    */       
/* 64 */       return null;
/*    */     } 
/*    */     
/* 67 */     if (ScaffoldingBlock.getDistance(level, pos) == 7) {
/* 68 */       return null;
/*    */     }
/*    */     
/* 71 */     return context;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   protected boolean mustSurvive() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ScaffoldingBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
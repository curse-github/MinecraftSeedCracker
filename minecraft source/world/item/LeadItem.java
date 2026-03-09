/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Leashable;
/*    */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LeadItem
/*    */   extends Item
/*    */ {
/* 19 */   public LeadItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 24 */     Level level = context.getLevel();
/* 25 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 27 */     BlockState state = level.getBlockState(pos);
/* 28 */     if (state.is(BlockTags.FENCES)) {
/* 29 */       Player player = context.getPlayer();
/* 30 */       if (!level.isClientSide() && player != null) {
/* 31 */         return bindPlayerMobs(player, level, pos);
/*    */       }
/*    */     } 
/*    */     
/* 35 */     return InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static InteractionResult bindPlayerMobs(Player player, Level level, BlockPos pos) {
/* 39 */     LeashFenceKnotEntity activeKnot = null;
/*    */     
/* 41 */     List<Leashable> entitiesToLeash = Leashable.leashableInArea(level, Vec3.atCenterOf(pos), l -> (l.getLeashHolder() == player));
/* 42 */     boolean anyLeashed = false;
/* 43 */     for (Leashable leashable : entitiesToLeash) {
/* 44 */       if (activeKnot == null) {
/* 45 */         activeKnot = LeashFenceKnotEntity.getOrCreateKnot(level, pos);
/* 46 */         activeKnot.playPlacementSound();
/*    */       } 
/* 48 */       if (leashable.canHaveALeashAttachedTo(activeKnot)) {
/* 49 */         leashable.setLeashedTo(activeKnot, true);
/* 50 */         anyLeashed = true;
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     if (anyLeashed) {
/* 55 */       level.gameEvent(GameEvent.BLOCK_ATTACH, pos, GameEvent.Context.of(player));
/* 56 */       return InteractionResult.SUCCESS_SERVER;
/*    */     } 
/*    */     
/* 59 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\LeadItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
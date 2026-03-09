/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class PlaceOnWaterBlockItem
/*    */   extends BlockItem {
/* 14 */   public PlaceOnWaterBlockItem(Block block, Item.Properties properties) { super(block, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public InteractionResult useOn(UseOnContext context) { return InteractionResult.PASS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 24 */     BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
/* 25 */     BlockHitResult blockAboveResult = hitResult.withPosition(hitResult.getBlockPos().above());
/* 26 */     return super.useOn(new UseOnContext(player, hand, blockAboveResult));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\PlaceOnWaterBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
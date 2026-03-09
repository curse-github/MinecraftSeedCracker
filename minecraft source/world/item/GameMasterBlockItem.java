/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GameMasterBlockItem
/*    */   extends BlockItem
/*    */ {
/* 11 */   public GameMasterBlockItem(Block block, Item.Properties properties) { super(block, properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState getPlacementState(BlockPlaceContext context) {
/* 16 */     Player player = context.getPlayer();
/* 17 */     return (player == null || player.canUseGameMasterBlocks()) ? super.getPlacementState(context) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\GameMasterBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedItem
/*    */   extends BlockItem {
/*  9 */   public BedItem(Block block, Item.Properties properties) { super(block, properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) { return context.getLevel().setBlock(context.getClickedPos(), placementState, 26); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BedItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
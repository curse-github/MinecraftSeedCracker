/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.WallHangingSignBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HangingSignItem
/*    */   extends SignItem {
/* 12 */   public HangingSignItem(Block hangingSign, Block wallHangingSign, Item.Properties properties) { super(properties, hangingSign, wallHangingSign, Direction.UP); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canPlace(LevelReader level, BlockState possibleState, BlockPos pos) {
/* 17 */     Block block = possibleState.getBlock(); if (block instanceof WallHangingSignBlock) { WallHangingSignBlock hangingSign = (WallHangingSignBlock)block;
/* 18 */       if (!hangingSign.canPlace(possibleState, level, pos)) {
/* 19 */         return false;
/*    */       } }
/*    */     
/* 22 */     return super.canPlace(level, possibleState, pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\HangingSignItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
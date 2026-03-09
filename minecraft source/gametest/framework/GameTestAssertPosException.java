/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertPosException
/*    */   extends GameTestAssertException {
/*    */   private final BlockPos absolutePos;
/*    */   private final BlockPos relativePos;
/*    */   
/*    */   public GameTestAssertPosException(Component baseMessage, BlockPos absolutePos, BlockPos relativePos, int tick) {
/* 12 */     super(baseMessage, tick);
/* 13 */     this.absolutePos = absolutePos;
/* 14 */     this.relativePos = relativePos;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public Component getDescription() { return Component.translatable("test.error.position", new Object[] { this.message, Integer.valueOf(this.absolutePos.getX()), Integer.valueOf(this.absolutePos.getY()), Integer.valueOf(this.absolutePos.getZ()), Integer.valueOf(this.relativePos.getX()), Integer.valueOf(this.relativePos.getY()), Integer.valueOf(this.relativePos.getZ()), Integer.valueOf(this.tick) }); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public Component getMessageToShowAtBlock() { return this.message; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public BlockPos getRelativePos() { return this.relativePos; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public BlockPos getAbsolutePos() { return this.absolutePos; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestAssertPosException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
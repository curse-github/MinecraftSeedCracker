/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PositionalRandomFactory
/*    */ {
/* 20 */   default RandomSource at(BlockPos pos) { return at(pos.getX(), pos.getY(), pos.getZ()); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   default RandomSource fromHashOf(Identifier name) { return fromHashOf(name.toString()); }
/*    */   
/*    */   RandomSource fromHashOf(String paramString);
/*    */   
/*    */   RandomSource fromSeed(long paramLong);
/*    */   
/*    */   RandomSource at(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   @VisibleForTesting
/*    */   void parityConfigString(StringBuilder paramStringBuilder);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\PositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
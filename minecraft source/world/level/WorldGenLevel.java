/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ public interface WorldGenLevel
/*    */   extends ServerLevelAccessor
/*    */ {
/*    */   long getSeed();
/*    */   
/* 12 */   default boolean ensureCanWrite(BlockPos pos) { return true; }
/*    */   
/*    */   default void setCurrentlyGenerating(Supplier<String> currentlyGenerating) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\WorldGenLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
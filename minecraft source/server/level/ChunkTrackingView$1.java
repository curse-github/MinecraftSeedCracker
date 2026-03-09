/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ChunkTrackingView
/*    */ {
/* 12 */   public boolean contains(int chunkX, int chunkZ, boolean includeNeighbors) { return false; }
/*    */   
/*    */   public void forEach(Consumer<ChunkPos> consumer) {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkTrackingView$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
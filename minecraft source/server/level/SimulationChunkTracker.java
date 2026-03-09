/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.TicketStorage;
/*    */ 
/*    */ public class SimulationChunkTracker
/*    */   extends ChunkTracker
/*    */ {
/*    */   public static final int MAX_LEVEL = 33;
/* 12 */   protected final Long2ByteMap chunks = new Long2ByteOpenHashMap();
/*    */   
/*    */   private final TicketStorage ticketStorage;
/*    */   
/*    */   public SimulationChunkTracker(TicketStorage ticketStorage) {
/* 17 */     super(34, 16, 256);
/* 18 */     this.ticketStorage = ticketStorage;
/* 19 */     ticketStorage.setSimulationChunkUpdatedListener(this::update);
/* 20 */     this.chunks.defaultReturnValue((byte)33);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected int getLevelFromSource(long to) { return this.ticketStorage.getTicketLevelAt(to, true); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public int getLevel(ChunkPos node) { return getLevel(node.toLong()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected int getLevel(long node) { return this.chunks.get(node); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setLevel(long node, int level) {
/* 39 */     if (level >= 33) {
/* 40 */       this.chunks.remove(node);
/*    */     } else {
/* 42 */       this.chunks.put(node, (byte)level);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 47 */   public void runAllUpdates() { runUpdates(2147483647); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\SimulationChunkTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
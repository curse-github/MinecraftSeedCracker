/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PoiSynchronizer
/*     */   extends TrackingDebugSynchronizer<DebugPoiInfo>
/*     */ {
/* 242 */   public PoiSynchronizer() { super(DebugSubscriptions.POIS); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 247 */     ServerLevel level = player.level();
/* 248 */     PoiManager poiManager = level.getPoiManager();
/* 249 */     poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> 
/* 250 */         player.connection.send(new ClientboundDebugBlockValuePacket(record
/* 251 */             .getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 257 */   public void onPoiAdded(ServerLevel level, PoiRecord record) { sendToPlayersTrackingChunk(level, new ChunkPos(record.getPos()), new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))); }
/*     */ 
/*     */ 
/*     */   
/* 261 */   public void onPoiRemoved(ServerLevel level, BlockPos poiPos) { sendToPlayersTrackingChunk(level, new ChunkPos(poiPos), new ClientboundDebugBlockValuePacket(poiPos, this.subscription.emptyUpdate())); }
/*     */ 
/*     */ 
/*     */   
/* 265 */   public void onPoiTicketCountChanged(ServerLevel level, BlockPos poiPos) { sendToPlayersTrackingChunk(level, new ChunkPos(poiPos), new ClientboundDebugBlockValuePacket(poiPos, this.subscription.packUpdate(level.getPoiManager().getDebugPoiInfo(poiPos)))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$PoiSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
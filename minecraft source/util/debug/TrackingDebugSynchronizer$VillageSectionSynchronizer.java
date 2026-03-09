/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
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
/*     */ public class VillageSectionSynchronizer
/*     */   extends TrackingDebugSynchronizer<Unit>
/*     */ {
/* 271 */   public VillageSectionSynchronizer() { super(DebugSubscriptions.VILLAGE_SECTIONS); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 276 */     ServerLevel level = player.level();
/* 277 */     PoiManager poiManager = level.getPoiManager();
/* 278 */     poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> {
/* 279 */           SectionPos centerSection = SectionPos.of(record.getPos());
/* 280 */           forEachVillageSectionUpdate(level, centerSection, ());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public void onPoiAdded(ServerLevel level, PoiRecord record) { sendVillageSectionsPacket(level, record.getPos()); }
/*     */ 
/*     */ 
/*     */   
/* 294 */   public void onPoiRemoved(ServerLevel level, BlockPos poiPos) { sendVillageSectionsPacket(level, poiPos); }
/*     */ 
/*     */   
/*     */   private void sendVillageSectionsPacket(ServerLevel level, BlockPos poiPos) {
/* 298 */     forEachVillageSectionUpdate(level, SectionPos.of(poiPos), (sectionPos, isVillage) -> {
/* 299 */           BlockPos sectionBlockPos = sectionPos.center();
/* 300 */           if (isVillage.booleanValue()) {
/* 301 */             sendToPlayersTrackingChunk(level, new ChunkPos(sectionBlockPos), new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */           } else {
/* 303 */             sendToPlayersTrackingChunk(level, new ChunkPos(sectionBlockPos), new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.emptyUpdate()));
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void forEachVillageSectionUpdate(ServerLevel level, SectionPos centerSection, BiConsumer<SectionPos, Boolean> consumer) {
/* 309 */     for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 310 */       for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 311 */         for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 312 */           SectionPos sectionPos = centerSection.offset(offsetX, offsetY, offsetZ);
/* 313 */           if (level.isVillage(sectionPos.center())) {
/* 314 */             consumer.accept(sectionPos, Boolean.valueOf(true));
/*     */           } else {
/* 316 */             consumer.accept(sectionPos, Boolean.valueOf(false));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$VillageSectionSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
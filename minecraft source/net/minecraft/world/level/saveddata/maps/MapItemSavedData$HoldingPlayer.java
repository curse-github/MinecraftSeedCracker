/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoldingPlayer
/*     */ {
/*     */   public final Player player;
/*     */   private boolean dirtyData;
/*     */   private int minDirtyX;
/*     */   private int minDirtyY;
/*     */   private int maxDirtyX;
/*     */   private int maxDirtyY;
/*     */   private boolean dirtyDecorations;
/*     */   private int tick;
/*     */   public int step;
/*     */   
/*     */   private HoldingPlayer(Player player) {
/* 108 */     this.dirtyData = true;
/*     */ 
/*     */     
/* 111 */     this.maxDirtyX = 127;
/* 112 */     this.maxDirtyY = 127;
/* 113 */     this.dirtyDecorations = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     this.player = player;
/*     */   }
/*     */   
/*     */   private MapItemSavedData.MapPatch createPatch() {
/* 122 */     int startX = this.minDirtyX;
/* 123 */     int startY = this.minDirtyY;
/* 124 */     int width = this.maxDirtyX + 1 - this.minDirtyX;
/* 125 */     int height = this.maxDirtyY + 1 - this.minDirtyY;
/*     */     
/* 127 */     byte[] patch = new byte[width * height];
/* 128 */     for (int x = 0; x < width; x++) {
/* 129 */       for (int y = 0; y < height; y++) {
/* 130 */         patch[x + y * width] = MapItemSavedData.this.colors[startX + x + (startY + y) * 128];
/*     */       }
/*     */     } 
/* 133 */     return new MapItemSavedData.MapPatch(startX, startY, width, height, patch);
/*     */   }
/*     */   private Packet<?> nextUpdatePacket(MapId id) {
/*     */     Collection<MapDecoration> decorations;
/*     */     MapItemSavedData.MapPatch patch;
/* 138 */     if (this.dirtyData) {
/* 139 */       this.dirtyData = false;
/* 140 */       patch = createPatch();
/*     */     } else {
/* 142 */       patch = null;
/*     */     } 
/*     */ 
/*     */     
/* 146 */     if (this.dirtyDecorations && this.tick++ % 5 == 0) {
/* 147 */       this.dirtyDecorations = false;
/* 148 */       decorations = MapItemSavedData.this.decorations.values();
/*     */     } else {
/* 150 */       decorations = null;
/*     */     } 
/*     */     
/* 153 */     if (decorations != null || patch != null) {
/* 154 */       return new ClientboundMapItemDataPacket(id, MapItemSavedData.this.scale, MapItemSavedData.this.locked, decorations, patch);
/*     */     }
/*     */     
/* 157 */     return null;
/*     */   }
/*     */   
/*     */   private void markColorsDirty(int x, int y) {
/* 161 */     if (this.dirtyData) {
/* 162 */       this.minDirtyX = Math.min(this.minDirtyX, x);
/* 163 */       this.minDirtyY = Math.min(this.minDirtyY, y);
/* 164 */       this.maxDirtyX = Math.max(this.maxDirtyX, x);
/* 165 */       this.maxDirtyY = Math.max(this.maxDirtyY, y);
/*     */     } else {
/* 167 */       this.dirtyData = true;
/* 168 */       this.minDirtyX = x;
/* 169 */       this.minDirtyY = y;
/* 170 */       this.maxDirtyX = x;
/* 171 */       this.maxDirtyY = y;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 176 */   private void markDecorationsDirty() { this.dirtyDecorations = true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData$HoldingPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
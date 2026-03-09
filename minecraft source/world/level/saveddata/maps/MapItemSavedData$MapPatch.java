/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MapPatch
/*     */   extends Record
/*     */ {
/*     */   private final int startX;
/*     */   private final int startY;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final byte[] mapColors;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #68	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #68	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #68	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  68 */   public MapPatch(int startX, int startY, int width, int height, byte[] mapColors) { this.startX = startX; this.startY = startY; this.width = width; this.height = height; this.mapColors = mapColors; } public int startX() { return this.startX; } public int startY() { return this.startY; } public int width() { return this.width; } public int height() { return this.height; } public byte[] mapColors() { return this.mapColors; }
/*  69 */   public static final StreamCodec<ByteBuf, Optional<MapPatch>> STREAM_CODEC = StreamCodec.of(MapPatch::write, MapPatch::read);
/*     */   
/*     */   private static void write(ByteBuf output, Optional<MapPatch> optional) {
/*  72 */     if (optional.isPresent()) {
/*  73 */       MapPatch patch = (MapPatch)optional.get();
/*  74 */       output.writeByte(patch.width);
/*  75 */       output.writeByte(patch.height);
/*  76 */       output.writeByte(patch.startX);
/*  77 */       output.writeByte(patch.startY);
/*  78 */       FriendlyByteBuf.writeByteArray(output, patch.mapColors);
/*     */     } else {
/*  80 */       output.writeByte(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<MapPatch> read(ByteBuf input) {
/*  85 */     int width = input.readUnsignedByte();
/*  86 */     if (width > 0) {
/*  87 */       int height = input.readUnsignedByte();
/*  88 */       int startX = input.readUnsignedByte();
/*  89 */       int startY = input.readUnsignedByte();
/*  90 */       byte[] mapColors = FriendlyByteBuf.readByteArray(input);
/*  91 */       return Optional.of(new MapPatch(startX, startY, width, height, mapColors));
/*     */     } 
/*  93 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyToMap(MapItemSavedData map) {
/*  98 */     for (int x = 0; x < this.width; x++) {
/*  99 */       for (int y = 0; y < this.height; y++)
/* 100 */         map.setColor(this.startX + x, this.startY + y, this.mapColors[x + y * this.width]); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData$MapPatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
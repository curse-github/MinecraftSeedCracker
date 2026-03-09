/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ public final class MapFrame extends Record {
/*    */   private final BlockPos pos;
/*    */   private final int rotation;
/*    */   private final int entityId;
/*    */   
/*  7 */   public MapFrame(BlockPos pos, int rotation, int entityId) { this.pos = pos; this.rotation = rotation; this.entityId = entityId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public int rotation() { return this.rotation; } public int entityId() { return this.entityId; }
/*  8 */   public static final Codec<MapFrame> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPos.CODEC
/*  9 */         .fieldOf("pos").forGetter(MapFrame::pos), Codec.INT
/* 10 */         .fieldOf("rotation").forGetter(MapFrame::rotation), Codec.INT
/* 11 */         .fieldOf("entity_id").forGetter(MapFrame::entityId))
/* 12 */       .apply(i, MapFrame::new));
/*    */ 
/*    */   
/* 15 */   public String getId() { return frameId(this.pos); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static String frameId(BlockPos pos) { return "frame-" + pos.getX() + "," + pos.getY() + "," + pos.getZ(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
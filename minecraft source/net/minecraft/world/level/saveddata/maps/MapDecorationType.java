/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class MapDecorationType extends Record {
/*    */   private final Identifier assetId;
/*    */   private final boolean showOnItemFrame;
/*    */   private final int mapColor;
/*    */   
/* 12 */   public MapDecorationType(Identifier assetId, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) { this.assetId = assetId; this.showOnItemFrame = showOnItemFrame; this.mapColor = mapColor; this.explorationMapElement = explorationMapElement; this.trackCount = trackCount; } private final boolean explorationMapElement; private final boolean trackCount; public static final int NO_MAP_COLOR = -1; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Identifier assetId() { return this.assetId; } public boolean showOnItemFrame() { return this.showOnItemFrame; } public int mapColor() { return this.mapColor; } public boolean explorationMapElement() { return this.explorationMapElement; } public boolean trackCount() { return this.trackCount; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final Codec<Holder<MapDecorationType>> CODEC = BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec();
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MapDecorationType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MAP_DECORATION_TYPE);
/*    */ 
/*    */   
/* 25 */   public boolean hasMapColor() { return (this.mapColor != -1); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapDecorationType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
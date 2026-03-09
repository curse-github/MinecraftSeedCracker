/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class MapItemColor extends Record {
/*    */   private final int rgb;
/*    */   
/*  8 */   public MapItemColor(int rgb) { this.rgb = rgb; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/MapItemColor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapItemColor; } public int rgb() { return this.rgb; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/MapItemColor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapItemColor; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/MapItemColor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/MapItemColor;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final Codec<MapItemColor> CODEC = Codec.INT.xmap(MapItemColor::new, MapItemColor::rgb);
/* 10 */   public static final StreamCodec<ByteBuf, MapItemColor> STREAM_CODEC = ByteBufCodecs.INT.map(MapItemColor::new, MapItemColor::rgb);
/*    */   
/* 12 */   public static final MapItemColor DEFAULT = new MapItemColor(4603950);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\MapItemColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
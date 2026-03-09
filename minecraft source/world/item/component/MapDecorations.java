/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*    */ 
/*    */ public final class MapDecorations extends Record {
/*    */   private final Map<String, Entry> decorations;
/*    */   
/* 11 */   public MapDecorations(Map<String, Entry> decorations) { this.decorations = decorations; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/MapDecorations;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations; } public Map<String, Entry> decorations() { return this.decorations; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/MapDecorations;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/MapDecorations;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/MapDecorations;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 15 */   public static final MapDecorations EMPTY = new MapDecorations(Map.of());
/*    */   
/* 17 */   public static final Codec<MapDecorations> CODEC = Codec.unboundedMap(Codec.STRING, Entry.CODEC).xmap(MapDecorations::new, MapDecorations::decorations);
/*    */ 
/*    */   
/* 20 */   public MapDecorations withDecoration(String id, Entry entry) { return new MapDecorations(Util.copyAndPut(this.decorations, id, entry)); }
/*    */   public static final class Entry extends Record { private final Holder<MapDecorationType> type; private final double x; private final double z; private final float rotation;
/*    */     
/* 23 */     public Entry(Holder<MapDecorationType> type, double x, double z, float rotation) { this.type = type; this.x = x; this.z = z; this.rotation = rotation; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/MapDecorations$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 23 */       //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry; } public Holder<MapDecorationType> type() { return this.type; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/MapDecorations$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/MapDecorations$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/component/MapDecorations$Entry;
/* 23 */       //   0	8	1	o	Ljava/lang/Object; } public double x() { return this.x; } public double z() { return this.z; } public float rotation() { return this.rotation; }
/* 24 */     public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(MapDecorationType.CODEC
/* 25 */           .fieldOf("type").forGetter(Entry::type), Codec.DOUBLE
/* 26 */           .fieldOf("x").forGetter(Entry::x), Codec.DOUBLE
/* 27 */           .fieldOf("z").forGetter(Entry::z), Codec.FLOAT
/* 28 */           .fieldOf("rotation").forGetter(Entry::rotation))
/* 29 */         .apply(i, Entry::new)); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\MapDecorations.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
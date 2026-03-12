/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public final class CustomModelData extends Record {
/*    */   private final List<Float> floats;
/*    */   private final List<Boolean> flags;
/*    */   private final List<String> strings;
/*    */   private final List<Integer> colors;
/*    */   
/* 13 */   public CustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Integer> colors) { this.floats = floats; this.flags = flags; this.strings = strings; this.colors = colors; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/CustomModelData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/item/component/CustomModelData; } public List<Float> floats() { return this.floats; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/CustomModelData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/CustomModelData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/CustomModelData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/CustomModelData;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public List<Boolean> flags() { return this.flags; } public List<String> strings() { return this.strings; } public List<Integer> colors() { return this.colors; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final CustomModelData EMPTY = new CustomModelData(
/* 20 */       List.of(), 
/* 21 */       List.of(), 
/* 22 */       List.of(), 
/* 23 */       List.of());
/*    */ 
/*    */   
/* 26 */   public static final Codec<CustomModelData> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.FLOAT
/* 27 */         .listOf().optionalFieldOf("floats", List.of()).forGetter(CustomModelData::floats), Codec.BOOL
/* 28 */         .listOf().optionalFieldOf("flags", List.of()).forGetter(CustomModelData::flags), Codec.STRING
/* 29 */         .listOf().optionalFieldOf("strings", List.of()).forGetter(CustomModelData::strings), ExtraCodecs.RGB_COLOR_CODEC
/* 30 */         .listOf().optionalFieldOf("colors", List.of()).forGetter(CustomModelData::colors))
/* 31 */       .apply(i, CustomModelData::new));
/*    */   
/* 33 */   public static final StreamCodec<ByteBuf, CustomModelData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT
/* 34 */       .apply(ByteBufCodecs.list()), CustomModelData::floats, ByteBufCodecs.BOOL
/* 35 */       .apply(ByteBufCodecs.list()), CustomModelData::flags, ByteBufCodecs.STRING_UTF8
/* 36 */       .apply(ByteBufCodecs.list()), CustomModelData::strings, ByteBufCodecs.INT
/* 37 */       .apply(ByteBufCodecs.list()), CustomModelData::colors, CustomModelData::new);
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> T getSafe(List<T> values, int index) {
/* 42 */     if (index < 0 || index >= values.size()) {
/* 43 */       return null;
/*    */     }
/*    */     
/* 46 */     return (T)values.get(index);
/*    */   }
/*    */ 
/*    */   
/* 50 */   public Float getFloat(int index) { return (Float)getSafe(this.floats, index); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Boolean getBoolean(int index) { return (Boolean)getSafe(this.flags, index); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public String getString(int index) { return (String)getSafe(this.strings, index); }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Integer getColor(int index) { return (Integer)getSafe(this.colors, index); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\CustomModelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
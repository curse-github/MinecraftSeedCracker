/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TypeSettings
/*    */   extends Record
/*    */ {
/*    */   private final boolean open;
/*    */   private final boolean filtering;
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/stats/RecipeBookSettings$TypeSettings; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 33 */   public TypeSettings(boolean open, boolean filtering) { this.open = open; this.filtering = filtering; } public boolean open() { return this.open; } public boolean filtering() { return this.filtering; }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final TypeSettings DEFAULT = new TypeSettings(false, false);
/*    */   
/* 39 */   public static final MapCodec<TypeSettings> CRAFTING_MAP_CODEC = codec("isGuiOpen", "isFilteringCraftable");
/* 40 */   public static final MapCodec<TypeSettings> FURNACE_MAP_CODEC = codec("isFurnaceGuiOpen", "isFurnaceFilteringCraftable");
/* 41 */   public static final MapCodec<TypeSettings> BLAST_FURNACE_MAP_CODEC = codec("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable");
/* 42 */   public static final MapCodec<TypeSettings> SMOKER_MAP_CODEC = codec("isSmokerGuiOpen", "isSmokerFilteringCraftable");
/*    */   
/* 44 */   public static final StreamCodec<ByteBuf, TypeSettings> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, TypeSettings::open, ByteBufCodecs.BOOL, TypeSettings::filtering, TypeSettings::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public String toString() { return "[open=" + this.open + ", filtering=" + this.filtering + "]"; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public TypeSettings setOpen(boolean open) { return new TypeSettings(open, this.filtering); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public TypeSettings setFiltering(boolean filtering) { return new TypeSettings(this.open, filtering); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   private static MapCodec<TypeSettings> codec(String openFieldName, String filteringFieldName) { return RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 65 */           .optionalFieldOf(openFieldName, Boolean.valueOf(false)).forGetter(TypeSettings::open), Codec.BOOL
/* 66 */           .optionalFieldOf(filteringFieldName, Boolean.valueOf(false)).forGetter(TypeSettings::filtering))
/* 67 */         .apply(i, TypeSettings::new)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\RecipeBookSettings$TypeSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
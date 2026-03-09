/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.inventory.RecipeBookType;
/*     */ 
/*     */ public final class RecipeBookSettings {
/*  18 */   public static final StreamCodec<FriendlyByteBuf, RecipeBookSettings> STREAM_CODEC = StreamCodec.composite(TypeSettings.STREAM_CODEC, o -> 
/*  19 */       o.crafting, TypeSettings.STREAM_CODEC, o -> 
/*  20 */       o.furnace, TypeSettings.STREAM_CODEC, o -> 
/*  21 */       o.blastFurnace, TypeSettings.STREAM_CODEC, o -> 
/*  22 */       o.smoker, RecipeBookSettings::new);
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static final MapCodec<RecipeBookSettings> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TypeSettings.CRAFTING_MAP_CODEC
/*  27 */         .forGetter(()), TypeSettings.FURNACE_MAP_CODEC
/*  28 */         .forGetter(()), TypeSettings.BLAST_FURNACE_MAP_CODEC
/*  29 */         .forGetter(()), TypeSettings.SMOKER_MAP_CODEC
/*  30 */         .forGetter(()))
/*  31 */       .apply(i, RecipeBookSettings::new)); private TypeSettings crafting; private TypeSettings furnace; private TypeSettings blastFurnace; private TypeSettings smoker;
/*     */   public static final class TypeSettings extends Record { private final boolean open; private final boolean filtering;
/*  33 */     public TypeSettings(boolean open, boolean filtering) { this.open = open; this.filtering = filtering; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/stats/RecipeBookSettings$TypeSettings; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/stats/RecipeBookSettings$TypeSettings;
/*  33 */       //   0	8	1	o	Ljava/lang/Object; } public boolean open() { return this.open; } public boolean filtering() { return this.filtering; }
/*     */ 
/*     */ 
/*     */     
/*  37 */     public static final TypeSettings DEFAULT = new TypeSettings(false, false);
/*     */     
/*  39 */     public static final MapCodec<TypeSettings> CRAFTING_MAP_CODEC = codec("isGuiOpen", "isFilteringCraftable");
/*  40 */     public static final MapCodec<TypeSettings> FURNACE_MAP_CODEC = codec("isFurnaceGuiOpen", "isFurnaceFilteringCraftable");
/*  41 */     public static final MapCodec<TypeSettings> BLAST_FURNACE_MAP_CODEC = codec("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable");
/*  42 */     public static final MapCodec<TypeSettings> SMOKER_MAP_CODEC = codec("isSmokerGuiOpen", "isSmokerFilteringCraftable");
/*     */     
/*  44 */     public static final StreamCodec<ByteBuf, TypeSettings> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, TypeSettings::open, ByteBufCodecs.BOOL, TypeSettings::filtering, TypeSettings::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     public String toString() { return "[open=" + this.open + ", filtering=" + this.filtering + "]"; }
/*     */ 
/*     */ 
/*     */     
/*  56 */     public TypeSettings setOpen(boolean open) { return new TypeSettings(open, this.filtering); }
/*     */ 
/*     */ 
/*     */     
/*  60 */     public TypeSettings setFiltering(boolean filtering) { return new TypeSettings(this.open, filtering); }
/*     */ 
/*     */ 
/*     */     
/*  64 */     private static MapCodec<TypeSettings> codec(String openFieldName, String filteringFieldName) { return RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/*  65 */             .optionalFieldOf(openFieldName, Boolean.valueOf(false)).forGetter(TypeSettings::open), Codec.BOOL
/*  66 */             .optionalFieldOf(filteringFieldName, Boolean.valueOf(false)).forGetter(TypeSettings::filtering))
/*  67 */           .apply(i, TypeSettings::new)); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public RecipeBookSettings() { this(TypeSettings.DEFAULT, TypeSettings.DEFAULT, TypeSettings.DEFAULT, TypeSettings.DEFAULT); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RecipeBookSettings(TypeSettings crafting, TypeSettings furnace, TypeSettings blastFurnace, TypeSettings smoker) {
/*  86 */     this.crafting = crafting;
/*  87 */     this.furnace = furnace;
/*  88 */     this.blastFurnace = blastFurnace;
/*  89 */     this.smoker = smoker;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public TypeSettings getSettings(RecipeBookType type) {
/*  94 */     switch (type) { default: throw new MatchException(null, null);case CRAFTING: case FURNACE: case BLAST_FURNACE: case SMOKER: break; }  return 
/*     */ 
/*     */ 
/*     */       
/*  98 */       this.smoker;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateSettings(RecipeBookType recipeBookType, UnaryOperator<TypeSettings> operator) {
/* 103 */     switch (recipeBookType) { case CRAFTING:
/* 104 */         this.crafting = (TypeSettings)operator.apply(this.crafting); break;
/* 105 */       case FURNACE: this.furnace = (TypeSettings)operator.apply(this.furnace); break;
/* 106 */       case BLAST_FURNACE: this.blastFurnace = (TypeSettings)operator.apply(this.blastFurnace); break;
/* 107 */       case SMOKER: this.smoker = (TypeSettings)operator.apply(this.smoker);
/*     */         break; }
/*     */   
/*     */   }
/*     */   
/* 112 */   public boolean isOpen(RecipeBookType type) { return (getSettings(type)).open; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public void setOpen(RecipeBookType type, boolean open) { updateSettings(type, s -> s.setOpen(open)); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public boolean isFiltering(RecipeBookType type) { return (getSettings(type)).filtering; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public void setFiltering(RecipeBookType type, boolean filtering) { updateSettings(type, s -> s.setFiltering(filtering)); }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public RecipeBookSettings copy() { return new RecipeBookSettings(this.crafting, this.furnace, this.blastFurnace, this.smoker); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceFrom(RecipeBookSettings other) {
/* 137 */     this.crafting = other.crafting;
/* 138 */     this.furnace = other.furnace;
/* 139 */     this.blastFurnace = other.blastFurnace;
/* 140 */     this.smoker = other.smoker;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\RecipeBookSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
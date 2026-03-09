/*    */ package net.minecraft.world.item.crafting.display;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class SmithingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay template;
/*    */   private final SlotDisplay base;
/*    */   
/*  8 */   public SmithingRecipeDisplay(SlotDisplay template, SlotDisplay base, SlotDisplay addition, SlotDisplay result, SlotDisplay craftingStation) { this.template = template; this.base = base; this.addition = addition; this.result = result; this.craftingStation = craftingStation; } private final SlotDisplay addition; private final SlotDisplay result; private final SlotDisplay craftingStation; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay template() { return this.template; } public SlotDisplay base() { return this.base; } public SlotDisplay addition() { return this.addition; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final MapCodec<SmithingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 18 */         .fieldOf("template").forGetter(SmithingRecipeDisplay::template), SlotDisplay.CODEC
/* 19 */         .fieldOf("base").forGetter(SmithingRecipeDisplay::base), SlotDisplay.CODEC
/* 20 */         .fieldOf("addition").forGetter(SmithingRecipeDisplay::addition), SlotDisplay.CODEC
/* 21 */         .fieldOf("result").forGetter(SmithingRecipeDisplay::result), SlotDisplay.CODEC
/* 22 */         .fieldOf("crafting_station").forGetter(SmithingRecipeDisplay::craftingStation))
/* 23 */       .apply(i, SmithingRecipeDisplay::new));
/*    */   
/* 25 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::template, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::base, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::addition, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::craftingStation, SmithingRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final RecipeDisplay.Type<SmithingRecipeDisplay> TYPE = new RecipeDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */ 
/*    */   
/* 38 */   public RecipeDisplay.Type<SmithingRecipeDisplay> type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SmithingRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
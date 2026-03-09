/*    */ package net.minecraft.world.item.crafting.display;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class FurnaceRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay ingredient;
/*    */   private final SlotDisplay fuel;
/*    */   private final SlotDisplay result;
/*    */   
/* 11 */   public FurnaceRecipeDisplay(SlotDisplay ingredient, SlotDisplay fuel, SlotDisplay result, SlotDisplay craftingStation, int duration, float experience) { this.ingredient = ingredient; this.fuel = fuel; this.result = result; this.craftingStation = craftingStation; this.duration = duration; this.experience = experience; } private final SlotDisplay craftingStation; private final int duration; private final float experience; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay ingredient() { return this.ingredient; } public SlotDisplay fuel() { return this.fuel; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; } public int duration() { return this.duration; } public float experience() { return this.experience; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final MapCodec<FurnaceRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 20 */         .fieldOf("ingredient").forGetter(FurnaceRecipeDisplay::ingredient), SlotDisplay.CODEC
/* 21 */         .fieldOf("fuel").forGetter(FurnaceRecipeDisplay::fuel), SlotDisplay.CODEC
/* 22 */         .fieldOf("result").forGetter(FurnaceRecipeDisplay::result), SlotDisplay.CODEC
/* 23 */         .fieldOf("crafting_station").forGetter(FurnaceRecipeDisplay::craftingStation), Codec.INT
/* 24 */         .fieldOf("duration").forGetter(FurnaceRecipeDisplay::duration), Codec.FLOAT
/* 25 */         .fieldOf("experience").forGetter(FurnaceRecipeDisplay::experience))
/* 26 */       .apply(i, FurnaceRecipeDisplay::new));
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, FurnaceRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::ingredient, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::fuel, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::result, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::craftingStation, ByteBufCodecs.VAR_INT, FurnaceRecipeDisplay::duration, ByteBufCodecs.FLOAT, FurnaceRecipeDisplay::experience, FurnaceRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static final RecipeDisplay.Type<FurnaceRecipeDisplay> TYPE = new RecipeDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */ 
/*    */   
/* 42 */   public RecipeDisplay.Type<FurnaceRecipeDisplay> type() { return TYPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean isEnabled(FeatureFlagSet enabledFeatures) { return (this.ingredient.isEnabled(enabledFeatures) && fuel().isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\FurnaceRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.crafting.display;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class ShapedCraftingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final int width;
/*    */   private final int height;
/*    */   private final List<SlotDisplay> ingredients;
/*    */   private final SlotDisplay result;
/*    */   
/* 13 */   public int width() { return this.width; } private final SlotDisplay craftingStation; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public List<SlotDisplay> ingredients() { return this.ingredients; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final MapCodec<ShapedCraftingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 21 */         .fieldOf("width").forGetter(ShapedCraftingRecipeDisplay::width), Codec.INT
/* 22 */         .fieldOf("height").forGetter(ShapedCraftingRecipeDisplay::height), SlotDisplay.CODEC
/* 23 */         .listOf().fieldOf("ingredients").forGetter(ShapedCraftingRecipeDisplay::ingredients), SlotDisplay.CODEC
/* 24 */         .fieldOf("result").forGetter(ShapedCraftingRecipeDisplay::result), SlotDisplay.CODEC
/* 25 */         .fieldOf("crafting_station").forGetter(ShapedCraftingRecipeDisplay::craftingStation))
/* 26 */       .apply(i, ShapedCraftingRecipeDisplay::new));
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedCraftingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ShapedCraftingRecipeDisplay::width, ByteBufCodecs.VAR_INT, ShapedCraftingRecipeDisplay::height, SlotDisplay.STREAM_CODEC
/*    */ 
/*    */       
/* 31 */       .apply(ByteBufCodecs.list()), ShapedCraftingRecipeDisplay::ingredients, SlotDisplay.STREAM_CODEC, ShapedCraftingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, ShapedCraftingRecipeDisplay::craftingStation, ShapedCraftingRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final RecipeDisplay.Type<ShapedCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public ShapedCraftingRecipeDisplay(int width, int height, List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) {
/* 40 */     if (ingredients.size() != width * height)
/* 41 */       throw new IllegalArgumentException("Invalid shaped recipe display contents"); 
/*    */     this.width = width;
/*    */     this.height = height;
/*    */     this.ingredients = ingredients;
/*    */     this.result = result;
/*    */     this.craftingStation = craftingStation;
/* 47 */   } public RecipeDisplay.Type<ShapedCraftingRecipeDisplay> type() { return TYPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean isEnabled(FeatureFlagSet enabledFeatures) { return (this.ingredients.stream().allMatch(e -> e.isEnabled(enabledFeatures)) && super.isEnabled(enabledFeatures)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\ShapedCraftingRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
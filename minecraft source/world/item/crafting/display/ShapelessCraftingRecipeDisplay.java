/*    */ package net.minecraft.world.item.crafting.display;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class ShapelessCraftingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final List<SlotDisplay> ingredients;
/*    */   private final SlotDisplay result;
/*    */   private final SlotDisplay craftingStation;
/*    */   
/* 12 */   public ShapelessCraftingRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) { this.ingredients = ingredients; this.result = result; this.craftingStation = craftingStation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay; } public List<SlotDisplay> ingredients() { return this.ingredients; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final MapCodec<ShapelessCraftingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 18 */         .listOf().fieldOf("ingredients").forGetter(ShapelessCraftingRecipeDisplay::ingredients), SlotDisplay.CODEC
/* 19 */         .fieldOf("result").forGetter(ShapelessCraftingRecipeDisplay::result), SlotDisplay.CODEC
/* 20 */         .fieldOf("crafting_station").forGetter(ShapelessCraftingRecipeDisplay::craftingStation))
/* 21 */       .apply(i, ShapelessCraftingRecipeDisplay::new));
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessCraftingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC
/* 24 */       .apply(ByteBufCodecs.list()), ShapelessCraftingRecipeDisplay::ingredients, SlotDisplay.STREAM_CODEC, ShapelessCraftingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, ShapelessCraftingRecipeDisplay::craftingStation, ShapelessCraftingRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final RecipeDisplay.Type<ShapelessCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */ 
/*    */   
/* 34 */   public RecipeDisplay.Type<ShapelessCraftingRecipeDisplay> type() { return TYPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean isEnabled(FeatureFlagSet enabledFeatures) { return (this.ingredients.stream().allMatch(e -> e.isEnabled(enabledFeatures)) && super.isEnabled(enabledFeatures)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\ShapelessCraftingRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
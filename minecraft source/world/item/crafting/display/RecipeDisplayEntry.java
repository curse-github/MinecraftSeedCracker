/*    */ package net.minecraft.world.item.crafting.display;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.context.ContextMap;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*    */ 
/*    */ public final class RecipeDisplayEntry extends Record {
/*    */   private final RecipeDisplayId id;
/*    */   private final RecipeDisplay display;
/*    */   private final OptionalInt group;
/*    */   private final RecipeBookCategory category;
/*    */   private final Optional<List<Ingredient>> craftingRequirements;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry; }
/*    */   
/* 24 */   public RecipeDisplayEntry(RecipeDisplayId id, RecipeDisplay display, OptionalInt group, RecipeBookCategory category, Optional<List<Ingredient>> craftingRequirements) { this.id = id; this.display = display; this.group = group; this.category = category; this.craftingRequirements = craftingRequirements; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayEntry;
/* 24 */     //   0	8	1	o	Ljava/lang/Object; } public RecipeDisplayId id() { return this.id; } public RecipeDisplay display() { return this.display; } public OptionalInt group() { return this.group; } public RecipeBookCategory category() { return this.category; } public Optional<List<Ingredient>> craftingRequirements() { return this.craftingRequirements; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDisplayEntry> STREAM_CODEC = StreamCodec.composite(RecipeDisplayId.STREAM_CODEC, RecipeDisplayEntry::id, RecipeDisplay.STREAM_CODEC, RecipeDisplayEntry::display, ByteBufCodecs.OPTIONAL_VAR_INT, RecipeDisplayEntry::group, 
/*    */ 
/*    */ 
/*    */       
/* 35 */       ByteBufCodecs.registry(Registries.RECIPE_BOOK_CATEGORY), RecipeDisplayEntry::category, Ingredient.CONTENTS_STREAM_CODEC
/* 36 */       .apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), RecipeDisplayEntry::craftingRequirements, RecipeDisplayEntry::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public List<ItemStack> resultItems(ContextMap context) { return this.display.result().resolveForStacks(context); }
/*    */ 
/*    */   
/*    */   public boolean canCraft(StackedItemContents providedContents) {
/* 45 */     if (this.craftingRequirements.isEmpty()) {
/* 46 */       return false;
/*    */     }
/* 48 */     return providedContents.canCraft((List)this.craftingRequirements.get(), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\RecipeDisplayEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
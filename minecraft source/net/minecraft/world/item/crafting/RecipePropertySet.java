/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class RecipePropertySet
/*    */ {
/* 19 */   public static final ResourceKey<? extends Registry<RecipePropertySet>> TYPE_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("recipe_property_set"));
/*    */   
/* 21 */   public static final ResourceKey<RecipePropertySet> SMITHING_BASE = registerVanilla("smithing_base");
/* 22 */   public static final ResourceKey<RecipePropertySet> SMITHING_TEMPLATE = registerVanilla("smithing_template");
/* 23 */   public static final ResourceKey<RecipePropertySet> SMITHING_ADDITION = registerVanilla("smithing_addition");
/* 24 */   public static final ResourceKey<RecipePropertySet> FURNACE_INPUT = registerVanilla("furnace_input");
/* 25 */   public static final ResourceKey<RecipePropertySet> BLAST_FURNACE_INPUT = registerVanilla("blast_furnace_input");
/* 26 */   public static final ResourceKey<RecipePropertySet> SMOKER_INPUT = registerVanilla("smoker_input");
/* 27 */   public static final ResourceKey<RecipePropertySet> CAMPFIRE_INPUT = registerVanilla("campfire_input");
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, RecipePropertySet> STREAM_CODEC = Item.STREAM_CODEC.apply(ByteBufCodecs.list())
/* 30 */     .map(holders -> 
/* 31 */       new RecipePropertySet(Set.copyOf(holders)), propertySet -> 
/* 32 */       List.copyOf(propertySet.items));
/*    */ 
/*    */   
/* 35 */   public static final RecipePropertySet EMPTY = new RecipePropertySet(Set.of());
/*    */   
/*    */   private final Set<Holder<Item>> items;
/*    */ 
/*    */   
/* 40 */   private RecipePropertySet(Set<Holder<Item>> items) { this.items = items; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   private static ResourceKey<RecipePropertySet> registerVanilla(String name) { return ResourceKey.create(TYPE_KEY, Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public boolean test(ItemStack itemStack) { return this.items.contains(itemStack.getItemHolder()); }
/*    */ 
/*    */   
/*    */   static RecipePropertySet create(Collection<Ingredient> ingredients) {
/* 52 */     Set<Holder<Item>> items = (Set)ingredients.stream().flatMap(Ingredient::items).collect(Collectors.toUnmodifiableSet());
/* 53 */     return new RecipePropertySet(items);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipePropertySet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*    */ 
/*    */ public abstract class AbstractCookingRecipe
/*    */   extends SingleItemRecipe {
/*    */   private final CookingBookCategory category;
/*    */   
/*    */   public AbstractCookingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
/* 23 */     super(group, ingredient, result);
/* 24 */     this.category = category;
/* 25 */     this.experience = experience;
/* 26 */     this.cookingTime = cookingTime;
/*    */   }
/*    */   
/*    */   private final float experience;
/*    */   private final int cookingTime;
/*    */   
/*    */   public abstract RecipeSerializer<? extends AbstractCookingRecipe> getSerializer();
/*    */   
/*    */   public abstract RecipeType<? extends AbstractCookingRecipe> getType();
/*    */   
/* 36 */   public float experience() { return this.experience; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int cookingTime() { return this.cookingTime; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public CookingBookCategory category() { return this.category; }
/*    */ 
/*    */   
/*    */   protected abstract Item furnaceIcon();
/*    */ 
/*    */   
/*    */   public List<RecipeDisplay> display() {
/* 51 */     return List.of(new FurnaceRecipeDisplay(
/* 52 */           input().display(), SlotDisplay.AnyFuel.INSTANCE, new SlotDisplay.ItemStackSlotDisplay(
/*    */             
/* 54 */             result()), new SlotDisplay.ItemSlotDisplay(
/* 55 */             furnaceIcon()), this.cookingTime, this.experience));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends AbstractCookingRecipe> {
/*    */     T create(String param1String, CookingBookCategory param1CookingBookCategory, Ingredient param1Ingredient, ItemStack param1ItemStack, float param1Float, int param1Int); }
/*    */   
/*    */   public static class Serializer<T extends AbstractCookingRecipe> extends Object implements RecipeSerializer<T> {
/*    */     private final MapCodec<T> codec;
/*    */     
/*    */     public Serializer(AbstractCookingRecipe.Factory<T> factory, int defaultCookingTime) {
/* 66 */       this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 73 */             Objects.requireNonNull(factory); return r.group(Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(AbstractCookingRecipe::category), Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result), Codec.FLOAT.fieldOf("experience").orElse(Float.valueOf(0.0F)).forGetter(AbstractCookingRecipe::experience), Codec.INT.fieldOf("cookingtime").orElse(Integer.valueOf(defaultCookingTime)).forGetter(AbstractCookingRecipe::cookingTime)).apply(r, factory::create);
/*    */           });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 82 */       Objects.requireNonNull(factory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, CookingBookCategory.STREAM_CODEC, AbstractCookingRecipe::category, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, ByteBufCodecs.FLOAT, AbstractCookingRecipe::experience, ByteBufCodecs.INT, AbstractCookingRecipe::cookingTime, factory::create);
/*    */     }
/*    */ 
/*    */     
/*    */     private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */     
/* 88 */     public MapCodec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 93 */     public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
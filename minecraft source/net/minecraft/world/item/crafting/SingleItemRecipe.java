/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class SingleItemRecipe extends Object implements Recipe<SingleRecipeInput> {
/*    */   private final Ingredient input;
/*    */   private final ItemStack result;
/*    */   private final String group;
/*    */   private PlacementInfo placementInfo;
/*    */   
/*    */   public SingleItemRecipe(String group, Ingredient input, ItemStack result) {
/* 22 */     this.group = group;
/* 23 */     this.input = input;
/* 24 */     this.result = result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean matches(SingleRecipeInput input, Level level) { return this.input.test(input.item()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public String group() { return this.group; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Ingredient input() { return this.input; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected ItemStack result() { return this.result; }
/*    */ 
/*    */ 
/*    */   
/*    */   public PlacementInfo placementInfo() {
/* 53 */     if (this.placementInfo == null) {
/* 54 */       this.placementInfo = PlacementInfo.create(this.input);
/*    */     }
/* 56 */     return this.placementInfo;
/*    */   }
/*    */   
/*    */   public abstract RecipeSerializer<? extends SingleItemRecipe> getSerializer();
/*    */   
/* 61 */   public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) { return this.result.copy(); }
/*    */   public abstract RecipeType<? extends SingleItemRecipe> getType();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends SingleItemRecipe> {
/*    */     T create(String param1String, Ingredient param1Ingredient, ItemStack param1ItemStack); }
/*    */   
/*    */   public static class Serializer<T extends SingleItemRecipe> extends Object implements RecipeSerializer<T> { protected Serializer(SingleItemRecipe.Factory<T> factory) {
/* 69 */       this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */ 
/*    */ 
/*    */             
/* 73 */             Objects.requireNonNull(factory); return r.group(Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result)).apply(r, factory::create);
/*    */           });
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 79 */       Objects.requireNonNull(factory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, factory::create);
/*    */     }
/*    */     
/*    */     private final MapCodec<T> codec;
/*    */     private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */     
/* 85 */     public MapCodec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 90 */     public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SingleItemRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
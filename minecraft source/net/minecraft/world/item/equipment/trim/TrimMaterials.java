/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*    */ 
/*    */ public class TrimMaterials
/*    */ {
/* 20 */   public static final ResourceKey<TrimMaterial> QUARTZ = registryKey("quartz");
/* 21 */   public static final ResourceKey<TrimMaterial> IRON = registryKey("iron");
/* 22 */   public static final ResourceKey<TrimMaterial> NETHERITE = registryKey("netherite");
/* 23 */   public static final ResourceKey<TrimMaterial> REDSTONE = registryKey("redstone");
/* 24 */   public static final ResourceKey<TrimMaterial> COPPER = registryKey("copper");
/* 25 */   public static final ResourceKey<TrimMaterial> GOLD = registryKey("gold");
/* 26 */   public static final ResourceKey<TrimMaterial> EMERALD = registryKey("emerald");
/* 27 */   public static final ResourceKey<TrimMaterial> DIAMOND = registryKey("diamond");
/* 28 */   public static final ResourceKey<TrimMaterial> LAPIS = registryKey("lapis");
/* 29 */   public static final ResourceKey<TrimMaterial> AMETHYST = registryKey("amethyst");
/* 30 */   public static final ResourceKey<TrimMaterial> RESIN = registryKey("resin");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<TrimMaterial> context) {
/* 33 */     register(context, QUARTZ, Style.EMPTY.withColor(14931140), MaterialAssetGroup.QUARTZ);
/* 34 */     register(context, IRON, Style.EMPTY.withColor(15527148), MaterialAssetGroup.IRON);
/* 35 */     register(context, NETHERITE, Style.EMPTY.withColor(6445145), MaterialAssetGroup.NETHERITE);
/* 36 */     register(context, REDSTONE, Style.EMPTY.withColor(9901575), MaterialAssetGroup.REDSTONE);
/* 37 */     register(context, COPPER, Style.EMPTY.withColor(11823181), MaterialAssetGroup.COPPER);
/* 38 */     register(context, GOLD, Style.EMPTY.withColor(14594349), MaterialAssetGroup.GOLD);
/* 39 */     register(context, EMERALD, Style.EMPTY.withColor(1155126), MaterialAssetGroup.EMERALD);
/* 40 */     register(context, DIAMOND, Style.EMPTY.withColor(7269586), MaterialAssetGroup.DIAMOND);
/* 41 */     register(context, LAPIS, Style.EMPTY.withColor(4288151), MaterialAssetGroup.LAPIS);
/* 42 */     register(context, AMETHYST, Style.EMPTY.withColor(10116294), MaterialAssetGroup.AMETHYST);
/* 43 */     register(context, RESIN, Style.EMPTY.withColor(16545810), MaterialAssetGroup.RESIN);
/*    */   }
/*    */   
/*    */   public static Optional<Holder<TrimMaterial>> getFromIngredient(HolderLookup.Provider registries, ItemStack stack) {
/* 47 */     ProvidesTrimMaterial material = (ProvidesTrimMaterial)stack.get(DataComponents.PROVIDES_TRIM_MATERIAL);
/* 48 */     return (material != null) ? material.unwrap(registries) : Optional.empty();
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> registryKey, Style hoverTextStyle, MaterialAssetGroup assets) {
/* 52 */     MutableComponent mutableComponent = Component.translatable(Util.makeDescriptionId("trim_material", registryKey.identifier())).withStyle(hoverTextStyle);
/* 53 */     context.register(registryKey, new TrimMaterial(assets, mutableComponent));
/*    */   }
/*    */ 
/*    */   
/* 57 */   private static ResourceKey<TrimMaterial> registryKey(String id) { return ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\trim\TrimMaterials.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
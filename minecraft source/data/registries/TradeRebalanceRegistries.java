/*    */ package net.minecraft.data.registries;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistrySetBuilder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.enchantment.providers.TradeRebalanceEnchantmentProviders;
/*    */ 
/*    */ public class TradeRebalanceRegistries
/*    */ {
/* 11 */   private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
/* 12 */     .add(Registries.ENCHANTMENT_PROVIDER, TradeRebalanceEnchantmentProviders::bootstrap);
/*    */ 
/*    */   
/* 15 */   public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanilla) { return RegistryPatchGenerator.createLookup(vanilla, BUILDER); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\registries\TradeRebalanceRegistries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
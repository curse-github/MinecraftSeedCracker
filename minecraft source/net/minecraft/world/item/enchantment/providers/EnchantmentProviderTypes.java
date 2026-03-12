/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public interface EnchantmentProviderTypes {
/*    */   static MapCodec<? extends EnchantmentProvider> bootstrap(Registry<MapCodec<? extends EnchantmentProvider>> registry) {
/*  8 */     Registry.register(registry, "by_cost", EnchantmentsByCost.CODEC);
/*  9 */     Registry.register(registry, "by_cost_with_difficulty", EnchantmentsByCostWithDifficulty.CODEC);
/* 10 */     return (MapCodec)Registry.register(registry, "single", SingleEnchantment.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentProviderTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
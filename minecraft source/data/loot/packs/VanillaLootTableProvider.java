/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.loot.LootTableProvider;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public class VanillaLootTableProvider
/*    */ {
/*    */   public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 15 */     return new LootTableProvider(output, 
/*    */         
/* 17 */         BuiltInLootTables.all(), 
/* 18 */         List.of(new LootTableProvider.SubProviderEntry[] { new LootTableProvider.SubProviderEntry(VanillaFishingLoot::new, LootContextParamSets.FISHING), new LootTableProvider.SubProviderEntry(VanillaChestLoot::new, LootContextParamSets.CHEST), new LootTableProvider.SubProviderEntry(VanillaEntityLoot::new, LootContextParamSets.ENTITY), new LootTableProvider.SubProviderEntry(VanillaEquipmentLoot::new, LootContextParamSets.EQUIPMENT), new LootTableProvider.SubProviderEntry(VanillaBlockLoot::new, LootContextParamSets.BLOCK), new LootTableProvider.SubProviderEntry(VanillaPiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER), new LootTableProvider.SubProviderEntry(VanillaGiftLoot::new, LootContextParamSets.GIFT), new LootTableProvider.SubProviderEntry(VanillaArchaeologyLoot::new, LootContextParamSets.ARCHAEOLOGY), new LootTableProvider.SubProviderEntry(VanillaShearingLoot::new, LootContextParamSets.SHEARING), new LootTableProvider.SubProviderEntry(VanillaEntityInteractLoot::new, LootContextParamSets.ENTITY_INTERACT), new LootTableProvider.SubProviderEntry(VanillaBlockInteractLoot::new, LootContextParamSets.BLOCK_INTERACT), new LootTableProvider.SubProviderEntry(VanillaChargedCreeperExplosionLoot::new, LootContextParamSets.ENTITY) }), registries);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaLootTableProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.data;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import joptsimple.AbstractOptionSpec;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.data.advancements.packs.VanillaAdvancementProvider;
/*     */ import net.minecraft.data.loot.packs.TradeRebalanceLootTableProvider;
/*     */ import net.minecraft.data.loot.packs.VanillaLootTableProvider;
/*     */ import net.minecraft.data.metadata.PackMetadataGenerator;
/*     */ import net.minecraft.data.registries.TradeRebalanceRegistries;
/*     */ import net.minecraft.data.registries.VanillaRegistries;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.data.structures.SnbtToNbt;
/*     */ import net.minecraft.data.structures.StructureUpdater;
/*     */ import net.minecraft.data.tags.TagsProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   @SuppressForbidden(reason = "System.out needed before bootstrap")
/*     */   public static void main(String[] args) throws IOException {
/*  68 */     SharedConstants.tryDetectVersion();
/*     */     
/*  70 */     OptionParser parser = new OptionParser();
/*  71 */     AbstractOptionSpec abstractOptionSpec = parser.accepts("help", "Show the help menu").forHelp();
/*  72 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("server", "Include server generators");
/*  73 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("dev", "Include development tools");
/*  74 */     OptionSpecBuilder optionSpecBuilder3 = parser.accepts("reports", "Include data reports");
/*  75 */     parser.accepts("validate", "Validate inputs");
/*  76 */     OptionSpecBuilder optionSpecBuilder4 = parser.accepts("all", "Include all generators");
/*  77 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", new String[0]);
/*  78 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("input", "Input folder").withRequiredArg();
/*  79 */     OptionSet optionSet = parser.parse(args);
/*     */     
/*  81 */     if (optionSet.has(abstractOptionSpec) || !optionSet.hasOptions()) {
/*  82 */       parser.printHelpOn(System.out);
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     Path output = Paths.get((String)argumentAcceptingOptionSpec1.value(optionSet), new String[0]);
/*  87 */     boolean allOptions = optionSet.has(optionSpecBuilder4);
/*  88 */     boolean server = (allOptions || optionSet.has(optionSpecBuilder1));
/*  89 */     boolean dev = (allOptions || optionSet.has(optionSpecBuilder2));
/*  90 */     boolean reports = (allOptions || optionSet.has(optionSpecBuilder3));
/*  91 */     Collection<Path> input = optionSet.valuesOf(argumentAcceptingOptionSpec2).stream().map(x$0 -> Paths.get(x$0, new String[0])).toList();
/*  92 */     DataGenerator generator = new DataGenerator(output, SharedConstants.getCurrentVersion(), true);
/*  93 */     addServerProviders(generator, input, server, dev, reports);
/*  94 */     generator.run();
/*  95 */     Util.shutdownExecutors();
/*     */   }
/*     */ 
/*     */   
/*  99 */   private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> target, CompletableFuture<HolderLookup.Provider> registries) { return output -> (DataProvider)target.apply(output, registries); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addServerProviders(DataGenerator generator, Collection<Path> input, boolean server, boolean dev, boolean reports) {
/* 104 */     DataGenerator.PackGenerator commonVanillaPack = generator.getVanillaPack(server);
/* 105 */     commonVanillaPack.addProvider(o -> (new SnbtToNbt(o, input)).addFilter(new StructureUpdater()));
/*     */ 
/*     */     
/* 108 */     CompletableFuture<HolderLookup.Provider> vanillaRegistries = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     DataGenerator.PackGenerator serverVanillaPack = generator.getVanillaPack(server);
/*     */     
/* 118 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.registries.RegistriesDatapackGenerator::new, vanillaRegistries));
/*     */ 
/*     */     
/* 121 */     serverVanillaPack.addProvider(bindRegistries(VanillaAdvancementProvider::create, vanillaRegistries));
/* 122 */     serverVanillaPack.addProvider(bindRegistries(VanillaLootTableProvider::create, vanillaRegistries));
/* 123 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.recipes.packs.VanillaRecipeProvider.Runner::new, vanillaRegistries));
/*     */ 
/*     */     
/* 126 */     TagsProvider<Block> vanillaBlockTagsProvider = (TagsProvider)serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.VanillaBlockTagsProvider::new, vanillaRegistries));
/* 127 */     TagsProvider<Item> vanillaItemTagsProvider = (TagsProvider)serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.VanillaItemTagsProvider::new, vanillaRegistries));
/* 128 */     TagsProvider<Biome> vanillaBiomeTagsProvider = (TagsProvider)serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.BiomeTagsProvider::new, vanillaRegistries));
/* 129 */     TagsProvider<BannerPattern> vanillaBannerPatternTagsProvider = (TagsProvider)serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.BannerPatternTagsProvider::new, vanillaRegistries));
/* 130 */     TagsProvider<Structure> vanillaStructureTagsProvider = (TagsProvider)serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.StructureTagsProvider::new, vanillaRegistries));
/*     */     
/* 132 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.DamageTypeTagsProvider::new, vanillaRegistries));
/* 133 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.DialogTagsProvider::new, vanillaRegistries));
/* 134 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.EntityTypeTagsProvider::new, vanillaRegistries));
/* 135 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.FlatLevelGeneratorPresetTagsProvider::new, vanillaRegistries));
/* 136 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.FluidTagsProvider::new, vanillaRegistries));
/* 137 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.GameEventTagsProvider::new, vanillaRegistries));
/* 138 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.InstrumentTagsProvider::new, vanillaRegistries));
/* 139 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.PaintingVariantTagsProvider::new, vanillaRegistries));
/* 140 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.PoiTypeTagsProvider::new, vanillaRegistries));
/* 141 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.WorldPresetTagsProvider::new, vanillaRegistries));
/* 142 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.VanillaEnchantmentTagsProvider::new, vanillaRegistries));
/* 143 */     serverVanillaPack.addProvider(bindRegistries(net.minecraft.data.tags.TimelineTagsProvider::new, vanillaRegistries));
/*     */ 
/*     */ 
/*     */     
/* 147 */     DataGenerator.PackGenerator devVanillaPack = generator.getVanillaPack(dev);
/* 148 */     devVanillaPack.addProvider(o -> new NbtToSnbt(o, input));
/*     */ 
/*     */ 
/*     */     
/* 152 */     DataGenerator.PackGenerator reportsVanillaPack = generator.getVanillaPack(reports);
/* 153 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.BiomeParametersDumpReport::new, vanillaRegistries));
/* 154 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.ItemListReport::new, vanillaRegistries));
/* 155 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.BlockListReport::new, vanillaRegistries));
/* 156 */     reportsVanillaPack.addProvider(bindRegistries(net.minecraft.data.info.CommandsReport::new, vanillaRegistries));
/* 157 */     reportsVanillaPack.addProvider(net.minecraft.data.info.RegistryDumpReport::new);
/* 158 */     reportsVanillaPack.addProvider(net.minecraft.data.info.PacketReport::new);
/* 159 */     reportsVanillaPack.addProvider(net.minecraft.data.info.DatapackStructureReport::new);
/* 160 */     reportsVanillaPack.addProvider(net.minecraft.server.jsonrpc.dataprovider.JsonRpcApiSchema::new);
/*     */ 
/*     */ 
/*     */     
/* 164 */     CompletableFuture<RegistrySetBuilder.PatchedRegistries> tradeRebalanceRegistries = TradeRebalanceRegistries.createLookup(vanillaRegistries);
/* 165 */     CompletableFuture<HolderLookup.Provider> patchedRegistrySet = tradeRebalanceRegistries.thenApply(RegistrySetBuilder.PatchedRegistries::patches);
/*     */     
/* 167 */     DataGenerator.PackGenerator tradeRebalancePack = generator.getBuiltinDatapack(server, "trade_rebalance");
/* 168 */     tradeRebalancePack.addProvider(bindRegistries(net.minecraft.data.registries.RegistriesDatapackGenerator::new, patchedRegistrySet));
/* 169 */     tradeRebalancePack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, Component.translatable("dataPack.trade_rebalance.description"), FeatureFlagSet.of(FeatureFlags.TRADE_REBALANCE)));
/* 170 */     tradeRebalancePack.addProvider(bindRegistries(TradeRebalanceLootTableProvider::create, vanillaRegistries));
/* 171 */     tradeRebalancePack.addProvider(bindRegistries(net.minecraft.data.tags.TradeRebalanceEnchantmentTagsProvider::new, vanillaRegistries));
/*     */ 
/*     */ 
/*     */     
/* 175 */     DataGenerator.PackGenerator redstoneChangesPack = generator.getBuiltinDatapack(server, "redstone_experiments");
/* 176 */     redstoneChangesPack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, Component.translatable("dataPack.redstone_experiments.description"), FeatureFlagSet.of(FeatureFlags.REDSTONE_EXPERIMENTS)));
/*     */ 
/*     */ 
/*     */     
/* 180 */     DataGenerator.PackGenerator minecartImprovementsPack = generator.getBuiltinDatapack(server, "minecart_improvements");
/* 181 */     minecartImprovementsPack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, Component.translatable("dataPack.minecart_improvements.description"), FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
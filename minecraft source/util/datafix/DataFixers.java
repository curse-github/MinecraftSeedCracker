/*      */ package net.minecraft.util.datafix;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*      */ import com.mojang.datafixers.DSL;
/*      */ import com.mojang.datafixers.DataFix;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.DataFixerBuilder;
/*      */ import com.mojang.datafixers.OpticFinder;
/*      */ import com.mojang.datafixers.TypeRewriteRule;
/*      */ import com.mojang.datafixers.Typed;
/*      */ import com.mojang.datafixers.schemas.Schema;
/*      */ import com.mojang.datafixers.types.Type;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.fixes.AbstractArrowPickupFix;
/*      */ import net.minecraft.util.datafix.fixes.AddFieldFix;
/*      */ import net.minecraft.util.datafix.fixes.AddFlagIfNotPresentFix;
/*      */ import net.minecraft.util.datafix.fixes.AddNewChoices;
/*      */ import net.minecraft.util.datafix.fixes.AdvancementsFix;
/*      */ import net.minecraft.util.datafix.fixes.AdvancementsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.AreaEffectCloudDurationScaleFix;
/*      */ import net.minecraft.util.datafix.fixes.AreaEffectCloudPotionFix;
/*      */ import net.minecraft.util.datafix.fixes.AttributeIdPrefixFix;
/*      */ import net.minecraft.util.datafix.fixes.AttributeModifierIdFix;
/*      */ import net.minecraft.util.datafix.fixes.AttributesRenameLegacy;
/*      */ import net.minecraft.util.datafix.fixes.BannerEntityCustomNameToOverrideComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.BannerPatternFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.BedItemColorFix;
/*      */ import net.minecraft.util.datafix.fixes.BeehiveFieldRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.BiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.BitStorageAlignFix;
/*      */ import net.minecraft.util.datafix.fixes.BlendingDataFix;
/*      */ import net.minecraft.util.datafix.fixes.BlendingDataRemoveFromNetherEndFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityBannerColorFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityBlockStateFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityCustomNameToComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityFurnaceBurnTimeFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityIdFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityJukeboxFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityShulkerBoxColorFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntitySignDoubleSidedEditableTextFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockNameFlatteningFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockPosFormatAndRenamesFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockPropertyRenameAndFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockStateStructureTemplateFix;
/*      */ import net.minecraft.util.datafix.fixes.BoatSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.CarvingStepRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.CatTypeFix;
/*      */ import net.minecraft.util.datafix.fixes.CauldronRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.CavesAndCliffsRenames;
/*      */ import net.minecraft.util.datafix.fixes.ChestedHorsesInventoryZeroIndexingFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkBedBlockEntityInjecterFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkBiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkDeleteIgnoredLightDataFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkDeleteLightFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkHeightAndBiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkLightRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkProtoTickListFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkRenamesFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkStatusFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkStatusFix2;
/*      */ import net.minecraft.util.datafix.fixes.ChunkStructuresTemplateRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkTicketUnpackPosFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkToProtochunkFix;
/*      */ import net.minecraft.util.datafix.fixes.ColorlessShulkerEntityFix;
/*      */ import net.minecraft.util.datafix.fixes.ContainerBlockEntityLockPredicateFix;
/*      */ import net.minecraft.util.datafix.fixes.CopperGolemWeatherStateFix;
/*      */ import net.minecraft.util.datafix.fixes.CriteriaRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.CustomModelDataExpandFix;
/*      */ import net.minecraft.util.datafix.fixes.DebugProfileOverlayReferenceFix;
/*      */ import net.minecraft.util.datafix.fixes.DecoratedPotFieldRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.DropChancesFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.DropInvalidSignDataFix;
/*      */ import net.minecraft.util.datafix.fixes.DyeItemRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EffectDurationFix;
/*      */ import net.minecraft.util.datafix.fixes.EmptyItemInHotbarFix;
/*      */ import net.minecraft.util.datafix.fixes.EmptyItemInVillagerTradeFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityArmorStandSilentFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityAttributeBaseValueFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityBlockStateFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityBrushableBlockFieldsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityCatSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityCodSalmonFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityCustomNameToComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityElderGuardianSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityEquipmentToArmorAndHandFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityFallDistanceFloatToDoubleFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityFieldsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityGoatMissingStateFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityHealthFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityHorseSaddleFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityHorseSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityIdFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityItemFrameDirectionFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityMinecartIdentifiersFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityPaintingItemFrameDirectionFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityPaintingMotiveFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityProjectileOwnerFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityPufferfishRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityRavagerRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityRedundantChanceTagsFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityRidingToPassengersFix;
/*      */ import net.minecraft.util.datafix.fixes.EntitySalmonSizeFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityShulkerColorFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityShulkerRotationFix;
/*      */ import net.minecraft.util.datafix.fixes.EntitySkeletonSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntitySpawnerItemVariantComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityStringUuidFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityTheRenameningFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityTippedArrowFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityVariantFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityWolfColorFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityZombieSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityZombieVillagerTypeFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityZombifiedPiglinRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EquipmentFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.EquippableAssetRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.FeatureFlagRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.FilteredBooksFix;
/*      */ import net.minecraft.util.datafix.fixes.FilteredSignsFix;
/*      */ import net.minecraft.util.datafix.fixes.FireResistantToDamageResistantComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.FixProjectileStoredItem;
/*      */ import net.minecraft.util.datafix.fixes.FixWolfHealth;
/*      */ import net.minecraft.util.datafix.fixes.FoodToConsumableFix;
/*      */ import net.minecraft.util.datafix.fixes.ForcePoiRebuild;
/*      */ import net.minecraft.util.datafix.fixes.ForcedChunkToTicketFix;
/*      */ import net.minecraft.util.datafix.fixes.FurnaceRecipeFix;
/*      */ import net.minecraft.util.datafix.fixes.GameRuleRegistryFix;
/*      */ import net.minecraft.util.datafix.fixes.GoatHornIdFix;
/*      */ import net.minecraft.util.datafix.fixes.GossipUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.HeightmapRenamingFix;
/*      */ import net.minecraft.util.datafix.fixes.HorseBodyArmorItemFix;
/*      */ import net.minecraft.util.datafix.fixes.IglooMetadataRemovalFix;
/*      */ import net.minecraft.util.datafix.fixes.InlineBlockPosFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.InvalidBlockEntityLockFix;
/*      */ import net.minecraft.util.datafix.fixes.InvalidLockComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemBannerColorFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemCustomNameToComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemIdFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemLoreFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemPotionFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemShulkerBoxColorFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemSpawnEggFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackCustomNameToOverrideComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackEnchantmentNamesFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackMapIdFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackSpawnEggFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackTheFlatteningFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemWaterPotionFix;
/*      */ import net.minecraft.util.datafix.fixes.JigsawPropertiesFix;
/*      */ import net.minecraft.util.datafix.fixes.JigsawRotationFix;
/*      */ import net.minecraft.util.datafix.fixes.JukeboxTicksSinceSongStartedFix;
/*      */ import net.minecraft.util.datafix.fixes.LeavesFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyDimensionIdFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyDragonFightFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyHoverEventFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyWorldBorderFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelDataGeneratorOptionsFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelFlatGeneratorInfoFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelLegacyWorldGenSettingsFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.LockComponentPredicateFix;
/*      */ import net.minecraft.util.datafix.fixes.LodestoneCompassComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.MapBannerBlockPosFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.MapIdFix;
/*      */ import net.minecraft.util.datafix.fixes.MemoryExpiryDataFix;
/*      */ import net.minecraft.util.datafix.fixes.MissingDimensionFix;
/*      */ import net.minecraft.util.datafix.fixes.MobEffectIdFix;
/*      */ import net.minecraft.util.datafix.fixes.MobSpawnerEntityIdentifiersFix;
/*      */ import net.minecraft.util.datafix.fixes.NamedEntityConvertUncheckedFix;
/*      */ import net.minecraft.util.datafix.fixes.NamedEntityWriteReadFix;
/*      */ import net.minecraft.util.datafix.fixes.NamespacedTypeRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.NewVillageFix;
/*      */ import net.minecraft.util.datafix.fixes.ObjectiveRenderTypeFix;
/*      */ import net.minecraft.util.datafix.fixes.OminousBannerBlockEntityRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.OminousBannerRarityFix;
/*      */ import net.minecraft.util.datafix.fixes.OminousBannerRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsAccessibilityOnboardFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsAddTextBackgroundFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsAmbientOcclusionFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsFancyGraphicsToGraphicsModeFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsForceVBOFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsGraphicsModeSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsKeyLwjgl3Fix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsKeyTranslationFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsLowerCaseLanguageFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsMenuBlurrinessFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsMusicToastFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsProgrammerArtFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsRenameFieldFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsSetGraphicsPresetToCustomFix;
/*      */ import net.minecraft.util.datafix.fixes.OverreachingTickFix;
/*      */ import net.minecraft.util.datafix.fixes.ParticleUnflatteningFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerEquipmentFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerHeadBlockProfileFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerRespawnDataFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.PoiTypeRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.PoiTypeRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.PrimedTntBlockStateFixer;
/*      */ import net.minecraft.util.datafix.fixes.ProjectileStoredWeaponFix;
/*      */ import net.minecraft.util.datafix.fixes.RaidRenamesDataFix;
/*      */ import net.minecraft.util.datafix.fixes.RandomSequenceSettingsFix;
/*      */ import net.minecraft.util.datafix.fixes.RecipesFix;
/*      */ import net.minecraft.util.datafix.fixes.RecipesRenameningFix;
/*      */ import net.minecraft.util.datafix.fixes.RedstoneWireConnectionsFix;
/*      */ import net.minecraft.util.datafix.fixes.References;
/*      */ import net.minecraft.util.datafix.fixes.RemapChunkStatusFix;
/*      */ import net.minecraft.util.datafix.fixes.RemoveBlockEntityTagFix;
/*      */ import net.minecraft.util.datafix.fixes.RemoveEmptyItemInBrushableBlockFix;
/*      */ import net.minecraft.util.datafix.fixes.RemoveGolemGossipFix;
/*      */ import net.minecraft.util.datafix.fixes.RenameEnchantmentsFix;
/*      */ import net.minecraft.util.datafix.fixes.RenamedCoralFansFix;
/*      */ import net.minecraft.util.datafix.fixes.RenamedCoralFix;
/*      */ import net.minecraft.util.datafix.fixes.ReorganizePoi;
/*      */ import net.minecraft.util.datafix.fixes.SaddleEquipmentSlotFix;
/*      */ import net.minecraft.util.datafix.fixes.SavedDataFeaturePoolElementFix;
/*      */ import net.minecraft.util.datafix.fixes.SavedDataUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.ScoreboardDisplayNameFix;
/*      */ import net.minecraft.util.datafix.fixes.ScoreboardDisplaySlotFix;
/*      */ import net.minecraft.util.datafix.fixes.SignTextStrictJsonFix;
/*      */ import net.minecraft.util.datafix.fixes.SpawnerDataFix;
/*      */ import net.minecraft.util.datafix.fixes.StatsCounterFix;
/*      */ import net.minecraft.util.datafix.fixes.StatsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.StriderGravityFix;
/*      */ import net.minecraft.util.datafix.fixes.StructureReferenceCountFix;
/*      */ import net.minecraft.util.datafix.fixes.StructureSettingsFlattenFix;
/*      */ import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
/*      */ import net.minecraft.util.datafix.fixes.TextComponentHoverAndClickEventFix;
/*      */ import net.minecraft.util.datafix.fixes.TextComponentStringifiedFlagsFix;
/*      */ import net.minecraft.util.datafix.fixes.ThrownPotionSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.TippedArrowPotionToItemFix;
/*      */ import net.minecraft.util.datafix.fixes.TooltipDisplayComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.TrappedChestBlockEntityFix;
/*      */ import net.minecraft.util.datafix.fixes.TrialSpawnerConfigFix;
/*      */ import net.minecraft.util.datafix.fixes.TrialSpawnerConfigInRegistryFix;
/*      */ import net.minecraft.util.datafix.fixes.TridentAnimationFix;
/*      */ import net.minecraft.util.datafix.fixes.UnflattenTextComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.VariantRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerDataFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerFollowRangeFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerRebuildLevelAndXpFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerSetCanPickUpLootFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerTradeFix;
/*      */ import net.minecraft.util.datafix.fixes.WallPropertyFix;
/*      */ import net.minecraft.util.datafix.fixes.WeaponSmithChestLootTableFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldBorderWarningTimeFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldGenSettingsDisallowOldCustomWorldsFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldGenSettingsFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldGenSettingsHeightAndBiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldSpawnDataFix;
/*      */ import net.minecraft.util.datafix.fixes.WriteAndReadFix;
/*      */ import net.minecraft.util.datafix.fixes.WrittenBookPagesStrictJsonFix;
/*      */ import net.minecraft.util.datafix.fixes.ZombieVillagerRebuildXpFix;
/*      */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DataFixers
/*      */ {
/*  398 */   private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
/*  399 */   private static final BiFunction<Integer, Schema, Schema> SAME_NAMESPACED = NamespacedSchema::new;
/*  400 */   private static final DataFixerBuilder.Result DATA_FIXER = createFixerUpper();
/*      */ 
/*      */   
/*      */   public static final int BLENDING_VERSION = 4295;
/*      */ 
/*      */ 
/*      */   
/*  407 */   public static DataFixer getDataFixer() { return DATA_FIXER.fixer(); }
/*      */ 
/*      */   
/*      */   private static DataFixerBuilder.Result createFixerUpper() {
/*  411 */     fixerUpper = new DataFixerBuilder(SharedConstants.getCurrentVersion().dataVersion().version());
/*  412 */     addFixers(fixerUpper);
/*  413 */     return fixerUpper.build();
/*      */   }
/*      */   
/*      */   public static CompletableFuture<?> optimize(Set<DSL.TypeReference> typesToOptimize) {
/*  417 */     if (typesToOptimize.isEmpty()) {
/*  418 */       return CompletableFuture.completedFuture(null);
/*      */     }
/*      */     
/*  421 */     Executor executor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder())
/*  422 */         .setNameFormat("Datafixer Bootstrap")
/*  423 */         .setDaemon(true)
/*  424 */         .setPriority(1)
/*  425 */         .build());
/*  426 */     return DATA_FIXER.optimize(typesToOptimize, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addFixers(DataFixerBuilder fixerUpper) {
/*  434 */     fixerUpper.addSchema(99, net.minecraft.util.datafix.schemas.V99::new);
/*      */ 
/*      */     
/*  437 */     Schema v100 = fixerUpper.addSchema(100, net.minecraft.util.datafix.schemas.V100::new);
/*  438 */     fixerUpper.addFixer(new EntityEquipmentToArmorAndHandFix(v100));
/*      */     
/*  440 */     Schema v101 = fixerUpper.addSchema(101, SAME);
/*  441 */     fixerUpper.addFixer(new VillagerSetCanPickUpLootFix(v101));
/*      */     
/*  443 */     Schema v102 = fixerUpper.addSchema(102, net.minecraft.util.datafix.schemas.V102::new);
/*  444 */     fixerUpper.addFixer(new ItemIdFix(v102, true));
/*  445 */     fixerUpper.addFixer(new ItemPotionFix(v102, false));
/*      */     
/*  447 */     Schema v105 = fixerUpper.addSchema(105, SAME);
/*  448 */     fixerUpper.addFixer(new ItemSpawnEggFix(v105, true));
/*      */ 
/*      */     
/*  451 */     Schema v106 = fixerUpper.addSchema(106, net.minecraft.util.datafix.schemas.V106::new);
/*  452 */     fixerUpper.addFixer(new MobSpawnerEntityIdentifiersFix(v106, true));
/*      */     
/*  454 */     Schema v107 = fixerUpper.addSchema(107, net.minecraft.util.datafix.schemas.V107::new);
/*  455 */     fixerUpper.addFixer(new EntityMinecartIdentifiersFix(v107));
/*      */     
/*  457 */     Schema v108 = fixerUpper.addSchema(108, SAME);
/*  458 */     fixerUpper.addFixer(new EntityStringUuidFix(v108, true));
/*      */     
/*  460 */     Schema v109 = fixerUpper.addSchema(109, SAME);
/*  461 */     fixerUpper.addFixer(new EntityHealthFix(v109, true));
/*      */     
/*  463 */     Schema v110 = fixerUpper.addSchema(110, SAME);
/*  464 */     fixerUpper.addFixer(new EntityHorseSaddleFix(v110, true));
/*      */     
/*  466 */     Schema v111 = fixerUpper.addSchema(111, SAME);
/*  467 */     fixerUpper.addFixer(new EntityPaintingItemFrameDirectionFix(v111, true));
/*      */     
/*  469 */     Schema v113 = fixerUpper.addSchema(113, SAME);
/*  470 */     fixerUpper.addFixer(new EntityRedundantChanceTagsFix(v113, true));
/*      */     
/*  472 */     Schema v135 = fixerUpper.addSchema(135, net.minecraft.util.datafix.schemas.V135::new);
/*  473 */     fixerUpper.addFixer(new EntityRidingToPassengersFix(v135, true));
/*      */     
/*  475 */     Schema v143 = fixerUpper.addSchema(143, net.minecraft.util.datafix.schemas.V143::new);
/*  476 */     fixerUpper.addFixer(new EntityTippedArrowFix(v143, true));
/*      */     
/*  478 */     Schema v147 = fixerUpper.addSchema(147, SAME);
/*  479 */     fixerUpper.addFixer(new EntityArmorStandSilentFix(v147, true));
/*      */     
/*  481 */     Schema v165 = fixerUpper.addSchema(165, SAME);
/*      */     
/*  483 */     fixerUpper.addFixer(new SignTextStrictJsonFix(v165));
/*  484 */     fixerUpper.addFixer(new WrittenBookPagesStrictJsonFix(v165));
/*      */ 
/*      */     
/*  487 */     Schema v501 = fixerUpper.addSchema(501, net.minecraft.util.datafix.schemas.V501::new);
/*  488 */     fixerUpper.addFixer(new AddNewChoices(v501, "Add 1.10 entities fix", References.ENTITY));
/*      */     
/*  490 */     Schema v502 = fixerUpper.addSchema(502, SAME);
/*  491 */     fixerUpper.addFixer(ItemRenameFix.create(v502, "cooked_fished item renamer", item -> 
/*  492 */           Objects.equals(NamespacedSchema.ensureNamespaced(item), "minecraft:cooked_fished") ? "minecraft:cooked_fish" : item));
/*      */     
/*  494 */     fixerUpper.addFixer(new EntityZombieVillagerTypeFix(v502, false));
/*      */     
/*  496 */     Schema v505 = fixerUpper.addSchema(505, SAME);
/*  497 */     fixerUpper.addFixer(new OptionsForceVBOFix(v505, false));
/*      */ 
/*      */     
/*  500 */     Schema v700 = fixerUpper.addSchema(700, net.minecraft.util.datafix.schemas.V700::new);
/*  501 */     fixerUpper.addFixer(new EntityElderGuardianSplitFix(v700, true));
/*      */     
/*  503 */     Schema v701 = fixerUpper.addSchema(701, net.minecraft.util.datafix.schemas.V701::new);
/*  504 */     fixerUpper.addFixer(new EntitySkeletonSplitFix(v701, true));
/*      */     
/*  506 */     Schema v702 = fixerUpper.addSchema(702, net.minecraft.util.datafix.schemas.V702::new);
/*  507 */     fixerUpper.addFixer(new EntityZombieSplitFix(v702));
/*      */     
/*  509 */     Schema v703 = fixerUpper.addSchema(703, net.minecraft.util.datafix.schemas.V703::new);
/*  510 */     fixerUpper.addFixer(new EntityHorseSplitFix(v703, true));
/*      */ 
/*      */     
/*  513 */     Schema v704 = fixerUpper.addSchema(704, net.minecraft.util.datafix.schemas.V704::new);
/*  514 */     fixerUpper.addFixer(new BlockEntityIdFix(v704, true));
/*      */     
/*  516 */     Schema v705 = fixerUpper.addSchema(705, net.minecraft.util.datafix.schemas.V705::new);
/*  517 */     fixerUpper.addFixer(new EntityIdFix(v705, true));
/*      */     
/*  519 */     Schema v804 = fixerUpper.addSchema(804, SAME_NAMESPACED);
/*  520 */     fixerUpper.addFixer(new ItemBannerColorFix(v804, true));
/*      */     
/*  522 */     Schema v806 = fixerUpper.addSchema(806, SAME_NAMESPACED);
/*  523 */     fixerUpper.addFixer(new ItemWaterPotionFix(v806, false));
/*      */ 
/*      */     
/*  526 */     Schema v808 = fixerUpper.addSchema(808, net.minecraft.util.datafix.schemas.V808::new);
/*  527 */     fixerUpper.addFixer(new AddNewChoices(v808, "added shulker box", References.BLOCK_ENTITY));
/*      */     
/*  529 */     Schema v808_1 = fixerUpper.addSchema(808, 1, SAME_NAMESPACED);
/*  530 */     fixerUpper.addFixer(new EntityShulkerColorFix(v808_1, false));
/*      */     
/*  532 */     Schema v813 = fixerUpper.addSchema(813, SAME_NAMESPACED);
/*  533 */     fixerUpper.addFixer(new ItemShulkerBoxColorFix(v813, false));
/*  534 */     fixerUpper.addFixer(new BlockEntityShulkerBoxColorFix(v813, false));
/*      */     
/*  536 */     Schema v816 = fixerUpper.addSchema(816, SAME_NAMESPACED);
/*  537 */     fixerUpper.addFixer(new OptionsLowerCaseLanguageFix(v816, false));
/*      */ 
/*      */     
/*  540 */     Schema v820 = fixerUpper.addSchema(820, SAME_NAMESPACED);
/*  541 */     fixerUpper.addFixer(ItemRenameFix.create(v820, "totem item renamer", createRenamer("minecraft:totem", "minecraft:totem_of_undying")));
/*      */ 
/*      */     
/*  544 */     Schema v1022 = fixerUpper.addSchema(1022, net.minecraft.util.datafix.schemas.V1022::new);
/*  545 */     fixerUpper.addFixer(new WriteAndReadFix(v1022, "added shoulder entities to players", References.PLAYER));
/*      */     
/*  547 */     Schema v1125 = fixerUpper.addSchema(1125, net.minecraft.util.datafix.schemas.V1125::new);
/*  548 */     fixerUpper.addFixer(new ChunkBedBlockEntityInjecterFix(v1125, true));
/*  549 */     fixerUpper.addFixer(new BedItemColorFix(v1125, false));
/*      */ 
/*      */     
/*  552 */     Schema v1344 = fixerUpper.addSchema(1344, SAME_NAMESPACED);
/*  553 */     fixerUpper.addFixer(new OptionsKeyLwjgl3Fix(v1344, false));
/*      */     
/*  555 */     Schema v1446 = fixerUpper.addSchema(1446, SAME_NAMESPACED);
/*  556 */     fixerUpper.addFixer(new OptionsKeyTranslationFix(v1446, false));
/*      */     
/*  558 */     Schema v1450 = fixerUpper.addSchema(1450, SAME_NAMESPACED);
/*  559 */     fixerUpper.addFixer(new BlockStateStructureTemplateFix(v1450, false));
/*      */     
/*  561 */     Schema v1451 = fixerUpper.addSchema(1451, net.minecraft.util.datafix.schemas.V1451::new);
/*  562 */     fixerUpper.addFixer(new AddNewChoices(v1451, "AddTrappedChestFix", References.BLOCK_ENTITY));
/*      */     
/*  564 */     Schema v1451_1 = fixerUpper.addSchema(1451, 1, net.minecraft.util.datafix.schemas.V1451_1::new);
/*  565 */     fixerUpper.addFixer(new ChunkPalettedStorageFix(v1451_1, true));
/*      */ 
/*      */     
/*  568 */     Schema v1451_2 = fixerUpper.addSchema(1451, 2, net.minecraft.util.datafix.schemas.V1451_2::new);
/*  569 */     fixerUpper.addFixer(new BlockEntityBlockStateFix(v1451_2, true));
/*      */     
/*  571 */     Schema v1451_3 = fixerUpper.addSchema(1451, 3, net.minecraft.util.datafix.schemas.V1451_3::new);
/*  572 */     fixerUpper.addFixer(new EntityBlockStateFix(v1451_3, true));
/*  573 */     fixerUpper.addFixer(new ItemStackMapIdFix(v1451_3, false));
/*      */     
/*  575 */     Schema v1451_4 = fixerUpper.addSchema(1451, 4, net.minecraft.util.datafix.schemas.V1451_4::new);
/*  576 */     fixerUpper.addFixer(new BlockNameFlatteningFix(v1451_4, true));
/*  577 */     fixerUpper.addFixer(new ItemStackTheFlatteningFix(v1451_4, false));
/*      */     
/*  579 */     Schema v1451_5 = fixerUpper.addSchema(1451, 5, net.minecraft.util.datafix.schemas.V1451_5::new);
/*      */     
/*  581 */     fixerUpper.addFixer(new RemoveBlockEntityTagFix(v1451_5, Set.of("minecraft:noteblock", "minecraft:flower_pot")));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  586 */     fixerUpper.addFixer(new ItemStackSpawnEggFix(v1451_5, false, "minecraft:spawn_egg"));
/*  587 */     fixerUpper.addFixer(new EntityWolfColorFix(v1451_5, false));
/*  588 */     fixerUpper.addFixer(new BlockEntityBannerColorFix(v1451_5, false));
/*  589 */     fixerUpper.addFixer(new LevelFlatGeneratorInfoFix(v1451_5, false));
/*      */     
/*  591 */     Schema v1451_6 = fixerUpper.addSchema(1451, 6, net.minecraft.util.datafix.schemas.V1451_6::new);
/*  592 */     fixerUpper.addFixer(new StatsCounterFix(v1451_6, true));
/*  593 */     fixerUpper.addFixer(new BlockEntityJukeboxFix(v1451_6, false));
/*      */     
/*  595 */     Schema v1451_8 = fixerUpper.addSchema(1451, 7, SAME_NAMESPACED);
/*  596 */     fixerUpper.addFixer(new VillagerTradeFix(v1451_8));
/*      */     
/*  598 */     Schema v1456 = fixerUpper.addSchema(1456, SAME_NAMESPACED);
/*  599 */     fixerUpper.addFixer(new EntityItemFrameDirectionFix(v1456, false));
/*      */     
/*  601 */     Schema v1458 = fixerUpper.addSchema(1458, net.minecraft.util.datafix.schemas.V1458::new);
/*  602 */     fixerUpper.addFixer(new EntityCustomNameToComponentFix(v1458));
/*  603 */     fixerUpper.addFixer(new ItemCustomNameToComponentFix(v1458));
/*  604 */     fixerUpper.addFixer(new BlockEntityCustomNameToComponentFix(v1458));
/*      */     
/*  606 */     Schema v1460 = fixerUpper.addSchema(1460, net.minecraft.util.datafix.schemas.V1460::new);
/*  607 */     fixerUpper.addFixer(new EntityPaintingMotiveFix(v1460, false));
/*      */     
/*  609 */     Schema v1466 = fixerUpper.addSchema(1466, net.minecraft.util.datafix.schemas.V1466::new);
/*  610 */     fixerUpper.addFixer(new AddNewChoices(v1466, "Add DUMMY block entity", References.BLOCK_ENTITY));
/*  611 */     fixerUpper.addFixer(new ChunkToProtochunkFix(v1466, true));
/*      */ 
/*      */     
/*  614 */     Schema v1470 = fixerUpper.addSchema(1470, net.minecraft.util.datafix.schemas.V1470::new);
/*  615 */     fixerUpper.addFixer(new AddNewChoices(v1470, "Add 1.13 entities fix", References.ENTITY));
/*      */     
/*  617 */     Schema v1474 = fixerUpper.addSchema(1474, SAME_NAMESPACED);
/*  618 */     fixerUpper.addFixer(new ColorlessShulkerEntityFix(v1474, false));
/*  619 */     fixerUpper.addFixer(BlockRenameFix.create(v1474, "Colorless shulker block fixer", block -> Objects.equals(NamespacedSchema.ensureNamespaced(block), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : block));
/*  620 */     fixerUpper.addFixer(ItemRenameFix.create(v1474, "Colorless shulker item fixer", block -> Objects.equals(NamespacedSchema.ensureNamespaced(block), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : block));
/*      */     
/*  622 */     Schema v1475 = fixerUpper.addSchema(1475, SAME_NAMESPACED);
/*  623 */     fixerUpper.addFixer(BlockRenameFix.create(v1475, "Flowing fixer", createRenamer(
/*  624 */             ImmutableMap.of("minecraft:flowing_water", "minecraft:water", "minecraft:flowing_lava", "minecraft:lava"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  630 */     Schema v1480 = fixerUpper.addSchema(1480, SAME_NAMESPACED);
/*  631 */     fixerUpper.addFixer(BlockRenameFix.create(v1480, "Rename coral blocks", createRenamer(RenamedCoralFix.RENAMED_IDS)));
/*  632 */     fixerUpper.addFixer(ItemRenameFix.create(v1480, "Rename coral items", createRenamer(RenamedCoralFix.RENAMED_IDS)));
/*      */     
/*  634 */     Schema v1481 = fixerUpper.addSchema(1481, net.minecraft.util.datafix.schemas.V1481::new);
/*  635 */     fixerUpper.addFixer(new AddNewChoices(v1481, "Add conduit", References.BLOCK_ENTITY));
/*      */     
/*  637 */     Schema v1483 = fixerUpper.addSchema(1483, net.minecraft.util.datafix.schemas.V1483::new);
/*  638 */     fixerUpper.addFixer(new EntityPufferfishRenameFix(v1483, true));
/*  639 */     fixerUpper.addFixer(ItemRenameFix.create(v1483, "Rename pufferfish egg item", createRenamer(EntityPufferfishRenameFix.RENAMED_IDS)));
/*      */     
/*  641 */     Schema v1484 = fixerUpper.addSchema(1484, SAME_NAMESPACED);
/*  642 */     fixerUpper.addFixer(ItemRenameFix.create(v1484, "Rename seagrass items", createRenamer(ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
/*      */ 
/*      */ 
/*      */     
/*  646 */     fixerUpper.addFixer(BlockRenameFix.create(v1484, "Rename seagrass blocks", createRenamer(ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  652 */     fixerUpper.addFixer(new HeightmapRenamingFix(v1484, false));
/*      */ 
/*      */     
/*  655 */     Schema v1486 = fixerUpper.addSchema(1486, net.minecraft.util.datafix.schemas.V1486::new);
/*  656 */     fixerUpper.addFixer(new EntityCodSalmonFix(v1486, true));
/*  657 */     fixerUpper.addFixer(ItemRenameFix.create(v1486, "Rename cod/salmon egg items", createRenamer(EntityCodSalmonFix.RENAMED_EGG_IDS)));
/*      */     
/*  659 */     Schema v1487 = fixerUpper.addSchema(1487, SAME_NAMESPACED);
/*  660 */     fixerUpper.addFixer(ItemRenameFix.create(v1487, "Rename prismarine_brick(s)_* blocks", createRenamer(ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
/*      */ 
/*      */ 
/*      */     
/*  664 */     fixerUpper.addFixer(BlockRenameFix.create(v1487, "Rename prismarine_brick(s)_* items", createRenamer(ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  669 */     Schema v1488 = fixerUpper.addSchema(1488, net.minecraft.util.datafix.schemas.V1488::new);
/*  670 */     fixerUpper.addFixer(BlockRenameFix.create(v1488, "Rename kelp/kelptop", createRenamer(ImmutableMap.of("minecraft:kelp_top", "minecraft:kelp", "minecraft:kelp", "minecraft:kelp_plant"))));
/*      */ 
/*      */ 
/*      */     
/*  674 */     fixerUpper.addFixer(ItemRenameFix.create(v1488, "Rename kelptop", createRenamer("minecraft:kelp_top", "minecraft:kelp")));
/*  675 */     fixerUpper.addFixer(new NamedEntityWriteReadFix(v1488, true, "Command block block entity custom name fix", References.BLOCK_ENTITY, "minecraft:command_block")
/*      */         {
/*      */           protected <T> Dynamic<T> fix(Dynamic<T> input) {
/*  678 */             return BlockEntityCustomNameToComponentFix.fixTagCustomName(input);
/*      */           }
/*      */         });
/*  681 */     fixerUpper.addFixer(new DataFix(v1488, false)
/*      */         {
/*      */           protected TypeRewriteRule makeRule()
/*      */           {
/*  685 */             Type<?> entityType = getInputSchema().getType(References.ENTITY);
/*  686 */             OpticFinder<String> idFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*  687 */             OpticFinder<?> customNameFinder = entityType.findField("CustomName");
/*  688 */             OpticFinder<Pair<String, String>> componentFinder = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*  689 */             return fixTypeEverywhereTyped("Command block minecart custom name fix", entityType, input -> {
/*  690 */                   String id = (String)input.getOptional(idFinder).orElse("");
/*  691 */                   if (!"minecraft:commandblock_minecart".equals(id)) {
/*  692 */                     return input;
/*      */                   }
/*  694 */                   return input.updateTyped(customNameFinder, ());
/*      */                 });
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*  700 */     fixerUpper.addFixer(new IglooMetadataRemovalFix(v1488, false));
/*      */     
/*  702 */     Schema v1490 = fixerUpper.addSchema(1490, SAME_NAMESPACED);
/*  703 */     fixerUpper.addFixer(BlockRenameFix.create(v1490, "Rename melon_block", createRenamer("minecraft:melon_block", "minecraft:melon")));
/*  704 */     fixerUpper.addFixer(ItemRenameFix.create(v1490, "Rename melon_block/melon/speckled_melon", createRenamer(ImmutableMap.of("minecraft:melon_block", "minecraft:melon", "minecraft:melon", "minecraft:melon_slice", "minecraft:speckled_melon", "minecraft:glistering_melon_slice"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  710 */     Schema v1492 = fixerUpper.addSchema(1492, SAME_NAMESPACED);
/*  711 */     fixerUpper.addFixer(new ChunkStructuresTemplateRenameFix(v1492, false));
/*      */     
/*  713 */     Schema v1494 = fixerUpper.addSchema(1494, SAME_NAMESPACED);
/*  714 */     fixerUpper.addFixer(new ItemStackEnchantmentNamesFix(v1494, false));
/*      */     
/*  716 */     Schema v1496 = fixerUpper.addSchema(1496, SAME_NAMESPACED);
/*  717 */     fixerUpper.addFixer(new LeavesFix(v1496, false));
/*      */     
/*  719 */     Schema v1500 = fixerUpper.addSchema(1500, SAME_NAMESPACED);
/*  720 */     fixerUpper.addFixer(new BlockEntityKeepPacked(v1500, false));
/*      */     
/*  722 */     Schema v1501 = fixerUpper.addSchema(1501, SAME_NAMESPACED);
/*  723 */     fixerUpper.addFixer(new AdvancementsFix(v1501, false));
/*      */     
/*  725 */     Schema v1502 = fixerUpper.addSchema(1502, SAME_NAMESPACED);
/*  726 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v1502, "Recipes fix", References.RECIPE, createRenamer(RecipesFix.RECIPES)));
/*      */     
/*  728 */     Schema v1506 = fixerUpper.addSchema(1506, SAME_NAMESPACED);
/*  729 */     fixerUpper.addFixer(new LevelDataGeneratorOptionsFix(v1506, false));
/*      */     
/*  731 */     Schema v1510 = fixerUpper.addSchema(1510, net.minecraft.util.datafix.schemas.V1510::new);
/*  732 */     fixerUpper.addFixer(BlockRenameFix.create(v1510, "Block renamening fix", createRenamer(EntityTheRenameningFix.RENAMED_BLOCKS)));
/*  733 */     fixerUpper.addFixer(ItemRenameFix.create(v1510, "Item renamening fix", createRenamer(EntityTheRenameningFix.RENAMED_ITEMS)));
/*  734 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v1510, "Recipes renamening fix", References.RECIPE, createRenamer(RecipesRenameningFix.RECIPES)));
/*  735 */     fixerUpper.addFixer(new EntityTheRenameningFix(v1510, true));
/*  736 */     fixerUpper.addFixer(new StatsRenameFix(v1510, "SwimStatsRenameFix", ImmutableMap.of("minecraft:swim_one_cm", "minecraft:walk_on_water_one_cm", "minecraft:dive_one_cm", "minecraft:walk_under_water_one_cm")));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  741 */     Schema v1514 = fixerUpper.addSchema(1514, SAME_NAMESPACED);
/*  742 */     fixerUpper.addFixer(new ScoreboardDisplayNameFix(v1514, "ObjectiveDisplayNameFix", References.OBJECTIVE));
/*  743 */     fixerUpper.addFixer(new ScoreboardDisplayNameFix(v1514, "TeamDisplayNameFix", References.TEAM));
/*  744 */     fixerUpper.addFixer(new ObjectiveRenderTypeFix(v1514));
/*      */     
/*  746 */     Schema v1515 = fixerUpper.addSchema(1515, SAME_NAMESPACED);
/*  747 */     fixerUpper.addFixer(BlockRenameFix.create(v1515, "Rename coral fan blocks", createRenamer(RenamedCoralFansFix.RENAMED_IDS)));
/*      */     
/*  749 */     Schema v1624 = fixerUpper.addSchema(1624, SAME_NAMESPACED);
/*  750 */     fixerUpper.addFixer(new TrappedChestBlockEntityFix(v1624, false));
/*      */     
/*  752 */     Schema v1800 = fixerUpper.addSchema(1800, net.minecraft.util.datafix.schemas.V1800::new);
/*  753 */     fixerUpper.addFixer(new AddNewChoices(v1800, "Added 1.14 mobs fix", References.ENTITY));
/*  754 */     fixerUpper.addFixer(ItemRenameFix.create(v1800, "Rename dye items", createRenamer(DyeItemRenameFix.RENAMED_IDS)));
/*      */     
/*  756 */     Schema v1801 = fixerUpper.addSchema(1801, net.minecraft.util.datafix.schemas.V1801::new);
/*  757 */     fixerUpper.addFixer(new AddNewChoices(v1801, "Added Illager Beast", References.ENTITY));
/*      */     
/*  759 */     Schema v1802 = fixerUpper.addSchema(1802, SAME_NAMESPACED);
/*  760 */     fixerUpper.addFixer(BlockRenameFix.create(v1802, "Rename sign blocks & stone slabs", createRenamer(ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign", "minecraft:wall_sign", "minecraft:oak_wall_sign"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  765 */     fixerUpper.addFixer(ItemRenameFix.create(v1802, "Rename sign item & stone slabs", createRenamer(ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  770 */     Schema v1803 = fixerUpper.addSchema(1803, SAME_NAMESPACED);
/*  771 */     fixerUpper.addFixer(new ItemLoreFix(v1803));
/*      */     
/*  773 */     Schema v1904 = fixerUpper.addSchema(1904, net.minecraft.util.datafix.schemas.V1904::new);
/*  774 */     fixerUpper.addFixer(new AddNewChoices(v1904, "Added Cats", References.ENTITY));
/*  775 */     fixerUpper.addFixer(new EntityCatSplitFix(v1904, false));
/*      */     
/*  777 */     Schema v1905 = fixerUpper.addSchema(1905, SAME_NAMESPACED);
/*  778 */     fixerUpper.addFixer(new ChunkStatusFix(v1905, false));
/*      */     
/*  780 */     Schema v1906 = fixerUpper.addSchema(1906, net.minecraft.util.datafix.schemas.V1906::new);
/*  781 */     fixerUpper.addFixer(new AddNewChoices(v1906, "Add POI Blocks", References.BLOCK_ENTITY));
/*      */     
/*  783 */     Schema v1909 = fixerUpper.addSchema(1909, net.minecraft.util.datafix.schemas.V1909::new);
/*  784 */     fixerUpper.addFixer(new AddNewChoices(v1909, "Add jigsaw", References.BLOCK_ENTITY));
/*      */     
/*  786 */     Schema v1911 = fixerUpper.addSchema(1911, SAME_NAMESPACED);
/*  787 */     fixerUpper.addFixer(new ChunkStatusFix2(v1911, false));
/*      */     
/*  789 */     Schema v1914 = fixerUpper.addSchema(1914, SAME_NAMESPACED);
/*  790 */     fixerUpper.addFixer(new WeaponSmithChestLootTableFix(v1914, false));
/*      */     
/*  792 */     Schema v1917 = fixerUpper.addSchema(1917, SAME_NAMESPACED);
/*  793 */     fixerUpper.addFixer(new CatTypeFix(v1917, false));
/*      */     
/*  795 */     Schema v1918 = fixerUpper.addSchema(1918, SAME_NAMESPACED);
/*  796 */     fixerUpper.addFixer(new VillagerDataFix(v1918, "minecraft:villager"));
/*  797 */     fixerUpper.addFixer(new VillagerDataFix(v1918, "minecraft:zombie_villager"));
/*      */     
/*  799 */     Schema v1920 = fixerUpper.addSchema(1920, net.minecraft.util.datafix.schemas.V1920::new);
/*  800 */     fixerUpper.addFixer(new NewVillageFix(v1920, false));
/*  801 */     fixerUpper.addFixer(new AddNewChoices(v1920, "Add campfire", References.BLOCK_ENTITY));
/*      */     
/*  803 */     Schema v1925 = fixerUpper.addSchema(1925, SAME_NAMESPACED);
/*  804 */     fixerUpper.addFixer(new MapIdFix(v1925));
/*      */     
/*  806 */     Schema v1928 = fixerUpper.addSchema(1928, net.minecraft.util.datafix.schemas.V1928::new);
/*  807 */     fixerUpper.addFixer(new EntityRavagerRenameFix(v1928, true));
/*  808 */     fixerUpper.addFixer(ItemRenameFix.create(v1928, "Rename ravager egg item", createRenamer(EntityRavagerRenameFix.RENAMED_IDS)));
/*      */     
/*  810 */     Schema v1929 = fixerUpper.addSchema(1929, net.minecraft.util.datafix.schemas.V1929::new);
/*  811 */     fixerUpper.addFixer(new AddNewChoices(v1929, "Add Wandering Trader and Trader Llama", References.ENTITY));
/*      */     
/*  813 */     Schema v1931 = fixerUpper.addSchema(1931, net.minecraft.util.datafix.schemas.V1931::new);
/*  814 */     fixerUpper.addFixer(new AddNewChoices(v1931, "Added Fox", References.ENTITY));
/*      */     
/*  816 */     Schema v1936 = fixerUpper.addSchema(1936, SAME_NAMESPACED);
/*  817 */     fixerUpper.addFixer(new OptionsAddTextBackgroundFix(v1936, false));
/*      */     
/*  819 */     Schema v1946 = fixerUpper.addSchema(1946, SAME_NAMESPACED);
/*  820 */     fixerUpper.addFixer(new ReorganizePoi(v1946, false));
/*      */     
/*  822 */     Schema v1948 = fixerUpper.addSchema(1948, SAME_NAMESPACED);
/*  823 */     fixerUpper.addFixer(new OminousBannerRenameFix(v1948));
/*      */     
/*  825 */     Schema v1953 = fixerUpper.addSchema(1953, SAME_NAMESPACED);
/*  826 */     fixerUpper.addFixer(new OminousBannerBlockEntityRenameFix(v1953, false));
/*      */     
/*  828 */     Schema v1955 = fixerUpper.addSchema(1955, SAME_NAMESPACED);
/*  829 */     fixerUpper.addFixer(new VillagerRebuildLevelAndXpFix(v1955, false));
/*  830 */     fixerUpper.addFixer(new ZombieVillagerRebuildXpFix(v1955, false));
/*      */     
/*  832 */     Schema v1961 = fixerUpper.addSchema(1961, SAME_NAMESPACED);
/*  833 */     fixerUpper.addFixer(new ChunkLightRemoveFix(v1961, false));
/*      */     
/*  835 */     Schema v1963 = fixerUpper.addSchema(1963, SAME_NAMESPACED);
/*  836 */     fixerUpper.addFixer(new RemoveGolemGossipFix(v1963, false));
/*      */     
/*  838 */     Schema v2100 = fixerUpper.addSchema(2100, net.minecraft.util.datafix.schemas.V2100::new);
/*  839 */     fixerUpper.addFixer(new AddNewChoices(v2100, "Added Bee and Bee Stinger", References.ENTITY));
/*  840 */     fixerUpper.addFixer(new AddNewChoices(v2100, "Add beehive", References.BLOCK_ENTITY));
/*  841 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v2100, "Rename sugar recipe", References.RECIPE, createRenamer("minecraft:sugar", "minecraft:sugar_from_sugar_cane")));
/*  842 */     fixerUpper.addFixer(new AdvancementsRenameFix(v2100, false, "Rename sugar recipe advancement", createRenamer("minecraft:recipes/misc/sugar", "minecraft:recipes/misc/sugar_from_sugar_cane")));
/*      */     
/*  844 */     Schema v2202 = fixerUpper.addSchema(2202, SAME_NAMESPACED);
/*  845 */     fixerUpper.addFixer(new ChunkBiomeFix(v2202, false));
/*      */     
/*  847 */     Schema v2209 = fixerUpper.addSchema(2209, SAME_NAMESPACED);
/*  848 */     UnaryOperator<String> beehiveRenamer = createRenamer("minecraft:bee_hive", "minecraft:beehive");
/*  849 */     fixerUpper.addFixer(ItemRenameFix.create(v2209, "Rename bee_hive item to beehive", beehiveRenamer));
/*  850 */     fixerUpper.addFixer(new PoiTypeRenameFix(v2209, "Rename bee_hive poi to beehive", beehiveRenamer));
/*  851 */     fixerUpper.addFixer(BlockRenameFix.create(v2209, "Rename bee_hive block to beehive", beehiveRenamer));
/*      */     
/*  853 */     Schema v2211 = fixerUpper.addSchema(2211, SAME_NAMESPACED);
/*  854 */     fixerUpper.addFixer(new StructureReferenceCountFix(v2211, false));
/*      */     
/*  856 */     Schema v2218 = fixerUpper.addSchema(2218, SAME_NAMESPACED);
/*  857 */     fixerUpper.addFixer(new ForcePoiRebuild(v2218, false));
/*      */     
/*  859 */     Schema v2501 = fixerUpper.addSchema(2501, net.minecraft.util.datafix.schemas.V2501::new);
/*  860 */     fixerUpper.addFixer(new FurnaceRecipeFix(v2501, true));
/*      */     
/*  862 */     Schema v2502 = fixerUpper.addSchema(2502, net.minecraft.util.datafix.schemas.V2502::new);
/*  863 */     fixerUpper.addFixer(new AddNewChoices(v2502, "Added Hoglin", References.ENTITY));
/*      */     
/*  865 */     Schema v2503 = fixerUpper.addSchema(2503, SAME_NAMESPACED);
/*  866 */     fixerUpper.addFixer(new WallPropertyFix(v2503, false));
/*  867 */     fixerUpper.addFixer(new AdvancementsRenameFix(v2503, false, "Composter category change", createRenamer("minecraft:recipes/misc/composter", "minecraft:recipes/decorations/composter")));
/*      */     
/*  869 */     Schema v2505 = fixerUpper.addSchema(2505, net.minecraft.util.datafix.schemas.V2505::new);
/*  870 */     fixerUpper.addFixer(new AddNewChoices(v2505, "Added Piglin", References.ENTITY));
/*  871 */     fixerUpper.addFixer(new MemoryExpiryDataFix(v2505, "minecraft:villager"));
/*      */     
/*  873 */     Schema v2508 = fixerUpper.addSchema(2508, SAME_NAMESPACED);
/*  874 */     fixerUpper.addFixer(ItemRenameFix.create(v2508, "Renamed fungi items to fungus", createRenamer(ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
/*      */ 
/*      */ 
/*      */     
/*  878 */     fixerUpper.addFixer(BlockRenameFix.create(v2508, "Renamed fungi blocks to fungus", createRenamer(ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  883 */     Schema v2509 = fixerUpper.addSchema(2509, net.minecraft.util.datafix.schemas.V2509::new);
/*  884 */     fixerUpper.addFixer(new EntityZombifiedPiglinRenameFix(v2509));
/*  885 */     fixerUpper.addFixer(ItemRenameFix.create(v2509, "Rename zombie pigman egg item", createRenamer(EntityZombifiedPiglinRenameFix.RENAMED_IDS)));
/*      */     
/*  887 */     Schema v2511 = fixerUpper.addSchema(2511, SAME_NAMESPACED);
/*  888 */     fixerUpper.addFixer(new EntityProjectileOwnerFix(v2511));
/*      */     
/*  890 */     Schema v2511_1 = fixerUpper.addSchema(2511, 1, net.minecraft.util.datafix.schemas.V2511_1::new);
/*  891 */     fixerUpper.addFixer(new NamedEntityConvertUncheckedFix(v2511_1, "SplashPotionItemFieldRenameFix", References.ENTITY, "minecraft:potion"));
/*      */     
/*  893 */     Schema v2514 = fixerUpper.addSchema(2514, SAME_NAMESPACED);
/*  894 */     fixerUpper.addFixer(new EntityUUIDFix(v2514));
/*  895 */     fixerUpper.addFixer(new BlockEntityUUIDFix(v2514));
/*  896 */     fixerUpper.addFixer(new PlayerUUIDFix(v2514));
/*  897 */     fixerUpper.addFixer(new LevelUUIDFix(v2514));
/*  898 */     fixerUpper.addFixer(new SavedDataUUIDFix(v2514));
/*  899 */     fixerUpper.addFixer(new ItemStackUUIDFix(v2514));
/*      */     
/*  901 */     Schema v2516 = fixerUpper.addSchema(2516, SAME_NAMESPACED);
/*  902 */     fixerUpper.addFixer(new GossipUUIDFix(v2516, "minecraft:villager"));
/*  903 */     fixerUpper.addFixer(new GossipUUIDFix(v2516, "minecraft:zombie_villager"));
/*      */     
/*  905 */     Schema v2518 = fixerUpper.addSchema(2518, SAME_NAMESPACED);
/*  906 */     fixerUpper.addFixer(new JigsawPropertiesFix(v2518, false));
/*  907 */     fixerUpper.addFixer(new JigsawRotationFix(v2518));
/*      */     
/*  909 */     Schema v2519 = fixerUpper.addSchema(2519, net.minecraft.util.datafix.schemas.V2519::new);
/*  910 */     fixerUpper.addFixer(new AddNewChoices(v2519, "Added Strider", References.ENTITY));
/*      */     
/*  912 */     Schema v2522 = fixerUpper.addSchema(2522, net.minecraft.util.datafix.schemas.V2522::new);
/*  913 */     fixerUpper.addFixer(new AddNewChoices(v2522, "Added Zoglin", References.ENTITY));
/*      */     
/*  915 */     Schema v2523 = fixerUpper.addSchema(2523, SAME_NAMESPACED);
/*  916 */     fixerUpper.addFixer(new AttributesRenameLegacy(v2523, "Attribute renames", createRenamerNoNamespace(ImmutableMap.builder()
/*  917 */             .put("generic.maxHealth", "minecraft:generic.max_health")
/*  918 */             .put("Max Health", "minecraft:generic.max_health")
/*      */             
/*  920 */             .put("zombie.spawnReinforcements", "minecraft:zombie.spawn_reinforcements")
/*  921 */             .put("Spawn Reinforcements Chance", "minecraft:zombie.spawn_reinforcements")
/*      */             
/*  923 */             .put("horse.jumpStrength", "minecraft:horse.jump_strength")
/*  924 */             .put("Jump Strength", "minecraft:horse.jump_strength")
/*      */             
/*  926 */             .put("generic.followRange", "minecraft:generic.follow_range")
/*  927 */             .put("Follow Range", "minecraft:generic.follow_range")
/*      */             
/*  929 */             .put("generic.knockbackResistance", "minecraft:generic.knockback_resistance")
/*  930 */             .put("Knockback Resistance", "minecraft:generic.knockback_resistance")
/*      */             
/*  932 */             .put("generic.movementSpeed", "minecraft:generic.movement_speed")
/*  933 */             .put("Movement Speed", "minecraft:generic.movement_speed")
/*      */             
/*  935 */             .put("generic.flyingSpeed", "minecraft:generic.flying_speed")
/*  936 */             .put("Flying Speed", "minecraft:generic.flying_speed")
/*      */             
/*  938 */             .put("generic.attackDamage", "minecraft:generic.attack_damage")
/*  939 */             .put("generic.attackKnockback", "minecraft:generic.attack_knockback")
/*  940 */             .put("generic.attackSpeed", "minecraft:generic.attack_speed")
/*  941 */             .put("generic.armorToughness", "minecraft:generic.armor_toughness")
/*  942 */             .build())));
/*      */ 
/*      */     
/*  945 */     Schema v2527 = fixerUpper.addSchema(2527, SAME_NAMESPACED);
/*  946 */     fixerUpper.addFixer(new BitStorageAlignFix(v2527));
/*      */     
/*  948 */     Schema v2528 = fixerUpper.addSchema(2528, SAME_NAMESPACED);
/*  949 */     fixerUpper.addFixer(ItemRenameFix.create(v2528, "Rename soul fire torch and soul fire lantern", createRenamer(ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
/*      */ 
/*      */ 
/*      */     
/*  953 */     fixerUpper.addFixer(BlockRenameFix.create(v2528, "Rename soul fire torch and soul fire lantern", createRenamer(ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_wall_torch", "minecraft:soul_wall_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  959 */     Schema v2529 = fixerUpper.addSchema(2529, SAME_NAMESPACED);
/*  960 */     fixerUpper.addFixer(new StriderGravityFix(v2529, false));
/*      */     
/*  962 */     Schema v2531 = fixerUpper.addSchema(2531, SAME_NAMESPACED);
/*  963 */     fixerUpper.addFixer(new RedstoneWireConnectionsFix(v2531));
/*      */     
/*  965 */     Schema v2533 = fixerUpper.addSchema(2533, SAME_NAMESPACED);
/*  966 */     fixerUpper.addFixer(new VillagerFollowRangeFix(v2533));
/*      */     
/*  968 */     Schema v2535 = fixerUpper.addSchema(2535, SAME_NAMESPACED);
/*  969 */     fixerUpper.addFixer(new EntityShulkerRotationFix(v2535));
/*      */ 
/*      */     
/*  972 */     Schema v2537 = fixerUpper.addSchema(2537, SAME_NAMESPACED);
/*  973 */     fixerUpper.addFixer(new LegacyDimensionIdFix(v2537));
/*      */     
/*  975 */     Schema v2538 = fixerUpper.addSchema(2538, SAME_NAMESPACED);
/*  976 */     fixerUpper.addFixer(new LevelLegacyWorldGenSettingsFix(v2538));
/*      */     
/*  978 */     Schema v2550 = fixerUpper.addSchema(2550, SAME_NAMESPACED);
/*  979 */     fixerUpper.addFixer(new WorldGenSettingsFix(v2550));
/*      */     
/*  981 */     Schema v2551 = fixerUpper.addSchema(2551, net.minecraft.util.datafix.schemas.V2551::new);
/*  982 */     fixerUpper.addFixer(new WriteAndReadFix(v2551, "add types to WorldGenData", References.WORLD_GEN_SETTINGS));
/*      */     
/*  984 */     Schema v2552 = fixerUpper.addSchema(2552, SAME_NAMESPACED);
/*  985 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v2552, "Nether biome rename", References.BIOME, createRenamer("minecraft:nether", "minecraft:nether_wastes")));
/*      */ 
/*      */     
/*  988 */     Schema v2553 = fixerUpper.addSchema(2553, SAME_NAMESPACED);
/*  989 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v2553, "Biomes fix", References.BIOME, createRenamer(BiomeFix.BIOMES)));
/*      */ 
/*      */     
/*  992 */     Schema v2556 = fixerUpper.addSchema(2556, SAME_NAMESPACED);
/*  993 */     fixerUpper.addFixer(new OptionsFancyGraphicsToGraphicsModeFix(v2556));
/*      */     
/*  995 */     Schema v2558 = fixerUpper.addSchema(2558, SAME_NAMESPACED);
/*  996 */     fixerUpper.addFixer(new MissingDimensionFix(v2558, false));
/*  997 */     fixerUpper.addFixer(new OptionsRenameFieldFix(v2558, false, "Rename swapHands setting", "key_key.swapHands", "key_key.swapOffhand"));
/*      */     
/*  999 */     Schema v2568 = fixerUpper.addSchema(2568, net.minecraft.util.datafix.schemas.V2568::new);
/* 1000 */     fixerUpper.addFixer(new AddNewChoices(v2568, "Added Piglin Brute", References.ENTITY));
/*      */     
/* 1002 */     Schema v2571 = fixerUpper.addSchema(2571, net.minecraft.util.datafix.schemas.V2571::new);
/* 1003 */     fixerUpper.addFixer(new AddNewChoices(v2571, "Added Goat", References.ENTITY));
/*      */     
/* 1005 */     Schema v2679 = fixerUpper.addSchema(2679, SAME_NAMESPACED);
/* 1006 */     fixerUpper.addFixer(new CauldronRenameFix(v2679, false));
/*      */     
/* 1008 */     Schema v2680 = fixerUpper.addSchema(2680, SAME_NAMESPACED);
/* 1009 */     fixerUpper.addFixer(ItemRenameFix.create(v2680, "Renamed grass path item to dirt path", createRenamer("minecraft:grass_path", "minecraft:dirt_path")));
/* 1010 */     fixerUpper.addFixer(BlockRenameFix.create(v2680, "Renamed grass path block to dirt path", createRenamer("minecraft:grass_path", "minecraft:dirt_path")));
/*      */     
/* 1012 */     Schema v2684 = fixerUpper.addSchema(2684, net.minecraft.util.datafix.schemas.V2684::new);
/* 1013 */     fixerUpper.addFixer(new AddNewChoices(v2684, "Added Sculk Sensor", References.BLOCK_ENTITY));
/*      */     
/* 1015 */     Schema v2686 = fixerUpper.addSchema(2686, net.minecraft.util.datafix.schemas.V2686::new);
/* 1016 */     fixerUpper.addFixer(new AddNewChoices(v2686, "Added Axolotl", References.ENTITY));
/*      */     
/* 1018 */     Schema v2688 = fixerUpper.addSchema(2688, net.minecraft.util.datafix.schemas.V2688::new);
/* 1019 */     fixerUpper.addFixer(new AddNewChoices(v2688, "Added Glow Squid", References.ENTITY));
/* 1020 */     fixerUpper.addFixer(new AddNewChoices(v2688, "Added Glow Item Frame", References.ENTITY));
/*      */     
/* 1022 */     Schema v2690 = fixerUpper.addSchema(2690, SAME_NAMESPACED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1044 */     ImmutableMap<String, String> copperBlocksToRename = ImmutableMap.builder().put("minecraft:weathered_copper_block", "minecraft:oxidized_copper_block").put("minecraft:semi_weathered_copper_block", "minecraft:weathered_copper_block").put("minecraft:lightly_weathered_copper_block", "minecraft:exposed_copper_block").put("minecraft:weathered_cut_copper", "minecraft:oxidized_cut_copper").put("minecraft:semi_weathered_cut_copper", "minecraft:weathered_cut_copper").put("minecraft:lightly_weathered_cut_copper", "minecraft:exposed_cut_copper").put("minecraft:weathered_cut_copper_stairs", "minecraft:oxidized_cut_copper_stairs").put("minecraft:semi_weathered_cut_copper_stairs", "minecraft:weathered_cut_copper_stairs").put("minecraft:lightly_weathered_cut_copper_stairs", "minecraft:exposed_cut_copper_stairs").put("minecraft:weathered_cut_copper_slab", "minecraft:oxidized_cut_copper_slab").put("minecraft:semi_weathered_cut_copper_slab", "minecraft:weathered_cut_copper_slab").put("minecraft:lightly_weathered_cut_copper_slab", "minecraft:exposed_cut_copper_slab").put("minecraft:waxed_semi_weathered_copper", "minecraft:waxed_weathered_copper").put("minecraft:waxed_lightly_weathered_copper", "minecraft:waxed_exposed_copper").put("minecraft:waxed_semi_weathered_cut_copper", "minecraft:waxed_weathered_cut_copper").put("minecraft:waxed_lightly_weathered_cut_copper", "minecraft:waxed_exposed_cut_copper").put("minecraft:waxed_semi_weathered_cut_copper_stairs", "minecraft:waxed_weathered_cut_copper_stairs").put("minecraft:waxed_lightly_weathered_cut_copper_stairs", "minecraft:waxed_exposed_cut_copper_stairs").put("minecraft:waxed_semi_weathered_cut_copper_slab", "minecraft:waxed_weathered_cut_copper_slab").put("minecraft:waxed_lightly_weathered_cut_copper_slab", "minecraft:waxed_exposed_cut_copper_slab").build();
/*      */     
/* 1046 */     fixerUpper.addFixer(ItemRenameFix.create(v2690, "Renamed copper block items to new oxidized terms", createRenamer(copperBlocksToRename)));
/* 1047 */     fixerUpper.addFixer(BlockRenameFix.create(v2690, "Renamed copper blocks to new oxidized terms", createRenamer(copperBlocksToRename)));
/*      */     
/* 1049 */     Schema v2691 = fixerUpper.addSchema(2691, SAME_NAMESPACED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1055 */     ImmutableMap<String, String> waxedCopperRename = ImmutableMap.builder().put("minecraft:waxed_copper", "minecraft:waxed_copper_block").put("minecraft:oxidized_copper_block", "minecraft:oxidized_copper").put("minecraft:weathered_copper_block", "minecraft:weathered_copper").put("minecraft:exposed_copper_block", "minecraft:exposed_copper").build();
/*      */     
/* 1057 */     fixerUpper.addFixer(ItemRenameFix.create(v2691, "Rename copper item suffixes", createRenamer(waxedCopperRename)));
/* 1058 */     fixerUpper.addFixer(BlockRenameFix.create(v2691, "Rename copper blocks suffixes", createRenamer(waxedCopperRename)));
/*      */     
/* 1060 */     Schema v2693 = fixerUpper.addSchema(2693, SAME_NAMESPACED);
/* 1061 */     fixerUpper.addFixer(new AddFlagIfNotPresentFix(v2693, References.WORLD_GEN_SETTINGS, "has_increased_height_already", false));
/*      */     
/* 1063 */     Schema v2696 = fixerUpper.addSchema(2696, SAME_NAMESPACED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1082 */     ImmutableMap<String, String> grimstoneBlocksToRename = ImmutableMap.builder().put("minecraft:grimstone", "minecraft:deepslate").put("minecraft:grimstone_slab", "minecraft:cobbled_deepslate_slab").put("minecraft:grimstone_stairs", "minecraft:cobbled_deepslate_stairs").put("minecraft:grimstone_wall", "minecraft:cobbled_deepslate_wall").put("minecraft:polished_grimstone", "minecraft:polished_deepslate").put("minecraft:polished_grimstone_slab", "minecraft:polished_deepslate_slab").put("minecraft:polished_grimstone_stairs", "minecraft:polished_deepslate_stairs").put("minecraft:polished_grimstone_wall", "minecraft:polished_deepslate_wall").put("minecraft:grimstone_tiles", "minecraft:deepslate_tiles").put("minecraft:grimstone_tile_slab", "minecraft:deepslate_tile_slab").put("minecraft:grimstone_tile_stairs", "minecraft:deepslate_tile_stairs").put("minecraft:grimstone_tile_wall", "minecraft:deepslate_tile_wall").put("minecraft:grimstone_bricks", "minecraft:deepslate_bricks").put("minecraft:grimstone_brick_slab", "minecraft:deepslate_brick_slab").put("minecraft:grimstone_brick_stairs", "minecraft:deepslate_brick_stairs").put("minecraft:grimstone_brick_wall", "minecraft:deepslate_brick_wall").put("minecraft:chiseled_grimstone", "minecraft:chiseled_deepslate").build();
/*      */     
/* 1084 */     fixerUpper.addFixer(ItemRenameFix.create(v2696, "Renamed grimstone block items to deepslate", createRenamer(grimstoneBlocksToRename)));
/* 1085 */     fixerUpper.addFixer(BlockRenameFix.create(v2696, "Renamed grimstone blocks to deepslate", createRenamer(grimstoneBlocksToRename)));
/*      */     
/* 1087 */     Schema v2700 = fixerUpper.addSchema(2700, SAME_NAMESPACED);
/* 1088 */     fixerUpper.addFixer(BlockRenameFix.create(v2700, "Renamed cave vines blocks", createRenamer(ImmutableMap.of("minecraft:cave_vines_head", "minecraft:cave_vines", "minecraft:cave_vines_body", "minecraft:cave_vines_plant"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1093 */     Schema v2701 = fixerUpper.addSchema(2701, SAME_NAMESPACED);
/* 1094 */     fixerUpper.addFixer(new SavedDataFeaturePoolElementFix(v2701));
/*      */     
/* 1096 */     Schema v2702 = fixerUpper.addSchema(2702, SAME_NAMESPACED);
/* 1097 */     fixerUpper.addFixer(new AbstractArrowPickupFix(v2702));
/*      */     
/* 1099 */     Schema v2704 = fixerUpper.addSchema(2704, net.minecraft.util.datafix.schemas.V2704::new);
/* 1100 */     fixerUpper.addFixer(new AddNewChoices(v2704, "Added Goat", References.ENTITY));
/*      */     
/* 1102 */     Schema v2707 = fixerUpper.addSchema(2707, net.minecraft.util.datafix.schemas.V2707::new);
/* 1103 */     fixerUpper.addFixer(new AddNewChoices(v2707, "Added Marker", References.ENTITY));
/* 1104 */     fixerUpper.addFixer(new AddFlagIfNotPresentFix(v2707, References.WORLD_GEN_SETTINGS, "has_increased_height_already", true));
/*      */     
/* 1106 */     Schema v2710 = fixerUpper.addSchema(2710, SAME_NAMESPACED);
/* 1107 */     fixerUpper.addFixer(new StatsRenameFix(v2710, "Renamed play_one_minute stat to play_time", ImmutableMap.of("minecraft:play_one_minute", "minecraft:play_time")));
/*      */     
/* 1109 */     Schema v2717 = fixerUpper.addSchema(2717, SAME_NAMESPACED);
/* 1110 */     fixerUpper.addFixer(ItemRenameFix.create(v2717, "Rename azalea_leaves_flowers", createRenamer(ImmutableMap.of("minecraft:azalea_leaves_flowers", "minecraft:flowering_azalea_leaves"))));
/*      */ 
/*      */     
/* 1113 */     fixerUpper.addFixer(BlockRenameFix.create(v2717, "Rename azalea_leaves_flowers items", createRenamer(ImmutableMap.of("minecraft:azalea_leaves_flowers", "minecraft:flowering_azalea_leaves"))));
/*      */ 
/*      */ 
/*      */     
/* 1117 */     Schema v2825 = fixerUpper.addSchema(2825, SAME_NAMESPACED);
/* 1118 */     fixerUpper.addFixer(new AddFlagIfNotPresentFix(v2825, References.WORLD_GEN_SETTINGS, "has_increased_height_already", false));
/*      */     
/* 1120 */     Schema v2831 = fixerUpper.addSchema(2831, net.minecraft.util.datafix.schemas.V2831::new);
/* 1121 */     fixerUpper.addFixer(new SpawnerDataFix(v2831));
/*      */     
/* 1123 */     Schema v2832 = fixerUpper.addSchema(2832, net.minecraft.util.datafix.schemas.V2832::new);
/* 1124 */     fixerUpper.addFixer(new WorldGenSettingsHeightAndBiomeFix(v2832));
/* 1125 */     fixerUpper.addFixer(new ChunkHeightAndBiomeFix(v2832));
/*      */     
/* 1127 */     Schema v2833 = fixerUpper.addSchema(2833, SAME_NAMESPACED);
/* 1128 */     fixerUpper.addFixer(new WorldGenSettingsDisallowOldCustomWorldsFix(v2833));
/*      */     
/* 1130 */     Schema v2838 = fixerUpper.addSchema(2838, SAME_NAMESPACED);
/* 1131 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v2838, "Caves and Cliffs biome renames", References.BIOME, createRenamer(CavesAndCliffsRenames.RENAMES)));
/*      */     
/* 1133 */     Schema v2841 = fixerUpper.addSchema(2841, SAME_NAMESPACED);
/* 1134 */     fixerUpper.addFixer(new ChunkProtoTickListFix(v2841));
/*      */     
/* 1136 */     Schema v2842 = fixerUpper.addSchema(2842, net.minecraft.util.datafix.schemas.V2842::new);
/* 1137 */     fixerUpper.addFixer(new ChunkRenamesFix(v2842));
/*      */     
/* 1139 */     Schema v2843 = fixerUpper.addSchema(2843, SAME_NAMESPACED);
/* 1140 */     fixerUpper.addFixer(new OverreachingTickFix(v2843));
/* 1141 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v2843, "Remove Deep Warm Ocean", References.BIOME, createRenamer("minecraft:deep_warm_ocean", "minecraft:warm_ocean")));
/*      */     
/* 1143 */     Schema v2846 = fixerUpper.addSchema(2846, SAME_NAMESPACED);
/* 1144 */     fixerUpper.addFixer(new AdvancementsRenameFix(v2846, false, "Rename some C&C part 2 advancements", createRenamer(ImmutableMap.of("minecraft:husbandry/play_jukebox_in_meadows", "minecraft:adventure/play_jukebox_in_meadows", "minecraft:adventure/caves_and_cliff", "minecraft:adventure/fall_from_world_height", "minecraft:adventure/ride_strider_in_overworld_lava", "minecraft:nether/ride_strider_in_overworld_lava"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1150 */     Schema v2852 = fixerUpper.addSchema(2852, SAME_NAMESPACED);
/* 1151 */     fixerUpper.addFixer(new WorldGenSettingsDisallowOldCustomWorldsFix(v2852));
/*      */     
/* 1153 */     Schema v2967 = fixerUpper.addSchema(2967, SAME_NAMESPACED);
/* 1154 */     fixerUpper.addFixer(new StructureSettingsFlattenFix(v2967));
/*      */     
/* 1156 */     Schema v2970 = fixerUpper.addSchema(2970, SAME_NAMESPACED);
/* 1157 */     fixerUpper.addFixer(new StructuresBecomeConfiguredFix(v2970));
/*      */     
/* 1159 */     Schema v3076 = fixerUpper.addSchema(3076, net.minecraft.util.datafix.schemas.V3076::new);
/* 1160 */     fixerUpper.addFixer(new AddNewChoices(v3076, "Added Sculk Catalyst", References.BLOCK_ENTITY));
/*      */     
/* 1162 */     Schema v3077 = fixerUpper.addSchema(3077, SAME_NAMESPACED);
/* 1163 */     fixerUpper.addFixer(new ChunkDeleteIgnoredLightDataFix(v3077));
/*      */     
/* 1165 */     Schema v3078 = fixerUpper.addSchema(3078, net.minecraft.util.datafix.schemas.V3078::new);
/* 1166 */     fixerUpper.addFixer(new AddNewChoices(v3078, "Added Frog", References.ENTITY));
/* 1167 */     fixerUpper.addFixer(new AddNewChoices(v3078, "Added Tadpole", References.ENTITY));
/* 1168 */     fixerUpper.addFixer(new AddNewChoices(v3078, "Added Sculk Shrieker", References.BLOCK_ENTITY));
/*      */     
/* 1170 */     Schema v3081 = fixerUpper.addSchema(3081, net.minecraft.util.datafix.schemas.V3081::new);
/* 1171 */     fixerUpper.addFixer(new AddNewChoices(v3081, "Added Warden", References.ENTITY));
/*      */     
/* 1173 */     Schema v3082 = fixerUpper.addSchema(3082, net.minecraft.util.datafix.schemas.V3082::new);
/* 1174 */     fixerUpper.addFixer(new AddNewChoices(v3082, "Added Chest Boat", References.ENTITY));
/*      */     
/* 1176 */     Schema v3083 = fixerUpper.addSchema(3083, net.minecraft.util.datafix.schemas.V3083::new);
/* 1177 */     fixerUpper.addFixer(new AddNewChoices(v3083, "Added Allay", References.ENTITY));
/*      */     
/* 1179 */     Schema v3084 = fixerUpper.addSchema(3084, SAME_NAMESPACED);
/* 1180 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v3084, "game_event_renames_3084", References.GAME_EVENT_NAME, createRenamer(ImmutableMap.builder()
/* 1181 */             .put("minecraft:block_press", "minecraft:block_activate")
/* 1182 */             .put("minecraft:block_switch", "minecraft:block_activate")
/* 1183 */             .put("minecraft:block_unpress", "minecraft:block_deactivate")
/* 1184 */             .put("minecraft:block_unswitch", "minecraft:block_deactivate")
/* 1185 */             .put("minecraft:drinking_finish", "minecraft:drink")
/* 1186 */             .put("minecraft:elytra_free_fall", "minecraft:elytra_glide")
/* 1187 */             .put("minecraft:entity_damaged", "minecraft:entity_damage")
/* 1188 */             .put("minecraft:entity_dying", "minecraft:entity_die")
/* 1189 */             .put("minecraft:entity_killed", "minecraft:entity_die")
/* 1190 */             .put("minecraft:mob_interact", "minecraft:entity_interact")
/* 1191 */             .put("minecraft:ravager_roar", "minecraft:entity_roar")
/* 1192 */             .put("minecraft:ring_bell", "minecraft:block_change")
/* 1193 */             .put("minecraft:shulker_close", "minecraft:container_close")
/* 1194 */             .put("minecraft:shulker_open", "minecraft:container_open")
/* 1195 */             .put("minecraft:wolf_shaking", "minecraft:entity_shake")
/* 1196 */             .build())));
/*      */ 
/*      */     
/* 1199 */     Schema v3086 = fixerUpper.addSchema(3086, SAME_NAMESPACED);
/* 1200 */     Objects.requireNonNull((Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), m -> {
/* 1201 */             m.defaultReturnValue("minecraft:tabby");
/* 1202 */             m.put(0, "minecraft:tabby");
/* 1203 */             m.put(1, "minecraft:black");
/* 1204 */             m.put(2, "minecraft:red");
/* 1205 */             m.put(3, "minecraft:siamese");
/* 1206 */             m.put(4, "minecraft:british");
/* 1207 */             m.put(5, "minecraft:calico");
/* 1208 */             m.put(6, "minecraft:persian");
/* 1209 */             m.put(7, "minecraft:ragdoll");
/* 1210 */             m.put(8, "minecraft:white");
/* 1211 */             m.put(9, "minecraft:jellie");
/* 1212 */             m.put(10, "minecraft:all_black"); })); fixerUpper.addFixer(new EntityVariantFix(v3086, "Change cat variant type", References.ENTITY, "minecraft:cat", "CatType", (Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), m -> { m.defaultReturnValue("minecraft:tabby"); m.put(0, "minecraft:tabby"); m.put(1, "minecraft:black"); m.put(2, "minecraft:red"); m.put(3, "minecraft:siamese"); m.put(4, "minecraft:british"); m.put(5, "minecraft:calico"); m.put(6, "minecraft:persian"); m.put(7, "minecraft:ragdoll"); m.put(8, "minecraft:white"); m.put(9, "minecraft:jellie"); m.put(10, "minecraft:all_black");
/*      */             })::get));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1227 */     ImmutableMap<String, String> catAdvancementMigrationMap = ImmutableMap.builder().put("textures/entity/cat/tabby.png", "minecraft:tabby").put("textures/entity/cat/black.png", "minecraft:black").put("textures/entity/cat/red.png", "minecraft:red").put("textures/entity/cat/siamese.png", "minecraft:siamese").put("textures/entity/cat/british_shorthair.png", "minecraft:british").put("textures/entity/cat/calico.png", "minecraft:calico").put("textures/entity/cat/persian.png", "minecraft:persian").put("textures/entity/cat/ragdoll.png", "minecraft:ragdoll").put("textures/entity/cat/white.png", "minecraft:white").put("textures/entity/cat/jellie.png", "minecraft:jellie").put("textures/entity/cat/all_black.png", "minecraft:all_black").build();
/* 1228 */     fixerUpper.addFixer(new CriteriaRenameFix(v3086, "Migrate cat variant advancement", "minecraft:husbandry/complete_catalogue", s -> (String)catAdvancementMigrationMap.getOrDefault(s, s)));
/*      */     
/* 1230 */     Schema v3087 = fixerUpper.addSchema(3087, SAME_NAMESPACED);
/* 1231 */     Objects.requireNonNull((Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), m -> {
/* 1232 */             m.put(0, "minecraft:temperate");
/* 1233 */             m.put(1, "minecraft:warm");
/* 1234 */             m.put(2, "minecraft:cold"); })); fixerUpper.addFixer(new EntityVariantFix(v3087, "Change frog variant type", References.ENTITY, "minecraft:frog", "Variant", (Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), m -> { m.put(0, "minecraft:temperate"); m.put(1, "minecraft:warm"); m.put(2, "minecraft:cold");
/*      */             })::get));
/*      */     
/* 1237 */     Schema v3090 = fixerUpper.addSchema(3090, SAME_NAMESPACED);
/* 1238 */     fixerUpper.addFixer(new EntityFieldsRenameFix(v3090, "EntityPaintingFieldsRenameFix", "minecraft:painting", 
/* 1239 */           Map.of("Motive", "variant", "Facing", "facing")));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1245 */     Schema v3093 = fixerUpper.addSchema(3093, SAME_NAMESPACED);
/* 1246 */     fixerUpper.addFixer(new EntityGoatMissingStateFix(v3093));
/*      */     
/* 1248 */     Schema v3094 = fixerUpper.addSchema(3094, SAME_NAMESPACED);
/* 1249 */     fixerUpper.addFixer(new GoatHornIdFix(v3094));
/*      */     
/* 1251 */     Schema v3097 = fixerUpper.addSchema(3097, SAME_NAMESPACED);
/* 1252 */     fixerUpper.addFixer(new FilteredBooksFix(v3097));
/* 1253 */     fixerUpper.addFixer(new FilteredSignsFix(v3097));
/* 1254 */     Map<String, String> renamedCatCriteria = Map.of("minecraft:british", "minecraft:british_shorthair");
/* 1255 */     fixerUpper.addFixer(new VariantRenameFix(v3097, "Rename british shorthair", References.ENTITY, "minecraft:cat", renamedCatCriteria));
/* 1256 */     fixerUpper.addFixer(new CriteriaRenameFix(v3097, "Migrate cat variant advancement for british shorthair", "minecraft:husbandry/complete_catalogue", s -> (String)renamedCatCriteria.getOrDefault(s, s)));
/* 1257 */     Objects.requireNonNull(Set.of("minecraft:unemployed", "minecraft:nitwit")); fixerUpper.addFixer(new PoiTypeRemoveFix(v3097, "Remove unpopulated villager PoI types", Set.of("minecraft:unemployed", "minecraft:nitwit")::contains));
/*      */     
/* 1259 */     Schema v3108 = fixerUpper.addSchema(3108, SAME_NAMESPACED);
/* 1260 */     fixerUpper.addFixer(new BlendingDataRemoveFromNetherEndFix(v3108));
/*      */     
/* 1262 */     Schema v3201 = fixerUpper.addSchema(3201, SAME_NAMESPACED);
/* 1263 */     fixerUpper.addFixer(new OptionsProgrammerArtFix(v3201));
/*      */     
/* 1265 */     Schema v3202 = fixerUpper.addSchema(3202, net.minecraft.util.datafix.schemas.V3202::new);
/* 1266 */     fixerUpper.addFixer(new AddNewChoices(v3202, "Added Hanging Sign", References.BLOCK_ENTITY));
/*      */     
/* 1268 */     Schema v3203 = fixerUpper.addSchema(3203, net.minecraft.util.datafix.schemas.V3203::new);
/* 1269 */     fixerUpper.addFixer(new AddNewChoices(v3203, "Added Camel", References.ENTITY));
/*      */     
/* 1271 */     Schema v3204 = fixerUpper.addSchema(3204, net.minecraft.util.datafix.schemas.V3204::new);
/* 1272 */     fixerUpper.addFixer(new AddNewChoices(v3204, "Added Chiseled Bookshelf", References.BLOCK_ENTITY));
/*      */     
/* 1274 */     Schema v3209 = fixerUpper.addSchema(3209, SAME_NAMESPACED);
/*      */     
/* 1276 */     fixerUpper.addFixer(new ItemStackSpawnEggFix(v3209, false, "minecraft:pig_spawn_egg"));
/*      */     
/* 1278 */     Schema v3214 = fixerUpper.addSchema(3214, SAME_NAMESPACED);
/* 1279 */     fixerUpper.addFixer(new OptionsAmbientOcclusionFix(v3214));
/*      */     
/* 1281 */     Schema v3319 = fixerUpper.addSchema(3319, SAME_NAMESPACED);
/* 1282 */     fixerUpper.addFixer(new OptionsAccessibilityOnboardFix(v3319));
/*      */     
/* 1284 */     Schema v3322 = fixerUpper.addSchema(3322, SAME_NAMESPACED);
/* 1285 */     fixerUpper.addFixer(new EffectDurationFix(v3322));
/*      */     
/* 1287 */     Schema v3325 = fixerUpper.addSchema(3325, net.minecraft.util.datafix.schemas.V3325::new);
/* 1288 */     fixerUpper.addFixer(new AddNewChoices(v3325, "Added displays", References.ENTITY));
/*      */     
/* 1290 */     Schema v3326 = fixerUpper.addSchema(3326, net.minecraft.util.datafix.schemas.V3326::new);
/* 1291 */     fixerUpper.addFixer(new AddNewChoices(v3326, "Added Sniffer", References.ENTITY));
/*      */     
/* 1293 */     Schema v3327 = fixerUpper.addSchema(3327, net.minecraft.util.datafix.schemas.V3327::new);
/* 1294 */     fixerUpper.addFixer(new AddNewChoices(v3327, "Archaeology", References.BLOCK_ENTITY));
/*      */     
/* 1296 */     Schema v3328 = fixerUpper.addSchema(3328, net.minecraft.util.datafix.schemas.V3328::new);
/* 1297 */     fixerUpper.addFixer(new AddNewChoices(v3328, "Added interaction", References.ENTITY));
/*      */     
/* 1299 */     Schema v3438 = fixerUpper.addSchema(3438, net.minecraft.util.datafix.schemas.V3438::new);
/* 1300 */     fixerUpper.addFixer(BlockEntityRenameFix.create(v3438, "Rename Suspicious Sand to Brushable Block", createRenamer("minecraft:suspicious_sand", "minecraft:brushable_block")));
/* 1301 */     fixerUpper.addFixer(new EntityBrushableBlockFieldsRenameFix(v3438));
/* 1302 */     fixerUpper.addFixer(ItemRenameFix.create(v3438, "Pottery shard renaming", createRenamer(
/* 1303 */             ImmutableMap.of("minecraft:pottery_shard_archer", "minecraft:archer_pottery_shard", "minecraft:pottery_shard_prize", "minecraft:prize_pottery_shard", "minecraft:pottery_shard_arms_up", "minecraft:arms_up_pottery_shard", "minecraft:pottery_shard_skull", "minecraft:skull_pottery_shard"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1309 */     fixerUpper.addFixer(new AddNewChoices(v3438, "Added calibrated sculk sensor", References.BLOCK_ENTITY));
/*      */     
/* 1311 */     Schema v3439 = fixerUpper.addSchema(3439, net.minecraft.util.datafix.schemas.V3439::new);
/* 1312 */     fixerUpper.addFixer(new BlockEntitySignDoubleSidedEditableTextFix(v3439, "Updated sign text format for Signs", "minecraft:sign"));
/*      */     
/* 1314 */     Schema v3439_1 = fixerUpper.addSchema(3439, 1, net.minecraft.util.datafix.schemas.V3439_1::new);
/* 1315 */     fixerUpper.addFixer(new BlockEntitySignDoubleSidedEditableTextFix(v3439_1, "Updated sign text format for Hanging Signs", "minecraft:hanging_sign"));
/*      */     
/* 1317 */     Schema v3440 = fixerUpper.addSchema(3440, SAME_NAMESPACED);
/* 1318 */     fixerUpper.addFixer(new NamespacedTypeRenameFix(v3440, "Replace experimental 1.20 overworld", References.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, createRenamer("minecraft:overworld_update_1_20", "minecraft:overworld")));
/* 1319 */     fixerUpper.addFixer(new FeatureFlagRemoveFix(v3440, "Remove 1.20 feature toggle", Set.of("minecraft:update_1_20")));
/*      */     
/* 1321 */     Schema v3447 = fixerUpper.addSchema(3447, SAME_NAMESPACED);
/* 1322 */     fixerUpper.addFixer(ItemRenameFix.create(v3447, "Pottery shard item renaming to Pottery sherd", createRenamer(
/* 1323 */             (Map)Stream.of(new String[] {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 "minecraft:angler_pottery_shard", "minecraft:archer_pottery_shard", "minecraft:arms_up_pottery_shard", "minecraft:blade_pottery_shard", "minecraft:brewer_pottery_shard", "minecraft:burn_pottery_shard", "minecraft:danger_pottery_shard", "minecraft:explorer_pottery_shard", "minecraft:friend_pottery_shard", "minecraft:heart_pottery_shard",
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 "minecraft:heartbreak_pottery_shard", "minecraft:howl_pottery_shard", "minecraft:miner_pottery_shard", "minecraft:mourner_pottery_shard", "minecraft:plenty_pottery_shard", "minecraft:prize_pottery_shard", "minecraft:sheaf_pottery_shard", "minecraft:shelter_pottery_shard", "minecraft:skull_pottery_shard", "minecraft:snort_pottery_shard"
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1344 */               }).collect(Collectors.toMap(
/* 1345 */                 Function.identity(), key -> 
/* 1346 */                 key.replace("_pottery_shard", "_pottery_sherd"))))));
/*      */ 
/*      */ 
/*      */     
/* 1350 */     Schema v3448 = fixerUpper.addSchema(3448, net.minecraft.util.datafix.schemas.V3448::new);
/* 1351 */     fixerUpper.addFixer(new DecoratedPotFieldRenameFix(v3448));
/*      */     
/* 1353 */     Schema v3450 = fixerUpper.addSchema(3450, SAME_NAMESPACED);
/* 1354 */     fixerUpper.addFixer(new RemapChunkStatusFix(v3450, "Remove liquid_carvers and heightmap chunk statuses", createRenamer(Map.of("minecraft:liquid_carvers", "minecraft:carvers", "minecraft:heightmaps", "minecraft:spawn"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1359 */     Schema v3451 = fixerUpper.addSchema(3451, SAME_NAMESPACED);
/* 1360 */     fixerUpper.addFixer(new ChunkDeleteLightFix(v3451));
/*      */     
/* 1362 */     Schema v3459 = fixerUpper.addSchema(3459, SAME_NAMESPACED);
/* 1363 */     fixerUpper.addFixer(new LegacyDragonFightFix(v3459));
/*      */     
/* 1365 */     Schema v3564 = fixerUpper.addSchema(3564, SAME_NAMESPACED);
/* 1366 */     fixerUpper.addFixer(new DropInvalidSignDataFix(v3564, "minecraft:sign"));
/*      */     
/* 1368 */     Schema v3564_1 = fixerUpper.addSchema(3564, 1, SAME_NAMESPACED);
/* 1369 */     fixerUpper.addFixer(new DropInvalidSignDataFix(v3564_1, "minecraft:hanging_sign"));
/*      */     
/* 1371 */     Schema v3565 = fixerUpper.addSchema(3565, SAME_NAMESPACED);
/* 1372 */     fixerUpper.addFixer(new RandomSequenceSettingsFix(v3565));
/*      */     
/* 1374 */     Schema v3566 = fixerUpper.addSchema(3566, SAME_NAMESPACED);
/* 1375 */     fixerUpper.addFixer(new ScoreboardDisplaySlotFix(v3566));
/*      */     
/* 1377 */     Schema v3568 = fixerUpper.addSchema(3568, SAME_NAMESPACED);
/* 1378 */     fixerUpper.addFixer(new MobEffectIdFix(v3568));
/*      */     
/* 1380 */     Schema v3682 = fixerUpper.addSchema(3682, net.minecraft.util.datafix.schemas.V3682::new);
/* 1381 */     fixerUpper.addFixer(new AddNewChoices(v3682, "Added Crafter", References.BLOCK_ENTITY));
/*      */     
/* 1383 */     Schema v3683 = fixerUpper.addSchema(3683, net.minecraft.util.datafix.schemas.V3683::new);
/* 1384 */     fixerUpper.addFixer(new PrimedTntBlockStateFixer(v3683));
/*      */     
/* 1386 */     Schema v3685 = fixerUpper.addSchema(3685, net.minecraft.util.datafix.schemas.V3685::new);
/* 1387 */     fixerUpper.addFixer(new FixProjectileStoredItem(v3685));
/*      */     
/* 1389 */     Schema v3689 = fixerUpper.addSchema(3689, net.minecraft.util.datafix.schemas.V3689::new);
/* 1390 */     fixerUpper.addFixer(new AddNewChoices(v3689, "Added Breeze", References.ENTITY));
/* 1391 */     fixerUpper.addFixer(new AddNewChoices(v3689, "Added Trial Spawner", References.BLOCK_ENTITY));
/*      */     
/* 1393 */     Schema v3692 = fixerUpper.addSchema(3692, SAME_NAMESPACED);
/* 1394 */     UnaryOperator<String> grassRenamer = createRenamer(Map.of("minecraft:grass", "minecraft:short_grass"));
/* 1395 */     fixerUpper.addFixer(BlockRenameFix.create(v3692, "Rename grass block to short_grass", grassRenamer));
/* 1396 */     fixerUpper.addFixer(ItemRenameFix.create(v3692, "Rename grass item to short_grass", grassRenamer));
/*      */     
/* 1398 */     Schema v3799 = fixerUpper.addSchema(3799, net.minecraft.util.datafix.schemas.V3799::new);
/* 1399 */     fixerUpper.addFixer(new AddNewChoices(v3799, "Added Armadillo", References.ENTITY));
/*      */     
/* 1401 */     Schema v3800 = fixerUpper.addSchema(3800, SAME_NAMESPACED);
/* 1402 */     UnaryOperator<String> scuteRenamer = createRenamer(Map.of("minecraft:scute", "minecraft:turtle_scute"));
/* 1403 */     fixerUpper.addFixer(ItemRenameFix.create(v3800, "Rename scute item to turtle_scute", scuteRenamer));
/*      */     
/* 1405 */     Schema v3803 = fixerUpper.addSchema(3803, SAME_NAMESPACED);
/* 1406 */     fixerUpper.addFixer(new RenameEnchantmentsFix(v3803, "Rename sweeping enchant to sweeping_edge", Map.of("minecraft:sweeping", "minecraft:sweeping_edge")));
/*      */     
/* 1408 */     Schema v3807 = fixerUpper.addSchema(3807, net.minecraft.util.datafix.schemas.V3807::new);
/* 1409 */     fixerUpper.addFixer(new AddNewChoices(v3807, "Added Vault", References.BLOCK_ENTITY));
/*      */     
/* 1411 */     Schema v3807_1 = fixerUpper.addSchema(3807, 1, SAME_NAMESPACED);
/* 1412 */     fixerUpper.addFixer(new MapBannerBlockPosFormatFix(v3807_1));
/*      */     
/* 1414 */     Schema v3808 = fixerUpper.addSchema(3808, net.minecraft.util.datafix.schemas.V3808::new);
/* 1415 */     fixerUpper.addFixer(new HorseBodyArmorItemFix(v3808, "minecraft:horse", "ArmorItem", true));
/*      */     
/* 1417 */     Schema v3808_1 = fixerUpper.addSchema(3808, 1, net.minecraft.util.datafix.schemas.V3808_1::new);
/* 1418 */     fixerUpper.addFixer(new HorseBodyArmorItemFix(v3808_1, "minecraft:llama", "DecorItem", false));
/*      */     
/* 1420 */     Schema v3808_2 = fixerUpper.addSchema(3808, 2, net.minecraft.util.datafix.schemas.V3808_2::new);
/* 1421 */     fixerUpper.addFixer(new HorseBodyArmorItemFix(v3808_2, "minecraft:trader_llama", "DecorItem", false));
/*      */     
/* 1423 */     Schema v3809 = fixerUpper.addSchema(3809, SAME_NAMESPACED);
/* 1424 */     fixerUpper.addFixer(new ChestedHorsesInventoryZeroIndexingFix(v3809));
/*      */     
/* 1426 */     Schema v3812 = fixerUpper.addSchema(3812, SAME_NAMESPACED);
/* 1427 */     fixerUpper.addFixer(new FixWolfHealth(v3812));
/*      */     
/* 1429 */     Schema v3813 = fixerUpper.addSchema(3813, net.minecraft.util.datafix.schemas.V3813::new);
/* 1430 */     fixerUpper.addFixer(new BlockPosFormatAndRenamesFix(v3813));
/*      */     
/* 1432 */     Schema v3814 = fixerUpper.addSchema(3814, SAME_NAMESPACED);
/* 1433 */     fixerUpper.addFixer(new AttributesRenameLegacy(v3814, "Rename jump strength attribute", createRenamer("minecraft:horse.jump_strength", "minecraft:generic.jump_strength")));
/*      */     
/* 1435 */     Schema v3816 = fixerUpper.addSchema(3816, net.minecraft.util.datafix.schemas.V3816::new);
/* 1436 */     fixerUpper.addFixer(new AddNewChoices(v3816, "Added Bogged", References.ENTITY));
/*      */     
/* 1438 */     Schema v3818 = fixerUpper.addSchema(3818, net.minecraft.util.datafix.schemas.V3818::new);
/* 1439 */     fixerUpper.addFixer(new BeehiveFieldRenameFix(v3818));
/* 1440 */     fixerUpper.addFixer(new EmptyItemInHotbarFix(v3818));
/*      */     
/* 1442 */     Schema v3818_1 = fixerUpper.addSchema(3818, 1, SAME_NAMESPACED);
/* 1443 */     fixerUpper.addFixer(new BannerPatternFormatFix(v3818_1));
/*      */     
/* 1445 */     Schema v3818_2 = fixerUpper.addSchema(3818, 2, SAME_NAMESPACED);
/* 1446 */     fixerUpper.addFixer(new TippedArrowPotionToItemFix(v3818_2));
/*      */     
/* 1448 */     Schema v3818_3 = fixerUpper.addSchema(3818, 3, net.minecraft.util.datafix.schemas.V3818_3::new);
/*      */     
/* 1450 */     fixerUpper.addFixer(new WriteAndReadFix(v3818_3, "Inject data component types", References.DATA_COMPONENTS));
/*      */     
/* 1452 */     Schema v3818_4 = fixerUpper.addSchema(3818, 4, net.minecraft.util.datafix.schemas.V3818_4::new);
/* 1453 */     fixerUpper.addFixer(new ParticleUnflatteningFix(v3818_4));
/*      */     
/* 1455 */     Schema v3818_5 = fixerUpper.addSchema(3818, 5, net.minecraft.util.datafix.schemas.V3818_5::new);
/* 1456 */     fixerUpper.addFixer(new ItemStackComponentizationFix(v3818_5));
/*      */     
/* 1458 */     Schema v3818_6 = fixerUpper.addSchema(3818, 6, SAME_NAMESPACED);
/* 1459 */     fixerUpper.addFixer(new AreaEffectCloudPotionFix(v3818_6));
/*      */     
/* 1461 */     Schema v3820 = fixerUpper.addSchema(3820, SAME_NAMESPACED);
/* 1462 */     fixerUpper.addFixer(new PlayerHeadBlockProfileFix(v3820));
/* 1463 */     fixerUpper.addFixer(new LodestoneCompassComponentFix(v3820));
/*      */     
/* 1465 */     Schema v3825 = fixerUpper.addSchema(3825, net.minecraft.util.datafix.schemas.V3825::new);
/* 1466 */     fixerUpper.addFixer(new ItemStackCustomNameToOverrideComponentFix(v3825));
/* 1467 */     fixerUpper.addFixer(new BannerEntityCustomNameToOverrideComponentFix(v3825));
/* 1468 */     fixerUpper.addFixer(new TrialSpawnerConfigFix(v3825));
/* 1469 */     fixerUpper.addFixer(new AddNewChoices(v3825, "Added Ominous Item Spawner", References.ENTITY));
/*      */     
/* 1471 */     Schema v3828 = fixerUpper.addSchema(3828, SAME_NAMESPACED);
/* 1472 */     fixerUpper.addFixer(new EmptyItemInVillagerTradeFix(v3828));
/*      */     
/* 1474 */     Schema v3833 = fixerUpper.addSchema(3833, SAME_NAMESPACED);
/* 1475 */     fixerUpper.addFixer(new RemoveEmptyItemInBrushableBlockFix(v3833));
/*      */     
/* 1477 */     Schema v3938 = fixerUpper.addSchema(3938, net.minecraft.util.datafix.schemas.V3938::new);
/* 1478 */     fixerUpper.addFixer(new ProjectileStoredWeaponFix(v3938));
/*      */     
/* 1480 */     Schema v3939 = fixerUpper.addSchema(3939, SAME_NAMESPACED);
/* 1481 */     fixerUpper.addFixer(new FeatureFlagRemoveFix(v3939, "Remove 1.21 feature toggle", Set.of("minecraft:update_1_21")));
/*      */     
/* 1483 */     Schema v3943 = fixerUpper.addSchema(3943, SAME_NAMESPACED);
/* 1484 */     fixerUpper.addFixer(new OptionsMenuBlurrinessFix(v3943));
/*      */     
/* 1486 */     Schema v3945 = fixerUpper.addSchema(3945, SAME_NAMESPACED);
/* 1487 */     fixerUpper.addFixer(new AttributeModifierIdFix(v3945));
/* 1488 */     fixerUpper.addFixer(new JukeboxTicksSinceSongStartedFix(v3945));
/*      */     
/* 1490 */     Schema v4054 = fixerUpper.addSchema(4054, SAME_NAMESPACED);
/* 1491 */     fixerUpper.addFixer(new OminousBannerRarityFix(v4054));
/*      */     
/* 1493 */     Schema v4055 = fixerUpper.addSchema(4055, SAME_NAMESPACED);
/* 1494 */     fixerUpper.addFixer(new AttributeIdPrefixFix(v4055));
/*      */     
/* 1496 */     Schema v4057 = fixerUpper.addSchema(4057, SAME_NAMESPACED);
/* 1497 */     fixerUpper.addFixer(new CarvingStepRemoveFix(v4057));
/*      */     
/* 1499 */     Schema v4059 = fixerUpper.addSchema(4059, net.minecraft.util.datafix.schemas.V4059::new);
/* 1500 */     fixerUpper.addFixer(new FoodToConsumableFix(v4059));
/*      */     
/* 1502 */     Schema v4061 = fixerUpper.addSchema(4061, SAME_NAMESPACED);
/* 1503 */     fixerUpper.addFixer(new TrialSpawnerConfigInRegistryFix(v4061));
/*      */     
/* 1505 */     Schema v4064 = fixerUpper.addSchema(4064, SAME_NAMESPACED);
/* 1506 */     fixerUpper.addFixer(new FireResistantToDamageResistantComponentFix(v4064));
/*      */     
/* 1508 */     Schema v4067 = fixerUpper.addSchema(4067, net.minecraft.util.datafix.schemas.V4067::new);
/* 1509 */     fixerUpper.addFixer(new BoatSplitFix(v4067));
/* 1510 */     fixerUpper.addFixer(new FeatureFlagRemoveFix(v4067, "Remove Bundle experimental feature flag", Set.of("minecraft:bundle")));
/*      */     
/* 1512 */     Schema v4068 = fixerUpper.addSchema(4068, SAME_NAMESPACED);
/* 1513 */     fixerUpper.addFixer(new LockComponentPredicateFix(v4068));
/* 1514 */     fixerUpper.addFixer(new ContainerBlockEntityLockPredicateFix(v4068));
/*      */     
/* 1516 */     Schema v4070 = fixerUpper.addSchema(4070, net.minecraft.util.datafix.schemas.V4070::new);
/* 1517 */     fixerUpper.addFixer(new AddNewChoices(v4070, "Added Pale Oak Boat and Pale Oak Chest Boat", References.ENTITY));
/*      */     
/* 1519 */     Schema v4071 = fixerUpper.addSchema(4071, net.minecraft.util.datafix.schemas.V4071::new);
/* 1520 */     fixerUpper.addFixer(new AddNewChoices(v4071, "Added Creaking", References.ENTITY));
/* 1521 */     fixerUpper.addFixer(new AddNewChoices(v4071, "Added Creaking Heart", References.BLOCK_ENTITY));
/*      */     
/* 1523 */     Schema v4081 = fixerUpper.addSchema(4081, SAME_NAMESPACED);
/* 1524 */     fixerUpper.addFixer(new EntitySalmonSizeFix(v4081));
/*      */     
/* 1526 */     Schema v4173 = fixerUpper.addSchema(4173, SAME_NAMESPACED);
/* 1527 */     fixerUpper.addFixer(new EntityFieldsRenameFix(v4173, "Rename TNT Minecart fuse", "minecraft:tnt_minecart", 
/* 1528 */           Map.of("TNTFuse", "fuse")));
/*      */ 
/*      */     
/* 1531 */     Schema v4175 = fixerUpper.addSchema(4175, SAME_NAMESPACED);
/* 1532 */     fixerUpper.addFixer(new EquippableAssetRenameFix(v4175));
/* 1533 */     fixerUpper.addFixer(new CustomModelDataExpandFix(v4175));
/*      */     
/* 1535 */     Schema v4176 = fixerUpper.addSchema(4176, SAME_NAMESPACED);
/* 1536 */     fixerUpper.addFixer(new InvalidBlockEntityLockFix(v4176));
/* 1537 */     fixerUpper.addFixer(new InvalidLockComponentFix(v4176));
/*      */     
/* 1539 */     Schema v4180 = fixerUpper.addSchema(4180, SAME_NAMESPACED);
/* 1540 */     fixerUpper.addFixer(new FeatureFlagRemoveFix(v4180, "Remove Winter Drop toggle", Set.of("minecraft:winter_drop")));
/*      */     
/* 1542 */     Schema v4181 = fixerUpper.addSchema(4181, SAME_NAMESPACED);
/* 1543 */     fixerUpper.addFixer(new BlockEntityFurnaceBurnTimeFix(v4181, "minecraft:furnace"));
/* 1544 */     fixerUpper.addFixer(new BlockEntityFurnaceBurnTimeFix(v4181, "minecraft:smoker"));
/* 1545 */     fixerUpper.addFixer(new BlockEntityFurnaceBurnTimeFix(v4181, "minecraft:blast_furnace"));
/*      */ 
/*      */     
/* 1548 */     Schema v4187 = fixerUpper.addSchema(4187, SAME_NAMESPACED);
/* 1549 */     fixerUpper.addFixer(new EntityAttributeBaseValueFix(v4187, "Villager follow range fix undo", "minecraft:villager", "minecraft:follow_range", value -> (value == 48.0D) ? 16.0D : value));
/* 1550 */     fixerUpper.addFixer(new EntityAttributeBaseValueFix(v4187, "Bee follow range fix", "minecraft:bee", "minecraft:follow_range", value -> (value == 48.0D) ? 16.0D : value));
/* 1551 */     fixerUpper.addFixer(new EntityAttributeBaseValueFix(v4187, "Allay follow range fix", "minecraft:allay", "minecraft:follow_range", value -> (value == 48.0D) ? 16.0D : value));
/* 1552 */     fixerUpper.addFixer(new EntityAttributeBaseValueFix(v4187, "Llama follow range fix", "minecraft:llama", "minecraft:follow_range", value -> (value == 40.0D) ? 16.0D : value));
/* 1553 */     fixerUpper.addFixer(new EntityAttributeBaseValueFix(v4187, "Piglin Brute follow range fix", "minecraft:piglin_brute", "minecraft:follow_range", value -> (value == 16.0D) ? 12.0D : value));
/* 1554 */     fixerUpper.addFixer(new EntityAttributeBaseValueFix(v4187, "Warden follow range fix", "minecraft:warden", "minecraft:follow_range", value -> (value == 16.0D) ? 24.0D : value));
/*      */     
/* 1556 */     Schema v4290 = fixerUpper.addSchema(4290, net.minecraft.util.datafix.schemas.V4290::new);
/* 1557 */     fixerUpper.addFixer(new UnflattenTextComponentFix(v4290));
/*      */     
/* 1559 */     Schema v4291 = fixerUpper.addSchema(4291, SAME_NAMESPACED);
/* 1560 */     fixerUpper.addFixer(new LegacyHoverEventFix(v4291));
/*      */     
/* 1562 */     fixerUpper.addFixer(new TextComponentStringifiedFlagsFix(v4291));
/*      */     
/* 1564 */     Schema v4292 = fixerUpper.addSchema(4292, net.minecraft.util.datafix.schemas.V4292::new);
/* 1565 */     fixerUpper.addFixer(new TextComponentHoverAndClickEventFix(v4292));
/*      */     
/* 1567 */     Schema v4293 = fixerUpper.addSchema(4293, SAME_NAMESPACED);
/* 1568 */     fixerUpper.addFixer(new DropChancesFormatFix(v4293));
/*      */     
/* 1570 */     Schema v4294 = fixerUpper.addSchema(4294, SAME_NAMESPACED);
/* 1571 */     fixerUpper.addFixer(new BlockPropertyRenameAndFix(v4294, "CreakingHeartBlockStateFix", "minecraft:creaking_heart", "active", "creaking_heart_state", value -> 
/*      */ 
/*      */           
/* 1574 */           value.equals("true") ? "awake" : "uprooted"));
/*      */ 
/*      */     
/* 1577 */     Schema blendingSchema = fixerUpper.addSchema(4295, SAME_NAMESPACED);
/*      */     
/* 1579 */     fixerUpper.addFixer(new BlendingDataFix(blendingSchema));
/*      */     
/* 1581 */     Schema v4296 = fixerUpper.addSchema(4296, SAME_NAMESPACED);
/* 1582 */     fixerUpper.addFixer(new AreaEffectCloudDurationScaleFix(v4296));
/*      */     
/* 1584 */     Schema v4297 = fixerUpper.addSchema(4297, SAME_NAMESPACED);
/* 1585 */     fixerUpper.addFixer(new ForcedChunkToTicketFix(v4297));
/*      */     
/* 1587 */     Schema v4299 = fixerUpper.addSchema(4299, SAME_NAMESPACED);
/* 1588 */     fixerUpper.addFixer(new EntitySpawnerItemVariantComponentFix(v4299));
/*      */     
/* 1590 */     Schema v4300 = fixerUpper.addSchema(4300, net.minecraft.util.datafix.schemas.V4300::new);
/* 1591 */     fixerUpper.addFixer(new SaddleEquipmentSlotFix(v4300));
/*      */     
/* 1593 */     Schema v4301 = fixerUpper.addSchema(4301, net.minecraft.util.datafix.schemas.V4301::new);
/* 1594 */     fixerUpper.addFixer(new EquipmentFormatFix(v4301));
/*      */     
/* 1596 */     Schema v4302 = fixerUpper.addSchema(4302, net.minecraft.util.datafix.schemas.V4302::new);
/* 1597 */     fixerUpper.addFixer(new AddNewChoices(v4302, "Added Test and Test Instance Block Entities", References.BLOCK_ENTITY));
/*      */     
/* 1599 */     Schema v4303 = fixerUpper.addSchema(4303, SAME_NAMESPACED);
/* 1600 */     fixerUpper.addFixer(new EntityFallDistanceFloatToDoubleFix(v4303, References.ENTITY));
/* 1601 */     fixerUpper.addFixer(new EntityFallDistanceFloatToDoubleFix(v4303, References.PLAYER));
/*      */     
/* 1603 */     Schema v4305 = fixerUpper.addSchema(4305, SAME_NAMESPACED);
/* 1604 */     fixerUpper.addFixer(new BlockPropertyRenameAndFix(v4305, "rename test block mode", "minecraft:test_block", "test_block_mode", "mode", a -> a));
/*      */     
/* 1606 */     Schema v4306 = fixerUpper.addSchema(4306, net.minecraft.util.datafix.schemas.V4306::new);
/* 1607 */     fixerUpper.addFixer(new ThrownPotionSplitFix(v4306));
/*      */     
/* 1609 */     Schema v4307 = fixerUpper.addSchema(4307, net.minecraft.util.datafix.schemas.V4307::new);
/* 1610 */     fixerUpper.addFixer(new TooltipDisplayComponentFix(v4307));
/*      */     
/* 1612 */     Schema v4309 = fixerUpper.addSchema(4309, SAME_NAMESPACED);
/* 1613 */     fixerUpper.addFixer(new RaidRenamesDataFix(v4309));
/* 1614 */     fixerUpper.addFixer(new ChunkTicketUnpackPosFix(v4309));
/*      */     
/* 1616 */     Schema v4311 = fixerUpper.addSchema(4311, SAME_NAMESPACED);
/* 1617 */     fixerUpper.addFixer(new AdvancementsRenameFix(v4311, false, "Use lodestone category change", createRenamer("minecraft:nether/use_lodestone", "minecraft:adventure/use_lodestone")));
/*      */     
/* 1619 */     Schema v4312 = fixerUpper.addSchema(4312, net.minecraft.util.datafix.schemas.V4312::new);
/* 1620 */     fixerUpper.addFixer(new PlayerEquipmentFix(v4312));
/*      */     
/* 1622 */     Schema v4314 = fixerUpper.addSchema(4314, SAME_NAMESPACED);
/* 1623 */     fixerUpper.addFixer(new InlineBlockPosFormatFix(v4314));
/*      */     
/* 1625 */     Schema v4420 = fixerUpper.addSchema(4420, net.minecraft.util.datafix.schemas.V4420::new);
/* 1626 */     fixerUpper.addFixer(new NamedEntityConvertUncheckedFix(v4420, "AreaEffectCloudCustomParticleFix", References.ENTITY, "minecraft:area_effect_cloud"));
/*      */     
/* 1628 */     Schema v4421 = fixerUpper.addSchema(4421, net.minecraft.util.datafix.schemas.V4421::new);
/* 1629 */     fixerUpper.addFixer(new AddNewChoices(v4421, "Added Happy Ghast", References.ENTITY));
/*      */     
/* 1631 */     Schema v4424 = fixerUpper.addSchema(4424, SAME_NAMESPACED);
/* 1632 */     fixerUpper.addFixer(new FeatureFlagRemoveFix(v4424, "Remove Locator Bar experimental feature flag", Set.of("minecraft:locator_bar")));
/* 1633 */     fixerUpper.addFixer(new AddFieldFix(v4424, References.PLAYER, "style", field -> field.createString("minecraft:default"), new String[] { "locator_bar_icon" }));
/* 1634 */     fixerUpper.addFixer(new AddFieldFix(v4424, References.ENTITY, "style", field -> field.createString("minecraft:default"), new String[] { "locator_bar_icon" }));
/*      */     
/* 1636 */     Schema v4531 = fixerUpper.addSchema(4531, net.minecraft.util.datafix.schemas.V4531::new);
/* 1637 */     fixerUpper.addFixer(new AddNewChoices(v4531, "Added Copper Golem", References.ENTITY));
/*      */     
/* 1639 */     Schema v4532 = fixerUpper.addSchema(4532, net.minecraft.util.datafix.schemas.V4532::new);
/* 1640 */     fixerUpper.addFixer(new AddNewChoices(v4532, "Added Copper Golem Statue Block Entity", References.BLOCK_ENTITY));
/*      */     
/* 1642 */     Schema v4533 = fixerUpper.addSchema(4533, net.minecraft.util.datafix.schemas.V4533::new);
/* 1643 */     fixerUpper.addFixer(new AddNewChoices(v4533, "Added Shelf", References.BLOCK_ENTITY));
/*      */     
/* 1645 */     Schema v4535 = fixerUpper.addSchema(4535, SAME_NAMESPACED);
/* 1646 */     fixerUpper.addFixer(new CopperGolemWeatherStateFix(v4535));
/*      */     
/* 1648 */     Schema v4537 = fixerUpper.addSchema(4537, SAME_NAMESPACED);
/* 1649 */     fixerUpper.addFixer(new ChunkDeleteLightFix(v4537));
/*      */     
/* 1651 */     Schema v4541 = fixerUpper.addSchema(4541, SAME_NAMESPACED);
/* 1652 */     fixerUpper.addFixer(BlockRenameFix.create(v4541, "Rename chain to iron_chain", createRenamer("minecraft:chain", "minecraft:iron_chain")));
/* 1653 */     fixerUpper.addFixer(ItemRenameFix.create(v4541, "Rename chain to iron_chain", createRenamer("minecraft:chain", "minecraft:iron_chain")));
/*      */     
/* 1655 */     Schema v4543 = fixerUpper.addSchema(4543, net.minecraft.util.datafix.schemas.V4543::new);
/* 1656 */     fixerUpper.addFixer(new AddNewChoices(v4543, "Added Mannequin", References.ENTITY));
/*      */     
/* 1658 */     Schema v4544 = fixerUpper.addSchema(4544, SAME_NAMESPACED);
/* 1659 */     fixerUpper.addFixer(new LegacyWorldBorderFix(v4544));
/*      */     
/* 1661 */     Schema v4548 = fixerUpper.addSchema(4548, SAME_NAMESPACED);
/* 1662 */     fixerUpper.addFixer(new WorldSpawnDataFix(v4548));
/* 1663 */     fixerUpper.addFixer(new PlayerRespawnDataFix(v4548));
/*      */     
/* 1665 */     Schema v4648 = fixerUpper.addSchema(4648, net.minecraft.util.datafix.schemas.V4648::new);
/* 1666 */     fixerUpper.addFixer(new AddNewChoices(v4648, "Added Nautilus and Zombie Nautilus", References.ENTITY));
/*      */     
/* 1668 */     Schema v4649 = fixerUpper.addSchema(4649, SAME_NAMESPACED);
/* 1669 */     fixerUpper.addFixer(new TridentAnimationFix(v4649));
/*      */     
/* 1671 */     Schema v4650 = fixerUpper.addSchema(4650, SAME_NAMESPACED);
/* 1672 */     fixerUpper.addFixer(new DebugProfileOverlayReferenceFix(v4650));
/*      */     
/* 1674 */     Schema v4651 = fixerUpper.addSchema(4651, SAME_NAMESPACED);
/* 1675 */     fixerUpper.addFixer(new OptionsGraphicsModeSplitFix(v4651, "cutoutLeaves", "false", "true", "true"));
/* 1676 */     fixerUpper.addFixer(new OptionsGraphicsModeSplitFix(v4651, "weatherRadius", "5", "10", "10"));
/* 1677 */     fixerUpper.addFixer(new OptionsGraphicsModeSplitFix(v4651, "vignette", "false", "true", "true"));
/* 1678 */     fixerUpper.addFixer(new OptionsGraphicsModeSplitFix(v4651, "improvedTransparency", "false", "false", "true"));
/* 1679 */     fixerUpper.addFixer(new OptionsSetGraphicsPresetToCustomFix(v4651));
/*      */     
/* 1681 */     Schema v4656 = fixerUpper.addSchema(4656, net.minecraft.util.datafix.schemas.V4656::new);
/* 1682 */     fixerUpper.addFixer(new AddNewChoices(v4656, "Added Parched and Camel Husk", References.ENTITY));
/*      */     
/* 1684 */     Schema v4657 = fixerUpper.addSchema(4657, SAME_NAMESPACED);
/* 1685 */     fixerUpper.addFixer(new WorldBorderWarningTimeFix(v4657));
/*      */     
/* 1687 */     Schema v4658 = fixerUpper.addSchema(4658, SAME_NAMESPACED);
/* 1688 */     fixerUpper.addFixer(new GameRuleRegistryFix(v4658));
/*      */     
/* 1690 */     Schema v4661 = fixerUpper.addSchema(4661, SAME_NAMESPACED);
/* 1691 */     fixerUpper.addFixer(new OptionsMusicToastFix(v4661, false));
/*      */   }
/*      */ 
/*      */   
/* 1695 */   private static UnaryOperator<String> createRenamerNoNamespace(Map<String, String> map) { return id -> (String)map.getOrDefault(id, id); }
/*      */ 
/*      */ 
/*      */   
/* 1699 */   private static UnaryOperator<String> createRenamer(Map<String, String> map) { return id -> (String)map.getOrDefault(NamespacedSchema.ensureNamespaced(id), id); }
/*      */ 
/*      */ 
/*      */   
/* 1703 */   private static UnaryOperator<String> createRenamer(String from, String to) { return id -> Objects.equals(NamespacedSchema.ensureNamespaced(id), from) ? to : id; }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\DataFixers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
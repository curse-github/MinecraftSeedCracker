/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.BuiltInMetadata;
/*     */ import net.minecraft.server.packs.FeatureFlagsMetadataSection;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.VanillaPackResources;
/*     */ import net.minecraft.server.packs.VanillaPackResourcesBuilder;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ 
/*     */ public class ServerPacksSource
/*     */   extends BuiltInPackSource
/*     */ {
/*  26 */   private static final PackMetadataSection VERSION_METADATA_SECTION = new PackMetadataSection(
/*  27 */       Component.translatable("dataPack.vanilla.description"), 
/*  28 */       SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).minorRange());
/*     */ 
/*     */   
/*  31 */   private static final FeatureFlagsMetadataSection FEATURE_FLAGS_METADATA_SECTION = new FeatureFlagsMetadataSection(FeatureFlags.DEFAULT_FLAGS);
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final BuiltInMetadata BUILT_IN_METADATA = BuiltInMetadata.of(PackMetadataSection.SERVER_TYPE, VERSION_METADATA_SECTION, FeatureFlagsMetadataSection.TYPE, FEATURE_FLAGS_METADATA_SECTION);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final PackLocationInfo VANILLA_PACK_INFO = new PackLocationInfo("vanilla", 
/*     */       
/*  42 */       Component.translatable("dataPack.vanilla.name"), PackSource.BUILT_IN, 
/*     */       
/*  44 */       Optional.of(CORE_PACK_INFO));
/*     */ 
/*     */   
/*  47 */   private static final PackSelectionConfig VANILLA_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.BOTTOM, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final PackSelectionConfig FEATURE_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final Identifier PACKS_DIR = Identifier.withDefaultNamespace("datapacks");
/*     */ 
/*     */   
/*  62 */   public ServerPacksSource(DirectoryValidator validator) { super(PackType.SERVER_DATA, createVanillaPackSource(), PACKS_DIR, validator); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static PackLocationInfo createBuiltInPackLocation(String id, Component title) { return new PackLocationInfo(id, title, PackSource.FEATURE, 
/*     */ 
/*     */ 
/*     */         
/*  70 */         Optional.of(KnownPack.vanilla(id))); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public static VanillaPackResources createVanillaPackSource() {
/*  76 */     return (new VanillaPackResourcesBuilder())
/*  77 */       .setMetadata(BUILT_IN_METADATA)
/*  78 */       .exposeNamespace(new String[] { "minecraft"
/*  79 */         }).applyDevelopmentConfig()
/*  80 */       .pushJarResources()
/*  81 */       .build(VANILLA_PACK_INFO);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected Component getPackTitle(String id) { return Component.literal(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   protected Pack createVanillaPack(PackResources resources) { return Pack.readMetaAndCreate(VANILLA_PACK_INFO, fixedResources(resources), PackType.SERVER_DATA, VANILLA_SELECTION_CONFIG); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   protected Pack createBuiltinPack(String id, Pack.ResourcesSupplier resources, Component name) { return Pack.readMetaAndCreate(createBuiltInPackLocation(id, name), resources, PackType.SERVER_DATA, FEATURE_SELECTION_CONFIG); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static PackRepository createPackRepository(Path datapackDir, DirectoryValidator validator) { return new PackRepository(new RepositorySource[] { new ServerPacksSource(validator), new FolderRepositorySource(datapackDir, PackType.SERVER_DATA, PackSource.WORLD, validator) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PackRepository createVanillaTrustedRepository() {
/* 107 */     return new PackRepository(new RepositorySource[] { new ServerPacksSource(new DirectoryValidator(path -> true)) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static PackRepository createPackRepository(LevelStorageSource.LevelStorageAccess levelSourceAccess) { return createPackRepository(levelSourceAccess.getLevelPath(LevelResource.DATAPACK_DIR), levelSourceAccess.parent().getWorldDirValidator()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\ServerPacksSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
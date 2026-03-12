/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class DataPackCommand
/*     */ {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  56 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_PACK = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.unknown", new Object[] { id }));
/*  57 */   private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_ENABLED = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.enable.failed", new Object[] { id }));
/*  58 */   private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_DISABLED = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.disable.failed", new Object[] { id }));
/*  59 */   private static final DynamicCommandExceptionType ERROR_CANNOT_DISABLE_FEATURE = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.disable.failed.feature", new Object[] { id }));
/*  60 */   private static final Dynamic2CommandExceptionType ERROR_PACK_FEATURES_NOT_ENABLED = new Dynamic2CommandExceptionType((id, flags) -> Component.translatableEscape("commands.datapack.enable.failed.no_flags", new Object[] { id, flags }));
/*     */   
/*  62 */   private static final DynamicCommandExceptionType ERROR_PACK_INVALID_NAME = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.create.invalid_name", new Object[] { id }));
/*  63 */   private static final DynamicCommandExceptionType ERROR_PACK_INVALID_FULL_NAME = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.create.invalid_full_name", new Object[] { id }));
/*  64 */   private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_EXISTS = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.create.already_exists", new Object[] { id }));
/*  65 */   private static final Dynamic2CommandExceptionType ERROR_PACK_METADATA_ENCODE_FAILURE = new Dynamic2CommandExceptionType((id, error) -> Component.translatableEscape("commands.datapack.create.metadata_encode_failure", new Object[] { id, error }));
/*  66 */   private static final DynamicCommandExceptionType ERROR_PACK_IO_FAILURE = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.datapack.create.io_failure", new Object[] { id }));
/*     */   
/*  68 */   private static final SuggestionProvider<CommandSourceStack> SELECTED_PACKS = (c, p) -> SharedSuggestionProvider.suggest(((CommandSourceStack)c.getSource()).getServer().getPackRepository().getSelectedIds().stream().map(StringArgumentType::escapeIfRequired), p);
/*     */   private static final SuggestionProvider<CommandSourceStack> UNSELECTED_PACKS = (c, p) -> {
/*  70 */       PackRepository packRepository = ((CommandSourceStack)c.getSource()).getServer().getPackRepository();
/*  71 */       Collection<String> selectedIds = packRepository.getSelectedIds();
/*  72 */       FeatureFlagSet enabledFeatures = ((CommandSourceStack)c.getSource()).enabledFeatures();
/*  73 */       return SharedSuggestionProvider.suggest(packRepository
/*  74 */           .getAvailablePacks()
/*  75 */           .stream()
/*  76 */           .filter(())
/*  77 */           .map(Pack::getId)
/*  78 */           .filter(())
/*  79 */           .map(StringArgumentType::escapeIfRequired), p);
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  85 */     dispatcher.register(
/*  86 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("datapack")
/*  87 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  88 */         .then(
/*  89 */           Commands.literal("enable")
/*  90 */           .then((
/*  91 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", StringArgumentType.string())
/*  92 */             .suggests(UNSELECTED_PACKS)
/*  93 */             .executes(c -> enablePack((CommandSourceStack)c.getSource(), getPack(c, "name", true), ())))
/*  94 */             .then(
/*  95 */               Commands.literal("after")
/*  96 */               .then(
/*  97 */                 Commands.argument("existing", StringArgumentType.string())
/*  98 */                 .suggests(SELECTED_PACKS)
/*  99 */                 .executes(c -> enablePack((CommandSourceStack)c.getSource(), getPack(c, "name", true), ())))))
/*     */ 
/*     */             
/* 102 */             .then(
/* 103 */               Commands.literal("before")
/* 104 */               .then(
/* 105 */                 Commands.argument("existing", StringArgumentType.string())
/* 106 */                 .suggests(SELECTED_PACKS)
/* 107 */                 .executes(c -> enablePack((CommandSourceStack)c.getSource(), getPack(c, "name", true), ())))))
/*     */ 
/*     */             
/* 110 */             .then(
/* 111 */               Commands.literal("last")
/* 112 */               .executes(c -> enablePack((CommandSourceStack)c.getSource(), getPack(c, "name", true), List::add))))
/*     */             
/* 114 */             .then(
/* 115 */               Commands.literal("first")
/* 116 */               .executes(c -> enablePack((CommandSourceStack)c.getSource(), getPack(c, "name", true), ()))))))
/*     */ 
/*     */ 
/*     */         
/* 120 */         .then(
/* 121 */           Commands.literal("disable")
/* 122 */           .then(
/* 123 */             Commands.argument("name", StringArgumentType.string())
/* 124 */             .suggests(SELECTED_PACKS)
/* 125 */             .executes(c -> disablePack((CommandSourceStack)c.getSource(), getPack(c, "name", false))))))
/*     */ 
/*     */         
/* 128 */         .then((
/* 129 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list")
/* 130 */           .executes(c -> listPacks((CommandSourceStack)c.getSource())))
/* 131 */           .then(
/* 132 */             Commands.literal("available")
/* 133 */             .executes(c -> listAvailablePacks((CommandSourceStack)c.getSource()))))
/*     */           
/* 135 */           .then(
/* 136 */             Commands.literal("enabled")
/* 137 */             .executes(c -> listEnabledPacks((CommandSourceStack)c.getSource())))))
/*     */ 
/*     */         
/* 140 */         .then((
/* 141 */           (LiteralArgumentBuilder)Commands.literal("create")
/* 142 */           .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 143 */           .then(
/* 144 */             Commands.argument("id", StringArgumentType.string())
/* 145 */             .then(
/* 146 */               Commands.argument("description", ComponentArgument.textComponent(context))
/* 147 */               .executes(c -> createPack((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "id"), ComponentArgument.getResolvedComponent(c, "description")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int createPack(CommandSourceStack source, String id, Component description) throws CommandSyntaxException {
/* 155 */     Path datapackDir = source.getServer().getWorldPath(LevelResource.DATAPACK_DIR);
/* 156 */     if (!FileUtil.isValidPathSegment(id)) {
/* 157 */       throw ERROR_PACK_INVALID_NAME.create(id);
/*     */     }
/* 159 */     if (!FileUtil.isPathPartPortable(id)) {
/* 160 */       throw ERROR_PACK_INVALID_FULL_NAME.create(id);
/*     */     }
/* 162 */     Path packDir = datapackDir.resolve(id);
/* 163 */     if (Files.exists(packDir, new java.nio.file.LinkOption[0])) {
/* 164 */       throw ERROR_PACK_ALREADY_EXISTS.create(id);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 169 */     PackMetadataSection packMetadataSection = new PackMetadataSection(description, SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).minorRange());
/*     */ 
/*     */     
/* 172 */     DataResult<JsonElement> encodedMeta = PackMetadataSection.SERVER_TYPE.codec().encodeStart(JsonOps.INSTANCE, packMetadataSection);
/* 173 */     Optional<DataResult.Error<JsonElement>> error = encodedMeta.error();
/* 174 */     if (error.isPresent()) {
/* 175 */       throw ERROR_PACK_METADATA_ENCODE_FAILURE.create(id, ((DataResult.Error)error.get()).message());
/*     */     }
/* 177 */     JsonObject topMcmeta = new JsonObject();
/* 178 */     topMcmeta.add(PackMetadataSection.SERVER_TYPE.name(), (JsonElement)encodedMeta.getOrThrow());
/*     */ 
/*     */     
/* 181 */     try { Files.createDirectory(packDir, new java.nio.file.attribute.FileAttribute[0]);
/* 182 */       Files.createDirectory(packDir.resolve(PackType.SERVER_DATA.getDirectory()), new java.nio.file.attribute.FileAttribute[0]);
/*     */ 
/*     */       
/* 185 */       BufferedWriter mcmetaFile = Files.newBufferedWriter(packDir.resolve("pack.mcmeta"), StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/* 186 */       try { JsonWriter jsonWriter = new JsonWriter(mcmetaFile);
/*     */         
/* 188 */         try { jsonWriter.setSerializeNulls(false);
/* 189 */           jsonWriter.setIndent("  ");
/* 190 */           GsonHelper.writeValue(jsonWriter, topMcmeta, null);
/* 191 */           jsonWriter.close(); } catch (Throwable throwable) { try { jsonWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (mcmetaFile != null) mcmetaFile.close();  } catch (Throwable throwable) { if (mcmetaFile != null)
/* 192 */           try { mcmetaFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 193 */     { LOGGER.warn("Failed to create pack at {}", datapackDir.toAbsolutePath(), e);
/* 194 */       throw ERROR_PACK_IO_FAILURE.create(id); }
/*     */ 
/*     */     
/* 197 */     source.sendSuccess(() -> Component.translatable("commands.datapack.create.success", new Object[] { id }), true);
/* 198 */     return 1;
/*     */   }
/*     */   
/*     */   private static int enablePack(CommandSourceStack source, Pack unopened, Inserter inserter) throws CommandSyntaxException {
/* 202 */     PackRepository packRepository = source.getServer().getPackRepository();
/*     */     
/* 204 */     List<Pack> selected = Lists.newArrayList(packRepository.getSelectedPacks());
/* 205 */     inserter.apply(selected, unopened);
/*     */     
/* 207 */     source.sendSuccess(() -> Component.translatable("commands.datapack.modify.enable", new Object[] { unopened.getChatLink(true) }), true);
/* 208 */     ReloadCommand.reloadPacks((Collection)selected.stream().map(Pack::getId).collect(Collectors.toList()), source);
/* 209 */     return selected.size();
/*     */   }
/*     */   
/*     */   private static int disablePack(CommandSourceStack source, Pack unopened) {
/* 213 */     PackRepository packRepository = source.getServer().getPackRepository();
/*     */     
/* 215 */     List<Pack> selected = Lists.newArrayList(packRepository.getSelectedPacks());
/* 216 */     selected.remove(unopened);
/*     */     
/* 218 */     source.sendSuccess(() -> Component.translatable("commands.datapack.modify.disable", new Object[] { unopened.getChatLink(true) }), true);
/* 219 */     ReloadCommand.reloadPacks((Collection)selected.stream().map(Pack::getId).collect(Collectors.toList()), source);
/* 220 */     return selected.size();
/*     */   }
/*     */ 
/*     */   
/* 224 */   private static int listPacks(CommandSourceStack source) { return listEnabledPacks(source) + listAvailablePacks(source); }
/*     */ 
/*     */   
/*     */   private static int listAvailablePacks(CommandSourceStack source) {
/* 228 */     PackRepository repository = source.getServer().getPackRepository();
/* 229 */     repository.reload();
/*     */     
/* 231 */     Collection<Pack> selectedPacks = repository.getSelectedPacks();
/* 232 */     Collection<Pack> availablePacks = repository.getAvailablePacks();
/* 233 */     FeatureFlagSet enabledFeatures = source.enabledFeatures();
/* 234 */     List<Pack> unselectedPacks = availablePacks.stream().filter(p -> (!selectedPacks.contains(p) && p.getRequestedFeatures().isSubsetOf(enabledFeatures))).toList();
/* 235 */     if (unselectedPacks.isEmpty()) {
/* 236 */       source.sendSuccess(() -> Component.translatable("commands.datapack.list.available.none"), false);
/*     */     } else {
/* 238 */       source.sendSuccess(() -> Component.translatable("commands.datapack.list.available.success", new Object[] { Integer.valueOf(unselectedPacks.size()), ComponentUtils.formatList(unselectedPacks, ()) }), false);
/*     */     } 
/*     */     
/* 241 */     return unselectedPacks.size();
/*     */   }
/*     */   
/*     */   private static int listEnabledPacks(CommandSourceStack source) {
/* 245 */     PackRepository repository = source.getServer().getPackRepository();
/* 246 */     repository.reload();
/*     */     
/* 248 */     Collection<? extends Pack> selectedPacks = repository.getSelectedPacks();
/* 249 */     if (selectedPacks.isEmpty()) {
/* 250 */       source.sendSuccess(() -> Component.translatable("commands.datapack.list.enabled.none"), false);
/*     */     } else {
/* 252 */       source.sendSuccess(() -> Component.translatable("commands.datapack.list.enabled.success", new Object[] { Integer.valueOf(selectedPacks.size()), ComponentUtils.formatList(selectedPacks, ()) }), false);
/*     */     } 
/*     */     
/* 255 */     return selectedPacks.size();
/*     */   }
/*     */   
/*     */   private static Pack getPack(CommandContext<CommandSourceStack> context, String name, boolean enabling) throws CommandSyntaxException {
/* 259 */     String id = StringArgumentType.getString(context, name);
/* 260 */     PackRepository repository = ((CommandSourceStack)context.getSource()).getServer().getPackRepository();
/* 261 */     Pack pack = repository.getPack(id);
/* 262 */     if (pack == null) {
/* 263 */       throw ERROR_UNKNOWN_PACK.create(id);
/*     */     }
/* 265 */     boolean enabled = repository.getSelectedPacks().contains(pack);
/* 266 */     if (enabling && enabled) {
/* 267 */       throw ERROR_PACK_ALREADY_ENABLED.create(id);
/*     */     }
/* 269 */     if (!enabling && !enabled) {
/* 270 */       throw ERROR_PACK_ALREADY_DISABLED.create(id);
/*     */     }
/* 272 */     FeatureFlagSet availableFeatures = ((CommandSourceStack)context.getSource()).enabledFeatures();
/* 273 */     FeatureFlagSet requestedFeatures = pack.getRequestedFeatures();
/* 274 */     if (!enabling && !requestedFeatures.isEmpty() && pack.getPackSource() == PackSource.FEATURE) {
/* 275 */       throw ERROR_CANNOT_DISABLE_FEATURE.create(id);
/*     */     }
/* 277 */     if (!requestedFeatures.isSubsetOf(availableFeatures)) {
/* 278 */       throw ERROR_PACK_FEATURES_NOT_ENABLED.create(id, FeatureFlags.printMissingFlags(availableFeatures, requestedFeatures));
/*     */     }
/* 280 */     return pack;
/*     */   }
/*     */   
/*     */   private static interface Inserter {
/*     */     void apply(List<Pack> param1List, Pack param1Pack) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DataPackCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
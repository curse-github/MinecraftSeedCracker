/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.commands.arguments.TemplateMirrorArgument;
/*     */ import net.minecraft.commands.arguments.TemplateRotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
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
/*     */ public class PlaceCommand
/*     */ {
/*  60 */   private static final SimpleCommandExceptionType ERROR_FEATURE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.feature.failed"));
/*  61 */   private static final SimpleCommandExceptionType ERROR_JIGSAW_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.jigsaw.failed"));
/*  62 */   private static final SimpleCommandExceptionType ERROR_STRUCTURE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.structure.failed"));
/*  63 */   private static final DynamicCommandExceptionType ERROR_TEMPLATE_INVALID = new DynamicCommandExceptionType(value -> Component.translatableEscape("commands.place.template.invalid", new Object[] { value }));
/*  64 */   private static final SimpleCommandExceptionType ERROR_TEMPLATE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.place.template.failed"));
/*     */   
/*     */   private static final SuggestionProvider<CommandSourceStack> SUGGEST_TEMPLATES = (context, builder) -> {
/*  67 */       StructureTemplateManager structureManager = ((CommandSourceStack)context.getSource()).getLevel().getStructureManager();
/*  68 */       return SharedSuggestionProvider.suggestResource(structureManager.listTemplates(), builder);
/*     */     };
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  72 */     dispatcher.register(
/*  73 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("place")
/*  74 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  75 */         .then(
/*  76 */           Commands.literal("feature")
/*  77 */           .then((
/*  78 */             (RequiredArgumentBuilder)Commands.argument("feature", ResourceKeyArgument.key(Registries.CONFIGURED_FEATURE))
/*  79 */             .executes(c -> placeFeature((CommandSourceStack)c.getSource(), ResourceKeyArgument.getConfiguredFeature(c, "feature"), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()))))
/*  80 */             .then(
/*  81 */               Commands.argument("pos", BlockPosArgument.blockPos())
/*  82 */               .executes(c -> placeFeature((CommandSourceStack)c.getSource(), ResourceKeyArgument.getConfiguredFeature(c, "feature"), BlockPosArgument.getLoadedBlockPos(c, "pos")))))))
/*     */ 
/*     */ 
/*     */         
/*  86 */         .then(
/*  87 */           Commands.literal("jigsaw")
/*  88 */           .then(
/*  89 */             Commands.argument("pool", ResourceKeyArgument.key(Registries.TEMPLATE_POOL))
/*  90 */             .then(
/*  91 */               Commands.argument("target", IdentifierArgument.id())
/*  92 */               .then((
/*  93 */                 (RequiredArgumentBuilder)Commands.argument("max_depth", IntegerArgumentType.integer(1, 20))
/*  94 */                 .executes(c -> placeJigsaw((CommandSourceStack)c.getSource(), ResourceKeyArgument.getStructureTemplatePool(c, "pool"), IdentifierArgument.getId(c, "target"), IntegerArgumentType.getInteger(c, "max_depth"), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()))))
/*  95 */                 .then(
/*  96 */                   Commands.argument("position", BlockPosArgument.blockPos())
/*  97 */                   .executes(c -> placeJigsaw((CommandSourceStack)c.getSource(), ResourceKeyArgument.getStructureTemplatePool(c, "pool"), IdentifierArgument.getId(c, "target"), IntegerArgumentType.getInteger(c, "max_depth"), BlockPosArgument.getLoadedBlockPos(c, "position")))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 103 */         .then(
/* 104 */           Commands.literal("structure")
/* 105 */           .then((
/* 106 */             (RequiredArgumentBuilder)Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE))
/* 107 */             .executes(c -> placeStructure((CommandSourceStack)c.getSource(), ResourceKeyArgument.getStructure(c, "structure"), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()))))
/* 108 */             .then(
/* 109 */               Commands.argument("pos", BlockPosArgument.blockPos())
/* 110 */               .executes(c -> placeStructure((CommandSourceStack)c.getSource(), ResourceKeyArgument.getStructure(c, "structure"), BlockPosArgument.getLoadedBlockPos(c, "pos")))))))
/*     */ 
/*     */ 
/*     */         
/* 114 */         .then(
/* 115 */           Commands.literal("template")
/* 116 */           .then((
/* 117 */             (RequiredArgumentBuilder)Commands.argument("template", IdentifierArgument.id())
/* 118 */             .suggests(SUGGEST_TEMPLATES)
/* 119 */             .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()), Rotation.NONE, Mirror.NONE, 1.0F, 0, false)))
/* 120 */             .then((
/* 121 */               (RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos())
/* 122 */               .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPosArgument.getLoadedBlockPos(c, "pos"), Rotation.NONE, Mirror.NONE, 1.0F, 0, false)))
/* 123 */               .then((
/* 124 */                 (RequiredArgumentBuilder)Commands.argument("rotation", TemplateRotationArgument.templateRotation())
/* 125 */                 .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPosArgument.getLoadedBlockPos(c, "pos"), TemplateRotationArgument.getRotation(c, "rotation"), Mirror.NONE, 1.0F, 0, false)))
/* 126 */                 .then((
/* 127 */                   (RequiredArgumentBuilder)Commands.argument("mirror", TemplateMirrorArgument.templateMirror())
/* 128 */                   .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPosArgument.getLoadedBlockPos(c, "pos"), TemplateRotationArgument.getRotation(c, "rotation"), TemplateMirrorArgument.getMirror(c, "mirror"), 1.0F, 0, false)))
/* 129 */                   .then((
/* 130 */                     (RequiredArgumentBuilder)Commands.argument("integrity", FloatArgumentType.floatArg(0.0F, 1.0F))
/* 131 */                     .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPosArgument.getLoadedBlockPos(c, "pos"), TemplateRotationArgument.getRotation(c, "rotation"), TemplateMirrorArgument.getMirror(c, "mirror"), FloatArgumentType.getFloat(c, "integrity"), 0, false)))
/* 132 */                     .then((
/* 133 */                       (RequiredArgumentBuilder)Commands.argument("seed", IntegerArgumentType.integer())
/* 134 */                       .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPosArgument.getLoadedBlockPos(c, "pos"), TemplateRotationArgument.getRotation(c, "rotation"), TemplateMirrorArgument.getMirror(c, "mirror"), FloatArgumentType.getFloat(c, "integrity"), IntegerArgumentType.getInteger(c, "seed"), false)))
/* 135 */                       .then(
/* 136 */                         Commands.literal("strict")
/* 137 */                         .executes(c -> placeTemplate((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "template"), BlockPosArgument.getLoadedBlockPos(c, "pos"), TemplateRotationArgument.getRotation(c, "rotation"), TemplateMirrorArgument.getMirror(c, "mirror"), FloatArgumentType.getFloat(c, "integrity"), IntegerArgumentType.getInteger(c, "seed"), true)))))))))));
/*     */   }
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
/*     */   public static int placeFeature(CommandSourceStack source, Holder.Reference<ConfiguredFeature<?, ?>> featureHolder, BlockPos pos) throws CommandSyntaxException {
/* 150 */     ServerLevel level = source.getLevel();
/* 151 */     ConfiguredFeature<?, ?> feature = (ConfiguredFeature)featureHolder.value();
/*     */     
/* 153 */     ChunkPos chunkPos = new ChunkPos(pos);
/* 154 */     checkLoaded(level, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1), new ChunkPos(chunkPos.x + 1, chunkPos.z + 1));
/*     */     
/* 156 */     if (!feature.place(level, level.getChunkSource().getGenerator(), level.getRandom(), pos)) {
/* 157 */       throw ERROR_FEATURE_FAILED.create();
/*     */     }
/* 159 */     String id = featureHolder.key().identifier().toString();
/* 160 */     source.sendSuccess(() -> Component.translatable("commands.place.feature.success", new Object[] { id, Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }), true);
/* 161 */     return 1;
/*     */   }
/*     */   
/*     */   public static int placeJigsaw(CommandSourceStack source, Holder<StructureTemplatePool> pool, Identifier target, int maxDepth, BlockPos pos) throws CommandSyntaxException {
/* 165 */     ServerLevel level = source.getLevel();
/* 166 */     ChunkPos chunk = new ChunkPos(pos);
/* 167 */     checkLoaded(level, chunk, chunk);
/* 168 */     if (!JigsawPlacement.generateJigsaw(level, pool, target, maxDepth, pos, false)) {
/* 169 */       throw ERROR_JIGSAW_FAILED.create();
/*     */     }
/* 171 */     source.sendSuccess(() -> Component.translatable("commands.place.jigsaw.success", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }), true);
/* 172 */     return 1;
/*     */   }
/*     */   
/*     */   public static int placeStructure(CommandSourceStack source, Holder.Reference<Structure> structureHolder, BlockPos pos) throws CommandSyntaxException {
/* 176 */     ServerLevel level = source.getLevel();
/* 177 */     Structure structure = (Structure)structureHolder.value();
/* 178 */     ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
/*     */     
/* 180 */     StructureStart start = structure.generate(structureHolder, level.dimension(), source.registryAccess(), chunkGenerator, chunkGenerator.getBiomeSource(), level.getChunkSource().randomState(), level.getStructureManager(), level.getSeed(), new ChunkPos(pos), 0, level, b -> true);
/* 181 */     if (!start.isValid()) {
/* 182 */       throw ERROR_STRUCTURE_FAILED.create();
/*     */     }
/* 184 */     BoundingBox boundingBox = start.getBoundingBox();
/* 185 */     ChunkPos chunkMin = new ChunkPos(SectionPos.blockToSectionCoord(boundingBox.minX()), SectionPos.blockToSectionCoord(boundingBox.minZ()));
/* 186 */     ChunkPos chunkMax = new ChunkPos(SectionPos.blockToSectionCoord(boundingBox.maxX()), SectionPos.blockToSectionCoord(boundingBox.maxZ()));
/*     */     
/* 188 */     checkLoaded(level, chunkMin, chunkMax);
/* 189 */     ChunkPos.rangeClosed(chunkMin, chunkMax).forEach(c -> 
/* 190 */         start.placeInChunk(level, level.structureManager(), chunkGenerator, level.getRandom(), new BoundingBox(c.getMinBlockX(), level.getMinY(), c.getMinBlockZ(), c.getMaxBlockX(), level.getMaxY() + 1, c.getMaxBlockZ()), c));
/*     */ 
/*     */     
/* 193 */     String id = structureHolder.key().identifier().toString();
/* 194 */     source.sendSuccess(() -> Component.translatable("commands.place.structure.success", new Object[] { id, Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }), true);
/* 195 */     return 1;
/*     */   }
/*     */   public static int placeTemplate(CommandSourceStack source, Identifier template, BlockPos pos, Rotation rotation, Mirror mirror, float integrity, int seed, boolean strict) throws CommandSyntaxException {
/*     */     Optional<StructureTemplate> maybeStructureTemplate;
/* 199 */     ServerLevel level = source.getLevel();
/* 200 */     StructureTemplateManager manager = level.getStructureManager();
/*     */     
/*     */     try {
/* 203 */       maybeStructureTemplate = manager.get(template);
/* 204 */     } catch (IdentifierException e) {
/* 205 */       throw ERROR_TEMPLATE_INVALID.create(template);
/*     */     } 
/* 207 */     if (maybeStructureTemplate.isEmpty()) {
/* 208 */       throw ERROR_TEMPLATE_INVALID.create(template);
/*     */     }
/* 210 */     StructureTemplate structureTemplate = (StructureTemplate)maybeStructureTemplate.get();
/* 211 */     checkLoaded(level, new ChunkPos(pos), new ChunkPos(pos.offset(structureTemplate.getSize())));
/*     */     
/* 213 */     StructurePlaceSettings placeSettings = (new StructurePlaceSettings()).setMirror(mirror).setRotation(rotation).setKnownShape(strict);
/* 214 */     if (integrity < 1.0F) {
/* 215 */       placeSettings.clearProcessors().addProcessor(new BlockRotProcessor(integrity)).setRandom(StructureBlockEntity.createRandom(seed));
/*     */     }
/*     */     
/* 218 */     boolean placed = structureTemplate.placeInWorld(level, pos, pos, placeSettings, StructureBlockEntity.createRandom(seed), 0x2 | (strict ? 816 : 0));
/* 219 */     if (!placed) {
/* 220 */       throw ERROR_TEMPLATE_FAILED.create();
/*     */     }
/* 222 */     source.sendSuccess(() -> Component.translatable("commands.place.template.success", new Object[] { Component.translationArg(template), Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }), true);
/* 223 */     return 1;
/*     */   }
/*     */   
/*     */   private static void checkLoaded(ServerLevel level, ChunkPos chunkMin, ChunkPos chunkMax) throws CommandSyntaxException {
/* 227 */     if (ChunkPos.rangeClosed(chunkMin, chunkMax).filter(c -> !level.isLoaded(c.getWorldPosition())).findAny().isPresent())
/* 228 */       throw BlockPosArgument.ERROR_NOT_LOADED.create(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\PlaceCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
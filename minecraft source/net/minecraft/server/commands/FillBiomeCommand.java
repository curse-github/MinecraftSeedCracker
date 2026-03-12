/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ 
/*     */ public class FillBiomeCommand
/*     */ {
/*  43 */   public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType(Component.translatable("argument.pos.unloaded"));
/*  44 */   private static final Dynamic2CommandExceptionType ERROR_VOLUME_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.fillbiome.toobig", new Object[] { max, count }));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  47 */     dispatcher.register(
/*  48 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fillbiome")
/*  49 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  50 */         .then(
/*  51 */           Commands.argument("from", BlockPosArgument.blockPos())
/*  52 */           .then(
/*  53 */             Commands.argument("to", BlockPosArgument.blockPos())
/*  54 */             .then((
/*  55 */               (RequiredArgumentBuilder)Commands.argument("biome", ResourceArgument.resource(context, Registries.BIOME))
/*  56 */               .executes(c -> fill((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "from"), BlockPosArgument.getLoadedBlockPos(c, "to"), ResourceArgument.getResource(c, "biome", Registries.BIOME), ())))
/*  57 */               .then(Commands.literal("replace")
/*  58 */                 .then(
/*  59 */                   Commands.argument("filter", ResourceOrTagArgument.resourceOrTag(context, Registries.BIOME))
/*  60 */                   .executes(c -> fill((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "from"), BlockPosArgument.getLoadedBlockPos(c, "to"), ResourceArgument.getResource(c, "biome", Registries.BIOME), ResourceOrTagArgument.getResourceOrTag(c, "filter", Registries.BIOME)))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static int quantize(int blockCoord) { return QuartPos.toBlock(QuartPos.fromBlock(blockCoord)); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static BlockPos quantize(BlockPos block) { return new BlockPos(quantize(block.getX()), quantize(block.getY()), quantize(block.getZ())); }
/*     */ 
/*     */   
/*     */   private static BiomeResolver makeResolver(MutableInt count, ChunkAccess chunk, BoundingBox region, Holder<Biome> toFill, Predicate<Holder<Biome>> filter) {
/*  78 */     return (quartX, quartY, quartZ, sampler) -> {
/*  79 */         int blockX = QuartPos.toBlock(quartX);
/*  80 */         int blockY = QuartPos.toBlock(quartY);
/*  81 */         int blockZ = QuartPos.toBlock(quartZ);
/*  82 */         Holder<Biome> currentBiome = chunk.getNoiseBiome(quartX, quartY, quartZ);
/*  83 */         if (region.isInside(blockX, blockY, blockZ) && filter.test(currentBiome)) {
/*  84 */           count.increment();
/*  85 */           return toFill;
/*     */         } 
/*  87 */         return currentBiome;
/*     */       };
/*     */   }
/*     */   
/*     */   public static Either<Integer, CommandSyntaxException> fill(ServerLevel level, BlockPos rawFrom, BlockPos rawTo, Holder<Biome> biome) {
/*  92 */     return fill(level, rawFrom, rawTo, biome, b -> true, m -> {
/*     */         
/*     */         });
/*     */   } public static Either<Integer, CommandSyntaxException> fill(ServerLevel level, BlockPos rawFrom, BlockPos rawTo, Holder<Biome> biome, Predicate<Holder<Biome>> filter, Consumer<Supplier<Component>> successMessageConsumer) {
/*  96 */     BlockPos from = quantize(rawFrom);
/*  97 */     BlockPos to = quantize(rawTo);
/*  98 */     BoundingBox region = BoundingBox.fromCorners(from, to);
/*  99 */     int volume = region.getXSpan() * region.getYSpan() * region.getZSpan();
/* 100 */     int limit = ((Integer)level.getGameRules().get(GameRules.MAX_BLOCK_MODIFICATIONS)).intValue();
/* 101 */     if (volume > limit) {
/* 102 */       return Either.right(ERROR_VOLUME_TOO_LARGE.create(Integer.valueOf(limit), Integer.valueOf(volume)));
/*     */     }
/*     */     
/* 105 */     List<ChunkAccess> chunks = new ArrayList<ChunkAccess>();
/* 106 */     for (int chunkZ = SectionPos.blockToSectionCoord(region.minZ()); chunkZ <= SectionPos.blockToSectionCoord(region.maxZ()); chunkZ++) {
/* 107 */       for (int chunkX = SectionPos.blockToSectionCoord(region.minX()); chunkX <= SectionPos.blockToSectionCoord(region.maxX()); chunkX++) {
/* 108 */         ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
/* 109 */         if (chunk == null) {
/* 110 */           return Either.right(ERROR_NOT_LOADED.create());
/*     */         }
/* 112 */         chunks.add(chunk);
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     MutableInt changedCount = new MutableInt(0);
/* 117 */     for (ChunkAccess chunk : chunks) {
/* 118 */       chunk.fillBiomesFromNoise(makeResolver(changedCount, chunk, region, biome, filter), level.getChunkSource().randomState().sampler());
/* 119 */       chunk.markUnsaved();
/*     */     } 
/* 121 */     (level.getChunkSource()).chunkMap.resendBiomesForChunks(chunks);
/*     */     
/* 123 */     successMessageConsumer.accept(() -> Component.translatable("commands.fillbiome.success.count", new Object[] { Integer.valueOf(changedCount.intValue()), Integer.valueOf(region.minX()), Integer.valueOf(region.minY()), Integer.valueOf(region.minZ()), Integer.valueOf(region.maxX()), Integer.valueOf(region.maxY()), Integer.valueOf(region.maxZ()) }));
/* 124 */     return Either.left(Integer.valueOf(changedCount.intValue()));
/*     */   }
/*     */   
/*     */   private static int fill(CommandSourceStack source, BlockPos rawFrom, BlockPos rawTo, Holder.Reference<Biome> biome, Predicate<Holder<Biome>> filter) throws CommandSyntaxException {
/* 128 */     Either<Integer, CommandSyntaxException> result = fill(source.getLevel(), rawFrom, rawTo, biome, filter, m -> source.sendSuccess(m, true));
/* 129 */     Optional<CommandSyntaxException> exception = result.right();
/* 130 */     if (exception.isPresent()) {
/* 131 */       throw (CommandSyntaxException)exception.get();
/*     */     }
/* 133 */     return ((Integer)result.left().get()).intValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\FillBiomeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
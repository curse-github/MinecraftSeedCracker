/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ColumnPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ForceLoadCommand {
/*     */   private static final int MAX_CHUNK_LIMIT = 256;
/*  28 */   private static final Dynamic2CommandExceptionType ERROR_TOO_MANY_CHUNKS = new Dynamic2CommandExceptionType((max, amount) -> Component.translatableEscape("commands.forceload.toobig", new Object[] { max, amount }));
/*  29 */   private static final Dynamic2CommandExceptionType ERROR_NOT_TICKING = new Dynamic2CommandExceptionType((pos, dimension) -> Component.translatableEscape("commands.forceload.query.failure", new Object[] { pos, dimension }));
/*  30 */   private static final SimpleCommandExceptionType ERROR_ALL_ADDED = new SimpleCommandExceptionType(Component.translatable("commands.forceload.added.failure"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_NONE_REMOVED = new SimpleCommandExceptionType(Component.translatable("commands.forceload.removed.failure"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  34 */     dispatcher.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("forceload")
/*  36 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  37 */         .then(
/*  38 */           Commands.literal("add")
/*  39 */           .then((
/*  40 */             (RequiredArgumentBuilder)Commands.argument("from", ColumnPosArgument.columnPos())
/*  41 */             .executes(c -> changeForceLoad((CommandSourceStack)c.getSource(), ColumnPosArgument.getColumnPos(c, "from"), ColumnPosArgument.getColumnPos(c, "from"), true)))
/*  42 */             .then(
/*  43 */               Commands.argument("to", ColumnPosArgument.columnPos())
/*  44 */               .executes(c -> changeForceLoad((CommandSourceStack)c.getSource(), ColumnPosArgument.getColumnPos(c, "from"), ColumnPosArgument.getColumnPos(c, "to"), true))))))
/*     */         
/*  46 */         .then((
/*  47 */           (LiteralArgumentBuilder)Commands.literal("remove")
/*  48 */           .then((
/*  49 */             (RequiredArgumentBuilder)Commands.argument("from", ColumnPosArgument.columnPos())
/*  50 */             .executes(c -> changeForceLoad((CommandSourceStack)c.getSource(), ColumnPosArgument.getColumnPos(c, "from"), ColumnPosArgument.getColumnPos(c, "from"), false)))
/*  51 */             .then(
/*  52 */               Commands.argument("to", ColumnPosArgument.columnPos())
/*  53 */               .executes(c -> changeForceLoad((CommandSourceStack)c.getSource(), ColumnPosArgument.getColumnPos(c, "from"), ColumnPosArgument.getColumnPos(c, "to"), false)))))
/*  54 */           .then(
/*  55 */             Commands.literal("all")
/*  56 */             .executes(c -> removeAll((CommandSourceStack)c.getSource())))))
/*     */ 
/*     */         
/*  59 */         .then((
/*  60 */           (LiteralArgumentBuilder)Commands.literal("query")
/*  61 */           .executes(c -> listForceLoad((CommandSourceStack)c.getSource())))
/*  62 */           .then(
/*  63 */             Commands.argument("pos", ColumnPosArgument.columnPos())
/*  64 */             .executes(c -> queryForceLoad((CommandSourceStack)c.getSource(), ColumnPosArgument.getColumnPos(c, "pos"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int queryForceLoad(CommandSourceStack source, ColumnPos pos) throws CommandSyntaxException {
/*  71 */     ChunkPos chunkPos = pos.toChunkPos();
/*  72 */     ServerLevel level = source.getLevel();
/*  73 */     ResourceKey<Level> dimension = level.dimension();
/*  74 */     boolean result = level.getForceLoadedChunks().contains(chunkPos.toLong());
/*     */     
/*  76 */     if (result) {
/*  77 */       source.sendSuccess(() -> Component.translatable("commands.forceload.query.success", new Object[] { Component.translationArg(chunkPos), Component.translationArg(dimension.identifier()) }), false);
/*  78 */       return 1;
/*     */     } 
/*  80 */     throw ERROR_NOT_TICKING.create(chunkPos, dimension.identifier());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int listForceLoad(CommandSourceStack source) {
/*  85 */     ServerLevel level = source.getLevel();
/*  86 */     ResourceKey<Level> dimension = level.dimension();
/*  87 */     LongSet forcedChunks = level.getForceLoadedChunks();
/*  88 */     int chunkCount = forcedChunks.size();
/*     */     
/*  90 */     if (chunkCount > 0) {
/*  91 */       String chunkList = Joiner.on(", ").join(forcedChunks.stream().sorted().map(ChunkPos::new).map(ChunkPos::toString).iterator());
/*     */       
/*  93 */       if (chunkCount == 1) {
/*  94 */         source.sendSuccess(() -> Component.translatable("commands.forceload.list.single", new Object[] { Component.translationArg(dimension.identifier()), chunkList }), false);
/*     */       } else {
/*  96 */         source.sendSuccess(() -> Component.translatable("commands.forceload.list.multiple", new Object[] { Integer.valueOf(chunkCount), Component.translationArg(dimension.identifier()), chunkList }), false);
/*     */       } 
/*     */     } else {
/*  99 */       source.sendFailure(Component.translatable("commands.forceload.added.none", new Object[] { Component.translationArg(dimension.identifier()) }));
/*     */     } 
/* 101 */     return chunkCount;
/*     */   }
/*     */   
/*     */   private static int removeAll(CommandSourceStack source) {
/* 105 */     ServerLevel level = source.getLevel();
/* 106 */     ResourceKey<Level> dimension = level.dimension();
/* 107 */     LongSet forcedChunks = level.getForceLoadedChunks();
/* 108 */     forcedChunks.forEach(chunk -> level.setChunkForced(ChunkPos.getX(chunk), ChunkPos.getZ(chunk), false));
/* 109 */     source.sendSuccess(() -> Component.translatable("commands.forceload.removed.all", new Object[] { Component.translationArg(dimension.identifier()) }), true);
/* 110 */     return 0;
/*     */   }
/*     */   
/*     */   private static int changeForceLoad(CommandSourceStack source, ColumnPos from, ColumnPos to, boolean add) throws CommandSyntaxException {
/* 114 */     int minX = Math.min(from.x(), to.x());
/* 115 */     int minZ = Math.min(from.z(), to.z());
/* 116 */     int maxX = Math.max(from.x(), to.x());
/* 117 */     int maxZ = Math.max(from.z(), to.z());
/*     */     
/* 119 */     if (minX < -30000000 || minZ < -30000000 || maxX >= 30000000 || maxZ >= 30000000)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 124 */       throw BlockPosArgument.ERROR_OUT_OF_WORLD.create();
/*     */     }
/*     */     
/* 127 */     int minChunkX = SectionPos.blockToSectionCoord(minX);
/* 128 */     int minChunkZ = SectionPos.blockToSectionCoord(minZ);
/* 129 */     int maxChunkX = SectionPos.blockToSectionCoord(maxX);
/* 130 */     int maxChunkZ = SectionPos.blockToSectionCoord(maxZ);
/*     */     
/* 132 */     long chunkCount = ((maxChunkX - minChunkX) + 1L) * ((maxChunkZ - minChunkZ) + 1L);
/*     */     
/* 134 */     if (chunkCount > 256L) {
/* 135 */       throw ERROR_TOO_MANY_CHUNKS.create(Integer.valueOf(256), Long.valueOf(chunkCount));
/*     */     }
/*     */     
/* 138 */     ServerLevel level = source.getLevel();
/* 139 */     ResourceKey<Level> dimension = level.dimension();
/*     */     
/* 141 */     ChunkPos firstChanged = null;
/* 142 */     int changedCount = 0;
/* 143 */     for (int x = minChunkX; x <= maxChunkX; x++) {
/* 144 */       for (int z = minChunkZ; z <= maxChunkZ; z++) {
/* 145 */         boolean changed = level.setChunkForced(x, z, add);
/* 146 */         if (changed) {
/* 147 */           changedCount++;
/* 148 */           if (firstChanged == null) {
/* 149 */             firstChanged = new ChunkPos(x, z);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     ChunkPos finalFirstChanged = firstChanged;
/* 156 */     int changedChunks = changedCount;
/* 157 */     if (changedChunks == 0)
/* 158 */       throw (add ? ERROR_ALL_ADDED : ERROR_NONE_REMOVED).create(); 
/* 159 */     if (changedChunks == 1) {
/* 160 */       source.sendSuccess(() -> Component.translatable("commands.forceload." + (add ? "added" : "removed") + ".single", new Object[] { Component.translationArg(finalFirstChanged), Component.translationArg(dimension.identifier()) }), true);
/*     */     } else {
/* 162 */       ChunkPos min = new ChunkPos(minChunkX, minChunkZ);
/* 163 */       ChunkPos max = new ChunkPos(maxChunkX, maxChunkZ);
/* 164 */       source.sendSuccess(() -> Component.translatable("commands.forceload." + (add ? "added" : "removed") + ".multiple", new Object[] { Integer.valueOf(changedChunks), Component.translationArg(dimension.identifier()), Component.translationArg(min), Component.translationArg(max) }), true);
/*     */     } 
/*     */     
/* 167 */     return changedChunks;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ForceLoadCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.DimensionArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CloneCommands
/*     */ {
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  49 */   private static final SimpleCommandExceptionType ERROR_OVERLAP = new SimpleCommandExceptionType(Component.translatable("commands.clone.overlap"));
/*  50 */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.clone.toobig", new Object[] { max, count }));
/*  51 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.clone.failed"));
/*  52 */   public static final Predicate<BlockInWorld> FILTER_AIR = b -> !b.getState().isAir();
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  55 */     dispatcher.register(
/*  56 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clone")
/*  57 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  58 */         .then(
/*  59 */           beginEndDestinationAndModeSuffix(context, c -> ((CommandSourceStack)c.getSource()).getLevel())))
/*     */         
/*  61 */         .then(
/*  62 */           Commands.literal("from")
/*  63 */           .then(
/*  64 */             Commands.argument("sourceDimension", DimensionArgument.dimension())
/*  65 */             .then(
/*  66 */               beginEndDestinationAndModeSuffix(context, c -> DimensionArgument.getDimension(c, "sourceDimension"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> beginEndDestinationAndModeSuffix(CommandBuildContext context, InCommandFunction<CommandContext<CommandSourceStack>, ServerLevel> fromDimension) {
/*  74 */     return Commands.argument("begin", BlockPosArgument.blockPos())
/*  75 */       .then((
/*  76 */         (RequiredArgumentBuilder)Commands.argument("end", BlockPosArgument.blockPos())
/*  77 */         .then(
/*  78 */           destinationAndStrictSuffix(context, fromDimension, c -> ((CommandSourceStack)c.getSource()).getLevel())))
/*     */         
/*  80 */         .then(
/*  81 */           Commands.literal("to")
/*  82 */           .then(
/*  83 */             Commands.argument("targetDimension", DimensionArgument.dimension())
/*  84 */             .then(
/*  85 */               destinationAndStrictSuffix(context, fromDimension, c -> DimensionArgument.getDimension(c, "targetDimension"))))));
/*     */   }
/*     */   private static final class DimensionAndPosition extends Record { private final ServerLevel dimension; private final BlockPos position;
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition; }
/*     */     
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition; }
/*     */     
/*  92 */     private DimensionAndPosition(ServerLevel dimension, BlockPos position) { this.dimension = dimension; this.position = position; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;
/*  92 */       //   0	8	1	o	Ljava/lang/Object; } public ServerLevel dimension() { return this.dimension; } public BlockPos position() { return this.position; } }
/*     */   private static DimensionAndPosition getLoadedDimensionAndPosition(CommandContext<CommandSourceStack> context, ServerLevel level, String positionArgument) throws CommandSyntaxException {
/*  94 */     BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(context, level, positionArgument);
/*  95 */     return new DimensionAndPosition(level, blockPos);
/*     */   }
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> destinationAndStrictSuffix(CommandBuildContext context, InCommandFunction<CommandContext<CommandSourceStack>, ServerLevel> fromDimension, InCommandFunction<CommandContext<CommandSourceStack>, ServerLevel> toDimension) {
/*  99 */     InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> beginPos = c -> getLoadedDimensionAndPosition(c, (ServerLevel)fromDimension.apply(c), "begin");
/* 100 */     InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> endPos = c -> getLoadedDimensionAndPosition(c, (ServerLevel)fromDimension.apply(c), "end");
/* 101 */     InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> destinationPos = c -> getLoadedDimensionAndPosition(c, (ServerLevel)toDimension.apply(c), "destination");
/*     */     
/* 103 */     return modeSuffix(context, beginPos, endPos, destinationPos, false, Commands.argument("destination", BlockPosArgument.blockPos()))
/* 104 */       .then(
/* 105 */         modeSuffix(context, beginPos, endPos, destinationPos, true, Commands.literal("strict")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> modeSuffix(CommandBuildContext context, InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> beginPos, InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> endPos, InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> destinationPos, boolean strict, ArgumentBuilder<CommandSourceStack, ?> builder) {
/* 116 */     return builder
/* 117 */       .executes(c -> clone((CommandSourceStack)c.getSource(), (DimensionAndPosition)beginPos.apply(c), (DimensionAndPosition)endPos.apply(c), (DimensionAndPosition)destinationPos.apply(c), (), Mode.NORMAL, strict))
/* 118 */       .then(
/* 119 */         wrapWithCloneMode(beginPos, endPos, destinationPos, c -> (), strict, 
/* 120 */           Commands.literal("replace")))
/*     */ 
/*     */       
/* 123 */       .then(
/* 124 */         wrapWithCloneMode(beginPos, endPos, destinationPos, c -> FILTER_AIR, strict, 
/* 125 */           Commands.literal("masked")))
/*     */ 
/*     */       
/* 128 */       .then(
/* 129 */         Commands.literal("filtered")
/* 130 */         .then(
/* 131 */           wrapWithCloneMode(beginPos, endPos, destinationPos, c -> BlockPredicateArgument.getBlockPredicate(c, "filter"), strict, 
/* 132 */             Commands.argument("filter", BlockPredicateArgument.blockPredicate(context)))));
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
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> wrapWithCloneMode(InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> beginPos, InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> endPos, InCommandFunction<CommandContext<CommandSourceStack>, DimensionAndPosition> destinationPos, InCommandFunction<CommandContext<CommandSourceStack>, Predicate<BlockInWorld>> filter, boolean strict, ArgumentBuilder<CommandSourceStack, ?> builder) {
/* 146 */     return builder
/* 147 */       .executes(c -> clone((CommandSourceStack)c.getSource(), (DimensionAndPosition)beginPos.apply(c), (DimensionAndPosition)endPos.apply(c), (DimensionAndPosition)destinationPos.apply(c), (Predicate)filter.apply(c), Mode.NORMAL, strict))
/* 148 */       .then(
/* 149 */         Commands.literal("force")
/* 150 */         .executes(c -> clone((CommandSourceStack)c.getSource(), (DimensionAndPosition)beginPos.apply(c), (DimensionAndPosition)endPos.apply(c), (DimensionAndPosition)destinationPos.apply(c), (Predicate)filter.apply(c), Mode.FORCE, strict)))
/*     */       
/* 152 */       .then(
/* 153 */         Commands.literal("move")
/* 154 */         .executes(c -> clone((CommandSourceStack)c.getSource(), (DimensionAndPosition)beginPos.apply(c), (DimensionAndPosition)endPos.apply(c), (DimensionAndPosition)destinationPos.apply(c), (Predicate)filter.apply(c), Mode.MOVE, strict)))
/*     */       
/* 156 */       .then(
/* 157 */         Commands.literal("normal")
/* 158 */         .executes(c -> clone((CommandSourceStack)c.getSource(), (DimensionAndPosition)beginPos.apply(c), (DimensionAndPosition)endPos.apply(c), (DimensionAndPosition)destinationPos.apply(c), (Predicate)filter.apply(c), Mode.NORMAL, strict)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int clone(CommandSourceStack source, DimensionAndPosition startPosAndDimension, DimensionAndPosition endPosAndDimension, DimensionAndPosition destPosAndDimension, Predicate<BlockInWorld> predicate, Mode mode, boolean strict) throws CommandSyntaxException {
/* 163 */     BlockPos startPos = startPosAndDimension.position();
/* 164 */     BlockPos endPos = endPosAndDimension.position();
/* 165 */     BoundingBox from = BoundingBox.fromCorners(startPos, endPos);
/* 166 */     BlockPos destPos = destPosAndDimension.position();
/* 167 */     BlockPos destEndPos = destPos.offset(from.getLength());
/* 168 */     BoundingBox destination = BoundingBox.fromCorners(destPos, destEndPos);
/* 169 */     ServerLevel fromDimension = startPosAndDimension.dimension();
/* 170 */     ServerLevel toDimension = destPosAndDimension.dimension();
/*     */     
/* 172 */     if (!mode.canOverlap() && fromDimension == toDimension && destination.intersects(from)) {
/* 173 */       throw ERROR_OVERLAP.create();
/*     */     }
/* 175 */     int area = from.getXSpan() * from.getYSpan() * from.getZSpan();
/* 176 */     int limit = ((Integer)source.getLevel().getGameRules().get(GameRules.MAX_BLOCK_MODIFICATIONS)).intValue();
/* 177 */     if (area > limit) {
/* 178 */       throw ERROR_AREA_TOO_LARGE.create(Integer.valueOf(limit), Integer.valueOf(area));
/*     */     }
/* 180 */     if (!fromDimension.hasChunksAt(startPos, endPos) || !toDimension.hasChunksAt(destPos, destEndPos)) {
/* 181 */       throw BlockPosArgument.ERROR_NOT_LOADED.create();
/*     */     }
/* 183 */     if (toDimension.isDebug()) {
/* 184 */       throw ERROR_FAILED.create();
/*     */     }
/*     */     
/* 187 */     List<CloneBlockInfo> solidList = Lists.newArrayList();
/* 188 */     List<CloneBlockInfo> blockEntitiesList = Lists.newArrayList();
/* 189 */     List<CloneBlockInfo> otherBlocksList = Lists.newArrayList();
/* 190 */     Deque<BlockPos> clearBlocksList = Lists.newLinkedList();
/* 191 */     int count = 0;
/*     */     
/* 193 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/* 194 */     try { BlockPos offset = new BlockPos(destination.minX() - from.minX(), destination.minY() - from.minY(), destination.minZ() - from.minZ());
/* 195 */       for (int z = from.minZ(); z <= from.maxZ(); z++) {
/* 196 */         for (int y = from.minY(); y <= from.maxY(); y++) {
/* 197 */           for (int x = from.minX(); x <= from.maxX(); x++) {
/* 198 */             BlockPos sourcePos = new BlockPos(x, y, z);
/* 199 */             BlockPos destinationPos = sourcePos.offset(offset);
/* 200 */             BlockInWorld block = new BlockInWorld(fromDimension, sourcePos, false);
/* 201 */             BlockState blockState = block.getState();
/* 202 */             if (predicate.test(block)) {
/*     */ 
/*     */ 
/*     */               
/* 206 */               BlockEntity blockEntity = fromDimension.getBlockEntity(sourcePos);
/* 207 */               if (blockEntity != null) {
/* 208 */                 TagValueOutput output = TagValueOutput.createWithContext(reporter.forChild(blockEntity.problemPath()), source.registryAccess());
/* 209 */                 blockEntity.saveCustomOnly(output);
/*     */ 
/*     */                 
/* 212 */                 CloneBlockEntityInfo blockEntityInfo = new CloneBlockEntityInfo(output.buildResult(), blockEntity.components());
/*     */                 
/* 214 */                 blockEntitiesList.add(new CloneBlockInfo(destinationPos, blockState, blockEntityInfo, toDimension.getBlockState(destinationPos)));
/* 215 */                 clearBlocksList.addLast(sourcePos);
/* 216 */               } else if (blockState.isSolidRender() || blockState.isCollisionShapeFullBlock(fromDimension, sourcePos)) {
/* 217 */                 solidList.add(new CloneBlockInfo(destinationPos, blockState, null, toDimension.getBlockState(destinationPos)));
/* 218 */                 clearBlocksList.addLast(sourcePos);
/*     */               } else {
/* 220 */                 otherBlocksList.add(new CloneBlockInfo(destinationPos, blockState, null, toDimension.getBlockState(destinationPos)));
/* 221 */                 clearBlocksList.addFirst(sourcePos);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 227 */       int defaultUpdateFlags = 0x2 | (strict ? 816 : 0);
/*     */       
/* 229 */       if (mode == Mode.MOVE) {
/* 230 */         for (BlockPos pos : clearBlocksList) {
/* 231 */           fromDimension.setBlock(pos, Blocks.BARRIER.defaultBlockState(), defaultUpdateFlags | 0x330);
/*     */         }
/* 233 */         int standardUpdateFlags = strict ? defaultUpdateFlags : 3;
/* 234 */         for (BlockPos pos : clearBlocksList) {
/* 235 */           fromDimension.setBlock(pos, Blocks.AIR.defaultBlockState(), standardUpdateFlags);
/*     */         }
/*     */       } 
/*     */       
/* 239 */       List<CloneBlockInfo> blockInfoList = Lists.newArrayList();
/* 240 */       blockInfoList.addAll(solidList);
/* 241 */       blockInfoList.addAll(blockEntitiesList);
/* 242 */       blockInfoList.addAll(otherBlocksList);
/*     */       
/* 244 */       List<CloneBlockInfo> reverse = Lists.reverse(blockInfoList);
/* 245 */       for (CloneBlockInfo cloneInfo : reverse) {
/* 246 */         toDimension.setBlock(cloneInfo.pos, Blocks.BARRIER.defaultBlockState(), defaultUpdateFlags | 0x330);
/*     */       }
/*     */       
/* 249 */       for (CloneBlockInfo cloneInfo : blockInfoList) {
/* 250 */         if (toDimension.setBlock(cloneInfo.pos, cloneInfo.state, defaultUpdateFlags)) {
/* 251 */           count++;
/*     */         }
/*     */       } 
/*     */       
/* 255 */       for (CloneBlockInfo cloneInfo : blockEntitiesList) {
/* 256 */         BlockEntity newBlockEntity = toDimension.getBlockEntity(cloneInfo.pos);
/* 257 */         if (cloneInfo.blockEntityInfo != null && newBlockEntity != null) {
/* 258 */           newBlockEntity.loadCustomOnly(TagValueInput.create(reporter.forChild(newBlockEntity.problemPath()), toDimension.registryAccess(), cloneInfo.blockEntityInfo.tag));
/*     */           
/* 260 */           newBlockEntity.setComponents(cloneInfo.blockEntityInfo.components);
/* 261 */           newBlockEntity.setChanged();
/*     */         } 
/* 263 */         toDimension.setBlock(cloneInfo.pos, cloneInfo.state, defaultUpdateFlags);
/*     */       } 
/*     */       
/* 266 */       if (!strict) {
/* 267 */         for (CloneBlockInfo cloneInfo : reverse) {
/* 268 */           toDimension.updateNeighboursOnBlockSet(cloneInfo.pos, cloneInfo.previousStateAtDestination);
/*     */         }
/*     */       }
/*     */       
/* 272 */       toDimension.getBlockTicks().copyAreaFrom(fromDimension.getBlockTicks(), from, offset);
/* 273 */       reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 275 */      if (count == 0) {
/* 276 */       throw ERROR_FAILED.create();
/*     */     }
/*     */     
/* 279 */     int finalCount = count;
/* 280 */     source.sendSuccess(() -> Component.translatable("commands.clone.success", new Object[] { Integer.valueOf(finalCount) }), true);
/*     */     
/* 282 */     return count;
/*     */   }
/*     */   
/*     */   private enum Mode {
/* 286 */     FORCE(true),
/* 287 */     MOVE(true),
/* 288 */     NORMAL(false);
/*     */ 
/*     */     
/*     */     private final boolean canOverlap;
/*     */ 
/*     */     
/* 294 */     Mode(boolean canOverlap) { this.canOverlap = canOverlap; }
/*     */ 
/*     */ 
/*     */     
/* 298 */     public boolean canOverlap() { return this.canOverlap; } }
/*     */   private static final class CloneBlockEntityInfo extends Record { private final CompoundTag tag;
/*     */     private final DataComponentMap components;
/*     */     
/* 302 */     private CloneBlockEntityInfo(CompoundTag tag, DataComponentMap components) { this.tag = tag; this.components = components; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #302	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 302 */       //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo; } public CompoundTag tag() { return this.tag; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #302	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #302	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;
/* 302 */       //   0	8	1	o	Ljava/lang/Object; } public DataComponentMap components() { return this.components; } }
/*     */   private static final class CloneBlockInfo extends Record { private final BlockPos pos; private final BlockState state; private final CloneCommands.CloneBlockEntityInfo blockEntityInfo; private final BlockState previousStateAtDestination;
/* 304 */     private CloneBlockInfo(BlockPos pos, BlockState state, CloneCommands.CloneBlockEntityInfo blockEntityInfo, BlockState previousStateAtDestination) { this.pos = pos; this.state = state; this.blockEntityInfo = blockEntityInfo; this.previousStateAtDestination = previousStateAtDestination; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #304	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #304	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #304	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;
/* 304 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; } public BlockState state() { return this.state; } public CloneCommands.CloneBlockEntityInfo blockEntityInfo() { return this.blockEntityInfo; } public BlockState previousStateAtDestination() { return this.previousStateAtDestination; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\CloneCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
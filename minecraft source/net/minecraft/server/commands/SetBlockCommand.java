/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*    */ import net.minecraft.commands.arguments.blocks.BlockStateArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetBlockCommand
/*    */ {
/* 28 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setblock.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 31 */     Predicate<BlockInWorld> filter = b -> b.getLevel().isEmptyBlock(b.getPos());
/* 32 */     dispatcher.register(
/* 33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setblock")
/* 34 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 35 */         .then(
/* 36 */           Commands.argument("pos", BlockPosArgument.blockPos())
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("block", BlockStateArgument.block(context))
/* 39 */             .executes(c -> setBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), BlockStateArgument.getBlock(c, "block"), Mode.REPLACE, null, false)))
/* 40 */             .then(
/* 41 */               Commands.literal("destroy")
/* 42 */               .executes(c -> setBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), BlockStateArgument.getBlock(c, "block"), Mode.DESTROY, null, false))))
/*    */             
/* 44 */             .then(
/* 45 */               Commands.literal("keep")
/* 46 */               .executes(c -> setBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), BlockStateArgument.getBlock(c, "block"), Mode.REPLACE, filter, false))))
/*    */             
/* 48 */             .then(
/* 49 */               Commands.literal("replace")
/* 50 */               .executes(c -> setBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), BlockStateArgument.getBlock(c, "block"), Mode.REPLACE, null, false))))
/*    */             
/* 52 */             .then(
/* 53 */               Commands.literal("strict")
/* 54 */               .executes(c -> setBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), BlockStateArgument.getBlock(c, "block"), Mode.REPLACE, null, true))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setBlock(CommandSourceStack source, BlockPos pos, BlockInput block, Mode mode, Predicate<BlockInWorld> predicate, boolean strict) throws CommandSyntaxException {
/*    */     boolean placeNeeded;
/* 62 */     ServerLevel level = source.getLevel();
/* 63 */     if (level.isDebug()) {
/* 64 */       throw ERROR_FAILED.create();
/*    */     }
/* 66 */     if (predicate != null && !predicate.test(new BlockInWorld(level, pos, true))) {
/* 67 */       throw ERROR_FAILED.create();
/*    */     }
/*    */ 
/*    */     
/* 71 */     if (mode == Mode.DESTROY) {
/* 72 */       level.destroyBlock(pos, true);
/* 73 */       placeNeeded = (!block.getState().isAir() || !level.getBlockState(pos).isAir());
/*    */     } else {
/* 75 */       placeNeeded = true;
/*    */     } 
/* 77 */     BlockState oldState = level.getBlockState(pos);
/* 78 */     if (placeNeeded && !block.place(level, pos, 0x2 | (strict ? 816 : 256))) {
/* 79 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 82 */     if (!strict) {
/* 83 */       level.updateNeighboursOnBlockSet(pos, oldState);
/*    */     }
/* 85 */     source.sendSuccess(() -> Component.translatable("commands.setblock.success", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }), true);
/* 86 */     return 1;
/*    */   }
/*    */   
/*    */   public enum Mode {
/* 90 */     REPLACE,
/* 91 */     DESTROY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SetBlockCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
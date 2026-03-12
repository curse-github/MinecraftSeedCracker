/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugPathCommand
/*    */ {
/* 22 */   private static final SimpleCommandExceptionType ERROR_NOT_MOB = new SimpleCommandExceptionType(Component.literal("Source is not a mob"));
/* 23 */   private static final SimpleCommandExceptionType ERROR_NO_PATH = new SimpleCommandExceptionType(Component.literal("Path not found"));
/* 24 */   private static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.literal("Target not reached"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 27 */     dispatcher.register(
/* 28 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debugpath")
/* 29 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 30 */         .then(
/* 31 */           Commands.argument("to", BlockPosArgument.blockPos())
/* 32 */           .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "to")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int fillBlocks(CommandSourceStack source, BlockPos target) throws CommandSyntaxException {
/* 38 */     Entity entity = source.getEntity();
/* 39 */     if (!(entity instanceof Mob)) {
/* 40 */       throw ERROR_NOT_MOB.create();
/*    */     }
/*    */     
/* 43 */     Mob mob = (Mob)entity;
/*    */     
/* 45 */     GroundPathNavigation groundPathNavigation = new GroundPathNavigation(mob, source.getLevel());
/* 46 */     Path path = groundPathNavigation.createPath(target, 0);
/*    */     
/* 48 */     if (path == null) {
/* 49 */       throw ERROR_NO_PATH.create();
/*    */     }
/* 51 */     if (!path.canReach()) {
/* 52 */       throw ERROR_NOT_COMPLETE.create();
/*    */     }
/*    */     
/* 55 */     source.sendSuccess(() -> Component.literal("Made path"), true);
/* 56 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugPathCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
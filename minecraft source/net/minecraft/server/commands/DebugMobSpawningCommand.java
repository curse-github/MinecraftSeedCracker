/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugMobSpawningCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 19 */     LiteralArgumentBuilder<CommandSourceStack> base = (LiteralArgumentBuilder)Commands.literal("debugmobspawning").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*    */     
/* 21 */     for (MobCategory mobCategory : MobCategory.values()) {
/* 22 */       base.then(
/* 23 */           Commands.literal(mobCategory.getName())
/* 24 */           .then(
/* 25 */             Commands.argument("at", BlockPosArgument.blockPos())
/* 26 */             .executes(c -> spawnMobs((CommandSourceStack)c.getSource(), mobCategory, BlockPosArgument.getLoadedBlockPos(c, "at")))));
/*    */     }
/*    */ 
/*    */     
/* 30 */     dispatcher.register(base);
/*    */   }
/*    */   
/*    */   private static int spawnMobs(CommandSourceStack source, MobCategory mobCategory, BlockPos at) {
/* 34 */     NaturalSpawner.spawnCategoryForPosition(mobCategory, source.getLevel(), at);
/* 35 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugMobSpawningCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
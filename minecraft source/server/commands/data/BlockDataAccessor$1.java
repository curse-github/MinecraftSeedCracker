/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements DataCommands.DataProvider
/*    */ {
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 38 */     BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, argPrefix + "Pos");
/* 39 */     BlockEntity entity = ((CommandSourceStack)context.getSource()).getLevel().getBlockEntity(pos);
/* 40 */     if (entity == null) {
/* 41 */       throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
/*    */     }
/* 43 */     return new BlockDataAccessor(entity, pos);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) { return parent.then(Commands.literal("block").then((ArgumentBuilder)function.apply(Commands.argument(argPrefix + "Pos", BlockPosArgument.blockPos())))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\BlockDataAccessor$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
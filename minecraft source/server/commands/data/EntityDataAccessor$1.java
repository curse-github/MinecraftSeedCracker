/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements DataCommands.DataProvider
/*    */ {
/* 38 */   public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException { return new EntityDataAccessor(EntityArgument.getEntity(context, arg)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) { return parent.then(Commands.literal("entity").then((ArgumentBuilder)function.apply(Commands.argument(arg, EntityArgument.entity())))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\EntityDataAccessor$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
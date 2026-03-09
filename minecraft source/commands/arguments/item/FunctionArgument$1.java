/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
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
/*    */   implements FunctionArgument.Result
/*    */ {
/*    */   null(FunctionArgument this$0) {}
/*    */   
/* 36 */   public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> c) throws CommandSyntaxException { return FunctionArgument.getFunctionTag(c, id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> context) throws CommandSyntaxException { return Pair.of(id, Either.right(FunctionArgument.getFunctionTag(context, id))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException { return Pair.of(id, FunctionArgument.getFunctionTag(context, id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\FunctionArgument$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
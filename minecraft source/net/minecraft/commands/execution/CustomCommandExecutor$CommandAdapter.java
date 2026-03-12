/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.Command;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CommandAdapter<T>
/*    */   extends CustomCommandExecutor<T>, Command<T>
/*    */ {
/* 16 */   default int run(CommandContext<T> context) throws CommandSyntaxException { throw new UnsupportedOperationException("This function should not run"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\CustomCommandExecutor$CommandAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
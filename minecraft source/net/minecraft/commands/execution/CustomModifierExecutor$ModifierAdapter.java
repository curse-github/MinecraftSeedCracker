/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.RedirectModifier;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ModifierAdapter<T>
/*    */   extends CustomModifierExecutor<T>, RedirectModifier<T>
/*    */ {
/* 17 */   default Collection<T> apply(CommandContext<T> context) throws CommandSyntaxException { throw new UnsupportedOperationException("This function should not run"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\CustomModifierExecutor$ModifierAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
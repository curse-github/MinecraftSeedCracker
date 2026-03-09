/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.ResultConsumer;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.execution.TraceCallbacks;
/*    */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*    */ 
/*    */ public interface ExecutionCommandSource<T extends ExecutionCommandSource<T>>
/*    */   extends PermissionSetSupplier {
/*    */   T withCallback(CommandResultCallback paramCommandResultCallback);
/*    */   
/*    */   CommandResultCallback callback();
/*    */   
/* 18 */   default T clearCallbacks() { return (T)withCallback(CommandResultCallback.EMPTY); }
/*    */ 
/*    */ 
/*    */   
/*    */   CommandDispatcher<T> dispatcher();
/*    */ 
/*    */ 
/*    */   
/*    */   void handleError(CommandExceptionType paramCommandExceptionType, Message paramMessage, boolean paramBoolean, TraceCallbacks paramTraceCallbacks);
/*    */ 
/*    */ 
/*    */   
/*    */   boolean isSilent();
/*    */ 
/*    */   
/* 33 */   default void handleError(CommandSyntaxException e, boolean forked, TraceCallbacks tracer) { handleError(e.getType(), e.getRawMessage(), forked, tracer); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   static <T extends ExecutionCommandSource<T>> ResultConsumer<T> resultConsumer() { return (context, success, result) -> ((ExecutionCommandSource)context.getSource()).callback().onResult(success, result); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\ExecutionCommandSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
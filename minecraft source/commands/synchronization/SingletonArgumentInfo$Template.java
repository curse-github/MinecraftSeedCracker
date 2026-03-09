/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Template
/*    */   extends Object
/*    */   implements ArgumentTypeInfo.Template<A>
/*    */ {
/*    */   private final Function<CommandBuildContext, A> constructor;
/*    */   
/* 16 */   public Template(Function<CommandBuildContext, A> constructor) { this.constructor = constructor; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public A instantiate(CommandBuildContext context) { return (A)(ArgumentType)this.constructor.apply(context); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public ArgumentTypeInfo<A, ?> type() { return SingletonArgumentInfo.this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\SingletonArgumentInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
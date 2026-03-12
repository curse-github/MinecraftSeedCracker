/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class SingletonArgumentInfo<A extends ArgumentType<?>> extends Object implements ArgumentTypeInfo<A, SingletonArgumentInfo<A>.Template> {
/*    */   private final Template template;
/*    */   
/*    */   public final class Template extends Object implements ArgumentTypeInfo.Template<A> {
/*    */     private final Function<CommandBuildContext, A> constructor;
/*    */     
/* 16 */     public Template(Function<CommandBuildContext, A> constructor) { this.constructor = constructor; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     public A instantiate(CommandBuildContext context) { return (A)(ArgumentType)this.constructor.apply(context); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     public ArgumentTypeInfo<A, ?> type() { return SingletonArgumentInfo.this; }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   private SingletonArgumentInfo(Function<CommandBuildContext, A> constructor) { this.template = new Template(constructor); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> contextFree(Supplier<T> constructor) { return new SingletonArgumentInfo(context -> (ArgumentType)constructor.get()); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> contextAware(Function<CommandBuildContext, T> constructor) { return new SingletonArgumentInfo(constructor); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {}
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Template deserializeFromNetwork(FriendlyByteBuf in) { return this.template; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Template unpack(A argument) { return this.template; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\SingletonArgumentInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
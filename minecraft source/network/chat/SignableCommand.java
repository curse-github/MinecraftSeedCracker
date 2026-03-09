/*    */ package net.minecraft.network.chat;
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.context.ParsedArgument;
/*    */ import com.mojang.brigadier.context.ParsedCommandNode;
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class SignableCommand<S> extends Record {
/*    */   private final List<Argument<S>> arguments;
/*    */   
/* 14 */   public SignableCommand(List<Argument<S>> arguments) { this.arguments = arguments; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignableCommand;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand<TS;>; } public List<Argument<S>> arguments() { return this.arguments; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignableCommand;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand<TS;>; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignableCommand;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand<TS;>; }
/* 16 */   public static <S> boolean hasSignableArguments(ParseResults<S> command) { return !of(command).arguments().isEmpty(); }
/*    */ 
/*    */   
/*    */   public static <S> SignableCommand<S> of(ParseResults<S> command) {
/* 20 */     String commandString = command.getReader().getString();
/* 21 */     CommandContextBuilder<S> rootContext = command.getContext();
/*    */     
/* 23 */     CommandContextBuilder<S> context = rootContext;
/*    */     
/* 25 */     List<Argument<S>> arguments = collectArguments(commandString, context);
/*    */     
/*    */     CommandContextBuilder<S> child;
/* 28 */     while ((child = context.getChild()) != null) {
/*    */ 
/*    */       
/* 31 */       if (child.getRootNode() == rootContext.getRootNode()) {
/*    */         break;
/*    */       }
/*    */       
/* 35 */       arguments.addAll(collectArguments(commandString, child));
/* 36 */       context = child;
/*    */     } 
/*    */     
/* 39 */     return new SignableCommand(arguments);
/*    */   }
/*    */   
/*    */   private static <S> List<Argument<S>> collectArguments(String commandString, CommandContextBuilder<S> context) {
/* 43 */     List<Argument<S>> arguments = new ArrayList<Argument<S>>();
/* 44 */     for (ParsedCommandNode<S> node : context.getNodes()) {
/* 45 */       CommandNode commandNode = node.getNode(); if (commandNode instanceof ArgumentCommandNode) { ArgumentCommandNode<S, ?> argument = (ArgumentCommandNode)commandNode; if (argument.getType() instanceof net.minecraft.commands.arguments.SignedArgument) {
/* 46 */           ParsedArgument<S, ?> parsed = (ParsedArgument)context.getArguments().get(argument.getName());
/* 47 */           if (parsed != null) {
/* 48 */             String value = parsed.getRange().get(commandString);
/* 49 */             arguments.add(new Argument(argument, value));
/*    */           } 
/*    */         }  }
/*    */     
/* 53 */     }  return arguments;
/*    */   }
/*    */   
/*    */   public Argument<S> getArgument(String name) {
/* 57 */     for (Argument<S> argument : this.arguments) {
/* 58 */       if (name.equals(argument.name())) {
/* 59 */         return argument;
/*    */       }
/*    */     } 
/* 62 */     return null;
/*    */   }
/*    */   public static final class Argument<S> extends Record { private final ArgumentCommandNode<S, ?> node; private final String value;
/* 65 */     public Argument(ArgumentCommandNode<S, ?> node, String value) { this.node = node; this.value = value; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignableCommand$Argument;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignableCommand$Argument;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignableCommand$Argument;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 65 */       //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>; } public ArgumentCommandNode<S, ?> node() { return this.node; } public String value() { return this.value; }
/*    */     
/* 67 */     public String name() { return this.node.getName(); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\SignableCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
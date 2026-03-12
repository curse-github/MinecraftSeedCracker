/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.commands.CommandSigningContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.network.chat.ChatDecorator;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.PlayerChatMessage;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.network.FilteredText;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ 
/*     */ public class MessageArgument
/*     */   extends Object
/*     */   implements SignedArgument<MessageArgument.Message> {
/*  30 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Hello world!", "foo", "@e", "Hello @p :)" });
/*     */   
/*  32 */   private static final Dynamic2CommandExceptionType TOO_LONG = new Dynamic2CommandExceptionType((length, maxLength) -> Component.translatableEscape("argument.message.too_long", new Object[] { length, maxLength }));
/*     */ 
/*     */   
/*  35 */   public static MessageArgument message() { return new MessageArgument(); }
/*     */ 
/*     */   
/*     */   public static Component getMessage(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/*  39 */     Message message = (Message)context.getArgument(name, Message.class);
/*  40 */     return message.resolveComponent((CommandSourceStack)context.getSource());
/*     */   }
/*     */   
/*     */   public static void resolveChatMessage(CommandContext<CommandSourceStack> context, String name, Consumer<PlayerChatMessage> task) throws CommandSyntaxException {
/*  44 */     Message message = (Message)context.getArgument(name, Message.class);
/*  45 */     CommandSourceStack sender = (CommandSourceStack)context.getSource();
/*  46 */     Component formatted = message.resolveComponent(sender);
/*     */     
/*  48 */     CommandSigningContext signingContext = sender.getSigningContext();
/*  49 */     PlayerChatMessage signedArgument = signingContext.getArgument(name);
/*  50 */     if (signedArgument != null) {
/*  51 */       resolveSignedMessage(task, sender, signedArgument.withUnsignedContent(formatted));
/*     */     } else {
/*  53 */       resolveDisguisedMessage(task, sender, PlayerChatMessage.system(message.text).withUnsignedContent(formatted));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void resolveSignedMessage(Consumer<PlayerChatMessage> task, CommandSourceStack sender, PlayerChatMessage signedArgument) {
/*  58 */     MinecraftServer server = sender.getServer();
/*  59 */     CompletableFuture<FilteredText> filteredFuture = filterPlainText(sender, signedArgument);
/*  60 */     Component decorated = server.getChatDecorator().decorate(sender.getPlayer(), signedArgument.decoratedContent());
/*     */     
/*  62 */     sender.getChatMessageChainer().append(filteredFuture, filtered -> {
/*     */ 
/*     */           
/*  65 */           PlayerChatMessage filteredMessage = signedArgument.withUnsignedContent(decorated).filter(filtered.mask());
/*  66 */           task.accept(filteredMessage);
/*     */         });
/*     */   }
/*     */   
/*     */   private static void resolveDisguisedMessage(Consumer<PlayerChatMessage> task, CommandSourceStack sender, PlayerChatMessage argument) {
/*  71 */     ChatDecorator decorator = sender.getServer().getChatDecorator();
/*  72 */     Component decorated = decorator.decorate(sender.getPlayer(), argument.decoratedContent());
/*  73 */     task.accept(argument.withUnsignedContent(decorated));
/*     */   }
/*     */   
/*     */   private static CompletableFuture<FilteredText> filterPlainText(CommandSourceStack sender, PlayerChatMessage message) {
/*  77 */     ServerPlayer player = sender.getPlayer();
/*  78 */     if (player != null && message.hasSignatureFrom(player.getUUID())) {
/*  79 */       return player.getTextFilter().processStreamMessage(message.signedContent());
/*     */     }
/*  81 */     return CompletableFuture.completedFuture(FilteredText.passThrough(message.signedContent()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public Message parse(StringReader reader) throws CommandSyntaxException { return Message.parseText(reader, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public <S> Message parse(StringReader reader, S source) throws CommandSyntaxException { return Message.parseText(reader, EntitySelectorParser.allowSelectors(source)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */   public static final class Message extends Record { private final String text; private final MessageArgument.Part[] parts;
/*     */     
/* 101 */     public Message(String text, Part[] parts) { this.text = text; this.parts = parts; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/MessageArgument$Message;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 101 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Message; } public String text() { return this.text; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/MessageArgument$Message;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Message; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/MessageArgument$Message;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Message;
/* 101 */       //   0	8	1	o	Ljava/lang/Object; } public MessageArgument.Part[] parts() { return this.parts; }
/*     */     
/* 103 */     private Component resolveComponent(CommandSourceStack sender) throws CommandSyntaxException { return toComponent(sender, sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS)); }
/*     */ 
/*     */     
/*     */     public Component toComponent(CommandSourceStack sender, boolean allowSelectors) throws CommandSyntaxException {
/* 107 */       if (this.parts.length == 0 || !allowSelectors) {
/* 108 */         return Component.literal(this.text);
/*     */       }
/*     */       
/* 111 */       MutableComponent result = Component.literal(this.text.substring(0, this.parts[0].start()));
/* 112 */       int readTo = this.parts[0].start();
/*     */       
/* 114 */       for (MessageArgument.Part part : this.parts) {
/* 115 */         Component component = part.toComponent(sender);
/* 116 */         if (readTo < part.start()) {
/* 117 */           result.append(this.text.substring(readTo, part.start()));
/*     */         }
/* 119 */         result.append(component);
/* 120 */         readTo = part.end();
/*     */       } 
/*     */       
/* 123 */       if (readTo < this.text.length()) {
/* 124 */         result.append(this.text.substring(readTo));
/*     */       }
/*     */       
/* 127 */       return result;
/*     */     }
/*     */     
/*     */     public static Message parseText(StringReader reader, boolean allowSelectors) throws CommandSyntaxException {
/* 131 */       if (reader.getRemainingLength() > 256) {
/* 132 */         throw MessageArgument.TOO_LONG.create(Integer.valueOf(reader.getRemainingLength()), Integer.valueOf(256));
/*     */       }
/*     */       
/* 135 */       String text = reader.getRemaining();
/*     */       
/* 137 */       if (!allowSelectors) {
/* 138 */         reader.setCursor(reader.getTotalLength());
/* 139 */         return new Message(text, new MessageArgument.Part[0]);
/*     */       } 
/*     */       
/* 142 */       List<MessageArgument.Part> result = Lists.newArrayList();
/* 143 */       int offset = reader.getCursor();
/*     */       
/* 145 */       while (reader.canRead()) {
/* 146 */         if (reader.peek() == '@') {
/* 147 */           EntitySelector parse; int start = reader.getCursor();
/*     */           
/*     */           try {
/* 150 */             EntitySelectorParser parser = new EntitySelectorParser(reader, true);
/* 151 */             parse = parser.parse();
/* 152 */           } catch (CommandSyntaxException ex) {
/* 153 */             if (ex.getType() == EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE || ex.getType() == EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE) {
/* 154 */               reader.setCursor(start + 1);
/*     */               continue;
/*     */             } 
/* 157 */             throw ex;
/*     */           } 
/* 159 */           result.add(new MessageArgument.Part(start - offset, reader.getCursor() - offset, parse)); continue;
/*     */         } 
/* 161 */         reader.skip();
/*     */       } 
/*     */ 
/*     */       
/* 165 */       return new Message(text, (Part[])result.toArray(new MessageArgument.Part[0]));
/*     */     } }
/*     */   public static final class Part extends Record { private final int start; private final int end; private final EntitySelector selector;
/*     */     
/* 169 */     public Part(int start, int end, EntitySelector selector) { this.start = start; this.end = end; this.selector = selector; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/MessageArgument$Part;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Part; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/MessageArgument$Part;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Part; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/MessageArgument$Part;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Part;
/* 169 */       //   0	8	1	o	Ljava/lang/Object; } public int start() { return this.start; } public int end() { return this.end; } public EntitySelector selector() { return this.selector; }
/*     */     
/* 171 */     public Component toComponent(CommandSourceStack sender) throws CommandSyntaxException { return EntitySelector.joinNames(this.selector.findEntities(sender)); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\MessageArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
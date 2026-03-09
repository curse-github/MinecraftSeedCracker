/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Message
/*     */   extends Record
/*     */ {
/*     */   private final String text;
/*     */   private final MessageArgument.Part[] parts;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/MessageArgument$Message;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #101	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Message; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/MessageArgument$Message;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #101	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Message; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/MessageArgument$Message;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #101	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Message;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 101 */   public Message(String text, Part[] parts) { this.text = text; this.parts = parts; } public String text() { return this.text; } public MessageArgument.Part[] parts() { return this.parts; }
/*     */   
/* 103 */   private Component resolveComponent(CommandSourceStack sender) throws CommandSyntaxException { return toComponent(sender, sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS)); }
/*     */ 
/*     */   
/*     */   public Component toComponent(CommandSourceStack sender, boolean allowSelectors) throws CommandSyntaxException {
/* 107 */     if (this.parts.length == 0 || !allowSelectors) {
/* 108 */       return Component.literal(this.text);
/*     */     }
/*     */     
/* 111 */     MutableComponent result = Component.literal(this.text.substring(0, this.parts[0].start()));
/* 112 */     int readTo = this.parts[0].start();
/*     */     
/* 114 */     for (MessageArgument.Part part : this.parts) {
/* 115 */       Component component = part.toComponent(sender);
/* 116 */       if (readTo < part.start()) {
/* 117 */         result.append(this.text.substring(readTo, part.start()));
/*     */       }
/* 119 */       result.append(component);
/* 120 */       readTo = part.end();
/*     */     } 
/*     */     
/* 123 */     if (readTo < this.text.length()) {
/* 124 */       result.append(this.text.substring(readTo));
/*     */     }
/*     */     
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   public static Message parseText(StringReader reader, boolean allowSelectors) throws CommandSyntaxException {
/* 131 */     if (reader.getRemainingLength() > 256) {
/* 132 */       throw MessageArgument.TOO_LONG.create(Integer.valueOf(reader.getRemainingLength()), Integer.valueOf(256));
/*     */     }
/*     */     
/* 135 */     String text = reader.getRemaining();
/*     */     
/* 137 */     if (!allowSelectors) {
/* 138 */       reader.setCursor(reader.getTotalLength());
/* 139 */       return new Message(text, new MessageArgument.Part[0]);
/*     */     } 
/*     */     
/* 142 */     List<MessageArgument.Part> result = Lists.newArrayList();
/* 143 */     int offset = reader.getCursor();
/*     */     
/* 145 */     while (reader.canRead()) {
/* 146 */       if (reader.peek() == '@') {
/* 147 */         EntitySelector parse; int start = reader.getCursor();
/*     */         
/*     */         try {
/* 150 */           EntitySelectorParser parser = new EntitySelectorParser(reader, true);
/* 151 */           parse = parser.parse();
/* 152 */         } catch (CommandSyntaxException ex) {
/* 153 */           if (ex.getType() == EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE || ex.getType() == EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE) {
/* 154 */             reader.setCursor(start + 1);
/*     */             continue;
/*     */           } 
/* 157 */           throw ex;
/*     */         } 
/* 159 */         result.add(new MessageArgument.Part(start - offset, reader.getCursor() - offset, parse)); continue;
/*     */       } 
/* 161 */       reader.skip();
/*     */     } 
/*     */ 
/*     */     
/* 165 */     return new Message(text, (Part[])result.toArray(new MessageArgument.Part[0]));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\MessageArgument$Message.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
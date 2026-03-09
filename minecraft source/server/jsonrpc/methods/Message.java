/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class Message extends Record {
/*    */   private final Optional<String> literal;
/*    */   private final Optional<String> translatable;
/*    */   private final Optional<List<String>> translatableParams;
/*    */   
/* 10 */   public Message(Optional<String> literal, Optional<String> translatable, Optional<List<String>> translatableParams) { this.literal = literal; this.translatable = translatable; this.translatableParams = translatableParams; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/Message;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/Message; } public Optional<String> literal() { return this.literal; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/Message;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/Message; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/Message;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/Message;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<String> translatable() { return this.translatable; } public Optional<List<String>> translatableParams() { return this.translatableParams; }
/* 11 */   public static final Codec<Message> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 12 */         .optionalFieldOf("literal").forGetter(Message::literal), Codec.STRING
/* 13 */         .optionalFieldOf("translatable").forGetter(Message::translatable), Codec.STRING
/* 14 */         .listOf().lenientOptionalFieldOf("translatableParams").forGetter(Message::translatableParams))
/* 15 */       .apply(i, Message::new));
/*    */   
/*    */   public Optional<Component> asComponent() {
/* 18 */     if (this.translatable.isPresent()) {
/* 19 */       String translationKey = (String)this.translatable.get();
/* 20 */       if (this.translatableParams.isPresent()) {
/* 21 */         List<String> translationArgs = (List)this.translatableParams.get();
/*    */         
/* 23 */         return Optional.of(Component.translatable(translationKey, translationArgs.toArray()));
/*    */       } 
/* 25 */       return Optional.of(Component.translatable(translationKey));
/*    */     } 
/*    */     
/* 28 */     return this.literal.map(Component::literal);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\Message.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
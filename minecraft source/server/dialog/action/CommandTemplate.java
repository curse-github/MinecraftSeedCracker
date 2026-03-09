/*    */ package net.minecraft.server.dialog.action;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ 
/*    */ public final class CommandTemplate extends Record implements Action {
/* 10 */   public CommandTemplate(ParsedTemplate template) { this.template = template; } private final ParsedTemplate template; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/CommandTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate; } public ParsedTemplate template() { return this.template; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/CommandTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/CommandTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/CommandTemplate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final MapCodec<CommandTemplate> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ParsedTemplate.CODEC
/* 14 */         .fieldOf("template").forGetter(CommandTemplate::template))
/* 15 */       .apply(i, CommandTemplate::new));
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<CommandTemplate> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) {
/* 24 */     String command = this.template.instantiate(Action.ValueGetter.getAsTemplateSubstitutions(parameters));
/* 25 */     return Optional.of(new ClickEvent.RunCommand(command));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\action\CommandTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
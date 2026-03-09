/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class Input extends Record {
/*    */   private final String key;
/*    */   private final InputControl control;
/*    */   
/*  8 */   public Input(String key, InputControl control) { this.key = key; this.control = control; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/Input;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/server/dialog/Input; } public String key() { return this.key; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/Input;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/Input; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/Input;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/Input;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public InputControl control() { return this.control; }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public static final Codec<Input> CODEC = RecordCodecBuilder.create(i -> i.group(ParsedTemplate.VARIABLE_CODEC
/* 13 */         .fieldOf("key").forGetter(Input::key), InputControl.MAP_CODEC
/* 14 */         .forGetter(Input::control))
/* 15 */       .apply(i, Input::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\Input.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
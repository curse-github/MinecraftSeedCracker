/*    */ package net.minecraft.server.dialog.input;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class BooleanInput extends Record implements InputControl {
/*    */   private final Component label;
/*    */   private final boolean initial;
/*    */   
/*  9 */   public BooleanInput(Component label, boolean initial, String onTrue, String onFalse) { this.label = label; this.initial = initial; this.onTrue = onTrue; this.onFalse = onFalse; } private final String onTrue; private final String onFalse; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/BooleanInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/BooleanInput; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/BooleanInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/BooleanInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/BooleanInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/BooleanInput;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public Component label() { return this.label; } public boolean initial() { return this.initial; } public String onTrue() { return this.onTrue; } public String onFalse() { return this.onFalse; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<BooleanInput> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/* 17 */         .fieldOf("label").forGetter(BooleanInput::label), Codec.BOOL
/* 18 */         .optionalFieldOf("initial", Boolean.valueOf(false)).forGetter(BooleanInput::initial), Codec.STRING
/* 19 */         .optionalFieldOf("on_true", "true").forGetter(BooleanInput::onTrue), Codec.STRING
/* 20 */         .optionalFieldOf("on_false", "false").forGetter(BooleanInput::onFalse))
/* 21 */       .apply(i, BooleanInput::new));
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<BooleanInput> mapCodec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\BooleanInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
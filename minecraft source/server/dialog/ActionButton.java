/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ 
/*    */ public final class ActionButton extends Record {
/*    */   private final CommonButtonData button;
/*    */   private final Optional<Action> action;
/*    */   
/*  9 */   public ActionButton(CommonButtonData button, Optional<Action> action) { this.button = button; this.action = action; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/ActionButton;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/server/dialog/ActionButton; } public CommonButtonData button() { return this.button; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/ActionButton;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ActionButton; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/ActionButton;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/ActionButton;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Action> action() { return this.action; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static final Codec<ActionButton> CODEC = RecordCodecBuilder.create(i -> i.group(CommonButtonData.MAP_CODEC
/* 14 */         .forGetter(ActionButton::button), Action.CODEC
/* 15 */         .optionalFieldOf("action").forGetter(ActionButton::action))
/* 16 */       .apply(i, ActionButton::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\ActionButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
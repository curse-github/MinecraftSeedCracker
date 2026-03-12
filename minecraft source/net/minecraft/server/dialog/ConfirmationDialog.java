/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class ConfirmationDialog extends Record implements SimpleDialog {
/*    */   private final CommonDialogData common;
/*    */   private final ActionButton yesButton;
/*    */   private final ActionButton noButton;
/*    */   
/* 10 */   public ConfirmationDialog(CommonDialogData common, ActionButton yesButton, ActionButton noButton) { this.common = common; this.yesButton = yesButton; this.noButton = noButton; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/ConfirmationDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/ConfirmationDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/ConfirmationDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ActionButton yesButton() { return this.yesButton; } public ActionButton noButton() { return this.noButton; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final MapCodec<ConfirmationDialog> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CommonDialogData.MAP_CODEC
/* 16 */         .forGetter(ConfirmationDialog::common), ActionButton.CODEC
/* 17 */         .fieldOf("yes").forGetter(ConfirmationDialog::yesButton), ActionButton.CODEC
/* 18 */         .fieldOf("no").forGetter(ConfirmationDialog::noButton))
/* 19 */       .apply(i, ConfirmationDialog::new));
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<ConfirmationDialog> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Optional<Action> onCancel() { return this.noButton.action(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public List<ActionButton> mainActions() { return List.of(this.yesButton, this.noButton); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\ConfirmationDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
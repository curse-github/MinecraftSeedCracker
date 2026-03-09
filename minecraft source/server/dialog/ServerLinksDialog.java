/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ public final class ServerLinksDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   private final Optional<ActionButton> exitAction;
/*    */   private final int columns;
/*    */   private final int buttonWidth;
/*    */   
/*  9 */   public ServerLinksDialog(CommonDialogData common, Optional<ActionButton> exitAction, int columns, int buttonWidth) { this.common = common; this.exitAction = exitAction; this.columns = columns; this.buttonWidth = buttonWidth; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/ServerLinksDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/server/dialog/ServerLinksDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/ServerLinksDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ServerLinksDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/ServerLinksDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/ServerLinksDialog;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<ActionButton> exitAction() { return this.exitAction; } public int columns() { return this.columns; } public int buttonWidth() { return this.buttonWidth; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<ServerLinksDialog> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CommonDialogData.MAP_CODEC
/* 17 */         .forGetter(ServerLinksDialog::common), ActionButton.CODEC
/* 18 */         .optionalFieldOf("exit_action").forGetter(ServerLinksDialog::exitAction), ExtraCodecs.POSITIVE_INT
/* 19 */         .optionalFieldOf("columns", Integer.valueOf(2)).forGetter(ServerLinksDialog::columns), WIDTH_CODEC
/* 20 */         .optionalFieldOf("button_width", Integer.valueOf(150)).forGetter(ServerLinksDialog::buttonWidth))
/* 21 */       .apply(i, ServerLinksDialog::new));
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<ServerLinksDialog> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\ServerLinksDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ 
/*    */ public final class DialogListDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   private final HolderSet<Dialog> dialogs;
/*    */   
/* 10 */   public DialogListDialog(CommonDialogData common, HolderSet<Dialog> dialogs, Optional<ActionButton> exitAction, int columns, int buttonWidth) { this.common = common; this.dialogs = dialogs; this.exitAction = exitAction; this.columns = columns; this.buttonWidth = buttonWidth; } private final Optional<ActionButton> exitAction; private final int columns; private final int buttonWidth; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/DialogListDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/DialogListDialog; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/DialogListDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/DialogListDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/DialogListDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/DialogListDialog;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public CommonDialogData common() { return this.common; } public HolderSet<Dialog> dialogs() { return this.dialogs; } public Optional<ActionButton> exitAction() { return this.exitAction; } public int columns() { return this.columns; } public int buttonWidth() { return this.buttonWidth; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final MapCodec<DialogListDialog> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CommonDialogData.MAP_CODEC
/* 19 */         .forGetter(DialogListDialog::common), Dialog.LIST_CODEC
/* 20 */         .fieldOf("dialogs").forGetter(DialogListDialog::dialogs), ActionButton.CODEC
/* 21 */         .optionalFieldOf("exit_action").forGetter(DialogListDialog::exitAction), ExtraCodecs.POSITIVE_INT
/* 22 */         .optionalFieldOf("columns", Integer.valueOf(2)).forGetter(DialogListDialog::columns), WIDTH_CODEC
/* 23 */         .optionalFieldOf("button_width", Integer.valueOf(150)).forGetter(DialogListDialog::buttonWidth))
/* 24 */       .apply(i, DialogListDialog::new));
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<DialogListDialog> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\DialogListDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
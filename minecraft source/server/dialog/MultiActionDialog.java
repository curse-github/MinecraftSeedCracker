/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class MultiActionDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   private final List<ActionButton> actions;
/*    */   private final Optional<ActionButton> exitAction;
/*    */   private final int columns;
/*    */   
/* 10 */   public MultiActionDialog(CommonDialogData common, List<ActionButton> actions, Optional<ActionButton> exitAction, int columns) { this.common = common; this.actions = actions; this.exitAction = exitAction; this.columns = columns; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/MultiActionDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/MultiActionDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/MultiActionDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/MultiActionDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/MultiActionDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/MultiActionDialog;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public List<ActionButton> actions() { return this.actions; } public Optional<ActionButton> exitAction() { return this.exitAction; } public int columns() { return this.columns; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final MapCodec<MultiActionDialog> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CommonDialogData.MAP_CODEC
/* 18 */         .forGetter(MultiActionDialog::common), 
/* 19 */         ExtraCodecs.nonEmptyList(ActionButton.CODEC.listOf()).fieldOf("actions").forGetter(MultiActionDialog::actions), ActionButton.CODEC
/* 20 */         .optionalFieldOf("exit_action").forGetter(MultiActionDialog::exitAction), ExtraCodecs.POSITIVE_INT
/* 21 */         .optionalFieldOf("columns", Integer.valueOf(2)).forGetter(MultiActionDialog::columns))
/* 22 */       .apply(i, MultiActionDialog::new));
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MapCodec<MultiActionDialog> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\MultiActionDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
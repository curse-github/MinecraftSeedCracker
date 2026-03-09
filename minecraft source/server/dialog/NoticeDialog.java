/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class NoticeDialog extends Record implements SimpleDialog {
/*    */   private final CommonDialogData common;
/*    */   private final ActionButton action;
/*    */   
/* 11 */   public NoticeDialog(CommonDialogData common, ActionButton action) { this.common = common; this.action = action; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/NoticeDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/server/dialog/NoticeDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/NoticeDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/NoticeDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/NoticeDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/NoticeDialog;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ActionButton action() { return this.action; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final ActionButton DEFAULT_ACTION = new ActionButton(new CommonButtonData(CommonComponents.GUI_OK, 150), 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 20 */       Optional.empty());
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<NoticeDialog> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CommonDialogData.MAP_CODEC
/* 24 */         .forGetter(NoticeDialog::common), ActionButton.CODEC
/* 25 */         .optionalFieldOf("action", DEFAULT_ACTION).forGetter(NoticeDialog::action))
/* 26 */       .apply(i, NoticeDialog::new));
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<NoticeDialog> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Optional<Action> onCancel() { return this.action.action(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public List<ActionButton> mainActions() { return List.of(this.action); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\NoticeDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
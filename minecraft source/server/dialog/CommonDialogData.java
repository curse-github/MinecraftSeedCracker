/*    */ package net.minecraft.server.dialog;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.dialog.body.DialogBody;
/*    */ 
/*    */ public final class CommonDialogData extends Record {
/*    */   private final Component title;
/*    */   private final Optional<Component> externalTitle;
/*    */   private final boolean canCloseWithEscape;
/*    */   
/* 14 */   public CommonDialogData(Component title, Optional<Component> externalTitle, boolean canCloseWithEscape, boolean pause, DialogAction afterAction, List<DialogBody> body, List<Input> inputs) { this.title = title; this.externalTitle = externalTitle; this.canCloseWithEscape = canCloseWithEscape; this.pause = pause; this.afterAction = afterAction; this.body = body; this.inputs = inputs; } private final boolean pause; private final DialogAction afterAction; private final List<DialogBody> body; private final List<Input> inputs; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/CommonDialogData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonDialogData; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/CommonDialogData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonDialogData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/CommonDialogData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/CommonDialogData;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Component title() { return this.title; } public Optional<Component> externalTitle() { return this.externalTitle; } public boolean canCloseWithEscape() { return this.canCloseWithEscape; } public boolean pause() { return this.pause; } public DialogAction afterAction() { return this.afterAction; } public List<DialogBody> body() { return this.body; } public List<Input> inputs() { return this.inputs; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<CommonDialogData> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/* 24 */         .fieldOf("title").forGetter(CommonDialogData::title), ComponentSerialization.CODEC
/* 25 */         .optionalFieldOf("external_title").forGetter(CommonDialogData::externalTitle), Codec.BOOL
/* 26 */         .optionalFieldOf("can_close_with_escape", Boolean.valueOf(true)).forGetter(CommonDialogData::canCloseWithEscape), Codec.BOOL
/* 27 */         .optionalFieldOf("pause", Boolean.valueOf(true)).forGetter(CommonDialogData::pause), DialogAction.CODEC
/* 28 */         .optionalFieldOf("after_action", DialogAction.CLOSE).forGetter(CommonDialogData::afterAction), DialogBody.COMPACT_LIST_CODEC
/* 29 */         .optionalFieldOf("body", List.of()).forGetter(CommonDialogData::body), Input.CODEC
/* 30 */         .listOf().optionalFieldOf("inputs", List.of()).forGetter(CommonDialogData::inputs))
/* 31 */       .apply(i, CommonDialogData::new)).validate(data -> {
/* 32 */         if (data.pause && !data.afterAction.willUnpause()) {
/* 33 */           return DataResult.error(());
/*    */         }
/*    */         
/* 36 */         return DataResult.success(data);
/*    */       });
/*    */ 
/*    */   
/* 40 */   public Component computeExternalTitle() { return (Component)this.externalTitle.orElse(this.title); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\CommonDialogData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
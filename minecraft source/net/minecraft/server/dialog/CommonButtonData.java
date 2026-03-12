/*    */ package net.minecraft.server.dialog;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class CommonButtonData extends Record {
/*    */   private final Component label;
/*    */   private final Optional<Component> tooltip;
/*    */   private final int width;
/*    */   public static final int DEFAULT_WIDTH = 150;
/*    */   
/* 10 */   public CommonButtonData(Component label, Optional<Component> tooltip, int width) { this.label = label; this.tooltip = tooltip; this.width = width; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/CommonButtonData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonButtonData; } public Component label() { return this.label; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/CommonButtonData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonButtonData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/CommonButtonData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/CommonButtonData;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Component> tooltip() { return this.tooltip; } public int width() { return this.width; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final MapCodec<CommonButtonData> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/* 19 */         .fieldOf("label").forGetter(CommonButtonData::label), ComponentSerialization.CODEC
/* 20 */         .optionalFieldOf("tooltip").forGetter(CommonButtonData::tooltip), Dialog.WIDTH_CODEC
/* 21 */         .optionalFieldOf("width", Integer.valueOf(150)).forGetter(CommonButtonData::width))
/* 22 */       .apply(i, CommonButtonData::new));
/*    */ 
/*    */   
/* 25 */   public CommonButtonData(Component label, int width) { this(label, Optional.empty(), width); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\CommonButtonData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
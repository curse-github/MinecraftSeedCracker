/*    */ package net.minecraft.server.dialog.body;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class PlainMessage extends Record implements DialogBody {
/*    */   private final Component contents;
/*    */   private final int width;
/*    */   public static final int DEFAULT_WIDTH = 200;
/*    */   
/* 10 */   public PlainMessage(Component contents, int width) { this.contents = contents; this.width = width; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/body/PlainMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/PlainMessage; } public Component contents() { return this.contents; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/body/PlainMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/PlainMessage; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/body/PlainMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/body/PlainMessage;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<PlainMessage> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/* 17 */         .fieldOf("contents").forGetter(PlainMessage::contents), Dialog.WIDTH_CODEC
/* 18 */         .optionalFieldOf("width", Integer.valueOf(200)).forGetter(PlainMessage::width))
/* 19 */       .apply(i, PlainMessage::new));
/*    */   
/* 21 */   public static final Codec<PlainMessage> CODEC = Codec.withAlternative(MAP_CODEC
/* 22 */       .codec(), ComponentSerialization.CODEC, contents -> 
/* 23 */       new PlainMessage(contents, 200));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<PlainMessage> mapCodec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\body\PlainMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
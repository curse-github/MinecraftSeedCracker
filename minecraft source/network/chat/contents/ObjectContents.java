/*    */ package net.minecraft.network.chat.contents;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*    */ import net.minecraft.network.chat.contents.objects.ObjectInfos;
/*    */ 
/*    */ public final class ObjectContents extends Record implements ComponentContents {
/* 13 */   public ObjectContents(ObjectInfo contents) { this.contents = contents; } private final ObjectInfo contents; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/ObjectContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ObjectContents; } public ObjectInfo contents() { return this.contents; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/ObjectContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ObjectContents; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/ObjectContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/ObjectContents;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 16 */   private static final String PLACEHOLDER = Character.toString('￼');
/*    */   
/* 18 */   public static final MapCodec<ObjectContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ObjectInfos.CODEC
/* 19 */         .forGetter(ObjectContents::contents))
/* 20 */       .apply(i, ObjectContents::new));
/*    */ 
/*    */ 
/*    */   
/* 24 */   public MapCodec<ObjectContents> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return output.accept(this.contents.description()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) { return output.accept(currentStyle.withFont(this.contents.fontDescription()), PLACEHOLDER); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\ObjectContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
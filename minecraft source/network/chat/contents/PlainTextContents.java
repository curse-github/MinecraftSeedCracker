/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.ComponentContents;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public interface PlainTextContents extends ComponentContents {
/* 13 */   public static final MapCodec<PlainTextContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 14 */         .fieldOf("text").forGetter(PlainTextContents::text))
/* 15 */       .apply(i, PlainTextContents::create));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final PlainTextContents EMPTY = new PlainTextContents()
/*    */     {
/*    */       public String toString() {
/* 25 */         return "empty";
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 30 */       public String text() { return ""; }
/*    */     };
/*    */   public static final class LiteralContents extends Record implements PlainTextContents { private final String text;
/*    */     
/* 34 */     public LiteralContents(String text) { this.text = text; } public String text() { return this.text; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 37 */     public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return output.accept(this.text); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) { return output.accept(currentStyle, this.text); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     public String toString() { return "literal{" + this.text + "}"; } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   static PlainTextContents create(String text) { return text.isEmpty() ? EMPTY : new LiteralContents(text); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   default MapCodec<PlainTextContents> codec() { return MAP_CODEC; }
/*    */   
/*    */   String text();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\PlainTextContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
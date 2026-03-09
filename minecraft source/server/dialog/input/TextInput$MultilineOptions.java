/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MultilineOptions
/*    */   extends Record
/*    */ {
/*    */   private final Optional<Integer> maxLines;
/*    */   private final Optional<Integer> height;
/*    */   public static final int MAX_HEIGHT = 512;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 40 */   public MultilineOptions(Optional<Integer> maxLines, Optional<Integer> height) { this.maxLines = maxLines; this.height = height; } public Optional<Integer> maxLines() { return this.maxLines; } public Optional<Integer> height() { return this.height; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final Codec<MultilineOptions> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_INT
/* 46 */         .optionalFieldOf("max_lines").forGetter(MultilineOptions::maxLines), 
/* 47 */         ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(MultilineOptions::height))
/* 48 */       .apply(i, MultilineOptions::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\TextInput$MultilineOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
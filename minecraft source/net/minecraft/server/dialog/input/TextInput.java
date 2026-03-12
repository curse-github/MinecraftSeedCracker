/*    */ package net.minecraft.server.dialog.input;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class TextInput extends Record implements InputControl {
/*    */   private final int width;
/*    */   private final Component label;
/*    */   private final boolean labelVisible;
/*    */   
/* 15 */   public TextInput(int width, Component label, boolean labelVisible, String initial, int maxLength, Optional<MultilineOptions> multiline) { this.width = width; this.label = label; this.labelVisible = labelVisible; this.initial = initial; this.maxLength = maxLength; this.multiline = multiline; } private final String initial; private final int maxLength; private final Optional<MultilineOptions> multiline; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public Component label() { return this.label; } public boolean labelVisible() { return this.labelVisible; } public String initial() { return this.initial; } public int maxLength() { return this.maxLength; } public Optional<MultilineOptions> multiline() { return this.multiline; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final MapCodec<TextInput> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Dialog.WIDTH_CODEC
/* 25 */         .optionalFieldOf("width", Integer.valueOf(200)).forGetter(TextInput::width), ComponentSerialization.CODEC
/* 26 */         .fieldOf("label").forGetter(TextInput::label), Codec.BOOL
/* 27 */         .optionalFieldOf("label_visible", Boolean.valueOf(true)).forGetter(TextInput::labelVisible), Codec.STRING
/* 28 */         .optionalFieldOf("initial", "").forGetter(TextInput::initial), ExtraCodecs.POSITIVE_INT
/* 29 */         .optionalFieldOf("max_length", Integer.valueOf(32)).forGetter(TextInput::maxLength), MultilineOptions.CODEC
/* 30 */         .optionalFieldOf("multiline").forGetter(TextInput::multiline))
/* 31 */       .apply(i, TextInput::new))
/* 32 */     .validate(o -> {
/* 33 */         if (o.initial.length() > o.maxLength()) {
/* 34 */           return DataResult.error(());
/*    */         }
/*    */         
/* 37 */         return DataResult.success(o);
/*    */       });
/*    */   public static final class MultilineOptions extends Record { private final Optional<Integer> maxLines; private final Optional<Integer> height; public static final int MAX_HEIGHT = 512;
/* 40 */     public MultilineOptions(Optional<Integer> maxLines, Optional<Integer> height) { this.maxLines = maxLines; this.height = height; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #40	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/* 40 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Integer> maxLines() { return this.maxLines; } public Optional<Integer> height() { return this.height; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 45 */     public static final Codec<MultilineOptions> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.POSITIVE_INT
/* 46 */           .optionalFieldOf("max_lines").forGetter(MultilineOptions::maxLines), 
/* 47 */           ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(MultilineOptions::height))
/* 48 */         .apply(i, MultilineOptions::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public MapCodec<TextInput> mapCodec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\TextInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
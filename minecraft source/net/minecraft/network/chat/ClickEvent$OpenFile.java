/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.Function;
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
/*    */ public final class OpenFile
/*    */   extends Record
/*    */   implements ClickEvent
/*    */ {
/*    */   private final String path;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 36 */   public OpenFile(String path) { this.path = path; } public String path() { return this.path; }
/* 37 */   public static final MapCodec<OpenFile> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING
/* 38 */         .fieldOf("path").forGetter(OpenFile::path))
/* 39 */       .apply(i, OpenFile::new));
/*    */ 
/*    */   
/* 42 */   public OpenFile(File file) { this(file.toString()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public OpenFile(Path path) { this(path.toFile()); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public File file() { return new File(this.path); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public ClickEvent.Action action() { return ClickEvent.Action.OPEN_FILE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ClickEvent$OpenFile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
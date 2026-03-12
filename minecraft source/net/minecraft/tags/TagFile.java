/*    */ package net.minecraft.tags;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class TagFile extends Record {
/*    */   private final List<TagEntry> entries;
/*    */   private final boolean replace;
/*    */   
/*  8 */   public TagFile(List<TagEntry> entries, boolean replace) { this.entries = entries; this.replace = replace; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagFile;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/tags/TagFile; } public List<TagEntry> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagFile;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagFile; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagFile;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/tags/TagFile;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public boolean replace() { return this.replace; }
/*  9 */   public static final Codec<TagFile> CODEC = RecordCodecBuilder.create(i -> i.group(TagEntry.CODEC
/* 10 */         .listOf().fieldOf("values").forGetter(TagFile::entries), Codec.BOOL
/* 11 */         .optionalFieldOf("replace", Boolean.valueOf(false)).forGetter(TagFile::replace))
/* 12 */       .apply(i, TagFile::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\TagFile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
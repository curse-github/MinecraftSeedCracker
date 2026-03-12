/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class StringTag extends Record implements PrimitiveTag {
/*   8 */   public String value() { return this.value; }
/*     */   private final String value;
/*     */   private static final int SELF_SIZE_IN_BYTES = 36;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/StringTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/StringTag; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/StringTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/StringTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  16 */   public static final TagType<StringTag> TYPE = new TagType.VariableSize<StringTag>()
/*     */     {
/*     */       public StringTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  19 */         return StringTag.valueOf(StringTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  24 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(StringTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static String readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  28 */         accounter.accountBytes(36L);
/*     */ 
/*     */         
/*  31 */         String data = input.readUTF();
/*  32 */         accounter.accountBytes(2L, data.length());
/*  33 */         return data;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  38 */       public void skip(DataInput input, NbtAccounter accounter) throws IOException { StringTag.skipString(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  43 */       public String getName() { return "STRING"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  48 */       public String getPrettyName() { return "TAG_String"; }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static void skipString(DataInput input) throws IOException { input.skipBytes(input.readUnsignedShort()); }
/*     */ 
/*     */   
/*  56 */   private static final StringTag EMPTY = new StringTag("");
/*     */   
/*     */   private static final char DOUBLE_QUOTE = '"';
/*     */   
/*     */   private static final char SINGLE_QUOTE = '\'';
/*     */   
/*     */   private static final char ESCAPE = '\\';
/*     */   private static final char NOT_SET = '\000';
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  66 */   public StringTag(String value) { this.value = value; }
/*     */ 
/*     */   
/*     */   public static StringTag valueOf(String data) {
/*  70 */     if (data.isEmpty()) {
/*  71 */       return EMPTY;
/*     */     }
/*  73 */     return new StringTag(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public void write(DataOutput output) throws IOException { output.writeUTF(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public int sizeInBytes() { return 36 + 2 * this.value.length(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public byte getId() { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public TagType<StringTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     StringTagVisitor visitor = new StringTagVisitor();
/*  99 */     visitor.visitString(this);
/* 100 */     return visitor.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public StringTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public Optional<String> asString() { return Optional.of(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void accept(TagVisitor visitor) { visitor.visitString(this); }
/*     */ 
/*     */   
/*     */   public static String quoteAndEscape(String input) {
/* 119 */     StringBuilder result = new StringBuilder();
/* 120 */     quoteAndEscape(input, result);
/* 121 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quoteAndEscape(String input, StringBuilder result) {
/* 129 */     int quoteMarkIndex = result.length();
/* 130 */     result.append(' ');
/* 131 */     char quote = Character.MIN_VALUE;
/* 132 */     for (int i = 0; i < input.length(); i++) {
/* 133 */       char c = input.charAt(i);
/* 134 */       if (c == '\\') {
/* 135 */         result.append("\\\\");
/* 136 */       } else if (c == '"' || c == '\'') {
/* 137 */         if (quote == '\000') {
/* 138 */           quote = (c == '"') ? '\'' : '"';
/*     */         }
/* 140 */         if (quote == c) {
/* 141 */           result.append('\\');
/*     */         }
/* 143 */         result.append(c);
/*     */       } else {
/* 145 */         String escaped = SnbtGrammar.escapeControlCharacters(c);
/* 146 */         if (escaped != null) {
/* 147 */           result.append('\\');
/* 148 */           result.append(escaped);
/*     */         } else {
/* 150 */           result.append(c);
/*     */         } 
/*     */       } 
/*     */     } 
/* 154 */     if (quote == '\000') {
/* 155 */       quote = '"';
/*     */     }
/*     */     
/* 158 */     result.setCharAt(quoteMarkIndex, quote);
/* 159 */     result.append(quote);
/*     */   }
/*     */   
/*     */   public static String escapeWithoutQuotes(String input) {
/* 163 */     StringBuilder result = new StringBuilder();
/* 164 */     escapeWithoutQuotes(input, result);
/* 165 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void escapeWithoutQuotes(String input, StringBuilder result) {
/* 172 */     for (int i = 0; i < input.length(); i++) {
/* 173 */       String escaped; char c = input.charAt(i);
/* 174 */       switch (c) { case '"': case '\'':
/*     */         case '\\':
/* 176 */           result.append('\\');
/* 177 */           result.append(c);
/*     */           break;
/*     */         default:
/* 180 */           escaped = SnbtGrammar.escapeControlCharacters(c);
/* 181 */           if (escaped != null) {
/* 182 */             result.append('\\');
/* 183 */             result.append(escaped); break;
/*     */           } 
/* 185 */           result.append(c);
/*     */           break; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visit(this.value); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\StringTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
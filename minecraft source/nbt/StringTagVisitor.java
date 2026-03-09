/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringTagVisitor
/*     */   implements TagVisitor
/*     */ {
/*  13 */   private static final Pattern UNQUOTED_KEY_MATCH = Pattern.compile("[A-Za-z._]+[A-Za-z0-9._+-]*");
/*     */   
/*  15 */   private final StringBuilder builder = new StringBuilder();
/*     */ 
/*     */   
/*  18 */   public String build() { return this.builder.toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   public void visitString(StringTag tag) { this.builder.append(StringTag.quoteAndEscape(tag.value())); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   public void visitByte(ByteTag tag) { this.builder.append(tag.value()).append('b'); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   public void visitShort(ShortTag tag) { this.builder.append(tag.value()).append('s'); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public void visitInt(IntTag tag) { this.builder.append(tag.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public void visitLong(LongTag tag) { this.builder.append(tag.value()).append('L'); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public void visitFloat(FloatTag tag) { this.builder.append(tag.value()).append('f'); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public void visitDouble(DoubleTag tag) { this.builder.append(tag.value()).append('d'); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag tag) {
/*  58 */     this.builder.append("[B;");
/*  59 */     byte[] data = tag.getAsByteArray();
/*  60 */     for (int i = 0; i < data.length; i++) {
/*  61 */       if (i != 0) {
/*  62 */         this.builder.append(',');
/*     */       }
/*  64 */       this.builder.append(data[i]).append('B');
/*     */     } 
/*  66 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag tag) {
/*  71 */     this.builder.append("[I;");
/*  72 */     int[] data = tag.getAsIntArray();
/*  73 */     for (int i = 0; i < data.length; i++) {
/*  74 */       if (i != 0) {
/*  75 */         this.builder.append(',');
/*     */       }
/*  77 */       this.builder.append(data[i]);
/*     */     } 
/*  79 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag tag) {
/*  84 */     this.builder.append("[L;");
/*  85 */     long[] data = tag.getAsLongArray();
/*  86 */     for (int i = 0; i < data.length; i++) {
/*  87 */       if (i != 0) {
/*  88 */         this.builder.append(',');
/*     */       }
/*  90 */       this.builder.append(data[i]).append('L');
/*     */     } 
/*  92 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag tag) {
/*  97 */     this.builder.append('[');
/*  98 */     for (int i = 0; i < tag.size(); i++) {
/*  99 */       if (i != 0) {
/* 100 */         this.builder.append(',');
/*     */       }
/* 102 */       tag.get(i).accept(this);
/*     */     } 
/* 104 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCompound(CompoundTag tag) {
/* 109 */     this.builder.append('{');
/*     */     
/* 111 */     List<Map.Entry<String, Tag>> entries = new ArrayList<Map.Entry<String, Tag>>(tag.entrySet());
/* 112 */     entries.sort(Map.Entry.comparingByKey());
/* 113 */     for (int i = 0; i < entries.size(); i++) {
/* 114 */       Map.Entry<String, Tag> entry = (Map.Entry)entries.get(i);
/* 115 */       if (i != 0) {
/* 116 */         this.builder.append(',');
/*     */       }
/* 118 */       handleKeyEscape((String)entry.getKey());
/* 119 */       this.builder.append(':');
/* 120 */       ((Tag)entry.getValue()).accept(this);
/*     */     } 
/*     */     
/* 123 */     this.builder.append('}');
/*     */   }
/*     */   
/*     */   private void handleKeyEscape(String input) {
/* 127 */     if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false") && UNQUOTED_KEY_MATCH.matcher(input).matches()) {
/* 128 */       this.builder.append(input);
/*     */     } else {
/* 130 */       StringTag.quoteAndEscape(input, this.builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public void visitEnd(EndTag tag) { this.builder.append("END"); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\StringTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
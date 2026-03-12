/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LongArrayTag
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final int SELF_SIZE_IN_BYTES = 24;
/*     */   
/*  20 */   public static final TagType<LongArrayTag> TYPE = new TagType.VariableSize<LongArrayTag>()
/*     */     {
/*     */       public LongArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  23 */         return new LongArrayTag(LongArrayTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  28 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(LongArrayTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static long[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.accountBytes(24L);
/*  33 */         int length = input.readInt();
/*  34 */         accounter.accountBytes(8L, length);
/*  35 */         long[] data = new long[length];
/*  36 */         for (int i = 0; i < length; i++) {
/*  37 */           data[i] = input.readLong();
/*     */         }
/*  39 */         return data;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  44 */       public void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(input.readInt() * 8); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       public String getName() { return "LONG[]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  54 */       public String getPrettyName() { return "TAG_Long_Array"; }
/*     */     };
/*     */ 
/*     */   
/*     */   private long[] data;
/*     */ 
/*     */   
/*  61 */   public LongArrayTag(long[] data) { this.data = data; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  66 */     output.writeInt(this.data.length);
/*  67 */     for (long i : this.data) {
/*  68 */       output.writeLong(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public int sizeInBytes() { return 24 + 8 * this.data.length; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public byte getId() { return 12; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public TagType<LongArrayTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  89 */     StringTagVisitor visitor = new StringTagVisitor();
/*  90 */     visitor.visitLongArray(this);
/*  91 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public LongArrayTag copy() {
/*  96 */     long[] cp = new long[this.data.length];
/*  97 */     System.arraycopy(this.data, 0, cp, 0, this.data.length);
/*  98 */     return new LongArrayTag(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 103 */     if (this == obj) {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     return (obj instanceof LongArrayTag && Arrays.equals(this.data, ((LongArrayTag)obj).data));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public int hashCode() { return Arrays.hashCode(this.data); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public void accept(TagVisitor visitor) { visitor.visitLongArray(this); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public long[] getAsLongArray() { return this.data; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public int size() { return this.data.length; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public LongTag get(int index) { return LongTag.valueOf(this.data[index]); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 136 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 137 */       this.data[index] = numeric.longValue();
/* 138 */       return true; }
/*     */     
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 145 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 146 */       this.data = ArrayUtils.add(this.data, index, numeric.longValue());
/* 147 */       return true; }
/*     */     
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag remove(int index) {
/* 154 */     long prev = this.data[index];
/* 155 */     this.data = ArrayUtils.remove(this.data, index);
/* 156 */     return LongTag.valueOf(prev);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public void clear() { this.data = new long[0]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public Optional<long[]> asLongArray() { return Optional.of(this.data); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visit(this.data); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\LongArrayTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
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
/*     */ public final class IntArrayTag
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final int SELF_SIZE_IN_BYTES = 24;
/*     */   
/*  20 */   public static final TagType<IntArrayTag> TYPE = new TagType.VariableSize<IntArrayTag>()
/*     */     {
/*     */       public IntArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  23 */         return new IntArrayTag(IntArrayTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  28 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(IntArrayTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static int[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.accountBytes(24L);
/*     */         
/*  34 */         int length = input.readInt();
/*  35 */         accounter.accountBytes(4L, length);
/*  36 */         int[] data = new int[length];
/*  37 */         for (int i = 0; i < length; i++) {
/*  38 */           data[i] = input.readInt();
/*     */         }
/*  40 */         return data;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  45 */       public void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(input.readInt() * 4); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  50 */       public String getName() { return "INT[]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       public String getPrettyName() { return "TAG_Int_Array"; }
/*     */     };
/*     */ 
/*     */   
/*     */   private int[] data;
/*     */ 
/*     */   
/*  62 */   public IntArrayTag(int[] data) { this.data = data; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  67 */     output.writeInt(this.data.length);
/*  68 */     for (int i : this.data) {
/*  69 */       output.writeInt(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public int sizeInBytes() { return 24 + 4 * this.data.length; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public byte getId() { return 11; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public TagType<IntArrayTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     StringTagVisitor visitor = new StringTagVisitor();
/*  91 */     visitor.visitIntArray(this);
/*  92 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public IntArrayTag copy() {
/*  97 */     int[] cp = new int[this.data.length];
/*  98 */     System.arraycopy(this.data, 0, cp, 0, this.data.length);
/*  99 */     return new IntArrayTag(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 104 */     if (this == obj) {
/* 105 */       return true;
/*     */     }
/*     */     
/* 108 */     return (obj instanceof IntArrayTag && Arrays.equals(this.data, ((IntArrayTag)obj).data));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public int hashCode() { return Arrays.hashCode(this.data); }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public int[] getAsIntArray() { return this.data; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public void accept(TagVisitor visitor) { visitor.visitIntArray(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public int size() { return this.data.length; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public IntTag get(int index) { return IntTag.valueOf(this.data[index]); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 137 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 138 */       this.data[index] = numeric.intValue();
/* 139 */       return true; }
/*     */     
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 146 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 147 */       this.data = ArrayUtils.add(this.data, index, numeric.intValue());
/* 148 */       return true; }
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag remove(int index) {
/* 155 */     int prev = this.data[index];
/* 156 */     this.data = ArrayUtils.remove(this.data, index);
/* 157 */     return IntTag.valueOf(prev);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public void clear() { this.data = new int[0]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public Optional<int[]> asIntArray() { return Optional.of(this.data); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visit(this.data); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\IntArrayTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
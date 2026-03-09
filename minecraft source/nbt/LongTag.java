/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class LongTag extends Record implements NumericTag {
/*   7 */   public long value() { return this.value; }
/*     */   
/*     */   private final long value;
/*     */   private static final int SELF_SIZE_IN_BYTES = 16;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/LongTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/LongTag; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/LongTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/LongTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*     */   private static class Cache { private static final int HIGH = 1024;
/*     */     private static final int LOW = -128;
/*  18 */     static final LongTag[] cache = new LongTag[1153];
/*     */     
/*     */     static  {
/*  21 */       for (i = 0; i < cache.length; i++) {
/*  22 */         cache[i] = new LongTag((-128 + i));
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  27 */   public static final TagType<LongTag> TYPE = new TagType.StaticSize<LongTag>()
/*     */     {
/*     */       public LongTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  30 */         return LongTag.valueOf(LongTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  35 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(LongTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static long readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  39 */         accounter.accountBytes(16L);
/*  40 */         return input.readLong();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  45 */       public int size() { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  50 */       public String getName() { return "LONG"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       public String getPrettyName() { return "TAG_Long"; }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  63 */   public LongTag(long value) { this.value = value; }
/*     */ 
/*     */   
/*     */   public static LongTag valueOf(long i) {
/*  67 */     if (i >= -128L && i <= 1024L) {
/*  68 */       return Cache.cache[(int)i - -128];
/*     */     }
/*  70 */     return new LongTag(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public void write(DataOutput output) throws IOException { output.writeLong(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public int sizeInBytes() { return 16; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public byte getId() { return 4; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public TagType<LongTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public LongTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void accept(TagVisitor visitor) { visitor.visitLong(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public long longValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public int intValue() { return (int)(this.value & 0xFFFFFFFFFFFFFFFFL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public short shortValue() { return (short)(int)(this.value & 0xFFFFL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public byte byteValue() { return (byte)(int)(this.value & 0xFFL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public double doubleValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public float floatValue() { return (float)this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public Number box() { return Long.valueOf(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visit(this.value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     StringTagVisitor visitor = new StringTagVisitor();
/* 146 */     visitor.visitLong(this);
/* 147 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\LongTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
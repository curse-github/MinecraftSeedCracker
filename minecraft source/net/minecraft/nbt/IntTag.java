/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class IntTag extends Record implements NumericTag {
/*   7 */   public int value() { return this.value; }
/*     */   
/*     */   private final int value;
/*     */   private static final int SELF_SIZE_IN_BYTES = 12;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/IntTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/IntTag; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/IntTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/IntTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*     */   private static class Cache { private static final int HIGH = 1024;
/*     */     private static final int LOW = -128;
/*  18 */     static final IntTag[] cache = new IntTag[1153];
/*     */     
/*     */     static  {
/*  21 */       for (i = 0; i < cache.length; i++) {
/*  22 */         cache[i] = new IntTag(-128 + i);
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  27 */   public static final TagType<IntTag> TYPE = new TagType.StaticSize<IntTag>()
/*     */     {
/*     */       public IntTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  30 */         return IntTag.valueOf(IntTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  35 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(IntTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static int readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  39 */         accounter.accountBytes(12L);
/*  40 */         return input.readInt();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  45 */       public int size() { return 4; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  50 */       public String getName() { return "INT"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       public String getPrettyName() { return "TAG_Int"; }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  63 */   public IntTag(int value) { this.value = value; }
/*     */ 
/*     */   
/*     */   public static IntTag valueOf(int i) {
/*  67 */     if (i >= -128 && i <= 1024) {
/*  68 */       return Cache.cache[i - -128];
/*     */     }
/*  70 */     return new IntTag(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public void write(DataOutput output) throws IOException { output.writeInt(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public int sizeInBytes() { return 12; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public byte getId() { return 3; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public TagType<IntTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public IntTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void accept(TagVisitor visitor) { visitor.visitInt(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public long longValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public int intValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public short shortValue() { return (short)(this.value & 0xFFFF); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public byte byteValue() { return (byte)(this.value & 0xFF); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public double doubleValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public float floatValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public Number box() { return Integer.valueOf(this.value); }
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
/* 146 */     visitor.visitInt(this);
/* 147 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\IntTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
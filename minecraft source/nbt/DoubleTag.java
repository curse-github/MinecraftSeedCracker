/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class DoubleTag extends Record implements NumericTag {
/*   9 */   public double value() { return this.value; }
/*     */ 
/*     */ 
/*     */   
/*     */   private final double value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 16;
/*     */   
/*  17 */   public static final DoubleTag ZERO = new DoubleTag(0.0D); public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/DoubleTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/DoubleTag; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/DoubleTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/DoubleTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*  19 */   public static final TagType<DoubleTag> TYPE = new TagType.StaticSize<DoubleTag>()
/*     */     {
/*     */       public DoubleTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  22 */         return DoubleTag.valueOf(DoubleTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  27 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(DoubleTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static double readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  31 */         accounter.accountBytes(16L);
/*  32 */         return input.readDouble();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  37 */       public int size() { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  42 */       public String getName() { return "DOUBLE"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  47 */       public String getPrettyName() { return "TAG_Double"; }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  55 */   public DoubleTag(double value) { this.value = value; }
/*     */ 
/*     */   
/*     */   public static DoubleTag valueOf(double data) {
/*  59 */     if (data == 0.0D) {
/*  60 */       return ZERO;
/*     */     }
/*  62 */     return new DoubleTag(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public void write(DataOutput output) throws IOException { output.writeDouble(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public int sizeInBytes() { return 16; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public byte getId() { return 6; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public TagType<DoubleTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public DoubleTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void accept(TagVisitor visitor) { visitor.visitDouble(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public long longValue() { return (long)Math.floor(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public int intValue() { return Mth.floor(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public short shortValue() { return (short)(Mth.floor(this.value) & 0xFFFF); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public byte byteValue() { return (byte)(Mth.floor(this.value) & 0xFF); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public double doubleValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public float floatValue() { return (float)this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public Number box() { return Double.valueOf(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visit(this.value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     StringTagVisitor visitor = new StringTagVisitor();
/* 138 */     visitor.visitDouble(this);
/* 139 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\DoubleTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
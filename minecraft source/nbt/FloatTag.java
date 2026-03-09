/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class FloatTag extends Record implements NumericTag {
/*   9 */   public float value() { return this.value; }
/*     */ 
/*     */ 
/*     */   
/*     */   private final float value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 12;
/*     */   
/*  17 */   public static final FloatTag ZERO = new FloatTag(0.0F); public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/FloatTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/FloatTag; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/FloatTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/FloatTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*  19 */   public static final TagType<FloatTag> TYPE = new TagType.StaticSize<FloatTag>()
/*     */     {
/*     */       public FloatTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  22 */         return FloatTag.valueOf(FloatTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  27 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(FloatTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static float readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  31 */         accounter.accountBytes(12L);
/*  32 */         return input.readFloat();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  37 */       public int size() { return 4; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  42 */       public String getName() { return "FLOAT"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  47 */       public String getPrettyName() { return "TAG_Float"; }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  55 */   public FloatTag(float value) { this.value = value; }
/*     */ 
/*     */   
/*     */   public static FloatTag valueOf(float data) {
/*  59 */     if (data == 0.0F) {
/*  60 */       return ZERO;
/*     */     }
/*  62 */     return new FloatTag(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public void write(DataOutput output) throws IOException { output.writeFloat(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public int sizeInBytes() { return 12; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public byte getId() { return 5; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public TagType<FloatTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public FloatTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void accept(TagVisitor visitor) { visitor.visitFloat(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public long longValue() { return (long)this.value; }
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
/* 122 */   public float floatValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public Number box() { return Float.valueOf(this.value); }
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
/* 138 */     visitor.visitFloat(this);
/* 139 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\FloatTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
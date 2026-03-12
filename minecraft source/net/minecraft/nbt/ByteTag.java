/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ByteTag extends Record implements NumericTag {
/*   7 */   public byte value() { return this.value; }
/*     */   
/*     */   private final byte value;
/*     */   private static final int SELF_SIZE_IN_BYTES = 9;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/ByteTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/ByteTag; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/ByteTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/ByteTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  16 */   private static class Cache { private static final ByteTag[] cache = new ByteTag[256];
/*     */     
/*     */     static  {
/*  19 */       for (i = 0; i < cache.length; i++) {
/*  20 */         cache[i] = new ByteTag((byte)(i - 128));
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  25 */   public static final TagType<ByteTag> TYPE = new TagType.StaticSize<ByteTag>()
/*     */     {
/*     */       public ByteTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  28 */         return ByteTag.valueOf(ByteTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  33 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(ByteTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static byte readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  37 */         accounter.accountBytes(9L);
/*  38 */         return input.readByte();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  43 */       public int size() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  48 */       public String getName() { return "BYTE"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  53 */       public String getPrettyName() { return "TAG_Byte"; }
/*     */     };
/*     */ 
/*     */   
/*  57 */   public static final ByteTag ZERO = valueOf((byte)0);
/*  58 */   public static final ByteTag ONE = valueOf((byte)1);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  64 */   public ByteTag(byte value) { this.value = value; }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static ByteTag valueOf(byte data) { return Cache.cache['' + data]; }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static ByteTag valueOf(boolean data) { return data ? ONE : ZERO; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public void write(DataOutput output) throws IOException { output.writeByte(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public int sizeInBytes() { return 9; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public byte getId() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public TagType<ByteTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public ByteTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public void accept(TagVisitor visitor) { visitor.visitByte(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public long longValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public int intValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public short shortValue() { return (short)this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public byte byteValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public double doubleValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public float floatValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public Number box() { return Byte.valueOf(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) { return visitor.visit(this.value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     StringTagVisitor visitor = new StringTagVisitor();
/* 148 */     visitor.visitByte(this);
/* 149 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ByteTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
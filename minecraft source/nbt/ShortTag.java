/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ShortTag extends Record implements NumericTag {
/*   7 */   public short value() { return this.value; }
/*     */   
/*     */   private final short value;
/*     */   private static final int SELF_SIZE_IN_BYTES = 10;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/ShortTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/ShortTag; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/ShortTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/ShortTag;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*     */   private static class Cache { private static final int HIGH = 1024;
/*     */     private static final int LOW = -128;
/*  18 */     static final ShortTag[] cache = new ShortTag[1153];
/*     */     
/*     */     static  {
/*  21 */       for (i = 0; i < cache.length; i++) {
/*  22 */         cache[i] = new ShortTag((short)(-128 + i));
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  27 */   public static final TagType<ShortTag> TYPE = new TagType.StaticSize<ShortTag>()
/*     */     {
/*     */       public ShortTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  30 */         return ShortTag.valueOf(ShortTag.null.readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  35 */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException { return output.visit(ShortTag.null.readAccounted(input, accounter)); }
/*     */ 
/*     */       
/*     */       private static short readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  39 */         accounter.accountBytes(10L);
/*  40 */         return input.readShort();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  45 */       public int size() { return 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  50 */       public String getName() { return "SHORT"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       public String getPrettyName() { return "TAG_Short"; }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  63 */   public ShortTag(short value) { this.value = value; }
/*     */ 
/*     */   
/*     */   public static ShortTag valueOf(short i) {
/*  67 */     if (i >= -128 && i <= 1024) {
/*  68 */       return Cache.cache[i - -128];
/*     */     }
/*  70 */     return new ShortTag(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public void write(DataOutput output) throws IOException { output.writeShort(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public int sizeInBytes() { return 10; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public byte getId() { return 2; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public TagType<ShortTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public ShortTag copy() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void accept(TagVisitor visitor) { visitor.visitShort(this); }
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
/* 115 */   public short shortValue() { return this.value; }
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
/* 135 */   public Number box() { return Short.valueOf(this.value); }
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
/* 146 */     visitor.visitShort(this);
/* 147 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ShortTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.SuppressForbidden;
/*    */ 
/*    */ public class DelegateDataOutput
/*    */   implements DataOutput
/*    */ {
/*    */   private final DataOutput parent;
/*    */   
/* 12 */   public DelegateDataOutput(DataOutput parent) { this.parent = parent; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public void write(int b) throws IOException { this.parent.write(b); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void write(byte[] b) throws IOException { this.parent.write(b); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void write(byte[] b, int off, int len) throws IOException { this.parent.write(b, off, len); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void writeBoolean(boolean v) throws IOException { this.parent.writeBoolean(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void writeByte(int v) throws IOException { this.parent.writeByte(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void writeShort(int v) throws IOException { this.parent.writeShort(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void writeChar(int v) throws IOException { this.parent.writeChar(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public void writeInt(int v) throws IOException { this.parent.writeInt(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public void writeLong(long v) throws IOException { this.parent.writeLong(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public void writeFloat(float v) throws IOException { this.parent.writeFloat(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public void writeDouble(double v) throws IOException { this.parent.writeDouble(v); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SuppressForbidden(reason = "Delegation is not use")
/* 73 */   public void writeBytes(String s) throws IOException { this.parent.writeBytes(s); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   public void writeChars(String s) throws IOException { this.parent.writeChars(s); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   public void writeUTF(String s) throws IOException { this.parent.writeUTF(s); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\DelegateDataOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
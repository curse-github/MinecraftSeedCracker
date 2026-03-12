/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class NetworkDataOutputStream {
/*    */   private final ByteArrayOutputStream outputStream;
/*    */   private final DataOutputStream dataOutputStream;
/*    */   
/*    */   public NetworkDataOutputStream(int size) {
/* 13 */     this.outputStream = new ByteArrayOutputStream(size);
/* 14 */     this.dataOutputStream = new DataOutputStream(this.outputStream);
/*    */   }
/*    */ 
/*    */   
/* 18 */   public void writeBytes(byte[] data) throws IOException { this.dataOutputStream.write(data, 0, data.length); }
/*    */ 
/*    */   
/*    */   public void writeString(String data) throws IOException {
/* 22 */     this.dataOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
/* 23 */     this.dataOutputStream.write(0);
/*    */   }
/*    */ 
/*    */   
/* 27 */   public void write(int data) { this.dataOutputStream.write(data); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void writeShort(short data) throws IOException { this.dataOutputStream.writeShort(Short.reverseBytes(data)); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void writeInt(int data) { this.dataOutputStream.writeInt(Integer.reverseBytes(data)); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void writeFloat(float data) throws IOException { this.dataOutputStream.writeInt(Integer.reverseBytes(Float.floatToIntBits(data))); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public byte[] toByteArray() { return this.outputStream.toByteArray(); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public void reset() { this.outputStream.reset(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\rcon\NetworkDataOutputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
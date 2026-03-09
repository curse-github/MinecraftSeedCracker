/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class LegacyProtocolUtils
/*    */ {
/*    */   public static final int CUSTOM_PAYLOAD_PACKET_ID = 250;
/*    */   public static final String CUSTOM_PAYLOAD_PACKET_PING_CHANNEL = "MC|PingHost";
/*    */   public static final int GET_INFO_PACKET_ID = 254;
/*    */   public static final int GET_INFO_PACKET_VERSION_1 = 1;
/*    */   public static final int DISCONNECT_PACKET_ID = 255;
/*    */   public static final int FAKE_PROTOCOL_VERSION = 127;
/*    */   
/*    */   public static void writeLegacyString(ByteBuf toSend, String str) {
/* 16 */     toSend.writeShort(str.length());
/* 17 */     toSend.writeCharSequence(str, StandardCharsets.UTF_16BE);
/*    */   }
/*    */   
/*    */   public static String readLegacyString(ByteBuf msg) {
/* 21 */     int charCount = msg.readShort();
/* 22 */     int byteCount = charCount * 2;
/* 23 */     String str = msg.toString(msg.readerIndex(), byteCount, StandardCharsets.UTF_16BE);
/* 24 */     msg.skipBytes(byteCount);
/* 25 */     return str;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\LegacyProtocolUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
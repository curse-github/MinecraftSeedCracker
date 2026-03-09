/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.DataAmount;
/*    */ import jdk.jfr.Enabled;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Category({"Minecraft", "Network"})
/*    */ @StackTrace(false)
/*    */ @Enabled(false)
/*    */ public abstract class PacketEvent
/*    */   extends Event
/*    */ {
/*    */   @Name("protocolId")
/*    */   @Label("Protocol Id")
/*    */   public final String protocolId;
/*    */   @Name("packetDirection")
/*    */   @Label("Packet Direction")
/*    */   public final String packetDirection;
/*    */   @Name("packetId")
/*    */   @Label("Packet Id")
/*    */   public final String packetId;
/*    */   @Name("remoteAddress")
/*    */   @Label("Remote Address")
/*    */   public final String remoteAddress;
/*    */   @Name("bytes")
/*    */   @Label("Bytes")
/*    */   @DataAmount
/*    */   public final int bytes;
/*    */   
/*    */   PacketEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int bytes) {
/* 41 */     this.protocolId = protocolId;
/* 42 */     this.packetDirection = packetDirection;
/* 43 */     this.packetId = packetId;
/* 44 */     this.remoteAddress = remoteAddress.toString();
/* 45 */     this.bytes = bytes;
/*    */   }
/*    */   
/*    */   public static final class Fields {
/*    */     public static final String REMOTE_ADDRESS = "remoteAddress";
/*    */     public static final String PROTOCOL_ID = "protocolId";
/*    */     public static final String PACKET_DIRECTION = "packetDirection";
/*    */     public static final String PACKET_ID = "packetId";
/*    */     public static final String BYTES = "bytes";
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\PacketEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Name("minecraft.PacketReceived")
/*    */ @Label("Network Packet Received")
/*    */ public class PacketReceivedEvent
/*    */   extends PacketEvent
/*    */ {
/*    */   public static final String NAME = "minecraft.PacketReceived";
/* 18 */   public static final EventType TYPE = EventType.getEventType(PacketReceivedEvent.class);
/*    */ 
/*    */   
/* 21 */   public PacketReceivedEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int readableBytes) { super(protocolId, packetDirection, packetId, remoteAddress, readableBytes); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\PacketReceivedEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
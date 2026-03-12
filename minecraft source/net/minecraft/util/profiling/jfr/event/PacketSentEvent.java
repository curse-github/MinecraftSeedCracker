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
/*    */ @Name("minecraft.PacketSent")
/*    */ @Label("Network Packet Sent")
/*    */ public class PacketSentEvent
/*    */   extends PacketEvent
/*    */ {
/*    */   public static final String NAME = "minecraft.PacketSent";
/* 18 */   public static final EventType TYPE = EventType.getEventType(PacketSentEvent.class);
/*    */ 
/*    */   
/* 21 */   public PacketSentEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int writtenBytes) { super(protocolId, packetDirection, packetId, remoteAddress, writtenBytes); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\PacketSentEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
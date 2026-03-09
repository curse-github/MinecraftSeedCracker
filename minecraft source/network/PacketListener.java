/*    */ package net.minecraft.network;
/*    */ 
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PacketListener
/*    */ {
/* 19 */   default void onPacketError(Packet packet, Exception cause) throws ReportedException { throw PacketUtils.makeReportedException(cause, packet, this); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   default DisconnectionDetails createDisconnectionInfo(Component reason, Throwable cause) { return new DisconnectionDetails(reason); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   default boolean shouldHandleMessage(Packet<?> packet) { return isAcceptingMessages(); }
/*    */ 
/*    */   
/*    */   default void fillCrashReport(CrashReport crashReport) {
/* 33 */     CrashReportCategory connection = crashReport.addCategory("Connection");
/* 34 */     connection.setDetail("Protocol", () -> protocol().id());
/* 35 */     connection.setDetail("Flow", () -> flow().toString());
/* 36 */     fillListenerSpecificCrashDetails(crashReport, connection);
/*    */   }
/*    */   
/*    */   default void fillListenerSpecificCrashDetails(CrashReport report, CrashReportCategory connectionDetails) {}
/*    */   
/*    */   PacketFlow flow();
/*    */   
/*    */   ConnectionProtocol protocol();
/*    */   
/*    */   void onDisconnect(DisconnectionDetails paramDisconnectionDetails);
/*    */   
/*    */   boolean isAcceptingMessages();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\PacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
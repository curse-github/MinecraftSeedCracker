/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements ProtocolInfo.Details
/*    */ {
/* 78 */   public ConnectionProtocol id() { return protocol; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   public PacketFlow flow() { return flow; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void listPackets(ProtocolInfo.Details.PacketVisitor output) {
/* 88 */     for (int i = 0; i < codecs.size(); i++) {
/* 89 */       ProtocolInfoBuilder.CodecEntry<?, ?, ?, ?> entry = (ProtocolInfoBuilder.CodecEntry)codecs.get(i);
/* 90 */       output.accept(entry.type, i);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
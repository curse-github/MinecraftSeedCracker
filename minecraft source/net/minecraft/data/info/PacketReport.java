/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*    */ import net.minecraft.network.protocol.game.GameProtocols;
/*    */ import net.minecraft.network.protocol.handshake.HandshakeProtocols;
/*    */ import net.minecraft.network.protocol.login.LoginProtocols;
/*    */ import net.minecraft.network.protocol.status.StatusProtocols;
/*    */ 
/*    */ public class PacketReport implements DataProvider {
/* 24 */   public PacketReport(PackOutput output) { this.output = output; }
/*    */   
/*    */   private final PackOutput output;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 29 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("packets.json");
/* 30 */     return DataProvider.saveStable(cache, serializePackets(), path);
/*    */   }
/*    */   
/*    */   private JsonElement serializePackets() {
/* 34 */     JsonObject protocols = new JsonObject();
/*    */     
/* 36 */     ((Map)Stream.of(new ProtocolInfo.DetailsProvider[] { HandshakeProtocols.SERVERBOUND_TEMPLATE, StatusProtocols.CLIENTBOUND_TEMPLATE, StatusProtocols.SERVERBOUND_TEMPLATE, LoginProtocols.CLIENTBOUND_TEMPLATE, LoginProtocols.SERVERBOUND_TEMPLATE, ConfigurationProtocols.CLIENTBOUND_TEMPLATE, ConfigurationProtocols.SERVERBOUND_TEMPLATE, GameProtocols.CLIENTBOUND_TEMPLATE, GameProtocols.SERVERBOUND_TEMPLATE
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
/* 51 */         }).map(ProtocolInfo.DetailsProvider::details)
/* 52 */       .collect(Collectors.groupingBy(ProtocolInfo.Details::id)))
/* 53 */       .forEach((protocolId, flows) -> {
/* 54 */           JsonObject protocolData = new JsonObject();
/* 55 */           protocols.add(protocolId.id(), protocolData);
/*    */           
/* 57 */           flows.forEach(());
/*    */         });
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
/* 69 */     return protocols;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 74 */   public String getName() { return "Packet Report"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\PacketReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
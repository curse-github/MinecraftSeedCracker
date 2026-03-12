/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class ServerResourcePackConfigurationTask
/*    */   implements ConfigurationTask {
/* 12 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_resource_pack");
/*    */   
/*    */   private final MinecraftServer.ServerResourcePackInfo info;
/*    */ 
/*    */   
/* 17 */   public ServerResourcePackConfigurationTask(MinecraftServer.ServerResourcePackInfo info) { this.info = info; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void start(Consumer<Packet<?>> connection) { connection.accept(new ClientboundResourcePackPushPacket(this.info.id(), this.info.url(), this.info.hash(), this.info.isRequired(), Optional.ofNullable(this.info.prompt()))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public ConfigurationTask.Type type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\config\ServerResourcePackConfigurationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
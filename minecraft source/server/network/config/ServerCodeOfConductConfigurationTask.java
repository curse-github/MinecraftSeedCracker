/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundCodeOfConductPacket;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class ServerCodeOfConductConfigurationTask
/*    */   implements ConfigurationTask {
/* 11 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_code_of_conduct");
/*    */   
/*    */   private final Supplier<String> codeOfConduct;
/*    */ 
/*    */   
/* 16 */   public ServerCodeOfConductConfigurationTask(Supplier<String> codeOfConduct) { this.codeOfConduct = codeOfConduct; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public void start(Consumer<Packet<?>> connection) { connection.accept(new ClientboundCodeOfConductPacket((String)this.codeOfConduct.get())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public ConfigurationTask.Type type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\config\ServerCodeOfConductConfigurationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
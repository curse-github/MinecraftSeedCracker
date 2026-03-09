/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistrySynchronization;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundSelectKnownPacks;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.tags.TagNetworkSerialization;
/*    */ 
/*    */ public class SynchronizeRegistriesTask
/*    */   implements ConfigurationTask {
/* 22 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("synchronize_registries");
/*    */   
/*    */   private final List<KnownPack> requestedPacks;
/*    */   private final LayeredRegistryAccess<RegistryLayer> registries;
/*    */   
/*    */   public SynchronizeRegistriesTask(List<KnownPack> knownPacks, LayeredRegistryAccess<RegistryLayer> registries) {
/* 28 */     this.requestedPacks = knownPacks;
/* 29 */     this.registries = registries;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void start(Consumer<Packet<?>> connection) { connection.accept(new ClientboundSelectKnownPacks(this.requestedPacks)); }
/*    */ 
/*    */   
/*    */   private void sendRegistries(Consumer<Packet<?>> connection, Set<KnownPack> negotiatedPacks) {
/* 38 */     RegistryOps registryOps = this.registries.compositeAccess().createSerializationContext(NbtOps.INSTANCE);
/* 39 */     RegistrySynchronization.packRegistries(registryOps, this.registries.getAccessFrom(RegistryLayer.WORLDGEN), negotiatedPacks, (registryKey, entries) -> 
/* 40 */         connection.accept(new ClientboundRegistryDataPacket(registryKey, entries)));
/*    */     
/* 42 */     connection.accept(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registries)));
/*    */   }
/*    */   
/*    */   public void handleResponse(List<KnownPack> acceptedPacks, Consumer<Packet<?>> connection) {
/* 46 */     if (acceptedPacks.equals(this.requestedPacks)) {
/* 47 */       sendRegistries(connection, Set.copyOf(this.requestedPacks));
/*    */     } else {
/*    */       
/* 50 */       sendRegistries(connection, Set.of());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public ConfigurationTask.Type type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\config\SynchronizeRegistriesTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
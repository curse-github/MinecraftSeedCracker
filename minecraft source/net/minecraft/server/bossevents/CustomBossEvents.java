/*    */ package net.minecraft.server.bossevents;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class CustomBossEvents
/*    */ {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 22 */   private static final Codec<Map<Identifier, CustomBossEvent.Packed>> EVENTS_CODEC = Codec.unboundedMap(Identifier.CODEC, CustomBossEvent.Packed.CODEC);
/*    */   
/* 24 */   private final Map<Identifier, CustomBossEvent> events = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public CustomBossEvent get(Identifier id) { return (CustomBossEvent)this.events.get(id); }
/*    */ 
/*    */   
/*    */   public CustomBossEvent create(Identifier id, Component name) {
/* 34 */     CustomBossEvent result = new CustomBossEvent(id, name);
/* 35 */     this.events.put(id, result);
/* 36 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 40 */   public void remove(CustomBossEvent event) { this.events.remove(event.getTextId()); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Collection<Identifier> getIds() { return this.events.keySet(); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public Collection<CustomBossEvent> getEvents() { return this.events.values(); }
/*    */ 
/*    */   
/*    */   public CompoundTag save(HolderLookup.Provider registries) {
/* 52 */     Map<Identifier, CustomBossEvent.Packed> packedEvents = Util.mapValues(this.events, CustomBossEvent::pack);
/* 53 */     return (CompoundTag)EVENTS_CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), packedEvents).getOrThrow();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void load(CompoundTag tag, HolderLookup.Provider registries) {
/* 59 */     Map<Identifier, CustomBossEvent.Packed> events = (Map)EVENTS_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial(error -> LOGGER.error("Failed to parse boss bar events: {}", error)).orElse(Map.of());
/* 60 */     events.forEach((id, packed) -> 
/* 61 */         this.events.put(id, CustomBossEvent.load(id, packed)));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPlayerConnect(ServerPlayer player) {
/* 66 */     for (CustomBossEvent event : this.events.values()) {
/* 67 */       event.onPlayerConnect(player);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onPlayerDisconnect(ServerPlayer player) {
/* 72 */     for (CustomBossEvent event : this.events.values())
/* 73 */       event.onPlayerDisconnect(player); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\bossevents\CustomBossEvents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
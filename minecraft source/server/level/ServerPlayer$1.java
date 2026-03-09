/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.TypedDataComponent;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
/*     */ import net.minecraft.util.HashOps;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerSynchronizer;
/*     */ import net.minecraft.world.inventory.RemoteSlot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements ContainerSynchronizer
/*     */ {
/* 298 */   private final LoadingCache<TypedDataComponent<?>, Integer> cache = CacheBuilder.newBuilder()
/* 299 */     .maximumSize(256L)
/* 300 */     .build(new CacheLoader<TypedDataComponent<?>, Integer>() {
/* 301 */         private final DynamicOps<HashCode> registryHashOps = ServerPlayer.null.this.this$0.registryAccess().createSerializationContext(HashOps.CRC32C_INSTANCE);
/*     */ 
/*     */ 
/*     */         
/* 305 */         public Integer load(TypedDataComponent<?> component) { return Integer.valueOf(((HashCode)component.encodeValue(this.registryHashOps).getOrThrow(msg -> new IllegalArgumentException("Failed to hash " + String.valueOf(component) + ": " + msg))).asInt()); }
/*     */       });
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendInitialData(AbstractContainerMenu container, List<ItemStack> slotItems, ItemStack carriedItem, int[] dataSlots) {
/* 311 */     ServerPlayer.this.connection.send(new ClientboundContainerSetContentPacket(container.containerId, container.incrementStateId(), slotItems, carriedItem));
/* 312 */     for (int slot = 0; slot < dataSlots.length; slot++) {
/* 313 */       broadcastDataValue(container, slot, dataSlots[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 319 */   public void sendSlotChange(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) { ServerPlayer.this.connection.send(new ClientboundContainerSetSlotPacket(container.containerId, container.incrementStateId(), slotIndex, itemStack)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 324 */   public void sendCarriedChange(AbstractContainerMenu container, ItemStack itemStack) { ServerPlayer.this.connection.send(new ClientboundSetCursorItemPacket(itemStack)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 329 */   public void sendDataChange(AbstractContainerMenu container, int id, int value) { broadcastDataValue(container, id, value); }
/*     */ 
/*     */ 
/*     */   
/* 333 */   private void broadcastDataValue(AbstractContainerMenu container, int id, int value) { ServerPlayer.this.connection.send(new ClientboundContainerSetDataPacket(container.containerId, id, value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public RemoteSlot createSlot() { Objects.requireNonNull(this.cache); return new RemoteSlot.Synchronized(this.cache::getUnchecked); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerPlayer$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
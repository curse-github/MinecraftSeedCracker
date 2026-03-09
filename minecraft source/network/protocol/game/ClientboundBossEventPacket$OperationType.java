/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ static enum OperationType
/*     */ {
/*  95 */   ADD(AddOperation::new),
/*  96 */   REMOVE(input -> ClientboundBossEventPacket.REMOVE_OPERATION),
/*  97 */   UPDATE_PROGRESS(UpdateProgressOperation::new),
/*  98 */   UPDATE_NAME(UpdateNameOperation::new),
/*  99 */   UPDATE_STYLE(UpdateStyleOperation::new),
/* 100 */   UPDATE_PROPERTIES(UpdatePropertiesOperation::new);
/*     */ 
/*     */   
/*     */   private final StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket.Operation> reader;
/*     */ 
/*     */   
/* 106 */   OperationType(StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket.Operation> reader) { this.reader = reader; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$OperationType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
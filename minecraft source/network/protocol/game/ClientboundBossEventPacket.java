/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class ClientboundBossEventPacket extends Object implements Packet<ClientGamePacketListener> {
/*  15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBossEventPacket> STREAM_CODEC = Packet.codec(ClientboundBossEventPacket::write, ClientboundBossEventPacket::new);
/*     */   
/*     */   private static final int FLAG_DARKEN = 1;
/*     */   
/*     */   private static final int FLAG_MUSIC = 2;
/*     */   private static final int FLAG_FOG = 4;
/*     */   private final UUID id;
/*     */   private final Operation operation;
/*     */   
/*     */   private ClientboundBossEventPacket(UUID id, Operation operation) {
/*  25 */     this.id = id;
/*  26 */     this.operation = operation;
/*     */   }
/*     */   
/*     */   private ClientboundBossEventPacket(RegistryFriendlyByteBuf input) {
/*  30 */     this.id = input.readUUID();
/*  31 */     OperationType type = (OperationType)input.readEnum(OperationType.class);
/*  32 */     this.operation = (Operation)type.reader.decode(input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public static ClientboundBossEventPacket createAddPacket(BossEvent event) { return new ClientboundBossEventPacket(event.getId(), new AddOperation(event)); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static ClientboundBossEventPacket createRemovePacket(UUID id) { return new ClientboundBossEventPacket(id, REMOVE_OPERATION); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static ClientboundBossEventPacket createUpdateProgressPacket(BossEvent event) { return new ClientboundBossEventPacket(event.getId(), new UpdateProgressOperation(event.getProgress())); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static ClientboundBossEventPacket createUpdateNamePacket(BossEvent event) { return new ClientboundBossEventPacket(event.getId(), new UpdateNameOperation(event.getName())); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static ClientboundBossEventPacket createUpdateStylePacket(BossEvent event) { return new ClientboundBossEventPacket(event.getId(), new UpdateStyleOperation(event.getColor(), event.getOverlay())); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static ClientboundBossEventPacket createUpdatePropertiesPacket(BossEvent event) { return new ClientboundBossEventPacket(event.getId(), new UpdatePropertiesOperation(event.shouldDarkenScreen(), event.shouldPlayBossMusic(), event.shouldCreateWorldFog())); }
/*     */ 
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  61 */     output.writeUUID(this.id);
/*  62 */     output.writeEnum(this.operation.getType());
/*  63 */     this.operation.write(output);
/*     */   }
/*     */   
/*     */   private static int encodeProperties(boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/*  67 */     int properties = 0;
/*  68 */     if (darkenScreen) {
/*  69 */       properties |= 0x1;
/*     */     }
/*  71 */     if (playMusic) {
/*  72 */       properties |= 0x2;
/*     */     }
/*  74 */     if (createWorldFog) {
/*  75 */       properties |= 0x4;
/*     */     }
/*  77 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public PacketType<ClientboundBossEventPacket> type() { return GamePacketTypes.CLIENTBOUND_BOSS_EVENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public void handle(ClientGamePacketListener listener) { listener.handleBossUpdate(this); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void dispatch(Handler handler) { this.operation.dispatch(this.id, handler); }
/*     */   
/*     */   private enum OperationType
/*     */   {
/*  95 */     ADD(AddOperation::new),
/*  96 */     REMOVE(input -> ClientboundBossEventPacket.REMOVE_OPERATION),
/*  97 */     UPDATE_PROGRESS(UpdateProgressOperation::new),
/*  98 */     UPDATE_NAME(UpdateNameOperation::new),
/*  99 */     UPDATE_STYLE(UpdateStyleOperation::new),
/* 100 */     UPDATE_PROPERTIES(UpdatePropertiesOperation::new);
/*     */ 
/*     */     
/*     */     private final StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket.Operation> reader;
/*     */ 
/*     */     
/* 106 */     OperationType(StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket.Operation> reader) { this.reader = reader; }
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface Handler
/*     */   {
/*     */     default void add(UUID id, Component name, float progress, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {}
/*     */ 
/*     */     
/*     */     default void remove(UUID id) {}
/*     */ 
/*     */     
/*     */     default void updateProgress(UUID id, float progress) {}
/*     */ 
/*     */     
/*     */     default void updateName(UUID id, Component name) {}
/*     */ 
/*     */     
/*     */     default void updateStyle(UUID id, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {}
/*     */ 
/*     */     
/*     */     default void updateProperties(UUID id, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AddOperation
/*     */     implements Operation
/*     */   {
/*     */     private final Component name;
/*     */     
/*     */     private final float progress;
/*     */     
/*     */     private final BossEvent.BossBarColor color;
/*     */     
/*     */     private final BossEvent.BossBarOverlay overlay;
/*     */     
/*     */     private final boolean darkenScreen;
/*     */     
/*     */     private final boolean playMusic;
/*     */     private final boolean createWorldFog;
/*     */     
/*     */     private AddOperation(BossEvent event) {
/* 148 */       this.name = event.getName();
/* 149 */       this.progress = event.getProgress();
/* 150 */       this.color = event.getColor();
/* 151 */       this.overlay = event.getOverlay();
/* 152 */       this.darkenScreen = event.shouldDarkenScreen();
/* 153 */       this.playMusic = event.shouldPlayBossMusic();
/* 154 */       this.createWorldFog = event.shouldCreateWorldFog();
/*     */     }
/*     */     
/*     */     private AddOperation(RegistryFriendlyByteBuf input) {
/* 158 */       this.name = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 159 */       this.progress = input.readFloat();
/* 160 */       this.color = (BossEvent.BossBarColor)input.readEnum(BossEvent.BossBarColor.class);
/* 161 */       this.overlay = (BossEvent.BossBarOverlay)input.readEnum(BossEvent.BossBarOverlay.class);
/* 162 */       int flags = input.readUnsignedByte();
/* 163 */       this.darkenScreen = ((flags & true) > 0);
/* 164 */       this.playMusic = ((flags & 0x2) > 0);
/* 165 */       this.createWorldFog = ((flags & 0x4) > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 170 */     public ClientboundBossEventPacket.OperationType getType() { return ClientboundBossEventPacket.OperationType.ADD; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.add(id, this.name, this.progress, this.color, this.overlay, this.darkenScreen, this.playMusic, this.createWorldFog); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(RegistryFriendlyByteBuf output) {
/* 180 */       ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.name);
/* 181 */       output.writeFloat(this.progress);
/* 182 */       output.writeEnum(this.color);
/* 183 */       output.writeEnum(this.overlay);
/* 184 */       output.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
/*     */     }
/*     */   }
/*     */   
/* 188 */   private static final Operation REMOVE_OPERATION = new Operation()
/*     */     {
/*     */       public ClientboundBossEventPacket.OperationType getType() {
/* 191 */         return ClientboundBossEventPacket.OperationType.REMOVE;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 196 */       public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.remove(id); }
/*     */       
/*     */       public void write(RegistryFriendlyByteBuf output) {}
/*     */     };
/*     */   
/*     */   private static final class UpdateProgressOperation extends Record implements Operation {
/*     */     private final float progress;
/*     */     
/* 204 */     private UpdateProgressOperation(float progress) { this.progress = progress; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 204 */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation; } public float progress() { return this.progress; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateProgressOperation;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/* 206 */     private UpdateProgressOperation(RegistryFriendlyByteBuf input) { this(input.readFloat()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     public ClientboundBossEventPacket.OperationType getType() { return ClientboundBossEventPacket.OperationType.UPDATE_PROGRESS; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.updateProgress(id, this.progress); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     public void write(RegistryFriendlyByteBuf output) { output.writeFloat(this.progress); } }
/*     */   
/*     */   private static final class UpdateNameOperation extends Record implements Operation { private final Component name;
/*     */     
/* 225 */     private UpdateNameOperation(Component name) { this.name = name; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket$UpdateNameOperation;
/* 225 */       //   0	8	1	o	Ljava/lang/Object; } public Component name() { return this.name; }
/*     */     
/* 227 */     private UpdateNameOperation(RegistryFriendlyByteBuf input) { this((Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     public ClientboundBossEventPacket.OperationType getType() { return ClientboundBossEventPacket.OperationType.UPDATE_NAME; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.updateName(id, this.name); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     public void write(RegistryFriendlyByteBuf output) { ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.name); } }
/*     */ 
/*     */   
/*     */   private static class UpdateStyleOperation
/*     */     implements Operation {
/*     */     private final BossEvent.BossBarColor color;
/*     */     private final BossEvent.BossBarOverlay overlay;
/*     */     
/*     */     private UpdateStyleOperation(BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {
/* 251 */       this.color = color;
/* 252 */       this.overlay = overlay;
/*     */     }
/*     */     
/*     */     private UpdateStyleOperation(RegistryFriendlyByteBuf input) {
/* 256 */       this.color = (BossEvent.BossBarColor)input.readEnum(BossEvent.BossBarColor.class);
/* 257 */       this.overlay = (BossEvent.BossBarOverlay)input.readEnum(BossEvent.BossBarOverlay.class);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 262 */     public ClientboundBossEventPacket.OperationType getType() { return ClientboundBossEventPacket.OperationType.UPDATE_STYLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.updateStyle(id, this.color, this.overlay); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(RegistryFriendlyByteBuf output) {
/* 272 */       output.writeEnum(this.color);
/* 273 */       output.writeEnum(this.overlay);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UpdatePropertiesOperation implements Operation {
/*     */     private final boolean darkenScreen;
/*     */     private final boolean playMusic;
/*     */     private final boolean createWorldFog;
/*     */     
/*     */     private UpdatePropertiesOperation(boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 283 */       this.darkenScreen = darkenScreen;
/* 284 */       this.playMusic = playMusic;
/* 285 */       this.createWorldFog = createWorldFog;
/*     */     }
/*     */     
/*     */     private UpdatePropertiesOperation(RegistryFriendlyByteBuf input) {
/* 289 */       int flags = input.readUnsignedByte();
/* 290 */       this.darkenScreen = ((flags & true) > 0);
/* 291 */       this.playMusic = ((flags & 0x2) > 0);
/* 292 */       this.createWorldFog = ((flags & 0x4) > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 297 */     public ClientboundBossEventPacket.OperationType getType() { return ClientboundBossEventPacket.OperationType.UPDATE_PROPERTIES; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.updateProperties(id, this.darkenScreen, this.playMusic, this.createWorldFog); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     public void write(RegistryFriendlyByteBuf output) { output.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog)); }
/*     */   }
/*     */   
/*     */   private static interface Operation {
/*     */     ClientboundBossEventPacket.OperationType getType();
/*     */     
/*     */     void dispatch(UUID param1UUID, ClientboundBossEventPacket.Handler param1Handler);
/*     */     
/*     */     void write(RegistryFriendlyByteBuf param1RegistryFriendlyByteBuf);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
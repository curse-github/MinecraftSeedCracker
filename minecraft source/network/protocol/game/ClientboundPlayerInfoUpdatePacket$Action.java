/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Action
/*     */ {
/*  92 */   ADD_PLAYER((entry, input) -> {
/*     */       
/*  94 */       String name = (String)ByteBufCodecs.PLAYER_NAME.decode(input);
/*  95 */       PropertyMap properties = (PropertyMap)ByteBufCodecs.GAME_PROFILE_PROPERTIES.decode(input);
/*  96 */       entry.profile = new GameProfile(entry.profileId, name, properties);
/*     */     }(output, entry) -> {
/*     */       
/*  99 */       GameProfile profile = (GameProfile)Objects.requireNonNull(entry.profile());
/* 100 */       ByteBufCodecs.PLAYER_NAME.encode(output, profile.name());
/* 101 */       ByteBufCodecs.GAME_PROFILE_PROPERTIES.encode(output, profile.properties());
/*     */     
/*     */     }),
/* 104 */   INITIALIZE_CHAT((entry, input) -> 
/* 105 */     entry.chatSession = (RemoteChatSession.Data)input.readNullable(RemoteChatSession.Data::read), (output, entry) -> 
/* 106 */     output.writeNullable(entry.chatSession, RemoteChatSession.Data::write)),
/*     */   
/* 108 */   UPDATE_GAME_MODE((entry, input) -> 
/* 109 */     entry.gameMode = GameType.byId(input.readVarInt()), (output, entry) -> 
/* 110 */     output.writeVarInt(entry.gameMode().getId())),
/*     */   
/* 112 */   UPDATE_LISTED((entry, input) -> 
/* 113 */     entry.listed = input.readBoolean(), (output, entry) -> 
/* 114 */     output.writeBoolean(entry.listed())),
/*     */   
/* 116 */   UPDATE_LATENCY((entry, input) -> 
/* 117 */     entry.latency = input.readVarInt(), (output, entry) -> 
/* 118 */     output.writeVarInt(entry.latency())),
/*     */   
/* 120 */   UPDATE_DISPLAY_NAME((entry, input) -> 
/* 121 */     entry.displayName = (Component)FriendlyByteBuf.readNullable(input, ComponentSerialization.TRUSTED_STREAM_CODEC), (output, entry) -> 
/* 122 */     FriendlyByteBuf.writeNullable(output, entry.displayName(), ComponentSerialization.TRUSTED_STREAM_CODEC)),
/*     */   
/* 124 */   UPDATE_LIST_ORDER((entry, input) -> 
/* 125 */     entry.listOrder = input.readVarInt(), (output, entry) -> 
/* 126 */     output.writeVarInt(entry.listOrder)),
/*     */   
/* 128 */   UPDATE_HAT((entry, input) -> 
/* 129 */     entry.showHat = input.readBoolean(), (output, entry) -> 
/* 130 */     output.writeBoolean(entry.showHat));
/*     */   
/*     */   private final Reader reader;
/*     */   
/*     */   private final Writer writer;
/*     */ 
/*     */   
/*     */   Action(Reader reader, Writer writer) {
/* 138 */     this.reader = reader;
/* 139 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   public static interface Reader {
/*     */     void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder param2EntryBuilder, RegistryFriendlyByteBuf param2RegistryFriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public static interface Writer {
/*     */     void write(RegistryFriendlyByteBuf param2RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket.Entry param2Entry);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
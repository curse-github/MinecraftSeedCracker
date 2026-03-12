/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.SkipPacketDecoderException;
/*     */ import net.minecraft.network.SkipPacketEncoderException;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.CodecModifier;
/*     */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*     */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*     */ import net.minecraft.network.protocol.UnboundProtocol;
/*     */ import net.minecraft.network.protocol.common.ClientboundClearDialogPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundCustomReportDetailsPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundPingPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundTransferPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*     */ import net.minecraft.network.protocol.common.CommonPacketTypes;
/*     */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundPongPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
/*     */ import net.minecraft.network.protocol.cookie.CookiePacketTypes;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*     */ import net.minecraft.network.protocol.ping.PingPacketTypes;
/*     */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameProtocols
/*     */ {
/*  44 */   public static final CodecModifier<RegistryFriendlyByteBuf, ServerboundSetCreativeModeSlotPacket, Context> HAS_INFINITE_MATERIALS = (original, context) -> new StreamCodec<RegistryFriendlyByteBuf, ServerboundSetCreativeModeSlotPacket>()
/*     */     {
/*     */ 
/*     */       
/*     */       public ServerboundSetCreativeModeSlotPacket decode(RegistryFriendlyByteBuf input)
/*     */       {
/*  50 */         if (!context.hasInfiniteMaterials()) {
/*  51 */           throw new SkipPacketDecoderException("Not in creative mode");
/*     */         }
/*  53 */         return (ServerboundSetCreativeModeSlotPacket)original.decode(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public void encode(RegistryFriendlyByteBuf output, ServerboundSetCreativeModeSlotPacket value) {
/*  58 */         if (!context.hasInfiniteMaterials()) {
/*  59 */           throw new SkipPacketEncoderException("Not in creative mode");
/*     */         }
/*  61 */         original.encode(output, value);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final UnboundProtocol<ServerGamePacketListener, RegistryFriendlyByteBuf, Context> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.contextServerboundProtocol(ConnectionProtocol.PLAY, builder -> builder
/*  72 */       .addPacket(GamePacketTypes.SERVERBOUND_ACCEPT_TELEPORTATION, ServerboundAcceptTeleportationPacket.STREAM_CODEC)
/*  73 */       .addPacket(GamePacketTypes.SERVERBOUND_BLOCK_ENTITY_TAG_QUERY, ServerboundBlockEntityTagQueryPacket.STREAM_CODEC)
/*  74 */       .addPacket(GamePacketTypes.SERVERBOUND_BUNDLE_ITEM_SELECTED, ServerboundSelectBundleItemPacket.STREAM_CODEC)
/*  75 */       .addPacket(GamePacketTypes.SERVERBOUND_CHANGE_DIFFICULTY, ServerboundChangeDifficultyPacket.STREAM_CODEC)
/*  76 */       .addPacket(GamePacketTypes.SERVERBOUND_CHANGE_GAME_MODE, ServerboundChangeGameModePacket.STREAM_CODEC)
/*  77 */       .addPacket(GamePacketTypes.SERVERBOUND_CHAT_ACK, ServerboundChatAckPacket.STREAM_CODEC)
/*  78 */       .addPacket(GamePacketTypes.SERVERBOUND_CHAT_COMMAND, ServerboundChatCommandPacket.STREAM_CODEC)
/*  79 */       .addPacket(GamePacketTypes.SERVERBOUND_CHAT_COMMAND_SIGNED, ServerboundChatCommandSignedPacket.STREAM_CODEC)
/*  80 */       .addPacket(GamePacketTypes.SERVERBOUND_CHAT, ServerboundChatPacket.STREAM_CODEC)
/*  81 */       .addPacket(GamePacketTypes.SERVERBOUND_CHAT_SESSION_UPDATE, ServerboundChatSessionUpdatePacket.STREAM_CODEC)
/*  82 */       .addPacket(GamePacketTypes.SERVERBOUND_CHUNK_BATCH_RECEIVED, ServerboundChunkBatchReceivedPacket.STREAM_CODEC)
/*  83 */       .addPacket(GamePacketTypes.SERVERBOUND_CLIENT_COMMAND, ServerboundClientCommandPacket.STREAM_CODEC)
/*  84 */       .addPacket(GamePacketTypes.SERVERBOUND_CLIENT_TICK_END, ServerboundClientTickEndPacket.STREAM_CODEC)
/*  85 */       .addPacket(CommonPacketTypes.SERVERBOUND_CLIENT_INFORMATION, ServerboundClientInformationPacket.STREAM_CODEC)
/*  86 */       .addPacket(GamePacketTypes.SERVERBOUND_COMMAND_SUGGESTION, ServerboundCommandSuggestionPacket.STREAM_CODEC)
/*  87 */       .addPacket(GamePacketTypes.SERVERBOUND_CONFIGURATION_ACKNOWLEDGED, ServerboundConfigurationAcknowledgedPacket.STREAM_CODEC)
/*  88 */       .addPacket(GamePacketTypes.SERVERBOUND_CONTAINER_BUTTON_CLICK, ServerboundContainerButtonClickPacket.STREAM_CODEC)
/*  89 */       .addPacket(GamePacketTypes.SERVERBOUND_CONTAINER_CLICK, ServerboundContainerClickPacket.STREAM_CODEC)
/*  90 */       .addPacket(GamePacketTypes.SERVERBOUND_CONTAINER_CLOSE, ServerboundContainerClosePacket.STREAM_CODEC)
/*  91 */       .addPacket(GamePacketTypes.SERVERBOUND_CONTAINER_SLOT_STATE_CHANGED, ServerboundContainerSlotStateChangedPacket.STREAM_CODEC)
/*  92 */       .addPacket(CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE, ServerboundCookieResponsePacket.STREAM_CODEC)
/*  93 */       .addPacket(CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD, ServerboundCustomPayloadPacket.STREAM_CODEC)
/*  94 */       .addPacket(GamePacketTypes.SERVERBOUND_DEBUG_SUBSCRIPTION_REQUEST, ServerboundDebugSubscriptionRequestPacket.STREAM_CODEC)
/*  95 */       .addPacket(GamePacketTypes.SERVERBOUND_EDIT_BOOK, ServerboundEditBookPacket.STREAM_CODEC)
/*  96 */       .addPacket(GamePacketTypes.SERVERBOUND_ENTITY_TAG_QUERY, ServerboundEntityTagQueryPacket.STREAM_CODEC)
/*  97 */       .addPacket(GamePacketTypes.SERVERBOUND_INTERACT, ServerboundInteractPacket.STREAM_CODEC)
/*  98 */       .addPacket(GamePacketTypes.SERVERBOUND_JIGSAW_GENERATE, ServerboundJigsawGeneratePacket.STREAM_CODEC)
/*  99 */       .addPacket(CommonPacketTypes.SERVERBOUND_KEEP_ALIVE, ServerboundKeepAlivePacket.STREAM_CODEC)
/* 100 */       .addPacket(GamePacketTypes.SERVERBOUND_LOCK_DIFFICULTY, ServerboundLockDifficultyPacket.STREAM_CODEC)
/* 101 */       .addPacket(GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS, ServerboundMovePlayerPacket.Pos.STREAM_CODEC)
/* 102 */       .addPacket(GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS_ROT, ServerboundMovePlayerPacket.PosRot.STREAM_CODEC)
/* 103 */       .addPacket(GamePacketTypes.SERVERBOUND_MOVE_PLAYER_ROT, ServerboundMovePlayerPacket.Rot.STREAM_CODEC)
/* 104 */       .addPacket(GamePacketTypes.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY, ServerboundMovePlayerPacket.StatusOnly.STREAM_CODEC)
/* 105 */       .addPacket(GamePacketTypes.SERVERBOUND_MOVE_VEHICLE, ServerboundMoveVehiclePacket.STREAM_CODEC)
/* 106 */       .addPacket(GamePacketTypes.SERVERBOUND_PADDLE_BOAT, ServerboundPaddleBoatPacket.STREAM_CODEC)
/* 107 */       .addPacket(GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_BLOCK, ServerboundPickItemFromBlockPacket.STREAM_CODEC)
/* 108 */       .addPacket(GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_ENTITY, ServerboundPickItemFromEntityPacket.STREAM_CODEC)
/* 109 */       .addPacket(PingPacketTypes.SERVERBOUND_PING_REQUEST, ServerboundPingRequestPacket.STREAM_CODEC)
/* 110 */       .addPacket(GamePacketTypes.SERVERBOUND_PLACE_RECIPE, ServerboundPlaceRecipePacket.STREAM_CODEC)
/* 111 */       .addPacket(GamePacketTypes.SERVERBOUND_PLAYER_ABILITIES, ServerboundPlayerAbilitiesPacket.STREAM_CODEC)
/* 112 */       .addPacket(GamePacketTypes.SERVERBOUND_PLAYER_ACTION, ServerboundPlayerActionPacket.STREAM_CODEC)
/* 113 */       .addPacket(GamePacketTypes.SERVERBOUND_PLAYER_COMMAND, ServerboundPlayerCommandPacket.STREAM_CODEC)
/* 114 */       .addPacket(GamePacketTypes.SERVERBOUND_PLAYER_INPUT, ServerboundPlayerInputPacket.STREAM_CODEC)
/* 115 */       .addPacket(GamePacketTypes.SERVERBOUND_PLAYER_LOADED, ServerboundPlayerLoadedPacket.STREAM_CODEC)
/* 116 */       .addPacket(CommonPacketTypes.SERVERBOUND_PONG, ServerboundPongPacket.STREAM_CODEC)
/* 117 */       .addPacket(GamePacketTypes.SERVERBOUND_RECIPE_BOOK_CHANGE_SETTINGS, ServerboundRecipeBookChangeSettingsPacket.STREAM_CODEC)
/* 118 */       .addPacket(GamePacketTypes.SERVERBOUND_RECIPE_BOOK_SEEN_RECIPE, ServerboundRecipeBookSeenRecipePacket.STREAM_CODEC)
/* 119 */       .addPacket(GamePacketTypes.SERVERBOUND_RENAME_ITEM, ServerboundRenameItemPacket.STREAM_CODEC)
/* 120 */       .addPacket(CommonPacketTypes.SERVERBOUND_RESOURCE_PACK, ServerboundResourcePackPacket.STREAM_CODEC)
/* 121 */       .addPacket(GamePacketTypes.SERVERBOUND_SEEN_ADVANCEMENTS, ServerboundSeenAdvancementsPacket.STREAM_CODEC)
/* 122 */       .addPacket(GamePacketTypes.SERVERBOUND_SELECT_TRADE, ServerboundSelectTradePacket.STREAM_CODEC)
/* 123 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_BEACON, ServerboundSetBeaconPacket.STREAM_CODEC)
/* 124 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_CARRIED_ITEM, ServerboundSetCarriedItemPacket.STREAM_CODEC)
/* 125 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_COMMAND_BLOCK, ServerboundSetCommandBlockPacket.STREAM_CODEC)
/* 126 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_COMMAND_MINECART, ServerboundSetCommandMinecartPacket.STREAM_CODEC)
/* 127 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_CREATIVE_MODE_SLOT, ServerboundSetCreativeModeSlotPacket.STREAM_CODEC, HAS_INFINITE_MATERIALS)
/* 128 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_JIGSAW_BLOCK, ServerboundSetJigsawBlockPacket.STREAM_CODEC)
/* 129 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_STRUCTURE_BLOCK, ServerboundSetStructureBlockPacket.STREAM_CODEC)
/* 130 */       .addPacket(GamePacketTypes.SERVERBOUND_SET_TEST_BLOCK, ServerboundSetTestBlockPacket.STREAM_CODEC)
/* 131 */       .addPacket(GamePacketTypes.SERVERBOUND_SIGN_UPDATE, ServerboundSignUpdatePacket.STREAM_CODEC)
/* 132 */       .addPacket(GamePacketTypes.SERVERBOUND_SWING, ServerboundSwingPacket.STREAM_CODEC)
/* 133 */       .addPacket(GamePacketTypes.SERVERBOUND_TELEPORT_TO_ENTITY, ServerboundTeleportToEntityPacket.STREAM_CODEC)
/* 134 */       .addPacket(GamePacketTypes.SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION, ServerboundTestInstanceBlockActionPacket.STREAM_CODEC)
/* 135 */       .addPacket(GamePacketTypes.SERVERBOUND_USE_ITEM_ON, ServerboundUseItemOnPacket.STREAM_CODEC)
/* 136 */       .addPacket(GamePacketTypes.SERVERBOUND_USE_ITEM, ServerboundUseItemPacket.STREAM_CODEC)
/* 137 */       .addPacket(CommonPacketTypes.SERVERBOUND_CUSTOM_CLICK_ACTION, ServerboundCustomClickActionPacket.STREAM_CODEC));
/*     */ 
/*     */   
/* 140 */   public static final SimpleUnboundProtocol<ClientGamePacketListener, RegistryFriendlyByteBuf> CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.PLAY, builder -> builder
/* 141 */       .withBundlePacket(GamePacketTypes.CLIENTBOUND_BUNDLE, ClientboundBundlePacket::new, new ClientboundBundleDelimiterPacket())
/* 142 */       .addPacket(GamePacketTypes.CLIENTBOUND_ADD_ENTITY, ClientboundAddEntityPacket.STREAM_CODEC)
/* 143 */       .addPacket(GamePacketTypes.CLIENTBOUND_ANIMATE, ClientboundAnimatePacket.STREAM_CODEC)
/* 144 */       .addPacket(GamePacketTypes.CLIENTBOUND_AWARD_STATS, ClientboundAwardStatsPacket.STREAM_CODEC)
/* 145 */       .addPacket(GamePacketTypes.CLIENTBOUND_BLOCK_CHANGED_ACK, ClientboundBlockChangedAckPacket.STREAM_CODEC)
/* 146 */       .addPacket(GamePacketTypes.CLIENTBOUND_BLOCK_DESTRUCTION, ClientboundBlockDestructionPacket.STREAM_CODEC)
/* 147 */       .addPacket(GamePacketTypes.CLIENTBOUND_BLOCK_ENTITY_DATA, ClientboundBlockEntityDataPacket.STREAM_CODEC)
/* 148 */       .addPacket(GamePacketTypes.CLIENTBOUND_BLOCK_EVENT, ClientboundBlockEventPacket.STREAM_CODEC)
/* 149 */       .addPacket(GamePacketTypes.CLIENTBOUND_BLOCK_UPDATE, ClientboundBlockUpdatePacket.STREAM_CODEC)
/* 150 */       .addPacket(GamePacketTypes.CLIENTBOUND_BOSS_EVENT, ClientboundBossEventPacket.STREAM_CODEC)
/* 151 */       .addPacket(GamePacketTypes.CLIENTBOUND_CHANGE_DIFFICULTY, ClientboundChangeDifficultyPacket.STREAM_CODEC)
/* 152 */       .addPacket(GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_FINISHED, ClientboundChunkBatchFinishedPacket.STREAM_CODEC)
/* 153 */       .addPacket(GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_START, ClientboundChunkBatchStartPacket.STREAM_CODEC)
/* 154 */       .addPacket(GamePacketTypes.CLIENTBOUND_CHUNKS_BIOMES, ClientboundChunksBiomesPacket.STREAM_CODEC)
/* 155 */       .addPacket(GamePacketTypes.CLIENTBOUND_CLEAR_TITLES, ClientboundClearTitlesPacket.STREAM_CODEC)
/* 156 */       .addPacket(GamePacketTypes.CLIENTBOUND_COMMAND_SUGGESTIONS, ClientboundCommandSuggestionsPacket.STREAM_CODEC)
/* 157 */       .addPacket(GamePacketTypes.CLIENTBOUND_COMMANDS, ClientboundCommandsPacket.STREAM_CODEC)
/* 158 */       .addPacket(GamePacketTypes.CLIENTBOUND_CONTAINER_CLOSE, ClientboundContainerClosePacket.STREAM_CODEC)
/* 159 */       .addPacket(GamePacketTypes.CLIENTBOUND_CONTAINER_SET_CONTENT, ClientboundContainerSetContentPacket.STREAM_CODEC)
/* 160 */       .addPacket(GamePacketTypes.CLIENTBOUND_CONTAINER_SET_DATA, ClientboundContainerSetDataPacket.STREAM_CODEC)
/* 161 */       .addPacket(GamePacketTypes.CLIENTBOUND_CONTAINER_SET_SLOT, ClientboundContainerSetSlotPacket.STREAM_CODEC)
/* 162 */       .addPacket(CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST, ClientboundCookieRequestPacket.STREAM_CODEC)
/* 163 */       .addPacket(GamePacketTypes.CLIENTBOUND_COOLDOWN, ClientboundCooldownPacket.STREAM_CODEC)
/* 164 */       .addPacket(GamePacketTypes.CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS, ClientboundCustomChatCompletionsPacket.STREAM_CODEC)
/* 165 */       .addPacket(CommonPacketTypes.CLIENTBOUND_CUSTOM_PAYLOAD, ClientboundCustomPayloadPacket.GAMEPLAY_STREAM_CODEC)
/* 166 */       .addPacket(GamePacketTypes.CLIENTBOUND_DAMAGE_EVENT, ClientboundDamageEventPacket.STREAM_CODEC)
/* 167 */       .addPacket(GamePacketTypes.CLIENTBOUND_DEBUG_BLOCK_VALUE, ClientboundDebugBlockValuePacket.STREAM_CODEC)
/* 168 */       .addPacket(GamePacketTypes.CLIENTBOUND_DEBUG_CHUNK_VALUE, ClientboundDebugChunkValuePacket.STREAM_CODEC)
/* 169 */       .addPacket(GamePacketTypes.CLIENTBOUND_DEBUG_ENTITY_VALUE, ClientboundDebugEntityValuePacket.STREAM_CODEC)
/* 170 */       .addPacket(GamePacketTypes.CLIENTBOUND_DEBUG_EVENT, ClientboundDebugEventPacket.STREAM_CODEC)
/* 171 */       .addPacket(GamePacketTypes.CLIENTBOUND_DEBUG_SAMPLE, ClientboundDebugSamplePacket.STREAM_CODEC)
/* 172 */       .addPacket(GamePacketTypes.CLIENTBOUND_DELETE_CHAT, ClientboundDeleteChatPacket.STREAM_CODEC)
/* 173 */       .addPacket(CommonPacketTypes.CLIENTBOUND_DISCONNECT, ClientboundDisconnectPacket.STREAM_CODEC)
/* 174 */       .addPacket(GamePacketTypes.CLIENTBOUND_DISGUISED_CHAT, ClientboundDisguisedChatPacket.STREAM_CODEC)
/* 175 */       .addPacket(GamePacketTypes.CLIENTBOUND_ENTITY_EVENT, ClientboundEntityEventPacket.STREAM_CODEC)
/* 176 */       .addPacket(GamePacketTypes.CLIENTBOUND_ENTITY_POSITION_SYNC, ClientboundEntityPositionSyncPacket.STREAM_CODEC)
/* 177 */       .addPacket(GamePacketTypes.CLIENTBOUND_EXPLODE, ClientboundExplodePacket.STREAM_CODEC)
/* 178 */       .addPacket(GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK, ClientboundForgetLevelChunkPacket.STREAM_CODEC)
/* 179 */       .addPacket(GamePacketTypes.CLIENTBOUND_GAME_EVENT, ClientboundGameEventPacket.STREAM_CODEC)
/* 180 */       .addPacket(GamePacketTypes.CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS, ClientboundGameTestHighlightPosPacket.STREAM_CODEC)
/* 181 */       .addPacket(GamePacketTypes.CLIENTBOUND_MOUNT_SCREEN_OPEN, ClientboundMountScreenOpenPacket.STREAM_CODEC)
/* 182 */       .addPacket(GamePacketTypes.CLIENTBOUND_HURT_ANIMATION, ClientboundHurtAnimationPacket.STREAM_CODEC)
/* 183 */       .addPacket(GamePacketTypes.CLIENTBOUND_INITIALIZE_BORDER, ClientboundInitializeBorderPacket.STREAM_CODEC)
/* 184 */       .addPacket(CommonPacketTypes.CLIENTBOUND_KEEP_ALIVE, ClientboundKeepAlivePacket.STREAM_CODEC)
/* 185 */       .addPacket(GamePacketTypes.CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT, ClientboundLevelChunkWithLightPacket.STREAM_CODEC)
/* 186 */       .addPacket(GamePacketTypes.CLIENTBOUND_LEVEL_EVENT, ClientboundLevelEventPacket.STREAM_CODEC)
/* 187 */       .addPacket(GamePacketTypes.CLIENTBOUND_LEVEL_PARTICLES, ClientboundLevelParticlesPacket.STREAM_CODEC)
/* 188 */       .addPacket(GamePacketTypes.CLIENTBOUND_LIGHT_UPDATE, ClientboundLightUpdatePacket.STREAM_CODEC)
/* 189 */       .addPacket(GamePacketTypes.CLIENTBOUND_LOGIN, ClientboundLoginPacket.STREAM_CODEC)
/* 190 */       .addPacket(GamePacketTypes.CLIENTBOUND_MAP_ITEM_DATA, ClientboundMapItemDataPacket.STREAM_CODEC)
/* 191 */       .addPacket(GamePacketTypes.CLIENTBOUND_MERCHANT_OFFERS, ClientboundMerchantOffersPacket.STREAM_CODEC)
/* 192 */       .addPacket(GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS, ClientboundMoveEntityPacket.Pos.STREAM_CODEC)
/* 193 */       .addPacket(GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS_ROT, ClientboundMoveEntityPacket.PosRot.STREAM_CODEC)
/* 194 */       .addPacket(GamePacketTypes.CLIENTBOUND_MOVE_MINECART_ALONG_TRACK, ClientboundMoveMinecartPacket.STREAM_CODEC)
/* 195 */       .addPacket(GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_ROT, ClientboundMoveEntityPacket.Rot.STREAM_CODEC)
/* 196 */       .addPacket(GamePacketTypes.CLIENTBOUND_MOVE_VEHICLE, ClientboundMoveVehiclePacket.STREAM_CODEC)
/* 197 */       .addPacket(GamePacketTypes.CLIENTBOUND_OPEN_BOOK, ClientboundOpenBookPacket.STREAM_CODEC)
/* 198 */       .addPacket(GamePacketTypes.CLIENTBOUND_OPEN_SCREEN, ClientboundOpenScreenPacket.STREAM_CODEC)
/* 199 */       .addPacket(GamePacketTypes.CLIENTBOUND_OPEN_SIGN_EDITOR, ClientboundOpenSignEditorPacket.STREAM_CODEC)
/* 200 */       .addPacket(CommonPacketTypes.CLIENTBOUND_PING, ClientboundPingPacket.STREAM_CODEC)
/* 201 */       .addPacket(PingPacketTypes.CLIENTBOUND_PONG_RESPONSE, ClientboundPongResponsePacket.STREAM_CODEC)
/* 202 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLACE_GHOST_RECIPE, ClientboundPlaceGhostRecipePacket.STREAM_CODEC)
/* 203 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_ABILITIES, ClientboundPlayerAbilitiesPacket.STREAM_CODEC)
/* 204 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_CHAT, ClientboundPlayerChatPacket.STREAM_CODEC)
/* 205 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_END, ClientboundPlayerCombatEndPacket.STREAM_CODEC)
/* 206 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_ENTER, ClientboundPlayerCombatEnterPacket.STREAM_CODEC)
/* 207 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_KILL, ClientboundPlayerCombatKillPacket.STREAM_CODEC)
/* 208 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_INFO_REMOVE, ClientboundPlayerInfoRemovePacket.STREAM_CODEC)
/* 209 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_INFO_UPDATE, ClientboundPlayerInfoUpdatePacket.STREAM_CODEC)
/* 210 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_LOOK_AT, ClientboundPlayerLookAtPacket.STREAM_CODEC)
/* 211 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_POSITION, ClientboundPlayerPositionPacket.STREAM_CODEC)
/* 212 */       .addPacket(GamePacketTypes.CLIENTBOUND_PLAYER_ROTATION, ClientboundPlayerRotationPacket.STREAM_CODEC)
/* 213 */       .addPacket(GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_ADD, ClientboundRecipeBookAddPacket.STREAM_CODEC)
/* 214 */       .addPacket(GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_REMOVE, ClientboundRecipeBookRemovePacket.STREAM_CODEC)
/* 215 */       .addPacket(GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_SETTINGS, ClientboundRecipeBookSettingsPacket.STREAM_CODEC)
/* 216 */       .addPacket(GamePacketTypes.CLIENTBOUND_REMOVE_ENTITIES, ClientboundRemoveEntitiesPacket.STREAM_CODEC)
/* 217 */       .addPacket(GamePacketTypes.CLIENTBOUND_REMOVE_MOB_EFFECT, ClientboundRemoveMobEffectPacket.STREAM_CODEC)
/* 218 */       .addPacket(GamePacketTypes.CLIENTBOUND_RESET_SCORE, ClientboundResetScorePacket.STREAM_CODEC)
/* 219 */       .addPacket(CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP, ClientboundResourcePackPopPacket.STREAM_CODEC)
/* 220 */       .addPacket(CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_PUSH, ClientboundResourcePackPushPacket.STREAM_CODEC)
/* 221 */       .addPacket(GamePacketTypes.CLIENTBOUND_RESPAWN, ClientboundRespawnPacket.STREAM_CODEC)
/* 222 */       .addPacket(GamePacketTypes.CLIENTBOUND_ROTATE_HEAD, ClientboundRotateHeadPacket.STREAM_CODEC)
/* 223 */       .addPacket(GamePacketTypes.CLIENTBOUND_SECTION_BLOCKS_UPDATE, ClientboundSectionBlocksUpdatePacket.STREAM_CODEC)
/* 224 */       .addPacket(GamePacketTypes.CLIENTBOUND_SELECT_ADVANCEMENTS_TAB, ClientboundSelectAdvancementsTabPacket.STREAM_CODEC)
/* 225 */       .addPacket(GamePacketTypes.CLIENTBOUND_SERVER_DATA, ClientboundServerDataPacket.STREAM_CODEC)
/* 226 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_ACTION_BAR_TEXT, ClientboundSetActionBarTextPacket.STREAM_CODEC)
/* 227 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER, ClientboundSetBorderCenterPacket.STREAM_CODEC)
/* 228 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_BORDER_LERP_SIZE, ClientboundSetBorderLerpSizePacket.STREAM_CODEC)
/* 229 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE, ClientboundSetBorderSizePacket.STREAM_CODEC)
/* 230 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DELAY, ClientboundSetBorderWarningDelayPacket.STREAM_CODEC)
/* 231 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DISTANCE, ClientboundSetBorderWarningDistancePacket.STREAM_CODEC)
/* 232 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_CAMERA, ClientboundSetCameraPacket.STREAM_CODEC)
/* 233 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_CENTER, ClientboundSetChunkCacheCenterPacket.STREAM_CODEC)
/* 234 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_RADIUS, ClientboundSetChunkCacheRadiusPacket.STREAM_CODEC)
/* 235 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_CURSOR_ITEM, ClientboundSetCursorItemPacket.STREAM_CODEC)
/* 236 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_DEFAULT_SPAWN_POSITION, ClientboundSetDefaultSpawnPositionPacket.STREAM_CODEC)
/* 237 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_DISPLAY_OBJECTIVE, ClientboundSetDisplayObjectivePacket.STREAM_CODEC)
/* 238 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_ENTITY_DATA, ClientboundSetEntityDataPacket.STREAM_CODEC)
/* 239 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_ENTITY_LINK, ClientboundSetEntityLinkPacket.STREAM_CODEC)
/* 240 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_ENTITY_MOTION, ClientboundSetEntityMotionPacket.STREAM_CODEC)
/* 241 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_EQUIPMENT, ClientboundSetEquipmentPacket.STREAM_CODEC)
/* 242 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_EXPERIENCE, ClientboundSetExperiencePacket.STREAM_CODEC)
/* 243 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_HEALTH, ClientboundSetHealthPacket.STREAM_CODEC)
/* 244 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_HELD_SLOT, ClientboundSetHeldSlotPacket.STREAM_CODEC)
/* 245 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_OBJECTIVE, ClientboundSetObjectivePacket.STREAM_CODEC)
/* 246 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_PASSENGERS, ClientboundSetPassengersPacket.STREAM_CODEC)
/* 247 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_PLAYER_INVENTORY, ClientboundSetPlayerInventoryPacket.STREAM_CODEC)
/* 248 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_PLAYER_TEAM, ClientboundSetPlayerTeamPacket.STREAM_CODEC)
/* 249 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_SCORE, ClientboundSetScorePacket.STREAM_CODEC)
/* 250 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_SIMULATION_DISTANCE, ClientboundSetSimulationDistancePacket.STREAM_CODEC)
/* 251 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_SUBTITLE_TEXT, ClientboundSetSubtitleTextPacket.STREAM_CODEC)
/* 252 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_TIME, ClientboundSetTimePacket.STREAM_CODEC)
/* 253 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_TITLE_TEXT, ClientboundSetTitleTextPacket.STREAM_CODEC)
/* 254 */       .addPacket(GamePacketTypes.CLIENTBOUND_SET_TITLES_ANIMATION, ClientboundSetTitlesAnimationPacket.STREAM_CODEC)
/* 255 */       .addPacket(GamePacketTypes.CLIENTBOUND_SOUND_ENTITY, ClientboundSoundEntityPacket.STREAM_CODEC)
/* 256 */       .addPacket(GamePacketTypes.CLIENTBOUND_SOUND, ClientboundSoundPacket.STREAM_CODEC)
/* 257 */       .addPacket(GamePacketTypes.CLIENTBOUND_START_CONFIGURATION, ClientboundStartConfigurationPacket.STREAM_CODEC)
/* 258 */       .addPacket(GamePacketTypes.CLIENTBOUND_STOP_SOUND, ClientboundStopSoundPacket.STREAM_CODEC)
/* 259 */       .addPacket(CommonPacketTypes.CLIENTBOUND_STORE_COOKIE, ClientboundStoreCookiePacket.STREAM_CODEC)
/* 260 */       .addPacket(GamePacketTypes.CLIENTBOUND_SYSTEM_CHAT, ClientboundSystemChatPacket.STREAM_CODEC)
/* 261 */       .addPacket(GamePacketTypes.CLIENTBOUND_TAB_LIST, ClientboundTabListPacket.STREAM_CODEC)
/* 262 */       .addPacket(GamePacketTypes.CLIENTBOUND_TAG_QUERY, ClientboundTagQueryPacket.STREAM_CODEC)
/* 263 */       .addPacket(GamePacketTypes.CLIENTBOUND_TAKE_ITEM_ENTITY, ClientboundTakeItemEntityPacket.STREAM_CODEC)
/* 264 */       .addPacket(GamePacketTypes.CLIENTBOUND_TELEPORT_ENTITY, ClientboundTeleportEntityPacket.STREAM_CODEC)
/* 265 */       .addPacket(GamePacketTypes.CLIENTBOUND_TEST_INSTANCE_BLOCK_STATUS, ClientboundTestInstanceBlockStatus.STREAM_CODEC)
/* 266 */       .addPacket(GamePacketTypes.CLIENTBOUND_TICKING_STATE, ClientboundTickingStatePacket.STREAM_CODEC)
/* 267 */       .addPacket(GamePacketTypes.CLIENTBOUND_TICKING_STEP, ClientboundTickingStepPacket.STREAM_CODEC)
/* 268 */       .addPacket(CommonPacketTypes.CLIENTBOUND_TRANSFER, ClientboundTransferPacket.STREAM_CODEC)
/* 269 */       .addPacket(GamePacketTypes.CLIENTBOUND_UPDATE_ADVANCEMENTS, ClientboundUpdateAdvancementsPacket.STREAM_CODEC)
/* 270 */       .addPacket(GamePacketTypes.CLIENTBOUND_UPDATE_ATTRIBUTES, ClientboundUpdateAttributesPacket.STREAM_CODEC)
/* 271 */       .addPacket(GamePacketTypes.CLIENTBOUND_UPDATE_MOB_EFFECT, ClientboundUpdateMobEffectPacket.STREAM_CODEC)
/* 272 */       .addPacket(GamePacketTypes.CLIENTBOUND_UPDATE_RECIPES, ClientboundUpdateRecipesPacket.STREAM_CODEC)
/* 273 */       .addPacket(CommonPacketTypes.CLIENTBOUND_UPDATE_TAGS, ClientboundUpdateTagsPacket.STREAM_CODEC)
/* 274 */       .addPacket(GamePacketTypes.CLIENTBOUND_PROJECTILE_POWER, ClientboundProjectilePowerPacket.STREAM_CODEC)
/* 275 */       .addPacket(CommonPacketTypes.CLIENTBOUND_CUSTOM_REPORT_DETAILS, ClientboundCustomReportDetailsPacket.STREAM_CODEC)
/* 276 */       .addPacket(CommonPacketTypes.CLIENTBOUND_SERVER_LINKS, ClientboundServerLinksPacket.STREAM_CODEC)
/* 277 */       .addPacket(GamePacketTypes.CLIENTBOUND_WAYPOINT, ClientboundTrackedWaypointPacket.STREAM_CODEC)
/* 278 */       .addPacket(CommonPacketTypes.CLIENTBOUND_CLEAR_DIALOG, ClientboundClearDialogPacket.STREAM_CODEC)
/* 279 */       .addPacket(CommonPacketTypes.CLIENTBOUND_SHOW_DIALOG, ClientboundShowDialogPacket.STREAM_CODEC));
/*     */   
/*     */   public static interface Context {
/*     */     boolean hasInfiniteMaterials();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\GameProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
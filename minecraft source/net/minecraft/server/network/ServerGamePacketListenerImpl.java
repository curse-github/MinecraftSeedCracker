/*      */ package net.minecraft.server.network;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.primitives.Floats;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.ParseResults;
/*      */ import com.mojang.brigadier.StringReader;
/*      */ import com.mojang.brigadier.suggestion.Suggestions;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.advancements.AdvancementHolder;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSigningContext;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.arguments.ArgumentSignatures;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.gametest.framework.GameTestInstance;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.DisconnectionDetails;
/*      */ import net.minecraft.network.HashedStack;
/*      */ import net.minecraft.network.TickablePacketListener;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.LastSeenMessages;
/*      */ import net.minecraft.network.chat.LastSeenMessagesValidator;
/*      */ import net.minecraft.network.chat.MessageSignature;
/*      */ import net.minecraft.network.chat.MessageSignatureCache;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.PlayerChatMessage;
/*      */ import net.minecraft.network.chat.RemoteChatSession;
/*      */ import net.minecraft.network.chat.SignableCommand;
/*      */ import net.minecraft.network.chat.SignedMessageBody;
/*      */ import net.minecraft.network.chat.SignedMessageChain;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketUtils;
/*      */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*      */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*      */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTestInstanceBlockStatus;
/*      */ import net.minecraft.network.protocol.game.GameProtocols;
/*      */ import net.minecraft.network.protocol.game.ServerGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatAckPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChunkBatchReceivedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerSlotStateChangedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundDebugSubscriptionRequestPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundEntityTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundInteractPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSelectBundleItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetTestBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSwingPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundTestInstanceBlockActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
/*      */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*      */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.commands.GameModeCommand;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.permissions.Permissions;
/*      */ import net.minecraft.util.FutureChain;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.SignatureValidator;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.util.TickThrottler;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.PositionMoveRotation;
/*      */ import net.minecraft.world.entity.Relative;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.player.PlayerModelPart;
/*      */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*      */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*      */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.AnvilMenu;
/*      */ import net.minecraft.world.inventory.BeaconMenu;
/*      */ import net.minecraft.world.inventory.CrafterMenu;
/*      */ import net.minecraft.world.inventory.MerchantMenu;
/*      */ import net.minecraft.world.inventory.RecipeBookMenu;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.component.PiercingWeapon;
/*      */ import net.minecraft.world.item.component.WritableBookContent;
/*      */ import net.minecraft.world.item.component.WrittenBookContent;
/*      */ import net.minecraft.world.item.crafting.RecipeHolder;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.BaseCommandBlock;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CommandBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CrafterBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ServerGamePacketListenerImpl
/*      */   extends ServerCommonPacketListenerImpl
/*      */   implements ServerGamePacketListener, ServerPlayerConnection, TickablePacketListener, GameProtocols.Context
/*      */ {
/*  220 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int NO_BLOCK_UPDATES_TO_ACK = -1;
/*      */   private static final int TRACKED_MESSAGE_DISCONNECT_THRESHOLD = 4096;
/*      */   private static final int MAXIMUM_FLYING_TICKS = 80;
/*      */   private static final int ATTACK_INDICATOR_TOLERANCE_TICKS = 5;
/*      */   public static final int CLIENT_LOADED_TIMEOUT_TIME = 60;
/*  227 */   private static final Component CHAT_VALIDATION_FAILED = Component.translatable("multiplayer.disconnect.chat_validation_failed");
/*  228 */   private static final Component INVALID_COMMAND_SIGNATURE = Component.translatable("chat.disabled.invalid_command_signature").withStyle(ChatFormatting.RED);
/*      */   private static final int MAX_COMMAND_SUGGESTIONS = 1000;
/*      */   public ServerPlayer player;
/*      */   public final PlayerChunkSender chunkSender;
/*      */   private int tickCount;
/*  233 */   private int ackBlockChangesUpTo = -1;
/*      */   
/*  235 */   private final TickThrottler chatSpamThrottler = new TickThrottler(20, 200);
/*  236 */   private final TickThrottler dropSpamThrottler = new TickThrottler(20, 1480);
/*      */   
/*      */   private double firstGoodX;
/*      */   
/*      */   private double firstGoodY;
/*      */   
/*      */   private double firstGoodZ;
/*      */   
/*      */   private double lastGoodX;
/*      */   private double lastGoodY;
/*      */   private double lastGoodZ;
/*      */   private Entity lastVehicle;
/*      */   private double vehicleFirstGoodX;
/*      */   private double vehicleFirstGoodY;
/*      */   private double vehicleFirstGoodZ;
/*      */   private double vehicleLastGoodX;
/*      */   private double vehicleLastGoodY;
/*      */   private double vehicleLastGoodZ;
/*      */   private Vec3 awaitingPositionFromClient;
/*      */   private int awaitingTeleport;
/*      */   private int awaitingTeleportTime;
/*      */   private boolean clientIsFloating;
/*      */   private int aboveGroundTickCount;
/*      */   private boolean clientVehicleIsFloating;
/*      */   private int aboveGroundVehicleTickCount;
/*      */   private int receivedMovePacketCount;
/*      */   private int knownMovePacketCount;
/*      */   private boolean receivedMovementThisTick;
/*      */   private RemoteChatSession chatSession;
/*      */   private SignedMessageChain.Decoder signedMessageDecoder;
/*  266 */   private final LastSeenMessagesValidator lastSeenMessages = new LastSeenMessagesValidator(20);
/*      */   
/*      */   private int nextChatIndex;
/*  269 */   private final MessageSignatureCache messageSignatureCache = MessageSignatureCache.createDefault();
/*      */   
/*      */   private final FutureChain chatMessageChain;
/*      */   
/*      */   private boolean waitingForSwitchToConfig;
/*      */   private boolean waitingForRespawn;
/*      */   private int clientLoadedTimeoutTimer;
/*      */   
/*      */   public ServerGamePacketListenerImpl(MinecraftServer server, Connection connection, ServerPlayer player, CommonListenerCookie cookie) {
/*  278 */     super(server, connection, cookie);
/*  279 */     restartClientLoadTimerAfterRespawn();
/*      */     
/*  281 */     this.chunkSender = new PlayerChunkSender(connection.isMemoryConnection());
/*  282 */     this.player = player;
/*  283 */     player.connection = this;
/*      */     
/*  285 */     player.getTextFilter().join();
/*      */     
/*  287 */     Objects.requireNonNull(server); this.signedMessageDecoder = SignedMessageChain.Decoder.unsigned(player.getUUID(), server::enforceSecureProfile);
/*      */     
/*  289 */     this.chatMessageChain = new FutureChain(server);
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  294 */     if (this.ackBlockChangesUpTo > -1) {
/*  295 */       send(new ClientboundBlockChangedAckPacket(this.ackBlockChangesUpTo));
/*  296 */       this.ackBlockChangesUpTo = -1;
/*      */     } 
/*      */     
/*  299 */     if (!this.server.isPaused() && tickPlayer()) {
/*      */       return;
/*      */     }
/*      */     
/*  303 */     keepConnectionAlive();
/*      */     
/*  305 */     this.chatSpamThrottler.tick();
/*  306 */     this.dropSpamThrottler.tick();
/*      */     
/*  308 */     if (this.player.getLastActionTime() > 0L && this.server
/*  309 */       .playerIdleTimeout() > 0 && 
/*  310 */       Util.getMillis() - this.player.getLastActionTime() > TimeUnit.MINUTES.toMillis(this.server.playerIdleTimeout()) && !this.player.wonGame)
/*      */     {
/*      */       
/*  313 */       disconnect(Component.translatable("multiplayer.disconnect.idling"));
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean tickPlayer() {
/*  318 */     resetPosition();
/*  319 */     this.player.xo = this.player.getX();
/*  320 */     this.player.yo = this.player.getY();
/*  321 */     this.player.zo = this.player.getZ();
/*  322 */     this.player.doTick();
/*  323 */     this.player.absSnapTo(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.getYRot(), this.player.getXRot());
/*  324 */     this.tickCount++;
/*  325 */     this.knownMovePacketCount = this.receivedMovePacketCount;
/*      */     
/*  327 */     if (this.clientIsFloating && !this.player.isSleeping() && !this.player.isPassenger() && !this.player.isDeadOrDying()) {
/*  328 */       if (++this.aboveGroundTickCount > getMaximumFlyingTicks(this.player)) {
/*  329 */         LOGGER.warn("{} was kicked for floating too long!", this.player.getPlainTextName());
/*  330 */         disconnect(Component.translatable("multiplayer.disconnect.flying"));
/*  331 */         return true;
/*      */       } 
/*      */     } else {
/*  334 */       this.clientIsFloating = false;
/*  335 */       this.aboveGroundTickCount = 0;
/*      */     } 
/*      */     
/*  338 */     this.lastVehicle = this.player.getRootVehicle();
/*  339 */     if (this.lastVehicle == this.player || this.lastVehicle.getControllingPassenger() != this.player) {
/*  340 */       this.lastVehicle = null;
/*  341 */       this.clientVehicleIsFloating = false;
/*  342 */       this.aboveGroundVehicleTickCount = 0;
/*      */     } else {
/*  344 */       this.vehicleFirstGoodX = this.lastVehicle.getX();
/*  345 */       this.vehicleFirstGoodY = this.lastVehicle.getY();
/*  346 */       this.vehicleFirstGoodZ = this.lastVehicle.getZ();
/*  347 */       this.vehicleLastGoodX = this.lastVehicle.getX();
/*  348 */       this.vehicleLastGoodY = this.lastVehicle.getY();
/*  349 */       this.vehicleLastGoodZ = this.lastVehicle.getZ();
/*  350 */       if (this.clientVehicleIsFloating && this.lastVehicle.getControllingPassenger() == this.player) {
/*  351 */         if (++this.aboveGroundVehicleTickCount > getMaximumFlyingTicks(this.lastVehicle)) {
/*  352 */           LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getPlainTextName());
/*  353 */           disconnect(Component.translatable("multiplayer.disconnect.flying"));
/*  354 */           return true;
/*      */         } 
/*      */       } else {
/*  357 */         this.clientVehicleIsFloating = false;
/*  358 */         this.aboveGroundVehicleTickCount = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  362 */     return false;
/*      */   }
/*      */   
/*      */   private int getMaximumFlyingTicks(Entity entity) {
/*  366 */     double gravity = entity.getGravity();
/*  367 */     if (gravity < 9.999999747378752E-6D)
/*      */     {
/*  369 */       return Integer.MAX_VALUE;
/*      */     }
/*  371 */     double gravityModifier = 0.08D / gravity;
/*  372 */     return Mth.ceil(80.0D * Math.max(gravityModifier, 1.0D));
/*      */   }
/*      */   
/*      */   public void resetFlyingTicks() {
/*  376 */     this.aboveGroundTickCount = 0;
/*  377 */     this.aboveGroundVehicleTickCount = 0;
/*      */   }
/*      */   
/*      */   public void resetPosition() {
/*  381 */     this.firstGoodX = this.player.getX();
/*  382 */     this.firstGoodY = this.player.getY();
/*  383 */     this.firstGoodZ = this.player.getZ();
/*  384 */     this.lastGoodX = this.player.getX();
/*  385 */     this.lastGoodY = this.player.getY();
/*  386 */     this.lastGoodZ = this.player.getZ();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  391 */   public boolean isAcceptingMessages() { return (this.connection.isConnected() && !this.waitingForSwitchToConfig); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean shouldHandleMessage(Packet<?> packet) {
/*  396 */     if (super.shouldHandleMessage(packet)) {
/*  397 */       return true;
/*      */     }
/*      */     
/*  400 */     return (this.waitingForSwitchToConfig && this.connection.isConnected() && packet instanceof ServerboundConfigurationAcknowledgedPacket);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  405 */   protected GameProfile playerProfile() { return this.player.getGameProfile(); }
/*      */ 
/*      */   
/*      */   private <T, R> CompletableFuture<R> filterTextPacket(T message, BiFunction<TextFilter, T, CompletableFuture<R>> action) {
/*  409 */     return ((CompletableFuture)action.apply(this.player.getTextFilter(), message))
/*  410 */       .thenApply(result -> {
/*  411 */           if (!isAcceptingMessages()) {
/*  412 */             LOGGER.debug("Ignoring packet due to disconnection");
/*      */             
/*  414 */             throw new CancellationException("disconnected");
/*      */           } 
/*  416 */           return result;
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*  421 */   private CompletableFuture<FilteredText> filterTextPacket(String message) { return filterTextPacket(message, TextFilter::processStreamMessage); }
/*      */ 
/*      */ 
/*      */   
/*  425 */   private CompletableFuture<List<FilteredText>> filterTextPacket(List<String> message) { return filterTextPacket(message, TextFilter::processMessageBundle); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handlePlayerInput(ServerboundPlayerInputPacket packet) {
/*  430 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  431 */     this.player.setLastClientInput(packet.input());
/*  432 */     if (hasClientLoaded()) {
/*  433 */       this.player.resetLastActionTime();
/*  434 */       this.player.setShiftKeyDown(packet.input().shift());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  439 */   private static boolean containsInvalidValues(double x, double y, double z, float yRot, float xRot) { return (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) || !Floats.isFinite(xRot) || !Floats.isFinite(yRot)); }
/*      */ 
/*      */ 
/*      */   
/*  443 */   private static double clampHorizontal(double value) { return Mth.clamp(value, -3.0E7D, 3.0E7D); }
/*      */ 
/*      */ 
/*      */   
/*  447 */   private static double clampVertical(double value) { return Mth.clamp(value, -2.0E7D, 2.0E7D); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) {
/*  452 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  453 */     if (containsInvalidValues(packet.position().x(), packet.position().y(), packet.position().z(), packet.yRot(), packet.xRot())) {
/*  454 */       disconnect(Component.translatable("multiplayer.disconnect.invalid_vehicle_movement"));
/*      */       
/*      */       return;
/*      */     } 
/*  458 */     if (updateAwaitingTeleport() || !hasClientLoaded()) {
/*      */       return;
/*      */     }
/*      */     
/*  462 */     Entity vehicle = this.player.getRootVehicle();
/*  463 */     if (vehicle != this.player && vehicle.getControllingPassenger() == this.player && vehicle == this.lastVehicle) {
/*  464 */       ServerLevel level = this.player.level();
/*  465 */       double oldX = vehicle.getX();
/*  466 */       double oldY = vehicle.getY();
/*  467 */       double oldZ = vehicle.getZ();
/*      */       
/*  469 */       double targetX = clampHorizontal(packet.position().x());
/*  470 */       double targetY = clampVertical(packet.position().y());
/*  471 */       double targetZ = clampHorizontal(packet.position().z());
/*  472 */       float targetYRot = Mth.wrapDegrees(packet.yRot());
/*  473 */       float targetXRot = Mth.wrapDegrees(packet.xRot());
/*      */       
/*  475 */       double xDist = targetX - this.vehicleFirstGoodX;
/*  476 */       double yDist = targetY - this.vehicleFirstGoodY;
/*  477 */       double zDist = targetZ - this.vehicleFirstGoodZ;
/*      */       
/*  479 */       double expectedDist = vehicle.getDeltaMovement().lengthSqr();
/*  480 */       double movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/*      */       
/*  482 */       if (movedDist - expectedDist > 100.0D && !isSingleplayerOwner()) {
/*  483 */         LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", new Object[] { vehicle.getPlainTextName(), this.player.getPlainTextName(), Double.valueOf(xDist), Double.valueOf(yDist), Double.valueOf(zDist) });
/*  484 */         send(ClientboundMoveVehiclePacket.fromEntity(vehicle));
/*      */         
/*      */         return;
/*      */       } 
/*  488 */       AABB oldAABB = vehicle.getBoundingBox();
/*      */       
/*  490 */       xDist = targetX - this.vehicleLastGoodX;
/*  491 */       yDist = targetY - this.vehicleLastGoodY;
/*  492 */       zDist = targetZ - this.vehicleLastGoodZ;
/*  493 */       boolean vehicleRestsOnSomething = vehicle.verticalCollisionBelow;
/*  494 */       if (vehicle instanceof LivingEntity) { LivingEntity livingVehicle = (LivingEntity)vehicle; if (livingVehicle.onClimbable()) {
/*  495 */           livingVehicle.resetFallDistance();
/*      */         } }
/*      */       
/*  498 */       vehicle.move(MoverType.PLAYER, new Vec3(xDist, yDist, zDist));
/*      */       
/*  500 */       double oyDist = yDist;
/*      */       
/*  502 */       xDist = targetX - vehicle.getX();
/*  503 */       yDist = targetY - vehicle.getY();
/*  504 */       if (yDist > -0.5D || yDist < 0.5D) {
/*  505 */         yDist = 0.0D;
/*      */       }
/*  507 */       zDist = targetZ - vehicle.getZ();
/*  508 */       movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/*  509 */       boolean fail = false;
/*  510 */       if (movedDist > 0.0625D) {
/*  511 */         fail = true;
/*  512 */         LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", new Object[] { vehicle.getPlainTextName(), this.player.getPlainTextName(), Double.valueOf(Math.sqrt(movedDist)) });
/*      */       } 
/*      */       
/*  515 */       if ((fail && level.noCollision(vehicle, oldAABB)) || isEntityCollidingWithAnythingNew(level, vehicle, oldAABB, targetX, targetY, targetZ)) {
/*  516 */         vehicle.absSnapTo(oldX, oldY, oldZ, targetYRot, targetXRot);
/*  517 */         send(ClientboundMoveVehiclePacket.fromEntity(vehicle));
/*  518 */         vehicle.removeLatestMovementRecording();
/*      */         
/*      */         return;
/*      */       } 
/*  522 */       vehicle.absSnapTo(targetX, targetY, targetZ, targetYRot, targetXRot);
/*      */       
/*  524 */       this.player.level().getChunkSource().move(this.player);
/*      */       
/*  526 */       Vec3 clientDeltaMovement = new Vec3(vehicle.getX() - oldX, vehicle.getY() - oldY, vehicle.getZ() - oldZ);
/*  527 */       handlePlayerKnownMovement(clientDeltaMovement);
/*  528 */       vehicle.setOnGroundWithMovement(packet.onGround(), clientDeltaMovement);
/*  529 */       vehicle.doCheckFallDamage(clientDeltaMovement.x, clientDeltaMovement.y, clientDeltaMovement.z, packet.onGround());
/*      */       
/*  531 */       this.player.checkMovementStatistics(clientDeltaMovement.x, clientDeltaMovement.y, clientDeltaMovement.z);
/*  532 */       this
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  537 */         .clientVehicleIsFloating = (oyDist >= -0.03125D && !vehicleRestsOnSomething && !this.server.allowFlight() && !vehicle.isFlyingVehicle() && !vehicle.isNoGravity() && noBlocksAround(vehicle));
/*      */       
/*  539 */       this.vehicleLastGoodX = vehicle.getX();
/*  540 */       this.vehicleLastGoodY = vehicle.getY();
/*  541 */       this.vehicleLastGoodZ = vehicle.getZ();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  546 */   private boolean noBlocksAround(Entity entity) { return entity.level().getBlockStates(entity.getBoundingBox().inflate(0.0625D).expandTowards(0.0D, -0.55D, 0.0D)).allMatch(BlockBehaviour.BlockStateBase::isAir); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) {
/*  551 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  552 */     if (packet.getId() == this.awaitingTeleport) {
/*  553 */       if (this.awaitingPositionFromClient == null) {
/*  554 */         disconnect(Component.translatable("multiplayer.disconnect.invalid_player_movement"));
/*      */         return;
/*      */       } 
/*  557 */       this.player.absSnapTo(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.getYRot(), this.player.getXRot());
/*      */       
/*  559 */       this.lastGoodX = this.awaitingPositionFromClient.x;
/*  560 */       this.lastGoodY = this.awaitingPositionFromClient.y;
/*  561 */       this.lastGoodZ = this.awaitingPositionFromClient.z;
/*  562 */       this.player.hasChangedDimension();
/*      */       
/*  564 */       this.awaitingPositionFromClient = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAcceptPlayerLoad(ServerboundPlayerLoadedPacket packet) {
/*  570 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  571 */     markClientLoaded();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) {
/*  576 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/*  578 */     RecipeManager.ServerDisplayInfo entry = this.server.getRecipeManager().getRecipeFromDisplay(packet.recipe());
/*  579 */     if (entry != null) {
/*  580 */       this.player.getRecipeBook().removeHighlight(entry.parent().id());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBundleItemSelectedPacket(ServerboundSelectBundleItemPacket packet) {
/*  586 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  587 */     this.player.containerMenu.setSelectedBundleItemIndex(packet.slotId(), packet.selectedItemIndex());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) {
/*  592 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  593 */     this.player.getRecipeBook().setBookSetting(packet.getBookType(), packet.isOpen(), packet.isFiltering());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packet) {
/*  598 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  599 */     if (packet.getAction() == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
/*  600 */       Identifier id = (Identifier)Objects.requireNonNull(packet.getTab());
/*  601 */       AdvancementHolder advancement = this.server.getAdvancements().get(id);
/*  602 */       if (advancement != null) {
/*  603 */         this.player.getAdvancements().setSelectedTab(advancement);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) {
/*  610 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  611 */     StringReader command = new StringReader(packet.getCommand());
/*  612 */     if (command.canRead() && command.peek() == '/') {
/*  613 */       command.skip();
/*      */     }
/*  615 */     ParseResults<CommandSourceStack> parse = this.server.getCommands().getDispatcher().parse(command, this.player.createCommandSourceStack());
/*  616 */     this.server.getCommands().getDispatcher().getCompletionSuggestions(parse).thenAccept(results -> {
/*  617 */           Suggestions suggestions = (results.getList().size() <= 1000) ? results : new Suggestions(results.getRange(), results.getList().subList(0, 1000));
/*  618 */           send(new ClientboundCommandSuggestionsPacket(packet.getId(), suggestions));
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packet) {
/*  624 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  625 */     if (!this.player.canUseGameMasterBlocks()) {
/*  626 */       this.player.sendSystemMessage(Component.translatable("advMode.notAllowed"));
/*      */       return;
/*      */     } 
/*  629 */     BaseCommandBlock commandBlock = null;
/*  630 */     CommandBlockEntity autoCommandBlock = null;
/*  631 */     BlockPos blockPos = packet.getPos();
/*  632 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  633 */     if (blockEntity instanceof CommandBlockEntity) { CommandBlockEntity commandBlockEntity = (CommandBlockEntity)blockEntity;
/*  634 */       autoCommandBlock = commandBlockEntity;
/*  635 */       commandBlock = autoCommandBlock.getCommandBlock(); }
/*      */ 
/*      */     
/*  638 */     String command = packet.getCommand();
/*  639 */     boolean trackOutput = packet.isTrackOutput();
/*      */     
/*  641 */     if (commandBlock != null) {
/*  642 */       CommandBlockEntity.Mode oldMode = autoCommandBlock.getMode();
/*      */       
/*  644 */       BlockState currentBlockState = this.player.level().getBlockState(blockPos);
/*  645 */       Direction direction = (Direction)currentBlockState.getValue(CommandBlock.FACING);
/*  646 */       switch (packet.getMode()) { case PERFORM_RESPAWN: 
/*      */         case REQUEST_STATS: 
/*      */         default:
/*  649 */           break; }  BlockState baseBlockState = Blocks.COMMAND_BLOCK.defaultBlockState();
/*      */       
/*  651 */       BlockState blockState = (BlockState)((BlockState)baseBlockState.setValue(CommandBlock.FACING, direction)).setValue(CommandBlock.CONDITIONAL, Boolean.valueOf(packet.isConditional()));
/*  652 */       if (blockState != currentBlockState) {
/*  653 */         this.player.level().setBlock(blockPos, blockState, 2);
/*      */         
/*  655 */         blockEntity.setBlockState(blockState);
/*  656 */         this.player.level().getChunkAt(blockPos).setBlockEntity(blockEntity);
/*      */       } 
/*      */       
/*  659 */       commandBlock.setCommand(command);
/*  660 */       commandBlock.setTrackOutput(trackOutput);
/*  661 */       if (!trackOutput) {
/*  662 */         commandBlock.setLastOutput(null);
/*      */       }
/*  664 */       autoCommandBlock.setAutomatic(packet.isAutomatic());
/*  665 */       if (oldMode != packet.getMode()) {
/*  666 */         autoCommandBlock.onModeSwitch();
/*      */       }
/*  668 */       if (this.player.level().isCommandBlockEnabled()) {
/*  669 */         commandBlock.onUpdated(this.player.level());
/*      */       }
/*  671 */       if (!StringUtil.isNullOrEmpty(command)) {
/*  672 */         this.player.sendSystemMessage(Component.translatable(this.player.level().isCommandBlockEnabled() ? "advMode.setCommand.success" : "advMode.setCommand.disabled", new Object[] { command }));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packet) {
/*  679 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  680 */     if (!this.player.canUseGameMasterBlocks()) {
/*  681 */       this.player.sendSystemMessage(Component.translatable("advMode.notAllowed"));
/*      */       return;
/*      */     } 
/*  684 */     BaseCommandBlock commandBlock = packet.getCommandBlock(this.player.level());
/*      */     
/*  686 */     if (commandBlock != null) {
/*  687 */       String command = packet.getCommand();
/*  688 */       commandBlock.setCommand(command);
/*  689 */       commandBlock.setTrackOutput(packet.isTrackOutput());
/*  690 */       if (!packet.isTrackOutput()) {
/*  691 */         commandBlock.setLastOutput(null);
/*      */       }
/*  693 */       boolean commandBlockEnabled = this.player.level().isCommandBlockEnabled();
/*  694 */       if (commandBlockEnabled) {
/*  695 */         commandBlock.onUpdated(this.player.level());
/*      */       }
/*  697 */       if (!StringUtil.isNullOrEmpty(command)) {
/*  698 */         this.player.sendSystemMessage(Component.translatable(commandBlockEnabled ? "advMode.setCommand.success" : "advMode.setCommand.disabled", new Object[] { command }));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePickItemFromBlock(ServerboundPickItemFromBlockPacket packet) {
/*  705 */     ServerLevel level = this.player.level();
/*  706 */     PacketUtils.ensureRunningOnSameThread(packet, this, level);
/*      */     
/*  708 */     BlockPos pos = packet.pos();
/*  709 */     if (!this.player.isWithinBlockInteractionRange(pos, 1.0D))
/*      */       return; 
/*  711 */     if (!level.isLoaded(pos)) {
/*      */       return;
/*      */     }
/*      */     
/*  715 */     BlockState blockState = level.getBlockState(pos);
/*      */     
/*  717 */     boolean includeData = (this.player.hasInfiniteMaterials() && packet.includeData());
/*  718 */     ItemStack itemStack = blockState.getCloneItemStack(level, pos, includeData);
/*  719 */     if (itemStack.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  723 */     if (includeData) {
/*  724 */       addBlockDataToItem(blockState, level, pos, itemStack);
/*      */     }
/*      */     
/*  727 */     tryPickItem(itemStack);
/*      */   }
/*      */   
/*      */   private static void addBlockDataToItem(BlockState blockState, ServerLevel level, BlockPos pos, ItemStack itemStack) {
/*  731 */     BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  732 */     if (blockEntity != null) {
/*  733 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER); 
/*  734 */       try { TagValueOutput output = TagValueOutput.createWithContext(reporter, level.registryAccess());
/*  735 */         blockEntity.saveCustomOnly(output);
/*  736 */         blockEntity.removeComponentsFromTag(output);
/*  737 */         BlockItem.setBlockEntityData(itemStack, blockEntity.getType(), output);
/*  738 */         itemStack.applyComponents(blockEntity.collectComponents());
/*  739 */         reporter.close(); }
/*      */       catch (Throwable throwable) { try { reporter.close(); }
/*      */         catch (Throwable throwable1)
/*      */         { throwable.addSuppressed(throwable1); }
/*      */          throw throwable; }
/*      */     
/*  745 */     }  } public void handlePickItemFromEntity(ServerboundPickItemFromEntityPacket packet) { ServerLevel level = this.player.level();
/*  746 */     PacketUtils.ensureRunningOnSameThread(packet, this, level);
/*      */     
/*  748 */     Entity entity = level.getEntityOrPart(packet.id());
/*  749 */     if (entity == null || !this.player.isWithinEntityInteractionRange(entity, 3.0D)) {
/*      */       return;
/*      */     }
/*      */     
/*  753 */     ItemStack itemStack = entity.getPickResult();
/*  754 */     if (itemStack != null && !itemStack.isEmpty()) {
/*  755 */       tryPickItem(itemStack);
/*      */     } }
/*      */ 
/*      */   
/*      */   private void tryPickItem(ItemStack itemStack) {
/*  760 */     if (!itemStack.isItemEnabled(this.player.level().enabledFeatures())) {
/*      */       return;
/*      */     }
/*      */     
/*  764 */     Inventory inventory = this.player.getInventory();
/*  765 */     int slotWithExistingItem = inventory.findSlotMatchingItem(itemStack);
/*  766 */     if (slotWithExistingItem != -1) {
/*  767 */       if (Inventory.isHotbarSlot(slotWithExistingItem)) {
/*  768 */         inventory.setSelectedSlot(slotWithExistingItem);
/*      */       } else {
/*  770 */         inventory.pickSlot(slotWithExistingItem);
/*      */       } 
/*  772 */     } else if (this.player.hasInfiniteMaterials()) {
/*  773 */       inventory.addAndPickItem(itemStack);
/*      */     } 
/*      */     
/*  776 */     send(new ClientboundSetHeldSlotPacket(inventory.getSelectedSlot()));
/*  777 */     this.player.inventoryMenu.broadcastChanges();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRenameItem(ServerboundRenameItemPacket packet) {
/*  782 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  783 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu; if (abstractContainerMenu instanceof AnvilMenu) { AnvilMenu menu = (AnvilMenu)abstractContainerMenu;
/*  784 */       if (!menu.stillValid(this.player)) {
/*  785 */         LOGGER.debug("Player {} interacted with invalid menu {}", this.player, menu);
/*      */         return;
/*      */       } 
/*  788 */       menu.setItemName(packet.getName()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBeaconPacket(ServerboundSetBeaconPacket packet) {
/*  794 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  795 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu; if (abstractContainerMenu instanceof BeaconMenu) { BeaconMenu menu = (BeaconMenu)abstractContainerMenu;
/*  796 */       if (!this.player.containerMenu.stillValid(this.player)) {
/*  797 */         LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */         return;
/*      */       } 
/*  800 */       menu.updateEffects(packet.primary(), packet.secondary()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packet) {
/*  806 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  807 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  810 */     BlockPos blockPos = packet.getPos();
/*  811 */     BlockState state = this.player.level().getBlockState(blockPos);
/*  812 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  813 */     if (blockEntity instanceof StructureBlockEntity) { StructureBlockEntity structure = (StructureBlockEntity)blockEntity;
/*  814 */       structure.setMode(packet.getMode());
/*  815 */       structure.setStructureName(packet.getName());
/*  816 */       structure.setStructurePos(packet.getOffset());
/*  817 */       structure.setStructureSize(packet.getSize());
/*  818 */       structure.setMirror(packet.getMirror());
/*  819 */       structure.setRotation(packet.getRotation());
/*  820 */       structure.setMetaData(packet.getData());
/*  821 */       structure.setIgnoreEntities(packet.isIgnoreEntities());
/*  822 */       structure.setStrict(packet.isStrict());
/*  823 */       structure.setShowAir(packet.isShowAir());
/*  824 */       structure.setShowBoundingBox(packet.isShowBoundingBox());
/*  825 */       structure.setIntegrity(packet.getIntegrity());
/*  826 */       structure.setSeed(packet.getSeed());
/*      */       
/*  828 */       if (structure.hasStructureName()) {
/*  829 */         String actualStructureName = structure.getStructureName();
/*  830 */         if (packet.getUpdateType() == StructureBlockEntity.UpdateType.SAVE_AREA) {
/*  831 */           if (structure.saveStructure()) {
/*  832 */             this.player.displayClientMessage(Component.translatable("structure_block.save_success", new Object[] { actualStructureName }), false);
/*      */           } else {
/*  834 */             this.player.displayClientMessage(Component.translatable("structure_block.save_failure", new Object[] { actualStructureName }), false);
/*      */           } 
/*  836 */         } else if (packet.getUpdateType() == StructureBlockEntity.UpdateType.LOAD_AREA) {
/*  837 */           if (!structure.isStructureLoadable()) {
/*  838 */             this.player.displayClientMessage(Component.translatable("structure_block.load_not_found", new Object[] { actualStructureName }), false);
/*  839 */           } else if (structure.placeStructureIfSameSize(this.player.level())) {
/*  840 */             this.player.displayClientMessage(Component.translatable("structure_block.load_success", new Object[] { actualStructureName }), false);
/*      */           } else {
/*  842 */             this.player.displayClientMessage(Component.translatable("structure_block.load_prepare", new Object[] { actualStructureName }), false);
/*      */           } 
/*  844 */         } else if (packet.getUpdateType() == StructureBlockEntity.UpdateType.SCAN_AREA) {
/*  845 */           if (structure.detectSize()) {
/*  846 */             this.player.displayClientMessage(Component.translatable("structure_block.size_success", new Object[] { actualStructureName }), false);
/*      */           } else {
/*  848 */             this.player.displayClientMessage(Component.translatable("structure_block.size_failure"), false);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  852 */         this.player.displayClientMessage(Component.translatable("structure_block.invalid_structure_name", new Object[] { packet.getName() }), false);
/*      */       } 
/*      */       
/*  855 */       structure.setChanged();
/*  856 */       this.player.level().sendBlockUpdated(blockPos, state, state, 3); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetTestBlock(ServerboundSetTestBlockPacket packet) {
/*  862 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  863 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  866 */     BlockPos blockPos = packet.position();
/*  867 */     BlockState initialState = this.player.level().getBlockState(blockPos);
/*  868 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  869 */     if (blockEntity instanceof TestBlockEntity) { TestBlockEntity testBlock = (TestBlockEntity)blockEntity;
/*  870 */       testBlock.setMode(packet.mode());
/*  871 */       testBlock.setMessage(packet.message());
/*  872 */       testBlock.setChanged();
/*  873 */       this.player.level().sendBlockUpdated(blockPos, initialState, testBlock.getBlockState(), 3); }
/*      */   
/*      */   }
/*      */   
/*      */   public void handleTestInstanceBlockAction(ServerboundTestInstanceBlockActionPacket packet) {
/*      */     TestInstanceBlockEntity blockEntity;
/*  879 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  880 */     BlockPos pos = packet.pos();
/*  881 */     if (this.player.canUseGameMasterBlocks()) { BlockEntity blockEntity1 = this.player.level().getBlockEntity(pos); if (blockEntity1 instanceof TestInstanceBlockEntity) { blockEntity = (TestInstanceBlockEntity)blockEntity1; } else { return; }
/*      */        }
/*      */     else { return; }
/*  884 */      if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.QUERY || packet
/*  885 */       .action() == ServerboundTestInstanceBlockActionPacket.Action.INIT) {
/*  886 */       Optional<Vec3i> size; MutableComponent mutableComponent; Registry<GameTestInstance> registry = this.player.registryAccess().lookupOrThrow(Registries.TEST_INSTANCE);
/*  887 */       Objects.requireNonNull(registry); Optional<Holder.Reference<GameTestInstance>> test = packet.data().test().flatMap(registry::get);
/*      */       
/*  889 */       if (test.isPresent()) {
/*  890 */         mutableComponent = ((GameTestInstance)((Holder.Reference)test.get()).value()).describe();
/*      */       } else {
/*  892 */         mutableComponent = Component.translatable("test_instance.description.no_test").withStyle(ChatFormatting.RED);
/*      */       } 
/*      */       
/*  895 */       if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.QUERY) {
/*  896 */         size = packet.data().test().flatMap(testKey -> TestInstanceBlockEntity.getStructureSize(this.player.level(), testKey));
/*      */       } else {
/*  898 */         size = Optional.empty();
/*      */       } 
/*  900 */       this.connection.send(new ClientboundTestInstanceBlockStatus(mutableComponent, size));
/*      */     } else {
/*  902 */       blockEntity.set(packet.data());
/*  903 */       if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.RESET) {
/*  904 */         Objects.requireNonNull(this.player); blockEntity.resetTest(this.player::sendSystemMessage);
/*  905 */       } else if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.SAVE) {
/*  906 */         Objects.requireNonNull(this.player); blockEntity.saveTest(this.player::sendSystemMessage);
/*  907 */       } else if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.EXPORT) {
/*  908 */         Objects.requireNonNull(this.player); blockEntity.exportTest(this.player::sendSystemMessage);
/*  909 */       } else if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.RUN) {
/*  910 */         Objects.requireNonNull(this.player); blockEntity.runTest(this.player::sendSystemMessage);
/*      */       } 
/*  912 */       BlockState state = this.player.level().getBlockState(pos);
/*  913 */       this.player.level().sendBlockUpdated(pos, Blocks.AIR.defaultBlockState(), state, 3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet) {
/*  919 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  920 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  923 */     BlockPos blockPos = packet.getPos();
/*  924 */     BlockState state = this.player.level().getBlockState(blockPos);
/*  925 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  926 */     if (blockEntity instanceof JigsawBlockEntity) { JigsawBlockEntity jigsaw = (JigsawBlockEntity)blockEntity;
/*  927 */       jigsaw.setName(packet.getName());
/*  928 */       jigsaw.setTarget(packet.getTarget());
/*  929 */       jigsaw.setPool(ResourceKey.create(Registries.TEMPLATE_POOL, packet.getPool()));
/*  930 */       jigsaw.setFinalState(packet.getFinalState());
/*  931 */       jigsaw.setJoint(packet.getJoint());
/*  932 */       jigsaw.setPlacementPriority(packet.getPlacementPriority());
/*  933 */       jigsaw.setSelectionPriority(packet.getSelectionPriority());
/*  934 */       jigsaw.setChanged();
/*  935 */       this.player.level().sendBlockUpdated(blockPos, state, state, 3); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleJigsawGenerate(ServerboundJigsawGeneratePacket packet) {
/*  941 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  942 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  945 */     BlockPos blockPos = packet.getPos();
/*  946 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  947 */     if (blockEntity instanceof JigsawBlockEntity) { JigsawBlockEntity jigsaw = (JigsawBlockEntity)blockEntity;
/*  948 */       jigsaw.generate(this.player.level(), packet.levels(), packet.keepJigsaws()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSelectTrade(ServerboundSelectTradePacket packet) {
/*  954 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*  955 */     int selection = packet.getItem();
/*  956 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu; if (abstractContainerMenu instanceof MerchantMenu) { MerchantMenu menu = (MerchantMenu)abstractContainerMenu;
/*  957 */       if (!menu.stillValid(this.player)) {
/*  958 */         LOGGER.debug("Player {} interacted with invalid menu {}", this.player, menu);
/*      */         return;
/*      */       } 
/*  961 */       menu.setSelectionHint(selection);
/*  962 */       menu.tryMoveItems(selection); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleEditBook(ServerboundEditBookPacket packet) {
/*  969 */     int slot = packet.slot();
/*  970 */     if (!Inventory.isHotbarSlot(slot) && slot != 40) {
/*      */       return;
/*      */     }
/*      */     
/*  974 */     List<String> contents = Lists.newArrayList();
/*  975 */     Optional<String> title = packet.title();
/*  976 */     Objects.requireNonNull(contents); title.ifPresent(contents::add);
/*  977 */     contents.addAll(packet.pages());
/*      */ 
/*      */ 
/*      */     
/*  981 */     Consumer<List<FilteredText>> handler = title.isPresent() ? (filteredContents -> signBook((FilteredText)filteredContents.get(0), filteredContents.subList(1, filteredContents.size()), slot)) : (filteredContents -> updateBookContents(filteredContents, slot));
/*  982 */     filterTextPacket(contents).thenAcceptAsync(handler, this.server);
/*      */   }
/*      */   
/*      */   private void updateBookContents(List<FilteredText> contents, int slot) {
/*  986 */     ItemStack carried = this.player.getInventory().getItem(slot);
/*  987 */     if (!carried.has(DataComponents.WRITABLE_BOOK_CONTENT)) {
/*      */       return;
/*      */     }
/*      */     
/*  991 */     List<Filterable<String>> pages = contents.stream().map(this::filterableFromOutgoing).toList();
/*  992 */     carried.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(pages));
/*      */   }
/*      */   
/*      */   private void signBook(FilteredText title, List<FilteredText> contents, int slot) {
/*  996 */     ItemStack carried = this.player.getInventory().getItem(slot);
/*  997 */     if (!carried.has(DataComponents.WRITABLE_BOOK_CONTENT)) {
/*      */       return;
/*      */     }
/*      */     
/* 1001 */     ItemStack writtenBook = carried.transmuteCopy(Items.WRITTEN_BOOK);
/* 1002 */     writtenBook.remove(DataComponents.WRITABLE_BOOK_CONTENT);
/*      */ 
/*      */ 
/*      */     
/* 1006 */     List<Filterable<Component>> pages = contents.stream().map(page -> filterableFromOutgoing(page).map(Component::literal)).toList();
/*      */     
/* 1008 */     writtenBook.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
/* 1009 */           filterableFromOutgoing(title), this.player
/* 1010 */           .getPlainTextName(), 0, pages, true));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1016 */     this.player.getInventory().setItem(slot, writtenBook);
/*      */   }
/*      */   
/*      */   private Filterable<String> filterableFromOutgoing(FilteredText text) {
/* 1020 */     if (this.player.isTextFilteringEnabled())
/*      */     {
/* 1022 */       return Filterable.passThrough(text.filteredOrEmpty());
/*      */     }
/* 1024 */     return Filterable.from(text);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleEntityTagQuery(ServerboundEntityTagQueryPacket packet) {
/* 1030 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 1032 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/*      */       return;
/*      */     }
/*      */     
/* 1036 */     Entity entity = this.player.level().getEntity(packet.getEntityId());
/* 1037 */     if (entity != null) {
/* 1038 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); 
/* 1039 */       try { TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
/* 1040 */         entity.saveWithoutId(output);
/* 1041 */         CompoundTag result = output.buildResult();
/* 1042 */         send(new ClientboundTagQueryPacket(packet.getTransactionId(), result));
/* 1043 */         reporter.close(); }
/*      */       catch (Throwable throwable) { try { reporter.close(); }
/*      */         catch (Throwable throwable1)
/*      */         { throwable.addSuppressed(throwable1); }
/*      */          throw throwable; }
/*      */     
/* 1049 */     }  } public void handleContainerSlotStateChanged(ServerboundContainerSlotStateChangedPacket packet) { PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 1051 */     if (this.player.isSpectator() || packet.containerId() != this.player.containerMenu.containerId) {
/*      */       return;
/*      */     }
/*      */     
/* 1055 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu; if (abstractContainerMenu instanceof CrafterMenu) { CrafterMenu crafterMenu = (CrafterMenu)abstractContainerMenu;
/* 1056 */       Container container = crafterMenu.getContainer(); if (container instanceof CrafterBlockEntity) { CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)container;
/* 1057 */         crafterBlockEntity.setSlotState(packet.slotId(), packet.newState()); }
/*      */        }
/*      */      }
/*      */ 
/*      */   
/*      */   public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket packet) {
/* 1063 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 1065 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/*      */       return;
/*      */     }
/*      */     
/* 1069 */     BlockEntity blockEntity = this.player.level().getBlockEntity(packet.getPos());
/* 1070 */     CompoundTag tag = (blockEntity != null) ? blockEntity.saveWithoutMetadata(this.player.registryAccess()) : null;
/* 1071 */     send(new ClientboundTagQueryPacket(packet.getTransactionId(), tag));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMovePlayer(ServerboundMovePlayerPacket packet) {
/* 1076 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1077 */     if (containsInvalidValues(packet.getX(0.0D), packet.getY(0.0D), packet.getZ(0.0D), packet.getYRot(0.0F), packet.getXRot(0.0F))) {
/* 1078 */       disconnect(Component.translatable("multiplayer.disconnect.invalid_player_movement"));
/*      */       return;
/*      */     } 
/* 1081 */     ServerLevel level = this.player.level();
/*      */     
/* 1083 */     if (this.player.wonGame) {
/*      */       return;
/*      */     }
/*      */     
/* 1087 */     if (this.tickCount == 0) {
/* 1088 */       resetPosition();
/*      */     }
/*      */     
/* 1091 */     if (!hasClientLoaded()) {
/*      */       return;
/*      */     }
/*      */     
/* 1095 */     float targetYRot = Mth.wrapDegrees(packet.getYRot(this.player.getYRot()));
/* 1096 */     float targetXRot = Mth.wrapDegrees(packet.getXRot(this.player.getXRot()));
/*      */     
/* 1098 */     if (updateAwaitingTeleport()) {
/*      */ 
/*      */       
/* 1101 */       this.player.absSnapRotationTo(targetYRot, targetXRot);
/*      */       
/*      */       return;
/*      */     } 
/* 1105 */     double targetX = clampHorizontal(packet.getX(this.player.getX()));
/* 1106 */     double targetY = clampVertical(packet.getY(this.player.getY()));
/* 1107 */     double targetZ = clampHorizontal(packet.getZ(this.player.getZ()));
/*      */     
/* 1109 */     if (this.player.isPassenger()) {
/* 1110 */       this.player.absSnapTo(this.player.getX(), this.player.getY(), this.player.getZ(), targetYRot, targetXRot);
/* 1111 */       this.player.level().getChunkSource().move(this.player);
/*      */       
/*      */       return;
/*      */     } 
/* 1115 */     double startX = this.player.getX();
/* 1116 */     double startY = this.player.getY();
/* 1117 */     double startZ = this.player.getZ();
/*      */     
/* 1119 */     double xDist = targetX - this.firstGoodX;
/* 1120 */     double yDist = targetY - this.firstGoodY;
/* 1121 */     double zDist = targetZ - this.firstGoodZ;
/*      */ 
/*      */     
/* 1124 */     double expectedDist = this.player.getDeltaMovement().lengthSqr();
/* 1125 */     double movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/*      */     
/* 1127 */     if (this.player.isSleeping()) {
/* 1128 */       if (movedDist > 1.0D) {
/* 1129 */         teleport(this.player.getX(), this.player.getY(), this.player.getZ(), targetYRot, targetXRot);
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/* 1134 */     boolean isFallFlying = this.player.isFallFlying();
/* 1135 */     if (level.tickRateManager().runsNormally()) {
/* 1136 */       this.receivedMovePacketCount++;
/* 1137 */       int deltaPackets = this.receivedMovePacketCount - this.knownMovePacketCount;
/* 1138 */       if (deltaPackets > 5) {
/* 1139 */         LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getPlainTextName(), Integer.valueOf(deltaPackets));
/* 1140 */         deltaPackets = 1;
/*      */       } 
/*      */       
/* 1143 */       if (shouldCheckPlayerMovement(isFallFlying)) {
/* 1144 */         float metersPerTick = isFallFlying ? 300.0F : 100.0F;
/* 1145 */         if (movedDist - expectedDist > (metersPerTick * deltaPackets)) {
/* 1146 */           LOGGER.warn("{} moved too quickly! {},{},{}", new Object[] { this.player.getPlainTextName(), Double.valueOf(xDist), Double.valueOf(yDist), Double.valueOf(zDist) });
/* 1147 */           teleport(this.player.getX(), this.player.getY(), this.player.getZ(), this.player.getYRot(), this.player.getXRot());
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1153 */     AABB oldAABB = this.player.getBoundingBox();
/*      */     
/* 1155 */     xDist = targetX - this.lastGoodX;
/* 1156 */     yDist = targetY - this.lastGoodY;
/* 1157 */     zDist = targetZ - this.lastGoodZ;
/*      */     
/* 1159 */     boolean movedUpwards = (yDist > 0.0D);
/*      */     
/* 1161 */     if (this.player.onGround() && !packet.isOnGround() && movedUpwards)
/*      */     {
/* 1163 */       this.player.jumpFromGround();
/*      */     }
/* 1165 */     boolean playerStandsOnSomething = this.player.verticalCollisionBelow;
/* 1166 */     this.player.move(MoverType.PLAYER, new Vec3(xDist, yDist, zDist));
/*      */     
/* 1168 */     double oyDist = yDist;
/*      */     
/* 1170 */     xDist = targetX - this.player.getX();
/* 1171 */     yDist = targetY - this.player.getY();
/* 1172 */     if (yDist > -0.5D || yDist < 0.5D) {
/* 1173 */       yDist = 0.0D;
/*      */     }
/* 1175 */     zDist = targetZ - this.player.getZ();
/* 1176 */     movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/* 1177 */     boolean fail = false;
/* 1178 */     if (!this.player.isChangingDimension() && movedDist > 0.0625D && !this.player.isSleeping() && !this.player.isCreative() && !this.player.isSpectator() && !this.player.isInPostImpulseGraceTime()) {
/* 1179 */       fail = true;
/* 1180 */       LOGGER.warn("{} moved wrongly!", this.player.getPlainTextName());
/*      */     } 
/*      */     
/* 1183 */     if (!this.player.noPhysics && !this.player.isSleeping() && ((
/* 1184 */       fail && level.noCollision(this.player, oldAABB)) || isEntityCollidingWithAnythingNew(level, this.player, oldAABB, targetX, targetY, targetZ))) {
/* 1185 */       teleport(startX, startY, startZ, targetYRot, targetXRot);
/* 1186 */       this.player.doCheckFallDamage(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ, packet.isOnGround());
/* 1187 */       this.player.removeLatestMovementRecording();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1192 */     this.player.absSnapTo(targetX, targetY, targetZ, targetYRot, targetXRot);
/*      */     
/* 1194 */     boolean isAutoSpinAttack = this.player.isAutoSpinAttack();
/* 1195 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1203 */       .clientIsFloating = (oyDist >= -0.03125D && !playerStandsOnSomething && !this.player.isSpectator() && !this.server.allowFlight() && !(this.player.getAbilities()).mayfly && !this.player.hasEffect(MobEffects.LEVITATION) && !isFallFlying && !isAutoSpinAttack && noBlocksAround(this.player));
/*      */     
/* 1205 */     this.player.level().getChunkSource().move(this.player);
/*      */     
/* 1207 */     Vec3 clientDeltaMovement = new Vec3(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ);
/* 1208 */     this.player.setOnGroundWithMovement(packet.isOnGround(), packet.horizontalCollision(), clientDeltaMovement);
/* 1209 */     this.player.doCheckFallDamage(clientDeltaMovement.x, clientDeltaMovement.y, clientDeltaMovement.z, packet.isOnGround());
/*      */     
/* 1211 */     handlePlayerKnownMovement(clientDeltaMovement);
/*      */ 
/*      */     
/* 1214 */     if (movedUpwards) {
/* 1215 */       this.player.resetFallDistance();
/*      */     }
/*      */     
/* 1218 */     if (packet.isOnGround() || this.player.hasLandedInLiquid() || this.player.onClimbable() || this.player.isSpectator() || isFallFlying || isAutoSpinAttack) {
/* 1219 */       this.player.tryResetCurrentImpulseContext();
/*      */     }
/*      */ 
/*      */     
/* 1223 */     this.player.checkMovementStatistics(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ);
/*      */     
/* 1225 */     this.lastGoodX = this.player.getX();
/* 1226 */     this.lastGoodY = this.player.getY();
/* 1227 */     this.lastGoodZ = this.player.getZ();
/*      */   }
/*      */   
/*      */   private boolean shouldCheckPlayerMovement(boolean isFallFlying) {
/* 1231 */     if (isSingleplayerOwner()) {
/* 1232 */       return false;
/*      */     }
/* 1234 */     if (this.player.isChangingDimension()) {
/* 1235 */       return false;
/*      */     }
/* 1237 */     GameRules gameRules = this.player.level().getGameRules();
/* 1238 */     if (!((Boolean)gameRules.get(GameRules.PLAYER_MOVEMENT_CHECK)).booleanValue()) {
/* 1239 */       return false;
/*      */     }
/* 1241 */     if (isFallFlying && !((Boolean)gameRules.get(GameRules.ELYTRA_MOVEMENT_CHECK)).booleanValue()) {
/* 1242 */       return false;
/*      */     }
/* 1244 */     return true;
/*      */   }
/*      */   
/*      */   private boolean updateAwaitingTeleport() {
/* 1248 */     if (this.awaitingPositionFromClient != null) {
/*      */       
/* 1250 */       if (this.tickCount - this.awaitingTeleportTime > 20) {
/* 1251 */         this.awaitingTeleportTime = this.tickCount;
/* 1252 */         teleport(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.getYRot(), this.player.getXRot());
/*      */       } 
/* 1254 */       return true;
/*      */     } 
/*      */     
/* 1257 */     this.awaitingTeleportTime = this.tickCount;
/* 1258 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isEntityCollidingWithAnythingNew(LevelReader level, Entity entity, AABB oldAABB, double newX, double newY, double newZ) {
/* 1262 */     AABB newAABB = entity.getBoundingBox().move(newX - entity.getX(), newY - entity.getY(), newZ - entity.getZ());
/* 1263 */     Iterable<VoxelShape> newCollisions = level.getPreMoveCollisions(entity, newAABB.deflate(9.999999747378752E-6D), oldAABB.getBottomCenter());
/* 1264 */     VoxelShape oldShape = Shapes.create(oldAABB.deflate(9.999999747378752E-6D));
/*      */     
/* 1266 */     for (VoxelShape shape : newCollisions) {
/* 1267 */       if (!Shapes.joinIsNotEmpty(shape, oldShape, BooleanOp.AND)) {
/* 1268 */         return true;
/*      */       }
/*      */     } 
/* 1271 */     return false;
/*      */   }
/*      */ 
/*      */   
/* 1275 */   public void teleport(double x, double y, double z, float yRot, float xRot) { teleport(new PositionMoveRotation(new Vec3(x, y, z), Vec3.ZERO, yRot, xRot), Collections.emptySet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void teleport(PositionMoveRotation destination, Set<Relative> relatives) {
/* 1284 */     this.awaitingTeleportTime = this.tickCount;
/* 1285 */     if (++this.awaitingTeleport == Integer.MAX_VALUE) {
/* 1286 */       this.awaitingTeleport = 0;
/*      */     }
/*      */     
/* 1289 */     this.player.teleportSetPosition(destination, relatives);
/* 1290 */     this.awaitingPositionFromClient = this.player.position();
/* 1291 */     send(ClientboundPlayerPositionPacket.of(this.awaitingTeleport, destination, relatives));
/*      */   }
/*      */   public void handlePlayerAction(ServerboundPlayerActionPacket packet) {
/*      */     PiercingWeapon piercingWeapon;
/*      */     ItemStack itemInHand;
/* 1296 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1297 */     if (!hasClientLoaded()) {
/*      */       return;
/*      */     }
/*      */     
/* 1301 */     BlockPos pos = packet.getPos();
/* 1302 */     this.player.resetLastActionTime();
/*      */     
/* 1304 */     ServerboundPlayerActionPacket.Action action = packet.getAction();
/*      */     
/* 1306 */     switch (action) {
/*      */       case PERFORM_RESPAWN:
/* 1308 */         if (this.player.isSpectator()) {
/*      */           return;
/*      */         }
/* 1311 */         itemInHand = this.player.getItemInHand(InteractionHand.MAIN_HAND);
/*      */         
/* 1313 */         if (this.player.cannotAttackWithItem(itemInHand, 5)) {
/*      */           return;
/*      */         }
/* 1316 */         piercingWeapon = (PiercingWeapon)itemInHand.get(DataComponents.PIERCING_WEAPON);
/* 1317 */         if (piercingWeapon != null) {
/* 1318 */           piercingWeapon.attack(this.player, EquipmentSlot.MAINHAND);
/*      */         }
/*      */         return;
/*      */       case REQUEST_STATS:
/* 1322 */         if (!this.player.isSpectator()) {
/* 1323 */           ItemStack swap = this.player.getItemInHand(InteractionHand.OFF_HAND);
/* 1324 */           this.player.setItemInHand(InteractionHand.OFF_HAND, this.player.getItemInHand(InteractionHand.MAIN_HAND));
/* 1325 */           this.player.setItemInHand(InteractionHand.MAIN_HAND, swap);
/* 1326 */           this.player.stopUsingItem();
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1330 */         if (!this.player.isSpectator()) {
/* 1331 */           this.player.drop(false);
/*      */         }
/*      */         return;
/*      */       case null:
/* 1335 */         if (!this.player.isSpectator()) {
/* 1336 */           this.player.drop(true);
/*      */         }
/*      */         return;
/*      */       case null:
/* 1340 */         this.player.releaseUsingItem();
/*      */         return;
/*      */       case null:
/*      */       case null:
/*      */       case null:
/* 1345 */         this.player.gameMode.handleBlockBreakAction(pos, action, packet.getDirection(), this.player.level().getMaxY(), packet.getSequence());
/* 1346 */         ackBlockChangesUpTo(packet.getSequence());
/*      */         return;
/*      */     } 
/* 1349 */     throw new IllegalArgumentException("Invalid player action");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean wasBlockPlacementAttempt(ServerPlayer player, ItemStack itemStack) { // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokevirtual isEmpty : ()Z
/*      */     //   4: ifeq -> 9
/*      */     //   7: iconst_0
/*      */     //   8: ireturn
/*      */     //   9: aload_1
/*      */     //   10: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*      */     //   13: astore_2
/*      */     //   14: aload_2
/*      */     //   15: instanceof net/minecraft/world/item/BlockItem
/*      */     //   18: ifne -> 43
/*      */     //   21: aload_2
/*      */     //   22: instanceof net/minecraft/world/item/BucketItem
/*      */     //   25: ifeq -> 58
/*      */     //   28: aload_2
/*      */     //   29: checkcast net/minecraft/world/item/BucketItem
/*      */     //   32: astore_3
/*      */     //   33: aload_3
/*      */     //   34: invokevirtual getContent : ()Lnet/minecraft/world/level/material/Fluid;
/*      */     //   37: getstatic net/minecraft/world/level/material/Fluids.EMPTY : Lnet/minecraft/world/level/material/Fluid;
/*      */     //   40: if_acmpeq -> 58
/*      */     //   43: aload_0
/*      */     //   44: invokevirtual getCooldowns : ()Lnet/minecraft/world/item/ItemCooldowns;
/*      */     //   47: aload_1
/*      */     //   48: invokevirtual isOnCooldown : (Lnet/minecraft/world/item/ItemStack;)Z
/*      */     //   51: ifne -> 58
/*      */     //   54: iconst_1
/*      */     //   55: goto -> 59
/*      */     //   58: iconst_0
/*      */     //   59: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1354	-> 0
/*      */     //   #1355	-> 7
/*      */     //   #1358	-> 9
/*      */     //   #1359	-> 14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   33	10	3	bucket	Lnet/minecraft/world/item/BucketItem;
/*      */     //   0	60	0	player	Lnet/minecraft/server/level/ServerPlayer;
/*      */     //   0	60	1	itemStack	Lnet/minecraft/world/item/ItemStack;
/*      */     //   14	46	2	item	Lnet/minecraft/world/item/Item; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleUseItemOn(ServerboundUseItemOnPacket packet) {
/* 1369 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1370 */     if (!hasClientLoaded()) {
/*      */       return;
/*      */     }
/* 1373 */     ackBlockChangesUpTo(packet.getSequence());
/*      */     
/* 1375 */     ServerLevel level = this.player.level();
/* 1376 */     InteractionHand hand = packet.getHand();
/* 1377 */     ItemStack itemStack = this.player.getItemInHand(hand);
/*      */     
/* 1379 */     if (!itemStack.isItemEnabled(level.enabledFeatures())) {
/*      */       return;
/*      */     }
/*      */     
/* 1383 */     BlockHitResult blockHit = packet.getHitResult();
/* 1384 */     Vec3 location = blockHit.getLocation();
/* 1385 */     BlockPos pos = blockHit.getBlockPos();
/* 1386 */     if (!this.player.isWithinBlockInteractionRange(pos, 1.0D)) {
/*      */       return;
/*      */     }
/* 1389 */     Vec3 distance = location.subtract(Vec3.atCenterOf(pos));
/* 1390 */     double limit = 1.0000001D;
/*      */     
/* 1392 */     if (Math.abs(distance.x()) >= 1.0000001D || Math.abs(distance.y()) >= 1.0000001D || Math.abs(distance.z()) >= 1.0000001D) {
/* 1393 */       LOGGER.warn("Rejecting UseItemOnPacket from {}: Location {} too far away from hit block {}.", new Object[] { this.player.getGameProfile().name(), location, pos });
/*      */       
/*      */       return;
/*      */     } 
/* 1397 */     Direction direction = blockHit.getDirection();
/* 1398 */     this.player.resetLastActionTime();
/*      */     
/* 1400 */     int maxY = this.player.level().getMaxY();
/* 1401 */     if (pos.getY() <= maxY) {
/* 1402 */       if (this.awaitingPositionFromClient == null && 
/* 1403 */         level.mayInteract(this.player, pos)) {
/* 1404 */         InteractionResult interactionResult = this.player.gameMode.useItemOn(this.player, level, itemStack, hand, blockHit);
/*      */         
/* 1406 */         if (interactionResult.consumesAction()) {
/* 1407 */           CriteriaTriggers.ANY_BLOCK_USE.trigger(this.player, blockHit.getBlockPos(), itemStack.copy());
/*      */         }
/*      */         
/* 1410 */         if (direction == Direction.UP && !interactionResult.consumesAction() && pos.getY() >= maxY && wasBlockPlacementAttempt(this.player, itemStack))
/*      */         
/* 1412 */         { MutableComponent mutableComponent = Component.translatable("build.tooHigh", new Object[] { Integer.valueOf(maxY) }).withStyle(ChatFormatting.RED);
/* 1413 */           this.player.sendSystemMessage(mutableComponent, true); }
/* 1414 */         else if (interactionResult instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)interactionResult; if (success.swingSource() == InteractionResult.SwingSource.SERVER) {
/* 1415 */             this.player.swing(hand, true);
/*      */           } }
/*      */       
/*      */       } 
/*      */     } else {
/* 1420 */       MutableComponent mutableComponent = Component.translatable("build.tooHigh", new Object[] { Integer.valueOf(maxY) }).withStyle(ChatFormatting.RED);
/* 1421 */       this.player.sendSystemMessage(mutableComponent, true);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1427 */     send(new ClientboundBlockUpdatePacket(level, pos));
/* 1428 */     send(new ClientboundBlockUpdatePacket(level, pos.relative(direction)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUseItem(ServerboundUseItemPacket packet) {
/* 1433 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1434 */     if (!hasClientLoaded()) {
/*      */       return;
/*      */     }
/* 1437 */     ackBlockChangesUpTo(packet.getSequence());
/*      */     
/* 1439 */     ServerLevel level = this.player.level();
/* 1440 */     InteractionHand hand = packet.getHand();
/* 1441 */     ItemStack itemStack = this.player.getItemInHand(hand);
/* 1442 */     this.player.resetLastActionTime();
/*      */     
/* 1444 */     if (itemStack.isEmpty() || !itemStack.isItemEnabled(level.enabledFeatures())) {
/*      */       return;
/*      */     }
/*      */     
/* 1448 */     float targetYRot = Mth.wrapDegrees(packet.getYRot());
/* 1449 */     float targetXRot = Mth.wrapDegrees(packet.getXRot());
/* 1450 */     if (targetXRot != this.player.getXRot() || targetYRot != this.player.getYRot())
/*      */     {
/* 1452 */       this.player.absSnapRotationTo(targetYRot, targetXRot);
/*      */     }
/*      */     
/* 1455 */     InteractionResult useResult = this.player.gameMode.useItem(this.player, level, itemStack, hand);
/* 1456 */     if (useResult instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)useResult; if (success.swingSource() == InteractionResult.SwingSource.SERVER) {
/* 1457 */         this.player.swing(hand, true);
/*      */       } }
/*      */   
/*      */   }
/*      */   
/*      */   public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packet) {
/* 1463 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1464 */     if (this.player.isSpectator()) {
/* 1465 */       for (ServerLevel level : this.server.getAllLevels()) {
/* 1466 */         Entity entity = packet.getEntity(level);
/*      */         
/* 1468 */         if (entity != null) {
/* 1469 */           this.player.teleportTo(level, entity.getX(), entity.getY(), entity.getZ(), Set.of(), entity.getYRot(), entity.getXRot(), true);
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePaddleBoat(ServerboundPaddleBoatPacket packet) {
/* 1478 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1479 */     Entity vehicle = this.player.getControlledVehicle();
/* 1480 */     if (vehicle instanceof AbstractBoat) { AbstractBoat boat = (AbstractBoat)vehicle;
/* 1481 */       boat.setPaddleState(packet.getLeft(), packet.getRight()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisconnect(DisconnectionDetails details) {
/* 1487 */     LOGGER.info("{} lost connection: {}", this.player.getPlainTextName(), details.reason().getString());
/* 1488 */     removePlayerFromWorld();
/*      */     
/* 1490 */     super.onDisconnect(details);
/*      */   }
/*      */   
/*      */   private void removePlayerFromWorld() {
/* 1494 */     this.chatMessageChain.close();
/*      */     
/* 1496 */     this.server.invalidateStatus();
/* 1497 */     this.server.getPlayerList().broadcastSystemMessage(Component.translatable("multiplayer.player.left", new Object[] { this.player.getDisplayName() }).withStyle(ChatFormatting.YELLOW), false);
/* 1498 */     this.player.disconnect();
/* 1499 */     this.server.getPlayerList().remove(this.player);
/*      */     
/* 1501 */     this.player.getTextFilter().leave();
/*      */   }
/*      */   
/*      */   public void ackBlockChangesUpTo(int packetSequenceNr) {
/* 1505 */     if (packetSequenceNr < 0) {
/* 1506 */       throw new IllegalArgumentException("Expected packet sequence nr >= 0");
/*      */     }
/*      */     
/* 1509 */     this.ackBlockChangesUpTo = Math.max(packetSequenceNr, this.ackBlockChangesUpTo);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) {
/* 1514 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1515 */     if (packet.getSlot() < 0 || packet.getSlot() >= Inventory.getSelectionSize()) {
/* 1516 */       LOGGER.warn("{} tried to set an invalid carried item", this.player.getPlainTextName());
/*      */       return;
/*      */     } 
/* 1519 */     if (this.player.getInventory().getSelectedSlot() != packet.getSlot() && this.player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
/* 1520 */       this.player.stopUsingItem();
/*      */     }
/* 1522 */     this.player.getInventory().setSelectedSlot(packet.getSlot());
/* 1523 */     this.player.resetLastActionTime();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChat(ServerboundChatPacket packet) {
/* 1528 */     Optional<LastSeenMessages> unpackedLastSeen = unpackAndApplyLastSeen(packet.lastSeenMessages());
/* 1529 */     if (unpackedLastSeen.isEmpty()) {
/*      */       return;
/*      */     }
/* 1532 */     tryHandleChat(packet.message(), false, () -> {
/*      */           PlayerChatMessage signedMessage;
/*      */           try {
/* 1535 */             signedMessage = getSignedMessage(packet, (LastSeenMessages)unpackedLastSeen.get());
/* 1536 */           } catch (net.minecraft.network.chat.SignedMessageChain.DecodeException e) {
/* 1537 */             handleMessageDecodeFailure(e);
/*      */             
/*      */             return;
/*      */           } 
/* 1541 */           CompletableFuture<FilteredText> filteredFuture = filterTextPacket(signedMessage.signedContent());
/* 1542 */           Component decorated = this.server.getChatDecorator().decorate(this.player, signedMessage.decoratedContent());
/*      */           
/* 1544 */           this.chatMessageChain.append(filteredFuture, ());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleChatCommand(ServerboundChatCommandPacket packet) {
/* 1555 */     tryHandleChat(packet.command(), true, () -> {
/* 1556 */           performUnsignedChatCommand(packet.command());
/* 1557 */           detectRateSpam();
/*      */         });
/*      */   }
/*      */   
/*      */   private void performUnsignedChatCommand(String command) {
/* 1562 */     ParseResults<CommandSourceStack> parsed = parseCommand(command);
/* 1563 */     if (this.server.enforceSecureProfile() && SignableCommand.hasSignableArguments(parsed)) {
/* 1564 */       LOGGER.error("Received unsigned command packet from {}, but the command requires signable arguments: {}", this.player.getGameProfile().name(), command);
/* 1565 */       this.player.sendSystemMessage(INVALID_COMMAND_SIGNATURE);
/*      */       return;
/*      */     } 
/* 1568 */     this.server.getCommands().performCommand(parsed, command);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSignedChatCommand(ServerboundChatCommandSignedPacket packet) {
/* 1573 */     Optional<LastSeenMessages> unpackedLastSeen = unpackAndApplyLastSeen(packet.lastSeenMessages());
/* 1574 */     if (unpackedLastSeen.isEmpty()) {
/*      */       return;
/*      */     }
/* 1577 */     tryHandleChat(packet.command(), true, () -> {
/* 1578 */           performSignedChatCommand(packet, (LastSeenMessages)unpackedLastSeen.get());
/* 1579 */           detectRateSpam();
/*      */         });
/*      */   }
/*      */   private void performSignedChatCommand(ServerboundChatCommandSignedPacket packet, LastSeenMessages lastSeenMessages) {
/*      */     Map<String, PlayerChatMessage> signedArguments;
/* 1584 */     ParseResults<CommandSourceStack> command = parseCommand(packet.command());
/*      */ 
/*      */     
/*      */     try {
/* 1588 */       signedArguments = collectSignedArguments(packet, SignableCommand.of(command), lastSeenMessages);
/* 1589 */     } catch (net.minecraft.network.chat.SignedMessageChain.DecodeException e) {
/* 1590 */       handleMessageDecodeFailure(e);
/*      */       
/*      */       return;
/*      */     } 
/* 1594 */     CommandSigningContext.SignedArguments signedArguments1 = new CommandSigningContext.SignedArguments(signedArguments);
/* 1595 */     command = Commands.mapSource(command, source -> source.withSigningContext(signingContext, this.chatMessageChain));
/*      */     
/* 1597 */     this.server.getCommands().performCommand(command, packet.command());
/*      */   }
/*      */   
/*      */   private void handleMessageDecodeFailure(SignedMessageChain.DecodeException e) {
/* 1601 */     LOGGER.warn("Failed to update secure chat state for {}: '{}'", this.player.getGameProfile().name(), e.getComponent().getString());
/* 1602 */     this.player.sendSystemMessage(e.getComponent().copy().withStyle(ChatFormatting.RED));
/*      */   }
/*      */ 
/*      */   
/*      */   private <S> Map<String, PlayerChatMessage> collectSignedArguments(ServerboundChatCommandSignedPacket packet, SignableCommand<S> command, LastSeenMessages lastSeenMessages) throws SignedMessageChain.DecodeException {
/* 1607 */     List<ArgumentSignatures.Entry> argumentSignatures = packet.argumentSignatures().entries();
/* 1608 */     List<SignableCommand.Argument<S>> parsedArguments = command.arguments();
/*      */ 
/*      */     
/* 1611 */     if (argumentSignatures.isEmpty()) {
/* 1612 */       return collectUnsignedArguments(parsedArguments);
/*      */     }
/*      */     
/* 1615 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/*      */ 
/*      */     
/* 1618 */     for (ArgumentSignatures.Entry clientArgument : argumentSignatures) {
/* 1619 */       SignableCommand.Argument<S> expectedArgument = command.getArgument(clientArgument.name());
/* 1620 */       if (expectedArgument == null) {
/*      */         
/* 1622 */         this.signedMessageDecoder.setChainBroken();
/* 1623 */         throw createSignedArgumentMismatchException(packet.command(), argumentSignatures, parsedArguments);
/*      */       } 
/* 1625 */       SignedMessageBody body = new SignedMessageBody(expectedArgument.value(), packet.timeStamp(), packet.salt(), lastSeenMessages);
/* 1626 */       object2ObjectOpenHashMap.put(expectedArgument.name(), this.signedMessageDecoder.unpack(clientArgument.signature(), body));
/*      */     } 
/*      */     
/* 1629 */     for (SignableCommand.Argument<S> expectedArgument : parsedArguments) {
/* 1630 */       if (!object2ObjectOpenHashMap.containsKey(expectedArgument.name())) {
/* 1631 */         throw createSignedArgumentMismatchException(packet.command(), argumentSignatures, parsedArguments);
/*      */       }
/*      */     } 
/*      */     
/* 1635 */     return object2ObjectOpenHashMap;
/*      */   }
/*      */   
/*      */   private <S> Map<String, PlayerChatMessage> collectUnsignedArguments(List<SignableCommand.Argument<S>> parsedArguments) throws SignedMessageChain.DecodeException {
/* 1639 */     Map<String, PlayerChatMessage> arguments = new HashMap<String, PlayerChatMessage>();
/* 1640 */     for (SignableCommand.Argument<S> parsedArgument : parsedArguments) {
/* 1641 */       SignedMessageBody body = SignedMessageBody.unsigned(parsedArgument.value());
/* 1642 */       arguments.put(parsedArgument.name(), this.signedMessageDecoder.unpack(null, body));
/*      */     } 
/* 1644 */     return arguments;
/*      */   }
/*      */   
/*      */   private static <S> SignedMessageChain.DecodeException createSignedArgumentMismatchException(String command, List<ArgumentSignatures.Entry> clientArguments, List<SignableCommand.Argument<S>> expectedArguments) {
/* 1648 */     String clientNames = (String)clientArguments.stream().map(ArgumentSignatures.Entry::name).collect(Collectors.joining(", "));
/* 1649 */     String expectedNames = (String)expectedArguments.stream().map(SignableCommand.Argument::name).collect(Collectors.joining(", "));
/* 1650 */     LOGGER.error("Signed command mismatch between server and client ('{}'): got [{}] from client, but expected [{}]", new Object[] { command, clientNames, expectedNames });
/* 1651 */     return new SignedMessageChain.DecodeException(INVALID_COMMAND_SIGNATURE);
/*      */   }
/*      */   
/*      */   private ParseResults<CommandSourceStack> parseCommand(String command) {
/* 1655 */     CommandDispatcher<CommandSourceStack> commands = this.server.getCommands().getDispatcher();
/* 1656 */     return commands.parse(command, this.player.createCommandSourceStack());
/*      */   }
/*      */   
/*      */   private void tryHandleChat(String message, boolean isCommand, Runnable chatHandler) {
/* 1660 */     if (isChatMessageIllegal(message)) {
/* 1661 */       disconnect(Component.translatable("multiplayer.disconnect.illegal_characters"));
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1666 */     if (!isCommand && this.player.getChatVisibility() == ChatVisiblity.HIDDEN) {
/*      */ 
/*      */       
/* 1669 */       send(new ClientboundSystemChatPacket(Component.translatable("chat.disabled.options").withStyle(ChatFormatting.RED), false));
/*      */       
/*      */       return;
/*      */     } 
/* 1673 */     this.player.resetLastActionTime();
/*      */ 
/*      */     
/* 1676 */     this.server.execute(chatHandler);
/*      */   }
/*      */ 
/*      */   
/*      */   private Optional<LastSeenMessages> unpackAndApplyLastSeen(LastSeenMessages.Update update) {
/* 1681 */     synchronized (this.lastSeenMessages) {
/*      */       
/* 1683 */       LastSeenMessages result = this.lastSeenMessages.applyUpdate(update);
/* 1684 */       return Optional.of(result);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isChatMessageIllegal(String message) {
/* 1694 */     for (int i = 0; i < message.length(); i++) {
/* 1695 */       if (!StringUtil.isAllowedChatCharacter(message.charAt(i))) {
/* 1696 */         return true;
/*      */       }
/*      */     } 
/* 1699 */     return false;
/*      */   }
/*      */   
/*      */   private PlayerChatMessage getSignedMessage(ServerboundChatPacket packet, LastSeenMessages lastSeenMessages) throws SignedMessageChain.DecodeException {
/* 1703 */     SignedMessageBody body = new SignedMessageBody(packet.message(), packet.timeStamp(), packet.salt(), lastSeenMessages);
/* 1704 */     return this.signedMessageDecoder.unpack(packet.signature(), body);
/*      */   }
/*      */   
/*      */   private void broadcastChatMessage(PlayerChatMessage message) {
/* 1708 */     this.server.getPlayerList().broadcastChatMessage(message, this.player, ChatType.bind(ChatType.CHAT, this.player));
/* 1709 */     detectRateSpam();
/*      */   }
/*      */   
/*      */   private void detectRateSpam() {
/* 1713 */     this.chatSpamThrottler.increment();
/* 1714 */     if (!this.chatSpamThrottler.isUnderThreshold() && !this.server.getPlayerList().isOp(this.player.nameAndId()) && !this.server.isSingleplayerOwner(this.player.nameAndId())) {
/* 1715 */       disconnect(Component.translatable("disconnect.spam"));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleChatAck(ServerboundChatAckPacket packet) {
/* 1722 */     synchronized (this.lastSeenMessages) {
/*      */       try {
/* 1724 */         this.lastSeenMessages.applyOffset(packet.offset());
/* 1725 */       } catch (net.minecraft.network.chat.LastSeenMessagesValidator.ValidationException e) {
/* 1726 */         LOGGER.error("Failed to validate message acknowledgement offset from {}: {}", this.player.getPlainTextName(), e.getMessage());
/* 1727 */         disconnect(CHAT_VALIDATION_FAILED);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAnimate(ServerboundSwingPacket packet) {
/* 1734 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1735 */     this.player.resetLastActionTime();
/* 1736 */     this.player.swing(packet.getHand());
/*      */   }
/*      */   
/*      */   public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) {
/*      */     Entity entity;
/* 1741 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1742 */     if (!hasClientLoaded()) {
/*      */       return;
/*      */     }
/* 1745 */     this.player.resetLastActionTime();
/* 1746 */     switch (packet.getAction()) {
/*      */       case PERFORM_RESPAWN:
/* 1748 */         this.player.setSprinting(true);
/*      */         return;
/*      */       case REQUEST_STATS:
/* 1751 */         this.player.setSprinting(false);
/*      */         return;
/*      */       case null:
/* 1754 */         if (this.player.isSleeping()) {
/* 1755 */           this.player.stopSleepInBed(false, true);
/* 1756 */           this.awaitingPositionFromClient = this.player.position();
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1760 */         entity = this.player.getControlledVehicle(); if (entity instanceof PlayerRideableJumping) { PlayerRideableJumping vehicle = (PlayerRideableJumping)entity;
/* 1761 */           int data = packet.getData();
/* 1762 */           if (vehicle.canJump() && data > 0) {
/* 1763 */             vehicle.handleStartJump(data);
/*      */           } }
/*      */         
/*      */         return;
/*      */       case null:
/* 1768 */         entity = this.player.getControlledVehicle(); if (entity instanceof PlayerRideableJumping) { PlayerRideableJumping vehicle = (PlayerRideableJumping)entity;
/* 1769 */           vehicle.handleStopJump(); }
/*      */         
/*      */         return;
/*      */       case null:
/* 1773 */         entity = this.player.getVehicle(); if (entity instanceof HasCustomInventoryScreen) { HasCustomInventoryScreen vehicleWithInventory = (HasCustomInventoryScreen)entity;
/* 1774 */           vehicleWithInventory.openCustomInventoryScreen(this.player); }
/*      */         
/*      */         return;
/*      */       case null:
/* 1778 */         if (!this.player.tryToStartFallFlying())
/*      */         {
/* 1780 */           this.player.stopFallFlying();
/*      */         }
/*      */         return;
/*      */     } 
/* 1784 */     throw new IllegalArgumentException("Invalid client command!");
/*      */   }
/*      */   
/*      */   public void sendPlayerChatMessage(PlayerChatMessage message, ChatType.Bound chatType) {
/*      */     int trackedCount;
/* 1789 */     send(new ClientboundPlayerChatPacket(this.nextChatIndex++, message
/*      */           
/* 1791 */           .link().sender(), message
/* 1792 */           .link().index(), message
/* 1793 */           .signature(), message
/* 1794 */           .signedBody().pack(this.messageSignatureCache), message
/* 1795 */           .unsignedContent(), message
/* 1796 */           .filterMask(), chatType));
/*      */ 
/*      */ 
/*      */     
/* 1800 */     MessageSignature signature = message.signature();
/* 1801 */     if (signature == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1805 */     this.messageSignatureCache.push(message.signedBody(), message.signature());
/*      */ 
/*      */     
/* 1808 */     synchronized (this.lastSeenMessages) {
/* 1809 */       this.lastSeenMessages.addPending(signature);
/* 1810 */       trackedCount = this.lastSeenMessages.trackedMessagesCount();
/*      */     } 
/* 1812 */     if (trackedCount > 4096) {
/* 1813 */       disconnect(Component.translatable("multiplayer.disconnect.too_many_pending_chats"));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1818 */   public void sendDisguisedChatMessage(Component content, ChatType.Bound chatType) { send(new ClientboundDisguisedChatPacket(content, chatType)); }
/*      */ 
/*      */ 
/*      */   
/* 1822 */   public SocketAddress getRemoteAddress() { return this.connection.getRemoteAddress(); }
/*      */ 
/*      */   
/*      */   public void switchToConfig() {
/* 1826 */     this.waitingForSwitchToConfig = true;
/* 1827 */     removePlayerFromWorld();
/* 1828 */     send(ClientboundStartConfigurationPacket.INSTANCE);
/* 1829 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.CLIENTBOUND);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1834 */   public void handlePingRequest(ServerboundPingRequestPacket packet) { this.connection.send(new ClientboundPongResponsePacket(packet.getTime())); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleInteract(ServerboundInteractPacket packet) {
/* 1844 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1845 */     if (!hasClientLoaded()) {
/*      */       return;
/*      */     }
/*      */     
/* 1849 */     final ServerLevel level = this.player.level();
/* 1850 */     final Entity target = packet.getTarget(level);
/* 1851 */     this.player.resetLastActionTime();
/*      */     
/* 1853 */     this.player.setShiftKeyDown(packet.isUsingSecondaryAction());
/*      */     
/* 1855 */     if (target != null) {
/* 1856 */       if (!level.getWorldBorder().isWithinBounds(target.blockPosition())) {
/*      */         return;
/*      */       }
/*      */       
/* 1860 */       AABB targetBounds = target.getBoundingBox();
/* 1861 */       if (packet.isWithinRange(this.player, targetBounds, 3.0D))
/* 1862 */         packet.dispatch(new ServerboundInteractPacket.Handler() {
/*      */               private void performInteraction(InteractionHand hand, ServerGamePacketListenerImpl.EntityInteraction interaction) {
/* 1864 */                 ItemStack tool = ServerGamePacketListenerImpl.this.player.getItemInHand(hand);
/* 1865 */                 if (!tool.isItemEnabled(level.enabledFeatures())) {
/*      */                   return;
/*      */                 }
/* 1868 */                 ItemStack usedItemStack = tool.copy();
/* 1869 */                 InteractionResult result = interaction.run(ServerGamePacketListenerImpl.this.player, target, hand);
/* 1870 */                 if (result instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)result;
/* 1871 */                   ItemStack awardedForStack = success.wasItemInteraction() ? usedItemStack : ItemStack.EMPTY;
/* 1872 */                   CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(ServerGamePacketListenerImpl.this.player, awardedForStack, target);
/* 1873 */                   if (success.swingSource() == InteractionResult.SwingSource.SERVER) {
/* 1874 */                     ServerGamePacketListenerImpl.this.player.swing(hand, true);
/*      */                   } }
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 1881 */               public void onInteraction(InteractionHand hand) { performInteraction(hand, Player::interactOn); }
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1886 */               public void onInteraction(InteractionHand hand, Vec3 location) { performInteraction(hand, (player, target, h) -> target.interactAt(player, location, h)); }
/*      */ 
/*      */ 
/*      */               
/*      */               public void onAttack() {
/* 1891 */                 if (!(target instanceof net.minecraft.world.entity.item.ItemEntity) && !(target instanceof net.minecraft.world.entity.ExperienceOrb) && target != ServerGamePacketListenerImpl.this.player) { if (target instanceof AbstractArrow) {
/* 1892 */                     AbstractArrow abstractArrow = (AbstractArrow)target; if (!abstractArrow.isAttackable()) {
/* 1893 */                       ServerGamePacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
/* 1894 */                       ServerGamePacketListenerImpl.LOGGER.warn("Player {} tried to attack an invalid entity", ServerGamePacketListenerImpl.this.player.getPlainTextName());
/*      */                       return;
/*      */                     } 
/*      */                   } 
/* 1898 */                   ItemStack heldItem = ServerGamePacketListenerImpl.this.player.getItemInHand(InteractionHand.MAIN_HAND);
/* 1899 */                   if (!heldItem.isItemEnabled(level.enabledFeatures())) {
/*      */                     return;
/*      */                   }
/*      */ 
/*      */                   
/* 1904 */                   if (ServerGamePacketListenerImpl.this.player.cannotAttackWithItem(heldItem, 5)) {
/*      */                     return;
/*      */                   }
/*      */                   
/* 1908 */                   ServerGamePacketListenerImpl.this.player.attack(target);
/*      */                   return; }
/*      */                 
/*      */                 ServerGamePacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
/*      */                 ServerGamePacketListenerImpl.LOGGER.warn("Player {} tried to attack an invalid entity", ServerGamePacketListenerImpl.this.player.getPlainTextName()); }
/*      */             }); 
/*      */     } 
/*      */   } @FunctionalInterface
/*      */   private static interface EntityInteraction { InteractionResult run(ServerPlayer param1ServerPlayer, Entity param1Entity, InteractionHand param1InteractionHand); } public void handleClientCommand(ServerboundClientCommandPacket packet) {
/* 1917 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1918 */     this.player.resetLastActionTime();
/* 1919 */     ServerboundClientCommandPacket.Action action = packet.getAction();
/* 1920 */     switch (action) {
/*      */       case PERFORM_RESPAWN:
/* 1922 */         if (this.player.wonGame) {
/* 1923 */           this.player.wonGame = false;
/* 1924 */           this.player = this.server.getPlayerList().respawn(this.player, true, Entity.RemovalReason.CHANGED_DIMENSION);
/* 1925 */           resetPosition();
/* 1926 */           restartClientLoadTimerAfterRespawn();
/* 1927 */           CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD); break;
/*      */         } 
/* 1929 */         if (this.player.getHealth() > 0.0F) {
/*      */           return;
/*      */         }
/* 1932 */         this.player = this.server.getPlayerList().respawn(this.player, false, Entity.RemovalReason.KILLED);
/* 1933 */         resetPosition();
/* 1934 */         restartClientLoadTimerAfterRespawn();
/* 1935 */         if (this.server.isHardcore()) {
/* 1936 */           this.player.setGameMode(GameType.SPECTATOR);
/*      */           
/* 1938 */           this.player.level().getGameRules().set(GameRules.SPECTATORS_GENERATE_CHUNKS, Boolean.valueOf(false), this.server);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case REQUEST_STATS:
/* 1943 */         this.player.getStats().sendStats(this.player);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerClose(ServerboundContainerClosePacket packet) {
/* 1950 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1951 */     this.player.doCloseContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerClick(ServerboundContainerClickPacket packet) {
/* 1956 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 1957 */     this.player.resetLastActionTime();
/* 1958 */     if (this.player.containerMenu.containerId != packet.containerId()) {
/*      */       return;
/*      */     }
/*      */     
/* 1962 */     if (this.player.isSpectator()) {
/*      */       
/* 1964 */       this.player.containerMenu.sendAllDataToRemote();
/*      */       
/*      */       return;
/*      */     } 
/* 1968 */     if (!this.player.containerMenu.stillValid(this.player)) {
/* 1969 */       LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */       
/*      */       return;
/*      */     } 
/* 1973 */     int slotIndex = packet.slotNum();
/* 1974 */     if (!this.player.containerMenu.isValidSlotIndex(slotIndex)) {
/*      */       
/* 1976 */       LOGGER.debug("Player {} clicked invalid slot index: {}, available slots: {}", new Object[] { this.player.getPlainTextName(), Integer.valueOf(slotIndex), Integer.valueOf(this.player.containerMenu.slots.size()) });
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1981 */     boolean fullResyncNeeded = (packet.stateId() != this.player.containerMenu.getStateId());
/*      */ 
/*      */     
/* 1984 */     this.player.containerMenu.suppressRemoteUpdates();
/* 1985 */     this.player.containerMenu.clicked(slotIndex, packet.buttonNum(), packet.clickType(), this.player);
/*      */ 
/*      */     
/* 1988 */     for (ObjectIterator objectIterator = Int2ObjectMaps.fastIterable(packet.changedSlots()).iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<HashedStack> e = (Int2ObjectMap.Entry)objectIterator.next();
/* 1989 */       this.player.containerMenu.setRemoteSlotUnsafe(e.getIntKey(), (HashedStack)e.getValue()); }
/*      */     
/* 1991 */     this.player.containerMenu.setRemoteCarried(packet.carriedItem());
/*      */ 
/*      */     
/* 1994 */     this.player.containerMenu.resumeRemoteUpdates();
/* 1995 */     if (fullResyncNeeded) {
/* 1996 */       this.player.containerMenu.broadcastFullState();
/*      */     } else {
/* 1998 */       this.player.containerMenu.broadcastChanges();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlaceRecipe(ServerboundPlaceRecipePacket packet) {
/* 2004 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2005 */     this.player.resetLastActionTime();
/* 2006 */     if (this.player.isSpectator() || this.player.containerMenu.containerId != packet.containerId()) {
/*      */       return;
/*      */     }
/*      */     
/* 2010 */     if (!this.player.containerMenu.stillValid(this.player)) {
/* 2011 */       LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */       
/*      */       return;
/*      */     } 
/* 2015 */     RecipeManager.ServerDisplayInfo displayInfo = this.server.getRecipeManager().getRecipeFromDisplay(packet.recipe());
/* 2016 */     if (displayInfo == null) {
/*      */       return;
/*      */     }
/*      */     
/* 2020 */     RecipeHolder<?> recipe = displayInfo.parent();
/* 2021 */     if (!this.player.getRecipeBook().contains(recipe.id())) {
/*      */       return;
/*      */     }
/*      */     
/* 2025 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu; if (abstractContainerMenu instanceof RecipeBookMenu) { RecipeBookMenu recipeBookMenu = (RecipeBookMenu)abstractContainerMenu;
/* 2026 */       if (recipe.value().placementInfo().isImpossibleToPlace()) {
/* 2027 */         LOGGER.debug("Player {} tried to place impossible recipe {}", this.player, recipe.id().identifier());
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 2032 */       RecipeBookMenu.PostPlaceAction postPlaceAction = recipeBookMenu.handlePlacement(packet.useMaxItems(), this.player.isCreative(), recipe, this.player.level(), this.player.getInventory());
/* 2033 */       if (postPlaceAction == RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE) {
/* 2034 */         send(new ClientboundPlaceGhostRecipePacket(this.player.containerMenu.containerId, displayInfo.display().display()));
/*      */       } }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) {
/* 2041 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2042 */     this.player.resetLastActionTime();
/* 2043 */     if (this.player.containerMenu.containerId != packet.containerId() || this.player.isSpectator()) {
/*      */       return;
/*      */     }
/*      */     
/* 2047 */     if (!this.player.containerMenu.stillValid(this.player)) {
/* 2048 */       LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */       
/*      */       return;
/*      */     } 
/* 2052 */     boolean clickAccepted = this.player.containerMenu.clickMenuButton(this.player, packet.buttonId());
/* 2053 */     if (clickAccepted) {
/* 2054 */       this.player.containerMenu.broadcastChanges();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) {
/* 2060 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2061 */     if (this.player.hasInfiniteMaterials()) {
/* 2062 */       boolean drop = (packet.slotNum() < 0);
/* 2063 */       ItemStack itemStack = packet.itemStack();
/* 2064 */       if (!itemStack.isItemEnabled(this.player.level().enabledFeatures())) {
/*      */         return;
/*      */       }
/*      */       
/* 2068 */       boolean validSlot = (packet.slotNum() >= 1 && packet.slotNum() <= 45);
/* 2069 */       boolean validData = (itemStack.isEmpty() || itemStack.getCount() <= itemStack.getMaxStackSize());
/*      */       
/* 2071 */       if (validSlot && validData) {
/* 2072 */         this.player.inventoryMenu.getSlot(packet.slotNum()).setByPlayer(itemStack);
/*      */ 
/*      */         
/* 2075 */         this.player.inventoryMenu.setRemoteSlot(packet.slotNum(), itemStack);
/* 2076 */         this.player.inventoryMenu.broadcastChanges();
/* 2077 */       } else if (drop && validData) {
/* 2078 */         if (this.dropSpamThrottler.isUnderThreshold()) {
/* 2079 */           this.dropSpamThrottler.increment();
/*      */           
/* 2081 */           this.player.drop(itemStack, true);
/*      */         } else {
/* 2083 */           LOGGER.warn("Player {} was dropping items too fast in creative mode, ignoring.", this.player.getPlainTextName());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSignUpdate(ServerboundSignUpdatePacket packet) {
/* 2091 */     List<String> lines = (List)Stream.of(packet.getLines()).map(ChatFormatting::stripFormatting).collect(Collectors.toList());
/* 2092 */     filterTextPacket(lines).thenAcceptAsync(filteredLines -> updateSignText(packet, filteredLines), this.server);
/*      */   }
/*      */   
/*      */   private void updateSignText(ServerboundSignUpdatePacket packet, List<FilteredText> lines) {
/* 2096 */     this.player.resetLastActionTime();
/* 2097 */     ServerLevel level = this.player.level();
/* 2098 */     BlockPos pos = packet.getPos();
/* 2099 */     if (level.hasChunkAt(pos)) {
/* 2100 */       SignBlockEntity sign; BlockEntity blockEntity = level.getBlockEntity(pos);
/*      */       
/* 2102 */       if (blockEntity instanceof SignBlockEntity) { sign = (SignBlockEntity)blockEntity; }
/*      */       else
/*      */       { return; }
/*      */       
/* 2106 */       sign.updateSignText(this.player, packet.isFrontText(), lines);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) {
/* 2112 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2113 */     (this.player.getAbilities()).flying = (packet.isFlying() && (this.player.getAbilities()).mayfly);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleClientInformation(ServerboundClientInformationPacket packet) {
/* 2118 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2119 */     boolean wasHatShown = this.player.isModelPartShown(PlayerModelPart.HAT);
/* 2120 */     this.player.updateOptions(packet.information());
/* 2121 */     if (this.player.isModelPartShown(PlayerModelPart.HAT) != wasHatShown) {
/* 2122 */       this.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_HAT, this.player));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChangeDifficulty(ServerboundChangeDifficultyPacket packet) {
/* 2128 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 2130 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) && !isSingleplayerOwner()) {
/* 2131 */       LOGGER.warn("Player {} tried to change difficulty to {} without required permissions", this.player.getGameProfile().name(), packet.difficulty().getDisplayName());
/*      */       
/*      */       return;
/*      */     } 
/* 2135 */     this.server.setDifficulty(packet.difficulty(), false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChangeGameMode(ServerboundChangeGameModePacket packet) {
/* 2140 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 2142 */     if (!GameModeCommand.PERMISSION_CHECK.check(this.player.permissions())) {
/* 2143 */       LOGGER.warn("Player {} tried to change game mode to {} without required permissions", this.player.getGameProfile().name(), packet.mode().getShortDisplayName().getString());
/*      */       
/*      */       return;
/*      */     } 
/* 2147 */     GameModeCommand.setGameMode(this.player, packet.mode());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleLockDifficulty(ServerboundLockDifficultyPacket packet) {
/* 2152 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 2154 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) && !isSingleplayerOwner()) {
/*      */       return;
/*      */     }
/*      */     
/* 2158 */     this.server.setDifficultyLocked(packet.isLocked());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket packet) {
/* 2163 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/*      */     
/* 2165 */     RemoteChatSession.Data newChatSession = packet.chatSession();
/* 2166 */     ProfilePublicKey.Data oldProfileKey = (this.chatSession != null) ? this.chatSession.profilePublicKey().data() : null;
/* 2167 */     ProfilePublicKey.Data newProfileKey = newChatSession.profilePublicKey();
/* 2168 */     if (Objects.equals(oldProfileKey, newProfileKey)) {
/*      */       return;
/*      */     }
/*      */     
/* 2172 */     if (oldProfileKey != null && newProfileKey.expiresAt().isBefore(oldProfileKey.expiresAt())) {
/* 2173 */       disconnect(ProfilePublicKey.EXPIRED_PROFILE_PUBLIC_KEY);
/*      */       
/*      */       return;
/*      */     } 
/*      */     try {
/* 2178 */       SignatureValidator profileKeySignatureValidator = this.server.services().profileKeySignatureValidator();
/* 2179 */       if (profileKeySignatureValidator == null) {
/* 2180 */         LOGGER.warn("Ignoring chat session from {} due to missing Services public key", this.player.getGameProfile().name());
/*      */         return;
/*      */       } 
/* 2183 */       resetPlayerChatState(newChatSession.validate(this.player.getGameProfile(), profileKeySignatureValidator));
/* 2184 */     } catch (net.minecraft.world.entity.player.ProfilePublicKey.ValidationException e) {
/* 2185 */       LOGGER.error("Failed to validate profile key: {}", e.getMessage());
/* 2186 */       disconnect(e.getComponent());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleConfigurationAcknowledged(ServerboundConfigurationAcknowledgedPacket packet) {
/* 2192 */     if (!this.waitingForSwitchToConfig) {
/* 2193 */       throw new IllegalStateException("Client acknowledged config, but none was requested");
/*      */     }
/* 2195 */     this.connection.setupInboundProtocol(ConfigurationProtocols.SERVERBOUND, new ServerConfigurationPacketListenerImpl(this.server, this.connection, createCookie(this.player.clientInformation())));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChunkBatchReceived(ServerboundChunkBatchReceivedPacket packet) {
/* 2200 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2201 */     this.chunkSender.onChunkBatchReceivedByClient(packet.desiredChunksPerTick());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDebugSubscriptionRequest(ServerboundDebugSubscriptionRequestPacket packet) {
/* 2206 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2207 */     this.player.requestDebugSubscriptions(packet.subscriptions());
/*      */   }
/*      */   
/*      */   private void resetPlayerChatState(RemoteChatSession chatSession) {
/* 2211 */     this.chatSession = chatSession;
/* 2212 */     this.signedMessageDecoder = chatSession.createMessageDecoder(this.player.getUUID());
/*      */ 
/*      */     
/* 2215 */     this.chatMessageChain.append(() -> {
/* 2216 */           this.player.setChatSession(chatSession);
/* 2217 */           this.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT), List.of(this.player)));
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleClientTickEnd(ServerboundClientTickEndPacket packet) {
/* 2232 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.player.level());
/* 2233 */     if (!this.receivedMovementThisTick) {
/* 2234 */       this.player.setKnownMovement(Vec3.ZERO);
/*      */     }
/* 2236 */     this.receivedMovementThisTick = false;
/*      */   }
/*      */   
/*      */   private void handlePlayerKnownMovement(Vec3 movement) {
/* 2240 */     if (movement.lengthSqr() > 9.999999747378752E-6D) {
/* 2241 */       this.player.resetLastActionTime();
/*      */     }
/* 2243 */     this.player.setKnownMovement(movement);
/* 2244 */     this.receivedMovementThisTick = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2249 */   public boolean hasInfiniteMaterials() { return this.player.hasInfiniteMaterials(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2254 */   public ServerPlayer getPlayer() { return this.player; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2261 */   public boolean hasClientLoaded() { return (!this.waitingForRespawn && this.clientLoadedTimeoutTimer <= 0); }
/*      */ 
/*      */   
/*      */   public void tickClientLoadTimeout() {
/* 2265 */     if (this.clientLoadedTimeoutTimer > 0) {
/* 2266 */       this.clientLoadedTimeoutTimer--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2274 */   private void markClientLoaded() { this.clientLoadedTimeoutTimer = 0; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2281 */   public void markClientUnloadedAfterDeath() { this.waitingForRespawn = true; }
/*      */ 
/*      */   
/*      */   private void restartClientLoadTimerAfterRespawn() {
/* 2285 */     this.waitingForRespawn = false;
/* 2286 */     this.clientLoadedTimeoutTimer = 60;
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerGamePacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
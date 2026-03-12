/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.cache.CacheBuilder;
/*      */ import com.google.common.cache.CacheLoader;
/*      */ import com.google.common.cache.LoadingCache;
/*      */ import com.google.common.hash.HashCode;
/*      */ import com.google.common.net.InetAddresses;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.UUIDUtil;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.TypedDataComponent;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.PacketSendListener;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.OutgoingChatMessage;
/*      */ import net.minecraft.network.chat.RemoteChatSession;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMountScreenOpenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
/*      */ import net.minecraft.network.protocol.status.ServerStatus;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.PlayerAdvancements;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.network.ServerGamePacketListenerImpl;
/*      */ import net.minecraft.server.network.TextFilter;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.stats.ServerRecipeBook;
/*      */ import net.minecraft.stats.ServerStatsCounter;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.HashOps;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.debug.DebugSubscription;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.attribute.BedRule;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.DamageTypes;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.PositionMoveRotation;
/*      */ import net.minecraft.world.entity.Relative;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Monster;
/*      */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Input;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*      */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ContainerListener;
/*      */ import net.minecraft.world.inventory.ContainerSynchronizer;
/*      */ import net.minecraft.world.inventory.HorseInventoryMenu;
/*      */ import net.minecraft.world.inventory.NautilusInventoryMenu;
/*      */ import net.minecraft.world.inventory.RemoteSlot;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemCooldowns;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.MapItem;
/*      */ import net.minecraft.world.item.ServerItemCooldowns;
/*      */ import net.minecraft.world.item.component.WrittenBookContent;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.crafting.RecipeHolder;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*      */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.portal.TeleportTransition;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.TagValueInput;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ public class ServerPlayer
/*      */   extends Player
/*      */ {
/*  211 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int NEUTRAL_MOB_DEATH_NOTIFICATION_RADII_XZ = 32;
/*      */   private static final int NEUTRAL_MOB_DEATH_NOTIFICATION_RADII_Y = 10;
/*      */   private static final int FLY_STAT_RECORDING_SPEED = 25;
/*      */   public static final double BLOCK_INTERACTION_DISTANCE_VERIFICATION_BUFFER = 1.0D;
/*      */   public static final double ENTITY_INTERACTION_DISTANCE_VERIFICATION_BUFFER = 3.0D;
/*      */   public static final int ENDER_PEARL_TICKET_RADIUS = 2;
/*      */   public static final String ENDER_PEARLS_TAG = "ender_pearls";
/*      */   public static final String ENDER_PEARL_DIMENSION_TAG = "ender_pearl_dimension";
/*      */   public static final String TAG_DIMENSION = "Dimension";
/*  222 */   private static final AttributeModifier CREATIVE_BLOCK_INTERACTION_RANGE_MODIFIER = new AttributeModifier(
/*  223 */       Identifier.withDefaultNamespace("creative_mode_block_range"), 0.5D, AttributeModifier.Operation.ADD_VALUE);
/*      */ 
/*      */ 
/*      */   
/*  227 */   private static final AttributeModifier CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER = new AttributeModifier(
/*  228 */       Identifier.withDefaultNamespace("creative_mode_entity_range"), 2.0D, AttributeModifier.Operation.ADD_VALUE);
/*      */ 
/*      */ 
/*      */   
/*  232 */   private static final Component SPAWN_SET_MESSAGE = Component.translatable("block.minecraft.set_spawn");
/*  233 */   private static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER = new AttributeModifier(
/*  234 */       Identifier.withDefaultNamespace("waypoint_transmit_range_crouch"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*      */   
/*      */   private static final boolean DEFAULT_SEEN_CREDITS = false;
/*      */   
/*      */   private static final boolean DEFAULT_SPAWN_EXTRA_PARTICLES_ON_FALL = false;
/*      */   
/*      */   public ServerGamePacketListenerImpl connection;
/*      */   
/*      */   private final MinecraftServer server;
/*      */   
/*      */   public final ServerPlayerGameMode gameMode;
/*      */   private final PlayerAdvancements advancements;
/*      */   private final ServerStatsCounter stats;
/*  247 */   private float lastRecordedHealthAndAbsorption = Float.MIN_VALUE;
/*  248 */   private int lastRecordedFoodLevel = Integer.MIN_VALUE;
/*  249 */   private int lastRecordedAirLevel = Integer.MIN_VALUE;
/*  250 */   private int lastRecordedArmor = Integer.MIN_VALUE;
/*  251 */   private int lastRecordedLevel = Integer.MIN_VALUE;
/*  252 */   private int lastRecordedExperience = Integer.MIN_VALUE;
/*  253 */   private float lastSentHealth = -1.0E8F;
/*  254 */   private int lastSentFood = -99999999;
/*      */   private boolean lastFoodSaturationZero = true;
/*  256 */   private int lastSentExp = -99999999;
/*  257 */   private ChatVisiblity chatVisibility = ChatVisiblity.FULL;
/*  258 */   private ParticleStatus particleStatus = ParticleStatus.ALL;
/*      */   private boolean canChatColor = true;
/*  260 */   private long lastActionTime = Util.getMillis();
/*      */   private Entity camera;
/*      */   private boolean isChangingDimension;
/*      */   public boolean seenCredits = false;
/*      */   private final ServerRecipeBook recipeBook;
/*      */   private Vec3 levitationStartPos;
/*      */   private int levitationStartTime;
/*      */   private boolean disconnected;
/*  268 */   private int requestedViewDistance = 2;
/*  269 */   private String language = "en_us";
/*      */   
/*      */   private Vec3 startingToFallPosition;
/*      */   
/*      */   private Vec3 enteredNetherPosition;
/*      */   private Vec3 enteredLavaOnVehiclePosition;
/*  275 */   private SectionPos lastSectionPos = SectionPos.of(0, 0, 0);
/*  276 */   private ChunkTrackingView chunkTrackingView = ChunkTrackingView.EMPTY;
/*      */   
/*      */   private RespawnConfig respawnConfig;
/*      */   
/*      */   private final TextFilter textFilter;
/*      */   
/*      */   private boolean textFilteringEnabled;
/*      */   private boolean allowsListing;
/*      */   private boolean spawnExtraParticlesOnFall = false;
/*  285 */   private WardenSpawnTracker wardenSpawnTracker = new WardenSpawnTracker();
/*      */   private BlockPos raidOmenPosition;
/*  287 */   private Vec3 lastKnownClientMovement = Vec3.ZERO;
/*  288 */   private Input lastClientInput = Input.EMPTY;
/*  289 */   private final Set<ThrownEnderpearl> enderPearls = new HashSet();
/*      */   
/*      */   private long timeEntitySatOnShoulder;
/*  292 */   private CompoundTag shoulderEntityLeft = new CompoundTag();
/*  293 */   private CompoundTag shoulderEntityRight = new CompoundTag();
/*      */   
/*  295 */   private final ContainerSynchronizer containerSynchronizer = new ContainerSynchronizer()
/*      */     {
/*      */       
/*  298 */       private final LoadingCache<TypedDataComponent<?>, Integer> cache = CacheBuilder.newBuilder()
/*  299 */         .maximumSize(256L)
/*  300 */         .build(new CacheLoader<TypedDataComponent<?>, Integer>() {
/*  301 */             private final DynamicOps<HashCode> registryHashOps = ServerPlayer.null.this.this$0.registryAccess().createSerializationContext(HashOps.CRC32C_INSTANCE);
/*      */ 
/*      */ 
/*      */             
/*  305 */             public Integer load(TypedDataComponent<?> component) { return Integer.valueOf(((HashCode)component.encodeValue(this.registryHashOps).getOrThrow(msg -> new IllegalArgumentException("Failed to hash " + String.valueOf(component) + ": " + msg))).asInt()); }
/*      */           });
/*      */ 
/*      */ 
/*      */       
/*      */       public void sendInitialData(AbstractContainerMenu container, List<ItemStack> slotItems, ItemStack carriedItem, int[] dataSlots) {
/*  311 */         ServerPlayer.this.connection.send(new ClientboundContainerSetContentPacket(container.containerId, container.incrementStateId(), slotItems, carriedItem));
/*  312 */         for (int slot = 0; slot < dataSlots.length; slot++) {
/*  313 */           broadcastDataValue(container, slot, dataSlots[slot]);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  319 */       public void sendSlotChange(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) { ServerPlayer.this.connection.send(new ClientboundContainerSetSlotPacket(container.containerId, container.incrementStateId(), slotIndex, itemStack)); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  324 */       public void sendCarriedChange(AbstractContainerMenu container, ItemStack itemStack) { ServerPlayer.this.connection.send(new ClientboundSetCursorItemPacket(itemStack)); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  329 */       public void sendDataChange(AbstractContainerMenu container, int id, int value) { broadcastDataValue(container, id, value); }
/*      */ 
/*      */ 
/*      */       
/*  333 */       private void broadcastDataValue(AbstractContainerMenu container, int id, int value) { ServerPlayer.this.connection.send(new ClientboundContainerSetDataPacket(container.containerId, id, value)); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  338 */       public RemoteSlot createSlot() { Objects.requireNonNull(this.cache); return new RemoteSlot.Synchronized(this.cache::getUnchecked); }
/*      */     };
/*      */ 
/*      */   
/*  342 */   private final ContainerListener containerListener = new ContainerListener()
/*      */     {
/*      */       public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack changedItem) {
/*  345 */         Slot slot = container.getSlot(slotIndex);
/*  346 */         if (slot instanceof net.minecraft.world.inventory.ResultSlot) {
/*      */           return;
/*      */         }
/*      */         
/*  350 */         if (slot.container == ServerPlayer.this.getInventory())
/*      */         {
/*  352 */           CriteriaTriggers.INVENTORY_CHANGED.trigger(ServerPlayer.this, ServerPlayer.this.getInventory(), changedItem);
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*      */     };
/*      */ 
/*      */   
/*      */   private RemoteChatSession chatSession;
/*      */   
/*      */   public final Object object;
/*      */ 
/*      */   
/*  366 */   private final CommandSource commandSource = new CommandSource()
/*      */     {
/*      */       public boolean acceptsSuccess() {
/*  369 */         return ((Boolean)ServerPlayer.this.level().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  374 */       public boolean acceptsFailure() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  379 */       public boolean shouldInformAdmins() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  384 */       public void sendSystemMessage(Component message) { ServerPlayer.this.sendSystemMessage(message); }
/*      */     };
/*      */ 
/*      */   
/*  388 */   private Set<DebugSubscription<?>> requestedDebugSubscriptions = Set.of(); private int containerCounter; public boolean wonGame;
/*      */   
/*      */   public ServerPlayer(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation) {
/*  391 */     super(level, gameProfile);
/*  392 */     this.server = server;
/*      */     
/*  394 */     this.textFilter = server.createTextFilterForPlayer(this);
/*  395 */     this.gameMode = server.createGameModeForPlayer(this);
/*  396 */     this.gameMode.setGameModeForPlayer(calculateGameModeForNewPlayer(null), null);
/*      */     
/*  398 */     this.recipeBook = new ServerRecipeBook((id, output) -> server.getRecipeManager().listDisplaysForRecipe(id, output));
/*      */     
/*  400 */     this.stats = server.getPlayerList().getPlayerStats(this);
/*  401 */     this.advancements = server.getPlayerList().getPlayerAdvancements(this);
/*      */     
/*  403 */     updateOptions(clientInformation);
/*      */     
/*  405 */     this.object = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos adjustSpawnLocation(ServerLevel level, BlockPos spawnSuggestion) {
/*  411 */     CompletableFuture<Vec3> future = PlayerSpawnFinder.findSpawn(level, spawnSuggestion);
/*  412 */     Objects.requireNonNull(future); this.server.managedBlock(future::isDone);
/*  413 */     return BlockPos.containing((Position)future.join());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  418 */     super.readAdditionalSaveData(input);
/*      */     
/*  420 */     this.wardenSpawnTracker = (WardenSpawnTracker)input.read("warden_spawn_tracker", WardenSpawnTracker.CODEC).orElseGet(WardenSpawnTracker::new);
/*      */     
/*  422 */     this.enteredNetherPosition = (Vec3)input.read("entered_nether_pos", Vec3.CODEC).orElse(null);
/*      */     
/*  424 */     this.seenCredits = input.getBooleanOr("seenCredits", false);
/*      */     
/*  426 */     input.read("recipeBook", ServerRecipeBook.Packed.CODEC)
/*  427 */       .ifPresent(p -> this.recipeBook.loadUntrusted(p, ()));
/*      */     
/*  429 */     if (isSleeping()) {
/*  430 */       stopSleeping();
/*      */     }
/*      */     
/*  433 */     this.respawnConfig = (RespawnConfig)input.read("respawn", RespawnConfig.CODEC).orElse(null);
/*      */     
/*  435 */     this.spawnExtraParticlesOnFall = input.getBooleanOr("spawn_extra_particles_on_fall", false);
/*      */     
/*  437 */     this.raidOmenPosition = (BlockPos)input.read("raid_omen_position", BlockPos.CODEC).orElse(null);
/*      */     
/*  439 */     this.gameMode.setGameModeForPlayer(
/*  440 */         calculateGameModeForNewPlayer(readPlayerMode(input, "playerGameType")), 
/*  441 */         readPlayerMode(input, "previousPlayerGameType"));
/*      */ 
/*      */     
/*  444 */     setShoulderEntityLeft((CompoundTag)input.read("ShoulderEntityLeft", CompoundTag.CODEC).orElseGet(CompoundTag::new));
/*  445 */     setShoulderEntityRight((CompoundTag)input.read("ShoulderEntityRight", CompoundTag.CODEC).orElseGet(CompoundTag::new));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  450 */     super.addAdditionalSaveData(output);
/*      */     
/*  452 */     output.store("warden_spawn_tracker", WardenSpawnTracker.CODEC, this.wardenSpawnTracker);
/*      */     
/*  454 */     storeGameTypes(output);
/*  455 */     output.putBoolean("seenCredits", this.seenCredits);
/*      */     
/*  457 */     output.storeNullable("entered_nether_pos", Vec3.CODEC, this.enteredNetherPosition);
/*      */     
/*  459 */     saveParentVehicle(output);
/*      */     
/*  461 */     output.store("recipeBook", ServerRecipeBook.Packed.CODEC, this.recipeBook.pack());
/*      */     
/*  463 */     output.putString("Dimension", level().dimension().identifier().toString());
/*      */     
/*  465 */     output.storeNullable("respawn", RespawnConfig.CODEC, this.respawnConfig);
/*      */     
/*  467 */     output.putBoolean("spawn_extra_particles_on_fall", this.spawnExtraParticlesOnFall);
/*      */     
/*  469 */     output.storeNullable("raid_omen_position", BlockPos.CODEC, this.raidOmenPosition);
/*      */     
/*  471 */     saveEnderPearls(output);
/*      */     
/*  473 */     if (!getShoulderEntityLeft().isEmpty()) {
/*  474 */       output.store("ShoulderEntityLeft", CompoundTag.CODEC, getShoulderEntityLeft());
/*      */     }
/*  476 */     if (!getShoulderEntityRight().isEmpty()) {
/*  477 */       output.store("ShoulderEntityRight", CompoundTag.CODEC, getShoulderEntityRight());
/*      */     }
/*      */   }
/*      */   
/*      */   private void saveParentVehicle(ValueOutput playerOutput) {
/*  482 */     Entity rootVehicle = getRootVehicle();
/*  483 */     Entity vehicle = getVehicle();
/*  484 */     if (vehicle != null && rootVehicle != this && rootVehicle.hasExactlyOnePlayerPassenger()) {
/*  485 */       ValueOutput vehicleWrapper = playerOutput.child("RootVehicle");
/*  486 */       vehicleWrapper.store("Attach", UUIDUtil.CODEC, vehicle.getUUID());
/*      */       
/*  488 */       rootVehicle.save(vehicleWrapper.child("Entity"));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void loadAndSpawnParentVehicle(ValueInput playerInput) {
/*  493 */     Optional<ValueInput> rootTag = playerInput.child("RootVehicle");
/*  494 */     if (rootTag.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  498 */     ServerLevel serverLevel = level();
/*  499 */     Entity vehicle = EntityType.loadEntityRecursive(((ValueInput)rootTag.get()).childOrEmpty("Entity"), serverLevel, EntitySpawnReason.LOAD, e -> {
/*  500 */           if (!serverLevel.addWithUUID(e)) {
/*  501 */             return null;
/*      */           }
/*  503 */           return e;
/*      */         });
/*      */     
/*  506 */     if (vehicle == null) {
/*      */       return;
/*      */     }
/*      */     
/*  510 */     UUID attachTo = (UUID)((ValueInput)rootTag.get()).read("Attach", UUIDUtil.CODEC).orElse(null);
/*      */     
/*  512 */     if (vehicle.getUUID().equals(attachTo)) {
/*  513 */       startRiding(vehicle, true, false);
/*      */     } else {
/*  515 */       for (Entity entity : vehicle.getIndirectPassengers()) {
/*  516 */         if (entity.getUUID().equals(attachTo)) {
/*  517 */           startRiding(entity, true, false);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  522 */     if (!isPassenger()) {
/*  523 */       LOGGER.warn("Couldn't reattach entity to player");
/*  524 */       vehicle.discard();
/*  525 */       for (Entity entity : vehicle.getIndirectPassengers()) {
/*  526 */         entity.discard();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void saveEnderPearls(ValueOutput playerOutput) {
/*  532 */     if (!this.enderPearls.isEmpty()) {
/*  533 */       ValueOutput.ValueOutputList pearlsOutput = playerOutput.childrenList("ender_pearls");
/*  534 */       for (ThrownEnderpearl enderPearl : this.enderPearls) {
/*  535 */         if (enderPearl.isRemoved()) {
/*  536 */           LOGGER.warn("Trying to save removed ender pearl, skipping");
/*      */           continue;
/*      */         } 
/*  539 */         ValueOutput pearlTag = pearlsOutput.addChild();
/*  540 */         enderPearl.save(pearlTag);
/*  541 */         pearlTag.store("ender_pearl_dimension", Level.RESOURCE_KEY_CODEC, enderPearl.level().dimension());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  547 */   public void loadAndSpawnEnderPearls(ValueInput playerInput) { playerInput.childrenListOrEmpty("ender_pearls").forEach(this::loadAndSpawnEnderPearl); }
/*      */ 
/*      */   
/*      */   private void loadAndSpawnEnderPearl(ValueInput pearlInput) {
/*  551 */     Optional<ResourceKey<Level>> pearlLevelKey = pearlInput.read("ender_pearl_dimension", Level.RESOURCE_KEY_CODEC);
/*  552 */     if (pearlLevelKey.isEmpty()) {
/*      */       return;
/*      */     }
/*  555 */     ServerLevel pearlLevel = level().getServer().getLevel((ResourceKey)pearlLevelKey.get());
/*  556 */     if (pearlLevel != null) {
/*  557 */       Entity pearl = EntityType.loadEntityRecursive(pearlInput, pearlLevel, EntitySpawnReason.LOAD, entity -> {
/*  558 */             if (!pearlLevel.addWithUUID(entity)) {
/*  559 */               return null;
/*      */             }
/*  561 */             return entity;
/*      */           });
/*  563 */       if (pearl != null) {
/*  564 */         placeEnderPearlTicket(pearlLevel, pearl.chunkPosition());
/*      */       } else {
/*  566 */         LOGGER.warn("Failed to spawn player ender pearl in level ({}), skipping", pearlLevelKey.get());
/*      */       } 
/*      */     } else {
/*  569 */       LOGGER.warn("Trying to load ender pearl without level ({}) being loaded, skipping", pearlLevelKey.get());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setExperiencePoints(int amount) {
/*  574 */     float limit = getXpNeededForNextLevel();
/*  575 */     float max = (limit - 1.0F) / limit;
/*  576 */     float experiencePointsToSet = Mth.clamp(amount / limit, 0.0F, max);
/*  577 */     if (experiencePointsToSet == this.experienceProgress) {
/*      */       return;
/*      */     }
/*  580 */     this.experienceProgress = experiencePointsToSet;
/*  581 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void setExperienceLevels(int amount) {
/*  585 */     if (amount == this.experienceLevel) {
/*      */       return;
/*      */     }
/*  588 */     this.experienceLevel = amount;
/*  589 */     this.lastSentExp = -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void giveExperienceLevels(int amount) {
/*  594 */     if (amount == 0) {
/*      */       return;
/*      */     }
/*  597 */     super.giveExperienceLevels(amount);
/*  598 */     this.lastSentExp = -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnchantmentPerformed(ItemStack itemStack, int enchantmentCost) {
/*  603 */     super.onEnchantmentPerformed(itemStack, enchantmentCost);
/*  604 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   private void initMenu(AbstractContainerMenu container) {
/*  608 */     container.addSlotListener(this.containerListener);
/*  609 */     container.setSynchronizer(this.containerSynchronizer);
/*      */   }
/*      */ 
/*      */   
/*  613 */   public void initInventoryMenu() { initMenu(this.inventoryMenu); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onEnterCombat() {
/*  618 */     super.onEnterCombat();
/*      */     
/*  620 */     this.connection.send(ClientboundPlayerCombatEnterPacket.INSTANCE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLeaveCombat() {
/*  625 */     super.onLeaveCombat();
/*      */     
/*  627 */     this.connection.send(new ClientboundPlayerCombatEndPacket(getCombatTracker()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  632 */   public void onInsideBlock(BlockState state) { CriteriaTriggers.ENTER_BLOCK.trigger(this, state); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  637 */   protected ItemCooldowns createItemCooldowns() { return new ServerItemCooldowns(this); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void tick() {
/*  642 */     this.connection.tickClientLoadTimeout();
/*      */     
/*  644 */     this.gameMode.tick();
/*      */     
/*  646 */     this.wardenSpawnTracker.tick();
/*      */     
/*  648 */     if (this.invulnerableTime > 0) {
/*  649 */       this.invulnerableTime--;
/*      */     }
/*  651 */     this.containerMenu.broadcastChanges();
/*      */     
/*  653 */     if (!this.containerMenu.stillValid(this)) {
/*  654 */       closeContainer();
/*  655 */       this.containerMenu = this.inventoryMenu;
/*      */     } 
/*      */     
/*  658 */     Entity camera = getCamera();
/*  659 */     if (camera != this) {
/*  660 */       if (camera.isAlive()) {
/*      */         
/*  662 */         absSnapTo(camera.getX(), camera.getY(), camera.getZ(), camera.getYRot(), camera.getXRot());
/*  663 */         level().getChunkSource().move(this);
/*  664 */         if (wantsToStopRiding())
/*      */         {
/*  666 */           setCamera(this);
/*      */         }
/*      */       } else {
/*  669 */         setCamera(this);
/*      */       } 
/*      */     }
/*      */     
/*  673 */     CriteriaTriggers.TICK.trigger(this);
/*  674 */     if (this.levitationStartPos != null) {
/*  675 */       CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.tickCount - this.levitationStartTime);
/*      */     }
/*      */     
/*  678 */     trackStartFallingPosition();
/*  679 */     trackEnteredOrExitedLavaOnVehicle();
/*  680 */     updatePlayerAttributes();
/*      */     
/*  682 */     this.advancements.flushDirty(this, true);
/*      */   }
/*      */   
/*      */   private void updatePlayerAttributes() {
/*  686 */     AttributeInstance blockInteractionRange = getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
/*  687 */     if (blockInteractionRange != null) {
/*  688 */       if (isCreative()) {
/*  689 */         blockInteractionRange.addOrUpdateTransientModifier(CREATIVE_BLOCK_INTERACTION_RANGE_MODIFIER);
/*      */       } else {
/*  691 */         blockInteractionRange.removeModifier(CREATIVE_BLOCK_INTERACTION_RANGE_MODIFIER);
/*      */       } 
/*      */     }
/*  694 */     AttributeInstance entityInteractionRange = getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
/*  695 */     if (entityInteractionRange != null) {
/*  696 */       if (isCreative()) {
/*  697 */         entityInteractionRange.addOrUpdateTransientModifier(CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER);
/*      */       } else {
/*  699 */         entityInteractionRange.removeModifier(CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER);
/*      */       } 
/*      */     }
/*  702 */     AttributeInstance waypointTransmitRange = getAttribute(Attributes.WAYPOINT_TRANSMIT_RANGE);
/*  703 */     if (waypointTransmitRange != null) {
/*  704 */       if (isCrouching()) {
/*  705 */         waypointTransmitRange.addOrUpdateTransientModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
/*      */       } else {
/*  707 */         waypointTransmitRange.removeModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void doTick() {
/*      */     try {
/*  714 */       if (!isSpectator() || !touchingUnloadedChunk()) {
/*  715 */         super.tick();
/*  716 */         if (!this.containerMenu.stillValid(this)) {
/*  717 */           closeContainer();
/*  718 */           this.containerMenu = this.inventoryMenu;
/*      */         } 
/*      */         
/*  721 */         this.foodData.tick(this);
/*  722 */         awardStat(Stats.PLAY_TIME);
/*  723 */         awardStat(Stats.TOTAL_WORLD_TIME);
/*  724 */         if (isAlive()) {
/*  725 */           awardStat(Stats.TIME_SINCE_DEATH);
/*      */         }
/*  727 */         if (isDiscrete()) {
/*  728 */           awardStat(Stats.CROUCH_TIME);
/*      */         }
/*  730 */         if (!isSleeping()) {
/*  731 */           awardStat(Stats.TIME_SINCE_REST);
/*      */         }
/*      */       } 
/*      */       
/*  735 */       for (int i = 0; i < getInventory().getContainerSize(); i++) {
/*  736 */         ItemStack itemStack = getInventory().getItem(i);
/*  737 */         if (!itemStack.isEmpty()) {
/*  738 */           synchronizeSpecialItemUpdates(itemStack);
/*      */         }
/*      */       } 
/*      */       
/*  742 */       if (getHealth() != this.lastSentHealth || this.lastSentFood != this.foodData.getFoodLevel() || ((this.foodData.getSaturationLevel() == 0.0F)) != this.lastFoodSaturationZero) {
/*  743 */         this.connection.send(new ClientboundSetHealthPacket(getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
/*  744 */         this.lastSentHealth = getHealth();
/*  745 */         this.lastSentFood = this.foodData.getFoodLevel();
/*  746 */         this.lastFoodSaturationZero = (this.foodData.getSaturationLevel() == 0.0F);
/*      */       } 
/*      */       
/*  749 */       if (getHealth() + getAbsorptionAmount() != this.lastRecordedHealthAndAbsorption) {
/*  750 */         this.lastRecordedHealthAndAbsorption = getHealth() + getAbsorptionAmount();
/*  751 */         updateScoreForCriteria(ObjectiveCriteria.HEALTH, Mth.ceil(this.lastRecordedHealthAndAbsorption));
/*      */       } 
/*      */       
/*  754 */       if (this.foodData.getFoodLevel() != this.lastRecordedFoodLevel) {
/*  755 */         this.lastRecordedFoodLevel = this.foodData.getFoodLevel();
/*  756 */         updateScoreForCriteria(ObjectiveCriteria.FOOD, Mth.ceil(this.lastRecordedFoodLevel));
/*      */       } 
/*      */       
/*  759 */       if (getAirSupply() != this.lastRecordedAirLevel) {
/*  760 */         this.lastRecordedAirLevel = getAirSupply();
/*  761 */         updateScoreForCriteria(ObjectiveCriteria.AIR, Mth.ceil(this.lastRecordedAirLevel));
/*      */       } 
/*      */       
/*  764 */       if (getArmorValue() != this.lastRecordedArmor) {
/*  765 */         this.lastRecordedArmor = getArmorValue();
/*  766 */         updateScoreForCriteria(ObjectiveCriteria.ARMOR, Mth.ceil(this.lastRecordedArmor));
/*      */       } 
/*      */       
/*  769 */       if (this.totalExperience != this.lastRecordedExperience) {
/*  770 */         this.lastRecordedExperience = this.totalExperience;
/*  771 */         updateScoreForCriteria(ObjectiveCriteria.EXPERIENCE, Mth.ceil(this.lastRecordedExperience));
/*      */       } 
/*      */       
/*  774 */       if (this.experienceLevel != this.lastRecordedLevel) {
/*  775 */         this.lastRecordedLevel = this.experienceLevel;
/*  776 */         updateScoreForCriteria(ObjectiveCriteria.LEVEL, Mth.ceil(this.lastRecordedLevel));
/*      */       } 
/*      */       
/*  779 */       if (this.totalExperience != this.lastSentExp) {
/*  780 */         this.lastSentExp = this.totalExperience;
/*  781 */         this.connection.send(new ClientboundSetExperiencePacket(this.experienceProgress, this.totalExperience, this.experienceLevel));
/*      */       } 
/*      */       
/*  784 */       if (this.tickCount % 20 == 0) {
/*  785 */         CriteriaTriggers.LOCATION.trigger(this);
/*      */       }
/*  787 */     } catch (Throwable t) {
/*  788 */       CrashReport report = CrashReport.forThrowable(t, "Ticking player");
/*  789 */       CrashReportCategory category = report.addCategory("Player being ticked");
/*      */       
/*  791 */       fillCrashReportCategory(category);
/*      */       
/*  793 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void synchronizeSpecialItemUpdates(ItemStack itemStack) {
/*  798 */     MapId mapId = (MapId)itemStack.get(DataComponents.MAP_ID);
/*  799 */     MapItemSavedData data = MapItem.getSavedData(mapId, level());
/*  800 */     if (data != null) {
/*  801 */       Packet<?> packet = data.getUpdatePacket(mapId, this);
/*  802 */       if (packet != null) {
/*  803 */         this.connection.send(packet);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void tickRegeneration() {
/*  810 */     if (level().getDifficulty() == Difficulty.PEACEFUL && ((Boolean)level().getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION)).booleanValue()) {
/*  811 */       if (this.tickCount % 20 == 0) {
/*  812 */         if (getHealth() < getMaxHealth()) {
/*  813 */           heal(1.0F);
/*      */         }
/*      */         
/*  816 */         float saturation = this.foodData.getSaturationLevel();
/*  817 */         if (saturation < 20.0F) {
/*  818 */           this.foodData.setSaturation(saturation + 1.0F);
/*      */         }
/*      */       } 
/*  821 */       if (this.tickCount % 10 == 0 && 
/*  822 */         this.foodData.needsFood()) {
/*  823 */         this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleShoulderEntities() {
/*  831 */     playShoulderEntityAmbientSound(getShoulderEntityLeft());
/*  832 */     playShoulderEntityAmbientSound(getShoulderEntityRight());
/*  833 */     if (this.fallDistance > 0.5D || isInWater() || (getAbilities()).flying || isSleeping() || this.isInPowderSnow) {
/*  834 */       removeEntitiesOnShoulder();
/*      */     }
/*      */   }
/*      */   
/*      */   private void playShoulderEntityAmbientSound(CompoundTag shoulderEntityTag) {
/*  839 */     if (shoulderEntityTag.isEmpty() || shoulderEntityTag.getBooleanOr("Silent", false)) {
/*      */       return;
/*      */     }
/*      */     
/*  843 */     if (this.random.nextInt(200) == 0) {
/*  844 */       EntityType<?> entityType = (EntityType)shoulderEntityTag.read("id", EntityType.CODEC).orElse(null);
/*  845 */       if (entityType == EntityType.PARROT && 
/*  846 */         !Parrot.imitateNearbyMobs(level(), this)) {
/*  847 */         level().playSound(null, getX(), getY(), getZ(), Parrot.getAmbient(level(), this.random), getSoundSource(), 1.0F, Parrot.getPitch(this.random));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setEntityOnShoulder(CompoundTag entityTag) {
/*  854 */     if (isPassenger() || !onGround() || isInWater() || this.isInPowderSnow) {
/*  855 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  859 */     if (getShoulderEntityLeft().isEmpty()) {
/*  860 */       setShoulderEntityLeft(entityTag);
/*  861 */       this.timeEntitySatOnShoulder = level().getGameTime();
/*  862 */       return true;
/*  863 */     }  if (getShoulderEntityRight().isEmpty()) {
/*  864 */       setShoulderEntityRight(entityTag);
/*  865 */       this.timeEntitySatOnShoulder = level().getGameTime();
/*  866 */       return true;
/*      */     } 
/*      */     
/*  869 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void removeEntitiesOnShoulder() {
/*  874 */     if (this.timeEntitySatOnShoulder + 20L < level().getGameTime()) {
/*  875 */       respawnEntityOnShoulder(getShoulderEntityLeft());
/*  876 */       setShoulderEntityLeft(new CompoundTag());
/*  877 */       respawnEntityOnShoulder(getShoulderEntityRight());
/*  878 */       setShoulderEntityRight(new CompoundTag());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void respawnEntityOnShoulder(CompoundTag tag) {
/*  883 */     ServerLevel serverLevel1 = level(); if (serverLevel1 instanceof ServerLevel) { ServerLevel serverLevel = serverLevel1; if (!tag.isEmpty()) {
/*  884 */         ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/*  885 */         try { EntityType.create(TagValueInput.create(reporter.forChild(() -> ".shoulder"), serverLevel.registryAccess(), tag), serverLevel, EntitySpawnReason.LOAD)
/*  886 */             .ifPresent(entity -> {
/*  887 */                 if (entity instanceof TamableAnimal) { TamableAnimal tamed = (TamableAnimal)entity;
/*  888 */                   tamed.setOwner(this); }
/*      */ 
/*      */                 
/*  891 */                 entity.setPos(getX(), getY() + 0.699999988079071D, getZ());
/*      */                 
/*  893 */                 serverLevel.addWithUUID(entity);
/*      */               });
/*  895 */           reporter.close(); }
/*      */         catch (Throwable throwable) { try { reporter.close(); }
/*      */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */            throw throwable; }
/*      */       
/*      */       }  }
/*  901 */      } public void resetFallDistance() { if (getHealth() > 0.0F && this.startingToFallPosition != null) {
/*  902 */       CriteriaTriggers.FALL_FROM_HEIGHT.trigger(this, this.startingToFallPosition);
/*      */     }
/*  904 */     this.startingToFallPosition = null;
/*  905 */     super.resetFallDistance(); }
/*      */ 
/*      */   
/*      */   public void trackStartFallingPosition() {
/*  909 */     if (this.fallDistance > 0.0D && this.startingToFallPosition == null) {
/*  910 */       this.startingToFallPosition = position();
/*      */       
/*  912 */       if (this.currentImpulseImpactPos != null && this.currentImpulseImpactPos.y <= this.startingToFallPosition.y) {
/*  913 */         CriteriaTriggers.FALL_AFTER_EXPLOSION.trigger(this, this.currentImpulseImpactPos, this.currentExplosionCause);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void trackEnteredOrExitedLavaOnVehicle() {
/*  919 */     if (getVehicle() != null && getVehicle().isInLava()) {
/*  920 */       if (this.enteredLavaOnVehiclePosition == null) {
/*  921 */         this.enteredLavaOnVehiclePosition = position();
/*      */       } else {
/*  923 */         CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.trigger(this, this.enteredLavaOnVehiclePosition);
/*      */       } 
/*      */     }
/*  926 */     if (this.enteredLavaOnVehiclePosition != null && (getVehicle() == null || !getVehicle().isInLava())) {
/*  927 */       this.enteredLavaOnVehiclePosition = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  932 */   private void updateScoreForCriteria(ObjectiveCriteria criteria, int value) { level().getScoreboard().forAllObjectives(criteria, this, score -> score.set(value)); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void die(DamageSource source) {
/*  937 */     gameEvent(GameEvent.ENTITY_DIE);
/*  938 */     boolean showDeathMessage = ((Boolean)level().getGameRules().get(GameRules.SHOW_DEATH_MESSAGES)).booleanValue();
/*      */     
/*  940 */     if (showDeathMessage) {
/*  941 */       Component deathMessage = getCombatTracker().getDeathMessage();
/*  942 */       this.connection.send(new ClientboundPlayerCombatKillPacket(getId(), deathMessage), 
/*  943 */           PacketSendListener.exceptionallySend(() -> {
/*  944 */               int truncatedMessageSize = 256;
/*  945 */               String truncatedDeathMessage = deathMessage.getString(256);
/*  946 */               MutableComponent mutableComponent1 = Component.translatable("death.attack.message_too_long", new Object[] { Component.literal(truncatedDeathMessage).withStyle(ChatFormatting.YELLOW) });
/*  947 */               MutableComponent mutableComponent2 = Component.translatable("death.attack.even_more_magic", new Object[] { getDisplayName() }).withStyle(());
/*  948 */               return new ClientboundPlayerCombatKillPacket(getId(), mutableComponent2);
/*      */             }));
/*      */       
/*  951 */       PlayerTeam playerTeam = getTeam();
/*  952 */       if (playerTeam == null || playerTeam.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
/*  953 */         this.server.getPlayerList().broadcastSystemMessage(deathMessage, false);
/*  954 */       } else if (playerTeam.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
/*  955 */         this.server.getPlayerList().broadcastSystemToTeam(this, deathMessage);
/*  956 */       } else if (playerTeam.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
/*  957 */         this.server.getPlayerList().broadcastSystemToAllExceptTeam(this, deathMessage);
/*      */       } 
/*      */     } else {
/*  960 */       this.connection.send(new ClientboundPlayerCombatKillPacket(getId(), CommonComponents.EMPTY));
/*      */     } 
/*  962 */     removeEntitiesOnShoulder();
/*  963 */     if (((Boolean)level().getGameRules().get(GameRules.FORGIVE_DEAD_PLAYERS)).booleanValue()) {
/*  964 */       tellNeutralMobsThatIDied();
/*      */     }
/*      */     
/*  967 */     if (!isSpectator()) {
/*  968 */       dropAllDeathLoot(level(), source);
/*      */     }
/*      */     
/*  971 */     level().getScoreboard().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, this, ScoreAccess::increment);
/*      */     
/*  973 */     LivingEntity killer = getKillCredit();
/*  974 */     if (killer != null) {
/*  975 */       awardStat(Stats.ENTITY_KILLED_BY.get(killer.getType()));
/*  976 */       killer.awardKillScore(this, source);
/*      */       
/*  978 */       createWitherRose(killer);
/*      */     } 
/*      */     
/*  981 */     level().broadcastEntityEvent(this, (byte)3);
/*      */     
/*  983 */     awardStat(Stats.DEATHS);
/*  984 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
/*  985 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/*  986 */     clearFire();
/*  987 */     setTicksFrozen(0);
/*  988 */     setSharedFlagOnFire(false);
/*  989 */     getCombatTracker().recheckStatus();
/*  990 */     setLastDeathLocation(Optional.of(GlobalPos.of(level().dimension(), blockPosition())));
/*  991 */     this.connection.markClientUnloadedAfterDeath();
/*      */   }
/*      */   
/*      */   private void tellNeutralMobsThatIDied() {
/*  995 */     AABB aabb = (new AABB(blockPosition())).inflate(32.0D, 10.0D, 32.0D);
/*  996 */     level().getEntitiesOfClass(Mob.class, aabb, EntitySelector.NO_SPECTATORS).stream()
/*  997 */       .filter(mob -> mob instanceof NeutralMob)
/*  998 */       .forEach(mob -> ((NeutralMob)mob).playerDied(level(), this));
/*      */   }
/*      */ 
/*      */   
/*      */   public void awardKillScore(Entity victim, DamageSource killingBlow) {
/* 1003 */     if (victim == this) {
/*      */       return;
/*      */     }
/* 1006 */     super.awardKillScore(victim, killingBlow);
/*      */     
/* 1008 */     ServerScoreboard serverScoreboard = level().getScoreboard();
/* 1009 */     serverScoreboard.forAllObjectives(ObjectiveCriteria.KILL_COUNT_ALL, this, ScoreAccess::increment);
/*      */     
/* 1011 */     if (victim instanceof Player) {
/* 1012 */       awardStat(Stats.PLAYER_KILLS);
/* 1013 */       serverScoreboard.forAllObjectives(ObjectiveCriteria.KILL_COUNT_PLAYERS, this, ScoreAccess::increment);
/*      */     } else {
/* 1015 */       awardStat(Stats.MOB_KILLS);
/*      */     } 
/*      */     
/* 1018 */     handleTeamKill(this, victim, ObjectiveCriteria.TEAM_KILL);
/* 1019 */     handleTeamKill(victim, this, ObjectiveCriteria.KILLED_BY_TEAM);
/*      */     
/* 1021 */     CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, victim, killingBlow);
/*      */   }
/*      */   
/*      */   private void handleTeamKill(ScoreHolder source, ScoreHolder target, ObjectiveCriteria[] criteriaByTeam) {
/* 1025 */     ServerScoreboard serverScoreboard = level().getScoreboard();
/* 1026 */     PlayerTeam ownTeam = serverScoreboard.getPlayersTeam(target.getScoreboardName());
/* 1027 */     if (ownTeam != null) {
/* 1028 */       int color = ownTeam.getColor().getId();
/* 1029 */       if (color >= 0 && color < criteriaByTeam.length) {
/* 1030 */         serverScoreboard.forAllObjectives(criteriaByTeam[color], source, ScoreAccess::increment);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 1037 */     if (isInvulnerableTo(level, source)) {
/* 1038 */       return false;
/*      */     }
/* 1040 */     Entity entity = source.getEntity();
/* 1041 */     if (entity instanceof Player) { Player player = (Player)entity; if (!canHarmPlayer(player))
/* 1042 */         return false;  }
/*      */     
/* 1044 */     if (entity instanceof AbstractArrow) { AbstractArrow arrow = (AbstractArrow)entity;
/* 1045 */       Entity currentOwner = arrow.getOwner();
/* 1046 */       if (currentOwner instanceof Player) { Player player = (Player)currentOwner; if (!canHarmPlayer(player))
/* 1047 */           return false;  }
/*      */        }
/*      */     
/* 1050 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canHarmPlayer(Player target) {
/* 1055 */     if (!isPvpAllowed()) {
/* 1056 */       return false;
/*      */     }
/* 1058 */     return super.canHarmPlayer(target);
/*      */   }
/*      */ 
/*      */   
/* 1062 */   private boolean isPvpAllowed() { return level().isPvpAllowed(); }
/*      */ 
/*      */   
/*      */   public TeleportTransition findRespawnPositionAndUseSpawnBlock(boolean consumeSpawnBlock, TeleportTransition.PostTeleportTransition postTeleportTransition) {
/* 1066 */     RespawnConfig respawnConfig = getRespawnConfig();
/* 1067 */     ServerLevel respawnLevel = this.server.getLevel(RespawnConfig.getDimensionOrDefault(respawnConfig));
/*      */     
/* 1069 */     if (respawnLevel != null && respawnConfig != null) {
/* 1070 */       Optional<RespawnPosAngle> respawn = findRespawnAndUseSpawnBlock(respawnLevel, respawnConfig, consumeSpawnBlock);
/* 1071 */       if (respawn.isPresent()) {
/* 1072 */         RespawnPosAngle respawnPosAngle = (RespawnPosAngle)respawn.get();
/* 1073 */         return new TeleportTransition(respawnLevel, respawnPosAngle.position(), Vec3.ZERO, respawnPosAngle.yaw(), respawnPosAngle.pitch(), postTeleportTransition);
/*      */       } 
/* 1075 */       return TeleportTransition.missingRespawnBlock(this, postTeleportTransition);
/*      */     } 
/* 1077 */     return TeleportTransition.createDefault(this, postTeleportTransition);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1082 */   public boolean isReceivingWaypoints() { return (getAttributeValue(Attributes.WAYPOINT_RECEIVE_RANGE) > 0.0D); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onAttributeUpdated(Holder<Attribute> attribute) {
/* 1087 */     if (attribute.is(Attributes.WAYPOINT_RECEIVE_RANGE)) {
/* 1088 */       ServerWaypointManager waypointManager = level().getWaypointManager();
/* 1089 */       if (getAttributes().getValue(attribute) > 0.0D) {
/* 1090 */         waypointManager.addPlayer(this);
/*      */       } else {
/* 1092 */         waypointManager.removePlayer(this);
/*      */       } 
/*      */     } 
/*      */     
/* 1096 */     super.onAttributeUpdated(attribute);
/*      */   }
/*      */   private static final class RespawnPosAngle extends Record { private final Vec3 position; private final float yaw; private final float pitch;
/* 1099 */     private RespawnPosAngle(Vec3 position, float yaw, float pitch) { this.position = position; this.yaw = yaw; this.pitch = pitch; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1099	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1099	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1099	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/* 1099 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 position() { return this.position; } public float yaw() { return this.yaw; } public float pitch() { return this.pitch; }
/*      */     
/* 1101 */     public static RespawnPosAngle of(Vec3 position, BlockPos lookAtBlockPos, float pitch) { return new RespawnPosAngle(position, calculateLookAtYaw(position, lookAtBlockPos), pitch); }
/*      */ 
/*      */     
/*      */     private static float calculateLookAtYaw(Vec3 position, BlockPos lookAtBlockPos) {
/* 1105 */       Vec3 lookDirection = Vec3.atBottomCenterOf(lookAtBlockPos).subtract(position).normalize();
/* 1106 */       return (float)Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 57.2957763671875D - 90.0D);
/*      */     } }
/*      */ 
/*      */   
/*      */   private static Optional<RespawnPosAngle> findRespawnAndUseSpawnBlock(ServerLevel level, RespawnConfig respawnConfig, boolean consumeSpawnBlock) {
/* 1111 */     LevelData.RespawnData respawnData = respawnConfig.respawnData;
/* 1112 */     BlockPos pos = respawnData.pos();
/* 1113 */     float yaw = respawnData.yaw();
/* 1114 */     float pitch = respawnData.pitch();
/* 1115 */     boolean forced = respawnConfig.forced;
/*      */     
/* 1117 */     BlockState blockState = level.getBlockState(pos);
/* 1118 */     Block block = blockState.getBlock();
/*      */     
/* 1120 */     if (block instanceof RespawnAnchorBlock && (forced || ((Integer)blockState.getValue(RespawnAnchorBlock.CHARGE)).intValue() > 0) && RespawnAnchorBlock.canSetSpawn(level, pos)) {
/* 1121 */       Optional<Vec3> standUpPosition = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, level, pos);
/* 1122 */       if (!forced && consumeSpawnBlock && standUpPosition.isPresent()) {
/* 1123 */         level.setBlock(pos, (BlockState)blockState.setValue(RespawnAnchorBlock.CHARGE, Integer.valueOf(((Integer)blockState.getValue(RespawnAnchorBlock.CHARGE)).intValue() - 1)), 3);
/*      */       }
/* 1125 */       return standUpPosition.map(p -> RespawnPosAngle.of(p, pos, 0.0F));
/* 1126 */     }  if (block instanceof BedBlock && ((BedRule)level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos)).canSetSpawn(level))
/*      */     {
/* 1128 */       return 
/* 1129 */         BedBlock.findStandUpPosition(EntityType.PLAYER, level, pos, (Direction)blockState.getValue(BedBlock.FACING), yaw)
/* 1130 */         .map(p -> RespawnPosAngle.of(p, pos, 0.0F));
/*      */     }
/*      */     
/* 1133 */     if (!forced) {
/* 1134 */       return Optional.empty();
/*      */     }
/*      */     
/* 1137 */     boolean freeBottom = block.isPossibleToRespawnInThis(blockState);
/* 1138 */     BlockState topState = level.getBlockState(pos.above());
/* 1139 */     boolean freeTop = topState.getBlock().isPossibleToRespawnInThis(topState);
/*      */     
/* 1141 */     if (freeBottom && freeTop) {
/* 1142 */       return Optional.of(new RespawnPosAngle(new Vec3(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D), yaw, pitch));
/*      */     }
/*      */     
/* 1145 */     return Optional.empty();
/*      */   }
/*      */ 
/*      */   
/*      */   public void showEndCredits() {
/* 1150 */     unRide();
/* 1151 */     level().removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
/* 1152 */     if (!this.wonGame) {
/* 1153 */       this.wonGame = true;
/* 1154 */       this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0.0F));
/* 1155 */       this.seenCredits = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerPlayer teleport(TeleportTransition transition) {
/* 1161 */     if (isRemoved()) {
/* 1162 */       return null;
/*      */     }
/*      */     
/* 1165 */     if (transition.missingRespawnBlock()) {
/* 1166 */       this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
/*      */     }
/*      */     
/* 1169 */     ServerLevel newLevel = transition.newLevel();
/* 1170 */     ServerLevel oldLevel = level();
/* 1171 */     ResourceKey<Level> lastDimension = oldLevel.dimension();
/*      */     
/* 1173 */     if (!transition.asPassenger()) {
/* 1174 */       removeVehicle();
/*      */     }
/*      */     
/* 1177 */     if (newLevel.dimension() == lastDimension) {
/* 1178 */       this.connection.teleport(PositionMoveRotation.of(transition), transition.relatives());
/* 1179 */       this.connection.resetPosition();
/* 1180 */       transition.postTeleportTransition().onTransition(this);
/* 1181 */       return this;
/*      */     } 
/*      */     
/* 1184 */     this.isChangingDimension = true;
/* 1185 */     LevelData levelData = newLevel.getLevelData();
/*      */     
/* 1187 */     this.connection.send(new ClientboundRespawnPacket(
/* 1188 */           createCommonSpawnInfo(newLevel), (byte)3));
/*      */ 
/*      */     
/* 1191 */     this.connection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
/* 1192 */     PlayerList playerList = this.server.getPlayerList();
/*      */     
/* 1194 */     playerList.sendPlayerPermissionLevel(this);
/*      */     
/* 1196 */     oldLevel.removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
/*      */     
/* 1198 */     unsetRemoved();
/*      */     
/* 1200 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/* 1202 */     profiler.push("moving");
/* 1203 */     if (lastDimension == Level.OVERWORLD && newLevel.dimension() == Level.NETHER) {
/* 1204 */       this.enteredNetherPosition = position();
/*      */     }
/*      */     
/* 1207 */     profiler.pop();
/*      */     
/* 1209 */     profiler.push("placing");
/*      */     
/* 1211 */     setServerLevel(newLevel);
/*      */     
/* 1213 */     this.connection.teleport(PositionMoveRotation.of(transition), transition.relatives());
/* 1214 */     this.connection.resetPosition();
/*      */     
/* 1216 */     newLevel.addDuringTeleport(this);
/*      */     
/* 1218 */     profiler.pop();
/*      */     
/* 1220 */     triggerDimensionChangeTriggers(oldLevel);
/*      */     
/* 1222 */     stopUsingItem();
/*      */     
/* 1224 */     this.connection.send(new ClientboundPlayerAbilitiesPacket(getAbilities()));
/* 1225 */     playerList.sendLevelInfo(this, newLevel);
/* 1226 */     playerList.sendAllPlayerInfo(this);
/* 1227 */     playerList.sendActivePlayerEffects(this);
/* 1228 */     transition.postTeleportTransition().onTransition(this);
/*      */     
/* 1230 */     this.lastSentExp = -1;
/* 1231 */     this.lastSentHealth = -1.0F;
/* 1232 */     this.lastSentFood = -1;
/*      */     
/* 1234 */     teleportSpectators(transition, oldLevel);
/* 1235 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void forceSetRotation(float yRot, boolean relativeY, float xRot, boolean relativeX) {
/* 1240 */     super.forceSetRotation(yRot, relativeY, xRot, relativeX);
/* 1241 */     this.connection.send(new ClientboundPlayerRotationPacket(yRot, relativeY, xRot, relativeX));
/*      */   }
/*      */   
/*      */   private void triggerDimensionChangeTriggers(ServerLevel oldLevel) {
/* 1245 */     ResourceKey<Level> oldKey = oldLevel.dimension();
/* 1246 */     ResourceKey<Level> newKey = level().dimension();
/* 1247 */     CriteriaTriggers.CHANGED_DIMENSION.trigger(this, oldKey, newKey);
/*      */     
/* 1249 */     if (oldKey == Level.NETHER && newKey == Level.OVERWORLD && this.enteredNetherPosition != null) {
/* 1250 */       CriteriaTriggers.NETHER_TRAVEL.trigger(this, this.enteredNetherPosition);
/*      */     }
/* 1252 */     if (newKey != Level.NETHER) {
/* 1253 */       this.enteredNetherPosition = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean broadcastToPlayer(ServerPlayer player) {
/* 1259 */     if (player.isSpectator()) {
/* 1260 */       return (getCamera() == this);
/*      */     }
/*      */     
/* 1263 */     if (isSpectator()) {
/* 1264 */       return false;
/*      */     }
/*      */     
/* 1267 */     return super.broadcastToPlayer(player);
/*      */   }
/*      */ 
/*      */   
/*      */   public void take(Entity entity, int orgCount) {
/* 1272 */     super.take(entity, orgCount);
/* 1273 */     this.containerMenu.broadcastChanges();
/*      */   }
/*      */ 
/*      */   
/*      */   public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos pos) {
/* 1278 */     Direction direction = (Direction)level().getBlockState(pos).getValue(HorizontalDirectionalBlock.FACING);
/* 1279 */     if (isSleeping() || !isAlive()) {
/* 1280 */       return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
/*      */     }
/*      */     
/* 1283 */     BedRule rule = (BedRule)level().environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos);
/* 1284 */     boolean canSleep = rule.canSleep(level());
/* 1285 */     boolean canSetSpawn = rule.canSetSpawn(level());
/*      */     
/* 1287 */     if (!canSetSpawn && !canSleep) {
/* 1288 */       return Either.left(rule.asProblem());
/*      */     }
/*      */     
/* 1291 */     if (!bedInRange(pos, direction)) {
/* 1292 */       return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY);
/*      */     }
/*      */     
/* 1295 */     if (bedBlocked(pos, direction)) {
/* 1296 */       return Either.left(Player.BedSleepingProblem.OBSTRUCTED);
/*      */     }
/*      */ 
/*      */     
/* 1300 */     if (canSetSpawn) {
/* 1301 */       setRespawnPosition(new RespawnConfig(LevelData.RespawnData.of(level().dimension(), pos, getYRot(), getXRot()), false), true);
/*      */     }
/*      */     
/* 1304 */     if (!canSleep) {
/* 1305 */       return Either.left(rule.asProblem());
/*      */     }
/*      */     
/* 1308 */     if (!isCreative()) {
/* 1309 */       double hRange = 8.0D;
/* 1310 */       double vRange = 5.0D;
/* 1311 */       Vec3 bedCenter = Vec3.atBottomCenterOf(pos);
/* 1312 */       List<Monster> monsters = level().getEntitiesOfClass(Monster.class, new AABB(bedCenter.x() - 8.0D, bedCenter.y() - 5.0D, bedCenter.z() - 8.0D, bedCenter.x() + 8.0D, bedCenter.y() + 5.0D, bedCenter.z() + 8.0D), monster -> monster.isPreventingPlayerRest(level(), this));
/* 1313 */       if (!monsters.isEmpty()) {
/* 1314 */         return Either.left(Player.BedSleepingProblem.NOT_SAFE);
/*      */       }
/*      */     } 
/*      */     
/* 1318 */     Either<Player.BedSleepingProblem, Unit> result = super.startSleepInBed(pos).ifRight(unit -> {
/* 1319 */           awardStat(Stats.SLEEP_IN_BED);
/* 1320 */           CriteriaTriggers.SLEPT_IN_BED.trigger(this);
/*      */         });
/* 1322 */     if (!level().canSleepThroughNights()) {
/* 1323 */       displayClientMessage(Component.translatable("sleep.not_possible"), true);
/*      */     }
/*      */     
/* 1326 */     level().updateSleepingPlayerList();
/* 1327 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void startSleeping(BlockPos bedPosition) {
/* 1332 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/* 1333 */     super.startSleeping(bedPosition);
/*      */   }
/*      */ 
/*      */   
/* 1337 */   private boolean bedInRange(BlockPos pos, Direction direction) { return (isReachableBedBlock(pos) || isReachableBedBlock(pos.relative(direction.getOpposite()))); }
/*      */ 
/*      */   
/*      */   private boolean isReachableBedBlock(BlockPos bedBlockPos) {
/* 1341 */     Vec3 bedBlockCenter = Vec3.atBottomCenterOf(bedBlockPos);
/* 1342 */     return (Math.abs(getX() - bedBlockCenter.x()) <= 3.0D && Math.abs(getY() - bedBlockCenter.y()) <= 2.0D && Math.abs(getZ() - bedBlockCenter.z()) <= 3.0D);
/*      */   }
/*      */   
/*      */   private boolean bedBlocked(BlockPos pos, Direction direction) {
/* 1346 */     BlockPos above = pos.above();
/* 1347 */     return (!freeAt(above) || !freeAt(above.relative(direction.getOpposite())));
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopSleepInBed(boolean forcefulWakeUp, boolean updateLevelList) {
/* 1352 */     if (isSleeping()) {
/* 1353 */       level().getChunkSource().sendToTrackingPlayersAndSelf(this, new ClientboundAnimatePacket(this, 2));
/*      */     }
/* 1355 */     super.stopSleepInBed(forcefulWakeUp, updateLevelList);
/* 1356 */     if (this.connection != null) {
/* 1357 */       this.connection.teleport(getX(), getY(), getZ(), getYRot(), getXRot());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1363 */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) { return (super.isInvulnerableTo(level, source) || (isChangingDimension() && !source.is(DamageTypes.ENDER_PEARL)) || !this.connection.hasClientLoaded()); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onChangedBlock(ServerLevel level, BlockPos pos) {
/* 1368 */     if (!isSpectator()) {
/* 1369 */       super.onChangedBlock(level, pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 1375 */     if (this.spawnExtraParticlesOnFall && onGround && this.fallDistance > 0.0D) {
/* 1376 */       Vec3 centered = pos.getCenter().add(0.0D, 0.5D, 0.0D);
/* 1377 */       int particles = (int)Mth.clamp(50.0D * this.fallDistance, 0.0D, 200.0D);
/*      */       
/* 1379 */       level().sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, onState), centered.x, centered.y, centered.z, particles, 0.30000001192092896D, 0.30000001192092896D, 0.30000001192092896D, 0.15000000596046448D);
/* 1380 */       this.spawnExtraParticlesOnFall = false;
/*      */     } 
/*      */     
/* 1383 */     super.checkFallDamage(ya, onGround, onState, pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onExplosionHit(Entity explosionCausedBy) {
/* 1388 */     super.onExplosionHit(explosionCausedBy);
/* 1389 */     this.currentImpulseImpactPos = position();
/* 1390 */     this.currentExplosionCause = explosionCausedBy;
/* 1391 */     setIgnoreFallDamageFromCurrentImpulse((explosionCausedBy != null && explosionCausedBy.getType() == EntityType.WIND_CHARGE));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void pushEntities() {
/* 1397 */     if (level().tickRateManager().runsNormally()) {
/* 1398 */       super.pushEntities();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void openTextEdit(SignBlockEntity sign, boolean isFrontText) {
/* 1405 */     this.connection.send(new ClientboundBlockUpdatePacket(level(), sign.getBlockPos()));
/* 1406 */     this.connection.send(new ClientboundOpenSignEditorPacket(sign.getBlockPos(), isFrontText));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1411 */   public void openDialog(Holder<Dialog> dialog) { this.connection.send(new ClientboundShowDialogPacket(dialog)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1418 */   private void nextContainerCounter() { this.containerCounter = this.containerCounter % 100 + 1; }
/*      */ 
/*      */ 
/*      */   
/*      */   public OptionalInt openMenu(MenuProvider provider) {
/* 1423 */     if (provider == null) {
/* 1424 */       return OptionalInt.empty();
/*      */     }
/*      */     
/* 1427 */     if (this.containerMenu != this.inventoryMenu) {
/* 1428 */       closeContainer();
/*      */     }
/*      */     
/* 1431 */     nextContainerCounter();
/*      */     
/* 1433 */     AbstractContainerMenu menu = provider.createMenu(this.containerCounter, getInventory(), this);
/* 1434 */     if (menu == null) {
/* 1435 */       if (isSpectator()) {
/* 1436 */         displayClientMessage(Component.translatable("container.spectatorCantOpen").withStyle(ChatFormatting.RED), true);
/*      */       }
/* 1438 */       return OptionalInt.empty();
/*      */     } 
/* 1440 */     this.connection.send(new ClientboundOpenScreenPacket(menu.containerId, menu.getType(), provider.getDisplayName()));
/* 1441 */     initMenu(menu);
/* 1442 */     this.containerMenu = menu;
/* 1443 */     return OptionalInt.of(this.containerCounter);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1448 */   public void sendMerchantOffers(int containerId, MerchantOffers offers, int merchantLevel, int merchantXp, boolean showProgressBar, boolean canRestock) { this.connection.send(new ClientboundMerchantOffersPacket(containerId, offers, merchantLevel, merchantXp, showProgressBar, canRestock)); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void openHorseInventory(AbstractHorse horse, Container container) {
/* 1453 */     if (this.containerMenu != this.inventoryMenu) {
/* 1454 */       closeContainer();
/*      */     }
/* 1456 */     nextContainerCounter();
/* 1457 */     int inventoryColumns = horse.getInventoryColumns();
/* 1458 */     this.connection.send(new ClientboundMountScreenOpenPacket(this.containerCounter, inventoryColumns, horse.getId()));
/* 1459 */     this.containerMenu = new HorseInventoryMenu(this.containerCounter, getInventory(), container, horse, inventoryColumns);
/* 1460 */     initMenu(this.containerMenu);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openNautilusInventory(AbstractNautilus nautilus, Container container) {
/* 1465 */     if (this.containerMenu != this.inventoryMenu) {
/* 1466 */       closeContainer();
/*      */     }
/* 1468 */     nextContainerCounter();
/* 1469 */     int inventoryColumns = nautilus.getInventoryColumns();
/* 1470 */     this.connection.send(new ClientboundMountScreenOpenPacket(this.containerCounter, inventoryColumns, nautilus.getId()));
/* 1471 */     this.containerMenu = new NautilusInventoryMenu(this.containerCounter, getInventory(), container, nautilus, inventoryColumns);
/* 1472 */     initMenu(this.containerMenu);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openItemGui(ItemStack itemStack, InteractionHand hand) {
/* 1477 */     if (itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/*      */ 
/*      */       
/* 1480 */       if (WrittenBookContent.resolveForItem(itemStack, createCommandSourceStack(), this)) {
/* 1481 */         this.containerMenu.broadcastChanges();
/*      */       }
/*      */       
/* 1484 */       this.connection.send(new ClientboundOpenBookPacket(hand));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1490 */   public void openCommandBlock(CommandBlockEntity commandBlock) { this.connection.send(ClientboundBlockEntityDataPacket.create(commandBlock, BlockEntity::saveCustomOnly)); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void closeContainer() {
/* 1495 */     this.connection.send(new ClientboundContainerClosePacket(this.containerMenu.containerId));
/* 1496 */     doCloseContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void doCloseContainer() {
/* 1501 */     this.containerMenu.removed(this);
/*      */     
/* 1503 */     this.inventoryMenu.transferState(this.containerMenu);
/* 1504 */     this.containerMenu = this.inventoryMenu;
/*      */   }
/*      */ 
/*      */   
/*      */   public void rideTick() {
/* 1509 */     double preX = getX();
/* 1510 */     double preY = getY();
/* 1511 */     double preZ = getZ();
/* 1512 */     super.rideTick();
/* 1513 */     checkRidingStatistics(getX() - preX, getY() - preY, getZ() - preZ);
/*      */   }
/*      */   
/*      */   public void checkMovementStatistics(double dx, double dy, double dz) {
/* 1517 */     if (isPassenger() || didNotMove(dx, dy, dz)) {
/*      */       return;
/*      */     }
/*      */     
/* 1521 */     if (isSwimming()) {
/* 1522 */       int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1523 */       if (distance > 0) {
/* 1524 */         awardStat(Stats.SWIM_ONE_CM, distance);
/* 1525 */         causeFoodExhaustion(0.01F * distance * 0.01F);
/*      */       } 
/* 1527 */     } else if (isEyeInFluid(FluidTags.WATER)) {
/* 1528 */       int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1529 */       if (distance > 0) {
/* 1530 */         awardStat(Stats.WALK_UNDER_WATER_ONE_CM, distance);
/* 1531 */         causeFoodExhaustion(0.01F * distance * 0.01F);
/*      */       } 
/* 1533 */     } else if (isInWater()) {
/* 1534 */       int horizontalDistance = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
/* 1535 */       if (horizontalDistance > 0) {
/* 1536 */         awardStat(Stats.WALK_ON_WATER_ONE_CM, horizontalDistance);
/* 1537 */         causeFoodExhaustion(0.01F * horizontalDistance * 0.01F);
/*      */       } 
/* 1539 */     } else if (onClimbable()) {
/* 1540 */       if (dy > 0.0D) {
/* 1541 */         awardStat(Stats.CLIMB_ONE_CM, (int)Math.round(dy * 100.0D));
/*      */       }
/* 1543 */     } else if (onGround()) {
/* 1544 */       int horizontalDistance = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
/* 1545 */       if (horizontalDistance > 0) {
/* 1546 */         if (isSprinting()) {
/* 1547 */           awardStat(Stats.SPRINT_ONE_CM, horizontalDistance);
/* 1548 */           causeFoodExhaustion(0.1F * horizontalDistance * 0.01F);
/* 1549 */         } else if (isCrouching()) {
/* 1550 */           awardStat(Stats.CROUCH_ONE_CM, horizontalDistance);
/* 1551 */           causeFoodExhaustion(0.0F * horizontalDistance * 0.01F);
/*      */         } else {
/* 1553 */           awardStat(Stats.WALK_ONE_CM, horizontalDistance);
/* 1554 */           causeFoodExhaustion(0.0F * horizontalDistance * 0.01F);
/*      */         } 
/*      */       }
/* 1557 */     } else if (isFallFlying()) {
/* 1558 */       int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1559 */       awardStat(Stats.AVIATE_ONE_CM, distance);
/*      */     } else {
/* 1561 */       int horizontalDistance = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
/* 1562 */       if (horizontalDistance > 25) {
/* 1563 */         awardStat(Stats.FLY_ONE_CM, horizontalDistance);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkRidingStatistics(double dx, double dy, double dz) {
/* 1569 */     if (!isPassenger() || didNotMove(dx, dy, dz)) {
/*      */       return;
/*      */     }
/*      */     
/* 1573 */     int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1574 */     Entity vehicle = getVehicle();
/* 1575 */     if (vehicle instanceof net.minecraft.world.entity.vehicle.minecart.AbstractMinecart) {
/* 1576 */       awardStat(Stats.MINECART_ONE_CM, distance);
/* 1577 */     } else if (vehicle instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
/* 1578 */       awardStat(Stats.BOAT_ONE_CM, distance);
/* 1579 */     } else if (vehicle instanceof net.minecraft.world.entity.animal.pig.Pig) {
/* 1580 */       awardStat(Stats.PIG_ONE_CM, distance);
/* 1581 */     } else if (vehicle instanceof AbstractHorse) {
/* 1582 */       awardStat(Stats.HORSE_ONE_CM, distance);
/* 1583 */     } else if (vehicle instanceof net.minecraft.world.entity.monster.Strider) {
/* 1584 */       awardStat(Stats.STRIDER_ONE_CM, distance);
/* 1585 */     } else if (vehicle instanceof net.minecraft.world.entity.animal.happyghast.HappyGhast) {
/* 1586 */       awardStat(Stats.HAPPY_GHAST_ONE_CM, distance);
/* 1587 */     } else if (vehicle instanceof AbstractNautilus) {
/* 1588 */       awardStat(Stats.NAUTILUS_ONE_CM, distance);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1593 */   private static boolean didNotMove(double dx, double dy, double dz) { return (dx == 0.0D && dy == 0.0D && dz == 0.0D); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void awardStat(Stat<?> stat, int count) {
/* 1598 */     this.stats.increment(this, stat, count);
/* 1599 */     level().getScoreboard().forAllObjectives(stat, this, score -> score.add(count));
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetStat(Stat<?> stat) {
/* 1604 */     this.stats.setValue(this, stat, 0);
/* 1605 */     level().getScoreboard().forAllObjectives(stat, this, ScoreAccess::reset);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1610 */   public int awardRecipes(Collection<RecipeHolder<?>> recipes) { return this.recipeBook.addRecipes(recipes, this); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1615 */   public void triggerRecipeCrafted(RecipeHolder<?> recipe, List<ItemStack> itemStacks) { CriteriaTriggers.RECIPE_CRAFTED.trigger(this, recipe.id(), itemStacks); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void awardRecipesByKey(List<ResourceKey<Recipe<?>>> recipeIds) {
/* 1622 */     List<RecipeHolder<?>> recipes = (List)recipeIds.stream().flatMap(id -> this.server.getRecipeManager().byKey(id).stream()).collect(Collectors.toList());
/* 1623 */     awardRecipes(recipes);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1628 */   public int resetRecipes(Collection<RecipeHolder<?>> recipe) { return this.recipeBook.removeRecipes(recipe, this); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void jumpFromGround() {
/* 1633 */     super.jumpFromGround();
/*      */     
/* 1635 */     awardStat(Stats.JUMP);
/* 1636 */     if (isSprinting()) {
/* 1637 */       causeFoodExhaustion(0.2F);
/*      */     } else {
/* 1639 */       causeFoodExhaustion(0.05F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void giveExperiencePoints(int i) {
/* 1645 */     if (i == 0) {
/*      */       return;
/*      */     }
/* 1648 */     super.giveExperiencePoints(i);
/* 1649 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void disconnect() {
/* 1653 */     this.disconnected = true;
/* 1654 */     ejectPassengers();
/* 1655 */     if (isSleeping()) {
/* 1656 */       stopSleepInBed(true, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1661 */   public boolean hasDisconnected() { return this.disconnected; }
/*      */ 
/*      */ 
/*      */   
/* 1665 */   public void resetSentInfo() { this.lastSentHealth = -1.0E8F; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1670 */   public void displayClientMessage(Component component, boolean overlayMessage) { sendSystemMessage(component, overlayMessage); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void completeUsingItem() {
/* 1675 */     if (!this.useItem.isEmpty() && isUsingItem()) {
/* 1676 */       this.connection.send(new ClientboundEntityEventPacket(this, (byte)9));
/* 1677 */       super.completeUsingItem();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor anchor, Vec3 pos) {
/* 1683 */     super.lookAt(anchor, pos);
/* 1684 */     this.connection.send(new ClientboundPlayerLookAtPacket(anchor, pos.x, pos.y, pos.z));
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor fromAnchor, Entity entity, EntityAnchorArgument.Anchor toAnchor) {
/* 1688 */     Vec3 pos = toAnchor.apply(entity);
/* 1689 */     super.lookAt(fromAnchor, pos);
/* 1690 */     this.connection.send(new ClientboundPlayerLookAtPacket(fromAnchor, entity, toAnchor));
/*      */   }
/*      */   
/*      */   public void restoreFrom(ServerPlayer oldPlayer, boolean restoreAll) {
/* 1694 */     this.wardenSpawnTracker = oldPlayer.wardenSpawnTracker;
/* 1695 */     this.chatSession = oldPlayer.chatSession;
/*      */ 
/*      */     
/* 1698 */     this.gameMode.setGameModeForPlayer(oldPlayer.gameMode
/* 1699 */         .getGameModeForPlayer(), oldPlayer.gameMode
/* 1700 */         .getPreviousGameModeForPlayer());
/*      */     
/* 1702 */     onUpdateAbilities();
/* 1703 */     getAttributes().assignBaseValues(oldPlayer.getAttributes());
/*      */     
/* 1705 */     if (restoreAll) {
/* 1706 */       getAttributes().assignPermanentModifiers(oldPlayer.getAttributes());
/* 1707 */       setHealth(oldPlayer.getHealth());
/* 1708 */       this.foodData = oldPlayer.foodData;
/*      */       
/* 1710 */       for (MobEffectInstance effect : oldPlayer.getActiveEffects()) {
/* 1711 */         addEffect(new MobEffectInstance(effect));
/*      */       }
/*      */       
/* 1714 */       transferInventoryXpAndScore(oldPlayer);
/* 1715 */       this.portalProcess = oldPlayer.portalProcess;
/*      */     } else {
/* 1717 */       setHealth(getMaxHealth());
/*      */       
/* 1719 */       if (((Boolean)level().getGameRules().get(GameRules.KEEP_INVENTORY)).booleanValue() || oldPlayer.isSpectator()) {
/* 1720 */         transferInventoryXpAndScore(oldPlayer);
/*      */       }
/*      */     } 
/*      */     
/* 1724 */     this.enchantmentSeed = oldPlayer.enchantmentSeed;
/* 1725 */     this.enderChestInventory = oldPlayer.enderChestInventory;
/* 1726 */     getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, (Byte)oldPlayer.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));
/* 1727 */     this.lastSentExp = -1;
/* 1728 */     this.lastSentHealth = -1.0F;
/* 1729 */     this.lastSentFood = -1;
/* 1730 */     this.recipeBook.copyOverData(oldPlayer.recipeBook);
/* 1731 */     this.seenCredits = oldPlayer.seenCredits;
/* 1732 */     this.enteredNetherPosition = oldPlayer.enteredNetherPosition;
/* 1733 */     this.chunkTrackingView = oldPlayer.chunkTrackingView;
/* 1734 */     this.requestedDebugSubscriptions = oldPlayer.requestedDebugSubscriptions;
/*      */     
/* 1736 */     setShoulderEntityLeft(oldPlayer.getShoulderEntityLeft());
/* 1737 */     setShoulderEntityRight(oldPlayer.getShoulderEntityRight());
/* 1738 */     setLastDeathLocation(oldPlayer.getLastDeathLocation());
/* 1739 */     waypointIcon().copyFrom(oldPlayer.waypointIcon());
/*      */   }
/*      */   
/*      */   private void transferInventoryXpAndScore(Player oldPlayer) {
/* 1743 */     getInventory().replaceWith(oldPlayer.getInventory());
/* 1744 */     this.experienceLevel = oldPlayer.experienceLevel;
/* 1745 */     this.totalExperience = oldPlayer.totalExperience;
/* 1746 */     this.experienceProgress = oldPlayer.experienceProgress;
/* 1747 */     setScore(oldPlayer.getScore());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEffectAdded(MobEffectInstance effect, Entity source) {
/* 1752 */     super.onEffectAdded(effect, source);
/* 1753 */     this.connection.send(new ClientboundUpdateMobEffectPacket(getId(), effect, true));
/*      */     
/* 1755 */     if (effect.is(MobEffects.LEVITATION)) {
/* 1756 */       this.levitationStartTime = this.tickCount;
/* 1757 */       this.levitationStartPos = position();
/*      */     } 
/*      */     
/* 1760 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this, source);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEffectUpdated(MobEffectInstance effect, boolean doRefreshAttributes, Entity source) {
/* 1765 */     super.onEffectUpdated(effect, doRefreshAttributes, source);
/* 1766 */     this.connection.send(new ClientboundUpdateMobEffectPacket(getId(), effect, false));
/*      */     
/* 1768 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this, source);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEffectsRemoved(Collection<MobEffectInstance> effects) {
/* 1773 */     super.onEffectsRemoved(effects);
/* 1774 */     for (MobEffectInstance effect : effects) {
/* 1775 */       this.connection.send(new ClientboundRemoveMobEffectPacket(getId(), effect.getEffect()));
/*      */       
/* 1777 */       if (effect.is(MobEffects.LEVITATION)) {
/* 1778 */         this.levitationStartPos = null;
/*      */       }
/*      */     } 
/*      */     
/* 1782 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1787 */   public void teleportTo(double x, double y, double z) { this.connection.teleport(new PositionMoveRotation(new Vec3(x, y, z), Vec3.ZERO, 0.0F, 0.0F), Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION })); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1792 */   public void teleportRelative(double dx, double dy, double dz) { this.connection.teleport(new PositionMoveRotation(new Vec3(dx, dy, dz), Vec3.ZERO, 0.0F, 0.0F), Relative.ALL); }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean teleportTo(ServerLevel level, double x, double y, double z, Set<Relative> relatives, float newYRot, float newXRot, boolean resetCamera) {
/* 1797 */     if (isSleeping()) {
/* 1798 */       stopSleepInBed(true, true);
/*      */     }
/*      */     
/* 1801 */     if (resetCamera) {
/* 1802 */       setCamera(this);
/*      */     }
/*      */     
/* 1805 */     boolean success = super.teleportTo(level, x, y, z, relatives, newYRot, newXRot, resetCamera);
/* 1806 */     if (success) {
/* 1807 */       setYHeadRot(relatives.contains(Relative.Y_ROT) ? (getYHeadRot() + newYRot) : newYRot);
/* 1808 */       this.connection.resetFlyingTicks();
/*      */     } 
/* 1810 */     return success;
/*      */   }
/*      */ 
/*      */   
/*      */   public void snapTo(double x, double y, double z) {
/* 1815 */     super.snapTo(x, y, z);
/* 1816 */     this.connection.resetPosition();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1821 */   public void crit(Entity entity) { level().getChunkSource().sendToTrackingPlayersAndSelf(this, new ClientboundAnimatePacket(entity, 4)); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1826 */   public void magicCrit(Entity entity) { level().getChunkSource().sendToTrackingPlayersAndSelf(this, new ClientboundAnimatePacket(entity, 5)); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onUpdateAbilities() {
/* 1831 */     if (this.connection == null) {
/*      */       return;
/*      */     }
/* 1834 */     this.connection.send(new ClientboundPlayerAbilitiesPacket(getAbilities()));
/* 1835 */     updateInvisibilityStatus();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1840 */   public ServerLevel level() { return (ServerLevel)super.level(); }
/*      */ 
/*      */   
/*      */   public boolean setGameMode(GameType mode) {
/* 1844 */     boolean wasSpectator = isSpectator();
/* 1845 */     if (!this.gameMode.changeGameModeForPlayer(mode)) {
/* 1846 */       return false;
/*      */     }
/* 1848 */     this.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, mode.getId()));
/*      */     
/* 1850 */     if (mode == GameType.SPECTATOR) {
/* 1851 */       removeEntitiesOnShoulder();
/* 1852 */       stopRiding();
/* 1853 */       stopUsingItem();
/* 1854 */       EnchantmentHelper.stopLocationBasedEffects(this);
/*      */     } else {
/* 1856 */       setCamera(this);
/* 1857 */       if (wasSpectator) {
/* 1858 */         EnchantmentHelper.runLocationChangedEffects(level(), this);
/*      */       }
/*      */     } 
/*      */     
/* 1862 */     onUpdateAbilities();
/* 1863 */     updateEffectVisibility();
/* 1864 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1869 */   public GameType gameMode() { return this.gameMode.getGameModeForPlayer(); }
/*      */ 
/*      */ 
/*      */   
/* 1873 */   public CommandSource commandSource() { return this.commandSource; }
/*      */ 
/*      */ 
/*      */   
/* 1877 */   public CommandSourceStack createCommandSourceStack() { return new CommandSourceStack(commandSource(), position(), getRotationVector(), level(), permissions(), getPlainTextName(), getDisplayName(), this.server, this); }
/*      */ 
/*      */ 
/*      */   
/* 1881 */   public void sendSystemMessage(Component message) { sendSystemMessage(message, false); }
/*      */ 
/*      */   
/*      */   public void sendSystemMessage(Component message, boolean overlay) {
/* 1885 */     if (!acceptsSystemMessages(overlay)) {
/*      */       return;
/*      */     }
/*      */     
/* 1889 */     this.connection.send(new ClientboundSystemChatPacket(message, overlay), PacketSendListener.exceptionallySend(() -> {
/* 1890 */             if (acceptsSystemMessages(false)) {
/* 1891 */               int truncatedMessageSize = 256;
/* 1892 */               String contents = message.getString(256);
/* 1893 */               MutableComponent mutableComponent = Component.literal(contents).withStyle(ChatFormatting.YELLOW);
/* 1894 */               return new ClientboundSystemChatPacket(Component.translatable("multiplayer.message_not_delivered", new Object[] { mutableComponent }).withStyle(ChatFormatting.RED), false);
/*      */             } 
/* 1896 */             return null;
/*      */           }));
/*      */   }
/*      */   
/*      */   public void sendChatMessage(OutgoingChatMessage message, boolean filtered, ChatType.Bound chatType) {
/* 1901 */     if (acceptsChatMessages()) {
/* 1902 */       message.sendToPlayer(this, filtered, chatType);
/*      */     }
/*      */   }
/*      */   
/*      */   public String getIpAddress() {
/* 1907 */     SocketAddress remoteAddress = this.connection.getRemoteAddress();
/* 1908 */     if (remoteAddress instanceof InetSocketAddress) { InetSocketAddress ipSocketAddress = (InetSocketAddress)remoteAddress;
/* 1909 */       return InetAddresses.toAddrString(ipSocketAddress.getAddress()); }
/*      */     
/* 1911 */     return "<unknown>";
/*      */   }
/*      */   
/*      */   public void updateOptions(ClientInformation information) {
/* 1915 */     this.language = information.language();
/* 1916 */     this.requestedViewDistance = information.viewDistance();
/* 1917 */     this.chatVisibility = information.chatVisibility();
/* 1918 */     this.canChatColor = information.chatColors();
/* 1919 */     this.textFilteringEnabled = information.textFilteringEnabled();
/* 1920 */     this.allowsListing = information.allowsListing();
/* 1921 */     this.particleStatus = information.particleStatus();
/*      */     
/* 1923 */     getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf((byte)information.modelCustomisation()));
/* 1924 */     getEntityData().set(DATA_PLAYER_MAIN_HAND, information.mainHand());
/*      */   }
/*      */   
/*      */   public ClientInformation clientInformation() {
/* 1928 */     int modelCustomization = ((Byte)getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION)).byteValue();
/*      */     
/* 1930 */     return new ClientInformation(this.language, this.requestedViewDistance, this.chatVisibility, this.canChatColor, modelCustomization, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1936 */         getMainArm(), this.textFilteringEnabled, this.allowsListing, this.particleStatus);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1944 */   public boolean canChatInColor() { return this.canChatColor; }
/*      */ 
/*      */ 
/*      */   
/* 1948 */   public ChatVisiblity getChatVisibility() { return this.chatVisibility; }
/*      */ 
/*      */   
/*      */   private boolean acceptsSystemMessages(boolean overlay) {
/* 1952 */     if (this.chatVisibility == ChatVisiblity.HIDDEN) {
/* 1953 */       return overlay;
/*      */     }
/* 1955 */     return true;
/*      */   }
/*      */ 
/*      */   
/* 1959 */   private boolean acceptsChatMessages() { return (this.chatVisibility == ChatVisiblity.FULL); }
/*      */ 
/*      */ 
/*      */   
/* 1963 */   public int requestedViewDistance() { return this.requestedViewDistance; }
/*      */ 
/*      */ 
/*      */   
/* 1967 */   public void sendServerStatus(ServerStatus status) { this.connection.send(new ClientboundServerDataPacket(status.description(), status.favicon().map(ServerStatus.Favicon::iconBytes))); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1972 */   public PermissionSet permissions() { return this.server.getProfilePermissions(nameAndId()); }
/*      */ 
/*      */ 
/*      */   
/* 1976 */   public void resetLastActionTime() { this.lastActionTime = Util.getMillis(); }
/*      */ 
/*      */ 
/*      */   
/* 1980 */   public ServerStatsCounter getStats() { return this.stats; }
/*      */ 
/*      */ 
/*      */   
/* 1984 */   public ServerRecipeBook getRecipeBook() { return this.recipeBook; }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateInvisibilityStatus() {
/* 1989 */     if (isSpectator()) {
/* 1990 */       removeEffectParticles();
/* 1991 */       setInvisible(true);
/*      */     } else {
/* 1993 */       super.updateInvisibilityStatus();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 1998 */   public Entity getCamera() { return (this.camera == null) ? this : this.camera; }
/*      */ 
/*      */   
/*      */   public void setCamera(Entity newCamera) {
/* 2002 */     Entity oldCamera = getCamera();
/* 2003 */     this.camera = (newCamera == null) ? this : newCamera;
/*      */     
/* 2005 */     if (oldCamera != this.camera) {
/* 2006 */       Level level1 = this.camera.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 2007 */         teleportTo(level, this.camera.getX(), this.camera.getY(), this.camera.getZ(), Set.of(), getYRot(), getXRot(), false); }
/*      */       
/* 2009 */       if (newCamera != null)
/*      */       {
/* 2011 */         level().getChunkSource().move(this);
/*      */       }
/* 2013 */       this.connection.send(new ClientboundSetCameraPacket(this.camera));
/* 2014 */       this.connection.resetPosition();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void processPortalCooldown() {
/* 2020 */     if (!this.isChangingDimension) {
/* 2021 */       super.processPortalCooldown();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void attack(Entity entity) {
/* 2027 */     if (isSpectator()) {
/* 2028 */       setCamera(entity);
/*      */     } else {
/* 2030 */       super.attack(entity);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 2035 */   public long getLastActionTime() { return this.lastActionTime; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2040 */   public Component getTabListDisplayName() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2045 */   public int getTabListOrder() { return 0; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void swing(InteractionHand hand) {
/* 2050 */     super.swing(hand);
/* 2051 */     resetAttackStrengthTicker();
/*      */   }
/*      */ 
/*      */   
/* 2055 */   public boolean isChangingDimension() { return this.isChangingDimension; }
/*      */ 
/*      */ 
/*      */   
/* 2059 */   public void hasChangedDimension() { this.isChangingDimension = false; }
/*      */ 
/*      */ 
/*      */   
/* 2063 */   public PlayerAdvancements getAdvancements() { return this.advancements; }
/*      */ 
/*      */ 
/*      */   
/* 2067 */   public RespawnConfig getRespawnConfig() { return this.respawnConfig; }
/*      */ 
/*      */ 
/*      */   
/* 2071 */   public void copyRespawnPosition(ServerPlayer player) { setRespawnPosition(player.respawnConfig, false); }
/*      */ 
/*      */   
/*      */   public void setRespawnPosition(RespawnConfig respawnConfig, boolean showMessage) {
/* 2075 */     if (showMessage && respawnConfig != null && !respawnConfig.isSamePosition(this.respawnConfig)) {
/* 2076 */       sendSystemMessage(SPAWN_SET_MESSAGE);
/*      */     }
/* 2078 */     this.respawnConfig = respawnConfig;
/*      */   }
/*      */ 
/*      */   
/* 2082 */   public SectionPos getLastSectionPos() { return this.lastSectionPos; }
/*      */ 
/*      */ 
/*      */   
/* 2086 */   public void setLastSectionPos(SectionPos lastSectionPos) { this.lastSectionPos = lastSectionPos; }
/*      */ 
/*      */ 
/*      */   
/* 2090 */   public ChunkTrackingView getChunkTrackingView() { return this.chunkTrackingView; }
/*      */ 
/*      */ 
/*      */   
/* 2094 */   public void setChunkTrackingView(ChunkTrackingView chunkTrackingView) { this.chunkTrackingView = chunkTrackingView; }
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemEntity drop(ItemStack itemStack, boolean randomly, boolean thrownFromHand) {
/* 2099 */     ItemEntity entity = super.drop(itemStack, randomly, thrownFromHand);
/*      */     
/* 2101 */     if (thrownFromHand) {
/* 2102 */       ItemStack droppedItemStack = (entity != null) ? entity.getItem() : ItemStack.EMPTY;
/* 2103 */       if (!droppedItemStack.isEmpty()) {
/* 2104 */         awardStat(Stats.ITEM_DROPPED.get(droppedItemStack.getItem()), itemStack.getCount());
/* 2105 */         awardStat(Stats.DROP);
/*      */       } 
/*      */     } 
/*      */     
/* 2109 */     return entity;
/*      */   }
/*      */ 
/*      */   
/* 2113 */   public TextFilter getTextFilter() { return this.textFilter; }
/*      */ 
/*      */   
/*      */   public void setServerLevel(ServerLevel level) {
/* 2117 */     setLevel(level);
/* 2118 */     this.gameMode.setLevel(level);
/*      */   }
/*      */ 
/*      */   
/* 2122 */   private static GameType readPlayerMode(ValueInput playerInput, String modeTag) { return (GameType)playerInput.read(modeTag, GameType.LEGACY_ID_CODEC).orElse(null); }
/*      */ 
/*      */ 
/*      */   
/*      */   private GameType calculateGameModeForNewPlayer(GameType loadedGameType) {
/* 2127 */     GameType forcedGameType = this.server.getForcedGameType();
/* 2128 */     if (forcedGameType != null) {
/* 2129 */       return forcedGameType;
/*      */     }
/*      */     
/* 2132 */     return (loadedGameType != null) ? loadedGameType : this.server.getDefaultGameType();
/*      */   }
/*      */   
/*      */   private void storeGameTypes(ValueOutput playerOutput) {
/* 2136 */     playerOutput.store("playerGameType", GameType.LEGACY_ID_CODEC, this.gameMode.getGameModeForPlayer());
/* 2137 */     GameType previousGameMode = this.gameMode.getPreviousGameModeForPlayer();
/* 2138 */     playerOutput.storeNullable("previousPlayerGameType", GameType.LEGACY_ID_CODEC, previousGameMode);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2143 */   public boolean isTextFilteringEnabled() { return this.textFilteringEnabled; }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean shouldFilterMessageTo(ServerPlayer serverPlayer) {
/* 2148 */     if (serverPlayer == this) {
/* 2149 */       return false;
/*      */     }
/* 2151 */     return (this.textFilteringEnabled || serverPlayer.textFilteringEnabled);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2156 */   public boolean mayInteract(ServerLevel level, BlockPos pos) { return (super.mayInteract(level, pos) && level.mayInteract(this, pos)); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateUsingItem(ItemStack useItem) {
/* 2161 */     CriteriaTriggers.USING_ITEM.trigger(this, useItem);
/* 2162 */     super.updateUsingItem(useItem);
/*      */   }
/*      */   
/*      */   public void drop(boolean all) {
/* 2166 */     Inventory inventory = getInventory();
/* 2167 */     ItemStack removed = inventory.removeFromSelected(all);
/* 2168 */     this.containerMenu.findSlot(inventory, inventory.getSelectedSlot()).ifPresent(slotIndex -> 
/* 2169 */         this.containerMenu.setRemoteSlot(slotIndex, inventory.getSelectedItem()));
/*      */     
/* 2171 */     if (this.useItem.isEmpty()) {
/* 2172 */       stopUsingItem();
/*      */     }
/* 2174 */     drop(removed, false, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleExtraItemsCreatedOnUse(ItemStack extraItems) {
/* 2179 */     if (!getInventory().add(extraItems)) {
/* 2180 */       drop(extraItems, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 2185 */   public boolean allowsListing() { return this.allowsListing; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2190 */   public Optional<WardenSpawnTracker> getWardenSpawnTracker() { return Optional.of(this.wardenSpawnTracker); }
/*      */ 
/*      */ 
/*      */   
/* 2194 */   public void setSpawnExtraParticlesOnFall(boolean toggle) { this.spawnExtraParticlesOnFall = toggle; }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onItemPickup(ItemEntity entity) {
/* 2199 */     super.onItemPickup(entity);
/* 2200 */     Entity thrower = entity.getOwner();
/* 2201 */     if (thrower != null) {
/* 2202 */       CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.trigger(this, entity.getItem(), thrower);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 2207 */   public void setChatSession(RemoteChatSession chatSession) { this.chatSession = chatSession; }
/*      */ 
/*      */   
/*      */   public RemoteChatSession getChatSession() {
/* 2211 */     if (this.chatSession != null && this.chatSession.hasExpired()) {
/* 2212 */       return null;
/*      */     }
/* 2214 */     return this.chatSession;
/*      */   }
/*      */ 
/*      */   
/*      */   public void indicateDamage(double xd, double zd) {
/* 2219 */     this.hurtDir = (float)(Mth.atan2(zd, xd) * 57.2957763671875D - getYRot());
/* 2220 */     this.connection.send(new ClientboundHurtAnimationPacket(this));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers) {
/* 2225 */     if (super.startRiding(entityToRide, force, sendEventAndTriggers)) {
/*      */       
/* 2227 */       entityToRide.positionRider(this);
/* 2228 */       this.connection.teleport(new PositionMoveRotation(position(), Vec3.ZERO, 0.0F, 0.0F), Relative.ROTATION);
/* 2229 */       if (entityToRide instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entityToRide;
/* 2230 */         this.server.getPlayerList().sendActiveEffects(livingEntity, this.connection); }
/*      */ 
/*      */       
/* 2233 */       this.connection.send(new ClientboundSetPassengersPacket(entityToRide));
/* 2234 */       return true;
/*      */     } 
/* 2236 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeVehicle() {
/* 2241 */     Entity oldVehicle = getVehicle();
/* 2242 */     super.removeVehicle();
/* 2243 */     if (oldVehicle instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)oldVehicle;
/* 2244 */       for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
/* 2245 */         this.connection.send(new ClientboundRemoveMobEffectPacket(oldVehicle.getId(), effect.getEffect()));
/*      */       } }
/*      */     
/* 2248 */     if (oldVehicle != null)
/*      */     {
/* 2250 */       this.connection.send(new ClientboundSetPassengersPacket(oldVehicle));
/*      */     }
/*      */   }
/*      */   
/*      */   public CommonPlayerSpawnInfo createCommonSpawnInfo(ServerLevel level) {
/* 2255 */     return new CommonPlayerSpawnInfo(level
/* 2256 */         .dimensionTypeRegistration(), level
/* 2257 */         .dimension(), 
/* 2258 */         BiomeManager.obfuscateSeed(level.getSeed()), this.gameMode
/* 2259 */         .getGameModeForPlayer(), this.gameMode
/* 2260 */         .getPreviousGameModeForPlayer(), level
/* 2261 */         .isDebug(), level
/* 2262 */         .isFlat(), 
/* 2263 */         getLastDeathLocation(), 
/* 2264 */         getPortalCooldown(), level
/* 2265 */         .getSeaLevel());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2270 */   public void setRaidOmenPosition(BlockPos raidOmenPosition) { this.raidOmenPosition = raidOmenPosition; }
/*      */ 
/*      */ 
/*      */   
/* 2274 */   public void clearRaidOmenPosition() { this.raidOmenPosition = null; }
/*      */ 
/*      */ 
/*      */   
/* 2278 */   public BlockPos getRaidOmenPosition() { return this.raidOmenPosition; }
/*      */ 
/*      */ 
/*      */   
/*      */   public Vec3 getKnownMovement() {
/* 2283 */     Entity vehicle = getVehicle();
/* 2284 */     if (vehicle != null && vehicle.getControllingPassenger() != this)
/*      */     {
/* 2286 */       return vehicle.getKnownMovement();
/*      */     }
/* 2288 */     return this.lastKnownClientMovement;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getKnownSpeed() {
/* 2293 */     Entity vehicle = getVehicle();
/* 2294 */     if (vehicle != null && vehicle.getControllingPassenger() != this)
/*      */     {
/* 2296 */       return vehicle.getKnownSpeed();
/*      */     }
/* 2298 */     return this.lastKnownClientMovement;
/*      */   }
/*      */ 
/*      */   
/* 2302 */   public void setKnownMovement(Vec3 lastKnownClientMovement) { this.lastKnownClientMovement = lastKnownClientMovement; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2307 */   protected float getEnchantedDamage(Entity entity, float dmg, DamageSource damageSource) { return EnchantmentHelper.modifyDamage(level(), getWeaponItem(), entity, damageSource, dmg); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onEquippedItemBroken(Item brokenItem, EquipmentSlot inSlot) {
/* 2312 */     super.onEquippedItemBroken(brokenItem, inSlot);
/* 2313 */     awardStat(Stats.ITEM_BROKEN.get(brokenItem));
/*      */   }
/*      */ 
/*      */   
/* 2317 */   public Input getLastClientInput() { return this.lastClientInput; }
/*      */ 
/*      */ 
/*      */   
/* 2321 */   public void setLastClientInput(Input lastClientInput) { this.lastClientInput = lastClientInput; }
/*      */ 
/*      */   
/*      */   public Vec3 getLastClientMoveIntent() {
/* 2325 */     float leftIntent = (this.lastClientInput.left() == this.lastClientInput.right()) ? 0.0F : (this.lastClientInput.left() ? 1.0F : -1.0F);
/* 2326 */     float forwardIntent = (this.lastClientInput.forward() == this.lastClientInput.backward()) ? 0.0F : (this.lastClientInput.forward() ? 1.0F : -1.0F);
/* 2327 */     return getInputVector(new Vec3(leftIntent, 0.0D, forwardIntent), 1.0F, getYRot());
/*      */   }
/*      */ 
/*      */   
/* 2331 */   public void registerEnderPearl(ThrownEnderpearl enderPearl) { this.enderPearls.add(enderPearl); }
/*      */ 
/*      */ 
/*      */   
/* 2335 */   public void deregisterEnderPearl(ThrownEnderpearl enderPearl) { this.enderPearls.remove(enderPearl); }
/*      */ 
/*      */ 
/*      */   
/* 2339 */   public Set<ThrownEnderpearl> getEnderPearls() { return this.enderPearls; }
/*      */ 
/*      */ 
/*      */   
/* 2343 */   public CompoundTag getShoulderEntityLeft() { return this.shoulderEntityLeft; }
/*      */ 
/*      */   
/*      */   protected void setShoulderEntityLeft(CompoundTag tag) {
/* 2347 */     this.shoulderEntityLeft = tag;
/* 2348 */     setShoulderParrotLeft(extractParrotVariant(tag));
/*      */   }
/*      */ 
/*      */   
/* 2352 */   public CompoundTag getShoulderEntityRight() { return this.shoulderEntityRight; }
/*      */ 
/*      */   
/*      */   protected void setShoulderEntityRight(CompoundTag tag) {
/* 2356 */     this.shoulderEntityRight = tag;
/* 2357 */     setShoulderParrotRight(extractParrotVariant(tag));
/*      */   }
/*      */   
/*      */   public long registerAndUpdateEnderPearlTicket(ThrownEnderpearl enderpearl) {
/* 2361 */     Level level = enderpearl.level(); if (level instanceof ServerLevel) { ServerLevel enderPearlLevel = (ServerLevel)level;
/* 2362 */       ChunkPos chunkPos = enderpearl.chunkPosition();
/* 2363 */       registerEnderPearl(enderpearl);
/* 2364 */       enderPearlLevel.resetEmptyTime();
/* 2365 */       return placeEnderPearlTicket(enderPearlLevel, chunkPos) - 1L; }
/*      */     
/* 2367 */     return 0L;
/*      */   }
/*      */   
/*      */   public static long placeEnderPearlTicket(ServerLevel level, ChunkPos chunk) {
/* 2371 */     level.getChunkSource().addTicketWithRadius(TicketType.ENDER_PEARL, chunk, 2);
/* 2372 */     return TicketType.ENDER_PEARL.timeout();
/*      */   }
/*      */ 
/*      */   
/* 2376 */   public void requestDebugSubscriptions(Set<DebugSubscription<?>> subscriptions) { this.requestedDebugSubscriptions = Set.copyOf(subscriptions); }
/*      */ 
/*      */   
/*      */   public Set<DebugSubscription<?>> debugSubscriptions() {
/* 2380 */     if (!this.server.debugSubscribers().hasRequiredPermissions(this)) {
/* 2381 */       return Set.of();
/*      */     }
/* 2383 */     return this.requestedDebugSubscriptions;
/*      */   }
/*      */   public static final class RespawnConfig extends Record { private final LevelData.RespawnData respawnData; private final boolean forced;
/* 2386 */     public RespawnConfig(LevelData.RespawnData respawnData, boolean forced) { this.respawnData = respawnData; this.forced = forced; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2386	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2386	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2386	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/* 2386 */       //   0	8	1	o	Ljava/lang/Object; } public LevelData.RespawnData respawnData() { return this.respawnData; } public boolean forced() { return this.forced; }
/*      */ 
/*      */ 
/*      */     
/* 2390 */     public static final Codec<RespawnConfig> CODEC = RecordCodecBuilder.create(i -> i.group(LevelData.RespawnData.MAP_CODEC
/* 2391 */           .forGetter(RespawnConfig::respawnData), Codec.BOOL
/* 2392 */           .optionalFieldOf("forced", Boolean.valueOf(false)).forGetter(RespawnConfig::forced))
/* 2393 */         .apply(i, RespawnConfig::new));
/*      */ 
/*      */     
/* 2396 */     private static ResourceKey<Level> getDimensionOrDefault(RespawnConfig respawnConfig) { return (respawnConfig != null) ? respawnConfig.respawnData().dimension() : Level.OVERWORLD; }
/*      */ 
/*      */ 
/*      */     
/* 2400 */     public boolean isSamePosition(RespawnConfig other) { return (other != null && this.respawnData.globalPos().equals(other.respawnData.globalPos())); } }
/*      */   public static final class SavedPosition extends Record { private final Optional<ResourceKey<Level>> dimension; private final Optional<Vec3> position;
/*      */     private final Optional<Vec2> rotation;
/*      */     
/* 2404 */     public SavedPosition(Optional<ResourceKey<Level>> dimension, Optional<Vec3> position, Optional<Vec2> rotation) { this.dimension = dimension; this.position = position; this.rotation = rotation; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2404	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2404	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2404	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/* 2404 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ResourceKey<Level>> dimension() { return this.dimension; } public Optional<Vec3> position() { return this.position; } public Optional<Vec2> rotation() { return this.rotation; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2409 */     public static final MapCodec<SavedPosition> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Level.RESOURCE_KEY_CODEC
/* 2410 */           .optionalFieldOf("Dimension").forGetter(SavedPosition::dimension), Vec3.CODEC
/* 2411 */           .optionalFieldOf("Pos").forGetter(SavedPosition::position), Vec2.CODEC
/* 2412 */           .optionalFieldOf("Rotation").forGetter(SavedPosition::rotation))
/* 2413 */         .apply(i, SavedPosition::new));
/*      */     
/* 2415 */     public static final SavedPosition EMPTY = new SavedPosition(Optional.empty(), Optional.empty(), Optional.empty()); }
/*      */ 
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
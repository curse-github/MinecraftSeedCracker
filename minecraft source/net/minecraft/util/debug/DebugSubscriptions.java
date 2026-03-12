/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DebugSubscriptions<T>
/*     */   extends Object
/*     */ {
/*  80 */   public static final DebugSubscription<?> DEDICATED_SERVER_TICK_TIME = registerSimple("dedicated_server_tick_time");
/*     */ 
/*     */   
/*  83 */   public static final DebugSubscription<DebugBeeInfo> BEES = registerWithValue("bees", DebugBeeInfo.STREAM_CODEC);
/*  84 */   public static final DebugSubscription<DebugBrainDump> BRAINS = registerWithValue("brains", DebugBrainDump.STREAM_CODEC);
/*  85 */   public static final DebugSubscription<DebugBreezeInfo> BREEZES = registerWithValue("breezes", DebugBreezeInfo.STREAM_CODEC);
/*  86 */   public static final DebugSubscription<DebugGoalInfo> GOAL_SELECTORS = registerWithValue("goal_selectors", DebugGoalInfo.STREAM_CODEC);
/*  87 */   public static final DebugSubscription<DebugPathInfo> ENTITY_PATHS = registerWithValue("entity_paths", DebugPathInfo.STREAM_CODEC);
/*     */ 
/*     */   
/*  90 */   public static final DebugSubscription<DebugEntityBlockIntersection> ENTITY_BLOCK_INTERSECTIONS = registerTemporaryValue("entity_block_intersections", DebugEntityBlockIntersection.STREAM_CODEC, 100);
/*  91 */   public static final DebugSubscription<DebugHiveInfo> BEE_HIVES = registerWithValue("bee_hives", DebugHiveInfo.STREAM_CODEC);
/*  92 */   public static final DebugSubscription<DebugPoiInfo> POIS = registerWithValue("pois", DebugPoiInfo.STREAM_CODEC);
/*  93 */   public static final DebugSubscription<Orientation> REDSTONE_WIRE_ORIENTATIONS = registerTemporaryValue("redstone_wire_orientations", Orientation.STREAM_CODEC, 200);
/*  94 */   public static final DebugSubscription<Unit> VILLAGE_SECTIONS = registerWithValue("village_sections", Unit.STREAM_CODEC);
/*     */ 
/*     */   
/*  97 */   public static final DebugSubscription<List<BlockPos>> RAIDS = registerWithValue("raids", BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*  98 */   public static final DebugSubscription<List<DebugStructureInfo>> STRUCTURES = registerWithValue("structures", DebugStructureInfo.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*     */ 
/*     */   
/* 101 */   public static final DebugSubscription<DebugGameEventListenerInfo> GAME_EVENT_LISTENERS = registerWithValue("game_event_listeners", DebugGameEventListenerInfo.STREAM_CODEC);
/*     */ 
/*     */   
/* 104 */   public static final DebugSubscription<BlockPos> NEIGHBOR_UPDATES = registerTemporaryValue("neighbor_updates", BlockPos.STREAM_CODEC, 200);
/* 105 */   public static final DebugSubscription<DebugGameEventInfo> GAME_EVENTS = registerTemporaryValue("game_events", DebugGameEventInfo.STREAM_CODEC, 60);
/*     */ 
/*     */   
/* 108 */   public static DebugSubscription<?> bootstrap(Registry<DebugSubscription<?>> registry) { return DEDICATED_SERVER_TICK_TIME; }
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static DebugSubscription<?> registerSimple(String id) { return (DebugSubscription)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(id), new DebugSubscription(null)); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   private static <T> DebugSubscription<T> registerWithValue(String id, StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec) { return (DebugSubscription)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(id), new DebugSubscription(valueStreamCodec)); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   private static <T> DebugSubscription<T> registerTemporaryValue(String id, StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec, int expireAfterTicks) { return (DebugSubscription)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(id), new DebugSubscription(valueStreamCodec, expireAfterTicks)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugSubscriptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
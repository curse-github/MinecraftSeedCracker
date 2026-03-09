/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameRules
/*     */ {
/*  22 */   public static Codec<GameRules> codec(FeatureFlagSet enabledFeatures) { return GameRuleMap.CODEC.xmap(map -> new GameRules(enabledFeatures, map), gameRules -> gameRules.rules); }
/*     */ 
/*     */   
/*  25 */   public static final GameRule<Boolean> ADVANCE_TIME = registerBoolean("advance_time", GameRuleCategory.UPDATES, !SharedConstants.DEBUG_WORLD_RECREATE);
/*  26 */   public static final GameRule<Boolean> ADVANCE_WEATHER = registerBoolean("advance_weather", GameRuleCategory.UPDATES, !SharedConstants.DEBUG_WORLD_RECREATE);
/*  27 */   public static final GameRule<Boolean> ALLOW_ENTERING_NETHER_USING_PORTALS = registerBoolean("allow_entering_nether_using_portals", GameRuleCategory.MISC, true);
/*  28 */   public static final GameRule<Boolean> BLOCK_DROPS = registerBoolean("block_drops", GameRuleCategory.DROPS, true);
/*  29 */   public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = registerBoolean("block_explosion_drop_decay", GameRuleCategory.DROPS, true);
/*  30 */   public static final GameRule<Boolean> COMMAND_BLOCKS_WORK = registerBoolean("command_blocks_work", GameRuleCategory.MISC, true);
/*  31 */   public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = registerBoolean("command_block_output", GameRuleCategory.CHAT, true);
/*  32 */   public static final GameRule<Boolean> DROWNING_DAMAGE = registerBoolean("drowning_damage", GameRuleCategory.PLAYER, true);
/*  33 */   public static final GameRule<Boolean> ELYTRA_MOVEMENT_CHECK = registerBoolean("elytra_movement_check", GameRuleCategory.PLAYER, true);
/*  34 */   public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = registerBoolean("ender_pearls_vanish_on_death", GameRuleCategory.PLAYER, true);
/*  35 */   public static final GameRule<Boolean> ENTITY_DROPS = registerBoolean("entity_drops", GameRuleCategory.DROPS, true);
/*  36 */   public static final GameRule<Boolean> FALL_DAMAGE = registerBoolean("fall_damage", GameRuleCategory.PLAYER, true);
/*  37 */   public static final GameRule<Boolean> FIRE_DAMAGE = registerBoolean("fire_damage", GameRuleCategory.PLAYER, true);
/*  38 */   public static final GameRule<Integer> FIRE_SPREAD_RADIUS_AROUND_PLAYER = registerInteger("fire_spread_radius_around_player", GameRuleCategory.UPDATES, 128, -1);
/*  39 */   public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = registerBoolean("forgive_dead_players", GameRuleCategory.MOBS, true);
/*  40 */   public static final GameRule<Boolean> FREEZE_DAMAGE = registerBoolean("freeze_damage", GameRuleCategory.PLAYER, true);
/*  41 */   public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = registerBoolean("global_sound_events", GameRuleCategory.MISC, true);
/*  42 */   public static final GameRule<Boolean> IMMEDIATE_RESPAWN = registerBoolean("immediate_respawn", GameRuleCategory.PLAYER, false);
/*  43 */   public static final GameRule<Boolean> KEEP_INVENTORY = registerBoolean("keep_inventory", GameRuleCategory.PLAYER, false);
/*  44 */   public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = registerBoolean("lava_source_conversion", GameRuleCategory.UPDATES, false);
/*  45 */   public static final GameRule<Boolean> LIMITED_CRAFTING = registerBoolean("limited_crafting", GameRuleCategory.PLAYER, false);
/*  46 */   public static final GameRule<Boolean> LOCATOR_BAR = registerBoolean("locator_bar", GameRuleCategory.PLAYER, true);
/*  47 */   public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = registerBoolean("log_admin_commands", GameRuleCategory.CHAT, true);
/*  48 */   public static final GameRule<Integer> MAX_BLOCK_MODIFICATIONS = registerInteger("max_block_modifications", GameRuleCategory.MISC, 32768, 1);
/*  49 */   public static final GameRule<Integer> MAX_COMMAND_FORKS = registerInteger("max_command_forks", GameRuleCategory.MISC, 65536, 0);
/*  50 */   public static final GameRule<Integer> MAX_COMMAND_SEQUENCE_LENGTH = registerInteger("max_command_sequence_length", GameRuleCategory.MISC, 65536, 0);
/*  51 */   public static final GameRule<Integer> MAX_ENTITY_CRAMMING = registerInteger("max_entity_cramming", GameRuleCategory.MOBS, 24, 0);
/*  52 */   public static final GameRule<Integer> MAX_MINECART_SPEED = registerInteger("max_minecart_speed", GameRuleCategory.MISC, 8, 1, 1000, FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS));
/*  53 */   public static final GameRule<Integer> MAX_SNOW_ACCUMULATION_HEIGHT = registerInteger("max_snow_accumulation_height", GameRuleCategory.UPDATES, 1, 0, 8);
/*  54 */   public static final GameRule<Boolean> MOB_DROPS = registerBoolean("mob_drops", GameRuleCategory.DROPS, true);
/*  55 */   public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = registerBoolean("mob_explosion_drop_decay", GameRuleCategory.DROPS, true);
/*  56 */   public static final GameRule<Boolean> MOB_GRIEFING = registerBoolean("mob_griefing", GameRuleCategory.MOBS, true);
/*  57 */   public static final GameRule<Boolean> NATURAL_HEALTH_REGENERATION = registerBoolean("natural_health_regeneration", GameRuleCategory.PLAYER, true);
/*  58 */   public static final GameRule<Boolean> PLAYER_MOVEMENT_CHECK = registerBoolean("player_movement_check", GameRuleCategory.PLAYER, true);
/*  59 */   public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = registerInteger("players_nether_portal_creative_delay", GameRuleCategory.PLAYER, 0, 0);
/*  60 */   public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = registerInteger("players_nether_portal_default_delay", GameRuleCategory.PLAYER, 80, 0);
/*  61 */   public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = registerInteger("players_sleeping_percentage", GameRuleCategory.PLAYER, 100, 0);
/*  62 */   public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = registerBoolean("projectiles_can_break_blocks", GameRuleCategory.DROPS, true);
/*  63 */   public static final GameRule<Boolean> PVP = registerBoolean("pvp", GameRuleCategory.PLAYER, true);
/*  64 */   public static final GameRule<Boolean> RAIDS = registerBoolean("raids", GameRuleCategory.MOBS, true);
/*  65 */   public static final GameRule<Integer> RANDOM_TICK_SPEED = registerInteger("random_tick_speed", GameRuleCategory.UPDATES, 3, 0);
/*  66 */   public static final GameRule<Boolean> REDUCED_DEBUG_INFO = registerBoolean("reduced_debug_info", GameRuleCategory.MISC, false);
/*  67 */   public static final GameRule<Integer> RESPAWN_RADIUS = registerInteger("respawn_radius", GameRuleCategory.PLAYER, 10, 0);
/*  68 */   public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = registerBoolean("send_command_feedback", GameRuleCategory.CHAT, true);
/*  69 */   public static final GameRule<Boolean> SHOW_ADVANCEMENT_MESSAGES = registerBoolean("show_advancement_messages", GameRuleCategory.CHAT, true);
/*  70 */   public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = registerBoolean("show_death_messages", GameRuleCategory.CHAT, true);
/*  71 */   public static final GameRule<Boolean> SPAWNER_BLOCKS_WORK = registerBoolean("spawner_blocks_work", GameRuleCategory.MISC, true);
/*  72 */   public static final GameRule<Boolean> SPAWN_MOBS = registerBoolean("spawn_mobs", GameRuleCategory.SPAWNING, true);
/*  73 */   public static final GameRule<Boolean> SPAWN_MONSTERS = registerBoolean("spawn_monsters", GameRuleCategory.SPAWNING, true);
/*  74 */   public static final GameRule<Boolean> SPAWN_PATROLS = registerBoolean("spawn_patrols", GameRuleCategory.SPAWNING, true);
/*  75 */   public static final GameRule<Boolean> SPAWN_PHANTOMS = registerBoolean("spawn_phantoms", GameRuleCategory.SPAWNING, true);
/*  76 */   public static final GameRule<Boolean> SPAWN_WANDERING_TRADERS = registerBoolean("spawn_wandering_traders", GameRuleCategory.SPAWNING, true);
/*  77 */   public static final GameRule<Boolean> SPAWN_WARDENS = registerBoolean("spawn_wardens", GameRuleCategory.SPAWNING, true);
/*  78 */   public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = registerBoolean("spectators_generate_chunks", GameRuleCategory.PLAYER, true);
/*  79 */   public static final GameRule<Boolean> SPREAD_VINES = registerBoolean("spread_vines", GameRuleCategory.UPDATES, true);
/*  80 */   public static final GameRule<Boolean> TNT_EXPLODES = registerBoolean("tnt_explodes", GameRuleCategory.MISC, true);
/*  81 */   public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = registerBoolean("tnt_explosion_drop_decay", GameRuleCategory.DROPS, false);
/*  82 */   public static final GameRule<Boolean> UNIVERSAL_ANGER = registerBoolean("universal_anger", GameRuleCategory.MOBS, false);
/*  83 */   public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = registerBoolean("water_source_conversion", GameRuleCategory.UPDATES, true);
/*     */   
/*     */   private final GameRuleMap rules;
/*     */   
/*     */   public GameRules(FeatureFlagSet enabledFeatures, GameRuleMap map) {
/*  88 */     this(enabledFeatures);
/*  89 */     Objects.requireNonNull(this.rules); this.rules.setFromIf(map, this.rules::has);
/*     */   }
/*     */ 
/*     */   
/*  93 */   public GameRules(FeatureFlagSet enabledFeatures) { this.rules = GameRuleMap.of(BuiltInRegistries.GAME_RULE.filterFeatures(enabledFeatures).listElements().map(Holder::value)); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public Stream<GameRule<?>> availableRules() { return this.rules.keySet().stream(); }
/*     */ 
/*     */   
/*     */   public <T> T get(GameRule<T> gameRule) {
/* 101 */     T value = (T)this.rules.get(gameRule);
/* 102 */     if (value == null) {
/* 103 */       throw new IllegalArgumentException("Tried to access invalid game rule");
/*     */     }
/* 105 */     return value;
/*     */   }
/*     */   
/*     */   public <T> void set(GameRule<T> gameRule, T value, MinecraftServer server) {
/* 109 */     if (!this.rules.has(gameRule)) {
/* 110 */       throw new IllegalArgumentException("Tried to set invalid game rule");
/*     */     }
/* 112 */     this.rules.set(gameRule, value);
/* 113 */     if (server != null) {
/* 114 */       server.onGameRuleChanged(gameRule, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 119 */   public GameRules copy(FeatureFlagSet enabledFeatures) { return new GameRules(enabledFeatures, this.rules); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public void setAll(GameRules other, MinecraftServer server) { setAll(other.rules, server); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public void setAll(GameRuleMap gameRulesMap, MinecraftServer server) { gameRulesMap.keySet().forEach(gameRule -> setFromOther(gameRulesMap, gameRule, server)); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   private <T> void setFromOther(GameRuleMap gameRulesMap, GameRule<T> gameRule, MinecraftServer server) { set(gameRule, Objects.requireNonNull(gameRulesMap.get(gameRule)), server); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitGameRuleTypes(GameRuleTypeVisitor visitor) {
/* 139 */     this.rules.keySet().forEach(gameRule -> {
/* 140 */           visitor.visit(gameRule);
/* 141 */           gameRule.callVisitor(visitor);
/*     */         });
/*     */   }
/*     */   
/*     */   private static GameRule<Boolean> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
/* 146 */     return register(id, category, GameRuleType.BOOL, 
/*     */ 
/*     */ 
/*     */         
/* 150 */         BoolArgumentType.bool(), Codec.BOOL, 
/*     */         
/* 152 */         Boolean.valueOf(defaultValue), 
/* 153 */         FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, b -> 
/*     */         
/* 155 */         b.booleanValue() ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 160 */   private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min) { return registerInteger(id, category, defaultValue, min, 2147483647, FeatureFlagSet.of()); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min, int max) { return registerInteger(id, category, defaultValue, min, max, FeatureFlagSet.of()); }
/*     */ 
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min, int max, FeatureFlagSet requiredFeatures) {
/* 168 */     return register(id, category, GameRuleType.INT, 
/*     */ 
/*     */ 
/*     */         
/* 172 */         IntegerArgumentType.integer(min, max), 
/* 173 */         Codec.intRange(min, max), 
/* 174 */         Integer.valueOf(defaultValue), requiredFeatures, GameRuleTypeVisitor::visitInteger, i -> 
/*     */ 
/*     */         
/* 177 */         i.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   private static <T> GameRule<T> register(String id, GameRuleCategory category, GameRuleType typeHint, ArgumentType<T> argumentType, Codec<T> codec, T defaultValue, FeatureFlagSet requiredFeatures, VisitorCaller<T> visitorCaller, ToIntFunction<T> commandResultFunction) { return (GameRule)Registry.register(BuiltInRegistries.GAME_RULE, id, new GameRule(category, typeHint, argumentType, visitorCaller, codec, commandResultFunction, defaultValue, requiredFeatures)); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public static GameRule<?> bootstrap(Registry<GameRule<?>> registry) { return ADVANCE_TIME; }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public <T> String getAsString(GameRule<T> gameRule) { return gameRule.serialize(get(gameRule)); }
/*     */   
/*     */   public static interface VisitorCaller<T> {
/*     */     void call(GameRuleTypeVisitor param1GameRuleTypeVisitor, GameRule<T> param1GameRule);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gamerules\GameRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
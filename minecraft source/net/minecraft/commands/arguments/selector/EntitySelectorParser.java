/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ToFloatFunction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySelectorParser
/*     */ {
/*     */   public static final char SYNTAX_SELECTOR_START = '@';
/*     */   private static final char SYNTAX_OPTIONS_START = '[';
/*     */   private static final char SYNTAX_OPTIONS_END = ']';
/*     */   public static final char SYNTAX_OPTIONS_KEY_VALUE_SEPARATOR = '=';
/*     */   private static final char SYNTAX_OPTIONS_SEPARATOR = ',';
/*     */   public static final char SYNTAX_NOT = '!';
/*     */   public static final char SYNTAX_TAG = '#';
/*     */   private static final char SELECTOR_NEAREST_PLAYER = 'p';
/*     */   private static final char SELECTOR_ALL_PLAYERS = 'a';
/*     */   private static final char SELECTOR_RANDOM_PLAYERS = 'r';
/*     */   private static final char SELECTOR_CURRENT_ENTITY = 's';
/*     */   private static final char SELECTOR_ALL_ENTITIES = 'e';
/*     */   private static final char SELECTOR_NEAREST_ENTITY = 'n';
/*  52 */   public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType(Component.translatable("argument.entity.invalid"));
/*  53 */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(type -> Component.translatableEscape("argument.entity.selector.unknown", new Object[] { type }));
/*  54 */   public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType(Component.translatable("argument.entity.selector.not_allowed"));
/*  55 */   public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType(Component.translatable("argument.entity.selector.missing"));
/*  56 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_OPTIONS = new SimpleCommandExceptionType(Component.translatable("argument.entity.options.unterminated"));
/*  57 */   public static final DynamicCommandExceptionType ERROR_EXPECTED_OPTION_VALUE = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.entity.options.valueless", new Object[] { name }));
/*     */   
/*  59 */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_NEAREST = (p, c) -> c.sort(());
/*  60 */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_FURTHEST = (p, c) -> c.sort(());
/*  61 */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_RANDOM = (p, c) -> Collections.shuffle(c);
/*     */   
/*  63 */   public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING = (b, s) -> b.buildFuture(); private final StringReader reader; private final boolean allowSelectors; private int maxResults; private boolean includesEntities; private boolean worldLimited; private MinMaxBounds.Doubles distance; private MinMaxBounds.Ints level; private Double x;
/*     */   private Double y;
/*     */   private Double z;
/*     */   private Double deltaX;
/*     */   private Double deltaY;
/*     */   private Double deltaZ;
/*     */   private MinMaxBounds.FloatDegrees rotX;
/*     */   private MinMaxBounds.FloatDegrees rotY;
/*     */   private final List<Predicate<Entity>> predicates;
/*     */   private BiConsumer<Vec3, List<? extends Entity>> order;
/*     */   private boolean currentEntity;
/*     */   private String playerName;
/*     */   private int startPosition;
/*     */   private UUID entityUUID;
/*     */   private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions;
/*     */   
/*     */   public EntitySelectorParser(StringReader reader, boolean allowSelectors) {
/*  80 */     this.predicates = new ArrayList();
/*  81 */     this.order = EntitySelector.ORDER_ARBITRARY;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     this.suggestions = SUGGEST_NOTHING;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.reader = reader;
/* 103 */     this.allowSelectors = allowSelectors;
/*     */   }
/*     */   
/*     */   private boolean hasNameEquals;
/*     */   private boolean hasNameNotEquals;
/*     */   private boolean isLimited;
/*     */   private boolean isSorted;
/*     */   
/* 111 */   public static <S> boolean allowSelectors(S source) { if (source instanceof PermissionSetSupplier) { PermissionSetSupplier sender = (PermissionSetSupplier)source; if (sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS)); }  return false; }
/*     */   private boolean hasGamemodeEquals; private boolean hasGamemodeNotEquals; private boolean hasTeamEquals; private boolean hasTeamNotEquals; private EntityType<?> type;
/*     */   private boolean typeInverse;
/*     */   private boolean hasScores;
/*     */   private boolean hasAdvancements;
/*     */   private boolean usesSelectors;
/*     */   
/*     */   @Deprecated
/* 119 */   public static boolean allowSelectors(PermissionSetSupplier source) { return source.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS); }
/*     */   
/*     */   public EntitySelector getSelector() {
/*     */     Function<Vec3, Vec3> position;
/*     */     AABB aabb;
/* 124 */     if (this.deltaX != null || this.deltaY != null || this.deltaZ != null) {
/* 125 */       aabb = createAabb((this.deltaX == null) ? 0.0D : this.deltaX.doubleValue(), (this.deltaY == null) ? 0.0D : this.deltaY.doubleValue(), (this.deltaZ == null) ? 0.0D : this.deltaZ.doubleValue());
/* 126 */     } else if (this.distance != null && this.distance.max().isPresent()) {
/* 127 */       double maxRange = ((Double)this.distance.max().get()).doubleValue();
/* 128 */       aabb = new AABB(-maxRange, -maxRange, -maxRange, maxRange + 1.0D, maxRange + 1.0D, maxRange + 1.0D);
/*     */     } else {
/* 130 */       aabb = null;
/*     */     } 
/*     */     
/* 133 */     if (this.x == null && this.y == null && this.z == null) {
/* 134 */       position = (o -> o);
/*     */     } else {
/* 136 */       position = (o -> new Vec3((this.x == null) ? o.x : this.x.doubleValue(), (this.y == null) ? o.y : this.y.doubleValue(), (this.z == null) ? o.z : this.z.doubleValue()));
/*     */     } 
/* 138 */     return new EntitySelector(this.maxResults, this.includesEntities, this.worldLimited, List.copyOf(this.predicates), this.distance, position, aabb, this.order, this.currentEntity, this.playerName, this.entityUUID, this.type, this.usesSelectors);
/*     */   }
/*     */   
/*     */   private AABB createAabb(double x, double y, double z) {
/* 142 */     boolean xNeg = (x < 0.0D);
/* 143 */     boolean yNeg = (y < 0.0D);
/* 144 */     boolean zNeg = (z < 0.0D);
/* 145 */     double xMin = xNeg ? x : 0.0D;
/* 146 */     double yMin = yNeg ? y : 0.0D;
/* 147 */     double zMin = zNeg ? z : 0.0D;
/* 148 */     double xMax = (xNeg ? 0.0D : x) + 1.0D;
/* 149 */     double yMax = (yNeg ? 0.0D : y) + 1.0D;
/* 150 */     double zMax = (zNeg ? 0.0D : z) + 1.0D;
/* 151 */     return new AABB(xMin, yMin, zMin, xMax, yMax, zMax);
/*     */   }
/*     */   
/*     */   private void finalizePredicates() {
/* 155 */     if (this.rotX != null) {
/* 156 */       this.predicates.add(createRotationPredicate(this.rotX, Entity::getXRot));
/*     */     }
/* 158 */     if (this.rotY != null) {
/* 159 */       this.predicates.add(createRotationPredicate(this.rotY, Entity::getYRot));
/*     */     }
/* 161 */     if (this.level != null) {
/* 162 */       this.predicates.add(e -> { if (e instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)e; if (this.level.matches(serverPlayer.experienceLevel)); }  return false;
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private Predicate<Entity> createRotationPredicate(MinMaxBounds.FloatDegrees range, ToFloatFunction<Entity> function) {
/* 168 */     float min = Mth.wrapDegrees(((Float)range.min().orElse(Float.valueOf(0.0F))).floatValue());
/* 169 */     float max = Mth.wrapDegrees(((Float)range.max().orElse(Float.valueOf(359.0F))).floatValue());
/* 170 */     return e -> {
/* 171 */         float rotation = Mth.wrapDegrees(function.applyAsFloat(e));
/* 172 */         if (min > max) {
/* 173 */           return (rotation >= min || rotation <= max);
/*     */         }
/* 175 */         return (rotation >= min && rotation <= max);
/*     */       };
/*     */   }
/*     */   protected void parseSelector() {
/*     */     boolean selectOnlyAlive, selectOnlyAlive, selectOnlyAlive, selectOnlyAlive, selectOnlyAlive, selectOnlyAlive;
/* 180 */     this.usesSelectors = true;
/* 181 */     this.suggestions = this::suggestSelector;
/* 182 */     if (!this.reader.canRead()) {
/* 183 */       throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader);
/*     */     }
/* 185 */     int start = this.reader.getCursor();
/* 186 */     char type = this.reader.read();
/*     */ 
/*     */     
/* 189 */     switch (type) {
/*     */       case 'p':
/* 191 */         this.maxResults = 1;
/* 192 */         this.includesEntities = false;
/* 193 */         this.order = ORDER_NEAREST;
/* 194 */         limitToType(EntityType.PLAYER);
/* 195 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'a':
/* 198 */         this.maxResults = Integer.MAX_VALUE;
/* 199 */         this.includesEntities = false;
/* 200 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 201 */         limitToType(EntityType.PLAYER);
/* 202 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'r':
/* 205 */         this.maxResults = 1;
/* 206 */         this.includesEntities = false;
/* 207 */         this.order = ORDER_RANDOM;
/* 208 */         limitToType(EntityType.PLAYER);
/* 209 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 's':
/* 212 */         this.maxResults = 1;
/* 213 */         this.includesEntities = true;
/* 214 */         this.currentEntity = true;
/*     */         
/* 216 */         selectOnlyAlive = false;
/*     */         break;
/*     */       case 'e':
/* 219 */         this.maxResults = Integer.MAX_VALUE;
/* 220 */         this.includesEntities = true;
/* 221 */         this.order = EntitySelector.ORDER_ARBITRARY;
/* 222 */         selectOnlyAlive = true;
/*     */         break;
/*     */       case 'n':
/* 225 */         this.maxResults = 1;
/* 226 */         this.includesEntities = true;
/* 227 */         this.order = ORDER_NEAREST;
/* 228 */         selectOnlyAlive = true;
/*     */         break;
/*     */       default:
/* 231 */         this.reader.setCursor(start);
/* 232 */         throw ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, "@" + String.valueOf(type));
/*     */     } 
/*     */ 
/*     */     
/* 236 */     if (selectOnlyAlive) {
/* 237 */       this.predicates.add(Entity::isAlive);
/*     */     }
/* 239 */     this.suggestions = this::suggestOpenOptions;
/* 240 */     if (this.reader.canRead() && this.reader.peek() == '[') {
/* 241 */       this.reader.skip();
/* 242 */       this.suggestions = this::suggestOptionsKeyOrClose;
/* 243 */       parseOptions();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseNameOrUUID() {
/* 248 */     if (this.reader.canRead()) {
/* 249 */       this.suggestions = this::suggestName;
/*     */     }
/* 251 */     int start = this.reader.getCursor();
/* 252 */     String name = this.reader.readString();
/*     */     
/*     */     try {
/* 255 */       this.entityUUID = UUID.fromString(name);
/* 256 */       this.includesEntities = true;
/* 257 */     } catch (IllegalArgumentException ex) {
/* 258 */       if (name.isEmpty() || name.length() > 16) {
/* 259 */         this.reader.setCursor(start);
/* 260 */         throw ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
/*     */       } 
/* 262 */       this.includesEntities = false;
/* 263 */       this.playerName = name;
/*     */     } 
/*     */     
/* 266 */     this.maxResults = 1;
/*     */   }
/*     */   
/*     */   protected void parseOptions() {
/* 270 */     this.suggestions = this::suggestOptionsKey;
/* 271 */     this.reader.skipWhitespace();
/* 272 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 273 */       this.reader.skipWhitespace();
/* 274 */       int start = this.reader.getCursor();
/* 275 */       String key = this.reader.readString();
/* 276 */       EntitySelectorOptions.Modifier modifier = EntitySelectorOptions.get(this, key, start);
/* 277 */       this.reader.skipWhitespace();
/* 278 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 279 */         this.reader.setCursor(start);
/* 280 */         throw ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, key);
/*     */       } 
/* 282 */       this.reader.skip();
/* 283 */       this.reader.skipWhitespace();
/*     */       
/* 285 */       this.suggestions = SUGGEST_NOTHING;
/* 286 */       modifier.handle(this);
/* 287 */       this.reader.skipWhitespace();
/*     */       
/* 289 */       this.suggestions = this::suggestOptionsNextOrClose;
/* 290 */       if (this.reader.canRead()) {
/* 291 */         if (this.reader.peek() == ',') {
/* 292 */           this.reader.skip();
/* 293 */           this.suggestions = this::suggestOptionsKey; continue;
/* 294 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 297 */         throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 302 */     if (this.reader.canRead()) {
/* 303 */       this.reader.skip();
/* 304 */       this.suggestions = SUGGEST_NOTHING;
/*     */     } else {
/* 306 */       throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldInvertValue() {
/* 311 */     this.reader.skipWhitespace();
/* 312 */     if (this.reader.canRead() && this.reader.peek() == '!') {
/* 313 */       this.reader.skip();
/* 314 */       this.reader.skipWhitespace();
/* 315 */       return true;
/*     */     } 
/* 317 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTag() {
/* 321 */     this.reader.skipWhitespace();
/* 322 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 323 */       this.reader.skip();
/* 324 */       this.reader.skipWhitespace();
/* 325 */       return true;
/*     */     } 
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 331 */   public StringReader getReader() { return this.reader; }
/*     */ 
/*     */ 
/*     */   
/* 335 */   public void addPredicate(Predicate<Entity> predicate) { this.predicates.add(predicate); }
/*     */ 
/*     */ 
/*     */   
/* 339 */   public void setWorldLimited() { this.worldLimited = true; }
/*     */ 
/*     */ 
/*     */   
/* 343 */   public MinMaxBounds.Doubles getDistance() { return this.distance; }
/*     */ 
/*     */ 
/*     */   
/* 347 */   public void setDistance(MinMaxBounds.Doubles distance) { this.distance = distance; }
/*     */ 
/*     */ 
/*     */   
/* 351 */   public MinMaxBounds.Ints getLevel() { return this.level; }
/*     */ 
/*     */ 
/*     */   
/* 355 */   public void setLevel(MinMaxBounds.Ints level) { this.level = level; }
/*     */ 
/*     */ 
/*     */   
/* 359 */   public MinMaxBounds.FloatDegrees getRotX() { return this.rotX; }
/*     */ 
/*     */ 
/*     */   
/* 363 */   public void setRotX(MinMaxBounds.FloatDegrees rotX) { this.rotX = rotX; }
/*     */ 
/*     */ 
/*     */   
/* 367 */   public MinMaxBounds.FloatDegrees getRotY() { return this.rotY; }
/*     */ 
/*     */ 
/*     */   
/* 371 */   public void setRotY(MinMaxBounds.FloatDegrees rotY) { this.rotY = rotY; }
/*     */ 
/*     */ 
/*     */   
/* 375 */   public Double getX() { return this.x; }
/*     */ 
/*     */ 
/*     */   
/* 379 */   public Double getY() { return this.y; }
/*     */ 
/*     */ 
/*     */   
/* 383 */   public Double getZ() { return this.z; }
/*     */ 
/*     */ 
/*     */   
/* 387 */   public void setX(double x) { this.x = Double.valueOf(x); }
/*     */ 
/*     */ 
/*     */   
/* 391 */   public void setY(double y) { this.y = Double.valueOf(y); }
/*     */ 
/*     */ 
/*     */   
/* 395 */   public void setZ(double z) { this.z = Double.valueOf(z); }
/*     */ 
/*     */ 
/*     */   
/* 399 */   public void setDeltaX(double deltaX) { this.deltaX = Double.valueOf(deltaX); }
/*     */ 
/*     */ 
/*     */   
/* 403 */   public void setDeltaY(double deltaY) { this.deltaY = Double.valueOf(deltaY); }
/*     */ 
/*     */ 
/*     */   
/* 407 */   public void setDeltaZ(double deltaZ) { this.deltaZ = Double.valueOf(deltaZ); }
/*     */ 
/*     */ 
/*     */   
/* 411 */   public Double getDeltaX() { return this.deltaX; }
/*     */ 
/*     */ 
/*     */   
/* 415 */   public Double getDeltaY() { return this.deltaY; }
/*     */ 
/*     */ 
/*     */   
/* 419 */   public Double getDeltaZ() { return this.deltaZ; }
/*     */ 
/*     */ 
/*     */   
/* 423 */   public void setMaxResults(int maxResults) { this.maxResults = maxResults; }
/*     */ 
/*     */ 
/*     */   
/* 427 */   public void setIncludesEntities(boolean includesEntities) { this.includesEntities = includesEntities; }
/*     */ 
/*     */ 
/*     */   
/* 431 */   public BiConsumer<Vec3, List<? extends Entity>> getOrder() { return this.order; }
/*     */ 
/*     */ 
/*     */   
/* 435 */   public void setOrder(BiConsumer<Vec3, List<? extends Entity>> order) { this.order = order; }
/*     */ 
/*     */   
/*     */   public EntitySelector parse() {
/* 439 */     this.startPosition = this.reader.getCursor();
/* 440 */     this.suggestions = this::suggestNameOrSelector;
/* 441 */     if (this.reader.canRead() && this.reader.peek() == '@') {
/* 442 */       if (!this.allowSelectors) {
/* 443 */         throw ERROR_SELECTORS_NOT_ALLOWED.createWithContext(this.reader);
/*     */       }
/* 445 */       this.reader.skip();
/* 446 */       parseSelector();
/*     */     } else {
/* 448 */       parseNameOrUUID();
/*     */     } 
/* 450 */     finalizePredicates();
/* 451 */     return getSelector();
/*     */   }
/*     */   
/*     */   private static void fillSelectorSuggestions(SuggestionsBuilder builder) {
/* 455 */     builder.suggest("@p", Component.translatable("argument.entity.selector.nearestPlayer"));
/* 456 */     builder.suggest("@a", Component.translatable("argument.entity.selector.allPlayers"));
/* 457 */     builder.suggest("@r", Component.translatable("argument.entity.selector.randomPlayer"));
/* 458 */     builder.suggest("@s", Component.translatable("argument.entity.selector.self"));
/* 459 */     builder.suggest("@e", Component.translatable("argument.entity.selector.allEntities"));
/* 460 */     builder.suggest("@n", Component.translatable("argument.entity.selector.nearestEntity"));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNameOrSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 464 */     names.accept(builder);
/* 465 */     if (this.allowSelectors) {
/* 466 */       fillSelectorSuggestions(builder);
/*     */     }
/* 468 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 472 */     SuggestionsBuilder sub = builder.createOffset(this.startPosition);
/* 473 */     names.accept(sub);
/* 474 */     return builder.add(sub).buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 478 */     SuggestionsBuilder sub = builder.createOffset(builder.getStart() - 1);
/* 479 */     fillSelectorSuggestions(sub);
/* 480 */     builder.add(sub);
/* 481 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 485 */     builder.suggest(String.valueOf('['));
/* 486 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 490 */     builder.suggest(String.valueOf(']'));
/* 491 */     EntitySelectorOptions.suggestNames(this, builder);
/* 492 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 496 */     EntitySelectorOptions.suggestNames(this, builder);
/* 497 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 501 */     builder.suggest(String.valueOf(','));
/* 502 */     builder.suggest(String.valueOf(']'));
/* 503 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) {
/* 507 */     builder.suggest(String.valueOf('='));
/* 508 */     return builder.buildFuture();
/*     */   }
/*     */ 
/*     */   
/* 512 */   public boolean isCurrentEntity() { return this.currentEntity; }
/*     */ 
/*     */ 
/*     */   
/* 516 */   public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions) { this.suggestions = suggestions; }
/*     */ 
/*     */ 
/*     */   
/* 520 */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> names) { return (CompletableFuture)this.suggestions.apply(builder.createOffset(this.reader.getCursor()), names); }
/*     */ 
/*     */ 
/*     */   
/* 524 */   public boolean hasNameEquals() { return this.hasNameEquals; }
/*     */ 
/*     */ 
/*     */   
/* 528 */   public void setHasNameEquals(boolean hasNameEquals) { this.hasNameEquals = hasNameEquals; }
/*     */ 
/*     */ 
/*     */   
/* 532 */   public boolean hasNameNotEquals() { return this.hasNameNotEquals; }
/*     */ 
/*     */ 
/*     */   
/* 536 */   public void setHasNameNotEquals(boolean hasNameNotEquals) { this.hasNameNotEquals = hasNameNotEquals; }
/*     */ 
/*     */ 
/*     */   
/* 540 */   public boolean isLimited() { return this.isLimited; }
/*     */ 
/*     */ 
/*     */   
/* 544 */   public void setLimited(boolean limited) { this.isLimited = limited; }
/*     */ 
/*     */ 
/*     */   
/* 548 */   public boolean isSorted() { return this.isSorted; }
/*     */ 
/*     */ 
/*     */   
/* 552 */   public void setSorted(boolean sorted) { this.isSorted = sorted; }
/*     */ 
/*     */ 
/*     */   
/* 556 */   public boolean hasGamemodeEquals() { return this.hasGamemodeEquals; }
/*     */ 
/*     */ 
/*     */   
/* 560 */   public void setHasGamemodeEquals(boolean hasGamemodeEquals) { this.hasGamemodeEquals = hasGamemodeEquals; }
/*     */ 
/*     */ 
/*     */   
/* 564 */   public boolean hasGamemodeNotEquals() { return this.hasGamemodeNotEquals; }
/*     */ 
/*     */ 
/*     */   
/* 568 */   public void setHasGamemodeNotEquals(boolean hasGamemodeNotEquals) { this.hasGamemodeNotEquals = hasGamemodeNotEquals; }
/*     */ 
/*     */ 
/*     */   
/* 572 */   public boolean hasTeamEquals() { return this.hasTeamEquals; }
/*     */ 
/*     */ 
/*     */   
/* 576 */   public void setHasTeamEquals(boolean hasTeamEquals) { this.hasTeamEquals = hasTeamEquals; }
/*     */ 
/*     */ 
/*     */   
/* 580 */   public boolean hasTeamNotEquals() { return this.hasTeamNotEquals; }
/*     */ 
/*     */ 
/*     */   
/* 584 */   public void setHasTeamNotEquals(boolean hasTeamNotEquals) { this.hasTeamNotEquals = hasTeamNotEquals; }
/*     */ 
/*     */ 
/*     */   
/* 588 */   public void limitToType(EntityType<?> type) { this.type = type; }
/*     */ 
/*     */ 
/*     */   
/* 592 */   public void setTypeLimitedInversely() { this.typeInverse = true; }
/*     */ 
/*     */ 
/*     */   
/* 596 */   public boolean isTypeLimited() { return (this.type != null); }
/*     */ 
/*     */ 
/*     */   
/* 600 */   public boolean isTypeLimitedInversely() { return this.typeInverse; }
/*     */ 
/*     */ 
/*     */   
/* 604 */   public boolean hasScores() { return this.hasScores; }
/*     */ 
/*     */ 
/*     */   
/* 608 */   public void setHasScores(boolean hasScores) { this.hasScores = hasScores; }
/*     */ 
/*     */ 
/*     */   
/* 612 */   public boolean hasAdvancements() { return this.hasAdvancements; }
/*     */ 
/*     */ 
/*     */   
/* 616 */   public void setHasAdvancements(boolean hasAdvancements) { this.hasAdvancements = hasAdvancements; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\selector\EntitySelectorParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
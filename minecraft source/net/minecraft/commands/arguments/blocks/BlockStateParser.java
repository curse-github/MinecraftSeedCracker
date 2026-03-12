/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockStateParser {
/*  37 */   public static final SimpleCommandExceptionType ERROR_NO_TAGS_ALLOWED = new SimpleCommandExceptionType(Component.translatable("argument.block.tag.disallowed"));
/*  38 */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_BLOCK = new DynamicCommandExceptionType(id -> Component.translatableEscape("argument.block.id.invalid", new Object[] { id }));
/*  39 */   public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_PROPERTY = new Dynamic2CommandExceptionType((block, property) -> Component.translatableEscape("argument.block.property.unknown", new Object[] { block, property }));
/*  40 */   public static final Dynamic2CommandExceptionType ERROR_DUPLICATE_PROPERTY = new Dynamic2CommandExceptionType((block, property) -> Component.translatableEscape("argument.block.property.duplicate", new Object[] { property, block }));
/*  41 */   public static final Dynamic3CommandExceptionType ERROR_INVALID_VALUE = new Dynamic3CommandExceptionType((block, property, value) -> Component.translatableEscape("argument.block.property.invalid", new Object[] { block, value, property }));
/*  42 */   public static final Dynamic2CommandExceptionType ERROR_EXPECTED_VALUE = new Dynamic2CommandExceptionType((block, property) -> Component.translatableEscape("argument.block.property.novalue", new Object[] { property, block }));
/*  43 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_PROPERTIES = new SimpleCommandExceptionType(Component.translatable("argument.block.property.unclosed"));
/*  44 */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(tag -> Component.translatableEscape("arguments.block.tag.unknown", new Object[] { tag })); private static final char SYNTAX_START_PROPERTIES = '['; private static final char SYNTAX_START_NBT = '{'; private static final char SYNTAX_END_PROPERTIES = ']'; private static final char SYNTAX_EQUALS = '=';
/*     */   private static final char SYNTAX_PROPERTY_SEPARATOR = ',';
/*     */   private static final char SYNTAX_TAG = '#';
/*     */   private final HolderLookup<Block> blocks;
/*     */   private final StringReader reader;
/*     */   private final boolean forTesting;
/*     */   private final boolean allowNbt;
/*     */   private final Map<Property<?>, Comparable<?>> properties;
/*     */   private final Map<String, String> vagueProperties;
/*  53 */   private static final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> SUGGEST_NOTHING = SuggestionsBuilder::buildFuture; private Identifier id; private StateDefinition<Block, BlockState> definition; private BlockState state;
/*     */   private CompoundTag nbt;
/*     */   private HolderSet<Block> tag;
/*     */   private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions;
/*     */   
/*     */   private BlockStateParser(HolderLookup<Block> blocks, StringReader reader, boolean forTesting, boolean allowNbt) {
/*  59 */     this.properties = Maps.newHashMap();
/*  60 */     this.vagueProperties = Maps.newHashMap();
/*  61 */     this.id = Identifier.withDefaultNamespace("");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     this.suggestions = SUGGEST_NOTHING;
/*     */ 
/*     */     
/*  69 */     this.blocks = blocks;
/*  70 */     this.reader = reader;
/*  71 */     this.forTesting = forTesting;
/*  72 */     this.allowNbt = allowNbt;
/*     */   }
/*     */   public static final class BlockResult extends Record { private final BlockState blockState; private final Map<Property<?>, Comparable<?>> properties; private final CompoundTag nbt;
/*  75 */     public BlockResult(BlockState blockState, Map<Property<?>, Comparable<?>> properties, CompoundTag nbt) { this.blockState = blockState; this.properties = properties; this.nbt = nbt; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #75	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  75 */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult; } public BlockState blockState() { return this.blockState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #75	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #75	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*  75 */       //   0	8	1	o	Ljava/lang/Object; } public Map<Property<?>, Comparable<?>> properties() { return this.properties; } public CompoundTag nbt() { return this.nbt; } }
/*     */   public static final class TagResult extends Record { private final HolderSet<Block> tag; private final Map<String, String> vagueProperties; private final CompoundTag nbt;
/*  77 */     public TagResult(HolderSet<Block> tag, Map<String, String> vagueProperties, CompoundTag nbt) { this.tag = tag; this.vagueProperties = vagueProperties; this.nbt = nbt; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*  77 */       //   0	8	1	o	Ljava/lang/Object; } public HolderSet<Block> tag() { return this.tag; } public Map<String, String> vagueProperties() { return this.vagueProperties; } public CompoundTag nbt() { return this.nbt; } }
/*     */ 
/*     */   
/*  80 */   public static BlockResult parseForBlock(HolderLookup<Block> blocks, String value, boolean allowNbt) throws CommandSyntaxException { return parseForBlock(blocks, new StringReader(value), allowNbt); }
/*     */ 
/*     */   
/*     */   public static BlockResult parseForBlock(HolderLookup<Block> blocks, StringReader reader, boolean allowNbt) throws CommandSyntaxException {
/*  84 */     int cursor = reader.getCursor();
/*     */     try {
/*  86 */       BlockStateParser parser = new BlockStateParser(blocks, reader, false, allowNbt);
/*  87 */       parser.parse();
/*     */       
/*  89 */       return new BlockResult(parser.state, parser.properties, parser.nbt);
/*  90 */     } catch (CommandSyntaxException e) {
/*  91 */       reader.setCursor(cursor);
/*  92 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  97 */   public static Either<BlockResult, TagResult> parseForTesting(HolderLookup<Block> blocks, String value, boolean allowNbt) throws CommandSyntaxException { return parseForTesting(blocks, new StringReader(value), allowNbt); }
/*     */ 
/*     */   
/*     */   public static Either<BlockResult, TagResult> parseForTesting(HolderLookup<Block> blocks, StringReader reader, boolean allowNbt) throws CommandSyntaxException {
/* 101 */     int cursor = reader.getCursor();
/*     */     try {
/* 103 */       BlockStateParser parser = new BlockStateParser(blocks, reader, true, allowNbt);
/* 104 */       parser.parse();
/* 105 */       if (parser.tag != null) {
/* 106 */         return Either.right(new TagResult(parser.tag, parser.vagueProperties, parser.nbt));
/*     */       }
/* 108 */       return Either.left(new BlockResult(parser.state, parser.properties, parser.nbt));
/* 109 */     } catch (CommandSyntaxException e) {
/* 110 */       reader.setCursor(cursor);
/* 111 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Suggestions> fillSuggestions(HolderLookup<Block> blocks, SuggestionsBuilder builder, boolean forTesting, boolean allowNbt) {
/* 116 */     StringReader reader = new StringReader(builder.getInput());
/* 117 */     reader.setCursor(builder.getStart());
/*     */     
/* 119 */     BlockStateParser parser = new BlockStateParser(blocks, reader, forTesting, allowNbt);
/*     */     try {
/* 121 */       parser.parse();
/* 122 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*     */ 
/*     */     
/* 125 */     return (CompletableFuture)parser.suggestions.apply(builder.createOffset(reader.getCursor()));
/*     */   }
/*     */   
/*     */   private void parse() throws CommandSyntaxException {
/* 129 */     if (this.forTesting) {
/* 130 */       this.suggestions = this::suggestBlockIdOrTag;
/*     */     } else {
/* 132 */       this.suggestions = this::suggestItem;
/*     */     } 
/* 134 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 135 */       readTag();
/* 136 */       this.suggestions = this::suggestOpenVaguePropertiesOrNbt;
/* 137 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 138 */         readVagueProperties();
/* 139 */         this.suggestions = this::suggestOpenNbt;
/*     */       } 
/*     */     } else {
/* 142 */       readBlock();
/* 143 */       this.suggestions = this::suggestOpenPropertiesOrNbt;
/* 144 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 145 */         readProperties();
/* 146 */         this.suggestions = this::suggestOpenNbt;
/*     */       } 
/*     */     } 
/* 149 */     if (this.allowNbt && this.reader.canRead() && this.reader.peek() == '{') {
/* 150 */       this.suggestions = SUGGEST_NOTHING;
/* 151 */       readNbt();
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestPropertyNameOrEnd(SuggestionsBuilder builder) {
/* 156 */     if (builder.getRemaining().isEmpty()) {
/* 157 */       builder.suggest(String.valueOf(']'));
/*     */     }
/*     */     
/* 160 */     return suggestPropertyName(builder);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyNameOrEnd(SuggestionsBuilder builder) {
/* 164 */     if (builder.getRemaining().isEmpty()) {
/* 165 */       builder.suggest(String.valueOf(']'));
/*     */     }
/* 167 */     return suggestVaguePropertyName(builder);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestPropertyName(SuggestionsBuilder builder) {
/* 171 */     String prefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 172 */     for (Property<?> property : this.state.getProperties()) {
/* 173 */       if (!this.properties.containsKey(property) && property.getName().startsWith(prefix)) {
/* 174 */         builder.suggest(property.getName() + "=");
/*     */       }
/*     */     } 
/* 177 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyName(SuggestionsBuilder builder) {
/* 181 */     String prefix = builder.getRemaining().toLowerCase(Locale.ROOT);
/* 182 */     if (this.tag != null) {
/* 183 */       for (Holder<Block> block : this.tag) {
/* 184 */         for (Property<?> property : ((Block)block.value()).getStateDefinition().getProperties()) {
/* 185 */           if (!this.vagueProperties.containsKey(property.getName()) && property.getName().startsWith(prefix)) {
/* 186 */             builder.suggest(property.getName() + "=");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 191 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenNbt(SuggestionsBuilder builder) {
/* 195 */     if (builder.getRemaining().isEmpty() && hasBlockEntity()) {
/* 196 */       builder.suggest(String.valueOf('{'));
/*     */     }
/* 198 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private boolean hasBlockEntity() {
/* 202 */     if (this.state != null) {
/* 203 */       return this.state.hasBlockEntity();
/*     */     }
/*     */     
/* 206 */     if (this.tag != null) {
/* 207 */       for (Holder<Block> block : this.tag) {
/* 208 */         if (((Block)block.value()).defaultBlockState().hasBlockEntity()) {
/* 209 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 214 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder builder) {
/* 218 */     if (builder.getRemaining().isEmpty()) {
/* 219 */       builder.suggest(String.valueOf('='));
/*     */     }
/* 221 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNextPropertyOrEnd(SuggestionsBuilder builder) {
/* 225 */     if (builder.getRemaining().isEmpty()) {
/* 226 */       builder.suggest(String.valueOf(']'));
/*     */     }
/* 228 */     if (builder.getRemaining().isEmpty() && this.properties.size() < this.state.getProperties().size()) {
/* 229 */       builder.suggest(String.valueOf(','));
/*     */     }
/* 231 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> SuggestionsBuilder addSuggestions(SuggestionsBuilder builder, Property<T> property) {
/* 235 */     for (Iterator iterator = property.getPossibleValues().iterator(); iterator.hasNext(); ) { T value = (T)(Comparable)iterator.next();
/* 236 */       if (value instanceof Integer) { Integer v = (Integer)value;
/* 237 */         builder.suggest(v.intValue()); continue; }
/*     */       
/* 239 */       builder.suggest(property.getName(value)); }
/*     */ 
/*     */     
/* 242 */     return builder;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyValue(SuggestionsBuilder builder, String key) {
/* 246 */     boolean hasMoreProperties = false;
/* 247 */     if (this.tag != null) {
/* 248 */       for (Holder<Block> blockHolder : this.tag) {
/* 249 */         Block block = (Block)blockHolder.value();
/* 250 */         Property<?> property = block.getStateDefinition().getProperty(key);
/* 251 */         if (property != null) {
/* 252 */           addSuggestions(builder, property);
/*     */         }
/* 254 */         if (!hasMoreProperties) {
/* 255 */           for (Property<?> prop : block.getStateDefinition().getProperties()) {
/* 256 */             if (!this.vagueProperties.containsKey(prop.getName())) {
/* 257 */               hasMoreProperties = true;
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 264 */     if (hasMoreProperties) {
/* 265 */       builder.suggest(String.valueOf(','));
/*     */     }
/* 267 */     builder.suggest(String.valueOf(']'));
/* 268 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenVaguePropertiesOrNbt(SuggestionsBuilder builder) {
/* 272 */     if (builder.getRemaining().isEmpty() && 
/* 273 */       this.tag != null) {
/* 274 */       boolean hasProperties = false;
/* 275 */       boolean hasEntity = false;
/*     */       
/* 277 */       for (Holder<Block> blockHolder : this.tag) {
/* 278 */         Block block = (Block)blockHolder.value();
/* 279 */         hasProperties |= (!block.getStateDefinition().getProperties().isEmpty());
/* 280 */         hasEntity |= block.defaultBlockState().hasBlockEntity();
/*     */         
/* 282 */         if (hasProperties && hasEntity) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 287 */       if (hasProperties) {
/* 288 */         builder.suggest(String.valueOf('['));
/*     */       }
/*     */       
/* 291 */       if (hasEntity) {
/* 292 */         builder.suggest(String.valueOf('{'));
/*     */       }
/*     */     } 
/*     */     
/* 296 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenPropertiesOrNbt(SuggestionsBuilder builder) {
/* 300 */     if (builder.getRemaining().isEmpty()) {
/* 301 */       if (!this.definition.getProperties().isEmpty()) {
/* 302 */         builder.suggest(String.valueOf('['));
/*     */       }
/* 304 */       if (this.state.hasBlockEntity()) {
/* 305 */         builder.suggest(String.valueOf('{'));
/*     */       }
/*     */     } 
/* 308 */     return builder.buildFuture();
/*     */   }
/*     */ 
/*     */   
/* 312 */   private CompletableFuture<Suggestions> suggestTag(SuggestionsBuilder builder) { return SharedSuggestionProvider.suggestResource(this.blocks.listTagIds().map(TagKey::location), builder, String.valueOf('#')); }
/*     */ 
/*     */ 
/*     */   
/* 316 */   private CompletableFuture<Suggestions> suggestItem(SuggestionsBuilder builder) { return SharedSuggestionProvider.suggestResource(this.blocks.listElementIds().map(ResourceKey::identifier), builder); }
/*     */ 
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestBlockIdOrTag(SuggestionsBuilder builder) {
/* 320 */     suggestTag(builder);
/* 321 */     suggestItem(builder);
/* 322 */     return builder.buildFuture();
/*     */   }
/*     */   
/*     */   private void readBlock() throws CommandSyntaxException {
/* 326 */     int start = this.reader.getCursor();
/* 327 */     this.id = Identifier.read(this.reader);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     Block block = (Block)((Holder.Reference)this.blocks.get(ResourceKey.create(Registries.BLOCK, this.id)).orElseThrow(() -> { this.reader.setCursor(start); return ERROR_UNKNOWN_BLOCK.createWithContext(this.reader, this.id.toString()); })).value();
/*     */     
/* 334 */     this.definition = block.getStateDefinition();
/* 335 */     this.state = block.defaultBlockState();
/*     */   }
/*     */   
/*     */   private void readTag() throws CommandSyntaxException {
/* 339 */     if (!this.forTesting) {
/* 340 */       throw ERROR_NO_TAGS_ALLOWED.createWithContext(this.reader);
/*     */     }
/*     */     
/* 343 */     int start = this.reader.getCursor();
/* 344 */     this.reader.expect('#');
/* 345 */     this.suggestions = this::suggestTag;
/* 346 */     Identifier id = Identifier.read(this.reader);
/* 347 */     this.tag = (HolderSet)this.blocks.get(TagKey.create(Registries.BLOCK, id)).orElseThrow(() -> {
/* 348 */           this.reader.setCursor(start);
/* 349 */           return ERROR_UNKNOWN_TAG.createWithContext(this.reader, id.toString());
/*     */         });
/*     */   }
/*     */   
/*     */   private void readProperties() throws CommandSyntaxException {
/* 354 */     this.reader.skip();
/* 355 */     this.suggestions = this::suggestPropertyNameOrEnd;
/*     */     
/* 357 */     this.reader.skipWhitespace();
/* 358 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 359 */       this.reader.skipWhitespace();
/* 360 */       int keyStart = this.reader.getCursor();
/* 361 */       String key = this.reader.readString();
/* 362 */       Property<?> property = this.definition.getProperty(key);
/* 363 */       if (property == null) {
/* 364 */         this.reader.setCursor(keyStart);
/* 365 */         throw ERROR_UNKNOWN_PROPERTY.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/* 367 */       if (this.properties.containsKey(property)) {
/* 368 */         this.reader.setCursor(keyStart);
/* 369 */         throw ERROR_DUPLICATE_PROPERTY.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/*     */       
/* 372 */       this.reader.skipWhitespace();
/* 373 */       this.suggestions = this::suggestEquals;
/* 374 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 375 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader, this.id.toString(), key);
/*     */       }
/* 377 */       this.reader.skip();
/* 378 */       this.reader.skipWhitespace();
/*     */       
/* 380 */       this.suggestions = (builder -> addSuggestions(builder, property).buildFuture());
/* 381 */       int start = this.reader.getCursor();
/* 382 */       setValue(property, this.reader.readString(), start);
/*     */       
/* 384 */       this.suggestions = this::suggestNextPropertyOrEnd;
/* 385 */       this.reader.skipWhitespace();
/* 386 */       if (this.reader.canRead()) {
/* 387 */         if (this.reader.peek() == ',') {
/* 388 */           this.reader.skip();
/* 389 */           this.suggestions = this::suggestPropertyName; continue;
/* 390 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 393 */         throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 398 */     if (this.reader.canRead()) {
/* 399 */       this.reader.skip();
/*     */     } else {
/* 401 */       throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readVagueProperties() throws CommandSyntaxException {
/* 406 */     this.reader.skip();
/* 407 */     this.suggestions = this::suggestVaguePropertyNameOrEnd;
/* 408 */     int valueStart = -1;
/*     */     
/* 410 */     this.reader.skipWhitespace();
/* 411 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 412 */       this.reader.skipWhitespace();
/* 413 */       int keyStart = this.reader.getCursor();
/* 414 */       String key = this.reader.readString();
/* 415 */       if (this.vagueProperties.containsKey(key)) {
/* 416 */         this.reader.setCursor(keyStart);
/* 417 */         throw ERROR_DUPLICATE_PROPERTY.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/*     */       
/* 420 */       this.reader.skipWhitespace();
/* 421 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 422 */         this.reader.setCursor(keyStart);
/* 423 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader, this.id.toString(), key);
/*     */       } 
/* 425 */       this.reader.skip();
/*     */       
/* 427 */       this.reader.skipWhitespace();
/* 428 */       this.suggestions = (builder -> suggestVaguePropertyValue(builder, key));
/* 429 */       valueStart = this.reader.getCursor();
/* 430 */       String value = this.reader.readString();
/* 431 */       this.vagueProperties.put(key, value);
/*     */       
/* 433 */       this.reader.skipWhitespace();
/* 434 */       if (this.reader.canRead()) {
/* 435 */         valueStart = -1;
/* 436 */         if (this.reader.peek() == ',') {
/* 437 */           this.reader.skip();
/* 438 */           this.suggestions = this::suggestVaguePropertyName; continue;
/* 439 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 442 */         throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 447 */     if (this.reader.canRead()) {
/* 448 */       this.reader.skip();
/*     */     } else {
/* 450 */       if (valueStart >= 0) {
/* 451 */         this.reader.setCursor(valueStart);
/*     */       }
/* 453 */       throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 458 */   private void readNbt() throws CommandSyntaxException { this.nbt = TagParser.parseCompoundAsArgument(this.reader); }
/*     */ 
/*     */   
/*     */   private <T extends Comparable<T>> void setValue(Property<T> property, String raw, int start) throws CommandSyntaxException {
/* 462 */     Optional<T> value = property.getValue(raw);
/* 463 */     if (value.isPresent()) {
/* 464 */       this.state = (BlockState)this.state.setValue(property, (Comparable)value.get());
/* 465 */       this.properties.put(property, (Comparable)value.get());
/*     */     } else {
/* 467 */       this.reader.setCursor(start);
/* 468 */       throw ERROR_INVALID_VALUE.createWithContext(this.reader, this.id.toString(), property.getName(), raw);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String serialize(BlockState state) {
/* 473 */     StringBuilder result = new StringBuilder((String)state.getBlockHolder().unwrapKey().map(r -> r.identifier().toString()).orElse("air"));
/* 474 */     if (!state.getProperties().isEmpty()) {
/* 475 */       result.append('[');
/* 476 */       boolean separate = false;
/* 477 */       for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
/* 478 */         if (separate) {
/* 479 */           result.append(',');
/*     */         }
/*     */         
/* 482 */         appendProperty(result, (Property)entry.getKey(), (Comparable)entry.getValue());
/* 483 */         separate = true;
/*     */       } 
/* 485 */       result.append(']');
/*     */     } 
/* 487 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Comparable<T>> void appendProperty(StringBuilder builder, Property<T> property, Comparable<?> value) {
/* 492 */     builder.append(property.getName());
/* 493 */     builder.append('=');
/* 494 */     builder.append(property.getName(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
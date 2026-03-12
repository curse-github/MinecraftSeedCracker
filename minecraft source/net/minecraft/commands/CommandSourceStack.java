/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.execution.TraceCallbacks;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.OutgoingChatMessage;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TaskChainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CommandSourceStack extends Object implements SharedSuggestionProvider, ExecutionCommandSource<CommandSourceStack> {
/*  53 */   public static final SimpleCommandExceptionType ERROR_NOT_PLAYER = new SimpleCommandExceptionType(Component.translatable("permissions.requires.player"));
/*  54 */   public static final SimpleCommandExceptionType ERROR_NOT_ENTITY = new SimpleCommandExceptionType(Component.translatable("permissions.requires.entity"));
/*     */   
/*     */   private final CommandSource source;
/*     */   
/*     */   private final Vec3 worldPosition;
/*     */   private final ServerLevel level;
/*     */   private final PermissionSet permissions;
/*     */   private final String textName;
/*     */   private final Component displayName;
/*     */   private final MinecraftServer server;
/*     */   private final boolean silent;
/*     */   private final Entity entity;
/*     */   private final CommandResultCallback resultCallback;
/*     */   private final EntityAnchorArgument.Anchor anchor;
/*     */   private final Vec2 rotation;
/*     */   private final CommandSigningContext signingContext;
/*     */   private final TaskChainer chatMessageChainer;
/*     */   
/*  72 */   public CommandSourceStack(CommandSource source, Vec3 position, Vec2 rotation, ServerLevel level, PermissionSet permissions, String textName, Component displayName, MinecraftServer server, Entity entity) { this(source, position, rotation, level, permissions, textName, displayName, server, entity, false, CommandResultCallback.EMPTY, EntityAnchorArgument.Anchor.FEET, CommandSigningContext.ANONYMOUS, TaskChainer.immediate(server)); }
/*     */ 
/*     */   
/*     */   private CommandSourceStack(CommandSource source, Vec3 position, Vec2 rotation, ServerLevel level, PermissionSet permissions, String textName, Component displayName, MinecraftServer server, Entity entity, boolean silent, CommandResultCallback resultCallback, EntityAnchorArgument.Anchor anchor, CommandSigningContext signingContext, TaskChainer chatMessageChainer) {
/*  76 */     this.source = source;
/*  77 */     this.worldPosition = position;
/*  78 */     this.level = level;
/*  79 */     this.silent = silent;
/*  80 */     this.entity = entity;
/*  81 */     this.permissions = permissions;
/*  82 */     this.textName = textName;
/*  83 */     this.displayName = displayName;
/*  84 */     this.server = server;
/*  85 */     this.resultCallback = resultCallback;
/*  86 */     this.anchor = anchor;
/*  87 */     this.rotation = rotation;
/*  88 */     this.signingContext = signingContext;
/*  89 */     this.chatMessageChainer = chatMessageChainer;
/*     */   }
/*     */   
/*     */   public CommandSourceStack withSource(CommandSource source) {
/*  93 */     if (this.source == source) {
/*  94 */       return this;
/*     */     }
/*  96 */     return new CommandSourceStack(source, this.worldPosition, this.rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withEntity(Entity entity) {
/* 100 */     if (this.entity == entity) {
/* 101 */       return this;
/*     */     }
/* 103 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissions, entity.getPlainTextName(), entity.getDisplayName(), this.server, entity, this.silent, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withPosition(Vec3 pos) {
/* 107 */     if (this.worldPosition.equals(pos)) {
/* 108 */       return this;
/*     */     }
/* 110 */     return new CommandSourceStack(this.source, pos, this.rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withRotation(Vec2 rotation) {
/* 114 */     if (this.rotation.equals(rotation)) {
/* 115 */       return this;
/*     */     }
/* 117 */     return new CommandSourceStack(this.source, this.worldPosition, rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandSourceStack withCallback(CommandResultCallback resultCallback) {
/* 122 */     if (Objects.equals(this.resultCallback, resultCallback)) {
/* 123 */       return this;
/*     */     }
/* 125 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withCallback(CommandResultCallback newCallback, BinaryOperator<CommandResultCallback> combiner) {
/* 129 */     CommandResultCallback newCompositeCallback = (CommandResultCallback)combiner.apply(this.resultCallback, newCallback);
/* 130 */     return withCallback(newCompositeCallback);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withSuppressedOutput() {
/* 134 */     if (this.silent || this.source.alwaysAccepts()) {
/* 135 */       return this;
/*     */     }
/* 137 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, true, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withPermission(PermissionSet permissions) {
/* 141 */     if (permissions == this.permissions) {
/* 142 */       return this;
/*     */     }
/* 144 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */ 
/*     */   
/* 148 */   public CommandSourceStack withMaximumPermission(PermissionSet newPermissions) { return withPermission(this.permissions.union(newPermissions)); }
/*     */ 
/*     */   
/*     */   public CommandSourceStack withAnchor(EntityAnchorArgument.Anchor anchor) {
/* 152 */     if (anchor == this.anchor) {
/* 153 */       return this;
/*     */     }
/* 155 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withLevel(ServerLevel level) {
/* 159 */     if (level == this.level) {
/* 160 */       return this;
/*     */     }
/* 162 */     double scale = DimensionType.getTeleportationScale(this.level.dimensionType(), level.dimensionType());
/* 163 */     Vec3 pos = new Vec3(this.worldPosition.x * scale, this.worldPosition.y, this.worldPosition.z * scale);
/* 164 */     return new CommandSourceStack(this.source, pos, this.rotation, level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, this.anchor, this.signingContext, this.chatMessageChainer);
/*     */   }
/*     */ 
/*     */   
/* 168 */   public CommandSourceStack facing(Entity entity, EntityAnchorArgument.Anchor anchor) { return facing(anchor.apply(entity)); }
/*     */ 
/*     */   
/*     */   public CommandSourceStack facing(Vec3 pos) {
/* 172 */     Vec3 from = this.anchor.apply(this);
/* 173 */     double xd = pos.x - from.x;
/* 174 */     double yd = pos.y - from.y;
/* 175 */     double zd = pos.z - from.z;
/* 176 */     double sd = Math.sqrt(xd * xd + zd * zd);
/*     */     
/* 178 */     float xRot = Mth.wrapDegrees((float)-(Mth.atan2(yd, sd) * 57.2957763671875D));
/* 179 */     float yRot = Mth.wrapDegrees((float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F);
/* 180 */     return withRotation(new Vec2(xRot, yRot));
/*     */   }
/*     */   
/*     */   public CommandSourceStack withSigningContext(CommandSigningContext signingContext, TaskChainer chatMessageChainer) {
/* 184 */     if (signingContext == this.signingContext && chatMessageChainer == this.chatMessageChainer) {
/* 185 */       return this;
/*     */     }
/* 187 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissions, this.textName, this.displayName, this.server, this.entity, this.silent, this.resultCallback, this.anchor, signingContext, chatMessageChainer);
/*     */   }
/*     */ 
/*     */   
/* 191 */   public Component getDisplayName() { return this.displayName; }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public String getTextName() { return this.textName; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public PermissionSet permissions() { return this.permissions; }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public Vec3 getPosition() { return this.worldPosition; }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public ServerLevel getLevel() { return this.level; }
/*     */ 
/*     */ 
/*     */   
/* 212 */   public Entity getEntity() { return this.entity; }
/*     */ 
/*     */   
/*     */   public Entity getEntityOrException() {
/* 216 */     if (this.entity == null) {
/* 217 */       throw ERROR_NOT_ENTITY.create();
/*     */     }
/* 219 */     return this.entity;
/*     */   }
/*     */   
/*     */   public ServerPlayer getPlayerOrException() throws CommandSyntaxException {
/* 223 */     Entity entity1 = this.entity; if (entity1 instanceof ServerPlayer) return (ServerPlayer)entity1;
/*     */ 
/*     */     
/* 226 */     throw ERROR_NOT_PLAYER.create();
/*     */   }
/*     */   
/*     */   public ServerPlayer getPlayer() throws CommandSyntaxException {
/* 230 */     Entity entity1 = this.entity; ServerPlayer player = (ServerPlayer)entity1; return (entity1 instanceof ServerPlayer) ? player : null;
/*     */   }
/*     */ 
/*     */   
/* 234 */   public boolean isPlayer() { return this.entity instanceof ServerPlayer; }
/*     */ 
/*     */ 
/*     */   
/* 238 */   public Vec2 getRotation() { return this.rotation; }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public MinecraftServer getServer() { return this.server; }
/*     */ 
/*     */ 
/*     */   
/* 246 */   public EntityAnchorArgument.Anchor getAnchor() { return this.anchor; }
/*     */ 
/*     */ 
/*     */   
/* 250 */   public CommandSigningContext getSigningContext() { return this.signingContext; }
/*     */ 
/*     */ 
/*     */   
/* 254 */   public TaskChainer getChatMessageChainer() { return this.chatMessageChainer; }
/*     */ 
/*     */   
/*     */   public boolean shouldFilterMessageTo(ServerPlayer receiver) {
/* 258 */     ServerPlayer player = getPlayer();
/* 259 */     if (receiver == player) {
/* 260 */       return false;
/*     */     }
/* 262 */     return ((player != null && player.isTextFilteringEnabled()) || receiver.isTextFilteringEnabled());
/*     */   }
/*     */   
/*     */   public void sendChatMessage(OutgoingChatMessage message, boolean filtered, ChatType.Bound chatType) {
/* 266 */     if (this.silent) {
/*     */       return;
/*     */     }
/*     */     
/* 270 */     ServerPlayer player = getPlayer();
/* 271 */     if (player != null) {
/* 272 */       player.sendChatMessage(message, filtered, chatType);
/*     */     } else {
/* 274 */       this.source.sendSystemMessage(chatType.decorate(message.content()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendSystemMessage(Component message) {
/* 279 */     if (this.silent) {
/*     */       return;
/*     */     }
/*     */     
/* 283 */     ServerPlayer player = getPlayer();
/* 284 */     if (player != null) {
/* 285 */       player.sendSystemMessage(message);
/*     */     } else {
/* 287 */       this.source.sendSystemMessage(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendSuccess(Supplier<Component> messageSupplier, boolean broadcast) {
/* 297 */     boolean shouldSendSystemMessage = (this.source.acceptsSuccess() && !this.silent);
/* 298 */     boolean shouldBroadcast = (broadcast && this.source.shouldInformAdmins() && !this.silent);
/* 299 */     if (!shouldSendSystemMessage && !shouldBroadcast) {
/*     */       return;
/*     */     }
/*     */     
/* 303 */     Component message = (Component)messageSupplier.get();
/* 304 */     if (shouldSendSystemMessage) {
/* 305 */       this.source.sendSystemMessage(message);
/*     */     }
/* 307 */     if (shouldBroadcast) {
/* 308 */       broadcastToAdmins(message);
/*     */     }
/*     */   }
/*     */   
/*     */   private void broadcastToAdmins(Component message) {
/* 313 */     MutableComponent mutableComponent = Component.translatable("chat.type.admin", new Object[] { getDisplayName(), message }).withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC });
/*     */     
/* 315 */     GameRules gameRules = this.level.getGameRules();
/* 316 */     if (((Boolean)gameRules.get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue()) {
/* 317 */       for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
/* 318 */         if (player.commandSource() != this.source && this.server.getPlayerList().isOp(player.nameAndId())) {
/* 319 */           player.sendSystemMessage(mutableComponent);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 324 */     if (this.source != this.server && ((Boolean)gameRules.get(GameRules.LOG_ADMIN_COMMANDS)).booleanValue()) {
/* 325 */       this.server.sendSystemMessage(mutableComponent);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendFailure(Component message) {
/* 330 */     if (this.source.acceptsFailure() && !this.silent) {
/* 331 */       this.source.sendSystemMessage(Component.empty().append(message).withStyle(ChatFormatting.RED));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 337 */   public CommandResultCallback callback() { return this.resultCallback; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 342 */   public Collection<String> getOnlinePlayerNames() { return Lists.newArrayList(this.server.getPlayerNames()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 347 */   public Collection<String> getAllTeams() { return this.server.getScoreboard().getTeamNames(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 352 */   public Stream<Identifier> getAvailableSounds() { return BuiltInRegistries.SOUND_EVENT.stream().map(SoundEvent::location); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 357 */   public CompletableFuture<Suggestions> customSuggestion(CommandContext<?> context) { return Suggestions.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> key, SharedSuggestionProvider.ElementSuggestionType elements, SuggestionsBuilder builder, CommandContext<?> context) {
/* 363 */     if (key == Registries.RECIPE) {
/* 364 */       return SharedSuggestionProvider.suggestResource(this.server.getRecipeManager().getRecipes().stream().map(e -> e.id().identifier()), builder);
/*     */     }
/*     */     
/* 367 */     if (key == Registries.ADVANCEMENT) {
/* 368 */       Collection<AdvancementHolder> advancements = this.server.getAdvancements().getAllAdvancements();
/* 369 */       return SharedSuggestionProvider.suggestResource(advancements.stream().map(AdvancementHolder::id), builder);
/*     */     } 
/*     */     
/* 372 */     return (CompletableFuture)getLookup(key).map(registry -> {
/* 373 */           suggestRegistryElements(registry, elements, builder);
/* 374 */           return builder.buildFuture();
/* 375 */         }).orElseGet(Suggestions::empty);
/*     */   }
/*     */   
/*     */   private Optional<? extends HolderLookup<?>> getLookup(ResourceKey<? extends Registry<?>> key) {
/* 379 */     Optional<? extends Registry<?>> lookup = registryAccess().lookup(key);
/* 380 */     if (lookup.isPresent()) {
/* 381 */       return lookup;
/*     */     }
/* 383 */     return this.server.reloadableRegistries().lookup().lookup(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 388 */   public Set<ResourceKey<Level>> levels() { return this.server.levelKeys(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 393 */   public RegistryAccess registryAccess() { return this.server.registryAccess(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 398 */   public FeatureFlagSet enabledFeatures() { return this.level.enabledFeatures(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 403 */   public CommandDispatcher<CommandSourceStack> dispatcher() { return getServer().getFunctions().getDispatcher(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleError(CommandExceptionType type, Message message, boolean forked, TraceCallbacks tracer) {
/* 408 */     if (tracer != null) {
/* 409 */       tracer.onError(message.getString());
/*     */     }
/* 411 */     if (!forked) {
/* 412 */       sendFailure(ComponentUtils.fromMessage(message));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 418 */   public boolean isSilent() { return this.silent; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\CommandSourceStack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.bossevents.CustomBossEvent;
/*     */ import net.minecraft.server.bossevents.CustomBossEvents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BossBarCommands
/*     */ {
/*  39 */   private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.bossbar.create.failed", new Object[] { id }));
/*  40 */   private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.bossbar.unknown", new Object[] { id }));
/*  41 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.players.unchanged"));
/*  42 */   private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.name.unchanged"));
/*  43 */   private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.color.unchanged"));
/*  44 */   private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.style.unchanged"));
/*  45 */   private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.value.unchanged"));
/*  46 */   private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.max.unchanged"));
/*  47 */   private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.visibility.unchanged.hidden"));
/*  48 */   private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.visibility.unchanged.visible"));
/*  49 */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR = (c, b) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack)c.getSource()).getServer().getCustomBossEvents().getIds(), b);
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  52 */     dispatcher.register(
/*  53 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("bossbar")
/*  54 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  55 */         .then(
/*  56 */           Commands.literal("add")
/*  57 */           .then(
/*  58 */             Commands.argument("id", IdentifierArgument.id())
/*  59 */             .then(
/*  60 */               Commands.argument("name", ComponentArgument.textComponent(context))
/*  61 */               .executes(c -> createBar((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), ComponentArgument.getResolvedComponent(c, "name")))))))
/*     */ 
/*     */ 
/*     */         
/*  65 */         .then(
/*  66 */           Commands.literal("remove")
/*  67 */           .then(
/*  68 */             Commands.argument("id", IdentifierArgument.id())
/*  69 */             .suggests(SUGGEST_BOSS_BAR)
/*  70 */             .executes(c -> removeBar((CommandSourceStack)c.getSource(), getBossBar(c))))))
/*     */ 
/*     */         
/*  73 */         .then(
/*  74 */           Commands.literal("list")
/*  75 */           .executes(c -> listBars((CommandSourceStack)c.getSource()))))
/*     */         
/*  77 */         .then(
/*  78 */           Commands.literal("set")
/*  79 */           .then((
/*  80 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id())
/*  81 */             .suggests(SUGGEST_BOSS_BAR)
/*  82 */             .then(
/*  83 */               Commands.literal("name")
/*  84 */               .then(
/*  85 */                 Commands.argument("name", ComponentArgument.textComponent(context))
/*  86 */                 .executes(c -> setName((CommandSourceStack)c.getSource(), getBossBar(c), ComponentArgument.getResolvedComponent(c, "name"))))))
/*     */ 
/*     */             
/*  89 */             .then((
/*  90 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color")
/*  91 */               .then(
/*  92 */                 Commands.literal("pink")
/*  93 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.PINK))))
/*     */               
/*  95 */               .then(
/*  96 */                 Commands.literal("blue")
/*  97 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.BLUE))))
/*     */               
/*  99 */               .then(
/* 100 */                 Commands.literal("red")
/* 101 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.RED))))
/*     */               
/* 103 */               .then(
/* 104 */                 Commands.literal("green")
/* 105 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.GREEN))))
/*     */               
/* 107 */               .then(
/* 108 */                 Commands.literal("yellow")
/* 109 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.YELLOW))))
/*     */               
/* 111 */               .then(
/* 112 */                 Commands.literal("purple")
/* 113 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.PURPLE))))
/*     */               
/* 115 */               .then(
/* 116 */                 Commands.literal("white")
/* 117 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarColor.WHITE)))))
/*     */ 
/*     */             
/* 120 */             .then((
/* 121 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("style")
/* 122 */               .then(
/* 123 */                 Commands.literal("progress")
/* 124 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.PROGRESS))))
/*     */               
/* 126 */               .then(
/* 127 */                 Commands.literal("notched_6")
/* 128 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_6))))
/*     */               
/* 130 */               .then(
/* 131 */                 Commands.literal("notched_10")
/* 132 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_10))))
/*     */               
/* 134 */               .then(
/* 135 */                 Commands.literal("notched_12")
/* 136 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_12))))
/*     */               
/* 138 */               .then(
/* 139 */                 Commands.literal("notched_20")
/* 140 */                 .executes(c -> setStyle((CommandSourceStack)c.getSource(), getBossBar(c), BossEvent.BossBarOverlay.NOTCHED_20)))))
/*     */ 
/*     */             
/* 143 */             .then(
/* 144 */               Commands.literal("value")
/* 145 */               .then(
/* 146 */                 Commands.argument("value", IntegerArgumentType.integer(0))
/* 147 */                 .executes(c -> setValue((CommandSourceStack)c.getSource(), getBossBar(c), IntegerArgumentType.getInteger(c, "value"))))))
/*     */ 
/*     */             
/* 150 */             .then(
/* 151 */               Commands.literal("max")
/* 152 */               .then(
/* 153 */                 Commands.argument("max", IntegerArgumentType.integer(1))
/* 154 */                 .executes(c -> setMax((CommandSourceStack)c.getSource(), getBossBar(c), IntegerArgumentType.getInteger(c, "max"))))))
/*     */ 
/*     */             
/* 157 */             .then(
/* 158 */               Commands.literal("visible")
/* 159 */               .then(
/* 160 */                 Commands.argument("visible", BoolArgumentType.bool())
/* 161 */                 .executes(c -> setVisible((CommandSourceStack)c.getSource(), getBossBar(c), BoolArgumentType.getBool(c, "visible"))))))
/*     */ 
/*     */             
/* 164 */             .then((
/* 165 */               (LiteralArgumentBuilder)Commands.literal("players")
/* 166 */               .executes(c -> setPlayers((CommandSourceStack)c.getSource(), getBossBar(c), Collections.emptyList())))
/* 167 */               .then(
/* 168 */                 Commands.argument("targets", EntityArgument.players())
/* 169 */                 .executes(c -> setPlayers((CommandSourceStack)c.getSource(), getBossBar(c), EntityArgument.getOptionalPlayers(c, "targets"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 174 */         .then(
/* 175 */           Commands.literal("get")
/* 176 */           .then((
/* 177 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id())
/* 178 */             .suggests(SUGGEST_BOSS_BAR)
/* 179 */             .then(
/* 180 */               Commands.literal("value")
/* 181 */               .executes(c -> getValue((CommandSourceStack)c.getSource(), getBossBar(c)))))
/*     */             
/* 183 */             .then(
/* 184 */               Commands.literal("max")
/* 185 */               .executes(c -> getMax((CommandSourceStack)c.getSource(), getBossBar(c)))))
/*     */             
/* 187 */             .then(
/* 188 */               Commands.literal("visible")
/* 189 */               .executes(c -> getVisible((CommandSourceStack)c.getSource(), getBossBar(c)))))
/*     */             
/* 191 */             .then(
/* 192 */               Commands.literal("players")
/* 193 */               .executes(c -> getPlayers((CommandSourceStack)c.getSource(), getBossBar(c)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getValue(CommandSourceStack source, CustomBossEvent bossBar) {
/* 201 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.get.value", new Object[] { bossBar.getDisplayName(), Integer.valueOf(bossBar.getValue()) }), true);
/* 202 */     return bossBar.getValue();
/*     */   }
/*     */   
/*     */   private static int getMax(CommandSourceStack source, CustomBossEvent bossBar) {
/* 206 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.get.max", new Object[] { bossBar.getDisplayName(), Integer.valueOf(bossBar.getMax()) }), true);
/* 207 */     return bossBar.getMax();
/*     */   }
/*     */   
/*     */   private static int getVisible(CommandSourceStack source, CustomBossEvent bossBar) {
/* 211 */     if (bossBar.isVisible()) {
/* 212 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.visible", new Object[] { bossBar.getDisplayName() }), true);
/* 213 */       return 1;
/*     */     } 
/* 215 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.hidden", new Object[] { bossBar.getDisplayName() }), true);
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getPlayers(CommandSourceStack source, CustomBossEvent bossBar) {
/* 221 */     if (bossBar.getPlayers().isEmpty()) {
/* 222 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.none", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } else {
/* 224 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.some", new Object[] { bossBar.getDisplayName(), Integer.valueOf(bossBar.getPlayers().size()), ComponentUtils.formatList(bossBar.getPlayers(), Player::getDisplayName) }), true);
/*     */     } 
/* 226 */     return bossBar.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int setVisible(CommandSourceStack source, CustomBossEvent bossBar, boolean visible) throws CommandSyntaxException {
/* 230 */     if (bossBar.isVisible() == visible) {
/* 231 */       if (visible) {
/* 232 */         throw ERROR_ALREADY_VISIBLE.create();
/*     */       }
/* 234 */       throw ERROR_ALREADY_HIDDEN.create();
/*     */     } 
/*     */     
/* 237 */     bossBar.setVisible(visible);
/* 238 */     if (visible) {
/* 239 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.visible", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } else {
/* 241 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.hidden", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } 
/* 243 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack source, CustomBossEvent bossBar, int value) throws CommandSyntaxException {
/* 247 */     if (bossBar.getValue() == value) {
/* 248 */       throw ERROR_NO_VALUE_CHANGE.create();
/*     */     }
/* 250 */     bossBar.setValue(value);
/* 251 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.value.success", new Object[] { bossBar.getDisplayName(), Integer.valueOf(value) }), true);
/* 252 */     return value;
/*     */   }
/*     */   
/*     */   private static int setMax(CommandSourceStack source, CustomBossEvent bossBar, int value) throws CommandSyntaxException {
/* 256 */     if (bossBar.getMax() == value) {
/* 257 */       throw ERROR_NO_MAX_CHANGE.create();
/*     */     }
/* 259 */     bossBar.setMax(value);
/* 260 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.max.success", new Object[] { bossBar.getDisplayName(), Integer.valueOf(value) }), true);
/* 261 */     return value;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack source, CustomBossEvent bossBar, BossEvent.BossBarColor color) throws CommandSyntaxException {
/* 265 */     if (bossBar.getColor().equals(color)) {
/* 266 */       throw ERROR_NO_COLOR_CHANGE.create();
/*     */     }
/* 268 */     bossBar.setColor(color);
/* 269 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.color.success", new Object[] { bossBar.getDisplayName() }), true);
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setStyle(CommandSourceStack source, CustomBossEvent bossBar, BossEvent.BossBarOverlay style) throws CommandSyntaxException {
/* 274 */     if (bossBar.getOverlay().equals(style)) {
/* 275 */       throw ERROR_NO_STYLE_CHANGE.create();
/*     */     }
/* 277 */     bossBar.setOverlay(style);
/* 278 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.style.success", new Object[] { bossBar.getDisplayName() }), true);
/* 279 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setName(CommandSourceStack source, CustomBossEvent bossBar, Component name) throws CommandSyntaxException {
/* 283 */     MutableComponent mutableComponent = ComponentUtils.updateForEntity(source, name, null, 0);
/* 284 */     if (bossBar.getName().equals(mutableComponent)) {
/* 285 */       throw ERROR_NO_NAME_CHANGE.create();
/*     */     }
/* 287 */     bossBar.setName(mutableComponent);
/* 288 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.set.name.success", new Object[] { bossBar.getDisplayName() }), true);
/* 289 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setPlayers(CommandSourceStack source, CustomBossEvent bossBar, Collection<ServerPlayer> targets) throws CommandSyntaxException {
/* 293 */     boolean changed = bossBar.setPlayers(targets);
/* 294 */     if (!changed) {
/* 295 */       throw ERROR_NO_PLAYER_CHANGE.create();
/*     */     }
/* 297 */     if (bossBar.getPlayers().isEmpty()) {
/* 298 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.none", new Object[] { bossBar.getDisplayName() }), true);
/*     */     } else {
/* 300 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.some", new Object[] { bossBar.getDisplayName(), Integer.valueOf(targets.size()), ComponentUtils.formatList(targets, Player::getDisplayName) }), true);
/*     */     } 
/* 302 */     return bossBar.getPlayers().size();
/*     */   }
/*     */   
/*     */   private static int listBars(CommandSourceStack source) {
/* 306 */     Collection<CustomBossEvent> events = source.getServer().getCustomBossEvents().getEvents();
/* 307 */     if (events.isEmpty()) {
/* 308 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.none"), false);
/*     */     } else {
/* 310 */       source.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.some", new Object[] { Integer.valueOf(events.size()), ComponentUtils.formatList(events, CustomBossEvent::getDisplayName) }), false);
/*     */     } 
/* 312 */     return events.size();
/*     */   }
/*     */   
/*     */   private static int createBar(CommandSourceStack source, Identifier id, Component name) throws CommandSyntaxException {
/* 316 */     CustomBossEvents events = source.getServer().getCustomBossEvents();
/* 317 */     if (events.get(id) != null) {
/* 318 */       throw ERROR_ALREADY_EXISTS.create(id.toString());
/*     */     }
/* 320 */     CustomBossEvent event = events.create(id, ComponentUtils.updateForEntity(source, name, null, 0));
/* 321 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.create.success", new Object[] { event.getDisplayName() }), true);
/* 322 */     return events.getEvents().size();
/*     */   }
/*     */   
/*     */   private static int removeBar(CommandSourceStack source, CustomBossEvent bossBar) {
/* 326 */     CustomBossEvents events = source.getServer().getCustomBossEvents();
/* 327 */     bossBar.removeAllPlayers();
/* 328 */     events.remove(bossBar);
/* 329 */     source.sendSuccess(() -> Component.translatable("commands.bossbar.remove.success", new Object[] { bossBar.getDisplayName() }), true);
/* 330 */     return events.getEvents().size();
/*     */   }
/*     */   
/*     */   public static CustomBossEvent getBossBar(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 334 */     Identifier id = IdentifierArgument.getId(context, "id");
/* 335 */     CustomBossEvent event = ((CommandSourceStack)context.getSource()).getServer().getCustomBossEvents().get(id);
/* 336 */     if (event == null) {
/* 337 */       throw ERROR_DOESNT_EXIST.create(id.toString());
/*     */     }
/* 339 */     return event;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\BossBarCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
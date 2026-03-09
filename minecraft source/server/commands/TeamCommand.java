/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.TeamArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeamCommand
/*     */ {
/*  38 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("commands.team.add.duplicate"));
/*  39 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EMPTY = new SimpleCommandExceptionType(Component.translatable("commands.team.empty.unchanged"));
/*  40 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_NAME = new SimpleCommandExceptionType(Component.translatable("commands.team.option.name.unchanged"));
/*  41 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_COLOR = new SimpleCommandExceptionType(Component.translatable("commands.team.option.color.unchanged"));
/*  42 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.friendlyfire.alreadyEnabled"));
/*  43 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.friendlyfire.alreadyDisabled"));
/*  44 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyEnabled"));
/*  45 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.seeFriendlyInvisibles.alreadyDisabled"));
/*  46 */   private static final SimpleCommandExceptionType ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.nametagVisibility.unchanged"));
/*  47 */   private static final SimpleCommandExceptionType ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.deathMessageVisibility.unchanged"));
/*  48 */   private static final SimpleCommandExceptionType ERROR_TEAM_COLLISION_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.team.option.collisionRule.unchanged"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  51 */     dispatcher.register(
/*  52 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("team")
/*  53 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  54 */         .then((
/*  55 */           (LiteralArgumentBuilder)Commands.literal("list")
/*  56 */           .executes(c -> listTeams((CommandSourceStack)c.getSource())))
/*  57 */           .then(
/*  58 */             Commands.argument("team", TeamArgument.team())
/*  59 */             .executes(c -> listMembers((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"))))))
/*     */ 
/*     */         
/*  62 */         .then(
/*  63 */           Commands.literal("add")
/*  64 */           .then((
/*  65 */             (RequiredArgumentBuilder)Commands.argument("team", StringArgumentType.word())
/*  66 */             .executes(c -> createTeam((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "team"))))
/*  67 */             .then(
/*  68 */               Commands.argument("displayName", ComponentArgument.textComponent(context))
/*  69 */               .executes(c -> createTeam((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "team"), ComponentArgument.getResolvedComponent(c, "displayName")))))))
/*     */ 
/*     */ 
/*     */         
/*  73 */         .then(
/*  74 */           Commands.literal("remove")
/*  75 */           .then(
/*  76 */             Commands.argument("team", TeamArgument.team())
/*  77 */             .executes(c -> deleteTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"))))))
/*     */ 
/*     */         
/*  80 */         .then(
/*  81 */           Commands.literal("empty")
/*  82 */           .then(
/*  83 */             Commands.argument("team", TeamArgument.team())
/*  84 */             .executes(c -> emptyTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"))))))
/*     */ 
/*     */         
/*  87 */         .then(
/*  88 */           Commands.literal("join")
/*  89 */           .then((
/*  90 */             (RequiredArgumentBuilder)Commands.argument("team", TeamArgument.team())
/*  91 */             .executes(c -> joinTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Collections.singleton(((CommandSourceStack)c.getSource()).getEntityOrException()))))
/*  92 */             .then(
/*  93 */               Commands.argument("members", ScoreHolderArgument.scoreHolders())
/*  94 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  95 */               .executes(c -> joinTeam((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "members")))))))
/*     */ 
/*     */ 
/*     */         
/*  99 */         .then(
/* 100 */           Commands.literal("leave")
/* 101 */           .then(
/* 102 */             Commands.argument("members", ScoreHolderArgument.scoreHolders())
/* 103 */             .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 104 */             .executes(c -> leaveTeam((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "members"))))))
/*     */ 
/*     */         
/* 107 */         .then(
/* 108 */           Commands.literal("modify")
/* 109 */           .then((
/* 110 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("team", TeamArgument.team())
/* 111 */             .then(
/* 112 */               Commands.literal("displayName")
/* 113 */               .then(
/* 114 */                 Commands.argument("displayName", ComponentArgument.textComponent(context))
/* 115 */                 .executes(c -> setDisplayName((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ComponentArgument.getResolvedComponent(c, "displayName"))))))
/*     */ 
/*     */             
/* 118 */             .then(
/* 119 */               Commands.literal("color")
/* 120 */               .then(
/* 121 */                 Commands.argument("value", ColorArgument.color())
/* 122 */                 .executes(c -> setColor((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ColorArgument.getColor(c, "value"))))))
/*     */ 
/*     */             
/* 125 */             .then(
/* 126 */               Commands.literal("friendlyFire")
/* 127 */               .then(
/* 128 */                 Commands.argument("allowed", BoolArgumentType.bool())
/* 129 */                 .executes(c -> setFriendlyFire((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), BoolArgumentType.getBool(c, "allowed"))))))
/*     */ 
/*     */             
/* 132 */             .then(
/* 133 */               Commands.literal("seeFriendlyInvisibles")
/* 134 */               .then(
/* 135 */                 Commands.argument("allowed", BoolArgumentType.bool())
/* 136 */                 .executes(c -> setFriendlySight((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), BoolArgumentType.getBool(c, "allowed"))))))
/*     */ 
/*     */             
/* 139 */             .then((
/* 140 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("nametagVisibility")
/* 141 */               .then(Commands.literal("never").executes(c -> setNametagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.NEVER))))
/* 142 */               .then(Commands.literal("hideForOtherTeams").executes(c -> setNametagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 143 */               .then(Commands.literal("hideForOwnTeam").executes(c -> setNametagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 144 */               .then(Commands.literal("always").executes(c -> setNametagVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 146 */             .then((
/* 147 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deathMessageVisibility")
/* 148 */               .then(Commands.literal("never").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.NEVER))))
/* 149 */               .then(Commands.literal("hideForOtherTeams").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 150 */               .then(Commands.literal("hideForOwnTeam").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 151 */               .then(Commands.literal("always").executes(c -> setDeathMessageVisibility((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 153 */             .then((
/* 154 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("collisionRule")
/* 155 */               .then(Commands.literal("never").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.NEVER))))
/* 156 */               .then(Commands.literal("pushOwnTeam").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.PUSH_OWN_TEAM))))
/* 157 */               .then(Commands.literal("pushOtherTeams").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.PUSH_OTHER_TEAMS))))
/* 158 */               .then(Commands.literal("always").executes(c -> setCollision((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), Team.CollisionRule.ALWAYS)))))
/*     */             
/* 160 */             .then(
/* 161 */               Commands.literal("prefix")
/* 162 */               .then(
/* 163 */                 Commands.argument("prefix", ComponentArgument.textComponent(context))
/* 164 */                 .executes(c -> setPrefix((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ComponentArgument.getResolvedComponent(c, "prefix"))))))
/*     */ 
/*     */             
/* 167 */             .then(
/* 168 */               Commands.literal("suffix")
/* 169 */               .then(
/* 170 */                 Commands.argument("suffix", ComponentArgument.textComponent(context))
/* 171 */                 .executes(c -> setSuffix((CommandSourceStack)c.getSource(), TeamArgument.getTeam(c, "team"), ComponentArgument.getResolvedComponent(c, "suffix"))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   private static Component getFirstMemberName(Collection<ScoreHolder> members) { return ((ScoreHolder)members.iterator().next()).getFeedbackDisplayName(); }
/*     */ 
/*     */   
/*     */   private static int leaveTeam(CommandSourceStack source, Collection<ScoreHolder> members) {
/* 184 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 186 */     for (ScoreHolder member : members) {
/* 187 */       serverScoreboard.removePlayerFromTeam(member.getScoreboardName());
/*     */     }
/*     */     
/* 190 */     if (members.size() == 1) {
/* 191 */       source.sendSuccess(() -> Component.translatable("commands.team.leave.success.single", new Object[] { getFirstMemberName(members) }), true);
/*     */     } else {
/* 193 */       source.sendSuccess(() -> Component.translatable("commands.team.leave.success.multiple", new Object[] { Integer.valueOf(members.size()) }), true);
/*     */     } 
/*     */     
/* 196 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int joinTeam(CommandSourceStack source, PlayerTeam team, Collection<ScoreHolder> members) {
/* 200 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 202 */     for (ScoreHolder member : members) {
/* 203 */       serverScoreboard.addPlayerToTeam(member.getScoreboardName(), team);
/*     */     }
/*     */     
/* 206 */     if (members.size() == 1) {
/* 207 */       source.sendSuccess(() -> Component.translatable("commands.team.join.success.single", new Object[] { getFirstMemberName(members), team.getFormattedDisplayName() }), true);
/*     */     } else {
/* 209 */       source.sendSuccess(() -> Component.translatable("commands.team.join.success.multiple", new Object[] { Integer.valueOf(members.size()), team.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 212 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int setNametagVisibility(CommandSourceStack source, PlayerTeam team, Team.Visibility visibility) throws CommandSyntaxException {
/* 216 */     if (team.getNameTagVisibility() == visibility) {
/* 217 */       throw ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED.create();
/*     */     }
/* 219 */     team.setNameTagVisibility(visibility);
/* 220 */     source.sendSuccess(() -> Component.translatable("commands.team.option.nametagVisibility.success", new Object[] { team.getFormattedDisplayName(), visibility.getDisplayName() }), true);
/* 221 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDeathMessageVisibility(CommandSourceStack source, PlayerTeam team, Team.Visibility visibility) throws CommandSyntaxException {
/* 225 */     if (team.getDeathMessageVisibility() == visibility) {
/* 226 */       throw ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED.create();
/*     */     }
/* 228 */     team.setDeathMessageVisibility(visibility);
/* 229 */     source.sendSuccess(() -> Component.translatable("commands.team.option.deathMessageVisibility.success", new Object[] { team.getFormattedDisplayName(), visibility.getDisplayName() }), true);
/* 230 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setCollision(CommandSourceStack source, PlayerTeam team, Team.CollisionRule collision) throws CommandSyntaxException {
/* 234 */     if (team.getCollisionRule() == collision) {
/* 235 */       throw ERROR_TEAM_COLLISION_UNCHANGED.create();
/*     */     }
/* 237 */     team.setCollisionRule(collision);
/* 238 */     source.sendSuccess(() -> Component.translatable("commands.team.option.collisionRule.success", new Object[] { team.getFormattedDisplayName(), collision.getDisplayName() }), true);
/* 239 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlySight(CommandSourceStack source, PlayerTeam team, boolean allowed) throws CommandSyntaxException {
/* 243 */     if (team.canSeeFriendlyInvisibles() == allowed) {
/* 244 */       if (allowed) {
/* 245 */         throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED.create();
/*     */       }
/* 247 */       throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED.create();
/*     */     } 
/*     */ 
/*     */     
/* 251 */     team.setSeeFriendlyInvisibles(allowed);
/* 252 */     source.sendSuccess(() -> Component.translatable("commands.team.option.seeFriendlyInvisibles." + (allowed ? "enabled" : "disabled"), new Object[] { team.getFormattedDisplayName() }), true);
/*     */     
/* 254 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlyFire(CommandSourceStack source, PlayerTeam team, boolean allowed) throws CommandSyntaxException {
/* 258 */     if (team.isAllowFriendlyFire() == allowed) {
/* 259 */       if (allowed) {
/* 260 */         throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED.create();
/*     */       }
/* 262 */       throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED.create();
/*     */     } 
/*     */ 
/*     */     
/* 266 */     team.setAllowFriendlyFire(allowed);
/* 267 */     source.sendSuccess(() -> Component.translatable("commands.team.option.friendlyfire." + (allowed ? "enabled" : "disabled"), new Object[] { team.getFormattedDisplayName() }), true);
/*     */     
/* 269 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack source, PlayerTeam team, Component displayName) throws CommandSyntaxException {
/* 273 */     if (team.getDisplayName().equals(displayName)) {
/* 274 */       throw ERROR_TEAM_ALREADY_NAME.create();
/*     */     }
/*     */     
/* 277 */     team.setDisplayName(displayName);
/* 278 */     source.sendSuccess(() -> Component.translatable("commands.team.option.name.success", new Object[] { team.getFormattedDisplayName() }), true);
/* 279 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack source, PlayerTeam team, ChatFormatting color) throws CommandSyntaxException {
/* 283 */     if (team.getColor() == color) {
/* 284 */       throw ERROR_TEAM_ALREADY_COLOR.create();
/*     */     }
/* 286 */     team.setColor(color);
/* 287 */     source.sendSuccess(() -> Component.translatable("commands.team.option.color.success", new Object[] { team.getFormattedDisplayName(), color.getName() }), true);
/* 288 */     return 0;
/*     */   }
/*     */   
/*     */   private static int emptyTeam(CommandSourceStack source, PlayerTeam team) throws CommandSyntaxException {
/* 292 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 293 */     Collection<String> members = Lists.newArrayList(team.getPlayers());
/*     */     
/* 295 */     if (members.isEmpty()) {
/* 296 */       throw ERROR_TEAM_ALREADY_EMPTY.create();
/*     */     }
/*     */     
/* 299 */     for (String member : members) {
/* 300 */       serverScoreboard.removePlayerFromTeam(member, team);
/*     */     }
/*     */     
/* 303 */     source.sendSuccess(() -> Component.translatable("commands.team.empty.success", new Object[] { Integer.valueOf(members.size()), team.getFormattedDisplayName() }), true);
/*     */     
/* 305 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int deleteTeam(CommandSourceStack source, PlayerTeam team) throws CommandSyntaxException {
/* 309 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 310 */     serverScoreboard.removePlayerTeam(team);
/* 311 */     source.sendSuccess(() -> Component.translatable("commands.team.remove.success", new Object[] { team.getFormattedDisplayName() }), true);
/* 312 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */ 
/*     */   
/* 316 */   private static int createTeam(CommandSourceStack source, String name) throws CommandSyntaxException { return createTeam(source, name, Component.literal(name)); }
/*     */ 
/*     */   
/*     */   private static int createTeam(CommandSourceStack source, String name, Component displayName) throws CommandSyntaxException {
/* 320 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 321 */     if (serverScoreboard.getPlayerTeam(name) != null) {
/* 322 */       throw ERROR_TEAM_ALREADY_EXISTS.create();
/*     */     }
/*     */     
/* 325 */     PlayerTeam team = serverScoreboard.addPlayerTeam(name);
/* 326 */     team.setDisplayName(displayName);
/*     */     
/* 328 */     source.sendSuccess(() -> Component.translatable("commands.team.add.success", new Object[] { team.getFormattedDisplayName() }), true);
/*     */     
/* 330 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int listMembers(CommandSourceStack source, PlayerTeam team) throws CommandSyntaxException {
/* 334 */     Collection<String> members = team.getPlayers();
/* 335 */     if (members.isEmpty()) {
/* 336 */       source.sendSuccess(() -> Component.translatable("commands.team.list.members.empty", new Object[] { team.getFormattedDisplayName() }), false);
/*     */     } else {
/* 338 */       source.sendSuccess(() -> Component.translatable("commands.team.list.members.success", new Object[] { team.getFormattedDisplayName(), Integer.valueOf(members.size()), ComponentUtils.formatList(members) }), false);
/*     */     } 
/* 340 */     return members.size();
/*     */   }
/*     */   
/*     */   private static int listTeams(CommandSourceStack source) {
/* 344 */     Collection<PlayerTeam> teams = source.getServer().getScoreboard().getPlayerTeams();
/* 345 */     if (teams.isEmpty()) {
/* 346 */       source.sendSuccess(() -> Component.translatable("commands.team.list.teams.empty"), false);
/*     */     } else {
/* 348 */       source.sendSuccess(() -> Component.translatable("commands.team.list.teams.success", new Object[] { Integer.valueOf(teams.size()), ComponentUtils.formatList(teams, PlayerTeam::getFormattedDisplayName) }), false);
/*     */     } 
/* 350 */     return teams.size();
/*     */   }
/*     */   
/*     */   private static int setPrefix(CommandSourceStack source, PlayerTeam team, Component prefix) throws CommandSyntaxException {
/* 354 */     team.setPlayerPrefix(prefix);
/* 355 */     source.sendSuccess(() -> Component.translatable("commands.team.option.prefix.success", new Object[] { prefix }), false);
/* 356 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setSuffix(CommandSourceStack source, PlayerTeam team, Component suffix) throws CommandSyntaxException {
/* 360 */     team.setPlayerSuffix(suffix);
/* 361 */     source.sendSuccess(() -> Component.translatable("commands.team.option.suffix.success", new Object[] { suffix }), false);
/* 362 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TeamCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Scoreboard
/*     */ {
/*     */   public static final String HIDDEN_SCORE_PREFIX = "#";
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   private final Object2ObjectMap<String, Objective> objectivesByName = new Object2ObjectOpenHashMap(16, 0.5F);
/*  37 */   private final Reference2ObjectMap<ObjectiveCriteria, List<Objective>> objectivesByCriteria = new Reference2ObjectOpenHashMap();
/*  38 */   private final Map<String, PlayerScores> playerScores = new Object2ObjectOpenHashMap(16, 0.5F);
/*  39 */   private final Map<DisplaySlot, Objective> displayObjectives = new EnumMap(DisplaySlot.class);
/*  40 */   private final Object2ObjectMap<String, PlayerTeam> teamsByName = new Object2ObjectOpenHashMap();
/*  41 */   private final Object2ObjectMap<String, PlayerTeam> teamsByPlayer = new Object2ObjectOpenHashMap();
/*     */ 
/*     */   
/*  44 */   public Objective getObjective(String name) { return (Objective)this.objectivesByName.get(name); }
/*     */ 
/*     */   
/*     */   public Objective addObjective(String name, ObjectiveCriteria criteria, Component displayName, ObjectiveCriteria.RenderType renderType, boolean displayAutoUpdate, NumberFormat numberFormat) {
/*  48 */     if (this.objectivesByName.containsKey(name)) {
/*  49 */       throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
/*     */     }
/*     */     
/*  52 */     Objective objective = new Objective(this, name, criteria, displayName, renderType, displayAutoUpdate, numberFormat);
/*     */     
/*  54 */     ((List)this.objectivesByCriteria.computeIfAbsent(criteria, k -> Lists.newArrayList())).add(objective);
/*  55 */     this.objectivesByName.put(name, objective);
/*  56 */     onObjectiveAdded(objective);
/*  57 */     return objective;
/*     */   }
/*     */ 
/*     */   
/*  61 */   public final void forAllObjectives(ObjectiveCriteria criteria, ScoreHolder name, Consumer<ScoreAccess> operation) { ((List)this.objectivesByCriteria.getOrDefault(criteria, Collections.emptyList())).forEach(o -> operation.accept(getOrCreatePlayerScore(name, o, true))); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   private PlayerScores getOrCreatePlayerInfo(String name) { return (PlayerScores)this.playerScores.computeIfAbsent(name, k -> new PlayerScores()); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public ScoreAccess getOrCreatePlayerScore(ScoreHolder holder, Objective objective) { return getOrCreatePlayerScore(holder, objective, false); }
/*     */ 
/*     */   
/*     */   public ScoreAccess getOrCreatePlayerScore(final ScoreHolder scoreHolder, final Objective objective, boolean forceWritable) {
/*  73 */     final boolean canModify = (forceWritable || !objective.getCriteria().isReadOnly());
/*     */     
/*  75 */     PlayerScores playerScore = getOrCreatePlayerInfo(scoreHolder.getScoreboardName());
/*  76 */     final MutableBoolean requiresSync = new MutableBoolean();
/*  77 */     final Score score = playerScore.getOrCreate(objective, newScore -> requiresSync.setTrue());
/*     */     
/*  79 */     return new ScoreAccess()
/*     */       {
/*     */         public int get() {
/*  82 */           return score.value();
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(int value) {
/*  87 */           if (!canModify) {
/*  88 */             throw new IllegalStateException("Cannot modify read-only score");
/*     */           }
/*     */           
/*  91 */           boolean hasChanged = requiresSync.isTrue();
/*     */           
/*  93 */           if (objective.displayAutoUpdate()) {
/*  94 */             Component newDisplay = scoreHolder.getDisplayName();
/*  95 */             if (newDisplay != null && !newDisplay.equals(score.display())) {
/*  96 */               score.display(newDisplay);
/*  97 */               hasChanged = true;
/*     */             } 
/*     */           } 
/*     */           
/* 101 */           if (value != score.value()) {
/* 102 */             score.value(value);
/* 103 */             hasChanged = true;
/*     */           } 
/*     */           
/* 106 */           if (hasChanged) {
/* 107 */             sendScoreToPlayers();
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 113 */         public Component display() { return score.display(); }
/*     */ 
/*     */ 
/*     */         
/*     */         public void display(Component display) {
/* 118 */           if (requiresSync.isTrue() || !Objects.equals(display, score.display())) {
/* 119 */             score.display(display);
/* 120 */             sendScoreToPlayers();
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void numberFormatOverride(NumberFormat numberFormat) {
/* 126 */           score.numberFormat(numberFormat);
/* 127 */           sendScoreToPlayers();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 132 */         public boolean locked() { return score.isLocked(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 137 */         public void unlock() { setLocked(false); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 142 */         public void lock() { setLocked(true); }
/*     */ 
/*     */         
/*     */         private void setLocked(boolean locked) {
/* 146 */           score.setLocked(locked);
/*     */           
/* 148 */           if (requiresSync.isTrue()) {
/* 149 */             sendScoreToPlayers();
/*     */           }
/*     */           
/* 152 */           Scoreboard.this.onScoreLockChanged(scoreHolder, objective);
/*     */         }
/*     */         
/*     */         private void sendScoreToPlayers() {
/* 156 */           Scoreboard.this.onScoreChanged(scoreHolder, objective, score);
/* 157 */           requiresSync.setFalse();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ReadOnlyScoreInfo getPlayerScoreInfo(ScoreHolder name, Objective objective) {
/* 163 */     PlayerScores playerScore = (PlayerScores)this.playerScores.get(name.getScoreboardName());
/* 164 */     if (playerScore != null) {
/* 165 */       return playerScore.get(objective);
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<PlayerScoreEntry> listPlayerScores(Objective objective) {
/* 171 */     List<PlayerScoreEntry> result = new ArrayList<PlayerScoreEntry>();
/*     */     
/* 173 */     this.playerScores.forEach((player, scores) -> {
/* 174 */           Score score = scores.get(objective);
/* 175 */           if (score != null) {
/* 176 */             result.add(new PlayerScoreEntry(player, score.value(), score.display(), score.numberFormat()));
/*     */           }
/*     */         });
/* 179 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 183 */   public Collection<Objective> getObjectives() { return this.objectivesByName.values(); }
/*     */ 
/*     */ 
/*     */   
/* 187 */   public Collection<String> getObjectiveNames() { return this.objectivesByName.keySet(); }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public Collection<ScoreHolder> getTrackedPlayers() { return this.playerScores.keySet().stream().map(ScoreHolder::forNameOnly).toList(); }
/*     */ 
/*     */   
/*     */   public void resetAllPlayerScores(ScoreHolder player) {
/* 195 */     PlayerScores removed = (PlayerScores)this.playerScores.remove(player.getScoreboardName());
/* 196 */     if (removed != null) {
/* 197 */       onPlayerRemoved(player);
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetSinglePlayerScore(ScoreHolder player, Objective objective) {
/* 202 */     PlayerScores scores = (PlayerScores)this.playerScores.get(player.getScoreboardName());
/* 203 */     if (scores != null) {
/* 204 */       boolean hasRemoved = scores.remove(objective);
/* 205 */       if (!scores.hasScores()) {
/* 206 */         PlayerScores removedPlayer = (PlayerScores)this.playerScores.remove(player.getScoreboardName());
/* 207 */         if (removedPlayer != null) {
/* 208 */           onPlayerRemoved(player);
/*     */         }
/* 210 */       } else if (hasRemoved) {
/* 211 */         onPlayerScoreRemoved(player, objective);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object2IntMap<Objective> listPlayerScores(ScoreHolder player) {
/* 217 */     PlayerScores scores = (PlayerScores)this.playerScores.get(player.getScoreboardName());
/* 218 */     return (scores != null) ? scores.listScores() : Object2IntMaps.emptyMap();
/*     */   }
/*     */   
/*     */   public void removeObjective(Objective objective) {
/* 222 */     this.objectivesByName.remove(objective.getName());
/*     */     
/* 224 */     for (DisplaySlot value : DisplaySlot.values()) {
/* 225 */       if (getDisplayObjective(value) == objective) {
/* 226 */         setDisplayObjective(value, null);
/*     */       }
/*     */     } 
/*     */     
/* 230 */     List<Objective> objectives = (List)this.objectivesByCriteria.get(objective.getCriteria());
/* 231 */     if (objectives != null) {
/* 232 */       objectives.remove(objective);
/*     */     }
/*     */     
/* 235 */     for (PlayerScores playerScore : this.playerScores.values()) {
/* 236 */       playerScore.remove(objective);
/*     */     }
/*     */     
/* 239 */     onObjectiveRemoved(objective);
/*     */   }
/*     */ 
/*     */   
/* 243 */   public void setDisplayObjective(DisplaySlot slot, Objective objective) { this.displayObjectives.put(slot, objective); }
/*     */ 
/*     */ 
/*     */   
/* 247 */   public Objective getDisplayObjective(DisplaySlot slot) { return (Objective)this.displayObjectives.get(slot); }
/*     */ 
/*     */ 
/*     */   
/* 251 */   public PlayerTeam getPlayerTeam(String name) { return (PlayerTeam)this.teamsByName.get(name); }
/*     */ 
/*     */   
/*     */   public PlayerTeam addPlayerTeam(String name) {
/* 255 */     PlayerTeam team = getPlayerTeam(name);
/* 256 */     if (team != null) {
/* 257 */       LOGGER.warn("Requested creation of existing team '{}'", name);
/* 258 */       return team;
/*     */     } 
/*     */     
/* 261 */     team = new PlayerTeam(this, name);
/* 262 */     this.teamsByName.put(name, team);
/* 263 */     onTeamAdded(team);
/*     */     
/* 265 */     return team;
/*     */   }
/*     */   
/*     */   public void removePlayerTeam(PlayerTeam team) {
/* 269 */     this.teamsByName.remove(team.getName());
/*     */ 
/*     */ 
/*     */     
/* 273 */     for (String player : team.getPlayers()) {
/* 274 */       this.teamsByPlayer.remove(player);
/*     */     }
/*     */     
/* 277 */     onTeamRemoved(team);
/*     */   }
/*     */   
/*     */   public boolean addPlayerToTeam(String player, PlayerTeam team) {
/* 281 */     if (getPlayersTeam(player) != null) {
/* 282 */       removePlayerFromTeam(player);
/*     */     }
/*     */     
/* 285 */     this.teamsByPlayer.put(player, team);
/* 286 */     return team.getPlayers().add(player);
/*     */   }
/*     */   
/*     */   public boolean removePlayerFromTeam(String player) {
/* 290 */     PlayerTeam team = getPlayersTeam(player);
/*     */     
/* 292 */     if (team != null) {
/* 293 */       removePlayerFromTeam(player, team);
/* 294 */       return true;
/*     */     } 
/* 296 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String player, PlayerTeam team) {
/* 301 */     if (getPlayersTeam(player) != team) {
/* 302 */       throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + team.getName() + "'.");
/*     */     }
/*     */     
/* 305 */     this.teamsByPlayer.remove(player);
/* 306 */     team.getPlayers().remove(player);
/*     */   }
/*     */ 
/*     */   
/* 310 */   public Collection<String> getTeamNames() { return this.teamsByName.keySet(); }
/*     */ 
/*     */ 
/*     */   
/* 314 */   public Collection<PlayerTeam> getPlayerTeams() { return this.teamsByName.values(); }
/*     */ 
/*     */ 
/*     */   
/* 318 */   public PlayerTeam getPlayersTeam(String name) { return (PlayerTeam)this.teamsByPlayer.get(name); }
/*     */ 
/*     */   
/*     */   public void onObjectiveAdded(Objective objective) {}
/*     */ 
/*     */   
/*     */   public void onObjectiveChanged(Objective objective) {}
/*     */ 
/*     */   
/*     */   public void onObjectiveRemoved(Objective objective) {}
/*     */ 
/*     */   
/*     */   protected void onScoreChanged(ScoreHolder owner, Objective objective, Score score) {}
/*     */ 
/*     */   
/*     */   protected void onScoreLockChanged(ScoreHolder owner, Objective objective) {}
/*     */ 
/*     */   
/*     */   public void onPlayerRemoved(ScoreHolder player) {}
/*     */ 
/*     */   
/*     */   public void onPlayerScoreRemoved(ScoreHolder player, Objective objective) {}
/*     */ 
/*     */   
/*     */   public void onTeamAdded(PlayerTeam team) {}
/*     */ 
/*     */   
/*     */   public void onTeamChanged(PlayerTeam team) {}
/*     */ 
/*     */   
/*     */   public void onTeamRemoved(PlayerTeam team) {}
/*     */ 
/*     */   
/*     */   public void entityRemoved(Entity entity) {
/* 352 */     if (entity instanceof net.minecraft.world.entity.player.Player || entity.isAlive()) {
/*     */       return;
/*     */     }
/* 355 */     resetAllPlayerScores(entity);
/* 356 */     removePlayerFromTeam(entity.getScoreboardName());
/*     */   }
/*     */ 
/*     */   
/* 360 */   protected List<PackedScore> packPlayerScores() { return this.playerScores.entrySet().stream()
/* 361 */       .flatMap(playerEntry -> {
/* 362 */           String player = (String)playerEntry.getKey();
/* 363 */           return ((PlayerScores)playerEntry.getValue()).listRawScores().entrySet().stream()
/* 364 */             .map(());
/*     */         
/* 366 */         }).toList(); }
/*     */ 
/*     */   
/*     */   protected void loadPlayerScore(PackedScore score) {
/* 370 */     Objective objective = getObjective(score.objective);
/* 371 */     if (objective == null) {
/* 372 */       LOGGER.error("Unknown objective {} for name {}, ignoring", score.objective, score.owner);
/*     */       return;
/*     */     } 
/* 375 */     getOrCreatePlayerInfo(score.owner).setScore(objective, new Score(score.score));
/*     */   }
/*     */ 
/*     */   
/* 379 */   protected List<PlayerTeam.Packed> packPlayerTeams() { return getPlayerTeams().stream().map(PlayerTeam::pack).toList(); }
/*     */ 
/*     */   
/*     */   protected void loadPlayerTeam(PlayerTeam.Packed packed) {
/* 383 */     PlayerTeam team = addPlayerTeam(packed.name());
/*     */     
/* 385 */     Objects.requireNonNull(team); packed.displayName().ifPresent(team::setDisplayName);
/* 386 */     Objects.requireNonNull(team); packed.color().ifPresent(team::setColor);
/* 387 */     team.setAllowFriendlyFire(packed.allowFriendlyFire());
/* 388 */     team.setSeeFriendlyInvisibles(packed.seeFriendlyInvisibles());
/* 389 */     team.setPlayerPrefix(packed.memberNamePrefix());
/* 390 */     team.setPlayerSuffix(packed.memberNameSuffix());
/* 391 */     team.setNameTagVisibility(packed.nameTagVisibility());
/* 392 */     team.setDeathMessageVisibility(packed.deathMessageVisibility());
/* 393 */     team.setCollisionRule(packed.collisionRule());
/*     */     
/* 395 */     for (String player : packed.players()) {
/* 396 */       addPlayerToTeam(player, team);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 401 */   protected List<Objective.Packed> packObjectives() { return getObjectives().stream().map(Objective::pack).toList(); }
/*     */ 
/*     */ 
/*     */   
/* 405 */   protected void loadObjective(Objective.Packed objective) { addObjective(objective.name(), objective.criteria(), objective.displayName(), objective.renderType(), objective.displayAutoUpdate(), (NumberFormat)objective.numberFormat().orElse(null)); }
/*     */ 
/*     */   
/*     */   protected Map<DisplaySlot, String> packDisplaySlots() {
/* 409 */     Map<DisplaySlot, String> displaySlots = new EnumMap<DisplaySlot, String>(DisplaySlot.class);
/* 410 */     for (DisplaySlot slot : DisplaySlot.values()) {
/* 411 */       Objective objective = getDisplayObjective(slot);
/* 412 */       if (objective != null) {
/* 413 */         displaySlots.put(slot, objective.getName());
/*     */       }
/*     */     } 
/* 416 */     return displaySlots;
/*     */   }
/*     */   public static final class PackedScore extends Record { private final String owner; private final String objective; private final Score.Packed score;
/* 419 */     public PackedScore(String owner, String objective, Score.Packed score) { this.owner = owner; this.objective = objective; this.score = score; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Scoreboard$PackedScore;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #419	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 419 */       //   0	7	0	this	Lnet/minecraft/world/scores/Scoreboard$PackedScore; } public String owner() { return this.owner; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Scoreboard$PackedScore;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #419	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/Scoreboard$PackedScore; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Scoreboard$PackedScore;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #419	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/scores/Scoreboard$PackedScore;
/* 419 */       //   0	8	1	o	Ljava/lang/Object; } public String objective() { return this.objective; } public Score.Packed score() { return this.score; }
/* 420 */     public static final Codec<PackedScore> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 421 */           .fieldOf("Name").forGetter(PackedScore::owner), Codec.STRING
/* 422 */           .fieldOf("Objective").forGetter(PackedScore::objective), Score.Packed.MAP_CODEC
/* 423 */           .forGetter(PackedScore::score))
/* 424 */         .apply(i, PackedScore::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Scoreboard.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
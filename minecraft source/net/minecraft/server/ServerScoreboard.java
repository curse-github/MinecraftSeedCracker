/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.scores.DisplaySlot;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerScoreEntry;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.ScoreboardSaveData;
/*     */ 
/*     */ public class ServerScoreboard extends Scoreboard {
/*     */   private final MinecraftServer server;
/*     */   
/*     */   public ServerScoreboard(MinecraftServer server) {
/*  30 */     this.trackedObjectives = Sets.newHashSet();
/*     */ 
/*     */ 
/*     */     
/*  34 */     this.server = server;
/*     */   }
/*     */   private final Set<Objective> trackedObjectives; private boolean dirty;
/*     */   public void load(ScoreboardSaveData.Packed data) {
/*  38 */     data.objectives().forEach(x$0 -> rec$.loadObjective(x$0));
/*  39 */     data.scores().forEach(x$0 -> rec$.loadPlayerScore(x$0));
/*  40 */     data.displaySlots().forEach((slot, name) -> {
/*  41 */           Objective objective = getObjective(name);
/*  42 */           setDisplayObjective(slot, objective);
/*     */         });
/*  44 */     data.teams().forEach(x$0 -> rec$.loadPlayerTeam(x$0));
/*     */   }
/*     */ 
/*     */   
/*  48 */   private ScoreboardSaveData.Packed store() { return new ScoreboardSaveData.Packed(packObjectives(), packPlayerScores(), packDisplaySlots(), packPlayerTeams()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onScoreChanged(ScoreHolder owner, Objective objective, Score score) {
/*  53 */     super.onScoreChanged(owner, objective, score);
/*     */     
/*  55 */     if (this.trackedObjectives.contains(objective)) {
/*  56 */       this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(owner.getScoreboardName(), objective.getName(), score.value(), Optional.ofNullable(score.display()), Optional.ofNullable(score.numberFormat())));
/*     */     }
/*     */     
/*  59 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onScoreLockChanged(ScoreHolder owner, Objective objective) {
/*  64 */     super.onScoreLockChanged(owner, objective);
/*     */     
/*  66 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerRemoved(ScoreHolder player) {
/*  71 */     super.onPlayerRemoved(player);
/*  72 */     this.server.getPlayerList().broadcastAll(new ClientboundResetScorePacket(player.getScoreboardName(), null));
/*  73 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerScoreRemoved(ScoreHolder player, Objective objective) {
/*  78 */     super.onPlayerScoreRemoved(player, objective);
/*  79 */     if (this.trackedObjectives.contains(objective)) {
/*  80 */       this.server.getPlayerList().broadcastAll(new ClientboundResetScorePacket(player.getScoreboardName(), objective.getName()));
/*     */     }
/*  82 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisplayObjective(DisplaySlot slot, Objective objective) {
/*  87 */     Objective old = getDisplayObjective(slot);
/*     */     
/*  89 */     super.setDisplayObjective(slot, objective);
/*     */     
/*  91 */     if (old != objective && old != null) {
/*  92 */       if (getObjectiveDisplaySlotCount(old) > 0) {
/*  93 */         this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(slot, objective));
/*     */       } else {
/*  95 */         stopTrackingObjective(old);
/*     */       } 
/*     */     }
/*     */     
/*  99 */     if (objective != null) {
/* 100 */       if (this.trackedObjectives.contains(objective)) {
/* 101 */         this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(slot, objective));
/*     */       } else {
/* 103 */         startTrackingObjective(objective);
/*     */       } 
/*     */     }
/*     */     
/* 107 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPlayerToTeam(String player, PlayerTeam team) {
/* 112 */     if (super.addPlayerToTeam(player, team)) {
/* 113 */       this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createPlayerPacket(team, player, ClientboundSetPlayerTeamPacket.Action.ADD));
/* 114 */       updatePlayerWaypoint(player);
/* 115 */       setDirty();
/*     */       
/* 117 */       return true;
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String player, PlayerTeam team) {
/* 125 */     super.removePlayerFromTeam(player, team);
/*     */     
/* 127 */     this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createPlayerPacket(team, player, ClientboundSetPlayerTeamPacket.Action.REMOVE));
/* 128 */     updatePlayerWaypoint(player);
/* 129 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveAdded(Objective objective) {
/* 134 */     super.onObjectiveAdded(objective);
/* 135 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveChanged(Objective objective) {
/* 140 */     super.onObjectiveChanged(objective);
/*     */     
/* 142 */     if (this.trackedObjectives.contains(objective)) {
/* 143 */       this.server.getPlayerList().broadcastAll(new ClientboundSetObjectivePacket(objective, 2));
/*     */     }
/*     */     
/* 146 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveRemoved(Objective objective) {
/* 151 */     super.onObjectiveRemoved(objective);
/*     */     
/* 153 */     if (this.trackedObjectives.contains(objective)) {
/* 154 */       stopTrackingObjective(objective);
/*     */     }
/*     */     
/* 157 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamAdded(PlayerTeam team) {
/* 162 */     super.onTeamAdded(team);
/*     */     
/* 164 */     this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
/*     */     
/* 166 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamChanged(PlayerTeam team) {
/* 171 */     super.onTeamChanged(team);
/*     */     
/* 173 */     this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false));
/* 174 */     updateTeamWaypoints(team);
/* 175 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamRemoved(PlayerTeam team) {
/* 180 */     super.onTeamRemoved(team);
/*     */     
/* 182 */     this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createRemovePacket(team));
/* 183 */     updateTeamWaypoints(team);
/* 184 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/* 188 */   protected void setDirty() { this.dirty = true; }
/*     */ 
/*     */   
/*     */   public void storeToSaveDataIfDirty(ScoreboardSaveData saveData) {
/* 192 */     if (this.dirty) {
/* 193 */       this.dirty = false;
/* 194 */       saveData.setData(store());
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Packet<?>> getStartTrackingPackets(Objective objective) {
/* 199 */     List<Packet<?>> packets = Lists.newArrayList();
/* 200 */     packets.add(new ClientboundSetObjectivePacket(objective, 0));
/*     */     
/* 202 */     for (DisplaySlot slot : DisplaySlot.values()) {
/* 203 */       if (getDisplayObjective(slot) == objective) {
/* 204 */         packets.add(new ClientboundSetDisplayObjectivePacket(slot, objective));
/*     */       }
/*     */     } 
/*     */     
/* 208 */     for (PlayerScoreEntry score : listPlayerScores(objective)) {
/* 209 */       packets.add(new ClientboundSetScorePacket(score.owner(), objective.getName(), score.value(), Optional.ofNullable(score.display()), Optional.ofNullable(score.numberFormatOverride())));
/*     */     }
/*     */     
/* 212 */     return packets;
/*     */   }
/*     */   
/*     */   public void startTrackingObjective(Objective objective) {
/* 216 */     List<Packet<?>> packets = getStartTrackingPackets(objective);
/*     */     
/* 218 */     for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
/* 219 */       for (Packet<?> packet : packets) {
/* 220 */         player.connection.send(packet);
/*     */       }
/*     */     } 
/*     */     
/* 224 */     this.trackedObjectives.add(objective);
/*     */   }
/*     */   
/*     */   public List<Packet<?>> getStopTrackingPackets(Objective objective) {
/* 228 */     List<Packet<?>> packets = Lists.newArrayList();
/* 229 */     packets.add(new ClientboundSetObjectivePacket(objective, 1));
/*     */     
/* 231 */     for (DisplaySlot slot : DisplaySlot.values()) {
/* 232 */       if (getDisplayObjective(slot) == objective) {
/* 233 */         packets.add(new ClientboundSetDisplayObjectivePacket(slot, objective));
/*     */       }
/*     */     } 
/*     */     
/* 237 */     return packets;
/*     */   }
/*     */   
/*     */   public void stopTrackingObjective(Objective objective) {
/* 241 */     List<Packet<?>> packets = getStopTrackingPackets(objective);
/*     */     
/* 243 */     for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
/* 244 */       for (Packet<?> packet : packets) {
/* 245 */         player.connection.send(packet);
/*     */       }
/*     */     } 
/*     */     
/* 249 */     this.trackedObjectives.remove(objective);
/*     */   }
/*     */   
/*     */   public int getObjectiveDisplaySlotCount(Objective objective) {
/* 253 */     int count = 0;
/*     */     
/* 255 */     for (DisplaySlot slot : DisplaySlot.values()) {
/* 256 */       if (getDisplayObjective(slot) == objective) {
/* 257 */         count++;
/*     */       }
/*     */     } 
/*     */     
/* 261 */     return count;
/*     */   }
/*     */   
/*     */   private void updatePlayerWaypoint(String player) {
/* 265 */     ServerPlayer serverPlayer = this.server.getPlayerList().getPlayerByName(player);
/* 266 */     if (serverPlayer != null) {
/* 267 */       serverPlayer.level().getWaypointManager().remakeConnections(serverPlayer);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateTeamWaypoints(PlayerTeam team) {
/* 272 */     for (ServerLevel level : this.server.getAllLevels())
/* 273 */       team.getPlayers().stream()
/* 274 */         .map(name -> this.server.getPlayerList().getPlayerByName(name))
/* 275 */         .filter(Objects::nonNull)
/* 276 */         .forEach(player -> level.getWaypointManager().remakeConnections(player)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerScoreboard.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
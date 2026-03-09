/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.AdvancementTree;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.CriterionTrigger;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.advancements.AdvancementVisibilityEvaluator;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class PlayerAdvancements
/*     */ {
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  50 */   private static final Gson GSON = (new GsonBuilder())
/*  51 */     .setPrettyPrinting()
/*  52 */     .create(); private final PlayerList playerList; private final Path playerSavePath; private AdvancementTree tree; private final Map<AdvancementHolder, AdvancementProgress> progress;
/*     */   private final Set<AdvancementHolder> visible;
/*     */   
/*     */   public PlayerAdvancements(DataFixer dataFixer, PlayerList playerList, ServerAdvancementManager manager, Path playerSavePath, ServerPlayer player) {
/*  56 */     this.progress = new LinkedHashMap();
/*  57 */     this.visible = new HashSet();
/*  58 */     this.progressChanged = new HashSet();
/*  59 */     this.rootsToUpdate = new HashSet();
/*     */ 
/*     */     
/*  62 */     this.isFirstPacket = true;
/*     */ 
/*     */ 
/*     */     
/*  66 */     this.playerList = playerList;
/*  67 */     this.playerSavePath = playerSavePath;
/*  68 */     this.player = player;
/*  69 */     this.tree = manager.tree();
/*     */     
/*  71 */     int defaultVersion = 1343;
/*  72 */     this.codec = DataFixTypes.ADVANCEMENTS.wrapCodec(Data.CODEC, dataFixer, 1343);
/*  73 */     load(manager);
/*     */   }
/*     */   private final Set<AdvancementHolder> progressChanged; private final Set<AdvancementNode> rootsToUpdate; private ServerPlayer player; private AdvancementHolder lastSelectedTab; private boolean isFirstPacket; private final Codec<Data> codec;
/*     */   
/*  77 */   public void setPlayer(ServerPlayer player) { this.player = player; }
/*     */ 
/*     */   
/*     */   public void stopListening() {
/*  81 */     for (CriterionTrigger<?> trigger : BuiltInRegistries.TRIGGER_TYPES) {
/*  82 */       trigger.removePlayerListeners(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reload(ServerAdvancementManager manager) {
/*  87 */     stopListening();
/*  88 */     this.progress.clear();
/*  89 */     this.visible.clear();
/*  90 */     this.rootsToUpdate.clear();
/*  91 */     this.progressChanged.clear();
/*  92 */     this.isFirstPacket = true;
/*  93 */     this.lastSelectedTab = null;
/*  94 */     this.tree = manager.tree();
/*  95 */     load(manager);
/*     */   }
/*     */   
/*     */   private void registerListeners(ServerAdvancementManager manager) {
/*  99 */     for (AdvancementHolder advancement : manager.getAllAdvancements()) {
/* 100 */       registerListeners(advancement);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkForAutomaticTriggers(ServerAdvancementManager manager) {
/* 105 */     for (AdvancementHolder holder : manager.getAllAdvancements()) {
/* 106 */       Advancement advancement = holder.value();
/* 107 */       if (advancement.criteria().isEmpty()) {
/* 108 */         award(holder, "");
/* 109 */         advancement.rewards().grant(this.player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void load(ServerAdvancementManager manager) {
/* 115 */     if (Files.isRegularFile(this.playerSavePath, new java.nio.file.LinkOption[0])) {
/* 116 */       try { Reader reader = Files.newBufferedReader(this.playerSavePath, StandardCharsets.UTF_8); 
/* 117 */         try { JsonElement json = StrictJsonParser.parse(reader);
/* 118 */           Data data = (Data)this.codec.parse(JsonOps.INSTANCE, json).getOrThrow(JsonParseException::new);
/* 119 */           applyFrom(manager, data);
/* 120 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonIOException e)
/* 121 */       { LOGGER.error("Couldn't access player advancements in {}", this.playerSavePath, e); }
/* 122 */       catch (JsonParseException e)
/* 123 */       { LOGGER.error("Couldn't parse player advancements in {}", this.playerSavePath, e); }
/*     */     
/*     */     }
/*     */     
/* 127 */     checkForAutomaticTriggers(manager);
/* 128 */     registerListeners(manager);
/*     */   }
/*     */   
/*     */   public void save() {
/* 132 */     JsonElement json = (JsonElement)this.codec.encodeStart(JsonOps.INSTANCE, asData()).getOrThrow();
/*     */     
/* 134 */     try { FileUtil.createDirectoriesSafe(this.playerSavePath.getParent());
/* 135 */       Writer outputWriter = Files.newBufferedWriter(this.playerSavePath, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/* 136 */       try { GSON.toJson(json, GSON.newJsonWriter(outputWriter));
/* 137 */         if (outputWriter != null) outputWriter.close();  } catch (Throwable throwable) { if (outputWriter != null)
/* 138 */           try { outputWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonIOException e)
/* 139 */     { LOGGER.error("Couldn't save player advancements to {}", this.playerSavePath, e); }
/*     */   
/*     */   }
/*     */   
/*     */   private void applyFrom(ServerAdvancementManager manager, Data data) {
/* 144 */     data.forEach((id, progress) -> {
/* 145 */           AdvancementHolder advancement = manager.get(id);
/* 146 */           if (advancement == null) {
/* 147 */             LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", id, this.playerSavePath);
/*     */             return;
/*     */           } 
/* 150 */           startProgress(advancement, progress);
/* 151 */           this.progressChanged.add(advancement);
/* 152 */           markForVisibilityUpdate(advancement);
/*     */         });
/*     */   }
/*     */   
/*     */   private Data asData() {
/* 157 */     Map<Identifier, AdvancementProgress> map = new LinkedHashMap<Identifier, AdvancementProgress>();
/* 158 */     this.progress.forEach((advancement, progress) -> {
/* 159 */           if (progress.hasProgress()) {
/* 160 */             map.put(advancement.id(), progress);
/*     */           }
/*     */         });
/* 163 */     return new Data(map);
/*     */   }
/*     */   
/*     */   public boolean award(AdvancementHolder holder, String criterion) {
/* 167 */     boolean result = false;
/*     */     
/* 169 */     AdvancementProgress progress = getOrStartProgress(holder);
/* 170 */     boolean wasDone = progress.isDone();
/*     */     
/* 172 */     if (progress.grantProgress(criterion)) {
/* 173 */       unregisterListeners(holder);
/* 174 */       this.progressChanged.add(holder);
/* 175 */       result = true;
/*     */       
/* 177 */       if (!wasDone && progress.isDone()) {
/* 178 */         holder.value().rewards().grant(this.player);
/* 179 */         holder.value().display().ifPresent(display -> {
/* 180 */               if (display.shouldAnnounceChat() && ((Boolean)this.player.level().getGameRules().get(GameRules.SHOW_ADVANCEMENT_MESSAGES)).booleanValue()) {
/* 181 */                 this.playerList.broadcastSystemMessage(display.getType().createAnnouncement(holder, this.player), false);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     if (!wasDone && progress.isDone()) {
/* 188 */       markForVisibilityUpdate(holder);
/*     */     }
/*     */     
/* 191 */     return result;
/*     */   }
/*     */   
/*     */   public boolean revoke(AdvancementHolder advancement, String criterion) {
/* 195 */     boolean result = false;
/*     */     
/* 197 */     AdvancementProgress progress = getOrStartProgress(advancement);
/* 198 */     boolean wasDone = progress.isDone();
/* 199 */     if (progress.revokeProgress(criterion)) {
/* 200 */       registerListeners(advancement);
/* 201 */       this.progressChanged.add(advancement);
/* 202 */       result = true;
/*     */     } 
/*     */     
/* 205 */     if (wasDone && !progress.isDone()) {
/* 206 */       markForVisibilityUpdate(advancement);
/*     */     }
/*     */     
/* 209 */     return result;
/*     */   }
/*     */   
/*     */   private void markForVisibilityUpdate(AdvancementHolder advancement) {
/* 213 */     AdvancementNode node = this.tree.get(advancement);
/* 214 */     if (node != null) {
/* 215 */       this.rootsToUpdate.add(node.root());
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerListeners(AdvancementHolder holder) {
/* 220 */     AdvancementProgress advancementProgress = getOrStartProgress(holder);
/* 221 */     if (advancementProgress.isDone()) {
/*     */       return;
/*     */     }
/* 224 */     for (Map.Entry<String, Criterion<?>> entry : holder.value().criteria().entrySet()) {
/* 225 */       CriterionProgress criterionProgress = advancementProgress.getCriterion((String)entry.getKey());
/* 226 */       if (criterionProgress == null || criterionProgress.isDone()) {
/*     */         continue;
/*     */       }
/* 229 */       registerListener(holder, (String)entry.getKey(), (Criterion)entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 234 */   private <T extends net.minecraft.advancements.CriterionTriggerInstance> void registerListener(AdvancementHolder holder, String key, Criterion<T> criterion) { criterion.trigger().addPlayerListener(this, new CriterionTrigger.Listener(criterion.triggerInstance(), holder, key)); }
/*     */ 
/*     */   
/*     */   private void unregisterListeners(AdvancementHolder holder) {
/* 238 */     AdvancementProgress advancementProgress = getOrStartProgress(holder);
/* 239 */     for (Map.Entry<String, Criterion<?>> entry : holder.value().criteria().entrySet()) {
/* 240 */       CriterionProgress criterionProgress = advancementProgress.getCriterion((String)entry.getKey());
/* 241 */       if (criterionProgress == null || (!criterionProgress.isDone() && !advancementProgress.isDone())) {
/*     */         continue;
/*     */       }
/* 244 */       removeListener(holder, (String)entry.getKey(), (Criterion)entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 249 */   private <T extends net.minecraft.advancements.CriterionTriggerInstance> void removeListener(AdvancementHolder holder, String key, Criterion<T> criterion) { criterion.trigger().removePlayerListener(this, new CriterionTrigger.Listener(criterion.triggerInstance(), holder, key)); }
/*     */ 
/*     */   
/*     */   public void flushDirty(ServerPlayer player, boolean showAdvancements) {
/* 253 */     if (this.isFirstPacket || !this.rootsToUpdate.isEmpty() || !this.progressChanged.isEmpty()) {
/* 254 */       Map<Identifier, AdvancementProgress> progress = new HashMap<Identifier, AdvancementProgress>();
/* 255 */       Set<AdvancementHolder> added = new HashSet<AdvancementHolder>();
/* 256 */       Set<Identifier> removed = new HashSet<Identifier>();
/*     */       
/* 258 */       for (AdvancementNode root : this.rootsToUpdate) {
/* 259 */         updateTreeVisibility(root, added, removed);
/*     */       }
/* 261 */       this.rootsToUpdate.clear();
/*     */       
/* 263 */       for (AdvancementHolder holder : this.progressChanged) {
/* 264 */         if (this.visible.contains(holder)) {
/* 265 */           progress.put(holder.id(), (AdvancementProgress)this.progress.get(holder));
/*     */         }
/*     */       } 
/* 268 */       this.progressChanged.clear();
/*     */       
/* 270 */       if (!progress.isEmpty() || !added.isEmpty() || !removed.isEmpty()) {
/* 271 */         player.connection.send(new ClientboundUpdateAdvancementsPacket(this.isFirstPacket, added, removed, progress, showAdvancements));
/*     */       }
/*     */     } 
/* 274 */     this.isFirstPacket = false;
/*     */   }
/*     */   
/*     */   public void setSelectedTab(AdvancementHolder holder) {
/* 278 */     AdvancementHolder old = this.lastSelectedTab;
/* 279 */     if (holder != null && holder.value().isRoot() && holder.value().display().isPresent()) {
/* 280 */       this.lastSelectedTab = holder;
/*     */     } else {
/* 282 */       this.lastSelectedTab = null;
/*     */     } 
/* 284 */     if (old != this.lastSelectedTab) {
/* 285 */       this.player.connection.send(new ClientboundSelectAdvancementsTabPacket((this.lastSelectedTab == null) ? null : this.lastSelectedTab.id()));
/*     */     }
/*     */   }
/*     */   
/*     */   public AdvancementProgress getOrStartProgress(AdvancementHolder advancement) {
/* 290 */     AdvancementProgress progress = (AdvancementProgress)this.progress.get(advancement);
/* 291 */     if (progress == null) {
/* 292 */       progress = new AdvancementProgress();
/* 293 */       startProgress(advancement, progress);
/*     */     } 
/* 295 */     return progress;
/*     */   }
/*     */   
/*     */   private void startProgress(AdvancementHolder holder, AdvancementProgress progress) {
/* 299 */     progress.update(holder.value().requirements());
/* 300 */     this.progress.put(holder, progress);
/*     */   }
/*     */   
/*     */   private void updateTreeVisibility(AdvancementNode root, Set<AdvancementHolder> added, Set<Identifier> removed) {
/* 304 */     AdvancementVisibilityEvaluator.evaluateVisibility(root, node -> 
/*     */         
/* 306 */         getOrStartProgress(node.holder()).isDone(), (node, shouldBeVisible) -> {
/*     */           
/* 308 */           AdvancementHolder advancement = node.holder();
/* 309 */           if (shouldBeVisible) {
/* 310 */             if (this.visible.add(advancement)) {
/* 311 */               added.add(advancement);
/* 312 */               if (this.progress.containsKey(advancement)) {
/* 313 */                 this.progressChanged.add(advancement);
/*     */               }
/*     */             }
/*     */           
/* 317 */           } else if (this.visible.remove(advancement)) {
/* 318 */             removed.add(advancement.id());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static final class Data extends Record { private final Map<Identifier, AdvancementProgress> map;
/*     */     
/* 325 */     private Data(Map<Identifier, AdvancementProgress> map) { this.map = map; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/PlayerAdvancements$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #325	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$Data; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/PlayerAdvancements$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #325	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/PlayerAdvancements$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #325	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/* 325 */       //   0	8	1	o	Ljava/lang/Object; } public Map<Identifier, AdvancementProgress> map() { return this.map; }
/* 326 */     public static final Codec<Data> CODEC = Codec.unboundedMap(Identifier.CODEC, AdvancementProgress.CODEC).xmap(Data::new, Data::map);
/*     */ 
/*     */     
/* 329 */     public void forEach(BiConsumer<Identifier, AdvancementProgress> consumer) { this.map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> consumer.accept((Identifier)entry.getKey(), (AdvancementProgress)entry.getValue())); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\PlayerAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerStatsCounter
/*     */   extends StatsCounter
/*     */ {
/*  43 */   private static final Gson GSON = (new GsonBuilder())
/*  44 */     .setPrettyPrinting()
/*  45 */     .create();
/*     */   
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  49 */   private static final Codec<Map<Stat<?>, Integer>> STATS_CODEC = Codec.dispatchedMap(BuiltInRegistries.STAT_TYPE
/*  50 */       .byNameCodec(), 
/*  51 */       Util.memoize(ServerStatsCounter::createTypedStatsCodec))
/*  52 */     .xmap(groupedStats -> {
/*     */         
/*  54 */         Map<Stat<?>, Integer> stats = new HashMap<Stat<?>, Integer>();
/*  55 */         groupedStats.forEach(());
/*  56 */         return stats;
/*     */ 
/*     */       
/*  59 */       }map -> (Map)map.entrySet().stream().collect(Collectors.groupingBy((), 
/*     */           
/*  61 */           Util.toMap())));
/*     */   
/*     */   private final Path file;
/*     */   private final Set<Stat<?>> dirty;
/*     */   
/*     */   private static <T> Codec<Map<Stat<?>, Integer>> createTypedStatsCodec(StatType<T> type) {
/*  67 */     Codec<T> valueCodec = type.getRegistry().byNameCodec();
/*  68 */     Objects.requireNonNull(type); Codec<Stat<?>> statCodec = valueCodec.flatComapMap(type::get, stat -> {
/*  69 */           if (stat.getType() == type) {
/*  70 */             return DataResult.success(stat.getValue());
/*     */           }
/*  72 */           return DataResult.error(());
/*     */         });
/*     */     
/*  75 */     return Codec.unboundedMap(statCodec, Codec.INT);
/*     */   }
/*     */   
/*     */   public ServerStatsCounter(MinecraftServer server, Path file) {
/*  79 */     this.dirty = Sets.newHashSet();
/*     */ 
/*     */     
/*  82 */     this.file = file;
/*  83 */     if (Files.isRegularFile(file, new java.nio.file.LinkOption[0])) {
/*  84 */       try { Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8); 
/*  85 */         try { JsonElement element = StrictJsonParser.parse(reader);
/*  86 */           parse(server.getFixerUpper(), element);
/*  87 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  88 */       { LOGGER.error("Couldn't read statistics file {}", file, e); }
/*  89 */       catch (JsonParseException e)
/*  90 */       { LOGGER.error("Couldn't parse statistics file {}", file, e); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public void save() {
/*     */     
/*  97 */     try { FileUtil.createDirectoriesSafe(this.file.getParent());
/*  98 */       Writer writer = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/*  99 */       try { GSON.toJson(toJson(), GSON.newJsonWriter(writer));
/* 100 */         if (writer != null) writer.close();  } catch (Throwable throwable) { if (writer != null)
/* 101 */           try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonIOException e)
/* 102 */     { LOGGER.error("Couldn't save stats to {}", this.file, e); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Player player, Stat<?> stat, int count) {
/* 108 */     super.setValue(player, stat, count);
/* 109 */     this.dirty.add(stat);
/*     */   }
/*     */   
/*     */   private Set<Stat<?>> getDirty() {
/* 113 */     Set<Stat<?>> result = Sets.newHashSet(this.dirty);
/* 114 */     this.dirty.clear();
/* 115 */     return result;
/*     */   }
/*     */   
/*     */   public void parse(DataFixer fixerUpper, JsonElement element) {
/* 119 */     Dynamic<JsonElement> data = new Dynamic<JsonElement>(JsonOps.INSTANCE, element);
/*     */     
/* 121 */     data = DataFixTypes.STATS.updateToCurrentVersion(fixerUpper, data, NbtUtils.getDataVersion(data, 1343));
/*     */     
/* 123 */     this.stats.putAll((Map)STATS_CODEC.parse(data.get("stats").orElseEmptyMap())
/* 124 */         .resultOrPartial(error -> LOGGER.error("Failed to parse statistics for {}: {}", this.file, error))
/* 125 */         .orElse(Map.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected JsonElement toJson() {
/* 130 */     JsonObject result = new JsonObject();
/* 131 */     result.add("stats", (JsonElement)STATS_CODEC.encodeStart(JsonOps.INSTANCE, this.stats).getOrThrow());
/* 132 */     result.addProperty("DataVersion", Integer.valueOf(SharedConstants.getCurrentVersion().dataVersion().version()));
/* 133 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 137 */   public void markAllDirty() { this.dirty.addAll(this.stats.keySet()); }
/*     */ 
/*     */   
/*     */   public void sendStats(ServerPlayer player) {
/* 141 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 143 */     for (Stat<?> stat : getDirty()) {
/* 144 */       object2IntOpenHashMap.put(stat, getValue(stat));
/*     */     }
/*     */     
/* 147 */     player.connection.send(new ClientboundAwardStatsPacket(object2IntOpenHashMap));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\ServerStatsCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
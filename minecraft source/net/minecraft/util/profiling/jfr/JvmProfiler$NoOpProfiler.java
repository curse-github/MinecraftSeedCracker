/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import org.slf4j.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoOpProfiler
/*     */   implements JvmProfiler
/*     */ {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean start(Environment environment) {
/*  59 */     LOGGER.warn("Attempted to start Flight Recorder, but it's not supported on this JVM");
/*  60 */     return false;
/*     */   }
/*     */   static final ProfiledDuration noOpCommit = ignored -> {
/*     */     
/*     */     };
/*  65 */   public Path stop() { throw new IllegalStateException("Attempted to stop Flight Recorder, but it's not supported on this JVM"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public boolean isRunning() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public boolean isAvailable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPacketReceived(ConnectionProtocol protocol, PacketType<?> packetId, SocketAddress remoteAddress, int readableBytes) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPacketSent(ConnectionProtocol protocol, PacketType<?> packetId, SocketAddress remoteAddress, int writtenBytes) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRegionFileRead(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int readBytes) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRegionFileWrite(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int writtenBytes) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onServerTick(float averageTickTime) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClientTick(int fps) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public ProfiledDuration onWorldLoadedStarted() { return noOpCommit; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public ProfiledDuration onChunkGenerate(ChunkPos pos, ResourceKey<Level> dimension, String name) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public ProfiledDuration onStructureGenerate(ChunkPos sourceChunkPos, ResourceKey<Level> dimension, Holder<Structure> structure) { return noOpCommit; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\JvmProfiler$NoOpProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
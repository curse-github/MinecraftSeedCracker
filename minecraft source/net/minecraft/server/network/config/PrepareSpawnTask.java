/*     */ package net.minecraft.server.network.config;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ChunkLoadCounter;
/*     */ import net.minecraft.server.level.PlayerSpawnFinder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.network.CommonListenerCookie;
/*     */ import net.minecraft.server.network.ConfigurationTask;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PrepareSpawnTask
/*     */   implements ConfigurationTask {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  35 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("prepare_spawn");
/*     */   
/*     */   public static final int PREPARE_CHUNK_RADIUS = 3;
/*     */   
/*     */   private final MinecraftServer server;
/*     */   private final NameAndId nameAndId;
/*     */   private final LevelLoadListener loadListener;
/*     */   private State state;
/*     */   
/*     */   public PrepareSpawnTask(MinecraftServer server, NameAndId nameAndId) {
/*  45 */     this.server = server;
/*  46 */     this.nameAndId = nameAndId;
/*  47 */     this.loadListener = server.getLevelLoadListener();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(Consumer<Packet<?>> connection) {
/*  52 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*     */     try {
/*  54 */       Optional<ValueInput> loadedData = this.server.getPlayerList().loadPlayerData(this.nameAndId).map(tag -> TagValueInput.create(reporter, this.server.registryAccess(), tag));
/*     */ 
/*     */ 
/*     */       
/*  58 */       ServerPlayer.SavedPosition loadedPosition = (ServerPlayer.SavedPosition)loadedData.flatMap(tag -> tag.read(ServerPlayer.SavedPosition.MAP_CODEC)).orElse(ServerPlayer.SavedPosition.EMPTY);
/*     */       
/*  60 */       LevelData.RespawnData respawnData = this.server.getWorldData().overworldData().getRespawnData();
/*  61 */       Objects.requireNonNull(this.server); ServerLevel spawnLevel = (ServerLevel)loadedPosition.dimension().map(this.server::getLevel).orElseGet(() -> {
/*  62 */             ServerLevel spawnDataLevel = this.server.getLevel(respawnData.dimension());
/*  63 */             return (spawnDataLevel != null) ? spawnDataLevel : this.server.overworld();
/*     */           });
/*     */       
/*  66 */       CompletableFuture<Vec3> spawnPosition = (CompletableFuture)loadedPosition.position().map(CompletableFuture::completedFuture).orElseGet(() -> PlayerSpawnFinder.findSpawn(spawnLevel, respawnData.pos()));
/*  67 */       Vec2 spawnAngle = (Vec2)loadedPosition.rotation().orElse(new Vec2(respawnData.yaw(), respawnData.pitch()));
/*     */       
/*  69 */       this.state = new Preparing(spawnLevel, spawnPosition, spawnAngle);
/*  70 */       reporter.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tick() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield state : Lnet/minecraft/server/network/config/PrepareSpawnTask$State;
/*     */     //   4: astore_1
/*     */     //   5: iconst_0
/*     */     //   6: istore_2
/*     */     //   7: aload_1
/*     */     //   8: iload_2
/*     */     //   9: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   14: tableswitch default -> 40, -1 -> 90, 0 -> 50, 1 -> 80
/*     */     //   40: new java/lang/MatchException
/*     */     //   43: dup
/*     */     //   44: aconst_null
/*     */     //   45: aconst_null
/*     */     //   46: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   49: athrow
/*     */     //   50: aload_1
/*     */     //   51: checkcast net/minecraft/server/network/config/PrepareSpawnTask$Preparing
/*     */     //   54: astore_3
/*     */     //   55: aload_3
/*     */     //   56: invokevirtual tick : ()Lnet/minecraft/server/network/config/PrepareSpawnTask$Ready;
/*     */     //   59: astore #4
/*     */     //   61: aload #4
/*     */     //   63: ifnull -> 76
/*     */     //   66: aload_0
/*     */     //   67: aload #4
/*     */     //   69: putfield state : Lnet/minecraft/server/network/config/PrepareSpawnTask$State;
/*     */     //   72: iconst_1
/*     */     //   73: goto -> 91
/*     */     //   76: iconst_0
/*     */     //   77: goto -> 91
/*     */     //   80: aload_1
/*     */     //   81: checkcast net/minecraft/server/network/config/PrepareSpawnTask$Ready
/*     */     //   84: astore #4
/*     */     //   86: iconst_1
/*     */     //   87: goto -> 91
/*     */     //   90: iconst_0
/*     */     //   91: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #75	-> 0
/*     */     //   #76	-> 50
/*     */     //   #77	-> 55
/*     */     //   #78	-> 61
/*     */     //   #79	-> 66
/*     */     //   #80	-> 72
/*     */     //   #82	-> 76
/*     */     //   #84	-> 80
/*     */     //   #85	-> 90
/*     */     //   #75	-> 91
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   61	19	4	ready	Lnet/minecraft/server/network/config/PrepareSpawnTask$Ready;
/*     */     //   55	25	3	preparing	Lnet/minecraft/server/network/config/PrepareSpawnTask$Preparing;
/*     */     //   86	4	4	ignored	Lnet/minecraft/server/network/config/PrepareSpawnTask$Ready;
/*     */     //   0	92	0	this	Lnet/minecraft/server/network/config/PrepareSpawnTask; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerPlayer spawnPlayer(Connection connection, CommonListenerCookie cookie) {
/*  90 */     State state1 = this.state; if (state1 instanceof Ready) { Ready ready = (Ready)state1;
/*  91 */       return ready.spawn(connection, cookie); }
/*     */     
/*  93 */     throw new IllegalStateException("Player spawn was not ready");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void keepAlive() {
/*  99 */     State state1 = this.state; if (state1 instanceof Ready) { Ready ready = (Ready)state1;
/* 100 */       ready.keepAlive(); }
/*     */   
/*     */   }
/*     */   
/*     */   public void close() {
/* 105 */     State state1 = this.state; if (state1 instanceof Preparing) { Preparing preparing = (Preparing)state1;
/* 106 */       preparing.cancel(); }
/*     */     
/* 108 */     this.state = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public ConfigurationTask.Type type() { return TYPE; }
/*     */   
/*     */   private final class Preparing
/*     */     implements State
/*     */   {
/*     */     private final ServerLevel spawnLevel;
/*     */     private final CompletableFuture<Vec3> spawnPosition;
/*     */     private final Vec2 spawnAngle;
/*     */     private CompletableFuture<?> chunkLoadFuture;
/*     */     private final ChunkLoadCounter chunkLoadCounter;
/*     */     
/*     */     private Preparing(ServerLevel spawnLevel, CompletableFuture<Vec3> spawnPosition, Vec2 spawnAngle) {
/* 125 */       this.chunkLoadCounter = new ChunkLoadCounter();
/*     */ 
/*     */       
/* 128 */       this.spawnLevel = spawnLevel;
/* 129 */       this.spawnPosition = spawnPosition;
/* 130 */       this.spawnAngle = spawnAngle;
/*     */     }
/*     */ 
/*     */     
/* 134 */     public void cancel() { this.spawnPosition.cancel(false); }
/*     */ 
/*     */     
/*     */     public PrepareSpawnTask.Ready tick() {
/* 138 */       if (!this.spawnPosition.isDone()) {
/* 139 */         return null;
/*     */       }
/*     */       
/* 142 */       Vec3 spawnPosition = (Vec3)this.spawnPosition.join();
/*     */       
/* 144 */       if (this.chunkLoadFuture == null) {
/* 145 */         ChunkPos spawnChunk = new ChunkPos(BlockPos.containing(spawnPosition));
/* 146 */         this.chunkLoadCounter.track(this.spawnLevel, () -> 
/* 147 */             this.chunkLoadFuture = this.spawnLevel.getChunkSource().addTicketAndLoadWithRadius(TicketType.PLAYER_SPAWN, spawnChunk, 3));
/*     */         
/* 149 */         PrepareSpawnTask.this.loadListener.start(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.totalChunks());
/* 150 */         PrepareSpawnTask.this.loadListener.updateFocus(this.spawnLevel.dimension(), spawnChunk);
/*     */       } 
/*     */       
/* 153 */       PrepareSpawnTask.this.loadListener.update(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.readyChunks(), this.chunkLoadCounter.totalChunks());
/*     */       
/* 155 */       if (!this.chunkLoadFuture.isDone()) {
/* 156 */         return null;
/*     */       }
/*     */       
/* 159 */       PrepareSpawnTask.this.loadListener.finish(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS);
/*     */       
/* 161 */       return new PrepareSpawnTask.Ready(PrepareSpawnTask.this, this.spawnLevel, spawnPosition, this.spawnAngle);
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface State {}
/*     */   
/*     */   private final class Ready implements State {
/*     */     private final ServerLevel spawnLevel;
/*     */     
/*     */     private Ready(ServerLevel spawnLevel, Vec3 spawnPosition, Vec2 spawnAngle) {
/* 171 */       this.spawnLevel = spawnLevel;
/* 172 */       this.spawnPosition = spawnPosition;
/* 173 */       this.spawnAngle = spawnAngle;
/*     */     }
/*     */     private final Vec3 spawnPosition; private final Vec2 spawnAngle;
/*     */     
/* 177 */     public void keepAlive() { this.spawnLevel.getChunkSource().addTicketWithRadius(TicketType.PLAYER_SPAWN, new ChunkPos(BlockPos.containing(this.spawnPosition)), 3); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ServerPlayer spawn(Connection connection, CommonListenerCookie cookie) {
/* 183 */       ChunkPos spawnChunk = new ChunkPos(BlockPos.containing(this.spawnPosition));
/* 184 */       this.spawnLevel.waitForEntities(spawnChunk, 3);
/*     */       
/* 186 */       ServerPlayer player = new ServerPlayer(PrepareSpawnTask.this.server, this.spawnLevel, cookie.gameProfile(), cookie.clientInformation());
/*     */       
/* 188 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(player.problemPath(), PrepareSpawnTask.LOGGER);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 193 */         Optional<ValueInput> input = PrepareSpawnTask.this.server.getPlayerList().loadPlayerData(PrepareSpawnTask.this.nameAndId).map(tag -> TagValueInput.create(reporter, PrepareSpawnTask.this.server.registryAccess(), tag));
/* 194 */         Objects.requireNonNull(player); input.ifPresent(player::load);
/*     */         
/* 196 */         player.snapTo(this.spawnPosition, this.spawnAngle.x, this.spawnAngle.y);
/*     */         
/* 198 */         PrepareSpawnTask.this.server.getPlayerList().placeNewPlayer(connection, player, cookie);
/*     */         
/* 200 */         input.ifPresent(tag -> {
/* 201 */               player.loadAndSpawnEnderPearls(tag);
/* 202 */               player.loadAndSpawnParentVehicle(tag);
/*     */             });
/*     */         
/* 205 */         ServerPlayer serverPlayer = player;
/* 206 */         reporter.close();
/*     */         return serverPlayer;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           reporter.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\config\PrepareSpawnTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
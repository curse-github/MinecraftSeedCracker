/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JukeboxSongPlayer
/*     */ {
/*     */   public static final int PLAY_EVENT_INTERVAL_TICKS = 20;
/*     */   private long ticksSinceSongStarted;
/*     */   private Holder<JukeboxSong> song;
/*     */   private final BlockPos blockPos;
/*     */   private final OnSongChanged onSongChanged;
/*     */   
/*     */   public JukeboxSongPlayer(OnSongChanged onSongChanged, BlockPos blockPos) {
/*  30 */     this.onSongChanged = onSongChanged;
/*  31 */     this.blockPos = blockPos;
/*     */   }
/*     */ 
/*     */   
/*  35 */   public boolean isPlaying() { return (this.song != null); }
/*     */ 
/*     */   
/*     */   public JukeboxSong getSong() {
/*  39 */     if (this.song == null) {
/*  40 */       return null;
/*     */     }
/*     */     
/*  43 */     return (JukeboxSong)this.song.value();
/*     */   }
/*     */ 
/*     */   
/*  47 */   public long getTicksSinceSongStarted() { return this.ticksSinceSongStarted; }
/*     */ 
/*     */   
/*     */   public void setSongWithoutPlaying(Holder<JukeboxSong> song, long ticksSinceSongStarted) {
/*  51 */     if (((JukeboxSong)song.value()).hasFinished(ticksSinceSongStarted)) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     this.song = song;
/*  56 */     this.ticksSinceSongStarted = ticksSinceSongStarted;
/*     */   }
/*     */   
/*     */   public void play(LevelAccessor level, Holder<JukeboxSong> song) {
/*  60 */     this.song = song;
/*  61 */     this.ticksSinceSongStarted = 0L;
/*  62 */     int songId = level.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG).getId((JukeboxSong)this.song.value());
/*  63 */     level.levelEvent(null, 1010, this.blockPos, songId);
/*  64 */     this.onSongChanged.notifyChange();
/*     */   }
/*     */   
/*     */   public void stop(LevelAccessor level, BlockState blockState) {
/*  68 */     if (this.song == null) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     this.song = null;
/*  73 */     this.ticksSinceSongStarted = 0L;
/*  74 */     level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.blockPos, GameEvent.Context.of(blockState));
/*  75 */     level.levelEvent(1011, this.blockPos, 0);
/*  76 */     this.onSongChanged.notifyChange();
/*     */   }
/*     */   
/*     */   public void tick(LevelAccessor level, BlockState blockState) {
/*  80 */     if (this.song == null) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     if (((JukeboxSong)this.song.value()).hasFinished(this.ticksSinceSongStarted)) {
/*  85 */       stop(level, blockState);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     if (shouldEmitJukeboxPlayingEvent()) {
/*  90 */       level.gameEvent(GameEvent.JUKEBOX_PLAY, this.blockPos, GameEvent.Context.of(blockState));
/*  91 */       spawnMusicParticles(level, this.blockPos);
/*     */     } 
/*     */     
/*  94 */     this.ticksSinceSongStarted++;
/*     */   }
/*     */ 
/*     */   
/*  98 */   private boolean shouldEmitJukeboxPlayingEvent() { return (this.ticksSinceSongStarted % 20L == 0L); }
/*     */ 
/*     */   
/*     */   private static void spawnMusicParticles(LevelAccessor level, BlockPos blockPos) {
/* 102 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 103 */       Vec3 pos = Vec3.atBottomCenterOf(blockPos).add(0.0D, 1.2000000476837158D, 0.0D);
/* 104 */       float randomColor = level.getRandom().nextInt(4) / 24.0F;
/* 105 */       serverLevel.sendParticles(ParticleTypes.NOTE, pos.x(), pos.y(), pos.z(), 0, randomColor, 0.0D, 0.0D, 1.0D); }
/*     */   
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OnSongChanged {
/*     */     void notifyChange();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\JukeboxSongPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
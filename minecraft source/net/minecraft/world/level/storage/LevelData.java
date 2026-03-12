/*    */ package net.minecraft.world.level.storage;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ 
/*    */ public interface LevelData {
/*    */   public static final class RespawnData extends Record {
/*    */     private final GlobalPos globalPos;
/*    */     private final float yaw;
/*    */     private final float pitch;
/*    */     
/* 21 */     public RespawnData(GlobalPos globalPos, float yaw, float pitch) { this.globalPos = globalPos; this.yaw = yaw; this.pitch = pitch; } public final String toString() throws Exception { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 21 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData; } public GlobalPos globalPos() { return this.globalPos; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public float yaw() { return this.yaw; } public float pitch() { return this.pitch; }
/* 22 */     public static final RespawnData DEFAULT = new RespawnData(GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO), 0.0F, 0.0F);
/* 23 */     public static final MapCodec<RespawnData> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(GlobalPos.MAP_CODEC
/* 24 */           .forGetter(RespawnData::globalPos), 
/* 25 */           Codec.floatRange(-180.0F, 180.0F).fieldOf("yaw").forGetter(RespawnData::yaw), 
/* 26 */           Codec.floatRange(-90.0F, 90.0F).fieldOf("pitch").forGetter(RespawnData::pitch))
/* 27 */         .apply(i, RespawnData::new));
/* 28 */     public static final Codec<RespawnData> CODEC = MAP_CODEC.codec();
/* 29 */     public static final StreamCodec<ByteBuf, RespawnData> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC, RespawnData::globalPos, ByteBufCodecs.FLOAT, RespawnData::yaw, ByteBufCodecs.FLOAT, RespawnData::pitch, RespawnData::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     public static RespawnData of(ResourceKey<Level> dimension, BlockPos pos, float yaw, float pitch) { return new RespawnData(GlobalPos.of(dimension, pos.immutable()), Mth.wrapDegrees(yaw), Mth.clamp(pitch, -90.0F, 90.0F)); }
/*    */ 
/*    */ 
/*    */     
/* 41 */     public ResourceKey<Level> dimension() { return this.globalPos.dimension(); }
/*    */ 
/*    */ 
/*    */     
/* 45 */     public BlockPos pos() { return this.globalPos.pos(); }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 68 */     category.setDetail("Level spawn location", () -> CrashReportCategory.formatLocation(levelHeightAccessor, getRespawnData().pos()));
/* 69 */     category.setDetail("Level time", () -> String.format(Locale.ROOT, "%d game time, %d day time", new Object[] { Long.valueOf(getGameTime()), Long.valueOf(getDayTime()) }));
/*    */   }
/*    */   
/*    */   RespawnData getRespawnData();
/*    */   
/*    */   long getGameTime();
/*    */   
/*    */   long getDayTime();
/*    */   
/*    */   boolean isThundering();
/*    */   
/*    */   boolean isRaining();
/*    */   
/*    */   void setRaining(boolean paramBoolean);
/*    */   
/*    */   boolean isHardcore();
/*    */   
/*    */   Difficulty getDifficulty();
/*    */   
/*    */   boolean isDifficultyLocked();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*    */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*    */ 
/*    */ @Name("minecraft.ChunkRegionRead")
/*    */ @Label("Region File Read")
/*    */ public class ChunkRegionReadEvent extends ChunkRegionIoEvent {
/*    */   public static final String EVENT_NAME = "minecraft.ChunkRegionRead";
/* 14 */   public static final EventType TYPE = EventType.getEventType(ChunkRegionReadEvent.class);
/*    */ 
/*    */   
/* 17 */   public ChunkRegionReadEvent(RegionStorageInfo info, ChunkPos chunkPos, RegionFileVersion version, int bytes) { super(info, chunkPos, version, bytes); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\ChunkRegionReadEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
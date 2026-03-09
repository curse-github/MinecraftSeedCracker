/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Enabled;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ @Name("minecraft.StructureGeneration")
/*    */ @Label("Structure Generation")
/*    */ @Category({"Minecraft", "World Generation"})
/*    */ @StackTrace(false)
/*    */ @Enabled(false)
/*    */ public class StructureGenerationEvent
/*    */   extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.StructureGeneration";
/* 24 */   public static final EventType TYPE = EventType.getEventType(StructureGenerationEvent.class);
/*    */   
/*    */   @Name("chunkPosX")
/*    */   @Label("Chunk X Position")
/*    */   public final int chunkPosX;
/*    */   
/*    */   @Name("chunkPosZ")
/*    */   @Label("Chunk Z Position")
/*    */   public final int chunkPosZ;
/*    */   
/*    */   @Name("structure")
/*    */   @Label("Structure")
/*    */   public final String structure;
/*    */   
/*    */   @Name("level")
/*    */   @Label("Level")
/*    */   public final String level;
/*    */   
/*    */   @Name("success")
/*    */   @Label("Success")
/*    */   public boolean success;
/*    */   
/*    */   public StructureGenerationEvent(ChunkPos sourceChunkPos, Holder<Structure> structure, ResourceKey<Level> level) {
/* 47 */     this.chunkPosX = sourceChunkPos.x;
/* 48 */     this.chunkPosZ = sourceChunkPos.z;
/* 49 */     this.structure = structure.getRegisteredName();
/* 50 */     this.level = level.identifier().toString();
/*    */   }
/*    */   
/*    */   public static interface Fields {
/*    */     public static final String CHUNK_POS_X = "chunkPosX";
/*    */     public static final String CHUNK_POS_Z = "chunkPosZ";
/*    */     public static final String STRUCTURE = "structure";
/*    */     public static final String LEVEL = "level";
/*    */     public static final String SUCCESS = "success";
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\StructureGenerationEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
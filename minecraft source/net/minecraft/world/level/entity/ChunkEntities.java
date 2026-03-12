/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public class ChunkEntities<T>
/*    */   extends Object
/*    */ {
/*    */   private final ChunkPos pos;
/*    */   private final List<T> entities;
/*    */   
/*    */   public ChunkEntities(ChunkPos pos, List<T> entities) {
/* 14 */     this.pos = pos;
/* 15 */     this.entities = entities;
/*    */   }
/*    */ 
/*    */   
/* 19 */   public ChunkPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public Stream<T> getEntities() { return this.entities.stream(); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean isEmpty() { return this.entities.isEmpty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\ChunkEntities.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
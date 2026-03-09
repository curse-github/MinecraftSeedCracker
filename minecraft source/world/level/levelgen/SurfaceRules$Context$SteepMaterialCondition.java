/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
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
/*     */ class SteepMaterialCondition
/*     */   extends SurfaceRules.LazyXZCondition
/*     */ {
/* 191 */   private SteepMaterialCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean compute() {
/* 196 */     int chunkBlockX = this.context.blockX & 0xF;
/* 197 */     int chunkBlockZ = this.context.blockZ & 0xF;
/*     */     
/* 199 */     int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 200 */     int zSouth = Math.min(chunkBlockZ + 1, 15);
/*     */     
/* 202 */     ChunkAccess chunk = this.context.chunk;
/* 203 */     int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 204 */     int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/*     */     
/* 206 */     if (heightSouth >= heightNorth + 4) {
/* 207 */       return true;
/*     */     }
/*     */     
/* 210 */     int xWest = Math.max(chunkBlockX - 1, 0);
/* 211 */     int xEast = Math.min(chunkBlockX + 1, 15);
/* 212 */     int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 213 */     int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/*     */     
/* 215 */     return (heightWest >= heightEast + 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$SteepMaterialCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
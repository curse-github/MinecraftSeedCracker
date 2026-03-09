/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.AABB;
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
/*     */ public class EndSpike
/*     */ {
/* 121 */   public static final Codec<EndSpike> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 122 */         .fieldOf("centerX").orElse(Integer.valueOf(0)).forGetter(()), Codec.INT
/* 123 */         .fieldOf("centerZ").orElse(Integer.valueOf(0)).forGetter(()), Codec.INT
/* 124 */         .fieldOf("radius").orElse(Integer.valueOf(0)).forGetter(()), Codec.INT
/* 125 */         .fieldOf("height").orElse(Integer.valueOf(0)).forGetter(()), Codec.BOOL
/* 126 */         .fieldOf("guarded").orElse(Boolean.valueOf(false)).forGetter(()))
/* 127 */       .apply(i, EndSpike::new));
/*     */   
/*     */   private final int centerX;
/*     */   private final int centerZ;
/*     */   private final int radius;
/*     */   private final int height;
/*     */   private final boolean guarded;
/*     */   private final AABB topBoundingBox;
/*     */   
/*     */   public EndSpike(int centerX, int centerZ, int radius, int height, boolean guarded) {
/* 137 */     this.centerX = centerX;
/* 138 */     this.centerZ = centerZ;
/* 139 */     this.radius = radius;
/* 140 */     this.height = height;
/* 141 */     this.guarded = guarded;
/*     */     
/* 143 */     this.topBoundingBox = new AABB((centerX - radius), DimensionType.MIN_Y, (centerZ - radius), (centerX + radius), DimensionType.MAX_Y, (centerZ + radius));
/*     */   }
/*     */   
/*     */   public boolean isCenterWithinChunk(BlockPos chunkOrigin) {
/* 147 */     return (SectionPos.blockToSectionCoord(chunkOrigin.getX()) == SectionPos.blockToSectionCoord(this.centerX) && 
/* 148 */       SectionPos.blockToSectionCoord(chunkOrigin.getZ()) == SectionPos.blockToSectionCoord(this.centerZ));
/*     */   }
/*     */ 
/*     */   
/* 152 */   public int getCenterX() { return this.centerX; }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public int getCenterZ() { return this.centerZ; }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public int getRadius() { return this.radius; }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public int getHeight() { return this.height; }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public boolean isGuarded() { return this.guarded; }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public AABB getTopBoundingBox() { return this.topBoundingBox; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SpikeFeature$EndSpike.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
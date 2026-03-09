/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class GravityProcessor extends StructureProcessor {
/* 13 */   public static final MapCodec<GravityProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Heightmap.Types.CODEC
/* 14 */         .fieldOf("heightmap").orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter(()), Codec.INT
/* 15 */         .fieldOf("offset").orElse(Integer.valueOf(0)).forGetter(()))
/* 16 */       .apply(i, GravityProcessor::new));
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int offset;
/*    */   
/*    */   public GravityProcessor(Heightmap.Types heightmap, int offset) {
/* 22 */     this.heightmap = heightmap;
/* 23 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/*    */     Heightmap.Types heightmap;
/* 29 */     if (level instanceof net.minecraft.server.level.ServerLevel) {
/*    */       
/* 31 */       if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
/* 32 */         heightmap = Heightmap.Types.WORLD_SURFACE;
/* 33 */       } else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 34 */         heightmap = Heightmap.Types.OCEAN_FLOOR;
/*    */       } else {
/* 36 */         heightmap = this.heightmap;
/*    */       } 
/*    */     } else {
/* 39 */       heightmap = this.heightmap;
/*    */     } 
/* 41 */     BlockPos pos = processedBlockInfo.pos();
/* 42 */     int height = level.getHeight(heightmap, pos.getX(), pos.getZ()) + this.offset;
/* 43 */     int delta = originalBlockInfo.pos().getY();
/* 44 */     return new StructureTemplate.StructureBlockInfo(new BlockPos(pos.getX(), height + delta, pos.getZ()), processedBlockInfo.state(), processedBlockInfo.nbt());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.GRAVITY; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\GravityProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
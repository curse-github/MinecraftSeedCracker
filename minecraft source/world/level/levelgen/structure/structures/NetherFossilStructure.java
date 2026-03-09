/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.EmptyBlockGetter;
/*    */ import net.minecraft.world.level.NoiseColumn;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class NetherFossilStructure extends Structure {
/* 21 */   public static final MapCodec<NetherFossilStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 22 */         settingsCodec(i), HeightProvider.CODEC
/* 23 */         .fieldOf("height").forGetter(()))
/* 24 */       .apply(i, NetherFossilStructure::new));
/*    */   
/*    */   public final HeightProvider height;
/*    */   
/*    */   public NetherFossilStructure(Structure.StructureSettings settings, HeightProvider height) {
/* 29 */     super(settings);
/* 30 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 35 */     WorldgenRandom random = context.random();
/* 36 */     int blockX = context.chunkPos().getMinBlockX() + random.nextInt(16);
/* 37 */     int blockZ = context.chunkPos().getMinBlockZ() + random.nextInt(16);
/*    */     
/* 39 */     int seaLevel = context.chunkGenerator().getSeaLevel();
/*    */ 
/*    */     
/* 42 */     WorldGenerationContext generationContext = new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor());
/*    */     
/* 44 */     int y = this.height.sample(random, generationContext);
/*    */     
/* 46 */     NoiseColumn column = context.chunkGenerator().getBaseColumn(blockX, blockZ, context.heightAccessor(), context.randomState());
/*    */     
/* 48 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(blockX, y, blockZ);
/* 49 */     while (y > seaLevel) {
/* 50 */       BlockState current = column.getBlock(y);
/*    */       
/* 52 */       y--;
/*    */       
/* 54 */       BlockState below = column.getBlock(y);
/* 55 */       if (current.isAir() && (below.is(Blocks.SOUL_SAND) || below.isFaceSturdy(EmptyBlockGetter.INSTANCE, pos.setY(y), Direction.UP))) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     if (y <= seaLevel) {
/* 61 */       return Optional.empty();
/*    */     }
/*    */     
/* 64 */     BlockPos position = new BlockPos(blockX, y, blockZ);
/* 65 */     return Optional.of(new Structure.GenerationStub(position, builder -> NetherFossilPieces.addPieces(context.structureTemplateManager(), builder, random, position)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public StructureType<?> type() { return StructureType.NETHER_FOSSIL; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFossilStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
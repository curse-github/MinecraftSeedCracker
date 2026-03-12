/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class NetherFortressStructure extends Structure {
/* 18 */   public static final WeightedList<MobSpawnSettings.SpawnerData> FORTRESS_ENEMIES = WeightedList.builder()
/* 19 */     .add(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 2, 3), 10)
/* 20 */     .add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 4, 4), 5)
/* 21 */     .add(new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 5, 5), 8)
/* 22 */     .add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 5, 5), 2)
/* 23 */     .add(new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 4, 4), 3)
/* 24 */     .build();
/*    */   
/* 26 */   public static final MapCodec<NetherFortressStructure> CODEC = simpleCodec(NetherFortressStructure::new);
/*    */ 
/*    */   
/* 29 */   public NetherFortressStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 34 */     ChunkPos chunkPos = context.chunkPos();
/*    */     
/* 36 */     BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), 64, chunkPos.getMinBlockZ());
/* 37 */     return Optional.of(new Structure.GenerationStub(startPos, builder -> generatePieces(builder, context)));
/*    */   }
/*    */ 
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 42 */     NetherFortressPieces.StartPiece start = new NetherFortressPieces.StartPiece(context.random(), context.chunkPos().getBlockX(2), context.chunkPos().getBlockZ(2));
/* 43 */     builder.addPiece(start);
/* 44 */     start.addChildren(start, builder, context.random());
/*    */     
/* 46 */     List<StructurePiece> pendingChildren = start.pendingChildren;
/* 47 */     while (!pendingChildren.isEmpty()) {
/* 48 */       int pos = context.random().nextInt(pendingChildren.size());
/* 49 */       StructurePiece structurePiece = (StructurePiece)pendingChildren.remove(pos);
/* 50 */       structurePiece.addChildren(start, builder, context.random());
/*    */     } 
/*    */ 
/*    */     
/* 54 */     builder.moveInsideHeights(context.random(), 48, 70);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public StructureType<?> type() { return StructureType.FORTRESS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
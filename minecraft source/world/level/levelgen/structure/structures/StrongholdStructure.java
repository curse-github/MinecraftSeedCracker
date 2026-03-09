/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class StrongholdStructure extends Structure {
/* 13 */   public static final MapCodec<StrongholdStructure> CODEC = simpleCodec(StrongholdStructure::new);
/*    */ 
/*    */   
/* 16 */   public StrongholdStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) { return Optional.of(new Structure.GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context))); }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/*    */     StrongholdPieces.StartPiece startRoom;
/* 25 */     int tries = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 31 */       builder.clear();
/* 32 */       context.random().setLargeFeatureSeed(context.seed() + tries++, (context.chunkPos()).x, (context.chunkPos()).z);
/* 33 */       StrongholdPieces.resetPieces();
/*    */       
/* 35 */       startRoom = new StrongholdPieces.StartPiece(context.random(), context.chunkPos().getBlockX(2), context.chunkPos().getBlockZ(2));
/* 36 */       builder.addPiece(startRoom);
/* 37 */       startRoom.addChildren(startRoom, builder, context.random());
/*    */       
/* 39 */       List<StructurePiece> pendingChildren = startRoom.pendingChildren;
/* 40 */       while (!pendingChildren.isEmpty()) {
/* 41 */         int pos = context.random().nextInt(pendingChildren.size());
/* 42 */         StructurePiece structurePiece = (StructurePiece)pendingChildren.remove(pos);
/* 43 */         structurePiece.addChildren(startRoom, builder, context.random());
/*    */       } 
/*    */       
/* 46 */       builder.moveBelowSeaLevel(context.chunkGenerator().getSeaLevel(), context.chunkGenerator().getMinY(), context.random(), 10);
/* 47 */     } while (builder.isEmpty() || startRoom.portalRoomPiece == null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public StructureType<?> type() { return StructureType.STRONGHOLD; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
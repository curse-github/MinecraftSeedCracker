/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class EndCityStructure extends Structure {
/* 16 */   public static final MapCodec<EndCityStructure> CODEC = simpleCodec(EndCityStructure::new);
/*    */ 
/*    */   
/* 19 */   public EndCityStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 24 */     Rotation rotation = Rotation.getRandom(context.random());
/* 25 */     BlockPos startPos = getLowestYIn5by5BoxOffset7Blocks(context, rotation);
/*    */ 
/*    */     
/* 28 */     if (startPos.getY() < 60) {
/* 29 */       return Optional.empty();
/*    */     }
/*    */     
/* 32 */     return Optional.of(new Structure.GenerationStub(startPos, builder -> generatePieces(builder, startPos, rotation, context)));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, BlockPos startPos, Rotation rotation, Structure.GenerationContext context) {
/* 36 */     List<StructurePiece> pieces = Lists.newArrayList();
/* 37 */     EndCityPieces.startHouseTower(context.structureTemplateManager(), startPos, rotation, pieces, context.random());
/*    */     
/* 39 */     Objects.requireNonNull(builder); pieces.forEach(builder::addPiece);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public StructureType<?> type() { return StructureType.END_CITY; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
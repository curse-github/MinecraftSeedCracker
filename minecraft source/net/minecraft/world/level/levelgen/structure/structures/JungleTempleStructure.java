/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ 
/*    */ public class JungleTempleStructure extends SinglePieceStructure {
/*  8 */   public static final MapCodec<JungleTempleStructure> CODEC = simpleCodec(JungleTempleStructure::new);
/*    */ 
/*    */   
/* 11 */   public JungleTempleStructure(Structure.StructureSettings settings) { super(JungleTemplePiece::new, 12, 15, settings); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public StructureType<?> type() { return StructureType.JUNGLE_TEMPLE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\JungleTempleStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
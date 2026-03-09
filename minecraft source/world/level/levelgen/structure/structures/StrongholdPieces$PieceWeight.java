/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PieceWeight
/*    */ {
/*    */   public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;
/*    */   public final int weight;
/*    */   public int placeCount;
/*    */   public final int maxPlaceCount;
/*    */   
/*    */   public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> pieceClass, int weight, int maxPlaceCount) {
/* 62 */     this.pieceClass = pieceClass;
/* 63 */     this.weight = weight;
/* 64 */     this.maxPlaceCount = maxPlaceCount;
/*    */   }
/*    */ 
/*    */   
/* 68 */   public boolean doPlace(int depth) { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public boolean isValid() { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$PieceWeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
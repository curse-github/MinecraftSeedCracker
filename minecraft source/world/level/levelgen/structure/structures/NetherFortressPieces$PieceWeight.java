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
/*    */ class PieceWeight
/*    */ {
/*    */   public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;
/*    */   public final int weight;
/*    */   public int placeCount;
/*    */   public final int maxPlaceCount;
/*    */   public final boolean allowInRow;
/*    */   
/*    */   public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount, boolean allowInRow) {
/* 46 */     this.pieceClass = pieceClass;
/* 47 */     this.weight = weight;
/* 48 */     this.maxPlaceCount = maxPlaceCount;
/* 49 */     this.allowInRow = allowInRow;
/*    */   }
/*    */ 
/*    */   
/* 53 */   public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount) { this(pieceClass, weight, maxPlaceCount, false); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean doPlace(int depth) { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean isValid() { return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$PieceWeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
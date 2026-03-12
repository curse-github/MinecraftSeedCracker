/*    */ package net.minecraft.world.level.block;
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
/*    */ public interface NeighborCombineResult<S>
/*    */ {
/*    */   <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> paramCombiner);
/*    */   
/*    */   public static final class Double<S>
/*    */     extends Object
/*    */     implements NeighborCombineResult<S>
/*    */   {
/*    */     private final S first;
/*    */     private final S second;
/*    */     
/*    */     public Double(S first, S second) {
/* 77 */       this.first = first;
/* 78 */       this.second = second;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 83 */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return (T)callback.acceptDouble(this.first, this.second); }
/*    */   }
/*    */   
/*    */   public static final class Single<S>
/*    */     extends Object
/*    */     implements NeighborCombineResult<S> {
/*    */     private final S single;
/*    */     
/* 91 */     public Single(S single) { this.single = single; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 96 */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) { return (T)callback.acceptSingle(this.single); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$NeighborCombineResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.nbt.CompoundTag;
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
/*    */ class PendingStore
/*    */ {
/*    */   private CompoundTag data;
/*    */   private final CompletableFuture<Void> result;
/*    */   
/*    */   public PendingStore(CompoundTag data) {
/* 57 */     this.result = new CompletableFuture();
/*    */ 
/*    */     
/* 60 */     this.data = data;
/*    */   }
/*    */   
/*    */   private CompoundTag copyData() {
/* 64 */     CompoundTag data = this.data;
/* 65 */     return (data == null) ? null : data.copy();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\IOWorker$PendingStore.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
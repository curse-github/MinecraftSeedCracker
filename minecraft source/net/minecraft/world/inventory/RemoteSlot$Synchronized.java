/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.network.HashedPatchMap;
/*    */ import net.minecraft.network.HashedStack;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ public class Synchronized
/*    */   implements RemoteSlot
/*    */ {
/*    */   private final HashedPatchMap.HashGenerator hasher;
/*    */   private ItemStack remoteStack;
/*    */   private HashedStack remoteHash;
/*    */   
/*    */   public Synchronized(HashedPatchMap.HashGenerator hasher) {
/* 41 */     this.remoteStack = null;
/* 42 */     this.remoteHash = null;
/*    */ 
/*    */     
/* 45 */     this.hasher = hasher;
/*    */   }
/*    */ 
/*    */   
/*    */   public void force(ItemStack outgoing) {
/* 50 */     this.remoteStack = outgoing.copy();
/* 51 */     this.remoteHash = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void receive(HashedStack incoming) {
/* 56 */     this.remoteStack = null;
/* 57 */     this.remoteHash = incoming;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(ItemStack local) {
/* 62 */     if (this.remoteStack != null) {
/* 63 */       return ItemStack.matches(this.remoteStack, local);
/*    */     }
/*    */     
/* 66 */     if (this.remoteHash != null && 
/* 67 */       this.remoteHash.matches(local, this.hasher)) {
/*    */       
/* 69 */       this.remoteStack = local.copy();
/* 70 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public void copyFrom(Synchronized other) {
/* 78 */     this.remoteStack = other.remoteStack;
/* 79 */     this.remoteHash = other.remoteHash;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\RemoteSlot$Synchronized.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
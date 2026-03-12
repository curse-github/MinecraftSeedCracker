/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ class null
/*    */   implements BundlerInfo.Bundler
/*    */ {
/* 32 */   private final List<Packet<? super T>> bundlePackets = new ArrayList();
/*    */ 
/*    */   
/*    */   public Packet<?> addPacket(Packet<?> packet) {
/* 36 */     if (packet == BundlerInfo.null.this.val$delimiterPacket)
/*    */     {
/* 38 */       return (Packet)BundlerInfo.null.this.val$constructor.apply(this.bundlePackets);
/*    */     }
/*    */     
/* 41 */     Packet<T> castPacket = packet;
/* 42 */     if (this.bundlePackets.size() >= 4096) {
/* 43 */       throw new IllegalStateException("Too many packets in a bundle");
/*    */     }
/* 45 */     this.bundlePackets.add(castPacket);
/* 46 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\BundlerInfo$1$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
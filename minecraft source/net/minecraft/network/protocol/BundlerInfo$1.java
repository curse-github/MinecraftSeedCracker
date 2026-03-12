/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements BundlerInfo
/*    */ {
/*    */   public void unbundlePacket(Packet<?> packet, Consumer<Packet<?>> output) {
/* 18 */     if (packet.type() == bundlePacketType) {
/* 19 */       P bundlerPacket = (P)(BundlePacket)packet;
/* 20 */       output.accept(delimiterPacket);
/* 21 */       bundlerPacket.subPackets().forEach(output);
/* 22 */       output.accept(delimiterPacket);
/*    */     } else {
/* 24 */       output.accept(packet);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public BundlerInfo.Bundler startPacketBundling(Packet<?> packet) {
/* 30 */     if (packet == delimiterPacket) {
/* 31 */       return new BundlerInfo.Bundler() {
/* 32 */           private final List<Packet<? super T>> bundlePackets = new ArrayList();
/*    */ 
/*    */           
/*    */           public Packet<?> addPacket(Packet<?> packet) {
/* 36 */             if (packet == delimiterPacket)
/*    */             {
/* 38 */               return (Packet)constructor.apply(this.bundlePackets);
/*    */             }
/*    */             
/* 41 */             Packet<T> castPacket = packet;
/* 42 */             if (this.bundlePackets.size() >= 4096) {
/* 43 */               throw new IllegalStateException("Too many packets in a bundle");
/*    */             }
/* 45 */             this.bundlePackets.add(castPacket);
/* 46 */             return null;
/*    */           }
/*    */         };
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\BundlerInfo$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
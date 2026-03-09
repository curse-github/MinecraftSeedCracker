/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BundlerInfo
/*    */ {
/*    */   public static final int BUNDLE_SIZE_LIMIT = 4096;
/*    */   
/*    */   static <T extends net.minecraft.network.PacketListener, P extends BundlePacket<? super T>> BundlerInfo createForPacket(final PacketType<P> bundlePacketType, final Function<Iterable<Packet<? super T>>, P> constructor, final BundleDelimiterPacket<? super T> delimiterPacket) {
/* 15 */     return new BundlerInfo()
/*    */       {
/*    */         public void unbundlePacket(Packet<?> packet, Consumer<Packet<?>> output) {
/* 18 */           if (packet.type() == bundlePacketType) {
/* 19 */             P bundlerPacket = (P)(BundlePacket)packet;
/* 20 */             output.accept(delimiterPacket);
/* 21 */             bundlerPacket.subPackets().forEach(output);
/* 22 */             output.accept(delimiterPacket);
/*    */           } else {
/* 24 */             output.accept(packet);
/*    */           } 
/*    */         }
/*    */ 
/*    */         
/*    */         public Bundler startPacketBundling(Packet<?> packet) {
/* 30 */           if (packet == delimiterPacket) {
/* 31 */             return new Bundler() {
/* 32 */                 private final List<Packet<? super T>> bundlePackets = new ArrayList();
/*    */ 
/*    */                 
/*    */                 public Packet<?> addPacket(Packet<?> packet) {
/* 36 */                   if (packet == delimiterPacket)
/*    */                   {
/* 38 */                     return (Packet)constructor.apply(this.bundlePackets);
/*    */                   }
/*    */                   
/* 41 */                   Packet<T> castPacket = packet;
/* 42 */                   if (this.bundlePackets.size() >= 4096) {
/* 43 */                     throw new IllegalStateException("Too many packets in a bundle");
/*    */                   }
/* 45 */                   this.bundlePackets.add(castPacket);
/* 46 */                   return null;
/*    */                 }
/*    */               };
/*    */           }
/* 50 */           return null;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   void unbundlePacket(Packet<?> paramPacket, Consumer<Packet<?>> paramConsumer);
/*    */   
/*    */   Bundler startPacketBundling(Packet<?> paramPacket);
/*    */   
/*    */   public static interface Bundler {
/*    */     Packet<?> addPacket(Packet<?> param1Packet);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\BundlerInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ public abstract class BundlePacket<T extends PacketListener>
/*    */   extends Object implements Packet<T> {
/*    */   private final Iterable<Packet<? super T>> packets;
/*    */   
/*  9 */   protected BundlePacket(Iterable<Packet<? super T>> packets) { this.packets = packets; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public final Iterable<Packet<? super T>> subPackets() { return this.packets; }
/*    */   
/*    */   public abstract PacketType<? extends BundlePacket<T>> type();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\BundlePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
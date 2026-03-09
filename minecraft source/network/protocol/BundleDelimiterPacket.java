/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BundleDelimiterPacket<T extends PacketListener>
/*    */   extends Object
/*    */   implements Packet<T>
/*    */ {
/* 11 */   public final void handle(T listener) { throw new AssertionError("This packet should be handled by pipeline"); }
/*    */   
/*    */   public abstract PacketType<? extends BundleDelimiterPacket<T>> type();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\BundleDelimiterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
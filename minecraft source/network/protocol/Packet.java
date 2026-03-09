/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamMemberEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Packet<T extends PacketListener>
/*    */ {
/*    */   PacketType<? extends Packet<T>> type();
/*    */   
/*    */   void handle(T paramT);
/*    */   
/* 21 */   default boolean isSkippable() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   default boolean isTerminal() { return false; }
/*    */ 
/*    */ 
/*    */   
/* 34 */   static <B extends io.netty.buffer.ByteBuf, T extends Packet<?>> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> writer, StreamDecoder<B, T> reader) { return StreamCodec.ofMember(writer, reader); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\Packet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
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
/*    */   extends Object
/*    */   implements StreamCodec<ByteBuf, ChunkPos>
/*    */ {
/* 34 */   public ChunkPos decode(ByteBuf input) { return FriendlyByteBuf.readChunkPos(input); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public void encode(ByteBuf output, ChunkPos value) { FriendlyByteBuf.writeChunkPos(output, value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ChunkPos$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ 
/*    */ 
/*    */ public class Type
/*    */   extends Object
/*    */   implements PositionSourceType<BlockPositionSource>
/*    */ {
/* 36 */   public MapCodec<BlockPositionSource> codec() { return BlockPositionSource.CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public StreamCodec<ByteBuf, BlockPositionSource> streamCodec() { return BlockPositionSource.STREAM_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\BlockPositionSource$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
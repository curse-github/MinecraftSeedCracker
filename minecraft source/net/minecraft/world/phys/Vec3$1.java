/*    */ package net.minecraft.world.phys;
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
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<ByteBuf, Vec3>
/*    */ {
/* 28 */   public Vec3 decode(ByteBuf input) { return FriendlyByteBuf.readVec3(input); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void encode(ByteBuf output, Vec3 value) { FriendlyByteBuf.writeVec3(output, value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\Vec3$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
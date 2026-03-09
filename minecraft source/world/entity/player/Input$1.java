/*    */ package net.minecraft.world.entity.player;
/*    */ 
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
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<FriendlyByteBuf, Input>
/*    */ {
/*    */   public void encode(FriendlyByteBuf output, Input value) {
/* 18 */     byte flags = 0;
/* 19 */     flags = (byte)(flags | (value.forward() ? 1 : 0));
/* 20 */     flags = (byte)(flags | (value.backward() ? 2 : 0));
/* 21 */     flags = (byte)(flags | (value.left() ? 4 : 0));
/* 22 */     flags = (byte)(flags | (value.right() ? 8 : 0));
/* 23 */     flags = (byte)(flags | (value.jump() ? 16 : 0));
/* 24 */     flags = (byte)(flags | (value.shift() ? 32 : 0));
/* 25 */     flags = (byte)(flags | (value.sprint() ? 64 : 0));
/* 26 */     output.writeByte(flags);
/*    */   }
/*    */ 
/*    */   
/*    */   public Input decode(FriendlyByteBuf input) {
/* 31 */     byte flags = input.readByte();
/* 32 */     boolean forward = ((flags & true) != 0);
/* 33 */     boolean backward = ((flags & 0x2) != 0);
/* 34 */     boolean left = ((flags & 0x4) != 0);
/* 35 */     boolean right = ((flags & 0x8) != 0);
/* 36 */     boolean jump = ((flags & 0x10) != 0);
/* 37 */     boolean shift = ((flags & 0x20) != 0);
/* 38 */     boolean sprint = ((flags & 0x40) != 0);
/* 39 */     return new Input(forward, backward, left, right, jump, shift, sprint);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\Input$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public static enum Unit {
/*  9 */   INSTANCE;
/*    */   static  {
/* 11 */     CODEC = MapCodec.unitCodec(INSTANCE);
/* 12 */     STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Unit> STREAM_CODEC;
/*    */   public static final Codec<Unit> CODEC;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Unit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.debug;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public static enum DebugEntityBlockIntersection {
/*    */   private static final IntFunction<DebugEntityBlockIntersection> BY_ID;
/*    */   public static final StreamCodec<ByteBuf, DebugEntityBlockIntersection> STREAM_CODEC;
/* 11 */   IN_BLOCK(0, 1610678016),
/* 12 */   IN_FLUID(1, 1610612991),
/* 13 */   IN_AIR(2, 1613968179);
/*    */   
/*    */   static  {
/* 16 */     BY_ID = ByIdMap.continuous(i -> i.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 17 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, i -> i.id);
/*    */   }
/*    */   private final int id;
/*    */   private final int color;
/*    */   
/*    */   DebugEntityBlockIntersection(int id, int color) {
/* 23 */     this.id = id;
/* 24 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/* 28 */   public int color() { return this.color; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugEntityBlockIntersection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
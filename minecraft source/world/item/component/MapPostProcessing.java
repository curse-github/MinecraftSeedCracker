/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public static enum MapPostProcessing {
/*    */   public static final IntFunction<MapPostProcessing> ID_MAP;
/* 11 */   LOCK(0),
/* 12 */   SCALE(1);
/*    */   
/*    */   static  {
/* 15 */     ID_MAP = ByIdMap.continuous(MapPostProcessing::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 16 */     STREAM_CODEC = ByteBufCodecs.idMapper(ID_MAP, MapPostProcessing::id);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, MapPostProcessing> STREAM_CODEC;
/*    */   
/* 21 */   MapPostProcessing(int id) { this.id = id; }
/*    */   
/*    */   private final int id;
/*    */   
/* 25 */   public int id() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\MapPostProcessing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
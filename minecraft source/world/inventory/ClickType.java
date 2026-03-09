/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public static enum ClickType {
/*    */   private static final IntFunction<ClickType> BY_ID;
/* 11 */   PICKUP(0),
/* 12 */   QUICK_MOVE(1),
/* 13 */   SWAP(2),
/* 14 */   CLONE(3),
/* 15 */   THROW(4),
/* 16 */   QUICK_CRAFT(5),
/* 17 */   PICKUP_ALL(6);
/*    */   
/*    */   static  {
/* 20 */     BY_ID = ByIdMap.continuous(ClickType::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 22 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ClickType::id);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, ClickType> STREAM_CODEC;
/*    */   
/* 27 */   ClickType(int id) { this.id = id; }
/*    */   
/*    */   private final int id;
/*    */   
/* 31 */   public int id() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ClickType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
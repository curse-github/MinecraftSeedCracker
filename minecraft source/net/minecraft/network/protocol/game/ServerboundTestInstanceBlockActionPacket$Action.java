/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
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
/*    */ public static enum Action
/*    */ {
/*    */   private static final IntFunction<Action> BY_ID;
/* 47 */   INIT(0),
/* 48 */   QUERY(1),
/* 49 */   SET(2),
/* 50 */   RESET(3),
/* 51 */   SAVE(4),
/* 52 */   EXPORT(5),
/* 53 */   RUN(6);
/*    */   static  {
/* 55 */     BY_ID = ByIdMap.continuous(e -> e.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 56 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */   }
/*    */   public static final StreamCodec<ByteBuf, Action> STREAM_CODEC;
/*    */   private final int id;
/*    */   
/* 61 */   Action(int id) { this.id = id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundTestInstanceBlockActionPacket$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
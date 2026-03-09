/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum State
/*     */ {
/*     */   public static final IntFunction<State> BY_ID;
/*  81 */   IDLING(0),
/*  82 */   FEELING_HAPPY(1),
/*  83 */   SCENTING(2),
/*  84 */   SNIFFING(3),
/*  85 */   SEARCHING(4),
/*  86 */   DIGGING(5),
/*  87 */   RISING(6);
/*     */   
/*     */   static  {
/*  90 */     BY_ID = ByIdMap.continuous(State::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/*  92 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, State::id);
/*     */   }
/*     */   
/*     */   public static final StreamCodec<ByteBuf, State> STREAM_CODEC;
/*     */   
/*  97 */   State(int id) { this.id = id; }
/*     */   
/*     */   private final int id;
/*     */   
/* 101 */   public int id() { return this.id; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sniffer\Sniffer$State.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
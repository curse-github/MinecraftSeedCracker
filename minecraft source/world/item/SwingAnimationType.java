/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum SwingAnimationType implements StringRepresentable {
/*    */   private static final IntFunction<SwingAnimationType> BY_ID;
/* 13 */   NONE(0, "none"),
/* 14 */   WHACK(1, "whack"),
/* 15 */   STAB(2, "stab"); public static final Codec<SwingAnimationType> CODEC;
/*    */   
/*    */   static  {
/* 18 */     BY_ID = ByIdMap.continuous(SwingAnimationType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 19 */     CODEC = StringRepresentable.fromEnum(SwingAnimationType::values);
/* 20 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SwingAnimationType::getId);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, SwingAnimationType> STREAM_CODEC;
/*    */   
/*    */   SwingAnimationType(int id, String name) {
/* 26 */     this.id = id;
/* 27 */     this.name = name;
/*    */   }
/*    */   private final int id; private final String name;
/*    */   
/* 31 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SwingAnimationType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
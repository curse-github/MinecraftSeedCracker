/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
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
/*    */ public static enum Status
/*    */   implements StringRepresentable
/*    */ {
/*    */   private static final IntFunction<Status> ID_MAP;
/*    */   public static final Codec<Status> CODEC;
/* 67 */   CLEARED("cleared", 0),
/* 68 */   RUNNING("running", 1),
/* 69 */   FINISHED("finished", 2);
/*    */   static  {
/* 71 */     ID_MAP = ByIdMap.continuous(s -> s.index, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 72 */     CODEC = StringRepresentable.fromEnum(Status::values);
/* 73 */     STREAM_CODEC = ByteBufCodecs.idMapper(Status::byIndex, s -> s.index);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Status> STREAM_CODEC;
/*    */   
/*    */   Status(String id, int index) {
/* 79 */     this.id = id;
/* 80 */     this.index = index;
/*    */   }
/*    */   private final String id;
/*    */   private final int index;
/*    */   
/* 85 */   public String getSerializedName() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public static Status byIndex(int index) { return (Status)ID_MAP.apply(index); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity$Status.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
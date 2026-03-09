/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Pose implements StringRepresentable {
/*    */   public static final IntFunction<Pose> BY_ID;
/* 13 */   STANDING(0, "standing"),
/* 14 */   FALL_FLYING(1, "fall_flying"),
/* 15 */   SLEEPING(2, "sleeping"),
/* 16 */   SWIMMING(3, "swimming"),
/* 17 */   SPIN_ATTACK(4, "spin_attack"),
/* 18 */   CROUCHING(5, "crouching"),
/* 19 */   LONG_JUMPING(6, "long_jumping"),
/* 20 */   DYING(7, "dying"),
/* 21 */   CROAKING(8, "croaking"),
/* 22 */   USING_TONGUE(9, "using_tongue"),
/* 23 */   SITTING(10, "sitting"),
/*    */   
/* 25 */   ROARING(11, "roaring"),
/* 26 */   SNIFFING(12, "sniffing"),
/* 27 */   EMERGING(13, "emerging"),
/* 28 */   DIGGING(14, "digging"),
/* 29 */   SLIDING(15, "sliding"),
/* 30 */   SHOOTING(16, "shooting"),
/* 31 */   INHALING(17, "inhaling"); public static final Codec<Pose> CODEC;
/*    */   
/*    */   static  {
/* 34 */     BY_ID = ByIdMap.continuous(Pose::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 36 */     CODEC = StringRepresentable.fromEnum(Pose::values);
/*    */     
/* 38 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Pose::id);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Pose> STREAM_CODEC;
/*    */   
/*    */   Pose(int id, String name) {
/* 44 */     this.id = id;
/* 45 */     this.name = name;
/*    */   }
/*    */   private final int id; private final String name;
/*    */   
/* 49 */   public int id() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Pose.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
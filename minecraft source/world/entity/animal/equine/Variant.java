/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Variant implements StringRepresentable {
/*    */   public static final Codec<Variant> CODEC;
/* 13 */   WHITE(0, "white"),
/* 14 */   CREAMY(1, "creamy"),
/* 15 */   CHESTNUT(2, "chestnut"),
/* 16 */   BROWN(3, "brown"),
/* 17 */   BLACK(4, "black"),
/* 18 */   GRAY(5, "gray"),
/* 19 */   DARK_BROWN(6, "dark_brown"); private static final IntFunction<Variant> BY_ID;
/*    */   
/*    */   static  {
/* 22 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*    */     
/* 24 */     BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*    */     
/* 26 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*    */   
/*    */   Variant(int id, String name) {
/* 32 */     this.id = id;
/* 33 */     this.name = name;
/*    */   }
/*    */   private final int id; private final String name;
/*    */   
/* 37 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
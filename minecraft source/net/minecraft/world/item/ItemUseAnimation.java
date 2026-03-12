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
/*    */ public static enum ItemUseAnimation
/*    */   implements StringRepresentable {
/* 13 */   NONE(0, "none"),
/* 14 */   EAT(1, "eat", true),
/* 15 */   DRINK(2, "drink", true),
/* 16 */   BLOCK(3, "block"),
/* 17 */   BOW(4, "bow"),
/* 18 */   TRIDENT(5, "trident"),
/* 19 */   CROSSBOW(6, "crossbow"),
/* 20 */   SPYGLASS(7, "spyglass"),
/* 21 */   TOOT_HORN(8, "toot_horn"),
/* 22 */   BRUSH(9, "brush"),
/* 23 */   BUNDLE(10, "bundle"),
/* 24 */   SPEAR(11, "spear", true);
/*    */   
/*    */   static  {
/* 27 */     BY_ID = ByIdMap.continuous(ItemUseAnimation::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 28 */     CODEC = StringRepresentable.fromEnum(ItemUseAnimation::values);
/* 29 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ItemUseAnimation::getId);
/*    */   }
/*    */   
/*    */   private static final IntFunction<ItemUseAnimation> BY_ID;
/*    */   public static final Codec<ItemUseAnimation> CODEC;
/*    */   public static final StreamCodec<ByteBuf, ItemUseAnimation> STREAM_CODEC;
/*    */   private final int id;
/*    */   private final String name;
/*    */   private final boolean customArmTransform;
/*    */   
/*    */   ItemUseAnimation(int id, String name, boolean customArmTransform) {
/* 40 */     this.id = id;
/* 41 */     this.name = name;
/* 42 */     this.customArmTransform = customArmTransform;
/*    */   }
/*    */ 
/*    */   
/* 46 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean hasCustomArmTransform() { return this.customArmTransform; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemUseAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
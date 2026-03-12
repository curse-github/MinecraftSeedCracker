/*    */ package net.minecraft.world.item;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Rarity implements StringRepresentable {
/*    */   public static final Codec<Rarity> CODEC;
/*    */   public static final IntFunction<Rarity> BY_ID;
/* 14 */   COMMON(0, "common", ChatFormatting.WHITE),
/* 15 */   UNCOMMON(1, "uncommon", ChatFormatting.YELLOW),
/* 16 */   RARE(2, "rare", ChatFormatting.AQUA),
/* 17 */   EPIC(3, "epic", ChatFormatting.LIGHT_PURPLE);
/*    */   static  {
/* 19 */     CODEC = StringRepresentable.fromValues(Rarity::values);
/*    */     
/* 21 */     BY_ID = ByIdMap.continuous(r -> r.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 22 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
/*    */   }
/*    */   public static final StreamCodec<ByteBuf, Rarity> STREAM_CODEC; private final int id;
/*    */   private final String name;
/*    */   private final ChatFormatting color;
/*    */   
/*    */   Rarity(int id, String name, ChatFormatting color) {
/* 29 */     this.id = id;
/* 30 */     this.name = name;
/* 31 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/* 35 */   public ChatFormatting color() { return this.color; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\Rarity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
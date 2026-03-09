/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum CraftingBookCategory implements StringRepresentable {
/*    */   public static final Codec<CraftingBookCategory> CODEC;
/* 13 */   BUILDING("building", 0),
/* 14 */   REDSTONE("redstone", 1),
/* 15 */   EQUIPMENT("equipment", 2),
/* 16 */   MISC("misc", 3);
/*    */   
/*    */   static  {
/* 19 */     CODEC = StringRepresentable.fromEnum(CraftingBookCategory::values);
/*    */     
/* 21 */     BY_ID = ByIdMap.continuous(CraftingBookCategory::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CraftingBookCategory::id);
/*    */   }
/*    */   public static final IntFunction<CraftingBookCategory> BY_ID;
/*    */   public static final StreamCodec<ByteBuf, CraftingBookCategory> STREAM_CODEC;
/*    */   
/*    */   CraftingBookCategory(String name, int id) {
/* 29 */     this.name = name;
/* 30 */     this.id = id;
/*    */   }
/*    */   private final String name;
/*    */   private final int id;
/*    */   
/* 35 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   private int id() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\CraftingBookCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
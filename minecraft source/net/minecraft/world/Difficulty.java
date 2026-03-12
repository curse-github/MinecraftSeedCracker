/*    */ package net.minecraft.world;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Difficulty
/*    */   implements StringRepresentable {
/*    */   public static final StringRepresentable.EnumCodec<Difficulty> CODEC;
/* 14 */   PEACEFUL(0, "peaceful"),
/* 15 */   EASY(1, "easy"),
/* 16 */   NORMAL(2, "normal"),
/* 17 */   HARD(3, "hard"); private static final IntFunction<Difficulty> BY_ID;
/*    */   
/*    */   static  {
/* 20 */     CODEC = StringRepresentable.fromEnum(Difficulty::values);
/*    */     
/* 22 */     BY_ID = ByIdMap.continuous(Difficulty::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Difficulty::getId);
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Difficulty> STREAM_CODEC;
/*    */   
/*    */   Difficulty(int id, String key) {
/* 29 */     this.id = id;
/* 30 */     this.key = key;
/*    */   }
/*    */   private final int id; private final String key;
/*    */   
/* 34 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public Component getDisplayName() { return Component.translatable("options.difficulty." + this.key); }
/*    */ 
/*    */   
/*    */   public Component getInfo() {
/* 42 */     return Component.translatable("options.difficulty." + this.key + ".info");
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/* 47 */   public static Difficulty byId(int id) { return (Difficulty)BY_ID.apply(id); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static Difficulty byName(String name) { return (Difficulty)CODEC.byName(name); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public String getKey() { return this.key; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public String getSerializedName() { return this.key; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\Difficulty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
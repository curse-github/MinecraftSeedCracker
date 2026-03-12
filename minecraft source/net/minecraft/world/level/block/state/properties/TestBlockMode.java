/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum TestBlockMode implements StringRepresentable {
/*    */   private static final IntFunction<TestBlockMode> BY_ID;
/* 14 */   START(0, "start"),
/* 15 */   LOG(1, "log"),
/* 16 */   FAIL(2, "fail"),
/* 17 */   ACCEPT(3, "accept"); public static final Codec<TestBlockMode> CODEC; public static final StreamCodec<ByteBuf, TestBlockMode> STREAM_CODEC;
/*    */   
/*    */   static  {
/* 20 */     BY_ID = ByIdMap.continuous(mode -> mode.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 22 */     CODEC = StringRepresentable.fromEnum(TestBlockMode::values);
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, mode -> mode.id);
/*    */   }
/*    */   private final int id;
/*    */   private final String name;
/*    */   private final Component displayName;
/*    */   private final Component detailedMessage;
/*    */   
/*    */   TestBlockMode(int id, String name) {
/* 31 */     this.id = id;
/* 32 */     this.name = name;
/* 33 */     this.displayName = Component.translatable("test_block.mode." + name);
/* 34 */     this.detailedMessage = Component.translatable("test_block.mode_info." + name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Component getDisplayName() { return this.displayName; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Component getDetailedMessage() { return this.detailedMessage; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\TestBlockMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
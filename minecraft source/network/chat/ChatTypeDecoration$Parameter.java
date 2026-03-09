/*    */ package net.minecraft.network.chat;
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
/*    */ public static enum Parameter
/*    */   implements StringRepresentable
/*    */ {
/*    */   private static final IntFunction<Parameter> BY_ID;
/*    */   public static final Codec<Parameter> CODEC;
/*    */   public static final StreamCodec<ByteBuf, Parameter> STREAM_CODEC;
/* 63 */   SENDER(0, "sender", (content, chatType) -> chatType.name()),
/* 64 */   TARGET(1, "target", (content, chatType) -> (Component)chatType.targetName().orElse(CommonComponents.EMPTY)),
/* 65 */   CONTENT(2, "content", (content, chatType) -> content);
/*    */   static  {
/* 67 */     BY_ID = ByIdMap.continuous(p -> p.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 69 */     CODEC = StringRepresentable.fromEnum(Parameter::values);
/* 70 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, p -> p.id);
/*    */   }
/*    */   private final int id;
/*    */   private final String name;
/*    */   private final Selector selector;
/*    */   
/*    */   Parameter(int id, String name, Selector selector) {
/* 77 */     this.id = id;
/* 78 */     this.name = name;
/* 79 */     this.selector = selector;
/*    */   }
/*    */ 
/*    */   
/* 83 */   public Component select(Component content, ChatType.Bound chatType) { return this.selector.select(content, chatType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   public String getSerializedName() { return this.name; }
/*    */   
/*    */   public static interface Selector {
/*    */     Component select(Component param2Component, ChatType.Bound param2Bound);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ChatTypeDecoration$Parameter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
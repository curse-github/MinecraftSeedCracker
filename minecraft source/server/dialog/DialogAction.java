/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DialogAction implements StringRepresentable {
/*    */   public static final IntFunction<DialogAction> BY_ID;
/* 12 */   CLOSE(0, "close"),
/* 13 */   NONE(1, "none"),
/* 14 */   WAIT_FOR_RESPONSE(2, "wait_for_response");
/*    */   
/*    */   static  {
/* 17 */     BY_ID = ByIdMap.continuous(s -> s.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 19 */     CODEC = StringRepresentable.fromEnum(DialogAction::values);
/* 20 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, s -> s.id);
/*    */   }
/*    */   public static final StringRepresentable.EnumCodec<DialogAction> CODEC;
/*    */   public static final StreamCodec<ByteBuf, DialogAction> STREAM_CODEC;
/*    */   
/*    */   DialogAction(int id, String name) {
/* 26 */     this.id = id;
/* 27 */     this.name = name;
/*    */   }
/*    */   private final int id;
/*    */   private final String name;
/*    */   
/* 32 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean willUnpause() { return (this == CLOSE || this == WAIT_FOR_RESPONSE); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\DialogAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
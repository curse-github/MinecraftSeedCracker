/*    */ package net.minecraft.server;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.net.URI;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
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
/*    */ 
/*    */ public static enum KnownLinkType
/*    */ {
/*    */   private static final IntFunction<KnownLinkType> BY_ID;
/*    */   public static final StreamCodec<ByteBuf, KnownLinkType> STREAM_CODEC;
/* 67 */   BUG_REPORT(0, "report_bug"),
/* 68 */   COMMUNITY_GUIDELINES(1, "community_guidelines"),
/* 69 */   SUPPORT(2, "support"),
/* 70 */   STATUS(3, "status"),
/* 71 */   FEEDBACK(4, "feedback"),
/* 72 */   COMMUNITY(5, "community"),
/* 73 */   WEBSITE(6, "website"),
/* 74 */   FORUMS(7, "forums"),
/* 75 */   NEWS(8, "news"),
/* 76 */   ANNOUNCEMENTS(9, "announcements");
/*    */   
/*    */   static  {
/* 79 */     BY_ID = ByIdMap.continuous(e -> e.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 81 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */   }
/*    */   
/*    */   private final int id;
/*    */   
/*    */   KnownLinkType(int id, String name) {
/* 87 */     this.id = id;
/* 88 */     this.name = name;
/*    */   }
/*    */   private final String name;
/*    */   
/* 92 */   private Component displayName() { return Component.translatable("known_server_link." + this.name); }
/*    */ 
/*    */ 
/*    */   
/* 96 */   public ServerLinks.Entry create(URI link) { return ServerLinks.Entry.knownType(this, link); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerLinks$KnownLinkType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
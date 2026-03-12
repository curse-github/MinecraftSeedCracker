/*    */ package net.minecraft.network.protocol.game;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ServerboundEditBookPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int slot;
/*    */   private final List<String> pages;
/*    */   private final Optional<String> title;
/*    */   
/* 14 */   public int slot() { return this.slot; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public List<String> pages() { return this.pages; } public Optional<String> title() { return this.title; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final StreamCodec<FriendlyByteBuf, ServerboundEditBookPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundEditBookPacket::slot, 
/*    */       
/* 21 */       ByteBufCodecs.stringUtf8(1024).apply(ByteBufCodecs.list(100)), ServerboundEditBookPacket::pages, 
/* 22 */       ByteBufCodecs.stringUtf8(32).apply(ByteBufCodecs::optional), ServerboundEditBookPacket::title, ServerboundEditBookPacket::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public ServerboundEditBookPacket(int slot, List<String> pages, Optional<String> title) {
/* 27 */     pages = List.copyOf(pages);
/*    */     this.slot = slot;
/*    */     this.pages = pages;
/*    */     this.title = title;
/*    */   }
/* 32 */   public PacketType<ServerboundEditBookPacket> type() { return GamePacketTypes.SERVERBOUND_EDIT_BOOK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void handle(ServerGamePacketListener listener) { listener.handleEditBook(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundEditBookPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
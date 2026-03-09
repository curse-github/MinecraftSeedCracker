/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*    */ 
/*    */ public final class ClientboundRecipeBookAddPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final List<Entry> entries;
/*    */   private final boolean replace;
/*    */   
/* 12 */   public ClientboundRecipeBookAddPacket(List<Entry> entries, boolean replace) { this.entries = entries; this.replace = replace; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket; } public List<Entry> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public boolean replace() { return this.replace; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeBookAddPacket> STREAM_CODEC = StreamCodec.composite(Entry.STREAM_CODEC
/* 17 */       .apply(ByteBufCodecs.list()), ClientboundRecipeBookAddPacket::entries, ByteBufCodecs.BOOL, ClientboundRecipeBookAddPacket::replace, ClientboundRecipeBookAddPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public PacketType<ClientboundRecipeBookAddPacket> type() { return GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_ADD; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void handle(ClientGamePacketListener listener) { listener.handleRecipeBookAdd(this); }
/*    */   public static final class Entry extends Record { private final RecipeDisplayEntry contents; private final byte flags; public static final byte FLAG_NOTIFICATION = 1; public static final byte FLAG_HIGHLIGHT = 2;
/*    */     
/* 32 */     public Entry(RecipeDisplayEntry contents, byte flags) { this.contents = contents; this.flags = flags; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;
/* 32 */       //   0	8	1	o	Ljava/lang/Object; } public RecipeDisplayEntry contents() { return this.contents; } public byte flags() { return this.flags; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(RecipeDisplayEntry.STREAM_CODEC, Entry::contents, ByteBufCodecs.BYTE, Entry::flags, Entry::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     public Entry(RecipeDisplayEntry contents, boolean notification, boolean highlight) { this(contents, (byte)((notification ? 1 : 0) | (highlight ? 2 : 0))); }
/*    */ 
/*    */ 
/*    */     
/* 50 */     public boolean notification() { return ((this.flags & true) != 0); }
/*    */ 
/*    */ 
/*    */     
/* 54 */     public boolean highlight() { return ((this.flags & 0x2) != 0); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRecipeBookAddPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*    */ 
/*    */ public final class ServerboundPlaceRecipePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int containerId;
/*    */   private final RecipeDisplayId recipe;
/*    */   private final boolean useMaxItems;
/*    */   
/* 10 */   public ServerboundPlaceRecipePacket(int containerId, RecipeDisplayId recipe, boolean useMaxItems) { this.containerId = containerId; this.recipe = recipe; this.useMaxItems = useMaxItems; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket; } public int containerId() { return this.containerId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public RecipeDisplayId recipe() { return this.recipe; } public boolean useMaxItems() { return this.useMaxItems; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlaceRecipePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundPlaceRecipePacket::containerId, RecipeDisplayId.STREAM_CODEC, ServerboundPlaceRecipePacket::recipe, ByteBufCodecs.BOOL, ServerboundPlaceRecipePacket::useMaxItems, ServerboundPlaceRecipePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public PacketType<ServerboundPlaceRecipePacket> type() { return GamePacketTypes.SERVERBOUND_PLACE_RECIPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void handle(ServerGamePacketListener listener) { listener.handlePlaceRecipe(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlaceRecipePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
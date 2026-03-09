/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ 
/*    */ public final class ClientboundPlaceGhostRecipePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int containerId;
/*    */   private final RecipeDisplay recipeDisplay;
/*    */   
/* 10 */   public ClientboundPlaceGhostRecipePacket(int containerId, RecipeDisplay recipeDisplay) { this.containerId = containerId; this.recipeDisplay = recipeDisplay; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket; } public int containerId() { return this.containerId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public RecipeDisplay recipeDisplay() { return this.recipeDisplay; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlaceGhostRecipePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundPlaceGhostRecipePacket::containerId, RecipeDisplay.STREAM_CODEC, ClientboundPlaceGhostRecipePacket::recipeDisplay, ClientboundPlaceGhostRecipePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundPlaceGhostRecipePacket> type() { return GamePacketTypes.CLIENTBOUND_PLACE_GHOST_RECIPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handlePlaceRecipe(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlaceGhostRecipePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Map;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*    */ import net.minecraft.world.item.crafting.StonecutterRecipe;
/*    */ 
/*    */ public final class ClientboundUpdateRecipesPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets;
/*    */   private final SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes;
/*    */   
/* 19 */   public ClientboundUpdateRecipesPacket(Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets, SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes) { this.itemSets = itemSets; this.stonecutterRecipes = stonecutterRecipes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket; } public Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets() { return this.itemSets; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes() { return this.stonecutterRecipes; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateRecipesPacket> STREAM_CODEC = StreamCodec.composite(
/* 24 */       ByteBufCodecs.map(java.util.HashMap::new, ResourceKey.streamCodec(RecipePropertySet.TYPE_KEY), RecipePropertySet.STREAM_CODEC), ClientboundUpdateRecipesPacket::itemSets, 
/* 25 */       SelectableRecipe.SingleInputSet.noRecipeCodec(), ClientboundUpdateRecipesPacket::stonecutterRecipes, ClientboundUpdateRecipesPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public PacketType<ClientboundUpdateRecipesPacket> type() { return GamePacketTypes.CLIENTBOUND_UPDATE_RECIPES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void handle(ClientGamePacketListener listener) { listener.handleUpdateRecipes(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateRecipesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
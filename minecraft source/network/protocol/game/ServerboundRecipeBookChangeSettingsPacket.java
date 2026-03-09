/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.inventory.RecipeBookType;
/*    */ 
/*    */ public class ServerboundRecipeBookChangeSettingsPacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundRecipeBookChangeSettingsPacket> STREAM_CODEC = Packet.codec(ServerboundRecipeBookChangeSettingsPacket::write, ServerboundRecipeBookChangeSettingsPacket::new);
/*    */   
/*    */   private final RecipeBookType bookType;
/*    */   private final boolean isOpen;
/*    */   private final boolean isFiltering;
/*    */   
/*    */   public ServerboundRecipeBookChangeSettingsPacket(RecipeBookType bookType, boolean isOpen, boolean isFiltering) {
/* 17 */     this.bookType = bookType;
/* 18 */     this.isOpen = isOpen;
/* 19 */     this.isFiltering = isFiltering;
/*    */   }
/*    */   
/*    */   private ServerboundRecipeBookChangeSettingsPacket(FriendlyByteBuf input) {
/* 23 */     this.bookType = (RecipeBookType)input.readEnum(RecipeBookType.class);
/* 24 */     this.isOpen = input.readBoolean();
/* 25 */     this.isFiltering = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeEnum(this.bookType);
/* 30 */     output.writeBoolean(this.isOpen);
/* 31 */     output.writeBoolean(this.isFiltering);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public PacketType<ServerboundRecipeBookChangeSettingsPacket> type() { return GamePacketTypes.SERVERBOUND_RECIPE_BOOK_CHANGE_SETTINGS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void handle(ServerGamePacketListener listener) { listener.handleRecipeBookChangeSettingsPacket(this); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public RecipeBookType getBookType() { return this.bookType; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public boolean isOpen() { return this.isOpen; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean isFiltering() { return this.isFiltering; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundRecipeBookChangeSettingsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPlayerActionPacket extends Object implements Packet<ServerGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerActionPacket> STREAM_CODEC = Packet.codec(ServerboundPlayerActionPacket::write, ServerboundPlayerActionPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final Direction direction;
/*    */   private final Action action;
/*    */   private final int sequence;
/*    */   
/*    */   public ServerboundPlayerActionPacket(Action action, BlockPos pos, Direction direction, int sequence) {
/* 19 */     this.action = action;
/* 20 */     this.pos = pos.immutable();
/* 21 */     this.direction = direction;
/* 22 */     this.sequence = sequence;
/*    */   }
/*    */ 
/*    */   
/* 26 */   public ServerboundPlayerActionPacket(Action action, BlockPos pos, Direction direction) { this(action, pos, direction, 0); }
/*    */ 
/*    */   
/*    */   private ServerboundPlayerActionPacket(FriendlyByteBuf input) {
/* 30 */     this.action = (Action)input.readEnum(Action.class);
/* 31 */     this.pos = input.readBlockPos();
/* 32 */     this.direction = Direction.from3DDataValue(input.readUnsignedByte());
/* 33 */     this.sequence = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 37 */     output.writeEnum(this.action);
/* 38 */     output.writeBlockPos(this.pos);
/* 39 */     output.writeByte(this.direction.get3DDataValue());
/* 40 */     output.writeVarInt(this.sequence);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public PacketType<ServerboundPlayerActionPacket> type() { return GamePacketTypes.SERVERBOUND_PLAYER_ACTION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public void handle(ServerGamePacketListener listener) { listener.handlePlayerAction(this); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public Direction getDirection() { return this.direction; }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Action getAction() { return this.action; }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public int getSequence() { return this.sequence; }
/*    */   
/*    */   public enum Action
/*    */   {
/* 70 */     START_DESTROY_BLOCK,
/* 71 */     ABORT_DESTROY_BLOCK,
/* 72 */     STOP_DESTROY_BLOCK,
/* 73 */     DROP_ALL_ITEMS,
/* 74 */     DROP_ITEM,
/* 75 */     RELEASE_USE_ITEM,
/* 76 */     SWAP_ITEM_WITH_OFFHAND,
/* 77 */     STAB;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerActionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
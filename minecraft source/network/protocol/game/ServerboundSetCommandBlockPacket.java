/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*    */ 
/*    */ public class ServerboundSetCommandBlockPacket extends Object implements Packet<ServerGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetCommandBlockPacket> STREAM_CODEC = Packet.codec(ServerboundSetCommandBlockPacket::write, ServerboundSetCommandBlockPacket::new);
/*    */   
/*    */   private static final int FLAG_TRACK_OUTPUT = 1;
/*    */   
/*    */   private static final int FLAG_CONDITIONAL = 2;
/*    */   private static final int FLAG_AUTOMATIC = 4;
/*    */   private final BlockPos pos;
/*    */   private final String command;
/*    */   private final boolean trackOutput;
/*    */   private final boolean conditional;
/*    */   private final boolean automatic;
/*    */   private final CommandBlockEntity.Mode mode;
/*    */   
/*    */   public ServerboundSetCommandBlockPacket(BlockPos pos, String command, CommandBlockEntity.Mode mode, boolean trackOutput, boolean conditional, boolean automatic) {
/* 25 */     this.pos = pos;
/* 26 */     this.command = command;
/* 27 */     this.trackOutput = trackOutput;
/* 28 */     this.conditional = conditional;
/* 29 */     this.automatic = automatic;
/* 30 */     this.mode = mode;
/*    */   }
/*    */   
/*    */   private ServerboundSetCommandBlockPacket(FriendlyByteBuf input) {
/* 34 */     this.pos = input.readBlockPos();
/* 35 */     this.command = input.readUtf();
/* 36 */     this.mode = (CommandBlockEntity.Mode)input.readEnum(CommandBlockEntity.Mode.class);
/* 37 */     int flags = input.readByte();
/* 38 */     this.trackOutput = ((flags & true) != 0);
/* 39 */     this.conditional = ((flags & 0x2) != 0);
/* 40 */     this.automatic = ((flags & 0x4) != 0);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 44 */     output.writeBlockPos(this.pos);
/* 45 */     output.writeUtf(this.command);
/* 46 */     output.writeEnum(this.mode);
/* 47 */     int flags = 0;
/* 48 */     if (this.trackOutput) {
/* 49 */       flags |= 0x1;
/*    */     }
/* 51 */     if (this.conditional) {
/* 52 */       flags |= 0x2;
/*    */     }
/* 54 */     if (this.automatic) {
/* 55 */       flags |= 0x4;
/*    */     }
/* 57 */     output.writeByte(flags);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public PacketType<ServerboundSetCommandBlockPacket> type() { return GamePacketTypes.SERVERBOUND_SET_COMMAND_BLOCK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public void handle(ServerGamePacketListener listener) { listener.handleSetCommandBlock(this); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public String getCommand() { return this.command; }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public boolean isTrackOutput() { return this.trackOutput; }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public boolean isConditional() { return this.conditional; }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public boolean isAutomatic() { return this.automatic; }
/*    */ 
/*    */ 
/*    */   
/* 91 */   public CommandBlockEntity.Mode getMode() { return this.mode; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCommandBlockPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
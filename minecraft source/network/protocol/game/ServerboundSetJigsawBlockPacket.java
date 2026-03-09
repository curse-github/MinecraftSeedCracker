/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*    */ 
/*    */ public class ServerboundSetJigsawBlockPacket extends Object implements Packet<ServerGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetJigsawBlockPacket> STREAM_CODEC = Packet.codec(ServerboundSetJigsawBlockPacket::write, ServerboundSetJigsawBlockPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final Identifier name;
/*    */   private final Identifier target;
/*    */   private final Identifier pool;
/*    */   private final String finalState;
/*    */   private final JigsawBlockEntity.JointType joint;
/*    */   private final int selectionPriority;
/*    */   private final int placementPriority;
/*    */   
/*    */   public ServerboundSetJigsawBlockPacket(BlockPos blockPos, Identifier name, Identifier target, Identifier pool, String finalState, JigsawBlockEntity.JointType joint, int selectionPriority, int placementPriority) {
/* 24 */     this.pos = blockPos;
/* 25 */     this.name = name;
/* 26 */     this.target = target;
/* 27 */     this.pool = pool;
/* 28 */     this.finalState = finalState;
/* 29 */     this.joint = joint;
/* 30 */     this.selectionPriority = selectionPriority;
/* 31 */     this.placementPriority = placementPriority;
/*    */   }
/*    */   
/*    */   private ServerboundSetJigsawBlockPacket(FriendlyByteBuf input) {
/* 35 */     this.pos = input.readBlockPos();
/* 36 */     this.name = input.readIdentifier();
/* 37 */     this.target = input.readIdentifier();
/* 38 */     this.pool = input.readIdentifier();
/* 39 */     this.finalState = input.readUtf();
/* 40 */     this.joint = (JigsawBlockEntity.JointType)JigsawBlockEntity.JointType.CODEC.byName(input.readUtf(), JigsawBlockEntity.JointType.ALIGNED);
/* 41 */     this.selectionPriority = input.readVarInt();
/* 42 */     this.placementPriority = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 46 */     output.writeBlockPos(this.pos);
/* 47 */     output.writeIdentifier(this.name);
/* 48 */     output.writeIdentifier(this.target);
/* 49 */     output.writeIdentifier(this.pool);
/* 50 */     output.writeUtf(this.finalState);
/* 51 */     output.writeUtf(this.joint.getSerializedName());
/* 52 */     output.writeVarInt(this.selectionPriority);
/* 53 */     output.writeVarInt(this.placementPriority);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public PacketType<ServerboundSetJigsawBlockPacket> type() { return GamePacketTypes.SERVERBOUND_SET_JIGSAW_BLOCK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public void handle(ServerGamePacketListener listener) { listener.handleSetJigsawBlock(this); }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public Identifier getName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public Identifier getTarget() { return this.target; }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public Identifier getPool() { return this.pool; }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public String getFinalState() { return this.finalState; }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public JigsawBlockEntity.JointType getJoint() { return this.joint; }
/*    */ 
/*    */ 
/*    */   
/* 91 */   public int getSelectionPriority() { return this.selectionPriority; }
/*    */ 
/*    */ 
/*    */   
/* 95 */   public int getPlacementPriority() { return this.placementPriority; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetJigsawBlockPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
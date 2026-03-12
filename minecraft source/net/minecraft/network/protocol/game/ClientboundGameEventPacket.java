/*    */ package net.minecraft.network.protocol.game;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundGameEventPacket extends Object implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundGameEventPacket> STREAM_CODEC = Packet.codec(ClientboundGameEventPacket::write, ClientboundGameEventPacket::new);
/*    */   
/*    */   public static class Type {
/* 15 */     private static final Int2ObjectMap<Type> TYPES = new Int2ObjectOpenHashMap();
/*    */     
/*    */     private final int id;
/*    */     
/*    */     public Type(int id) {
/* 20 */       this.id = id;
/* 21 */       TYPES.put(id, this);
/*    */     }
/*    */   }
/*    */   
/* 25 */   public static final Type NO_RESPAWN_BLOCK_AVAILABLE = new Type(0);
/* 26 */   public static final Type START_RAINING = new Type(1);
/* 27 */   public static final Type STOP_RAINING = new Type(2);
/* 28 */   public static final Type CHANGE_GAME_MODE = new Type(3);
/* 29 */   public static final Type WIN_GAME = new Type(4);
/* 30 */   public static final Type DEMO_EVENT = new Type(5);
/* 31 */   public static final Type PLAY_ARROW_HIT_SOUND = new Type(6);
/* 32 */   public static final Type RAIN_LEVEL_CHANGE = new Type(7);
/* 33 */   public static final Type THUNDER_LEVEL_CHANGE = new Type(8);
/* 34 */   public static final Type PUFFER_FISH_STING = new Type(9);
/* 35 */   public static final Type GUARDIAN_ELDER_EFFECT = new Type(10);
/* 36 */   public static final Type IMMEDIATE_RESPAWN = new Type(11);
/* 37 */   public static final Type LIMITED_CRAFTING = new Type(12);
/* 38 */   public static final Type LEVEL_CHUNKS_LOAD_START = new Type(13);
/*    */   
/*    */   public static final int DEMO_PARAM_INTRO = 0;
/*    */   
/*    */   public static final int DEMO_PARAM_HINT_1 = 101;
/*    */   public static final int DEMO_PARAM_HINT_2 = 102;
/*    */   public static final int DEMO_PARAM_HINT_3 = 103;
/*    */   public static final int DEMO_PARAM_HINT_4 = 104;
/*    */   private final Type event;
/*    */   private final float param;
/*    */   
/*    */   public ClientboundGameEventPacket(Type event, float param) {
/* 50 */     this.event = event;
/* 51 */     this.param = param;
/*    */   }
/*    */   
/*    */   private ClientboundGameEventPacket(FriendlyByteBuf input) {
/* 55 */     this.event = (Type)Type.TYPES.get(input.readUnsignedByte());
/* 56 */     this.param = input.readFloat();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 60 */     output.writeByte(this.event.id);
/* 61 */     output.writeFloat(this.param);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public PacketType<ClientboundGameEventPacket> type() { return GamePacketTypes.CLIENTBOUND_GAME_EVENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public void handle(ClientGamePacketListener listener) { listener.handleGameEvent(this); }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public Type getEvent() { return this.event; }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public float getParam() { return this.param; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundGameEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
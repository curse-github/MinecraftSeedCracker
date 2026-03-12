/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.player.Abilities;
/*    */ 
/*    */ public class ClientboundPlayerAbilitiesPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerAbilitiesPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerAbilitiesPacket::write, ClientboundPlayerAbilitiesPacket::new);
/*    */   
/*    */   private static final int FLAG_INVULNERABLE = 1;
/*    */   
/*    */   private static final int FLAG_FLYING = 2;
/*    */   
/*    */   private static final int FLAG_CAN_FLY = 4;
/*    */   private static final int FLAG_INSTABUILD = 8;
/*    */   private final boolean invulnerable;
/*    */   private final boolean isFlying;
/*    */   private final boolean canFly;
/*    */   private final boolean instabuild;
/*    */   private final float flyingSpeed;
/*    */   private final float walkingSpeed;
/*    */   
/*    */   public ClientboundPlayerAbilitiesPacket(Abilities abilities) {
/* 26 */     this.invulnerable = abilities.invulnerable;
/* 27 */     this.isFlying = abilities.flying;
/* 28 */     this.canFly = abilities.mayfly;
/* 29 */     this.instabuild = abilities.instabuild;
/* 30 */     this.flyingSpeed = abilities.getFlyingSpeed();
/* 31 */     this.walkingSpeed = abilities.getWalkingSpeed();
/*    */   }
/*    */   
/*    */   private ClientboundPlayerAbilitiesPacket(FriendlyByteBuf input) {
/* 35 */     byte bitfield = input.readByte();
/*    */     
/* 37 */     this.invulnerable = ((bitfield & true) != 0);
/* 38 */     this.isFlying = ((bitfield & 0x2) != 0);
/* 39 */     this.canFly = ((bitfield & 0x4) != 0);
/* 40 */     this.instabuild = ((bitfield & 0x8) != 0);
/* 41 */     this.flyingSpeed = input.readFloat();
/* 42 */     this.walkingSpeed = input.readFloat();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 46 */     byte bitfield = 0;
/*    */     
/* 48 */     if (this.invulnerable) {
/* 49 */       bitfield = (byte)(bitfield | true);
/*    */     }
/* 51 */     if (this.isFlying) {
/* 52 */       bitfield = (byte)(bitfield | 0x2);
/*    */     }
/* 54 */     if (this.canFly) {
/* 55 */       bitfield = (byte)(bitfield | 0x4);
/*    */     }
/* 57 */     if (this.instabuild) {
/* 58 */       bitfield = (byte)(bitfield | 0x8);
/*    */     }
/*    */     
/* 61 */     output.writeByte(bitfield);
/* 62 */     output.writeFloat(this.flyingSpeed);
/* 63 */     output.writeFloat(this.walkingSpeed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public PacketType<ClientboundPlayerAbilitiesPacket> type() { return GamePacketTypes.CLIENTBOUND_PLAYER_ABILITIES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 73 */   public void handle(ClientGamePacketListener listener) { listener.handlePlayerAbilities(this); }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public boolean isInvulnerable() { return this.invulnerable; }
/*    */ 
/*    */ 
/*    */   
/* 81 */   public boolean isFlying() { return this.isFlying; }
/*    */ 
/*    */ 
/*    */   
/* 85 */   public boolean canFly() { return this.canFly; }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public boolean canInstabuild() { return this.instabuild; }
/*    */ 
/*    */ 
/*    */   
/* 93 */   public float getFlyingSpeed() { return this.flyingSpeed; }
/*    */ 
/*    */ 
/*    */   
/* 97 */   public float getWalkingSpeed() { return this.walkingSpeed; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerAbilitiesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.network.protocol.login;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.PublicKey;
/*    */ import java.util.Arrays;
/*    */ import javax.crypto.SecretKey;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.Crypt;
/*    */ import net.minecraft.util.CryptException;
/*    */ 
/*    */ public class ServerboundKeyPacket extends Object implements Packet<ServerLoginPacketListener> {
/* 16 */   public static final StreamCodec<FriendlyByteBuf, ServerboundKeyPacket> STREAM_CODEC = Packet.codec(ServerboundKeyPacket::write, ServerboundKeyPacket::new);
/*    */   
/*    */   private final byte[] keybytes;
/*    */   private final byte[] encryptedChallenge;
/*    */   
/*    */   public ServerboundKeyPacket(SecretKey secretKey, PublicKey publicKey, byte[] challenge) throws CryptException {
/* 22 */     this.keybytes = Crypt.encryptUsingKey(publicKey, secretKey.getEncoded());
/* 23 */     this.encryptedChallenge = Crypt.encryptUsingKey(publicKey, challenge);
/*    */   }
/*    */   
/*    */   private ServerboundKeyPacket(FriendlyByteBuf input) {
/* 27 */     this.keybytes = input.readByteArray();
/* 28 */     this.encryptedChallenge = input.readByteArray();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 32 */     output.writeByteArray(this.keybytes);
/* 33 */     output.writeByteArray(this.encryptedChallenge);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public PacketType<ServerboundKeyPacket> type() { return LoginPacketTypes.SERVERBOUND_KEY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public void handle(ServerLoginPacketListener listener) { listener.handleKey(this); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public SecretKey getSecretKey(PrivateKey privateKey) throws CryptException { return Crypt.decryptByteToSecretKey(privateKey, this.keybytes); }
/*    */ 
/*    */   
/*    */   public boolean isChallengeValid(byte[] challenge, PrivateKey privateKey) {
/*    */     try {
/* 52 */       return Arrays.equals(challenge, Crypt.decryptUsingKey(privateKey, this.encryptedChallenge));
/* 53 */     } catch (CryptException e) {
/* 54 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundKeyPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class ServerItemCooldowns
/*    */   extends ItemCooldowns {
/*    */   private final ServerPlayer player;
/*    */   
/* 11 */   public ServerItemCooldowns(ServerPlayer player) { this.player = player; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onCooldownStarted(Identifier cooldownGroup, int duration) {
/* 16 */     super.onCooldownStarted(cooldownGroup, duration);
/* 17 */     this.player.connection.send(new ClientboundCooldownPacket(cooldownGroup, duration));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCooldownEnded(Identifier cooldownGroup) {
/* 22 */     super.onCooldownEnded(cooldownGroup);
/* 23 */     this.player.connection.send(new ClientboundCooldownPacket(cooldownGroup, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ServerItemCooldowns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
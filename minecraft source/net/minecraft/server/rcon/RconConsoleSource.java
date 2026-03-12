/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import net.minecraft.commands.CommandSource;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RconConsoleSource implements CommandSource {
/*    */   private static final String RCON = "Rcon";
/* 14 */   private static final Component RCON_COMPONENT = Component.literal("Rcon"); private final StringBuffer buffer; public RconConsoleSource(MinecraftServer server) {
/* 15 */     this.buffer = new StringBuffer();
/*    */ 
/*    */ 
/*    */     
/* 19 */     this.server = server;
/*    */   }
/*    */   private final MinecraftServer server;
/*    */   
/* 23 */   public void prepareForCommand() { this.buffer.setLength(0); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public String getCommandResponse() { return this.buffer.toString(); }
/*    */ 
/*    */   
/*    */   public CommandSourceStack createCommandSourceStack() {
/* 31 */     ServerLevel level = this.server.overworld();
/* 32 */     return new CommandSourceStack(this, Vec3.atLowerCornerOf(level.getRespawnData().pos()), Vec2.ZERO, level, LevelBasedPermissionSet.OWNER, "Rcon", RCON_COMPONENT, this.server, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void sendSystemMessage(Component message) { this.buffer.append(message.getString()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public boolean acceptsSuccess() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean acceptsFailure() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean shouldInformAdmins() { return this.server.shouldRconBroadcast(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\rcon\RconConsoleSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
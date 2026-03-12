/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CloseableCommandBlockSource
/*     */   implements CommandSource, AutoCloseable
/*     */ {
/*     */   private final ServerLevel level;
/* 174 */   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT);
/*     */   
/*     */   private boolean closed;
/*     */ 
/*     */   
/* 179 */   protected CloseableCommandBlockSource(ServerLevel level) { this.level = level; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public boolean acceptsSuccess() { return (!this.closed && ((Boolean)this.level.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   public boolean acceptsFailure() { return !this.closed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public boolean shouldInformAdmins() { return (!this.closed && ((Boolean)this.level.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT)).booleanValue()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendSystemMessage(Component message) {
/* 199 */     if (!this.closed) {
/* 200 */       BaseCommandBlock.this.lastOutput = Component.literal("[" + TIME_FORMAT.format(ZonedDateTime.now()) + "] ").append(message);
/* 201 */       BaseCommandBlock.this.onUpdated(this.level);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public void close() throws Exception { this.closed = true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BaseCommandBlock$CloseableCommandBlockSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
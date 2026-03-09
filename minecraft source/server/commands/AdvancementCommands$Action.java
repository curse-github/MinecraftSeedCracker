/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.server.level.ServerPlayer;
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
/*     */ static final abstract enum Action
/*     */ {
/*     */   GRANT, REVOKE;
/*     */   private final String key;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/server/commands/AdvancementCommands$Action$1
/*     */     //   3: dup
/*     */     //   4: ldc 'GRANT'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'grant'
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   12: putstatic net/minecraft/server/commands/AdvancementCommands$Action.GRANT : Lnet/minecraft/server/commands/AdvancementCommands$Action;
/*     */     //   15: new net/minecraft/server/commands/AdvancementCommands$Action$2
/*     */     //   18: dup
/*     */     //   19: ldc 'REVOKE'
/*     */     //   21: iconst_1
/*     */     //   22: ldc 'revoke'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   27: putstatic net/minecraft/server/commands/AdvancementCommands$Action.REVOKE : Lnet/minecraft/server/commands/AdvancementCommands$Action;
/*     */     //   30: invokestatic $values : ()[Lnet/minecraft/server/commands/AdvancementCommands$Action;
/*     */     //   33: putstatic net/minecraft/server/commands/AdvancementCommands$Action.$VALUES : [Lnet/minecraft/server/commands/AdvancementCommands$Action;
/*     */     //   36: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #233	-> 0
/*     */     //   #251	-> 15
/*     */     //   #232	-> 30
/*     */   }
/*     */   
/* 274 */   Action(String key) { this.key = "commands.advancement." + key; }
/*     */ 
/*     */   
/*     */   public int perform(ServerPlayer player, Iterable<AdvancementHolder> advancements, boolean showAdvancements) {
/* 278 */     int count = 0;
/* 279 */     if (!showAdvancements)
/*     */     {
/* 281 */       player.getAdvancements().flushDirty(player, true);
/*     */     }
/* 283 */     for (AdvancementHolder advancement : advancements) {
/* 284 */       if (perform(player, advancement)) {
/* 285 */         count++;
/*     */       }
/*     */     } 
/* 288 */     if (!showAdvancements) {
/* 289 */       player.getAdvancements().flushDirty(player, false);
/*     */     }
/* 291 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 299 */   protected String getKey() { return this.key; }
/*     */   
/*     */   protected abstract boolean perform(ServerPlayer paramServerPlayer, AdvancementHolder paramAdvancementHolder);
/*     */   
/*     */   protected abstract boolean performCriterion(ServerPlayer paramServerPlayer, AdvancementHolder paramAdvancementHolder, String paramString);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\AdvancementCommands$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
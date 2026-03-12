/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class MinecraftGameRuleServiceImpl
/*    */   implements MinecraftGameRuleService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   private final GameRules gameRules;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftGameRuleServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
/* 19 */     this.server = server;
/* 20 */     this.gameRules = server.getWorldData().getGameRules();
/* 21 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> update, ClientInfo clientInfo) {
/* 26 */     GameRule<T> gameRule = update.gameRule();
/* 27 */     T oldValue = (T)this.gameRules.get(gameRule);
/* 28 */     T newValue = (T)update.value();
/* 29 */     this.gameRules.set(gameRule, newValue, this.server);
/* 30 */     this.jsonrpcLogger.log(clientInfo, "Game rule '{}' updated from '{}' to '{}'", new Object[] { gameRule.id(), gameRule.serialize(oldValue), gameRule.serialize(newValue) });
/* 31 */     return update;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> gameRule, T value) { return new GameRulesService.GameRuleUpdate(gameRule, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Stream<GameRule<?>> getAvailableGameRules() { return this.gameRules.availableRules(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public <T> T getRuleValue(GameRule<T> gameRule) { return (T)this.gameRules.get(gameRule); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftGameRuleServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
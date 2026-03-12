package net.minecraft.server.jsonrpc.internalapi;

import java.util.stream.Stream;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.jsonrpc.methods.GameRulesService;
import net.minecraft.world.level.gamerules.GameRule;

public interface MinecraftGameRuleService {
  <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> paramGameRuleUpdate, ClientInfo paramClientInfo);
  
  <T> T getRuleValue(GameRule<T> paramGameRule);
  
  <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> paramGameRule, T paramT);
  
  Stream<GameRule<?>> getAvailableGameRules();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftGameRuleService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
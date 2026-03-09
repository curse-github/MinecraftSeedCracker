/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
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
/*     */ public final class SetGameRules
/*     */   extends Record
/*     */   implements TestEnvironmentDefinition
/*     */ {
/*     */   private final GameRuleMap gameRulesMap;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #121	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #121	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #121	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 121 */   public SetGameRules(GameRuleMap gameRulesMap) { this.gameRulesMap = gameRulesMap; } public GameRuleMap gameRulesMap() { return this.gameRulesMap; }
/* 122 */   public static final MapCodec<SetGameRules> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(GameRuleMap.CODEC
/* 123 */         .fieldOf("rules").forGetter(SetGameRules::gameRulesMap))
/* 124 */       .apply(i, SetGameRules::new));
/*     */ 
/*     */   
/*     */   public void setup(ServerLevel level) {
/* 128 */     GameRules gameRules = level.getGameRules();
/* 129 */     MinecraftServer server = level.getServer();
/* 130 */     gameRules.setAll(this.gameRulesMap, server);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public void teardown(ServerLevel level) { this.gameRulesMap.keySet().forEach(gameRule -> resetRule(level, gameRule)); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   private <T> void resetRule(ServerLevel level, GameRule<T> gameRule) { level.getGameRules().set(gameRule, gameRule.defaultValue(), level.getServer()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public MapCodec<SetGameRules> codec() { return CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$SetGameRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
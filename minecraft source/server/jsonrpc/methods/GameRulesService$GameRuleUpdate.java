/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleType;
/*    */ 
/*    */ public final class GameRuleUpdate<T> extends Record {
/*    */   private final GameRule<T> gameRule;
/*    */   private final T value;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate<TT;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate<TT;>; }
/*    */   
/* 17 */   public GameRuleUpdate(GameRule<T> gameRule, T value) { this.gameRule = gameRule; this.value = value; } public GameRule<T> gameRule() { return this.gameRule; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 17 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate<TT;>; } public T value() { return (T)this.value; }
/* 18 */   public static final Codec<GameRuleUpdate<?>> TYPED_CODEC = BuiltInRegistries.GAME_RULE.byNameCodec().dispatch("key", GameRuleUpdate::gameRule, GameRuleUpdate::getValueAndTypeCodec);
/* 19 */   public static final Codec<GameRuleUpdate<?>> CODEC = BuiltInRegistries.GAME_RULE.byNameCodec().dispatch("key", GameRuleUpdate::gameRule, GameRuleUpdate::getValueCodec);
/*    */   
/*    */   private static <T> MapCodec<? extends GameRuleUpdate<T>> getValueCodec(GameRule<T> gameRule) {
/* 22 */     return gameRule.valueCodec().fieldOf("value")
/* 23 */       .xmap(value -> new GameRuleUpdate(gameRule, value), GameRuleUpdate::value);
/*    */   }
/*    */   
/*    */   private static <T> MapCodec<? extends GameRuleUpdate<T>> getValueAndTypeCodec(GameRule<T> gameRule) {
/* 27 */     return RecordCodecBuilder.mapCodec(i -> i.group(
/* 28 */           StringRepresentable.fromEnum(GameRuleType::values).fieldOf("type").forGetter(()), gameRule
/* 29 */           .valueCodec().fieldOf("value").forGetter(GameRuleUpdate::value))
/* 30 */         .apply(i, ()));
/*    */   }
/*    */   
/*    */   private static <T> GameRuleUpdate<T> getUntypedRule(GameRule<T> gameRule, GameRuleType readType, T value) {
/* 34 */     if (gameRule.gameRuleType() != readType) {
/* 35 */       throw new InvalidParameterJsonRpcException("Stated type \"" + String.valueOf(readType) + "\" mismatches with actual type \"" + String.valueOf(gameRule.gameRuleType()) + "\" of gamerule \"" + gameRule.id() + "\"");
/*    */     }
/* 37 */     return new GameRuleUpdate(gameRule, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\GameRulesService$GameRuleUpdate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements GameRuleTypeVisitor
/*    */ {
/*    */   public <T> void visit(GameRule<T> gameRule) {
/* 24 */     LiteralArgumentBuilder<CommandSourceStack> unqualified = Commands.literal(gameRule.id());
/* 25 */     LiteralArgumentBuilder<CommandSourceStack> qualified = Commands.literal(gameRule.getIdentifier().toString());
/* 26 */     ((LiteralArgumentBuilder)base
/* 27 */       .then(GameRuleCommand.buildRuleArguments(gameRule, unqualified)))
/* 28 */       .then(GameRuleCommand.buildRuleArguments(gameRule, qualified));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\GameRuleCommand$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
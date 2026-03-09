/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.BanListEntry;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class BanListCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 18 */     dispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("banlist")
/* 20 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 21 */         .executes(s -> {
/* 22 */             PlayerList players = ((CommandSourceStack)s.getSource()).getServer().getPlayerList();
/* 23 */             return showList((CommandSourceStack)s.getSource(), Lists.newArrayList(Iterables.concat(players.getBans().getEntries(), players.getIpBans().getEntries())));
/*    */           
/* 25 */           })).then(
/* 26 */           Commands.literal("ips")
/* 27 */           .executes(s -> showList((CommandSourceStack)s.getSource(), ((CommandSourceStack)s.getSource()).getServer().getPlayerList().getIpBans().getEntries()))))
/*    */         
/* 29 */         .then(
/* 30 */           Commands.literal("players")
/* 31 */           .executes(s -> showList((CommandSourceStack)s.getSource(), ((CommandSourceStack)s.getSource()).getServer().getPlayerList().getBans().getEntries()))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int showList(CommandSourceStack source, Collection<? extends BanListEntry<?>> list) {
/* 37 */     if (list.isEmpty()) {
/* 38 */       source.sendSuccess(() -> Component.translatable("commands.banlist.none"), false);
/*    */     } else {
/* 40 */       source.sendSuccess(() -> Component.translatable("commands.banlist.list", new Object[] { Integer.valueOf(list.size()) }), false);
/* 41 */       for (BanListEntry<?> entry : list) {
/* 42 */         source.sendSuccess(() -> Component.translatable("commands.banlist.entry", new Object[] { entry.getDisplayName(), entry.getSource(), entry.getReasonMessage() }), false);
/*    */       } 
/*    */     } 
/* 45 */     return list.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\BanListCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.world.level.storage.WorldData;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadCommand {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static void reloadPacks(Collection<String> selectedPacks, CommandSourceStack source) {
/* 22 */     source.getServer().reloadResources(selectedPacks).exceptionally(throwable -> {
/* 23 */           LOGGER.warn("Failed to execute reload", throwable);
/* 24 */           source.sendFailure(Component.translatable("commands.reload.failure"));
/* 25 */           return null;
/*    */         });
/*    */   }
/*    */   
/*    */   private static Collection<String> discoverNewPacks(PackRepository packRepository, WorldData worldData, Collection<String> currentPacks) {
/* 30 */     packRepository.reload();
/* 31 */     Collection<String> selected = Lists.newArrayList(currentPacks);
/* 32 */     Collection<String> disabled = worldData.getDataConfiguration().dataPacks().getDisabled();
/*    */     
/* 34 */     for (String pack : packRepository.getAvailableIds()) {
/* 35 */       if (!disabled.contains(pack) && !selected.contains(pack)) {
/* 36 */         selected.add(pack);
/*    */       }
/*    */     } 
/* 39 */     return selected;
/*    */   }
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 43 */     dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reload")
/* 44 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 45 */         .executes(s -> {
/* 46 */             CommandSourceStack source = (CommandSourceStack)s.getSource();
/* 47 */             MinecraftServer server = source.getServer();
/* 48 */             PackRepository packRepository = server.getPackRepository();
/* 49 */             WorldData worldData = server.getWorldData();
/* 50 */             Collection<String> currentPacks = packRepository.getSelectedIds();
/* 51 */             Collection<String> newSelectedPacks = discoverNewPacks(packRepository, worldData, currentPacks);
/* 52 */             source.sendSuccess((), true);
/* 53 */             reloadPacks(newSelectedPacks, source);
/* 54 */             return 0;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ReloadCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
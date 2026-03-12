/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ 
/*    */ public class CommandsReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CommandsReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 20 */     this.output = output;
/* 21 */     this.registries = registries;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 26 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("commands.json");
/*    */     
/* 28 */     return this.registries.thenCompose(provider -> {
/* 29 */           CommandDispatcher<CommandSourceStack> dispatcher = (new Commands(Commands.CommandSelection.ALL, Commands.createValidationContext(provider))).getDispatcher();
/* 30 */           return DataProvider.saveStable(cache, ArgumentUtils.serializeNodeToJson(dispatcher, dispatcher.getRoot()), path);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public final String getName() { return "Command Syntax"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\CommandsReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
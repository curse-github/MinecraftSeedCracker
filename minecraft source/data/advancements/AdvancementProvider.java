/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class AdvancementProvider implements DataProvider {
/*    */   private final PackOutput.PathProvider pathProvider;
/*    */   private final List<AdvancementSubProvider> subProviders;
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public AdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, List<AdvancementSubProvider> subProviders) {
/* 26 */     this.pathProvider = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
/* 27 */     this.subProviders = subProviders;
/* 28 */     this.registries = registries;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 33 */     return this.registries.thenCompose(lookup -> {
/* 34 */           Set<Identifier> allAdvancements = new HashSet<Identifier>();
/* 35 */           List<CompletableFuture<?>> tasks = new ArrayList<CompletableFuture<?>>();
/* 36 */           Consumer<AdvancementHolder> consumer = ();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 45 */           for (AdvancementSubProvider subProvider : this.subProviders) {
/* 46 */             subProvider.generate(lookup, consumer);
/*    */           }
/*    */           
/* 49 */           return CompletableFuture.allOf((CompletableFuture[])tasks.toArray(()));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public final String getName() { return "Advancements"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\advancements\AdvancementProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.DefaultedRegistry;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class RegistryDumpReport
/*    */   implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/* 20 */   public RegistryDumpReport(PackOutput output) { this.output = output; }
/*    */ 
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 25 */     JsonObject root = new JsonObject();
/*    */     
/* 27 */     BuiltInRegistries.REGISTRY.listElements().forEach(e -> root.add(e.key().identifier().toString(), dumpRegistry((Registry)e.value())));
/*    */     
/* 29 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("registries.json");
/* 30 */     return DataProvider.saveStable(cache, root, path);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> JsonElement dumpRegistry(Registry<T> registry) {
/* 35 */     JsonObject result = new JsonObject();
/*    */     
/* 37 */     if (registry instanceof DefaultedRegistry) {
/* 38 */       Identifier defaultKey = ((DefaultedRegistry)registry).getDefaultKey();
/* 39 */       result.addProperty("default", defaultKey.toString());
/*    */     } 
/*    */     
/* 42 */     int registryId = BuiltInRegistries.REGISTRY.getId(registry);
/* 43 */     result.addProperty("protocol_id", Integer.valueOf(registryId));
/*    */     
/* 45 */     JsonObject entries = new JsonObject();
/* 46 */     registry.listElements().forEach(holder -> {
/* 47 */           T value = (T)holder.value();
/* 48 */           int protocolId = registry.getId(value);
/*    */           
/* 50 */           JsonObject entry = new JsonObject();
/* 51 */           entry.addProperty("protocol_id", Integer.valueOf(protocolId));
/*    */           
/* 53 */           entries.add(holder.key().identifier().toString(), entry);
/*    */         });
/* 55 */     result.add("entries", entries);
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public final String getName() { return "Registry Dump"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\RegistryDumpReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
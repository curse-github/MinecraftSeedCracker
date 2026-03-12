/*    */ package net.minecraft.data.info;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public class ItemListReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public ItemListReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 22 */     this.output = output;
/* 23 */     this.registries = registries;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 28 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("items.json");
/*    */     
/* 30 */     return this.registries.thenCompose(registries -> {
/* 31 */           JsonObject root = new JsonObject();
/*    */           
/* 33 */           RegistryOps<JsonElement> registryOps = registries.createSerializationContext(JsonOps.INSTANCE);
/* 34 */           registries.lookupOrThrow(Registries.ITEM).listElements().forEach(());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 41 */           return DataProvider.saveStable(cache, root, path);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public final String getName() { return "Item List"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\ItemListReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
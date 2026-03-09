/*    */ package net.minecraft.data.info;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.BlockTypes;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockListReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public BlockListReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 28 */     this.output = output;
/* 29 */     this.registries = registries;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 34 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("blocks.json");
/*    */     
/* 36 */     return this.registries.thenCompose(registries -> {
/* 37 */           JsonObject root = new JsonObject();
/* 38 */           RegistryOps<JsonElement> registryOps = registries.createSerializationContext(JsonOps.INSTANCE);
/*    */           
/* 40 */           registries.lookupOrThrow(Registries.BLOCK).listElements().forEach(());
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 83 */           return DataProvider.saveStable(cache, root, path);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public final String getName() { return "Block List"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\BlockListReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
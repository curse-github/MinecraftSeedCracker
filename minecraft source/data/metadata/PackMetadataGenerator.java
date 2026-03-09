/*    */ package net.minecraft.data.metadata;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.DetectedVersion;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.packs.FeatureFlagsMetadataSection;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public class PackMetadataGenerator implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public PackMetadataGenerator(PackOutput output) {
/* 25 */     this.elements = new HashMap();
/*    */ 
/*    */     
/* 28 */     this.output = output;
/*    */   }
/*    */   private final Map<String, Supplier<JsonElement>> elements;
/*    */   public <T> PackMetadataGenerator add(MetadataSectionType<T> type, T value) {
/* 32 */     this.elements.put(type.name(), () -> ((JsonElement)type.codec().encodeStart(JsonOps.INSTANCE, value).getOrThrow(IllegalArgumentException::new)).getAsJsonObject());
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 38 */     JsonObject result = new JsonObject();
/* 39 */     this.elements.forEach((id, data) -> result.add(id, (JsonElement)data.get()));
/* 40 */     return DataProvider.saveStable(cache, result, this.output.getOutputFolder().resolve("pack.mcmeta"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public final String getName() { return "Pack Metadata"; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public static PackMetadataGenerator forFeaturePack(PackOutput output, Component description) { return (new PackMetadataGenerator(output))
/* 50 */       .add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(description, DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA).minorRange())); }
/*    */ 
/*    */   
/*    */   public static PackMetadataGenerator forFeaturePack(PackOutput output, Component description, FeatureFlagSet flags) {
/* 54 */     return forFeaturePack(output, description)
/* 55 */       .add(FeatureFlagsMetadataSection.TYPE, new FeatureFlagsMetadataSection(flags));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\metadata\PackMetadataGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
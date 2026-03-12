/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class FileToIdConverter
/*    */ {
/*    */   private final String prefix;
/*    */   private final String extension;
/*    */   
/*    */   public FileToIdConverter(String prefix, String extension) {
/* 16 */     this.prefix = prefix;
/* 17 */     this.extension = extension;
/*    */   }
/*    */ 
/*    */   
/* 21 */   public static FileToIdConverter json(String prefix) { return new FileToIdConverter(prefix, ".json"); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static FileToIdConverter registry(ResourceKey<? extends Registry<?>> registry) { return json(Registries.elementsDirPath(registry)); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public Identifier idToFile(Identifier id) { return id.withPath(this.prefix + "/" + this.prefix + id.getPath()); }
/*    */ 
/*    */   
/*    */   public Identifier fileToId(Identifier file) {
/* 33 */     String path = file.getPath();
/* 34 */     return file.withPath(path.substring(this.prefix.length() + 1, path.length() - this.extension.length()));
/*    */   }
/*    */ 
/*    */   
/* 38 */   public Map<Identifier, Resource> listMatchingResources(ResourceManager manager) { return manager.listResources(this.prefix, id -> id.getPath().endsWith(this.extension)); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Map<Identifier, List<Resource>> listMatchingResourceStacks(ResourceManager manager) { return manager.listResourceStacks(this.prefix, id -> id.getPath().endsWith(this.extension)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\FileToIdConverter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
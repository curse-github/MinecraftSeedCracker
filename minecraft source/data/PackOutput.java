/*    */ package net.minecraft.data;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ public class PackOutput
/*    */ {
/*    */   private final Path outputFolder;
/*    */   
/* 14 */   public PackOutput(Path outputFolder) { this.outputFolder = outputFolder; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public Path getOutputFolder() { return this.outputFolder; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public Path getOutputFolder(Target target) { return getOutputFolder().resolve(target.directory); }
/*    */   
/*    */   public enum Target
/*    */   {
/* 26 */     DATA_PACK("data"),
/* 27 */     RESOURCE_PACK("assets"),
/* 28 */     REPORTS("reports");
/*    */ 
/*    */     
/*    */     private final String directory;
/*    */ 
/*    */     
/* 34 */     Target(String directory) { this.directory = directory; }
/*    */   }
/*    */   
/*    */   public static class PathProvider
/*    */   {
/*    */     private final Path root;
/*    */     private final String kind;
/*    */     
/*    */     private PathProvider(PackOutput output, PackOutput.Target target, String kind) {
/* 43 */       this.root = output.getOutputFolder(target);
/* 44 */       this.kind = kind;
/*    */     }
/*    */ 
/*    */     
/* 48 */     public Path file(Identifier element, String extension) { return this.root.resolve(element.getNamespace()).resolve(this.kind).resolve(element.getPath() + "." + element.getPath()); }
/*    */ 
/*    */     
/*    */     public Path json(Identifier element) {
/* 52 */       return this.root.resolve(element.getNamespace()).resolve(this.kind).resolve(element.getPath() + ".json");
/*    */     }
/*    */     
/*    */     public Path json(ResourceKey<?> element) {
/* 56 */       return this.root.resolve(element.identifier().getNamespace()).resolve(this.kind).resolve(element.identifier().getPath() + ".json");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 61 */   public PathProvider createPathProvider(Target target, String kind) { return new PathProvider(this, target, kind); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public PathProvider createRegistryElementsPathProvider(ResourceKey<? extends Registry<?>> registryKey) { return createPathProvider(Target.DATA_PACK, Registries.elementsDirPath(registryKey)); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public PathProvider createRegistryTagsPathProvider(ResourceKey<? extends Registry<?>> registryKey) { return createPathProvider(Target.DATA_PACK, Registries.tagsDirPath(registryKey)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\PackOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
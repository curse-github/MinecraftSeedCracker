/*    */ package net.minecraft.data;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ public class PathProvider
/*    */ {
/*    */   private final Path root;
/*    */   private final String kind;
/*    */   
/*    */   private PathProvider(PackOutput output, PackOutput.Target target, String kind) {
/* 43 */     this.root = output.getOutputFolder(target);
/* 44 */     this.kind = kind;
/*    */   }
/*    */ 
/*    */   
/* 48 */   public Path file(Identifier element, String extension) { return this.root.resolve(element.getNamespace()).resolve(this.kind).resolve(element.getPath() + "." + element.getPath()); }
/*    */ 
/*    */   
/*    */   public Path json(Identifier element) {
/* 52 */     return this.root.resolve(element.getNamespace()).resolve(this.kind).resolve(element.getPath() + ".json");
/*    */   }
/*    */   
/*    */   public Path json(ResourceKey<?> element) {
/* 56 */     return this.root.resolve(element.identifier().getNamespace()).resolve(this.kind).resolve(element.identifier().getPath() + ".json");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\PackOutput$PathProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
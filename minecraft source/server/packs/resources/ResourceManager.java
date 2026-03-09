/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackResources;
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
/*    */ public interface ResourceManager
/*    */   extends ResourceProvider
/*    */ {
/*    */   Set<String> getNamespaces();
/*    */   
/*    */   List<Resource> getResourceStack(Identifier paramIdentifier);
/*    */   
/*    */   Map<Identifier, Resource> listResources(String paramString, Predicate<Identifier> paramPredicate);
/*    */   
/*    */   Map<Identifier, List<Resource>> listResourceStacks(String paramString, Predicate<Identifier> paramPredicate);
/*    */   
/*    */   Stream<PackResources> listPacks();
/*    */   
/*    */   public enum Empty
/*    */     implements ResourceManager
/*    */   {
/* 39 */     INSTANCE;
/*    */ 
/*    */ 
/*    */     
/* 43 */     public Set<String> getNamespaces() { return Set.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     public Optional<Resource> getResource(Identifier location) { return Optional.empty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     public List<Resource> getResourceStack(Identifier location) { return List.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filter) { return Map.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) { return Map.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 68 */     public Stream<PackResources> listPacks() { return Stream.of(new PackResources[0]); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
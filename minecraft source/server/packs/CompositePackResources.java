/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ 
/*    */ public class CompositePackResources
/*    */   implements PackResources
/*    */ {
/*    */   private final PackResources primaryPackResources;
/*    */   private final List<PackResources> packResourcesStack;
/*    */   
/*    */   public CompositePackResources(PackResources primaryPackResources, List<PackResources> overlayPackResources) {
/* 24 */     this.primaryPackResources = primaryPackResources;
/*    */     
/* 26 */     List<PackResources> stack = new ArrayList<PackResources>(overlayPackResources.size() + 1);
/* 27 */     stack.addAll(Lists.reverse(overlayPackResources));
/* 28 */     stack.add(primaryPackResources);
/* 29 */     this.packResourcesStack = List.copyOf(stack);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public IoSupplier<InputStream> getRootResource(String... path) { return this.primaryPackResources.getRootResource(path); }
/*    */ 
/*    */ 
/*    */   
/*    */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
/* 39 */     for (PackResources packResources : this.packResourcesStack) {
/* 40 */       IoSupplier<InputStream> resource = packResources.getResource(type, location);
/* 41 */       if (resource != null) {
/* 42 */         return resource;
/*    */       }
/*    */     } 
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/* 51 */     Map<Identifier, IoSupplier<InputStream>> result = new HashMap<Identifier, IoSupplier<InputStream>>();
/* 52 */     for (PackResources packResources : this.packResourcesStack) {
/* 53 */       Objects.requireNonNull(result); packResources.listResources(type, namespace, directory, result::putIfAbsent);
/*    */     } 
/* 55 */     result.forEach(output);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getNamespaces(PackType type) {
/* 60 */     Set<String> result = new HashSet<String>();
/* 61 */     for (PackResources overlayPackResource : this.packResourcesStack) {
/* 62 */       result.addAll(overlayPackResource.getNamespaces(type));
/*    */     }
/* 64 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) throws IOException { return (T)this.primaryPackResources.getMetadataSection(metadataSerializer); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public PackLocationInfo location() { return this.primaryPackResources.location(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public void close() { this.packResourcesStack.forEach(PackResources::close); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\CompositePackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
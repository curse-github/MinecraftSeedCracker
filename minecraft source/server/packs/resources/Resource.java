/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Resource
/*    */ {
/*    */   private final PackResources source;
/*    */   private final IoSupplier<InputStream> streamSupplier;
/*    */   private final IoSupplier<ResourceMetadata> metadataSupplier;
/*    */   private ResourceMetadata cachedMetadata;
/*    */   
/*    */   public Resource(PackResources source, IoSupplier<InputStream> streamSupplier, IoSupplier<ResourceMetadata> metadataSupplier) {
/* 22 */     this.source = source;
/* 23 */     this.streamSupplier = streamSupplier;
/* 24 */     this.metadataSupplier = metadataSupplier;
/*    */   }
/*    */   
/*    */   public Resource(PackResources source, IoSupplier<InputStream> streamSupplier) {
/* 28 */     this.source = source;
/* 29 */     this.streamSupplier = streamSupplier;
/* 30 */     this.metadataSupplier = ResourceMetadata.EMPTY_SUPPLIER;
/* 31 */     this.cachedMetadata = ResourceMetadata.EMPTY;
/*    */   }
/*    */ 
/*    */   
/* 35 */   public PackResources source() { return this.source; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public String sourcePackId() { return this.source.packId(); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Optional<KnownPack> knownPackInfo() { return this.source.knownPackInfo(); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public InputStream open() throws IOException { return (InputStream)this.streamSupplier.get(); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public BufferedReader openAsReader() throws IOException { return new BufferedReader(new InputStreamReader(open(), StandardCharsets.UTF_8)); }
/*    */ 
/*    */   
/*    */   public ResourceMetadata metadata() throws IOException {
/* 55 */     if (this.cachedMetadata == null) {
/* 56 */       this.cachedMetadata = (ResourceMetadata)this.metadataSupplier.get();
/*    */     }
/* 58 */     return this.cachedMetadata;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\Resource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ 
/*    */ 
/*    */ public interface PackResources
/*    */   extends AutoCloseable
/*    */ {
/*    */   public static final String METADATA_EXTENSION = ".mcmeta";
/*    */   public static final String PACK_META = "pack.mcmeta";
/*    */   
/*    */   IoSupplier<InputStream> getRootResource(String... paramVarArgs);
/*    */   
/*    */   IoSupplier<InputStream> getResource(PackType paramPackType, Identifier paramIdentifier);
/*    */   
/*    */   void listResources(PackType paramPackType, String paramString1, String paramString2, ResourceOutput paramResourceOutput);
/*    */   
/*    */   Set<String> getNamespaces(PackType paramPackType);
/*    */   
/*    */   <T> T getMetadataSection(MetadataSectionType<T> paramMetadataSectionType) throws IOException;
/*    */   
/*    */   PackLocationInfo location();
/*    */   
/* 32 */   default String packId() { return location().id(); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   default Optional<KnownPack> knownPackInfo() { return location().knownPackInfo(); }
/*    */   
/*    */   void close();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ResourceOutput extends BiConsumer<Identifier, IoSupplier<InputStream>> {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
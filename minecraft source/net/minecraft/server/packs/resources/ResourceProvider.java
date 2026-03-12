/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ResourceProvider
/*    */ {
/* 14 */   public static final ResourceProvider EMPTY = location -> Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   default Resource getResourceOrThrow(Identifier location) throws FileNotFoundException { return (Resource)getResource(location).orElseThrow(() -> new FileNotFoundException(location.toString())); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   default InputStream open(Identifier location) throws IOException { return getResourceOrThrow(location).open(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   default BufferedReader openAsReader(Identifier location) throws IOException { return getResourceOrThrow(location).openAsReader(); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   static ResourceProvider fromMap(Map<Identifier, Resource> map) { return location -> Optional.ofNullable((Resource)map.get(location)); }
/*    */   
/*    */   Optional<Resource> getResource(Identifier paramIdentifier);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ResourceProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
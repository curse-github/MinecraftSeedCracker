/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class AbstractPackResources
/*    */   implements PackResources {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final PackLocationInfo location;
/*    */   
/* 23 */   protected AbstractPackResources(PackLocationInfo location) { this.location = location; }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) throws IOException {
/* 28 */     IoSupplier<InputStream> metadata = getRootResource(new String[] { "pack.mcmeta" });
/* 29 */     if (metadata == null) {
/* 30 */       return null;
/*    */     }
/* 32 */     InputStream resource = (InputStream)metadata.get(); 
/* 33 */     try { Object object = getMetadataFromStream(metadataSerializer, resource, this.location);
/* 34 */       if (resource != null) resource.close();  return (T)object; }
/*    */     catch (Throwable throwable) { if (resource != null)
/*    */         try { resource.close(); }
/*    */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 39 */      } public static <T> T getMetadataFromStream(MetadataSectionType<T> serializer, InputStream stream, PackLocationInfo location) { JsonObject metadata; try { BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)); 
/* 40 */       try { metadata = GsonHelper.parse(reader);
/* 41 */         reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 42 */     { LOGGER.error("Couldn't load {} {} metadata: {}", new Object[] { location.id(), serializer.name(), e.getMessage() });
/* 43 */       return null; }
/*    */ 
/*    */     
/* 46 */     if (!metadata.has(serializer.name())) {
/* 47 */       return null;
/*    */     }
/*    */     
/* 50 */     return (T)serializer.codec().parse(JsonOps.INSTANCE, metadata.get(serializer.name()))
/* 51 */       .ifError(error -> LOGGER.error("Couldn't load {} {} metadata: {}", new Object[] { location.id(), serializer.name(), error.message()
/* 52 */           })).result().orElse(null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public PackLocationInfo location() { return this.location; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\AbstractPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
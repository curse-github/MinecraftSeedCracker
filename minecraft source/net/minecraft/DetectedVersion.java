/*    */ package net.minecraft;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.Date;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.DataVersion;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class DetectedVersion
/*    */ {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 22 */   public static final WorldVersion BUILT_IN = createBuiltIn(
/* 23 */       UUID.randomUUID().toString().replaceAll("-", ""), "Development Version");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static WorldVersion createBuiltIn(String id, String name) { return createBuiltIn(id, name, true); }
/*    */ 
/*    */   
/*    */   public static WorldVersion createBuiltIn(String id, String name, boolean stable) {
/* 32 */     return new WorldVersion.Simple(id, name, new DataVersion(4671, "main"), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 39 */         SharedConstants.getProtocolVersion(), 
/* 40 */         PackFormat.of(75, 0), 
/*    */ 
/*    */ 
/*    */         
/* 44 */         PackFormat.of(94, 1), new Date(), stable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static WorldVersion createFromJson(JsonObject root) {
/* 54 */     JsonObject packVersion = GsonHelper.getAsJsonObject(root, "pack_version");
/*    */     
/* 56 */     return new WorldVersion.Simple(
/* 57 */         GsonHelper.getAsString(root, "id"), 
/* 58 */         GsonHelper.getAsString(root, "name"), new DataVersion(
/*    */           
/* 60 */           GsonHelper.getAsInt(root, "world_version"), 
/* 61 */           GsonHelper.getAsString(root, "series_id", "main")), 
/*    */         
/* 63 */         GsonHelper.getAsInt(root, "protocol_version"), 
/* 64 */         PackFormat.of(
/* 65 */           GsonHelper.getAsInt(packVersion, "resource_major"), 
/* 66 */           GsonHelper.getAsInt(packVersion, "resource_minor")), 
/*    */         
/* 68 */         PackFormat.of(
/* 69 */           GsonHelper.getAsInt(packVersion, "data_major"), 
/* 70 */           GsonHelper.getAsInt(packVersion, "data_minor")), 
/*    */         
/* 72 */         Date.from(ZonedDateTime.parse(GsonHelper.getAsString(root, "build_time")).toInstant()), 
/* 73 */         GsonHelper.getAsBoolean(root, "stable"));
/*    */   }
/*    */   
/*    */   public static WorldVersion tryDetectVersion() {
/*    */     
/* 78 */     try { stream = DetectedVersion.class.getResourceAsStream("/version.json"); 
/* 79 */       try { if (stream == null)
/* 80 */         { LOGGER.warn("Missing version information!");
/* 81 */           WorldVersion worldVersion = BUILT_IN;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 87 */           if (stream != null) stream.close();  return worldVersion; }  InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8); try { WorldVersion worldVersion = createFromJson(GsonHelper.parse(reader)); reader.close(); if (stream != null) stream.close();  return worldVersion; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonParseException e)
/* 88 */     { throw new IllegalStateException("Game version information is corrupt", e); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\DetectedVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
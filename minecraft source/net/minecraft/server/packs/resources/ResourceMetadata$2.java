/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
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
/*    */ class null
/*    */   implements ResourceMetadata
/*    */ {
/*    */   public <T> Optional<T> getSection(MetadataSectionType<T> serializer) {
/* 35 */     String name = serializer.name();
/* 36 */     if (metadata.has(name)) {
/* 37 */       T section = (T)serializer.codec().parse(JsonOps.INSTANCE, metadata.get(name)).getOrThrow(com.google.gson.JsonParseException::new);
/* 38 */       return Optional.of(section);
/*    */     } 
/* 40 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ResourceMetadata$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
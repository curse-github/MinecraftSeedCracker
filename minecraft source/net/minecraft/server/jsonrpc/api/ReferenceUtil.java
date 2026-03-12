/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ReferenceUtil {
/* 10 */   public static final Codec<URI> REFERENCE_CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */         
/*    */         try {
/* 13 */           return DataResult.success(new URI(string));
/* 14 */         } catch (URISyntaxException e) {
/* 15 */           Objects.requireNonNull(e); return DataResult.error(e::getMessage);
/*    */         } 
/*    */       }URI::toString);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static URI createLocalReference(String typeId) { return URI.create("#/components/schemas/" + typeId); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\api\ReferenceUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
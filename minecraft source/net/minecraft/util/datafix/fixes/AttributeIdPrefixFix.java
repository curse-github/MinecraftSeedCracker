/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class AttributeIdPrefixFix
/*    */   extends AttributesRenameFix {
/*  9 */   private static final List<String> PREFIXES = List.of("generic.", "horse.", "player.", "zombie.");
/*    */ 
/*    */   
/* 12 */   public AttributeIdPrefixFix(Schema outputSchema) { super(outputSchema, "AttributeIdPrefixFix", AttributeIdPrefixFix::replaceId); }
/*    */ 
/*    */   
/*    */   private static String replaceId(String id) {
/* 16 */     String namespacedId = NamespacedSchema.ensureNamespaced(id);
/* 17 */     for (String prefix : PREFIXES) {
/* 18 */       String namespacedPrefix = NamespacedSchema.ensureNamespaced(prefix);
/* 19 */       if (namespacedId.startsWith(namespacedPrefix)) {
/* 20 */         return "minecraft:" + namespacedId.substring(namespacedPrefix.length());
/*    */       }
/*    */     } 
/* 23 */     return id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AttributeIdPrefixFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
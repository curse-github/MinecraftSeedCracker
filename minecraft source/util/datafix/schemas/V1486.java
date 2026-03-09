/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1486
/*    */   extends NamespacedSchema
/*    */ {
/* 11 */   public V1486(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 18 */     map.put("minecraft:cod", (Supplier)map.remove("minecraft:cod_mob"));
/* 19 */     map.put("minecraft:salmon", (Supplier)map.remove("minecraft:salmon_mob"));
/*    */     
/* 21 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1486.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
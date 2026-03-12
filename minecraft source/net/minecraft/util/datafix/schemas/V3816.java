/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ public class V3816
/*    */   extends NamespacedSchema
/*    */ {
/* 12 */   public V3816(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 17 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 18 */     schema.registerSimple(map, "minecraft:bogged");
/* 19 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3816.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
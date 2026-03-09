/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1928
/*    */   extends NamespacedSchema
/*    */ {
/* 11 */   public V1928(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) { schema.registerSimple(map, name); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 22 */     map.remove("minecraft:illager_beast");
/* 23 */     registerMob(schema, map, "minecraft:ravager");
/*    */     
/* 25 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1928.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
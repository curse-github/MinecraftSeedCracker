/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V2100
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V2100(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) { schema.registerSimple(map, name); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 25 */     registerMob(schema, map, "minecraft:bee");
/* 26 */     registerMob(schema, map, "minecraft:bee_stinger");
/* 27 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 32 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*    */     
/* 34 */     schema.register(map, "minecraft:beehive", () -> DSL.optionalFields("Bees", 
/* 35 */           DSL.list(
/* 36 */             DSL.optionalFields("EntityData", References.ENTITY_TREE
/* 37 */               .in(schema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2100.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
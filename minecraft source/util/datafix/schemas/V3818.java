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
/*    */ public class V3818
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V3818(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     schema.register(map, "minecraft:beehive", () -> DSL.optionalFields("bees", 
/* 22 */           DSL.list(
/* 23 */             DSL.optionalFields("entity_data", References.ENTITY_TREE
/* 24 */               .in(schema)))));
/*    */ 
/*    */ 
/*    */     
/* 28 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3818.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
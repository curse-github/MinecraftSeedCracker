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
/*    */ public class V1906
/*    */   extends NamespacedSchema
/*    */ {
/* 14 */   public V1906(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*    */     
/* 21 */     registerInventory(schema, map, "minecraft:barrel");
/* 22 */     registerInventory(schema, map, "minecraft:smoker");
/* 23 */     registerInventory(schema, map, "minecraft:blast_furnace");
/*    */     
/* 25 */     schema.register(map, "minecraft:lectern", name -> DSL.optionalFields("Book", References.ITEM_STACK
/* 26 */           .in(schema)));
/*    */ 
/*    */     
/* 29 */     schema.registerSimple(map, "minecraft:bell");
/*    */     
/* 31 */     return map;
/*    */   }
/*    */ 
/*    */   
/* 35 */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) { schema.register(map, name, () -> V1458.nameableInventory(schema)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1906.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
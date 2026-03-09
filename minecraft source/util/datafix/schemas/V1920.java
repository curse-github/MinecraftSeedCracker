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
/*    */ public class V1920
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V1920(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) { schema.register(map, name, () -> DSL.optionalFields("Items", 
/* 20 */           DSL.list(References.ITEM_STACK.in(schema)))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 26 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*    */     
/* 28 */     registerInventory(schema, map, "minecraft:campfire");
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1920.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
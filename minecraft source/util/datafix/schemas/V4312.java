/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V4312
/*    */   extends NamespacedSchema
/*    */ {
/* 22 */   public V4312(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 27 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 28 */     schema.registerType(false, References.PLAYER, () -> DSL.and(References.ENTITY_EQUIPMENT
/* 29 */           .in(schema), 
/* 30 */           DSL.optionalFields(new Pair[] {
/*    */               
/* 32 */               Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE
/* 33 */                   .in(schema))), 
/*    */               
/* 35 */               Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(schema))), 
/*    */               
/* 37 */               Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(schema))), 
/* 38 */               Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(schema))), 
/* 39 */               Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(schema)), 
/* 40 */               Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(schema)), 
/* 41 */               Pair.of("recipeBook", DSL.optionalFields("recipes", 
/* 42 */                   DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", 
/* 43 */                   DSL.list(References.RECIPE.in(schema))))
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4312.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
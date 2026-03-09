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
/*    */ 
/*    */ 
/*    */ public class V1022
/*    */   extends Schema
/*    */ {
/* 24 */   public V1022(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 29 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 31 */     schema.registerType(false, References.RECIPE, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 32 */     schema.registerType(false, References.PLAYER, () -> DSL.optionalFields(new Pair[] {
/* 33 */             Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE
/* 34 */                 .in(schema))), 
/*    */             
/* 36 */             Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(schema))), 
/* 37 */             Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(schema))), 
/* 38 */             Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(schema))), 
/* 39 */             Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(schema)), 
/* 40 */             Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(schema)), 
/* 41 */             Pair.of("recipeBook", DSL.optionalFields("recipes", 
/* 42 */                 DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", 
/* 43 */                 DSL.list(References.RECIPE.in(schema))))
/*    */           }));
/*    */ 
/*    */     
/* 47 */     schema.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1022.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
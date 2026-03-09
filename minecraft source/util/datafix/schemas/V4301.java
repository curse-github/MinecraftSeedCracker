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
/*    */ public class V4301
/*    */   extends NamespacedSchema
/*    */ {
/* 18 */   public V4301(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 23 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 24 */     schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.optional(DSL.field("equipment", 
/*    */             
/* 26 */             DSL.optionalFields(new Pair[] {
/* 27 */                 Pair.of("mainhand", References.ITEM_STACK.in(schema)), 
/* 28 */                 Pair.of("offhand", References.ITEM_STACK.in(schema)), 
/* 29 */                 Pair.of("feet", References.ITEM_STACK.in(schema)), 
/* 30 */                 Pair.of("legs", References.ITEM_STACK.in(schema)), 
/* 31 */                 Pair.of("chest", References.ITEM_STACK.in(schema)), 
/* 32 */                 Pair.of("head", References.ITEM_STACK.in(schema)), 
/* 33 */                 Pair.of("body", References.ITEM_STACK.in(schema)), 
/* 34 */                 Pair.of("saddle", References.ITEM_STACK.in(schema))
/*    */               }))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4301.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V100
/*    */   extends Schema
/*    */ {
/* 18 */   public V100(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 23 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 25 */     schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.and(
/* 26 */           DSL.optional(DSL.field("ArmorItems", DSL.list(References.ITEM_STACK.in(schema)))), new TypeTemplate[] {
/* 27 */             DSL.optional(DSL.field("HandItems", DSL.list(References.ITEM_STACK.in(schema)))), 
/*    */             
/* 29 */             DSL.optional(DSL.field("body_armor_item", References.ITEM_STACK.in(schema))), 
/*    */             
/* 31 */             DSL.optional(DSL.field("saddle", References.ITEM_STACK.in(schema)))
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V100.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
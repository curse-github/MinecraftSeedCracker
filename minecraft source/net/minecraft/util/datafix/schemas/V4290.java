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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V4290
/*    */   extends NamespacedSchema
/*    */ {
/* 22 */   public V4290(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 27 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 28 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.or(
/* 29 */           DSL.or(
/* 30 */             DSL.constType(DSL.string()), 
/* 31 */             DSL.list(References.TEXT_COMPONENT.in(schema))), 
/*    */           
/* 33 */           DSL.optionalFields("extra", 
/* 34 */             DSL.list(References.TEXT_COMPONENT.in(schema)), "separator", References.TEXT_COMPONENT
/* 35 */             .in(schema), "hoverEvent", 
/*    */             
/* 37 */             DSL.taggedChoice("action", DSL.string(), Map.of("show_text", 
/* 38 */                 DSL.optionalFields("contents", References.TEXT_COMPONENT
/* 39 */                   .in(schema)), "show_item", 
/*    */                 
/* 41 */                 DSL.optionalFields("contents", 
/* 42 */                   DSL.or(References.ITEM_STACK
/* 43 */                     .in(schema), References.ITEM_NAME
/* 44 */                     .in(schema))), "show_entity", 
/*    */ 
/*    */                 
/* 47 */                 DSL.optionalFields("type", References.ENTITY_NAME
/* 48 */                   .in(schema), "name", References.TEXT_COMPONENT
/* 49 */                   .in(schema)))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4290.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
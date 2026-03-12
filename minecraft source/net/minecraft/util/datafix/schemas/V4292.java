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
/*    */ public class V4292
/*    */   extends NamespacedSchema
/*    */ {
/* 21 */   public V4292(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 26 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 27 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.or(
/* 28 */           DSL.or(
/* 29 */             DSL.constType(DSL.string()), 
/* 30 */             DSL.list(References.TEXT_COMPONENT.in(schema))), 
/*    */           
/* 32 */           DSL.optionalFields("extra", 
/* 33 */             DSL.list(References.TEXT_COMPONENT.in(schema)), "separator", References.TEXT_COMPONENT
/* 34 */             .in(schema), "hover_event", 
/* 35 */             DSL.taggedChoice("action", DSL.string(), Map.of("show_text", 
/* 36 */                 DSL.optionalFields("value", References.TEXT_COMPONENT
/* 37 */                   .in(schema)), "show_item", References.ITEM_STACK
/*    */                 
/* 39 */                 .in(schema), "show_entity", 
/* 40 */                 DSL.optionalFields("id", References.ENTITY_NAME
/* 41 */                   .in(schema), "name", References.TEXT_COMPONENT
/* 42 */                   .in(schema)))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4292.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.SequencedMap;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V4059
/*    */   extends NamespacedSchema
/*    */ {
/* 20 */   public V4059(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema schema) {
/* 24 */     SequencedMap<String, Supplier<TypeTemplate>> components = V3818_3.components(schema);
/* 25 */     components.remove("minecraft:food");
/* 26 */     components.put("minecraft:use_remainder", () -> References.ITEM_STACK.in(schema));
/*    */     
/* 28 */     components.put("minecraft:equippable", () -> DSL.optionalFields("allowed_entities", 
/* 29 */           DSL.or(References.ENTITY_NAME.in(schema), DSL.list(References.ENTITY_NAME.in(schema)))));
/*    */     
/* 31 */     return components;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 36 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 37 */     schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(schema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4059.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
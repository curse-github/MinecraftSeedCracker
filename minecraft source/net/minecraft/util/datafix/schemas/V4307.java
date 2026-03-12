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
/*    */ public class V4307
/*    */   extends NamespacedSchema
/*    */ {
/* 19 */   public V4307(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema schema) {
/* 23 */     SequencedMap<String, Supplier<TypeTemplate>> components = V4059.components(schema);
/* 24 */     components.put("minecraft:can_place_on", () -> adventureModePredicate(schema));
/* 25 */     components.put("minecraft:can_break", () -> adventureModePredicate(schema));
/* 26 */     return components;
/*    */   }
/*    */   
/*    */   private static TypeTemplate adventureModePredicate(Schema schema) {
/* 30 */     TypeTemplate predicate = DSL.optionalFields("blocks", 
/* 31 */         DSL.or(References.BLOCK_NAME.in(schema), DSL.list(References.BLOCK_NAME.in(schema))));
/*    */     
/* 33 */     return DSL.or(predicate, DSL.list(predicate));
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 38 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 39 */     schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(schema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4307.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
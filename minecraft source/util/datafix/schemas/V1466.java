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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V1466
/*    */   extends NamespacedSchema
/*    */ {
/* 25 */   public V1466(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 30 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 32 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", 
/* 33 */           DSL.optionalFields("Entities", 
/* 34 */             DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", 
/* 35 */             DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", 
/* 36 */             DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", 
/* 37 */             DSL.list(DSL.optionalFields("Palette", 
/* 38 */                 DSL.list(References.BLOCK_STATE.in(schema)))), "Structures", 
/*    */             
/* 40 */             DSL.optionalFields("Starts", 
/* 41 */               DSL.compoundList(References.STRUCTURE_FEATURE.in(schema))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 49 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*    */     
/* 51 */     map.put("DUMMY", DSL::remainder);
/*    */     
/* 53 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1466.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
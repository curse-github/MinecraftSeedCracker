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
/*    */ public class V2842
/*    */   extends NamespacedSchema
/*    */ {
/* 25 */   public V2842(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 30 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 32 */     schema.registerType(false, References.CHUNK, () -> DSL.optionalFields("entities", 
/* 33 */           DSL.list(References.ENTITY_TREE.in(schema)), "block_entities", 
/* 34 */           DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "block_ticks", 
/* 35 */           DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "sections", 
/* 36 */           DSL.list(DSL.optionalFields("biomes", 
/* 37 */               DSL.optionalFields("palette", 
/* 38 */                 DSL.list(References.BIOME.in(schema))), "block_states", 
/*    */               
/* 40 */               DSL.optionalFields("palette", 
/* 41 */                 DSL.list(References.BLOCK_STATE.in(schema))))), "structures", 
/*    */ 
/*    */           
/* 44 */           DSL.optionalFields("starts", 
/* 45 */             DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2842.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
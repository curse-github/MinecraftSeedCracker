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
/*    */ public class V1451_1
/*    */   extends NamespacedSchema
/*    */ {
/* 22 */   public V1451_1(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 27 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 29 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", 
/* 30 */           DSL.optionalFields("Entities", 
/* 31 */             DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", 
/* 32 */             DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", 
/* 33 */             DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", 
/* 34 */             DSL.list(DSL.optionalFields("Palette", 
/* 35 */                 DSL.list(References.BLOCK_STATE.in(schema)))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
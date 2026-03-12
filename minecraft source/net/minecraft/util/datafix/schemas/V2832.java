/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V2832
/*    */   extends NamespacedSchema
/*    */ {
/* 32 */   public V2832(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) { super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 39 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", 
/* 40 */           DSL.optionalFields("Entities", 
/* 41 */             DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", 
/* 42 */             DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", 
/* 43 */             DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", 
/* 44 */             DSL.list(DSL.optionalFields("biomes", 
/* 45 */                 DSL.optionalFields("palette", 
/* 46 */                   DSL.list(References.BIOME.in(schema))), "block_states", 
/*    */                 
/* 48 */                 DSL.optionalFields("palette", 
/* 49 */                   DSL.list(References.BLOCK_STATE.in(schema))))), "Structures", 
/*    */ 
/*    */             
/* 52 */             DSL.optionalFields("Starts", 
/* 53 */               DSL.compoundList(References.STRUCTURE_FEATURE.in(schema))))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     schema.registerType(false, References.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, () -> DSL.constType(namespacedString()));
/*    */     
/* 60 */     schema.registerType(false, References.WORLD_GEN_SETTINGS, () -> DSL.fields("dimensions", 
/* 61 */           DSL.compoundList(DSL.constType(namespacedString()), DSL.fields("generator", 
/* 62 */               DSL.taggedChoiceLazy("type", DSL.string(), ImmutableMap.of("minecraft:debug", DSL::remainder, "minecraft:flat", (), "minecraft:noise", ())))))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2832.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
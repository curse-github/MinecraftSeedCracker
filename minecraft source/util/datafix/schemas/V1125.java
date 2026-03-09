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
/*    */ public class V1125
/*    */   extends NamespacedSchema
/*    */ {
/* 19 */   public V1125(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*    */     
/* 26 */     schema.registerSimple(map, "minecraft:bed");
/*    */     
/* 28 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 33 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 34 */     schema.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", 
/* 35 */           DSL.optionalFields("criteria", 
/* 36 */             DSL.compoundList(References.BIOME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", 
/*    */           
/* 38 */           DSL.optionalFields("criteria", 
/* 39 */             DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", 
/*    */           
/* 41 */           DSL.optionalFields("criteria", 
/* 42 */             DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", 
/*    */           
/* 44 */           DSL.optionalFields("criteria", 
/* 45 */             DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string())))));
/*    */ 
/*    */     
/* 48 */     schema.registerType(false, References.BIOME, () -> DSL.constType(namespacedString()));
/* 49 */     schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(namespacedString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1125.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
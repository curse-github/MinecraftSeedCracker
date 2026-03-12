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
/*    */ public class V2831
/*    */   extends NamespacedSchema
/*    */ {
/* 17 */   public V2831(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 22 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 24 */     schema.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", 
/* 25 */           DSL.list(DSL.fields("data", 
/* 26 */               DSL.fields("entity", References.ENTITY_TREE
/* 27 */                 .in(schema)))), "SpawnData", 
/*    */ 
/*    */           
/* 30 */           DSL.fields("entity", References.ENTITY_TREE
/* 31 */             .in(schema))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2831.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
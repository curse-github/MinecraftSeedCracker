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
/*    */ public class V2684
/*    */   extends NamespacedSchema
/*    */ {
/* 16 */   public V2684(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 21 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 22 */     schema.registerType(false, References.GAME_EVENT_NAME, () -> DSL.constType(namespacedString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 27 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 28 */     schema.register(map, "minecraft:sculk_sensor", () -> DSL.optionalFields("listener", 
/* 29 */           DSL.optionalFields("event", 
/* 30 */             DSL.optionalFields("game_event", References.GAME_EVENT_NAME
/* 31 */               .in(schema)))));
/*    */ 
/*    */ 
/*    */     
/* 35 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2684.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
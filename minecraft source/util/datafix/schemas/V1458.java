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
/*    */ public class V1458
/*    */   extends NamespacedSchema
/*    */ {
/* 20 */   public V1458(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 25 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 27 */     schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT
/* 28 */           .in(schema), 
/* 29 */           DSL.optionalFields("CustomName", References.TEXT_COMPONENT
/*    */ 
/*    */             
/* 32 */             .in(schema), 
/* 33 */             DSL.taggedChoiceLazy("id", namespacedString(), entityTypes))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 40 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 41 */     schema.register(map, "minecraft:beacon", () -> nameable(schema));
/* 42 */     schema.register(map, "minecraft:banner", () -> nameable(schema));
/* 43 */     schema.register(map, "minecraft:brewing_stand", () -> nameableInventory(schema));
/* 44 */     schema.register(map, "minecraft:chest", () -> nameableInventory(schema));
/* 45 */     schema.register(map, "minecraft:trapped_chest", () -> nameableInventory(schema));
/* 46 */     schema.register(map, "minecraft:dispenser", () -> nameableInventory(schema));
/* 47 */     schema.register(map, "minecraft:dropper", () -> nameableInventory(schema));
/* 48 */     schema.register(map, "minecraft:enchanting_table", () -> nameable(schema));
/* 49 */     schema.register(map, "minecraft:furnace", () -> nameableInventory(schema));
/* 50 */     schema.register(map, "minecraft:hopper", () -> nameableInventory(schema));
/* 51 */     schema.register(map, "minecraft:shulker_box", () -> nameableInventory(schema));
/* 52 */     return map;
/*    */   }
/*    */   
/*    */   public static TypeTemplate nameableInventory(Schema schema) {
/* 56 */     return DSL.optionalFields("Items", 
/* 57 */         DSL.list(References.ITEM_STACK.in(schema)), "CustomName", References.TEXT_COMPONENT
/* 58 */         .in(schema));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public static TypeTemplate nameable(Schema schema) { return DSL.optionalFields("CustomName", References.TEXT_COMPONENT
/* 64 */         .in(schema)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1458.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
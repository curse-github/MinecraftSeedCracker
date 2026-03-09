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
/*    */ public class V1451_3
/*    */   extends NamespacedSchema
/*    */ {
/* 19 */   public V1451_3(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */ 
/*    */     
/* 27 */     schema.registerSimple(map, "minecraft:egg");
/* 28 */     schema.registerSimple(map, "minecraft:ender_pearl");
/* 29 */     schema.registerSimple(map, "minecraft:fireball");
/* 30 */     schema.register(map, "minecraft:potion", name -> DSL.optionalFields("Potion", References.ITEM_STACK
/* 31 */           .in(schema)));
/*    */     
/* 33 */     schema.registerSimple(map, "minecraft:small_fireball");
/* 34 */     schema.registerSimple(map, "minecraft:snowball");
/* 35 */     schema.registerSimple(map, "minecraft:wither_skull");
/* 36 */     schema.registerSimple(map, "minecraft:xp_bottle");
/*    */     
/* 38 */     schema.register(map, "minecraft:arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE
/* 39 */           .in(schema)));
/*    */     
/* 41 */     schema.register(map, "minecraft:enderman", () -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE
/* 42 */           .in(schema)));
/*    */     
/* 44 */     schema.register(map, "minecraft:falling_block", () -> DSL.optionalFields("BlockState", References.BLOCK_STATE
/* 45 */           .in(schema), "TileEntityData", References.BLOCK_ENTITY
/* 46 */           .in(schema)));
/*    */     
/* 48 */     schema.register(map, "minecraft:spectral_arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE
/* 49 */           .in(schema)));
/*    */     
/* 51 */     schema.register(map, "minecraft:chest_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 52 */           .in(schema), "Items", 
/* 53 */           DSL.list(References.ITEM_STACK.in(schema))));
/*    */     
/* 55 */     schema.register(map, "minecraft:commandblock_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 56 */           .in(schema), "LastOutput", References.TEXT_COMPONENT
/* 57 */           .in(schema)));
/*    */     
/* 59 */     schema.register(map, "minecraft:furnace_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 60 */           .in(schema)));
/*    */     
/* 62 */     schema.register(map, "minecraft:hopper_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 63 */           .in(schema), "Items", 
/* 64 */           DSL.list(References.ITEM_STACK.in(schema))));
/*    */     
/* 66 */     schema.register(map, "minecraft:minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 67 */           .in(schema)));
/*    */     
/* 69 */     schema.register(map, "minecraft:spawner_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 70 */           .in(schema), References.UNTAGGED_SPAWNER
/* 71 */           .in(schema)));
/*    */     
/* 73 */     schema.register(map, "minecraft:tnt_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE
/* 74 */           .in(schema)));
/*    */ 
/*    */     
/* 77 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1510
/*    */   extends NamespacedSchema
/*    */ {
/* 11 */   public V1510(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 18 */     map.put("minecraft:command_block_minecart", (Supplier)map.remove("minecraft:commandblock_minecart"));
/* 19 */     map.put("minecraft:end_crystal", (Supplier)map.remove("minecraft:ender_crystal"));
/* 20 */     map.put("minecraft:snow_golem", (Supplier)map.remove("minecraft:snowman"));
/* 21 */     map.put("minecraft:evoker", (Supplier)map.remove("minecraft:evocation_illager"));
/* 22 */     map.put("minecraft:evoker_fangs", (Supplier)map.remove("minecraft:evocation_fangs"));
/* 23 */     map.put("minecraft:illusioner", (Supplier)map.remove("minecraft:illusion_illager"));
/* 24 */     map.put("minecraft:vindicator", (Supplier)map.remove("minecraft:vindication_illager"));
/* 25 */     map.put("minecraft:iron_golem", (Supplier)map.remove("minecraft:villager_golem"));
/* 26 */     map.put("minecraft:experience_orb", (Supplier)map.remove("minecraft:xp_orb"));
/* 27 */     map.put("minecraft:experience_bottle", (Supplier)map.remove("minecraft:xp_bottle"));
/* 28 */     map.put("minecraft:eye_of_ender", (Supplier)map.remove("minecraft:eye_of_ender_signal"));
/* 29 */     map.put("minecraft:firework_rocket", (Supplier)map.remove("minecraft:fireworks_rocket"));
/*    */     
/* 31 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1510.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
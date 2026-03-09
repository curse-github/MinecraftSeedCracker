/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V4071
/*    */   extends NamespacedSchema
/*    */ {
/* 11 */   public V4071(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 17 */     schema.registerSimple(map, "minecraft:creaking");
/* 18 */     schema.registerSimple(map, "minecraft:creaking_transient");
/* 19 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 25 */     registerSimple(map, "minecraft:creaking_heart");
/* 26 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4071.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
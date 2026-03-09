/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V4532
/*    */   extends NamespacedSchema
/*    */ {
/* 11 */   public V4532(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 17 */     schema.registerSimple(map, "minecraft:copper_golem_statue");
/* 18 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4532.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
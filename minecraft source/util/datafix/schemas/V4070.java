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
/*    */ public class V4070
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V4070(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 21 */     schema.registerSimple(map, "minecraft:pale_oak_boat");
/* 22 */     schema.register(map, "minecraft:pale_oak_chest_boat", name -> DSL.optionalFields("Items", 
/* 23 */           DSL.list(References.ITEM_STACK.in(schema))));
/*    */     
/* 25 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4070.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
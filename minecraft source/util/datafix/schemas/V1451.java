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
/*    */ public class V1451
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V1451(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*    */     
/* 22 */     schema.register(map, "minecraft:trapped_chest", () -> DSL.optionalFields("Items", 
/* 23 */           DSL.list(References.ITEM_STACK.in(schema))));
/*    */ 
/*    */     
/* 26 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
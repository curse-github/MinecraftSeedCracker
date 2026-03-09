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
/*    */ public class V3448
/*    */   extends NamespacedSchema
/*    */ {
/* 17 */   public V3448(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 22 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 23 */     schema.register(map, "minecraft:decorated_pot", () -> DSL.optionalFields("sherds", 
/* 24 */           DSL.list(References.ITEM_NAME.in(schema)), "item", References.ITEM_STACK
/* 25 */           .in(schema)));
/*    */     
/* 27 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3448.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
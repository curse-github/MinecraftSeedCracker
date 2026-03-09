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
/*    */ public class V1929
/*    */   extends NamespacedSchema
/*    */ {
/* 16 */   public V1929(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 22 */     schema.register(map, "minecraft:wandering_trader", name -> DSL.optionalFields("Inventory", 
/* 23 */           DSL.list(References.ITEM_STACK.in(schema)), "Offers", 
/* 24 */           DSL.optionalFields("Recipes", 
/* 25 */             DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*    */ 
/*    */ 
/*    */     
/* 29 */     schema.register(map, "minecraft:trader_llama", name -> DSL.optionalFields("Items", 
/* 30 */           DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK
/* 31 */           .in(schema), "DecorItem", References.ITEM_STACK
/* 32 */           .in(schema)));
/*    */ 
/*    */     
/* 35 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1929.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
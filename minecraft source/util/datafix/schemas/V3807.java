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
/*    */ public class V3807
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V3807(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     schema.register(map, "minecraft:vault", () -> DSL.optionalFields("config", 
/* 22 */           DSL.optionalFields("key_item", References.ITEM_STACK
/* 23 */             .in(schema)), "server_data", 
/*    */           
/* 25 */           DSL.optionalFields("items_to_eject", 
/* 26 */             DSL.list(References.ITEM_STACK.in(schema))), "shared_data", 
/*    */           
/* 28 */           DSL.optionalFields("display_item", References.ITEM_STACK
/* 29 */             .in(schema))));
/*    */ 
/*    */     
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3807.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
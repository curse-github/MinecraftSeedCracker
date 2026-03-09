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
/*    */ public class V3083
/*    */   extends NamespacedSchema
/*    */ {
/* 16 */   public V3083(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 22 */     schema.register(map, "minecraft:allay", () -> DSL.optionalFields("Inventory", 
/* 23 */           DSL.list(References.ITEM_STACK.in(schema)), "listener", 
/* 24 */           DSL.optionalFields("event", 
/* 25 */             DSL.optionalFields("game_event", References.GAME_EVENT_NAME
/* 26 */               .in(schema)))));
/*    */ 
/*    */ 
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3083.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
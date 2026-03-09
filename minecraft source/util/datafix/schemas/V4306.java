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
/*    */ public class V4306
/*    */   extends NamespacedSchema
/*    */ {
/* 14 */   public V4306(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 20 */     map.remove("minecraft:potion");
/* 21 */     schema.register(map, "minecraft:splash_potion", () -> DSL.optionalFields("Item", References.ITEM_STACK
/* 22 */           .in(schema)));
/*    */     
/* 24 */     schema.register(map, "minecraft:lingering_potion", () -> DSL.optionalFields("Item", References.ITEM_STACK
/* 25 */           .in(schema)));
/*    */     
/* 27 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4306.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
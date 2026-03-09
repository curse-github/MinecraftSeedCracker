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
/*    */ public class V3325
/*    */   extends NamespacedSchema
/*    */ {
/* 16 */   public V3325(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 23 */     schema.register(map, "minecraft:item_display", name -> DSL.optionalFields("item", References.ITEM_STACK
/* 24 */           .in(schema)));
/*    */     
/* 26 */     schema.register(map, "minecraft:block_display", name -> DSL.optionalFields("block_state", References.BLOCK_STATE
/* 27 */           .in(schema)));
/*    */     
/* 29 */     schema.register(map, "minecraft:text_display", () -> DSL.optionalFields("text", References.TEXT_COMPONENT
/* 30 */           .in(schema)));
/*    */ 
/*    */     
/* 33 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3325.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
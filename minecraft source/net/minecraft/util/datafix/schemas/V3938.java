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
/*    */ public class V3938
/*    */   extends NamespacedSchema
/*    */ {
/* 16 */   public V3938(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */   
/*    */   protected static TypeTemplate abstractArrow(Schema schema) {
/* 20 */     return DSL.optionalFields("inBlockState", References.BLOCK_STATE
/* 21 */         .in(schema), "item", References.ITEM_STACK
/* 22 */         .in(schema), "weapon", References.ITEM_STACK
/* 23 */         .in(schema));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 29 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 30 */     schema.register(map, "minecraft:spectral_arrow", () -> abstractArrow(schema));
/* 31 */     schema.register(map, "minecraft:arrow", () -> abstractArrow(schema));
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3938.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
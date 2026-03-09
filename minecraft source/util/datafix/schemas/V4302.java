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
/*    */ public class V4302
/*    */   extends NamespacedSchema
/*    */ {
/* 15 */   public V4302(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     schema.registerSimple(map, "minecraft:test_block");
/* 22 */     schema.register(map, "minecraft:test_instance_block", () -> DSL.optionalFields("data", 
/* 23 */           DSL.optionalFields("error_message", References.TEXT_COMPONENT
/* 24 */             .in(schema)), "errors", 
/*    */           
/* 26 */           DSL.list(DSL.optionalFields("text", References.TEXT_COMPONENT
/* 27 */               .in(schema)))));
/*    */ 
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V4302.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
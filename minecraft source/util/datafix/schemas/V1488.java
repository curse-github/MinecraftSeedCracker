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
/*    */ public class V1488
/*    */   extends NamespacedSchema
/*    */ {
/* 14 */   public V1488(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 20 */     schema.register(map, "minecraft:command_block", () -> DSL.optionalFields("CustomName", References.TEXT_COMPONENT
/* 21 */           .in(schema), "LastOutput", References.TEXT_COMPONENT
/* 22 */           .in(schema)));
/*    */     
/* 24 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1488.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
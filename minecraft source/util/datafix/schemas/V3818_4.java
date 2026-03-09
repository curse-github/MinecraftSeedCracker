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
/*    */ public class V3818_4
/*    */   extends NamespacedSchema
/*    */ {
/* 16 */   public V3818_4(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 21 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */ 
/*    */     
/* 24 */     schema.registerType(true, References.PARTICLE, () -> DSL.optionalFields("item", References.ITEM_STACK
/* 25 */           .in(schema), "block_state", References.BLOCK_STATE
/* 26 */           .in(schema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V3818_4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
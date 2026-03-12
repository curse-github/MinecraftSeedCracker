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
/*    */ 
/*    */ 
/*    */ public class V135
/*    */   extends Schema
/*    */ {
/* 19 */   public V135(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 24 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 26 */     schema.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", 
/* 27 */           DSL.optionalFields("Entity", References.ENTITY_TREE
/* 28 */             .in(schema)), "ender_pearls", 
/*    */           
/* 30 */           DSL.list(References.ENTITY_TREE.in(schema)), "Inventory", 
/* 31 */           DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", 
/* 32 */           DSL.list(References.ITEM_STACK.in(schema))));
/*    */     
/* 34 */     schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", 
/* 35 */           DSL.list(References.ENTITY_TREE.in(schema)), References.ENTITY
/* 36 */           .in(schema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V135.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
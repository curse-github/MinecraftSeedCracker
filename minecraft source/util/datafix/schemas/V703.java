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
/*    */ public class V703
/*    */   extends Schema
/*    */ {
/* 15 */   public V703(int versionKey, Schema parent) { super(versionKey, parent); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 22 */     map.remove("EntityHorse");
/* 23 */     schema.register(map, "Horse", () -> DSL.optionalFields("ArmorItem", References.ITEM_STACK
/* 24 */           .in(schema), "SaddleItem", References.ITEM_STACK
/* 25 */           .in(schema)));
/*    */     
/* 27 */     schema.register(map, "Donkey", () -> DSL.optionalFields("Items", 
/* 28 */           DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK
/* 29 */           .in(schema)));
/*    */     
/* 31 */     schema.register(map, "Mule", () -> DSL.optionalFields("Items", 
/* 32 */           DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK
/* 33 */           .in(schema)));
/*    */     
/* 35 */     schema.register(map, "ZombieHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK
/* 36 */           .in(schema)));
/*    */     
/* 38 */     schema.register(map, "SkeletonHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK
/* 39 */           .in(schema)));
/*    */ 
/*    */     
/* 42 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V703.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
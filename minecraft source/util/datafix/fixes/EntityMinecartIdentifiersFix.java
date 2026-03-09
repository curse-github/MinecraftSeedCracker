/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EntityMinecartIdentifiersFix extends EntityRenameFix {
/* 12 */   public EntityMinecartIdentifiersFix(Schema outputSchema) { super("EntityMinecartIdentifiersFix", outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/* 17 */     if (!name.equals("Minecart")) {
/* 18 */       return Pair.of(name, entity);
/*    */     }
/*    */     
/* 21 */     int id = ((Dynamic)entity.getOrCreate(DSL.remainderFinder())).get("Type").asInt(0);
/* 22 */     switch (id) { default: 
/*    */       case 1: 
/*    */       case 2:
/* 25 */         break; }  String newName = "MinecartFurnace";
/*    */ 
/*    */     
/* 28 */     Type<?> newType = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(newName);
/* 29 */     return Pair.of(newName, Util.writeAndReadTypedOrThrow(entity, newType, dynamic -> dynamic.remove("Type")));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityMinecartIdentifiersFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
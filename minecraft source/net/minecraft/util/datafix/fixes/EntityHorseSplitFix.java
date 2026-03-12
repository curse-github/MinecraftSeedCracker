/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EntityHorseSplitFix
/*    */   extends EntityRenameFix
/*    */ {
/* 15 */   public EntityHorseSplitFix(Schema outputSchema, boolean changesType) { super("EntityHorseSplitFix", outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/* 20 */     if (Objects.equals("EntityHorse", name)) {
/* 21 */       Dynamic<?> tag = (Dynamic)entity.get(DSL.remainderFinder());
/* 22 */       int type = tag.get("Type").asInt(0);
/* 23 */       switch (type) { default: 
/*    */         case 1: 
/*    */         case 2: 
/*    */         case 3: 
/*    */         case 4:
/* 28 */           break; }  String newName = "SkeletonHorse";
/*    */ 
/*    */       
/* 31 */       Type<?> newType = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(newName);
/* 32 */       return Pair.of(newName, Util.writeAndReadTypedOrThrow(entity, newType, dynamic -> dynamic.remove("Type")));
/*    */     } 
/* 34 */     return Pair.of(name, entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityHorseSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
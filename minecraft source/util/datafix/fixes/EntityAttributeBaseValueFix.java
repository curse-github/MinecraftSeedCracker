/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.DoubleUnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityAttributeBaseValueFix
/*    */   extends NamedEntityFix {
/*    */   private final String attributeId;
/*    */   private final DoubleUnaryOperator valueFixer;
/*    */   
/*    */   public EntityAttributeBaseValueFix(Schema outputSchema, String name, String entityName, String attributeId, DoubleUnaryOperator valueFixer) {
/* 16 */     super(outputSchema, false, name, References.ENTITY, entityName);
/* 17 */     this.attributeId = attributeId;
/* 18 */     this.valueFixer = valueFixer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixValue); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixValue(Dynamic<?> tag) {
/* 27 */     return tag.update("attributes", attributes -> 
/* 28 */         tag.createList(attributes.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityAttributeBaseValueFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
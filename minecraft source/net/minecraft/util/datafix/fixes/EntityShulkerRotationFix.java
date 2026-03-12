/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityShulkerRotationFix
/*    */   extends NamedEntityFix {
/* 12 */   public EntityShulkerRotationFix(Schema outputSchema) { super(outputSchema, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker"); }
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 16 */     List<Double> rotation = input.get("Rotation").asList(d -> Double.valueOf(d.asDouble(180.0D)));
/* 17 */     if (!rotation.isEmpty()) {
/* 18 */       rotation.set(0, Double.valueOf(((Double)rotation.get(0)).doubleValue() - 180.0D));
/* 19 */       Objects.requireNonNull(input); return input.set("Rotation", input.createList(rotation.stream().map(input::createDouble)));
/*    */     } 
/* 21 */     return input;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityShulkerRotationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
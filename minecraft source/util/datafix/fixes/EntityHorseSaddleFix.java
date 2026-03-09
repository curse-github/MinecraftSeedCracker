/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityHorseSaddleFix
/*    */   extends NamedEntityFix
/*    */ {
/* 17 */   public EntityHorseSaddleFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "EntityHorseSaddleFix", References.ENTITY, "EntityHorse"); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 24 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 25 */     Type<?> itemStackType = getInputSchema().getTypeRaw(References.ITEM_STACK);
/* 26 */     OpticFinder<?> saddleF = DSL.fieldFinder("SaddleItem", itemStackType);
/*    */     
/* 28 */     Optional<? extends Typed<?>> saddle = entity.getOptionalTyped(saddleF);
/* 29 */     Dynamic<?> tag = (Dynamic)entity.get(DSL.remainderFinder());
/* 30 */     if (saddle.isEmpty() && tag.get("Saddle").asBoolean(false)) {
/* 31 */       Typed<?> newSaddle = (Typed)itemStackType.pointTyped(entity.getOps()).orElseThrow(IllegalStateException::new);
/* 32 */       newSaddle = newSaddle.set(idF, Pair.of(References.ITEM_NAME.typeName(), "minecraft:saddle"));
/*    */       
/* 34 */       Dynamic<?> saddleTag = tag.emptyMap();
/* 35 */       saddleTag = saddleTag.set("Count", saddleTag.createByte((byte)1));
/* 36 */       saddleTag = saddleTag.set("Damage", saddleTag.createShort((short)0));
/*    */       
/* 38 */       newSaddle = newSaddle.set(DSL.remainderFinder(), saddleTag);
/* 39 */       tag.remove("Saddle");
/*    */       
/* 41 */       return entity.set(saddleF, newSaddle).set(DSL.remainderFinder(), tag);
/*    */     } 
/* 43 */     return entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityHorseSaddleFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
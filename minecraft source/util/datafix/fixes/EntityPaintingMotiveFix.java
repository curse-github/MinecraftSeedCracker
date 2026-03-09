/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityPaintingMotiveFix
/*    */   extends NamedEntityFix {
/* 17 */   public EntityPaintingMotiveFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "EntityPaintingMotiveFix", References.ENTITY, "minecraft:painting"); }
/*    */ 
/*    */   
/* 20 */   private static final Map<String, String> MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/* 21 */         map.put("donkeykong", "donkey_kong");
/* 22 */         map.put("burningskull", "burning_skull");
/* 23 */         map.put("skullandroses", "skull_and_roses");
/*    */       });
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 27 */     Optional<String> motive = input.get("Motive").asString().result();
/* 28 */     if (motive.isPresent()) {
/* 29 */       String lowerCaseMotive = ((String)motive.get()).toLowerCase(Locale.ROOT);
/* 30 */       return input.set("Motive", input.createString(NamespacedSchema.ensureNamespaced((String)MAP.getOrDefault(lowerCaseMotive, lowerCaseMotive))));
/*    */     } 
/* 32 */     return input;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityPaintingMotiveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
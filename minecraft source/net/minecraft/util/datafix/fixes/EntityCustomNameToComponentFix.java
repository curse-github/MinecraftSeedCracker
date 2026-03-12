/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ public class EntityCustomNameToComponentFix
/*    */   extends DataFix
/*    */ {
/* 22 */   public EntityCustomNameToComponentFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/* 28 */     Type<?> newEntityType = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 30 */     OpticFinder<String> idF = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 32 */     OpticFinder<String> customNameF = entityType.findField("CustomName");
/* 33 */     Type<?> newCustomNameType = newEntityType.findFieldType("CustomName");
/*    */     
/* 35 */     return fixTypeEverywhereTyped("EntityCustomNameToComponentFix", entityType, newEntityType, entity -> 
/* 36 */         fixEntity(entity, newEntityType, idF, customNameF, newCustomNameType));
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Typed<?> fixEntity(Typed<?> entity, Type<?> newEntityType, OpticFinder<String> idF, OpticFinder<String> customNameF, Type<T> newCustomNameType) {
/* 41 */     Optional<String> customName = entity.getOptional(customNameF);
/* 42 */     if (customName.isEmpty()) {
/* 43 */       return ExtraDataFixUtils.cast(newEntityType, entity);
/*    */     }
/*    */ 
/*    */     
/* 47 */     if (((String)customName.get()).isEmpty()) {
/* 48 */       return Util.writeAndReadTypedOrThrow(entity, newEntityType, dynamic -> dynamic.remove("CustomName"));
/*    */     }
/*    */     
/* 51 */     String id = (String)entity.getOptional(idF).orElse("");
/* 52 */     Dynamic<?> component = fixCustomName(entity.getOps(), (String)customName.get(), id);
/* 53 */     return entity.set(customNameF, Util.readTypedOrThrow(newCustomNameType, component));
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> fixCustomName(DynamicOps<T> ops, String customName, String id) {
/* 58 */     if ("minecraft:commandblock_minecart".equals(id)) {
/* 59 */       return new Dynamic(ops, ops.createString(customName));
/*    */     }
/* 61 */     return LegacyComponentDataFixUtils.createPlainTextComponent(ops, customName);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityCustomNameToComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
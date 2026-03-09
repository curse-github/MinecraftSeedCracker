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
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlockEntityCustomNameToComponentFix
/*    */   extends DataFix {
/* 20 */   private static final Set<String> NAMEABLE_BLOCK_ENTITIES = Set.of(new String[] { "minecraft:beacon", "minecraft:banner", "minecraft:brewing_stand", "minecraft:chest", "minecraft:trapped_chest", "minecraft:dispenser", "minecraft:dropper", "minecraft:enchanting_table", "minecraft:furnace", "minecraft:hopper", "minecraft:shulker_box" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public BlockEntityCustomNameToComponentFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 41 */     OpticFinder<String> idFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 43 */     Type<?> inputType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 44 */     Type<?> outputType = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 45 */     Type<?> patchedInputType = ExtraDataFixUtils.patchSubType(inputType, inputType, outputType);
/*    */     
/* 47 */     return fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", inputType, outputType, input -> {
/* 48 */           Optional<String> id = input.getOptional(idFinder);
/* 49 */           if (id.isPresent() && !NAMEABLE_BLOCK_ENTITIES.contains(id.get())) {
/* 50 */             return ExtraDataFixUtils.cast(outputType, input);
/*    */           }
/* 52 */           return Util.writeAndReadTypedOrThrow(
/* 53 */               ExtraDataFixUtils.cast(patchedInputType, input), outputType, BlockEntityCustomNameToComponentFix::fixTagCustomName);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Dynamic<T> fixTagCustomName(Dynamic<T> tag) {
/* 61 */     String name = tag.get("CustomName").asString("");
/*    */     
/* 63 */     if (name.isEmpty()) {
/* 64 */       return tag.remove("CustomName");
/*    */     }
/* 66 */     return tag.set("CustomName", LegacyComponentDataFixUtils.createPlainTextComponent(tag.getOps(), name));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityCustomNameToComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
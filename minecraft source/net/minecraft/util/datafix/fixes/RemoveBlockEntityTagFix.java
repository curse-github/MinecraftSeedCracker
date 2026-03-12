/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RemoveBlockEntityTagFix
/*    */   extends DataFix {
/*    */   private final Set<String> blockEntityIdsToDrop;
/*    */   
/*    */   public RemoveBlockEntityTagFix(Schema outputSchema, Set<String> blockEntityIdsToDrop) {
/* 22 */     super(outputSchema, true);
/* 23 */     this.blockEntityIdsToDrop = blockEntityIdsToDrop;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 28 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 29 */     OpticFinder<?> itemTagF = itemStackType.findField("tag");
/* 30 */     OpticFinder<?> itemBlockEntityF = itemTagF.type().findField("BlockEntityTag");
/*    */     
/* 32 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/* 33 */     OpticFinder<?> fallingBlockF = DSL.namedChoice("minecraft:falling_block", getInputSchema().getChoiceType(References.ENTITY, "minecraft:falling_block"));
/* 34 */     OpticFinder<?> fallingBlockEntityTagF = fallingBlockF.type().findField("TileEntityData");
/*    */     
/* 36 */     Type<?> structureType = getInputSchema().getType(References.STRUCTURE);
/* 37 */     OpticFinder<?> blocksF = structureType.findField("blocks");
/* 38 */     OpticFinder<?> blockTypeF = DSL.typeFinder(((List.ListType)blocksF.type()).getElement());
/* 39 */     OpticFinder<?> blockNbtF = blockTypeF.type().findField("nbt");
/*    */     
/* 41 */     OpticFinder<String> blockEntityIdF = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 43 */     return TypeRewriteRule.seq(
/* 44 */         fixTypeEverywhereTyped("ItemRemoveBlockEntityTagFix", itemStackType, input -> 
/* 45 */           input.updateTyped(itemTagF, ())), new TypeRewriteRule[] {
/*    */ 
/*    */ 
/*    */           
/* 49 */           fixTypeEverywhereTyped("FallingBlockEntityRemoveBlockEntityTagFix", entityType, input -> 
/* 50 */             input.updateTyped(fallingBlockF, ())), 
/*    */ 
/*    */ 
/*    */           
/* 54 */           fixTypeEverywhereTyped("StructureRemoveBlockEntityTagFix", structureType, input -> 
/* 55 */             input.updateTyped(blocksF, ())), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 63 */           convertUnchecked("ItemRemoveBlockEntityTagFix - update block entity type", getInputSchema().getType(References.BLOCK_ENTITY), getOutputSchema().getType(References.BLOCK_ENTITY))
/*    */         });
/*    */   }
/*    */   
/*    */   private Typed<?> removeBlockEntity(Typed<?> tag, OpticFinder<?> blockEntityF, OpticFinder<String> blockEntityIdF, String blockEntityFieldName) {
/* 68 */     Optional<? extends Typed<?>> maybeBlockEntity = tag.getOptionalTyped(blockEntityF);
/* 69 */     if (maybeBlockEntity.isEmpty()) {
/* 70 */       return tag;
/*    */     }
/* 72 */     String blockEntityId = (String)((Typed)maybeBlockEntity.get()).getOptional(blockEntityIdF).orElse("");
/* 73 */     if (!this.blockEntityIdsToDrop.contains(blockEntityId)) {
/* 74 */       return tag;
/*    */     }
/* 76 */     return Util.writeAndReadTypedOrThrow(tag, tag.getType(), tagData -> tagData.remove(blockEntityFieldName));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\RemoveBlockEntityTagFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
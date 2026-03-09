/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityBlockStateFix extends NamedEntityFix {
/* 12 */   public BlockEntityBlockStateFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "BlockEntityBlockStateFix", References.BLOCK_ENTITY, "minecraft:piston"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     Type<?> newType = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:piston");
/*    */     
/* 19 */     Type<?> blockStateType = newType.findFieldType("blockState");
/* 20 */     OpticFinder<?> blockStateF = DSL.fieldFinder("blockState", blockStateType);
/* 21 */     Dynamic<?> tag = (Dynamic)entity.get(DSL.remainderFinder());
/*    */     
/* 23 */     int block = tag.get("blockId").asInt(0);
/* 24 */     tag = tag.remove("blockId");
/* 25 */     int data = tag.get("blockData").asInt(0) & 0xF;
/* 26 */     tag = tag.remove("blockData");
/*    */     
/* 28 */     Dynamic<?> blockStateTag = BlockStateData.getTag(block << 4 | data);
/* 29 */     Typed<?> output = (Typed)newType.pointTyped(entity.getOps()).orElseThrow(() -> new IllegalStateException("Could not create new piston block entity."));
/* 30 */     return output.set(DSL.remainderFinder(), tag).set(blockStateF, (Typed)((Pair)blockStateType.readTyped(blockStateTag).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created block state tag."))).getFirst());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityBlockStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.CompoundList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class NewVillageFix
/*    */   extends DataFix {
/* 22 */   public NewVillageFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     CompoundList.CompoundListType<String, ?> startsType = DSL.compoundList(DSL.string(), getInputSchema().getType(References.STRUCTURE_FEATURE));
/* 28 */     OpticFinder<? extends List<? extends Pair<String, ?>>> finder = startsType.finder();
/*    */     
/* 30 */     return cap(startsType);
/*    */   }
/*    */   
/*    */   private <SF> TypeRewriteRule cap(CompoundList.CompoundListType<String, SF> startsType) {
/* 34 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 35 */     Type<?> structureType = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 36 */     OpticFinder<?> levelFinder = chunkType.findField("Level");
/* 37 */     OpticFinder<?> structuresFinder = levelFinder.type().findField("Structures");
/* 38 */     OpticFinder<?> startsFinder = structuresFinder.type().findField("Starts");
/* 39 */     OpticFinder<List<Pair<String, SF>>> listFinder = startsType.finder();
/* 40 */     return TypeRewriteRule.seq(
/* 41 */         fixTypeEverywhereTyped("NewVillageFix", chunkType, input -> 
/* 42 */           input.updateTyped(levelFinder, ())), 
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
/* 57 */         fixTypeEverywhereTyped("NewVillageStartFix", structureType, input -> input.update(DSL.remainderFinder(), ())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\NewVillageFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
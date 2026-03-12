/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlendingDataFix
/*    */   extends DataFix {
/*    */   private final String name;
/* 20 */   private static final Set<String> STATUSES_TO_SKIP_BLENDING = Set.of("minecraft:empty", "minecraft:structure_starts", "minecraft:structure_references", "minecraft:biomes");
/*    */   
/*    */   public BlendingDataFix(Schema outputSchema) {
/* 23 */     super(outputSchema, false);
/* 24 */     this.name = "Blending Data Fix v" + outputSchema.getVersionKey();
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 29 */     Type<?> chunkType = getOutputSchema().getType(References.CHUNK);
/*    */     
/* 31 */     return fixTypeEverywhereTyped(this.name, chunkType, chunk -> 
/* 32 */         chunk.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updateChunkTag(Dynamic<?> chunkTag, OptionalDynamic<?> contextTag) {
/* 37 */     chunkTag = chunkTag.remove("blending_data");
/* 38 */     boolean isOverworld = "minecraft:overworld".equals(contextTag.get("dimension").asString().result().orElse(""));
/*    */     
/* 40 */     Optional<? extends Dynamic<?>> statusOpt = chunkTag.get("Status").result();
/* 41 */     if (isOverworld && statusOpt.isPresent()) {
/* 42 */       String status = NamespacedSchema.ensureNamespaced(((Dynamic)statusOpt.get()).asString("empty"));
/* 43 */       Optional<? extends Dynamic<?>> belowZeroRetrogenOpt = chunkTag.get("below_zero_retrogen").result();
/*    */       
/* 45 */       if (!STATUSES_TO_SKIP_BLENDING.contains(status)) {
/*    */         
/* 47 */         chunkTag = updateBlendingData(chunkTag, 384, -64);
/* 48 */       } else if (belowZeroRetrogenOpt.isPresent()) {
/*    */         
/* 50 */         Dynamic<?> belowZeroRetrogen = (Dynamic)belowZeroRetrogenOpt.get();
/* 51 */         String targetStatus = NamespacedSchema.ensureNamespaced(belowZeroRetrogen.get("target_status").asString("empty"));
/* 52 */         if (!STATUSES_TO_SKIP_BLENDING.contains(targetStatus)) {
/* 53 */           chunkTag = updateBlendingData(chunkTag, 256, 0);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 58 */     return chunkTag;
/*    */   }
/*    */   
/*    */   private static Dynamic<?> updateBlendingData(Dynamic<?> chunkTag, int height, int minY) {
/* 62 */     return chunkTag.set("blending_data", chunkTag.createMap(Map.of(chunkTag
/* 63 */             .createString("min_section"), chunkTag.createInt(SectionPos.blockToSectionCoord(minY)), chunkTag
/* 64 */             .createString("max_section"), chunkTag.createInt(SectionPos.blockToSectionCoord(minY + height)))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlendingDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
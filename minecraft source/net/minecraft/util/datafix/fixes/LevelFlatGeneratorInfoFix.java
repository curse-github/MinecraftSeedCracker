/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Splitter;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.StreamSupport;
/*    */ import org.apache.commons.lang3.math.NumberUtils;
/*    */ 
/*    */ public class LevelFlatGeneratorInfoFix extends DataFix {
/* 20 */   public LevelFlatGeneratorInfoFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */   
/*    */   private static final String GENERATOR_OPTIONS = "generatorOptions";
/*    */   
/*    */   @VisibleForTesting
/*    */   static final String DEFAULT = "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/* 27 */   private static final Splitter SPLITTER = Splitter.on(';').limit(5);
/* 28 */   private static final Splitter LAYER_SPLITTER = Splitter.on(',');
/* 29 */   private static final Splitter OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2);
/* 30 */   private static final Splitter AMOUNT_SPLITTER = Splitter.on('*').limit(2);
/* 31 */   private static final Splitter BLOCK_SPLITTER = Splitter.on(':').limit(3);
/*    */ 
/*    */ 
/*    */   
/* 35 */   public TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("LevelFlatGeneratorInfoFix", getInputSchema().getType(References.LEVEL), input -> input.update(DSL.remainderFinder(), this::fix)); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> input) {
/* 39 */     if (input.get("generatorName").asString("").equalsIgnoreCase("flat"))
/* 40 */       return input.update("generatorOptions", options -> { Objects.requireNonNull(options); return (Dynamic)DataFixUtils.orElse(options.asString().map(this::fixString).map(options::createString).result(), options);
/*    */           }); 
/* 42 */     return input;
/*    */   } @VisibleForTesting
/*    */   String fixString(String generatorOptions) {
/*    */     String layerInfo;
/*    */     int version;
/* 47 */     if (generatorOptions.isEmpty()) {
/* 48 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*    */     }
/*    */     
/* 51 */     Iterator<String> parts = SPLITTER.split(generatorOptions).iterator();
/*    */     
/* 53 */     String firstPart = (String)parts.next();
/*    */ 
/*    */     
/* 56 */     if (parts.hasNext()) {
/* 57 */       version = NumberUtils.toInt(firstPart, 0);
/* 58 */       layerInfo = (String)parts.next();
/*    */     } else {
/* 60 */       version = 0;
/* 61 */       layerInfo = firstPart;
/*    */     } 
/*    */     
/* 64 */     if (version < 0 || version > 3) {
/* 65 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*    */     }
/*    */     
/* 68 */     StringBuilder result = new StringBuilder();
/*    */     
/* 70 */     Splitter heightSplitter = (version < 3) ? OLD_AMOUNT_SPLITTER : AMOUNT_SPLITTER;
/*    */     
/* 72 */     result.append((String)StreamSupport.stream(LAYER_SPLITTER.split(layerInfo).spliterator(), false).map(layerString -> {
/*    */             String layerType;
/*    */             int height;
/* 75 */             List<String> list = heightSplitter.splitToList(layerString);
/* 76 */             if (list.size() == 2) {
/* 77 */               height = NumberUtils.toInt((String)list.get(0));
/* 78 */               layerType = (String)list.get(1);
/*    */             } else {
/* 80 */               height = 1;
/* 81 */               layerType = (String)list.get(0);
/*    */             } 
/* 83 */             List<String> layerParts = BLOCK_SPLITTER.splitToList(layerType);
/*    */             
/* 85 */             int nameIndex = ((String)layerParts.get(0)).equals("minecraft") ? 1 : 0;
/*    */             
/* 87 */             String blockString = (String)layerParts.get(nameIndex);
/* 88 */             int blockId = (version == 3) ? EntityBlockStateFix.getBlockId("minecraft:" + blockString) : NumberUtils.toInt(blockString, 0);
/* 89 */             int dataIndex = nameIndex + 1;
/* 90 */             int data = (layerParts.size() > dataIndex) ? NumberUtils.toInt((String)layerParts.get(dataIndex), 0) : 0;
/*    */             
/* 92 */             return ((height == 1) ? "" : ("" + height + "*")) + ((height == 1) ? "" : ("" + height + "*"));
/* 93 */           }).collect(Collectors.joining(",")));
/*    */     
/* 95 */     while (parts.hasNext()) {
/* 96 */       result.append(';').append((String)parts.next());
/*    */     }
/*    */     
/* 99 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LevelFlatGeneratorInfoFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
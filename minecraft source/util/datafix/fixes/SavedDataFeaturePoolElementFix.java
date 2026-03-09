/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class SavedDataFeaturePoolElementFix extends DataFix {
/*  21 */   private static final Pattern INDEX_PATTERN = Pattern.compile("\\[(\\d+)\\]");
/*  22 */   private static final Set<String> PIECE_TYPE = Sets.newHashSet(new String[] { "minecraft:jigsaw", "minecraft:nvi", "minecraft:pcp", "minecraft:bastionremnant", "minecraft:runtime" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   private static final Set<String> FEATURES = Sets.newHashSet(new String[] { "minecraft:tree", "minecraft:flower", "minecraft:block_pile", "minecraft:random_patch" });
/*     */ 
/*     */   
/*  32 */   public SavedDataFeaturePoolElementFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public TypeRewriteRule makeRule() { return writeFixAndRead("SavedDataFeaturePoolElementFix", getInputSchema().getType(References.STRUCTURE_FEATURE), getOutputSchema().getType(References.STRUCTURE_FEATURE), SavedDataFeaturePoolElementFix::fixTag); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   private static <T> Dynamic<T> fixTag(Dynamic<T> input) { return input.update("Children", SavedDataFeaturePoolElementFix::updateChildren); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static <T> Dynamic<T> updateChildren(Dynamic<T> input) { Objects.requireNonNull(input); return (Dynamic)input.asStreamOpt().map(SavedDataFeaturePoolElementFix::updateChildren).map(input::createList).result().orElse(input); }
/*     */ 
/*     */   
/*     */   private static Stream<? extends Dynamic<?>> updateChildren(Stream<? extends Dynamic<?>> stream) {
/*  49 */     return stream.map(child -> {
/*     */           
/*  51 */           String id = child.get("id").asString("");
/*  52 */           if (!PIECE_TYPE.contains(id)) {
/*  53 */             return child;
/*     */           }
/*  55 */           OptionalDynamic<?> poolElement = child.get("pool_element");
/*  56 */           if (!poolElement.get("element_type").asString("").equals("minecraft:feature_pool_element")) {
/*  57 */             return child;
/*     */           }
/*     */           
/*  60 */           return child.update("pool_element", ());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> OptionalDynamic<T> get(Dynamic<T> input, String... path) {
/*  68 */     if (path.length == 0) {
/*  69 */       throw new IllegalArgumentException("Missing path");
/*     */     }
/*     */     
/*  72 */     OptionalDynamic<T> output = input.get(path[0]);
/*  73 */     for (int i = 1; i < path.length; i++) {
/*  74 */       String element = path[i];
/*     */       
/*  76 */       Matcher matcher = INDEX_PATTERN.matcher(element);
/*  77 */       if (matcher.matches()) {
/*  78 */         int id = Integer.parseInt(matcher.group(1));
/*  79 */         List<? extends Dynamic<T>> dynamics = output.asList(Function.identity());
/*  80 */         if (id >= 0 && id < dynamics.size()) {
/*  81 */           output = new OptionalDynamic<T>(input.getOps(), DataResult.success((Dynamic)dynamics.get(id)));
/*     */         } else {
/*  83 */           output = new OptionalDynamic<T>(input.getOps(), DataResult.error(() -> "Missing id:" + id));
/*     */         } 
/*     */       } else {
/*  86 */         output = output.get(element);
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     return output;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static Dynamic<?> fixFeature(Dynamic<?> value) {
/*  95 */     Optional<String> replacement = getReplacement(
/*  96 */         get(value, new String[] { "type" }).asString(""), 
/*  97 */         get(value, new String[] { "name" }).asString(""), 
/*     */         
/*  99 */         get(value, new String[] { "config", "state_provider", "type" }).asString(""), 
/* 100 */         get(value, new String[] { "config", "state_provider", "state", "Name" }).asString(""), 
/* 101 */         get(value, new String[] { "config", "state_provider", "entries", "[0]", "data", "Name" }).asString(""), 
/*     */         
/* 103 */         get(value, new String[] { "config", "foliage_placer", "type" }).asString(""), 
/* 104 */         get(value, new String[] { "config", "leaves_provider", "state", "Name" }).asString(""));
/*     */ 
/*     */     
/* 107 */     if (replacement.isPresent()) {
/* 108 */       return value.createString((String)replacement.get());
/*     */     }
/* 110 */     return value;
/*     */   }
/*     */   
/*     */   private static Optional<String> getReplacement(String type, String name, String stateProviderType, String stateProviderState, String stateProviderFirstWeighedState, String foliagePlacerType, String leavesProviderState) {
/*     */     String feature;
/* 115 */     if (!type.isEmpty()) {
/* 116 */       feature = type;
/* 117 */     } else if (!name.isEmpty()) {
/* 118 */       if ("minecraft:normal_tree".equals(name)) {
/* 119 */         feature = "minecraft:tree";
/*     */       } else {
/* 121 */         feature = name;
/*     */       } 
/*     */     } else {
/* 124 */       return Optional.empty();
/*     */     } 
/*     */     
/* 127 */     if (FEATURES.contains(feature)) {
/* 128 */       if ("minecraft:random_patch".equals(feature)) {
/* 129 */         if ("minecraft:simple_state_provider".equals(stateProviderType)) {
/* 130 */           if ("minecraft:sweet_berry_bush".equals(stateProviderState))
/* 131 */             return Optional.of("minecraft:patch_berry_bush"); 
/* 132 */           if ("minecraft:cactus".equals(stateProviderState)) {
/* 133 */             return Optional.of("minecraft:patch_cactus");
/*     */           }
/* 135 */         } else if ("minecraft:weighted_state_provider".equals(stateProviderType) && (
/* 136 */           "minecraft:grass".equals(stateProviderFirstWeighedState) || "minecraft:fern".equals(stateProviderFirstWeighedState))) {
/* 137 */           return Optional.of("minecraft:patch_taiga_grass");
/*     */         }
/*     */       
/* 140 */       } else if ("minecraft:block_pile".equals(feature)) {
/* 141 */         if ("minecraft:simple_state_provider".equals(stateProviderType) || "minecraft:rotated_block_provider".equals(stateProviderType)) {
/* 142 */           if ("minecraft:hay_block".equals(stateProviderState))
/* 143 */             return Optional.of("minecraft:pile_hay"); 
/* 144 */           if ("minecraft:melon".equals(stateProviderState))
/* 145 */             return Optional.of("minecraft:pile_melon"); 
/* 146 */           if ("minecraft:snow".equals(stateProviderState)) {
/* 147 */             return Optional.of("minecraft:pile_snow");
/*     */           }
/* 149 */         } else if ("minecraft:weighted_state_provider".equals(stateProviderType)) {
/* 150 */           if ("minecraft:packed_ice".equals(stateProviderFirstWeighedState) || "minecraft:blue_ice".equals(stateProviderFirstWeighedState))
/* 151 */             return Optional.of("minecraft:pile_ice"); 
/* 152 */           if ("minecraft:jack_o_lantern".equals(stateProviderFirstWeighedState) || "minecraft:pumpkin".equals(stateProviderFirstWeighedState))
/* 153 */             return Optional.of("minecraft:pile_pumpkin"); 
/*     */         } 
/*     */       } else {
/* 156 */         if ("minecraft:flower".equals(feature))
/* 157 */           return Optional.of("minecraft:flower_plain"); 
/* 158 */         if ("minecraft:tree".equals(feature)) {
/* 159 */           if ("minecraft:acacia_foliage_placer".equals(foliagePlacerType))
/* 160 */             return Optional.of("minecraft:acacia"); 
/* 161 */           if ("minecraft:blob_foliage_placer".equals(foliagePlacerType) && "minecraft:oak_leaves".equals(leavesProviderState))
/* 162 */             return Optional.of("minecraft:oak"); 
/* 163 */           if ("minecraft:pine_foliage_placer".equals(foliagePlacerType))
/* 164 */             return Optional.of("minecraft:pine"); 
/* 165 */           if ("minecraft:spruce_foliage_placer".equals(foliagePlacerType)) {
/* 166 */             return Optional.of("minecraft:spruce");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 171 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\SavedDataFeaturePoolElementFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
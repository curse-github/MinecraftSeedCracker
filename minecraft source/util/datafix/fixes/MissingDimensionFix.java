/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FieldFinder;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.CompoundList;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MissingDimensionFix
/*     */   extends DataFix
/*     */ {
/*  35 */   public MissingDimensionFix(Schema schema, boolean changesType) { super(schema, changesType); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   protected static <A> Type<Pair<A, Dynamic<?>>> fields(String name, Type<A> type) { return DSL.and(DSL.field(name, type), DSL.remainderType()); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   protected static <A> Type<Pair<Either<A, Unit>, Dynamic<?>>> optionalFields(String name, Type<A> type) { return DSL.and(DSL.optional(DSL.field(name, type)), DSL.remainderType()); }
/*     */ 
/*     */   
/*     */   protected static <A1, A2> Type<Pair<Either<A1, Unit>, Pair<Either<A2, Unit>, Dynamic<?>>>> optionalFields(String name1, Type<A1> type1, String name2, Type<A2> type2) {
/*  47 */     return DSL.and(
/*  48 */         DSL.optional(DSL.field(name1, type1)), 
/*  49 */         DSL.optional(DSL.field(name2, type2)), 
/*  50 */         DSL.remainderType());
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  55 */     Schema schema = getInputSchema();
/*  56 */     Type<?> generatorType = DSL.taggedChoiceType("type", DSL.string(), ImmutableMap.of("minecraft:debug", 
/*  57 */           DSL.remainderType(), "minecraft:flat", 
/*  58 */           flatType(schema), "minecraft:noise", 
/*  59 */           optionalFields("biome_source", 
/*  60 */             DSL.taggedChoiceType("type", DSL.string(), ImmutableMap.of("minecraft:fixed", 
/*  61 */                 fields("biome", schema.getType(References.BIOME)), "minecraft:multi_noise", 
/*  62 */                 DSL.list(fields("biome", schema.getType(References.BIOME))), "minecraft:checkerboard", 
/*  63 */                 fields("biomes", DSL.list(schema.getType(References.BIOME))), "minecraft:vanilla_layered", 
/*  64 */                 DSL.remainderType(), "minecraft:the_end", 
/*  65 */                 DSL.remainderType())), "settings", 
/*     */             
/*  67 */             DSL.or(DSL.string(), optionalFields("default_block", schema
/*  68 */                 .getType(References.BLOCK_NAME), "default_fluid", schema
/*  69 */                 .getType(References.BLOCK_NAME))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     CompoundList.CompoundListType<String, ?> dimensionsType = DSL.compoundList(NamespacedSchema.namespacedString(), fields("generator", generatorType));
/*  75 */     Type<?> expectedDimensionsType = DSL.and(dimensionsType, DSL.remainderType());
/*     */     
/*  77 */     Type<?> settings = schema.getType(References.WORLD_GEN_SETTINGS);
/*     */     
/*  79 */     FieldFinder<?> dimensionsFinder = new FieldFinder<?>("dimensions", expectedDimensionsType);
/*  80 */     if (!settings.findFieldType("dimensions").equals(expectedDimensionsType)) {
/*  81 */       throw new IllegalStateException();
/*     */     }
/*  83 */     OpticFinder<? extends List<? extends Pair<String, ?>>> dimensionListFinder = dimensionsType.finder();
/*  84 */     return fixTypeEverywhereTyped("MissingDimensionFix", settings, input -> input.updateTyped(dimensionsFinder, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Type<? extends Pair<? extends Either<? extends Pair<? extends Either<?, Unit>, ? extends Pair<? extends Either<? extends List<? extends Pair<? extends Either<?, Unit>, Dynamic<?>>>, Unit>, Dynamic<?>>>, Unit>, Dynamic<?>>> flatType(Schema schema) {
/*  99 */     return optionalFields("settings", optionalFields("biome", schema
/* 100 */           .getType(References.BIOME), "layers", 
/* 101 */           DSL.list(optionalFields("block", schema.getType(References.BLOCK_NAME)))));
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Dynamic<T> recreateSettings(Dynamic<T> tag) {
/* 106 */     long seed = tag.get("seed").asLong(0L);
/* 107 */     return new Dynamic(tag.getOps(), WorldGenSettingsFix.vanillaLevels(tag, seed, WorldGenSettingsFix.defaultOverworld(tag, seed), false));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\MissingDimensionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
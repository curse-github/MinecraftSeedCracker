/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class MobEffectIdFix
/*     */   extends DataFix {
/*  24 */   private static final Int2ObjectMap<String> ID_MAP = (Int2ObjectMap)Util.make(new Int2ObjectOpenHashMap(), m -> {
/*  25 */         m.put(1, "minecraft:speed");
/*  26 */         m.put(2, "minecraft:slowness");
/*  27 */         m.put(3, "minecraft:haste");
/*  28 */         m.put(4, "minecraft:mining_fatigue");
/*  29 */         m.put(5, "minecraft:strength");
/*  30 */         m.put(6, "minecraft:instant_health");
/*  31 */         m.put(7, "minecraft:instant_damage");
/*  32 */         m.put(8, "minecraft:jump_boost");
/*  33 */         m.put(9, "minecraft:nausea");
/*  34 */         m.put(10, "minecraft:regeneration");
/*  35 */         m.put(11, "minecraft:resistance");
/*  36 */         m.put(12, "minecraft:fire_resistance");
/*  37 */         m.put(13, "minecraft:water_breathing");
/*  38 */         m.put(14, "minecraft:invisibility");
/*  39 */         m.put(15, "minecraft:blindness");
/*  40 */         m.put(16, "minecraft:night_vision");
/*  41 */         m.put(17, "minecraft:hunger");
/*  42 */         m.put(18, "minecraft:weakness");
/*  43 */         m.put(19, "minecraft:poison");
/*  44 */         m.put(20, "minecraft:wither");
/*  45 */         m.put(21, "minecraft:health_boost");
/*  46 */         m.put(22, "minecraft:absorption");
/*  47 */         m.put(23, "minecraft:saturation");
/*  48 */         m.put(24, "minecraft:glowing");
/*  49 */         m.put(25, "minecraft:levitation");
/*  50 */         m.put(26, "minecraft:luck");
/*  51 */         m.put(27, "minecraft:unluck");
/*  52 */         m.put(28, "minecraft:slow_falling");
/*  53 */         m.put(29, "minecraft:conduit_power");
/*  54 */         m.put(30, "minecraft:dolphins_grace");
/*  55 */         m.put(31, "minecraft:bad_omen");
/*  56 */         m.put(32, "minecraft:hero_of_the_village");
/*  57 */         m.put(33, "minecraft:darkness");
/*     */       });
/*     */   
/*  60 */   private static final Set<String> MOB_EFFECT_INSTANCE_CARRIER_ITEMS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public MobEffectIdFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static <T> Optional<Dynamic<T>> getAndConvertMobEffectId(Dynamic<T> obj, String fieldName) { Objects.requireNonNull(obj); return obj.get(fieldName).asNumber().result().map(id -> (String)ID_MAP.get(id.intValue())).map(obj::createString); }
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectIdField(Dynamic<T> input, String oldFieldName, Dynamic<T> output, String newFieldName) {
/*  81 */     Optional<Dynamic<T>> mappedId = getAndConvertMobEffectId(input, oldFieldName);
/*  82 */     return output.replaceField(oldFieldName, newFieldName, mappedId);
/*     */   }
/*     */ 
/*     */   
/*  86 */   private static <T> Dynamic<T> updateMobEffectIdField(Dynamic<T> input, String oldFieldName, String newFieldName) { return updateMobEffectIdField(input, oldFieldName, input, newFieldName); }
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectInstance(Dynamic<T> input) {
/*  90 */     input = updateMobEffectIdField(input, "Id", "id");
/*  91 */     input = input.renameField("Ambient", "ambient");
/*  92 */     input = input.renameField("Amplifier", "amplifier");
/*  93 */     input = input.renameField("Duration", "duration");
/*  94 */     input = input.renameField("ShowParticles", "show_particles");
/*  95 */     input = input.renameField("ShowIcon", "show_icon");
/*     */     
/*  97 */     Optional<Dynamic<T>> hiddenEffect = input.get("HiddenEffect").result().map(MobEffectIdFix::updateMobEffectInstance);
/*  98 */     return input.replaceField("HiddenEffect", "hidden_effect", hiddenEffect);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectInstanceList(Dynamic<T> input, String oldField, String newField) {
/* 102 */     Optional<Dynamic<T>> newValue = input.get(oldField).asStreamOpt().result().map(effects -> input.createList(effects.map(MobEffectIdFix::updateMobEffectInstance)));
/* 103 */     return input.replaceField(oldField, newField, newValue);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateSuspiciousStewEntry(Dynamic<T> input, Dynamic<T> output) {
/* 107 */     output = updateMobEffectIdField(input, "EffectId", output, "id");
/*     */     
/* 109 */     Optional<Dynamic<T>> duration = input.get("EffectDuration").result();
/* 110 */     return output.replaceField("EffectDuration", "duration", duration);
/*     */   }
/*     */ 
/*     */   
/* 114 */   private static <T> Dynamic<T> updateSuspiciousStewEntry(Dynamic<T> input) { return updateSuspiciousStewEntry(input, input); }
/*     */ 
/*     */   
/*     */   private Typed<?> updateNamedChoice(Typed<?> input, DSL.TypeReference typeReference, String name, Function<Dynamic<?>, Dynamic<?>> function) {
/* 118 */     Type<?> oldType = getInputSchema().getChoiceType(typeReference, name);
/* 119 */     Type<?> newType = getOutputSchema().getChoiceType(typeReference, name);
/* 120 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, typedTag -> typedTag.update(DSL.remainderFinder(), function));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule blockEntityFixer() {
/* 124 */     Type<?> blockEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 125 */     return fixTypeEverywhereTyped("BlockEntityMobEffectIdFix", blockEntityType, input -> 
/* 126 */         updateNamedChoice(input, References.BLOCK_ENTITY, "minecraft:beacon", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixMooshroomTag(Dynamic<T> entityTag) {
/* 135 */     Dynamic<T> initialEntry = entityTag.emptyMap();
/* 136 */     Dynamic<T> entry = updateSuspiciousStewEntry(entityTag, initialEntry);
/*     */     
/* 138 */     if (!entry.equals(initialEntry)) {
/* 139 */       entityTag = entityTag.set("stew_effects", entityTag.createList(Stream.of(entry)));
/*     */     }
/* 141 */     return entityTag.remove("EffectId").remove("EffectDuration");
/*     */   }
/*     */ 
/*     */   
/* 145 */   private static <T> Dynamic<T> fixArrowTag(Dynamic<T> data) { return updateMobEffectInstanceList(data, "CustomPotionEffects", "custom_potion_effects"); }
/*     */ 
/*     */ 
/*     */   
/* 149 */   private static <T> Dynamic<T> fixAreaEffectCloudTag(Dynamic<T> data) { return updateMobEffectInstanceList(data, "Effects", "effects"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static Dynamic<?> updateLivingEntityTag(Dynamic<?> data) { return updateMobEffectInstanceList(data, "ActiveEffects", "active_effects"); }
/*     */ 
/*     */   
/*     */   private TypeRewriteRule entityFixer() {
/* 158 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/* 159 */     return fixTypeEverywhereTyped("EntityMobEffectIdFix", entityType, input -> {
/* 160 */           input = updateNamedChoice(input, References.ENTITY, "minecraft:mooshroom", MobEffectIdFix::fixMooshroomTag);
/* 161 */           input = updateNamedChoice(input, References.ENTITY, "minecraft:arrow", MobEffectIdFix::fixArrowTag);
/* 162 */           input = updateNamedChoice(input, References.ENTITY, "minecraft:area_effect_cloud", MobEffectIdFix::fixAreaEffectCloudTag);
/* 163 */           return input.update(DSL.remainderFinder(), MobEffectIdFix::updateLivingEntityTag);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeRewriteRule playerFixer() {
/* 169 */     Type<?> playerType = getInputSchema().getType(References.PLAYER);
/* 170 */     return fixTypeEverywhereTyped("PlayerMobEffectIdFix", playerType, input -> 
/* 171 */         input.update(DSL.remainderFinder(), MobEffectIdFix::updateLivingEntityTag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixSuspiciousStewTag(Dynamic<T> tag) {
/* 179 */     Optional<Dynamic<T>> effectsList = tag.get("Effects").asStreamOpt().result().map(list -> tag.createList(list.map(MobEffectIdFix::updateSuspiciousStewEntry)));
/*     */     
/* 181 */     return tag.replaceField("Effects", "effects", effectsList);
/*     */   }
/*     */   
/*     */   private TypeRewriteRule itemStackFixer() {
/* 185 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*     */     
/* 187 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 188 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 189 */     return fixTypeEverywhereTyped("ItemStackMobEffectIdFix", itemStackType, input -> {
/* 190 */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/* 191 */           if (idOpt.isPresent()) {
/* 192 */             String id = (String)((Pair)idOpt.get()).getSecond();
/* 193 */             if (id.equals("minecraft:suspicious_stew")) {
/* 194 */               return input.updateTyped(tagF, ());
/*     */             }
/* 196 */             if (MOB_EFFECT_INSTANCE_CARRIER_ITEMS.contains(id)) {
/* 197 */               return input.updateTyped(tagF, ());
/*     */             }
/*     */           } 
/* 200 */           return input;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 206 */     return TypeRewriteRule.seq(
/* 207 */         blockEntityFixer(), new TypeRewriteRule[] {
/* 208 */           entityFixer(), 
/* 209 */           playerFixer(), 
/* 210 */           itemStackFixer()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\MobEffectIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemStackComponentizationFix extends DataFix {
/*     */   private static final int HIDE_ENCHANTMENTS = 1;
/*     */   private static final int HIDE_MODIFIERS = 2;
/*     */   private static final int HIDE_UNBREAKABLE = 4;
/*     */   private static final int HIDE_CAN_DESTROY = 8;
/*     */   private static final int HIDE_CAN_PLACE = 16;
/*     */   private static final int HIDE_ADDITIONAL = 32;
/*     */   private static final int HIDE_DYE = 64;
/*     */   private static final int HIDE_UPGRADES = 128;
/*  38 */   private static final Set<String> POTION_HOLDER_IDS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final Set<String> BUCKETED_MOB_IDS = Set.of("minecraft:pufferfish_bucket", "minecraft:salmon_bucket", "minecraft:cod_bucket", "minecraft:tropical_fish_bucket", "minecraft:axolotl_bucket", "minecraft:tadpole_bucket");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final List<String> BUCKETED_MOB_TAGS = List.of("NoAI", "Silent", "NoGravity", "Glowing", "Invulnerable", "Health", "Age", "Variant", "HuntingCooldown", "BucketVariantTag");
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
/*  66 */   private static final Set<String> BOOLEAN_BLOCK_STATE_PROPERTIES = Set.of(new String[] { "attached", "bottom", "conditional", "disarmed", "drag", "enabled", "extended", "eye", "falling", "hanging", "has_bottle_0", "has_bottle_1", "has_bottle_2", "has_record", "has_book", "inverted", "in_wall", "lit", "locked", "occupied", "open", "persistent", "powered", "short", "signal_fire", "snowy", "triggered", "unstable", "waterlogged", "berries", "bloom", "shrieking", "can_summon", "up", "down", "north", "east", "south", "west", "slot_0_occupied", "slot_1_occupied", "slot_2_occupied", "slot_3_occupied", "slot_4_occupied", "slot_5_occupied", "cracked", "crafting" });
/*     */   
/*  68 */   private static final Splitter PROPERTY_SPLITTER = Splitter.on(',');
/*     */ 
/*     */   
/*  71 */   public ItemStackComponentizationFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */   
/*     */   private static void fixItemStack(ItemStackData itemStack, Dynamic<?> dynamic) {
/*  75 */     int hideFlags = itemStack.removeTag("HideFlags").asInt(0);
/*  76 */     itemStack.moveTagToComponent("Damage", "minecraft:damage", dynamic.createInt(0));
/*  77 */     itemStack.moveTagToComponent("RepairCost", "minecraft:repair_cost", dynamic.createInt(0));
/*  78 */     itemStack.moveTagToComponent("CustomModelData", "minecraft:custom_model_data");
/*     */     
/*  80 */     itemStack.removeTag("BlockStateTag").result()
/*  81 */       .ifPresent(blockStateTag -> itemStack.setComponent("minecraft:block_state", fixBlockStateTag(blockStateTag)));
/*     */     
/*  83 */     itemStack.moveTagToComponent("EntityTag", "minecraft:entity_data");
/*  84 */     itemStack.fixSubTag("BlockEntityTag", false, blockEntityTag -> {
/*  85 */           String id = NamespacedSchema.ensureNamespaced(blockEntityTag.get("id").asString(""));
/*  86 */           blockEntityTag = fixBlockEntityTag(itemStack, blockEntityTag, id);
/*  87 */           Dynamic<?> withoutId = blockEntityTag.remove("id");
/*  88 */           if (withoutId.equals(blockEntityTag.emptyMap())) {
/*  89 */             return withoutId;
/*     */           }
/*  91 */           return blockEntityTag;
/*     */         });
/*  93 */     itemStack.moveTagToComponent("BlockEntityTag", "minecraft:block_entity_data");
/*     */     
/*  95 */     if (itemStack.removeTag("Unbreakable").asBoolean(false)) {
/*  96 */       Dynamic<?> component = dynamic.emptyMap();
/*     */       
/*  98 */       if ((hideFlags & 0x4) != 0) {
/*  99 */         component = component.set("show_in_tooltip", dynamic.createBoolean(false));
/*     */       }
/* 101 */       itemStack.setComponent("minecraft:unbreakable", component);
/*     */     } 
/*     */     
/* 104 */     fixEnchantments(itemStack, dynamic, "Enchantments", "minecraft:enchantments", ((hideFlags & true) != 0));
/* 105 */     if (itemStack.is("minecraft:enchanted_book"))
/*     */     {
/* 107 */       fixEnchantments(itemStack, dynamic, "StoredEnchantments", "minecraft:stored_enchantments", ((hideFlags & 0x20) != 0));
/*     */     }
/*     */     
/* 110 */     itemStack.fixSubTag("display", false, display -> fixDisplay(itemStack, display, hideFlags));
/*     */     
/* 112 */     fixAdventureModeChecks(itemStack, dynamic, hideFlags);
/* 113 */     fixAttributeModifiers(itemStack, dynamic, hideFlags);
/*     */     
/* 115 */     Optional<? extends Dynamic<?>> trim = itemStack.removeTag("Trim").result();
/* 116 */     if (trim.isPresent()) {
/* 117 */       Dynamic<?> fixedTrim = (Dynamic)trim.get();
/* 118 */       if ((hideFlags & 0x80) != 0) {
/* 119 */         fixedTrim = fixedTrim.set("show_in_tooltip", fixedTrim.createBoolean(false));
/*     */       }
/* 121 */       itemStack.setComponent("minecraft:trim", fixedTrim);
/*     */     } 
/*     */     
/* 124 */     if ((hideFlags & 0x20) != 0) {
/* 125 */       itemStack.setComponent("minecraft:hide_additional_tooltip", dynamic.emptyMap());
/*     */     }
/*     */     
/* 128 */     if (itemStack.is("minecraft:crossbow")) {
/*     */       
/* 130 */       itemStack.removeTag("Charged");
/* 131 */       itemStack.moveTagToComponent("ChargedProjectiles", "minecraft:charged_projectiles", dynamic.createList(Stream.empty()));
/*     */     } 
/* 133 */     if (itemStack.is("minecraft:bundle")) {
/* 134 */       itemStack.moveTagToComponent("Items", "minecraft:bundle_contents", dynamic.createList(Stream.empty()));
/*     */     }
/* 136 */     if (itemStack.is("minecraft:filled_map")) {
/* 137 */       itemStack.moveTagToComponent("map", "minecraft:map_id");
/*     */ 
/*     */       
/* 140 */       Map<? extends Dynamic<?>, ? extends Dynamic<?>> decorations = (Map)itemStack.removeTag("Decorations").asStream().map(ItemStackComponentizationFix::fixMapDecoration).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (first, second) -> 
/*     */             
/* 142 */             first));
/*     */       
/* 144 */       if (!decorations.isEmpty()) {
/* 145 */         itemStack.setComponent("minecraft:map_decorations", dynamic.createMap(decorations));
/*     */       }
/*     */     } 
/* 148 */     if (itemStack.is(POTION_HOLDER_IDS)) {
/* 149 */       fixPotionContents(itemStack, dynamic);
/*     */     }
/* 151 */     if (itemStack.is("minecraft:writable_book")) {
/* 152 */       fixWritableBook(itemStack, dynamic);
/*     */     }
/* 154 */     if (itemStack.is("minecraft:written_book")) {
/* 155 */       fixWrittenBook(itemStack, dynamic);
/*     */     }
/* 157 */     if (itemStack.is("minecraft:suspicious_stew")) {
/* 158 */       itemStack.moveTagToComponent("effects", "minecraft:suspicious_stew_effects");
/*     */     }
/* 160 */     if (itemStack.is("minecraft:debug_stick")) {
/* 161 */       itemStack.moveTagToComponent("DebugProperty", "minecraft:debug_stick_state");
/*     */     }
/* 163 */     if (itemStack.is(BUCKETED_MOB_IDS)) {
/* 164 */       fixBucketedMobData(itemStack, dynamic);
/*     */     }
/* 166 */     if (itemStack.is("minecraft:goat_horn")) {
/* 167 */       itemStack.moveTagToComponent("instrument", "minecraft:instrument");
/*     */     }
/* 169 */     if (itemStack.is("minecraft:knowledge_book")) {
/* 170 */       itemStack.moveTagToComponent("Recipes", "minecraft:recipes");
/*     */     }
/* 172 */     if (itemStack.is("minecraft:compass")) {
/* 173 */       fixLodestoneTracker(itemStack, dynamic);
/*     */     }
/* 175 */     if (itemStack.is("minecraft:firework_rocket")) {
/* 176 */       fixFireworkRocket(itemStack);
/*     */     }
/* 178 */     if (itemStack.is("minecraft:firework_star")) {
/* 179 */       fixFireworkStar(itemStack);
/*     */     }
/* 181 */     if (itemStack.is("minecraft:player_head")) {
/* 182 */       itemStack.removeTag("SkullOwner").result().ifPresent(skullOwner -> 
/* 183 */           itemStack.setComponent("minecraft:profile", fixProfile(skullOwner)));
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   private static Dynamic<?> fixBlockStateTag(Dynamic<?> blockStateTag) { Objects.requireNonNull(blockStateTag); return (Dynamic)DataFixUtils.orElse(blockStateTag.asMapOpt().result().map(entries -> (Map)entries.collect(Collectors.toMap(Pair::getFirst, ()))).map(blockStateTag::createMap), blockStateTag); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixDisplay(ItemStackData itemStack, Dynamic<?> display, int hideFlags) {
/* 213 */     display.get("Name").result().filter(LegacyComponentDataFixUtils::isStrictlyValidJson).ifPresent(name -> 
/* 214 */         itemStack.setComponent("minecraft:custom_name", name));
/*     */ 
/*     */     
/* 217 */     OptionalDynamic<?> lore = display.get("Lore");
/* 218 */     if (lore.result().isPresent()) {
/* 219 */       itemStack.setComponent("minecraft:lore", display.createList(display.get("Lore").asStream()
/* 220 */             .filter(LegacyComponentDataFixUtils::isStrictlyValidJson)));
/*     */     }
/*     */     
/* 223 */     Optional<Integer> color = display.get("color").asNumber().result().map(Number::intValue);
/* 224 */     boolean hideDye = ((hideFlags & 0x40) != 0);
/* 225 */     if (color.isPresent() || hideDye) {
/*     */       
/* 227 */       Dynamic<?> dyedColor = display.emptyMap().set("rgb", display.createInt(((Integer)color.orElse(Integer.valueOf(10511680))).intValue()));
/* 228 */       if (hideDye) {
/* 229 */         dyedColor = dyedColor.set("show_in_tooltip", display.createBoolean(false));
/*     */       }
/* 231 */       itemStack.setComponent("minecraft:dyed_color", dyedColor);
/*     */     } 
/*     */     
/* 234 */     Optional<String> locName = display.get("LocName").asString().result();
/* 235 */     if (locName.isPresent()) {
/* 236 */       itemStack.setComponent("minecraft:item_name", LegacyComponentDataFixUtils.createTranslatableComponent(display.getOps(), (String)locName.get()));
/*     */     }
/*     */     
/* 239 */     if (itemStack.is("minecraft:filled_map")) {
/* 240 */       itemStack.setComponent("minecraft:map_color", display.get("MapColor"));
/* 241 */       display = display.remove("MapColor");
/*     */     } 
/*     */     
/* 244 */     return display.remove("Name").remove("Lore").remove("color").remove("LocName"); } private static <T> Dynamic<T> fixBlockEntityTag(ItemStackData itemStack, Dynamic<T> blockEntity, String id) {
/*     */     Optional<Dynamic<T>> item;
/*     */     List<Dynamic<T>> items;
/*     */     Optional<Number> base;
/* 248 */     itemStack.setComponent("minecraft:lock", blockEntity.get("Lock"));
/* 249 */     blockEntity = blockEntity.remove("Lock");
/*     */     
/* 251 */     Optional<Dynamic<T>> lootTable = blockEntity.get("LootTable").result();
/* 252 */     if (lootTable.isPresent()) {
/* 253 */       Dynamic<T> containerLoot = blockEntity.emptyMap().set("loot_table", (Dynamic)lootTable.get());
/* 254 */       long seed = blockEntity.get("LootTableSeed").asLong(0L);
/* 255 */       if (seed != 0L) {
/* 256 */         containerLoot = containerLoot.set("seed", blockEntity.createLong(seed));
/*     */       }
/* 258 */       itemStack.setComponent("minecraft:container_loot", containerLoot);
/* 259 */       blockEntity = blockEntity.remove("LootTable").remove("LootTableSeed");
/*     */     } 
/*     */     
/* 262 */     switch (id) {
/*     */       case "minecraft:skull":
/* 264 */         itemStack.setComponent("minecraft:note_block_sound", blockEntity.get("note_block_sound"));
/*     */ 
/*     */       
/*     */       case "minecraft:decorated_pot":
/* 268 */         itemStack.setComponent("minecraft:pot_decorations", blockEntity.get("sherds"));
/* 269 */         item = blockEntity.get("item").result();
/* 270 */         if (item.isPresent()) {
/* 271 */           itemStack.setComponent("minecraft:container", blockEntity.createList(Stream.of(blockEntity
/* 272 */                   .emptyMap()
/* 273 */                   .set("slot", blockEntity.createInt(0))
/* 274 */                   .set("item", (Dynamic)item.get()))));
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       case "minecraft:banner":
/* 280 */         itemStack.setComponent("minecraft:banner_patterns", blockEntity.get("patterns"));
/* 281 */         base = blockEntity.get("Base").asNumber().result();
/* 282 */         if (base.isPresent()) {
/* 283 */           itemStack.setComponent("minecraft:base_color", blockEntity.createString(ExtraDataFixUtils.dyeColorIdToName(((Number)base.get()).intValue())));
/*     */         }
/*     */ 
/*     */       
/*     */       case "minecraft:shulker_box":
/*     */       case "minecraft:chest":
/*     */       case "minecraft:trapped_chest":
/*     */       case "minecraft:furnace":
/*     */       case "minecraft:ender_chest":
/*     */       case "minecraft:dispenser":
/*     */       case "minecraft:dropper":
/*     */       case "minecraft:brewing_stand":
/*     */       case "minecraft:hopper":
/*     */       case "minecraft:barrel":
/*     */       case "minecraft:smoker":
/*     */       case "minecraft:blast_furnace":
/*     */       case "minecraft:campfire":
/*     */       case "minecraft:chiseled_bookshelf":
/*     */       case "minecraft:crafter":
/* 302 */         items = blockEntity.get("Items").asList(dynamic -> 
/* 303 */             dynamic.emptyMap()
/* 304 */             .set("slot", dynamic.createInt(dynamic.get("Slot").asByte((byte)0) & 0xFF))
/* 305 */             .set("item", dynamic.remove("Slot")));
/*     */         
/* 307 */         if (!items.isEmpty()) {
/* 308 */           itemStack.setComponent("minecraft:container", blockEntity.createList(items.stream()));
/*     */         }
/*     */ 
/*     */       
/*     */       case "minecraft:beehive":
/* 313 */         itemStack.setComponent("minecraft:bees", blockEntity.get("bees"));
/*     */     } 
/*     */     
/* 316 */     return blockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fixEnchantments(ItemStackData itemStack, Dynamic<?> dynamic, String key, String componentType, boolean hideInTooltip) {
/* 321 */     OptionalDynamic<?> rawEnchantments = itemStack.removeTag(key);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 326 */     List<Pair<String, Integer>> enchantments = rawEnchantments.asList(Function.identity()).stream().flatMap(enchantment -> parseEnchantment(enchantment).stream()).filter(enchantment -> (((Integer)enchantment.getSecond()).intValue() > 0)).toList();
/*     */     
/* 328 */     if (!enchantments.isEmpty() || hideInTooltip) {
/* 329 */       Dynamic<?> component = dynamic.emptyMap();
/*     */       
/* 331 */       Dynamic<?> levels = dynamic.emptyMap();
/* 332 */       for (Pair<String, Integer> enchantment : enchantments) {
/* 333 */         levels = levels.set((String)enchantment.getFirst(), dynamic.createInt(((Integer)enchantment.getSecond()).intValue()));
/*     */       }
/* 335 */       component = component.set("levels", levels);
/*     */       
/* 337 */       if (hideInTooltip) {
/* 338 */         component = component.set("show_in_tooltip", dynamic.createBoolean(false));
/*     */       }
/* 340 */       itemStack.setComponent(componentType, component);
/*     */     } 
/*     */ 
/*     */     
/* 344 */     if (rawEnchantments.result().isPresent() && enchantments.isEmpty()) {
/* 345 */       itemStack.setComponent("minecraft:enchantment_glint_override", dynamic.createBoolean(true));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 350 */   private static Optional<Pair<String, Integer>> parseEnchantment(Dynamic<?> entry) { return entry.get("id").asString().apply2stable((id, level) -> 
/* 351 */         Pair.of(id, Integer.valueOf(Mth.clamp(level.intValue(), 0, 255))), entry
/* 352 */         .get("lvl").asNumber())
/* 353 */       .result(); }
/*     */ 
/*     */   
/*     */   private static void fixAdventureModeChecks(ItemStackData itemStack, Dynamic<?> dynamic, int hideFlags) {
/* 357 */     fixBlockStatePredicates(itemStack, dynamic, "CanDestroy", "minecraft:can_break", ((hideFlags & 0x8) != 0));
/* 358 */     fixBlockStatePredicates(itemStack, dynamic, "CanPlaceOn", "minecraft:can_place_on", ((hideFlags & 0x10) != 0));
/*     */   }
/*     */   
/*     */   private static void fixBlockStatePredicates(ItemStackData itemStack, Dynamic<?> dynamic, String tag, String componentId, boolean hideInTooltip) {
/* 362 */     Optional<? extends Dynamic<?>> oldPredicate = itemStack.removeTag(tag).result();
/* 363 */     if (oldPredicate.isEmpty()) {
/*     */       return;
/*     */     }
/* 366 */     Dynamic<?> component = dynamic.emptyMap().set("predicates", dynamic.createList(((Dynamic)oldPredicate.get()).asStream().map(value -> (Dynamic)DataFixUtils.orElse(value
/* 367 */               .asString().map(()).result(), value))));
/*     */ 
/*     */     
/* 370 */     if (hideInTooltip) {
/* 371 */       component = component.set("show_in_tooltip", dynamic.createBoolean(false));
/*     */     }
/* 373 */     itemStack.setComponent(componentId, component);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixBlockStatePredicate(Dynamic<?> dynamic, String string) {
/* 377 */     int startProperties = string.indexOf('[');
/* 378 */     int startNbt = string.indexOf('{');
/*     */     
/* 380 */     int blockNameEnd = string.length();
/* 381 */     if (startProperties != -1) {
/* 382 */       blockNameEnd = startProperties;
/*     */     }
/* 384 */     if (startNbt != -1) {
/* 385 */       blockNameEnd = Math.min(blockNameEnd, startNbt);
/*     */     }
/*     */     
/* 388 */     String blockOrTagName = string.substring(0, blockNameEnd);
/*     */     
/* 390 */     Dynamic<?> predicate = dynamic.emptyMap().set("blocks", dynamic.createString(blockOrTagName.trim()));
/*     */     
/* 392 */     int endProperties = string.indexOf(']');
/* 393 */     if (startProperties != -1 && endProperties != -1) {
/* 394 */       Dynamic<?> properties = dynamic.emptyMap();
/* 395 */       Iterable<String> flatProperties = PROPERTY_SPLITTER.split(string.substring(startProperties + 1, endProperties));
/* 396 */       for (String property : flatProperties) {
/* 397 */         int assignment = property.indexOf('=');
/* 398 */         if (assignment == -1) {
/*     */           continue;
/*     */         }
/* 401 */         String key = property.substring(0, assignment).trim();
/* 402 */         String value = property.substring(assignment + 1).trim();
/* 403 */         properties = properties.set(key, dynamic.createString(value));
/*     */       } 
/* 405 */       predicate = predicate.set("state", properties);
/*     */     } 
/*     */     
/* 408 */     int endNbt = string.indexOf('}');
/* 409 */     if (startNbt != -1 && endNbt != -1) {
/* 410 */       predicate = predicate.set("nbt", dynamic.createString(string.substring(startNbt, endNbt + 1)));
/*     */     }
/*     */     
/* 413 */     return predicate;
/*     */   }
/*     */   
/*     */   private static void fixAttributeModifiers(ItemStackData itemStack, Dynamic<?> dynamic, int hideFlags) {
/* 417 */     OptionalDynamic<?> attributeModifiersField = itemStack.removeTag("AttributeModifiers");
/* 418 */     if (attributeModifiersField.result().isEmpty()) {
/*     */       return;
/*     */     }
/* 421 */     boolean hideInTooltip = ((hideFlags & 0x2) != 0);
/* 422 */     List<? extends Dynamic<?>> attributeModifiers = attributeModifiersField.asList(ItemStackComponentizationFix::fixAttributeModifier);
/*     */     
/* 424 */     Dynamic<?> component = dynamic.emptyMap().set("modifiers", dynamic.createList(attributeModifiers.stream()));
/* 425 */     if (hideInTooltip) {
/* 426 */       component = component.set("show_in_tooltip", dynamic.createBoolean(false));
/*     */     }
/* 428 */     itemStack.setComponent("minecraft:attribute_modifiers", component);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixAttributeModifier(Dynamic<?> input) {
/* 436 */     result = input.emptyMap().set("name", input.createString("")).set("amount", input.createDouble(0.0D)).set("operation", input.createString("add_value"));
/* 437 */     result = Dynamic.copyField(input, "AttributeName", result, "type");
/* 438 */     result = Dynamic.copyField(input, "Slot", result, "slot");
/* 439 */     result = Dynamic.copyField(input, "UUID", result, "uuid");
/* 440 */     result = Dynamic.copyField(input, "Name", result, "name");
/* 441 */     result = Dynamic.copyField(input, "Amount", result, "amount");
/* 442 */     return Dynamic.copyAndFixField(input, "Operation", result, "operation", operation -> {
/* 443 */           switch (operation.asInt(0)) { default: case 1: case 2: break; }  return operation.createString(
/*     */ 
/*     */               
/* 446 */               "add_multiplied_total");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Pair<Dynamic<?>, Dynamic<?>> fixMapDecoration(Dynamic<?> decoration) {
/* 453 */     Dynamic<?> id = (Dynamic)DataFixUtils.orElseGet(decoration.get("id").result(), () -> decoration.createString(""));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     Dynamic<?> value = decoration.emptyMap().set("type", decoration.createString(fixMapDecorationType(decoration.get("type").asInt(0)))).set("x", decoration.createDouble(decoration.get("x").asDouble(0.0D))).set("z", decoration.createDouble(decoration.get("z").asDouble(0.0D))).set("rotation", decoration.createFloat((float)decoration.get("rot").asDouble(0.0D)));
/* 459 */     return Pair.of(id, value);
/*     */   }
/*     */   
/*     */   private static String fixMapDecorationType(int id) {
/* 463 */     switch (id) { default: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 20: case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: break; }  return 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 497 */       "swamp_hut";
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fixPotionContents(ItemStackData itemStack, Dynamic<?> dynamic) {
/* 502 */     Dynamic<?> component = dynamic.emptyMap();
/* 503 */     Optional<String> potion = itemStack.removeTag("Potion").asString().result().filter(id -> !id.equals("minecraft:empty"));
/* 504 */     if (potion.isPresent()) {
/* 505 */       component = component.set("potion", dynamic.createString((String)potion.get()));
/*     */     }
/* 507 */     component = itemStack.moveTagInto("CustomPotionColor", component, "custom_color");
/* 508 */     component = itemStack.moveTagInto("custom_potion_effects", component, "custom_effects");
/* 509 */     if (!component.equals(dynamic.emptyMap())) {
/* 510 */       itemStack.setComponent("minecraft:potion_contents", component);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void fixWritableBook(ItemStackData itemStack, Dynamic<?> dynamic) {
/* 515 */     Dynamic<?> pages = fixBookPages(itemStack, dynamic);
/* 516 */     if (pages != null) {
/* 517 */       itemStack.setComponent("minecraft:writable_book_content", dynamic.emptyMap().set("pages", pages));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void fixWrittenBook(ItemStackData itemStack, Dynamic<?> dynamic) {
/* 522 */     Dynamic<?> pages = fixBookPages(itemStack, dynamic);
/* 523 */     String title = itemStack.removeTag("title").asString("");
/* 524 */     Optional<String> filteredTitle = itemStack.removeTag("filtered_title").asString().result();
/* 525 */     Dynamic<?> component = dynamic.emptyMap();
/* 526 */     component = component.set("title", createFilteredText(dynamic, title, filteredTitle));
/* 527 */     component = itemStack.moveTagInto("author", component, "author");
/* 528 */     component = itemStack.moveTagInto("resolved", component, "resolved");
/* 529 */     component = itemStack.moveTagInto("generation", component, "generation");
/* 530 */     if (pages != null) {
/* 531 */       component = component.set("pages", pages);
/*     */     }
/* 533 */     itemStack.setComponent("minecraft:written_book_content", component);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixBookPages(ItemStackData itemStack, Dynamic<?> dynamic) {
/* 537 */     List<String> pages = itemStack.removeTag("pages").asList(page -> page.asString(""));
/* 538 */     Map<String, String> filteredPages = itemStack.removeTag("filtered_pages").asMap(key -> key.asString("0"), page -> page.asString(""));
/* 539 */     if (pages.isEmpty()) {
/* 540 */       return null;
/*     */     }
/* 542 */     List<Dynamic<?>> fixedPages = new ArrayList<Dynamic<?>>(pages.size());
/* 543 */     for (int i = 0; i < pages.size(); i++) {
/* 544 */       String page = (String)pages.get(i);
/* 545 */       String filteredPage = (String)filteredPages.get(String.valueOf(i));
/* 546 */       fixedPages.add(createFilteredText(dynamic, page, Optional.ofNullable(filteredPage)));
/*     */     } 
/* 548 */     return dynamic.createList(fixedPages.stream());
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> createFilteredText(Dynamic<?> dynamic, String text, Optional<String> filtered) {
/* 553 */     Dynamic<?> fixedPage = dynamic.emptyMap().set("raw", dynamic.createString(text));
/* 554 */     if (filtered.isPresent()) {
/* 555 */       fixedPage = fixedPage.set("filtered", dynamic.createString((String)filtered.get()));
/*     */     }
/* 557 */     return fixedPage;
/*     */   }
/*     */   
/*     */   private static void fixBucketedMobData(ItemStackData itemStack, Dynamic<?> dynamic) {
/* 561 */     Dynamic<?> data = dynamic.emptyMap();
/* 562 */     for (String key : BUCKETED_MOB_TAGS) {
/* 563 */       data = itemStack.moveTagInto(key, data, key);
/*     */     }
/*     */     
/* 566 */     if (!data.equals(dynamic.emptyMap())) {
/* 567 */       itemStack.setComponent("minecraft:bucket_entity_data", data);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void fixLodestoneTracker(ItemStackData itemStack, Dynamic<?> dynamic) {
/* 572 */     Optional<? extends Dynamic<?>> lodestonePos = itemStack.removeTag("LodestonePos").result();
/* 573 */     Optional<? extends Dynamic<?>> lodestoneDimension = itemStack.removeTag("LodestoneDimension").result();
/* 574 */     if (lodestonePos.isEmpty() && lodestoneDimension.isEmpty()) {
/*     */       return;
/*     */     }
/* 577 */     boolean lodestoneTracked = itemStack.removeTag("LodestoneTracked").asBoolean(true);
/* 578 */     Dynamic<?> component = dynamic.emptyMap();
/* 579 */     if (lodestonePos.isPresent() && lodestoneDimension.isPresent()) {
/* 580 */       component = component.set("target", dynamic.emptyMap()
/* 581 */           .set("pos", (Dynamic)lodestonePos.get())
/* 582 */           .set("dimension", (Dynamic)lodestoneDimension.get()));
/*     */     }
/*     */     
/* 585 */     if (!lodestoneTracked) {
/* 586 */       component = component.set("tracked", dynamic.createBoolean(false));
/*     */     }
/* 588 */     itemStack.setComponent("minecraft:lodestone_tracker", component);
/*     */   }
/*     */   
/*     */   private static void fixFireworkStar(ItemStackData itemStack) {
/* 592 */     itemStack.fixSubTag("Explosion", true, explosion -> {
/* 593 */           itemStack.setComponent("minecraft:firework_explosion", fixFireworkExplosion(explosion));
/* 594 */           return explosion.remove("Type").remove("Colors").remove("FadeColors").remove("Trail").remove("Flicker");
/*     */         });
/*     */   }
/*     */   
/*     */   private static void fixFireworkRocket(ItemStackData itemStack) {
/* 599 */     itemStack.fixSubTag("Fireworks", true, fireworks -> {
/* 600 */           Stream<? extends Dynamic<?>> explosions = fireworks.get("Explosions").asStream().map(ItemStackComponentizationFix::fixFireworkExplosion);
/* 601 */           int flight = fireworks.get("Flight").asInt(0);
/* 602 */           itemStack.setComponent("minecraft:fireworks", fireworks.emptyMap()
/* 603 */               .set("explosions", fireworks.createList(explosions))
/* 604 */               .set("flight_duration", fireworks.createByte((byte)flight)));
/*     */           
/* 606 */           return fireworks.remove("Explosions").remove("Flight");
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixFireworkExplosion(Dynamic<?> explosion) {
/* 611 */     switch (explosion.get("Type").asInt(0)) { default: 
/*     */       case 1: 
/*     */       case 2: 
/*     */       case 3:
/*     */       
/*     */       case 4:
/* 617 */         break; }  explosion = explosion.set("shape", explosion.createString("burst")).remove("Type");
/* 618 */     explosion = explosion.renameField("Colors", "colors");
/* 619 */     explosion = explosion.renameField("FadeColors", "fade_colors");
/* 620 */     explosion = explosion.renameField("Trail", "has_trail");
/* 621 */     return explosion.renameField("Flicker", "has_twinkle");
/*     */   }
/*     */ 
/*     */   
/*     */   public static Dynamic<?> fixProfile(Dynamic<?> dynamic) {
/* 626 */     Optional<String> simpleName = dynamic.asString().result();
/* 627 */     if (simpleName.isPresent()) {
/* 628 */       if (isValidPlayerName((String)simpleName.get())) {
/* 629 */         return dynamic.emptyMap().set("name", dynamic.createString((String)simpleName.get()));
/*     */       }
/* 631 */       return dynamic.emptyMap();
/*     */     } 
/*     */ 
/*     */     
/* 635 */     String name = dynamic.get("Name").asString("");
/* 636 */     Optional<? extends Dynamic<?>> id = dynamic.get("Id").result();
/* 637 */     Dynamic<?> properties = fixProfileProperties(dynamic.get("Properties"));
/*     */     
/* 639 */     Dynamic<?> profile = dynamic.emptyMap();
/* 640 */     if (isValidPlayerName(name)) {
/* 641 */       profile = profile.set("name", dynamic.createString(name));
/*     */     }
/* 643 */     if (id.isPresent()) {
/* 644 */       profile = profile.set("id", (Dynamic)id.get());
/*     */     }
/* 646 */     if (properties != null) {
/* 647 */       profile = profile.set("properties", properties);
/*     */     }
/*     */     
/* 650 */     return profile;
/*     */   }
/*     */   
/*     */   private static boolean isValidPlayerName(String name) {
/* 654 */     if (name.length() > 16) {
/* 655 */       return false;
/*     */     }
/* 657 */     return name.chars().filter(c -> (c <= 32 || c >= 127)).findAny().isEmpty();
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixProfileProperties(OptionalDynamic<?> dynamic) {
/* 661 */     Map<String, List<Pair<String, Optional<String>>>> properties = dynamic.asMap(key -> 
/* 662 */         key.asString(""), list -> 
/* 663 */         list.asList(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 669 */     if (properties.isEmpty()) {
/* 670 */       return null;
/*     */     }
/* 672 */     return dynamic.createList(properties.entrySet().stream()
/* 673 */         .flatMap(entry -> ((List)entry.getValue()).stream()
/* 674 */           .map(())));
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
/*     */   protected TypeRewriteRule makeRule() {
/* 689 */     return writeFixAndRead("ItemStack componentization", getInputSchema().getType(References.ITEM_STACK), getOutputSchema().getType(References.ITEM_STACK), dynamic -> {
/* 690 */           Optional<? extends Dynamic<?>> fixedItemStack = ItemStackData.read(dynamic).map(());
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 695 */           return (Dynamic)DataFixUtils.orElse(fixedItemStack, dynamic);
/*     */         });
/*     */   }
/*     */   
/*     */   private static class ItemStackData {
/*     */     private final String item;
/*     */     private final int count;
/*     */     private Dynamic<?> components;
/*     */     private final Dynamic<?> remainder;
/*     */     private Dynamic<?> tag;
/*     */     
/*     */     private ItemStackData(String item, int count, Dynamic<?> remainder) {
/* 707 */       this.item = NamespacedSchema.ensureNamespaced(item);
/* 708 */       this.count = count;
/* 709 */       this.components = remainder.emptyMap();
/* 710 */       this.tag = remainder.get("tag").orElseEmptyMap();
/*     */       
/* 712 */       this.remainder = remainder.remove("tag");
/*     */     }
/*     */ 
/*     */     
/* 716 */     public static Optional<ItemStackData> read(Dynamic<?> dynamic) { return dynamic.get("id").asString().apply2stable((item, count) -> 
/* 717 */           new ItemStackData(item, count.intValue(), dynamic.remove("id").remove("Count")), dynamic
/* 718 */           .get("Count").asNumber())
/* 719 */         .result(); }
/*     */ 
/*     */     
/*     */     public OptionalDynamic<?> removeTag(String key) {
/* 723 */       OptionalDynamic<?> value = this.tag.get(key);
/* 724 */       this.tag = this.tag.remove(key);
/* 725 */       return value;
/*     */     }
/*     */ 
/*     */     
/* 729 */     public void setComponent(String type, Dynamic<?> value) { this.components = this.components.set(type, value); }
/*     */ 
/*     */     
/*     */     public void setComponent(String type, OptionalDynamic<?> optionalValue) {
/* 733 */       optionalValue.result().ifPresent(value -> 
/* 734 */           this.components = this.components.set(type, value));
/*     */     }
/*     */ 
/*     */     
/*     */     public Dynamic<?> moveTagInto(String fromKey, Dynamic<?> target, String toKey) {
/* 739 */       Optional<? extends Dynamic<?>> value = removeTag(fromKey).result();
/* 740 */       if (value.isPresent()) {
/* 741 */         return target.set(toKey, (Dynamic)value.get());
/*     */       }
/* 743 */       return target;
/*     */     }
/*     */     
/*     */     public void moveTagToComponent(String key, String type, Dynamic<?> defaultValue) {
/* 747 */       Optional<? extends Dynamic<?>> value = removeTag(key).result();
/* 748 */       if (value.isPresent() && !((Dynamic)value.get()).equals(defaultValue)) {
/* 749 */         setComponent(type, (Dynamic)value.get());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 754 */     public void moveTagToComponent(String key, String type) { removeTag(key).result().ifPresent(value -> setComponent(type, value)); }
/*     */ 
/*     */     
/*     */     public void fixSubTag(String key, boolean dontFixWhenFieldIsMissing, UnaryOperator<Dynamic<?>> function) {
/* 758 */       OptionalDynamic<?> value = this.tag.get(key);
/* 759 */       if (dontFixWhenFieldIsMissing && value.result().isEmpty()) {
/*     */         return;
/*     */       }
/* 762 */       Dynamic<?> map = value.orElseEmptyMap();
/* 763 */       map = (Dynamic)function.apply(map);
/* 764 */       if (map.equals(map.emptyMap())) {
/* 765 */         this.tag = this.tag.remove(key);
/*     */       } else {
/* 767 */         this.tag = this.tag.set(key, map);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Dynamic<?> write() {
/* 774 */       Dynamic<?> result = this.tag.emptyMap().set("id", this.tag.createString(this.item)).set("count", this.tag.createInt(this.count));
/* 775 */       if (!this.tag.equals(this.tag.emptyMap())) {
/* 776 */         this.components = this.components.set("minecraft:custom_data", this.tag);
/*     */       }
/* 778 */       if (!this.components.equals(this.tag.emptyMap())) {
/* 779 */         result = result.set("components", this.components);
/*     */       }
/* 781 */       return mergeRemainder(result, this.remainder);
/*     */     }
/*     */     
/*     */     private static <T> Dynamic<T> mergeRemainder(Dynamic<T> itemStack, Dynamic<?> remainder) {
/* 785 */       DynamicOps<T> ops = itemStack.getOps();
/* 786 */       return (Dynamic)ops.getMap(itemStack.getValue())
/* 787 */         .flatMap(itemStackMap -> ops.mergeToMap(remainder.convert(ops).getValue(), itemStackMap))
/* 788 */         .map(merged -> new Dynamic(ops, merged))
/* 789 */         .result().orElse(itemStack);
/*     */     }
/*     */ 
/*     */     
/* 793 */     public boolean is(String id) { return this.item.equals(id); }
/*     */ 
/*     */ 
/*     */     
/* 797 */     public boolean is(Set<String> ids) { return ids.contains(this.item); }
/*     */ 
/*     */ 
/*     */     
/* 801 */     public boolean hasComponent(String id) { return this.components.get(id).result().isPresent(); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackComponentizationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
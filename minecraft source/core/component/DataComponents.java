/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.EncoderCache;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.equine.Variant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.fish.Salmon;
/*     */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.AdventureModePredicate;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.EitherHolder;
/*     */ import net.minecraft.world.item.JukeboxPlayable;
/*     */ import net.minecraft.world.item.Rarity;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Bees;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.component.BlocksAttacks;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.item.component.CustomModelData;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.DeathProtection;
/*     */ import net.minecraft.world.item.component.DebugStickState;
/*     */ import net.minecraft.world.item.component.DyedItemColor;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.item.component.InstrumentComponent;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.component.ItemLore;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.LodestoneTracker;
/*     */ import net.minecraft.world.item.component.MapDecorations;
/*     */ import net.minecraft.world.item.component.MapItemColor;
/*     */ import net.minecraft.world.item.component.MapPostProcessing;
/*     */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.item.component.SeededContainerLoot;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataComponents
/*     */ {
/* 106 */   static final EncoderCache ENCODER_CACHE = new EncoderCache(512);
/*     */ 
/*     */ 
/*     */   
/* 110 */   public static final DataComponentType<CustomData> CUSTOM_DATA = register("custom_data", b -> b
/* 111 */       .persistent(CustomData.CODEC));
/* 112 */   public static final DataComponentType<Integer> MAX_STACK_SIZE = register("max_stack_size", b -> b
/* 113 */       .persistent(ExtraCodecs.intRange(1, 99))
/* 114 */       .networkSynchronized(ByteBufCodecs.VAR_INT));
/* 115 */   public static final DataComponentType<Integer> MAX_DAMAGE = register("max_damage", b -> b
/* 116 */       .persistent(ExtraCodecs.POSITIVE_INT)
/* 117 */       .networkSynchronized(ByteBufCodecs.VAR_INT));
/* 118 */   public static final DataComponentType<Integer> DAMAGE = register("damage", b -> b
/* 119 */       .persistent(ExtraCodecs.NON_NEGATIVE_INT).ignoreSwapAnimation()
/* 120 */       .networkSynchronized(ByteBufCodecs.VAR_INT));
/* 121 */   public static final DataComponentType<Unit> UNBREAKABLE = register("unbreakable", b -> b
/* 122 */       .persistent(Unit.CODEC)
/* 123 */       .networkSynchronized(Unit.STREAM_CODEC));
/* 124 */   public static final DataComponentType<UseEffects> USE_EFFECTS = register("use_effects", b -> b
/* 125 */       .persistent(UseEffects.CODEC)
/* 126 */       .networkSynchronized(UseEffects.STREAM_CODEC));
/* 127 */   public static final DataComponentType<Component> CUSTOM_NAME = register("custom_name", b -> b
/* 128 */       .persistent(ComponentSerialization.CODEC)
/* 129 */       .networkSynchronized(ComponentSerialization.STREAM_CODEC)
/* 130 */       .cacheEncoding());
/* 131 */   public static final DataComponentType<Float> MINIMUM_ATTACK_CHARGE = register("minimum_attack_charge", b -> b
/* 132 */       .persistent(ExtraCodecs.floatRange(0.0F, 1.0F))
/* 133 */       .networkSynchronized(ByteBufCodecs.FLOAT));
/* 134 */   public static final DataComponentType<EitherHolder<DamageType>> DAMAGE_TYPE = register("damage_type", b -> b
/* 135 */       .persistent(EitherHolder.codec(Registries.DAMAGE_TYPE, DamageType.CODEC))
/* 136 */       .networkSynchronized(EitherHolder.streamCodec(Registries.DAMAGE_TYPE, DamageType.STREAM_CODEC)));
/*     */   
/* 138 */   public static final DataComponentType<Component> ITEM_NAME = register("item_name", b -> b
/* 139 */       .persistent(ComponentSerialization.CODEC)
/* 140 */       .networkSynchronized(ComponentSerialization.STREAM_CODEC)
/* 141 */       .cacheEncoding());
/* 142 */   public static final DataComponentType<Identifier> ITEM_MODEL = register("item_model", b -> b
/* 143 */       .persistent(Identifier.CODEC)
/* 144 */       .networkSynchronized(Identifier.STREAM_CODEC)
/* 145 */       .cacheEncoding());
/* 146 */   public static final DataComponentType<ItemLore> LORE = register("lore", b -> b
/* 147 */       .persistent(ItemLore.CODEC)
/* 148 */       .networkSynchronized(ItemLore.STREAM_CODEC)
/* 149 */       .cacheEncoding());
/* 150 */   public static final DataComponentType<Rarity> RARITY = register("rarity", b -> b
/* 151 */       .persistent(Rarity.CODEC)
/* 152 */       .networkSynchronized(Rarity.STREAM_CODEC));
/* 153 */   public static final DataComponentType<ItemEnchantments> ENCHANTMENTS = register("enchantments", b -> b
/* 154 */       .persistent(ItemEnchantments.CODEC)
/* 155 */       .networkSynchronized(ItemEnchantments.STREAM_CODEC)
/* 156 */       .cacheEncoding());
/* 157 */   public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON = register("can_place_on", b -> b
/* 158 */       .persistent(AdventureModePredicate.CODEC)
/* 159 */       .networkSynchronized(AdventureModePredicate.STREAM_CODEC)
/* 160 */       .cacheEncoding());
/* 161 */   public static final DataComponentType<AdventureModePredicate> CAN_BREAK = register("can_break", b -> b
/* 162 */       .persistent(AdventureModePredicate.CODEC)
/* 163 */       .networkSynchronized(AdventureModePredicate.STREAM_CODEC)
/* 164 */       .cacheEncoding());
/* 165 */   public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = register("attribute_modifiers", b -> b
/* 166 */       .persistent(ItemAttributeModifiers.CODEC)
/* 167 */       .networkSynchronized(ItemAttributeModifiers.STREAM_CODEC)
/* 168 */       .cacheEncoding());
/* 169 */   public static final DataComponentType<CustomModelData> CUSTOM_MODEL_DATA = register("custom_model_data", b -> b
/* 170 */       .persistent(CustomModelData.CODEC)
/* 171 */       .networkSynchronized(CustomModelData.STREAM_CODEC));
/* 172 */   public static final DataComponentType<TooltipDisplay> TOOLTIP_DISPLAY = register("tooltip_display", b -> b
/* 173 */       .persistent(TooltipDisplay.CODEC)
/* 174 */       .networkSynchronized(TooltipDisplay.STREAM_CODEC)
/* 175 */       .cacheEncoding());
/* 176 */   public static final DataComponentType<Integer> REPAIR_COST = register("repair_cost", b -> b
/* 177 */       .persistent(ExtraCodecs.NON_NEGATIVE_INT)
/* 178 */       .networkSynchronized(ByteBufCodecs.VAR_INT));
/* 179 */   public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK = register("creative_slot_lock", b -> b
/*     */       
/* 181 */       .networkSynchronized(Unit.STREAM_CODEC));
/* 182 */   public static final DataComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = register("enchantment_glint_override", b -> b
/* 183 */       .persistent(Codec.BOOL)
/* 184 */       .networkSynchronized(ByteBufCodecs.BOOL));
/* 185 */   public static final DataComponentType<Unit> INTANGIBLE_PROJECTILE = register("intangible_projectile", b -> b
/* 186 */       .persistent(Unit.CODEC));
/* 187 */   public static final DataComponentType<FoodProperties> FOOD = register("food", b -> b
/* 188 */       .persistent(FoodProperties.DIRECT_CODEC)
/* 189 */       .networkSynchronized(FoodProperties.DIRECT_STREAM_CODEC)
/* 190 */       .cacheEncoding());
/* 191 */   public static final DataComponentType<Consumable> CONSUMABLE = register("consumable", b -> b
/* 192 */       .persistent(Consumable.CODEC)
/* 193 */       .networkSynchronized(Consumable.STREAM_CODEC)
/* 194 */       .cacheEncoding());
/* 195 */   public static final DataComponentType<UseRemainder> USE_REMAINDER = register("use_remainder", b -> b
/* 196 */       .persistent(UseRemainder.CODEC)
/* 197 */       .networkSynchronized(UseRemainder.STREAM_CODEC)
/* 198 */       .cacheEncoding());
/* 199 */   public static final DataComponentType<UseCooldown> USE_COOLDOWN = register("use_cooldown", b -> b
/* 200 */       .persistent(UseCooldown.CODEC)
/* 201 */       .networkSynchronized(UseCooldown.STREAM_CODEC)
/* 202 */       .cacheEncoding());
/* 203 */   public static final DataComponentType<DamageResistant> DAMAGE_RESISTANT = register("damage_resistant", b -> b
/* 204 */       .persistent(DamageResistant.CODEC)
/* 205 */       .networkSynchronized(DamageResistant.STREAM_CODEC)
/* 206 */       .cacheEncoding());
/* 207 */   public static final DataComponentType<Tool> TOOL = register("tool", b -> b
/* 208 */       .persistent(Tool.CODEC)
/* 209 */       .networkSynchronized(Tool.STREAM_CODEC)
/* 210 */       .cacheEncoding());
/* 211 */   public static final DataComponentType<Weapon> WEAPON = register("weapon", b -> b
/* 212 */       .persistent(Weapon.CODEC)
/* 213 */       .networkSynchronized(Weapon.STREAM_CODEC)
/* 214 */       .cacheEncoding());
/* 215 */   public static final DataComponentType<AttackRange> ATTACK_RANGE = register("attack_range", b -> b
/* 216 */       .persistent(AttackRange.CODEC)
/* 217 */       .networkSynchronized(AttackRange.STREAM_CODEC)
/* 218 */       .cacheEncoding());
/* 219 */   public static final DataComponentType<Enchantable> ENCHANTABLE = register("enchantable", b -> b
/* 220 */       .persistent(Enchantable.CODEC)
/* 221 */       .networkSynchronized(Enchantable.STREAM_CODEC)
/* 222 */       .cacheEncoding());
/* 223 */   public static final DataComponentType<Equippable> EQUIPPABLE = register("equippable", b -> b
/* 224 */       .persistent(Equippable.CODEC)
/* 225 */       .networkSynchronized(Equippable.STREAM_CODEC)
/* 226 */       .cacheEncoding());
/* 227 */   public static final DataComponentType<Repairable> REPAIRABLE = register("repairable", b -> b
/* 228 */       .persistent(Repairable.CODEC)
/* 229 */       .networkSynchronized(Repairable.STREAM_CODEC)
/* 230 */       .cacheEncoding());
/* 231 */   public static final DataComponentType<Unit> GLIDER = register("glider", b -> b
/* 232 */       .persistent(Unit.CODEC)
/* 233 */       .networkSynchronized(Unit.STREAM_CODEC));
/* 234 */   public static final DataComponentType<Identifier> TOOLTIP_STYLE = register("tooltip_style", b -> b
/* 235 */       .persistent(Identifier.CODEC)
/* 236 */       .networkSynchronized(Identifier.STREAM_CODEC)
/* 237 */       .cacheEncoding());
/* 238 */   public static final DataComponentType<DeathProtection> DEATH_PROTECTION = register("death_protection", b -> b
/* 239 */       .persistent(DeathProtection.CODEC)
/* 240 */       .networkSynchronized(DeathProtection.STREAM_CODEC)
/* 241 */       .cacheEncoding());
/* 242 */   public static final DataComponentType<BlocksAttacks> BLOCKS_ATTACKS = register("blocks_attacks", b -> b
/* 243 */       .persistent(BlocksAttacks.CODEC)
/* 244 */       .networkSynchronized(BlocksAttacks.STREAM_CODEC)
/* 245 */       .cacheEncoding());
/* 246 */   public static final DataComponentType<PiercingWeapon> PIERCING_WEAPON = register("piercing_weapon", b -> b
/* 247 */       .persistent(PiercingWeapon.CODEC)
/* 248 */       .networkSynchronized(PiercingWeapon.STREAM_CODEC)
/* 249 */       .cacheEncoding());
/* 250 */   public static final DataComponentType<KineticWeapon> KINETIC_WEAPON = register("kinetic_weapon", b -> b
/* 251 */       .persistent(KineticWeapon.CODEC)
/* 252 */       .networkSynchronized(KineticWeapon.STREAM_CODEC)
/* 253 */       .cacheEncoding());
/* 254 */   public static final DataComponentType<SwingAnimation> SWING_ANIMATION = register("swing_animation", b -> b
/* 255 */       .persistent(SwingAnimation.CODEC)
/* 256 */       .networkSynchronized(SwingAnimation.STREAM_CODEC));
/*     */ 
/*     */ 
/*     */   
/* 260 */   public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS = register("stored_enchantments", b -> b
/* 261 */       .persistent(ItemEnchantments.CODEC)
/* 262 */       .networkSynchronized(ItemEnchantments.STREAM_CODEC)
/* 263 */       .cacheEncoding());
/* 264 */   public static final DataComponentType<DyedItemColor> DYED_COLOR = register("dyed_color", b -> b
/* 265 */       .persistent(DyedItemColor.CODEC)
/* 266 */       .networkSynchronized(DyedItemColor.STREAM_CODEC));
/* 267 */   public static final DataComponentType<MapItemColor> MAP_COLOR = register("map_color", b -> b
/* 268 */       .persistent(MapItemColor.CODEC)
/* 269 */       .networkSynchronized(MapItemColor.STREAM_CODEC));
/* 270 */   public static final DataComponentType<MapId> MAP_ID = register("map_id", b -> b
/* 271 */       .persistent(MapId.CODEC)
/* 272 */       .networkSynchronized(MapId.STREAM_CODEC));
/* 273 */   public static final DataComponentType<MapDecorations> MAP_DECORATIONS = register("map_decorations", b -> b
/* 274 */       .persistent(MapDecorations.CODEC)
/* 275 */       .cacheEncoding());
/* 276 */   public static final DataComponentType<MapPostProcessing> MAP_POST_PROCESSING = register("map_post_processing", b -> b
/* 277 */       .networkSynchronized(MapPostProcessing.STREAM_CODEC));
/* 278 */   public static final DataComponentType<ChargedProjectiles> CHARGED_PROJECTILES = register("charged_projectiles", b -> b
/* 279 */       .persistent(ChargedProjectiles.CODEC)
/* 280 */       .networkSynchronized(ChargedProjectiles.STREAM_CODEC)
/* 281 */       .cacheEncoding());
/* 282 */   public static final DataComponentType<BundleContents> BUNDLE_CONTENTS = register("bundle_contents", b -> b
/* 283 */       .persistent(BundleContents.CODEC)
/* 284 */       .networkSynchronized(BundleContents.STREAM_CODEC)
/* 285 */       .cacheEncoding());
/* 286 */   public static final DataComponentType<PotionContents> POTION_CONTENTS = register("potion_contents", b -> b
/* 287 */       .persistent(PotionContents.CODEC)
/* 288 */       .networkSynchronized(PotionContents.STREAM_CODEC)
/* 289 */       .cacheEncoding());
/* 290 */   public static final DataComponentType<Float> POTION_DURATION_SCALE = register("potion_duration_scale", b -> b
/* 291 */       .persistent(ExtraCodecs.NON_NEGATIVE_FLOAT)
/* 292 */       .networkSynchronized(ByteBufCodecs.FLOAT)
/* 293 */       .cacheEncoding());
/* 294 */   public static final DataComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = register("suspicious_stew_effects", b -> b
/* 295 */       .persistent(SuspiciousStewEffects.CODEC)
/* 296 */       .networkSynchronized(SuspiciousStewEffects.STREAM_CODEC)
/* 297 */       .cacheEncoding());
/* 298 */   public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = register("writable_book_content", b -> b
/* 299 */       .persistent(WritableBookContent.CODEC)
/* 300 */       .networkSynchronized(WritableBookContent.STREAM_CODEC)
/* 301 */       .cacheEncoding());
/* 302 */   public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = register("written_book_content", b -> b
/* 303 */       .persistent(WrittenBookContent.CODEC)
/* 304 */       .networkSynchronized(WrittenBookContent.STREAM_CODEC)
/* 305 */       .cacheEncoding());
/* 306 */   public static final DataComponentType<ArmorTrim> TRIM = register("trim", b -> b
/* 307 */       .persistent(ArmorTrim.CODEC)
/* 308 */       .networkSynchronized(ArmorTrim.STREAM_CODEC)
/* 309 */       .cacheEncoding());
/* 310 */   public static final DataComponentType<DebugStickState> DEBUG_STICK_STATE = register("debug_stick_state", b -> b
/* 311 */       .persistent(DebugStickState.CODEC)
/* 312 */       .cacheEncoding());
/* 313 */   public static final DataComponentType<TypedEntityData<EntityType<?>>> ENTITY_DATA = register("entity_data", b -> b
/* 314 */       .persistent(TypedEntityData.codec(EntityType.CODEC))
/* 315 */       .networkSynchronized(TypedEntityData.streamCodec(EntityType.STREAM_CODEC)));
/* 316 */   public static final DataComponentType<CustomData> BUCKET_ENTITY_DATA = register("bucket_entity_data", b -> b
/* 317 */       .persistent(CustomData.CODEC)
/* 318 */       .networkSynchronized(CustomData.STREAM_CODEC));
/* 319 */   public static final DataComponentType<TypedEntityData<BlockEntityType<?>>> BLOCK_ENTITY_DATA = register("block_entity_data", b -> b
/* 320 */       .persistent(TypedEntityData.codec(BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec()))
/* 321 */       .networkSynchronized(TypedEntityData.streamCodec(ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE))));
/* 322 */   public static final DataComponentType<InstrumentComponent> INSTRUMENT = register("instrument", b -> b
/* 323 */       .persistent(InstrumentComponent.CODEC)
/* 324 */       .networkSynchronized(InstrumentComponent.STREAM_CODEC)
/* 325 */       .cacheEncoding());
/* 326 */   public static final DataComponentType<ProvidesTrimMaterial> PROVIDES_TRIM_MATERIAL = register("provides_trim_material", b -> b
/* 327 */       .persistent(ProvidesTrimMaterial.CODEC)
/* 328 */       .networkSynchronized(ProvidesTrimMaterial.STREAM_CODEC)
/* 329 */       .cacheEncoding());
/* 330 */   public static final DataComponentType<OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER = register("ominous_bottle_amplifier", b -> b
/* 331 */       .persistent(OminousBottleAmplifier.CODEC)
/* 332 */       .networkSynchronized(OminousBottleAmplifier.STREAM_CODEC));
/* 333 */   public static final DataComponentType<JukeboxPlayable> JUKEBOX_PLAYABLE = register("jukebox_playable", b -> b
/* 334 */       .persistent(JukeboxPlayable.CODEC)
/* 335 */       .networkSynchronized(JukeboxPlayable.STREAM_CODEC));
/*     */   
/* 337 */   public static final DataComponentType<TagKey<BannerPattern>> PROVIDES_BANNER_PATTERNS = register("provides_banner_patterns", b -> b
/* 338 */       .persistent(TagKey.hashedCodec(Registries.BANNER_PATTERN))
/* 339 */       .networkSynchronized(TagKey.streamCodec(Registries.BANNER_PATTERN))
/* 340 */       .cacheEncoding());
/* 341 */   public static final DataComponentType<List<ResourceKey<Recipe<?>>>> RECIPES = register("recipes", b -> b
/* 342 */       .persistent(Recipe.KEY_CODEC.listOf())
/* 343 */       .cacheEncoding());
/* 344 */   public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER = register("lodestone_tracker", b -> b
/* 345 */       .persistent(LodestoneTracker.CODEC)
/* 346 */       .networkSynchronized(LodestoneTracker.STREAM_CODEC)
/* 347 */       .cacheEncoding());
/* 348 */   public static final DataComponentType<FireworkExplosion> FIREWORK_EXPLOSION = register("firework_explosion", b -> b
/* 349 */       .persistent(FireworkExplosion.CODEC)
/* 350 */       .networkSynchronized(FireworkExplosion.STREAM_CODEC)
/* 351 */       .cacheEncoding());
/* 352 */   public static final DataComponentType<Fireworks> FIREWORKS = register("fireworks", b -> b
/* 353 */       .persistent(Fireworks.CODEC)
/* 354 */       .networkSynchronized(Fireworks.STREAM_CODEC)
/* 355 */       .cacheEncoding());
/* 356 */   public static final DataComponentType<ResolvableProfile> PROFILE = register("profile", b -> b
/* 357 */       .persistent(ResolvableProfile.CODEC)
/* 358 */       .networkSynchronized(ResolvableProfile.STREAM_CODEC)
/* 359 */       .cacheEncoding());
/* 360 */   public static final DataComponentType<Identifier> NOTE_BLOCK_SOUND = register("note_block_sound", b -> b
/* 361 */       .persistent(Identifier.CODEC)
/* 362 */       .networkSynchronized(Identifier.STREAM_CODEC));
/* 363 */   public static final DataComponentType<BannerPatternLayers> BANNER_PATTERNS = register("banner_patterns", b -> b
/* 364 */       .persistent(BannerPatternLayers.CODEC)
/* 365 */       .networkSynchronized(BannerPatternLayers.STREAM_CODEC)
/* 366 */       .cacheEncoding());
/* 367 */   public static final DataComponentType<DyeColor> BASE_COLOR = register("base_color", b -> b
/* 368 */       .persistent(DyeColor.CODEC)
/* 369 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/* 370 */   public static final DataComponentType<PotDecorations> POT_DECORATIONS = register("pot_decorations", b -> b
/* 371 */       .persistent(PotDecorations.CODEC)
/* 372 */       .networkSynchronized(PotDecorations.STREAM_CODEC)
/* 373 */       .cacheEncoding());
/* 374 */   public static final DataComponentType<ItemContainerContents> CONTAINER = register("container", b -> b
/* 375 */       .persistent(ItemContainerContents.CODEC)
/* 376 */       .networkSynchronized(ItemContainerContents.STREAM_CODEC)
/* 377 */       .cacheEncoding());
/* 378 */   public static final DataComponentType<BlockItemStateProperties> BLOCK_STATE = register("block_state", b -> b
/* 379 */       .persistent(BlockItemStateProperties.CODEC)
/* 380 */       .networkSynchronized(BlockItemStateProperties.STREAM_CODEC)
/* 381 */       .cacheEncoding());
/* 382 */   public static final DataComponentType<Bees> BEES = register("bees", b -> b
/* 383 */       .persistent(Bees.CODEC)
/* 384 */       .networkSynchronized(Bees.STREAM_CODEC)
/* 385 */       .cacheEncoding());
/* 386 */   public static final DataComponentType<LockCode> LOCK = register("lock", b -> b
/* 387 */       .persistent(LockCode.CODEC));
/* 388 */   public static final DataComponentType<SeededContainerLoot> CONTAINER_LOOT = register("container_loot", b -> b
/* 389 */       .persistent(SeededContainerLoot.CODEC));
/* 390 */   public static final DataComponentType<Holder<SoundEvent>> BREAK_SOUND = register("break_sound", b -> b
/* 391 */       .persistent(SoundEvent.CODEC)
/* 392 */       .networkSynchronized(SoundEvent.STREAM_CODEC)
/* 393 */       .cacheEncoding());
/*     */   
/* 395 */   public static final DataComponentType<Holder<VillagerType>> VILLAGER_VARIANT = register("villager/variant", b -> b
/* 396 */       .persistent(VillagerType.CODEC)
/* 397 */       .networkSynchronized(VillagerType.STREAM_CODEC));
/*     */   
/* 399 */   public static final DataComponentType<Holder<WolfVariant>> WOLF_VARIANT = register("wolf/variant", b -> b
/* 400 */       .persistent(WolfVariant.CODEC)
/* 401 */       .networkSynchronized(WolfVariant.STREAM_CODEC));
/*     */   
/* 403 */   public static final DataComponentType<Holder<WolfSoundVariant>> WOLF_SOUND_VARIANT = register("wolf/sound_variant", b -> b
/* 404 */       .persistent(WolfSoundVariant.CODEC)
/* 405 */       .networkSynchronized(WolfSoundVariant.STREAM_CODEC));
/*     */   
/* 407 */   public static final DataComponentType<DyeColor> WOLF_COLLAR = register("wolf/collar", b -> b
/* 408 */       .persistent(DyeColor.CODEC)
/* 409 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   
/* 411 */   public static final DataComponentType<Fox.Variant> FOX_VARIANT = register("fox/variant", b -> b
/* 412 */       .persistent(Fox.Variant.CODEC)
/* 413 */       .networkSynchronized(Fox.Variant.STREAM_CODEC));
/*     */   
/* 415 */   public static final DataComponentType<Salmon.Variant> SALMON_SIZE = register("salmon/size", b -> b
/* 416 */       .persistent(Salmon.Variant.CODEC)
/* 417 */       .networkSynchronized(Salmon.Variant.STREAM_CODEC));
/*     */   
/* 419 */   public static final DataComponentType<Parrot.Variant> PARROT_VARIANT = register("parrot/variant", b -> b
/* 420 */       .persistent(Parrot.Variant.CODEC)
/* 421 */       .networkSynchronized(Parrot.Variant.STREAM_CODEC));
/*     */   
/* 423 */   public static final DataComponentType<TropicalFish.Pattern> TROPICAL_FISH_PATTERN = register("tropical_fish/pattern", b -> b
/* 424 */       .persistent(TropicalFish.Pattern.CODEC)
/* 425 */       .networkSynchronized(TropicalFish.Pattern.STREAM_CODEC));
/*     */   
/* 427 */   public static final DataComponentType<DyeColor> TROPICAL_FISH_BASE_COLOR = register("tropical_fish/base_color", b -> b
/* 428 */       .persistent(DyeColor.CODEC)
/* 429 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   
/* 431 */   public static final DataComponentType<DyeColor> TROPICAL_FISH_PATTERN_COLOR = register("tropical_fish/pattern_color", b -> b
/* 432 */       .persistent(DyeColor.CODEC)
/* 433 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   
/* 435 */   public static final DataComponentType<MushroomCow.Variant> MOOSHROOM_VARIANT = register("mooshroom/variant", b -> b
/* 436 */       .persistent(MushroomCow.Variant.CODEC)
/* 437 */       .networkSynchronized(MushroomCow.Variant.STREAM_CODEC));
/*     */   
/* 439 */   public static final DataComponentType<Rabbit.Variant> RABBIT_VARIANT = register("rabbit/variant", b -> b
/* 440 */       .persistent(Rabbit.Variant.CODEC)
/* 441 */       .networkSynchronized(Rabbit.Variant.STREAM_CODEC));
/*     */   
/* 443 */   public static final DataComponentType<Holder<PigVariant>> PIG_VARIANT = register("pig/variant", b -> b
/* 444 */       .persistent(PigVariant.CODEC)
/* 445 */       .networkSynchronized(PigVariant.STREAM_CODEC));
/*     */   
/* 447 */   public static final DataComponentType<Holder<CowVariant>> COW_VARIANT = register("cow/variant", b -> b
/* 448 */       .persistent(CowVariant.CODEC)
/* 449 */       .networkSynchronized(CowVariant.STREAM_CODEC));
/*     */   
/* 451 */   public static final DataComponentType<EitherHolder<ChickenVariant>> CHICKEN_VARIANT = register("chicken/variant", b -> b
/* 452 */       .persistent(EitherHolder.codec(Registries.CHICKEN_VARIANT, ChickenVariant.CODEC))
/* 453 */       .networkSynchronized(EitherHolder.streamCodec(Registries.CHICKEN_VARIANT, ChickenVariant.STREAM_CODEC)));
/*     */   
/* 455 */   public static final DataComponentType<EitherHolder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = register("zombie_nautilus/variant", b -> b
/* 456 */       .persistent(EitherHolder.codec(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.CODEC))
/* 457 */       .networkSynchronized(EitherHolder.streamCodec(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.STREAM_CODEC)));
/*     */   
/* 459 */   public static final DataComponentType<Holder<FrogVariant>> FROG_VARIANT = register("frog/variant", b -> b
/* 460 */       .persistent(FrogVariant.CODEC)
/* 461 */       .networkSynchronized(FrogVariant.STREAM_CODEC));
/*     */   
/* 463 */   public static final DataComponentType<Variant> HORSE_VARIANT = register("horse/variant", b -> b
/* 464 */       .persistent(Variant.CODEC)
/* 465 */       .networkSynchronized(Variant.STREAM_CODEC));
/*     */   
/* 467 */   public static final DataComponentType<Holder<PaintingVariant>> PAINTING_VARIANT = register("painting/variant", b -> b
/* 468 */       .persistent(PaintingVariant.CODEC)
/* 469 */       .networkSynchronized(PaintingVariant.STREAM_CODEC));
/*     */   
/* 471 */   public static final DataComponentType<Llama.Variant> LLAMA_VARIANT = register("llama/variant", b -> b
/* 472 */       .persistent(Llama.Variant.CODEC)
/* 473 */       .networkSynchronized(Llama.Variant.STREAM_CODEC));
/*     */   
/* 475 */   public static final DataComponentType<Axolotl.Variant> AXOLOTL_VARIANT = register("axolotl/variant", b -> b
/* 476 */       .persistent(Axolotl.Variant.CODEC)
/* 477 */       .networkSynchronized(Axolotl.Variant.STREAM_CODEC));
/*     */   
/* 479 */   public static final DataComponentType<Holder<CatVariant>> CAT_VARIANT = register("cat/variant", b -> b
/* 480 */       .persistent(CatVariant.CODEC)
/* 481 */       .networkSynchronized(CatVariant.STREAM_CODEC));
/*     */   
/* 483 */   public static final DataComponentType<DyeColor> CAT_COLLAR = register("cat/collar", b -> b
/* 484 */       .persistent(DyeColor.CODEC)
/* 485 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   
/* 487 */   public static final DataComponentType<DyeColor> SHEEP_COLOR = register("sheep/color", b -> b
/* 488 */       .persistent(DyeColor.CODEC)
/* 489 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   
/* 491 */   public static final DataComponentType<DyeColor> SHULKER_COLOR = register("shulker/color", b -> b
/* 492 */       .persistent(DyeColor.CODEC)
/* 493 */       .networkSynchronized(DyeColor.STREAM_CODEC));
/*     */ 
/*     */   
/* 496 */   public static final DataComponentMap COMMON_ITEM_COMPONENTS = DataComponentMap.builder()
/* 497 */     .set(MAX_STACK_SIZE, Integer.valueOf(64))
/* 498 */     .set(LORE, ItemLore.EMPTY)
/* 499 */     .set(ENCHANTMENTS, ItemEnchantments.EMPTY)
/* 500 */     .set(REPAIR_COST, Integer.valueOf(0))
/* 501 */     .set(USE_EFFECTS, UseEffects.DEFAULT)
/* 502 */     .set(ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
/* 503 */     .set(RARITY, Rarity.COMMON)
/* 504 */     .set(BREAK_SOUND, SoundEvents.ITEM_BREAK)
/* 505 */     .set(TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT)
/* 506 */     .set(SWING_ANIMATION, SwingAnimation.DEFAULT)
/* 507 */     .build();
/*     */ 
/*     */   
/* 510 */   public static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> registry) { return CUSTOM_DATA; }
/*     */ 
/*     */ 
/*     */   
/* 514 */   private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) { return (DataComponentType)Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
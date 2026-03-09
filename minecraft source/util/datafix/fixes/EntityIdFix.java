/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public class EntityIdFix extends DataFix {
/*  15 */   public EntityIdFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */   
/*  18 */   private static final Map<String, String> ID_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/*  19 */         map.put("AreaEffectCloud", "minecraft:area_effect_cloud");
/*  20 */         map.put("ArmorStand", "minecraft:armor_stand");
/*  21 */         map.put("Arrow", "minecraft:arrow");
/*  22 */         map.put("Bat", "minecraft:bat");
/*  23 */         map.put("Blaze", "minecraft:blaze");
/*  24 */         map.put("Boat", "minecraft:boat");
/*  25 */         map.put("CaveSpider", "minecraft:cave_spider");
/*  26 */         map.put("Chicken", "minecraft:chicken");
/*  27 */         map.put("Cow", "minecraft:cow");
/*  28 */         map.put("Creeper", "minecraft:creeper");
/*  29 */         map.put("Donkey", "minecraft:donkey");
/*  30 */         map.put("DragonFireball", "minecraft:dragon_fireball");
/*  31 */         map.put("ElderGuardian", "minecraft:elder_guardian");
/*  32 */         map.put("EnderCrystal", "minecraft:ender_crystal");
/*  33 */         map.put("EnderDragon", "minecraft:ender_dragon");
/*  34 */         map.put("Enderman", "minecraft:enderman");
/*  35 */         map.put("Endermite", "minecraft:endermite");
/*  36 */         map.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
/*  37 */         map.put("FallingSand", "minecraft:falling_block");
/*  38 */         map.put("Fireball", "minecraft:fireball");
/*  39 */         map.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
/*  40 */         map.put("Ghast", "minecraft:ghast");
/*  41 */         map.put("Giant", "minecraft:giant");
/*  42 */         map.put("Guardian", "minecraft:guardian");
/*  43 */         map.put("Horse", "minecraft:horse");
/*  44 */         map.put("Husk", "minecraft:husk");
/*  45 */         map.put("Item", "minecraft:item");
/*  46 */         map.put("ItemFrame", "minecraft:item_frame");
/*  47 */         map.put("LavaSlime", "minecraft:magma_cube");
/*  48 */         map.put("LeashKnot", "minecraft:leash_knot");
/*  49 */         map.put("MinecartChest", "minecraft:chest_minecart");
/*  50 */         map.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
/*  51 */         map.put("MinecartFurnace", "minecraft:furnace_minecart");
/*  52 */         map.put("MinecartHopper", "minecraft:hopper_minecart");
/*  53 */         map.put("MinecartRideable", "minecraft:minecart");
/*  54 */         map.put("MinecartSpawner", "minecraft:spawner_minecart");
/*  55 */         map.put("MinecartTNT", "minecraft:tnt_minecart");
/*  56 */         map.put("Mule", "minecraft:mule");
/*  57 */         map.put("MushroomCow", "minecraft:mooshroom");
/*  58 */         map.put("Ozelot", "minecraft:ocelot");
/*  59 */         map.put("Painting", "minecraft:painting");
/*  60 */         map.put("Pig", "minecraft:pig");
/*  61 */         map.put("PigZombie", "minecraft:zombie_pigman");
/*  62 */         map.put("PolarBear", "minecraft:polar_bear");
/*  63 */         map.put("PrimedTnt", "minecraft:tnt");
/*  64 */         map.put("Rabbit", "minecraft:rabbit");
/*  65 */         map.put("Sheep", "minecraft:sheep");
/*  66 */         map.put("Shulker", "minecraft:shulker");
/*  67 */         map.put("ShulkerBullet", "minecraft:shulker_bullet");
/*  68 */         map.put("Silverfish", "minecraft:silverfish");
/*  69 */         map.put("Skeleton", "minecraft:skeleton");
/*  70 */         map.put("SkeletonHorse", "minecraft:skeleton_horse");
/*  71 */         map.put("Slime", "minecraft:slime");
/*  72 */         map.put("SmallFireball", "minecraft:small_fireball");
/*  73 */         map.put("SnowMan", "minecraft:snowman");
/*  74 */         map.put("Snowball", "minecraft:snowball");
/*  75 */         map.put("SpectralArrow", "minecraft:spectral_arrow");
/*  76 */         map.put("Spider", "minecraft:spider");
/*  77 */         map.put("Squid", "minecraft:squid");
/*  78 */         map.put("Stray", "minecraft:stray");
/*  79 */         map.put("ThrownEgg", "minecraft:egg");
/*  80 */         map.put("ThrownEnderpearl", "minecraft:ender_pearl");
/*  81 */         map.put("ThrownExpBottle", "minecraft:xp_bottle");
/*  82 */         map.put("ThrownPotion", "minecraft:potion");
/*  83 */         map.put("Villager", "minecraft:villager");
/*  84 */         map.put("VillagerGolem", "minecraft:villager_golem");
/*  85 */         map.put("Witch", "minecraft:witch");
/*  86 */         map.put("WitherBoss", "minecraft:wither");
/*  87 */         map.put("WitherSkeleton", "minecraft:wither_skeleton");
/*  88 */         map.put("WitherSkull", "minecraft:wither_skull");
/*  89 */         map.put("Wolf", "minecraft:wolf");
/*  90 */         map.put("XPOrb", "minecraft:xp_orb");
/*  91 */         map.put("Zombie", "minecraft:zombie");
/*  92 */         map.put("ZombieHorse", "minecraft:zombie_horse");
/*  93 */         map.put("ZombieVillager", "minecraft:zombie_villager");
/*     */       });
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  98 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.ENTITY);
/*  99 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.ENTITY);
/*     */     
/* 101 */     Type<?> oldItemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 102 */     Type<?> newItemStackType = getOutputSchema().getType(References.ITEM_STACK);
/*     */     
/* 104 */     return TypeRewriteRule.seq(
/* 105 */         convertUnchecked("item stack entity name hook converter", oldItemStackType, newItemStackType), 
/* 106 */         fixTypeEverywhere("EntityIdFix", oldType, newType, ops -> ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
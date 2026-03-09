/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.schemas.V1451_6;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class StatsCounterFix
/*     */   extends DataFix {
/*  24 */   public StatsCounterFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */   
/*  27 */   private static final Set<String> SPECIAL_OBJECTIVE_CRITERIA = Set.of(new String[] { "dummy", "trigger", "deathCount", "playerKillCount", "totalKillCount", "health", "food", "air", "armor", "xp", "level", "killedByTeam.aqua", "killedByTeam.black", "killedByTeam.blue", "killedByTeam.dark_aqua", "killedByTeam.dark_blue", "killedByTeam.dark_gray", "killedByTeam.dark_green", "killedByTeam.dark_purple", "killedByTeam.dark_red", "killedByTeam.gold", "killedByTeam.gray", "killedByTeam.green", "killedByTeam.light_purple", "killedByTeam.red", "killedByTeam.white", "killedByTeam.yellow", "teamkill.aqua", "teamkill.black", "teamkill.blue", "teamkill.dark_aqua", "teamkill.dark_blue", "teamkill.dark_gray", "teamkill.dark_green", "teamkill.dark_purple", "teamkill.dark_red", "teamkill.gold", "teamkill.gray", "teamkill.green", "teamkill.light_purple", "teamkill.red", "teamkill.white", "teamkill.yellow" });
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
/*  75 */   private static final Set<String> SKIP = ImmutableSet.builder()
/*  76 */     .add("stat.craftItem.minecraft.spawn_egg")
/*  77 */     .add("stat.useItem.minecraft.spawn_egg")
/*  78 */     .add("stat.breakItem.minecraft.spawn_egg")
/*  79 */     .add("stat.pickup.minecraft.spawn_egg")
/*  80 */     .add("stat.drop.minecraft.spawn_egg")
/*  81 */     .build();
/*     */   
/*  83 */   private static final Map<String, String> CUSTOM_MAP = ImmutableMap.builder()
/*  84 */     .put("stat.leaveGame", "minecraft:leave_game")
/*  85 */     .put("stat.playOneMinute", "minecraft:play_one_minute")
/*  86 */     .put("stat.timeSinceDeath", "minecraft:time_since_death")
/*  87 */     .put("stat.sneakTime", "minecraft:sneak_time")
/*  88 */     .put("stat.walkOneCm", "minecraft:walk_one_cm")
/*  89 */     .put("stat.crouchOneCm", "minecraft:crouch_one_cm")
/*  90 */     .put("stat.sprintOneCm", "minecraft:sprint_one_cm")
/*  91 */     .put("stat.swimOneCm", "minecraft:swim_one_cm")
/*  92 */     .put("stat.fallOneCm", "minecraft:fall_one_cm")
/*  93 */     .put("stat.climbOneCm", "minecraft:climb_one_cm")
/*  94 */     .put("stat.flyOneCm", "minecraft:fly_one_cm")
/*  95 */     .put("stat.diveOneCm", "minecraft:dive_one_cm")
/*  96 */     .put("stat.minecartOneCm", "minecraft:minecart_one_cm")
/*  97 */     .put("stat.boatOneCm", "minecraft:boat_one_cm")
/*  98 */     .put("stat.pigOneCm", "minecraft:pig_one_cm")
/*  99 */     .put("stat.horseOneCm", "minecraft:horse_one_cm")
/* 100 */     .put("stat.aviateOneCm", "minecraft:aviate_one_cm")
/* 101 */     .put("stat.jump", "minecraft:jump")
/* 102 */     .put("stat.drop", "minecraft:drop")
/* 103 */     .put("stat.damageDealt", "minecraft:damage_dealt")
/* 104 */     .put("stat.damageTaken", "minecraft:damage_taken")
/* 105 */     .put("stat.deaths", "minecraft:deaths")
/* 106 */     .put("stat.mobKills", "minecraft:mob_kills")
/* 107 */     .put("stat.animalsBred", "minecraft:animals_bred")
/* 108 */     .put("stat.playerKills", "minecraft:player_kills")
/* 109 */     .put("stat.fishCaught", "minecraft:fish_caught")
/* 110 */     .put("stat.talkedToVillager", "minecraft:talked_to_villager")
/* 111 */     .put("stat.tradedWithVillager", "minecraft:traded_with_villager")
/* 112 */     .put("stat.cakeSlicesEaten", "minecraft:eat_cake_slice")
/* 113 */     .put("stat.cauldronFilled", "minecraft:fill_cauldron")
/* 114 */     .put("stat.cauldronUsed", "minecraft:use_cauldron")
/* 115 */     .put("stat.armorCleaned", "minecraft:clean_armor")
/* 116 */     .put("stat.bannerCleaned", "minecraft:clean_banner")
/* 117 */     .put("stat.brewingstandInteraction", "minecraft:interact_with_brewingstand")
/* 118 */     .put("stat.beaconInteraction", "minecraft:interact_with_beacon")
/* 119 */     .put("stat.dropperInspected", "minecraft:inspect_dropper")
/* 120 */     .put("stat.hopperInspected", "minecraft:inspect_hopper")
/* 121 */     .put("stat.dispenserInspected", "minecraft:inspect_dispenser")
/* 122 */     .put("stat.noteblockPlayed", "minecraft:play_noteblock")
/* 123 */     .put("stat.noteblockTuned", "minecraft:tune_noteblock")
/* 124 */     .put("stat.flowerPotted", "minecraft:pot_flower")
/* 125 */     .put("stat.trappedChestTriggered", "minecraft:trigger_trapped_chest")
/* 126 */     .put("stat.enderchestOpened", "minecraft:open_enderchest")
/* 127 */     .put("stat.itemEnchanted", "minecraft:enchant_item")
/* 128 */     .put("stat.recordPlayed", "minecraft:play_record")
/* 129 */     .put("stat.furnaceInteraction", "minecraft:interact_with_furnace")
/* 130 */     .put("stat.craftingTableInteraction", "minecraft:interact_with_crafting_table")
/* 131 */     .put("stat.chestOpened", "minecraft:open_chest")
/* 132 */     .put("stat.sleepInBed", "minecraft:sleep_in_bed")
/* 133 */     .put("stat.shulkerBoxOpened", "minecraft:open_shulker_box")
/* 134 */     .build();
/*     */   
/*     */   private static final String BLOCK_KEY = "stat.mineBlock";
/*     */   private static final String NEW_BLOCK_KEY = "minecraft:mined";
/* 138 */   private static final Map<String, String> ITEM_KEYS = ImmutableMap.builder()
/* 139 */     .put("stat.craftItem", "minecraft:crafted")
/* 140 */     .put("stat.useItem", "minecraft:used")
/* 141 */     .put("stat.breakItem", "minecraft:broken")
/* 142 */     .put("stat.pickup", "minecraft:picked_up")
/* 143 */     .put("stat.drop", "minecraft:dropped")
/* 144 */     .build();
/*     */   
/* 146 */   private static final Map<String, String> ENTITY_KEYS = ImmutableMap.builder()
/* 147 */     .put("stat.entityKilledBy", "minecraft:killed_by")
/* 148 */     .put("stat.killEntity", "minecraft:killed")
/* 149 */     .build();
/*     */   
/* 151 */   private static final Map<String, String> ENTITIES = ImmutableMap.builder()
/* 152 */     .put("Bat", "minecraft:bat")
/* 153 */     .put("Blaze", "minecraft:blaze")
/* 154 */     .put("CaveSpider", "minecraft:cave_spider")
/* 155 */     .put("Chicken", "minecraft:chicken")
/* 156 */     .put("Cow", "minecraft:cow")
/* 157 */     .put("Creeper", "minecraft:creeper")
/* 158 */     .put("Donkey", "minecraft:donkey")
/* 159 */     .put("ElderGuardian", "minecraft:elder_guardian")
/* 160 */     .put("Enderman", "minecraft:enderman")
/* 161 */     .put("Endermite", "minecraft:endermite")
/* 162 */     .put("EvocationIllager", "minecraft:evocation_illager")
/* 163 */     .put("Ghast", "minecraft:ghast")
/* 164 */     .put("Guardian", "minecraft:guardian")
/* 165 */     .put("Horse", "minecraft:horse")
/* 166 */     .put("Husk", "minecraft:husk")
/* 167 */     .put("Llama", "minecraft:llama")
/* 168 */     .put("LavaSlime", "minecraft:magma_cube")
/* 169 */     .put("MushroomCow", "minecraft:mooshroom")
/* 170 */     .put("Mule", "minecraft:mule")
/* 171 */     .put("Ozelot", "minecraft:ocelot")
/* 172 */     .put("Parrot", "minecraft:parrot")
/* 173 */     .put("Pig", "minecraft:pig")
/* 174 */     .put("PolarBear", "minecraft:polar_bear")
/* 175 */     .put("Rabbit", "minecraft:rabbit")
/* 176 */     .put("Sheep", "minecraft:sheep")
/* 177 */     .put("Shulker", "minecraft:shulker")
/* 178 */     .put("Silverfish", "minecraft:silverfish")
/* 179 */     .put("SkeletonHorse", "minecraft:skeleton_horse")
/* 180 */     .put("Skeleton", "minecraft:skeleton")
/* 181 */     .put("Slime", "minecraft:slime")
/* 182 */     .put("Spider", "minecraft:spider")
/* 183 */     .put("Squid", "minecraft:squid")
/* 184 */     .put("Stray", "minecraft:stray")
/* 185 */     .put("Vex", "minecraft:vex")
/* 186 */     .put("Villager", "minecraft:villager")
/* 187 */     .put("VindicationIllager", "minecraft:vindication_illager")
/* 188 */     .put("Witch", "minecraft:witch")
/* 189 */     .put("WitherSkeleton", "minecraft:wither_skeleton")
/* 190 */     .put("Wolf", "minecraft:wolf")
/* 191 */     .put("ZombieHorse", "minecraft:zombie_horse")
/* 192 */     .put("PigZombie", "minecraft:zombie_pigman")
/* 193 */     .put("ZombieVillager", "minecraft:zombie_villager")
/* 194 */     .put("Zombie", "minecraft:zombie")
/* 195 */     .build(); private static final String NEW_CUSTOM_KEY = "minecraft:custom";
/*     */   private static final class StatType extends Record { private final String type;
/*     */     private final String typeKey;
/*     */     
/* 199 */     private StatType(String type, String typeKey) { this.type = type; this.typeKey = typeKey; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 199 */       //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType; } public String type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;
/* 199 */       //   0	8	1	o	Ljava/lang/Object; } public String typeKey() { return this.typeKey; } }
/*     */   
/*     */   private static StatType unpackLegacyKey(String key) {
/* 202 */     if (SKIP.contains(key)) {
/* 203 */       return null;
/*     */     }
/*     */     
/* 206 */     String customKey = (String)CUSTOM_MAP.get(key);
/* 207 */     if (customKey != null) {
/* 208 */       return new StatType("minecraft:custom", customKey);
/*     */     }
/*     */     
/* 211 */     int splitIndex = StringUtils.ordinalIndexOf(key, ".", 2);
/* 212 */     if (splitIndex < 0) {
/* 213 */       return null;
/*     */     }
/*     */     
/* 216 */     String prefix = key.substring(0, splitIndex);
/* 217 */     if ("stat.mineBlock".equals(prefix)) {
/* 218 */       String newKey = upgradeBlock(key.substring(splitIndex + 1).replace('.', ':'));
/* 219 */       return new StatType("minecraft:mined", newKey);
/*     */     } 
/*     */     
/* 222 */     String itemKey = (String)ITEM_KEYS.get(prefix);
/* 223 */     if (itemKey != null) {
/* 224 */       String oldItem = key.substring(splitIndex + 1).replace('.', ':');
/* 225 */       String newItem = upgradeItem(oldItem);
/* 226 */       String newKey = (newItem == null) ? oldItem : newItem;
/* 227 */       return new StatType(itemKey, newKey);
/*     */     } 
/*     */     
/* 230 */     String entityKey = (String)ENTITY_KEYS.get(prefix);
/* 231 */     if (entityKey != null) {
/* 232 */       String oldEntity = key.substring(splitIndex + 1).replace('.', ':');
/* 233 */       String newKey = (String)ENTITIES.getOrDefault(oldEntity, oldEntity);
/* 234 */       return new StatType(entityKey, newKey);
/*     */     } 
/*     */     
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 242 */     return TypeRewriteRule.seq(
/* 243 */         makeStatFixer(), 
/* 244 */         makeObjectiveFixer());
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeRewriteRule makeStatFixer() {
/* 249 */     Type<?> inputType = getInputSchema().getType(References.STATS);
/* 250 */     Type<?> outputType = getOutputSchema().getType(References.STATS);
/* 251 */     return fixTypeEverywhereTyped("StatsCounterFix", inputType, outputType, input -> {
/* 252 */           Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/* 253 */           Map<Dynamic<?>, Dynamic<?>> stats = Maps.newHashMap();
/* 254 */           Optional<? extends Map<? extends Dynamic<?>, ? extends Dynamic<?>>> map = tag.getMapValues().result();
/* 255 */           if (map.isPresent()) {
/* 256 */             for (Map.Entry<? extends Dynamic<?>, ? extends Dynamic<?>> entry : ((Map)map.get()).entrySet()) {
/* 257 */               if (((Dynamic)entry.getValue()).asNumber().result().isPresent()) {
/* 258 */                 String key = ((Dynamic)entry.getKey()).asString("");
/* 259 */                 StatType statType = unpackLegacyKey(key);
/* 260 */                 if (statType != null) {
/* 261 */                   Dynamic<?> newTypeKey = tag.createString(statType.type());
/* 262 */                   Dynamic<?> element = (Dynamic)stats.computeIfAbsent(newTypeKey, ());
/* 263 */                   stats.put(newTypeKey, element.set(statType.typeKey(), (Dynamic)entry.getValue()));
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/* 268 */           return Util.readTypedOrThrow(outputType, tag.emptyMap().set("stats", tag.createMap(stats)));
/*     */         });
/*     */   }
/*     */   
/*     */   private TypeRewriteRule makeObjectiveFixer() {
/* 273 */     Type<?> inputType = getInputSchema().getType(References.OBJECTIVE);
/* 274 */     Type<?> outputType = getOutputSchema().getType(References.OBJECTIVE);
/* 275 */     return fixTypeEverywhereTyped("ObjectiveStatFix", inputType, outputType, input -> {
/* 276 */           Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/* 277 */           Dynamic<?> updatedTag = tag.update("CriteriaName", ());
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
/* 291 */           return Util.readTypedOrThrow(outputType, updatedTag);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 296 */   private static String upgradeItem(String name) { return ItemStackTheFlatteningFix.updateItem(name, 0); }
/*     */ 
/*     */ 
/*     */   
/* 300 */   private static String upgradeBlock(String name) { return BlockStateData.upgradeBlock(name); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\StatsCounterFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
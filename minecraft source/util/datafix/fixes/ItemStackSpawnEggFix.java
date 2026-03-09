/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemStackSpawnEggFix
/*     */   extends DataFix {
/*     */   private final String itemType;
/*     */   
/*     */   public ItemStackSpawnEggFix(Schema outputSchema, boolean changesType, String itemType) {
/*  24 */     super(outputSchema, changesType);
/*  25 */     this.itemType = itemType;
/*     */   }
/*     */   
/*  28 */   private static final Map<String, String> MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/*  29 */         map.put("minecraft:bat", "minecraft:bat_spawn_egg");
/*  30 */         map.put("minecraft:blaze", "minecraft:blaze_spawn_egg");
/*  31 */         map.put("minecraft:cave_spider", "minecraft:cave_spider_spawn_egg");
/*  32 */         map.put("minecraft:chicken", "minecraft:chicken_spawn_egg");
/*  33 */         map.put("minecraft:cow", "minecraft:cow_spawn_egg");
/*  34 */         map.put("minecraft:creeper", "minecraft:creeper_spawn_egg");
/*  35 */         map.put("minecraft:donkey", "minecraft:donkey_spawn_egg");
/*  36 */         map.put("minecraft:elder_guardian", "minecraft:elder_guardian_spawn_egg");
/*  37 */         map.put("minecraft:ender_dragon", "minecraft:ender_dragon_spawn_egg");
/*  38 */         map.put("minecraft:enderman", "minecraft:enderman_spawn_egg");
/*  39 */         map.put("minecraft:endermite", "minecraft:endermite_spawn_egg");
/*  40 */         map.put("minecraft:evocation_illager", "minecraft:evocation_illager_spawn_egg");
/*  41 */         map.put("minecraft:ghast", "minecraft:ghast_spawn_egg");
/*  42 */         map.put("minecraft:guardian", "minecraft:guardian_spawn_egg");
/*  43 */         map.put("minecraft:horse", "minecraft:horse_spawn_egg");
/*  44 */         map.put("minecraft:husk", "minecraft:husk_spawn_egg");
/*  45 */         map.put("minecraft:iron_golem", "minecraft:iron_golem_spawn_egg");
/*  46 */         map.put("minecraft:llama", "minecraft:llama_spawn_egg");
/*  47 */         map.put("minecraft:magma_cube", "minecraft:magma_cube_spawn_egg");
/*  48 */         map.put("minecraft:mooshroom", "minecraft:mooshroom_spawn_egg");
/*  49 */         map.put("minecraft:mule", "minecraft:mule_spawn_egg");
/*  50 */         map.put("minecraft:ocelot", "minecraft:ocelot_spawn_egg");
/*  51 */         map.put("minecraft:pufferfish", "minecraft:pufferfish_spawn_egg");
/*  52 */         map.put("minecraft:parrot", "minecraft:parrot_spawn_egg");
/*  53 */         map.put("minecraft:pig", "minecraft:pig_spawn_egg");
/*  54 */         map.put("minecraft:polar_bear", "minecraft:polar_bear_spawn_egg");
/*  55 */         map.put("minecraft:rabbit", "minecraft:rabbit_spawn_egg");
/*  56 */         map.put("minecraft:sheep", "minecraft:sheep_spawn_egg");
/*  57 */         map.put("minecraft:shulker", "minecraft:shulker_spawn_egg");
/*  58 */         map.put("minecraft:silverfish", "minecraft:silverfish_spawn_egg");
/*  59 */         map.put("minecraft:skeleton", "minecraft:skeleton_spawn_egg");
/*  60 */         map.put("minecraft:skeleton_horse", "minecraft:skeleton_horse_spawn_egg");
/*  61 */         map.put("minecraft:slime", "minecraft:slime_spawn_egg");
/*  62 */         map.put("minecraft:snow_golem", "minecraft:snow_golem_spawn_egg");
/*  63 */         map.put("minecraft:spider", "minecraft:spider_spawn_egg");
/*  64 */         map.put("minecraft:squid", "minecraft:squid_spawn_egg");
/*  65 */         map.put("minecraft:stray", "minecraft:stray_spawn_egg");
/*  66 */         map.put("minecraft:turtle", "minecraft:turtle_spawn_egg");
/*  67 */         map.put("minecraft:vex", "minecraft:vex_spawn_egg");
/*  68 */         map.put("minecraft:villager", "minecraft:villager_spawn_egg");
/*  69 */         map.put("minecraft:vindication_illager", "minecraft:vindication_illager_spawn_egg");
/*  70 */         map.put("minecraft:witch", "minecraft:witch_spawn_egg");
/*  71 */         map.put("minecraft:wither", "minecraft:wither_spawn_egg");
/*  72 */         map.put("minecraft:wither_skeleton", "minecraft:wither_skeleton_spawn_egg");
/*  73 */         map.put("minecraft:wolf", "minecraft:wolf_spawn_egg");
/*  74 */         map.put("minecraft:zombie", "minecraft:zombie_spawn_egg");
/*  75 */         map.put("minecraft:zombie_horse", "minecraft:zombie_horse_spawn_egg");
/*  76 */         map.put("minecraft:zombie_pigman", "minecraft:zombie_pigman_spawn_egg");
/*  77 */         map.put("minecraft:zombie_villager", "minecraft:zombie_villager_spawn_egg");
/*     */       });
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  82 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*     */     
/*  84 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*  85 */     OpticFinder<String> entityIdF = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*  86 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/*  87 */     OpticFinder<?> entityF = tagF.type().findField("EntityTag");
/*     */     
/*  89 */     return fixTypeEverywhereTyped("ItemInstanceSpawnEggFix" + getOutputSchema().getVersionKey(), itemStackType, input -> {
/*  90 */           Optional<Pair<String, String>> id = input.getOptional(idF);
/*  91 */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), this.itemType)) {
/*  92 */             Typed<?> tag = input.getOrCreateTyped(tagF);
/*     */             
/*  94 */             Typed<?> entity = tag.getOrCreateTyped(entityF);
/*  95 */             Optional<String> entityId = entity.getOptional(entityIdF);
/*     */             
/*  97 */             if (entityId.isPresent()) {
/*  98 */               return input.set(idF, Pair.of(References.ITEM_NAME.typeName(), (String)MAP.getOrDefault(entityId.get(), "minecraft:pig_spawn_egg")));
/*     */             }
/*     */           } 
/* 101 */           return input;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackSpawnEggFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
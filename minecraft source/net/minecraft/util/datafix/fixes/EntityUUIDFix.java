/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityUUIDFix extends AbstractUUIDFix {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  18 */   private static final Set<String> ABSTRACT_HORSES = Sets.newHashSet();
/*  19 */   private static final Set<String> TAMEABLE_ANIMALS = Sets.newHashSet();
/*  20 */   private static final Set<String> ANIMALS = Sets.newHashSet();
/*  21 */   private static final Set<String> MOBS = Sets.newHashSet();
/*  22 */   private static final Set<String> LIVING_ENTITIES = Sets.newHashSet();
/*  23 */   private static final Set<String> PROJECTILES = Sets.newHashSet();
/*     */   
/*     */   static  {
/*  26 */     ABSTRACT_HORSES.add("minecraft:donkey");
/*  27 */     ABSTRACT_HORSES.add("minecraft:horse");
/*  28 */     ABSTRACT_HORSES.add("minecraft:llama");
/*  29 */     ABSTRACT_HORSES.add("minecraft:mule");
/*  30 */     ABSTRACT_HORSES.add("minecraft:skeleton_horse");
/*  31 */     ABSTRACT_HORSES.add("minecraft:trader_llama");
/*  32 */     ABSTRACT_HORSES.add("minecraft:zombie_horse");
/*  33 */     TAMEABLE_ANIMALS.add("minecraft:cat");
/*  34 */     TAMEABLE_ANIMALS.add("minecraft:parrot");
/*  35 */     TAMEABLE_ANIMALS.add("minecraft:wolf");
/*  36 */     ANIMALS.add("minecraft:bee");
/*  37 */     ANIMALS.add("minecraft:chicken");
/*  38 */     ANIMALS.add("minecraft:cow");
/*  39 */     ANIMALS.add("minecraft:fox");
/*  40 */     ANIMALS.add("minecraft:mooshroom");
/*  41 */     ANIMALS.add("minecraft:ocelot");
/*  42 */     ANIMALS.add("minecraft:panda");
/*  43 */     ANIMALS.add("minecraft:pig");
/*  44 */     ANIMALS.add("minecraft:polar_bear");
/*  45 */     ANIMALS.add("minecraft:rabbit");
/*  46 */     ANIMALS.add("minecraft:sheep");
/*  47 */     ANIMALS.add("minecraft:turtle");
/*  48 */     ANIMALS.add("minecraft:hoglin");
/*  49 */     MOBS.add("minecraft:bat");
/*  50 */     MOBS.add("minecraft:blaze");
/*  51 */     MOBS.add("minecraft:cave_spider");
/*  52 */     MOBS.add("minecraft:cod");
/*  53 */     MOBS.add("minecraft:creeper");
/*  54 */     MOBS.add("minecraft:dolphin");
/*  55 */     MOBS.add("minecraft:drowned");
/*  56 */     MOBS.add("minecraft:elder_guardian");
/*  57 */     MOBS.add("minecraft:ender_dragon");
/*  58 */     MOBS.add("minecraft:enderman");
/*  59 */     MOBS.add("minecraft:endermite");
/*  60 */     MOBS.add("minecraft:evoker");
/*  61 */     MOBS.add("minecraft:ghast");
/*  62 */     MOBS.add("minecraft:giant");
/*  63 */     MOBS.add("minecraft:guardian");
/*  64 */     MOBS.add("minecraft:husk");
/*  65 */     MOBS.add("minecraft:illusioner");
/*  66 */     MOBS.add("minecraft:magma_cube");
/*  67 */     MOBS.add("minecraft:pufferfish");
/*  68 */     MOBS.add("minecraft:zombified_piglin");
/*  69 */     MOBS.add("minecraft:salmon");
/*  70 */     MOBS.add("minecraft:shulker");
/*  71 */     MOBS.add("minecraft:silverfish");
/*  72 */     MOBS.add("minecraft:skeleton");
/*  73 */     MOBS.add("minecraft:slime");
/*  74 */     MOBS.add("minecraft:snow_golem");
/*  75 */     MOBS.add("minecraft:spider");
/*  76 */     MOBS.add("minecraft:squid");
/*  77 */     MOBS.add("minecraft:stray");
/*  78 */     MOBS.add("minecraft:tropical_fish");
/*  79 */     MOBS.add("minecraft:vex");
/*  80 */     MOBS.add("minecraft:villager");
/*  81 */     MOBS.add("minecraft:iron_golem");
/*  82 */     MOBS.add("minecraft:vindicator");
/*  83 */     MOBS.add("minecraft:pillager");
/*  84 */     MOBS.add("minecraft:wandering_trader");
/*  85 */     MOBS.add("minecraft:witch");
/*  86 */     MOBS.add("minecraft:wither");
/*  87 */     MOBS.add("minecraft:wither_skeleton");
/*  88 */     MOBS.add("minecraft:zombie");
/*  89 */     MOBS.add("minecraft:zombie_villager");
/*  90 */     MOBS.add("minecraft:phantom");
/*  91 */     MOBS.add("minecraft:ravager");
/*  92 */     MOBS.add("minecraft:piglin");
/*  93 */     LIVING_ENTITIES.add("minecraft:armor_stand");
/*  94 */     PROJECTILES.add("minecraft:arrow");
/*  95 */     PROJECTILES.add("minecraft:dragon_fireball");
/*  96 */     PROJECTILES.add("minecraft:firework_rocket");
/*  97 */     PROJECTILES.add("minecraft:fireball");
/*  98 */     PROJECTILES.add("minecraft:llama_spit");
/*  99 */     PROJECTILES.add("minecraft:small_fireball");
/* 100 */     PROJECTILES.add("minecraft:snowball");
/* 101 */     PROJECTILES.add("minecraft:spectral_arrow");
/* 102 */     PROJECTILES.add("minecraft:egg");
/* 103 */     PROJECTILES.add("minecraft:ender_pearl");
/* 104 */     PROJECTILES.add("minecraft:experience_bottle");
/* 105 */     PROJECTILES.add("minecraft:potion");
/* 106 */     PROJECTILES.add("minecraft:trident");
/* 107 */     PROJECTILES.add("minecraft:wither_skull");
/*     */   }
/*     */ 
/*     */   
/* 111 */   public EntityUUIDFix(Schema outputSchema) { super(outputSchema, References.ENTITY); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 116 */     return fixTypeEverywhereTyped("EntityUUIDFixes", getInputSchema().getType(this.typeReference), input -> {
/* 117 */           input = input.update(DSL.remainderFinder(), EntityUUIDFix::updateEntityUUID);
/* 118 */           for (String name : ABSTRACT_HORSES) {
/* 119 */             input = updateNamedChoice(input, name, EntityUUIDFix::updateAnimalOwner);
/*     */           }
/* 121 */           for (String name : TAMEABLE_ANIMALS) {
/* 122 */             input = updateNamedChoice(input, name, EntityUUIDFix::updateAnimalOwner);
/*     */           }
/* 124 */           for (String name : ANIMALS) {
/* 125 */             input = updateNamedChoice(input, name, EntityUUIDFix::updateAnimal);
/*     */           }
/* 127 */           for (String name : MOBS) {
/* 128 */             input = updateNamedChoice(input, name, EntityUUIDFix::updateMob);
/*     */           }
/* 130 */           for (String name : LIVING_ENTITIES) {
/* 131 */             input = updateNamedChoice(input, name, EntityUUIDFix::updateLivingEntity);
/*     */           }
/* 133 */           for (String name : PROJECTILES) {
/* 134 */             input = updateNamedChoice(input, name, EntityUUIDFix::updateProjectile);
/*     */           }
/* 136 */           input = updateNamedChoice(input, "minecraft:bee", EntityUUIDFix::updateHurtBy);
/* 137 */           input = updateNamedChoice(input, "minecraft:zombified_piglin", EntityUUIDFix::updateHurtBy);
/* 138 */           input = updateNamedChoice(input, "minecraft:fox", EntityUUIDFix::updateFox);
/* 139 */           input = updateNamedChoice(input, "minecraft:item", EntityUUIDFix::updateItem);
/* 140 */           input = updateNamedChoice(input, "minecraft:shulker_bullet", EntityUUIDFix::updateShulkerBullet);
/* 141 */           input = updateNamedChoice(input, "minecraft:area_effect_cloud", EntityUUIDFix::updateAreaEffectCloud);
/* 142 */           input = updateNamedChoice(input, "minecraft:zombie_villager", EntityUUIDFix::updateZombieVillager);
/* 143 */           input = updateNamedChoice(input, "minecraft:evoker_fangs", EntityUUIDFix::updateEvokerFangs);
/* 144 */           return updateNamedChoice(input, "minecraft:piglin", EntityUUIDFix::updatePiglin);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updatePiglin(Dynamic<?> tag) {
/* 150 */     return tag.update("Brain", brain -> brain.update("memories", ()));
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
/* 161 */   private static Dynamic<?> updateEvokerFangs(Dynamic<?> tag) { return (Dynamic)replaceUUIDLeastMost(tag, "OwnerUUID", "Owner").orElse(tag); }
/*     */ 
/*     */ 
/*     */   
/* 165 */   private static Dynamic<?> updateZombieVillager(Dynamic<?> tag) { return (Dynamic)replaceUUIDLeastMost(tag, "ConversionPlayer", "ConversionPlayer").orElse(tag); }
/*     */ 
/*     */ 
/*     */   
/* 169 */   private static Dynamic<?> updateAreaEffectCloud(Dynamic<?> tag) { return (Dynamic)replaceUUIDLeastMost(tag, "OwnerUUID", "Owner").orElse(tag); }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateShulkerBullet(Dynamic<?> tag) {
/* 173 */     tag = (Dynamic)replaceUUIDMLTag(tag, "Owner", "Owner").orElse(tag);
/* 174 */     return (Dynamic)replaceUUIDMLTag(tag, "Target", "Target").orElse(tag);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateItem(Dynamic<?> tag) {
/* 178 */     tag = (Dynamic)replaceUUIDMLTag(tag, "Owner", "Owner").orElse(tag);
/* 179 */     return (Dynamic)replaceUUIDMLTag(tag, "Thrower", "Thrower").orElse(tag);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateFox(Dynamic<?> tag) {
/* 183 */     Optional<Dynamic<?>> trustedUUIDs = tag.get("TrustedUUIDs").result().map(uuidTags -> 
/* 184 */         tag.createList(uuidTags.asStream().map(())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     return (Dynamic)DataFixUtils.orElse(trustedUUIDs.map(trusted -> 
/* 192 */           tag.remove("TrustedUUIDs").set("Trusted", trusted)), tag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   private static Dynamic<?> updateHurtBy(Dynamic<?> tag) { return (Dynamic)replaceUUIDString(tag, "HurtBy", "HurtBy").orElse(tag); }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateAnimalOwner(Dynamic<?> tag) {
/* 201 */     Dynamic<?> fixed = updateAnimal(tag);
/* 202 */     return (Dynamic)replaceUUIDString(fixed, "OwnerUUID", "Owner").orElse(fixed);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAnimal(Dynamic<?> tag) {
/* 206 */     Dynamic<?> fixed = updateMob(tag);
/* 207 */     return (Dynamic)replaceUUIDLeastMost(fixed, "LoveCause", "LoveCause").orElse(fixed);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateMob(Dynamic<?> tag) {
/* 211 */     return updateLivingEntity(tag).update("Leash", leashTag -> 
/* 212 */         (Dynamic)replaceUUIDLeastMost(leashTag, "UUID", "UUID").orElse(leashTag));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Dynamic<?> updateLivingEntity(Dynamic<?> tag) {
/* 217 */     return tag.update("Attributes", attributes -> 
/* 218 */         tag.createList(attributes.asStream().map(())));
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
/* 229 */   private static Dynamic<?> updateProjectile(Dynamic<?> tag) { return (Dynamic)DataFixUtils.orElse(tag.get("OwnerUUID").result().map(owner -> 
/* 230 */           tag.remove("OwnerUUID").set("Owner", owner)), tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 235 */   public static Dynamic<?> updateEntityUUID(Dynamic<?> tag) { return (Dynamic)replaceUUIDLeastMost(tag, "UUID", "UUID").orElse(tag); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
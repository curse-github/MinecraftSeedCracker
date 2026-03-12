/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class AttributeModifierIdFix
/*     */   extends DataFix {
/*  22 */   private static final Map<UUID, String> ID_MAP = ImmutableMap.builder()
/*  23 */     .put(UUID.fromString("736565d2-e1a7-403d-a3f8-1aeb3e302542"), "minecraft:creative_mode_block_range")
/*  24 */     .put(UUID.fromString("98491ef6-97b1-4584-ae82-71a8cc85cf73"), "minecraft:creative_mode_entity_range")
/*  25 */     .put(UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635"), "minecraft:effect.speed")
/*  26 */     .put(UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890"), "minecraft:effect.slowness")
/*  27 */     .put(UUID.fromString("AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3"), "minecraft:effect.haste")
/*  28 */     .put(UUID.fromString("55FCED67-E92A-486E-9800-B47F202C4386"), "minecraft:effect.mining_fatigue")
/*  29 */     .put(UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"), "minecraft:effect.strength")
/*  30 */     .put(UUID.fromString("C0105BF3-AEF8-46B0-9EBC-92943757CCBE"), "minecraft:effect.jump_boost")
/*  31 */     .put(UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5"), "minecraft:effect.weakness")
/*  32 */     .put(UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC"), "minecraft:effect.health_boost")
/*  33 */     .put(UUID.fromString("EAE29CF0-701E-4ED6-883A-96F798F3DAB5"), "minecraft:effect.absorption")
/*  34 */     .put(UUID.fromString("03C3C89D-7037-4B42-869F-B146BCB64D2E"), "minecraft:effect.luck")
/*  35 */     .put(UUID.fromString("CC5AF142-2BD2-4215-B636-2605AED11727"), "minecraft:effect.unluck")
/*  36 */     .put(UUID.fromString("6555be74-63b3-41f1-a245-77833b3c2562"), "minecraft:evil")
/*  37 */     .put(UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ce"), "minecraft:powder_snow")
/*  38 */     .put(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"), "minecraft:sprinting")
/*  39 */     .put(UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0"), "minecraft:attacking")
/*  40 */     .put(UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667"), "minecraft:baby")
/*  41 */     .put(UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F"), "minecraft:covered")
/*  42 */     .put(UUID.fromString("9e362924-01de-4ddd-a2b2-d0f7a405a174"), "minecraft:suffocating")
/*  43 */     .put(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"), "minecraft:drinking")
/*  44 */     .put(UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "minecraft:baby")
/*  45 */     .put(UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718"), "minecraft:attacking")
/*  46 */     .put(UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), "minecraft:armor.boots")
/*  47 */     .put(UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), "minecraft:armor.leggings")
/*  48 */     .put(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), "minecraft:armor.chestplate")
/*  49 */     .put(UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), "minecraft:armor.helmet")
/*  50 */     .put(UUID.fromString("C1C72771-8B8E-BA4A-ACE0-81A93C8928B2"), "minecraft:armor.body")
/*  51 */     .put(UUID.fromString("b572ecd2-ac0c-4071-abde-9594af072a37"), "minecraft:enchantment.fire_protection")
/*  52 */     .put(UUID.fromString("40a9968f-5c66-4e2f-b7f4-2ec2f4b3e450"), "minecraft:enchantment.blast_protection")
/*  53 */     .put(UUID.fromString("07a65791-f64d-4e79-86c7-f83932f007ec"), "minecraft:enchantment.respiration")
/*  54 */     .put(UUID.fromString("60b1b7db-fffd-4ad0-817c-d6c6a93d8a45"), "minecraft:enchantment.aqua_affinity")
/*  55 */     .put(UUID.fromString("11dc269a-4476-46c0-aff3-9e17d7eb6801"), "minecraft:enchantment.depth_strider")
/*  56 */     .put(UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038"), "minecraft:enchantment.soul_speed")
/*  57 */     .put(UUID.fromString("b9716dbd-50df-4080-850e-70347d24e687"), "minecraft:enchantment.soul_speed")
/*  58 */     .put(UUID.fromString("92437d00-c3a7-4f2e-8f6c-1f21585d5dd0"), "minecraft:enchantment.swift_sneak")
/*  59 */     .put(UUID.fromString("5d3d087b-debe-4037-b53e-d84f3ff51f17"), "minecraft:enchantment.sweeping_edge")
/*  60 */     .put(UUID.fromString("3ceb37c0-db62-46b5-bd02-785457b01d96"), "minecraft:enchantment.efficiency")
/*  61 */     .put(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"), "minecraft:base_attack_damage")
/*  62 */     .put(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"), "minecraft:base_attack_speed")
/*  63 */     .build();
/*     */   
/*  65 */   private static final Map<String, String> NAME_MAP = Map.of("Random spawn bonus", "minecraft:random_spawn_bonus", "Random zombie-spawn bonus", "minecraft:zombie_random_spawn_bonus", "Leader zombie bonus", "minecraft:leader_zombie_bonus", "Zombie reinforcement callee charge", "minecraft:reinforcement_callee_charge", "Zombie reinforcement caller charge", "minecraft:reinforcement_caller_charge");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public AttributeModifierIdFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  79 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*  80 */     OpticFinder<?> componentsFinder = itemStackType.findField("components");
/*  81 */     return TypeRewriteRule.seq(
/*  82 */         fixTypeEverywhereTyped("AttributeIdFix (ItemStack)", itemStackType, itemStack -> 
/*  83 */           itemStack.updateTyped(componentsFinder, ())), new TypeRewriteRule[] {
/*     */           
/*  85 */           fixTypeEverywhereTyped("AttributeIdFix (Entity)", getInputSchema().getType(References.ENTITY), AttributeModifierIdFix::fixEntity), 
/*  86 */           fixTypeEverywhereTyped("AttributeIdFix (Player)", getInputSchema().getType(References.PLAYER), AttributeModifierIdFix::fixEntity)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static Stream<Dynamic<?>> fixModifiersTypeWrapper(Stream<?> modifiers) { return fixModifiers(modifiers); }
/*     */ 
/*     */   
/*     */   private static Stream<Dynamic<?>> fixModifiers(Stream<Dynamic<?>> modifiers) {
/*  96 */     Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/*     */     
/*  98 */     modifiers.forEach(modifier -> {
/*  99 */           UUID uuid = uuidFromIntArray(modifier.get("uuid").asIntStream().toArray());
/* 100 */           String name = modifier.get("name").asString("");
/* 101 */           String idFromUUID = (uuid != null) ? (String)ID_MAP.get(uuid) : null;
/* 102 */           String idFromName = (String)NAME_MAP.get(name);
/* 103 */           if (idFromUUID != null) {
/* 104 */             modifier = modifier.set("id", modifier.createString(idFromUUID));
/* 105 */             converted.put(idFromUUID, modifier.remove("uuid").remove("name"));
/* 106 */           } else if (idFromName != null) {
/*     */             
/* 108 */             Dynamic<?> preExisting = (Dynamic)converted.get(idFromName);
/* 109 */             if (preExisting == null) {
/* 110 */               modifier = modifier.set("id", modifier.createString(idFromName));
/* 111 */               converted.put(idFromName, modifier.remove("uuid").remove("name"));
/*     */             } else {
/* 113 */               double amount = preExisting.get("amount").asDouble(0.0D);
/* 114 */               double added = modifier.get("amount").asDouble(0.0D);
/* 115 */               converted.put(idFromName, preExisting.set("amount", modifier.createDouble(amount + added)));
/*     */             } 
/*     */           } else {
/*     */             
/* 119 */             String id = "minecraft:" + ((uuid != null) ? uuid.toString().toLowerCase(Locale.ROOT) : "unknown");
/* 120 */             modifier = modifier.set("id", modifier.createString(id));
/* 121 */             converted.put(id, modifier.remove("uuid").remove("name"));
/*     */           } 
/*     */         });
/* 124 */     return object2ObjectArrayMap.values().stream();
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertModifierForEntity(Dynamic<?> modifier) {
/* 128 */     return modifier
/* 129 */       .renameField("UUID", "uuid")
/* 130 */       .renameField("Name", "name")
/* 131 */       .renameField("Amount", "amount")
/* 132 */       .renameAndFixField("Operation", "operation", operation -> { switch (operation.asInt(0)) { case 0: case 1: case 2:  }  return 
/*     */ 
/*     */             
/* 135 */             "add_multiplied_total".createString(
/* 136 */               "invalid");
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixItemStackComponents(Dynamic<?> components) {
/* 141 */     return components.update("minecraft:attribute_modifiers", attributeModifiers -> 
/* 142 */         attributeModifiers.update("modifiers", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixAttribute(Dynamic<?> attribute) {
/* 152 */     return attribute
/* 153 */       .renameField("Name", "id")
/* 154 */       .renameField("Base", "base")
/* 155 */       .renameAndFixField("Modifiers", "modifiers", modifiers -> {
/*     */ 
/*     */           
/* 158 */           Objects.requireNonNull(attribute); return (Dynamic)DataFixUtils.orElse(modifiers.asStreamOpt().result().map(()).map(AttributeModifierIdFix::fixModifiersTypeWrapper).map(attribute::createList), modifiers);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Typed<?> fixEntity(Typed<?> entity) {
/* 164 */     return entity.update(DSL.remainderFinder(), tag -> 
/* 165 */         tag.renameAndFixField("Attributes", "attributes", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UUID uuidFromIntArray(int[] intArray) {
/* 175 */     if (intArray.length != 4) {
/* 176 */       return null;
/*     */     }
/* 178 */     return new UUID(intArray[0] << 32 | intArray[1] & 0xFFFFFFFFL, intArray[2] << 32 | intArray[3] & 0xFFFFFFFFL);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AttributeModifierIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.storage.loot.parameters;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ 
/*     */ public class LootContextParamSets {
/*  14 */   private static final BiMap<Identifier, ContextKeySet> REGISTRY = HashBiMap.create(); public static final Codec<ContextKeySet> CODEC; public static final ContextKeySet EMPTY; public static final ContextKeySet CHEST; public static final ContextKeySet COMMAND; public static final ContextKeySet SELECTOR; public static final ContextKeySet FISHING; public static final ContextKeySet ENTITY; public static final ContextKeySet EQUIPMENT; public static final ContextKeySet ARCHAEOLOGY; public static final ContextKeySet GIFT;
/*     */   public static final ContextKeySet PIGLIN_BARTER;
/*     */   public static final ContextKeySet VAULT;
/*     */   public static final ContextKeySet ADVANCEMENT_REWARD;
/*     */   
/*     */   static  {
/*  20 */     Objects.requireNonNull(REGISTRY.inverse()); CODEC = Identifier.CODEC.comapFlatMap(location -> (DataResult)Optional.ofNullable((ContextKeySet)REGISTRY.get(location)).map(DataResult::success).orElseGet(()), REGISTRY.inverse()::get);
/*     */ 
/*     */     
/*  23 */     EMPTY = register("empty", builder -> {
/*     */         
/*  25 */         }); CHEST = register("chest", builder -> builder
/*  26 */         .required(LootContextParams.ORIGIN)
/*  27 */         .optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  30 */     COMMAND = register("command", builder -> builder
/*  31 */         .required(LootContextParams.ORIGIN)
/*  32 */         .optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  35 */     SELECTOR = register("selector", builder -> builder
/*  36 */         .required(LootContextParams.ORIGIN)
/*  37 */         .required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  40 */     FISHING = register("fishing", builder -> builder
/*  41 */         .required(LootContextParams.ORIGIN)
/*  42 */         .required(LootContextParams.TOOL)
/*  43 */         .optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  46 */     ENTITY = register("entity", builder -> builder
/*  47 */         .required(LootContextParams.THIS_ENTITY)
/*  48 */         .required(LootContextParams.ORIGIN)
/*  49 */         .required(LootContextParams.DAMAGE_SOURCE)
/*  50 */         .optional(LootContextParams.ATTACKING_ENTITY)
/*  51 */         .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
/*  52 */         .optional(LootContextParams.LAST_DAMAGE_PLAYER));
/*     */ 
/*     */     
/*  55 */     EQUIPMENT = register("equipment", builder -> builder
/*  56 */         .required(LootContextParams.ORIGIN)
/*  57 */         .required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  60 */     ARCHAEOLOGY = register("archaeology", builder -> builder
/*  61 */         .required(LootContextParams.ORIGIN)
/*  62 */         .required(LootContextParams.THIS_ENTITY)
/*  63 */         .required(LootContextParams.TOOL));
/*     */ 
/*     */     
/*  66 */     GIFT = register("gift", builder -> builder
/*  67 */         .required(LootContextParams.ORIGIN)
/*  68 */         .required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  71 */     PIGLIN_BARTER = register("barter", builder -> builder
/*  72 */         .required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */     
/*  75 */     VAULT = register("vault", builder -> builder
/*  76 */         .required(LootContextParams.ORIGIN)
/*  77 */         .optional(LootContextParams.THIS_ENTITY)
/*  78 */         .optional(LootContextParams.TOOL));
/*     */ 
/*     */     
/*  81 */     ADVANCEMENT_REWARD = register("advancement_reward", builder -> builder
/*  82 */         .required(LootContextParams.THIS_ENTITY)
/*  83 */         .required(LootContextParams.ORIGIN));
/*     */ 
/*     */     
/*  86 */     ADVANCEMENT_ENTITY = register("advancement_entity", builder -> builder
/*  87 */         .required(LootContextParams.THIS_ENTITY)
/*  88 */         .required(LootContextParams.ORIGIN));
/*     */ 
/*     */     
/*  91 */     ADVANCEMENT_LOCATION = register("advancement_location", builder -> builder
/*  92 */         .required(LootContextParams.THIS_ENTITY)
/*  93 */         .required(LootContextParams.ORIGIN)
/*  94 */         .required(LootContextParams.TOOL)
/*  95 */         .required(LootContextParams.BLOCK_STATE));
/*     */ 
/*     */     
/*  98 */     BLOCK_USE = register("block_use", builder -> builder
/*  99 */         .required(LootContextParams.THIS_ENTITY)
/* 100 */         .required(LootContextParams.ORIGIN)
/* 101 */         .required(LootContextParams.BLOCK_STATE));
/*     */ 
/*     */     
/* 104 */     ALL_PARAMS = register("generic", builder -> builder
/* 105 */         .required(LootContextParams.THIS_ENTITY)
/* 106 */         .required(LootContextParams.LAST_DAMAGE_PLAYER)
/* 107 */         .required(LootContextParams.DAMAGE_SOURCE)
/* 108 */         .required(LootContextParams.ATTACKING_ENTITY)
/* 109 */         .required(LootContextParams.DIRECT_ATTACKING_ENTITY)
/* 110 */         .required(LootContextParams.ORIGIN)
/* 111 */         .required(LootContextParams.BLOCK_STATE)
/* 112 */         .required(LootContextParams.BLOCK_ENTITY)
/* 113 */         .required(LootContextParams.TOOL)
/* 114 */         .required(LootContextParams.EXPLOSION_RADIUS));
/*     */ 
/*     */     
/* 117 */     BLOCK = register("block", builder -> builder
/* 118 */         .required(LootContextParams.BLOCK_STATE)
/* 119 */         .required(LootContextParams.ORIGIN)
/* 120 */         .required(LootContextParams.TOOL)
/* 121 */         .optional(LootContextParams.THIS_ENTITY)
/* 122 */         .optional(LootContextParams.BLOCK_ENTITY)
/* 123 */         .optional(LootContextParams.EXPLOSION_RADIUS));
/*     */ 
/*     */     
/* 126 */     SHEARING = register("shearing", builder -> builder
/* 127 */         .required(LootContextParams.ORIGIN)
/* 128 */         .required(LootContextParams.THIS_ENTITY)
/* 129 */         .required(LootContextParams.TOOL));
/*     */ 
/*     */     
/* 132 */     ENTITY_INTERACT = register("entity_interact", builder -> builder
/* 133 */         .required(LootContextParams.TARGET_ENTITY)
/* 134 */         .optional(LootContextParams.INTERACTING_ENTITY)
/* 135 */         .required(LootContextParams.TOOL));
/*     */ 
/*     */     
/* 138 */     BLOCK_INTERACT = register("block_interact", builder -> builder
/* 139 */         .required(LootContextParams.BLOCK_STATE)
/* 140 */         .optional(LootContextParams.BLOCK_ENTITY)
/* 141 */         .optional(LootContextParams.INTERACTING_ENTITY)
/* 142 */         .optional(LootContextParams.TOOL));
/*     */ 
/*     */     
/* 145 */     ENCHANTED_DAMAGE = register("enchanted_damage", builder -> builder
/* 146 */         .required(LootContextParams.THIS_ENTITY)
/* 147 */         .required(LootContextParams.ENCHANTMENT_LEVEL)
/* 148 */         .required(LootContextParams.ORIGIN)
/* 149 */         .required(LootContextParams.DAMAGE_SOURCE)
/* 150 */         .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
/* 151 */         .optional(LootContextParams.ATTACKING_ENTITY));
/*     */ 
/*     */     
/* 154 */     ENCHANTED_ITEM = register("enchanted_item", builder -> builder
/* 155 */         .required(LootContextParams.TOOL)
/* 156 */         .required(LootContextParams.ENCHANTMENT_LEVEL));
/*     */ 
/*     */     
/* 159 */     ENCHANTED_LOCATION = register("enchanted_location", builder -> builder
/* 160 */         .required(LootContextParams.THIS_ENTITY)
/* 161 */         .required(LootContextParams.ENCHANTMENT_LEVEL)
/* 162 */         .required(LootContextParams.ORIGIN)
/* 163 */         .required(LootContextParams.ENCHANTMENT_ACTIVE));
/*     */ 
/*     */     
/* 166 */     ENCHANTED_ENTITY = register("enchanted_entity", builder -> builder
/* 167 */         .required(LootContextParams.THIS_ENTITY)
/* 168 */         .required(LootContextParams.ENCHANTMENT_LEVEL)
/* 169 */         .required(LootContextParams.ORIGIN));
/*     */ 
/*     */     
/* 172 */     HIT_BLOCK = register("hit_block", builder -> builder
/* 173 */         .required(LootContextParams.THIS_ENTITY)
/* 174 */         .required(LootContextParams.ENCHANTMENT_LEVEL)
/* 175 */         .required(LootContextParams.ORIGIN)
/* 176 */         .required(LootContextParams.BLOCK_STATE));
/*     */   }
/*     */   public static final ContextKeySet ADVANCEMENT_ENTITY; public static final ContextKeySet ADVANCEMENT_LOCATION; public static final ContextKeySet BLOCK_USE; public static final ContextKeySet ALL_PARAMS; public static final ContextKeySet BLOCK; public static final ContextKeySet SHEARING; public static final ContextKeySet ENTITY_INTERACT; public static final ContextKeySet BLOCK_INTERACT; public static final ContextKeySet ENCHANTED_DAMAGE; public static final ContextKeySet ENCHANTED_ITEM; public static final ContextKeySet ENCHANTED_LOCATION; public static final ContextKeySet ENCHANTED_ENTITY; public static final ContextKeySet HIT_BLOCK;
/*     */   private static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> consumer) {
/* 180 */     ContextKeySet.Builder builder = new ContextKeySet.Builder();
/* 181 */     consumer.accept(builder);
/* 182 */     ContextKeySet result = builder.build();
/* 183 */     Identifier id = Identifier.withDefaultNamespace(name);
/* 184 */     ContextKeySet prev = (ContextKeySet)REGISTRY.put(id, result);
/* 185 */     if (prev != null) {
/* 186 */       throw new IllegalStateException("Loot table parameter set " + String.valueOf(id) + " is already registered");
/*     */     }
/* 188 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParamSets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
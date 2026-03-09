/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
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
/*     */ public class V705
/*     */   extends NamespacedSchema
/*     */ {
/*  34 */   public V705(int versionKey, Schema parent) { super(versionKey, parent); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) { schema.registerSimple(map, name); }
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected static void registerThrowableProjectile(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) { schema.register(map, name, () -> DSL.optionalFields("inTile", References.BLOCK_NAME
/*  43 */           .in(schema))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/*  49 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/*     */     
/*  51 */     schema.register(map, "minecraft:area_effect_cloud", name -> DSL.optionalFields("Particle", References.PARTICLE
/*  52 */           .in(schema)));
/*     */     
/*  54 */     registerMob(schema, map, "minecraft:armor_stand");
/*  55 */     schema.register(map, "minecraft:arrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME
/*  56 */           .in(schema)));
/*     */     
/*  58 */     registerMob(schema, map, "minecraft:bat");
/*  59 */     registerMob(schema, map, "minecraft:blaze");
/*  60 */     schema.registerSimple(map, "minecraft:boat");
/*  61 */     registerMob(schema, map, "minecraft:cave_spider");
/*  62 */     schema.register(map, "minecraft:chest_minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/*  63 */           .in(schema), "Items", 
/*  64 */           DSL.list(References.ITEM_STACK.in(schema))));
/*     */     
/*  66 */     registerMob(schema, map, "minecraft:chicken");
/*  67 */     schema.register(map, "minecraft:commandblock_minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/*  68 */           .in(schema), "LastOutput", References.TEXT_COMPONENT
/*  69 */           .in(schema)));
/*     */     
/*  71 */     registerMob(schema, map, "minecraft:cow");
/*  72 */     registerMob(schema, map, "minecraft:creeper");
/*  73 */     schema.register(map, "minecraft:donkey", name -> DSL.optionalFields("Items", 
/*  74 */           DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK
/*  75 */           .in(schema)));
/*     */     
/*  77 */     schema.registerSimple(map, "minecraft:dragon_fireball");
/*  78 */     registerThrowableProjectile(schema, map, "minecraft:egg");
/*  79 */     registerMob(schema, map, "minecraft:elder_guardian");
/*  80 */     schema.registerSimple(map, "minecraft:ender_crystal");
/*  81 */     registerMob(schema, map, "minecraft:ender_dragon");
/*  82 */     schema.register(map, "minecraft:enderman", name -> DSL.optionalFields("carried", References.BLOCK_NAME
/*  83 */           .in(schema)));
/*     */     
/*  85 */     registerMob(schema, map, "minecraft:endermite");
/*  86 */     registerThrowableProjectile(schema, map, "minecraft:ender_pearl");
/*  87 */     schema.registerSimple(map, "minecraft:eye_of_ender_signal");
/*  88 */     schema.register(map, "minecraft:falling_block", name -> DSL.optionalFields("Block", References.BLOCK_NAME
/*  89 */           .in(schema), "TileEntityData", References.BLOCK_ENTITY
/*  90 */           .in(schema)));
/*     */     
/*  92 */     registerThrowableProjectile(schema, map, "minecraft:fireball");
/*  93 */     schema.register(map, "minecraft:fireworks_rocket", name -> DSL.optionalFields("FireworksItem", References.ITEM_STACK
/*  94 */           .in(schema)));
/*     */     
/*  96 */     schema.register(map, "minecraft:furnace_minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/*  97 */           .in(schema)));
/*     */     
/*  99 */     registerMob(schema, map, "minecraft:ghast");
/* 100 */     registerMob(schema, map, "minecraft:giant");
/* 101 */     registerMob(schema, map, "minecraft:guardian");
/* 102 */     schema.register(map, "minecraft:hopper_minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/* 103 */           .in(schema), "Items", 
/* 104 */           DSL.list(References.ITEM_STACK.in(schema))));
/*     */     
/* 106 */     schema.register(map, "minecraft:horse", name -> DSL.optionalFields("ArmorItem", References.ITEM_STACK
/*     */           
/* 108 */           .in(schema), "SaddleItem", References.ITEM_STACK
/* 109 */           .in(schema)));
/*     */     
/* 111 */     registerMob(schema, map, "minecraft:husk");
/* 112 */     schema.register(map, "minecraft:item", name -> DSL.optionalFields("Item", References.ITEM_STACK
/* 113 */           .in(schema)));
/*     */     
/* 115 */     schema.register(map, "minecraft:item_frame", name -> DSL.optionalFields("Item", References.ITEM_STACK
/* 116 */           .in(schema)));
/*     */     
/* 118 */     schema.registerSimple(map, "minecraft:leash_knot");
/* 119 */     registerMob(schema, map, "minecraft:magma_cube");
/* 120 */     schema.register(map, "minecraft:minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/* 121 */           .in(schema)));
/*     */     
/* 123 */     registerMob(schema, map, "minecraft:mooshroom");
/* 124 */     schema.register(map, "minecraft:mule", name -> DSL.optionalFields("Items", 
/* 125 */           DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK
/* 126 */           .in(schema)));
/*     */     
/* 128 */     registerMob(schema, map, "minecraft:ocelot");
/* 129 */     schema.registerSimple(map, "minecraft:painting");
/* 130 */     registerMob(schema, map, "minecraft:parrot");
/* 131 */     registerMob(schema, map, "minecraft:pig");
/* 132 */     registerMob(schema, map, "minecraft:polar_bear");
/* 133 */     schema.register(map, "minecraft:potion", name -> DSL.optionalFields("Potion", References.ITEM_STACK
/* 134 */           .in(schema), "inTile", References.BLOCK_NAME
/* 135 */           .in(schema)));
/*     */     
/* 137 */     registerMob(schema, map, "minecraft:rabbit");
/* 138 */     registerMob(schema, map, "minecraft:sheep");
/* 139 */     registerMob(schema, map, "minecraft:shulker");
/* 140 */     schema.registerSimple(map, "minecraft:shulker_bullet");
/* 141 */     registerMob(schema, map, "minecraft:silverfish");
/* 142 */     registerMob(schema, map, "minecraft:skeleton");
/* 143 */     schema.register(map, "minecraft:skeleton_horse", name -> DSL.optionalFields("SaddleItem", References.ITEM_STACK
/* 144 */           .in(schema)));
/*     */     
/* 146 */     registerMob(schema, map, "minecraft:slime");
/* 147 */     registerThrowableProjectile(schema, map, "minecraft:small_fireball");
/* 148 */     registerThrowableProjectile(schema, map, "minecraft:snowball");
/* 149 */     registerMob(schema, map, "minecraft:snowman");
/* 150 */     schema.register(map, "minecraft:spawner_minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/* 151 */           .in(schema), References.UNTAGGED_SPAWNER
/* 152 */           .in(schema)));
/*     */     
/* 154 */     schema.register(map, "minecraft:spectral_arrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME
/* 155 */           .in(schema)));
/*     */     
/* 157 */     registerMob(schema, map, "minecraft:spider");
/* 158 */     registerMob(schema, map, "minecraft:squid");
/* 159 */     registerMob(schema, map, "minecraft:stray");
/* 160 */     schema.registerSimple(map, "minecraft:tnt");
/* 161 */     schema.register(map, "minecraft:tnt_minecart", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME
/* 162 */           .in(schema)));
/*     */     
/* 164 */     schema.register(map, "minecraft:villager", name -> DSL.optionalFields("Inventory", 
/* 165 */           DSL.list(References.ITEM_STACK.in(schema)), "Offers", 
/* 166 */           DSL.optionalFields("Recipes", 
/* 167 */             DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*     */ 
/*     */     
/* 170 */     registerMob(schema, map, "minecraft:villager_golem");
/* 171 */     registerMob(schema, map, "minecraft:witch");
/* 172 */     registerMob(schema, map, "minecraft:wither");
/* 173 */     registerMob(schema, map, "minecraft:wither_skeleton");
/* 174 */     registerThrowableProjectile(schema, map, "minecraft:wither_skull");
/* 175 */     registerMob(schema, map, "minecraft:wolf");
/* 176 */     registerThrowableProjectile(schema, map, "minecraft:xp_bottle");
/* 177 */     schema.registerSimple(map, "minecraft:xp_orb");
/* 178 */     registerMob(schema, map, "minecraft:zombie");
/* 179 */     schema.register(map, "minecraft:zombie_horse", name -> DSL.optionalFields("SaddleItem", References.ITEM_STACK
/* 180 */           .in(schema)));
/*     */     
/* 182 */     registerMob(schema, map, "minecraft:zombie_pigman");
/* 183 */     schema.register(map, "minecraft:zombie_villager", name -> DSL.optionalFields("Offers", 
/* 184 */           DSL.optionalFields("Recipes", 
/* 185 */             DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     schema.registerSimple(map, "minecraft:evocation_fangs");
/* 191 */     registerMob(schema, map, "minecraft:evocation_illager");
/* 192 */     registerMob(schema, map, "minecraft:illusion_illager");
/* 193 */     schema.register(map, "minecraft:llama", name -> DSL.optionalFields("Items", 
/* 194 */           DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK
/* 195 */           .in(schema), "DecorItem", References.ITEM_STACK
/* 196 */           .in(schema)));
/*     */     
/* 198 */     schema.registerSimple(map, "minecraft:llama_spit");
/* 199 */     registerMob(schema, map, "minecraft:vex");
/* 200 */     registerMob(schema, map, "minecraft:vindication_illager");
/*     */     
/* 202 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 207 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 208 */     schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT
/* 209 */           .in(schema), 
/* 210 */           DSL.optionalFields("CustomName", 
/* 211 */             DSL.constType(DSL.string()), 
/* 212 */             DSL.taggedChoiceLazy("id", namespacedString(), entityTypes))));
/*     */ 
/*     */     
/* 215 */     schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME
/* 216 */             .in(schema), "tag", 
/* 217 */             V99.itemStackTag(schema)), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */   }
/*     */ 
/*     */   
/* 221 */   private static final Map<String, String> ITEM_TO_ENTITY = ImmutableMap.builder()
/* 222 */     .put("minecraft:armor_stand", "minecraft:armor_stand")
/* 223 */     .put("minecraft:painting", "minecraft:painting")
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     .put("minecraft:armadillo_spawn_egg", "minecraft:armadillo")
/* 230 */     .put("minecraft:allay_spawn_egg", "minecraft:allay")
/* 231 */     .put("minecraft:axolotl_spawn_egg", "minecraft:axolotl")
/* 232 */     .put("minecraft:bat_spawn_egg", "minecraft:bat")
/* 233 */     .put("minecraft:bee_spawn_egg", "minecraft:bee")
/* 234 */     .put("minecraft:blaze_spawn_egg", "minecraft:blaze")
/* 235 */     .put("minecraft:bogged_spawn_egg", "minecraft:bogged")
/* 236 */     .put("minecraft:breeze_spawn_egg", "minecraft:breeze")
/* 237 */     .put("minecraft:cat_spawn_egg", "minecraft:cat")
/* 238 */     .put("minecraft:camel_spawn_egg", "minecraft:camel")
/* 239 */     .put("minecraft:cave_spider_spawn_egg", "minecraft:cave_spider")
/* 240 */     .put("minecraft:chicken_spawn_egg", "minecraft:chicken")
/* 241 */     .put("minecraft:cod_spawn_egg", "minecraft:cod")
/* 242 */     .put("minecraft:cow_spawn_egg", "minecraft:cow")
/* 243 */     .put("minecraft:creeper_spawn_egg", "minecraft:creeper")
/* 244 */     .put("minecraft:dolphin_spawn_egg", "minecraft:dolphin")
/* 245 */     .put("minecraft:donkey_spawn_egg", "minecraft:donkey")
/* 246 */     .put("minecraft:drowned_spawn_egg", "minecraft:drowned")
/* 247 */     .put("minecraft:elder_guardian_spawn_egg", "minecraft:elder_guardian")
/* 248 */     .put("minecraft:ender_dragon_spawn_egg", "minecraft:ender_dragon")
/* 249 */     .put("minecraft:enderman_spawn_egg", "minecraft:enderman")
/* 250 */     .put("minecraft:endermite_spawn_egg", "minecraft:endermite")
/* 251 */     .put("minecraft:evoker_spawn_egg", "minecraft:evoker")
/* 252 */     .put("minecraft:fox_spawn_egg", "minecraft:fox")
/* 253 */     .put("minecraft:frog_spawn_egg", "minecraft:frog")
/* 254 */     .put("minecraft:ghast_spawn_egg", "minecraft:ghast")
/* 255 */     .put("minecraft:glow_squid_spawn_egg", "minecraft:glow_squid")
/* 256 */     .put("minecraft:goat_spawn_egg", "minecraft:goat")
/* 257 */     .put("minecraft:guardian_spawn_egg", "minecraft:guardian")
/* 258 */     .put("minecraft:hoglin_spawn_egg", "minecraft:hoglin")
/* 259 */     .put("minecraft:horse_spawn_egg", "minecraft:horse")
/* 260 */     .put("minecraft:husk_spawn_egg", "minecraft:husk")
/* 261 */     .put("minecraft:iron_golem_spawn_egg", "minecraft:iron_golem")
/* 262 */     .put("minecraft:llama_spawn_egg", "minecraft:llama")
/* 263 */     .put("minecraft:magma_cube_spawn_egg", "minecraft:magma_cube")
/* 264 */     .put("minecraft:mooshroom_spawn_egg", "minecraft:mooshroom")
/* 265 */     .put("minecraft:mule_spawn_egg", "minecraft:mule")
/* 266 */     .put("minecraft:ocelot_spawn_egg", "minecraft:ocelot")
/* 267 */     .put("minecraft:panda_spawn_egg", "minecraft:panda")
/* 268 */     .put("minecraft:parrot_spawn_egg", "minecraft:parrot")
/* 269 */     .put("minecraft:phantom_spawn_egg", "minecraft:phantom")
/* 270 */     .put("minecraft:pig_spawn_egg", "minecraft:pig")
/* 271 */     .put("minecraft:piglin_spawn_egg", "minecraft:piglin")
/* 272 */     .put("minecraft:piglin_brute_spawn_egg", "minecraft:piglin_brute")
/* 273 */     .put("minecraft:pillager_spawn_egg", "minecraft:pillager")
/* 274 */     .put("minecraft:polar_bear_spawn_egg", "minecraft:polar_bear")
/* 275 */     .put("minecraft:pufferfish_spawn_egg", "minecraft:pufferfish")
/* 276 */     .put("minecraft:rabbit_spawn_egg", "minecraft:rabbit")
/* 277 */     .put("minecraft:ravager_spawn_egg", "minecraft:ravager")
/* 278 */     .put("minecraft:salmon_spawn_egg", "minecraft:salmon")
/* 279 */     .put("minecraft:sheep_spawn_egg", "minecraft:sheep")
/* 280 */     .put("minecraft:shulker_spawn_egg", "minecraft:shulker")
/* 281 */     .put("minecraft:silverfish_spawn_egg", "minecraft:silverfish")
/* 282 */     .put("minecraft:skeleton_spawn_egg", "minecraft:skeleton")
/* 283 */     .put("minecraft:skeleton_horse_spawn_egg", "minecraft:skeleton_horse")
/* 284 */     .put("minecraft:slime_spawn_egg", "minecraft:slime")
/* 285 */     .put("minecraft:sniffer_spawn_egg", "minecraft:sniffer")
/* 286 */     .put("minecraft:snow_golem_spawn_egg", "minecraft:snow_golem")
/* 287 */     .put("minecraft:spider_spawn_egg", "minecraft:spider")
/* 288 */     .put("minecraft:squid_spawn_egg", "minecraft:squid")
/* 289 */     .put("minecraft:stray_spawn_egg", "minecraft:stray")
/* 290 */     .put("minecraft:strider_spawn_egg", "minecraft:strider")
/* 291 */     .put("minecraft:tadpole_spawn_egg", "minecraft:tadpole")
/* 292 */     .put("minecraft:trader_llama_spawn_egg", "minecraft:trader_llama")
/* 293 */     .put("minecraft:tropical_fish_spawn_egg", "minecraft:tropical_fish")
/* 294 */     .put("minecraft:turtle_spawn_egg", "minecraft:turtle")
/* 295 */     .put("minecraft:vex_spawn_egg", "minecraft:vex")
/* 296 */     .put("minecraft:villager_spawn_egg", "minecraft:villager")
/* 297 */     .put("minecraft:vindicator_spawn_egg", "minecraft:vindicator")
/* 298 */     .put("minecraft:wandering_trader_spawn_egg", "minecraft:wandering_trader")
/* 299 */     .put("minecraft:warden_spawn_egg", "minecraft:warden")
/* 300 */     .put("minecraft:witch_spawn_egg", "minecraft:witch")
/* 301 */     .put("minecraft:wither_spawn_egg", "minecraft:wither")
/* 302 */     .put("minecraft:wither_skeleton_spawn_egg", "minecraft:wither_skeleton")
/* 303 */     .put("minecraft:wolf_spawn_egg", "minecraft:wolf")
/* 304 */     .put("minecraft:zoglin_spawn_egg", "minecraft:zoglin")
/* 305 */     .put("minecraft:zombie_spawn_egg", "minecraft:zombie")
/* 306 */     .put("minecraft:zombie_horse_spawn_egg", "minecraft:zombie_horse")
/* 307 */     .put("minecraft:zombie_villager_spawn_egg", "minecraft:zombie_villager")
/* 308 */     .put("minecraft:zombified_piglin_spawn_egg", "minecraft:zombified_piglin")
/* 309 */     .put("minecraft:item_frame", "minecraft:item_frame")
/* 310 */     .put("minecraft:boat", "minecraft:oak_boat")
/* 311 */     .put("minecraft:oak_boat", "minecraft:oak_boat")
/* 312 */     .put("minecraft:oak_chest_boat", "minecraft:oak_chest_boat")
/* 313 */     .put("minecraft:spruce_boat", "minecraft:spruce_boat")
/* 314 */     .put("minecraft:spruce_chest_boat", "minecraft:spruce_chest_boat")
/* 315 */     .put("minecraft:birch_boat", "minecraft:birch_boat")
/* 316 */     .put("minecraft:birch_chest_boat", "minecraft:birch_chest_boat")
/* 317 */     .put("minecraft:jungle_boat", "minecraft:jungle_boat")
/* 318 */     .put("minecraft:jungle_chest_boat", "minecraft:jungle_chest_boat")
/* 319 */     .put("minecraft:acacia_boat", "minecraft:acacia_boat")
/* 320 */     .put("minecraft:acacia_chest_boat", "minecraft:acacia_chest_boat")
/* 321 */     .put("minecraft:cherry_boat", "minecraft:cherry_boat")
/* 322 */     .put("minecraft:cherry_chest_boat", "minecraft:cherry_chest_boat")
/* 323 */     .put("minecraft:dark_oak_boat", "minecraft:dark_oak_boat")
/* 324 */     .put("minecraft:dark_oak_chest_boat", "minecraft:dark_oak_chest_boat")
/* 325 */     .put("minecraft:mangrove_boat", "minecraft:mangrove_boat")
/* 326 */     .put("minecraft:mangrove_chest_boat", "minecraft:mangrove_chest_boat")
/* 327 */     .put("minecraft:bamboo_raft", "minecraft:bamboo_raft")
/* 328 */     .put("minecraft:bamboo_chest_raft", "minecraft:bamboo_chest_raft")
/* 329 */     .put("minecraft:minecart", "minecraft:minecart")
/* 330 */     .put("minecraft:chest_minecart", "minecraft:chest_minecart")
/* 331 */     .put("minecraft:furnace_minecart", "minecraft:furnace_minecart")
/* 332 */     .put("minecraft:tnt_minecart", "minecraft:tnt_minecart")
/* 333 */     .put("minecraft:hopper_minecart", "minecraft:hopper_minecart")
/* 334 */     .build();
/*     */   
/* 336 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> ops, T value) {
/* 339 */         return (T)V99.addNames(new Dynamic(ops, value), V704.ITEM_TO_BLOCKENTITY, V705.ITEM_TO_ENTITY);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V705.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
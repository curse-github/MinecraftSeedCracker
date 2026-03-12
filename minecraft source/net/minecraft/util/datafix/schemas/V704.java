/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityIdFix;
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
/*     */ public class V704
/*     */   extends Schema
/*     */ {
/*  31 */   public V704(int versionKey, Schema parent) { super(versionKey, parent); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Type<?> getChoiceType(DSL.TypeReference type, String choiceName) {
/*  36 */     if (Objects.equals(type.typeName(), References.BLOCK_ENTITY.typeName())) {
/*  37 */       return super.getChoiceType(type, NamespacedSchema.ensureNamespaced(choiceName));
/*     */     }
/*  39 */     return super.getChoiceType(type, choiceName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/*  44 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/*  45 */     BlockEntityIdFix.ID_MAP.forEach((oldId, newId) -> 
/*  46 */         map.put(newId, (Supplier)Objects.requireNonNull((Supplier)map.remove(oldId), ())));
/*     */     
/*  48 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/*  53 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*     */     
/*  55 */     schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS
/*  56 */           .in(schema), 
/*  57 */           DSL.taggedChoiceLazy("id", NamespacedSchema.namespacedString(), blockEntityTypes)));
/*     */ 
/*     */     
/*  60 */     schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME
/*  61 */             .in(schema), "tag", 
/*  62 */             V99.itemStackTag(schema)), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */   }
/*     */ 
/*     */   
/*  66 */   protected static final Map<String, String> ITEM_TO_BLOCKENTITY = (Map)DataFixUtils.make(() -> {
/*  67 */         map = Maps.newHashMap();
/*     */         
/*  69 */         map.put("minecraft:furnace", "minecraft:furnace");
/*  70 */         map.put("minecraft:lit_furnace", "minecraft:furnace");
/*  71 */         map.put("minecraft:chest", "minecraft:chest");
/*  72 */         map.put("minecraft:trapped_chest", "minecraft:chest");
/*  73 */         map.put("minecraft:ender_chest", "minecraft:ender_chest");
/*  74 */         map.put("minecraft:jukebox", "minecraft:jukebox");
/*  75 */         map.put("minecraft:dispenser", "minecraft:dispenser");
/*  76 */         map.put("minecraft:dropper", "minecraft:dropper");
/*  77 */         map.put("minecraft:sign", "minecraft:sign");
/*  78 */         map.put("minecraft:mob_spawner", "minecraft:mob_spawner");
/*  79 */         map.put("minecraft:spawner", "minecraft:mob_spawner");
/*  80 */         map.put("minecraft:noteblock", "minecraft:noteblock");
/*  81 */         map.put("minecraft:brewing_stand", "minecraft:brewing_stand");
/*  82 */         map.put("minecraft:enhanting_table", "minecraft:enchanting_table");
/*  83 */         map.put("minecraft:command_block", "minecraft:command_block");
/*  84 */         map.put("minecraft:beacon", "minecraft:beacon");
/*  85 */         map.put("minecraft:skull", "minecraft:skull");
/*  86 */         map.put("minecraft:daylight_detector", "minecraft:daylight_detector");
/*  87 */         map.put("minecraft:hopper", "minecraft:hopper");
/*  88 */         map.put("minecraft:banner", "minecraft:banner");
/*  89 */         map.put("minecraft:flower_pot", "minecraft:flower_pot");
/*  90 */         map.put("minecraft:repeating_command_block", "minecraft:command_block");
/*  91 */         map.put("minecraft:chain_command_block", "minecraft:command_block");
/*     */         
/*  93 */         map.put("minecraft:shulker_box", "minecraft:shulker_box");
/*  94 */         map.put("minecraft:white_shulker_box", "minecraft:shulker_box");
/*  95 */         map.put("minecraft:orange_shulker_box", "minecraft:shulker_box");
/*  96 */         map.put("minecraft:magenta_shulker_box", "minecraft:shulker_box");
/*  97 */         map.put("minecraft:light_blue_shulker_box", "minecraft:shulker_box");
/*  98 */         map.put("minecraft:yellow_shulker_box", "minecraft:shulker_box");
/*  99 */         map.put("minecraft:lime_shulker_box", "minecraft:shulker_box");
/* 100 */         map.put("minecraft:pink_shulker_box", "minecraft:shulker_box");
/* 101 */         map.put("minecraft:gray_shulker_box", "minecraft:shulker_box");
/* 102 */         map.put("minecraft:silver_shulker_box", "minecraft:shulker_box");
/* 103 */         map.put("minecraft:cyan_shulker_box", "minecraft:shulker_box");
/* 104 */         map.put("minecraft:purple_shulker_box", "minecraft:shulker_box");
/* 105 */         map.put("minecraft:blue_shulker_box", "minecraft:shulker_box");
/* 106 */         map.put("minecraft:brown_shulker_box", "minecraft:shulker_box");
/* 107 */         map.put("minecraft:green_shulker_box", "minecraft:shulker_box");
/* 108 */         map.put("minecraft:red_shulker_box", "minecraft:shulker_box");
/* 109 */         map.put("minecraft:black_shulker_box", "minecraft:shulker_box");
/*     */         
/* 111 */         map.put("minecraft:bed", "minecraft:bed");
/*     */         
/* 113 */         map.put("minecraft:light_gray_shulker_box", "minecraft:shulker_box");
/*     */         
/* 115 */         map.put("minecraft:banner", "minecraft:banner");
/* 116 */         map.put("minecraft:white_banner", "minecraft:banner");
/* 117 */         map.put("minecraft:orange_banner", "minecraft:banner");
/* 118 */         map.put("minecraft:magenta_banner", "minecraft:banner");
/* 119 */         map.put("minecraft:light_blue_banner", "minecraft:banner");
/* 120 */         map.put("minecraft:yellow_banner", "minecraft:banner");
/* 121 */         map.put("minecraft:lime_banner", "minecraft:banner");
/* 122 */         map.put("minecraft:pink_banner", "minecraft:banner");
/* 123 */         map.put("minecraft:gray_banner", "minecraft:banner");
/* 124 */         map.put("minecraft:silver_banner", "minecraft:banner");
/* 125 */         map.put("minecraft:light_gray_banner", "minecraft:banner");
/* 126 */         map.put("minecraft:cyan_banner", "minecraft:banner");
/* 127 */         map.put("minecraft:purple_banner", "minecraft:banner");
/* 128 */         map.put("minecraft:blue_banner", "minecraft:banner");
/* 129 */         map.put("minecraft:brown_banner", "minecraft:banner");
/* 130 */         map.put("minecraft:green_banner", "minecraft:banner");
/* 131 */         map.put("minecraft:red_banner", "minecraft:banner");
/* 132 */         map.put("minecraft:black_banner", "minecraft:banner");
/*     */ 
/*     */         
/* 135 */         map.put("minecraft:standing_sign", "minecraft:sign");
/* 136 */         map.put("minecraft:wall_sign", "minecraft:sign");
/* 137 */         map.put("minecraft:piston_head", "minecraft:piston");
/* 138 */         map.put("minecraft:daylight_detector_inverted", "minecraft:daylight_detector");
/* 139 */         map.put("minecraft:unpowered_comparator", "minecraft:comparator");
/* 140 */         map.put("minecraft:powered_comparator", "minecraft:comparator");
/* 141 */         map.put("minecraft:wall_banner", "minecraft:banner");
/* 142 */         map.put("minecraft:standing_banner", "minecraft:banner");
/* 143 */         map.put("minecraft:structure_block", "minecraft:structure_block");
/* 144 */         map.put("minecraft:end_portal", "minecraft:end_portal");
/* 145 */         map.put("minecraft:end_gateway", "minecraft:end_gateway");
/*     */         
/* 147 */         map.put("minecraft:sign", "minecraft:sign");
/*     */ 
/*     */         
/* 150 */         map.put("minecraft:shield", "minecraft:banner");
/*     */ 
/*     */         
/* 153 */         map.put("minecraft:white_bed", "minecraft:bed");
/* 154 */         map.put("minecraft:orange_bed", "minecraft:bed");
/* 155 */         map.put("minecraft:magenta_bed", "minecraft:bed");
/* 156 */         map.put("minecraft:light_blue_bed", "minecraft:bed");
/* 157 */         map.put("minecraft:yellow_bed", "minecraft:bed");
/* 158 */         map.put("minecraft:lime_bed", "minecraft:bed");
/* 159 */         map.put("minecraft:pink_bed", "minecraft:bed");
/* 160 */         map.put("minecraft:gray_bed", "minecraft:bed");
/* 161 */         map.put("minecraft:silver_bed", "minecraft:bed");
/* 162 */         map.put("minecraft:light_gray_bed", "minecraft:bed");
/* 163 */         map.put("minecraft:cyan_bed", "minecraft:bed");
/* 164 */         map.put("minecraft:purple_bed", "minecraft:bed");
/* 165 */         map.put("minecraft:blue_bed", "minecraft:bed");
/* 166 */         map.put("minecraft:brown_bed", "minecraft:bed");
/* 167 */         map.put("minecraft:green_bed", "minecraft:bed");
/* 168 */         map.put("minecraft:red_bed", "minecraft:bed");
/* 169 */         map.put("minecraft:black_bed", "minecraft:bed");
/*     */         
/* 171 */         map.put("minecraft:oak_sign", "minecraft:sign");
/* 172 */         map.put("minecraft:spruce_sign", "minecraft:sign");
/* 173 */         map.put("minecraft:birch_sign", "minecraft:sign");
/* 174 */         map.put("minecraft:jungle_sign", "minecraft:sign");
/* 175 */         map.put("minecraft:acacia_sign", "minecraft:sign");
/* 176 */         map.put("minecraft:dark_oak_sign", "minecraft:sign");
/* 177 */         map.put("minecraft:crimson_sign", "minecraft:sign");
/* 178 */         map.put("minecraft:warped_sign", "minecraft:sign");
/*     */         
/* 180 */         map.put("minecraft:skeleton_skull", "minecraft:skull");
/* 181 */         map.put("minecraft:wither_skeleton_skull", "minecraft:skull");
/* 182 */         map.put("minecraft:zombie_head", "minecraft:skull");
/* 183 */         map.put("minecraft:player_head", "minecraft:skull");
/* 184 */         map.put("minecraft:creeper_head", "minecraft:skull");
/* 185 */         map.put("minecraft:dragon_head", "minecraft:skull");
/*     */         
/* 187 */         map.put("minecraft:barrel", "minecraft:barrel");
/* 188 */         map.put("minecraft:conduit", "minecraft:conduit");
/* 189 */         map.put("minecraft:smoker", "minecraft:smoker");
/* 190 */         map.put("minecraft:blast_furnace", "minecraft:blast_furnace");
/* 191 */         map.put("minecraft:lectern", "minecraft:lectern");
/* 192 */         map.put("minecraft:bell", "minecraft:bell");
/* 193 */         map.put("minecraft:jigsaw", "minecraft:jigsaw");
/* 194 */         map.put("minecraft:campfire", "minecraft:campfire");
/* 195 */         map.put("minecraft:bee_nest", "minecraft:beehive");
/* 196 */         map.put("minecraft:beehive", "minecraft:beehive");
/* 197 */         map.put("minecraft:sculk_sensor", "minecraft:sculk_sensor");
/*     */         
/* 199 */         map.put("minecraft:decorated_pot", "minecraft:decorated_pot");
/* 200 */         map.put("minecraft:crafter", "minecraft:crafter");
/* 201 */         return ImmutableMap.copyOf(map);
/*     */       });
/*     */   
/* 204 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> ops, T value) {
/* 207 */         return (T)V99.addNames(new Dynamic(ops, value), V704.ITEM_TO_BLOCKENTITY, V99.ITEM_TO_ENTITY);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\schemas\V704.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
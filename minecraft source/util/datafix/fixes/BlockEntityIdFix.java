/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class BlockEntityIdFix extends DataFix {
/* 16 */   public BlockEntityIdFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */   
/* 19 */   public static final Map<String, String> ID_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/* 20 */         map.put("Airportal", "minecraft:end_portal");
/* 21 */         map.put("Banner", "minecraft:banner");
/* 22 */         map.put("Beacon", "minecraft:beacon");
/* 23 */         map.put("Cauldron", "minecraft:brewing_stand");
/* 24 */         map.put("Chest", "minecraft:chest");
/* 25 */         map.put("Comparator", "minecraft:comparator");
/* 26 */         map.put("Control", "minecraft:command_block");
/* 27 */         map.put("DLDetector", "minecraft:daylight_detector");
/* 28 */         map.put("Dropper", "minecraft:dropper");
/* 29 */         map.put("EnchantTable", "minecraft:enchanting_table");
/* 30 */         map.put("EndGateway", "minecraft:end_gateway");
/* 31 */         map.put("EnderChest", "minecraft:ender_chest");
/* 32 */         map.put("FlowerPot", "minecraft:flower_pot");
/* 33 */         map.put("Furnace", "minecraft:furnace");
/* 34 */         map.put("Hopper", "minecraft:hopper");
/* 35 */         map.put("MobSpawner", "minecraft:mob_spawner");
/* 36 */         map.put("Music", "minecraft:noteblock");
/* 37 */         map.put("Piston", "minecraft:piston");
/* 38 */         map.put("RecordPlayer", "minecraft:jukebox");
/* 39 */         map.put("Sign", "minecraft:sign");
/* 40 */         map.put("Skull", "minecraft:skull");
/* 41 */         map.put("Structure", "minecraft:structure_block");
/* 42 */         map.put("Trap", "minecraft:dispenser");
/*    */       });
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 47 */     Type<?> oldItemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 48 */     Type<?> newItemStackType = getOutputSchema().getType(References.ITEM_STACK);
/*    */     
/* 50 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 51 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
/*    */     
/* 53 */     return TypeRewriteRule.seq(
/* 54 */         convertUnchecked("item stack block entity name hook converter", oldItemStackType, newItemStackType), 
/* 55 */         fixTypeEverywhere("BlockEntityIdFix", oldType, newType, ops -> ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
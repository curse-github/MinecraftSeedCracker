/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ItemStackEnchantmentNamesFix extends DataFix {
/* 17 */   private static final Int2ObjectMap<String> MAP = (Int2ObjectMap)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
/* 18 */         map.put(0, "minecraft:protection");
/* 19 */         map.put(1, "minecraft:fire_protection");
/* 20 */         map.put(2, "minecraft:feather_falling");
/* 21 */         map.put(3, "minecraft:blast_protection");
/* 22 */         map.put(4, "minecraft:projectile_protection");
/* 23 */         map.put(5, "minecraft:respiration");
/* 24 */         map.put(6, "minecraft:aqua_affinity");
/* 25 */         map.put(7, "minecraft:thorns");
/* 26 */         map.put(8, "minecraft:depth_strider");
/* 27 */         map.put(9, "minecraft:frost_walker");
/* 28 */         map.put(10, "minecraft:binding_curse");
/*    */         
/* 30 */         map.put(16, "minecraft:sharpness");
/* 31 */         map.put(17, "minecraft:smite");
/* 32 */         map.put(18, "minecraft:bane_of_arthropods");
/* 33 */         map.put(19, "minecraft:knockback");
/* 34 */         map.put(20, "minecraft:fire_aspect");
/* 35 */         map.put(21, "minecraft:looting");
/* 36 */         map.put(22, "minecraft:sweeping");
/*    */         
/* 38 */         map.put(32, "minecraft:efficiency");
/* 39 */         map.put(33, "minecraft:silk_touch");
/* 40 */         map.put(34, "minecraft:unbreaking");
/* 41 */         map.put(35, "minecraft:fortune");
/*    */         
/* 43 */         map.put(48, "minecraft:power");
/* 44 */         map.put(49, "minecraft:punch");
/* 45 */         map.put(50, "minecraft:flame");
/* 46 */         map.put(51, "minecraft:infinity");
/*    */         
/* 48 */         map.put(61, "minecraft:luck_of_the_sea");
/* 49 */         map.put(62, "minecraft:lure");
/*    */         
/* 51 */         map.put(65, "minecraft:loyalty");
/* 52 */         map.put(66, "minecraft:impaling");
/* 53 */         map.put(67, "minecraft:riptide");
/* 54 */         map.put(68, "minecraft:channeling");
/*    */         
/* 56 */         map.put(70, "minecraft:mending");
/* 57 */         map.put(71, "minecraft:vanishing_curse");
/*    */       });
/*    */ 
/*    */   
/* 61 */   public ItemStackEnchantmentNamesFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 66 */     Type<?> item = getInputSchema().getType(References.ITEM_STACK);
/* 67 */     OpticFinder<?> tagFinder = item.findField("tag");
/* 68 */     return fixTypeEverywhereTyped("ItemStackEnchantmentFix", item, input -> input.updateTyped(tagFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> tag) {
/* 74 */     Objects.requireNonNull(tag); Optional<? extends Dynamic<?>> newEnch = tag.get("ench").asStreamOpt().map(s -> s.map(())).map(tag::createList).result();
/*    */     
/* 76 */     if (newEnch.isPresent()) {
/* 77 */       tag = tag.remove("ench").set("Enchantments", (Dynamic)newEnch.get());
/*    */     }
/*    */     
/* 80 */     return tag.update("StoredEnchantments", list -> {
/*    */ 
/*    */           
/* 83 */           Objects.requireNonNull(list); return (Dynamic)DataFixUtils.orElse(list.asStreamOpt().map(()).map(list::createList).result(), list);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackEnchantmentNamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
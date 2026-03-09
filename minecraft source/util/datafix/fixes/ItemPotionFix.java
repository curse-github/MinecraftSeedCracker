/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemPotionFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final int SPLASH = 16384;
/*     */   
/*  24 */   public ItemPotionFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */ 
/*     */   
/*  28 */   private static final String[] POTIONS = (String[])DataFixUtils.make(new String[128], map -> {
/*  29 */         map[0] = "minecraft:water";
/*  30 */         map[1] = "minecraft:regeneration";
/*  31 */         map[2] = "minecraft:swiftness";
/*  32 */         map[3] = "minecraft:fire_resistance";
/*  33 */         map[4] = "minecraft:poison";
/*  34 */         map[5] = "minecraft:healing";
/*  35 */         map[6] = "minecraft:night_vision";
/*  36 */         map[7] = null;
/*  37 */         map[8] = "minecraft:weakness";
/*  38 */         map[9] = "minecraft:strength";
/*  39 */         map[10] = "minecraft:slowness";
/*  40 */         map[11] = "minecraft:leaping";
/*  41 */         map[12] = "minecraft:harming";
/*  42 */         map[13] = "minecraft:water_breathing";
/*  43 */         map[14] = "minecraft:invisibility";
/*  44 */         map[15] = null;
/*  45 */         map[16] = "minecraft:awkward";
/*  46 */         map[17] = "minecraft:regeneration";
/*  47 */         map[18] = "minecraft:swiftness";
/*  48 */         map[19] = "minecraft:fire_resistance";
/*  49 */         map[20] = "minecraft:poison";
/*  50 */         map[21] = "minecraft:healing";
/*  51 */         map[22] = "minecraft:night_vision";
/*  52 */         map[23] = null;
/*  53 */         map[24] = "minecraft:weakness";
/*  54 */         map[25] = "minecraft:strength";
/*  55 */         map[26] = "minecraft:slowness";
/*  56 */         map[27] = "minecraft:leaping";
/*  57 */         map[28] = "minecraft:harming";
/*  58 */         map[29] = "minecraft:water_breathing";
/*  59 */         map[30] = "minecraft:invisibility";
/*  60 */         map[31] = null;
/*  61 */         map[32] = "minecraft:thick";
/*  62 */         map[33] = "minecraft:strong_regeneration";
/*  63 */         map[34] = "minecraft:strong_swiftness";
/*  64 */         map[35] = "minecraft:fire_resistance";
/*  65 */         map[36] = "minecraft:strong_poison";
/*  66 */         map[37] = "minecraft:strong_healing";
/*  67 */         map[38] = "minecraft:night_vision";
/*  68 */         map[39] = null;
/*  69 */         map[40] = "minecraft:weakness";
/*  70 */         map[41] = "minecraft:strong_strength";
/*  71 */         map[42] = "minecraft:slowness";
/*  72 */         map[43] = "minecraft:strong_leaping";
/*  73 */         map[44] = "minecraft:strong_harming";
/*  74 */         map[45] = "minecraft:water_breathing";
/*  75 */         map[46] = "minecraft:invisibility";
/*  76 */         map[47] = null;
/*  77 */         map[48] = null;
/*  78 */         map[49] = "minecraft:strong_regeneration";
/*  79 */         map[50] = "minecraft:strong_swiftness";
/*  80 */         map[51] = "minecraft:fire_resistance";
/*  81 */         map[52] = "minecraft:strong_poison";
/*  82 */         map[53] = "minecraft:strong_healing";
/*  83 */         map[54] = "minecraft:night_vision";
/*  84 */         map[55] = null;
/*  85 */         map[56] = "minecraft:weakness";
/*  86 */         map[57] = "minecraft:strong_strength";
/*  87 */         map[58] = "minecraft:slowness";
/*  88 */         map[59] = "minecraft:strong_leaping";
/*  89 */         map[60] = "minecraft:strong_harming";
/*  90 */         map[61] = "minecraft:water_breathing";
/*  91 */         map[62] = "minecraft:invisibility";
/*  92 */         map[63] = null;
/*  93 */         map[64] = "minecraft:mundane";
/*  94 */         map[65] = "minecraft:long_regeneration";
/*  95 */         map[66] = "minecraft:long_swiftness";
/*  96 */         map[67] = "minecraft:long_fire_resistance";
/*  97 */         map[68] = "minecraft:long_poison";
/*  98 */         map[69] = "minecraft:healing";
/*  99 */         map[70] = "minecraft:long_night_vision";
/* 100 */         map[71] = null;
/* 101 */         map[72] = "minecraft:long_weakness";
/* 102 */         map[73] = "minecraft:long_strength";
/* 103 */         map[74] = "minecraft:long_slowness";
/* 104 */         map[75] = "minecraft:long_leaping";
/* 105 */         map[76] = "minecraft:harming";
/* 106 */         map[77] = "minecraft:long_water_breathing";
/* 107 */         map[78] = "minecraft:long_invisibility";
/* 108 */         map[79] = null;
/* 109 */         map[80] = "minecraft:awkward";
/* 110 */         map[81] = "minecraft:long_regeneration";
/* 111 */         map[82] = "minecraft:long_swiftness";
/* 112 */         map[83] = "minecraft:long_fire_resistance";
/* 113 */         map[84] = "minecraft:long_poison";
/* 114 */         map[85] = "minecraft:healing";
/* 115 */         map[86] = "minecraft:long_night_vision";
/* 116 */         map[87] = null;
/* 117 */         map[88] = "minecraft:long_weakness";
/* 118 */         map[89] = "minecraft:long_strength";
/* 119 */         map[90] = "minecraft:long_slowness";
/* 120 */         map[91] = "minecraft:long_leaping";
/* 121 */         map[92] = "minecraft:harming";
/* 122 */         map[93] = "minecraft:long_water_breathing";
/* 123 */         map[94] = "minecraft:long_invisibility";
/* 124 */         map[95] = null;
/* 125 */         map[96] = "minecraft:thick";
/* 126 */         map[97] = "minecraft:regeneration";
/* 127 */         map[98] = "minecraft:swiftness";
/* 128 */         map[99] = "minecraft:long_fire_resistance";
/* 129 */         map[100] = "minecraft:poison";
/* 130 */         map[101] = "minecraft:strong_healing";
/* 131 */         map[102] = "minecraft:long_night_vision";
/* 132 */         map[103] = null;
/* 133 */         map[104] = "minecraft:long_weakness";
/* 134 */         map[105] = "minecraft:strength";
/* 135 */         map[106] = "minecraft:long_slowness";
/* 136 */         map[107] = "minecraft:leaping";
/* 137 */         map[108] = "minecraft:strong_harming";
/* 138 */         map[109] = "minecraft:long_water_breathing";
/* 139 */         map[110] = "minecraft:long_invisibility";
/* 140 */         map[111] = null;
/* 141 */         map[112] = null;
/* 142 */         map[113] = "minecraft:regeneration";
/* 143 */         map[114] = "minecraft:swiftness";
/* 144 */         map[115] = "minecraft:long_fire_resistance";
/* 145 */         map[116] = "minecraft:poison";
/* 146 */         map[117] = "minecraft:strong_healing";
/* 147 */         map[118] = "minecraft:long_night_vision";
/* 148 */         map[119] = null;
/* 149 */         map[120] = "minecraft:long_weakness";
/* 150 */         map[121] = "minecraft:strength";
/* 151 */         map[122] = "minecraft:long_slowness";
/* 152 */         map[123] = "minecraft:leaping";
/* 153 */         map[124] = "minecraft:strong_harming";
/* 154 */         map[125] = "minecraft:long_water_breathing";
/* 155 */         map[126] = "minecraft:long_invisibility";
/* 156 */         map[127] = null;
/*     */       });
/*     */   
/*     */   public static final String DEFAULT = "minecraft:water";
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 163 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 164 */     OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 165 */     OpticFinder<?> tagFinder = itemStackType.findField("tag");
/*     */     
/* 167 */     return fixTypeEverywhereTyped("ItemPotionFix", itemStackType, input -> {
/* 168 */           Optional<Pair<String, String>> id = input.getOptional(idFinder);
/* 169 */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), "minecraft:potion")) {
/* 170 */             Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/* 171 */             Optional<? extends Typed<?>> tag = input.getOptionalTyped(tagFinder);
/* 172 */             short damage = rest.get("Damage").asShort((short)0);
/* 173 */             if (tag.isPresent()) {
/* 174 */               Typed<?> output = input;
/* 175 */               Dynamic<?> tagRest = (Dynamic)((Typed)tag.get()).get(DSL.remainderFinder());
/* 176 */               Optional<String> potion = tagRest.get("Potion").asString().result();
/* 177 */               if (potion.isEmpty()) {
/* 178 */                 String potionName = POTIONS[damage & 0x7F];
/* 179 */                 Typed<?> newTag = ((Typed)tag.get()).set(DSL.remainderFinder(), tagRest.set("Potion", tagRest.createString((potionName == null) ? "minecraft:water" : potionName)));
/* 180 */                 output = output.set(tagFinder, newTag);
/*     */                 
/* 182 */                 if ((damage & 0x4000) == 16384) {
/* 183 */                   output = output.set(idFinder, Pair.of(References.ITEM_NAME.typeName(), "minecraft:splash_potion"));
/*     */                 }
/*     */               } 
/*     */               
/* 187 */               if (damage != 0) {
/* 188 */                 rest = rest.set("Damage", rest.createShort((short)0));
/*     */               }
/*     */               
/* 191 */               return output.set(DSL.remainderFinder(), rest);
/*     */             } 
/*     */           } 
/* 194 */           return input;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
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
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemSpawnEggFix
/*     */   extends DataFix
/*     */ {
/*  23 */   public ItemSpawnEggFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */   
/*  26 */   private static final String[] ID_TO_ENTITY = (String[])DataFixUtils.make(new String[256], map -> {
/*     */         
/*  28 */         map[1] = "Item";
/*  29 */         map[2] = "XPOrb";
/*     */         
/*  31 */         map[7] = "ThrownEgg";
/*  32 */         map[8] = "LeashKnot";
/*  33 */         map[9] = "Painting";
/*  34 */         map[10] = "Arrow";
/*  35 */         map[11] = "Snowball";
/*  36 */         map[12] = "Fireball";
/*  37 */         map[13] = "SmallFireball";
/*  38 */         map[14] = "ThrownEnderpearl";
/*  39 */         map[15] = "EyeOfEnderSignal";
/*  40 */         map[16] = "ThrownPotion";
/*  41 */         map[17] = "ThrownExpBottle";
/*  42 */         map[18] = "ItemFrame";
/*  43 */         map[19] = "WitherSkull";
/*     */         
/*  45 */         map[20] = "PrimedTnt";
/*  46 */         map[21] = "FallingSand";
/*  47 */         map[22] = "FireworksRocketEntity";
/*  48 */         map[23] = "TippedArrow";
/*  49 */         map[24] = "SpectralArrow";
/*  50 */         map[25] = "ShulkerBullet";
/*  51 */         map[26] = "DragonFireball";
/*     */         
/*  53 */         map[30] = "ArmorStand";
/*     */         
/*  55 */         map[41] = "Boat";
/*     */         
/*  57 */         map[42] = "MinecartRideable";
/*  58 */         map[43] = "MinecartChest";
/*  59 */         map[44] = "MinecartFurnace";
/*  60 */         map[45] = "MinecartTNT";
/*  61 */         map[46] = "MinecartHopper";
/*  62 */         map[47] = "MinecartSpawner";
/*  63 */         map[40] = "MinecartCommandBlock";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  70 */         map[50] = "Creeper";
/*  71 */         map[51] = "Skeleton";
/*  72 */         map[52] = "Spider";
/*  73 */         map[53] = "Giant";
/*  74 */         map[54] = "Zombie";
/*  75 */         map[55] = "Slime";
/*  76 */         map[56] = "Ghast";
/*  77 */         map[57] = "PigZombie";
/*  78 */         map[58] = "Enderman";
/*  79 */         map[59] = "CaveSpider";
/*  80 */         map[60] = "Silverfish";
/*  81 */         map[61] = "Blaze";
/*  82 */         map[62] = "LavaSlime";
/*  83 */         map[63] = "EnderDragon";
/*  84 */         map[64] = "WitherBoss";
/*  85 */         map[65] = "Bat";
/*  86 */         map[66] = "Witch";
/*  87 */         map[67] = "Endermite";
/*  88 */         map[68] = "Guardian";
/*  89 */         map[69] = "Shulker";
/*     */         
/*  91 */         map[90] = "Pig";
/*  92 */         map[91] = "Sheep";
/*  93 */         map[92] = "Cow";
/*  94 */         map[93] = "Chicken";
/*  95 */         map[94] = "Squid";
/*  96 */         map[95] = "Wolf";
/*  97 */         map[96] = "MushroomCow";
/*  98 */         map[97] = "SnowMan";
/*  99 */         map[98] = "Ozelot";
/* 100 */         map[99] = "VillagerGolem";
/* 101 */         map[100] = "EntityHorse";
/* 102 */         map[101] = "Rabbit";
/*     */         
/* 104 */         map[120] = "Villager";
/*     */         
/* 106 */         map[200] = "EnderCrystal";
/*     */       });
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 111 */     Schema inputSchema = getInputSchema();
/* 112 */     Type<?> itemStackType = inputSchema.getType(References.ITEM_STACK);
/*     */     
/* 114 */     OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 115 */     OpticFinder<String> entityIdFinder = DSL.fieldFinder("id", DSL.string());
/* 116 */     OpticFinder<?> tagFinder = itemStackType.findField("tag");
/* 117 */     OpticFinder<?> entityTagFinder = tagFinder.type().findField("EntityTag");
/* 118 */     OpticFinder<?> entityFinder = DSL.typeFinder(inputSchema.getTypeRaw(References.ENTITY));
/*     */     
/* 120 */     return fixTypeEverywhereTyped("ItemSpawnEggFix", itemStackType, input -> {
/* 121 */           Optional<Pair<String, String>> id = input.getOptional(idFinder);
/* 122 */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), "minecraft:spawn_egg")) {
/* 123 */             Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/* 124 */             short damage = rest.get("Damage").asShort((short)0);
/*     */             
/* 126 */             Optional<? extends Typed<?>> tagOptional = input.getOptionalTyped(tagFinder);
/* 127 */             Optional<? extends Typed<?>> entityTreeOptional = tagOptional.flatMap(());
/* 128 */             Optional<? extends Typed<?>> entityOptional = entityTreeOptional.flatMap(());
/* 129 */             Optional<String> oldId = entityOptional.flatMap(());
/*     */             
/* 131 */             Typed<?> output = input;
/*     */             
/* 133 */             String entityName = ID_TO_ENTITY[damage & 0xFF];
/* 134 */             if (entityName != null && (
/* 135 */               oldId.isEmpty() || !Objects.equals(oldId.get(), entityName))) {
/* 136 */               Typed<?> tag = input.getOrCreateTyped(tagFinder);
/*     */               
/* 138 */               Dynamic<?> entityTag = (Dynamic)DataFixUtils.orElse(tag
/* 139 */                   .getOptionalTyped(entityTagFinder).map(()), rest
/* 140 */                   .emptyMap());
/*     */               
/* 142 */               entityTag = entityTag.set("id", entityTag.createString(entityName));
/*     */               
/* 144 */               output = output.set(tagFinder, ExtraDataFixUtils.readAndSet(tag, entityTagFinder, entityTag));
/*     */             } 
/*     */             
/* 147 */             if (damage != 0) {
/* 148 */               rest = rest.set("Damage", rest.createShort((short)0));
/* 149 */               output = output.set(DSL.remainderFinder(), rest);
/*     */             } 
/* 151 */             return output;
/*     */           } 
/* 153 */           return input;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemSpawnEggFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
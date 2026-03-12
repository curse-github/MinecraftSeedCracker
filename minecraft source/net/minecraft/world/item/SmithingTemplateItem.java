/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ 
/*     */ public class SmithingTemplateItem
/*     */   extends Item {
/*  14 */   private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
/*  15 */   private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
/*  16 */   private static final Component INGREDIENTS_TITLE = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.ingredients"))).withStyle(TITLE_FORMAT);
/*  17 */   private static final Component APPLIES_TO_TITLE = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.applies_to"))).withStyle(TITLE_FORMAT);
/*  18 */   private static final Component SMITHING_TEMPLATE_SUFFIX = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template"))).withStyle(TITLE_FORMAT);
/*  19 */   private static final Component ARMOR_TRIM_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.applies_to"))).withStyle(DESCRIPTION_FORMAT);
/*  20 */   private static final Component ARMOR_TRIM_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.ingredients"))).withStyle(DESCRIPTION_FORMAT);
/*  21 */   private static final Component ARMOR_TRIM_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.base_slot_description")));
/*  22 */   private static final Component ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.additions_slot_description")));
/*  23 */   private static final Component NETHERITE_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
/*  24 */   private static final Component NETHERITE_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
/*  25 */   private static final Component NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.base_slot_description")));
/*  26 */   private static final Component NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.netherite_upgrade.additions_slot_description")));
/*  27 */   private static final Identifier EMPTY_SLOT_HELMET = Identifier.withDefaultNamespace("container/slot/helmet");
/*  28 */   private static final Identifier EMPTY_SLOT_CHESTPLATE = Identifier.withDefaultNamespace("container/slot/chestplate");
/*  29 */   private static final Identifier EMPTY_SLOT_LEGGINGS = Identifier.withDefaultNamespace("container/slot/leggings");
/*  30 */   private static final Identifier EMPTY_SLOT_BOOTS = Identifier.withDefaultNamespace("container/slot/boots");
/*  31 */   private static final Identifier EMPTY_SLOT_HOE = Identifier.withDefaultNamespace("container/slot/hoe");
/*  32 */   private static final Identifier EMPTY_SLOT_AXE = Identifier.withDefaultNamespace("container/slot/axe");
/*  33 */   private static final Identifier EMPTY_SLOT_SWORD = Identifier.withDefaultNamespace("container/slot/sword");
/*  34 */   private static final Identifier EMPTY_SLOT_SHOVEL = Identifier.withDefaultNamespace("container/slot/shovel");
/*  35 */   private static final Identifier EMPTY_SLOT_SPEAR = Identifier.withDefaultNamespace("container/slot/spear");
/*  36 */   private static final Identifier EMPTY_SLOT_PICKAXE = Identifier.withDefaultNamespace("container/slot/pickaxe");
/*  37 */   private static final Identifier EMPTY_SLOT_INGOT = Identifier.withDefaultNamespace("container/slot/ingot");
/*  38 */   private static final Identifier EMPTY_SLOT_REDSTONE_DUST = Identifier.withDefaultNamespace("container/slot/redstone_dust");
/*  39 */   private static final Identifier EMPTY_SLOT_QUARTZ = Identifier.withDefaultNamespace("container/slot/quartz");
/*  40 */   private static final Identifier EMPTY_SLOT_EMERALD = Identifier.withDefaultNamespace("container/slot/emerald");
/*  41 */   private static final Identifier EMPTY_SLOT_DIAMOND = Identifier.withDefaultNamespace("container/slot/diamond");
/*  42 */   private static final Identifier EMPTY_SLOT_LAPIS_LAZULI = Identifier.withDefaultNamespace("container/slot/lapis_lazuli");
/*  43 */   private static final Identifier EMPTY_SLOT_AMETHYST_SHARD = Identifier.withDefaultNamespace("container/slot/amethyst_shard");
/*  44 */   private static final Identifier EMPTY_SLOT_NAUTILUS_ARMOR = Identifier.withDefaultNamespace("container/slot/nautilus_armor");
/*     */   
/*     */   private final Component appliesTo;
/*     */   private final Component ingredients;
/*     */   private final Component baseSlotDescription;
/*     */   private final Component additionsSlotDescription;
/*     */   private final List<Identifier> baseSlotEmptyIcons;
/*     */   private final List<Identifier> additionalSlotEmptyIcons;
/*     */   
/*     */   public SmithingTemplateItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<Identifier> baseSlotEmptyIcons, List<Identifier> additionalSlotEmptyIcons, Item.Properties properties) {
/*  54 */     super(properties);
/*     */     
/*  56 */     this.appliesTo = appliesTo;
/*  57 */     this.ingredients = ingredients;
/*  58 */     this.baseSlotDescription = baseSlotDescription;
/*  59 */     this.additionsSlotDescription = additionsSlotDescription;
/*  60 */     this.baseSlotEmptyIcons = baseSlotEmptyIcons;
/*  61 */     this.additionalSlotEmptyIcons = additionalSlotEmptyIcons;
/*     */   }
/*     */ 
/*     */   
/*  65 */   public static SmithingTemplateItem createArmorTrimTemplate(Item.Properties properties) { return new SmithingTemplateItem(ARMOR_TRIM_APPLIES_TO, ARMOR_TRIM_INGREDIENTS, ARMOR_TRIM_BASE_SLOT_DESCRIPTION, ARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION, createTrimmableArmorIconList(), createTrimmableMaterialIconList(), properties); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static SmithingTemplateItem createNetheriteUpgradeTemplate(Item.Properties properties) { return new SmithingTemplateItem(NETHERITE_UPGRADE_APPLIES_TO, NETHERITE_UPGRADE_INGREDIENTS, NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION, NETHERITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createNetheriteUpgradeIconList(), createNetheriteUpgradeMaterialList(), properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static List<Identifier> createTrimmableArmorIconList() { return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   private static List<Identifier> createTrimmableMaterialIconList() { return List.of(EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_QUARTZ, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_EMERALD, EMPTY_SLOT_AMETHYST_SHARD); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static List<Identifier> createNetheriteUpgradeIconList() { return List.of(new Identifier[] { EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL, EMPTY_SLOT_NAUTILUS_ARMOR, EMPTY_SLOT_SPEAR }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private static List<Identifier> createNetheriteUpgradeMaterialList() { return List.of(EMPTY_SLOT_INGOT); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
/*  92 */     builder.accept(SMITHING_TEMPLATE_SUFFIX);
/*  93 */     builder.accept(CommonComponents.EMPTY);
/*  94 */     builder.accept(APPLIES_TO_TITLE);
/*  95 */     builder.accept(CommonComponents.space().append(this.appliesTo));
/*  96 */     builder.accept(INGREDIENTS_TITLE);
/*  97 */     builder.accept(CommonComponents.space().append(this.ingredients));
/*     */   }
/*     */ 
/*     */   
/* 101 */   public Component getBaseSlotDescription() { return this.baseSlotDescription; }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public Component getAdditionSlotDescription() { return this.additionsSlotDescription; }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public List<Identifier> getBaseSlotEmptyIcons() { return this.baseSlotEmptyIcons; }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public List<Identifier> getAdditionalSlotEmptyIcons() { return this.additionalSlotEmptyIcons; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SmithingTemplateItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
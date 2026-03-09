/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ 
/*     */ public class SpawnArmorTrimsCommand {
/*  40 */   private static final List<ResourceKey<TrimPattern>> VANILLA_TRIM_PATTERNS = List.of(new ResourceKey[] { TrimPatterns.SENTRY, TrimPatterns.DUNE, TrimPatterns.COAST, TrimPatterns.WILD, TrimPatterns.WARD, TrimPatterns.EYE, TrimPatterns.VEX, TrimPatterns.TIDE, TrimPatterns.SNOUT, TrimPatterns.RIB, TrimPatterns.SPIRE, TrimPatterns.WAYFINDER, TrimPatterns.SHAPER, TrimPatterns.SILENCE, TrimPatterns.RAISER, TrimPatterns.HOST, TrimPatterns.FLOW, TrimPatterns.BOLT });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static final List<ResourceKey<TrimMaterial>> VANILLA_TRIM_MATERIALS = List.of(new ResourceKey[] { TrimMaterials.QUARTZ, TrimMaterials.IRON, TrimMaterials.NETHERITE, TrimMaterials.REDSTONE, TrimMaterials.COPPER, TrimMaterials.GOLD, TrimMaterials.EMERALD, TrimMaterials.DIAMOND, TrimMaterials.LAPIS, TrimMaterials.AMETHYST, TrimMaterials.RESIN });
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final ToIntFunction<ResourceKey<TrimPattern>> TRIM_PATTERN_ORDER = Util.createIndexLookup(VANILLA_TRIM_PATTERNS);
/*  51 */   private static final ToIntFunction<ResourceKey<TrimMaterial>> TRIM_MATERIAL_ORDER = Util.createIndexLookup(VANILLA_TRIM_MATERIALS);
/*     */ 
/*     */   
/*  54 */   private static final DynamicCommandExceptionType ERROR_INVALID_PATTERN = new DynamicCommandExceptionType(value -> Component.translatableEscape("Invalid pattern", new Object[] { value }));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  57 */     dispatcher.register(
/*  58 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawn_armor_trims")
/*  59 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  60 */         .then(
/*  61 */           Commands.literal("*_lag_my_game")
/*  62 */           .executes(c -> spawnAllArmorTrims((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException()))))
/*     */         
/*  64 */         .then(
/*  65 */           Commands.argument("pattern", ResourceKeyArgument.key(Registries.TRIM_PATTERN))
/*  66 */           .executes(c -> spawnArmorTrim((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ResourceKeyArgument.getRegistryKey(c, "pattern", Registries.TRIM_PATTERN, ERROR_INVALID_PATTERN)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static int spawnAllArmorTrims(CommandSourceStack source, Player player) { return spawnArmorTrims(source, player, source.getServer().registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).listElements()); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   private static int spawnArmorTrim(CommandSourceStack source, Player player, ResourceKey<TrimPattern> pattern) { return spawnArmorTrims(source, player, Stream.of((Holder.Reference)source.getServer().registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).get(pattern).orElseThrow())); }
/*     */ 
/*     */   
/*     */   private static int spawnArmorTrims(CommandSourceStack source, Player player, Stream<Holder.Reference<TrimPattern>> patterns) {
/*  80 */     ServerLevel level = source.getLevel();
/*     */     
/*  82 */     List<Holder.Reference<TrimPattern>> sortedPatterns = patterns.sorted(Comparator.comparing(h -> Integer.valueOf(TRIM_PATTERN_ORDER.applyAsInt(h.key())))).toList();
/*  83 */     List<Holder.Reference<TrimMaterial>> sortedMaterials = level.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL).listElements().sorted(Comparator.comparing(h -> Integer.valueOf(TRIM_MATERIAL_ORDER.applyAsInt(h.key())))).toList();
/*  84 */     List<Holder.Reference<Item>> equippableItems = findEquippableItemsWithAssets(level.registryAccess().lookupOrThrow(Registries.ITEM));
/*     */     
/*  86 */     BlockPos origin = player.blockPosition().relative(player.getDirection(), 5);
/*     */     
/*  88 */     double padding = 3.0D;
/*  89 */     for (int materialIndex = 0; materialIndex < sortedMaterials.size(); materialIndex++) {
/*  90 */       Holder.Reference<TrimMaterial> material = (Holder.Reference)sortedMaterials.get(materialIndex);
/*  91 */       for (int patternIndex = 0; patternIndex < sortedPatterns.size(); patternIndex++) {
/*  92 */         Holder.Reference<TrimPattern> pattern = (Holder.Reference)sortedPatterns.get(patternIndex);
/*  93 */         ArmorTrim trim = new ArmorTrim(material, pattern);
/*     */         
/*  95 */         for (int itemIndex = 0; itemIndex < equippableItems.size(); itemIndex++) {
/*  96 */           Holder.Reference<Item> equippableItem = (Holder.Reference)equippableItems.get(itemIndex);
/*     */           
/*  98 */           double x = origin.getX() + 0.5D - itemIndex * 3.0D;
/*  99 */           double y = origin.getY() + 0.5D + materialIndex * 3.0D;
/* 100 */           double z = origin.getZ() + 0.5D + (patternIndex * 10);
/* 101 */           ArmorStand armorStand = new ArmorStand(level, x, y, z);
/* 102 */           armorStand.setYRot(180.0F);
/* 103 */           armorStand.setNoGravity(true);
/*     */           
/* 105 */           ItemStack stack = new ItemStack(equippableItem);
/* 106 */           Equippable equippable = (Equippable)Objects.requireNonNull((Equippable)stack.get(DataComponents.EQUIPPABLE));
/* 107 */           stack.set(DataComponents.TRIM, trim);
/* 108 */           armorStand.setItemSlot(equippable.slot(), stack);
/* 109 */           if (itemIndex == 0) {
/* 110 */             armorStand.setCustomName(((TrimPattern)trim.pattern().value()).copyWithStyle(trim.material()).copy().append(" & ").append(((TrimMaterial)trim.material().value()).description()));
/* 111 */             armorStand.setCustomNameVisible(true);
/*     */           } else {
/* 113 */             armorStand.setInvisible(true);
/*     */           } 
/* 115 */           level.addFreshEntity(armorStand);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     source.sendSuccess(() -> Component.literal("Armorstands with trimmed armor spawned around you"), true);
/*     */     
/* 122 */     return 1;
/*     */   }
/*     */   
/*     */   private static List<Holder.Reference<Item>> findEquippableItemsWithAssets(HolderLookup<Item> items) {
/* 126 */     List<Holder.Reference<Item>> result = new ArrayList<Holder.Reference<Item>>();
/* 127 */     items.listElements().forEach(item -> {
/* 128 */           Equippable equippable = (Equippable)((Item)item.value()).components().get(DataComponents.EQUIPPABLE);
/* 129 */           if (equippable != null && equippable.slot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR && equippable.assetId().isPresent()) {
/* 130 */             result.add(item);
/*     */           }
/*     */         });
/* 133 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SpawnArmorTrimsCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
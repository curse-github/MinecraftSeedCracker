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
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class TooltipDisplayComponentFix
/*     */   extends DataFix {
/*  23 */   private static final List<String> CONVERTED_ADDITIONAL_TOOLTIP_TYPES = List.of(new String[] { "minecraft:banner_patterns", "minecraft:bees", "minecraft:block_entity_data", "minecraft:block_state", "minecraft:bundle_contents", "minecraft:charged_projectiles", "minecraft:container", "minecraft:container_loot", "minecraft:firework_explosion", "minecraft:fireworks", "minecraft:instrument", "minecraft:map_id", "minecraft:painting/variant", "minecraft:pot_decorations", "minecraft:potion_contents", "minecraft:tropical_fish/pattern", "minecraft:written_book_content" });
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
/*     */ 
/*     */ 
/*     */   
/*  44 */   public TooltipDisplayComponentFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  49 */     Type<?> componentsType = getInputSchema().getType(References.DATA_COMPONENTS);
/*  50 */     Type<?> newComponentsType = getOutputSchema().getType(References.DATA_COMPONENTS);
/*  51 */     OpticFinder<?> canPlaceOnFinder = componentsType.findField("minecraft:can_place_on");
/*  52 */     OpticFinder<?> canBreakFinder = componentsType.findField("minecraft:can_break");
/*  53 */     Type<?> newCanPlaceOnType = newComponentsType.findFieldType("minecraft:can_place_on");
/*  54 */     Type<?> newCanBreakType = newComponentsType.findFieldType("minecraft:can_break");
/*  55 */     return fixTypeEverywhereTyped("TooltipDisplayComponentFix", componentsType, newComponentsType, typed -> fix(typed, canPlaceOnFinder, canBreakFinder, newCanPlaceOnType, newCanBreakType));
/*     */   }
/*     */   
/*     */   private static Typed<?> fix(Typed<?> typed, OpticFinder<?> canPlaceOnFinder, OpticFinder<?> canBreakFinder, Type<?> newCanPlaceOnType, Type<?> newCanBreakType) {
/*  59 */     Set<String> hiddenTooltips = new HashSet<String>();
/*  60 */     typed = fixAdventureModePredicate(typed, canPlaceOnFinder, newCanPlaceOnType, "minecraft:can_place_on", hiddenTooltips);
/*  61 */     typed = fixAdventureModePredicate(typed, canBreakFinder, newCanBreakType, "minecraft:can_break", hiddenTooltips);
/*     */     
/*  63 */     return typed.update(DSL.remainderFinder(), remainder -> {
/*  64 */           remainder = fixSimpleComponent(remainder, "minecraft:trim", hiddenTooltips);
/*  65 */           remainder = fixSimpleComponent(remainder, "minecraft:unbreakable", hiddenTooltips);
/*  66 */           remainder = fixComponentAndUnwrap(remainder, "minecraft:dyed_color", "rgb", hiddenTooltips);
/*  67 */           remainder = fixComponentAndUnwrap(remainder, "minecraft:attribute_modifiers", "modifiers", hiddenTooltips);
/*  68 */           remainder = fixComponentAndUnwrap(remainder, "minecraft:enchantments", "levels", hiddenTooltips);
/*  69 */           remainder = fixComponentAndUnwrap(remainder, "minecraft:stored_enchantments", "levels", hiddenTooltips);
/*  70 */           remainder = fixComponentAndUnwrap(remainder, "minecraft:jukebox_playable", "song", hiddenTooltips);
/*     */           
/*  72 */           boolean hideTooltip = remainder.get("minecraft:hide_tooltip").result().isPresent();
/*  73 */           remainder = remainder.remove("minecraft:hide_tooltip");
/*  74 */           boolean hideAdditionalTooltip = remainder.get("minecraft:hide_additional_tooltip").result().isPresent();
/*  75 */           remainder = remainder.remove("minecraft:hide_additional_tooltip");
/*     */           
/*  77 */           if (hideAdditionalTooltip)
/*     */           {
/*     */             
/*  80 */             for (String componentId : CONVERTED_ADDITIONAL_TOOLTIP_TYPES) {
/*  81 */               if (remainder.get(componentId).result().isPresent()) {
/*  82 */                 hiddenTooltips.add(componentId);
/*     */               }
/*     */             } 
/*     */           }
/*     */           
/*  87 */           if (hiddenTooltips.isEmpty() && !hideTooltip) {
/*  88 */             return remainder;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*  93 */           Objects.requireNonNull(remainder); return remainder.set("minecraft:tooltip_display", remainder.createMap(Map.of(remainder.createString("hide_tooltip"), remainder.createBoolean(hideTooltip), remainder.createString("hidden_components"), remainder.createList(hiddenTooltips.stream().map(remainder::createString)))));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   private static Dynamic<?> fixSimpleComponent(Dynamic<?> remainder, String componentId, Set<String> hiddenTooltips) { return fixRemainderComponent(remainder, componentId, hiddenTooltips, UnaryOperator.identity()); }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixComponentAndUnwrap(Dynamic<?> remainder, String componentId, String fieldName, Set<String> hiddenTooltips) {
/* 103 */     return fixRemainderComponent(remainder, componentId, hiddenTooltips, component -> 
/* 104 */         (Dynamic)DataFixUtils.orElse(component.get(fieldName).result(), component));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixRemainderComponent(Dynamic<?> remainder, String componentId, Set<String> hiddenTooltips, UnaryOperator<Dynamic<?>> fixer) {
/* 109 */     return remainder.update(componentId, component -> {
/* 110 */           boolean showInTooltip = component.get("show_in_tooltip").asBoolean(true);
/* 111 */           if (!showInTooltip) {
/* 112 */             hiddenTooltips.add(componentId);
/*     */           }
/* 114 */           return (Dynamic)fixer.apply(component.remove("show_in_tooltip"));
/*     */         });
/*     */   }
/*     */   
/*     */   private static Typed<?> fixAdventureModePredicate(Typed<?> typedComponents, OpticFinder<?> componentFinder, Type<?> newType, String componentId, Set<String> hiddenTooltips) {
/* 119 */     return typedComponents.updateTyped(componentFinder, newType, typedComponent -> 
/* 120 */         Util.writeAndReadTypedOrThrow(typedComponent, newType, ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TooltipDisplayComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
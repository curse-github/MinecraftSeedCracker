/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ToggleTooltips extends LootItemConditionalFunction {
/* 17 */   public static final MapCodec<ToggleTooltips> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(
/* 18 */         Codec.unboundedMap(DataComponentType.CODEC, Codec.BOOL).fieldOf("toggles").forGetter(()))
/* 19 */       .apply(i, ToggleTooltips::new));
/*    */   
/*    */   private final Map<DataComponentType<?>, Boolean> values;
/*    */   
/*    */   private ToggleTooltips(List<LootItemCondition> predicates, Map<DataComponentType<?>, Boolean> values) {
/* 24 */     super(predicates);
/* 25 */     this.values = values;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 30 */     itemStack.update(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT, display -> {
/* 31 */           for (Map.Entry<DataComponentType<?>, Boolean> entry : this.values.entrySet()) {
/* 32 */             boolean shown = ((Boolean)entry.getValue()).booleanValue();
/* 33 */             display = display.withHidden((DataComponentType)entry.getKey(), !shown);
/*    */           } 
/* 35 */           return display;
/*    */         });
/* 37 */     return itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public LootItemFunctionType<ToggleTooltips> getType() { return LootItemFunctions.TOGGLE_TOOLTIPS; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ToggleTooltips.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
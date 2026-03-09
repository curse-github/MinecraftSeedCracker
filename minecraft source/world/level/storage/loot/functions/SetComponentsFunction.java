/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetComponentsFunction extends LootItemConditionalFunction {
/* 14 */   public static final MapCodec<SetComponentsFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(DataComponentPatch.CODEC
/* 15 */         .fieldOf("components").forGetter(()))
/* 16 */       .apply(i, SetComponentsFunction::new));
/*    */   
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   private SetComponentsFunction(List<LootItemCondition> predicates, DataComponentPatch components) {
/* 21 */     super(predicates);
/* 22 */     this.components = components;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public LootItemFunctionType<SetComponentsFunction> getType() { return LootItemFunctions.SET_COMPONENTS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 32 */     itemStack.applyComponentsAndValidate(this.components);
/* 33 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 37 */   public static <T> LootItemConditionalFunction.Builder<?> setComponent(DataComponentType<T> type, T value) { return simpleBuilder(conditions -> new SetComponentsFunction(conditions, DataComponentPatch.builder().set(type, value).build())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetComponentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
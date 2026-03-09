/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetOminousBottleAmplifierFunction extends LootItemConditionalFunction {
/* 19 */   static final MapCodec<SetOminousBottleAmplifierFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(NumberProviders.CODEC
/* 20 */         .fieldOf("amplifier").forGetter(()))
/* 21 */       .apply(i, SetOminousBottleAmplifierFunction::new));
/*    */   
/*    */   private final NumberProvider amplifierGenerator;
/*    */   
/*    */   private SetOminousBottleAmplifierFunction(List<LootItemCondition> predicates, NumberProvider amplifierGenerator) {
/* 26 */     super(predicates);
/* 27 */     this.amplifierGenerator = amplifierGenerator;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.amplifierGenerator.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public LootItemFunctionType<SetOminousBottleAmplifierFunction> getType() { return LootItemFunctions.SET_OMINOUS_BOTTLE_AMPLIFIER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 42 */     int amplifierValue = Mth.clamp(this.amplifierGenerator.getInt(context), 0, 4);
/* 43 */     itemStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(amplifierValue));
/* 44 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 48 */   public NumberProvider amplifier() { return this.amplifierGenerator; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static LootItemConditionalFunction.Builder<?> setAmplifier(NumberProvider generator) { return simpleBuilder(conditions -> new SetOminousBottleAmplifierFunction(conditions, generator)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetOminousBottleAmplifierFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
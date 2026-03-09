/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetItemCountFunction extends LootItemConditionalFunction {
/* 17 */   public static final MapCodec<SetItemCountFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(NumberProviders.CODEC
/* 18 */           .fieldOf("count").forGetter(()), Codec.BOOL
/* 19 */           .fieldOf("add").orElse(Boolean.valueOf(false)).forGetter(())))
/* 20 */       .apply(i, SetItemCountFunction::new));
/*    */   
/*    */   private final NumberProvider value;
/*    */   private final boolean add;
/*    */   
/*    */   private SetItemCountFunction(List<LootItemCondition> predicates, NumberProvider value, boolean add) {
/* 26 */     super(predicates);
/* 27 */     this.value = value;
/* 28 */     this.add = add;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public LootItemFunctionType<SetItemCountFunction> getType() { return LootItemFunctions.SET_COUNT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.value.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 43 */     int base = this.add ? itemStack.getCount() : 0;
/* 44 */     itemStack.setCount(base + this.value.getInt(context));
/* 45 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider value) { return simpleBuilder(conditions -> new SetItemCountFunction(conditions, value, false)); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider value, boolean add) { return simpleBuilder(conditions -> new SetItemCountFunction(conditions, value, add)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetItemCountFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
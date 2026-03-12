/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LimitCount extends LootItemConditionalFunction {
/* 15 */   public static final MapCodec<LimitCount> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(IntRange.CODEC
/* 16 */         .fieldOf("limit").forGetter(()))
/* 17 */       .apply(i, LimitCount::new));
/*    */   
/*    */   private final IntRange limiter;
/*    */   
/*    */   private LimitCount(List<LootItemCondition> predicates, IntRange limiter) {
/* 22 */     super(predicates);
/* 23 */     this.limiter = limiter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public LootItemFunctionType<LimitCount> getType() { return LootItemFunctions.LIMIT_COUNT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public Set<ContextKey<?>> getReferencedContextParams() { return this.limiter.getReferencedContextParams(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 38 */     int limit = this.limiter.clamp(context, itemStack.getCount());
/* 39 */     itemStack.setCount(limit);
/* 40 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 44 */   public static LootItemConditionalFunction.Builder<?> limitCount(IntRange limiter) { return simpleBuilder(conditions -> new LimitCount(conditions, limiter)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LimitCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
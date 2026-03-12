/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ApplyExplosionDecay extends LootItemConditionalFunction {
/* 14 */   public static final MapCodec<ApplyExplosionDecay> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).apply(i, ApplyExplosionDecay::new));
/*    */ 
/*    */   
/* 17 */   private ApplyExplosionDecay(List<LootItemCondition> predicates) { super(predicates); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public LootItemFunctionType<ApplyExplosionDecay> getType() { return LootItemFunctions.EXPLOSION_DECAY; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 27 */     Float explosionRadius = (Float)context.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
/*    */     
/* 29 */     if (explosionRadius != null) {
/* 30 */       RandomSource random = context.getRandom();
/*    */       
/* 32 */       float probability = 1.0F / explosionRadius.floatValue();
/* 33 */       int currentCount = itemStack.getCount();
/* 34 */       int resultCount = 0;
/* 35 */       for (int i = 0; i < currentCount; i++) {
/* 36 */         if (random.nextFloat() <= probability) {
/* 37 */           resultCount++;
/*    */         }
/*    */       } 
/*    */       
/* 41 */       itemStack.setCount(resultCount);
/*    */     } 
/* 43 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 47 */   public static LootItemConditionalFunction.Builder<?> explosionDecay() { return simpleBuilder(ApplyExplosionDecay::new); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyExplosionDecay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
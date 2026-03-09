/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 41 */   private final ImmutableMap.Builder<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = ImmutableMap.builder();
/*    */ 
/*    */   
/* 44 */   public static Builder effects() { return new Builder(); }
/*    */ 
/*    */   
/*    */   public Builder and(Holder<MobEffect> effect) {
/* 48 */     this.effectMap.put(effect, new MobEffectsPredicate.MobEffectInstancePredicate());
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder and(Holder<MobEffect> effect, MobEffectsPredicate.MobEffectInstancePredicate predicate) {
/* 53 */     this.effectMap.put(effect, predicate);
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 58 */   public Optional<MobEffectsPredicate> build() { return Optional.of(new MobEffectsPredicate(this.effectMap.build())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MobEffectsPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
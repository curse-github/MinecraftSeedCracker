/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<SetStewEffectFunction.Builder>
/*    */ {
/* 80 */   private final ImmutableList.Builder<SetStewEffectFunction.EffectEntry> effects = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */   
/* 84 */   protected Builder getThis() { return this; }
/*    */ 
/*    */   
/*    */   public Builder withEffect(Holder<MobEffect> effect, NumberProvider duration) {
/* 88 */     this.effects.add(new SetStewEffectFunction.EffectEntry(effect, duration));
/* 89 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 94 */   public LootItemFunction build() { return new SetStewEffectFunction(getConditions(), this.effects.build()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
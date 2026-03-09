/*    */ package net.minecraft.world.timeline;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.LongSupplier;
/*    */ import net.minecraft.util.KeyframeTrack;
/*    */ import net.minecraft.util.KeyframeTrackSampler;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributeLayer;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ 
/*    */ public class AttributeTrackSampler<Value, Argument>
/*    */   extends Object
/*    */   implements EnvironmentAttributeLayer.TimeBased<Value>
/*    */ {
/*    */   private final AttributeModifier<Value, Argument> modifier;
/*    */   private final KeyframeTrackSampler<Argument> argumentSampler;
/*    */   private final LongSupplier dayTimeGetter;
/*    */   private int cachedTickId;
/*    */   private Argument cachedArgument;
/*    */   
/*    */   public AttributeTrackSampler(Optional<Integer> periodTicks, AttributeModifier<Value, Argument> modifier, KeyframeTrack<Argument> argumentTrack, LerpFunction<Argument> argumentLerp, LongSupplier dayTimeGetter) {
/* 22 */     this.modifier = modifier;
/* 23 */     this.dayTimeGetter = dayTimeGetter;
/* 24 */     this.argumentSampler = argumentTrack.bakeSampler(periodTicks, argumentLerp);
/*    */   }
/*    */ 
/*    */   
/*    */   public Value applyTimeBased(Value baseValue, int cacheTickId) {
/* 29 */     if (this.cachedArgument == null || cacheTickId != this.cachedTickId) {
/* 30 */       this.cachedTickId = cacheTickId;
/* 31 */       this.cachedArgument = this.argumentSampler.sample(this.dayTimeGetter.getAsLong());
/*    */     } 
/* 33 */     return (Value)this.modifier.apply(baseValue, this.cachedArgument);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\timeline\AttributeTrackSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
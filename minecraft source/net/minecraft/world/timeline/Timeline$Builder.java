/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
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
/*     */ public class Builder
/*     */ {
/* 102 */   private Optional<Integer> periodTicks = Optional.empty();
/* 103 */   private final ImmutableMap.Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder setPeriodTicks(int periodTicks) {
/* 109 */     this.periodTicks = Optional.of(Integer.valueOf(periodTicks));
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Argument> Builder addModifierTrack(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier, Consumer<KeyframeTrack.Builder<Argument>> builder) {
/* 114 */     attribute.type().checkAllowedModifier(modifier);
/* 115 */     KeyframeTrack.Builder<Argument> argumentTrack = new KeyframeTrack.Builder<Argument>();
/* 116 */     builder.accept(argumentTrack);
/* 117 */     this.tracks.put(attribute, new AttributeTrack(modifier, argumentTrack.build()));
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 122 */   public <Value> Builder addTrack(EnvironmentAttribute<Value> attribute, Consumer<KeyframeTrack.Builder<Value>> builder) { return addModifierTrack(attribute, AttributeModifier.override(), builder); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public Timeline build() { return new Timeline(this.periodTicks, this.tracks.build()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\timeline\Timeline$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
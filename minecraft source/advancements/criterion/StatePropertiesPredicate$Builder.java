/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
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
/* 149 */   private final ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers = ImmutableList.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static Builder properties() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public Builder hasProperty(Property<?> property, String value) {
/* 159 */     this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(property.getName(), new StatePropertiesPredicate.ExactMatcher(value)));
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 164 */   public Builder hasProperty(Property<Integer> property, int value) { return hasProperty(property, Integer.toString(value)); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public Builder hasProperty(Property<Boolean> property, boolean value) { return hasProperty(property, Boolean.toString(value)); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public <T extends Comparable<T> & StringRepresentable> Builder hasProperty(Property<T> property, T value) { return hasProperty(property, ((StringRepresentable)value).getSerializedName()); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public Optional<StatePropertiesPredicate> build() { return Optional.of(new StatePropertiesPredicate(this.matchers.build())); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\StatePropertiesPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
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
/*     */ public class Builder<O, S extends StateHolder<O, S>>
/*     */   extends Object
/*     */ {
/*     */   private final O owner;
/*     */   private final Map<String, Property<?>> properties;
/*     */   
/*     */   public Builder(O owner) {
/* 121 */     this.properties = Maps.newHashMap();
/*     */ 
/*     */     
/* 124 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public Builder<O, S> add(Property... properties) {
/* 128 */     for (Property<?> property : properties) {
/* 129 */       validateProperty(property);
/* 130 */       this.properties.put(property.getName(), property);
/*     */     } 
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> void validateProperty(Property<T> property) {
/* 136 */     String name = property.getName();
/* 137 */     if (!StateDefinition.NAME_PATTERN.matcher(name).matches()) {
/* 138 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + String.valueOf(this.owner));
/*     */     }
/*     */     
/* 141 */     Collection<T> values = property.getPossibleValues();
/* 142 */     if (values.size() <= 1) {
/* 143 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + String.valueOf(this.owner) + " with <= 1 possible values");
/*     */     }
/*     */     
/* 146 */     for (Iterator iterator = values.iterator(); iterator.hasNext(); ) { T comparable = (T)(Comparable)iterator.next();
/* 147 */       String valueName = property.getName(comparable);
/* 148 */       if (!StateDefinition.NAME_PATTERN.matcher(valueName).matches()) {
/* 149 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + String.valueOf(this.owner) + " with invalidly named value: " + name);
/*     */       } }
/*     */ 
/*     */     
/* 153 */     if (this.properties.containsKey(name)) {
/* 154 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + String.valueOf(this.owner));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 159 */   public StateDefinition<O, S> create(Function<O, S> defaultState, StateDefinition.Factory<O, S> factory) { return new StateDefinition(defaultState, this.owner, factory, this.properties); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\StateDefinition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
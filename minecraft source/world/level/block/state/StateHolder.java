/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class StateHolder<O, S>
/*     */   extends Object {
/*     */   public static final String NAME_TAG = "Name";
/*     */   public static final String PROPERTIES_TAG = "Properties";
/*     */   
/*  22 */   private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION = new Function<Map.Entry<Property<?>, Comparable<?>>, String>()
/*     */     {
/*     */       public String apply(Map.Entry<Property<?>, Comparable<?>> entry) {
/*  25 */         if (entry == null) {
/*  26 */           return "<NULL>";
/*     */         }
/*     */         
/*  29 */         Property<?> property = (Property)entry.getKey();
/*  30 */         return property.getName() + "=" + property.getName();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  35 */       private <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> value) { return property.getName(value); }
/*     */     };
/*     */ 
/*     */   
/*     */   protected final O owner;
/*     */   
/*     */   private final Reference2ObjectArrayMap<Property<?>, Comparable<?>> values;
/*     */   
/*     */   private Map<Property<?>, S[]> neighbours;
/*     */   protected final MapCodec<S> propertiesCodec;
/*     */   
/*     */   protected StateHolder(O owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<S> propertiesCodec) {
/*  47 */     this.owner = owner;
/*  48 */     this.values = values;
/*  49 */     this.propertiesCodec = propertiesCodec;
/*     */   }
/*     */ 
/*     */   
/*  53 */   public <T extends Comparable<T>> S cycle(Property<T> property) { return (S)setValue(property, (Comparable)findNextInCollection(property.getPossibleValues(), getValue(property))); }
/*     */ 
/*     */   
/*     */   protected static <T> T findNextInCollection(List<T> values, T current) {
/*  57 */     int nextIndex = values.indexOf(current) + 1;
/*  58 */     return (T)((nextIndex == values.size()) ? values.getFirst() : values.get(nextIndex));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  63 */     StringBuilder builder = new StringBuilder();
/*  64 */     builder.append(this.owner);
/*     */     
/*  66 */     if (!getValues().isEmpty()) {
/*  67 */       builder.append('[');
/*  68 */       builder.append((String)getValues().entrySet().stream().map(PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
/*  69 */       builder.append(']');
/*     */     } 
/*     */     
/*  72 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public final boolean equals(Object obj) { return super.equals(obj); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public int hashCode() { return super.hashCode(); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public Collection<Property<?>> getProperties() { return Collections.unmodifiableCollection(this.values.keySet()); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean hasProperty(Property<?> property) { return this.values.containsKey(property); }
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>> T getValue(Property<T> property) {
/*  96 */     Comparable<?> value = (Comparable)this.values.get(property);
/*  97 */     if (value == null) {
/*  98 */       throw new IllegalArgumentException("Cannot get property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner));
/*     */     }
/*     */     
/* 101 */     return (T)(Comparable)property.getValueClass().cast(value);
/*     */   }
/*     */ 
/*     */   
/* 105 */   public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> property) { return Optional.ofNullable(getNullableValue(property)); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public <T extends Comparable<T>> T getValueOrElse(Property<T> property, T defaultValue) { return (T)(Comparable)Objects.requireNonNullElse(getNullableValue(property), defaultValue); }
/*     */ 
/*     */   
/*     */   private <T extends Comparable<T>> T getNullableValue(Property<T> property) {
/* 113 */     Comparable<?> value = (Comparable)this.values.get(property);
/* 114 */     if (value == null) {
/* 115 */       return null;
/*     */     }
/* 117 */     return (T)(Comparable)property.getValueClass().cast(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S setValue(Property<T> property, V value) {
/* 127 */     Comparable<?> oldValue = (Comparable)this.values.get(property);
/* 128 */     if (oldValue == null) {
/* 129 */       throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner));
/*     */     }
/*     */     
/* 132 */     return (S)setValueInternal(property, value, oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S trySetValue(Property<T> property, V value) {
/* 142 */     Comparable<?> oldValue = (Comparable)this.values.get(property);
/* 143 */     if (oldValue == null) {
/* 144 */       return (S)this;
/*     */     }
/*     */     
/* 147 */     return (S)setValueInternal(property, value, oldValue);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends Comparable<T>, V extends T> S setValueInternal(Property<T> property, V value, Comparable<?> oldValue) {
/* 152 */     if (oldValue.equals(value)) {
/* 153 */       return (S)this;
/*     */     }
/*     */     
/* 156 */     int internalIndex = property.getInternalIndex(value);
/* 157 */     if (internalIndex < 0) {
/* 158 */       throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " to " + String.valueOf(value) + " on " + String.valueOf(this.owner) + ", it is not an allowed value");
/*     */     }
/* 160 */     return (S)(Object[])this.neighbours.get(property)[internalIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> statesByValues) {
/* 165 */     if (this.neighbours != null) {
/* 166 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 169 */     Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(this.values.size());
/* 170 */     for (ObjectIterator objectIterator = this.values.entrySet().iterator(); objectIterator.hasNext(); ) { Map.Entry<Property<?>, Comparable<?>> entry = (Map.Entry)objectIterator.next();
/* 171 */       Property<?> property = (Property)entry.getKey();
/*     */       
/* 173 */       reference2ObjectArrayMap.put(property, property.getPossibleValues().stream()
/* 174 */           .map(value -> statesByValues.get(makeNeighbourValues(property, value)))
/* 175 */           .toArray()); }
/*     */ 
/*     */ 
/*     */     
/* 179 */     this.neighbours = reference2ObjectArrayMap;
/*     */   }
/*     */   
/*     */   private Map<Property<?>, Comparable<?>> makeNeighbourValues(Property<?> property, Comparable<?> value) {
/* 183 */     Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(this.values);
/* 184 */     reference2ObjectArrayMap.put(property, value);
/* 185 */     return reference2ObjectArrayMap;
/*     */   }
/*     */ 
/*     */   
/* 189 */   public Map<Property<?>, Comparable<?>> getValues() { return this.values; }
/*     */ 
/*     */   
/*     */   protected static <O, S extends StateHolder<O, S>> Codec<S> codec(Codec<O> ownerCodec, Function<O, S> defaultState) {
/* 193 */     return ownerCodec.dispatch("Name", s -> s.owner, o -> {
/* 194 */           S defaultValue = (S)(StateHolder)defaultState.apply(o);
/* 195 */           if (defaultValue.getValues().isEmpty()) {
/* 196 */             return MapCodec.unit(defaultValue);
/*     */           }
/* 198 */           return defaultValue.propertiesCodec.codec().lenientOptionalFieldOf("Properties").xmap((), Optional::of);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\StateHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
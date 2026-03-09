/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSortedMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class StateDefinition<O, S extends StateHolder<O, S>> extends Object {
/*  28 */   private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
/*     */   
/*     */   private final O owner;
/*     */   private final ImmutableSortedMap<String, Property<?>> propertiesByName;
/*     */   private final ImmutableList<S> states;
/*     */   
/*     */   protected StateDefinition(Function<O, S> defaultState, O owner, Factory<O, S> factory, Map<String, Property<?>> properties) {
/*  35 */     this.owner = owner;
/*  36 */     this.propertiesByName = ImmutableSortedMap.copyOf(properties);
/*     */     
/*  38 */     Supplier<S> defaultSupplier = () -> (StateHolder)defaultState.apply(owner);
/*  39 */     MapCodec<S> codec = MapCodec.of(Encoder.empty(), Decoder.unit(defaultSupplier));
/*  40 */     for (UnmodifiableIterator unmodifiableIterator1 = this.propertiesByName.entrySet().iterator(); unmodifiableIterator1.hasNext(); ) { Map.Entry<String, Property<?>> entry = (Map.Entry)unmodifiableIterator1.next();
/*  41 */       codec = appendPropertyCodec(codec, defaultSupplier, (String)entry.getKey(), (Property)entry.getValue()); }
/*     */ 
/*     */     
/*  44 */     MapCodec<S> propertiesCodec = codec;
/*     */ 
/*     */     
/*  47 */     Map<Map<Property<?>, Comparable<?>>, S> statesByValues = Maps.newLinkedHashMap();
/*  48 */     List<S> states = Lists.newArrayList();
/*     */     
/*  50 */     Stream<List<Pair<Property<?>, Comparable<?>>>> stream = Stream.of(Collections.emptyList());
/*  51 */     for (UnmodifiableIterator unmodifiableIterator2 = this.propertiesByName.values().iterator(); unmodifiableIterator2.hasNext(); ) { Property<?> property = (Property)unmodifiableIterator2.next();
/*  52 */       stream = stream.flatMap(list -> property.getPossibleValues().stream().map(())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     stream.forEach(list -> {
/*  60 */           Reference2ObjectArrayMap<Property<?>, Comparable<?>> map = new Reference2ObjectArrayMap<Property<?>, Comparable<?>>(list.size());
/*  61 */           for (Pair<Property<?>, Comparable<?>> pair : list) {
/*  62 */             map.put((Property)pair.getFirst(), (Comparable)pair.getSecond());
/*     */           }
/*  64 */           S blockState = (S)(StateHolder)factory.create(owner, map, propertiesCodec);
/*     */           
/*  66 */           statesByValues.put(map, blockState);
/*  67 */           states.add(blockState);
/*     */         });
/*     */     
/*  70 */     for (Iterator iterator = states.iterator(); iterator.hasNext(); ) { S blockState = (S)(StateHolder)iterator.next();
/*  71 */       blockState.populateNeighbours(statesByValues); }
/*     */ 
/*     */     
/*  74 */     this.states = ImmutableList.copyOf(states);
/*     */   }
/*     */   
/*     */   private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(MapCodec<S> codec, Supplier<S> defaultSupplier, String name, Property<T> property) {
/*  78 */     return Codec.mapPair(codec, property
/*     */         
/*  80 */         .valueCodec().fieldOf(name).orElseGet(e -> {  }() -> property.value((StateHolder)defaultSupplier.get())))
/*  81 */       .xmap(pair -> 
/*  82 */         (StateHolder)((StateHolder)pair.getFirst()).setValue(property, ((Property.Value)pair.getSecond()).value()), state -> 
/*  83 */         Pair.of(state, property.value(state)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public ImmutableList<S> getPossibleStates() { return this.states; }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public S any() { return (S)(StateHolder)this.states.get(0); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public O getOwner() { return (O)this.owner; }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public Collection<Property<?>> getProperties() { return this.propertiesByName.values(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public String toString() { return MoreObjects.toStringHelper(this)
/* 106 */       .add("block", this.owner)
/* 107 */       .add("properties", this.propertiesByName.values().stream().map(Property::getName).collect(Collectors.toList()))
/* 108 */       .toString(); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public Property<?> getProperty(String name) { return (Property)this.propertiesByName.get(name); }
/*     */   
/*     */   public static interface Factory<O, S> {
/*     */     S create(O param1O, Reference2ObjectArrayMap<Property<?>, Comparable<?>> param1Reference2ObjectArrayMap, MapCodec<S> param1MapCodec); }
/*     */   
/*     */   public static class Builder<O, S extends StateHolder<O, S>> extends Object { private final O owner;
/*     */     private final Map<String, Property<?>> properties;
/*     */     
/*     */     public Builder(O owner) {
/* 121 */       this.properties = Maps.newHashMap();
/*     */ 
/*     */       
/* 124 */       this.owner = owner;
/*     */     }
/*     */     
/*     */     public Builder<O, S> add(Property... properties) {
/* 128 */       for (Property<?> property : properties) {
/* 129 */         validateProperty(property);
/* 130 */         this.properties.put(property.getName(), property);
/*     */       } 
/* 132 */       return this;
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> void validateProperty(Property<T> property) {
/* 136 */       String name = property.getName();
/* 137 */       if (!StateDefinition.NAME_PATTERN.matcher(name).matches()) {
/* 138 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + String.valueOf(this.owner));
/*     */       }
/*     */       
/* 141 */       Collection<T> values = property.getPossibleValues();
/* 142 */       if (values.size() <= 1) {
/* 143 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + String.valueOf(this.owner) + " with <= 1 possible values");
/*     */       }
/*     */       
/* 146 */       for (Iterator iterator = values.iterator(); iterator.hasNext(); ) { T comparable = (T)(Comparable)iterator.next();
/* 147 */         String valueName = property.getName(comparable);
/* 148 */         if (!StateDefinition.NAME_PATTERN.matcher(valueName).matches()) {
/* 149 */           throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + String.valueOf(this.owner) + " with invalidly named value: " + name);
/*     */         } }
/*     */ 
/*     */       
/* 153 */       if (this.properties.containsKey(name)) {
/* 154 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + String.valueOf(this.owner));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 159 */     public StateDefinition<O, S> create(Function<O, S> defaultState, StateDefinition.Factory<O, S> factory) { return new StateDefinition(defaultState, this.owner, factory, this.properties); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\StateDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
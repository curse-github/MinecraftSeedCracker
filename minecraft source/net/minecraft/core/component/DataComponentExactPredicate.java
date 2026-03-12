/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class DataComponentExactPredicate extends Object implements Predicate<DataComponentGetter> {
/*  17 */   public static final Codec<DataComponentExactPredicate> CODEC = DataComponentType.VALUE_MAP_CODEC.xmap(map -> 
/*  18 */       new DataComponentExactPredicate((List)map.entrySet().stream().map(TypedDataComponent::fromEntryUnchecked).collect(Collectors.toList())), predicate -> 
/*  19 */       (Map)predicate.expectedComponents.stream().filter(()).collect(Collectors.toMap(TypedDataComponent::type, TypedDataComponent::value)));
/*     */ 
/*     */   
/*  22 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentExactPredicate> STREAM_CODEC = TypedDataComponent.STREAM_CODEC.apply(ByteBufCodecs.list())
/*  23 */     .map(DataComponentExactPredicate::new, predicate -> predicate.expectedComponents);
/*     */   
/*  25 */   public static final DataComponentExactPredicate EMPTY = new DataComponentExactPredicate(List.of());
/*     */   
/*     */   private final List<TypedDataComponent<?>> expectedComponents;
/*     */ 
/*     */   
/*  30 */   private DataComponentExactPredicate(List<TypedDataComponent<?>> expectedComponents) { this.expectedComponents = expectedComponents; }
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static Builder builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static <T> DataComponentExactPredicate expect(DataComponentType<T> type, T value) { return new DataComponentExactPredicate(List.of(new TypedDataComponent(type, value))); }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static DataComponentExactPredicate allOf(DataComponentMap components) { return new DataComponentExactPredicate(ImmutableList.copyOf(components)); }
/*     */ 
/*     */   
/*     */   public static DataComponentExactPredicate someOf(DataComponentMap components, DataComponentType... types) {
/*  46 */     Builder result = new Builder();
/*  47 */     for (DataComponentType<?> type : types) {
/*  48 */       TypedDataComponent<?> value = components.getTyped(type);
/*  49 */       if (value != null) {
/*  50 */         result.expect(value);
/*     */       }
/*     */     } 
/*  53 */     return result.build();
/*     */   }
/*     */ 
/*     */   
/*  57 */   public boolean isEmpty() { return this.expectedComponents.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public boolean equals(Object obj) { if (obj instanceof DataComponentExactPredicate) { DataComponentExactPredicate predicate = (DataComponentExactPredicate)obj; if (this.expectedComponents.equals(predicate.expectedComponents)); }  return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public int hashCode() { return this.expectedComponents.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public String toString() { return this.expectedComponents.toString(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean test(DataComponentGetter actualComponents) {
/*  77 */     for (TypedDataComponent<?> expected : this.expectedComponents) {
/*  78 */       Object actual = actualComponents.get(expected.type());
/*  79 */       if (!Objects.equals(expected.value(), actual)) {
/*  80 */         return false;
/*     */       }
/*     */     } 
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  87 */   public boolean alwaysMatches() { return this.expectedComponents.isEmpty(); }
/*     */ 
/*     */   
/*     */   public DataComponentPatch asPatch() {
/*  91 */     DataComponentPatch.Builder patch = DataComponentPatch.builder();
/*  92 */     for (TypedDataComponent<?> component : this.expectedComponents) {
/*  93 */       patch.set(component);
/*     */     }
/*  95 */     return patch.build();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  99 */     private final List<TypedDataComponent<?>> expectedComponents = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     public <T> Builder expect(TypedDataComponent<T> value) { return expect(value.type(), value.value()); }
/*     */ 
/*     */     
/*     */     public <T> Builder expect(DataComponentType<? super T> type, T value) {
/* 109 */       for (TypedDataComponent<?> component : this.expectedComponents) {
/* 110 */         if (component.type() == type) {
/* 111 */           throw new IllegalArgumentException("Predicate already has component of type: '" + String.valueOf(type) + "'");
/*     */         }
/*     */       } 
/* 114 */       this.expectedComponents.add(new TypedDataComponent(type, value));
/* 115 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 119 */     public DataComponentExactPredicate build() { return new DataComponentExactPredicate(List.copyOf(this.expectedComponents)); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentExactPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
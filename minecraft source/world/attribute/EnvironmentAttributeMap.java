/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ 
/*     */ public final class EnvironmentAttributeMap {
/*  19 */   public static final EnvironmentAttributeMap EMPTY = new EnvironmentAttributeMap(Map.of());
/*     */ 
/*     */   
/*  22 */   public static final Codec<EnvironmentAttributeMap> CODEC = Codec.lazyInitialized(() -> 
/*  23 */       Codec.dispatchedMap(EnvironmentAttributes.CODEC, Util.memoize(Entry::createCodec))
/*  24 */       .xmap(EnvironmentAttributeMap::new, ()));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   public static final Codec<EnvironmentAttributeMap> NETWORK_CODEC = CODEC.xmap(EnvironmentAttributeMap::filterSyncable, EnvironmentAttributeMap::filterSyncable);
/*     */   
/*  31 */   public static final Codec<EnvironmentAttributeMap> CODEC_ONLY_POSITIONAL = CODEC.validate(map -> {
/*  32 */         List<EnvironmentAttribute<?>> illegalAttributes = map.keySet().stream().filter(()).toList();
/*  33 */         if (!illegalAttributes.isEmpty()) {
/*  34 */           return DataResult.error(());
/*     */         }
/*  36 */         return DataResult.success(map);
/*     */       });
/*     */   private final Map<EnvironmentAttribute<?>, Entry<?, ?>> entries;
/*     */   
/*  40 */   private static EnvironmentAttributeMap filterSyncable(EnvironmentAttributeMap attributes) { return new EnvironmentAttributeMap(Map.copyOf(
/*  41 */           Maps.filterKeys(attributes.entries, EnvironmentAttribute::isSyncable))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private EnvironmentAttributeMap(Map<EnvironmentAttribute<?>, Entry<?, ?>> entries) { this.entries = entries; }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static Builder builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public <Value> Entry<Value, ?> get(EnvironmentAttribute<Value> attribute) { return (Entry)this.entries.get(attribute); }
/*     */ 
/*     */   
/*     */   public <Value> Value applyModifier(EnvironmentAttribute<Value> attribute, Value baseValue) {
/*  61 */     Entry<Value, ?> entry = get(attribute);
/*  62 */     return (Value)((entry != null) ? entry.applyModifier(baseValue) : baseValue);
/*     */   }
/*     */ 
/*     */   
/*  66 */   public boolean contains(EnvironmentAttribute<?> attribute) { return this.entries.containsKey(attribute); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public Set<EnvironmentAttribute<?>> keySet() { return this.entries.keySet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  75 */     if (obj == this) {
/*  76 */       return true;
/*     */     }
/*  78 */     if (obj instanceof EnvironmentAttributeMap) { EnvironmentAttributeMap attributes = (EnvironmentAttributeMap)obj; if (this.entries.equals(attributes.entries)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public int hashCode() { return this.entries.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public String toString() { return this.entries.toString(); }
/*     */   public static final class Entry<Value, Argument> extends Record { private final Argument argument; private final AttributeModifier<Value, Argument> modifier;
/*     */     
/*  91 */     public Entry(Argument argument, AttributeModifier<Value, Argument> modifier) { this.argument = argument; this.modifier = modifier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  91 */       //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry<TValue;TArgument;>; } public Argument argument() { return (Argument)this.argument; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry<TValue;TArgument;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  91 */       //   0	8	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry<TValue;TArgument;>; } public AttributeModifier<Value, Argument> modifier() { return this.modifier; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static <Value> Codec<Entry<Value, ?>> createCodec(EnvironmentAttribute<Value> attribute) {
/*  98 */       Codec<Entry<Value, ?>> fullCodec = attribute.type().modifierCodec().dispatch("modifier", Entry::modifier, Util.memoize(modifier -> createFullCodec(attribute, modifier)));
/*     */       
/* 100 */       return Codec.either(attribute.valueCodec(), fullCodec).xmap(either -> 
/* 101 */           (Entry)either.map((), ()), entry -> {
/*     */             
/* 103 */             if (entry.modifier == AttributeModifier.override()) {
/* 104 */               return Either.left(entry.argument());
/*     */             }
/* 106 */             return Either.right(entry);
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <Value, Argument> MapCodec<Entry<Value, Argument>> createFullCodec(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier) {
/* 113 */       return RecordCodecBuilder.mapCodec(i -> i.group(modifier
/* 114 */             .argumentCodec(attribute).fieldOf("argument").forGetter(Entry::argument))
/* 115 */           .apply(i, ()));
/*     */     }
/*     */ 
/*     */     
/* 119 */     public Value applyModifier(Value subject) { return (Value)this.modifier.apply(subject, this.argument); } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 124 */     private final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder putAll(EnvironmentAttributeMap map) {
/* 130 */       this.entries.putAll(map.entries);
/* 131 */       return this;
/*     */     }
/*     */     
/*     */     public <Value, Parameter> Builder modify(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Parameter> modifier, Parameter value) {
/* 135 */       attribute.type().checkAllowedModifier(modifier);
/* 136 */       this.entries.put(attribute, new EnvironmentAttributeMap.Entry(value, modifier));
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 141 */     public <Value> Builder set(EnvironmentAttribute<Value> attribute, Value value) { return modify(attribute, AttributeModifier.override(), value); }
/*     */ 
/*     */     
/*     */     public EnvironmentAttributeMap build() {
/* 145 */       if (this.entries.isEmpty()) {
/* 146 */         return EnvironmentAttributeMap.EMPTY;
/*     */       }
/* 148 */       return new EnvironmentAttributeMap(Map.copyOf(this.entries));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
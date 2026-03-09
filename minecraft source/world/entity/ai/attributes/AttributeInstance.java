/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class AttributeInstance {
/*     */   private final Holder<Attribute> attribute;
/*     */   private final Map<AttributeModifier.Operation, Map<Identifier, AttributeModifier>> modifiersByOperation;
/*     */   private final Map<Identifier, AttributeModifier> modifierById;
/*     */   private final Map<Identifier, AttributeModifier> permanentModifiers;
/*     */   
/*     */   public AttributeInstance(Holder<Attribute> attribute, Consumer<AttributeInstance> onDirty) {
/*  23 */     this.modifiersByOperation = Maps.newEnumMap(AttributeModifier.Operation.class);
/*  24 */     this.modifierById = new Object2ObjectArrayMap();
/*  25 */     this.permanentModifiers = new Object2ObjectArrayMap();
/*     */     
/*  27 */     this.dirty = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     this.attribute = attribute;
/*  33 */     this.onDirty = onDirty;
/*  34 */     this.baseValue = ((Attribute)attribute.value()).getDefaultValue();
/*     */   }
/*     */   private double baseValue; private boolean dirty; private double cachedValue; private final Consumer<AttributeInstance> onDirty;
/*     */   
/*  38 */   public Holder<Attribute> getAttribute() { return this.attribute; }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public double getBaseValue() { return this.baseValue; }
/*     */ 
/*     */   
/*     */   public void setBaseValue(double baseValue) {
/*  46 */     if (baseValue == this.baseValue) {
/*     */       return;
/*     */     }
/*  49 */     this.baseValue = baseValue;
/*  50 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  55 */   Map<Identifier, AttributeModifier> getModifiers(AttributeModifier.Operation operation) { return (Map)this.modifiersByOperation.computeIfAbsent(operation, key -> new Object2ObjectOpenHashMap()); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public Set<AttributeModifier> getModifiers() { return ImmutableSet.copyOf(this.modifierById.values()); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public Set<AttributeModifier> getPermanentModifiers() { return ImmutableSet.copyOf(this.permanentModifiers.values()); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public AttributeModifier getModifier(Identifier id) { return (AttributeModifier)this.modifierById.get(id); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean hasModifier(Identifier modifier) { return (this.modifierById.get(modifier) != null); }
/*     */ 
/*     */   
/*     */   private void addModifier(AttributeModifier modifier) {
/*  75 */     AttributeModifier previous = (AttributeModifier)this.modifierById.putIfAbsent(modifier.id(), modifier);
/*  76 */     if (previous != null) {
/*  77 */       throw new IllegalArgumentException("Modifier is already applied on this attribute!");
/*     */     }
/*  79 */     getModifiers(modifier.operation()).put(modifier.id(), modifier);
/*  80 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addOrUpdateTransientModifier(AttributeModifier modifier) {
/*  84 */     AttributeModifier oldModifier = (AttributeModifier)this.modifierById.put(modifier.id(), modifier);
/*  85 */     if (modifier == oldModifier) {
/*     */       return;
/*     */     }
/*  88 */     getModifiers(modifier.operation()).put(modifier.id(), modifier);
/*  89 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*  93 */   public void addTransientModifier(AttributeModifier modifier) { addModifier(modifier); }
/*     */ 
/*     */   
/*     */   public void addOrReplacePermanentModifier(AttributeModifier modifier) {
/*  97 */     removeModifier(modifier.id());
/*  98 */     addModifier(modifier);
/*  99 */     this.permanentModifiers.put(modifier.id(), modifier);
/*     */   }
/*     */   
/*     */   public void addPermanentModifier(AttributeModifier modifier) {
/* 103 */     addModifier(modifier);
/* 104 */     this.permanentModifiers.put(modifier.id(), modifier);
/*     */   }
/*     */   
/*     */   public void addPermanentModifiers(Collection<AttributeModifier> modifiers) {
/* 108 */     for (AttributeModifier modifier : modifiers) {
/* 109 */       addPermanentModifier(modifier);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setDirty() {
/* 114 */     this.dirty = true;
/* 115 */     this.onDirty.accept(this);
/*     */   }
/*     */ 
/*     */   
/* 119 */   public void removeModifier(AttributeModifier modifier) { removeModifier(modifier.id()); }
/*     */ 
/*     */   
/*     */   public boolean removeModifier(Identifier id) {
/* 123 */     AttributeModifier modifier = (AttributeModifier)this.modifierById.remove(id);
/* 124 */     if (modifier == null) {
/* 125 */       return false;
/*     */     }
/* 127 */     getModifiers(modifier.operation()).remove(id);
/* 128 */     this.permanentModifiers.remove(id);
/* 129 */     setDirty();
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public void removeModifiers() {
/* 134 */     for (AttributeModifier modifier : getModifiers()) {
/* 135 */       removeModifier(modifier);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getValue() {
/* 140 */     if (this.dirty) {
/* 141 */       this.cachedValue = calculateValue();
/* 142 */       this.dirty = false;
/*     */     } 
/*     */     
/* 145 */     return this.cachedValue;
/*     */   }
/*     */   
/*     */   private double calculateValue() {
/* 149 */     double base = getBaseValue();
/*     */     
/* 151 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_VALUE)) {
/* 152 */       base += modifier.amount();
/*     */     }
/*     */     
/* 155 */     double result = base;
/*     */     
/* 157 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_MULTIPLIED_BASE)) {
/* 158 */       result += base * modifier.amount();
/*     */     }
/*     */     
/* 161 */     for (AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)) {
/* 162 */       result *= (1.0D + modifier.amount());
/*     */     }
/*     */     
/* 165 */     return ((Attribute)this.attribute.value()).sanitizeValue(result);
/*     */   }
/*     */ 
/*     */   
/* 169 */   private Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation operation) { return ((Map)this.modifiersByOperation.getOrDefault(operation, Map.of())).values(); }
/*     */ 
/*     */   
/*     */   public void replaceFrom(AttributeInstance other) {
/* 173 */     this.baseValue = other.baseValue;
/*     */     
/* 175 */     this.modifierById.clear();
/* 176 */     this.modifierById.putAll(other.modifierById);
/*     */     
/* 178 */     this.permanentModifiers.clear();
/* 179 */     this.permanentModifiers.putAll(other.permanentModifiers);
/*     */     
/* 181 */     this.modifiersByOperation.clear();
/* 182 */     other.modifiersByOperation.forEach((operation, attributeModifiers) -> 
/* 183 */         getModifiers(operation).putAll(attributeModifiers));
/*     */     
/* 185 */     setDirty();
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 189 */     return new Packed(this.attribute, this.baseValue, 
/*     */ 
/*     */         
/* 192 */         List.copyOf(this.permanentModifiers.values()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(Packed packed) {
/* 197 */     this.baseValue = packed.baseValue;
/*     */     
/* 199 */     for (AttributeModifier modifier : packed.modifiers) {
/* 200 */       this.modifierById.put(modifier.id(), modifier);
/* 201 */       getModifiers(modifier.operation()).put(modifier.id(), modifier);
/* 202 */       this.permanentModifiers.put(modifier.id(), modifier);
/*     */     } 
/* 204 */     setDirty();
/*     */   }
/*     */   public static final class Packed extends Record { private final Holder<Attribute> attribute; private final double baseValue; private final List<AttributeModifier> modifiers;
/* 207 */     public Packed(Holder<Attribute> attribute, double baseValue, List<AttributeModifier> modifiers) { this.attribute = attribute; this.baseValue = baseValue; this.modifiers = modifiers; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/* 207 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Attribute> attribute() { return this.attribute; } public double baseValue() { return this.baseValue; } public List<AttributeModifier> modifiers() { return this.modifiers; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(BuiltInRegistries.ATTRIBUTE
/* 213 */           .holderByNameCodec().fieldOf("id").forGetter(Packed::attribute), Codec.DOUBLE
/* 214 */           .fieldOf("base").orElse(Double.valueOf(0.0D)).forGetter(Packed::baseValue), AttributeModifier.CODEC
/* 215 */           .listOf().optionalFieldOf("modifiers", List.of()).forGetter(Packed::modifiers))
/* 216 */         .apply(i, Packed::new));
/*     */     
/* 218 */     public static final Codec<List<Packed>> LIST_CODEC = CODEC.listOf(); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
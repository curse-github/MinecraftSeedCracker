/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class EnvironmentAttributeSystem
/*     */   implements EnvironmentAttributeReader {
/*     */   private EnvironmentAttributeSystem(Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute) {
/*  27 */     this.attributeSamplers = new Reference2ObjectOpenHashMap();
/*     */ 
/*     */     
/*  30 */     layersByAttribute.forEach((attribute, layers) -> 
/*  31 */         this.attributeSamplers.put(attribute, bakeLayerSampler(attribute, layers)));
/*     */   }
/*     */   
/*     */   private final Map<EnvironmentAttribute<?>, ValueSampler<?>> attributeSamplers;
/*     */   
/*     */   private <Value> ValueSampler<Value> bakeLayerSampler(EnvironmentAttribute<Value> attribute, List<? extends EnvironmentAttributeLayer<?>> untypedLayers) {
/*  37 */     List<EnvironmentAttributeLayer<Value>> layers = new ArrayList<EnvironmentAttributeLayer<Value>>(untypedLayers);
/*     */     
/*  39 */     Value constantBaseValue = (Value)attribute.defaultValue();
/*  40 */     while (!layers.isEmpty()) { Object object = layers.getFirst(); if (object instanceof EnvironmentAttributeLayer.Constant) { EnvironmentAttributeLayer.Constant<Value> constantLayer = (EnvironmentAttributeLayer.Constant)object;
/*  41 */         constantBaseValue = (Value)constantLayer.applyConstant(constantBaseValue);
/*  42 */         layers.removeFirst(); }
/*     */        }
/*     */     
/*  45 */     boolean isAffectedByPosition = layers.stream().anyMatch(layer -> layer instanceof EnvironmentAttributeLayer.Positional);
/*     */     
/*  47 */     return new ValueSampler(attribute, constantBaseValue, List.copyOf(layers), isAffectedByPosition);
/*     */   }
/*     */ 
/*     */   
/*  51 */   public static Builder builder() { return new Builder(); }
/*     */ 
/*     */   
/*     */   private static void addDefaultLayers(Builder builder, Level level) {
/*  55 */     RegistryAccess registries = level.registryAccess();
/*  56 */     BiomeManager biomeManager = level.getBiomeManager();
/*  57 */     Objects.requireNonNull(level); LongSupplier dayTimeGetter = level::getDayTime;
/*  58 */     addDimensionLayer(builder, level.dimensionType());
/*  59 */     addBiomeLayer(builder, registries.lookupOrThrow(Registries.BIOME), biomeManager);
/*  60 */     level.dimensionType().timelines().forEach(timeline -> builder.addTimelineLayer(timeline, dayTimeGetter));
/*  61 */     if (level.canHaveWeather()) {
/*  62 */       WeatherAttributes.addBuiltinLayers(builder, WeatherAttributes.WeatherAccess.from(level));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  67 */   private static void addDimensionLayer(Builder builder, DimensionType dimensionType) { builder.addConstantLayer(dimensionType.attributes()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addBiomeLayer(Builder builder, HolderLookup<Biome> biomes, BiomeManager biomeManager) {
/*  75 */     Stream<EnvironmentAttribute<?>> attributesProvidedByBiomes = biomes.listElements().flatMap(biome -> ((Biome)biome.value()).getAttributes().keySet().stream()).distinct();
/*  76 */     attributesProvidedByBiomes.forEach(attribute -> 
/*  77 */         addBiomeLayerForAttribute(builder, attribute, biomeManager));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <Value> void addBiomeLayerForAttribute(Builder builder, EnvironmentAttribute<Value> attribute, BiomeManager biomeManager) {
/*  82 */     builder.addPositionalLayer(attribute, (baseValue, pos, biomeWeights) -> {
/*  83 */           if (biomeWeights != null && attribute.isSpatiallyInterpolated()) {
/*  84 */             return biomeWeights.applyAttributeLayer(attribute, baseValue);
/*     */           }
/*  86 */           Holder<Biome> biome = biomeManager.getNoiseBiomeAtPosition(pos.x, pos.y, pos.z);
/*  87 */           return ((Biome)biome.value()).getAttributes().applyModifier(attribute, baseValue);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public void invalidateTickCache() { this.attributeSamplers.values().forEach(ValueSampler::invalidateTickCache); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private <Value> ValueSampler<Value> getValueSampler(EnvironmentAttribute<Value> attribute) { return (ValueSampler)this.attributeSamplers.get(attribute); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
/* 104 */     if (SharedConstants.IS_RUNNING_IN_IDE && attribute.isPositional()) {
/* 105 */       throw new IllegalStateException("Position must always be provided for positional attribute " + String.valueOf(attribute));
/*     */     }
/* 107 */     ValueSampler<Value> sampler = getValueSampler(attribute);
/* 108 */     if (sampler == null) {
/* 109 */       return (Value)attribute.defaultValue();
/*     */     }
/* 111 */     return (Value)sampler.getDimensionValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 116 */     ValueSampler<Value> sampler = getValueSampler(attribute);
/* 117 */     if (sampler == null) {
/* 118 */       return (Value)attribute.defaultValue();
/*     */     }
/* 120 */     return (Value)sampler.getValue(pos, biomeInterpolator);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   <Value> Value getConstantBaseValue(EnvironmentAttribute<Value> attribute) {
/* 125 */     ValueSampler<Value> sampler = getValueSampler(attribute);
/* 126 */     return (Value)((sampler != null) ? sampler.baseValue : attribute.defaultValue());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isAffectedByPosition(EnvironmentAttribute<?> attribute) {
/* 131 */     ValueSampler<?> sampler = getValueSampler(attribute);
/* 132 */     return (sampler != null && sampler.isAffectedByPosition);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 136 */     private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addDefaultLayers(Level level) {
/* 142 */       EnvironmentAttributeSystem.addDefaultLayers(this, level);
/* 143 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addConstantLayer(EnvironmentAttributeMap attributeMap) {
/* 147 */       for (EnvironmentAttribute<?> attribute : attributeMap.keySet()) {
/* 148 */         addConstantEntry(attribute, attributeMap);
/*     */       }
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     private <Value> Builder addConstantEntry(EnvironmentAttribute<Value> attribute, EnvironmentAttributeMap attributeMap) {
/* 154 */       EnvironmentAttributeMap.Entry<Value, ?> entry = attributeMap.get(attribute);
/* 155 */       if (entry == null) {
/* 156 */         throw new IllegalArgumentException("Missing attribute " + String.valueOf(attribute));
/*     */       }
/* 158 */       Objects.requireNonNull(entry); return addConstantLayer(attribute, entry::applyModifier);
/*     */     }
/*     */ 
/*     */     
/* 162 */     public <Value> Builder addConstantLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Constant<Value> layer) { return addLayer(attribute, layer); }
/*     */ 
/*     */ 
/*     */     
/* 166 */     public <Value> Builder addTimeBasedLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.TimeBased<Value> layer) { return addLayer(attribute, layer); }
/*     */ 
/*     */ 
/*     */     
/* 170 */     public <Value> Builder addPositionalLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Positional<Value> layer) { return addLayer(attribute, layer); }
/*     */ 
/*     */     
/*     */     private <Value> Builder addLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer<Value> layer) {
/* 174 */       ((List)this.layersByAttribute.computeIfAbsent(attribute, t -> new ArrayList())).add(layer);
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addTimelineLayer(Holder<Timeline> timeline, LongSupplier dayTimeGetter) {
/* 179 */       for (EnvironmentAttribute<?> attribute : ((Timeline)timeline.value()).attributes()) {
/* 180 */         addTimelineLayerForAttribute(timeline, attribute, dayTimeGetter);
/*     */       }
/* 182 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 186 */     private <Value> void addTimelineLayerForAttribute(Holder<Timeline> timeline, EnvironmentAttribute<Value> attribute, LongSupplier dayTimeGetter) { addTimeBasedLayer(attribute, ((Timeline)timeline.value()).createTrackSampler(attribute, dayTimeGetter)); }
/*     */ 
/*     */ 
/*     */     
/* 190 */     public EnvironmentAttributeSystem build() { return new EnvironmentAttributeSystem(this.layersByAttribute); }
/*     */   }
/*     */   
/*     */   private static class ValueSampler<Value>
/*     */     extends Object
/*     */   {
/*     */     private final EnvironmentAttribute<Value> attribute;
/*     */     private final Value baseValue;
/*     */     private final List<EnvironmentAttributeLayer<Value>> layers;
/*     */     private final boolean isAffectedByPosition;
/*     */     private Value cachedTickValue;
/*     */     private int cacheTickId;
/*     */     
/*     */     private ValueSampler(EnvironmentAttribute<Value> attribute, Value baseValue, List<EnvironmentAttributeLayer<Value>> layers, boolean isAffectedByPosition) {
/* 204 */       this.attribute = attribute;
/* 205 */       this.baseValue = baseValue;
/* 206 */       this.layers = layers;
/* 207 */       this.isAffectedByPosition = isAffectedByPosition;
/*     */     }
/*     */     
/*     */     public void invalidateTickCache() {
/* 211 */       this.cachedTickValue = null;
/* 212 */       this.cacheTickId++;
/*     */     }
/*     */     
/*     */     public Value getDimensionValue() {
/* 216 */       if (this.cachedTickValue != null) {
/* 217 */         return (Value)this.cachedTickValue;
/*     */       }
/* 219 */       Value result = (Value)computeValueNotPositional();
/* 220 */       this.cachedTickValue = result;
/* 221 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Value getValue(Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 226 */       if (!this.isAffectedByPosition) {
/* 227 */         return (Value)getDimensionValue();
/*     */       }
/* 229 */       return (Value)computeValuePositional(pos, biomeInterpolator);
/*     */     }
/*     */     
/*     */     private Value computeValuePositional(Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield baseValue : Ljava/lang/Object;
/*     */       //   4: astore_3
/*     */       //   5: aload_0
/*     */       //   6: getfield layers : Ljava/util/List;
/*     */       //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   14: astore #4
/*     */       //   16: aload #4
/*     */       //   18: invokeinterface hasNext : ()Z
/*     */       //   23: ifeq -> 170
/*     */       //   26: aload #4
/*     */       //   28: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   33: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */       //   36: astore #5
/*     */       //   38: aload #5
/*     */       //   40: dup
/*     */       //   41: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   44: pop
/*     */       //   45: astore #6
/*     */       //   47: iconst_0
/*     */       //   48: istore #7
/*     */       //   50: aload #6
/*     */       //   52: iload #7
/*     */       //   54: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   59: tableswitch default -> 84, 0 -> 94, 1 -> 115, 2 -> 140
/*     */       //   84: new java/lang/MatchException
/*     */       //   87: dup
/*     */       //   88: aconst_null
/*     */       //   89: aconst_null
/*     */       //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   93: athrow
/*     */       //   94: aload #6
/*     */       //   96: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */       //   99: astore #8
/*     */       //   101: aload #8
/*     */       //   103: aload_3
/*     */       //   104: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   109: checkcast java/lang/Object
/*     */       //   112: goto -> 166
/*     */       //   115: aload #6
/*     */       //   117: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */       //   120: astore #9
/*     */       //   122: aload #9
/*     */       //   124: aload_3
/*     */       //   125: aload_0
/*     */       //   126: getfield cacheTickId : I
/*     */       //   129: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */       //   134: checkcast java/lang/Object
/*     */       //   137: goto -> 166
/*     */       //   140: aload #6
/*     */       //   142: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */       //   145: astore #10
/*     */       //   147: aload #10
/*     */       //   149: aload_3
/*     */       //   150: aload_1
/*     */       //   151: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   154: checkcast net/minecraft/world/phys/Vec3
/*     */       //   157: aload_2
/*     */       //   158: invokeinterface applyPositional : (Ljava/lang/Object;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;)Ljava/lang/Object;
/*     */       //   163: checkcast java/lang/Object
/*     */       //   166: astore_3
/*     */       //   167: goto -> 16
/*     */       //   170: aload_0
/*     */       //   171: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */       //   174: aload_3
/*     */       //   175: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   178: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #233	-> 0
/*     */       //   #234	-> 5
/*     */       //   #235	-> 38
/*     */       //   #236	-> 94
/*     */       //   #237	-> 115
/*     */       //   #238	-> 140
/*     */       //   #240	-> 167
/*     */       //   #241	-> 170
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   101	14	8	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant;
/*     */       //   122	18	9	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased;
/*     */       //   147	19	10	positionalLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional;
/*     */       //   38	129	5	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer;
/*     */       //   0	179	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler;
/*     */       //   0	179	1	pos	Lnet/minecraft/world/phys/Vec3;
/*     */       //   0	179	2	biomeInterpolator	Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;
/*     */       //   5	174	3	result	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   101	14	8	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant<TValue;>;
/*     */       //   122	18	9	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased<TValue;>;
/*     */       //   147	19	10	positionalLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional<TValue;>;
/*     */       //   38	129	5	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer<TValue;>;
/*     */       //   0	179	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler<TValue;>;
/*     */       //   5	174	3	result	TValue; }
/*     */     
/*     */     private Value computeValueNotPositional() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield baseValue : Ljava/lang/Object;
/*     */       //   4: astore_1
/*     */       //   5: aload_0
/*     */       //   6: getfield layers : Ljava/util/List;
/*     */       //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   14: astore_2
/*     */       //   15: aload_2
/*     */       //   16: invokeinterface hasNext : ()Z
/*     */       //   21: ifeq -> 151
/*     */       //   24: aload_2
/*     */       //   25: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   30: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */       //   33: astore_3
/*     */       //   34: aload_3
/*     */       //   35: dup
/*     */       //   36: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   39: pop
/*     */       //   40: astore #4
/*     */       //   42: iconst_0
/*     */       //   43: istore #5
/*     */       //   45: aload #4
/*     */       //   47: iload #5
/*     */       //   49: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   54: tableswitch default -> 80, 0 -> 90, 1 -> 111, 2 -> 136
/*     */       //   80: new java/lang/MatchException
/*     */       //   83: dup
/*     */       //   84: aconst_null
/*     */       //   85: aconst_null
/*     */       //   86: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   89: athrow
/*     */       //   90: aload #4
/*     */       //   92: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */       //   95: astore #6
/*     */       //   97: aload #6
/*     */       //   99: aload_1
/*     */       //   100: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   105: checkcast java/lang/Object
/*     */       //   108: goto -> 147
/*     */       //   111: aload #4
/*     */       //   113: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */       //   116: astore #7
/*     */       //   118: aload #7
/*     */       //   120: aload_1
/*     */       //   121: aload_0
/*     */       //   122: getfield cacheTickId : I
/*     */       //   125: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */       //   130: checkcast java/lang/Object
/*     */       //   133: goto -> 147
/*     */       //   136: aload #4
/*     */       //   138: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */       //   141: astore #8
/*     */       //   143: aload_1
/*     */       //   144: checkcast java/lang/Object
/*     */       //   147: astore_1
/*     */       //   148: goto -> 15
/*     */       //   151: aload_0
/*     */       //   152: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */       //   155: aload_1
/*     */       //   156: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   159: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #245	-> 0
/*     */       //   #246	-> 5
/*     */       //   #247	-> 34
/*     */       //   #248	-> 90
/*     */       //   #249	-> 111
/*     */       //   #251	-> 136
/*     */       //   #253	-> 148
/*     */       //   #254	-> 151
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   97	14	6	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant;
/*     */       //   118	18	7	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased;
/*     */       //   143	4	8	ignored	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional;
/*     */       //   34	114	3	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer;
/*     */       //   0	160	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler;
/*     */       //   5	155	1	result	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   97	14	6	constantLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Constant<TValue;>;
/*     */       //   118	18	7	timeBasedLayer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased<TValue;>;
/*     */       //   143	4	8	ignored	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer$Positional<TValue;>;
/*     */       //   34	114	3	layer	Lnet/minecraft/world/attribute/EnvironmentAttributeLayer<TValue;>;
/*     */       //   0	160	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$ValueSampler<TValue;>;
/*     */       //   5	155	1	result	TValue; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
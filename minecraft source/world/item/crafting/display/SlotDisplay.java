/*     */ package net.minecraft.world.item.crafting.display;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ 
/*     */ public interface SlotDisplay {
/*  33 */   public static final Codec<SlotDisplay> CODEC = BuiltInRegistries.SLOT_DISPLAY.byNameCodec().dispatch(SlotDisplay::type, Type::codec);
/*  34 */   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay> STREAM_CODEC = ByteBufCodecs.registry(Registries.SLOT_DISPLAY).dispatch(SlotDisplay::type, Type::streamCodec);
/*     */ 
/*     */   
/*     */   <T> Stream<T> resolve(ContextMap paramContextMap, DisplayContentsFactory<T> paramDisplayContentsFactory);
/*     */ 
/*     */   
/*     */   Type<? extends SlotDisplay> type();
/*     */   
/*  42 */   default boolean isEnabled(FeatureFlagSet enabledFeatures) { return true; }
/*     */   public static final class Type<T extends SlotDisplay> extends Record { private final MapCodec<T> codec; private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*     */     
/*  45 */     public Type(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) { this.codec = codec; this.streamCodec = streamCodec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  45 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>; } public MapCodec<T> codec() { return this.codec; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  45 */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>; } public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; } }
/*     */ 
/*     */   
/*     */   public static class ItemStackContentsFactory
/*     */     extends Object
/*     */     implements DisplayContentsFactory.ForStacks<ItemStack>
/*     */   {
/*  52 */     public static final ItemStackContentsFactory INSTANCE = new ItemStackContentsFactory();
/*     */ 
/*     */ 
/*     */     
/*  56 */     public ItemStack forStack(ItemStack stack) { return stack; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  61 */   default List<ItemStack> resolveForStacks(ContextMap context) { return resolve(context, ItemStackContentsFactory.INSTANCE).toList(); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   default ItemStack resolveForFirstStack(ContextMap context) { return (ItemStack)resolve(context, ItemStackContentsFactory.INSTANCE).findFirst().orElse(ItemStack.EMPTY); }
/*     */   
/*     */   public static class Empty
/*     */     implements SlotDisplay {
/*  69 */     public static final Empty INSTANCE = new Empty();
/*     */     
/*  71 */     public static final MapCodec<Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*  73 */     public static final StreamCodec<RegistryFriendlyByteBuf, Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/*  75 */     public static final SlotDisplay.Type<Empty> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     public SlotDisplay.Type<Empty> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     public String toString() { return "<empty>"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) { return Stream.empty(); }
/*     */   }
/*     */   
/*     */   public static class AnyFuel
/*     */     implements SlotDisplay {
/*  97 */     public static final AnyFuel INSTANCE = new AnyFuel();
/*     */     
/*  99 */     public static final MapCodec<AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */     
/* 101 */     public static final StreamCodec<RegistryFriendlyByteBuf, AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/* 103 */     public static final SlotDisplay.Type<AnyFuel> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     public SlotDisplay.Type<AnyFuel> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     public String toString() { return "<any fuel>"; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 120 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 121 */         FuelValues fuelValues = (FuelValues)context.getOptional(SlotDisplayContext.FUEL_VALUES);
/* 122 */         if (fuelValues != null) {
/* 123 */           Objects.requireNonNull(stacks); return fuelValues.fuelItems().stream().map(stacks::forStack);
/*     */         }  }
/*     */       
/* 126 */       return Stream.empty();
/*     */     } }
/*     */   public static final class SmithingTrimDemoSlotDisplay extends Record implements SlotDisplay { private final SlotDisplay base; private final SlotDisplay material; private final Holder<TrimPattern> pattern;
/*     */     
/* 130 */     public SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<TrimPattern> pattern) { this.base = base; this.material = material; this.pattern = pattern; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/* 130 */       //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay base() { return this.base; } public SlotDisplay material() { return this.material; } public Holder<TrimPattern> pattern() { return this.pattern; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     public static final MapCodec<SmithingTrimDemoSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 137 */           .fieldOf("base").forGetter(SmithingTrimDemoSlotDisplay::base), SlotDisplay.CODEC
/* 138 */           .fieldOf("material").forGetter(SmithingTrimDemoSlotDisplay::material), TrimPattern.CODEC
/* 139 */           .fieldOf("pattern").forGetter(SmithingTrimDemoSlotDisplay::pattern))
/* 140 */         .apply(i, SmithingTrimDemoSlotDisplay::new));
/*     */     
/* 142 */     public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimDemoSlotDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::base, SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::material, TrimPattern.STREAM_CODEC, SmithingTrimDemoSlotDisplay::pattern, SmithingTrimDemoSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     public static final SlotDisplay.Type<SmithingTrimDemoSlotDisplay> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */     
/* 153 */     public SlotDisplay.Type<SmithingTrimDemoSlotDisplay> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 158 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 159 */         HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 160 */         if (registries != null) {
/*     */           
/* 162 */           RandomSource randomSource = RandomSource.create(System.identityHashCode(this));
/* 163 */           List<ItemStack> bases = this.base.resolveForStacks(context);
/* 164 */           if (bases.isEmpty()) {
/* 165 */             return Stream.empty();
/*     */           }
/* 167 */           List<ItemStack> materials = this.material.resolveForStacks(context);
/* 168 */           if (materials.isEmpty()) {
/* 169 */             return Stream.empty();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 178 */           Objects.requireNonNull(stacks); return Stream.generate(() -> { ItemStack base = (ItemStack)Util.getRandom(bases, randomSource); ItemStack material = (ItemStack)Util.getRandom(materials, randomSource); return SmithingTrimRecipe.applyTrim(registries, base, material, this.pattern); }).limit(256L).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
/*     */         }  }
/*     */       
/* 181 */       return Stream.empty();
/*     */     } }
/*     */   public static final class ItemSlotDisplay extends Record implements SlotDisplay { private final Holder<Item> item;
/*     */     
/* 185 */     public ItemSlotDisplay(Holder<Item> item) { this.item = item; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/* 185 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Item> item() { return this.item; }
/* 186 */     public static final MapCodec<ItemSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Item.CODEC
/* 187 */           .fieldOf("item").forGetter(ItemSlotDisplay::item))
/* 188 */         .apply(i, ItemSlotDisplay::new));
/*     */     
/* 190 */     public static final StreamCodec<RegistryFriendlyByteBuf, ItemSlotDisplay> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemSlotDisplay::item, ItemSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     public static final SlotDisplay.Type<ItemSlotDisplay> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */     
/* 199 */     public SlotDisplay.Type<ItemSlotDisplay> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */     
/* 203 */     public ItemSlotDisplay(Item item) { this(item.builtInRegistryHolder()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 208 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 209 */         return Stream.of(stacks.forStack(this.item)); }
/*     */       
/* 211 */       return Stream.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 216 */     public boolean isEnabled(FeatureFlagSet enabledFeatures) { return ((Item)this.item.value()).isEnabled(enabledFeatures); } }
/*     */   
/*     */   public static final class ItemStackSlotDisplay extends Record implements SlotDisplay { private final ItemStack stack;
/*     */     
/* 220 */     public ItemStackSlotDisplay(ItemStack stack) { this.stack = stack; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #220	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #220	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 220 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay; } public ItemStack stack() { return this.stack; }
/* 221 */     public static final MapCodec<ItemStackSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ItemStack.STRICT_CODEC
/* 222 */           .fieldOf("item").forGetter(ItemStackSlotDisplay::stack))
/* 223 */         .apply(i, ItemStackSlotDisplay::new));
/*     */     
/* 225 */     public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSlotDisplay> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC, ItemStackSlotDisplay::stack, ItemStackSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     public static final SlotDisplay.Type<ItemStackSlotDisplay> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */     
/* 234 */     public SlotDisplay.Type<ItemStackSlotDisplay> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 239 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 240 */         return Stream.of(stacks.forStack(this.stack)); }
/*     */       
/* 242 */       return Stream.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 247 */     public boolean equals(Object o) { if (this != o) { if (o instanceof ItemStackSlotDisplay) { ItemStackSlotDisplay that = (ItemStackSlotDisplay)o; if (ItemStack.matches(this.stack, that.stack)); }  return false; }
/*     */        }
/*     */ 
/*     */ 
/*     */     
/* 252 */     public boolean isEnabled(FeatureFlagSet enabledFeatures) { return this.stack.getItem().isEnabled(enabledFeatures); } }
/*     */   
/*     */   public static final class TagSlotDisplay extends Record implements SlotDisplay { private final TagKey<Item> tag;
/*     */     
/* 256 */     public TagSlotDisplay(TagKey<Item> tag) { this.tag = tag; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/* 256 */       //   0	8	1	o	Ljava/lang/Object; } public TagKey<Item> tag() { return this.tag; }
/* 257 */     public static final MapCodec<TagSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 258 */           TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(TagSlotDisplay::tag))
/* 259 */         .apply(i, TagSlotDisplay::new));
/*     */     
/* 261 */     public static final StreamCodec<RegistryFriendlyByteBuf, TagSlotDisplay> STREAM_CODEC = StreamCodec.composite(
/* 262 */         TagKey.streamCodec(Registries.ITEM), TagSlotDisplay::tag, TagSlotDisplay::new);
/*     */ 
/*     */ 
/*     */     
/* 266 */     public static final SlotDisplay.Type<TagSlotDisplay> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */     
/* 270 */     public SlotDisplay.Type<TagSlotDisplay> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 275 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks)factory;
/* 276 */         HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 277 */         if (registries != null) {
/* 278 */           return registries.lookupOrThrow(Registries.ITEM)
/* 279 */             .get(this.tag)
/* 280 */             .map(t -> { Objects.requireNonNull(stacks); return t.stream().map(stacks::forStack);
/* 281 */               }).stream().flatMap(s -> s);
/*     */         } }
/*     */       
/* 284 */       return Stream.empty();
/*     */     } }
/*     */   public static final class Composite extends Record implements SlotDisplay { private final List<SlotDisplay> contents;
/*     */     
/* 288 */     public Composite(List<SlotDisplay> contents) { this.contents = contents; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;
/* 288 */       //   0	8	1	o	Ljava/lang/Object; } public List<SlotDisplay> contents() { return this.contents; }
/* 289 */     public static final MapCodec<Composite> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 290 */           .listOf().fieldOf("contents").forGetter(Composite::contents))
/* 291 */         .apply(i, Composite::new));
/*     */     
/* 293 */     public static final StreamCodec<RegistryFriendlyByteBuf, Composite> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC
/* 294 */         .apply(ByteBufCodecs.list()), Composite::contents, Composite::new);
/*     */ 
/*     */ 
/*     */     
/* 298 */     public static final SlotDisplay.Type<Composite> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */     
/* 302 */     public SlotDisplay.Type<Composite> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) { return this.contents.stream().flatMap(d -> d.resolve(context, factory)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     public boolean isEnabled(FeatureFlagSet enabledFeatures) { return this.contents.stream().allMatch(c -> c.isEnabled(enabledFeatures)); } }
/*     */   public static final class WithRemainder extends Record implements SlotDisplay { private final SlotDisplay input;
/*     */     private final SlotDisplay remainder;
/*     */     
/* 316 */     public WithRemainder(SlotDisplay input, SlotDisplay remainder) { this.input = input; this.remainder = remainder; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/* 316 */       //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay input() { return this.input; } public SlotDisplay remainder() { return this.remainder; }
/*     */ 
/*     */ 
/*     */     
/* 320 */     public static final MapCodec<WithRemainder> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SlotDisplay.CODEC
/* 321 */           .fieldOf("input").forGetter(WithRemainder::input), SlotDisplay.CODEC
/* 322 */           .fieldOf("remainder").forGetter(WithRemainder::remainder))
/* 323 */         .apply(i, WithRemainder::new));
/*     */     
/* 325 */     public static final StreamCodec<RegistryFriendlyByteBuf, WithRemainder> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, WithRemainder::input, SlotDisplay.STREAM_CODEC, WithRemainder::remainder, WithRemainder::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     public static final SlotDisplay.Type<WithRemainder> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */     
/* 335 */     public SlotDisplay.Type<WithRemainder> type() { return TYPE; }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 340 */       if (factory instanceof DisplayContentsFactory.ForRemainders) { DisplayContentsFactory.ForRemainders<T> remainders = (DisplayContentsFactory.ForRemainders)factory;
/* 341 */         List<T> resolvedRemainders = this.remainder.resolve(context, factory).toList();
/* 342 */         return this.input.resolve(context, factory).map(input -> remainders.addRemainder(input, resolvedRemainders)); }
/*     */       
/* 344 */       return this.input.resolve(context, factory);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 349 */     public boolean isEnabled(FeatureFlagSet enabledFeatures) { return (this.input.isEnabled(enabledFeatures) && this.remainder.isEnabled(enabledFeatures)); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SlotDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
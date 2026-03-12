/*     */ package net.minecraft.world.item.component;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import org.apache.commons.lang3.function.TriConsumer;
/*     */ 
/*     */ public final class ItemAttributeModifiers extends Record {
/*     */   private final List<Entry> modifiers;
/*     */   
/*  37 */   public ItemAttributeModifiers(List<Entry> modifiers) { this.modifiers = modifiers; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers;
/*  37 */     //   0	8	1	o	Ljava/lang/Object; } public List<Entry> modifiers() { return this.modifiers; }
/*  38 */   public static final ItemAttributeModifiers EMPTY = new ItemAttributeModifiers(List.of());
/*     */   
/*  40 */   public static final Codec<ItemAttributeModifiers> CODEC = Entry.CODEC.listOf().xmap(ItemAttributeModifiers::new, ItemAttributeModifiers::modifiers);
/*     */   
/*  42 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers> STREAM_CODEC = StreamCodec.composite(Entry.STREAM_CODEC
/*  43 */       .apply(ByteBufCodecs.list()), ItemAttributeModifiers::modifiers, ItemAttributeModifiers::new);
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*     */ 
/*     */   
/*  50 */   public static Builder builder() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public ItemAttributeModifiers withModifierAdded(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
/*  54 */     ImmutableList.Builder<Entry> newModifiers = ImmutableList.builderWithExpectedSize(this.modifiers.size() + 1);
/*  55 */     for (Entry entry : this.modifiers) {
/*  56 */       if (!entry.matches(attribute, modifier.id())) {
/*  57 */         newModifiers.add(entry);
/*     */       }
/*     */     } 
/*  60 */     newModifiers.add(new Entry(attribute, modifier, slot));
/*  61 */     return new ItemAttributeModifiers(newModifiers.build());
/*     */   }
/*     */   
/*     */   public void forEach(EquipmentSlotGroup slot, TriConsumer<Holder<Attribute>, AttributeModifier, Display> consumer) {
/*  65 */     for (Entry entry : this.modifiers) {
/*  66 */       if (entry.slot.equals(slot)) {
/*  67 */         consumer.accept(entry.attribute, entry.modifier, entry.display);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forEach(EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/*  73 */     for (Entry entry : this.modifiers) {
/*  74 */       if (entry.slot.equals(slot)) {
/*  75 */         consumer.accept(entry.attribute, entry.modifier);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forEach(EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/*  81 */     for (Entry entry : this.modifiers) {
/*  82 */       if (entry.slot.test(slot)) {
/*  83 */         consumer.accept(entry.attribute, entry.modifier);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public double compute(Holder<Attribute> attribute, double baseValue, EquipmentSlot slot) {
/*  89 */     double value = baseValue;
/*  90 */     for (Entry entry : this.modifiers) {
/*  91 */       if (!entry.slot.test(slot)) {
/*     */         continue;
/*     */       }
/*  94 */       if (entry.attribute != attribute) {
/*     */         continue;
/*     */       }
/*  97 */       double amount = entry.modifier.amount();
/*  98 */       switch (entry.modifier.operation()) { default: throw new MatchException(null, null);case ADD_VALUE: case ADD_MULTIPLIED_BASE: case ADD_MULTIPLIED_TOTAL: break; }  value += 
/*     */ 
/*     */         
/* 101 */         amount * value;
/*     */     } 
/*     */     
/* 104 */     return value;
/*     */   }
/*     */   
/*     */   public static interface Display {
/* 108 */     public static final Codec<Display> CODEC = Type.CODEC.dispatch("type", Display::type, type -> type.codec);
/*     */     
/* 110 */     public static final StreamCodec<RegistryFriendlyByteBuf, Display> STREAM_CODEC = Type.STREAM_CODEC.cast().dispatch(Display::type, Type::streamCodec);
/*     */ 
/*     */     
/* 113 */     static Display attributeModifiers() { return Default.INSTANCE; }
/*     */ 
/*     */ 
/*     */     
/* 117 */     static Display hidden() { return Hidden.INSTANCE; }
/*     */ 
/*     */ 
/*     */     
/* 121 */     static Display override(Component component) { return new OverrideText(component); }
/*     */     
/*     */     Type type();
/*     */     
/*     */     void apply(Consumer<Component> param1Consumer, Player param1Player, Holder<Attribute> param1Holder, AttributeModifier param1AttributeModifier);
/*     */     
/*     */     public enum Type
/*     */       implements StringRepresentable {
/* 129 */       DEFAULT("default", 0, ItemAttributeModifiers.Display.Default.CODEC, ItemAttributeModifiers.Display.Default.STREAM_CODEC),
/* 130 */       HIDDEN("hidden", 1, ItemAttributeModifiers.Display.Hidden.CODEC, ItemAttributeModifiers.Display.Hidden.STREAM_CODEC),
/* 131 */       OVERRIDE("override", 2, ItemAttributeModifiers.Display.OverrideText.CODEC, ItemAttributeModifiers.Display.OverrideText.STREAM_CODEC); private static final Codec<Type> CODEC; private static final IntFunction<Type> BY_ID; private static final StreamCodec<ByteBuf, Type> STREAM_CODEC; private final String name; private final int id; private final MapCodec<? extends ItemAttributeModifiers.Display> codec; private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec;
/*     */       
/*     */       static  {
/* 134 */         CODEC = StringRepresentable.fromEnum(Type::values);
/*     */         
/* 136 */         BY_ID = ByIdMap.continuous(Type::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 137 */         STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::id);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       Type(String name, int id, MapCodec<? extends ItemAttributeModifiers.Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec) {
/* 145 */         this.name = name;
/* 146 */         this.id = id;
/* 147 */         this.codec = codec;
/* 148 */         this.streamCodec = streamCodec;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 153 */       public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */       
/* 157 */       private int id() { return this.id; }
/*     */ 
/*     */ 
/*     */       
/* 161 */       private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec() { return this.streamCodec; } }
/*     */     public static final class Default extends Record implements Display { public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #165	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; }
/*     */       public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #165	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; }
/*     */       public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #165	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;
/*     */         //   0	8	1	o	Ljava/lang/Object; }
/*     */       
/* 166 */       private static final Default INSTANCE = new Default();
/*     */       
/* 168 */       private static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);
/* 169 */       private static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */ 
/*     */ 
/*     */       
/* 173 */       public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.DEFAULT; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
/* 179 */         double displayAmount, amount = modifier.amount();
/* 180 */         boolean displayWithBase = false;
/*     */         
/* 182 */         if (player != null) {
/* 183 */           if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
/* 184 */             amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/* 185 */             displayWithBase = true;
/* 186 */           } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
/* 187 */             amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
/* 188 */             displayWithBase = true;
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 193 */         if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 194 */           displayAmount = amount * 100.0D;
/* 195 */         } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
/* 196 */           displayAmount = amount * 10.0D;
/*     */         } else {
/* 198 */           displayAmount = amount;
/*     */         } 
/*     */         
/* 201 */         if (displayWithBase) {
/* 202 */           consumer.accept(
/* 203 */               CommonComponents.space().append(
/* 204 */                 Component.translatable("attribute.modifier.equals." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 205 */                     .format(displayAmount), 
/* 206 */                     Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/*     */                   
/* 208 */                   })).withStyle(ChatFormatting.DARK_GREEN));
/*     */         }
/* 210 */         else if (amount > 0.0D) {
/* 211 */           consumer.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 212 */                   .format(displayAmount), 
/* 213 */                   Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/* 214 */                 }).withStyle(((Attribute)attribute.value()).getStyle(true)));
/* 215 */         } else if (amount < 0.0D) {
/* 216 */           consumer.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 217 */                   .format(-displayAmount), 
/* 218 */                   Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/* 219 */                 }).withStyle(((Attribute)attribute.value()).getStyle(false)));
/*     */         } 
/*     */       } }
/*     */     public static final class Hidden extends Record implements Display { public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #224	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden; }
/*     */       public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #224	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden; }
/*     */       public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #224	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;
/*     */         //   0	8	1	o	Ljava/lang/Object; }
/* 225 */       private static final Hidden INSTANCE = new Hidden();
/*     */       
/* 227 */       private static final MapCodec<Hidden> CODEC = MapCodec.unit(INSTANCE);
/* 228 */       private static final StreamCodec<RegistryFriendlyByteBuf, Hidden> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */ 
/*     */ 
/*     */       
/* 232 */       public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.HIDDEN; }
/*     */       
/*     */       public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {} }
/*     */ 
/*     */     
/*     */     public static final class OverrideText extends Record implements Display { private final Component component;
/*     */       
/* 239 */       public OverrideText(Component component) { this.component = component; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #239	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #239	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #239	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;
/* 239 */         //   0	8	1	o	Ljava/lang/Object; } public Component component() { return this.component; }
/* 240 */       private static final MapCodec<OverrideText> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/* 241 */             .fieldOf("value").forGetter(OverrideText::component))
/* 242 */           .apply(i, OverrideText::new));
/*     */       
/* 244 */       private static final StreamCodec<RegistryFriendlyByteBuf, OverrideText> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, OverrideText::component, OverrideText::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 251 */       public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.OVERRIDE; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 256 */       public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) { consumer.accept(this.component); } } } public enum Type implements StringRepresentable { DEFAULT("default", 0, ItemAttributeModifiers.Display.Default.CODEC, ItemAttributeModifiers.Display.Default.STREAM_CODEC), HIDDEN("hidden", 1, ItemAttributeModifiers.Display.Hidden.CODEC, ItemAttributeModifiers.Display.Hidden.STREAM_CODEC), OVERRIDE("override", 2, ItemAttributeModifiers.Display.OverrideText.CODEC, ItemAttributeModifiers.Display.OverrideText.STREAM_CODEC); private static final Codec<Type> CODEC; private static final IntFunction<Type> BY_ID; private static final StreamCodec<ByteBuf, Type> STREAM_CODEC; private final String name; private final int id; private final MapCodec<? extends ItemAttributeModifiers.Display> codec; private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec; static  { CODEC = StringRepresentable.fromEnum(Type::values); BY_ID = ByIdMap.continuous(Type::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO); STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::id); } Type(String name, int id, MapCodec<? extends ItemAttributeModifiers.Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec) { this.name = name; this.id = id; this.codec = codec; this.streamCodec = streamCodec; } public String getSerializedName() { return this.name; } private int id() { return this.id; } private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec() { return this.streamCodec; } } public static final class Default extends Record implements Display { public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;
/*     */       //   0	8	1	o	Ljava/lang/Object; } private static final Default INSTANCE = new Default(); private static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE); private static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE); public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.DEFAULT; } public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) { double displayAmount, amount = modifier.amount(); boolean displayWithBase = false; if (player != null) if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) { amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE); displayWithBase = true; } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) { amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED); displayWithBase = true; }   if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) { displayAmount = amount * 100.0D; } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) { displayAmount = amount * 10.0D; } else { displayAmount = amount; }  if (displayWithBase) { consumer.accept(CommonComponents.space().append(Component.translatable("attribute.modifier.equals." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(((Attribute)attribute.value()).getDescriptionId()) })).withStyle(ChatFormatting.DARK_GREEN)); } else if (amount > 0.0D) { consumer.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(((Attribute)attribute.value()).getDescriptionId()) }).withStyle(((Attribute)attribute.value()).getStyle(true))); } else if (amount < 0.0D) { consumer.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-displayAmount), Component.translatable(((Attribute)attribute.value()).getDescriptionId()) }).withStyle(((Attribute)attribute.value()).getStyle(false))); }  } } public static final class Hidden extends Record implements Display { public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;
/*     */       //   0	8	1	o	Ljava/lang/Object; } private static final Hidden INSTANCE = new Hidden(); private static final MapCodec<Hidden> CODEC = MapCodec.unit(INSTANCE); private static final StreamCodec<RegistryFriendlyByteBuf, Hidden> STREAM_CODEC = StreamCodec.unit(INSTANCE); public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.HIDDEN; } public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {} } public static final class OverrideText extends Record implements Display { private final Component component; public OverrideText(Component component) { this.component = component; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 256 */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText; } public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) { consumer.accept(this.component); } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     public Component component() { return this.component; }
/*     */     private static final MapCodec<OverrideText> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC.fieldOf("value").forGetter(OverrideText::component)).apply(i, OverrideText::new)); private static final StreamCodec<RegistryFriendlyByteBuf, OverrideText> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, OverrideText::component, OverrideText::new);
/*     */     public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.OVERRIDE; } }
/*     */   public static final class Entry extends Record { private final Holder<Attribute> attribute; private final AttributeModifier modifier; private final EquipmentSlotGroup slot; private final ItemAttributeModifiers.Display display;
/* 261 */     public Entry(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot, ItemAttributeModifiers.Display display) { this.attribute = attribute; this.modifier = modifier; this.slot = slot; this.display = display; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #261	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #261	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #261	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Entry;
/* 261 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Attribute> attribute() { return this.attribute; } public AttributeModifier modifier() { return this.modifier; } public EquipmentSlotGroup slot() { return this.slot; } public ItemAttributeModifiers.Display display() { return this.display; }
/* 262 */     public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(Attribute.CODEC
/* 263 */           .fieldOf("type").forGetter(Entry::attribute), AttributeModifier.MAP_CODEC
/* 264 */           .forGetter(Entry::modifier), EquipmentSlotGroup.CODEC
/* 265 */           .optionalFieldOf("slot", EquipmentSlotGroup.ANY).forGetter(Entry::slot), ItemAttributeModifiers.Display.CODEC
/* 266 */           .optionalFieldOf("display", ItemAttributeModifiers.Display.Default.INSTANCE).forGetter(Entry::display))
/* 267 */         .apply(i, Entry::new));
/*     */     
/* 269 */     public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(Attribute.STREAM_CODEC, Entry::attribute, AttributeModifier.STREAM_CODEC, Entry::modifier, EquipmentSlotGroup.STREAM_CODEC, Entry::slot, ItemAttributeModifiers.Display.STREAM_CODEC, Entry::display, Entry::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     public Entry(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) { this(attribute, modifier, slot, ItemAttributeModifiers.Display.attributeModifiers()); }
/*     */ 
/*     */ 
/*     */     
/* 282 */     public boolean matches(Holder<Attribute> attribute, Identifier id) { return (attribute.equals(this.attribute) && this.modifier.is(id)); } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 287 */     private final ImmutableList.Builder<ItemAttributeModifiers.Entry> entries = ImmutableList.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
/* 293 */       this.entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
/* 294 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder add(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot, ItemAttributeModifiers.Display display) {
/* 299 */       this.entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot, display));
/* 300 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 304 */     public ItemAttributeModifiers build() { return new ItemAttributeModifiers(this.entries.build()); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ItemAttributeModifiers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
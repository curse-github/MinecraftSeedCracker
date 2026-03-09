/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
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
/*     */ public interface Display
/*     */ {
/* 108 */   public static final Codec<Display> CODEC = Type.CODEC.dispatch("type", Display::type, type -> type.codec);
/*     */   
/* 110 */   public static final StreamCodec<RegistryFriendlyByteBuf, Display> STREAM_CODEC = Type.STREAM_CODEC.cast().dispatch(Display::type, Type::streamCodec);
/*     */ 
/*     */   
/* 113 */   static Display attributeModifiers() { return Default.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 117 */   static Display hidden() { return Hidden.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 121 */   static Display override(Component component) { return new OverrideText(component); }
/*     */   
/*     */   Type type();
/*     */   
/*     */   void apply(Consumer<Component> paramConsumer, Player paramPlayer, Holder<Attribute> paramHolder, AttributeModifier paramAttributeModifier);
/*     */   
/*     */   public enum Type
/*     */     implements StringRepresentable {
/* 129 */     DEFAULT("default", 0, ItemAttributeModifiers.Display.Default.CODEC, ItemAttributeModifiers.Display.Default.STREAM_CODEC),
/* 130 */     HIDDEN("hidden", 1, ItemAttributeModifiers.Display.Hidden.CODEC, ItemAttributeModifiers.Display.Hidden.STREAM_CODEC),
/* 131 */     OVERRIDE("override", 2, ItemAttributeModifiers.Display.OverrideText.CODEC, ItemAttributeModifiers.Display.OverrideText.STREAM_CODEC); private static final Codec<Type> CODEC; private static final IntFunction<Type> BY_ID; private static final StreamCodec<ByteBuf, Type> STREAM_CODEC; private final String name; private final int id; private final MapCodec<? extends ItemAttributeModifiers.Display> codec; private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec;
/*     */     
/*     */     static  {
/* 134 */       CODEC = StringRepresentable.fromEnum(Type::values);
/*     */       
/* 136 */       BY_ID = ByIdMap.continuous(Type::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 137 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::id);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Type(String name, int id, MapCodec<? extends ItemAttributeModifiers.Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec) {
/* 145 */       this.name = name;
/* 146 */       this.id = id;
/* 147 */       this.codec = codec;
/* 148 */       this.streamCodec = streamCodec;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 153 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 157 */     private int id() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/* 161 */     private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec() { return this.streamCodec; } }
/*     */   public static final class Default extends Record implements Display { public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; }
/*     */     public final boolean equals(Object o) { // Byte code:
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
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     
/* 166 */     private static final Default INSTANCE = new Default();
/*     */     
/* 168 */     private static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);
/* 169 */     private static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */ 
/*     */ 
/*     */     
/* 173 */     public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.DEFAULT; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
/* 179 */       double displayAmount, amount = modifier.amount();
/* 180 */       boolean displayWithBase = false;
/*     */       
/* 182 */       if (player != null) {
/* 183 */         if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
/* 184 */           amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/* 185 */           displayWithBase = true;
/* 186 */         } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
/* 187 */           amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
/* 188 */           displayWithBase = true;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 193 */       if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 194 */         displayAmount = amount * 100.0D;
/* 195 */       } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
/* 196 */         displayAmount = amount * 10.0D;
/*     */       } else {
/* 198 */         displayAmount = amount;
/*     */       } 
/*     */       
/* 201 */       if (displayWithBase) {
/* 202 */         consumer.accept(
/* 203 */             CommonComponents.space().append(
/* 204 */               Component.translatable("attribute.modifier.equals." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 205 */                   .format(displayAmount), 
/* 206 */                   Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/*     */                 
/* 208 */                 })).withStyle(ChatFormatting.DARK_GREEN));
/*     */       }
/* 210 */       else if (amount > 0.0D) {
/* 211 */         consumer.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 212 */                 .format(displayAmount), 
/* 213 */                 Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/* 214 */               }).withStyle(((Attribute)attribute.value()).getStyle(true)));
/* 215 */       } else if (amount < 0.0D) {
/* 216 */         consumer.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 217 */                 .format(-displayAmount), 
/* 218 */                 Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/* 219 */               }).withStyle(((Attribute)attribute.value()).getStyle(false)));
/*     */       } 
/*     */     } }
/*     */   public static final class Hidden extends Record implements Display { public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Hidden; }
/*     */     public final boolean equals(Object o) { // Byte code:
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
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/* 225 */     private static final Hidden INSTANCE = new Hidden();
/*     */     
/* 227 */     private static final MapCodec<Hidden> CODEC = MapCodec.unit(INSTANCE);
/* 228 */     private static final StreamCodec<RegistryFriendlyByteBuf, Hidden> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */ 
/*     */ 
/*     */     
/* 232 */     public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.HIDDEN; }
/*     */     
/*     */     public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {} }
/*     */   
/*     */   public static final class OverrideText extends Record implements Display {
/*     */     private final Component component;
/*     */     
/* 239 */     public OverrideText(Component component) { this.component = component; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 239 */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText; } public Component component() { return this.component; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$OverrideText; } public final boolean equals(Object o) { // Byte code:
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
/* 240 */     private static final MapCodec<OverrideText> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/* 241 */           .fieldOf("value").forGetter(OverrideText::component))
/* 242 */         .apply(i, OverrideText::new));
/*     */     
/* 244 */     private static final StreamCodec<RegistryFriendlyByteBuf, OverrideText> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, OverrideText::component, OverrideText::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.OVERRIDE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) { consumer.accept(this.component); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Display.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
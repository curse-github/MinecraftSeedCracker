/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.StreamCodec;
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
/*     */ public final class Default
/*     */   extends Record
/*     */   implements ItemAttributeModifiers.Display
/*     */ {
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/ItemAttributeModifiers$Display$Default;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 166 */   private static final Default INSTANCE = new Default();
/*     */   
/* 168 */   private static final MapCodec<Default> CODEC = MapCodec.unit(INSTANCE);
/* 169 */   private static final StreamCodec<RegistryFriendlyByteBuf, Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */ 
/*     */ 
/*     */   
/* 173 */   public ItemAttributeModifiers.Display.Type type() { return ItemAttributeModifiers.Display.Type.DEFAULT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
/* 179 */     double displayAmount, amount = modifier.amount();
/* 180 */     boolean displayWithBase = false;
/*     */     
/* 182 */     if (player != null) {
/* 183 */       if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
/* 184 */         amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/* 185 */         displayWithBase = true;
/* 186 */       } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
/* 187 */         amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
/* 188 */         displayWithBase = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 193 */     if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
/* 194 */       displayAmount = amount * 100.0D;
/* 195 */     } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
/* 196 */       displayAmount = amount * 10.0D;
/*     */     } else {
/* 198 */       displayAmount = amount;
/*     */     } 
/*     */     
/* 201 */     if (displayWithBase) {
/* 202 */       consumer.accept(
/* 203 */           CommonComponents.space().append(
/* 204 */             Component.translatable("attribute.modifier.equals." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 205 */                 .format(displayAmount), 
/* 206 */                 Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/*     */               
/* 208 */               })).withStyle(ChatFormatting.DARK_GREEN));
/*     */     }
/* 210 */     else if (amount > 0.0D) {
/* 211 */       consumer.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 212 */               .format(displayAmount), 
/* 213 */               Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/* 214 */             }).withStyle(((Attribute)attribute.value()).getStyle(true)));
/* 215 */     } else if (amount < 0.0D) {
/* 216 */       consumer.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(), new Object[] { ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT
/* 217 */               .format(-displayAmount), 
/* 218 */               Component.translatable(((Attribute)attribute.value()).getDescriptionId())
/* 219 */             }).withStyle(((Attribute)attribute.value()).getStyle(false)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Display$Default.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
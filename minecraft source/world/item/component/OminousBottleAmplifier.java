/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class OminousBottleAmplifier extends Record implements ConsumableListener, TooltipProvider {
/*    */   private final int value;
/*    */   public static final int EFFECT_DURATION = 120000;
/*    */   
/* 23 */   public OminousBottleAmplifier(int value) { this.value = value; } public static final int MIN_AMPLIFIER = 0; public static final int MAX_AMPLIFIER = 4; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/OminousBottleAmplifier;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/item/component/OminousBottleAmplifier; } public int value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/OminousBottleAmplifier;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/OminousBottleAmplifier; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/OminousBottleAmplifier;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/OminousBottleAmplifier;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 27 */   public static final Codec<OminousBottleAmplifier> CODEC = ExtraCodecs.intRange(0, 4).xmap(OminousBottleAmplifier::new, OminousBottleAmplifier::value);
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, OminousBottleAmplifier> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, OminousBottleAmplifier::value, OminousBottleAmplifier::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) { user.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, this.value, false, false, true)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 40 */     List<MobEffectInstance> effects = List.of(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, this.value, false, false, true));
/* 41 */     PotionContents.addPotionTooltip(effects, consumer, 1.0F, context.tickRate());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\OminousBottleAmplifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
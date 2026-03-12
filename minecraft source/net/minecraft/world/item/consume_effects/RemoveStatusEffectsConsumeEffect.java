/*    */ package net.minecraft.world.item.consume_effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class RemoveStatusEffectsConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final HolderSet<MobEffect> effects;
/*    */   
/* 17 */   public RemoveStatusEffectsConsumeEffect(HolderSet<MobEffect> effects) { this.effects = effects; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect; } public HolderSet<MobEffect> effects() { return this.effects; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 18 */   public static final MapCodec<RemoveStatusEffectsConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 19 */         RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("effects").forGetter(RemoveStatusEffectsConsumeEffect::effects))
/* 20 */       .apply(i, RemoveStatusEffectsConsumeEffect::new));
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, RemoveStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.composite(
/* 22 */       ByteBufCodecs.holderSet(Registries.MOB_EFFECT), RemoveStatusEffectsConsumeEffect::effects, RemoveStatusEffectsConsumeEffect::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public RemoveStatusEffectsConsumeEffect(Holder<MobEffect> only) { this(HolderSet.direct(new Holder[] { only })); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public ConsumeEffect.Type<RemoveStatusEffectsConsumeEffect> getType() { return ConsumeEffect.Type.REMOVE_EFFECTS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean apply(Level level, ItemStack stack, LivingEntity user) {
/* 37 */     boolean hasRemovedAny = false;
/* 38 */     for (Holder<MobEffect> effect : this.effects) {
/* 39 */       if (user.removeEffect(effect)) {
/* 40 */         hasRemovedAny = true;
/*    */       }
/*    */     } 
/*    */     
/* 44 */     return hasRemovedAny;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\consume_effects\RemoveStatusEffectsConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
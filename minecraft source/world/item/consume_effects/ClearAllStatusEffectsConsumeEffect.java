/*    */ package net.minecraft.world.item.consume_effects;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClearAllStatusEffectsConsumeEffect extends Record implements ConsumeEffect {
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect; }
/*    */   
/* 11 */   public static final ClearAllStatusEffectsConsumeEffect INSTANCE = new ClearAllStatusEffectsConsumeEffect(); public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/ClearAllStatusEffectsConsumeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final MapCodec<ClearAllStatusEffectsConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClearAllStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public ConsumeEffect.Type<ClearAllStatusEffectsConsumeEffect> getType() { return ConsumeEffect.Type.CLEAR_ALL_EFFECTS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean apply(Level level, ItemStack stack, LivingEntity user) { return user.removeAllEffects(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\consume_effects\ClearAllStatusEffectsConsumeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.consume_effects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Type<T extends ConsumeEffect>
/*    */   extends Record
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type<TT;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type<TT;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/ConsumeEffect$Type<TT;>; }
/*    */   
/* 25 */   public Type(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) { this.codec = codec; this.streamCodec = streamCodec; } public MapCodec<T> codec() { return this.codec; } public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/* 26 */   public static final Type<ApplyStatusEffectsConsumeEffect> APPLY_EFFECTS = register("apply_effects", ApplyStatusEffectsConsumeEffect.CODEC, ApplyStatusEffectsConsumeEffect.STREAM_CODEC);
/* 27 */   public static final Type<RemoveStatusEffectsConsumeEffect> REMOVE_EFFECTS = register("remove_effects", RemoveStatusEffectsConsumeEffect.CODEC, RemoveStatusEffectsConsumeEffect.STREAM_CODEC);
/* 28 */   public static final Type<ClearAllStatusEffectsConsumeEffect> CLEAR_ALL_EFFECTS = register("clear_all_effects", ClearAllStatusEffectsConsumeEffect.CODEC, ClearAllStatusEffectsConsumeEffect.STREAM_CODEC);
/* 29 */   public static final Type<TeleportRandomlyConsumeEffect> TELEPORT_RANDOMLY = register("teleport_randomly", TeleportRandomlyConsumeEffect.CODEC, TeleportRandomlyConsumeEffect.STREAM_CODEC);
/* 30 */   public static final Type<PlaySoundConsumeEffect> PLAY_SOUND = register("play_sound", PlaySoundConsumeEffect.CODEC, PlaySoundConsumeEffect.STREAM_CODEC);
/*    */ 
/*    */   
/* 33 */   private static <T extends ConsumeEffect> Type<T> register(String name, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) { return (Type)Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, name, new Type(codec, streamCodec)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\consume_effects\ConsumeEffect$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
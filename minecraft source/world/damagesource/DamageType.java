/*    */ package net.minecraft.world.damagesource;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ 
/*    */ public final class DamageType extends Record {
/*    */   private final String msgId;
/*    */   private final DamageScaling scaling;
/*    */   
/* 12 */   public DamageType(String msgId, DamageScaling scaling, float exhaustion, DamageEffects effects, DeathMessageType deathMessageType) { this.msgId = msgId; this.scaling = scaling; this.exhaustion = exhaustion; this.effects = effects; this.deathMessageType = deathMessageType; } private final float exhaustion; private final DamageEffects effects; private final DeathMessageType deathMessageType; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/damagesource/DamageType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/damagesource/DamageType; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/damagesource/DamageType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/damagesource/DamageType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/damagesource/DamageType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/damagesource/DamageType;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public String msgId() { return this.msgId; } public DamageScaling scaling() { return this.scaling; } public float exhaustion() { return this.exhaustion; } public DamageEffects effects() { return this.effects; } public DeathMessageType deathMessageType() { return this.deathMessageType; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final Codec<DamageType> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 20 */         .fieldOf("message_id").forGetter(DamageType::msgId), DamageScaling.CODEC
/* 21 */         .fieldOf("scaling").forGetter(DamageType::scaling), Codec.FLOAT
/* 22 */         .fieldOf("exhaustion").forGetter(DamageType::exhaustion), DamageEffects.CODEC
/* 23 */         .optionalFieldOf("effects", DamageEffects.HURT).forGetter(DamageType::effects), DeathMessageType.CODEC
/* 24 */         .optionalFieldOf("death_message_type", DeathMessageType.DEFAULT).forGetter(DamageType::deathMessageType))
/* 25 */       .apply(i, DamageType::new));
/*    */   
/* 27 */   public static final Codec<Holder<DamageType>> CODEC = RegistryFixedCodec.create(Registries.DAMAGE_TYPE);
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<DamageType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.DAMAGE_TYPE);
/*    */ 
/*    */   
/* 31 */   public DamageType(String msgdId, DamageScaling scaling, float exhaustion) { this(msgdId, scaling, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public DamageType(String msgdId, DamageScaling scaling, float exhaustion, DamageEffects effects) { this(msgdId, scaling, exhaustion, effects, DeathMessageType.DEFAULT); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public DamageType(String msgdId, float exhaustion, DamageEffects effects) { this(msgdId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, effects); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public DamageType(String msgdId, float exhaustion) { this(msgdId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\DamageType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
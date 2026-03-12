/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public final class UseEffects extends Record {
/*    */   private final boolean canSprint;
/*    */   private final boolean interactVibrations;
/*    */   private final float speedMultiplier;
/*    */   
/*  9 */   public UseEffects(boolean canSprint, boolean interactVibrations, float speedMultiplier) { this.canSprint = canSprint; this.interactVibrations = interactVibrations; this.speedMultiplier = speedMultiplier; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseEffects; } public boolean canSprint() { return this.canSprint; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseEffects; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseEffects;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean interactVibrations() { return this.interactVibrations; } public float speedMultiplier() { return this.speedMultiplier; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2F);
/*    */   
/* 16 */   public static final Codec<UseEffects> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 17 */         .optionalFieldOf("can_sprint", Boolean.valueOf(DEFAULT.canSprint)).forGetter(UseEffects::canSprint), Codec.BOOL
/* 18 */         .optionalFieldOf("interact_vibrations", Boolean.valueOf(DEFAULT.interactVibrations)).forGetter(UseEffects::interactVibrations), 
/* 19 */         Codec.floatRange(0.0F, 1.0F).optionalFieldOf("speed_multiplier", Float.valueOf(DEFAULT.speedMultiplier)).forGetter(UseEffects::speedMultiplier))
/* 20 */       .apply(i, UseEffects::new));
/* 21 */   public static final StreamCodec<ByteBuf, UseEffects> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, UseEffects::canSprint, ByteBufCodecs.BOOL, UseEffects::interactVibrations, ByteBufCodecs.FLOAT, UseEffects::speedMultiplier, UseEffects::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\UseEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
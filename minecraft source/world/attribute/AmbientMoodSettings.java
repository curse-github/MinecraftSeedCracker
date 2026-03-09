/*    */ package net.minecraft.world.attribute;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public final class AmbientMoodSettings extends Record {
/*    */   private final Holder<SoundEvent> soundEvent;
/*    */   private final int tickDelay;
/*    */   private final int blockSearchExtent;
/*    */   private final double soundPositionOffset;
/*    */   
/* 10 */   public AmbientMoodSettings(Holder<SoundEvent> soundEvent, int tickDelay, int blockSearchExtent, double soundPositionOffset) { this.soundEvent = soundEvent; this.tickDelay = tickDelay; this.blockSearchExtent = blockSearchExtent; this.soundPositionOffset = soundPositionOffset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientMoodSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings; } public Holder<SoundEvent> soundEvent() { return this.soundEvent; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientMoodSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientMoodSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientMoodSettings;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int tickDelay() { return this.tickDelay; } public int blockSearchExtent() { return this.blockSearchExtent; } public double soundPositionOffset() { return this.soundPositionOffset; }
/* 11 */   public static final Codec<AmbientMoodSettings> CODEC = RecordCodecBuilder.create(i -> i.group(SoundEvent.CODEC
/* 12 */         .fieldOf("sound").forGetter(()), Codec.INT
/* 13 */         .fieldOf("tick_delay").forGetter(()), Codec.INT
/* 14 */         .fieldOf("block_search_extent").forGetter(()), Codec.DOUBLE
/* 15 */         .fieldOf("offset").forGetter(()))
/* 16 */       .apply(i, AmbientMoodSettings::new));
/*    */   
/* 18 */   public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS = new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AmbientMoodSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
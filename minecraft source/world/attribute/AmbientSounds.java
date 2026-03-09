/*    */ package net.minecraft.world.attribute;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public final class AmbientSounds extends Record {
/*    */   private final Optional<Holder<SoundEvent>> loop;
/*    */   private final Optional<AmbientMoodSettings> mood;
/*    */   private final List<AmbientAdditionsSettings> additions;
/*    */   
/* 12 */   public AmbientSounds(Optional<Holder<SoundEvent>> loop, Optional<AmbientMoodSettings> mood, List<AmbientAdditionsSettings> additions) { this.loop = loop; this.mood = mood; this.additions = additions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientSounds;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientSounds; } public Optional<Holder<SoundEvent>> loop() { return this.loop; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientSounds;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientSounds; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientSounds;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientSounds;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<AmbientMoodSettings> mood() { return this.mood; } public List<AmbientAdditionsSettings> additions() { return this.additions; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final AmbientSounds EMPTY = new AmbientSounds(Optional.empty(), Optional.empty(), List.of());
/* 18 */   public static final AmbientSounds LEGACY_CAVE_SETTINGS = new AmbientSounds(Optional.empty(), Optional.of(AmbientMoodSettings.LEGACY_CAVE_SETTINGS), List.of());
/*    */   
/* 20 */   public static final Codec<AmbientSounds> CODEC = RecordCodecBuilder.create(i -> i.group(SoundEvent.CODEC
/* 21 */         .optionalFieldOf("loop").forGetter(AmbientSounds::loop), AmbientMoodSettings.CODEC
/* 22 */         .optionalFieldOf("mood").forGetter(AmbientSounds::mood), 
/* 23 */         ExtraCodecs.compactListCodec(AmbientAdditionsSettings.CODEC).optionalFieldOf("additions", List.of()).forGetter(AmbientSounds::additions))
/* 24 */       .apply(i, AmbientSounds::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AmbientSounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
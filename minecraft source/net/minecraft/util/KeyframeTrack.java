/*    */ package net.minecraft.util;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public final class KeyframeTrack<T> extends Record {
/*    */   private final List<Keyframe<T>> keyframes;
/*    */   private final EasingType easingType;
/*    */   
/* 15 */   public List<Keyframe<T>> keyframes() { return this.keyframes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/KeyframeTrack;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrack;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrack<TT;>; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/KeyframeTrack;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrack;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrack<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/KeyframeTrack;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/KeyframeTrack;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 15 */     //   0	8	0	this	Lnet/minecraft/util/KeyframeTrack<TT;>; } public EasingType easingType() { return this.easingType; }
/*    */ 
/*    */ 
/*    */   
/*    */   public KeyframeTrack(List<Keyframe<T>> keyframes, EasingType easingType) {
/* 20 */     if (keyframes.isEmpty())
/* 21 */       throw new IllegalArgumentException("Track has no keyframes"); 
/*    */     this.keyframes = keyframes;
/*    */     this.easingType = easingType;
/*    */   }
/*    */   public static <T> MapCodec<KeyframeTrack<T>> mapCodec(Codec<T> valueCodec) {
/* 26 */     Codec<List<Keyframe<T>>> keyframesCodec = Keyframe.codec(valueCodec).listOf().validate(KeyframeTrack::validateKeyframes);
/* 27 */     return RecordCodecBuilder.mapCodec(i -> i.group(keyframesCodec
/* 28 */           .fieldOf("keyframes").forGetter(KeyframeTrack::keyframes), EasingType.CODEC
/* 29 */           .optionalFieldOf("ease", EasingType.LINEAR).forGetter(KeyframeTrack::easingType))
/* 30 */         .apply(i, KeyframeTrack::new));
/*    */   }
/*    */   
/*    */   private static <T> DataResult<List<Keyframe<T>>> validateKeyframes(List<Keyframe<T>> keyframes) {
/* 34 */     if (keyframes.isEmpty()) {
/* 35 */       return DataResult.error(() -> "Keyframes must not be empty");
/*    */     }
/* 37 */     if (!Comparators.isInOrder(keyframes, Comparator.comparingInt(Keyframe::ticks))) {
/* 38 */       return DataResult.error(() -> "Keyframes must be ordered by ticks field");
/*    */     }
/* 40 */     if (keyframes.size() > 1) {
/* 41 */       int repeatCount = 0;
/* 42 */       int lastTicks = ((Keyframe)keyframes.getLast()).ticks();
/* 43 */       for (Keyframe<T> keyframe : keyframes) {
/* 44 */         if (keyframe.ticks() == lastTicks) {
/* 45 */           if (++repeatCount > 2) {
/* 46 */             return DataResult.error(() -> "More than 2 keyframes on same tick: " + keyframe.ticks());
/*    */           }
/*    */         } else {
/* 49 */           repeatCount = 0;
/*    */         } 
/* 51 */         lastTicks = keyframe.ticks();
/*    */       } 
/*    */     } 
/* 54 */     return DataResult.success(keyframes);
/*    */   }
/*    */   
/*    */   public static DataResult<KeyframeTrack<?>> validatePeriod(KeyframeTrack<?> track, int periodTicks) {
/* 58 */     for (Keyframe<?> keyframe : track.keyframes()) {
/* 59 */       int tick = keyframe.ticks();
/* 60 */       if (tick < 0 || tick > periodTicks) {
/* 61 */         return DataResult.error(() -> "Keyframe at tick " + keyframe.ticks() + " must be in range [0; " + periodTicks + "]");
/*    */       }
/*    */     } 
/* 64 */     return DataResult.success(track);
/*    */   }
/*    */ 
/*    */   
/* 68 */   public KeyframeTrackSampler<T> bakeSampler(Optional<Integer> periodTicks, LerpFunction<T> lerp) { return new KeyframeTrackSampler(this, periodTicks, lerp); }
/*    */   
/*    */   public static class Builder<T>
/*    */     extends Object {
/* 72 */     private final ImmutableList.Builder<Keyframe<T>> keyframes = ImmutableList.builder();
/* 73 */     private EasingType easing = EasingType.LINEAR;
/*    */     
/*    */     public Builder<T> addKeyframe(int ticks, T value) {
/* 76 */       this.keyframes.add(new Keyframe(ticks, value));
/* 77 */       return this;
/*    */     }
/*    */     
/*    */     public Builder<T> setEasing(EasingType easing) {
/* 81 */       this.easing = easing;
/* 82 */       return this;
/*    */     }
/*    */     
/*    */     public KeyframeTrack<T> build() {
/* 86 */       List<Keyframe<T>> keyframes = (List)KeyframeTrack.validateKeyframes(this.keyframes.build()).getOrThrow();
/* 87 */       return new KeyframeTrack(keyframes, this.easing);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\KeyframeTrack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder<T>
/*    */   extends Object
/*    */ {
/* 72 */   private final ImmutableList.Builder<Keyframe<T>> keyframes = ImmutableList.builder();
/* 73 */   private EasingType easing = EasingType.LINEAR;
/*    */   
/*    */   public Builder<T> addKeyframe(int ticks, T value) {
/* 76 */     this.keyframes.add(new Keyframe(ticks, value));
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public Builder<T> setEasing(EasingType easing) {
/* 81 */     this.easing = easing;
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public KeyframeTrack<T> build() {
/* 86 */     List<Keyframe<T>> keyframes = (List)KeyframeTrack.validateKeyframes(this.keyframes.build()).getOrThrow();
/* 87 */     return new KeyframeTrack(keyframes, this.easing);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\KeyframeTrack$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
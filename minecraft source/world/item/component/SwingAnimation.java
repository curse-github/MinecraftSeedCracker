/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ 
/*    */ public final class SwingAnimation extends Record {
/*    */   private final SwingAnimationType type;
/*    */   private final int duration;
/*    */   
/* 11 */   public SwingAnimation(SwingAnimationType type, int duration) { this.type = type; this.duration = duration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SwingAnimation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/component/SwingAnimation; } public SwingAnimationType type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SwingAnimation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SwingAnimation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SwingAnimation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SwingAnimation;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public int duration() { return this.duration; }
/* 12 */   public static final SwingAnimation DEFAULT = new SwingAnimation(SwingAnimationType.WHACK, 6);
/* 13 */   public static final Codec<SwingAnimation> CODEC = RecordCodecBuilder.create(i -> i.group(SwingAnimationType.CODEC
/* 14 */         .optionalFieldOf("type", DEFAULT.type).forGetter(SwingAnimation::type), ExtraCodecs.POSITIVE_INT
/* 15 */         .optionalFieldOf("duration", Integer.valueOf(DEFAULT.duration)).forGetter(SwingAnimation::duration))
/* 16 */       .apply(i, SwingAnimation::new));
/* 17 */   public static final StreamCodec<ByteBuf, SwingAnimation> STREAM_CODEC = StreamCodec.composite(SwingAnimationType.STREAM_CODEC, SwingAnimation::type, ByteBufCodecs.VAR_INT, SwingAnimation::duration, SwingAnimation::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\SwingAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
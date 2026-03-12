/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*    */ 
/*    */ public class Score
/*    */   implements ReadOnlyScoreInfo {
/*    */   private int value;
/*    */   private boolean locked = true;
/*    */   private Component display;
/*    */   private NumberFormat numberFormat;
/*    */   
/*    */   public Score() {}
/*    */   
/*    */   public Score(Packed packed) {
/* 24 */     this.value = packed.value;
/* 25 */     this.locked = packed.locked;
/* 26 */     this.display = (Component)packed.display.orElse(null);
/* 27 */     this.numberFormat = (NumberFormat)packed.numberFormat.orElse(null);
/*    */   }
/*    */   
/*    */   public Packed pack() {
/* 31 */     return new Packed(this.value, this.locked, 
/*    */ 
/*    */         
/* 34 */         Optional.ofNullable(this.display), 
/* 35 */         Optional.ofNullable(this.numberFormat));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public int value() { return this.value; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void value(int score) { this.value = score; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public boolean isLocked() { return this.locked; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public void setLocked(boolean locked) { this.locked = locked; }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public Component display() { return this.display; }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public void display(Component display) { this.display = display; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public NumberFormat numberFormat() { return this.numberFormat; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public void numberFormat(NumberFormat numberFormat) { this.numberFormat = numberFormat; }
/*    */   public static final class Packed extends Record { private final int value; private final boolean locked; private final Optional<Component> display; private final Optional<NumberFormat> numberFormat;
/*    */     
/* 74 */     public Packed(int value, boolean locked, Optional<Component> display, Optional<NumberFormat> numberFormat) { this.value = value; this.locked = locked; this.display = display; this.numberFormat = numberFormat; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Score$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 74 */       //   0	7	0	this	Lnet/minecraft/world/scores/Score$Packed; } public int value() { return this.value; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Score$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/scores/Score$Packed; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Score$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/scores/Score$Packed;
/* 74 */       //   0	8	1	o	Ljava/lang/Object; } public boolean locked() { return this.locked; } public Optional<Component> display() { return this.display; } public Optional<NumberFormat> numberFormat() { return this.numberFormat; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 80 */     public static final MapCodec<Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 81 */           .optionalFieldOf("Score", Integer.valueOf(0)).forGetter(Packed::value), Codec.BOOL
/* 82 */           .optionalFieldOf("Locked", Boolean.valueOf(false)).forGetter(Packed::locked), ComponentSerialization.CODEC
/* 83 */           .optionalFieldOf("display").forGetter(Packed::display), NumberFormatTypes.CODEC
/* 84 */           .optionalFieldOf("format").forGetter(Packed::numberFormat))
/* 85 */         .apply(i, Packed::new)); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\Score.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
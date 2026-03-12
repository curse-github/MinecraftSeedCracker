/*     */ package net.minecraft.server.dialog.input;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class NumberRangeInput extends Record implements InputControl {
/*     */   private final int width;
/*     */   private final Component label;
/*     */   private final String labelFormat;
/*     */   private final RangeInfo rangeInfo;
/*     */   
/*  15 */   public NumberRangeInput(int width, Component label, String labelFormat, RangeInfo rangeInfo) { this.width = width; this.label = label; this.labelFormat = labelFormat; this.rangeInfo = rangeInfo; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/NumberRangeInput;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  15 */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/NumberRangeInput;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/NumberRangeInput;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput;
/*  15 */     //   0	8	1	o	Ljava/lang/Object; } public Component label() { return this.label; } public String labelFormat() { return this.labelFormat; } public RangeInfo rangeInfo() { return this.rangeInfo; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  22 */   public static final MapCodec<NumberRangeInput> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Dialog.WIDTH_CODEC
/*  23 */         .optionalFieldOf("width", Integer.valueOf(200)).forGetter(NumberRangeInput::width), ComponentSerialization.CODEC
/*  24 */         .fieldOf("label").forGetter(NumberRangeInput::label), Codec.STRING
/*  25 */         .optionalFieldOf("label_format", "options.generic_value").forGetter(NumberRangeInput::labelFormat), RangeInfo.MAP_CODEC
/*  26 */         .forGetter(NumberRangeInput::rangeInfo))
/*  27 */       .apply(i, NumberRangeInput::new));
/*     */ 
/*     */ 
/*     */   
/*  31 */   public MapCodec<NumberRangeInput> mapCodec() { return MAP_CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public Component computeLabel(String value) { return Component.translatable(this.labelFormat, new Object[] { this.label, value }); }
/*     */   public static final class RangeInfo extends Record { private final float start; private final float end; private final Optional<Float> initial;
/*     */     private final Optional<Float> step;
/*     */     
/*  39 */     public RangeInfo(float start, float end, Optional<Float> initial, Optional<Float> step) { this.start = start; this.end = end; this.initial = initial; this.step = step; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*  39 */       //   0	8	1	o	Ljava/lang/Object; } public float start() { return this.start; } public float end() { return this.end; } public Optional<Float> initial() { return this.initial; } public Optional<Float> step() { return this.step; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     public static final MapCodec<RangeInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/*  47 */           .fieldOf("start").forGetter(RangeInfo::start), Codec.FLOAT
/*  48 */           .fieldOf("end").forGetter(RangeInfo::end), Codec.FLOAT
/*  49 */           .optionalFieldOf("initial").forGetter(RangeInfo::initial), ExtraCodecs.POSITIVE_FLOAT
/*  50 */           .optionalFieldOf("step").forGetter(RangeInfo::step))
/*  51 */         .apply(i, RangeInfo::new))
/*  52 */       .validate(range -> {
/*  53 */           if (range.initial.isPresent()) {
/*  54 */             double initial = ((Float)range.initial.get()).floatValue();
/*  55 */             double min = Math.min(range.start, range.end);
/*  56 */             double max = Math.max(range.start, range.end);
/*  57 */             if (initial < min || initial > max) {
/*  58 */               return DataResult.error(());
/*     */             }
/*     */           } 
/*  61 */           return DataResult.success(range);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float computeScaledValue(float sliderValue) {
/*  68 */       float valueInRange = Mth.lerp(sliderValue, this.start, this.end);
/*     */       
/*  70 */       if (this.step.isEmpty())
/*     */       {
/*  72 */         return valueInRange;
/*     */       }
/*     */       
/*  75 */       float step = ((Float)this.step.get()).floatValue();
/*  76 */       float initialValue = initialScaledValue();
/*  77 */       float deltaToInitial = valueInRange - initialValue;
/*     */       
/*  79 */       int stepsOutsideInitial = Math.round(deltaToInitial / step);
/*  80 */       float result = initialValue + stepsOutsideInitial * step;
/*  81 */       if (!isOutOfRange(result)) {
/*  82 */         return result;
/*     */       }
/*     */       
/*  85 */       int oneStepLess = stepsOutsideInitial - Mth.sign(stepsOutsideInitial);
/*  86 */       return initialValue + oneStepLess * step;
/*     */     }
/*     */     
/*     */     private boolean isOutOfRange(float scaledValue) {
/*  90 */       float sliderPos = scaledValueToSlider(scaledValue);
/*  91 */       return (sliderPos < 0.0D || sliderPos > 1.0D);
/*     */     }
/*     */     
/*     */     private float initialScaledValue() {
/*  95 */       if (this.initial.isPresent()) {
/*  96 */         return ((Float)this.initial.get()).floatValue();
/*     */       }
/*     */       
/*  99 */       return (this.start + this.end) / 2.0F;
/*     */     }
/*     */     
/*     */     public float initialSliderValue() {
/* 103 */       float value = initialScaledValue();
/* 104 */       return scaledValueToSlider(value);
/*     */     }
/*     */     
/*     */     private float scaledValueToSlider(float value) {
/* 108 */       if (this.start == this.end) {
/* 109 */         return 0.5F;
/*     */       }
/* 111 */       return Mth.inverseLerp(value, this.start, this.end);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\input\NumberRangeInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
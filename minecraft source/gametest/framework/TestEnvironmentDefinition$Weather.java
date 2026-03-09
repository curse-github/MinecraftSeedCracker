/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Weather
/*     */   extends Record
/*     */   implements TestEnvironmentDefinition
/*     */ {
/*     */   private final Type weather;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #52	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #52	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #52	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$Weather;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  52 */   public Weather(Type weather) { this.weather = weather; } public Type weather() { return this.weather; }
/*     */   
/*  54 */   public enum Type implements StringRepresentable { CLEAR("clear", 100000, 0, false, false),
/*  55 */     RAIN("rain", 0, 100000, true, false),
/*  56 */     THUNDER("thunder", 0, 100000, true, true); public static final Codec<Type> CODEC;
/*     */     
/*     */     static  {
/*  59 */       CODEC = StringRepresentable.fromEnum(Type::values);
/*     */     }
/*     */     private final String id;
/*     */     private final int clearTime;
/*     */     private final int rainTime;
/*     */     private final boolean raining;
/*     */     private final boolean thundering;
/*     */     
/*     */     Type(String id, int clearTime, int rainTime, boolean raining, boolean thundering) {
/*  68 */       this.id = id;
/*  69 */       this.clearTime = clearTime;
/*  70 */       this.rainTime = rainTime;
/*  71 */       this.raining = raining;
/*  72 */       this.thundering = thundering;
/*     */     }
/*     */ 
/*     */     
/*  76 */     void apply(ServerLevel level) { level.setWeatherParameters(this.clearTime, this.rainTime, this.raining, this.thundering); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     public String getSerializedName() { return this.id; } }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final MapCodec<Weather> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Type.CODEC
/*  86 */         .fieldOf("weather").forGetter(Weather::weather))
/*  87 */       .apply(i, Weather::new));
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void setup(ServerLevel level) { this.weather.apply(level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void teardown(ServerLevel level) { level.resetWeatherCycle(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public MapCodec<Weather> codec() { return CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$Weather.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
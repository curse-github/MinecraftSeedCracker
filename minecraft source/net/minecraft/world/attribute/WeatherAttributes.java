/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*    */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*    */ import net.minecraft.world.attribute.modifier.FloatWithAlpha;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.timeline.Timelines;
/*    */ 
/*    */ 
/*    */ public class WeatherAttributes
/*    */ {
/* 15 */   public static final EnvironmentAttributeMap RAIN = EnvironmentAttributeMap.builder()
/* 16 */     .modify(EnvironmentAttributes.SKY_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.6F, 0.75F))
/* 17 */     .modify(EnvironmentAttributes.FOG_COLOR, ColorModifier.MULTIPLY_RGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.6F)))
/* 18 */     .modify(EnvironmentAttributes.CLOUD_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.5F))
/* 19 */     .modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.3125F))
/* 20 */     .modify(EnvironmentAttributes.SKY_LIGHT_COLOR, ColorModifier.ALPHA_BLEND, Integer.valueOf(ARGB.color(0.3125F, Timelines.NIGHT_SKY_LIGHT_COLOR)))
/* 21 */     .modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.3125F))
/* 22 */     .set(EnvironmentAttributes.STAR_BRIGHTNESS, Float.valueOf(0.0F))
/* 23 */     .modify(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, ColorModifier.MULTIPLY_ARGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.6F)))
/* 24 */     .set(EnvironmentAttributes.BEES_STAY_IN_HIVE, Boolean.valueOf(true))
/* 25 */     .build();
/*    */   
/* 27 */   public static final EnvironmentAttributeMap THUNDER = EnvironmentAttributeMap.builder()
/* 28 */     .modify(EnvironmentAttributes.SKY_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.94F))
/* 29 */     .modify(EnvironmentAttributes.FOG_COLOR, ColorModifier.MULTIPLY_RGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.3F)))
/* 30 */     .modify(EnvironmentAttributes.CLOUD_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.095F, 0.94F))
/* 31 */     .modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.52734375F))
/* 32 */     .modify(EnvironmentAttributes.SKY_LIGHT_COLOR, ColorModifier.ALPHA_BLEND, Integer.valueOf(ARGB.color(0.52734375F, Timelines.NIGHT_SKY_LIGHT_COLOR)))
/* 33 */     .modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.52734375F))
/* 34 */     .set(EnvironmentAttributes.STAR_BRIGHTNESS, Float.valueOf(0.0F))
/* 35 */     .modify(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, ColorModifier.MULTIPLY_ARGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.3F)))
/* 36 */     .set(EnvironmentAttributes.BEES_STAY_IN_HIVE, Boolean.valueOf(true))
/* 37 */     .build();
/*    */   
/* 39 */   private static final Set<EnvironmentAttribute<?>> WEATHER_ATTRIBUTES = Sets.union(RAIN.keySet(), THUNDER.keySet());
/*    */   
/*    */   public static void addBuiltinLayers(EnvironmentAttributeSystem.Builder system, WeatherAccess weatherAccess) {
/* 42 */     for (EnvironmentAttribute<?> attribute : WEATHER_ATTRIBUTES) {
/* 43 */       addLayer(system, weatherAccess, attribute);
/*    */     }
/*    */   }
/*    */   
/*    */   private static <Value> void addLayer(EnvironmentAttributeSystem.Builder system, WeatherAccess weatherAccess, EnvironmentAttribute<Value> attribute) {
/* 48 */     EnvironmentAttributeMap.Entry<Value, ?> rainEntry = RAIN.get(attribute);
/* 49 */     EnvironmentAttributeMap.Entry<Value, ?> thunderEntry = THUNDER.get(attribute);
/* 50 */     system.addTimeBasedLayer(attribute, (result, cacheTickId) -> {
/* 51 */           float thunderLevel = weatherAccess.thunderLevel();
/* 52 */           float rainLevel = weatherAccess.rainLevel() - thunderLevel;
/* 53 */           if (rainEntry != null && rainLevel > 0.0F) {
/* 54 */             Value rainValue = (Value)rainEntry.applyModifier(result);
/* 55 */             result = attribute.type().stateChangeLerp().apply(rainLevel, result, rainValue);
/*    */           } 
/* 57 */           if (thunderEntry != null && thunderLevel > 0.0F) {
/* 58 */             Value thunderValue = (Value)thunderEntry.applyModifier(result);
/* 59 */             result = attribute.type().stateChangeLerp().apply(thunderLevel, result, thunderValue);
/*    */           } 
/* 61 */           return result;
/*    */         });
/*    */   }
/*    */   
/*    */   public static interface WeatherAccess
/*    */   {
/* 67 */     static WeatherAccess from(final Level level) { return new WeatherAccess()
/*    */         {
/*    */           public float rainLevel() {
/* 70 */             return level.getRainLevel(1.0F);
/*    */           }
/*    */ 
/*    */ 
/*    */           
/* 75 */           public float thunderLevel() { return level.getThunderLevel(1.0F); } }; } float rainLevel(); float thunderLevel(); } class null implements WeatherAccess { public float thunderLevel() { return level.getThunderLevel(1.0F); }
/*    */     
/*    */     public float rainLevel() { return level.getRainLevel(1.0F); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\WeatherAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
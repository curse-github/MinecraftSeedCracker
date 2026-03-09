/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.TriState;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ 
/*    */ public interface AttributeTypes
/*    */ {
/* 18 */   public static final AttributeType<Boolean> BOOLEAN = register("boolean", AttributeType.ofNotInterpolated(Codec.BOOL, AttributeModifier.BOOLEAN_LIBRARY));
/* 19 */   public static final AttributeType<TriState> TRI_STATE = register("tri_state", AttributeType.ofNotInterpolated(TriState.CODEC));
/* 20 */   public static final AttributeType<Float> FLOAT = register("float", AttributeType.ofInterpolated(Codec.FLOAT, AttributeModifier.FLOAT_LIBRARY, 
/*    */ 
/*    */         
/* 23 */         LerpFunction.ofFloat()));
/*    */   
/* 25 */   public static final AttributeType<Float> ANGLE_DEGREES = register("angle_degrees", AttributeType.ofInterpolated(Codec.FLOAT, AttributeModifier.FLOAT_LIBRARY, 
/*    */ 
/*    */         
/* 28 */         LerpFunction.ofFloat(), 
/* 29 */         LerpFunction.ofDegrees(90.0F)));
/*    */   
/* 31 */   public static final AttributeType<Integer> RGB_COLOR = register("rgb_color", AttributeType.ofInterpolated(ExtraCodecs.STRING_RGB_COLOR, AttributeModifier.RGB_COLOR_LIBRARY, 
/*    */ 
/*    */         
/* 34 */         LerpFunction.ofColor()));
/*    */   
/* 36 */   public static final AttributeType<Integer> ARGB_COLOR = register("argb_color", AttributeType.ofInterpolated(ExtraCodecs.STRING_ARGB_COLOR, AttributeModifier.ARGB_COLOR_LIBRARY, 
/*    */ 
/*    */         
/* 39 */         LerpFunction.ofColor()));
/*    */ 
/*    */   
/* 42 */   public static final AttributeType<MoonPhase> MOON_PHASE = register("moon_phase", AttributeType.ofNotInterpolated(MoonPhase.CODEC));
/* 43 */   public static final AttributeType<Activity> ACTIVITY = register("activity", AttributeType.ofNotInterpolated(BuiltInRegistries.ACTIVITY.byNameCodec()));
/* 44 */   public static final AttributeType<BedRule> BED_RULE = register("bed_rule", AttributeType.ofNotInterpolated(BedRule.CODEC));
/* 45 */   public static final AttributeType<ParticleOptions> PARTICLE = register("particle", AttributeType.ofNotInterpolated(ParticleTypes.CODEC));
/* 46 */   public static final AttributeType<List<AmbientParticle>> AMBIENT_PARTICLES = register("ambient_particles", AttributeType.ofNotInterpolated(AmbientParticle.CODEC.listOf()));
/* 47 */   public static final AttributeType<BackgroundMusic> BACKGROUND_MUSIC = register("background_music", AttributeType.ofNotInterpolated(BackgroundMusic.CODEC));
/* 48 */   public static final AttributeType<AmbientSounds> AMBIENT_SOUNDS = register("ambient_sounds", AttributeType.ofNotInterpolated(AmbientSounds.CODEC));
/*    */   
/* 50 */   public static final Codec<AttributeType<?>> CODEC = BuiltInRegistries.ATTRIBUTE_TYPE.byNameCodec();
/*    */ 
/*    */   
/* 53 */   static AttributeType<?> bootstrap(Registry<AttributeType<?>> registry) { return BOOLEAN; }
/*    */ 
/*    */   
/*    */   static <Value> AttributeType<Value> register(String name, AttributeType<Value> type) {
/* 57 */     Registry.register(BuiltInRegistries.ATTRIBUTE_TYPE, Identifier.withDefaultNamespace(name), type);
/* 58 */     return type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AttributeTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
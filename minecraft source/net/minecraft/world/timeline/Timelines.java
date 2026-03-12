/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.EasingType;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.attribute.modifier.BooleanModifier;
/*     */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*     */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ public interface Timelines
/*     */ {
/*  21 */   public static final ResourceKey<Timeline> DAY = key("day");
/*  22 */   public static final ResourceKey<Timeline> MOON = key("moon");
/*  23 */   public static final ResourceKey<Timeline> VILLAGER_SCHEDULE = key("villager_schedule");
/*  24 */   public static final ResourceKey<Timeline> EARLY_GAME = key("early_game");
/*     */   
/*     */   public static final float DAY_SKY_LIGHT_LEVEL = 15.0F;
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_LEVEL = 4.0F;
/*  29 */   public static final int NIGHT_SKY_LIGHT_COLOR = ARGB.colorFromFloat(1.0F, 0.48F, 0.48F, 1.0F);
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_FACTOR = 0.24F;
/*     */   public static final int NIGHT_SKY_COLOR_MULTIPLIER = -16777216;
/*  33 */   public static final int NIGHT_FOG_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.06F, 0.06F, 0.09F);
/*  34 */   public static final int NIGHT_CLOUD_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.1F, 0.1F, 0.15F);
/*     */ 
/*     */   
/*     */   static void bootstrap(BootstrapContext<Timeline> context) {
/*  38 */     EasingType skyAngleEase = EasingType.symmetricCubicBezier(0.362F, 0.241F);
/*     */     
/*  40 */     int nightStart = 12600;
/*  41 */     int nightEnd = 23401;
/*  42 */     int noon = 6000;
/*     */     
/*  44 */     context.register(DAY, Timeline.builder()
/*  45 */         .setPeriodTicks(24000)
/*  46 */         .addTrack(EnvironmentAttributes.SUN_ANGLE, track -> track
/*  47 */           .setEasing(skyAngleEase)
/*  48 */           .addKeyframe(6000, Float.valueOf(360.0F))
/*  49 */           .addKeyframe(6000, Float.valueOf(0.0F)))
/*     */         
/*  51 */         .addTrack(EnvironmentAttributes.MOON_ANGLE, track -> track
/*  52 */           .setEasing(skyAngleEase)
/*  53 */           .addKeyframe(6000, Float.valueOf(540.0F))
/*  54 */           .addKeyframe(6000, Float.valueOf(180.0F)))
/*     */         
/*  56 */         .addTrack(EnvironmentAttributes.STAR_ANGLE, track -> track
/*  57 */           .setEasing(skyAngleEase)
/*  58 */           .addKeyframe(6000, Float.valueOf(360.0F))
/*  59 */           .addKeyframe(6000, Float.valueOf(0.0F)))
/*     */         
/*  61 */         .addModifierTrack(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, BooleanModifier.OR, track -> track
/*  62 */           .addKeyframe(12600, Boolean.valueOf(true))
/*  63 */           .addKeyframe(23401, Boolean.valueOf(false)))
/*     */         
/*  65 */         .addModifierTrack(EnvironmentAttributes.FOG_COLOR, ColorModifier.MULTIPLY_RGB, track -> track
/*  66 */           .addKeyframe(133, Integer.valueOf(-1))
/*  67 */           .addKeyframe(11867, Integer.valueOf(-1))
/*  68 */           .addKeyframe(13670, Integer.valueOf(NIGHT_FOG_COLOR_MULTIPLIER))
/*  69 */           .addKeyframe(22330, Integer.valueOf(NIGHT_FOG_COLOR_MULTIPLIER)))
/*     */         
/*  71 */         .addModifierTrack(EnvironmentAttributes.SKY_COLOR, ColorModifier.MULTIPLY_RGB, track -> track
/*  72 */           .addKeyframe(133, Integer.valueOf(-1))
/*  73 */           .addKeyframe(11867, Integer.valueOf(-1))
/*  74 */           .addKeyframe(13670, Integer.valueOf(-16777216))
/*  75 */           .addKeyframe(22330, Integer.valueOf(-16777216)))
/*     */         
/*  77 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_COLOR, ColorModifier.MULTIPLY_RGB, track -> track
/*  78 */           .addKeyframe(730, Integer.valueOf(-1))
/*  79 */           .addKeyframe(11270, Integer.valueOf(-1))
/*  80 */           .addKeyframe(13140, Integer.valueOf(NIGHT_SKY_LIGHT_COLOR))
/*  81 */           .addKeyframe(22860, Integer.valueOf(NIGHT_SKY_LIGHT_COLOR)))
/*     */         
/*  83 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_FACTOR, FloatModifier.MULTIPLY, track -> track
/*  84 */           .addKeyframe(730, Float.valueOf(1.0F))
/*  85 */           .addKeyframe(11270, Float.valueOf(1.0F))
/*  86 */           .addKeyframe(13140, Float.valueOf(0.24F))
/*  87 */           .addKeyframe(22860, Float.valueOf(0.24F)))
/*     */         
/*  89 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_LEVEL, FloatModifier.MULTIPLY, track -> track
/*  90 */           .addKeyframe(133, Float.valueOf(1.0F))
/*  91 */           .addKeyframe(11867, Float.valueOf(1.0F))
/*  92 */           .addKeyframe(13670, Float.valueOf(0.26666668F))
/*  93 */           .addKeyframe(22330, Float.valueOf(0.26666668F)))
/*     */         
/*  95 */         .addTrack(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, track -> track
/*  96 */           .addKeyframe(71, Integer.valueOf(1609540403))
/*  97 */           .addKeyframe(310, Integer.valueOf(703969843))
/*  98 */           .addKeyframe(565, Integer.valueOf(117167155))
/*  99 */           .addKeyframe(730, Integer.valueOf(16770355))
/* 100 */           .addKeyframe(11270, Integer.valueOf(16770355))
/* 101 */           .addKeyframe(11397, Integer.valueOf(83679283))
/* 102 */           .addKeyframe(11522, Integer.valueOf(268028723))
/* 103 */           .addKeyframe(11690, Integer.valueOf(703969843))
/* 104 */           .addKeyframe(11929, Integer.valueOf(1609540403))
/* 105 */           .addKeyframe(12243, Integer.valueOf(-1310226637))
/* 106 */           .addKeyframe(12358, Integer.valueOf(-857440717))
/* 107 */           .addKeyframe(12512, Integer.valueOf(-371166669))
/* 108 */           .addKeyframe(12613, Integer.valueOf(-153261261))
/* 109 */           .addKeyframe(12732, Integer.valueOf(-19242189))
/* 110 */           .addKeyframe(12841, Integer.valueOf(-19440589))
/* 111 */           .addKeyframe(13035, Integer.valueOf(-321760973))
/* 112 */           .addKeyframe(13252, Integer.valueOf(-1043577037))
/* 113 */           .addKeyframe(13775, Integer.valueOf(918435635))
/* 114 */           .addKeyframe(13888, Integer.valueOf(532362547))
/* 115 */           .addKeyframe(14039, Integer.valueOf(163001139))
/* 116 */           .addKeyframe(14192, Integer.valueOf(11744051))
/* 117 */           .addKeyframe(21807, Integer.valueOf(11678515))
/* 118 */           .addKeyframe(21961, Integer.valueOf(163001139))
/* 119 */           .addKeyframe(22112, Integer.valueOf(532362547))
/* 120 */           .addKeyframe(22225, Integer.valueOf(918435635))
/* 121 */           .addKeyframe(22748, Integer.valueOf(-1043577037))
/* 122 */           .addKeyframe(22965, Integer.valueOf(-321760973))
/* 123 */           .addKeyframe(23159, Integer.valueOf(-19440589))
/* 124 */           .addKeyframe(23272, Integer.valueOf(-19242189))
/* 125 */           .addKeyframe(23488, Integer.valueOf(-371166669))
/* 126 */           .addKeyframe(23642, Integer.valueOf(-857440717))
/* 127 */           .addKeyframe(23757, Integer.valueOf(-1310226637)))
/*     */         
/* 129 */         .addModifierTrack(EnvironmentAttributes.STAR_BRIGHTNESS, FloatModifier.MAXIMUM, track -> track
/* 130 */           .addKeyframe(92, Float.valueOf(0.037F))
/* 131 */           .addKeyframe(627, Float.valueOf(0.0F))
/* 132 */           .addKeyframe(11373, Float.valueOf(0.0F))
/* 133 */           .addKeyframe(11732, Float.valueOf(0.016F))
/* 134 */           .addKeyframe(11959, Float.valueOf(0.044F))
/* 135 */           .addKeyframe(12399, Float.valueOf(0.143F))
/* 136 */           .addKeyframe(12729, Float.valueOf(0.258F))
/* 137 */           .addKeyframe(13228, Float.valueOf(0.5F))
/* 138 */           .addKeyframe(22772, Float.valueOf(0.5F))
/* 139 */           .addKeyframe(23032, Float.valueOf(0.364F))
/* 140 */           .addKeyframe(23356, Float.valueOf(0.225F))
/* 141 */           .addKeyframe(23758, Float.valueOf(0.101F)))
/*     */         
/* 143 */         .addModifierTrack(EnvironmentAttributes.CLOUD_COLOR, ColorModifier.MULTIPLY_ARGB, track -> track
/* 144 */           .addKeyframe(133, Integer.valueOf(-1))
/* 145 */           .addKeyframe(11867, Integer.valueOf(-1))
/* 146 */           .addKeyframe(13670, Integer.valueOf(NIGHT_CLOUD_COLOR_MULTIPLIER))
/* 147 */           .addKeyframe(22330, Integer.valueOf(NIGHT_CLOUD_COLOR_MULTIPLIER)))
/*     */         
/* 149 */         .addTrack(EnvironmentAttributes.EYEBLOSSOM_OPEN, track -> track
/* 150 */           .addKeyframe(12600, TriState.TRUE)
/* 151 */           .addKeyframe(23401, TriState.FALSE))
/*     */         
/* 153 */         .addModifierTrack(EnvironmentAttributes.CREAKING_ACTIVE, BooleanModifier.OR, track -> track
/* 154 */           .addKeyframe(12600, Boolean.valueOf(true))
/* 155 */           .addKeyframe(23401, Boolean.valueOf(false)))
/*     */         
/* 157 */         .addModifierTrack(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, FloatModifier.MAXIMUM, track -> track
/* 158 */           .setEasing(EasingType.CONSTANT)
/* 159 */           .addKeyframe(21062, Float.valueOf(1.0F))
/* 160 */           .addKeyframe(21905, Float.valueOf(0.002F)))
/*     */         
/* 162 */         .addModifierTrack(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, FloatModifier.MAXIMUM, track -> track
/* 163 */           .setEasing(EasingType.CONSTANT)
/* 164 */           .addKeyframe(362, Float.valueOf(0.0F))
/* 165 */           .addKeyframe(23667, Float.valueOf(0.7F)))
/*     */         
/* 167 */         .addModifierTrack(EnvironmentAttributes.BEES_STAY_IN_HIVE, BooleanModifier.OR, track -> track
/* 168 */           .addKeyframe(12542, Boolean.valueOf(true))
/* 169 */           .addKeyframe(23460, Boolean.valueOf(false)))
/*     */         
/* 171 */         .addModifierTrack(EnvironmentAttributes.MONSTERS_BURN, BooleanModifier.OR, track -> track
/* 172 */           .addKeyframe(12542, Boolean.valueOf(false))
/* 173 */           .addKeyframe(23460, Boolean.valueOf(true)))
/*     */         
/* 175 */         .build());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Timeline.Builder moonPhases = Timeline.builder().setPeriodTicks(24000 * MoonPhase.COUNT).addTrack(EnvironmentAttributes.MOON_PHASE, track -> { for (MoonPhase phase : MoonPhase.values()) track.addKeyframe(phase.startTick(), phase);  }).addModifierTrack(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, FloatModifier.MAXIMUM, track -> {
/* 186 */           track.setEasing(EasingType.CONSTANT);
/* 187 */           for (MoonPhase phase : MoonPhase.values()) {
/* 188 */             track.addKeyframe(phase.startTick(), Float.valueOf(DimensionType.MOON_BRIGHTNESS_PER_PHASE[phase.index()] * 0.5F));
/*     */           }
/*     */         });
/*     */     
/* 192 */     context.register(MOON, moonPhases.build());
/*     */     
/* 194 */     int workStartTime = 2000;
/* 195 */     int totalWorkTime = 7000;
/*     */     
/* 197 */     context.register(VILLAGER_SCHEDULE, Timeline.builder()
/* 198 */         .setPeriodTicks(24000)
/* 199 */         .addTrack(EnvironmentAttributes.VILLAGER_ACTIVITY, track -> track
/* 200 */           .addKeyframe(10, Activity.IDLE)
/* 201 */           .addKeyframe(2000, Activity.WORK)
/* 202 */           .addKeyframe(9000, Activity.MEET)
/* 203 */           .addKeyframe(11000, Activity.IDLE)
/* 204 */           .addKeyframe(12000, Activity.REST))
/*     */         
/* 206 */         .addTrack(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY, track -> track
/* 207 */           .addKeyframe(10, Activity.IDLE)
/* 208 */           .addKeyframe(3000, Activity.PLAY)
/* 209 */           .addKeyframe(6000, Activity.IDLE)
/* 210 */           .addKeyframe(10000, Activity.PLAY)
/* 211 */           .addKeyframe(12000, Activity.REST))
/*     */         
/* 213 */         .build());
/*     */ 
/*     */     
/* 216 */     context.register(EARLY_GAME, Timeline.builder()
/* 217 */         .addModifierTrack(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, BooleanModifier.AND, track -> track
/* 218 */           .addKeyframe(0, Boolean.valueOf(false))
/* 219 */           .addKeyframe(120000, Boolean.valueOf(true)))
/*     */         
/* 221 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 226 */   private static ResourceKey<Timeline> key(String id) { return ResourceKey.create(Registries.TIMELINE, Identifier.withDefaultNamespace(id)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\timeline\Timelines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
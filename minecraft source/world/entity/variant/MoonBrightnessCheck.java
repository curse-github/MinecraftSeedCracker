/*    */ package net.minecraft.world.entity.variant;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class MoonBrightnessCheck extends Record implements SpawnCondition {
/* 11 */   public MoonBrightnessCheck(MinMaxBounds.Doubles range) { this.range = range; } private final MinMaxBounds.Doubles range; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck; } public MinMaxBounds.Doubles range() { return this.range; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 15 */   public static final MapCodec<MoonBrightnessCheck> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(MinMaxBounds.Doubles.CODEC
/* 16 */         .fieldOf("range").forGetter(MoonBrightnessCheck::range))
/* 17 */       .apply(i, MoonBrightnessCheck::new));
/*    */ 
/*    */   
/*    */   public boolean test(SpawnContext context) {
/* 21 */     MoonPhase moonPhase = (MoonPhase)context.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, Vec3.atCenterOf(context.pos()));
/* 22 */     float moonBrightness = DimensionType.MOON_BRIGHTNESS_PER_PHASE[moonPhase.index()];
/* 23 */     return this.range.matches(moonBrightness);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<MoonBrightnessCheck> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\MoonBrightnessCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
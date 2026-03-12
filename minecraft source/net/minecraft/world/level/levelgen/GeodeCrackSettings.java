/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*    */ 
/*    */ public class GeodeCrackSettings {
/*  8 */   public static final Codec<GeodeCrackSettings> CODEC = RecordCodecBuilder.create(i -> i.group(GeodeConfiguration.CHANCE_RANGE
/*  9 */         .fieldOf("generate_crack_chance").orElse(Double.valueOf(1.0D)).forGetter(()), 
/* 10 */         Codec.doubleRange(0.0D, 5.0D).fieldOf("base_crack_size").orElse(Double.valueOf(2.0D)).forGetter(()), 
/* 11 */         Codec.intRange(0, 10).fieldOf("crack_point_offset").orElse(Integer.valueOf(2)).forGetter(()))
/* 12 */       .apply(i, GeodeCrackSettings::new));
/*    */   
/*    */   public final double generateCrackChance;
/*    */   public final double baseCrackSize;
/*    */   public final int crackPointOffset;
/*    */   
/*    */   public GeodeCrackSettings(double generateCrackChance, double baseCrackSize, int crackPointOffset) {
/* 19 */     this.generateCrackChance = generateCrackChance;
/* 20 */     this.baseCrackSize = baseCrackSize;
/* 21 */     this.crackPointOffset = crackPointOffset;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\GeodeCrackSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
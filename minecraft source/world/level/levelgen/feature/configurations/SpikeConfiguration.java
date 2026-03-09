/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ 
/*    */ public class SpikeConfiguration implements FeatureConfiguration {
/* 13 */   public static final Codec<SpikeConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 14 */         .fieldOf("crystal_invulnerable").orElse(Boolean.valueOf(false)).forGetter(()), SpikeFeature.EndSpike.CODEC
/* 15 */         .listOf().fieldOf("spikes").forGetter(()), BlockPos.CODEC
/* 16 */         .optionalFieldOf("crystal_beam_target").forGetter(()))
/* 17 */       .apply(i, SpikeConfiguration::new));
/*    */   
/*    */   private final boolean crystalInvulnerable;
/*    */   
/*    */   private final List<SpikeFeature.EndSpike> spikes;
/*    */   private final BlockPos crystalBeamTarget;
/*    */   
/* 24 */   public SpikeConfiguration(boolean crystalInvulnerable, List<SpikeFeature.EndSpike> spikes, BlockPos crystalBeamTarget) { this(crystalInvulnerable, spikes, Optional.ofNullable(crystalBeamTarget)); }
/*    */ 
/*    */   
/*    */   private SpikeConfiguration(boolean crystalInvulnerable, List<SpikeFeature.EndSpike> spikes, Optional<BlockPos> crystalBeamTarget) {
/* 28 */     this.crystalInvulnerable = crystalInvulnerable;
/* 29 */     this.spikes = spikes;
/* 30 */     this.crystalBeamTarget = (BlockPos)crystalBeamTarget.orElse(null);
/*    */   }
/*    */ 
/*    */   
/* 34 */   public boolean isCrystalInvulnerable() { return this.crystalInvulnerable; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public List<SpikeFeature.EndSpike> getSpikes() { return this.spikes; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public BlockPos getCrystalBeamTarget() { return this.crystalBeamTarget; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpikeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
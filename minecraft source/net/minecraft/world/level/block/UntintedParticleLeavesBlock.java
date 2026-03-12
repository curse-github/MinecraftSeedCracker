/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class UntintedParticleLeavesBlock extends LeavesBlock {
/* 14 */   public static final MapCodec<UntintedParticleLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), ParticleTypes.CODEC
/* 16 */         .fieldOf("leaf_particle").forGetter(()), 
/* 17 */         propertiesCodec())
/* 18 */       .apply(i, UntintedParticleLeavesBlock::new));
/*    */   
/*    */   protected final ParticleOptions leafParticle;
/*    */   
/*    */   public UntintedParticleLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, BlockBehaviour.Properties properties) {
/* 23 */     super(leafParticleChance, properties);
/* 24 */     this.leafParticle = leafParticle;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) { ParticleUtils.spawnParticleBelow(level, pos, random, this.leafParticle); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public MapCodec<UntintedParticleLeavesBlock> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\UntintedParticleLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
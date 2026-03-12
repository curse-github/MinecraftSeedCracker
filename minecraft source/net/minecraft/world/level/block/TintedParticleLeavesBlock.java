/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ColorParticleOption;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class TintedParticleLeavesBlock extends LeavesBlock {
/* 14 */   public static final MapCodec<TintedParticleLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), 
/* 16 */         propertiesCodec())
/* 17 */       .apply(i, TintedParticleLeavesBlock::new));
/*    */ 
/*    */   
/* 20 */   public TintedParticleLeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) { super(leafParticleChance, properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
/* 25 */     ColorParticleOption particle = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, level.getClientLeafTintColor(pos));
/* 26 */     ParticleUtils.spawnParticleBelow(level, pos, random, particle);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public MapCodec<? extends TintedParticleLeavesBlock> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TintedParticleLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleType;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class TorchBlock extends BaseTorchBlock {
/* 15 */   protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE
/* 16 */     .byNameCodec()
/* 17 */     .comapFlatMap(type -> { SimpleParticleType simple = (SimpleParticleType)type; return (type instanceof SimpleParticleType) ? 
/* 18 */           DataResult.success(simple) : 
/* 19 */           DataResult.error(()); }type -> type)
/* 20 */     .fieldOf("particle_options");
/*    */   
/* 22 */   public static final MapCodec<TorchBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PARTICLE_OPTIONS_FIELD
/* 23 */         .forGetter(()), 
/* 24 */         propertiesCodec())
/* 25 */       .apply(i, TorchBlock::new));
/*    */   
/*    */   protected final SimpleParticleType flameParticle;
/*    */   
/* 29 */   public MapCodec<? extends TorchBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
/* 35 */     super(properties);
/* 36 */     this.flameParticle = flameParticle;
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 41 */     double x = pos.getX() + 0.5D;
/* 42 */     double y = pos.getY() + 0.7D;
/* 43 */     double z = pos.getZ() + 0.5D;
/* 44 */     level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
/* 45 */     level.addParticle(this.flameParticle, x, y, z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
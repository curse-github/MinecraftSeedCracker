/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.CarvingMask;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.Aquifer;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class NetherWorldCarver
/*    */   extends CaveWorldCarver {
/*    */   public NetherWorldCarver(Codec<CaveCarverConfiguration> configurationFactory) {
/* 20 */     super(configurationFactory);
/* 21 */     this.liquids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected int getCaveBound() { return 10; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected float getThickness(RandomSource random) { return (random.nextFloat() * 2.0F + random.nextFloat()) * 2.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected double getYScale() { return 5.0D; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean carveBlock(CarvingContext context, CaveCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, CarvingMask mask, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos helperPos, Aquifer aquifer, MutableBoolean hasGrass) {
/* 44 */     if (canReplaceBlock(configuration, chunk.getBlockState(blockPos))) {
/*    */       BlockState state;
/* 46 */       if (blockPos.getY() <= context.getMinGenY() + 31) {
/* 47 */         state = LAVA.createLegacyBlock();
/*    */       } else {
/* 49 */         state = CAVE_AIR;
/*    */       } 
/* 51 */       chunk.setBlockState(blockPos, state);
/* 52 */       return true;
/*    */     } 
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\NetherWorldCarver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
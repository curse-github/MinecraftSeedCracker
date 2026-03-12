/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HayBlock extends RotatedPillarBlock {
/* 11 */   public static final MapCodec<HayBlock> CODEC = simpleCodec(HayBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 15 */   public MapCodec<HayBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/*    */   public HayBlock(BlockBehaviour.Properties properties) {
/* 19 */     super(properties);
/* 20 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AXIS, Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) { entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HayBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
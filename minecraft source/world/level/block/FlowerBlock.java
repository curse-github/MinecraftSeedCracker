/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FlowerBlock extends VegetationBlock implements SuspiciousEffectHolder {
/* 21 */   protected static final MapCodec<SuspiciousStewEffects> EFFECTS_FIELD = SuspiciousStewEffects.CODEC.fieldOf("suspicious_stew_effects");
/*    */   
/* 23 */   public static final MapCodec<FlowerBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(EFFECTS_FIELD
/* 24 */         .forGetter(FlowerBlock::getSuspiciousEffects), 
/* 25 */         propertiesCodec())
/* 26 */       .apply(i, FlowerBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<? extends FlowerBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 10.0D);
/*    */   
/*    */   private final SuspiciousStewEffects suspiciousStewEffects;
/*    */ 
/*    */   
/* 38 */   public FlowerBlock(Holder<MobEffect> suspiciousStewEffect, float effectSeconds, BlockBehaviour.Properties properties) { this(makeEffectList(suspiciousStewEffect, effectSeconds), properties); }
/*    */ 
/*    */   
/*    */   public FlowerBlock(SuspiciousStewEffects suspiciousStewEffects, BlockBehaviour.Properties properties) {
/* 42 */     super(properties);
/* 43 */     this.suspiciousStewEffects = suspiciousStewEffects;
/*    */   }
/*    */ 
/*    */   
/* 47 */   protected static SuspiciousStewEffects makeEffectList(Holder<MobEffect> suspiciousStewEffect, float effectSeconds) { return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(suspiciousStewEffect, 
/* 48 */             Mth.floor(effectSeconds * 20.0F)))); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE.move(state.getOffset(pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public SuspiciousStewEffects getSuspiciousEffects() { return this.suspiciousStewEffects; }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public MobEffectInstance getBeeInteractionEffect() { return null; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
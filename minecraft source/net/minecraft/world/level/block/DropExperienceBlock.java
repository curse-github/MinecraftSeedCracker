/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DropExperienceBlock extends Block {
/* 12 */   public static final MapCodec<DropExperienceBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 13 */         IntProvider.codec(0, 10).fieldOf("experience").forGetter(()), 
/* 14 */         propertiesCodec())
/* 15 */       .apply(i, DropExperienceBlock::new));
/*    */   
/*    */   private final IntProvider xpRange;
/*    */   
/* 19 */   public MapCodec<? extends DropExperienceBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DropExperienceBlock(IntProvider xpRange, BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/* 26 */     this.xpRange = xpRange;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 31 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 32 */     if (dropExperience)
/* 33 */       tryDropExperience(level, pos, tool, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DropExperienceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
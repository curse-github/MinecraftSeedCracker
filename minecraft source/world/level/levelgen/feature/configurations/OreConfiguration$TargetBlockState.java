/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TargetBlockState
/*    */ {
/* 45 */   public static final Codec<TargetBlockState> CODEC = RecordCodecBuilder.create(i -> i.group(RuleTest.CODEC
/* 46 */         .fieldOf("target").forGetter(()), BlockState.CODEC
/* 47 */         .fieldOf("state").forGetter(()))
/* 48 */       .apply(i, TargetBlockState::new));
/*    */   
/*    */   public final RuleTest target;
/*    */   public final BlockState state;
/*    */   
/*    */   private TargetBlockState(RuleTest target, BlockState state) {
/* 54 */     this.target = target;
/* 55 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\OreConfiguration$TargetBlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
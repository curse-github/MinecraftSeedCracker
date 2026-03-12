/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*    */ 
/*    */ 
/*    */ public class ProcessorRule
/*    */ {
/* 17 */   public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;
/*    */   
/* 19 */   public static final Codec<ProcessorRule> CODEC = RecordCodecBuilder.create(i -> i.group(RuleTest.CODEC
/* 20 */         .fieldOf("input_predicate").forGetter(()), RuleTest.CODEC
/* 21 */         .fieldOf("location_predicate").forGetter(()), PosRuleTest.CODEC
/* 22 */         .lenientOptionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(()), BlockState.CODEC
/* 23 */         .fieldOf("output_state").forGetter(()), RuleBlockEntityModifier.CODEC
/* 24 */         .lenientOptionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(()))
/* 25 */       .apply(i, ProcessorRule::new));
/*    */ 
/*    */   
/*    */   private final RuleTest inputPredicate;
/*    */   
/*    */   private final RuleTest locPredicate;
/*    */   
/*    */   private final PosRuleTest posPredicate;
/*    */   
/*    */   private final BlockState outputState;
/*    */   
/*    */   private final RuleBlockEntityModifier blockEntityModifier;
/*    */ 
/*    */   
/* 39 */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, BlockState outputState) { this(inputPredicate, locPredicate, PosAlwaysTrueTest.INSTANCE, outputState); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState) { this(inputPredicate, locPredicate, posPredicate, outputState, DEFAULT_BLOCK_ENTITY_MODIFIER); }
/*    */ 
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState, RuleBlockEntityModifier blockEntityModifier) {
/* 47 */     this.inputPredicate = inputPredicate;
/* 48 */     this.locPredicate = locPredicate;
/* 49 */     this.posPredicate = posPredicate;
/* 50 */     this.outputState = outputState;
/* 51 */     this.blockEntityModifier = blockEntityModifier;
/*    */   }
/*    */ 
/*    */   
/* 55 */   public boolean test(BlockState inputState, BlockState locState, BlockPos inTemplatePos, BlockPos worldPos, BlockPos reference, RandomSource random) { return (this.inputPredicate.test(inputState, random) && this.locPredicate.test(locState, random) && this.posPredicate.test(inTemplatePos, worldPos, reference, random)); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public BlockState getOutputState() { return this.outputState; }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public CompoundTag getOutputTag(RandomSource random, CompoundTag existingTag) { return this.blockEntityModifier.apply(random, existingTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\ProcessorRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
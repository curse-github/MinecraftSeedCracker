/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface RuleBlockEntityModifier
/*    */ {
/* 10 */   public static final Codec<RuleBlockEntityModifier> CODEC = BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER.byNameCodec().dispatch(RuleBlockEntityModifier::getType, RuleBlockEntityModifierType::codec);
/*    */   
/*    */   CompoundTag apply(RandomSource paramRandomSource, CompoundTag paramCompoundTag);
/*    */   
/*    */   RuleBlockEntityModifierType<?> getType();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\RuleBlockEntityModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
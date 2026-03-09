/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Clear
/*    */   implements RuleBlockEntityModifier {
/*  9 */   private static final Clear INSTANCE = new Clear();
/* 10 */   public static final MapCodec<Clear> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */   
/* 14 */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) { return new CompoundTag(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RuleBlockEntityModifierType<?> getType() { return RuleBlockEntityModifierType.CLEAR; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\Clear.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
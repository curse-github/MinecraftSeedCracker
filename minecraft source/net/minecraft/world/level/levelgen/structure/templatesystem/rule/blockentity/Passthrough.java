/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Passthrough
/*    */   implements RuleBlockEntityModifier {
/*  9 */   public static final Passthrough INSTANCE = new Passthrough();
/* 10 */   public static final MapCodec<Passthrough> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */   
/* 14 */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) { return existingTag; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RuleBlockEntityModifierType<?> getType() { return RuleBlockEntityModifierType.PASSTHROUGH; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\Passthrough.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
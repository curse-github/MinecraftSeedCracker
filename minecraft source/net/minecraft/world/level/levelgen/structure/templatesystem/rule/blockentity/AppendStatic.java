/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class AppendStatic implements RuleBlockEntityModifier {
/* 10 */   public static final MapCodec<AppendStatic> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CompoundTag.CODEC
/* 11 */         .fieldOf("data").forGetter(()))
/* 12 */       .apply(i, AppendStatic::new));
/*    */   
/*    */   private final CompoundTag tag;
/*    */   
/* 16 */   public AppendStatic(CompoundTag tag) { this.tag = tag; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) { return (existingTag == null) ? this.tag.copy() : existingTag.merge(this.tag); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public RuleBlockEntityModifierType<?> getType() { return RuleBlockEntityModifierType.APPEND_STATIC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\AppendStatic.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
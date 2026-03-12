/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatLevelGeneratorPresetTags
/*    */ {
/* 12 */   public static final TagKey<FlatLevelGeneratorPreset> VISIBLE = create("visible");
/*    */ 
/*    */   
/* 15 */   private static TagKey<FlatLevelGeneratorPreset> create(String name) { return TagKey.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\FlatLevelGeneratorPresetTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
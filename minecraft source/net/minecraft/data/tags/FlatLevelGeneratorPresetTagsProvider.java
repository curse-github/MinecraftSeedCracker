/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.tags.FlatLevelGeneratorPresetTags;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*    */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
/*    */ 
/*    */ public class FlatLevelGeneratorPresetTagsProvider
/*    */   extends KeyTagProvider<FlatLevelGeneratorPreset>
/*    */ {
/* 14 */   public FlatLevelGeneratorPresetTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) { super(output, Registries.FLAT_LEVEL_GENERATOR_PRESET, lookupProvider); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(FlatLevelGeneratorPresetTags.VISIBLE)
/* 20 */       .add(FlatLevelGeneratorPresets.CLASSIC_FLAT)
/* 21 */       .add(FlatLevelGeneratorPresets.TUNNELERS_DREAM)
/* 22 */       .add(FlatLevelGeneratorPresets.WATER_WORLD)
/* 23 */       .add(FlatLevelGeneratorPresets.OVERWORLD)
/* 24 */       .add(FlatLevelGeneratorPresets.SNOWY_KINGDOM)
/* 25 */       .add(FlatLevelGeneratorPresets.BOTTOMLESS_PIT)
/* 26 */       .add(FlatLevelGeneratorPresets.DESERT)
/* 27 */       .add(FlatLevelGeneratorPresets.REDSTONE_READY)
/* 28 */       .add(FlatLevelGeneratorPresets.THE_VOID);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\FlatLevelGeneratorPresetTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
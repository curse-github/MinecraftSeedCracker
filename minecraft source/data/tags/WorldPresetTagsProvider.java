/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.tags.WorldPresetTags;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*    */ 
/*    */ public class WorldPresetTagsProvider
/*    */   extends KeyTagProvider<WorldPreset>
/*    */ {
/* 14 */   public WorldPresetTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) { super(output, Registries.WORLD_PRESET, lookupProvider); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(WorldPresetTags.NORMAL)
/* 20 */       .add(WorldPresets.NORMAL)
/* 21 */       .add(WorldPresets.FLAT)
/* 22 */       .add(WorldPresets.LARGE_BIOMES)
/* 23 */       .add(WorldPresets.AMPLIFIED)
/* 24 */       .add(WorldPresets.SINGLE_BIOME_SURFACE);
/*    */ 
/*    */     
/* 27 */     tag(WorldPresetTags.EXTENDED)
/* 28 */       .addTag(WorldPresetTags.NORMAL)
/* 29 */       .add(WorldPresets.DEBUG);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\WorldPresetTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
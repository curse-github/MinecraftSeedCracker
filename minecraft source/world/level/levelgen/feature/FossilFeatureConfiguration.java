/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*    */ 
/*    */ public class FossilFeatureConfiguration implements FeatureConfiguration {
/* 14 */   public static final Codec<FossilFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(Identifier.CODEC
/* 15 */         .listOf().fieldOf("fossil_structures").forGetter(()), Identifier.CODEC
/* 16 */         .listOf().fieldOf("overlay_structures").forGetter(()), StructureProcessorType.LIST_CODEC
/* 17 */         .fieldOf("fossil_processors").forGetter(()), StructureProcessorType.LIST_CODEC
/* 18 */         .fieldOf("overlay_processors").forGetter(()), 
/* 19 */         Codec.intRange(0, 7).fieldOf("max_empty_corners_allowed").forGetter(()))
/* 20 */       .apply(i, FossilFeatureConfiguration::new));
/*    */   
/*    */   public final List<Identifier> fossilStructures;
/*    */   public final List<Identifier> overlayStructures;
/*    */   public final Holder<StructureProcessorList> fossilProcessors;
/*    */   public final Holder<StructureProcessorList> overlayProcessors;
/*    */   public final int maxEmptyCornersAllowed;
/*    */   
/*    */   public FossilFeatureConfiguration(List<Identifier> fossilStructures, List<Identifier> overlayStructures, Holder<StructureProcessorList> fossilProcessors, Holder<StructureProcessorList> overlayProcessors, int maxEmptyCornersAllowed) {
/* 29 */     if (fossilStructures.isEmpty()) {
/* 30 */       throw new IllegalArgumentException("Fossil structure lists need at least one entry");
/*    */     }
/* 32 */     if (fossilStructures.size() != overlayStructures.size()) {
/* 33 */       throw new IllegalArgumentException("Fossil structure lists must be equal lengths");
/*    */     }
/* 35 */     this.fossilStructures = fossilStructures;
/* 36 */     this.overlayStructures = overlayStructures;
/* 37 */     this.fossilProcessors = fossilProcessors;
/* 38 */     this.overlayProcessors = overlayProcessors;
/* 39 */     this.maxEmptyCornersAllowed = maxEmptyCornersAllowed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FossilFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
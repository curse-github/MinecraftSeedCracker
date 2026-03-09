/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ 
/*    */ public class LegacySinglePoolElement
/*    */   extends SinglePoolElement
/*    */ {
/* 23 */   public static final MapCodec<LegacySinglePoolElement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 24 */         templateCodec(), 
/* 25 */         processorsCodec(), 
/* 26 */         projectionCodec(), 
/* 27 */         overrideLiquidSettingsCodec())
/* 28 */       .apply(i, LegacySinglePoolElement::new));
/*    */ 
/*    */   
/* 31 */   protected LegacySinglePoolElement(Either<Identifier, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<LiquidSettings> liquidSettings) { super(template, processors, projection, liquidSettings); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox chunkBB, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 36 */     StructurePlaceSettings settings = super.getSettings(rotation, chunkBB, liquidSettings, keepJigsaws);
/* 37 */     settings.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 38 */     settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 39 */     return settings;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public StructurePoolElementType<?> getType() { return StructurePoolElementType.LEGACY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String toString() { return "LegacySingle[" + String.valueOf(this.template) + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\LegacySinglePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ public class IglooPieces
/*     */ {
/*     */   public static final int GENERATION_HEIGHT = 90;
/*  38 */   private static final Identifier STRUCTURE_LOCATION_IGLOO = Identifier.withDefaultNamespace("igloo/top");
/*  39 */   private static final Identifier STRUCTURE_LOCATION_LADDER = Identifier.withDefaultNamespace("igloo/middle");
/*  40 */   private static final Identifier STRUCTURE_LOCATION_LABORATORY = Identifier.withDefaultNamespace("igloo/bottom");
/*     */   
/*  42 */   private static final Map<Identifier, BlockPos> PIVOTS = ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, new BlockPos(3, 5, 5), STRUCTURE_LOCATION_LADDER, new BlockPos(1, 3, 1), STRUCTURE_LOCATION_LABORATORY, new BlockPos(3, 6, 7));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final Map<Identifier, BlockPos> OFFSETS = ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, BlockPos.ZERO, STRUCTURE_LOCATION_LADDER, new BlockPos(2, -3, 4), STRUCTURE_LOCATION_LABORATORY, new BlockPos(0, -3, -2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPieces(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  55 */     if (random.nextDouble() < 0.5D) {
/*  56 */       int depth = random.nextInt(8) + 4;
/*  57 */       structurePieceAccessor.addPiece(new IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_LABORATORY, position, rotation, depth * 3));
/*  58 */       for (int i = 0; i < depth - 1; i++) {
/*  59 */         structurePieceAccessor.addPiece(new IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_LADDER, position, rotation, i * 3));
/*     */       }
/*     */     } 
/*     */     
/*  63 */     structurePieceAccessor.addPiece(new IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_IGLOO, position, rotation, 0));
/*     */   }
/*     */   
/*     */   public static class IglooPiece
/*     */     extends TemplateStructurePiece {
/*  68 */     public IglooPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation, int depth) { super(StructurePieceType.IGLOO, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation, templateLocation), makePosition(templateLocation, position, depth)); }
/*     */ 
/*     */ 
/*     */     
/*  72 */     public IglooPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) { super(StructurePieceType.IGLOO, tag, structureTemplateManager, location -> makeSettings((Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow(), location)); }
/*     */ 
/*     */ 
/*     */     
/*  76 */     private static StructurePlaceSettings makeSettings(Rotation rotation, Identifier templateLocation) { return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot((BlockPos)IglooPieces.PIVOTS.get(templateLocation)).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING); }
/*     */ 
/*     */ 
/*     */     
/*  80 */     private static BlockPos makePosition(Identifier templateLocation, BlockPos position, int depth) { return position.offset((Vec3i)IglooPieces.OFFSETS.get(templateLocation)).below(depth); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  85 */       super.addAdditionalSaveData(context, tag);
/*  86 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/*  91 */       if (!"chest".equals(markerId)) {
/*     */         return;
/*     */       }
/*     */       
/*  95 */       level.setBlock(position, Blocks.AIR.defaultBlockState(), 3);
/*  96 */       BlockEntity chest = level.getBlockEntity(position.below());
/*  97 */       if (chest instanceof ChestBlockEntity) {
/*  98 */         ((ChestBlockEntity)chest).setLootTable(BuiltInLootTables.IGLOO_CHEST, random.nextLong());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 104 */       Identifier templateLocation = Identifier.parse(this.templateName);
/*     */       
/* 106 */       StructurePlaceSettings settings = makeSettings(this.placeSettings.getRotation(), templateLocation);
/*     */       
/* 108 */       BlockPos offset = (BlockPos)IglooPieces.OFFSETS.get(templateLocation);
/* 109 */       BlockPos entrancePos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3 - offset.getX(), 0, -offset.getZ())));
/* 110 */       int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, entrancePos.getX(), entrancePos.getZ());
/* 111 */       BlockPos oldTemplatePos = this.templatePosition;
/* 112 */       this.templatePosition = this.templatePosition.offset(0, height - 90 - 1, 0);
/*     */       
/* 114 */       super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */       
/* 116 */       if (templateLocation.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
/* 117 */         BlockPos trapDoorPos = this.templatePosition.offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(3, 0, 5)));
/* 118 */         BlockState belowState = level.getBlockState(trapDoorPos.below());
/* 119 */         if (!belowState.isAir() && !belowState.is(Blocks.LADDER)) {
/* 120 */           level.setBlock(trapDoorPos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
/*     */         }
/*     */       } 
/*     */       
/* 124 */       this.templatePosition = oldTemplatePos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\IglooPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
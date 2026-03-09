/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
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
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class OceanRuinPieces
/*     */ {
/*  57 */   private static final StructureProcessor WARM_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.SAND, Blocks.SUSPICIOUS_SAND, BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
/*  58 */   private static final StructureProcessor COLD_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL, BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY);
/*     */ 
/*     */   
/*  61 */   private static final Identifier[] WARM_RUINS = { Identifier.withDefaultNamespace("underwater_ruin/warm_1"), 
/*  62 */       Identifier.withDefaultNamespace("underwater_ruin/warm_2"), 
/*  63 */       Identifier.withDefaultNamespace("underwater_ruin/warm_3"), 
/*  64 */       Identifier.withDefaultNamespace("underwater_ruin/warm_4"), 
/*  65 */       Identifier.withDefaultNamespace("underwater_ruin/warm_5"), 
/*  66 */       Identifier.withDefaultNamespace("underwater_ruin/warm_6"), 
/*  67 */       Identifier.withDefaultNamespace("underwater_ruin/warm_7"), 
/*  68 */       Identifier.withDefaultNamespace("underwater_ruin/warm_8") };
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static final Identifier[] RUINS_BRICK = { Identifier.withDefaultNamespace("underwater_ruin/brick_1"), 
/*  73 */       Identifier.withDefaultNamespace("underwater_ruin/brick_2"), 
/*  74 */       Identifier.withDefaultNamespace("underwater_ruin/brick_3"), 
/*  75 */       Identifier.withDefaultNamespace("underwater_ruin/brick_4"), 
/*  76 */       Identifier.withDefaultNamespace("underwater_ruin/brick_5"), 
/*  77 */       Identifier.withDefaultNamespace("underwater_ruin/brick_6"), 
/*  78 */       Identifier.withDefaultNamespace("underwater_ruin/brick_7"), 
/*  79 */       Identifier.withDefaultNamespace("underwater_ruin/brick_8") };
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final Identifier[] RUINS_CRACKED = { Identifier.withDefaultNamespace("underwater_ruin/cracked_1"), 
/*  84 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_2"), 
/*  85 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_3"), 
/*  86 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_4"), 
/*  87 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_5"), 
/*  88 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_6"), 
/*  89 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_7"), 
/*  90 */       Identifier.withDefaultNamespace("underwater_ruin/cracked_8") };
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final Identifier[] RUINS_MOSSY = { Identifier.withDefaultNamespace("underwater_ruin/mossy_1"), 
/*  95 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_2"), 
/*  96 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_3"), 
/*  97 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_4"), 
/*  98 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_5"), 
/*  99 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_6"), 
/* 100 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_7"), 
/* 101 */       Identifier.withDefaultNamespace("underwater_ruin/mossy_8") };
/*     */ 
/*     */ 
/*     */   
/* 105 */   private static final Identifier[] BIG_RUINS_BRICK = { Identifier.withDefaultNamespace("underwater_ruin/big_brick_1"), 
/* 106 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_2"), 
/* 107 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_3"), 
/* 108 */       Identifier.withDefaultNamespace("underwater_ruin/big_brick_8") };
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static final Identifier[] BIG_RUINS_MOSSY = { Identifier.withDefaultNamespace("underwater_ruin/big_mossy_1"), 
/* 113 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_2"), 
/* 114 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_3"), 
/* 115 */       Identifier.withDefaultNamespace("underwater_ruin/big_mossy_8") };
/*     */ 
/*     */ 
/*     */   
/* 119 */   private static final Identifier[] BIG_RUINS_CRACKED = { Identifier.withDefaultNamespace("underwater_ruin/big_cracked_1"), 
/* 120 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_2"), 
/* 121 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_3"), 
/* 122 */       Identifier.withDefaultNamespace("underwater_ruin/big_cracked_8") };
/*     */ 
/*     */ 
/*     */   
/* 126 */   private static final Identifier[] BIG_WARM_RUINS = { Identifier.withDefaultNamespace("underwater_ruin/big_warm_4"), 
/* 127 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_5"), 
/* 128 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_6"), 
/* 129 */       Identifier.withDefaultNamespace("underwater_ruin/big_warm_7") };
/*     */ 
/*     */   
/*     */   private static StructureProcessor archyRuleProcessor(Block candidateBlock, Block replacementBlock, ResourceKey<LootTable> lootTable) {
/* 133 */     return new CappedProcessor(new RuleProcessor(
/*     */           
/* 135 */           List.of(new ProcessorRule(new BlockMatchTest(candidateBlock), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, replacementBlock
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 140 */               .defaultBlockState(), new AppendLoot(lootTable)))), 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 145 */         ConstantInt.of(5));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 150 */   private static Identifier getSmallWarmRuin(RandomSource random) { return (Identifier)Util.getRandom(WARM_RUINS, random); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static Identifier getBigWarmRuin(RandomSource random) { return (Identifier)Util.getRandom(BIG_WARM_RUINS, random); }
/*     */ 
/*     */   
/*     */   public static void addPieces(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, StructurePieceAccessor structurePieceAccessor, RandomSource random, OceanRuinStructure structure) {
/* 158 */     boolean isLarge = (random.nextFloat() <= structure.largeProbability);
/* 159 */     float baseIntegrity = isLarge ? 0.9F : 0.8F;
/*     */     
/* 161 */     addPiece(structureTemplateManager, position, rotation, structurePieceAccessor, random, structure, isLarge, baseIntegrity);
/*     */     
/* 163 */     if (isLarge && random.nextFloat() <= structure.clusterProbability) {
/* 164 */       addClusterRuins(structureTemplateManager, random, rotation, position, structure, structurePieceAccessor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addClusterRuins(StructureTemplateManager structureTemplateManager, RandomSource random, Rotation rotation, BlockPos p, OceanRuinStructure structure, StructurePieceAccessor structurePieceAccessor) {
/* 170 */     BlockPos parentPos = new BlockPos(p.getX(), 90, p.getZ());
/* 171 */     BlockPos parentCorner = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, rotation, BlockPos.ZERO).offset(parentPos);
/* 172 */     BoundingBox parentBB = BoundingBox.fromCorners(parentPos, parentCorner);
/* 173 */     BlockPos parentBottomLeft = new BlockPos(Math.min(parentPos.getX(), parentCorner.getX()), parentPos.getY(), Math.min(parentPos.getZ(), parentCorner.getZ()));
/* 174 */     List<BlockPos> allPositions = allPositions(random, parentBottomLeft);
/* 175 */     int ruins = Mth.nextInt(random, 4, 8);
/*     */     
/* 177 */     for (int i = 0; i < ruins; i++) {
/* 178 */       if (!allPositions.isEmpty()) {
/* 179 */         int idx = random.nextInt(allPositions.size());
/* 180 */         BlockPos pos = (BlockPos)allPositions.remove(idx);
/* 181 */         Rotation nextRotation = Rotation.getRandom(random);
/* 182 */         BlockPos nextCorner = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, nextRotation, BlockPos.ZERO).offset(pos);
/* 183 */         BoundingBox nextBB = BoundingBox.fromCorners(pos, nextCorner);
/* 184 */         if (!nextBB.intersects(parentBB))
/*     */         {
/*     */ 
/*     */           
/* 188 */           addPiece(structureTemplateManager, pos, nextRotation, structurePieceAccessor, random, structure, false, 0.8F); } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<BlockPos> allPositions(RandomSource random, BlockPos origin) {
/* 194 */     List<BlockPos> positions = Lists.newArrayList();
/* 195 */     positions.add(origin.offset(-16 + Mth.nextInt(random, 1, 8), 0, 16 + Mth.nextInt(random, 1, 7)));
/* 196 */     positions.add(origin.offset(-16 + Mth.nextInt(random, 1, 8), 0, Mth.nextInt(random, 1, 7)));
/* 197 */     positions.add(origin.offset(-16 + Mth.nextInt(random, 1, 8), 0, -16 + Mth.nextInt(random, 4, 8)));
/* 198 */     positions.add(origin.offset(Mth.nextInt(random, 1, 7), 0, 16 + Mth.nextInt(random, 1, 7)));
/* 199 */     positions.add(origin.offset(Mth.nextInt(random, 1, 7), 0, -16 + Mth.nextInt(random, 4, 6)));
/* 200 */     positions.add(origin.offset(16 + Mth.nextInt(random, 1, 7), 0, 16 + Mth.nextInt(random, 3, 8)));
/* 201 */     positions.add(origin.offset(16 + Mth.nextInt(random, 1, 7), 0, Mth.nextInt(random, 1, 7)));
/* 202 */     positions.add(origin.offset(16 + Mth.nextInt(random, 1, 7), 0, -16 + Mth.nextInt(random, 4, 8)));
/*     */     
/* 204 */     return positions;
/*     */   }
/*     */   private static void addPiece(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, StructurePieceAccessor structurePieceAccessor, RandomSource random, OceanRuinStructure structure, boolean isLarge, float baseIntegrity) {
/*     */     Identifier startPieceLocation;
/* 208 */     switch (structure.biomeTemp) {
/*     */       
/*     */       default:
/* 211 */         startPieceLocation = isLarge ? getBigWarmRuin(random) : getSmallWarmRuin(random);
/* 212 */         structurePieceAccessor.addPiece(new OceanRuinPiece(structureTemplateManager, startPieceLocation, position, rotation, baseIntegrity, structure.biomeTemp, isLarge)); return;
/*     */       case COLD:
/*     */         break;
/* 215 */     }  Identifier[] bricks = isLarge ? BIG_RUINS_BRICK : RUINS_BRICK;
/* 216 */     Identifier[] cracked = isLarge ? BIG_RUINS_CRACKED : RUINS_CRACKED;
/* 217 */     Identifier[] mossy = isLarge ? BIG_RUINS_MOSSY : RUINS_MOSSY;
/*     */     
/* 219 */     int idx = random.nextInt(bricks.length);
/* 220 */     structurePieceAccessor.addPiece(new OceanRuinPiece(structureTemplateManager, bricks[idx], position, rotation, baseIntegrity, structure.biomeTemp, isLarge));
/* 221 */     structurePieceAccessor.addPiece(new OceanRuinPiece(structureTemplateManager, cracked[idx], position, rotation, 0.7F, structure.biomeTemp, isLarge));
/* 222 */     structurePieceAccessor.addPiece(new OceanRuinPiece(structureTemplateManager, mossy[idx], position, rotation, 0.5F, structure.biomeTemp, isLarge));
/*     */   }
/*     */   
/*     */   public static class OceanRuinPiece
/*     */     extends TemplateStructurePiece
/*     */   {
/*     */     private final OceanRuinStructure.Type biomeType;
/*     */     private final float integrity;
/*     */     private final boolean isLarge;
/*     */     
/*     */     public OceanRuinPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation, float integrity, OceanRuinStructure.Type biomeType, boolean isLarge) {
/* 233 */       super(StructurePieceType.OCEAN_RUIN, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation, integrity, biomeType), position);
/*     */       
/* 235 */       this.integrity = integrity;
/* 236 */       this.biomeType = biomeType;
/* 237 */       this.isLarge = isLarge;
/*     */     }
/*     */     
/*     */     private OceanRuinPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag, Rotation rotation, float integrity, OceanRuinStructure.Type biomeType, boolean isLarge) {
/* 241 */       super(StructurePieceType.OCEAN_RUIN, tag, structureTemplateManager, location -> makeSettings(rotation, integrity, biomeType));
/*     */       
/* 243 */       this.integrity = integrity;
/* 244 */       this.biomeType = biomeType;
/* 245 */       this.isLarge = isLarge;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static StructurePlaceSettings makeSettings(Rotation rotation, float integrity, OceanRuinStructure.Type biomeType) {
/* 251 */       StructureProcessor suspiciousBlockProcessor = (biomeType == OceanRuinStructure.Type.COLD) ? OceanRuinPieces.COLD_SUSPICIOUS_BLOCK_PROCESSOR : OceanRuinPieces.WARM_SUSPICIOUS_BLOCK_PROCESSOR;
/*     */       
/* 253 */       return (new StructurePlaceSettings())
/* 254 */         .setRotation(rotation)
/* 255 */         .setMirror(Mirror.NONE)
/* 256 */         .addProcessor(new BlockRotProcessor(integrity))
/* 257 */         .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR)
/* 258 */         .addProcessor(suspiciousBlockProcessor);
/*     */     }
/*     */     
/*     */     public static OceanRuinPiece create(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/* 262 */       Rotation rotation = (Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow();
/* 263 */       float integrity = tag.getFloatOr("Integrity", 0.0F);
/* 264 */       OceanRuinStructure.Type biomeType = (OceanRuinStructure.Type)tag.read("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC).orElseThrow();
/* 265 */       boolean isLarge = tag.getBooleanOr("IsLarge", false);
/* 266 */       return new OceanRuinPiece(structureTemplateManager, tag, rotation, integrity, biomeType, isLarge);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 271 */       super.addAdditionalSaveData(context, tag);
/* 272 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 273 */       tag.putFloat("Integrity", this.integrity);
/* 274 */       tag.store("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC, this.biomeType);
/* 275 */       tag.putBoolean("IsLarge", this.isLarge);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/* 280 */       if ("chest".equals(markerId)) {
/* 281 */         level.setBlock(position, (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.WATERLOGGED, Boolean.valueOf(level.getFluidState(position).is(FluidTags.WATER))), 2);
/*     */         
/* 283 */         BlockEntity chest = level.getBlockEntity(position);
/* 284 */         if (chest instanceof ChestBlockEntity) {
/* 285 */           ((ChestBlockEntity)chest).setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, random.nextLong());
/*     */         }
/*     */       }
/* 288 */       else if ("drowned".equals(markerId)) {
/* 289 */         Drowned drowned = (Drowned)EntityType.DROWNED.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 290 */         if (drowned != null) {
/* 291 */           drowned.setPersistenceRequired();
/* 292 */           drowned.snapTo(position, 0.0F, 0.0F);
/* 293 */           drowned.finalizeSpawn(level, level.getCurrentDifficultyAt(position), EntitySpawnReason.STRUCTURE, null);
/* 294 */           level.addFreshEntityWithPassengers(drowned);
/* 295 */           if (position.getY() > level.getSeaLevel()) {
/* 296 */             level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
/*     */           } else {
/* 298 */             level.setBlock(position, Blocks.WATER.defaultBlockState(), 2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 306 */       int height = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
/* 307 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), height, this.templatePosition.getZ());
/* 308 */       BlockPos corner = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.placeSettings.getRotation(), BlockPos.ZERO).offset(this.templatePosition);
/* 309 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), getHeight(this.templatePosition, level, corner), this.templatePosition.getZ());
/*     */       
/* 311 */       super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */     }
/*     */     
/*     */     private int getHeight(BlockPos pos, BlockGetter level, BlockPos corner) {
/* 315 */       int newY = pos.getY();
/* 316 */       int minY = 512;
/* 317 */       int topY = newY - 1;
/* 318 */       int area = 0;
/* 319 */       for (BlockPos p : BlockPos.betweenClosed(pos, corner)) {
/* 320 */         int x = p.getX();
/* 321 */         int z = p.getZ();
/* 322 */         int floorY = pos.getY() - 1;
/* 323 */         BlockPos.MutableBlockPos tempPos = new BlockPos.MutableBlockPos(x, floorY, z);
/* 324 */         BlockState tempState = level.getBlockState(tempPos);
/* 325 */         FluidState tempFluid = level.getFluidState(tempPos);
/* 326 */         while ((tempState.isAir() || tempFluid.is(FluidTags.WATER) || tempState.is(BlockTags.ICE)) && floorY > level.getMinY() + 1) {
/* 327 */           floorY--;
/* 328 */           tempPos.set(x, floorY, z);
/* 329 */           tempState = level.getBlockState(tempPos);
/* 330 */           tempFluid = level.getFluidState(tempPos);
/*     */         } 
/*     */         
/* 333 */         minY = Math.min(minY, floorY);
/* 334 */         if (floorY < topY - 2) {
/* 335 */           area++;
/*     */         }
/*     */       } 
/*     */       
/* 339 */       int width = Math.abs(pos.getX() - corner.getX());
/* 340 */       if (topY - minY > 2 && area > width - 2) {
/* 341 */         newY = minY + 1;
/*     */       }
/*     */       
/* 344 */       return newY;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class TemplateStructurePiece
/*     */   extends StructurePiece
/*     */ {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final String templateName;
/*     */   
/*     */   protected StructureTemplate template;
/*     */   protected StructurePlaceSettings placeSettings;
/*     */   protected BlockPos templatePosition;
/*     */   
/*     */   public TemplateStructurePiece(StructurePieceType type, int genDepth, StructureTemplateManager structureTemplateManager, Identifier templateLocation, String templateName, StructurePlaceSettings placeSettings, BlockPos position) {
/*  43 */     super(type, genDepth, structureTemplateManager.getOrCreate(templateLocation).getBoundingBox(placeSettings, position));
/*  44 */     setOrientation(Direction.NORTH);
/*     */     
/*  46 */     this.templateName = templateName;
/*  47 */     this.templatePosition = position;
/*  48 */     this.template = structureTemplateManager.getOrCreate(templateLocation);
/*  49 */     this.placeSettings = placeSettings;
/*     */   }
/*     */   
/*     */   public TemplateStructurePiece(StructurePieceType type, CompoundTag tag, StructureTemplateManager structureTemplateManager, Function<Identifier, StructurePlaceSettings> structurePlaceSettingsSupplier) {
/*  53 */     super(type, tag);
/*  54 */     setOrientation(Direction.NORTH);
/*     */     
/*  56 */     this.templateName = tag.getStringOr("Template", "");
/*  57 */     this.templatePosition = new BlockPos(tag.getIntOr("TPX", 0), tag.getIntOr("TPY", 0), tag.getIntOr("TPZ", 0));
/*  58 */     Identifier templateLocation = makeTemplateLocation();
/*  59 */     this.template = structureTemplateManager.getOrCreate(templateLocation);
/*  60 */     this.placeSettings = (StructurePlaceSettings)structurePlaceSettingsSupplier.apply(templateLocation);
/*     */ 
/*     */     
/*  63 */     this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/*     */   }
/*     */ 
/*     */   
/*  67 */   protected Identifier makeTemplateLocation() { return Identifier.parse(this.templateName); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  72 */     tag.putInt("TPX", this.templatePosition.getX());
/*  73 */     tag.putInt("TPY", this.templatePosition.getY());
/*  74 */     tag.putInt("TPZ", this.templatePosition.getZ());
/*  75 */     tag.putString("Template", this.templateName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  80 */     this.placeSettings.setBoundingBox(chunkBB);
/*     */     
/*  82 */     this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/*  83 */     if (this.template.placeInWorld(level, this.templatePosition, referencePos, this.placeSettings, random, 2)) {
/*  84 */       List<StructureTemplate.StructureBlockInfo> dataMarkers = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.STRUCTURE_BLOCK);
/*  85 */       for (StructureTemplate.StructureBlockInfo dataMarker : dataMarkers) {
/*  86 */         if (dataMarker.nbt() == null) {
/*     */           continue;
/*     */         }
/*     */         
/*  90 */         StructureMode mode = (StructureMode)dataMarker.nbt().read("mode", StructureMode.LEGACY_CODEC).orElseThrow();
/*  91 */         if (mode != StructureMode.DATA) {
/*     */           continue;
/*     */         }
/*     */         
/*  95 */         handleDataMarker(dataMarker.nbt().getStringOr("metadata", ""), dataMarker.pos(), level, random, chunkBB);
/*     */       } 
/*     */       
/*  98 */       List<StructureTemplate.StructureBlockInfo> jigsawBlocks = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.JIGSAW);
/*  99 */       for (StructureTemplate.StructureBlockInfo jigsawBlock : jigsawBlocks) {
/* 100 */         if (jigsawBlock.nbt() == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 104 */         String stateString = jigsawBlock.nbt().getStringOr("final_state", "minecraft:air");
/* 105 */         BlockState targetState = Blocks.AIR.defaultBlockState();
/*     */         try {
/* 107 */           targetState = BlockStateParser.parseForBlock(level.holderLookup(Registries.BLOCK), stateString, true).blockState();
/* 108 */         } catch (CommandSyntaxException e) {
/* 109 */           LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", stateString, jigsawBlock.pos());
/*     */         } 
/*     */         
/* 112 */         level.setBlock(jigsawBlock.pos(), targetState, 3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void move(int dx, int dy, int dz) {
/* 125 */     super.move(dx, dy, dz);
/* 126 */     this.templatePosition = this.templatePosition.offset(dx, dy, dz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public Rotation getRotation() { return this.placeSettings.getRotation(); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public StructureTemplate template() { return this.template; }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public BlockPos templatePosition() { return this.templatePosition; }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public StructurePlaceSettings placeSettings() { return this.placeSettings; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\TemplateStructurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
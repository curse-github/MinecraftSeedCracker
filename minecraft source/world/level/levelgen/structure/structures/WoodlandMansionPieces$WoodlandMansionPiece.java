/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WoodlandMansionPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*  40 */   public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation) { this(structureTemplateManager, templateName, position, rotation, Mirror.NONE); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation, Mirror mirror) { super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, structureTemplateManager, makeLocation(templateName), templateName, makeSettings(mirror, rotation), position); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) { super(StructurePieceType.WOODLAND_MANSION_PIECE, tag, structureTemplateManager, location -> makeSettings((Mirror)tag.read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), (Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow())); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected Identifier makeTemplateLocation() { return makeLocation(this.templateName); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static Identifier makeLocation(String templateName) { return Identifier.withDefaultNamespace("woodland_mansion/" + templateName); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation) { return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(rotation).setMirror(mirror).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  66 */     super.addAdditionalSaveData(context, tag);
/*     */     
/*  68 */     tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*  69 */     tag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/*  74 */     if (markerId.startsWith("Chest")) {
/*  75 */       Rotation rot = this.placeSettings.getRotation();
/*  76 */       BlockState chestState = Blocks.CHEST.defaultBlockState();
/*  77 */       if ("ChestWest".equals(markerId)) {
/*  78 */         chestState = (BlockState)chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.WEST));
/*  79 */       } else if ("ChestEast".equals(markerId)) {
/*  80 */         chestState = (BlockState)chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.EAST));
/*  81 */       } else if ("ChestSouth".equals(markerId)) {
/*  82 */         chestState = (BlockState)chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.SOUTH));
/*  83 */       } else if ("ChestNorth".equals(markerId)) {
/*  84 */         chestState = (BlockState)chestState.setValue(ChestBlock.FACING, rot.rotate(Direction.NORTH));
/*     */       } 
/*  86 */       createChest(level, chunkBB, random, position, BuiltInLootTables.WOODLAND_MANSION, chestState);
/*     */     } else {
/*  88 */       int i, numberOfAllays; List<Mob> mobs = new ArrayList<Mob>();
/*  89 */       switch (markerId) {
/*     */         case "Mage":
/*  91 */           mobs.add((Mob)EntityType.EVOKER.create(level.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           break;
/*     */         case "Warrior":
/*  94 */           mobs.add((Mob)EntityType.VINDICATOR.create(level.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           break;
/*     */         case "Group of Allays":
/*  97 */           numberOfAllays = level.getRandom().nextInt(3) + 1;
/*  98 */           for (i = 0; i < numberOfAllays; i++) {
/*  99 */             mobs.add((Mob)EntityType.ALLAY.create(level.getLevel(), EntitySpawnReason.STRUCTURE));
/*     */           }
/*     */           break;
/*     */         
/*     */         default:
/*     */           return;
/*     */       } 
/* 106 */       for (Mob mob : mobs) {
/* 107 */         if (mob == null) {
/*     */           continue;
/*     */         }
/* 110 */         mob.setPersistenceRequired();
/* 111 */         mob.snapTo(position, 0.0F, 0.0F);
/* 112 */         mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/* 113 */         level.addFreshEntityWithPassengers(mob);
/* 114 */         level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$WoodlandMansionPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
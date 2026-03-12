/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class EndCityPieces
/*     */ {
/*     */   private static final int MAX_GEN_DEPTH = 8;
/*     */   
/*     */   private static EndCityPiece addPiece(StructureTemplateManager structureTemplateManager, EndCityPiece parent, BlockPos offset, String templateName, Rotation rotation, boolean overwrite) {
/*  36 */     EndCityPiece child = new EndCityPiece(structureTemplateManager, templateName, parent.templatePosition(), rotation, overwrite);
/*  37 */     BlockPos origin = parent.template().calculateConnectedPosition(parent.placeSettings(), offset, child.placeSettings(), BlockPos.ZERO);
/*  38 */     child.move(origin.getX(), origin.getY(), origin.getZ());
/*     */     
/*  40 */     return child;
/*     */   }
/*     */   
/*     */   public static class EndCityPiece
/*     */     extends TemplateStructurePiece {
/*  45 */     public EndCityPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation, boolean overwrite) { super(StructurePieceType.END_CITY_PIECE, 0, structureTemplateManager, makeIdentifier(templateName), templateName, makeSettings(overwrite, rotation), position); }
/*     */ 
/*     */ 
/*     */     
/*  49 */     public EndCityPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) { super(StructurePieceType.END_CITY_PIECE, tag, structureTemplateManager, location -> makeSettings(tag.getBooleanOr("OW", false), (Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow())); }
/*     */ 
/*     */     
/*     */     private static StructurePlaceSettings makeSettings(boolean overwrite, Rotation rotation) {
/*  53 */       BlockIgnoreProcessor processor = overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/*  54 */       return (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor(processor).setRotation(rotation);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  59 */     protected Identifier makeTemplateLocation() { return makeIdentifier(this.templateName); }
/*     */ 
/*     */ 
/*     */     
/*  63 */     private static Identifier makeIdentifier(String templateName) { return Identifier.withDefaultNamespace("end_city/" + templateName); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  68 */       super.addAdditionalSaveData(context, tag);
/*     */       
/*  70 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*  71 */       tag.putBoolean("OW", (this.placeSettings.getProcessors().get(false) == BlockIgnoreProcessor.STRUCTURE_BLOCK));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/*  76 */       if (markerId.startsWith("Chest")) {
/*  77 */         BlockPos chestPosition = position.below();
/*     */         
/*  79 */         if (chunkBB.isInside(chestPosition)) {
/*  80 */           RandomizableContainer.setBlockEntityLootTable(level, random, chestPosition, BuiltInLootTables.END_CITY_TREASURE);
/*     */         }
/*  82 */       } else if (chunkBB.isInside(position) && Level.isInSpawnableBounds(position)) {
/*  83 */         if (markerId.startsWith("Sentry")) {
/*  84 */           Shulker sentry = (Shulker)EntityType.SHULKER.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/*  85 */           if (sentry != null) {
/*  86 */             sentry.setPos(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D);
/*  87 */             level.addFreshEntity(sentry);
/*     */           } 
/*  89 */         } else if (markerId.startsWith("Elytra")) {
/*  90 */           ItemFrame itemFrame = new ItemFrame(level.getLevel(), position, this.placeSettings.getRotation().rotate(Direction.SOUTH));
/*  91 */           itemFrame.setItem(new ItemStack(Items.ELYTRA), false);
/*  92 */           level.addFreshEntity(itemFrame);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startHouseTower(StructureTemplateManager structureTemplateManager, BlockPos origin, Rotation rotation, List<StructurePiece> pieces, RandomSource random) {
/* 107 */     FAT_TOWER_GENERATOR.init();
/* 108 */     HOUSE_TOWER_GENERATOR.init();
/* 109 */     TOWER_BRIDGE_GENERATOR.init();
/* 110 */     TOWER_GENERATOR.init();
/*     */     
/* 112 */     EndCityPiece lastPiece = addHelper(pieces, new EndCityPiece(structureTemplateManager, "base_floor", origin, rotation, true));
/* 113 */     lastPiece = addHelper(pieces, addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_1", rotation, false));
/* 114 */     lastPiece = addHelper(pieces, addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "third_floor_1", rotation, false));
/* 115 */     lastPiece = addHelper(pieces, addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
/*     */     
/* 117 */     recursiveChildren(structureTemplateManager, TOWER_GENERATOR, 1, lastPiece, null, pieces, random);
/*     */   }
/*     */   
/*     */   private static EndCityPiece addHelper(List<StructurePiece> pieces, EndCityPiece piece) {
/* 121 */     pieces.add(piece);
/* 122 */     return piece;
/*     */   }
/*     */   
/*     */   private static boolean recursiveChildren(StructureTemplateManager structureTemplateManager, SectionGenerator generator, int genDepth, EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 126 */     if (genDepth > 8) {
/* 127 */       return false;
/*     */     }
/*     */     
/* 130 */     List<StructurePiece> childPieces = Lists.newArrayList();
/* 131 */     if (generator.generate(structureTemplateManager, genDepth, parent, offset, childPieces, random)) {
/*     */       
/* 133 */       boolean collision = false;
/* 134 */       int childTag = random.nextInt();
/* 135 */       for (StructurePiece child : childPieces) {
/* 136 */         child.setGenDepth(childTag);
/* 137 */         StructurePiece collisionPiece = StructurePiece.findCollisionPiece(pieces, child.getBoundingBox());
/* 138 */         if (collisionPiece != null && collisionPiece.getGenDepth() != parent.getGenDepth()) {
/* 139 */           collision = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 143 */       if (!collision) {
/* 144 */         pieces.addAll(childPieces);
/* 145 */         return true;
/*     */       } 
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */   
/* 151 */   private static final SectionGenerator HOUSE_TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 158 */         if (genDepth > 8) {
/* 159 */           return false;
/*     */         }
/*     */         
/* 162 */         Rotation rotation = parent.placeSettings().getRotation();
/* 163 */         EndCityPieces.EndCityPiece lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, parent, offset, "base_floor", rotation, true));
/*     */         
/* 165 */         int numFloors = random.nextInt(3);
/* 166 */         if (numFloors == 0) {
/* 167 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "base_roof", rotation, true));
/* 168 */         } else if (numFloors == 1) {
/* 169 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 170 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "second_roof", rotation, false));
/*     */           
/* 172 */           EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
/* 173 */         } else if (numFloors == 2) {
/* 174 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 175 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "third_floor_2", rotation, false));
/* 176 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
/*     */           
/* 178 */           EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
/*     */         } 
/* 180 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 184 */   private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList(new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6)) });
/*     */   
/*     */   private static interface SectionGenerator {
/*     */     void init();
/*     */     
/*     */     boolean generate(StructureTemplateManager param1StructureTemplateManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, RandomSource param1RandomSource); }
/*     */   
/* 191 */   private static final SectionGenerator TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 198 */         Rotation rotation = parent.placeSettings().getRotation();
/* 199 */         EndCityPieces.EndCityPiece lastPiece = parent;
/* 200 */         lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(3 + random.nextInt(2), -3, 3 + random.nextInt(2)), "tower_base", rotation, true));
/* 201 */         lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 7, 0), "tower_piece", rotation, true));
/*     */         
/* 203 */         EndCityPieces.EndCityPiece bridgePiece = (random.nextInt(3) == 0) ? lastPiece : null;
/*     */         
/* 205 */         int towerHeight = 1 + random.nextInt(3);
/* 206 */         for (int i = 0; i < towerHeight; i++) {
/* 207 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 4, 0), "tower_piece", rotation, true));
/* 208 */           if (i < towerHeight - 1 && random.nextBoolean()) {
/* 209 */             bridgePiece = lastPiece;
/*     */           }
/*     */         } 
/*     */         
/* 213 */         if (bridgePiece != null) {
/* 214 */           for (Tuple<Rotation, BlockPos> bridge : EndCityPieces.TOWER_BRIDGES) {
/* 215 */             if (random.nextBoolean()) {
/*     */               
/* 217 */               EndCityPieces.EndCityPiece bridgeStart = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, bridgePiece, (BlockPos)bridge.getB(), "bridge_end", rotation.getRotated((Rotation)bridge.getA()), true));
/* 218 */               EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, genDepth + 1, bridgeStart, null, pieces, random);
/*     */             } 
/*     */           } 
/*     */           
/* 222 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */         }
/* 224 */         else if (genDepth == 7) {
/* 225 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */         } else {
/* 227 */           return EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.FAT_TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
/*     */         } 
/*     */         
/* 230 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 234 */   private static final SectionGenerator TOWER_BRIDGE_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public boolean shipCreated;
/*     */ 
/*     */       
/* 239 */       public void init() { this.shipCreated = false; }
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 244 */         Rotation rotation = parent.placeSettings().getRotation();
/* 245 */         int bridgeLength = random.nextInt(4) + 1;
/*     */         
/* 247 */         EndCityPieces.EndCityPiece lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, parent, new BlockPos(0, 0, -4), "bridge_piece", rotation, true));
/* 248 */         lastPiece.setGenDepth(-1);
/* 249 */         int nextY = 0;
/* 250 */         for (int i = 0; i < bridgeLength; i++) {
/* 251 */           if (random.nextBoolean()) {
/* 252 */             lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -4), "bridge_piece", rotation, true));
/* 253 */             nextY = 0;
/*     */           } else {
/* 255 */             if (random.nextBoolean()) {
/* 256 */               lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -4), "bridge_steep_stairs", rotation, true));
/*     */             } else {
/* 258 */               lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -8), "bridge_gentle_stairs", rotation, true));
/*     */             } 
/* 260 */             nextY = 4;
/*     */           } 
/*     */         } 
/*     */         
/* 264 */         if (this.shipCreated || random.nextInt(10 - genDepth) != 0) {
/* 265 */           if (!EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.HOUSE_TOWER_GENERATOR, genDepth + 1, lastPiece, new BlockPos(-3, nextY + 1, -11), pieces, random)) {
/* 266 */             return false;
/*     */           }
/*     */         } else {
/*     */           
/* 270 */           EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-8 + random.nextInt(8), nextY, -70 + random.nextInt(10)), "ship", rotation, true));
/* 271 */           this.shipCreated = true;
/*     */         } 
/*     */ 
/*     */         
/* 275 */         lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(4, nextY, 0), "bridge_end", rotation.getRotated(Rotation.CLOCKWISE_180), true));
/* 276 */         lastPiece.setGenDepth(-1);
/*     */         
/* 278 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 282 */   private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList(new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   private static final SectionGenerator FAT_TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 297 */         Rotation rotation = parent.placeSettings().getRotation();
/*     */         
/* 299 */         EndCityPieces.EndCityPiece lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, parent, new BlockPos(-3, 4, -3), "fat_tower_base", rotation, true));
/* 300 */         lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 4, 0), "fat_tower_middle", rotation, true));
/* 301 */         for (int i = 0; i < 2 && 
/* 302 */           random.nextInt(3) != 0; i++) {
/*     */ 
/*     */           
/* 305 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 8, 0), "fat_tower_middle", rotation, true));
/*     */           
/* 307 */           for (Tuple<Rotation, BlockPos> bridge : EndCityPieces.FAT_TOWER_BRIDGES) {
/* 308 */             if (random.nextBoolean()) {
/*     */               
/* 310 */               EndCityPieces.EndCityPiece bridgeStart = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, (BlockPos)bridge.getB(), "bridge_end", rotation.getRotated((Rotation)bridge.getA()), true));
/* 311 */               EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, genDepth + 1, bridgeStart, null, pieces, random);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 316 */         lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-2, 8, -2), "fat_tower_top", rotation, true));
/* 317 */         return true;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
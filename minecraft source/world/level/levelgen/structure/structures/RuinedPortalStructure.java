/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ 
/*     */ public class RuinedPortalStructure extends Structure {
/*  35 */   private static final String[] STRUCTURE_LOCATION_PORTALS = { "ruined_portal/portal_1", "ruined_portal/portal_2", "ruined_portal/portal_3", "ruined_portal/portal_4", "ruined_portal/portal_5", "ruined_portal/portal_6", "ruined_portal/portal_7", "ruined_portal/portal_8", "ruined_portal/portal_9", "ruined_portal/portal_10" };
/*     */   
/*     */   private static final float PROBABILITY_OF_GIANT_PORTAL = 0.05F;
/*     */   
/*     */   private static final int MIN_Y_INDEX = 15;
/*  40 */   private static final String[] STRUCTURE_LOCATION_GIANT_PORTALS = { "ruined_portal/giant_portal_1", "ruined_portal/giant_portal_2", "ruined_portal/giant_portal_3" }; private final List<Setup> setups;
/*     */   public static final class Setup extends Record { private final RuinedPortalPiece.VerticalPlacement placement; private final float airPocketProbability; private final float mossiness; private final boolean overgrown;
/*     */     private final boolean vines;
/*     */     private final boolean canBeCold;
/*     */     private final boolean replaceWithBlackstone;
/*     */     private final float weight;
/*     */     
/*  47 */     public Setup(RuinedPortalPiece.VerticalPlacement placement, float airPocketProbability, float mossiness, boolean overgrown, boolean vines, boolean canBeCold, boolean replaceWithBlackstone, float weight) { this.placement = placement; this.airPocketProbability = airPocketProbability; this.mossiness = mossiness; this.overgrown = overgrown; this.vines = vines; this.canBeCold = canBeCold; this.replaceWithBlackstone = replaceWithBlackstone; this.weight = weight; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  47 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup; } public RuinedPortalPiece.VerticalPlacement placement() { return this.placement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;
/*  47 */       //   0	8	1	o	Ljava/lang/Object; } public float airPocketProbability() { return this.airPocketProbability; } public float mossiness() { return this.mossiness; } public boolean overgrown() { return this.overgrown; } public boolean vines() { return this.vines; } public boolean canBeCold() { return this.canBeCold; } public boolean replaceWithBlackstone() { return this.replaceWithBlackstone; } public float weight() { return this.weight; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     public static final Codec<Setup> CODEC = RecordCodecBuilder.create(i -> i.group(RuinedPortalPiece.VerticalPlacement.CODEC
/*  58 */           .fieldOf("placement").forGetter(Setup::placement), 
/*  59 */           Codec.floatRange(0.0F, 1.0F).fieldOf("air_pocket_probability").forGetter(Setup::airPocketProbability), 
/*  60 */           Codec.floatRange(0.0F, 1.0F).fieldOf("mossiness").forGetter(Setup::mossiness), Codec.BOOL
/*  61 */           .fieldOf("overgrown").forGetter(Setup::overgrown), Codec.BOOL
/*  62 */           .fieldOf("vines").forGetter(Setup::vines), Codec.BOOL
/*  63 */           .fieldOf("can_be_cold").forGetter(Setup::canBeCold), Codec.BOOL
/*  64 */           .fieldOf("replace_with_blackstone").forGetter(Setup::replaceWithBlackstone), ExtraCodecs.POSITIVE_FLOAT
/*  65 */           .fieldOf("weight").forGetter(Setup::weight))
/*  66 */         .apply(i, Setup::new)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final MapCodec<RuinedPortalStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  72 */         settingsCodec(i), 
/*  73 */         ExtraCodecs.nonEmptyList(Setup.CODEC.listOf()).fieldOf("setups").forGetter(()))
/*  74 */       .apply(i, RuinedPortalStructure::new));
/*     */   
/*     */   public RuinedPortalStructure(Structure.StructureSettings settings, List<Setup> setups) {
/*  77 */     super(settings);
/*  78 */     this.setups = setups;
/*     */   }
/*     */ 
/*     */   
/*  82 */   public RuinedPortalStructure(Structure.StructureSettings settings, Setup setup) { this(settings, List.of(setup)); }
/*     */ 
/*     */   
/*     */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/*     */     Identifier templateLocation;
/*  87 */     RuinedPortalPiece.Properties properties = new RuinedPortalPiece.Properties();
/*     */     
/*  89 */     WorldgenRandom random = context.random();
/*     */ 
/*     */     
/*  92 */     Setup chosenSetup = null;
/*  93 */     if (this.setups.size() > 1) {
/*  94 */       float total = 0.0F;
/*  95 */       for (Setup s : this.setups) {
/*  96 */         total += s.weight();
/*     */       }
/*  98 */       float pick = random.nextFloat();
/*  99 */       for (Setup s : this.setups) {
/* 100 */         pick -= s.weight() / total;
/* 101 */         if (pick < 0.0F) {
/* 102 */           chosenSetup = s;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 107 */       chosenSetup = (Setup)this.setups.get(0);
/*     */     } 
/* 109 */     if (chosenSetup == null) {
/* 110 */       throw new IllegalStateException();
/*     */     }
/* 112 */     Setup setup = chosenSetup;
/*     */     
/* 114 */     properties.airPocket = sample(random, setup.airPocketProbability());
/* 115 */     properties.mossiness = setup.mossiness();
/* 116 */     properties.overgrown = setup.overgrown();
/* 117 */     properties.vines = setup.vines();
/* 118 */     properties.replaceWithBlackstone = setup.replaceWithBlackstone();
/*     */ 
/*     */     
/* 121 */     if (random.nextFloat() < 0.05F) {
/* 122 */       templateLocation = Identifier.withDefaultNamespace(STRUCTURE_LOCATION_GIANT_PORTALS[random.nextInt(STRUCTURE_LOCATION_GIANT_PORTALS.length)]);
/*     */     } else {
/* 124 */       templateLocation = Identifier.withDefaultNamespace(STRUCTURE_LOCATION_PORTALS[random.nextInt(STRUCTURE_LOCATION_PORTALS.length)]);
/*     */     } 
/*     */     
/* 127 */     StructureTemplate template = context.structureTemplateManager().getOrCreate(templateLocation);
/* 128 */     Rotation rotation = (Rotation)Util.getRandom(Rotation.values(), random);
/* 129 */     Mirror mirror = (random.nextFloat() < 0.5F) ? Mirror.NONE : Mirror.FRONT_BACK;
/* 130 */     BlockPos pivot = new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
/*     */     
/* 132 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 133 */     LevelHeightAccessor heightAccessor = context.heightAccessor();
/* 134 */     RandomState randomState = context.randomState();
/*     */     
/* 136 */     BlockPos basePosition = context.chunkPos().getWorldPosition();
/* 137 */     BoundingBox boundingBox = template.getBoundingBox(basePosition, rotation, pivot, mirror);
/* 138 */     BlockPos center = boundingBox.getCenter();
/* 139 */     int surfaceY = chunkGenerator.getBaseHeight(center.getX(), center.getZ(), RuinedPortalPiece.getHeightMapType(setup.placement()), heightAccessor, randomState) - 1;
/* 140 */     int projectedY = findSuitableY(random, chunkGenerator, setup.placement(), properties.airPocket, surfaceY, boundingBox.getYSpan(), boundingBox, heightAccessor, randomState);
/*     */     
/* 142 */     BlockPos origin = new BlockPos(basePosition.getX(), projectedY, basePosition.getZ());
/* 143 */     return Optional.of(new Structure.GenerationStub(origin, builder -> {
/* 144 */             if (setup.canBeCold()) {
/* 145 */               properties.cold = isCold(origin, context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(origin.getX()), QuartPos.fromBlock(origin.getY()), QuartPos.fromBlock(origin.getZ()), randomState.sampler()), chunkGenerator.getSeaLevel());
/*     */             }
/*     */             
/* 148 */             builder.addPiece(new RuinedPortalPiece(context.structureTemplateManager(), origin, setup.placement(), properties, templateLocation, template, rotation, mirror, pivot));
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean sample(WorldgenRandom random, float limit) {
/* 154 */     if (limit == 0.0F)
/* 155 */       return false; 
/* 156 */     if (limit == 1.0F) {
/* 157 */       return true;
/*     */     }
/* 159 */     return (random.nextFloat() < limit);
/*     */   }
/*     */ 
/*     */   
/* 163 */   private static boolean isCold(BlockPos pos, Holder<Biome> biome, int seaLevel) { return ((Biome)biome.value()).coldEnoughToSnow(pos, seaLevel); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int findSuitableY(RandomSource random, ChunkGenerator generator, RuinedPortalPiece.VerticalPlacement verticalPlacement, boolean airPocket, int surfaceYAtCenter, int ySpan, BoundingBox boundingBox, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 170 */     int newY, minY = heightAccessor.getMinY() + 15;
/* 171 */     if (verticalPlacement == RuinedPortalPiece.VerticalPlacement.IN_NETHER) {
/* 172 */       if (airPocket) {
/*     */         
/* 174 */         newY = Mth.randomBetweenInclusive(random, 32, 100);
/*     */       }
/* 176 */       else if (random.nextFloat() < 0.5F) {
/*     */         
/* 178 */         newY = Mth.randomBetweenInclusive(random, 27, 29);
/*     */       } else {
/*     */         
/* 181 */         newY = Mth.randomBetweenInclusive(random, 29, 100);
/*     */       }
/*     */     
/* 184 */     } else if (verticalPlacement == RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN) {
/* 185 */       int maxY = surfaceYAtCenter - ySpan;
/* 186 */       newY = getRandomWithinInterval(random, 70, maxY);
/* 187 */     } else if (verticalPlacement == RuinedPortalPiece.VerticalPlacement.UNDERGROUND) {
/* 188 */       int maxY = surfaceYAtCenter - ySpan;
/* 189 */       newY = getRandomWithinInterval(random, minY, maxY);
/* 190 */     } else if (verticalPlacement == RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED) {
/* 191 */       newY = surfaceYAtCenter - ySpan + Mth.randomBetweenInclusive(random, 2, 8);
/*     */     } else {
/* 193 */       newY = surfaceYAtCenter;
/*     */     } 
/*     */     
/* 196 */     ImmutableList immutableList = ImmutableList.of(new BlockPos(boundingBox
/* 197 */           .minX(), 0, boundingBox.minZ()), new BlockPos(boundingBox
/* 198 */           .maxX(), 0, boundingBox.minZ()), new BlockPos(boundingBox
/* 199 */           .minX(), 0, boundingBox.maxZ()), new BlockPos(boundingBox
/* 200 */           .maxX(), 0, boundingBox.maxZ()));
/*     */ 
/*     */     
/* 203 */     List<NoiseColumn> columns = (List)immutableList.stream().map(p -> generator.getBaseColumn(p.getX(), p.getZ(), heightAccessor, randomState)).collect(Collectors.toList());
/*     */     
/* 205 */     Heightmap.Types heightmap = (verticalPlacement == RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
/*     */     
/* 207 */     int projectedY = newY;
/*     */ 
/*     */ 
/*     */     
/* 211 */     label40: while (projectedY > minY) {
/* 212 */       int cornersOnSolidGround = 0;
/* 213 */       for (NoiseColumn column : columns) {
/*     */         
/* 215 */         BlockState blockState = column.getBlock(projectedY);
/*     */         
/* 217 */         cornersOnSolidGround++;
/* 218 */         if (heightmap.isOpaque().test(blockState) && cornersOnSolidGround == 3) {
/*     */           break label40;
/*     */         }
/*     */       } 
/*     */       
/* 223 */       projectedY--;
/*     */     } 
/* 225 */     return projectedY;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getRandomWithinInterval(RandomSource random, int minPreferred, int max) {
/* 230 */     if (minPreferred < max) {
/* 231 */       return Mth.randomBetweenInclusive(random, minPreferred, max);
/*     */     }
/* 233 */     return max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public StructureType<?> type() { return StructureType.RUINED_PORTAL; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
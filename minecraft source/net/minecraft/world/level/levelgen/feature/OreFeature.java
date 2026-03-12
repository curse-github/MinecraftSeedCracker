/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.BulkSectionAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ 
/*     */ public class OreFeature
/*     */   extends Feature<OreConfiguration> {
/*  20 */   public OreFeature(Codec<OreConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<OreConfiguration> context) {
/*  25 */     RandomSource random = context.random();
/*  26 */     BlockPos origin = context.origin();
/*  27 */     WorldGenLevel level = context.level();
/*  28 */     OreConfiguration config = (OreConfiguration)context.config();
/*  29 */     float dir = random.nextFloat() * 3.1415927F;
/*     */     
/*  31 */     float spreadXY = config.size / 8.0F;
/*  32 */     int maxRadius = Mth.ceil((config.size / 16.0F * 2.0F + 1.0F) / 2.0F);
/*  33 */     double x0 = origin.getX() + Math.sin(dir) * spreadXY;
/*  34 */     double x1 = origin.getX() - Math.sin(dir) * spreadXY;
/*  35 */     double z0 = origin.getZ() + Math.cos(dir) * spreadXY;
/*  36 */     double z1 = origin.getZ() - Math.cos(dir) * spreadXY;
/*     */     
/*  38 */     int spreadY = 2;
/*  39 */     double y0 = (origin.getY() + random.nextInt(3) - 2);
/*  40 */     double y1 = (origin.getY() + random.nextInt(3) - 2);
/*     */     
/*  42 */     int xStart = origin.getX() - Mth.ceil(spreadXY) - maxRadius;
/*  43 */     int yStart = origin.getY() - 2 - maxRadius;
/*  44 */     int zStart = origin.getZ() - Mth.ceil(spreadXY) - maxRadius;
/*  45 */     int sizeXZ = 2 * (Mth.ceil(spreadXY) + maxRadius);
/*  46 */     int sizeY = 2 * (2 + maxRadius);
/*     */ 
/*     */     
/*  49 */     for (int xprobe = xStart; xprobe <= xStart + sizeXZ; xprobe++) {
/*  50 */       for (int zprobe = zStart; zprobe <= zStart + sizeXZ; zprobe++) {
/*  51 */         if (yStart <= level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, xprobe, zprobe)) {
/*  52 */           return doPlace(level, random, config, x0, x1, z0, z1, y0, y1, xStart, yStart, zStart, sizeXZ, sizeY);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean doPlace(WorldGenLevel level, RandomSource random, OreConfiguration config, double x0, double x1, double z0, double z1, double y0, double y1, int xStart, int yStart, int zStart, int sizeXZ, int sizeY) {
/*  61 */     int placed = 0;
/*     */     
/*  63 */     BitSet tested = new BitSet(sizeXZ * sizeY * sizeXZ);
/*  64 */     BlockPos.MutableBlockPos orePos = new BlockPos.MutableBlockPos();
/*  65 */     int size = config.size;
/*  66 */     double[] data = new double[size * 4];
/*     */     
/*  68 */     for (int i = 0; i < size; i++) {
/*  69 */       float step = i / size;
/*  70 */       double xx = Mth.lerp(step, x0, x1);
/*  71 */       double yy = Mth.lerp(step, y0, y1);
/*  72 */       double zz = Mth.lerp(step, z0, z1);
/*     */       
/*  74 */       double ss = random.nextDouble() * size / 16.0D;
/*  75 */       double r = ((Mth.sin((3.1415927F * step)) + 1.0F) * ss + 1.0D) / 2.0D;
/*     */       
/*  77 */       data[i * 4 + 0] = xx;
/*  78 */       data[i * 4 + 1] = yy;
/*  79 */       data[i * 4 + 2] = zz;
/*  80 */       data[i * 4 + 3] = r;
/*     */     } 
/*     */     
/*  83 */     for (int i1 = 0; i1 < size - 1; i1++) {
/*  84 */       if (data[i1 * 4 + 3] > 0.0D)
/*     */       {
/*     */ 
/*     */         
/*  88 */         for (int i2 = i1 + 1; i2 < size; i2++) {
/*  89 */           if (data[i2 * 4 + 3] > 0.0D) {
/*     */ 
/*     */ 
/*     */             
/*  93 */             double dx = data[i1 * 4 + 0] - data[i2 * 4 + 0];
/*  94 */             double dy = data[i1 * 4 + 1] - data[i2 * 4 + 1];
/*  95 */             double dz = data[i1 * 4 + 2] - data[i2 * 4 + 2];
/*  96 */             double dr = data[i1 * 4 + 3] - data[i2 * 4 + 3];
/*     */             
/*  98 */             if (dr * dr > dx * dx + dy * dy + dz * dz)
/*  99 */               if (dr > 0.0D) {
/* 100 */                 data[i2 * 4 + 3] = -1.0D;
/*     */               } else {
/* 102 */                 data[i1 * 4 + 3] = -1.0D;
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 108 */     BulkSectionAccess sectionGetter = new BulkSectionAccess(level); 
/* 109 */     try { for (int i = 0; i < size; i++) {
/* 110 */         double r = data[i * 4 + 3];
/* 111 */         if (r >= 0.0D) {
/*     */ 
/*     */ 
/*     */           
/* 115 */           double xx = data[i * 4 + 0];
/* 116 */           double yy = data[i * 4 + 1];
/* 117 */           double zz = data[i * 4 + 2];
/*     */ 
/*     */           
/* 120 */           int xMin = Math.max(Mth.floor(xx - r), xStart);
/* 121 */           int yMin = Math.max(Mth.floor(yy - r), yStart);
/* 122 */           int zMin = Math.max(Mth.floor(zz - r), zStart);
/*     */           
/* 124 */           int xMax = Math.max(Mth.floor(xx + r), xMin);
/* 125 */           int yMax = Math.max(Mth.floor(yy + r), yMin);
/* 126 */           int zMax = Math.max(Mth.floor(zz + r), zMin);
/*     */           
/* 128 */           for (int x = xMin; x <= xMax; x++) {
/* 129 */             double xd = (x + 0.5D - xx) / r;
/* 130 */             if (xd * xd < 1.0D)
/* 131 */               for (int y = yMin; y <= yMax; y++) {
/* 132 */                 double yd = (y + 0.5D - yy) / r;
/* 133 */                 if (xd * xd + yd * yd < 1.0D)
/* 134 */                   for (int z = zMin; z <= zMax; z++) {
/* 135 */                     double zd = (z + 0.5D - zz) / r;
/* 136 */                     if (xd * xd + yd * yd + zd * zd < 1.0D && 
/* 137 */                       !level.isOutsideBuildHeight(y)) {
/*     */ 
/*     */                       
/* 140 */                       int bitSetIndex = x - xStart + (y - yStart) * sizeXZ + (z - zStart) * sizeXZ * sizeY;
/* 141 */                       if (!tested.get(bitSetIndex)) {
/*     */ 
/*     */                         
/* 144 */                         tested.set(bitSetIndex);
/*     */                         
/* 146 */                         orePos.set(x, y, z);
/* 147 */                         if (level.ensureCanWrite(orePos)) {
/*     */ 
/*     */                           
/* 150 */                           LevelChunkSection section = sectionGetter.getSection(orePos);
/* 151 */                           if (section != null) {
/*     */ 
/*     */                             
/* 154 */                             int sectionRelativeX = SectionPos.sectionRelative(x);
/* 155 */                             int sectionRelativeY = SectionPos.sectionRelative(y);
/* 156 */                             int sectionRelativeZ = SectionPos.sectionRelative(z);
/*     */                             
/* 158 */                             BlockState blockState = section.getBlockState(sectionRelativeX, sectionRelativeY, sectionRelativeZ);
/* 159 */                             for (OreConfiguration.TargetBlockState targetState : config.targetStates) {
/* 160 */                               Objects.requireNonNull(sectionGetter); if (canPlaceOre(blockState, sectionGetter::getBlockState, random, config, targetState, orePos)) {
/* 161 */                                 section.setBlockState(sectionRelativeX, sectionRelativeY, sectionRelativeZ, targetState.state, false);
/* 162 */                                 placed++; break;
/*     */                               } 
/*     */                             } 
/*     */                           } 
/*     */                         } 
/*     */                       } 
/*     */                     } 
/*     */                   }  
/*     */               }  
/*     */           } 
/*     */         } 
/* 173 */       }  sectionGetter.close(); } catch (Throwable throwable) { try { sectionGetter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 175 */      return (placed > 0);
/*     */   }
/*     */   
/*     */   public static boolean canPlaceOre(BlockState orePosState, Function<BlockPos, BlockState> blockGetter, RandomSource random, OreConfiguration config, OreConfiguration.TargetBlockState targetState, BlockPos.MutableBlockPos orePos) {
/* 179 */     if (!targetState.target.test(orePosState, random)) {
/* 180 */       return false;
/*     */     }
/* 182 */     if (shouldSkipAirCheck(random, config.discardChanceOnAirExposure)) {
/* 183 */       return true;
/*     */     }
/* 185 */     return !isAdjacentToAir(blockGetter, orePos);
/*     */   }
/*     */   
/*     */   protected static boolean shouldSkipAirCheck(RandomSource random, float discardChanceOnAirExposure) {
/* 189 */     if (discardChanceOnAirExposure <= 0.0F) {
/* 190 */       return true;
/*     */     }
/* 192 */     if (discardChanceOnAirExposure >= 1.0F) {
/* 193 */       return false;
/*     */     }
/* 195 */     return (random.nextFloat() >= discardChanceOnAirExposure);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\OreFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
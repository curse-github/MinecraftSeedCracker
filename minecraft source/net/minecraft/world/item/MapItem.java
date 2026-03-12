/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.LinkedHashMultiset;
/*     */ import com.google.common.collect.Multisets;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.MapPostProcessing;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ 
/*     */ public class MapItem
/*     */   extends Item
/*     */ {
/*     */   public static final int IMAGE_WIDTH = 128;
/*     */   public static final int IMAGE_HEIGHT = 128;
/*     */   
/*  40 */   public MapItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */   
/*     */   public static ItemStack create(ServerLevel level, int originX, int originZ, byte scale, boolean trackPosition, boolean unlimitedTracking) {
/*  44 */     ItemStack map = new ItemStack(Items.FILLED_MAP);
/*  45 */     MapId newId = createNewSavedData(level, originX, originZ, scale, trackPosition, unlimitedTracking, level.dimension());
/*  46 */     map.set(DataComponents.MAP_ID, newId);
/*  47 */     return map;
/*     */   }
/*     */ 
/*     */   
/*  51 */   public static MapItemSavedData getSavedData(MapId id, Level level) { return (id == null) ? null : level.getMapData(id); }
/*     */ 
/*     */   
/*     */   public static MapItemSavedData getSavedData(ItemStack itemStack, Level level) {
/*  55 */     MapId id = (MapId)itemStack.get(DataComponents.MAP_ID);
/*  56 */     return getSavedData(id, level);
/*     */   }
/*     */   
/*     */   private static MapId createNewSavedData(ServerLevel level, int xSpawn, int zSpawn, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
/*  60 */     MapItemSavedData newData = MapItemSavedData.createFresh(xSpawn, zSpawn, (byte)scale, trackingPosition, unlimitedTracking, dimension);
/*  61 */     MapId id = level.getFreeMapId();
/*  62 */     level.setMapData(id, newData);
/*  63 */     return id;
/*     */   }
/*     */   
/*     */   public void update(Level level, Entity player, MapItemSavedData data) {
/*  67 */     if (level.dimension() != data.dimension || !(player instanceof Player)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  72 */     int scale = 1 << data.scale;
/*  73 */     int centerX = data.centerX;
/*  74 */     int centerZ = data.centerZ;
/*     */ 
/*     */     
/*  77 */     int playerImgX = Mth.floor(player.getX() - centerX) / scale + 64;
/*  78 */     int playerImgY = Mth.floor(player.getZ() - centerZ) / scale + 64;
/*  79 */     int radius = 128 / scale;
/*     */     
/*  81 */     if (level.dimensionType().hasCeiling()) {
/*  82 */       radius /= 2;
/*     */     }
/*     */     
/*  85 */     MapItemSavedData.HoldingPlayer holdingPlayer = data.getHoldingPlayer((Player)player);
/*  86 */     holdingPlayer.step++;
/*  87 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*  88 */     BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/*     */     
/*  90 */     boolean foundConsecutiveChanges = false;
/*  91 */     for (int imgX = playerImgX - radius + 1; imgX < playerImgX + radius; imgX++) {
/*  92 */       if ((imgX & 0xF) == (holdingPlayer.step & 0xF) || foundConsecutiveChanges) {
/*     */ 
/*     */ 
/*     */         
/*  96 */         foundConsecutiveChanges = false;
/*  97 */         double previousAverageAreaHeight = 0.0D;
/*  98 */         for (int imgY = playerImgY - radius - 1; imgY < playerImgY + radius; imgY++) {
/*  99 */           if (imgX >= 0 && imgY >= -1 && imgX < 128 && imgY < 128) {
/*     */ 
/*     */ 
/*     */             
/* 103 */             int distanceToPlayerSqr = Mth.square(imgX - playerImgX) + Mth.square(imgY - playerImgY);
/*     */             
/* 105 */             boolean ditherBlack = (distanceToPlayerSqr > (radius - 2) * (radius - 2));
/*     */             
/* 107 */             int averagingAreaMinX = (centerX / scale + imgX - 64) * scale;
/* 108 */             int averagingAreaMinZ = (centerZ / scale + imgY - 64) * scale;
/*     */             
/* 110 */             LinkedHashMultiset linkedHashMultiset = LinkedHashMultiset.create();
/*     */             
/* 112 */             LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(averagingAreaMinX), SectionPos.blockToSectionCoord(averagingAreaMinZ));
/* 113 */             if (!chunk.isEmpty()) {
/*     */               MapColor.Brightness brightness;
/*     */ 
/*     */               
/* 117 */               int waterDepth = 0;
/*     */               
/* 119 */               double averageAreaHeight = 0.0D;
/* 120 */               if (level.dimensionType().hasCeiling()) {
/* 121 */                 int ceilingNoise = averagingAreaMinX + averagingAreaMinZ * 231871;
/* 122 */                 ceilingNoise = ceilingNoise * ceilingNoise * 31287121 + ceilingNoise * 11;
/*     */                 
/* 124 */                 if ((ceilingNoise >> 20 & true) == 0) {
/* 125 */                   linkedHashMultiset.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
/*     */                 } else {
/* 127 */                   linkedHashMultiset.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
/*     */                 } 
/*     */                 
/* 130 */                 averageAreaHeight = 100.0D;
/*     */               
/*     */               }
/*     */               else {
/*     */ 
/*     */                 
/* 136 */                 for (int averagingAreaDeltaX = 0; averagingAreaDeltaX < scale; averagingAreaDeltaX++) {
/* 137 */                   for (int averagingAreaDeltaZ = 0; averagingAreaDeltaZ < scale; averagingAreaDeltaZ++) {
/* 138 */                     BlockState state; blockPos.set(averagingAreaMinX + averagingAreaDeltaX, 0, averagingAreaMinZ + averagingAreaDeltaZ);
/* 139 */                     int columnY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) + 1;
/*     */                     
/* 141 */                     if (columnY > level.getMinY()) {
/*     */                       do {
/* 143 */                         columnY--;
/* 144 */                         blockPos.setY(columnY);
/* 145 */                         state = chunk.getBlockState(blockPos);
/* 146 */                       } while (state.getMapColor(level, blockPos) == MapColor.NONE && columnY > level.getMinY());
/*     */                       
/* 148 */                       if (columnY > level.getMinY() && !state.getFluidState().isEmpty()) {
/*     */                         BlockState belowBlock;
/* 150 */                         int solidY = columnY - 1;
/*     */                         
/* 152 */                         belowPos.set(blockPos);
/*     */                         do {
/* 154 */                           belowPos.setY(solidY--);
/* 155 */                           belowBlock = chunk.getBlockState(belowPos);
/* 156 */                           waterDepth++;
/* 157 */                         } while (solidY > level.getMinY() && !belowBlock.getFluidState().isEmpty());
/*     */                         
/* 159 */                         state = getCorrectStateForFluidBlock(level, state, blockPos);
/*     */                       } 
/*     */                     } else {
/* 162 */                       state = Blocks.BEDROCK.defaultBlockState();
/*     */                     } 
/*     */                     
/* 165 */                     data.checkBanners(level, blockPos.getX(), blockPos.getZ());
/*     */                     
/* 167 */                     averageAreaHeight += columnY / (scale * scale);
/*     */                     
/* 169 */                     linkedHashMultiset.add(state.getMapColor(level, blockPos));
/*     */                   } 
/*     */                 } 
/*     */               } 
/* 173 */               waterDepth /= scale * scale;
/*     */               
/* 175 */               MapColor color = (MapColor)Iterables.getFirst(Multisets.copyHighestCountFirst(linkedHashMultiset), MapColor.NONE);
/*     */               
/* 177 */               if (color == MapColor.WATER) {
/* 178 */                 double diff = waterDepth * 0.1D + (imgX + imgY & true) * 0.2D;
/* 179 */                 if (diff < 0.5D) {
/* 180 */                   brightness = MapColor.Brightness.HIGH;
/* 181 */                 } else if (diff > 0.9D) {
/* 182 */                   brightness = MapColor.Brightness.LOW;
/*     */                 } else {
/* 184 */                   brightness = MapColor.Brightness.NORMAL;
/*     */                 } 
/*     */               } else {
/* 187 */                 double diff = (averageAreaHeight - previousAverageAreaHeight) * 4.0D / (scale + 4) + ((imgX + imgY & true) - 0.5D) * 0.4D;
/* 188 */                 if (diff > 0.6D) {
/* 189 */                   brightness = MapColor.Brightness.HIGH;
/* 190 */                 } else if (diff < -0.6D) {
/* 191 */                   brightness = MapColor.Brightness.LOW;
/*     */                 } else {
/* 193 */                   brightness = MapColor.Brightness.NORMAL;
/*     */                 } 
/*     */               } 
/*     */               
/* 197 */               previousAverageAreaHeight = averageAreaHeight;
/*     */               
/* 199 */               if (imgY >= 0)
/*     */               {
/*     */                 
/* 202 */                 if (distanceToPlayerSqr < radius * radius)
/*     */                 {
/*     */                   
/* 205 */                   if (!ditherBlack || (imgX + imgY & true) != 0)
/*     */                   {
/*     */ 
/*     */                     
/* 209 */                     foundConsecutiveChanges |= data.updateColor(imgX, imgY, color.getPackedId(brightness)); }  }  } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } private BlockState getCorrectStateForFluidBlock(Level level, BlockState state, BlockPos pos) {
/* 215 */     FluidState fluidState = state.getFluidState();
/* 216 */     if (!fluidState.isEmpty() && !state.isFaceSturdy(level, pos, Direction.UP)) {
/* 217 */       return fluidState.createLegacyBlock();
/*     */     }
/*     */     
/* 220 */     return state;
/*     */   }
/*     */ 
/*     */   
/* 224 */   private static boolean isBiomeWatery(boolean[] isBiomeWatery, int x, int z) { return isBiomeWatery[z * 128 + x]; }
/*     */ 
/*     */   
/*     */   public static void renderBiomePreviewMap(ServerLevel level, ItemStack mapItemStack) {
/* 228 */     MapItemSavedData data = getSavedData(mapItemStack, level);
/* 229 */     if (data == null) {
/*     */       return;
/*     */     }
/* 232 */     if (level.dimension() != data.dimension) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 237 */     int scale = 1 << data.scale;
/* 238 */     int centerX = data.centerX;
/* 239 */     int centerZ = data.centerZ;
/*     */     
/* 241 */     boolean[] isBiomeWatery = new boolean[16384];
/*     */     
/* 243 */     int unscaledStartX = centerX / scale - 64;
/* 244 */     int unscaledStartZ = centerZ / scale - 64;
/* 245 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 246 */     for (int row = 0; row < 128; row++) {
/* 247 */       for (int column = 0; column < 128; column++) {
/* 248 */         Holder<Biome> biome = level.getBiome(pos.set((unscaledStartX + column) * scale, 0, (unscaledStartZ + row) * scale));
/* 249 */         isBiomeWatery[row * 128 + column] = biome.is(BiomeTags.WATER_ON_MAP_OUTLINES);
/*     */       } 
/*     */     } 
/* 252 */     for (int mx = 1; mx < 127; mx++) {
/* 253 */       for (int mz = 1; mz < 127; mz++) {
/* 254 */         int waterCount = 0;
/* 255 */         for (int dx = -1; dx < 2; dx++) {
/* 256 */           for (int dz = -1; dz < 2; dz++) {
/* 257 */             if ((dx != 0 || dz != 0) && isBiomeWatery(isBiomeWatery, mx + dx, mz + dz)) {
/* 258 */               waterCount++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 263 */         MapColor.Brightness brightness = MapColor.Brightness.LOWEST;
/* 264 */         MapColor newColor = MapColor.NONE;
/*     */         
/* 266 */         if (isBiomeWatery(isBiomeWatery, mx, mz)) {
/* 267 */           newColor = MapColor.COLOR_ORANGE;
/* 268 */           if (waterCount > 7 && mz % 2 == 0) {
/* 269 */             switch ((mx + (int)(Mth.sin((mz + 0.0F)) * 7.0F)) / 8 % 5) { case 0: case 4:
/* 270 */                 brightness = MapColor.Brightness.LOW; break;
/* 271 */               case 1: case 3: brightness = MapColor.Brightness.NORMAL; break;
/* 272 */               case 2: brightness = MapColor.Brightness.HIGH; break; }
/*     */           
/* 274 */           } else if (waterCount > 7) {
/* 275 */             newColor = MapColor.NONE;
/* 276 */           } else if (waterCount > 5) {
/* 277 */             brightness = MapColor.Brightness.NORMAL;
/* 278 */           } else if (waterCount > 3) {
/* 279 */             brightness = MapColor.Brightness.LOW;
/* 280 */           } else if (waterCount > 1) {
/* 281 */             brightness = MapColor.Brightness.LOW;
/*     */           } 
/* 283 */         } else if (waterCount > 0) {
/* 284 */           newColor = MapColor.COLOR_BROWN;
/* 285 */           if (waterCount > 3) {
/* 286 */             brightness = MapColor.Brightness.NORMAL;
/*     */           } else {
/* 288 */             brightness = MapColor.Brightness.LOWEST;
/*     */           } 
/*     */         } 
/*     */         
/* 292 */         if (newColor != MapColor.NONE) {
/* 293 */           data.setColor(mx, mz, newColor.getPackedId(brightness));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot) {
/* 301 */     MapItemSavedData data = getSavedData(itemStack, level);
/* 302 */     if (data == null) {
/*     */       return;
/*     */     }
/*     */     
/* 306 */     if (owner instanceof Player) { Player player = (Player)owner;
/* 307 */       data.tickCarriedBy(player, itemStack); }
/*     */ 
/*     */     
/* 310 */     if (!data.locked && slot != null && slot.getType() == EquipmentSlot.Type.HAND) {
/* 311 */       update(level, owner, data);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCraftedPostProcess(ItemStack itemStack, Level level) {
/* 319 */     MapPostProcessing postProcessing = (MapPostProcessing)itemStack.remove(DataComponents.MAP_POST_PROCESSING);
/* 320 */     if (postProcessing == null) {
/*     */       return;
/*     */     }
/* 323 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 324 */       switch (postProcessing) { case LOCK:
/* 325 */           lockMap(itemStack, serverLevel); break;
/* 326 */         case SCALE: scaleMap(itemStack, serverLevel);
/*     */           break; }
/*     */        }
/*     */   
/*     */   }
/*     */   private static void scaleMap(ItemStack itemStack, ServerLevel level) {
/* 332 */     MapItemSavedData original = getSavedData(itemStack, level);
/*     */     
/* 334 */     if (original != null) {
/* 335 */       MapId id = level.getFreeMapId();
/* 336 */       level.setMapData(id, original.scaled());
/* 337 */       itemStack.set(DataComponents.MAP_ID, id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void lockMap(ItemStack map, ServerLevel level) {
/* 342 */     MapItemSavedData mapData = getSavedData(map, level);
/* 343 */     if (mapData != null) {
/* 344 */       MapId id = level.getFreeMapId();
/* 345 */       MapItemSavedData newData = mapData.locked();
/* 346 */       level.setMapData(id, newData);
/* 347 */       map.set(DataComponents.MAP_ID, id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/* 353 */     BlockState clicked = context.getLevel().getBlockState(context.getClickedPos());
/* 354 */     if (clicked.is(BlockTags.BANNERS)) {
/* 355 */       if (!context.getLevel().isClientSide()) {
/* 356 */         MapItemSavedData data = getSavedData(context.getItemInHand(), context.getLevel());
/* 357 */         if (data != null && 
/* 358 */           !data.toggleBanner(context.getLevel(), context.getClickedPos())) {
/* 359 */           return InteractionResult.FAIL;
/*     */         }
/*     */       } 
/*     */       
/* 363 */       return InteractionResult.SUCCESS;
/*     */     } 
/* 365 */     return super.useOn(context);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\MapItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.MapDecorations;
/*     */ import net.minecraft.world.item.component.MapItemColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MapItemSavedData
/*     */   extends SavedData {
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int MAP_SIZE = 128; private static final int HALF_MAP_SIZE = 64; public static final int MAX_SCALE = 4; public static final int TRACKED_DECORATION_LIMIT = 256; private static final String FRAME_PREFIX = "frame-"; public final int centerX; public final int centerZ; public final ResourceKey<Level> dimension; private final boolean trackingPosition;
/*     */   private final boolean unlimitedTracking;
/*     */   public final byte scale;
/*     */   public byte[] colors;
/*     */   public final boolean locked;
/*     */   private final List<HoldingPlayer> carriedBy;
/*     */   private final Map<Player, HoldingPlayer> carriedByPlayers;
/*     */   private final Map<String, MapBanner> bannerMarkers;
/*  55 */   public static final Codec<MapItemSavedData> CODEC = RecordCodecBuilder.create(i -> i.group(Level.RESOURCE_KEY_CODEC
/*  56 */         .fieldOf("dimension").forGetter(()), Codec.INT
/*  57 */         .fieldOf("xCenter").forGetter(()), Codec.INT
/*  58 */         .fieldOf("zCenter").forGetter(()), Codec.BYTE
/*  59 */         .optionalFieldOf("scale", Byte.valueOf((byte)0)).forGetter(()), Codec.BYTE_BUFFER
/*  60 */         .fieldOf("colors").forGetter(()), Codec.BOOL
/*  61 */         .optionalFieldOf("trackingPosition", Boolean.valueOf(true)).forGetter(()), Codec.BOOL
/*  62 */         .optionalFieldOf("unlimitedTracking", Boolean.valueOf(false)).forGetter(()), Codec.BOOL
/*  63 */         .optionalFieldOf("locked", Boolean.valueOf(false)).forGetter(()), MapBanner.CODEC
/*  64 */         .listOf().optionalFieldOf("banners", List.of()).forGetter(()), MapFrame.CODEC
/*  65 */         .listOf().optionalFieldOf("frames", List.of()).forGetter(()))
/*  66 */       .apply(i, MapItemSavedData::new)); private final Map<String, MapDecoration> decorations; private final Map<String, MapFrame> frameMarkers; private int trackedDecorationCount;
/*     */   public static final class MapPatch extends Record { private final int startX; private final int startY; private final int width; private final int height; private final byte[] mapColors;
/*  68 */     public MapPatch(int startX, int startY, int width, int height, byte[] mapColors) { this.startX = startX; this.startY = startY; this.width = width; this.height = height; this.mapColors = mapColors; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*  68 */       //   0	8	1	o	Ljava/lang/Object; } public int startX() { return this.startX; } public int startY() { return this.startY; } public int width() { return this.width; } public int height() { return this.height; } public byte[] mapColors() { return this.mapColors; }
/*  69 */     public static final StreamCodec<ByteBuf, Optional<MapPatch>> STREAM_CODEC = StreamCodec.of(MapPatch::write, MapPatch::read);
/*     */     
/*     */     private static void write(ByteBuf output, Optional<MapPatch> optional) {
/*  72 */       if (optional.isPresent()) {
/*  73 */         MapPatch patch = (MapPatch)optional.get();
/*  74 */         output.writeByte(patch.width);
/*  75 */         output.writeByte(patch.height);
/*  76 */         output.writeByte(patch.startX);
/*  77 */         output.writeByte(patch.startY);
/*  78 */         FriendlyByteBuf.writeByteArray(output, patch.mapColors);
/*     */       } else {
/*  80 */         output.writeByte(0);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Optional<MapPatch> read(ByteBuf input) {
/*  85 */       int width = input.readUnsignedByte();
/*  86 */       if (width > 0) {
/*  87 */         int height = input.readUnsignedByte();
/*  88 */         int startX = input.readUnsignedByte();
/*  89 */         int startY = input.readUnsignedByte();
/*  90 */         byte[] mapColors = FriendlyByteBuf.readByteArray(input);
/*  91 */         return Optional.of(new MapPatch(startX, startY, width, height, mapColors));
/*     */       } 
/*  93 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyToMap(MapItemSavedData map) {
/*  98 */       for (int x = 0; x < this.width; x++) {
/*  99 */         for (int y = 0; y < this.height; y++)
/* 100 */           map.setColor(this.startX + x, this.startY + y, this.mapColors[x + y * this.width]); 
/*     */       } 
/*     */     } }
/*     */   public class HoldingPlayer { public final Player player; private boolean dirtyData; private int minDirtyX; private int minDirtyY; private int maxDirtyX; private int maxDirtyY; private boolean dirtyDecorations;
/*     */     private int tick;
/*     */     public int step;
/*     */     
/*     */     private HoldingPlayer(Player player) {
/* 108 */       this.dirtyData = true;
/*     */ 
/*     */       
/* 111 */       this.maxDirtyX = 127;
/* 112 */       this.maxDirtyY = 127;
/* 113 */       this.dirtyDecorations = true;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       this.player = player;
/*     */     }
/*     */     
/*     */     private MapItemSavedData.MapPatch createPatch() {
/* 122 */       int startX = this.minDirtyX;
/* 123 */       int startY = this.minDirtyY;
/* 124 */       int width = this.maxDirtyX + 1 - this.minDirtyX;
/* 125 */       int height = this.maxDirtyY + 1 - this.minDirtyY;
/*     */       
/* 127 */       byte[] patch = new byte[width * height];
/* 128 */       for (int x = 0; x < width; x++) {
/* 129 */         for (int y = 0; y < height; y++) {
/* 130 */           patch[x + y * width] = MapItemSavedData.this.colors[startX + x + (startY + y) * 128];
/*     */         }
/*     */       } 
/* 133 */       return new MapItemSavedData.MapPatch(startX, startY, width, height, patch);
/*     */     }
/*     */     private Packet<?> nextUpdatePacket(MapId id) {
/*     */       Collection<MapDecoration> decorations;
/*     */       MapItemSavedData.MapPatch patch;
/* 138 */       if (this.dirtyData) {
/* 139 */         this.dirtyData = false;
/* 140 */         patch = createPatch();
/*     */       } else {
/* 142 */         patch = null;
/*     */       } 
/*     */ 
/*     */       
/* 146 */       if (this.dirtyDecorations && this.tick++ % 5 == 0) {
/* 147 */         this.dirtyDecorations = false;
/* 148 */         decorations = MapItemSavedData.this.decorations.values();
/*     */       } else {
/* 150 */         decorations = null;
/*     */       } 
/*     */       
/* 153 */       if (decorations != null || patch != null) {
/* 154 */         return new ClientboundMapItemDataPacket(id, MapItemSavedData.this.scale, MapItemSavedData.this.locked, decorations, patch);
/*     */       }
/*     */       
/* 157 */       return null;
/*     */     }
/*     */     
/*     */     private void markColorsDirty(int x, int y) {
/* 161 */       if (this.dirtyData) {
/* 162 */         this.minDirtyX = Math.min(this.minDirtyX, x);
/* 163 */         this.minDirtyY = Math.min(this.minDirtyY, y);
/* 164 */         this.maxDirtyX = Math.max(this.maxDirtyX, x);
/* 165 */         this.maxDirtyY = Math.max(this.maxDirtyY, y);
/*     */       } else {
/* 167 */         this.dirtyData = true;
/* 168 */         this.minDirtyX = x;
/* 169 */         this.minDirtyY = y;
/* 170 */         this.maxDirtyX = x;
/* 171 */         this.maxDirtyY = y;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 176 */     private void markDecorationsDirty() { this.dirtyDecorations = true; } }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SavedDataType<MapItemSavedData> type(MapId id)
/*     */   {
/* 196 */     return new SavedDataType(id
/* 197 */         .key(), () -> {
/* 198 */           throw new IllegalStateException("Should never create an empty map saved data");
/*     */         }CODEC, DataFixTypes.SAVED_DATA_MAP_DATA); } private MapItemSavedData(int centerX, int centerZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, boolean locked, ResourceKey<Level> dimension) { this.colors = new byte[16384];
/*     */     this.carriedBy = Lists.newArrayList();
/*     */     this.carriedByPlayers = Maps.newHashMap();
/*     */     this.bannerMarkers = Maps.newHashMap();
/*     */     this.decorations = Maps.newLinkedHashMap();
/*     */     this.frameMarkers = Maps.newHashMap();
/* 205 */     this.scale = scale;
/* 206 */     this.centerX = centerX;
/* 207 */     this.centerZ = centerZ;
/* 208 */     this.dimension = dimension;
/* 209 */     this.trackingPosition = trackingPosition;
/* 210 */     this.unlimitedTracking = unlimitedTracking;
/* 211 */     this.locked = locked; }
/*     */ 
/*     */   
/*     */   private MapItemSavedData(ResourceKey<Level> dimension, int centerX, int centerZ, byte scale, ByteBuffer colors, boolean trackingPosition, boolean unlimitedTracking, boolean locked, List<MapBanner> banners, List<MapFrame> frames) {
/* 215 */     this(centerX, centerZ, (byte)Mth.clamp(scale, 0, 4), trackingPosition, unlimitedTracking, locked, dimension);
/*     */     
/* 217 */     if (colors.array().length == 16384) {
/* 218 */       this.colors = colors.array();
/*     */     }
/*     */     
/* 221 */     for (MapBanner banner : banners) {
/* 222 */       this.bannerMarkers.put(banner.getId(), banner);
/* 223 */       addDecoration(banner.getDecoration(), null, banner.getId(), banner.pos().getX(), banner.pos().getZ(), 180.0D, (Component)banner.name().orElse(null));
/*     */     } 
/*     */     
/* 226 */     for (MapFrame frame : frames) {
/* 227 */       this.frameMarkers.put(frame.getId(), frame);
/* 228 */       addDecoration(MapDecorationTypes.FRAME, null, getFrameKey(frame.entityId()), frame.pos().getX(), frame.pos().getZ(), frame.rotation(), null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static MapItemSavedData createFresh(double originX, double originY, byte scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
/* 233 */     int size = '' * (1 << scale);
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
/* 244 */     int areaX = Mth.floor((originX + 64.0D) / size);
/* 245 */     int areaZ = Mth.floor((originY + 64.0D) / size);
/*     */     
/* 247 */     int x = areaX * size + size / 2 - 64;
/* 248 */     int z = areaZ * size + size / 2 - 64;
/*     */     
/* 250 */     return new MapItemSavedData(x, z, scale, trackingPosition, unlimitedTracking, false, dimension);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 255 */   public static MapItemSavedData createForClient(byte scale, boolean isLocked, ResourceKey<Level> dimension) { return new MapItemSavedData(0, 0, scale, false, false, isLocked, dimension); }
/*     */ 
/*     */   
/*     */   public MapItemSavedData locked() {
/* 259 */     MapItemSavedData result = new MapItemSavedData(this.centerX, this.centerZ, this.scale, this.trackingPosition, this.unlimitedTracking, true, this.dimension);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     result.bannerMarkers.putAll(this.bannerMarkers);
/* 270 */     result.decorations.putAll(this.decorations);
/* 271 */     result.trackedDecorationCount = this.trackedDecorationCount;
/* 272 */     System.arraycopy(this.colors, 0, result.colors, 0, this.colors.length);
/*     */     
/* 274 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 278 */   public MapItemSavedData scaled() { return createFresh(this.centerX, this.centerZ, (byte)Mth.clamp(this.scale + 1, 0, 4), this.trackingPosition, this.unlimitedTracking, this.dimension); }
/*     */ 
/*     */   
/*     */   private static Predicate<ItemStack> mapMatcher(ItemStack mapStack) {
/* 282 */     MapId mapId = (MapId)mapStack.get(DataComponents.MAP_ID);
/* 283 */     return stack -> {
/* 284 */         if (stack == mapStack) {
/* 285 */           return true;
/*     */         }
/* 287 */         return (stack.is(mapStack.getItem()) && Objects.equals(mapId, stack.get(DataComponents.MAP_ID)));
/*     */       };
/*     */   }
/*     */   
/*     */   public void tickCarriedBy(Player tickingPlayer, ItemStack itemStack) {
/* 292 */     if (!this.carriedByPlayers.containsKey(tickingPlayer)) {
/* 293 */       HoldingPlayer holdingPlayer = new HoldingPlayer(tickingPlayer);
/* 294 */       this.carriedByPlayers.put(tickingPlayer, holdingPlayer);
/* 295 */       this.carriedBy.add(holdingPlayer);
/*     */     } 
/*     */     
/* 298 */     Predicate<ItemStack> mapMatcher = mapMatcher(itemStack);
/* 299 */     if (!tickingPlayer.getInventory().contains(mapMatcher)) {
/* 300 */       removeDecoration(tickingPlayer.getPlainTextName());
/*     */     }
/*     */     
/* 303 */     for (int i = 0; i < this.carriedBy.size(); i++) {
/* 304 */       HoldingPlayer otherHoldingPlayer = (HoldingPlayer)this.carriedBy.get(i);
/* 305 */       Player otherPlayer = otherHoldingPlayer.player;
/* 306 */       String otherPlayerName = otherPlayer.getPlainTextName();
/*     */       
/* 308 */       if (otherPlayer.isRemoved() || (!otherPlayer.getInventory().contains(mapMatcher) && !itemStack.isFramed())) {
/* 309 */         this.carriedByPlayers.remove(otherPlayer);
/* 310 */         this.carriedBy.remove(otherHoldingPlayer);
/* 311 */         removeDecoration(otherPlayerName);
/* 312 */       } else if (!itemStack.isFramed() && otherPlayer.level().dimension() == this.dimension && this.trackingPosition) {
/* 313 */         addDecoration(MapDecorationTypes.PLAYER, otherPlayer.level(), otherPlayerName, otherPlayer.getX(), otherPlayer.getZ(), otherPlayer.getYRot(), null);
/*     */       } 
/*     */       
/* 316 */       if (!otherPlayer.equals(tickingPlayer) && hasMapInvisibilityItemEquipped(otherPlayer)) {
/* 317 */         removeDecoration(otherPlayerName);
/*     */       }
/*     */     } 
/*     */     
/* 321 */     if (itemStack.isFramed() && this.trackingPosition) {
/* 322 */       ItemFrame frame = itemStack.getFrame();
/* 323 */       BlockPos pos = frame.getPos();
/* 324 */       MapFrame existingFrame = (MapFrame)this.frameMarkers.get(MapFrame.frameId(pos));
/*     */ 
/*     */       
/* 327 */       if (existingFrame != null && frame.getId() != existingFrame.entityId() && this.frameMarkers.containsKey(existingFrame.getId())) {
/* 328 */         removeDecoration(getFrameKey(existingFrame.entityId()));
/*     */       }
/* 330 */       MapFrame mapFrame = new MapFrame(pos, frame.getDirection().get2DDataValue() * 90, frame.getId());
/* 331 */       addDecoration(MapDecorationTypes.FRAME, tickingPlayer.level(), getFrameKey(frame.getId()), pos.getX(), pos.getZ(), (frame.getDirection().get2DDataValue() * 90), null);
/* 332 */       MapFrame oldFrame = (MapFrame)this.frameMarkers.put(mapFrame.getId(), mapFrame);
/* 333 */       if (!mapFrame.equals(oldFrame)) {
/* 334 */         setDirty();
/*     */       }
/*     */     } 
/*     */     
/* 338 */     MapDecorations staticDecorations = (MapDecorations)itemStack.getOrDefault(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY);
/* 339 */     if (!this.decorations.keySet().containsAll(staticDecorations.decorations().keySet())) {
/* 340 */       staticDecorations.decorations().forEach((id, entry) -> {
/* 341 */             if (!this.decorations.containsKey(id)) {
/* 342 */               addDecoration(entry.type(), tickingPlayer.level(), id, entry.x(), entry.z(), entry.rotation(), null);
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean hasMapInvisibilityItemEquipped(Player player) {
/* 349 */     for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
/* 350 */       if (equipmentSlot != EquipmentSlot.MAINHAND && equipmentSlot != EquipmentSlot.OFFHAND)
/*     */       {
/*     */ 
/*     */         
/* 354 */         if (player.getItemBySlot(equipmentSlot).is(ItemTags.MAP_INVISIBILITY_EQUIPMENT)) {
/* 355 */           return true;
/*     */         }
/*     */       }
/*     */     } 
/* 359 */     return false;
/*     */   }
/*     */   
/*     */   private void removeDecoration(String string) {
/* 363 */     MapDecoration decoration = (MapDecoration)this.decorations.remove(string);
/* 364 */     if (decoration != null && ((MapDecorationType)decoration.type().value()).trackCount()) {
/* 365 */       this.trackedDecorationCount--;
/*     */     }
/* 367 */     setDecorationsDirty();
/*     */   }
/*     */   
/*     */   public static void addTargetDecoration(ItemStack itemStack, BlockPos position, String key, Holder<MapDecorationType> decorationType) {
/* 371 */     MapDecorations.Entry newDecoration = new MapDecorations.Entry(decorationType, position.getX(), position.getZ(), 180.0F);
/* 372 */     itemStack.update(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY, decorations -> decorations.withDecoration(key, newDecoration));
/*     */ 
/*     */     
/* 375 */     if (((MapDecorationType)decorationType.value()).hasMapColor()) {
/* 376 */       itemStack.set(DataComponents.MAP_COLOR, new MapItemColor(((MapDecorationType)decorationType.value()).mapColor()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDecoration(Holder<MapDecorationType> type, LevelAccessor level, String key, double xPos, double zPos, double yRot, Component name) {
/* 384 */     int scaling = 1 << this.scale;
/* 385 */     float xDeltaFromCenter = (float)(xPos - this.centerX) / scaling;
/* 386 */     float yDeltaFromCenter = (float)(zPos - this.centerZ) / scaling;
/*     */     
/* 388 */     MapDecorationLocation locationAndType = calculateDecorationLocationAndType(type, level, yRot, xDeltaFromCenter, yDeltaFromCenter);
/* 389 */     if (locationAndType == null) {
/* 390 */       removeDecoration(key);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 395 */     MapDecoration newDecoration = new MapDecoration(locationAndType.type(), locationAndType.x(), locationAndType.y(), locationAndType.rot(), Optional.ofNullable(name));
/* 396 */     MapDecoration previousDecoration = (MapDecoration)this.decorations.put(key, newDecoration);
/* 397 */     if (!newDecoration.equals(previousDecoration)) {
/* 398 */       if (previousDecoration != null && ((MapDecorationType)previousDecoration.type().value()).trackCount()) {
/* 399 */         this.trackedDecorationCount--;
/*     */       }
/* 401 */       if (((MapDecorationType)locationAndType.type().value()).trackCount()) {
/* 402 */         this.trackedDecorationCount++;
/*     */       }
/* 404 */       setDecorationsDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private MapDecorationLocation calculateDecorationLocationAndType(Holder<MapDecorationType> type, LevelAccessor level, double yRot, float xDeltaFromCenter, float yDeltaFromCenter) {
/* 409 */     byte clampedXDeltaFromCenter = clampMapCoordinate(xDeltaFromCenter);
/* 410 */     byte clampedYDeltaFromCenter = clampMapCoordinate(yDeltaFromCenter);
/*     */     
/* 412 */     if (type.is(MapDecorationTypes.PLAYER)) {
/* 413 */       Pair<Holder<MapDecorationType>, Byte> typeAndRotation = playerDecorationTypeAndRotation(type, level, yRot, xDeltaFromCenter, yDeltaFromCenter);
/* 414 */       return (typeAndRotation == null) ? null : new MapDecorationLocation((Holder)typeAndRotation.getFirst(), clampedXDeltaFromCenter, clampedYDeltaFromCenter, ((Byte)typeAndRotation.getSecond()).byteValue());
/*     */     } 
/*     */     
/* 417 */     if (isInsideMap(xDeltaFromCenter, yDeltaFromCenter) || this.unlimitedTracking) {
/* 418 */       return new MapDecorationLocation(type, clampedXDeltaFromCenter, clampedYDeltaFromCenter, calculateRotation(level, yRot));
/*     */     }
/*     */     
/* 421 */     return null;
/*     */   }
/*     */   
/*     */   private Pair<Holder<MapDecorationType>, Byte> playerDecorationTypeAndRotation(Holder<MapDecorationType> type, LevelAccessor level, double yRot, float xDeltaFromCenter, float yDeltaFromCenter) {
/* 425 */     if (isInsideMap(xDeltaFromCenter, yDeltaFromCenter)) {
/* 426 */       return Pair.of(type, Byte.valueOf(calculateRotation(level, yRot)));
/*     */     }
/* 428 */     Holder<MapDecorationType> outsideMapDecorationType = decorationTypeForPlayerOutsideMap(xDeltaFromCenter, yDeltaFromCenter);
/* 429 */     if (outsideMapDecorationType == null) {
/* 430 */       return null;
/*     */     }
/* 432 */     return Pair.of(outsideMapDecorationType, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   private byte calculateRotation(LevelAccessor level, double yRot) {
/* 436 */     if (this.dimension == Level.NETHER && level != null) {
/* 437 */       int s = (int)(level.getGameTime() / 10L);
/* 438 */       return (byte)(s * s * 34187121 + s * 121 >> 15 & 0xF);
/*     */     } 
/*     */     
/* 441 */     double adjustedYRot = (yRot < 0.0D) ? (yRot - 8.0D) : (yRot + 8.0D);
/* 442 */     return (byte)(int)(adjustedYRot * 16.0D / 360.0D);
/*     */   }
/*     */   
/*     */   private static boolean isInsideMap(float xd, float yd) {
/* 446 */     int halfSize = 63;
/* 447 */     return (xd >= -63.0F && yd >= -63.0F && xd <= 63.0F && yd <= 63.0F);
/*     */   }
/*     */   
/*     */   private Holder<MapDecorationType> decorationTypeForPlayerOutsideMap(float xDeltaFromCenter, float yDeltaFromCenter) {
/* 451 */     int rangeLimit = 320;
/* 452 */     boolean isWithinLimits = (Math.abs(xDeltaFromCenter) < 320.0F && Math.abs(yDeltaFromCenter) < 320.0F);
/* 453 */     if (isWithinLimits) {
/* 454 */       return MapDecorationTypes.PLAYER_OFF_MAP;
/*     */     }
/* 456 */     return this.unlimitedTracking ? 
/* 457 */       MapDecorationTypes.PLAYER_OFF_LIMITS : 
/* 458 */       null;
/*     */   }
/*     */   
/*     */   private static byte clampMapCoordinate(float deltaFromCenter) {
/* 462 */     int halfSize = 63;
/* 463 */     if (deltaFromCenter <= -63.0F) {
/* 464 */       return Byte.MIN_VALUE;
/*     */     }
/* 466 */     if (deltaFromCenter >= 63.0F) {
/* 467 */       return Byte.MAX_VALUE;
/*     */     }
/* 469 */     return (byte)(int)((deltaFromCenter * 2.0F) + 0.5D);
/*     */   }
/*     */   private static final class MapDecorationLocation extends Record { private final Holder<MapDecorationType> type; private final byte x; private final byte y; private final byte rot;
/* 472 */     private MapDecorationLocation(Holder<MapDecorationType> type, byte x, byte y, byte rot) { this.type = type; this.x = x; this.y = y; this.rot = rot; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/* 472 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<MapDecorationType> type() { return this.type; } public byte x() { return this.x; } public byte y() { return this.y; } public byte rot() { return this.rot; } }
/*     */   
/*     */   public Packet<?> getUpdatePacket(MapId id, Player player) {
/* 475 */     HoldingPlayer holdingPlayer = (HoldingPlayer)this.carriedByPlayers.get(player);
/*     */     
/* 477 */     if (holdingPlayer == null) {
/* 478 */       return null;
/*     */     }
/*     */     
/* 481 */     return holdingPlayer.nextUpdatePacket(id);
/*     */   }
/*     */   
/*     */   private void setColorsDirty(int x, int y) {
/* 485 */     setDirty();
/* 486 */     for (HoldingPlayer holdingPlayer : this.carriedBy) {
/* 487 */       holdingPlayer.markColorsDirty(x, y);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 493 */   private void setDecorationsDirty() { this.carriedBy.forEach(HoldingPlayer::markDecorationsDirty); }
/*     */ 
/*     */   
/*     */   public HoldingPlayer getHoldingPlayer(Player player) {
/* 497 */     HoldingPlayer holdingPlayer = (HoldingPlayer)this.carriedByPlayers.get(player);
/*     */     
/* 499 */     if (holdingPlayer == null) {
/* 500 */       holdingPlayer = new HoldingPlayer(player);
/* 501 */       this.carriedByPlayers.put(player, holdingPlayer);
/* 502 */       this.carriedBy.add(holdingPlayer);
/*     */     } 
/*     */     
/* 505 */     return holdingPlayer;
/*     */   }
/*     */   
/*     */   public boolean toggleBanner(LevelAccessor level, BlockPos pos) {
/* 509 */     double xPos = pos.getX() + 0.5D;
/* 510 */     double zPos = pos.getZ() + 0.5D;
/* 511 */     int scale = 1 << this.scale;
/* 512 */     double xd = (xPos - this.centerX) / scale;
/* 513 */     double yd = (zPos - this.centerZ) / scale;
/* 514 */     int halfSize = 63;
/* 515 */     if (xd >= -63.0D && yd >= -63.0D && xd <= 63.0D && yd <= 63.0D) {
/* 516 */       MapBanner banner = MapBanner.fromWorld(level, pos);
/* 517 */       if (banner == null) {
/* 518 */         return false;
/*     */       }
/*     */       
/* 521 */       if (this.bannerMarkers.remove(banner.getId(), banner)) {
/* 522 */         removeDecoration(banner.getId());
/* 523 */         setDirty();
/* 524 */         return true;
/* 525 */       }  if (!isTrackedCountOverLimit(256)) {
/* 526 */         this.bannerMarkers.put(banner.getId(), banner);
/* 527 */         addDecoration(banner.getDecoration(), level, banner.getId(), xPos, zPos, 180.0D, (Component)banner.name().orElse(null));
/* 528 */         setDirty();
/* 529 */         return true;
/*     */       } 
/*     */     } 
/* 532 */     return false;
/*     */   }
/*     */   
/*     */   public void checkBanners(BlockGetter level, int x, int z) {
/* 536 */     for (Iterator<MapBanner> iterator = this.bannerMarkers.values().iterator(); iterator.hasNext(); ) {
/* 537 */       MapBanner expected = (MapBanner)iterator.next();
/* 538 */       if (expected.pos().getX() == x && expected.pos().getZ() == z) {
/* 539 */         MapBanner current = MapBanner.fromWorld(level, expected.pos());
/* 540 */         if (!expected.equals(current)) {
/* 541 */           iterator.remove();
/* 542 */           removeDecoration(expected.getId());
/* 543 */           setDirty();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 550 */   public Collection<MapBanner> getBanners() { return this.bannerMarkers.values(); }
/*     */ 
/*     */   
/*     */   public void removedFromFrame(BlockPos pos, int entityID) {
/* 554 */     removeDecoration(getFrameKey(entityID));
/* 555 */     this.frameMarkers.remove(MapFrame.frameId(pos));
/* 556 */     setDirty();
/*     */   }
/*     */   
/*     */   public boolean updateColor(int x, int y, byte newColor) {
/* 560 */     byte oldColor = this.colors[x + y * 128];
/* 561 */     if (oldColor != newColor) {
/* 562 */       setColor(x, y, newColor);
/* 563 */       return true;
/*     */     } 
/* 565 */     return false;
/*     */   }
/*     */   
/*     */   public void setColor(int x, int y, byte newColor) {
/* 569 */     this.colors[x + y * 128] = newColor;
/* 570 */     setColorsDirty(x, y);
/*     */   }
/*     */   
/*     */   public boolean isExplorationMap() {
/* 574 */     for (MapDecoration decoration : this.decorations.values()) {
/* 575 */       if (((MapDecorationType)decoration.type().value()).explorationMapElement()) {
/* 576 */         return true;
/*     */       }
/*     */     } 
/* 579 */     return false;
/*     */   }
/*     */   
/*     */   public void addClientSideDecorations(List<MapDecoration> decorations) {
/* 583 */     this.decorations.clear();
/* 584 */     this.trackedDecorationCount = 0;
/* 585 */     for (int i = 0; i < decorations.size(); i++) {
/* 586 */       MapDecoration decoration = (MapDecoration)decorations.get(i);
/* 587 */       this.decorations.put("icon-" + i, decoration);
/* 588 */       if (((MapDecorationType)decoration.type().value()).trackCount()) {
/* 589 */         this.trackedDecorationCount++;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 595 */   public Iterable<MapDecoration> getDecorations() { return this.decorations.values(); }
/*     */ 
/*     */ 
/*     */   
/* 599 */   public boolean isTrackedCountOverLimit(int limit) { return (this.trackedDecorationCount >= limit); }
/*     */ 
/*     */ 
/*     */   
/* 603 */   private static String getFrameKey(int id) { return "frame-" + id; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
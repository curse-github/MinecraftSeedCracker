/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SimpleBitStorage;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Heightmap
/*     */ {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  31 */   private static final Predicate<BlockState> NOT_AIR = input -> !input.isAir(); private final BitStorage data; private final Predicate<BlockState> isOpaque;
/*  32 */   private static final Predicate<BlockState> MATERIAL_MOTION_BLOCKING = BlockBehaviour.BlockStateBase::blocksMotion;
/*     */   private final ChunkAccess chunk;
/*     */   
/*  35 */   public enum Usage { WORLDGEN,
/*  36 */     LIVE_WORLD,
/*  37 */     CLIENT; }
/*     */ 
/*     */   
/*     */   public enum Types
/*     */     implements StringRepresentable {
/*  42 */     WORLD_SURFACE_WG(0, "WORLD_SURFACE_WG", Heightmap.Usage.WORLDGEN, Heightmap.NOT_AIR),
/*  43 */     WORLD_SURFACE(1, "WORLD_SURFACE", Heightmap.Usage.CLIENT, Heightmap.NOT_AIR),
/*  44 */     OCEAN_FLOOR_WG(2, "OCEAN_FLOOR_WG", Heightmap.Usage.WORLDGEN, Heightmap.MATERIAL_MOTION_BLOCKING),
/*  45 */     OCEAN_FLOOR(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING),
/*  46 */     MOTION_BLOCKING(4, "MOTION_BLOCKING", Heightmap.Usage.CLIENT, input -> (input.blocksMotion() || !input.getFluidState().isEmpty())),
/*  47 */     MOTION_BLOCKING_NO_LEAVES(5, "MOTION_BLOCKING_NO_LEAVES", Heightmap.Usage.CLIENT, input -> ((input.blocksMotion() || !input.getFluidState().isEmpty()) && !(input.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock))); public static final Codec<Types> CODEC; private static final IntFunction<Types> BY_ID;
/*     */     public static final StreamCodec<ByteBuf, Types> STREAM_CODEC;
/*     */     
/*     */     static  {
/*  51 */       CODEC = StringRepresentable.fromEnum(Types::values);
/*     */       
/*  53 */       BY_ID = ByIdMap.continuous(t -> t.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  54 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, t -> t.id);
/*     */     }
/*     */     private final int id;
/*     */     private final String serializationKey;
/*     */     private final Heightmap.Usage usage;
/*     */     private final Predicate<BlockState> isOpaque;
/*     */     
/*     */     Types(int id, String serializationKey, Heightmap.Usage usage, Predicate<BlockState> isOpaque) {
/*  62 */       this.id = id;
/*  63 */       this.serializationKey = serializationKey;
/*  64 */       this.usage = usage;
/*  65 */       this.isOpaque = isOpaque;
/*     */     }
/*     */ 
/*     */     
/*  69 */     public String getSerializationKey() { return this.serializationKey; }
/*     */ 
/*     */ 
/*     */     
/*  73 */     public boolean sendToClient() { return (this.usage == Heightmap.Usage.CLIENT); }
/*     */ 
/*     */ 
/*     */     
/*  77 */     public boolean keepAfterWorldgen() { return (this.usage != Heightmap.Usage.WORLDGEN); }
/*     */ 
/*     */ 
/*     */     
/*  81 */     public Predicate<BlockState> isOpaque() { return this.isOpaque; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     public String getSerializedName() { return this.serializationKey; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Heightmap(ChunkAccess chunk, Types heightmapType) {
/*  95 */     this.isOpaque = heightmapType.isOpaque();
/*  96 */     this.chunk = chunk;
/*  97 */     int heightBits = Mth.ceillog2(chunk.getHeight() + 1);
/*  98 */     this.data = new SimpleBitStorage(heightBits, 256);
/*     */   }
/*     */   
/*     */   public static void primeHeightmaps(ChunkAccess chunk, Set<Types> types) {
/* 102 */     if (types.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     int size = types.size();
/* 107 */     ObjectArrayList objectArrayList = new ObjectArrayList(size);
/* 108 */     ObjectListIterator<Heightmap> iterator = objectArrayList.iterator();
/*     */     
/* 110 */     int highestSectionPosition = chunk.getHighestSectionPosition() + 16;
/* 111 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 112 */     for (int x = 0; x < 16; x++) {
/* 113 */       for (int z = 0; z < 16; z++) {
/* 114 */         for (Types type : types) {
/* 115 */           objectArrayList.add(chunk.getOrCreateHeightmapUnprimed(type));
/*     */         }
/*     */         
/* 118 */         for (int y = highestSectionPosition - 1; y >= chunk.getMinY(); y--) {
/* 119 */           pos.set(x, y, z);
/* 120 */           BlockState state = chunk.getBlockState(pos);
/* 121 */           if (!state.is(Blocks.AIR)) {
/*     */ 
/*     */             
/* 124 */             while (iterator.hasNext()) {
/* 125 */               Heightmap heightmap = (Heightmap)iterator.next();
/* 126 */               if (heightmap.isOpaque.test(state)) {
/* 127 */                 heightmap.setHeight(x, z, y + 1);
/* 128 */                 iterator.remove();
/*     */               } 
/*     */             } 
/* 131 */             if (objectArrayList.isEmpty()) {
/*     */               break;
/*     */             }
/* 134 */             iterator.back(size);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public boolean update(int localX, int localY, int localZ, BlockState state) {
/* 141 */     int firstAvailable = getFirstAvailable(localX, localZ);
/* 142 */     if (localY <= firstAvailable - 2)
/*     */     {
/* 144 */       return false;
/*     */     }
/*     */     
/* 147 */     if (this.isOpaque.test(state)) {
/*     */       
/* 149 */       if (localY >= firstAvailable) {
/* 150 */         setHeight(localX, localZ, localY + 1);
/* 151 */         return true;
/*     */       }
/*     */     
/*     */     }
/* 155 */     else if (firstAvailable - 1 == localY) {
/* 156 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 157 */       for (int y = localY - 1; y >= this.chunk.getMinY(); y--) {
/* 158 */         pos.set(localX, y, localZ);
/* 159 */         if (this.isOpaque.test(this.chunk.getBlockState(pos))) {
/* 160 */           setHeight(localX, localZ, y + 1);
/* 161 */           return true;
/*     */         } 
/*     */       } 
/* 164 */       setHeight(localX, localZ, this.chunk.getMinY());
/* 165 */       return true;
/*     */     } 
/*     */     
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 172 */   public int getFirstAvailable(int x, int z) { return getFirstAvailable(getIndex(x, z)); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int getHighestTaken(int x, int z) { return getFirstAvailable(getIndex(x, z)) - 1; }
/*     */ 
/*     */ 
/*     */   
/* 180 */   private int getFirstAvailable(int index) { return this.data.get(index) + this.chunk.getMinY(); }
/*     */ 
/*     */ 
/*     */   
/* 184 */   private void setHeight(int x, int z, int height) { this.data.set(getIndex(x, z), height - this.chunk.getMinY()); }
/*     */ 
/*     */   
/*     */   public void setRawData(ChunkAccess chunk, Types type, long[] data) {
/* 188 */     long[] rawData = this.data.getRaw();
/* 189 */     if (rawData.length == data.length) {
/* 190 */       System.arraycopy(data, 0, rawData, 0, data.length);
/*     */       return;
/*     */     } 
/* 193 */     LOGGER.warn("Ignoring heightmap data for chunk {}, size does not match; expected: {}, got: {}", new Object[] { chunk.getPos(), Integer.valueOf(rawData.length), Integer.valueOf(data.length) });
/* 194 */     primeHeightmaps(chunk, EnumSet.of(type));
/*     */   }
/*     */ 
/*     */   
/* 198 */   public long[] getRawData() { return this.data.getRaw(); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   private static int getIndex(int x, int z) { return x + z * 16; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Heightmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
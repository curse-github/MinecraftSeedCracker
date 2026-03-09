/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public static enum Types
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final Codec<Types> CODEC;
/*    */   private static final IntFunction<Types> BY_ID;
/* 42 */   WORLD_SURFACE_WG(0, "WORLD_SURFACE_WG", Heightmap.Usage.WORLDGEN, Heightmap.NOT_AIR),
/* 43 */   WORLD_SURFACE(1, "WORLD_SURFACE", Heightmap.Usage.CLIENT, Heightmap.NOT_AIR),
/* 44 */   OCEAN_FLOOR_WG(2, "OCEAN_FLOOR_WG", Heightmap.Usage.WORLDGEN, Heightmap.MATERIAL_MOTION_BLOCKING),
/* 45 */   OCEAN_FLOOR(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING),
/* 46 */   MOTION_BLOCKING(4, "MOTION_BLOCKING", Heightmap.Usage.CLIENT, input -> (input.blocksMotion() || !input.getFluidState().isEmpty())),
/* 47 */   MOTION_BLOCKING_NO_LEAVES(5, "MOTION_BLOCKING_NO_LEAVES", Heightmap.Usage.CLIENT, input -> ((input.blocksMotion() || !input.getFluidState().isEmpty()) && !(input.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)));
/*    */   public static final StreamCodec<ByteBuf, Types> STREAM_CODEC;
/*    */   
/*    */   static  {
/* 51 */     CODEC = StringRepresentable.fromEnum(Types::values);
/*    */     
/* 53 */     BY_ID = ByIdMap.continuous(t -> t.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 54 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, t -> t.id);
/*    */   }
/*    */ 
/*    */   
/*    */   private final int id;
/*    */   private final String serializationKey;
/*    */   
/*    */   Types(int id, String serializationKey, Heightmap.Usage usage, Predicate<BlockState> isOpaque) {
/* 62 */     this.id = id;
/* 63 */     this.serializationKey = serializationKey;
/* 64 */     this.usage = usage;
/* 65 */     this.isOpaque = isOpaque;
/*    */   }
/*    */   private final Heightmap.Usage usage; private final Predicate<BlockState> isOpaque;
/*    */   
/* 69 */   public String getSerializationKey() { return this.serializationKey; }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public boolean sendToClient() { return (this.usage == Heightmap.Usage.CLIENT); }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public boolean keepAfterWorldgen() { return (this.usage != Heightmap.Usage.WORLDGEN); }
/*    */ 
/*    */ 
/*    */   
/* 81 */   public Predicate<BlockState> isOpaque() { return this.isOpaque; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   public String getSerializedName() { return this.serializationKey; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Heightmap$Types.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
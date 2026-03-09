/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public static enum Rotation
/*     */   implements StringRepresentable
/*     */ {
/*  20 */   NONE(0, "none", OctahedralGroup.IDENTITY),
/*  21 */   CLOCKWISE_90(1, "clockwise_90", OctahedralGroup.ROT_90_Y_NEG),
/*  22 */   CLOCKWISE_180(2, "180", OctahedralGroup.ROT_180_FACE_XZ),
/*  23 */   COUNTERCLOCKWISE_90(3, "counterclockwise_90", OctahedralGroup.ROT_90_Y_POS); public static final IntFunction<Rotation> BY_ID; public static final Codec<Rotation> CODEC; public static final StreamCodec<ByteBuf, Rotation> STREAM_CODEC;
/*     */   
/*     */   static  {
/*  26 */     BY_ID = ByIdMap.continuous(Rotation::getIndex, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*     */     
/*  28 */     CODEC = StringRepresentable.fromEnum(Rotation::values);
/*  29 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Rotation::getIndex);
/*     */ 
/*     */     
/*  32 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(Rotation::valueOf);
/*     */   }
/*     */   @Deprecated
/*     */   public static final Codec<Rotation> LEGACY_CODEC; private final int index; private final String id;
/*     */   private final OctahedralGroup rotation;
/*     */   
/*     */   Rotation(int index, String id, OctahedralGroup rotation) {
/*  39 */     this.index = index;
/*  40 */     this.id = id;
/*  41 */     this.rotation = rotation;
/*     */   }
/*     */   
/*     */   public Rotation getRotated(Rotation rot) {
/*  45 */     switch (rot.ordinal()) { case 2:
/*  46 */         switch (ordinal()) { default: throw new MatchException(null, null);
/*     */           case 0: 
/*     */           case 1: 
/*     */           case 2: 
/*     */           case 3: break; } 
/*     */       case 3:
/*  52 */         switch (ordinal()) { default: throw new MatchException(null, null);
/*     */           case 0: 
/*     */           case 1: 
/*     */           case 2: 
/*     */           case 3: break; } 
/*     */       case 1:
/*  58 */         switch (ordinal()) { default: throw new MatchException(null, null);
/*     */           case 0: 
/*     */           case 1: 
/*     */           case 2:
/*     */           
/*     */           case 3:
/*  64 */             break; }  }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public OctahedralGroup rotation() { return this.rotation; }
/*     */ 
/*     */   
/*     */   public Direction rotate(Direction direction) {
/*  73 */     if (direction.getAxis() == Direction.Axis.Y) {
/*  74 */       return direction;
/*     */     }
/*  76 */     switch (ordinal()) { case 2: case 3: case 1:  }  return 
/*     */ 
/*     */ 
/*     */       
/*  80 */       direction;
/*     */   }
/*     */ 
/*     */   
/*     */   public int rotate(int rotation, int steps) {
/*  85 */     switch (ordinal()) { case 2: case 3: case 1:  }  return 
/*     */ 
/*     */ 
/*     */       
/*  89 */       rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static Rotation getRandom(RandomSource random) { return (Rotation)Util.getRandom(values(), random); }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static List<Rotation> getShuffled(RandomSource random) { return Util.shuffledCopy(values(), random); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public String getSerializedName() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 107 */   private int getIndex() { return this.index; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\Rotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
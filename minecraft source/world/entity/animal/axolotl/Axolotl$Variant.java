/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Variant
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Variant DEFAULT;
/*     */   private static final IntFunction<Variant> BY_ID;
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/* 131 */   LUCY(0, "lucy", true),
/* 132 */   WILD(1, "wild", true),
/* 133 */   GOLD(2, "gold", true),
/* 134 */   CYAN(3, "cyan", true),
/* 135 */   BLUE(4, "blue", false); public static final Codec<Variant> CODEC;
/*     */   static  {
/* 137 */     DEFAULT = LUCY;
/*     */     
/* 139 */     BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/* 141 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */     
/* 143 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 145 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */   }
/*     */   @Deprecated
/*     */   public static final Codec<Variant> LEGACY_CODEC;
/*     */   private final int id;
/*     */   
/*     */   Variant(int id, String name, boolean common) {
/* 152 */     this.id = id;
/* 153 */     this.name = name;
/* 154 */     this.common = common;
/*     */   }
/*     */   private final String name; private final boolean common;
/*     */   
/* 158 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public static Variant getCommonSpawnVariant(RandomSource random) { return getSpawnVariant(random, true); }
/*     */ 
/*     */ 
/*     */   
/* 179 */   public static Variant getRareSpawnVariant(RandomSource random) { return getSpawnVariant(random, false); }
/*     */ 
/*     */   
/*     */   private static Variant getSpawnVariant(RandomSource random, boolean common) {
/* 183 */     Variant[] validVariants = (Variant[])Arrays.stream(values()).filter(v -> (v.common == common)).toArray(x$0 -> new Variant[x$0]);
/* 184 */     return (Variant)Util.getRandom(validVariants, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
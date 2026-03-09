/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.biome.Biome;
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
/*     */   public static final StringRepresentable.EnumCodec<Variant> CODEC;
/*     */   private static final IntFunction<Variant> BY_ID;
/* 161 */   RED(0, "red"),
/* 162 */   SNOW(1, "snow");
/*     */   static  {
/* 164 */     DEFAULT = RED;
/*     */     
/* 166 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 168 */     BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/* 170 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */   }
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   Variant(int id, String name) {
/* 176 */     this.id = id;
/* 177 */     this.name = name;
/*     */   }
/*     */   private final int id;
/*     */   private final String name;
/*     */   
/* 182 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 186 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public static Variant byBiome(Holder<Biome> biome) { return biome.is(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fox\Fox$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
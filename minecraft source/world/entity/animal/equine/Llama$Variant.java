/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*  88 */   CREAMY(0, "creamy"),
/*  89 */   WHITE(1, "white"),
/*  90 */   BROWN(2, "brown"),
/*  91 */   GRAY(3, "gray"); public static final Variant DEFAULT; private static final IntFunction<Variant> BY_ID; public static final Codec<Variant> CODEC;
/*     */   
/*     */   static  {
/*  94 */     DEFAULT = CREAMY;
/*     */     
/*  96 */     BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*  97 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */     
/*  99 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */     
/* 101 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */   }
/*     */   @Deprecated
/*     */   public static final Codec<Variant> LEGACY_CODEC; public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   Variant(int id, String name) {
/* 107 */     this.id = id;
/* 108 */     this.name = name;
/*     */   }
/*     */   private final int id; private final String name;
/*     */   
/* 112 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\Llama$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
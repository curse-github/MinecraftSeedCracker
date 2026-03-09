/*     */ package net.minecraft.world.entity.animal.rabbit;
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
/*     */ 
/*     */ public static enum Variant
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Variant DEFAULT;
/*     */   private static final IntFunction<Variant> BY_ID;
/*  91 */   BROWN(0, "brown"),
/*  92 */   WHITE(1, "white"),
/*  93 */   BLACK(2, "black"),
/*  94 */   WHITE_SPLOTCHED(3, "white_splotched"),
/*  95 */   GOLD(4, "gold"),
/*  96 */   SALT(5, "salt"),
/*  97 */   EVIL(99, "evil"); public static final Codec<Variant> CODEC;
/*     */   
/*     */   static  {
/* 100 */     DEFAULT = BROWN;
/*     */     
/* 102 */     BY_ID = ByIdMap.sparse(Variant::id, values(), DEFAULT);
/*     */     
/* 104 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 106 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::id);
/*     */     
/* 108 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */   }
/*     */   @Deprecated
/*     */   public static final Codec<Variant> LEGACY_CODEC;
/*     */   
/*     */   Variant(int id, String name) {
/* 114 */     this.id = id;
/* 115 */     this.name = name;
/*     */   }
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC; private final int id;
/*     */   private final String name;
/*     */   
/* 120 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public int id() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*     */ package net.minecraft.world.entity.animal.parrot;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 142 */   RED_BLUE(0, "red_blue"),
/* 143 */   BLUE(1, "blue"),
/* 144 */   GREEN(2, "green"),
/* 145 */   YELLOW_BLUE(3, "yellow_blue"),
/* 146 */   GRAY(4, "gray"); public static final Variant DEFAULT; private static final IntFunction<Variant> BY_ID; public static final Codec<Variant> CODEC;
/*     */   
/*     */   static  {
/* 149 */     DEFAULT = RED_BLUE;
/*     */     
/* 151 */     BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 152 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 154 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */     
/* 156 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */   }
/*     */   @Deprecated
/*     */   public static final Codec<Variant> LEGACY_CODEC; public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   Variant(int id, String name) {
/* 162 */     this.id = id;
/* 163 */     this.name = name;
/*     */   }
/*     */   private final int id; private final String name;
/*     */   
/* 167 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\parrot\Parrot$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
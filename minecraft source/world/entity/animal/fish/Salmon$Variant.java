/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 165 */   SMALL("small", 0, 0.5F),
/* 166 */   MEDIUM("medium", 1, 1.0F),
/* 167 */   LARGE("large", 2, 1.5F);
/*     */   static  {
/* 169 */     DEFAULT = MEDIUM;
/*     */     
/* 171 */     CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 173 */     BY_ID = ByIdMap.continuous(Variant::id, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */     
/* 175 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */   }
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   private final String name;
/*     */   
/*     */   Variant(String name, int id, float boundingBoxScale) {
/* 182 */     this.name = name;
/* 183 */     this.id = id;
/* 184 */     this.boundingBoxScale = boundingBoxScale;
/*     */   }
/*     */   private final int id;
/*     */   private final float boundingBoxScale;
/*     */   
/* 189 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 193 */   private int id() { return this.id; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\Salmon$Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
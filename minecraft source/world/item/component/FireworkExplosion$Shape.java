/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
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
/*     */ public static enum Shape
/*     */   implements StringRepresentable
/*     */ {
/*     */   private static final IntFunction<Shape> BY_ID;
/* 105 */   SMALL_BALL(0, "small_ball"),
/* 106 */   LARGE_BALL(1, "large_ball"),
/* 107 */   STAR(2, "star"),
/* 108 */   CREEPER(3, "creeper"),
/* 109 */   BURST(4, "burst"); public static final StreamCodec<ByteBuf, Shape> STREAM_CODEC;
/*     */   
/*     */   static  {
/* 112 */     BY_ID = ByIdMap.continuous(Shape::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 113 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Shape::getId);
/*     */     
/* 115 */     CODEC = StringRepresentable.fromValues(Shape::values);
/*     */   }
/*     */   
/*     */   public static final Codec<Shape> CODEC;
/*     */   
/*     */   Shape(int id, String name) {
/* 121 */     this.id = id;
/* 122 */     this.name = name;
/*     */   }
/*     */   private final int id; private final String name;
/*     */   
/* 126 */   public MutableComponent getName() { return Component.translatable("item.minecraft.firework_star.shape." + this.name); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static Shape byId(int id) { return (Shape)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\FireworkExplosion$Shape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
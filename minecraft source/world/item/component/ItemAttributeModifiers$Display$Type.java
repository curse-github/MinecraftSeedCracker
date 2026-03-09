/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
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
/*     */ public static enum Type
/*     */   implements StringRepresentable
/*     */ {
/*     */   private static final Codec<Type> CODEC;
/*     */   private static final IntFunction<Type> BY_ID;
/*     */   private static final StreamCodec<ByteBuf, Type> STREAM_CODEC;
/* 129 */   DEFAULT("default", 0, ItemAttributeModifiers.Display.Default.CODEC, ItemAttributeModifiers.Display.Default.STREAM_CODEC),
/* 130 */   HIDDEN("hidden", 1, ItemAttributeModifiers.Display.Hidden.CODEC, ItemAttributeModifiers.Display.Hidden.STREAM_CODEC),
/* 131 */   OVERRIDE("override", 2, ItemAttributeModifiers.Display.OverrideText.CODEC, ItemAttributeModifiers.Display.OverrideText.STREAM_CODEC);
/*     */   
/*     */   static  {
/* 134 */     CODEC = StringRepresentable.fromEnum(Type::values);
/*     */     
/* 136 */     BY_ID = ByIdMap.continuous(Type::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 137 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::id);
/*     */   }
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   private final int id;
/*     */   
/*     */   Type(String name, int id, MapCodec<? extends ItemAttributeModifiers.Display> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec) {
/* 145 */     this.name = name;
/* 146 */     this.id = id;
/* 147 */     this.codec = codec;
/* 148 */     this.streamCodec = streamCodec;
/*     */   }
/*     */   private final MapCodec<? extends ItemAttributeModifiers.Display> codec;
/*     */   private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec;
/*     */   
/* 153 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 157 */   private int id() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/* 161 */   private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers.Display> streamCodec() { return this.streamCodec; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ItemAttributeModifiers$Display$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
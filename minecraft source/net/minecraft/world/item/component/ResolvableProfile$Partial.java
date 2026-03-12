/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Partial
/*     */   extends Record
/*     */ {
/*     */   private final Optional<String> name;
/*     */   private final Optional<UUID> id;
/*     */   private final PropertyMap properties;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 118 */   protected Partial(Optional<String> name, Optional<UUID> id, PropertyMap properties) { this.name = name; this.id = id; this.properties = properties; } public Optional<String> name() { return this.name; } public Optional<UUID> id() { return this.id; } public PropertyMap properties() { return this.properties; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final Partial EMPTY = new Partial(Optional.empty(), Optional.empty(), PropertyMap.EMPTY);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   private static final MapCodec<Partial> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.PLAYER_NAME
/* 129 */         .optionalFieldOf("name").forGetter(Partial::name), UUIDUtil.CODEC
/* 130 */         .optionalFieldOf("id").forGetter(Partial::id), ExtraCodecs.PROPERTY_MAP
/* 131 */         .optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(Partial::properties))
/* 132 */       .apply(i, Partial::new));
/*     */   
/* 134 */   public static final StreamCodec<ByteBuf, Partial> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.PLAYER_NAME
/* 135 */       .apply(ByteBufCodecs::optional), Partial::name, UUIDUtil.STREAM_CODEC
/* 136 */       .apply(ByteBufCodecs::optional), Partial::id, ByteBufCodecs.GAME_PROFILE_PROPERTIES, Partial::properties, Partial::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   private GameProfile createProfile() { return ResolvableProfile.createPartialProfile(this.name, this.id, this.properties); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ResolvableProfile$Partial.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum PlayerModelType implements StringRepresentable {
/*    */   public static final Codec<PlayerModelType> CODEC;
/*    */   private static final Function<String, PlayerModelType> NAME_LOOKUP;
/* 14 */   SLIM("slim", "slim"),
/* 15 */   WIDE("wide", "default");
/*    */   static  {
/* 17 */     CODEC = StringRepresentable.fromEnum(PlayerModelType::values);
/* 18 */     NAME_LOOKUP = StringRepresentable.createNameLookup(values(), e -> e.legacyServicesId);
/* 19 */     STREAM_CODEC = ByteBufCodecs.BOOL.map(slim -> slim.booleanValue() ? SLIM : WIDE, type -> Boolean.valueOf((type == SLIM)));
/*    */   }
/*    */   
/*    */   public static final StreamCodec<ByteBuf, PlayerModelType> STREAM_CODEC;
/*    */   
/*    */   PlayerModelType(String id, String legacyServicesId) {
/* 25 */     this.id = id;
/* 26 */     this.legacyServicesId = legacyServicesId;
/*    */   }
/*    */   private final String id; private final String legacyServicesId;
/*    */   
/* 30 */   public static PlayerModelType byLegacyServicesName(String name) { return (PlayerModelType)Objects.requireNonNullElse((PlayerModelType)NAME_LOOKUP.apply(name), WIDE); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\PlayerModelType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
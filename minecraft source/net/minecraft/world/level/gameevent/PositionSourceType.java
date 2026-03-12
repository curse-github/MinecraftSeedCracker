/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface PositionSourceType<T extends PositionSource> {
/* 10 */   public static final PositionSourceType<BlockPositionSource> BLOCK = register("block", new BlockPositionSource.Type());
/* 11 */   public static final PositionSourceType<EntityPositionSource> ENTITY = register("entity", new EntityPositionSource.Type());
/*    */ 
/*    */   
/*    */   MapCodec<T> codec();
/*    */   
/*    */   StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*    */   
/* 18 */   static <S extends PositionSourceType<T>, T extends PositionSource> S register(String name, S serializer) { return (S)(PositionSourceType)Registry.register(BuiltInRegistries.POSITION_SOURCE_TYPE, name, serializer); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\PositionSourceType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
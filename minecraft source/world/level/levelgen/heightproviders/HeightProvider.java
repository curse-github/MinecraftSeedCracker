/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public abstract class HeightProvider {
/* 11 */   private static final Codec<Either<VerticalAnchor, HeightProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(VerticalAnchor.CODEC, BuiltInRegistries.HEIGHT_PROVIDER_TYPE
/*    */       
/* 13 */       .byNameCodec().dispatch(HeightProvider::getType, HeightProviderType::codec));
/*    */   
/* 15 */   public static final Codec<HeightProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> 
/* 16 */       (HeightProvider)either.map(ConstantHeight::of, ()), f -> 
/* 17 */       (f.getType() == HeightProviderType.CONSTANT) ? Either.left(((ConstantHeight)f).getValue()) : Either.right(f));
/*    */   
/*    */   public abstract HeightProviderType<?> getType();
/*    */   
/*    */   public abstract int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\HeightProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TypeBase<T extends DataComponentPredicate>
/*    */   extends Object
/*    */   implements DataComponentPredicate.Type<T>
/*    */ {
/*    */   private final Codec<T> codec;
/*    */   private final MapCodec<DataComponentPredicate.Single<T>> wrappedCodec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec;
/*    */   
/*    */   public TypeBase(Codec<T> codec) {
/* 79 */     this.codec = codec;
/* 80 */     this.wrappedCodec = DataComponentPredicate.Single.wrapCodec(this, codec);
/* 81 */     this.singleStreamCodec = ByteBufCodecs.fromCodecWithRegistries(codec).map(v -> new DataComponentPredicate.Single(this, v), DataComponentPredicate.Single::predicate);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public Codec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   public MapCodec<DataComponentPredicate.Single<T>> wrappedCodec() { return this.wrappedCodec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 96 */   public StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate.Single<T>> singleStreamCodec() { return this.singleStreamCodec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$TypeBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
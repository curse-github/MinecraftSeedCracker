/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
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
/*    */ interface ValueMatcher
/*    */ {
/* 50 */   public static final Codec<ValueMatcher> CODEC = Codec.either(StatePropertiesPredicate.ExactMatcher.CODEC, StatePropertiesPredicate.RangedMatcher.CODEC).xmap(Either::unwrap, matcher -> {
/*    */ 
/*    */         
/* 53 */         if (matcher instanceof StatePropertiesPredicate.ExactMatcher) { StatePropertiesPredicate.ExactMatcher exact = (StatePropertiesPredicate.ExactMatcher)matcher;
/* 54 */           return Either.left(exact); }
/* 55 */          if (matcher instanceof StatePropertiesPredicate.RangedMatcher) { StatePropertiesPredicate.RangedMatcher ranged = (StatePropertiesPredicate.RangedMatcher)matcher;
/* 56 */           return Either.right(ranged); }
/*    */         
/* 58 */         throw new UnsupportedOperationException();
/*    */       });
/*    */   
/* 61 */   public static final StreamCodec<ByteBuf, ValueMatcher> STREAM_CODEC = ByteBufCodecs.either(StatePropertiesPredicate.ExactMatcher.STREAM_CODEC, StatePropertiesPredicate.RangedMatcher.STREAM_CODEC).map(Either::unwrap, matcher -> {
/*    */ 
/*    */         
/* 64 */         if (matcher instanceof StatePropertiesPredicate.ExactMatcher) { StatePropertiesPredicate.ExactMatcher exact = (StatePropertiesPredicate.ExactMatcher)matcher;
/* 65 */           return Either.left(exact); }
/* 66 */          if (matcher instanceof StatePropertiesPredicate.RangedMatcher) { StatePropertiesPredicate.RangedMatcher ranged = (StatePropertiesPredicate.RangedMatcher)matcher;
/* 67 */           return Either.right(ranged); }
/*    */         
/* 69 */         throw new UnsupportedOperationException();
/*    */       });
/*    */   
/*    */   <T extends Comparable<T>> boolean match(StateHolder<?, ?> paramStateHolder, Property<T> paramProperty);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\StatePropertiesPredicate$ValueMatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
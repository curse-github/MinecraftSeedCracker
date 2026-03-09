/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RandomizedIntStateProvider extends BlockStateProvider {
/* 18 */   public static final MapCodec<RandomizedIntStateProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockStateProvider.CODEC
/* 19 */         .fieldOf("source").forGetter(()), Codec.STRING
/* 20 */         .fieldOf("property").forGetter(()), IntProvider.CODEC
/* 21 */         .fieldOf("values").forGetter(()))
/* 22 */       .apply(i, RandomizedIntStateProvider::new));
/*    */   
/*    */   private final BlockStateProvider source;
/*    */   private final String propertyName;
/*    */   private IntegerProperty property;
/*    */   private final IntProvider values;
/*    */   
/*    */   public RandomizedIntStateProvider(BlockStateProvider source, IntegerProperty property, IntProvider values) {
/* 30 */     this.source = source;
/* 31 */     this.property = property;
/* 32 */     this.propertyName = property.getName();
/* 33 */     this.values = values;
/*    */     
/* 35 */     Collection<Integer> possibleValues = property.getPossibleValues();
/* 36 */     for (int i = values.getMinValue(); i <= values.getMaxValue(); i++) {
/* 37 */       if (!possibleValues.contains(Integer.valueOf(i))) {
/* 38 */         throw new IllegalArgumentException("Property value out of range: " + property.getName() + ": " + i);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public RandomizedIntStateProvider(BlockStateProvider source, String propertyName, IntProvider values) {
/* 44 */     this.source = source;
/* 45 */     this.propertyName = propertyName;
/* 46 */     this.values = values;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   protected BlockStateProviderType<?> type() { return BlockStateProviderType.RANDOMIZED_INT_STATE_PROVIDER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, BlockPos pos) {
/* 56 */     BlockState unmodifiedState = this.source.getState(random, pos);
/* 57 */     if (this.property == null || !unmodifiedState.hasProperty(this.property)) {
/* 58 */       IntegerProperty property = findProperty(unmodifiedState, this.propertyName);
/* 59 */       if (property == null) {
/* 60 */         return unmodifiedState;
/*    */       }
/* 62 */       this.property = property;
/*    */     } 
/* 64 */     return (BlockState)unmodifiedState.setValue(this.property, Integer.valueOf(this.values.sample(random)));
/*    */   }
/*    */   
/*    */   private static IntegerProperty findProperty(BlockState source, String propertyName) {
/* 68 */     Collection<Property<?>> properties = source.getProperties();
/*    */ 
/*    */ 
/*    */     
/* 72 */     Optional<IntegerProperty> found = properties.stream().filter(p -> p.getName().equals(propertyName)).filter(p -> p instanceof IntegerProperty).map(p -> (IntegerProperty)p).findAny();
/* 73 */     return (IntegerProperty)found.orElse(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RandomizedIntStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
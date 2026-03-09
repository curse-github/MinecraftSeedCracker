/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class ConstantHeight extends HeightProvider {
/*  9 */   public static final ConstantHeight ZERO = new ConstantHeight(VerticalAnchor.absolute(0));
/*    */   
/* 11 */   public static final MapCodec<ConstantHeight> CODEC = VerticalAnchor.CODEC.fieldOf("value").xmap(ConstantHeight::new, ConstantHeight::getValue);
/*    */   
/*    */   private final VerticalAnchor value;
/*    */ 
/*    */   
/* 16 */   public static ConstantHeight of(VerticalAnchor value) { return new ConstantHeight(value); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private ConstantHeight(VerticalAnchor value) { this.value = value; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public VerticalAnchor getValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public int sample(RandomSource random, WorldGenerationContext context) { return this.value.resolveY(context); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public HeightProviderType<?> getType() { return HeightProviderType.CONSTANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public String toString() { return this.value.toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\ConstantHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
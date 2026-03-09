/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
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
/*    */ public static enum PositionSourceType
/*    */   implements StringRepresentable
/*    */ {
/* 37 */   ENTITY_POSITION("entity_position", (pos, center, bbSpan, random) -> pos),
/* 38 */   BOUNDING_BOX("in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5D) * bbSpan);
/*    */   
/*    */   static  {
/* 41 */     CODEC = StringRepresentable.fromEnum(PositionSourceType::values);
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<PositionSourceType> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final CoordinateSource source;
/*    */   
/*    */   PositionSourceType(String id, CoordinateSource source) {
/* 52 */     this.id = id;
/* 53 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/* 57 */   public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) { return this.source.getCoordinate(position, center, boundingBoxSpan, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public String getSerializedName() { return this.id; }
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface CoordinateSource {
/*    */     double getCoordinate(double param2Double1, double param2Double2, float param2Float, RandomSource param2RandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSourceType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
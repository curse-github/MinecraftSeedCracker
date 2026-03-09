/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.math.OctahedralGroup;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum Mirror implements StringRepresentable {
/*    */   public static final Codec<Mirror> CODEC;
/*    */   @Deprecated
/*    */   public static final Codec<Mirror> LEGACY_CODEC;
/* 12 */   NONE("none", OctahedralGroup.IDENTITY),
/* 13 */   LEFT_RIGHT("left_right", OctahedralGroup.INVERT_Z),
/* 14 */   FRONT_BACK("front_back", OctahedralGroup.INVERT_X);
/*    */   
/*    */   static  {
/* 17 */     CODEC = StringRepresentable.fromEnum(Mirror::values);
/*    */ 
/*    */     
/* 20 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(Mirror::valueOf);
/*    */   }
/*    */   private final String id;
/*    */   private final Component symbol;
/*    */   private final OctahedralGroup rotation;
/*    */   
/*    */   Mirror(String id, OctahedralGroup rotation) {
/* 27 */     this.id = id;
/* 28 */     this.symbol = Component.translatable("mirror." + id);
/* 29 */     this.rotation = rotation;
/*    */   }
/*    */   
/*    */   public int mirror(int rotation, int steps) {
/* 33 */     int halfSteps = steps / 2;
/* 34 */     int correctedRotation = (rotation > halfSteps) ? (rotation - steps) : rotation;
/* 35 */     switch (ordinal()) {
/*    */       case 2:
/* 37 */         return (steps - correctedRotation) % steps;
/*    */       case 1:
/* 39 */         return (halfSteps - correctedRotation + steps) % steps;
/*    */     } 
/* 41 */     return rotation;
/*    */   }
/*    */ 
/*    */   
/*    */   public Rotation getRotation(Direction value) {
/* 46 */     Direction.Axis axis = value.getAxis();
/* 47 */     return ((this == LEFT_RIGHT && axis == Direction.Axis.Z) || (this == FRONT_BACK && axis == Direction.Axis.X)) ? Rotation.CLOCKWISE_180 : Rotation.NONE;
/*    */   }
/*    */   
/*    */   public Direction mirror(Direction direction) {
/* 51 */     if (this == FRONT_BACK && direction.getAxis() == Direction.Axis.X) {
/* 52 */       return direction.getOpposite();
/*    */     }
/* 54 */     if (this == LEFT_RIGHT && direction.getAxis() == Direction.Axis.Z) {
/* 55 */       return direction.getOpposite();
/*    */     }
/* 57 */     return direction;
/*    */   }
/*    */ 
/*    */   
/* 61 */   public OctahedralGroup rotation() { return this.rotation; }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public Component symbol() { return this.symbol; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\Mirror.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
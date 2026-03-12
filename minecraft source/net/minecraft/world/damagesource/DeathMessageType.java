/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum DeathMessageType implements StringRepresentable {
/*  7 */   DEFAULT("default"),
/*  8 */   FALL_VARIANTS("fall_variants"),
/*  9 */   INTENTIONAL_GAME_DESIGN("intentional_game_design");
/*    */   
/*    */   static  {
/* 12 */     CODEC = StringRepresentable.fromEnum(DeathMessageType::values);
/*    */   }
/*    */   public static final Codec<DeathMessageType> CODEC;
/*    */   private final String id;
/*    */   
/* 17 */   DeathMessageType(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\DeathMessageType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
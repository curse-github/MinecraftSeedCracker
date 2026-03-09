/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum PlayerModelPart implements StringRepresentable {
/*  8 */   CAPE(0, "cape"),
/*  9 */   JACKET(1, "jacket"),
/* 10 */   LEFT_SLEEVE(2, "left_sleeve"),
/* 11 */   RIGHT_SLEEVE(3, "right_sleeve"),
/* 12 */   LEFT_PANTS_LEG(4, "left_pants_leg"),
/* 13 */   RIGHT_PANTS_LEG(5, "right_pants_leg"),
/* 14 */   HAT(6, "hat"); public static final Codec<PlayerModelPart> CODEC;
/*    */   
/*    */   static  {
/* 17 */     CODEC = StringRepresentable.fromEnum(PlayerModelPart::values);
/*    */   }
/*    */   private final int bit;
/*    */   private final int mask;
/*    */   private final String id;
/*    */   private final Component name;
/*    */   
/*    */   PlayerModelPart(int bit, String name) {
/* 25 */     this.bit = bit;
/* 26 */     this.mask = 1 << bit;
/* 27 */     this.id = name;
/* 28 */     this.name = Component.translatable("options.modelPart." + name);
/*    */   }
/*    */ 
/*    */   
/* 32 */   public int getMask() { return this.mask; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getBit() { return this.bit; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public String getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Component getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\PlayerModelPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.item.equipment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public static enum ArmorType implements StringRepresentable {
/*  8 */   HELMET(EquipmentSlot.HEAD, 11, "helmet"),
/*  9 */   CHESTPLATE(EquipmentSlot.CHEST, 16, "chestplate"),
/* 10 */   LEGGINGS(EquipmentSlot.LEGS, 15, "leggings"),
/* 11 */   BOOTS(EquipmentSlot.FEET, 13, "boots"),
/* 12 */   BODY(EquipmentSlot.BODY, 16, "body"); public static final Codec<ArmorType> CODEC;
/*    */   
/*    */   static  {
/* 15 */     CODEC = StringRepresentable.fromValues(ArmorType::values);
/*    */   }
/*    */   private final EquipmentSlot slot;
/*    */   private final String name;
/*    */   private final int unitDurability;
/*    */   
/*    */   ArmorType(EquipmentSlot slot, int unitDurability, String name) {
/* 22 */     this.slot = slot;
/* 23 */     this.name = name;
/* 24 */     this.unitDurability = unitDurability;
/*    */   }
/*    */ 
/*    */   
/* 28 */   public int getDurability(int multiplier) { return this.unitDurability * multiplier; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public EquipmentSlot getSlot() { return this.slot; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\ArmorType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
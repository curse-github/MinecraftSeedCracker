/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum BillboardConstraints
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Codec<BillboardConstraints> CODEC;
/*     */   public static final IntFunction<BillboardConstraints> BY_ID;
/* 103 */   FIXED((byte)0, "fixed"),
/* 104 */   VERTICAL((byte)1, "vertical"),
/* 105 */   HORIZONTAL((byte)2, "horizontal"),
/* 106 */   CENTER((byte)3, "center");
/*     */   
/*     */   static  {
/* 109 */     CODEC = StringRepresentable.fromEnum(BillboardConstraints::values);
/* 110 */     BY_ID = ByIdMap.continuous(BillboardConstraints::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   
/*     */   private final byte id;
/*     */   
/*     */   BillboardConstraints(byte id, String name) {
/* 116 */     this.name = name;
/* 117 */     this.id = id;
/*     */   }
/*     */   
/*     */   private final String name;
/*     */   
/* 122 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 126 */   private byte getId() { return this.id; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Display$BillboardConstraints.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
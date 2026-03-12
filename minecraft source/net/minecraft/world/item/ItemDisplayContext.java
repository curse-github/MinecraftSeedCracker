/*    */ package net.minecraft.world.item;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum ItemDisplayContext implements StringRepresentable {
/*    */   public static final Codec<ItemDisplayContext> CODEC;
/*    */   public static final IntFunction<ItemDisplayContext> BY_ID;
/* 10 */   NONE(0, "none"),
/* 11 */   THIRD_PERSON_LEFT_HAND(1, "thirdperson_lefthand"),
/* 12 */   THIRD_PERSON_RIGHT_HAND(2, "thirdperson_righthand"),
/* 13 */   FIRST_PERSON_LEFT_HAND(3, "firstperson_lefthand"),
/* 14 */   FIRST_PERSON_RIGHT_HAND(4, "firstperson_righthand"),
/* 15 */   HEAD(5, "head"),
/* 16 */   GUI(6, "gui"),
/* 17 */   GROUND(7, "ground"),
/* 18 */   FIXED(8, "fixed"),
/* 19 */   ON_SHELF(9, "on_shelf");
/*    */   
/*    */   static  {
/* 22 */     CODEC = StringRepresentable.fromEnum(ItemDisplayContext::values);
/* 23 */     BY_ID = ByIdMap.continuous(ItemDisplayContext::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/*    */   private final byte id;
/*    */   private final String name;
/*    */   
/*    */   ItemDisplayContext(int id, String name) {
/* 29 */     this.name = name;
/* 30 */     this.id = (byte)id;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public byte getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public boolean firstPerson() { return (this == FIRST_PERSON_LEFT_HAND || this == FIRST_PERSON_RIGHT_HAND); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean leftHand() { return (this == FIRST_PERSON_LEFT_HAND || this == THIRD_PERSON_LEFT_HAND); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemDisplayContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
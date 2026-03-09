/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public static enum Markings
/*    */ {
/*  8 */   NONE(0),
/*  9 */   WHITE(1),
/* 10 */   WHITE_FIELD(2),
/* 11 */   WHITE_DOTS(3),
/* 12 */   BLACK_DOTS(4); private static final IntFunction<Markings> BY_ID;
/*    */   
/*    */   static  {
/* 15 */     BY_ID = ByIdMap.continuous(Markings::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*    */   }
/*    */   private final int id;
/*    */   
/* 19 */   Markings(int id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static Markings byId(int id) { return (Markings)BY_ID.apply(id); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\Markings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
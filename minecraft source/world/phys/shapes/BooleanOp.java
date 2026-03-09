/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ public interface BooleanOp {
/*    */   public static final BooleanOp FALSE = (first, second) -> false;
/*  5 */   public static final BooleanOp NOT_OR = (first, second) -> (!first && !second);
/*  6 */   public static final BooleanOp ONLY_SECOND = (first, second) -> (second && !first);
/*  7 */   public static final BooleanOp NOT_FIRST = (first, second) -> !first;
/*  8 */   public static final BooleanOp ONLY_FIRST = (first, second) -> (first && !second);
/*  9 */   public static final BooleanOp NOT_SECOND = (first, second) -> !second;
/* 10 */   public static final BooleanOp NOT_SAME = (first, second) -> (first != second);
/* 11 */   public static final BooleanOp NOT_AND = (first, second) -> (!first || !second);
/* 12 */   public static final BooleanOp AND = (first, second) -> (first && second);
/* 13 */   public static final BooleanOp SAME = (first, second) -> (first == second);
/* 14 */   public static final BooleanOp SECOND = (first, second) -> second;
/* 15 */   public static final BooleanOp CAUSES = (first, second) -> (!first || second);
/* 16 */   public static final BooleanOp FIRST = (first, second) -> first;
/* 17 */   public static final BooleanOp CAUSED_BY = (first, second) -> (first || !second);
/* 18 */   public static final BooleanOp OR = (first, second) -> (first || second);
/*    */   public static final BooleanOp TRUE = (first, second) -> true;
/*    */   
/*    */   boolean apply(boolean paramBoolean1, boolean paramBoolean2);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\BooleanOp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
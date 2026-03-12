/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.time.Month;
/*    */ import java.time.MonthDay;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SpecialDates {
/*  9 */   public static final MonthDay HALLOWEEN = MonthDay.of(Month.OCTOBER, 31);
/*    */   
/* 11 */   public static final List<MonthDay> CHRISTMAS_RANGE = List.of(
/* 12 */       MonthDay.of(Month.DECEMBER, 24), 
/* 13 */       MonthDay.of(Month.DECEMBER, 25), 
/* 14 */       MonthDay.of(Month.DECEMBER, 26));
/*    */ 
/*    */   
/* 17 */   public static final MonthDay CHRISTMAS = MonthDay.of(Month.DECEMBER, 24);
/*    */   
/* 19 */   public static final MonthDay NEW_YEAR = MonthDay.of(Month.JANUARY, 1);
/*    */ 
/*    */   
/* 22 */   public static MonthDay dayNow() { return MonthDay.from(ZonedDateTime.now()); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static boolean isHalloween() { return HALLOWEEN.equals(dayNow()); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static boolean isExtendedChristmas() { return CHRISTMAS_RANGE.contains(dayNow()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SpecialDates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.stats;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface StatFormatter {
/*  9 */   public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*    */   static  {
/* 11 */     Objects.requireNonNull(NumberFormat.getIntegerInstance(Locale.US)); DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format;
/* 12 */     DIVIDE_BY_TEN = (value -> DECIMAL_FORMAT.format(value * 0.1D));
/* 13 */     DISTANCE = (cm -> {
/* 14 */         double meters = cm / 100.0D;
/* 15 */         double kilometers = meters / 1000.0D;
/*    */         
/* 17 */         if (kilometers > 0.5D)
/* 18 */           return DECIMAL_FORMAT.format(kilometers) + " km"; 
/* 19 */         if (meters > 0.5D) {
/* 20 */           return DECIMAL_FORMAT.format(meters) + " m";
/*    */         }
/* 22 */         return "" + cm + " cm";
/*    */       });
/* 24 */     TIME = (value -> {
/* 25 */         double seconds = value / 20.0D;
/* 26 */         double minutes = seconds / 60.0D;
/* 27 */         double hours = minutes / 60.0D;
/* 28 */         double days = hours / 24.0D;
/* 29 */         double years = days / 365.0D;
/*    */         
/* 31 */         if (years > 0.5D)
/* 32 */           return DECIMAL_FORMAT.format(years) + " y"; 
/* 33 */         if (days > 0.5D)
/* 34 */           return DECIMAL_FORMAT.format(days) + " d"; 
/* 35 */         if (hours > 0.5D)
/* 36 */           return DECIMAL_FORMAT.format(hours) + " h"; 
/* 37 */         if (minutes > 0.5D) {
/* 38 */           return DECIMAL_FORMAT.format(minutes) + " min";
/*    */         }
/* 40 */         return "" + seconds + " s";
/*    */       });
/*    */   }
/*    */   
/*    */   public static final StatFormatter DEFAULT;
/*    */   public static final StatFormatter DIVIDE_BY_TEN;
/*    */   public static final StatFormatter DISTANCE;
/*    */   public static final StatFormatter TIME;
/*    */   
/*    */   String format(int paramInt);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\StatFormatter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */